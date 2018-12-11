package com.parkinglot.api.models;


import com.parkinglot.api.domain.Order;

import java.util.Objects;

public class OrderResponse {

    private String vehicleNumber;

    private String orderStatus;

    private Long orderId;

    private Long parkingLotId;

    private Long employeeId;

    public static OrderResponse create(String vehicleNumber, Long orderId, String orderStatus, Long parkingLotId, Long employeeId) {
        Objects.requireNonNull(vehicleNumber);
        Objects.requireNonNull(orderId);

        final OrderResponse response = new OrderResponse();
        response.setVehicleNumber(vehicleNumber);
        response.setOrderId(orderId);
        response.setOrderStatus(orderStatus);
        response.setParkingLotId(parkingLotId);
        response.setEmployeeId(employeeId);
        return response;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public static OrderResponse create(Order entity) {
        return create(entity.getVehicleNumber(), entity.getId(), entity.getOrderStatus(),entity.getParkingLotId(),entity.getEmployeeId());
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(Long parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}