package DataLayer.DataAccess;

import BusinessLayer.Domain.MaintenanceRequest;
import BusinessLayer.Repository.IMaintenanceRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Optional;

/**
 * Maintenance data access implementation.
 */
public class MaintenanceDB implements IMaintenanceRepository {
    private final IDbConnectionProvider connectionProvider;

    public MaintenanceDB() {
        this(SqlServerConnectionManager.getInstance());
    }

    public MaintenanceDB(IDbConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    public void save(MaintenanceRequest request) {
        if (request.getRequestID() == 0) {
            insert(request);
        } else {
            updateAll(request);
        }
    }

    private void insert(MaintenanceRequest request) {
        String sql = "INSERT INTO MaintenanceRequests (unitID, issueDescription, requestDate, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, request.getUnitID());
            ps.setString(2, request.getIssueDescription());
            ps.setTimestamp(3, Timestamp.valueOf(request.getRequestDate()));
            ps.setString(4, request.getStatus());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    request.setRequestID(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert maintenance request", e);
        }
    }

    private void updateAll(MaintenanceRequest request) {
        String sql = "UPDATE MaintenanceRequests SET unitID = ?, issueDescription = ?, requestDate = ?, status = ? WHERE requestID = ?";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, request.getUnitID());
            ps.setString(2, request.getIssueDescription());
            ps.setTimestamp(3, Timestamp.valueOf(request.getRequestDate()));
            ps.setString(4, request.getStatus());
            ps.setInt(5, request.getRequestID());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update maintenance request", e);
        }
    }

    @Override
    public void update(MaintenanceRequest request) {
        updateAll(request);
    }

    @Override
    public Optional<MaintenanceRequest> findByID(int requestID) {
        String sql = "SELECT requestID, unitID, issueDescription, requestDate, status FROM MaintenanceRequests WHERE requestID = ?";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find maintenance request", e);
        }
        return Optional.empty();
    }

    private MaintenanceRequest mapRow(ResultSet rs) throws SQLException {
        return new MaintenanceRequest(
                rs.getInt("requestID"),
                rs.getInt("unitID"),
                rs.getString("issueDescription"),
                rs.getTimestamp("requestDate").toLocalDateTime(),
                rs.getString("status")
        );
    }
}
