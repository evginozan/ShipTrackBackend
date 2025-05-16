package com.evginozan.cargoservice.dto;

import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
public class CargoCreateDto {
    @NotBlank(message = "Gönderici adı boş olamaz")
    private String senderFirstName;

    @NotBlank(message = "Gönderici soyadı boş olamaz")
    private String senderLastName;

    @NotBlank(message = "Gönderici telefonu boş olamaz")
    private String senderPhone;

    @NotBlank(message = "Alıcı adı boş olamaz")
    private String receiverFirstName;

    @NotBlank(message = "Alıcı soyadı boş olamaz")
    private String receiverLastName;

    @NotBlank(message = "Alıcı T.C. kimlik numarası boş olamaz")
    private String receiverIdNumber;

    @NotBlank(message = "Alıcı telefonu boş olamaz")
    private String receiverPhone;

    @NotBlank(message = "Varış adresi boş olamaz")
    private String destinationAddress;

    @NotNull(message = "Ağırlık boş olamaz")
    private BigDecimal weight;

    @NotNull(message = "Genişlik boş olamaz")
    private Integer width;

    @NotNull(message = "Yükseklik boş olamaz")
    private Integer height;

    @NotNull(message = "Uzunluk boş olamaz")
    private Integer length;

    @NotNull(message = "Gönderici şube boş olamaz")
    private Long senderBranchId;

    @NotNull(message = "Varış şubesi boş olamaz")
    private Long destinationBranchId;
}