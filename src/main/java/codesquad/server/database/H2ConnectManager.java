package codesquad.server.database;

import codesquad.server.properties.ApplicationProperties;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import javax.sql.ConnectionPoolDataSource;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.jdbcx.JdbcDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class H2ConnectManager implements ConnectManager {

    private static final String INIT_SQL_PATH = "/sql/init.sql";
    private static final Logger log = LoggerFactory.getLogger(H2ConnectManager.class);

    private final JdbcConnectionPool jdbcConnectionPool;

    private H2ConnectManager(ApplicationProperties applicationProperties) {
        init(applicationProperties.getDBFilePath(), applicationProperties.getDBCurrentUsername(),
            applicationProperties.getDBCurrentPassword());
        ConnectionPoolDataSource connectionPoolDataSource = generateConnectionPoolDataSource(
            applicationProperties.getDBCurrentUrl(), applicationProperties.getDBCurrentUsername(),
            applicationProperties.getDBCurrentPassword());
        this.jdbcConnectionPool = JdbcConnectionPool.create(connectionPoolDataSource);

    }

    @Override
    public Connection getConnection() throws SQLException {
        return jdbcConnectionPool.getConnection();
    }

    private ConnectionPoolDataSource generateConnectionPoolDataSource(String jdbcUrl,
        String username, String password) {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL(jdbcUrl);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    private void init(String url, String user, String password) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
            InputStream inputStream = getClass().getResourceAsStream(INIT_SQL_PATH);
            Statement statement = connection.createStatement()) {
            String initScript = new String(Objects.requireNonNull(inputStream).readAllBytes());
            statement.execute(initScript);
        } catch (SQLException | IOException e) {
            log.error(e.getMessage());
        }
    }

}
