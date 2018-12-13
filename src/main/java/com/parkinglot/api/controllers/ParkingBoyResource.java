package com.parkinglot.api.controllers;

import com.parkinglot.api.domain.Employee;
import com.parkinglot.api.domain.EmployeeRepository;
import com.parkinglot.api.domain.EmployeeStatus;
import com.parkinglot.api.domain.OrderRepository;
import com.parkinglot.api.domain.ParkingLotRepository;
import com.parkinglot.api.user.RoleName;
import com.parkinglot.api.models.EmployeeDetailResponse;
import com.parkinglot.api.models.EmployeeResponse;
import com.parkinglot.api.models.ParkingLotResponse;
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

import javax.persistence.EntityManager;
import java.net.*;
import java.util.*;
import java.util.stream.*;

import static com.parkinglot.api.controllers.OrderResource.ORDER_STATUS_PARKED;

@RestController
@RequestMapping("/parkingboys")
public class ParkingBoyResource {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ParkingLotRepository parkingLotRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @CrossOrigin
    @GetMapping
    public ResponseEntity<Object> getParkingBoys(
        @RequestParam(value = "status", required = false) String status,
        @RequestParam(value = "username", required = false) String username) {
        if(username!=null){
            final Employee employee = employeeRepository.findByUsername(username);
            if (employee == null) {
                return ResponseEntity.status(404).body("employee username: " + username + " not found");
            }
            return ResponseEntity.ok(EmployeeResponse.create(employee));
        }else {
            List<EmployeeDetailResponse> parkingBoys = employeeRepository.findAll()
                .stream()
                .filter(order -> status == null || order.getStatus().equals(status))
                .filter(parkingBoy -> isRole(parkingBoy, RoleName.ROLE_PARKING_CLERK))
                .map(EmployeeDetailResponse::create)
                .collect(Collectors.toList());
            parkingBoys.forEach(parkingBoy -> {
                    appendParkingLot(parkingBoy);
                }
            );
            return ResponseEntity.ok(parkingBoys);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/search")
    public ResponseEntity<List<EmployeeDetailResponse>> getParkingBoysBy(
        @RequestParam(value = "q") String expect) {
        List<EmployeeDetailResponse> parkingBoys = employeeRepository.findAll()
            .stream()
            .filter(parkingBoy -> findContain(parkingBoy, expect))
            .filter(parkingBoy -> isRole(parkingBoy, RoleName.ROLE_PARKING_CLERK))
            .map(EmployeeDetailResponse::create)
            .collect(Collectors.toList());
        parkingBoys.forEach(parkingBoy -> {
                appendParkingLot(parkingBoy);
            }
        );
        return ResponseEntity.ok(parkingBoys);
    }

    private boolean isRole(Employee employee, RoleName roleName) {
        return employee.getAuthorities().stream().anyMatch(auth -> auth.getName().equals(roleName));
    }

    private boolean findContain(Employee employee, String expect) {
        String dataCollection =
            employee.getEmail() + employee.getName() + employee.getPhone() + employee.getStatus() + employee.getUsername() + "/" + employee.getId();
        return dataCollection.toUpperCase().contains(expect.toUpperCase());
    }

    private void appendParkingLot(EmployeeDetailResponse parkingBoy) {
        List<ParkingLotResponse> parkingLots = parkingLotRepository
            .findByEmployeeId(parkingBoy.getEmployeeId()).stream()
            .map(ParkingLotResponse::create)
            .collect(Collectors.toList());
        parkingBoy.setParkingLotResponses(parkingLots);
        parkingLots.forEach(parkingLot -> parkingLot.setParkedCount(getCarCountInParkingLot(parkingLot.getParkingLotId())));
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
    @GetMapping(value = "/username/{username}")
    public ResponseEntity<Object> getParkingBoyByUsername(@PathVariable String username) {
        final Employee employee = employeeRepository.findByUsername(username);
        if (employee == null) {
            return ResponseEntity.status(404).body("employee username: " + username + " not found");
        }
        return ResponseEntity.ok(EmployeeResponse.create(employee));
    }

    @CrossOrigin
    @PostMapping(consumes = {"application/json"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> add(@RequestBody Employee employee) {
        employee.setPassword(bCryptPasswordEncoder.encode(employee.getPassword()));
        employee.setAuthorities(employee.getAuthorities());
        if (employee.getStatus() != null && !isValidStatus(employee.getStatus())) {
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
        if (!isValidStatus(status)) {
            return ResponseEntity.status(400).body("status: " + status + " not found/support");
        }
        if (!employee.isPresent()) {
            return ResponseEntity.status(404).body("parking boy not found");
        }
        employee.get().setStatus(status);
        employeeRepository.save(employee.get());
        return ResponseEntity.ok(EmployeeResponse.create(employee.get()));
    }

    boolean isValidStatus(String status) {
        return Arrays.stream(EmployeeStatus.values()).anyMatch((t) -> t.name().equals(status));
    }

    private int getCarCountInParkingLot(Long parkingLotId) {
        return orderRepository
            .findByParkingLotIdAndOrderStatus(parkingLotId, ORDER_STATUS_PARKED)
            .size();
    }
}
