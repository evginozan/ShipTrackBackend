package com.evginozan.cargoservice.dto;

import com.evginozan.cargoservice.model.CargoStatus;
import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
@Builder
public class StatusUpdateDto {
    @NotNull(message = "Durum boş olamaz")
    private CargoStatus status;

    private String description;

    private Long branchId;
}