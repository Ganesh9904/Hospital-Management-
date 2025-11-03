package com.hospital.management.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.management.dto.LoginRequest;
import com.hospital.management.dto.LoginResponse;
import com.hospital.management.entity.Patient;
import com.hospital.management.service.PatientService;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private PatientService patientService;

    @PostMapping("/login")        // hadles patient login
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<Patient> patient = patientService.loginPatient(loginRequest.getEmail(), loginRequest.getPassword());
        if (patient.isPresent()) {
            Patient p = patient.get();
            return ResponseEntity.ok(new LoginResponse(p.getId(), p.getName(), p.getEmail()));
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}
