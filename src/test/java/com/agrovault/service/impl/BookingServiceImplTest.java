package com.agrovault.service.impl;

import com.agrovault.dto.request.BookingRequest;
import com.agrovault.dto.response.BookingResponse;
import com.agrovault.entity.Booking;
import com.agrovault.entity.BookingStatus;
import com.agrovault.entity.Role;
import com.agrovault.entity.Storage;
import com.agrovault.entity.User;
import com.agrovault.event.BookingCreatedEvent;
import com.agrovault.repository.BookingRepository;
import com.agrovault.repository.StorageRepository;
import com.agrovault.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private StorageRepository storageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void createBookingReservesCapacityAndPublishesEvent() {
        UUID storageId = UUID.randomUUID();
        User farmer = User.builder()
                .email("farmer@agrovault.com")
                .role(Role.FARMER)
                .build();
        Storage storage = Storage.builder()
                .id(storageId)
                .name("Cold Vault")
                .availableCapacity(100.0)
                .build();
        BookingRequest request = BookingRequest.builder()
                .storageId(storageId)
                .produceType("Grapes")
                .quantity(25.0)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(7))
                .build();

        when(userRepository.findByEmail(farmer.getEmail())).thenReturn(Optional.of(farmer));
        when(storageRepository.findByIdWithLock(storageId)).thenReturn(Optional.of(storage));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking booking = invocation.getArgument(0);
            booking.setId(UUID.randomUUID());
            return booking;
        });

        BookingResponse response = bookingService.createBooking(request, farmer.getEmail());

        assertThat(storage.getAvailableCapacity()).isEqualTo(75.0);
        assertThat(response.getStatus()).isEqualTo(BookingStatus.PENDING.name());

        ArgumentCaptor<BookingCreatedEvent> eventCaptor = ArgumentCaptor.forClass(BookingCreatedEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        assertThat(eventCaptor.getValue().getBooking().getProduceType()).isEqualTo("Grapes");
    }

    @Test
    void cancellingBookingRestoresCapacity() {
        UUID storageId = UUID.randomUUID();
        Storage storage = Storage.builder()
                .id(storageId)
                .availableCapacity(70.0)
                .build();
        Booking booking = Booking.builder()
                .id(UUID.randomUUID())
                .storage(storage)
                .quantity(20.0)
                .status(BookingStatus.PENDING)
                .build();

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(storageRepository.findByIdWithLock(storageId)).thenReturn(Optional.of(storage));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookingResponse response = bookingService.updateBookingStatus(booking.getId(), BookingStatus.CANCELLED);

        assertThat(storage.getAvailableCapacity()).isEqualTo(90.0);
        assertThat(response.getStatus()).isEqualTo(BookingStatus.CANCELLED.name());
    }
}
