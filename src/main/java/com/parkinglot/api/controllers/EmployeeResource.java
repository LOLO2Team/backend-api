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
        final List<EmployeeResponse> employees = employeeRepository.findAll()
            .stream()
            .map(EmployeeResponse::create)
            .collect(Collectors.toList());
        return ResponseEntity.ok(employees);
    }

    @CrossOrigin
    @PostMapping(consumes = {"application/json"})
    public ResponseEntity<Object> add(@RequestBody Employee employee) {
        if(employee.getRole()== null || !Arrays.stream(RoleName.values()).anyMatch((t) -> t.name().equals(employee.getRole()))){
            return ResponseEntity.status(400).body("the role not found");
        }
        Employee savedEmployee = employeeRepository.save(employee);
        if (savedEmployee != null) {
            return ResponseEntity
                .created(URI.create("/parkingboys/" + employee.getId()))
                .body(EmployeeResponse.create(savedEmployee));
        }
        return ResponseEntity.badRequest().build();
    }

}
