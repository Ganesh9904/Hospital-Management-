package com.hospital.management.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    public Doctor registerDoctor(Doctor doctor) {
        if (doctorRepository.findByEmail(doctor.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        return doctorRepository.save(doctor);
    }

    public Optional<Doctor> loginDoctor(String email, String password) {
        List<Doctor> doctors = doctorRepository.findByEmailAndPassword(email, password);
        if (!doctors.isEmpty()) {
            return Optional.of(doctors.get(0));
        } else {
            return Optional.empty();
        }
    }

    public Optional<Doctor> findByEmail(String email) {
        return doctorRepository.findByEmail(email);
    }

    public String generateResetToken(String email) {
        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);
        if (doctorOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();
            String token = UUID.randomUUID().toString();
            doctor.setResetToken(token);
            doctorRepository.save(doctor);
            return token;
        }
        return null;
    }

    public boolean resetPassword(String token, String newPassword) {
        List<Doctor> doctors = doctorRepository.findByResetToken(token);
        if (!doctors.isEmpty()) {
            Doctor doctor = doctors.get(0);
            doctor.setPassword(newPassword);
            doctor.setResetToken(null);
            doctorRepository.save(doctor);
            return true;
        }
        return false;
    }

    private DoctorDTO convertToDTO(Doctor doctor) {
        return new DoctorDTO(doctor.getId(), doctor.getName(), doctor.getSpecialization(),
                             doctor.getAvailableDays(), doctor.getAvailableTime());
    }
}
