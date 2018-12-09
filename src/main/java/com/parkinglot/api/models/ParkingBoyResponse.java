package com.parkinglot.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.parkinglot.api.domain.ParkingBoy;

import java.util.Objects;

public class ParkingBoyResponse {
    private Long employeeId;
    private String name;

    public Long getEmployeeId() {
        return employeeId;
    }
    public String getName() {
        return name;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public void setName(String name){this.name = name;}

    public static ParkingBoyResponse create(Long employeeId, String name) {
        Objects.requireNonNull(employeeId);
        Objects.requireNonNull(name);

        final ParkingBoyResponse response = new ParkingBoyResponse();
        response.setEmployeeId(employeeId);
        response.setName(name);
        return response;
    }

    public static ParkingBoyResponse create(ParkingBoy entity) {
        return create(entity.getId(), entity.getName());
    }

    @JsonIgnore
    public boolean isValid() {
        return employeeId != null;
    }
}
