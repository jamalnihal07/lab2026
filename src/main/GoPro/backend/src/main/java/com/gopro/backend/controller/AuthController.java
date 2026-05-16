package com.gopro.backend.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gopro.backend.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private DataService dataService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        String type = body.get("type");

        ObjectNode root = dataService.readAll();

        if ("admin".equals(type)) {
            var admins = root.get("admins");
            if (admins != null) {
                for (var node : admins) {
                    if (node.get("email").asText().equals(email) && node.get("password").asText().equals(password)) {
                        ((ObjectNode) node).put("type", "admin");
                        return ResponseEntity.ok(node);
                    }
                }
            }
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        if ("driver".equals(type)) {
            var drivers = root.get("drivers");
            if (drivers != null) {
                for (var node : drivers) {
                    if (node.get("email").asText().equals(email) && node.get("password").asText().equals(password)) {
                        ((ObjectNode) node).put("type", "driver");
                        return ResponseEntity.ok(node);
                    }
                }
            }
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        var users = root.get("users");
        if (users != null) {
            for (var node : users) {
                if (node.get("email").asText().equals(email) && node.get("password").asText().equals(password)) {
                    ((ObjectNode) node).put("type", "rider");
                    return ResponseEntity.ok(node);
                }
            }
        }

        return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
    }
}
