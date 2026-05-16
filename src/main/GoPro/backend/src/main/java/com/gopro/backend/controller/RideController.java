package com.gopro.backend.controller;

import com.gopro.backend.model.Driver;
import com.gopro.backend.model.Ride;
import com.gopro.backend.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RideController {
    @Autowired
    private DataService dataService;

    @GetMapping("/rides")
    public ResponseEntity<?> getAllRides() {
        return ResponseEntity.ok(dataService.getRides());
    }

    @GetMapping("/rides/active")
    public ResponseEntity<?> getActiveRides() {
        List<Ride> list = dataService.getRides();
        List<Ride> active = new ArrayList<>();
        for (Ride r : list) {
            if ("active".equals(r.status) || "in-progress".equals(r.status)) active.add(r);
        }
        return ResponseEntity.ok(active);
    }

    @PostMapping("/rides")
    public ResponseEntity<?> createRide(@RequestBody Map<String, Object> body) {
        List<Ride> rides = dataService.getRides();
        Ride ride = new Ride();
        ride.id = "R" + (int) (Math.random() * 100000);
        ride.userId = (String) body.getOrDefault("userId", null);
        ride.driverId = (String) body.getOrDefault("driverId", null);
        ride.pickup = (String) body.getOrDefault("pickup", "Unknown pickup");
        ride.dropoff = (String) body.getOrDefault("dropoff", "Unknown dropoff");
        ride.distance = body.get("distance") == null ? 0.0 : Double.parseDouble(body.get("distance").toString());
        ride.fare = body.get("fare") == null ? 0 : (int) Double.parseDouble(body.get("fare").toString());
        ride.status = "active";
        ride.date = Instant.now().toString();
        ride.passengerName = (String) body.getOrDefault("passengerName", "Guest");
        rides.add(0, ride);
        dataService.saveRides(rides);
        return ResponseEntity.ok(ride);
    }

    @PostMapping("/rides/{id}/start")
    public ResponseEntity<?> startRide(@PathVariable String id) {
        List<Ride> rides = dataService.getRides();
        for (Ride r : rides) {
            if (r.id != null && r.id.equals(id)) {
                r.status = "in-progress";
                dataService.saveRides(rides);
                return ResponseEntity.ok(r);
            }
        }
        return ResponseEntity.status(404).body(Map.of("error", "Ride not found"));
    }

    @PostMapping("/rides/{id}/complete")
    public ResponseEntity<?> completeRide(@PathVariable String id) {
        List<Ride> rides = dataService.getRides();
        List<Driver> drivers = dataService.getDrivers();
        for (Ride r : rides) {
            if (r.id != null && r.id.equals(id)) {
                r.status = "completed";
                r.completedAt = Instant.now().toString();
                // update driver earnings
                if (r.driverId != null) {
                    for (Driver d : drivers) {
                        if (d.id != null && d.id.equals(r.driverId)) {
                            if (d.earnings == null) d.earnings = new java.util.HashMap<>();
                            int fare = r.fare == null ? 0 : r.fare;
                            d.earnings.put("today", d.earnings.getOrDefault("today", 0) + fare);
                            d.earnings.put("week", d.earnings.getOrDefault("week", 0) + fare);
                            d.earnings.put("month", d.earnings.getOrDefault("month", 0) + fare);
                            d.earnings.put("total", d.earnings.getOrDefault("total", 0) + fare);
                            d.totalRides = (d.totalRides == null ? 0 : d.totalRides) + 1;
                        }
                    }
                    dataService.saveDrivers(drivers);
                }
                dataService.saveRides(rides);
                return ResponseEntity.ok(r);
            }
        }
        return ResponseEntity.status(404).body(Map.of("error", "Ride not found"));
    }
}
