package com.agrovault.controller;

import com.agrovault.dto.request.TemperatureLogRequest;
import com.agrovault.dto.response.ApiResponse;
import com.agrovault.service.TemperatureLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/temperature-logs")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TemperatureLogController {

    private final TemperatureLogService temperatureLogService;

    @PostMapping
    @PreAuthorize("hasRole('STORAGE_OWNER')")
    public ResponseEntity<ApiResponse<String>> logTemperature(
            @Valid @RequestBody TemperatureLogRequest request) {
        temperatureLogService.logTemperature(
                request.getStorageId(),
                request.getTemperature(),
                request.getHumidity() != null ? request.getHumidity() : 0.0);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Temperature logged successfully", null));
    }
}
