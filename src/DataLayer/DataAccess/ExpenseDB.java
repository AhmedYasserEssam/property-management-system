package DataLayer.DataAccess;

import BusinessLayer.Domain.Expense;
import BusinessLayer.Repository.IExpenseRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Optional;

/**
 * Expense data access implementation.
 */
public class ExpenseDB extends BaseRepository<Expense> implements IExpenseRepository {
    private final IDbConnectionProvider connectionProvider;

    public ExpenseDB() {
        this(SqlServerConnectionManager.getInstance());
    }

    public ExpenseDB(IDbConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    protected int getEntityID(Expense expense) {
        return expense.getExpenseID();
    }

    @Override
    protected void insert(Expense expense) {
        String sql = "INSERT INTO Expenses (propertyID, amount, category, expenseDate) VALUES (?, ?, ?, ?)";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, expense.getPropertyID());
            ps.setDouble(2, expense.getAmount());
            ps.setString(3, expense.getCategory());
            ps.setTimestamp(4, Timestamp.valueOf(expense.getDate()));
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    expense.setExpenseID(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert expense", e);
        }
    }

    @Override
    protected void update(Expense expense) {
        String sql = "UPDATE Expenses SET propertyID = ?, amount = ?, category = ?, expenseDate = ? WHERE expenseID = ?";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, expense.getPropertyID());
            ps.setDouble(2, expense.getAmount());
            ps.setString(3, expense.getCategory());
            ps.setTimestamp(4, Timestamp.valueOf(expense.getDate()));
            ps.setInt(5, expense.getExpenseID());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update expense", e);
        }
    }

    @Override
    public Optional<Expense> findByID(int expenseID) {
        String sql = "SELECT expenseID, propertyID, amount, category, expenseDate FROM Expenses WHERE expenseID = ?";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, expenseID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find expense", e);
        }
        return Optional.empty();
    }

    private Expense mapRow(ResultSet rs) throws SQLException {
        return new Expense(
                rs.getInt("expenseID"),
                rs.getInt("propertyID"),
                rs.getDouble("amount"),
                rs.getString("category"),
                rs.getTimestamp("expenseDate").toLocalDateTime()
        );
    }
}
