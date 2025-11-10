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

    public List<AppointmentDTO> getDoctorAppointments(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public boolean cancelAppointment(Long appointmentId, String reason) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
        if (appointmentOpt.isPresent()) {
            Appointment appointment = appointmentOpt.get();
            // Send cancellation email to patient
            String subject = "Appointment Canceled by Dr. " + appointment.getDoctor().getName();
            String body = "Dear " + appointment.getPatient().getName() + ",\n\nYour appointment with Dr. " + appointment.getDoctor().getName() +
                          " scheduled for " + appointment.getDate() + " at " + appointment.getTime() +
                          " has been canceled.\n\nReason: " + reason +
                          "\n\nWe apologize for any inconvenience caused. Please contact us to reschedule.\n\nBest regards,\nCityCare Hospital Team";
            try {
                emailService.sendAppointmentNotification(appointment.getPatient().getEmail(), appointment.getDoctor().getEmail(), subject, body);
            } catch (Exception e) {
                System.err.println("Failed to send cancellation email: " + e.getMessage());
            }
            // Update status to cancelled
            appointment.setStatus("cancelled");
            appointmentRepository.save(appointment);
            return true;
        }
        return false;
    }

    private AppointmentDTO convertToDTO(Appointment appointment) {
        return new AppointmentDTO(appointment.getId(), appointment.getPatient().getId(),
                                  appointment.getDoctor().getId(), appointment.getDoctor().getName(),
                                  appointment.getDate(), appointment.getTime(), appointment.getReason(),
                                  appointment.getPatient().getName(), appointment.getStatus());
    }
}
