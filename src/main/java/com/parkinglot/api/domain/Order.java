package com.parkinglot.api.domain;

import javax.persistence.*;

@Entity
@Table(name = "parking_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "vehicle_number", length = 8, unique = true, nullable = false)
    private String vehicleNumber;

    @Column(name = "order_status", nullable = false)
    private String orderStatus;

    @Column(name = "parking_lot_id")
    private Long parkingLotId;

    @Column(name = "parker_employee_id")
    private Long parkerEmployeeId;

    @Column(name = "fetcher_employee_id")
    private Long fetcherEmployeeId;

    //Constructor
    protected Order() {
    }

    public Order(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
        //Cannot set default value in h2-db schema
        //Default value -> "pending"
        this.orderStatus = "pending";
    }
    //--------------------------------------------------------------------------

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getOrderID() {
        return id;
    }

    public void setOrderID(Long orderID) {
        this.id = orderID;
    }

    public Long getId() {
        return id;
    }

    public Long getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(Long parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public Long getParkerEmployeeId() {
        return parkerEmployeeId;
    }

    public void setParkerEmployeeId(Long parkerEmployeeId) {
        this.parkerEmployeeId = parkerEmployeeId;
    }

    public Long getFetcherEmployeeId() {
        return fetcherEmployeeId;
    }

    public void setFetcherEmployeeId(Long fetcherEmployeeId) {
        this.fetcherEmployeeId = fetcherEmployeeId;
    }

}
