package com.hospital.management.dto;
public class DoctorDTO {

    private Long id;
    private String name;
    private String specialization;
    private String availableDays;
    private String availableTime;
    public DoctorDTO() {}
    public DoctorDTO(Long id, String name, String specialization, String availableDays, String availableTime) {
        this.id = id;
        this.name = name;
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
}
