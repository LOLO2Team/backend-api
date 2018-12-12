package com.parkinglot.api.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByNameAndRole(String name, String role);
    List<Employee> findByRole(String role);
    Employee findByUsername(String username);
    Employee findByUsernameAndRole(String username, String role);
}
