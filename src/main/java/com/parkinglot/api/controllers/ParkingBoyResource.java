package com.parkinglot.api.controllers;

import com.parkinglot.api.domain.Employee;
import com.parkinglot.api.domain.EmployeeRepository;
import com.parkinglot.api.domain.ParkingLotRepository;
import com.parkinglot.api.domain.RoleName;
import com.parkinglot.api.models.EmployeeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.net.*;
import java.util.*;
import java.util.stream.*;

@RestController
@RequestMapping("/parkingboys")
public class ParkingBoyResource {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ParkingLotRepository parkingLotRepository;
    @Autowired
    private EntityManager entityManager;

    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAll() {
        final List<EmployeeResponse> parkingBoys = employeeRepository.findByRole(RoleName.ROLE_PARKING_CLERK.toString())
            .stream()
            .map(EmployeeResponse::create)
            .collect(Collectors.toList());
        return ResponseEntity.ok(parkingBoys);
    }

    @CrossOrigin
    @PostMapping(consumes = {"application/json"})
    public ResponseEntity<String> add(@RequestBody Employee employee) {
        employee.setRole(RoleName.ROLE_PARKING_CLERK.toString());
        if (employeeRepository.save(employee) != null) {
            return ResponseEntity.created(URI.create("/parkingboys/" + employee.getId())).build();
        }
        return ResponseEntity.badRequest().build();
    }

}
