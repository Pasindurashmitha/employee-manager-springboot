package com.example.employee_manager.repository;

import com.example.employee_manager.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Check whether an email already exists
    boolean existsByEmail(String email);

    // Check whether another employee uses the same email
    boolean existsByEmailAndIdNot(String email, Long id);
}

