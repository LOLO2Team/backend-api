package com.parkinglot.api.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Long> {

    List<ParkingLot> findByEmployeeId(long employeeId);
}
