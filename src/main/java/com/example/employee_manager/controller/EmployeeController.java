
package com.example.employee_manager.controller;

import com.example.employee_manager.model.Employee;
import com.example.employee_manager.service.EmployeeService;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Display all employees or search by name
    @GetMapping({"/", "/employees"})
    public String listEmployees(
            @RequestParam(required = false) String keyword,
            Model model) {

        List<Employee> employees;

        if (keyword == null || keyword.trim().isEmpty()) {
            employees = employeeService.getAllEmployees();
        } else {
            employees = employeeService.searchEmployees(keyword.trim());
        }

        model.addAttribute("employees", employees);
        model.addAttribute("keyword", keyword);

        return "employees";
    }

    // Show new employee form
    @GetMapping("/employees/new")
    public String showNewEmployeeForm(Model model) {

        model.addAttribute("employee", new Employee());

        return "employee-form";
    }

    // Save or update employee
    @PostMapping("/employees/save")
    public String saveEmployee(
            @Valid @ModelAttribute("employee") Employee employee,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        // Check validation errors
        if (result.hasErrors()) {
            return "employee-form";
        }

        // Check duplicate email
        if (employeeService.emailExists(
                employee.getEmail(), employee.getId())) {

            result.rejectValue(
                    "email",
                    "duplicate",
                    "This email is already registered"
            );

            return "employee-form";
        }

        boolean isNewEmployee = employee.getId() == null;

        employeeService.saveEmployee(employee);

        if (isNewEmployee) {
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Employee saved successfully!"
            );
        } else {
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Employee updated successfully!"
            );
        }

        return "redirect:/";
    }

    // Show edit form
    @GetMapping("/employees/edit/{id}")
    public String editEmployee(
            @PathVariable Long id,
            Model model) {

        Employee employee = employeeService
                .getEmployeeById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid employee ID: " + id
                        ));

        model.addAttribute("employee", employee);

        return "employee-form";
    }

    // Delete employee
    @GetMapping("/employees/delete/{id}")
    public String deleteEmployee(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        employeeService.deleteEmployee(id);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Employee deleted successfully!"
        );

        return "redirect:/";
    }
}
