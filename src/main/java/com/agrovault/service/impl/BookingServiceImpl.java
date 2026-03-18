package com.agrovault.service.impl;

import com.agrovault.dto.request.BookingRequest;
import com.agrovault.dto.response.BookingResponse;
import com.agrovault.entity.Booking;
import com.agrovault.entity.BookingStatus;
import com.agrovault.entity.Storage;
import com.agrovault.entity.User;
import com.agrovault.event.BookingCreatedEvent;
import com.agrovault.exception.ForbiddenException;
import com.agrovault.exception.InsufficientCapacityException;
import com.agrovault.exception.ResourceNotFoundException;
import com.agrovault.repository.BookingRepository;
import com.agrovault.repository.StorageRepository;
import com.agrovault.repository.UserRepository;
import com.agrovault.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final StorageRepository storageRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    @CacheEvict(value = {"storagesByCity", "all-storages"}, allEntries = true)
    public BookingResponse createBooking(BookingRequest request, String farmerEmail) {
        User farmer = userRepository.findByEmail(farmerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + farmerEmail));

        Storage storage = storageRepository.findByIdWithLock(request.getStorageId())
                .orElseThrow(() -> new ResourceNotFoundException("Storage not found with id: " + request.getStorageId()));

        if (storage.getAvailableCapacity() < request.getQuantity()) {
            throw new InsufficientCapacityException(
                    "Insufficient capacity. Available: " + storage.getAvailableCapacity()
                            + ", Requested: " + request.getQuantity());
        }

        storage.setAvailableCapacity(storage.getAvailableCapacity() - request.getQuantity());
        storageRepository.save(storage);

        Booking booking = Booking.builder()
                .farmer(farmer)
                .storage(storage)
                .produceType(request.getProduceType())
                .quantity(request.getQuantity())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(BookingStatus.PENDING)
                .build();

        Booking saved = bookingRepository.save(booking);

        eventPublisher.publishEvent(new BookingCreatedEvent(this, saved));

        return mapToBookingResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getBooking(UUID id, String userEmail) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        User requester = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        boolean isFarmer = booking.getFarmer().getId().equals(requester.getId());
        boolean isAdmin = requester.getRole().name().equals("ADMIN");

        if (!isFarmer && !isAdmin) {
            throw new ForbiddenException("You do not have access to this booking");
        }

        return mapToBookingResponse(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getUserBookings(String farmerEmail) {
        User farmer = userRepository.findByEmail(farmerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + farmerEmail));

        return bookingRepository.findByFarmer(farmer).stream()
                .map(this::mapToBookingResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::mapToBookingResponse)
                .toList();
    }

    @Override
    @Transactional
    @CacheEvict(value = {"storagesByCity", "all-storages"}, allEntries = true)
    public BookingResponse updateBookingStatus(UUID id, BookingStatus status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        if (status == BookingStatus.CANCELLED && booking.getStatus() != BookingStatus.CANCELLED) {
            Storage storage = storageRepository.findByIdWithLock(booking.getStorage().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Storage not found"));
            storage.setAvailableCapacity(storage.getAvailableCapacity() + booking.getQuantity());
            storageRepository.save(storage);
        }

        booking.setStatus(status);
        Booking saved = bookingRepository.save(booking);
        return mapToBookingResponse(saved);
    }

    private BookingResponse mapToBookingResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .storageName(booking.getStorage().getName())
                .produceType(booking.getProduceType())
                .quantity(booking.getQuantity())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .status(booking.getStatus().name())
                .createdAt(booking.getCreatedAt())
                .build();
    }
}
