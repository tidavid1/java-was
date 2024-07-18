package codesquad.csv;

import codesquad.server.properties.ApplicationProperties;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

public class CsvDriver implements Driver {

    private final CsvManager csvManager;
    private final String dbPath;

    private CsvDriver(ApplicationProperties applicationProperties) {
        this.dbPath = applicationProperties.getCsvFolderPath();
        this.csvManager = new CsvManager(dbPath);
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        if (!acceptsURL(url)) {
            return null;
        }
        String filePath = url.substring("jdbc:csv:".length())
            .replace("~", System.getProperty("user.home"));
        if (!filePath.equals(dbPath)) {
            return null;
        }
        return new CsvConnection(csvManager);
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return url != null && url.contains("jdbc:csv:");
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return new DriverPropertyInfo[0];
    }

    @Override
    public int getMajorVersion() {
        return 1;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }
}
