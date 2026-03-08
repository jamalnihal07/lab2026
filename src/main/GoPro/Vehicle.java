public class Vehicle {
    private String vehicleId;
    private String driverId;
    private String vehicleType; // BIKE, AUTO, MINI, SEDAN, SUV
    private String vehicleNumber;
    private String model;
    private String color;
    private boolean isActive;

    public Vehicle(String vehicleId, String driverId, String vehicleType, String vehicleNumber, String model, String color) {
        this.vehicleId = vehicleId;
        this.driverId = driverId;
        this.vehicleType = vehicleType;
        this.vehicleNumber = vehicleNumber;
        this.model = model;
        this.color = color;
        this.isActive = true;
    }

    public void activateVehicle() {
        this.isActive = true;
        System.out.println("[GoPro] Vehicle " + vehicleNumber + " (" + model + ") activated.");
    }

    public void deactivateVehicle() {
        this.isActive = false;
        System.out.println("[GoPro] Vehicle " + vehicleNumber + " deactivated.");
    }

    public void updateVehicleDetails(String model, String color) {
        this.model = model;
        this.color = color;
        System.out.println("[GoPro] Vehicle details updated: " + model + " - " + color);
    }

    // Getters
    public String getVehicleId() { return vehicleId; }
    public String getDriverId() { return driverId; }
    public String getVehicleType() { return vehicleType; }
    public String getVehicleNumber() { return vehicleNumber; }
    public String getModel() { return model; }
    public String getColor() { return color; }
    public boolean isActive() { return isActive; }

    @Override
    public String toString() {
        return "Vehicle{type='" + vehicleType + "', number='" + vehicleNumber + "', model='" + model + "', color='" + color + "'}";
    }
}
