package PresentationLayer.UI;

import BusinessLayer.Controller.ExpenseController;
import BusinessLayer.Domain.Expense;

/**
 * Bridge abstraction for expense forms.
 */
public class ExpenseFormUI {
    private final ExpenseController expenseController;

    public ExpenseFormUI(ExpenseController expenseController) {
        this.expenseController = expenseController;
    }

    public void submitRecordExpense() {
        throw new UnsupportedOperationException(
                "Use submitRecordExpense(propertyID, amount, category) to delegate to the bridge controller.");
    }

    public Expense submitRecordExpense(int propertyID, double amount, String category) {
        return expenseController.recordExpense(propertyID, amount, category);
    }

}