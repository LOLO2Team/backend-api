package com.parkinglot.api.services;

import com.parkinglot.api.domain.*;
import com.parkinglot.api.models.ParkingLotResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.parkinglot.api.controllers.OrderResource.ORDER_STATUS_PARKED;

@Service
public class ParkingLotService {

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private OrderRepository orderRepository;


    public boolean isValidStatus(String status) {
        return Arrays.stream(ParkingLotStatus.values()).anyMatch((t) -> t.name().equals(status));
    }

    public boolean hasSpace(ParkingLot parkingLot) {
        return parkingLot.getCapacity() > getCarCountInParkingLot(parkingLot.getId());
    }


    public List<ParkingLotResponse> searchParkingLots(@RequestParam("q") String expect) {
        return parkingLotRepository
                .findAll().stream()
                .filter(parkingLot -> findContain(parkingLot, expect))
                .map(ParkingLotResponse::create)
                .collect(Collectors.toList());
    }

    public boolean findContain(ParkingLot parkingLot, String expect){
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

    public int getCarCountInParkingLot(Long parkingLotId) {
        return orderRepository
                .findByParkingLotIdAndOrderStatus(parkingLotId, ORDER_STATUS_PARKED)
                .size();
    }

    public void setParkedCountToParkingLots(List<ParkingLotResponse> parkingLots) {
        parkingLots.forEach(parkingLot -> parkingLot.setParkedCount(getCarCountInParkingLot(parkingLot.getParkingLotId())));
    }

    public List<ParkingLotResponse> getHasSpaceParkingLotsByEmployeeId(@RequestParam(value = "employeeId", required = false) Long employeeId) {
        return parkingLotRepository
                .findByEmployeeId(employeeId).stream()
                .filter(parkingLot -> hasSpace(parkingLot))
                .map(ParkingLotResponse::create)
                .collect(Collectors.toList());
    }

    public List<ParkingLotResponse> getAllParkingLots() {
        return parkingLotRepository
                .findAll().stream()
                .map(ParkingLotResponse::create)
                .collect(Collectors.toList());
    }
}
