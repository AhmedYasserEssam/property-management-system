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

    private static final String DEFAULT_DB_URL =
            "jdbc:sqlserver://localhost:1433;databaseName=RealEstateDB;encrypt=true;trustServerCertificate=true";
    private static final String DEFAULT_DB_USER = "sa";
    private static final String DEFAULT_DB_PASSWORD = "HelloWorld123!";

    private SqlServerConnectionManager() {
        this.connectionUrl = getOptionalEnv("RES_DB_URL", DEFAULT_DB_URL);
        this.username = getOptionalEnv("RES_DB_USER", DEFAULT_DB_USER);
        this.password = getOptionalEnv("RES_DB_PASSWORD", DEFAULT_DB_PASSWORD);

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

    private String getOptionalEnv(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return value;
    }
}
