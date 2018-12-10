package com.parkinglot.api.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByParkingLotIdAndOrderStatus(Long parkingLotId, String orderStatus);
}
