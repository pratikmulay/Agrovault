package com.agrovault.service;

import com.agrovault.dto.request.CapacityUpdateRequest;
import com.agrovault.dto.request.StorageRequest;
import com.agrovault.dto.response.StorageResponse;
import com.agrovault.dto.response.StoragesByCityResponse;

import java.util.List;
import java.util.UUID;

public interface StorageService {

    StorageResponse createStorage(StorageRequest request, String ownerEmail);

    StorageResponse updateStorage(UUID id, StorageRequest request, String ownerEmail);

    StorageResponse updateAvailableCapacity(UUID id, CapacityUpdateRequest request);

    List<StorageResponse> getAllStorages();

    List<StoragesByCityResponse> getStoragesByCity(String cityName);

    List<StorageResponse> getOwnerStorages(String ownerEmail);
}
