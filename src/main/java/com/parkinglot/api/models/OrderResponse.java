package com.parkinglot.api.models;


import com.parkinglot.api.domain.Order;

import java.util.Objects;

public class OrderResponse {
    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public Long getOrderID() {
        return orderID;
    }

    private String vehicleNumber;

    private String orderStatus;

    private Long orderID;

    public static OrderResponse create(String vehicleNumber, Long orderID) {
        Objects.requireNonNull(vehicleNumber);
        Objects.requireNonNull(orderID);

        final OrderResponse response = new OrderResponse();
        response.setVehicleNumber(vehicleNumber);
        response.setOrderID(orderID);
        return response;
    }

    public static OrderResponse create(Order entity) {
        return create(entity.getVehicleNumber(), entity.getOrderID());
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public void setOrderID(Long orderID) {
        this.orderID = orderID;
    }
}
