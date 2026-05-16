package com.gopro.backend.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gopro.backend.model.Driver;
import com.gopro.backend.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DriverController {
    @Autowired
    private DataService dataService;

    @GetMapping("/driver/{id}")
    public ResponseEntity<?> getDriver(@PathVariable String id) {
        List<Driver> drivers = dataService.getDrivers();
        for (Driver d : drivers) {
            if (d.id != null && d.id.equals(id)) return ResponseEntity.ok(d);
        }
        return ResponseEntity.status(404).body(Map.of("error", "Driver not found"));
    }

    @PostMapping("/driver/{id}/status")
    public ResponseEntity<?> setStatus(@PathVariable String id, @RequestBody Map<String, String> body) {
        String status = body.get("status");
        List<Driver> drivers = dataService.getDrivers();
        boolean updated = false;
        for (Driver d : drivers) {
            if (d.id != null && d.id.equals(id)) {
                d.status = status;
                updated = true;
                break;
            }
        }
        if (updated) {
            dataService.saveDrivers(drivers);
            return ResponseEntity.ok(Map.of("id", id, "status", status));
        }
        return ResponseEntity.status(404).body(Map.of("error", "Driver not found"));
    }
}
