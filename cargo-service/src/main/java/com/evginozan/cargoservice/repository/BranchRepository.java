package com.evginozan.cargoservice.repository;

import com.evginozan.cargoservice.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    List<Branch> findByCity(String city);

    List<Branch> findByCityAndDistrict(String city, String district);

    List<Branch> findByIsTransferCenter(Boolean isTransferCenter);
}