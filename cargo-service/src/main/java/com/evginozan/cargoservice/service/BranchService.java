package com.evginozan.cargoservice.service;

import com.evginozan.cargoservice.dto.BranchDto;
import com.evginozan.cargoservice.exception.ResourceNotFoundException;
import com.evginozan.cargoservice.model.Branch;
import com.evginozan.cargoservice.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;


    public List<BranchDto> getAllBranches() {
        List<Branch> branches = branchRepository.findAll();

        return branches.stream()
                .map(this::convertToBranchDto)
                .collect(Collectors.toList());
    }


    public BranchDto getBranchById(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Şube bulunamadı"));

        return convertToBranchDto(branch);
    }


    public List<BranchDto> getBranchesByCity(String city) {
        List<Branch> branches = branchRepository.findByCity(city);

        return branches.stream()
                .map(this::convertToBranchDto)
                .collect(Collectors.toList());
    }


    public List<BranchDto> getBranchesByCityAndDistrict(String city, String district) {
        List<Branch> branches = branchRepository.findByCityAndDistrict(city, district);

        return branches.stream()
                .map(this::convertToBranchDto)
                .collect(Collectors.toList());
    }


    public List<BranchDto> getTransferCenters() {
        List<Branch> branches = branchRepository.findByIsTransferCenter(true);

        return branches.stream()
                .map(this::convertToBranchDto)
                .collect(Collectors.toList());
    }


    private BranchDto convertToBranchDto(Branch branch) {
        return BranchDto.builder()
                .id(branch.getId())
                .name(branch.getName())
                .city(branch.getCity())
                .district(branch.getDistrict())
                .address(branch.getAddress())
                .phone(branch.getPhone())
                .isTransferCenter(branch.getIsTransferCenter())
                .build();
    }
}