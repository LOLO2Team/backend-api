package com.parkinglot.api.domain;

import javax.persistence.*;

@Entity
@Table(name = "parking_boy")
public class ParkingBoy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    @Column(name = "name", length = 32)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected ParkingBoy() {}

    public ParkingBoy(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }
}

