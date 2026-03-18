package com.agrovault.controller;

import com.agrovault.dto.response.ApiResponse;
import com.agrovault.dto.response.CityResponse;
import com.agrovault.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cities")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CityController {

    private final CityRepository cityRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CityResponse>>> getAllCities() {
        List<CityResponse> cities = cityRepository.findAll().stream()
                .map(city -> CityResponse.builder()
                        .id(city.getId())
                        .name(city.getName())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(cities));
    }
}
