package com.agrovault.repository;

import com.agrovault.entity.City;
import com.agrovault.entity.Storage;
import com.agrovault.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StorageRepository extends JpaRepository<Storage, UUID> {

    List<Storage> findByOwner(User owner);

    List<Storage> findByCity(City city);

    List<Storage> findByCityAndAvailableCapacityGreaterThan(City city, double minCapacity);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Storage s WHERE s.id = :id")
    Optional<Storage> findByIdWithLock(UUID id);
}
