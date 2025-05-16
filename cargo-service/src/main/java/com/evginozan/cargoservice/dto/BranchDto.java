package com.evginozan.cargoservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BranchDto {
    private Long id;
    private String name;
    private String city;
    private String district;
    private String address;
    private String phone;
    private Boolean isTransferCenter;
}