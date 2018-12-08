package com.parkinglot.api.controllers;

import com.parkinglot.api.domain.*;
import com.parkinglot.api.models.OrderResponse;
import com.parkinglot.api.models.ParkingBoyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.net.*;
import java.util.*;
import java.util.stream.*;

import static com.parkinglot.api.controllers.OrderResource.ORDER_STATUS_PARKING;
import static com.parkinglot.api.controllers.OrderResource.ORDER_STATUS_PENDING;

@RestController
@RequestMapping("/parkingboys")
public class ParkingBoyResource {

    @Autowired
    private ParkingBoyRepository parkingBoyRepository;
    @Autowired
    private ParkingLotRepository parkingLotRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private EntityManager entityManager;

    @GetMapping
    public ResponseEntity<ParkingBoyResponse[]> getAll() {
        final ParkingBoyResponse[] parkingBoys = parkingBoyRepository.findAll().stream()
                .map(ParkingBoyResponse::create)
                .toArray(ParkingBoyResponse[]::new);
        return ResponseEntity.ok(parkingBoys);
    }

    @PostMapping(consumes = {"application/json"})
    public ResponseEntity<String> add(@RequestBody ParkingBoy parkingBoy) {
        if (parkingBoyRepository.save(parkingBoy) != null) {
            return ResponseEntity.created(URI.create("/parkingboys/" + parkingBoy.getId())).build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping(value = "/{employeeId}/parkinglots/{parkingLotId}/orders/{orderId}")
    public ResponseEntity<OrderResponse> parkCarToParkingLot(
            @PathVariable Long employeeId,
            @PathVariable Long parkingLotId,
            @PathVariable Long orderId ) {

//      uncomment after parkingboy sign up finished
//        Optional<ParkingBoy> parkingBoy = parkingBoyRepository.findById(employeeId);
//        if (!parkingBoy.isPresent()) {
//            return ResponseEntity.status(404).header("errorMessage","parking boy id:"+orderId+" not found").build();
//        }

        Optional<ParkingLot> parkingLot = parkingLotRepository.findById(parkingLotId);
        if (!parkingLot.isPresent()) {
            return ResponseEntity.status(404).header("errorMessage","parking lot id:"+parkingLotId+" not found").build();
        }

        Optional<Order> order = orderRepository.findById(orderId);
        if (!order.isPresent()) {
            return ResponseEntity.status(404).header("errorMessage","parking order id:"+orderId+" not found").build();
        }

        if(!order.get().getOrderStatus().equals(ORDER_STATUS_PENDING)){
            return ResponseEntity.status(400).header("errorMessage","parking order id:"+orderId+" status is not pending").build();
        }

        order.get().setOrderStatus(ORDER_STATUS_PARKING);
        order.get().setParkingLotId(parkingLotId);

        return ResponseEntity.ok(OrderResponse.create( order.get()));
    }
}
