package com.parkinglot.api.controllers;


import com.parkinglot.api.domain.Order;
import com.parkinglot.api.domain.OrderRepository;
import com.parkinglot.api.models.OrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderResource {
    @Autowired
    private OrderRepository orderRepository;

    final static String ORDER_STATUS_PENDING = "pending";
    final static String ORDER_STATUS_PARKING = "parking";
    final static String ORDER_STATUS_FETCHED = "fetched";
    final static String ORDER_STATUS_CANCEL = "cancel";

    @PostMapping(produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<OrderResponse> postOrder(@RequestBody Order order) {
        Order orderWithStatus= new Order(order.getVehicleNumber());
        orderRepository.save(orderWithStatus);
        orderRepository.flush();
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping(produces = {"application/json"})
    public ResponseEntity<OrderResponse[]> getAllOrder() {
        final OrderResponse[] orders = orderRepository.findAll().stream()
                .filter(order -> order.getOrderStatus().equals(ORDER_STATUS_PENDING))
                .map(OrderResponse::create)
                .toArray(OrderResponse[]::new);
        return ResponseEntity.ok(orders);
    }

}
