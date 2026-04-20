package DataLayer.DataAccess;

import BusinessLayer.Domain.Tenant;
import BusinessLayer.Repository.ITenantRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/**
 * Tenant data access implementation.
 */
public class TenantDB extends BaseRepository<Tenant> implements ITenantRepository {
    private final IDbConnectionProvider connectionProvider;

    public TenantDB() {
        this(SqlServerConnectionManager.getInstance());
    }

    public TenantDB(IDbConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    protected int getEntityID(Tenant tenant) {
        return tenant.getTenantID();
    }

    @Override
    protected void insert(Tenant tenant) {
        String sql = "INSERT INTO Tenants (fullName, contactInfo) VALUES (?, ?)";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, tenant.getFullName());
            ps.setString(2, tenant.getContactInfo());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    tenant.setTenantID(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert tenant", e);
        }
    }

    @Override
    protected void update(Tenant tenant) {
        String sql = "UPDATE Tenants SET fullName = ?, contactInfo = ? WHERE tenantID = ?";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenant.getFullName());
            ps.setString(2, tenant.getContactInfo());
            ps.setInt(3, tenant.getTenantID());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update tenant", e);
        }
    }

    @Override
    public Optional<Tenant> findByID(int tenantID) {
        String sql = "SELECT tenantID, fullName, contactInfo FROM Tenants WHERE tenantID = ?";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tenantID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find tenant", e);
        }
        return Optional.empty();
    }

    private Tenant mapRow(ResultSet rs) throws SQLException {
        return new Tenant(
                rs.getInt("tenantID"),
                rs.getString("fullName"),
                rs.getString("contactInfo")
        );
    }
}
