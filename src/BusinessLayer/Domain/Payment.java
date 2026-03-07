package BusinessLayer.Domain;

import java.time.LocalDateTime;

/**
 * Payment domain entity.
 */
public class Payment {
    private int paymentID;
    private int leaseID;
    private double amount;
    private LocalDateTime paymentDate;
    private String paymentMethod;

    public Payment() {
    }

    public Payment(int paymentID, int leaseID, double amount, LocalDateTime paymentDate, String paymentMethod) {
        this.paymentID = paymentID;
        this.leaseID = leaseID;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
    }

    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public int getLeaseID() {
        return leaseID;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

}