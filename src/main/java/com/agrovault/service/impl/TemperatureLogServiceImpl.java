package com.agrovault.service.impl;

import com.agrovault.entity.Storage;
import com.agrovault.entity.TemperatureLog;
import com.agrovault.exception.ResourceNotFoundException;
import com.agrovault.repository.StorageRepository;
import com.agrovault.repository.TemperatureLogRepository;
import com.agrovault.service.TemperatureLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TemperatureLogServiceImpl implements TemperatureLogService {

    private final TemperatureLogRepository temperatureLogRepository;
    private final StorageRepository storageRepository;

    @Override
    @Transactional
    public TemperatureLog logTemperature(UUID storageId, double temperature, double humidity) {
        Storage storage = storageRepository.findById(storageId)
                .orElseThrow(() -> new ResourceNotFoundException("Storage not found with id: " + storageId));

        TemperatureLog log = TemperatureLog.builder()
                .storage(storage)
                .temperature(temperature)
                .humidity(humidity)
                .build();

        return temperatureLogRepository.save(log);
    }
}
