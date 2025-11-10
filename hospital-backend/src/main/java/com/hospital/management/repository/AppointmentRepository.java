package com.hospital.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hospital.management.entity.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientId(Long patientId);
    @Query("SELECT a FROM Appointment a JOIN FETCH a.patient WHERE a.doctor.id = :doctorId")
    List<Appointment> findByDoctorId(@Param("doctorId") Long doctorId);
}
