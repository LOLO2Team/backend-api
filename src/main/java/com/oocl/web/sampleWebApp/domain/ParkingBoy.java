package com.oocl.web.sampleWebApp.domain;

import javax.persistence.*;

@Entity
@Table(name = "parking_boy")
public class ParkingBoy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private long id;

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
}

