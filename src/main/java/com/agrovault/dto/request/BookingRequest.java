package com.agrovault.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {

    @NotNull
    private UUID storageId;

    private String produceType;

    @NotNull
    @Positive
    private Double quantity;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}
