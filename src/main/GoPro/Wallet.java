import java.time.LocalDateTime;
import java.util.*;

// Module 8: Wallet Management
public class Wallet {
    private String walletId;
    private String userId;
    private double balance;
    private List<String> transactions;

    public Wallet(String walletId, String userId) {
        this.walletId = walletId;
        this.userId = userId;
        this.balance = 0.0;
        this.transactions = new ArrayList<>();
    }

    public void addMoney(double amount) {
        if (amount > 0) {
            this.balance += amount;
            String txn = "[+" + amount + "] Added to wallet | Balance: Rs." + balance + " | " + LocalDateTime.now();
            transactions.add(txn);
            System.out.println("[GoPro] Rs." + amount + " added to wallet. Balance: Rs." + balance);
        } else {
            System.out.println("[GoPro] Invalid amount.");
        }
    }

    public boolean deductMoney(double amount) {
        if (amount > 0 && balance >= amount) {
            this.balance -= amount;
            String txn = "[-" + amount + "] Deducted from wallet | Balance: Rs." + balance + " | " + LocalDateTime.now();
            transactions.add(txn);
            System.out.println("[GoPro] Rs." + amount + " deducted. Balance: Rs." + balance);
            return true;
        } else {
            System.out.println("[GoPro] Insufficient wallet balance. Current: Rs." + balance);
            return false;
        }
    }

    public double getBalance() {
        return balance;
    }

    public void showTransactionHistory() {
        System.out.println("[GoPro] ===== Wallet Transactions =====");
        if (transactions.isEmpty()) {
            System.out.println("  No transactions yet.");
        } else {
            for (String txn : transactions) {
                System.out.println("  " + txn);
            }
        }
        System.out.println("  Current Balance: Rs." + balance);
    }

    @Override
    public String toString() {
        return "Wallet{userId='" + userId + "', balance=Rs." + balance + "}";
    }
}
