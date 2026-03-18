package com.agrovault.service;

import com.agrovault.dto.request.BookingRequest;
import com.agrovault.dto.response.BookingResponse;
import com.agrovault.entity.BookingStatus;

import java.util.List;
import java.util.UUID;

public interface BookingService {

    BookingResponse createBooking(BookingRequest request, String farmerEmail);

    BookingResponse getBooking(UUID id, String userEmail);

    List<BookingResponse> getUserBookings(String farmerEmail);

    List<BookingResponse> getAllBookings();

    BookingResponse updateBookingStatus(UUID id, BookingStatus status);
}
