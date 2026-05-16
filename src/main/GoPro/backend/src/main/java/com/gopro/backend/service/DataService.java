package com.gopro.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gopro.backend.model.Driver;
import com.gopro.backend.model.Ride;

import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DataService {
    private static final Path DATA_PATH = Path.of("backend-data.json");
    private final ObjectMapper mapper = new ObjectMapper();

    public synchronized ObjectNode readAll() {
        try {
            if (!Files.exists(DATA_PATH)) {
                // create with empty structure
                ObjectNode root = mapper.createObjectNode();
                root.putArray("users");
                root.putArray("drivers");
                root.putArray("rides");
                root.putArray("admins");
                mapper.writerWithDefaultPrettyPrinter().writeValue(DATA_PATH.toFile(), root);
                return root;
            }
            return (ObjectNode) mapper.readTree(DATA_PATH.toFile());
        } catch (Exception e) {
            throw new RuntimeException("Failed to read data file: " + e.getMessage(), e);
        }
    }

    public synchronized void writeAll(ObjectNode node) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(DATA_PATH.toFile(), node);
        } catch (Exception e) {
            throw new RuntimeException("Failed to write data file: " + e.getMessage(), e);
        }
    }

    // helpers
    public synchronized List<Driver> getDrivers() {
        try {
            ObjectNode root = readAll();
            List<Driver> list = new ArrayList<>();
            if (root.has("drivers")) {
                list = mapper.convertValue(root.get("drivers"), mapper.getTypeFactory().constructCollectionType(List.class, Driver.class));
            }
            return list;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public synchronized List<Ride> getRides() {
        try {
            ObjectNode root = readAll();
            List<Ride> list = new ArrayList<>();
            if (root.has("rides")) {
                list = mapper.convertValue(root.get("rides"), mapper.getTypeFactory().constructCollectionType(List.class, Ride.class));
            }
            return list;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public synchronized void saveRides(List<Ride> rides) {
        try {
            ObjectNode root = readAll();
            root.set("rides", mapper.valueToTree(rides));
            writeAll(root);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void saveDrivers(List<Driver> drivers) {
        try {
            ObjectNode root = readAll();
            root.set("drivers", mapper.valueToTree(drivers));
            writeAll(root);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
