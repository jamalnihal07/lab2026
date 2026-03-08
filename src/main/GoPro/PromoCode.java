import java.time.LocalDateTime;
import java.util.*;

// Module 13: Promo Code / Coupon Management
public class PromoCode {
    private String code;
    private int discountPercent;
    private double maxDiscount;
    private double minOrderAmount;
    private int usageLimit;
    private int timesUsed;
    private boolean isActive;
    private LocalDateTime expiryDate;

    // Store all promo codes
    private static List<PromoCode> allPromoCodes = new ArrayList<>();

    public PromoCode(String code, int discountPercent, double maxDiscount, double minOrderAmount, int usageLimit, LocalDateTime expiryDate) {
        this.code = code;
        this.discountPercent = discountPercent;
        this.maxDiscount = maxDiscount;
        this.minOrderAmount = minOrderAmount;
        this.usageLimit = usageLimit;
        this.timesUsed = 0;
        this.isActive = true;
        this.expiryDate = expiryDate;
    }

    public void addPromo() {
        allPromoCodes.add(this);
        System.out.println("[GoPro] Promo code '" + code + "' created: " + discountPercent + "% off (max Rs." + maxDiscount + ")");
    }

    public double applyPromo(double fareAmount) {
        if (!isActive) {
            System.out.println("[GoPro] Promo code '" + code + "' is not active.");
            return 0;
        }
        if (timesUsed >= usageLimit) {
            System.out.println("[GoPro] Promo code '" + code + "' usage limit reached.");
            return 0;
        }
        if (fareAmount < minOrderAmount) {
            System.out.println("[GoPro] Minimum order Rs." + minOrderAmount + " required for code '" + code + "'.");
            return 0;
        }
        if (LocalDateTime.now().isAfter(expiryDate)) {
            System.out.println("[GoPro] Promo code '" + code + "' has expired.");
            return 0;
        }

        double discount = (fareAmount * discountPercent) / 100;
        discount = Math.min(discount, maxDiscount);
        timesUsed++;
        System.out.println("[GoPro] Promo '" + code + "' applied! Discount: Rs." + String.format("%.2f", discount)
                + " | New fare: Rs." + String.format("%.2f", (fareAmount - discount)));
        return discount;
    }

    public void deactivate() {
        this.isActive = false;
        System.out.println("[GoPro] Promo code '" + code + "' deactivated.");
    }

    public static void showAllPromos() {
        System.out.println("[GoPro] ===== Available Promo Codes =====");
        for (PromoCode p : allPromoCodes) {
            if (p.isActive) {
                System.out.println("  " + p.code + " - " + p.discountPercent + "% off (max Rs." + p.maxDiscount
                        + ") | Min: Rs." + p.minOrderAmount + " | Used: " + p.timesUsed + "/" + p.usageLimit);
            }
        }
    }

    // Getters
    public String getCode() { return code; }
    public int getDiscountPercent() { return discountPercent; }
    public boolean isActive() { return isActive; }

    @Override
    public String toString() {
        return "PromoCode{'" + code + "', " + discountPercent + "% off, active=" + isActive + "}";
    }
}
