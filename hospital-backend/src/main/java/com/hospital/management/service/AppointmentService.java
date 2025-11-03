package com.hospital.management.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.management.dto.AppointmentDTO;
import com.hospital.management.entity.Appointment;
import com.hospital.management.entity.Doctor;
import com.hospital.management.entity.Patient;
import com.hospital.management.repository.AppointmentRepository;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.PatientRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private EmailService emailService;

    public Optional<AppointmentDTO> bookAppointment(AppointmentDTO appointmentDTO) {
        Optional<Patient> patientOpt = patientRepository.findById(appointmentDTO.getPatientId());
        Optional<Doctor> doctorOpt = doctorRepository.findById(appointmentDTO.getDoctorId());

        if (patientOpt.isPresent() && doctorOpt.isPresent()) {
            Appointment appointment = new Appointment();
            appointment.setPatient(patientOpt.get());
            appointment.setDoctor(doctorOpt.get());
            appointment.setDate(appointmentDTO.getAppointmentDate());
            appointment.setTime(appointmentDTO.getAppointmentTime());
            appointment.setReason(appointmentDTO.getReason());

            Appointment saved = appointmentRepository.save(appointment);
            try {
                String subject = "Appointment Booked";
                String body = "Your appointment with Dr. " + doctorOpt.get().getName() + " on " +
                              appointmentDTO.getAppointmentDate() + " at " + appointmentDTO.getAppointmentTime() + " has been booked.";
                emailService.sendAppointmentNotification(patientOpt.get().getEmail(), doctorOpt.get().getEmail(), subject, body);
            } catch (Exception e) {
                System.err.println("Failed to send email notification: " + e.getMessage());
            }

            return Optional.of(convertToDTO(saved));
        }
        return Optional.empty();
    }
    public List<AppointmentDTO> getAllAppointments() {
        return appointmentRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    public List<AppointmentDTO> getPatientAppointmentHistory(Long patientId) {
        return appointmentRepository.findByPatientId(patientId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private AppointmentDTO convertToDTO(Appointment appointment) {
        return new AppointmentDTO(appointment.getId(), appointment.getPatient().getId(),
                                  appointment.getDoctor().getId(), appointment.getDoctor().getName(),
                                  appointment.getDate(), appointment.getTime(), appointment.getReason());
    }
}
