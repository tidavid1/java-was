package codesquad.server;

import codesquad.server.properties.ApplicationProperties;
import java.sql.SQLException;
import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class H2Server implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(H2Server.class);
    private final String tcpPort;
    private final String tcpBaseDir;
    private final String webConsolePort;

    public H2Server(ApplicationProperties properties) {
        tcpPort = properties.getDBTcpPort();
        tcpBaseDir = properties.getDBTcpBaseDir();
        webConsolePort = properties.getDBWebConsolePort();
    }

    @Override
    public void run() {
        log.debug("H2 Server Started");
        try {
            // Turn On H2 TCP Server
            Server.createTcpServer(
                "-tcp",
                "-tcpAllowOthers",
                "-tcpPort", tcpPort,
                "-baseDir", tcpBaseDir
            ).start();
            // Turn On Web Console Server
            Server.createWebServer(
                "-web",
                "-webAllowOthers",
                "-webPort", webConsolePort
            ).start();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

}
