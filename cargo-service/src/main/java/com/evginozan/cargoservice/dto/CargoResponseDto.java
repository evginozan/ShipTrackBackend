package com.evginozan.cargoservice.dto;

import com.evginozan.cargoservice.model.CargoStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CargoResponseDto {
    private Long id;
    private String trackingCode;
    private String senderFirstName;
    private String senderLastName;
    private String senderPhone;
    private String receiverFirstName;
    private String receiverLastName;
    private String receiverPhone;
    private String destinationAddress;
    private BigDecimal weight;
    private String dimensions;
    private CargoStatus status;
    private String senderBranch;
    private String currentBranch;
    private String destinationBranch;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deliveredAt;
    private List<String> history;
}