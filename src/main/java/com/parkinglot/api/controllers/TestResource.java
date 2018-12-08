package com.parkinglot.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class TestResource {

    @GetMapping
    public ResponseEntity<String[]> testConnectionMethod() {
        return ResponseEntity.ok(new String[] {"The", "server", "just", "run", "!"});
    }
}
