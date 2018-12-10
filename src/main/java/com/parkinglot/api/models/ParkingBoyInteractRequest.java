package com.parkinglot.api.models;

public class ParkingBoyInteractRequest {
    private long parkingLotId;
    private long employeeId;

    public ParkingBoyInteractRequest(long parkingLotId, long employeeId) {
        this.parkingLotId = parkingLotId;
        this.employeeId = employeeId;
    }

    public long getParkingLotId() {
        return parkingLotId;
    }

    public long getEmployeeId() {
        return employeeId;
    }
}
