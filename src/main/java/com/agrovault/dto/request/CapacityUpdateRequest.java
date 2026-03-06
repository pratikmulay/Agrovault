package com.agrovault.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CapacityUpdateRequest {

    @NotNull
    @PositiveOrZero
    private Double availableCapacity;
}
