package com.parkinglot.api.controllers;

import com.parkinglot.api.domain.OrderRepository;
import com.parkinglot.api.domain.ParkingBoy;
import com.parkinglot.api.domain.ParkingBoyRepository;
import com.parkinglot.api.domain.ParkingLot;
import com.parkinglot.api.domain.ParkingLotRepository;
import com.parkinglot.api.models.ParkingLotResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.*;
import java.util.*;
import java.util.stream.*;

import static com.parkinglot.api.controllers.OrderResource.ORDER_STATUS_PARKED;

@RestController
@RequestMapping("/parkinglots")
public class ParkingLotResource {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ParkingLotRepository parkingLotRepository;
    @Autowired
    private ParkingBoyRepository parkingBoyRepository;

    @GetMapping
    public ResponseEntity<List<ParkingLotResponse>> getAvailableParkingLots(@RequestParam(value = "employeeId") Long employeeId) {
        final List<ParkingLotResponse> parkingLots = parkingLotRepository
            .findAll().stream()
            .filter(parkingLot -> parkingLot.getEmployeeId() != null && parkingLot.getEmployeeId() == employeeId)
            .filter(parkingLot -> hasSpace(parkingLot))
            .map(parkingLot -> ParkingLotResponse
                .create(parkingLot.getId(), parkingLot.getParkingLotName(), parkingLot.getCapacity(), parkingLot.getReservedSpace(),
                    parkingLot.getEmployeeId()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(parkingLots);
    }

    @PutMapping(value = "/{parkingLotId}/employeeId/{employeeId}", produces = {"application/json"})
    public ResponseEntity<String> assignParkingLot(@PathVariable Long parkingLotId, @PathVariable Long employeeId) {

        Optional<ParkingBoy> parkingBoy = parkingBoyRepository.findById(employeeId);
        if (!parkingBoy.isPresent()) {
            return ResponseEntity.status(404)
                .header("errorMessage", "parking boy id:" + employeeId + " not found")
                .build();
        }

        Optional<ParkingLot> parkingLot = parkingLotRepository.findById(parkingLotId);
        if (!parkingLot.isPresent()) {
            return ResponseEntity.status(404)
                .header("errorMessage", "parking lot id:" + parkingLotId + " not found")
                .build();
        }

        parkingLot.get().setEmployeeId(employeeId);
        if (parkingLotRepository.save(parkingLot.get()) != null) {
            return ResponseEntity.created(URI.create("/parkinglots/" + parkingLot.get().getId())).build();
        }
        parkingLotRepository.flush();
        return ResponseEntity.badRequest().build();
    }

    @PostMapping
    public ResponseEntity<String> add(@RequestBody ParkingLot parkingLot) {
        if (parkingLotRepository.save(parkingLot) != null) {
            return ResponseEntity.created(URI.create("/parkinglots/" + parkingLot.getId())).build();
        }
        return ResponseEntity.badRequest().build();
    }

    private boolean hasSpace(ParkingLot parkingLot) {
        int carCountInParkingLot = orderRepository.findAll()
            .stream()
            .filter(order -> order.getParkingLotId() != null && order.getParkingLotId() == parkingLot.getId())
            .filter(order -> order.getOrderStatus().equals(ORDER_STATUS_PARKED))
            .collect(Collectors.toList())
            .size();
        System.out.println(carCountInParkingLot);
        System.out.println(parkingLot.getCapacity());
        return parkingLot.getCapacity() > carCountInParkingLot;
    }
}
