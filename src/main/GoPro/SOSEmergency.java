import java.time.LocalDateTime;

// Module 14: SOS / Emergency Service
public class SOSEmergency {
    private String sosId;
    private String userId;
    private String bookingId;
    private String emergencyContact;
    private String location;
    private String status; // TRIGGERED, ACKNOWLEDGED, RESOLVED
    private LocalDateTime triggeredAt;

    public SOSEmergency(String userId, String emergencyContact) {
        this.userId = userId;
        this.emergencyContact = emergencyContact;
        this.status = "IDLE";
    }

    public void triggerSOS(String bookingId, String location) {
        this.sosId = "SOS" + System.currentTimeMillis();
        this.bookingId = bookingId;
        this.location = location;
        this.status = "TRIGGERED";
        this.triggeredAt = LocalDateTime.now();
        System.out.println("[GoPro] !!!! SOS TRIGGERED !!!!");
        System.out.println("[GoPro] User: " + userId + " | Ride: " + bookingId);
        System.out.println("[GoPro] Location: " + location);
        System.out.println("[GoPro] Emergency contact " + emergencyContact + " has been notified.");
        System.out.println("[GoPro] GoPro Safety Team alerted. Help is on the way!");
    }

    public void acknowledgeSOS() {
        if ("TRIGGERED".equals(status)) {
            this.status = "ACKNOWLEDGED";
            System.out.println("[GoPro] SOS " + sosId + " acknowledged by safety team.");
        }
    }

    public void resolveSOS(String resolution) {
        this.status = "RESOLVED";
        System.out.println("[GoPro] SOS " + sosId + " RESOLVED. " + resolution);
    }

    public void shareLocation(String bookingId, String location) {
        System.out.println("[GoPro] Live location shared with " + emergencyContact + " for ride " + bookingId);
        System.out.println("[GoPro] Current location: " + location);
    }

    public void updateEmergencyContact(String newContact) {
        this.emergencyContact = newContact;
        System.out.println("[GoPro] Emergency contact updated to " + newContact);
    }

    public String getStatus() { return status; }
    public String getEmergencyContact() { return emergencyContact; }

    @Override
    public String toString() {
        return "SOSEmergency{user='" + userId + "', status='" + status + "', contact='" + emergencyContact + "'}";
    }
}
