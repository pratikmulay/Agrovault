package com.agrovault.controller;

import com.agrovault.dto.request.CapacityUpdateRequest;
import com.agrovault.dto.request.StorageRequest;
import com.agrovault.dto.response.ApiResponse;
import com.agrovault.dto.response.StorageResponse;
import com.agrovault.dto.response.StoragesByCityResponse;
import com.agrovault.service.StorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/storages")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    @PostMapping
    @PreAuthorize("hasRole('STORAGE_OWNER')")
    public ResponseEntity<ApiResponse<StorageResponse>> createStorage(
            @Valid @RequestBody StorageRequest request,
            Authentication authentication) {
        StorageResponse response = storageService.createStorage(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Storage created successfully", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STORAGE_OWNER')")
    public ResponseEntity<ApiResponse<StorageResponse>> updateStorage(
            @PathVariable UUID id,
            @Valid @RequestBody StorageRequest request,
            Authentication authentication) {
        StorageResponse response = storageService.updateStorage(id, request, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Storage updated successfully", response));
    }

    @PutMapping("/{id}/capacity")
    @PreAuthorize("hasRole('STORAGE_OWNER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<StorageResponse>> updateCapacity(
            @PathVariable UUID id,
            @Valid @RequestBody CapacityUpdateRequest request) {
        StorageResponse response = storageService.updateAvailableCapacity(id, request);
        return ResponseEntity.ok(ApiResponse.success("Capacity updated successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StorageResponse>>> getAllStorages() {
        List<StorageResponse> storages = storageService.getAllStorages();
        return ResponseEntity.ok(ApiResponse.success(storages));
    }

    @GetMapping("/city")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<ApiResponse<List<StoragesByCityResponse>>> getStoragesByCity(
            @RequestParam String city) {
        List<StoragesByCityResponse> storages = storageService.getStoragesByCity(city);
        return ResponseEntity.ok(ApiResponse.success(storages));
    }

    @GetMapping("/owner")
    @PreAuthorize("hasRole('STORAGE_OWNER')")
    public ResponseEntity<ApiResponse<List<StorageResponse>>> getOwnerStorages(Authentication authentication) {
        List<StorageResponse> storages = storageService.getOwnerStorages(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(storages));
    }
}
