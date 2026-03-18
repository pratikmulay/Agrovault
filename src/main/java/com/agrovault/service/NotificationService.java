package com.agrovault.service;

import com.agrovault.entity.Booking;
import com.agrovault.entity.Storage;
import com.agrovault.event.BookingCreatedEvent;
import com.agrovault.event.TemperatureAlertEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    @Async
    @EventListener
    public void handleBookingCreated(BookingCreatedEvent event) {
        Booking booking = event.getBooking();
        log.info("[NOTIFICATION] Booking confirmed for farmer {} at storage {}, quantity={}",
                booking.getFarmer().getName(),
                booking.getStorage().getName(),
                booking.getQuantity());
    }

    @Async
    @EventListener
    public void handleTemperatureAlert(TemperatureAlertEvent event) {
        Storage storage = event.getStorage();
        log.info("[ALERT] Storage {} temperature breach! Current={}, Allowed=[{}, {}]",
                storage.getName(),
                event.getCurrentTemperature(),
                storage.getTemperatureMin(),
                storage.getTemperatureMax());
    }
}
