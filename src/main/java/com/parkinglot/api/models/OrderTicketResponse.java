package com.parkinglot.api.models;

import com.parkinglot.api.domain.Order;

import java.util.*;

public class OrderTicketResponse {
    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public Long getOrderID() {
        return orderID;
    }

    private String vehicleNumber;

    private Long orderID;

    public static OrderTicketResponse create(String vehicleNumber, Long orderID, String orderStatus) {
        Objects.requireNonNull(vehicleNumber);
        Objects.requireNonNull(orderID);

        final OrderTicketResponse response = new OrderTicketResponse();
        response.setVehicleNumber(vehicleNumber);
        response.setOrderID(orderID);
        return response;
    }
    public static OrderTicketResponse create(Order entity) {
        return create(entity.getVehicleNumber(), entity.getId(), entity.getOrderStatus());
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public void setOrderID(Long orderID) {
        this.orderID = orderID;
    }
}
