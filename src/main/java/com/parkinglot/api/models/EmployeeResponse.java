package com.parkinglot.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.parkinglot.api.domain.Employee;
import com.parkinglot.api.user.Role;

import java.util.*;

public class EmployeeResponse {
    private Long employeeId;
    private String name;
    private String username;
    private String email;
    private String phone;
    private String status;
    @JsonIgnore
    private List<Role> roles;

    private List<String> rolesList;

    public List<String> getRolesList() {
        return rolesList;
    }

    public void setRolesList(List<String> rolesList) {
        this.rolesList = rolesList;
    }

    public List<Role> getRoles() {
        return roles;
    }
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

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

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public static EmployeeResponse create(Long employeeId, String name, String username, String phone, String email, String status, List<Role> roles) {
        Objects.requireNonNull(employeeId);
        Objects.requireNonNull(name);
        Objects.requireNonNull(roles);

        final EmployeeResponse response = new EmployeeResponse();
        response.setEmployeeId(employeeId);
        response.setName(name);
        response.setUsername(username);
        response.setPhone(phone);
        response.setEmail(email);
        response.setStatus(status);

        List<String> roleList = new ArrayList<>();
        for (Role role : roles) {
            roleList.add(role.getName().toString());
        }
        response.setRoles(roles);
        response.setRolesList(roleList);

        return response;
    }

    public static EmployeeResponse create(Employee entity) {
        return create(entity.getId(), entity.getName(), entity.getUsername(), entity.getPhone(), entity.getEmail(), entity.getStatus(),entity.getAuthorities());
    }

    @JsonIgnore
    public boolean isValid() {
        return employeeId != null;
    }
}
