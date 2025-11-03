package com.hospital.management.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String specialization;
    private String availableDays; 
    private String availableTime;

    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointments;

    public Doctor() {}

    public Doctor(String name, String email, String specialization, String availableDays, String availableTime) {
        this.name = name;
        this.email = email;
        this.specialization = specialization;
        this.availableDays = availableDays;
        this.availableTime = availableTime;
    }
    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }

    public String getName() { 
        return name; 
    }
    public void setName(String name) { 
        this.name = name; 
    }

    public String getEmail() { 
        return email; }
    public void setEmail(String email) { 
        this.email = email; 
    }

    public String getSpecialization() { 
        return specialization; 
    }
    public void setSpecialization(String specialization) { 
        this.specialization = specialization; 
    }

    public String getAvailableDays() {
         return availableDays; 
        }
    public void setAvailableDays(String availableDays) {
         this.availableDays = availableDays; 
        }

    public String getAvailableTime() {
         return availableTime; 
        }
    public void setAvailableTime(String availableTime) {
         this.availableTime = availableTime; 
        }

    public List<Appointment> getAppointments() { 
        return appointments; 
    }
    public void setAppointments(List<Appointment> appointments) {
         this.appointments = appointments; 
    }
}
