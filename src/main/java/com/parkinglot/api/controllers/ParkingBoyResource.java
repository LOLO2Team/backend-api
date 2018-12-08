package com.parkinglot.api.controllers;

import com.parkinglot.api.domain.ParkingBoy;
import com.parkinglot.api.domain.ParkingBoyRepository;
import com.parkinglot.api.domain.ParkingLot;
import com.parkinglot.api.domain.ParkingLotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.net.*;
import java.util.*;
import java.util.stream.*;

@RestController
@RequestMapping("/parkingboys")
public class ParkingBoyResource {

    @Autowired
    private ParkingBoyRepository parkingBoyRepository;
    @Autowired
    private ParkingLotRepository parkingLotRepository;
    @Autowired
    private EntityManager entityManager;

    @GetMapping
    public ResponseEntity<String[]> testConnectionMethod() {
        return ResponseEntity.ok(new String[] {"I", "am", "a", "new", "array"});
    }

    @PostMapping(consumes = {"application/json"})
    public ResponseEntity<String> add(@RequestBody ParkingBoy parkingBoy) {
        if (parkingBoyRepository.save(parkingBoy) != null) {
            return ResponseEntity.created(URI.create("/parkingboys/" + parkingBoy.getId())).build();
        }
        return ResponseEntity.badRequest().build();
    }
}
