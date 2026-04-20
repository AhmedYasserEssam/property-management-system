package DataLayer.DataAccess;

import BusinessLayer.Domain.Property;
import BusinessLayer.Repository.IPropertyRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Property data access implementation.
 */
public class PropertyDB extends BaseRepository<Property> implements IPropertyRepository {
    private final IDbConnectionProvider connectionProvider;

    public PropertyDB() {
        this(SqlServerConnectionManager.getInstance());
    }

    public PropertyDB(IDbConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    protected void beforeSave(Property property) {
        System.out.println("[RelationalPropertyDB] Property saving -> SQL Server");
    }

    @Override
    protected int getEntityID(Property property) {
        return property.getPropertyID();
    }

    @Override
    protected void insert(Property property) {
        String sql = "INSERT INTO Properties (ownerID, address, propertyType) VALUES (?, ?, ?)";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, property.getOwnerID());
            ps.setString(2, property.getAddress());
            ps.setString(3, property.getPropertyType());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    property.setPropertyID(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert property", e);
        }
    }

    @Override
    protected void update(Property property) {
        String sql = "UPDATE Properties SET ownerID = ?, address = ?, propertyType = ? WHERE propertyID = ?";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, property.getOwnerID());
            ps.setString(2, property.getAddress());
            ps.setString(3, property.getPropertyType());
            ps.setInt(4, property.getPropertyID());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update property", e);
        }
    }

    @Override
    public Optional<Property> findByID(int propertyID) {
        String sql = "SELECT propertyID, ownerID, address, propertyType FROM Properties WHERE propertyID = ?";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, propertyID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find property", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Property> findByOwnerID(int ownerID) {
        String sql = "SELECT propertyID, ownerID, address, propertyType FROM Properties WHERE ownerID = ?";
        List<Property> result = new ArrayList<>();
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ownerID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find properties by owner", e);
        }
        return result;
    }

    private Property mapRow(ResultSet rs) throws SQLException {
        return new Property(
                rs.getInt("propertyID"),
                rs.getInt("ownerID"),
                rs.getString("address"),
                rs.getString("propertyType")
        );
    }
}
