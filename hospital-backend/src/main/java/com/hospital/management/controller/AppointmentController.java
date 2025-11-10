package com.hospital.management.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.management.dto.AppointmentDTO;
import com.hospital.management.service.AppointmentService;
import com.hospital.management.service.EmailService;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private EmailService emailService;

    @GetMapping
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        List<AppointmentDTO> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    @PostMapping("/book") // for booking new appointment
    public ResponseEntity<?> bookAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        Optional<AppointmentDTO> bookedAppointment = appointmentService.bookAppointment(appointmentDTO);
        if (bookedAppointment.isPresent()) {
            return ResponseEntity.ok(bookedAppointment.get());
        } else {
            return ResponseEntity.badRequest().body("Invalid patient or doctor ID");
        }
    }

    @GetMapping("/history/{patientId}") // history of the patient
    public ResponseEntity<List<AppointmentDTO>> getPatientAppointmentHistory(@PathVariable Long patientId) {
        List<AppointmentDTO> history = appointmentService.getPatientAppointmentHistory(patientId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentDTO>> getDoctorAppointments(@PathVariable Long doctorId) {
        List<AppointmentDTO> appointments = appointmentService.getDoctorAppointments(doctorId);
        return ResponseEntity.ok(appointments);
    }

    @PutMapping("/cancel/{appointmentId}")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long appointmentId, @RequestBody CancelRequest cancelRequest) {
        boolean success = appointmentService.cancelAppointment(appointmentId, cancelRequest.getReason());
        if (success) {
            return ResponseEntity.ok("Appointment cancelled successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to cancel appointment.");
        }
    }

    @PostMapping("/test-email")  // for testing email
    public ResponseEntity<?> testEmail() {
        try {
            emailService.sendAppointmentNotification("test@example.com", "test@example.com", "Test Email", "This is a test email.");
            return ResponseEntity.ok("Test email sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to send test email: " + e.getMessage());
        }
    }

    // DTO for cancel request
    public static class CancelRequest {
        private String reason;

        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }
}
