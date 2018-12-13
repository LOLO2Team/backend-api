package com.parkinglot.api.services;

import com.parkinglot.api.domain.OrderRepository;
import com.parkinglot.api.models.OrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<OrderResponse> getOrders(String status, String employeeId){
        return orderRepository.findAll().stream()
                .filter(order -> status == null || order.getOrderStatus().equals(status))
                .filter(order -> employeeId == null || (order.getEmployeeId()!=null && order.getEmployeeId()==Long.parseLong(employeeId)))
                .map(OrderResponse::create)
                .collect(Collectors.toList());
    }
}
