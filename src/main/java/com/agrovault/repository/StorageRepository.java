package com.agrovault.repository;

import com.agrovault.entity.City;
import com.agrovault.entity.Storage;
import com.agrovault.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StorageRepository extends JpaRepository<Storage, UUID> {

    List<Storage> findByOwner(User owner);

    List<Storage> findByCity(City city);

    List<Storage> findByCityAndAvailableCapacityGreaterThan(City city, double minCapacity);
}
