package com.agrovault.dto.response;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageResponse implements Serializable {

    private UUID id;
    private String name;
    private String cityName;
    private Double latitude;
    private Double longitude;
    private Double totalCapacity;
    private Double availableCapacity;
    private Double temperatureMin;
    private Double temperatureMax;
    private String ownerName;
}
