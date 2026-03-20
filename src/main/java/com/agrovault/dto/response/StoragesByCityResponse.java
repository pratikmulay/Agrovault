package com.agrovault.dto.response;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoragesByCityResponse implements Serializable {

    private UUID id;
    private String name;
    private Double availableCapacity;
    private Double totalCapacity;
    private Double temperatureMin;
    private Double temperatureMax;
    private String cityName;
    private String ownerName;
}
