package com.hospital.management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospital.management.entity.Patient;
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    public List<Patient> findByEmailAndPassword(String email, String password);

    public Optional<Patient> findByEmail(String email);
}
