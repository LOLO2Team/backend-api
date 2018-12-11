package com.parkinglot.api.controllers;

import com.parkinglot.api.domain.OrderRepository;
import com.parkinglot.api.domain.Employee;
import com.parkinglot.api.domain.EmployeeRepository;
import com.parkinglot.api.domain.ParkingLot;
import com.parkinglot.api.domain.ParkingLotRepository;
import com.parkinglot.api.models.ParkingLotResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
    private EmployeeRepository employeeRepository;

    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<ParkingLotResponse>> getAvailableParkingLots(@RequestParam(value = "employeeId", required = false) Long employeeId) {

        List<ParkingLotResponse> parkingLots;
        if(employeeId==null) {
            parkingLots = parkingLotRepository.findAll().stream()
                .map(ParkingLotResponse::create)
                .collect(Collectors.toList());
        }else {
            parkingLots = parkingLotRepository
                .findByEmployeeId(employeeId).stream()
                .filter(parkingLot -> hasSpace(parkingLot))
                .map(parkingLot -> ParkingLotResponse
                    .create(parkingLot.getId(), parkingLot.getParkingLotName(), parkingLot.getCapacity(), parkingLot.getReservedSpace(),
                        parkingLot.getEmployeeId()))
                .collect(Collectors.toList());
        }

        return ResponseEntity.ok(parkingLots);
    }

    @CrossOrigin
    @PutMapping(value = "/{parkingLotId}/employeeId/{employeeId}", produces = {"application/json"})
    public ResponseEntity<String> assignParkingLot(@PathVariable Long parkingLotId, @PathVariable Long employeeId) {

        Optional<Employee> parkingBoy = employeeRepository.findById(employeeId);
        if (!parkingBoy.isPresent()) {
            return ResponseEntity.status(404).header("errorMessage", "parking boy id:" + employeeId + " not found").build();
        }

        Optional<ParkingLot> parkingLot = parkingLotRepository.findById(parkingLotId);
        if (!parkingLot.isPresent()) {
            return ResponseEntity.status(404).header("errorMessage", "parking lot id:" + parkingLotId + " not found").build();
        }

        parkingLot.get().setEmployeeId(employeeId);
        if (parkingLotRepository.save(parkingLot.get()) != null) {
            return ResponseEntity.created(URI.create("/parkinglots/" + parkingLot.get().getId())).build();
        }
        parkingLotRepository.flush();
        return ResponseEntity.badRequest().build();
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity<String> add(@RequestBody ParkingLot parkingLot) {
        if (parkingLotRepository.save(parkingLot) != null) {
            return ResponseEntity.created(URI.create("/parkinglots/" + parkingLot.getId())).build();
        }
        return ResponseEntity.badRequest().build();
    }

    private boolean hasSpace(ParkingLot parkingLot) {
        int carCountInParkingLot = orderRepository
            .findByParkingLotIdAndOrderStatus(parkingLot.getId(), ORDER_STATUS_PARKED)
            .size();
        return parkingLot.getCapacity() > carCountInParkingLot;
    }
}
