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

    @PostMapping(produces = {"application/json"},consumes = {"application/json"})
    public ResponseEntity<OrderResponse> postOrder(@RequestBody Order order) {
        orderRepository.save(order);
        orderRepository.flush();
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping(produces = {"application/json"})
    public ResponseEntity<OrderResponse[]> getAllOrder(){
        final OrderResponse[] orders  = orderRepository.findAll().stream()
                .map(OrderResponse::create)
                .toArray(OrderResponse[]::new);
        return ResponseEntity.ok(orders);
    }

}
