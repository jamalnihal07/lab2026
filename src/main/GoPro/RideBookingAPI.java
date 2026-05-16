import java.time.LocalDateTime;
import java.util.*;

// REST API Backend for Ride Booking
public class RideBookingAPI {
    private static final Map<String, RideBooking> activeRides = new HashMap<>();
    private static final List<String> rideHistory = new ArrayList<>();
    
    // Vehicle types with pricing
    private static final Map<String, Double> VEHICLE_RATES = new HashMap<String, Double>() {{
        put("BIKE", 5.0);      // Rs per km
        put("AUTO", 8.0);
        put("MINI", 10.0);
        put("SEDAN", 12.0);
        put("SUV", 15.0);
    }};

    private static final Map<String, Integer> VEHICLE_CAPACITY = new HashMap<String, Integer>() {{
        put("BIKE", 1);
        put("AUTO", 3);
        put("MINI", 4);
        put("SEDAN", 5);
        put("SUV", 7);
    }};

    // API: Get available vehicles at a location
    public static List<Map<String, Object>> getAvailableVehicles(String pickupLocation, double latitude, double longitude) {
        List<Map<String, Object>> vehicles = new ArrayList<>();
        
        for (String vehicleType : VEHICLE_RATES.keySet()) {
            Map<String, Object> vehicle = new HashMap<>();
            vehicle.put("type", vehicleType);
            vehicle.put("capacity", VEHICLE_CAPACITY.get(vehicleType));
            vehicle.put("ratePerKm", VEHICLE_RATES.get(vehicleType));
            vehicle.put("availableCount", (int)(Math.random() * 10) + 2);
            vehicle.put("eta", (int)(Math.random() * 8) + 2); // ETA in minutes
            vehicle.put("icon", getVehicleIcon(vehicleType));
            vehicles.add(vehicle);
        }
        
        System.out.println("[GoPro API] Available vehicles at " + pickupLocation + ": " + vehicles.size() + " types");
        return vehicles;
    }

    // API: Calculate fare estimate
    public static Map<String, Object> calculateFareEstimate(String vehicleType, double distance, int estimatedTime) {
        Map<String, Object> estimate = new HashMap<>();
        
        if (!VEHICLE_RATES.containsKey(vehicleType)) {
            estimate.put("error", "Invalid vehicle type");
            return estimate;
        }
        
        double ratePerKm = VEHICLE_RATES.get(vehicleType);
        double ratePerMin = 0.5; // Rs per minute waiting time
        
        double fareAmount = (distance * ratePerKm) + (estimatedTime * ratePerMin);
        double surgePricing = calculateSurgeMultiplier(); // Dynamic pricing
        double finalFare = fareAmount * surgePricing;
        
        estimate.put("vehicleType", vehicleType);
        estimate.put("distance", distance);
        estimate.put("duration", estimatedTime);
        estimate.put("baseFare", Math.round(fareAmount * 100.0) / 100.0);
        estimate.put("surgePricing", Math.round(surgePricing * 100.0) / 100.0);
        estimate.put("finalFare", Math.round(finalFare * 100.0) / 100.0);
        estimate.put("estimatedArrival", estimatedTime);
        
        System.out.println("[GoPro API] Fare estimate: " + vehicleType + " - Rs." + estimate.get("finalFare"));
        return estimate;
    }

    // API: Book a ride
    public static Map<String, Object> bookRide(String userId, String vehicleType, 
                                                 String pickupLoc, String dropoffLoc,
                                                 double pickupLat, double pickupLng,
                                                 double dropoffLat, double dropoffLng,
                                                 double fare) {
        Map<String, Object> response = new HashMap<>();
        
        String rideId = "RIDE" + System.currentTimeMillis();
        RideBooking booking = new RideBooking(rideId, userId, pickupLoc, dropoffLoc, vehicleType);
        booking.confirmRide(assignDriver(), fare);
        
        activeRides.put(rideId, booking);
        rideHistory.add(rideId);
        
        response.put("status", "success");
        response.put("rideId", rideId);
        response.put("userId", userId);
        response.put("vehicleType", vehicleType);
        response.put("pickupLocation", pickupLoc);
        response.put("dropoffLocation", dropoffLoc);
        response.put("fare", fare);
        response.put("bookingTime", LocalDateTime.now().toString());
        response.put("driverId", booking.getDriverId());
        response.put("eta", 5); // ETA in minutes
        
        System.out.println("[GoPro API] Ride booked: " + rideId + " for user " + userId);
        return response;
    }

