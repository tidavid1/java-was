package codesquad.server.properties;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.NoSuchFileException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationProperties {

    private static final Logger log = LoggerFactory.getLogger(ApplicationProperties.class);

    private final Properties properties;

    private ApplicationProperties() {
        this.properties = init();
    }


    public String getDBTcpPort() {
        return properties.getProperty("was.db.tcp.port");
    }

    public String getDBTcpBaseDir() {
        String property = properties.getProperty("was.db.tcp.base_dir");
        return replaceHomePath(property);
    }

    public String getDBWebConsolePort() {
        return properties.getProperty("was.db.web_console.port");
    }

    public String getDBFilePath() {
        String property = properties.getProperty("was.db.file.path");
        return replaceHomePath(property);
    }

    public String getDBCurrentUrl() {
        return properties.getProperty("was.db.current.url");
    }

    public String getDBCurrentUsername() {
        return properties.getProperty("was.db.current.username");
    }

    public String getDBCurrentPassword() {
        return properties.getProperty("was.db.current.password");
    }

    public String getImageFolderPath() {
        String property = properties.getProperty("was.image.folder.path");
        return replaceHomePath(property);
    }

    private Properties init() {
        Properties prop = new Properties();
        try (InputStream inputStream = getClass().getResourceAsStream("/application.properties")) {
            if (inputStream == null) {
                throw new NoSuchFileException("파일을 찾을 수 없습니다: /application.properties");
            }
            prop.load(inputStream);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return prop;
    }

    private String replaceHomePath(String property) {
        return property.replace("~", System.getProperty("user.home"));
    }

}
