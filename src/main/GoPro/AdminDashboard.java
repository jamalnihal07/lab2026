import java.util.*;

// Module 15: Admin Dashboard
public class AdminDashboard {
    private String adminId;
    private String adminName;

    public AdminDashboard(String adminId, String adminName) {
        this.adminId = adminId;
        this.adminName = adminName;
    }

    public void showDashboard(int totalUsers, int totalDrivers, int totalRides, int activeRides, double totalRevenue) {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║         GoPro ADMIN DASHBOARD                   ║");
        System.out.println("║         Admin: " + padRight(adminName, 34) + "║");
        System.out.println("╠══════════════════════════════════════════════════╣");
        System.out.println("║  Total Users       : " + padRight(String.valueOf(totalUsers), 28) + "║");
        System.out.println("║  Total Drivers     : " + padRight(String.valueOf(totalDrivers), 28) + "║");
        System.out.println("║  Total Rides       : " + padRight(String.valueOf(totalRides), 28) + "║");
        System.out.println("║  Active Rides      : " + padRight(String.valueOf(activeRides), 28) + "║");
        System.out.println("║  Total Revenue     : " + padRight("Rs." + String.format("%.2f", totalRevenue), 28) + "║");
        System.out.println("╚══════════════════════════════════════════════════╝");
    }

    public void viewAllDrivers(List<Driver> drivers) {
        System.out.println("[GoPro Admin] ===== All Drivers (" + drivers.size() + ") =====");
        for (Driver d : drivers) {
            System.out.println("  " + d);
        }
    }

    public void viewAllUsers(List<User> users) {
        System.out.println("[GoPro Admin] ===== All Users (" + users.size() + ") =====");
        for (User u : users) {
            System.out.println("  " + u);
        }
    }

    public void blockDriver(Driver driver, String reason) {
        driver.goOffline();
        System.out.println("[GoPro Admin] Driver " + driver.getName() + " BLOCKED. Reason: " + reason);
    }

    public void approveDriver(Driver driver) {
        driver.verifyDriver();
        System.out.println("[GoPro Admin] Driver " + driver.getName() + " APPROVED.");
    }

    public void generateRevenueReport(double dailyRevenue, double weeklyRevenue, double monthlyRevenue) {
        System.out.println("[GoPro Admin] ===== Revenue Report =====");
        System.out.println("  Today  : Rs." + String.format("%.2f", dailyRevenue));
        System.out.println("  Week   : Rs." + String.format("%.2f", weeklyRevenue));
        System.out.println("  Month  : Rs." + String.format("%.2f", monthlyRevenue));
    }

    private String padRight(String s, int length) {
        if (s.length() >= length) return s.substring(0, length);
        return s + " ".repeat(length - s.length());
    }
}
