package com.agrovault.repository;

import com.agrovault.entity.Booking;
import com.agrovault.entity.Storage;
import com.agrovault.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    List<Booking> findByFarmer(User farmer);

    List<Booking> findByStorage(Storage storage);
}
