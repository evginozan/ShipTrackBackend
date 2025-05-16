package com.evginozan.cargoservice.controller;

import com.evginozan.cargoservice.dto.CargoCreateDto;
import com.evginozan.cargoservice.dto.CargoResponseDto;
import com.evginozan.cargoservice.dto.StatusUpdateDto;
import com.evginozan.cargoservice.service.CargoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cargo")
@RequiredArgsConstructor
public class CargoController {

    private final CargoService cargoService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<CargoResponseDto> createCargo(@Valid @RequestBody CargoCreateDto cargoDto) {
        return ResponseEntity.ok(cargoService.createCargo(cargoDto));
    }

    @GetMapping("/{trackingCode}")
    public ResponseEntity<CargoResponseDto> getCargoByTrackingCode(@PathVariable String trackingCode) {
        return ResponseEntity.ok(cargoService.getCargoByTrackingCode(trackingCode));
    }

    @PatchMapping("/{trackingCode}/status")
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<CargoResponseDto> updateCargoStatus(
            @PathVariable String trackingCode,
            @Valid @RequestBody StatusUpdateDto statusUpdateDto) {
        return ResponseEntity.ok(cargoService.updateCargoStatus(trackingCode, statusUpdateDto));
    }

    @GetMapping("/sender/{phone}")
    public ResponseEntity<List<CargoResponseDto>> getCargosBySenderPhone(@PathVariable String phone) {
        return ResponseEntity.ok(cargoService.getCargosBySenderPhone(phone));
    }

    @GetMapping("/receiver/{phone}")
    public ResponseEntity<List<CargoResponseDto>> getCargosByReceiverPhone(@PathVariable String phone) {
        return ResponseEntity.ok(cargoService.getCargosByReceiverPhone(phone));
    }

    @GetMapping("/{trackingCode}/delivery-code")
    public ResponseEntity<Map<String, String>> getDeliveryCode(@PathVariable String trackingCode) {
        String deliveryCode = cargoService.getDeliveryCode(trackingCode);
        return ResponseEntity.ok(Map.of("deliveryCode", deliveryCode));
    }

    @PostMapping("/{trackingCode}/verify")
    public ResponseEntity<CargoResponseDto> verifyDeliveryCode(
            @PathVariable String trackingCode,
            @RequestBody Map<String, String> request) {
        String deliveryCode = request.get("deliveryCode");
        return ResponseEntity.ok(cargoService.verifyDeliveryCode(trackingCode, deliveryCode));
    }
}