package com.parkinglot.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.parkinglot.api.domain.Employee;

import java.util.*;

public class EmployeeDetailResponse {
    private Long employeeId;
    private String name;
    private String username;
    private String email;
    private String phone;
    private String status;
    private List<ParkingLotResponse> parkingLotResponses;

    public Long getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ParkingLotResponse> getParkingLotResponses() {
        return parkingLotResponses;
    }

    public void setParkingLotResponses(List<ParkingLotResponse> parkingLotResponses) {
        this.parkingLotResponses = parkingLotResponses;
    }

    public static EmployeeDetailResponse create(Long employeeId, String name, String username, String phone, String email, String status) {
        Objects.requireNonNull(employeeId);
        Objects.requireNonNull(name);

        final EmployeeDetailResponse response = new EmployeeDetailResponse();
        response.setEmployeeId(employeeId);
        response.setName(name);
        response.setUsername(username);
        response.setPhone(phone);
        response.setEmail(email);
        response.setStatus(status);

        return response;
    }

    public static EmployeeDetailResponse create(Employee entity) {
        return create(entity.getId(), entity.getName(), entity.getUsername(), entity.getPhone(), entity.getEmail(),
            entity.getStatus());
    }

    @JsonIgnore
    public boolean isValid() {
        return employeeId != null;
    }
}
