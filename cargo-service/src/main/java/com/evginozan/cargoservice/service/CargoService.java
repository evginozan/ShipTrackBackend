package com.evginozan.cargoservice.service;

import com.evginozan.cargoservice.dto.CargoCreateDto;
import com.evginozan.cargoservice.dto.CargoResponseDto;
import com.evginozan.cargoservice.dto.StatusUpdateDto;
import com.evginozan.cargoservice.exception.ResourceNotFoundException;
import com.evginozan.cargoservice.model.Branch;
import com.evginozan.cargoservice.model.Cargo;
import com.evginozan.cargoservice.model.CargoHistory;
import com.evginozan.cargoservice.model.CargoStatus;
import com.evginozan.cargoservice.repository.BranchRepository;
import com.evginozan.cargoservice.repository.CargoHistoryRepository;
import com.evginozan.cargoservice.repository.CargoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CargoService {

    private final CargoRepository cargoRepository;
    private final BranchRepository branchRepository;
    private final CargoHistoryRepository cargoHistoryRepository;


    @Transactional
    public CargoResponseDto createCargo(CargoCreateDto cargoDto) {
        Branch senderBranch = branchRepository.findById(cargoDto.getSenderBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Gönderici şube bulunamadı"));

        Branch destinationBranch = branchRepository.findById(cargoDto.getDestinationBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Varış şubesi bulunamadı"));

        Cargo cargo = Cargo.builder()
                .senderFirstName(cargoDto.getSenderFirstName())
                .senderLastName(cargoDto.getSenderLastName())
                .senderPhone(cargoDto.getSenderPhone())
                .receiverFirstName(cargoDto.getReceiverFirstName())
                .receiverLastName(cargoDto.getReceiverLastName())
                .receiverIdNumber(cargoDto.getReceiverIdNumber())
                .receiverPhone(cargoDto.getReceiverPhone())
                .destinationAddress(cargoDto.getDestinationAddress())
                .weight(cargoDto.getWeight())
                .width(cargoDto.getWidth())
                .height(cargoDto.getHeight())
                .length(cargoDto.getLength())
                .senderBranch(senderBranch)
                .currentBranch(senderBranch)
                .destinationBranch(destinationBranch)
                .build();

        Cargo savedCargo = cargoRepository.save(cargo);

        CargoHistory history = CargoHistory.builder()
                .cargo(savedCargo)
                .description("Kargo kabul edildi")
                .status(CargoStatus.AT_SENDER_BRANCH)
                .branch(senderBranch)
                .build();

        cargoHistoryRepository.save(history);

        return convertToCargoResponseDto(savedCargo);
    }


    @Transactional
    public CargoResponseDto updateCargoStatus(String trackingCode, StatusUpdateDto statusUpdateDto) {
        Cargo cargo = cargoRepository.findByTrackingCode(trackingCode)
                .orElseThrow(() -> new ResourceNotFoundException("Kargo bulunamadı"));

        CargoStatus newStatus = statusUpdateDto.getStatus();

        validateStatusUpdate(cargo.getStatus(), newStatus);

        if (statusUpdateDto.getBranchId() != null) {
            Branch branch = branchRepository.findById(statusUpdateDto.getBranchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Şube bulunamadı"));
            cargo.setCurrentBranch(branch);
        }

        if (newStatus == CargoStatus.OUT_FOR_DELIVERY &&
                cargo.getDeliveryCode() == null) {
            cargo.generateDeliveryCode();
        }

        if (newStatus == CargoStatus.DELIVERED) {
            cargo.setDeliveredAt(LocalDateTime.now());
        }

        cargo.setStatus(newStatus);

        CargoHistory history = CargoHistory.builder()
                .cargo(cargo)
                .description(statusUpdateDto.getDescription())
                .status(newStatus)
                .branch(cargo.getCurrentBranch())
                .build();

        cargoHistoryRepository.save(history);

        return convertToCargoResponseDto(cargoRepository.save(cargo));
    }


    private void validateStatusUpdate(CargoStatus currentStatus, CargoStatus newStatus) {
        if (currentStatus == CargoStatus.DELIVERED) {
            throw new IllegalStateException("Teslim edilmiş kargonun durumu değiştirilemez");
        }

        switch (currentStatus) {
            case AT_SENDER_BRANCH:
                if (newStatus != CargoStatus.IN_TRANSIT) {
                    throw new IllegalStateException("Gönderici şubeden sonra sadece taşıma durumuna geçilebilir");
                }
                break;
            case IN_TRANSIT:
                if (newStatus != CargoStatus.AT_TRANSFER_CENTER &&
                        newStatus != CargoStatus.AT_DELIVERY_BRANCH) {
                    throw new IllegalStateException("Taşımadan sonra sadece aktarma merkezi veya teslimat şubesi durumuna geçilebilir");
                }
                break;
            case AT_TRANSFER_CENTER:
                if (newStatus != CargoStatus.IN_TRANSIT) {
                    throw new IllegalStateException("Aktarma merkezinden sonra sadece taşıma durumuna geçilebilir");
                }
                break;
            case AT_DELIVERY_BRANCH:
                if (newStatus != CargoStatus.OUT_FOR_DELIVERY) {
                    throw new IllegalStateException("Teslimat şubesinden sonra sadece dağıtım durumuna geçilebilir");
                }
                break;
            case OUT_FOR_DELIVERY:
                if (newStatus != CargoStatus.DELIVERED &&
                        newStatus != CargoStatus.AT_DELIVERY_BRANCH) {
                    throw new IllegalStateException("Dağıtımdan sonra sadece teslim edildi veya teslimat şubesi durumuna geçilebilir");
                }
                break;
        }
    }


    public CargoResponseDto getCargoByTrackingCode(String trackingCode) {
        Cargo cargo = cargoRepository.findByTrackingCode(trackingCode)
                .orElseThrow(() -> new ResourceNotFoundException("Kargo bulunamadı"));

        return convertToCargoResponseDto(cargo);
    }


    @Transactional
    public CargoResponseDto verifyDeliveryCode(String trackingCode, String deliveryCode) {
        Cargo cargo = cargoRepository.findByTrackingCode(trackingCode)
                .orElseThrow(() -> new ResourceNotFoundException("Kargo bulunamadı"));

        if (cargo.getStatus() != CargoStatus.OUT_FOR_DELIVERY) {
            throw new IllegalStateException("Kargo dağıtımda değil, teslimat kodu kullanılamaz");
        }

        if (!cargo.getDeliveryCode().equals(deliveryCode)) {
            throw new IllegalStateException("Teslimat kodu geçersiz");
        }

        cargo.setStatus(CargoStatus.DELIVERED);
        cargo.setDeliveredAt(LocalDateTime.now());

        CargoHistory history = CargoHistory.builder()
                .cargo(cargo)
                .description("Kargo teslim edildi")
                .status(CargoStatus.DELIVERED)
                .branch(cargo.getCurrentBranch())
                .build();

        cargoHistoryRepository.save(history);

        return convertToCargoResponseDto(cargoRepository.save(cargo));
    }


    public List<CargoResponseDto> getCargosBySenderPhone(String senderPhone) {
        List<Cargo> cargos = cargoRepository.findBySenderPhoneOrderByCreatedAtDesc(senderPhone);

        return cargos.stream()
                .map(this::convertToCargoResponseDto)
                .collect(Collectors.toList());
    }


    public List<CargoResponseDto> getCargosByReceiverPhone(String receiverPhone) {
        List<Cargo> cargos = cargoRepository.findByReceiverPhoneOrderByCreatedAtDesc(receiverPhone);

        return cargos.stream()
                .map(this::convertToCargoResponseDto)
                .collect(Collectors.toList());
    }


    private CargoResponseDto convertToCargoResponseDto(Cargo cargo) {
        List<CargoHistory> history = cargoHistoryRepository
                .findByCargoTrackingCodeOrderByTimestampDesc(cargo.getTrackingCode());

        return CargoResponseDto.builder()
                .id(cargo.getId())
                .trackingCode(cargo.getTrackingCode())
                .senderFirstName(cargo.getSenderFirstName())
                .senderLastName(cargo.getSenderLastName())
                .senderPhone(cargo.getSenderPhone())
                .receiverFirstName(cargo.getReceiverFirstName())
                .receiverLastName(cargo.getReceiverLastName())
                .receiverPhone(cargo.getReceiverPhone())
                .destinationAddress(cargo.getDestinationAddress())
                .weight(cargo.getWeight())
                .dimensions(cargo.getWidth() + "x" + cargo.getHeight() + "x" + cargo.getLength() + " cm")
                .status(cargo.getStatus())
                .senderBranch(cargo.getSenderBranch().getName())
                .currentBranch(cargo.getCurrentBranch().getName())
                .destinationBranch(cargo.getDestinationBranch().getName())
                .createdAt(cargo.getCreatedAt())
                .updatedAt(cargo.getUpdatedAt())
                .deliveredAt(cargo.getDeliveredAt())
                .history(history.stream()
                        .map(h -> h.getTimestamp() + " - " + h.getStatus() + " - " + h.getDescription())
                        .collect(Collectors.toList()))
                .build();
    }


    public String getDeliveryCode(String trackingCode) {
        Cargo cargo = cargoRepository.findByTrackingCode(trackingCode)
                .orElseThrow(() -> new ResourceNotFoundException("Kargo bulunamadı"));

        if (cargo.getStatus() != CargoStatus.OUT_FOR_DELIVERY) {
            throw new IllegalStateException("Kargo dağıtımda değil, teslimat kodu görüntülenemez");
        }

        return cargo.getDeliveryCode();
    }
}