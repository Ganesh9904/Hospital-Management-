package com.hospital.management.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.management.dto.DoctorDTO;
import com.hospital.management.entity.Doctor;
import com.hospital.management.repository.DoctorRepository;
@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;
    public List<DoctorDTO> getAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    private DoctorDTO convertToDTO(Doctor doctor) {
        return new DoctorDTO(doctor.getId(), doctor.getName(), doctor.getSpecialization(),
                             doctor.getAvailableDays(), doctor.getAvailableTime());
    }
}
