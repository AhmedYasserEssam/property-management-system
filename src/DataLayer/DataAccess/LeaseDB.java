package DataLayer.DataAccess;

import BusinessLayer.Domain.Lease;
import BusinessLayer.Domain.IClock;
import BusinessLayer.Repository.ILeaseRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Data access implementation for leases.
 */
public class LeaseDB extends BaseRepository<Lease> implements ILeaseRepository {
    private final IClock clock;
    private final IDbConnectionProvider connectionProvider;

    public LeaseDB(IClock clock) {
        this(clock, SqlServerConnectionManager.getInstance());
    }

    public LeaseDB(IClock clock, IDbConnectionProvider connectionProvider) {
        this.clock = clock;
        this.connectionProvider = connectionProvider;
    }

    @Override
    protected int getEntityID(Lease lease) {
        return lease.getLeaseID();
    }

    @Override
    protected void insert(Lease lease) {
        String sql = "INSERT INTO Leases (tenantID, unitID, startDate, endDate, rentAmount, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, lease.getTenantID());
            ps.setInt(2, lease.getUnitID());
            ps.setTimestamp(3, Timestamp.valueOf(lease.getStartDate()));
            ps.setTimestamp(4, Timestamp.valueOf(lease.getEndDate()));
            ps.setDouble(5, lease.getRentAmount());
            ps.setString(6, lease.getStatus());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    lease.setLeaseID(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert lease", e);
        }
    }

    @Override
    protected void update(Lease lease) {
        String sql = "UPDATE Leases SET tenantID = ?, unitID = ?, startDate = ?, endDate = ?, rentAmount = ?, status = ? WHERE leaseID = ?";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, lease.getTenantID());
            ps.setInt(2, lease.getUnitID());
            ps.setTimestamp(3, Timestamp.valueOf(lease.getStartDate()));
            ps.setTimestamp(4, Timestamp.valueOf(lease.getEndDate()));
            ps.setDouble(5, lease.getRentAmount());
            ps.setString(6, lease.getStatus());
            ps.setInt(7, lease.getLeaseID());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update lease", e);
        }
    }

    @Override
    public Optional<Lease> findByID(int leaseID) {
        String sql = "SELECT leaseID, tenantID, unitID, startDate, endDate, rentAmount, status FROM Leases WHERE leaseID = ?";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, leaseID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find lease", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Lease> fetchLeasesNearEnd(int thresholdDays) {
        String sql = "SELECT leaseID, tenantID, unitID, startDate, endDate, rentAmount, status "
                + "FROM Leases "
                + "WHERE endDate >= ? AND endDate <= ? "
                + "ORDER BY endDate";
        List<Lease> result = new ArrayList<>();
        Timestamp now = Timestamp.valueOf(clock.getCurrentDate());
        Timestamp cutoff = Timestamp.valueOf(clock.getCurrentDate().plusDays(thresholdDays));
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, now);
            ps.setTimestamp(2, cutoff);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch leases near end", e);
        }
        return result;
    }

    @Override
    public Iterator<Lease> expiringLeasesIterator(int thresholdDays) {
        return fetchLeasesNearEnd(thresholdDays).iterator();
    }

    private Lease mapRow(ResultSet rs) throws SQLException {
        Lease lease = new Lease(
                rs.getInt("leaseID"),
                rs.getInt("tenantID"),
                rs.getInt("unitID"),
                rs.getTimestamp("startDate").toLocalDateTime(),
                rs.getTimestamp("endDate").toLocalDateTime(),
                rs.getDouble("rentAmount")
        );
        String status = rs.getString("status");
        if ("EXPIRING".equals(status)) {
            lease.markExpiringIfWithinDays(Integer.MAX_VALUE, lease.getEndDate());
        } else if ("EXPIRED".equals(status)) {
            lease.markExpired();
        }
        return lease;
    }
}
