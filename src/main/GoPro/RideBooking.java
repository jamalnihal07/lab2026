import java.time.LocalDateTime;

// Module 4: Ride Booking
public class RideBooking {
    private String bookingId;
    private String userId;
    private String driverId;
    private String pickup;
    private String destination;
    private String rideType; // BIKE, AUTO, MINI, SEDAN, SUV
    private String status;   // REQUESTED, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED
    private LocalDateTime bookingTime;
    private double fare;

    public RideBooking(String bookingId, String userId, String pickup, String destination, String rideType) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.pickup = pickup;
        this.destination = destination;
        this.rideType = rideType;
        this.status = "REQUESTED";
        this.bookingTime = LocalDateTime.now();
        this.driverId = null;
        this.fare = 0.0;
        System.out.println("[GoPro] Ride booked! ID: " + bookingId + " | " + pickup + " -> " + destination + " | Type: " + rideType);
    }

    public void confirmRide(String driverId, double fare) {
        this.driverId = driverId;
        this.fare = fare;
        this.status = "CONFIRMED";
        System.out.println("[GoPro] Ride " + bookingId + " CONFIRMED. Driver: " + driverId + " | Fare: Rs." + fare);
    }

    public void startRide() {
        if ("CONFIRMED".equals(status)) {
            this.status = "IN_PROGRESS";
            System.out.println("[GoPro] Ride " + bookingId + " STARTED. " + pickup + " -> " + destination);
        } else {
            System.out.println("[GoPro] Cannot start ride. Current status: " + status);
        }
    }

    public void completeRide() {
        if ("IN_PROGRESS".equals(status)) {
            this.status = "COMPLETED";
            System.out.println("[GoPro] Ride " + bookingId + " COMPLETED. Fare: Rs." + fare);
        } else {
            System.out.println("[GoPro] Cannot complete ride. Current status: " + status);
        }
    }

    public void cancelRide(String reason) {
        if ("REQUESTED".equals(status) || "CONFIRMED".equals(status)) {
            this.status = "CANCELLED";
            System.out.println("[GoPro] Ride " + bookingId + " CANCELLED. Reason: " + reason);
        } else {
            System.out.println("[GoPro] Cannot cancel ride. Current status: " + status);
        }
    }

    // Getters
    public String getBookingId() { return bookingId; }
    public String getUserId() { return userId; }
    public String getDriverId() { return driverId; }
    public String getPickup() { return pickup; }
    public String getDestination() { return destination; }
    public String getRideType() { return rideType; }
    public String getStatus() { return status; }
    public LocalDateTime getBookingTime() { return bookingTime; }
    public double getFare() { return fare; }

    @Override
    public String toString() {
        return "RideBooking{id='" + bookingId + "', " + pickup + " -> " + destination + ", status='" + status + "', fare=Rs." + fare + "}";
    }
}
