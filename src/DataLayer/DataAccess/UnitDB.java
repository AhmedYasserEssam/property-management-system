package DataLayer.DataAccess;

import BusinessLayer.Domain.Unit;
import BusinessLayer.Repository.IUnitRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/**
 * Unit data access implementation.
 */
public class UnitDB implements IUnitRepository {
    private final IDbConnectionProvider connectionProvider;

    public UnitDB() {
        this(SqlServerConnectionManager.getInstance());
    }

    public UnitDB(IDbConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    public void save(Unit unit) {
        if (unit.getUnitID() == 0) {
            insert(unit);
        } else {
            update(unit);
        }
    }

    private void insert(Unit unit) {
        String sql = "INSERT INTO Units (unitNumber, rentalPrice, area, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, unit.getUnitNumber());
            ps.setDouble(2, unit.getRentalPrice());
            ps.setDouble(3, unit.getArea());
            ps.setString(4, unit.getStatus());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    unit.setUnitID(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert unit", e);
        }
    }

    private void update(Unit unit) {
        String sql = "UPDATE Units SET unitNumber = ?, rentalPrice = ?, area = ?, status = ? WHERE unitID = ?";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, unit.getUnitNumber());
            ps.setDouble(2, unit.getRentalPrice());
            ps.setDouble(3, unit.getArea());
            ps.setString(4, unit.getStatus());
            ps.setInt(5, unit.getUnitID());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update unit", e);
        }
    }

    @Override
    public Optional<Unit> findByID(int id) {
        String sql = "SELECT unitID, unitNumber, rentalPrice, area, status FROM Units WHERE unitID = ?";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find unit", e);
        }
        return Optional.empty();
    }

    private Unit mapRow(ResultSet rs) throws SQLException {
        return new Unit(
                rs.getInt("unitID"),
                rs.getString("unitNumber"),
                rs.getDouble("rentalPrice"),
                rs.getDouble("area"),
                rs.getString("status")
        );
    }
}
