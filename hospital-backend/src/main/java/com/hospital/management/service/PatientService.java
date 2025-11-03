package com.hospital.management.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.management.dto.PatientDTO;
import com.hospital.management.entity.Patient;
import com.hospital.management.repository.PatientRepository;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public Patient registerPatient(Patient patient) {
        if (patientRepository.findByEmail(patient.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        return patientRepository.save(patient);
    }
    public Optional<Patient> loginPatient(String email, String password) {
        List<Patient> patients = patientRepository.findByEmailAndPassword(email, password);
        if (!patients.isEmpty()) {
            return Optional.of(patients.get(0));
        } else {
            return Optional.empty();
        }
    }
    public Optional<PatientDTO> getPatientProfile(Long id) {
        return patientRepository.findById(id)
                .map(this::convertToDTO);
    }

    public Optional<PatientDTO> updatePatientProfile(Long id, Patient updatedPatient) {
        return patientRepository.findById(id)
                .map(patient -> {
                    patient.setName(updatedPatient.getName());
                    patient.setEmail(updatedPatient.getEmail());
                    patient.setContact(updatedPatient.getContact());
                    patient.setPassword(updatedPatient.getPassword());
                    Patient saved = patientRepository.save(patient);
                    return convertToDTO(saved);
                });
    }
    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    private PatientDTO convertToDTO(Patient patient) {
        return new PatientDTO(patient.getId(), patient.getName(), patient.getEmail(), patient.getContact());
    }
}
