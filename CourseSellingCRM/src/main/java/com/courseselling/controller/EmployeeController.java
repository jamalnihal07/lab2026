package com.courseselling.controller;

import com.courseselling.model.Employee;
import com.courseselling.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling employee CRM panel requests.
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/dashboard")
    public String employeeDashboard() {
        return "employee/dashboard";
    }

    @GetMapping("/interactions")
    public String viewInteractions(Model model) {
        // TODO: Fetch interactions for current employee
        return "employee/interactions";
    }

    @GetMapping("/interactions/new")
    public String newInteraction(Model model) {
        // TODO: Show form to log new interaction
        return "employee/interaction-form";
    }

    @PostMapping("/interactions/save")
    public String saveInteraction() {
        // TODO: Save interaction
        return "redirect:/employee/interactions";
    }

    @GetMapping("/customers")
    public String viewCustomers(Model model) {
        // TODO: Fetch customer list
        return "employee/customers";
    }

    @GetMapping("/sales")
    public String viewSales(Model model) {
        // TODO: Fetch sales for employee
        return "employee/sales";
    }

    @GetMapping("/orders/new")
    public String newOrder(Model model) {
        // TODO: Show order creation form
        return "employee/order-form";
    }

    @PostMapping("/orders/save")
    public String saveOrder() {
        // TODO: Save order
        return "redirect:/employee/sales";
    }

    @GetMapping("/performance")
    public String viewPerformance(Model model) {
        // TODO: Fetch performance metrics
        return "employee/performance";
    }
}
