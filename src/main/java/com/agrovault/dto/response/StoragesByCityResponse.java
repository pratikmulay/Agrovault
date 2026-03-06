package com.agrovault.dto.response;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoragesByCityResponse {

    private UUID id;
    private String name;
    private Double availableCapacity;
    private Double totalCapacity;
    private Double temperatureMin;
    private Double temperatureMax;
    private String cityName;
    private String ownerName;
}
