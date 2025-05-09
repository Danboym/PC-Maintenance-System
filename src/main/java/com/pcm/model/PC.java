package com.pcm.model;

public class PC {
    private int id;
    private String brand;
    private String hdd;
    private String ram;
    private String os;
    private int registrationYear;
    private String status;
    private String location;
    private int technicianId;
    
    public PC() {}
    
    public PC(int id, String brand, String hdd, String ram, String os, int registrationYear,
             String status, String location, int technicianId) {
        this.id = id;
        this.brand = brand;
        this.hdd = hdd;
        this.ram = ram;
        this.os = os;
        this.registrationYear = registrationYear;
        this.status = status;
        this.location = location;
        this.technicianId = technicianId;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getBrand() {
        return brand;
    }
    
    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    public String getHdd() {
        return hdd;
    }
    
    public void setHdd(String hdd) {
        this.hdd = hdd;
    }
    
    public String getRam() {
        return ram;
    }
    
    public void setRam(String ram) {
        this.ram = ram;
    }
    
    public String getOs() {
        return os;
    }
    
    public void setOs(String os) {
        this.os = os;
    }
    
    public int getRegistrationYear() {
        return registrationYear;
    }
    
    public void setRegistrationYear(int registrationYear) {
        this.registrationYear = registrationYear;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public int getTechnicianId() {
        return technicianId;
    }
    
    public void setTechnicianId(int technicianId) {
        this.technicianId = technicianId;
    }

	public void setSpecifications(String parameter) {
		// TODO Auto-generated method stub
		
	}
} 