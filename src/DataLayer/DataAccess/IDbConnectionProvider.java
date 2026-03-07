package DataLayer.DataAccess;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDbConnectionProvider {
    Connection getConnection() throws SQLException;
}
