package codesquad.server;

import java.sql.SQLException;
import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class H2Server implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(H2Server.class);

    @Override
    public void run() {
        try {
            // Turn On H2 TCP Server
            Server.createTcpServer(
                "-tcp",
                "-tcpAllowOthers",
                "-tcpPort", "9092",
                "-baseDir", "./db"
            ).start();
            // Turn On Web Console Server
            Server.createWebServer(
                "-web",
                "-webAllowOthers",
                "-webPort", "8082"
            ).start();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

}
