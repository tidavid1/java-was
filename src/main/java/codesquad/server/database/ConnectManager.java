package codesquad.server.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectManager {

    Connection getConnection() throws SQLException;
}
