package com.parkinglot.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parkingboys")
public class ParkingBoyResource {

    @GetMapping
    public ResponseEntity<String[]> getAll() {
        return ResponseEntity.ok(new String[]{"I", "am", "a", "new", "array"});
    }

}
