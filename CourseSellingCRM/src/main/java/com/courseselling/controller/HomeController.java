package com.courseselling.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for handling home and dashboard pages.
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/admin")
    public String adminDashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/employee")
    public String employeeDashboard() {
        return "employee/dashboard";
    }
}
