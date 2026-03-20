package com.agrovault.service.impl;

import com.agrovault.dto.request.StorageRequest;
import com.agrovault.dto.response.StorageResponse;
import com.agrovault.entity.City;
import com.agrovault.entity.Role;
import com.agrovault.entity.Storage;
import com.agrovault.entity.User;
import com.agrovault.repository.CityRepository;
import com.agrovault.repository.StorageRepository;
import com.agrovault.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StorageServiceImplTest {

    @Mock
    private StorageRepository storageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private StorageServiceImpl storageService;

    @Test
    void createStorageFallsBackToCityCoordinatesWhenRequestDoesNotProvideThem() {
        User owner = User.builder()
                .email("owner@agrovault.com")
                .role(Role.STORAGE_OWNER)
                .name("Owner")
                .build();
        City city = City.builder()
                .id(1)
                .name("Pune")
                .latitude(18.5204)
                .longitude(73.8567)
                .build();

        StorageRequest request = StorageRequest.builder()
                .name("Pune Fresh Vault")
                .cityId(1)
                .totalCapacity(500.0)
                .temperatureMin(2.0)
                .temperatureMax(8.0)
                .build();

        when(userRepository.findByEmail(owner.getEmail())).thenReturn(Optional.of(owner));
        when(cityRepository.findById(city.getId())).thenReturn(Optional.of(city));
        when(storageRepository.save(any(Storage.class))).thenAnswer(invocation -> {
            Storage storage = invocation.getArgument(0);
            storage.setId(java.util.UUID.randomUUID());
            return storage;
        });

        StorageResponse response = storageService.createStorage(request, owner.getEmail());

        assertThat(response.getLatitude()).isEqualTo(city.getLatitude());
        assertThat(response.getLongitude()).isEqualTo(city.getLongitude());
        assertThat(response.getAvailableCapacity()).isEqualTo(request.getTotalCapacity());
    }
}
