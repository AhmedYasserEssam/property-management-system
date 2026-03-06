package BusinessLayer.Domain;

import java.time.LocalDateTime;

/**
 * Expense domain entity.
 */
public class Expense {
    private int expenseID;
    private int propertyID;
    private double amount;
    private String category;
    private LocalDateTime date;

    public Expense() {
    }

    public Expense(int expenseID, int propertyID, double amount, String category, LocalDateTime date) {
        this.expenseID = expenseID;
        this.propertyID = propertyID;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public int getExpenseID() {
        return expenseID;
    }

    public void setExpenseID(int expenseID) {
        this.expenseID = expenseID;
    }

    public int getPropertyID() {
        return propertyID;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void recategorize(String category) {
        this.category = category;
    }

}