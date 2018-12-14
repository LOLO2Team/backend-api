package com.parkinglot.api.controllers;

import com.parkinglot.api.domain.Employee;
import com.parkinglot.api.domain.EmployeeRepository;
import com.parkinglot.api.domain.OrderRepository;
import com.parkinglot.api.domain.ParkingLot;
import com.parkinglot.api.domain.ParkingLotRepository;
import com.parkinglot.api.domain.ParkingLotStatus;
import com.parkinglot.api.models.ParkingLotResponse;
import com.parkinglot.api.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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

@RestController
@RequestMapping("/parkinglots")
public class ParkingLotResource {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ParkingLotRepository parkingLotRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ParkingLotService parkingLotService;
    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<ParkingLotResponse>> getAvailableParkingLots(@RequestParam(value = "employeeId", required = false) Long employeeId) {
        List<ParkingLotResponse> parkingLots;
        if (employeeId == null) {
            parkingLots = parkingLotService.getAllParkingLots();
        } else {
            parkingLots = parkingLotService.getHasSpaceParkingLotsByEmployeeId(employeeId);
        }
        parkingLotService.setParkedCountToParkingLots(parkingLots);
        return ResponseEntity.ok(parkingLots);
    }

    @CrossOrigin
    @GetMapping(value = "/search")
    public ResponseEntity<List<ParkingLotResponse>> searchParkingLot(@RequestParam(value = "q") String expect) {
        List<ParkingLotResponse> parkingLots = parkingLotService.searchParkingLots(expect);
        parkingLotService.setParkedCountToParkingLots(parkingLots);
        return ResponseEntity.ok(parkingLots);
    }

    @CrossOrigin
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_MANAGER')")
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
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_MANAGER')")
    @DeleteMapping(value = "/{parkingLotId}/employeeId", produces = {"application/json"})
    public ResponseEntity<String> removeAssignedParkingLot(@PathVariable Long parkingLotId) {

        Optional<ParkingLot> parkingLot = parkingLotRepository.findById(parkingLotId);
        if (!parkingLot.isPresent()) {
            return ResponseEntity.status(404).body("parking lot id:" + parkingLotId + " not found");
        }

        parkingLot.get().setEmployeeId(null);
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

    @CrossOrigin
    @PutMapping(value = "/{parkingLotId}/status/{status}")
    public ResponseEntity<Object> updateStatus(@PathVariable Long parkingLotId, @PathVariable String status) {
        Optional<ParkingLot> parkingLot = parkingLotRepository.findById(parkingLotId);
        if (!parkingLotService.isValidStatus(status)) {
            return ResponseEntity.status(400).body("parking lot status: " + status + " not found/support");
        }
        if (!parkingLot.isPresent()) {
            return ResponseEntity.status(404).body("parking lot not found");
        }
        if (status.equals(ParkingLotStatus.CLOSED.name()) && parkingLot.get().getEmployeeId()!=null) {
            return ResponseEntity.status(409).body("this parking lot is assigned to parking boy: "+parkingLot.get().getEmployeeId());
        }
        parkingLot.get().setParkingLotStatus(status);
        parkingLotRepository.save(parkingLot.get());
        ParkingLotResponse parkingLotResponse = ParkingLotResponse.create(parkingLot.get());
        parkingLotResponse.setParkedCount(parkingLotService.getCarCountInParkingLot(parkingLotResponse.getParkingLotId()));
        return ResponseEntity.ok(parkingLotResponse);
    }
}
