package com.hospital.management.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.management.dto.PharmacyDTO;
import com.hospital.management.entity.Pharmacy;
import com.hospital.management.repository.PharmacyRepository;

@Service
public class PharmacyService {

    @Autowired
    private PharmacyRepository pharmacyRepository;
    public List<PharmacyDTO> getAllPharmacyItems() {
        return pharmacyRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    private PharmacyDTO convertToDTO(Pharmacy pharmacy) {
        return new PharmacyDTO(pharmacy.getId(), pharmacy.getName(),
                               pharmacy.getDescription(), pharmacy.getPrice());
    }
}
