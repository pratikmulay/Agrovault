package com.agrovault.service;

import com.agrovault.entity.TemperatureLog;

import java.util.UUID;

public interface TemperatureLogService {

    TemperatureLog logTemperature(UUID storageId, double temperature, double humidity);
}
