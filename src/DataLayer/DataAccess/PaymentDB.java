package DataLayer.DataAccess;

import BusinessLayer.Domain.Payment;
import BusinessLayer.Repository.IPaymentRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Optional;

/**
 * Payment data access implementation.
 */
public class PaymentDB implements IPaymentRepository {
    private final IDbConnectionProvider connectionProvider;

    public PaymentDB() {
        this(SqlServerConnectionManager.getInstance());
    }

    public PaymentDB(IDbConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    public void save(Payment payment) {
        if (payment.getPaymentID() == 0) {
            insert(payment);
        } else {
            update(payment);
        }
    }

    private void insert(Payment payment) {
        String sql = "INSERT INTO Payments (leaseID, amount, paymentDate, paymentMethod) VALUES (?, ?, ?, ?)";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, payment.getLeaseID());
            ps.setDouble(2, payment.getAmount());
            ps.setTimestamp(3, Timestamp.valueOf(payment.getPaymentDate()));
            ps.setString(4, payment.getPaymentMethod());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    payment.setPaymentID(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert payment", e);
        }
    }

    private void update(Payment payment) {
        String sql = "UPDATE Payments SET leaseID = ?, amount = ?, paymentDate = ?, paymentMethod = ? WHERE paymentID = ?";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, payment.getLeaseID());
            ps.setDouble(2, payment.getAmount());
            ps.setTimestamp(3, Timestamp.valueOf(payment.getPaymentDate()));
            ps.setString(4, payment.getPaymentMethod());
            ps.setInt(5, payment.getPaymentID());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update payment", e);
        }
    }

    @Override
    public Optional<Payment> findByID(int paymentID) {
        String sql = "SELECT paymentID, leaseID, amount, paymentDate, paymentMethod FROM Payments WHERE paymentID = ?";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, paymentID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find payment", e);
        }
        return Optional.empty();
    }

    private Payment mapRow(ResultSet rs) throws SQLException {
        return new Payment(
                rs.getInt("paymentID"),
                rs.getInt("leaseID"),
                rs.getDouble("amount"),
                rs.getTimestamp("paymentDate").toLocalDateTime(),
                rs.getString("paymentMethod")
        );
    }
}
