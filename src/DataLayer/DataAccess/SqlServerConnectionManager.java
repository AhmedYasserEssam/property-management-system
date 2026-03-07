package DataLayer.DataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton responsible for SQL Server connection initialization.
 */
public final class SqlServerConnectionManager implements IDbConnectionProvider {
    private static volatile SqlServerConnectionManager instance;

    private final String connectionUrl;
    private final String username;
    private final String password;

    private SqlServerConnectionManager() {
        this.connectionUrl = getRequiredEnv("RES_DB_URL");
        this.username = getRequiredEnv("RES_DB_USER");
        this.password = getRequiredEnv("RES_DB_PASSWORD");

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("SQL Server JDBC driver not found in classpath.", ex);
        }
    }

    public static SqlServerConnectionManager getInstance() {
        if (instance == null) {
            synchronized (SqlServerConnectionManager.class) {
                if (instance == null) {
                    instance = new SqlServerConnectionManager();
                }
            }
        }
        return instance;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionUrl, username, password);
    }

    private String getRequiredEnv(String key) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required environment variable: " + key);
        }
        return value;
    }
}
