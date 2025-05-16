package com.evginozan.cargoservice.repository;

import com.evginozan.cargoservice.model.Cargo;
import com.evginozan.cargoservice.model.CargoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Long> {
    Optional<Cargo> findByTrackingCode(String trackingCode);

    List<Cargo> findBySenderPhoneOrderByCreatedAtDesc(String senderPhone);

    List<Cargo> findByReceiverPhoneOrderByCreatedAtDesc(String receiverPhone);

    List<Cargo> findByStatus(CargoStatus status);

    List<Cargo> findByCurrentBranchId(Long branchId);
}