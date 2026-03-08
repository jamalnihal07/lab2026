import java.time.LocalDateTime;

// Module 7: Payment Processing
public class Payment {
    private String paymentId;
    private String bookingId;
    private String userId;
    private double amount;
    private String paymentMethod; // CASH, UPI, CARD, WALLET
    private String status;        // PENDING, SUCCESS, FAILED, REFUNDED
    private LocalDateTime paymentTime;

    public Payment(String paymentId, String bookingId, String userId, double amount, String paymentMethod) {
        this.paymentId = paymentId;
        this.bookingId = bookingId;
        this.userId = userId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = "PENDING";
        this.paymentTime = LocalDateTime.now();
    }

    public boolean processPayment() {
        System.out.println("[GoPro] Processing " + paymentMethod + " payment of Rs." + amount + "...");
        // Simulate payment processing
        this.status = "SUCCESS";
        this.paymentTime = LocalDateTime.now();
        System.out.println("[GoPro] Payment " + paymentId + " SUCCESS via " + paymentMethod + " | Amount: Rs." + amount);
        return true;
    }

    public void refundPayment() {
        if ("SUCCESS".equals(status)) {
            this.status = "REFUNDED";
            System.out.println("[GoPro] Payment " + paymentId + " REFUNDED. Rs." + amount + " returned via " + paymentMethod);
        } else {
            System.out.println("[GoPro] Cannot refund. Payment status: " + status);
        }
    }

    public void failPayment(String reason) {
        this.status = "FAILED";
        System.out.println("[GoPro] Payment " + paymentId + " FAILED. Reason: " + reason);
    }

    // Getters
    public String getPaymentId() { return paymentId; }
    public String getBookingId() { return bookingId; }
    public double getAmount() { return amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return "Payment{id='" + paymentId + "', amount=Rs." + amount + ", method='" + paymentMethod + "', status='" + status + "'}";
    }
}
