package com.hospital.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.management.dto.PharmacyDTO;
import com.hospital.management.service.PharmacyService;

@RestController
@RequestMapping("/api/pharmacy")
public class PharmacyController {
    @Autowired
    private PharmacyService pharmacyService;

    @GetMapping("/items")
    public ResponseEntity<List<PharmacyDTO>> getAllPharmacyItems() {
        List<PharmacyDTO> items = pharmacyService.getAllPharmacyItems();
        return ResponseEntity.ok(items);
    }
}
