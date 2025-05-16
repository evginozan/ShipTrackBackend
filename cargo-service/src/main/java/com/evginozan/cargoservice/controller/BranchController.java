package com.evginozan.cargoservice.controller;

import com.evginozan.cargoservice.dto.BranchDto;
import com.evginozan.cargoservice.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @GetMapping
    public ResponseEntity<List<BranchDto>> getAllBranches() {
        return ResponseEntity.ok(branchService.getAllBranches());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchDto> getBranchById(@PathVariable Long id) {
        return ResponseEntity.ok(branchService.getBranchById(id));
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<BranchDto>> getBranchesByCity(@PathVariable String city) {
        return ResponseEntity.ok(branchService.getBranchesByCity(city));
    }

    @GetMapping("/city/{city}/district/{district}")
    public ResponseEntity<List<BranchDto>> getBranchesByCityAndDistrict(
            @PathVariable String city,
            @PathVariable String district) {
        return ResponseEntity.ok(branchService.getBranchesByCityAndDistrict(city, district));
    }

    @GetMapping("/transfer-centers")
    public ResponseEntity<List<BranchDto>> getTransferCenters() {
        return ResponseEntity.ok(branchService.getTransferCenters());
    }
}