package com.parkinglot.api.controllers;

import com.google.gson.Gson;
import com.parkinglot.api.domain.Employee;
import com.parkinglot.api.domain.EmployeeRepository;
import com.parkinglot.api.domain.ParkingLotRepository;
import com.parkinglot.api.models.EmployeeDetailResponse;
import com.parkinglot.api.models.EmployeeResponse;
import com.parkinglot.api.services.EmployeeService;
import com.parkinglot.api.user.Role;
import com.parkinglot.api.user.RoleName;
import com.parkinglot.api.user.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

@RestController
@RequestMapping(value={"/employees", "/parkingboys"})
public class EmployeeResource {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ParkingLotRepository parkingLotRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    public EmployeeResource(EmployeeRepository employeeRepository,
        BCryptPasswordEncoder bCryptPasswordEncoder, EmployeeService employeeService, RoleRepository roleRepository) {
        this.employeeRepository = employeeRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.employeeService = employeeService;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody Employee employee) {
        employee.setPassword(bCryptPasswordEncoder.encode(employee.getPassword()));
        employee.setAuthorities(employee.getAuthorities());
        employeeRepository.save(employee);
        employeeRepository.flush();
    }

    @CrossOrigin
    @GetMapping
    public ResponseEntity<Object> getAllEmployees(
        @RequestParam(value = "status", required = false) String status,
        @RequestParam(value = "username", required = false) String username,
        @RequestParam(value = "type", required = false) String type) {

        if (username != null) {
            final Employee employee = employeeRepository.findByUsername(username);
            if (employee == null) {
                return ResponseEntity.status(404).body("employee username: " + username + " not found");
            }
            return ResponseEntity.ok(EmployeeResponse.create(employee));
        } else if(type!=null && type.equalsIgnoreCase("parkingBoysDetail")){
            List<EmployeeDetailResponse> parkingBoys = employeeService.getParkingBoys(status);
            return ResponseEntity.ok(parkingBoys);
        }else{
            final List<EmployeeResponse> Employees = employeeService.getEmployees();
            return ResponseEntity.ok(Employees);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/{username}/roles", produces = {"application/json"})
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
    @PostMapping(consumes = {"application/json"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> add(@RequestBody Employee employee) {
        employee.setPassword(bCryptPasswordEncoder.encode(employee.getPassword()));
        employee.setAuthorities(employee.getAuthorities());
        if (employee.getStatus() != null && !employeeService.isValidStatus(employee.getStatus())) {
            return ResponseEntity.status(400).body("status: " + employee.getStatus() + " not found/support");
        }
        final Employee newParkingBoy = employeeRepository.save(employee);
        if (newParkingBoy == null) {
            return ResponseEntity.status(400).body("parking boy created fail");
        }
        return ResponseEntity.created(URI.create("/parkingboys/" + employee.getId())).body(EmployeeResponse.create(employee));
    }

    @CrossOrigin
    @PutMapping(value = "/{employeeId}/status/{status}")
    public ResponseEntity<Object> updateStatus(@PathVariable Long employeeId, @PathVariable String status) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        if (!employeeService.isValidStatus(status)) {
            return ResponseEntity.status(400).body("status: " + status + " not found/support");
        }
        if (!employee.isPresent()) {
            return ResponseEntity.status(404).body("parking boy not found");
        }
        employee.get().setStatus(status);
        employeeRepository.save(employee.get());
        return ResponseEntity.ok(EmployeeResponse.create(employee.get()));
    }

    @CrossOrigin
    @GetMapping(value = "/{employeeId}")
    public ResponseEntity<Object> getParkingBoy(@PathVariable Long employeeId) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        if (!employee.isPresent()) {
            return ResponseEntity.status(404).body("employee id: " + employeeId + " not found");
        }
        return ResponseEntity.ok(EmployeeResponse.create(employee.get()));
    }

    @CrossOrigin
    @GetMapping(value = "/search")
    public ResponseEntity<List<EmployeeDetailResponse>> getParkingBoysBy(
        @RequestParam(value = "q") String expect) {
        List<EmployeeDetailResponse> parkingBoys = employeeRepository.findAll()
            .stream()
            .filter(parkingBoy -> employeeService.findContain(parkingBoy, expect))
            .filter(parkingBoy -> employeeService.isRole(parkingBoy, RoleName.ROLE_PARKING_CLERK))
            .map(EmployeeDetailResponse::create)
            .collect(Collectors.toList());
        parkingBoys.forEach(parkingBoy -> {
                employeeService.appendParkingLot(parkingBoy);
            }
        );
        return ResponseEntity.ok(parkingBoys);
    }

    @CrossOrigin
    @PutMapping(value = "/{employeeId}/roles/{role}")
    public ResponseEntity<Object> updateRole(@PathVariable Long employeeId, @PathVariable String role) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        if (!employee.isPresent()) {
            return ResponseEntity.status(404).body("parking boy not found");
        }

        Optional<Role> newRole = roleRepository.findByName(RoleName.valueOf(role));
        List<Role> newRoleList = new ArrayList<>();
        newRoleList.add(newRole.get());
        employee.get().setAuthorities(newRoleList);
       System.out.println("Employee Role: " + newRole.get().getName());
        employeeRepository.save(employee.get());
        employeeRepository.flush();
        return ResponseEntity.ok(EmployeeResponse.create(employee.get()));
    }

}
