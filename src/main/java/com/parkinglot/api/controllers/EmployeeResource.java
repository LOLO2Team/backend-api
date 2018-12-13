package com.parkinglot.api.controllers;

import com.google.gson.Gson;
import com.parkinglot.api.domain.Employee;
import com.parkinglot.api.domain.EmployeeRepository;
import com.parkinglot.api.domain.ParkingLotRepository;
import com.parkinglot.api.models.EmployeeResponse;
import com.parkinglot.api.user.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.io.Console;
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
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public EmployeeResource(EmployeeRepository employeeRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.employeeRepository = employeeRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody Employee employee) {
        employee.setPassword(bCryptPasswordEncoder.encode(employee.getPassword()));
        //Default role is staff
        employee.setAuthorities(employee.getAuthorities());
        employeeRepository.save(employee);
        employeeRepository.flush();
    }

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
    public ResponseEntity<Object> getByEmployeeByUsername(@PathVariable String username) {
        final Employee employee = employeeRepository.findByUsername(username);
        if (employee == null) {
            return ResponseEntity.status(404).body("employee username: " + username + " not found");
        }
        return ResponseEntity.ok(EmployeeResponse.create(employee));
    }

    @CrossOrigin
    @GetMapping(value = "/username/{username}/roles", produces = {"application/json"})
    public ResponseEntity<Object> getRoleByEmployeeByUsername(@PathVariable String username) {
        final Employee employee = employeeRepository.findByUsername(username);
        if (employee == null) {
            return ResponseEntity.status(404).body("employee username: " + username + " not found");
        }
        List<String> roles = new ArrayList<>();
        employee.getAuthorities().forEach(role -> roles.add(role.getName().toString()));
        String json = new Gson().toJson(roles);
        return ResponseEntity.ok(json);
    }


    @CrossOrigin
    @GetMapping(value = "/username/{username}/id", produces = {"application/json"})
    public ResponseEntity<Object> getIdByEmployeeByUsername(@PathVariable String username) {
        final Employee employee = employeeRepository.findByUsername(username);
        if (employee == null) {
            return ResponseEntity.status(404).body("employee username: " + username + " not found");
        }
        String json = new Gson().toJson(employee.getId());
        return ResponseEntity.ok(json);
    }

    @CrossOrigin
    @PostMapping(consumes = {"application/json"})
    public ResponseEntity<Object> add(@RequestBody Employee employee) {
        final Employee newEmployee = employeeRepository.save(employee);
        if (newEmployee == null) {
            return ResponseEntity.status(400).body("employee created fail");
        }
        return ResponseEntity.created(URI.create("/employees/" + employee.getId())).body(EmployeeResponse.create(employee));
    }
}
