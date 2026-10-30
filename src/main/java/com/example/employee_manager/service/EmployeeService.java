
package com.example.employee_manager.service;

import com.example.employee_manager.model.Employee;
import com.example.employee_manager.repository.EmployeeRepository;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Get all employees
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Get employee by ID
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    // Check whether email exists
    public boolean emailExists(String email, Long id) {
        if (id == null) {
            return employeeRepository.existsByEmail(email);
        }

        return employeeRepository.existsByEmailAndIdNot(email, id);
    }

    // Save employee
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    // Delete employee
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    // Search employees by name
    public List<Employee> searchEmployees(String keyword) {
        return employeeRepository
                .findByFullNameContainingIgnoreCase(keyword);
    }
}
