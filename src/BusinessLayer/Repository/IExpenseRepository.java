package BusinessLayer.Repository;

import BusinessLayer.Domain.Expense;

import java.util.Optional;

public interface IExpenseRepository {
    void save(Expense expense);

    Optional<Expense> findByID(int expenseID);
}
