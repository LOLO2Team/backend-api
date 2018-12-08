package com.parkinglot.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.parkinglot.api.domain.ParkingLot;

import java.util.*;

public class ParkingLotResponse {
    private Long parkingLotId;
    private int capacity;
    private int reservedSpace;

    public void setParkingLotId(Long parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public static ParkingLotResponse create(Long parkingLotId, int capacity, int reservedSpace) {
        Objects.requireNonNull(parkingLotId);

        final ParkingLotResponse response = new ParkingLotResponse();
        response.setParkingLotId(parkingLotId);
        response.setCapacity(capacity);
        response.setReservedSpace(reservedSpace);
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
        return create(entity.getId(), entity.getCapacity(), entity.getReservedSpace());
    }

    @JsonIgnore
    public boolean isValid() {
        return parkingLotId != null;
    }
}
