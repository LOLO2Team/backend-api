package com.parkinglot.api.controllers;

import com.parkinglot.api.domain.Order;
import com.parkinglot.api.domain.OrderRepository;
import com.parkinglot.api.domain.ParkingBoy;
import com.parkinglot.api.domain.ParkingBoyRepository;
import com.parkinglot.api.domain.ParkingLot;
import com.parkinglot.api.domain.ParkingLotRepository;
import com.parkinglot.api.models.OrderResponse;
import com.parkinglot.api.models.ParkingBoyInteractRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.util.*;

@RestController
@RequestMapping("/orders")
public class OrderResource {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ParkingBoyRepository parkingBoyRepository;
    @Autowired
    private ParkingLotRepository parkingLotRepository;
    @Autowired
    private EntityManager entityManager;

    final static String ORDER_STATUS_PENDING = "pending";
    final static String ORDER_STATUS_PARKING = "parking";
    final static String ORDER_STATUS_FETCHING = "fetching";
    final static String ORDER_STATUS_FETCHED = "fetched";
    final static String ORDER_STATUS_CANCEL = "cancel";

    @PostMapping(produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<OrderResponse> postOrder(@RequestBody Order order) {
        Order orderWithStatus = new Order(order.getVehicleNumber());
        orderRepository.save(orderWithStatus);
        orderRepository.flush();
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @CrossOrigin
    @GetMapping(produces = {"application/json"})
    public ResponseEntity<OrderResponse[]> getOrders(@RequestParam(value = "status", required = false) String status) {
        OrderResponse[] orders;
        if (status == null) {
            orders = orderRepository.findAll().stream()
                .map(OrderResponse::create)
                .toArray(OrderResponse[]::new);
        } else {
            orders = orderRepository.findAll().stream()
                .filter(order -> order.getOrderStatus().equals(status))
                .map(OrderResponse::create)
                .toArray(OrderResponse[]::new);
        }
        return ResponseEntity.ok(orders);
    }

    @PutMapping(value = "/{orderId}")
    public ResponseEntity<OrderResponse> parkCarToParkingLot(
        @PathVariable Long orderId, @RequestBody ParkingBoyInteractRequest parkingBoyInteractRequest) {

        Optional<ParkingBoy> parkingBoy = parkingBoyRepository.findById(parkingBoyInteractRequest.getEmployeeId());
        if (!parkingBoy.isPresent()) {
            return ResponseEntity.status(404).header("errorMessage", "parking boy id:" + parkingBoyInteractRequest.getEmployeeId() + " not found")
                .build();
        }

        Optional<ParkingLot> parkingLot = parkingLotRepository.findById(parkingBoyInteractRequest.getParkingLotId());
        if (!parkingLot.isPresent()) {
            return ResponseEntity.status(404).header("errorMessage", "parking lot id:" + parkingBoyInteractRequest.getParkingLotId() + " not found")
                .build();
        }

        Optional<Order> order = orderRepository.findById(orderId);
        if (!order.isPresent()) {
            return ResponseEntity.status(404).header("errorMessage", "parking order id:" + orderId + " not found").build();
        }

        if (!order.get().getOrderStatus().equals(ORDER_STATUS_PENDING)) {
            return ResponseEntity.status(400).header("errorMessage", "parking order id:" + orderId + " status is not pending").build();
        }

        order.get().setOrderStatus(ORDER_STATUS_PARKING);
        order.get().setParkingLotId(parkingLot.get().getId());
        order.get().setParkerEmployeeId(parkingBoy.get().getId());
        orderRepository.save(order.get());
        orderRepository.flush();

        return ResponseEntity.ok(OrderResponse.create(order.get()));
    }

    @PatchMapping(value = "/{orderId}")
    public ResponseEntity<OrderResponse> createNewFetchRequest(
        @PathVariable Long orderId, @RequestBody long employeeId) {

        Optional<ParkingBoy> parkingBoy = parkingBoyRepository.findById(employeeId);
        if (!parkingBoy.isPresent()) {
            return ResponseEntity.status(404).header("errorMessage", "parking boy id:" + employeeId + " not found")
                .build();
        }

        Optional<Order> order = orderRepository.findById(orderId);
        if (!order.isPresent()) {
            return ResponseEntity.status(404).header("errorMessage", "parking order id:" + orderId + " not found").build();
        }

        if (!order.get().getOrderStatus().equals(ORDER_STATUS_PARKING)) {
            return ResponseEntity.status(400).header("errorMessage", "parking order id:" + orderId + " status is not pending").build();
        }

        order.get().setOrderStatus(ORDER_STATUS_FETCHING);
        order.get().setFetcherEmployeeId(parkingBoy.get().getId());
        orderRepository.save(order.get());
        orderRepository.flush();

        return ResponseEntity.ok(OrderResponse.create(order.get()));
    }

    @DeleteMapping(value = "/{orderId}")
    public ResponseEntity<OrderResponse> fetchCarToParkingLot(
        @PathVariable Long orderId) {

        Optional<Order> order = orderRepository.findById(orderId);
        if (!order.isPresent()) {
            return ResponseEntity.status(404).header("errorMessage", "parking order id:" + orderId + " not found").build();
        }

        if (!order.get().getOrderStatus().equals(ORDER_STATUS_FETCHING)) {
            return ResponseEntity.status(400).header("errorMessage", "parking order id:" + orderId + " status is not parking").build();
        }

        order.get().setOrderStatus(ORDER_STATUS_FETCHED);
        orderRepository.save(order.get());
        orderRepository.flush();

        return ResponseEntity.ok(OrderResponse.create(order.get()));
    }
}
