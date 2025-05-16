package com.evginozan.cargoservice.repository;

import com.evginozan.cargoservice.model.CargoHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CargoHistoryRepository extends JpaRepository<CargoHistory, Long> {
    List<CargoHistory> findByCargoTrackingCodeOrderByTimestampDesc(String trackingCode);
}