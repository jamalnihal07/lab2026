import java.util.*;

// Module 11: Ride History
public class RideHistory {
    private List<RideBooking> rideRecords;

    public RideHistory() {
        this.rideRecords = new ArrayList<>();
    }

    public void addRide(RideBooking ride) {
        rideRecords.add(ride);
        System.out.println("[GoPro] Ride " + ride.getBookingId() + " added to history.");
    }

    public void showAllRides() {
        System.out.println("[GoPro] ===== Ride History (" + rideRecords.size() + " rides) =====");
        if (rideRecords.isEmpty()) {
            System.out.println("  No rides yet.");
        } else {
            for (RideBooking ride : rideRecords) {
                System.out.println("  " + ride);
            }
        }
    }

    public void showRidesByStatus(String status) {
        System.out.println("[GoPro] ===== " + status + " Rides =====");
        for (RideBooking ride : rideRecords) {
            if (ride.getStatus().equals(status)) {
                System.out.println("  " + ride);
            }
        }
    }

    public double getTotalSpent() {
        double total = 0;
        for (RideBooking ride : rideRecords) {
            if ("COMPLETED".equals(ride.getStatus())) {
                total += ride.getFare();
            }
        }
        System.out.println("[GoPro] Total spent: Rs." + String.format("%.2f", total));
        return total;
    }

    public int getTotalRides() {
        return rideRecords.size();
    }

    public int getCompletedRides() {
        int count = 0;
        for (RideBooking ride : rideRecords) {
            if ("COMPLETED".equals(ride.getStatus())) count++;
        }
        return count;
    }
}
