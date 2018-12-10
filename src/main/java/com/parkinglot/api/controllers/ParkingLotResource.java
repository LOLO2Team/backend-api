package com.parkinglot.api.controllers;

import com.parkinglot.api.domain.ParkingLot;
import com.parkinglot.api.domain.ParkingLotRepository;
import com.parkinglot.api.models.ParkingLotResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/parkinglots")
public class ParkingLotResource {

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @GetMapping
    public ResponseEntity<ParkingLotResponse[]> getAll() {
        final ParkingLotResponse[] parkingLots = parkingLotRepository.findAll().stream()
            .map(parkingLot -> ParkingLotResponse.create(parkingLot.getId(),parkingLot.getParkingLotName(), parkingLot.getCapacity(), parkingLot.getReservedSpace(), parkingLot.getEmployeeId()))
            .toArray(ParkingLotResponse[]::new);
        return ResponseEntity.ok(parkingLots);
    }

    @PostMapping
    public ResponseEntity<String> add(@RequestBody  ParkingLot parkingLot) {
        if(parkingLotRepository.save(parkingLot)!=null) {
            return ResponseEntity.created(URI.create("/parkinglots/" + parkingLot.getId())).build();
        }
        return ResponseEntity.badRequest().build();
    }
}
