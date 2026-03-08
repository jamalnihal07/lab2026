public class Driver {
    private String driverId;
    private String name;
    private String phone;
    private String licenseNumber;
    private boolean isAvailable;
    private boolean isVerified;
    private double rating;
    private int totalTrips;
    private String currentLocation;

    public Driver(String driverId, String name, String phone, String licenseNumber) {
        this.driverId = driverId;
        this.name = name;
        this.phone = phone;
        this.licenseNumber = licenseNumber;
        this.isAvailable = false;
        this.isVerified = false;
        this.rating = 5.0;
        this.totalTrips = 0;
        this.currentLocation = "Unknown";
    }

    public void verifyDriver() {
        if (licenseNumber != null && !licenseNumber.isEmpty()) {
            this.isVerified = true;
            System.out.println("[GoPro] Driver " + name + " verified successfully.");
        } else {
            System.out.println("[GoPro] Driver verification failed. Invalid license.");
        }
    }

    public void goOnline(String location) {
        if (isVerified) {
            this.isAvailable = true;
            this.currentLocation = location;
            System.out.println("[GoPro] Driver " + name + " is now ONLINE at " + location);
        } else {
            System.out.println("[GoPro] Driver not verified. Cannot go online.");
        }
    }

    public void goOffline() {
        this.isAvailable = false;
        System.out.println("[GoPro] Driver " + name + " is now OFFLINE.");
    }

    public void updateRating(double newRating) {
        this.rating = ((this.rating * totalTrips) + newRating) / (totalTrips + 1);
        this.totalTrips++;
        System.out.println("[GoPro] Driver " + name + " rating updated to " + String.format("%.1f", rating));
    }

    public void updateLocation(String location) {
        this.currentLocation = location;
    }

    // Getters
    public String getDriverId() { return driverId; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public boolean isAvailable() { return isAvailable; }
    public boolean isVerified() { return isVerified; }
    public double getRating() { return rating; }
    public int getTotalTrips() { return totalTrips; }
    public String getCurrentLocation() { return currentLocation; }

    @Override
    public String toString() {
        return "Driver{name='" + name + "', available=" + isAvailable + ", rating=" + String.format("%.1f", rating) + ", trips=" + totalTrips + "}";
    }
}
