package BusinessLayer.Controller;

import BusinessLayer.Domain.Expense;
import BusinessLayer.Domain.IClock;
import BusinessLayer.Repository.IExpenseRepository;

import java.time.LocalDateTime;

/**
 * Orchestrates expense use cases.
 */
public class ExpenseController {
    private final IExpenseRepository expenseRepository;
    private final IClock clock;

    public ExpenseController(IExpenseRepository expenseRepository, IClock clock) {
        this.expenseRepository = expenseRepository;
        this.clock = clock;
    }

    public Expense recordExpense(int propertyID, double amount, String category) {
        Expense expense = new Expense(0, propertyID, amount, category, clock.getCurrentDate());
        expenseRepository.save(expense);
        return expense;
    }

    public Expense recordExpense(int propertyID, double amount, String category, LocalDateTime date) {
        Expense expense = new Expense(0, propertyID, amount, category, date);
        expenseRepository.save(expense);
        return expense;
    }

}
