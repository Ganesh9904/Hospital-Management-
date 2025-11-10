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
import com.hospital.management.entity.Doctor;
import com.hospital.management.entity.Patient;
import com.hospital.management.service.DoctorService;
import com.hospital.management.service.EmailService;
import com.hospital.management.service.PatientService;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/login")        // handles patient login
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<Patient> patient = patientService.loginPatient(loginRequest.getEmail(), loginRequest.getPassword());
        if (patient.isPresent()) {
            Patient p = patient.get();
            return ResponseEntity.ok(new LoginResponse(p.getId(), p.getName(), p.getEmail()));
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @PostMapping("/doctor-login")
    public ResponseEntity<?> doctorLogin(@RequestBody LoginRequest loginRequest) {
        Optional<Doctor> doctor = doctorService.loginDoctor(loginRequest.getEmail(), loginRequest.getPassword());
        if (doctor.isPresent()) {
            Doctor d = doctor.get();
            return ResponseEntity.ok(new LoginResponse(d.getId(), d.getName(), d.getEmail()));
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @PostMapping("/doctor/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        String token = doctorService.generateResetToken(request.getEmail());
        if (token != null) {
            String resetLink = "http://localhost:8080/doctor/reset/reset-password.html?token=" + token;
            String subject = "Password Reset - CityCare Hospital";
            String body = "Dear Doctor,\n\nYou have requested to reset your password. Please click the link below to reset your password:\n\n" + resetLink + "\n\nIf you did not request this, please ignore this email.\n\nBest regards,\nCityCare Hospital Team";
            try {
                emailService.sendPasswordResetEmail(request.getEmail(), subject, body);
                return ResponseEntity.ok("Password reset email sent successfully.");
            } catch (Exception e) {
                return ResponseEntity.internalServerError().body("Failed to send email.");
            }
        } else {
            return ResponseEntity.badRequest().body("Email not found.");
        }
    }

    @PostMapping("/doctor/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        boolean success = doctorService.resetPassword(request.getToken(), request.getNewPassword());
        if (success) {
            return ResponseEntity.ok("Password reset successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired token.");
        }
    }

    @PostMapping("/patient/forgot-password")
    public ResponseEntity<?> patientForgotPassword(@RequestBody ForgotPasswordRequest request) {
        String token = patientService.generateResetToken(request.getEmail());
        if (token != null) {
            String resetLink = "http://localhost:8080/patient/reset/reset-password.html?token=" + token;
            String subject = "Password Reset - CityCare Hospital";
            String body = "Dear Patient,\n\nYou have requested to reset your password. Please click the link below to reset your password:\n\n" + resetLink + "\n\nIf you did not request this, please ignore this email.\n\nBest regards,\nCityCare Hospital Team";
            try {
                emailService.sendPasswordResetEmail(request.getEmail(), subject, body);
                return ResponseEntity.ok("Password reset email sent successfully.");
            } catch (Exception e) {
                return ResponseEntity.internalServerError().body("Failed to send email.");
            }
        } else {
            return ResponseEntity.badRequest().body("Email not found.");
        }
    }

    @PostMapping("/patient/reset-password")
    public ResponseEntity<?> patientResetPassword(@RequestBody ResetPasswordRequest request) {
        boolean success = patientService.resetPassword(request.getToken(), request.getNewPassword());
        if (success) {
            return ResponseEntity.ok("Password reset successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired token.");
        }
    }

    // DTO classes for forgot and reset password
    public static class ForgotPasswordRequest {
        private String email;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    public static class ResetPasswordRequest {
        private String token;
        private String newPassword;

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }
}
