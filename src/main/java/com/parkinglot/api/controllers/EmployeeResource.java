package com.parkinglot.api.controllers;

import com.parkinglot.api.domain.Employee;
import com.parkinglot.api.domain.EmployeeRepository;
import com.parkinglot.api.domain.ParkingLotRepository;
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
@RequestMapping("/employees")
public class EmployeeResource {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ParkingLotRepository parkingLotRepository;
    @Autowired
    private EntityManager entityManager;

    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAll() {
        final List<EmployeeResponse> Employees = employeeRepository.findAll()
            .stream()
            .map(EmployeeResponse::create)
            .collect(Collectors.toList());
        return ResponseEntity.ok(Employees);
    }

    @CrossOrigin
    @GetMapping(value = "/username/{username}")
    public ResponseEntity<Object> getByEmployeeByUsername(String username) {
        final Employee employee = employeeRepository.findByUsername(username);
        if (employee == null) {
            return ResponseEntity.status(404).body("employee username: " + username + " not found");
        }
        return ResponseEntity.ok(EmployeeResponse.create(employee));
    }

    @CrossOrigin
    @PostMapping(consumes = {"application/json"})
    public ResponseEntity<Object> add(@RequestBody Employee employee) {
        final Employee newEmployee = employeeRepository.save(employee);
        if (employeeRepository.save(employee) == null) {
            return ResponseEntity.status(400).body("parking boy created fail");
        }
        return ResponseEntity.created(URI.create("/employees/" + employee.getId())).body(EmployeeResponse.create(employee));
    }
}
