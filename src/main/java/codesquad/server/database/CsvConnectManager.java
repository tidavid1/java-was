package codesquad.server.database;

import codesquad.server.properties.ApplicationProperties;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvConnectManager implements ConnectManager {

    private static final String[] INIT_CSV_PATHS = {"/sql/users.csv", "/sql/comments.csv",
        "/sql/articles.csv"};
    private static final Logger log = LoggerFactory.getLogger(CsvConnectManager.class);

    private final String jdbcUrl;

    private CsvConnectManager(ApplicationProperties applicationProperties) {
        String csvFolderPath = applicationProperties.getCsvFolderPath();
        this.jdbcUrl = "jdbc:csv:" + csvFolderPath;
        init(csvFolderPath);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl);
    }

    private void init(String folderPath) {
        new File(folderPath).mkdirs();
        for (String path : INIT_CSV_PATHS) {
            String filePath = folderPath + path.substring(4);
            File file = new File(filePath);
            if (!file.exists()) {
                try (InputStream inputStream = getClass().getResourceAsStream(
                    path); FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead);
                    }
                    fileOutputStream.flush();
                } catch (IOException | NullPointerException e) {
                    log.error(e.getMessage());
                }
            }

        }
    }
}
