package com.parkinglot.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.parkinglot.api.domain.Employee;

import java.util.*;

public class EmployeeResponse {
    private Long employeeId;
    private String name;
    private String username;
    private String email;
    private String phone;
    private String role;

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static EmployeeResponse create(Long employeeId, String name, String username, String phone, String email, String role) {
        Objects.requireNonNull(employeeId);
        Objects.requireNonNull(name);

        final EmployeeResponse response = new EmployeeResponse();
        response.setEmployeeId(employeeId);
        response.setName(name);
        response.setUsername(username);
        response.setPhone(phone);
        response.setEmail(email);
        response.setRole(role);

        return response;
    }

    public static EmployeeResponse create(Employee entity) {
        return create(entity.getId(), entity.getName(), entity.getUsername(), entity.getPhone(), entity.getEmail(), entity.getRole());
    }

    @JsonIgnore
    public boolean isValid() {
        return employeeId != null;
    }
}
