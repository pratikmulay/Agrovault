package com.agrovault.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemperatureLogRequest {

    @NotNull
    private UUID storageId;

    @NotNull
    private Double temperature;

    private Double humidity;
}
