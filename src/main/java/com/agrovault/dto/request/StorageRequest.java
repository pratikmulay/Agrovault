package com.agrovault.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageRequest {

    @NotBlank
    private String name;

    @NotNull
    private Integer cityId;

    private Double latitude;

    private Double longitude;

    @NotNull
    @Positive
    private Double totalCapacity;

    private Double temperatureMin;

    private Double temperatureMax;
}
