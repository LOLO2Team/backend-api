package com.parkinglot.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.parkinglot.api.domain.ParkingLot;

import java.util.*;

public class ParkingLotResponse {
    private Long parkingLotId;
    private String parkingLotName;
    private int capacity;
    private int reservedSpace;
    private Integer parkedCount;
    private Long employeeId;
    private String parkingLotStatus;

    public String getParkingLotName() {
        return parkingLotName;
    }

    public void setParkingLotName(String parkingLotName) {
        this.parkingLotName = parkingLotName;
    }

    public void setParkingLotId(Long parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getParkedCount() {
        return parkedCount;
    }

    public void setParkedCount(Integer parkedCount) {
        this.parkedCount = parkedCount;
    }

    public static ParkingLotResponse create(Long parkingLotId , String parkingLotName, int capacity, int reservedSpace, Long employeeId, String parkingLotStatus) {
        Objects.requireNonNull(parkingLotId);
        Objects.requireNonNull(parkingLotName);
        final ParkingLotResponse response = new ParkingLotResponse();
        response.setParkingLotId(parkingLotId);
        response.setParkingLotName(parkingLotName);
        response.setCapacity(capacity);
        response.setReservedSpace(reservedSpace);
        response.setEmployeeId(employeeId);
        response.setParkingLotStatus(parkingLotStatus);
        return response;
    }

    public int getReservedSpace() {
        return reservedSpace;
    }

    public void setReservedSpace(int reservedSpace) {
        this.reservedSpace = reservedSpace;
    }

    public Long getParkingLotId() {
        return parkingLotId;
    }

    public int getCapacity() {
        return capacity;
    }

    public static ParkingLotResponse create(ParkingLot entity) {
        return create(entity.getId(), entity.getParkingLotName(), entity.getCapacity(), entity.getReservedSpace(), entity.getEmployeeId(), entity.getParkingLotStatus());
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getParkingLotStatus() {
        return parkingLotStatus;
    }

    public void setParkingLotStatus(String parkingLotStatus) {
        this.parkingLotStatus = parkingLotStatus;
    }

    @JsonIgnore
    public boolean isValid() {
        return parkingLotId != null;
    }
}
