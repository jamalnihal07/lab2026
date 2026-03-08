import java.util.*;

// Module 5: Ride Matching - Matches riders with nearest available drivers
public class RideMatching {
    private List<Driver> availableDrivers;

    public RideMatching() {
        this.availableDrivers = new ArrayList<>();
    }

    public void addDriver(Driver driver) {
        if (driver.isAvailable() && driver.isVerified()) {
            availableDrivers.add(driver);
            System.out.println("[GoPro] Driver " + driver.getName() + " added to available pool.");
        }
    }

    public void removeDriver(Driver driver) {
        availableDrivers.remove(driver);
        System.out.println("[GoPro] Driver " + driver.getName() + " removed from available pool.");
    }

    public Driver findNearestDriver(String rideType, String location) {
        System.out.println("[GoPro] Searching for " + rideType + " driver near " + location + "...");
        Driver bestMatch = null;
        double bestRating = 0;

        for (Driver driver : availableDrivers) {
            if (driver.isAvailable() && driver.getRating() > bestRating) {
                bestMatch = driver;
                bestRating = driver.getRating();
            }
        }

        if (bestMatch != null) {
            System.out.println("[GoPro] Driver found: " + bestMatch.getName() + " (Rating: " + String.format("%.1f", bestMatch.getRating()) + ")");
        } else {
            System.out.println("[GoPro] No drivers available nearby. Please try again.");
        }
        return bestMatch;
    }

    public int getAvailableDriverCount() {
        return availableDrivers.size();
    }

    public void displayAvailableDrivers() {
        System.out.println("[GoPro] Available Drivers (" + availableDrivers.size() + "):");
        for (Driver d : availableDrivers) {
            System.out.println("  - " + d);
        }
    }
}
