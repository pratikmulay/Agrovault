package com.agrovault.repository;

import com.agrovault.entity.Storage;
import com.agrovault.entity.TemperatureLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TemperatureLogRepository extends JpaRepository<TemperatureLog, Long> {

    List<TemperatureLog> findByStorageAndRecordedAtAfter(Storage storage, LocalDateTime after);

    Optional<TemperatureLog> findTopByStorageOrderByRecordedAtDesc(Storage storage);
}
