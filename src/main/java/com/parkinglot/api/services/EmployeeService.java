package com.parkinglot.api.services;

import com.parkinglot.api.domain.Employee;
import com.parkinglot.api.domain.EmployeeRepository;
import com.parkinglot.api.domain.EmployeeStatus;
import com.parkinglot.api.domain.OrderRepository;
import com.parkinglot.api.domain.ParkingLotRepository;
import com.parkinglot.api.models.EmployeeDetailResponse;
import com.parkinglot.api.models.EmployeeResponse;
import com.parkinglot.api.models.ParkingLotResponse;
import com.parkinglot.api.user.Role;
import com.parkinglot.api.user.RoleName;
import com.parkinglot.api.user.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.*;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ParkingLotRepository parkingLotRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ParkingLotService parkingLotService;


    public EmployeeResponse findByUsername(String username) {
        final Employee employee = employeeRepository.findByUsername(username);
        return EmployeeResponse.create(employee);
    }

    public boolean isRole(Employee employee, RoleName roleName) {
        return employee.getAuthorities().stream().anyMatch(auth -> auth.getName().equals(roleName));
    }

    public boolean findContain(Employee employee, String expect) {
        String dataCollection =
            employee.getEmail() + employee.getName() + employee.getPhone() + employee.getStatus() + employee.getUsername() + "/" + employee.getId();
        return dataCollection.toUpperCase().contains(expect.toUpperCase());
    }

    public void appendParkingLot(EmployeeDetailResponse parkingBoy) {
        List<ParkingLotResponse> parkingLots = parkingLotRepository
            .findByEmployeeId(parkingBoy.getEmployeeId()).stream()
            .map(ParkingLotResponse::create)
            .collect(Collectors.toList());
        parkingBoy.setParkingLotResponses(parkingLots);
        parkingLots.forEach(parkingLot -> parkingLot.setParkedCount(
                parkingLotService.getCarCountInParkingLot(parkingLot.getParkingLotId())));
    }


    public boolean isValidStatus(String status) {
        return Arrays.stream(EmployeeStatus.values()).anyMatch((t) -> t.name().equals(status));
    }

    public List<EmployeeDetailResponse> getParkingBoys(String status) {
        List<EmployeeDetailResponse> parkingBoys = employeeRepository.findAll()
            .stream()
            .filter(order -> status == null || order.getStatus().equals(status))
            .filter(parkingBoy -> isRole(parkingBoy, RoleName.ROLE_PARKING_CLERK))
            .map(EmployeeDetailResponse::create)
            .collect(Collectors.toList());
        parkingBoys.forEach(parkingBoy -> {appendParkingLot(parkingBoy); });
        return parkingBoys;
    }

    public List<EmployeeResponse> getEmployees(){
        return employeeRepository.findAll()
            .stream()
            .map(EmployeeResponse::create)
            .collect(Collectors.toList());
    }

    public List<EmployeeResponse> search(String expect){
        List<EmployeeResponse> employees = employeeRepository.findAll()
                .stream()
                .filter(parkingBoy -> findContain(parkingBoy, expect))
                .map(EmployeeResponse::create)
                .collect(Collectors.toList());
        return employees;
    }

    public List<Role> createRoleList(String role){
        Optional<Role> newRole = roleRepository.findByName(RoleName.valueOf(role));
        List<Role> newRoleList = new ArrayList<>();
        newRoleList.add(newRole.get());
        return newRoleList;
    }
}
