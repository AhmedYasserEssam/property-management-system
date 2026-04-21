package DataLayer.DataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class AuthService {
    public enum Role {
        OWNER,
        TENANT
    }

    public static final class AuthenticatedUser {
        private final Role role;
        private final int userId;
        private final String displayName;

        public AuthenticatedUser(Role role, int userId, String displayName) {
            this.role = role;
            this.userId = userId;
            this.displayName = displayName;
        }

        public Role getRole() {
            return role;
        }

        public int getUserId() {
            return userId;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    private final IDbConnectionProvider connectionProvider;

    public AuthService() {
        this(SqlServerConnectionManager.getInstance());
    }

    public AuthService(IDbConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public Optional<AuthenticatedUser> authenticate(Role role, String username, String password) {
        if (role == null || username == null || username.isBlank() || password == null || password.isBlank()) {
            return Optional.empty();
        }

        String normalizedUsername = username.trim();
        String normalizedPassword = password.trim();
        switch (role) {
            case OWNER:
                return authenticateOwner(normalizedUsername, normalizedPassword);
            case TENANT:
                return authenticateTenant(normalizedUsername, normalizedPassword);
            default:
                return Optional.empty();
        }
    }

    public AuthenticatedUser registerOwner(String name, String password) {
        String normalizedName = requireText(name, "Owner name");
        String normalizedPassword = requireText(password, "Owner password");

        String sql = "INSERT INTO Owners (name, contactInfo) VALUES (?, ?)";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, normalizedName);
            ps.setString(2, normalizedPassword);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return new AuthenticatedUser(Role.OWNER, keys.getInt(1), normalizedName);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to register owner", ex);
        }

        throw new IllegalStateException("Owner registration did not return a generated ID.");
    }

    public AuthenticatedUser registerTenant(String fullName, String password) {
        String normalizedName = requireText(fullName, "Tenant full name");
        String normalizedPassword = requireText(password, "Tenant password");

        String sql = "INSERT INTO Tenants (fullName, contactInfo) VALUES (?, ?)";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, normalizedName);
            ps.setString(2, normalizedPassword);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return new AuthenticatedUser(Role.TENANT, keys.getInt(1), normalizedName);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to register tenant", ex);
        }

        throw new IllegalStateException("Tenant registration did not return a generated ID.");
    }

    private String requireText(String value, String fieldName) {
        if (value == null || value.trim().isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank.");
        }
        return value.trim();
    }

    private Optional<AuthenticatedUser> authenticateOwner(String username, String password) {
        String sql = "SELECT TOP 1 ownerID, name FROM Owners WHERE name = ? AND contactInfo = ? ORDER BY ownerID";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new AuthenticatedUser(
                            Role.OWNER,
                            rs.getInt("ownerID"),
                            rs.getString("name")));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to authenticate owner", ex);
        }

        return Optional.empty();
    }

    private Optional<AuthenticatedUser> authenticateTenant(String username, String password) {
        String sql = "SELECT TOP 1 tenantID, fullName FROM Tenants WHERE fullName = ? AND contactInfo = ? ORDER BY tenantID";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new AuthenticatedUser(
                            Role.TENANT,
                            rs.getInt("tenantID"),
                            rs.getString("fullName")));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to authenticate tenant", ex);
        }

        return Optional.empty();
    }
}