    // API: Apply coupon code
    public static Map<String, Object> applyCoupon(String couponCode, double fareAmount) {
        Map<String, Object> response = new HashMap<>();
        
        // Sample coupon validation
        if ("SAVE50".equals(couponCode)) {
            double discount = Math.min(50, fareAmount * 0.10);
            response.put("valid", true);
            response.put("code", couponCode);
            response.put("discount", Math.round(discount * 100.0) / 100.0);
            response.put("newFare", Math.round((fareAmount - discount) * 100.0) / 100.0);
            System.out.println("[GoPro API] Coupon applied: " + couponCode + " | Discount: Rs." + discount);
        } else if ("RIDE25".equals(couponCode)) {
            double discount = Math.min(25, fareAmount * 0.05);
            response.put("valid", true);
            response.put("code", couponCode);
            response.put("discount", Math.round(discount * 100.0) / 100.0);
            response.put("newFare", Math.round((fareAmount - discount) * 100.0) / 100.0);
        } else {
            response.put("valid", false);
            response.put("message", "Invalid or expired coupon code");
            System.out.println("[GoPro API] Invalid coupon: " + couponCode);
        }
        
        return response;
    }

    // API: Process payment
    public static Map<String, Object> processPayment(String rideId, String paymentMethod, double amount) {
        Map<String, Object> response = new HashMap<>();
        
        String paymentId = "PAY" + System.currentTimeMillis();
        Payment payment = new Payment(paymentId, rideId, "", amount, paymentMethod);
        
        if (payment.processPayment()) {
            response.put("status", "success");
            response.put("paymentId", paymentId);
            response.put("rideId", rideId);
            response.put("amount", amount);
            response.put("method", paymentMethod);
            response.put("timestamp", LocalDateTime.now().toString());
        } else {
            response.put("status", "failed");
            response.put("message", "Payment processing failed");
        }
        
        return response;
    }

    // API: Get ride status
    public static Map<String, Object> getRideStatus(String rideId) {
        Map<String, Object> status = new HashMap<>();
        
        if (activeRides.containsKey(rideId)) {
            RideBooking ride = activeRides.get(rideId);
            status.put("rideId", rideId);
            status.put("status", "in_progress");
            status.put("driverId", ride.getDriverId());
            status.put("driverName", "Anuj Singh");
            status.put("vehicleType", ride.getRideType());
            status.put("driverRating", 4.8);
            status.put("currentLocation", generateRandomLocation());
            status.put("eta", 8); // ETA in minutes
        } else {
            status.put("status", "not_found");
            status.put("message", "Ride not found");
        }
        
        return status;
    }

    // API: Cancel ride
    public static Map<String, Object> cancelRide(String rideId, String reason) {
        Map<String, Object> response = new HashMap<>();
        
        if (activeRides.containsKey(rideId)) {
            activeRides.remove(rideId);
            response.put("status", "cancelled");
            response.put("rideId", rideId);
            response.put("reason", reason);
            response.put("refund", "Your payment will be refunded within 24 hours");
            System.out.println("[GoPro API] Ride cancelled: " + rideId);
        } else {
            response.put("status", "error");
            response.put("message", "Ride not found");
        }
        
        return response;
    }

    // Helper methods
    private static double calculateSurgeMultiplier() {
        int hour = LocalDateTime.now().getHour();
        // Peak hours: 8-9 AM, 5-7 PM
        if ((hour >= 8 && hour <= 9) || (hour >= 17 && hour <= 19)) {
            return 1.5;
        }
        return 1.0;
    }

    private static String assignDriver() {
        return "D" + (int)(Math.random() * 1000);
    }

    private static String generateRandomLocation() {
        return Math.round(Math.random() * 100) / 100.0 + "," + Math.round(Math.random() * 100) / 100.0;
    }

    private static String getVehicleIcon(String type) {
        switch (type) {
            case "BIKE": return "🏍️";
            case "AUTO": return "🔶";
            case "MINI": return "🚗";
            case "SEDAN": return "🚙";
            case "SUV": return "🚐";
            default: return "🚗";
        }
    }

    // Get available coupons
    public static List<Map<String, String>> getAvailableCoupons() {
        List<Map<String, String>> coupons = new ArrayList<>();
        
        Map<String, String> coupon1 = new HashMap<>();
        coupon1.put("code", "SAVE50");
        coupon1.put("description", "Save up to Rs.50 on your first ride");
        coupon1.put("discount", "10% off");
        coupons.add(coupon1);
        
        Map<String, String> coupon2 = new HashMap<>();
        coupon2.put("code", "RIDE25");
        coupon2.put("description", "Save up to Rs.25 on rides");
        coupon2.put("discount", "5% off");
        coupons.add(coupon2);
        
        return coupons;
    }
}
