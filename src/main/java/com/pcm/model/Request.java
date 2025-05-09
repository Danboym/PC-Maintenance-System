package com.pcm.model;

public class Request {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String telephone;
    private String requestDate;
    private String unit;
    private String status;
    private String requestType;
    private int technicianId;
    
    public Request() {}
    
    public Request(int id, String firstName, String lastName, String email, String telephone,
                  String requestDate, String unit, String status, String requestType, int technicianId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.telephone = telephone;
        this.requestDate = requestDate;
        this.unit = unit;
        this.status = status;
        this.requestType = requestType;
        this.technicianId = technicianId;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTelephone() {
        return telephone;
    }
    
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    public String getRequestDate() {
        return requestDate;
    }
    
    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getRequestType() {
        return requestType;
    }
    
    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
    
    public int getTechnicianId() {
        return technicianId;
    }
    
    public void setTechnicianId(int technicianId) {
        this.technicianId = technicianId;
    }
} 