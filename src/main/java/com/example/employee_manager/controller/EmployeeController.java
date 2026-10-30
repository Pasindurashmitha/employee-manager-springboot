
package com.example.employee_manager.controller;

import com.example.employee_manager.model.Employee;
import com.example.employee_manager.service.EmployeeService;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Display all employees
    @GetMapping("/")
    public String listEmployees(Model model) {

        model.addAttribute(
                "employees",
                employeeService.getAllEmployees()
        );

        return "employees";
    }

    // Display create employee form
    @GetMapping("/employees/new")
    public String showCreateForm(Model model) {

        model.addAttribute("employee", new Employee());

        return "employee-form";
    }

    // Save or Update employee
    @PostMapping("/employees/save")
    public String saveEmployee(
            @Valid @ModelAttribute("employee") Employee employee,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        // Check duplicate email
        if (employeeService.emailExists(
                employee.getEmail(),
                employee.getId())) {

            result.rejectValue(
                    "email",
                    "duplicate",
                    "This email address is already registered."
            );
        }

        // Check validation errors
        if (result.hasErrors()) {
            return "employee-form";
        }

        // Check whether employee is new or existing
        boolean isUpdate = employee.getId() != null;

        // Save employee details
        employeeService.saveEmployee(employee);

        // Display success message
        if (isUpdate) {

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Employee updated successfully!"
            );

        } else {

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Employee saved successfully!"
            );
        }

        return "redirect:/";
    }

    // Display edit employee form
    @GetMapping("/employees/edit/{id}")
    public String showEditForm(
            @PathVariable Long id,
            Model model) {

        Employee employee = employeeService
                .getEmployeeById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid employee Id: " + id
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
