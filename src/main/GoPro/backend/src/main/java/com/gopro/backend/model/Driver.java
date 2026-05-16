package com.gopro.backend.model;

import java.util.Map;

public class Driver {
    public String id;
    public String name;
    public String email;
    public String phone;
    public String password;
    public String type;
    public Integer totalRides;
    public Double avgRating;
    public Map<String, Object> vehicle;
    public Map<String, Integer> earnings;
    public Boolean verified;
    public String status;
}
