package codesquad.server.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import javax.sql.ConnectionPoolDataSource;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.jdbcx.JdbcDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class H2ConnectManager {

    // TODO: 싱글턴 객체 관리 방식 진짜 구현 필요할듯 ㄹㅇ 빈으로라도...
    private static H2ConnectManager instance;
    private static final String INIT_SQL_PATH = "/sql/init.sql";
    private static final Logger log = LoggerFactory.getLogger(H2ConnectManager.class);

    private final JdbcConnectionPool jdbcConnectionPool;

    private H2ConnectManager(String jdbcUrl, String username, String password) {
        ConnectionPoolDataSource connectionPoolDataSource = generateConnectionPoolDataSource(
            jdbcUrl, username, password);
        this.jdbcConnectionPool = JdbcConnectionPool.create(connectionPoolDataSource);
        init();
    }

    public static H2ConnectManager getInstance() {
        if (instance == null) {
            instance = new H2ConnectManager("jdbc:h2:./db/java-was", "sa", "");
        }
        return instance;
    }

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

    private void init() {
        try (Connection connection = getConnection(); InputStream inputStream = getClass().getResourceAsStream(
            INIT_SQL_PATH); Statement statement = connection.createStatement()) {
            String initScript = new String(Objects.requireNonNull(inputStream).readAllBytes());
            statement.execute(initScript);
        } catch (SQLException | IOException e) {
            log.error(e.getMessage());
        }
    }

}
