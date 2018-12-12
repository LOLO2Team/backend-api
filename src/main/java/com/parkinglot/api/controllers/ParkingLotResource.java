package com.parkinglot.api.controllers;

import com.parkinglot.api.domain.*;
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
        if (employeeId == null) {
            parkingLots = parkingLotRepository
                .findAll().stream()
                .map(ParkingLotResponse::create)
                .collect(Collectors.toList());
        } else {
            parkingLots = parkingLotRepository
                .findByEmployeeId(employeeId).stream()
                .filter(parkingLot -> hasSpace(parkingLot))
                .map(ParkingLotResponse::create)
                .collect(Collectors.toList());
        }
        parkingLots.forEach(parkingLot -> parkingLot.setParkedCount(getCarCountInParkingLot(parkingLot.getParkingLotId())));
        return ResponseEntity.ok(parkingLots);
    }

    @CrossOrigin
    @GetMapping(value = "/search")
    public ResponseEntity<List<ParkingLotResponse>> searchParkingLot(@RequestParam(value = "q") String expect) {
        List<ParkingLotResponse> parkingLots = parkingLotRepository
                .findAll().stream()
                .filter(parkingLot -> findContain(parkingLot, expect))
                .map(ParkingLotResponse::create)
                .collect(Collectors.toList());
        parkingLots.forEach(parkingLot -> parkingLot.setParkedCount(getCarCountInParkingLot(parkingLot.getParkingLotId())));
        return ResponseEntity.ok(parkingLots);
    }

    private boolean findContain(ParkingLot parkingLot, String expect){
        String dataCollection = parkingLot.getParkingLotName() +parkingLot.getParkingLotStatus() +"/"+parkingLot.getId()+"/"+parkingLot.getCapacity();
        if(parkingLot.getEmployeeId()!=null){
            Optional<Employee> opEmployee = employeeRepository.findById(parkingLot.getEmployeeId());
            if(opEmployee.isPresent()) {
                Employee employee = opEmployee.get();
                dataCollection += employee.getEmail()+employee.getName()+employee.getPhone()+employee.getStatus()+employee.getUsername()+"/"+employee.getId();
            }
        }
        return dataCollection.toUpperCase().contains(expect.toUpperCase());
    }

    @CrossOrigin
    @PutMapping(value = "/{parkingLotId}/employeeId/{employeeId}", produces = {"application/json"})
    public ResponseEntity<String> assignParkingLot(@PathVariable Long parkingLotId, @PathVariable Long employeeId) {

        Optional<Employee> parkingBoy = employeeRepository.findById(employeeId);
        if (!parkingBoy.isPresent()) {
            return ResponseEntity.status(404).body("parking boy id:" + employeeId + " not found");
        }

        Optional<ParkingLot> parkingLot = parkingLotRepository.findById(parkingLotId);
        if (!parkingLot.isPresent()) {
            return ResponseEntity.status(404).body("parking lot id:" + parkingLotId + " not found");
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

    private int getCarCountInParkingLot(Long parkingLotId) {
        return orderRepository
            .findByParkingLotIdAndOrderStatus(parkingLotId, ORDER_STATUS_PARKED)
            .size();
    }

    @CrossOrigin
    @PutMapping(value = "/{parkingLotId}/status/{status}")
    public ResponseEntity<Object> updateStatus(@PathVariable Long parkingLotId, @PathVariable String status) {
        Optional<ParkingLot> parkingLot = parkingLotRepository.findById(parkingLotId);
        if (!isValidStatus(status)) {
            return ResponseEntity.status(400).body("parking lot status: " + status + " not found/support");
        }
        if (!parkingLot.isPresent()) {
            return ResponseEntity.status(404).body("parking lot not found");
        }
        parkingLot.get().setParkingLotStatus(status);
        parkingLotRepository.save(parkingLot.get());
        ParkingLotResponse parkingLotResponse = ParkingLotResponse.create(parkingLot.get());
        parkingLotResponse.setParkedCount(getCarCountInParkingLot(parkingLotResponse.getParkingLotId()));
        return ResponseEntity.ok(parkingLotResponse);
    }

    boolean isValidStatus(String status) {
        return Arrays.stream(ParkingLotStatus.values()).anyMatch((t) -> t.name().equals(status));
    }

    private boolean hasSpace(ParkingLot parkingLot) {
        return parkingLot.getCapacity() > getCarCountInParkingLot(parkingLot.getId());
    }
}
