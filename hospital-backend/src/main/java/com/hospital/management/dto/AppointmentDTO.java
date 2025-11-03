package com.hospital.management.dto;

import java.time.LocalDate;
import java.time.LocalTime;


public class AppointmentDTO {

    private Long id;
    private Long patientId;
    private Long doctorId;
    private String doctorName;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String reason;

    public AppointmentDTO() {}

    public AppointmentDTO(Long id, Long patientId, Long doctorId, String doctorName, LocalDate appointmentDate, LocalTime appointmentTime, String reason) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.reason = reason;
    }
    public Long getId() {
         return id;
     }
    public void setId(Long id) { 
        this.id = id; 
    }

    public Long getPatientId() {
         return patientId; 
        }
    public void setPatientId(Long patientId) { 
        this.patientId = patientId; 
    }

    public Long getDoctorId() {
         return doctorId; 
    }
    public void setDoctorId(Long doctorId) {
         this.doctorId = doctorId; 
        }

    public LocalDate getAppointmentDate() {
         return appointmentDate; 
        }
    public void setAppointmentDate(LocalDate appointmentDate) { 
        this.appointmentDate = appointmentDate; 
    }

    public LocalTime getAppointmentTime() {
         return appointmentTime; 
        }
    public void setAppointmentTime(LocalTime appointmentTime) {
         this.appointmentTime = appointmentTime; 
        }

    public LocalDate getDate() {
         return appointmentDate; 
        }
    public void setDate(LocalDate date) {
         this.appointmentDate = date; 
        }

    public LocalTime getTime() {
         return appointmentTime; 
        }
    public void setTime(LocalTime time, LocalTime appointmentTime) { 
        this.appointmentTime = appointmentTime; 
    }

    public String getReason() { 
        return reason; 
    }
    public void setReason(String reason) {
         this.reason = reason;
         }

    public String getDoctorName() { 
        return doctorName; 
    }
    public void setDoctorName(String doctorName) { 
        this.doctorName = doctorName; 
    }
}
