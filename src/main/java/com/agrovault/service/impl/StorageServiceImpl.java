package com.agrovault.service.impl;

import com.agrovault.dto.request.CapacityUpdateRequest;
import com.agrovault.dto.request.StorageRequest;
import com.agrovault.dto.response.StorageResponse;
import com.agrovault.dto.response.StoragesByCityResponse;
import com.agrovault.entity.City;
import com.agrovault.entity.Storage;
import com.agrovault.entity.User;
import com.agrovault.exception.ForbiddenException;
import com.agrovault.exception.ResourceNotFoundException;
import com.agrovault.repository.CityRepository;
import com.agrovault.repository.StorageRepository;
import com.agrovault.repository.UserRepository;
import com.agrovault.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final StorageRepository storageRepository;
    private final UserRepository userRepository;
    private final CityRepository cityRepository;

    @Override
    @Transactional
    @CacheEvict(value = {"storagesByCity", "all-storages"}, allEntries = true)
    public StorageResponse createStorage(StorageRequest request, String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + ownerEmail));

        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + request.getCityId()));

        Storage storage = Storage.builder()
                .name(request.getName())
                .owner(owner)
                .city(city)
                .latitude(resolveLatitude(request, city))
                .longitude(resolveLongitude(request, city))
                .totalCapacity(request.getTotalCapacity())
                .availableCapacity(request.getTotalCapacity())
                .temperatureMin(request.getTemperatureMin())
                .temperatureMax(request.getTemperatureMax())
                .build();

        Storage saved = storageRepository.save(storage);
        return mapToStorageResponse(saved);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"storagesByCity", "all-storages"}, allEntries = true)
    public StorageResponse updateStorage(UUID id, StorageRequest request, String ownerEmail) {
        Storage storage = storageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Storage not found with id: " + id));

        if (!storage.getOwner().getEmail().equals(ownerEmail)) {
            throw new ForbiddenException("You are not the owner of this storage");
        }

        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + request.getCityId()));

        storage.setName(request.getName());
        storage.setCity(city);
        storage.setLatitude(resolveLatitude(request, city));
        storage.setLongitude(resolveLongitude(request, city));
        storage.setTotalCapacity(request.getTotalCapacity());
        storage.setTemperatureMin(request.getTemperatureMin());
        storage.setTemperatureMax(request.getTemperatureMax());

        Storage saved = storageRepository.save(storage);
        return mapToStorageResponse(saved);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"storagesByCity", "all-storages"}, allEntries = true)
    public StorageResponse updateAvailableCapacity(UUID id, CapacityUpdateRequest request) {
        Storage storage = storageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Storage not found with id: " + id));

        storage.setAvailableCapacity(request.getAvailableCapacity());
        Storage saved = storageRepository.save(storage);
        return mapToStorageResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "all-storages")
    public List<StorageResponse> getAllStorages() {
        return storageRepository.findAll().stream()
                .map(this::mapToStorageResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StorageResponse> getOwnerStorages(String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + ownerEmail));

        return storageRepository.findByOwner(owner).stream()
                .map(this::mapToStorageResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "storagesByCity", key = "#cityName.toLowerCase()")
    public List<StoragesByCityResponse> getStoragesByCity(String cityName) {
        City city = cityRepository.findByNameIgnoreCase(cityName)
                .orElseThrow(() -> new ResourceNotFoundException("City not found: " + cityName));

        List<Storage> storages = storageRepository.findByCityAndAvailableCapacityGreaterThan(city, 0);

        return storages.stream()
                .map(this::mapToStoragesByCityResponse)
                .toList();
    }

    private StorageResponse mapToStorageResponse(Storage storage) {
        return StorageResponse.builder()
                .id(storage.getId())
                .name(storage.getName())
                .cityName(storage.getCity().getName())
                .latitude(storage.getLatitude())
                .longitude(storage.getLongitude())
                .totalCapacity(storage.getTotalCapacity())
                .availableCapacity(storage.getAvailableCapacity())
                .temperatureMin(storage.getTemperatureMin())
                .temperatureMax(storage.getTemperatureMax())
                .ownerName(storage.getOwner().getName())
                .build();
    }

    private StoragesByCityResponse mapToStoragesByCityResponse(Storage storage) {
        return StoragesByCityResponse.builder()
                .id(storage.getId())
                .name(storage.getName())
                .availableCapacity(storage.getAvailableCapacity())
                .totalCapacity(storage.getTotalCapacity())
                .temperatureMin(storage.getTemperatureMin())
                .temperatureMax(storage.getTemperatureMax())
                .cityName(storage.getCity().getName())
                .ownerName(storage.getOwner().getName())
                .build();
    }

    private Double resolveLatitude(StorageRequest request, City city) {
        return request.getLatitude() != null ? request.getLatitude() : city.getLatitude();
    }

    private Double resolveLongitude(StorageRequest request, City city) {
        return request.getLongitude() != null ? request.getLongitude() : city.getLongitude();
    }
}
