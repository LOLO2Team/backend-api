package com.parkinglot.api.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "parking_lot")
public class ParkingLot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private int capacity;
    private int reservedSpace;

    @Column(name = "parking_lot_name")
    private String parkingLotName;

    private Long employeeId;

    public ParkingLot() {
    }

    public ParkingLot(String parkingLotName, int capacity) {
        this.parkingLotName = parkingLotName;
        this.capacity = capacity;
    }

    public long getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getReservedSpace() {
        return reservedSpace;
    }

    public void setReservedSpace(int reservedSpace) {
        this.reservedSpace = reservedSpace;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getParkingLotName() {
        return parkingLotName;
    }

    public void setParkingLotName(String parkingLotName) {
        this.parkingLotName = parkingLotName;
    }
}
