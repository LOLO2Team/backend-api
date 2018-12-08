package com.parkinglot.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.parkinglot.api.domain.ParkingBoy;

import java.util.Objects;

public class ParkingBoyResponse {
    private Long employeeId;

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public static ParkingBoyResponse create(Long employeeId) {
        Objects.requireNonNull(employeeId);

        final ParkingBoyResponse response = new ParkingBoyResponse();
        response.setEmployeeId(employeeId);
        return response;
    }

    public static ParkingBoyResponse create(ParkingBoy entity) {
        return create(entity.getId());
    }

    @JsonIgnore
    public boolean isValid() {
        return employeeId != null;
    }
}
