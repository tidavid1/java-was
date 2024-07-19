package codesquad.server;

import codesquad.server.bean.BeanFactory;
import codesquad.server.database.csv.CsvDriver;
import codesquad.server.endpoint.EndPointRegister;
import codesquad.server.properties.ApplicationProperties;
import codesquad.server.runner.ConnectionRunner;
import codesquad.server.statics.StaticFileProvider;
import codesquad.server.template.TemplateFileProvider;
import java.io.IOException;
import java.net.ServerSocket;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketServer {

    private final Logger log = LoggerFactory.getLogger(SocketServer.class);

    private final int port;
    private final ExecutorService executorService;
    private final BeanFactory beanFactory;

    public SocketServer(int port) {
        this.port = port;
        this.executorService = Executors.newCachedThreadPool();
        this.beanFactory = BeanFactory.getInstance();
        try {
            DriverManager.registerDriver(beanFactory.getBean(CsvDriver.class));
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage());
        }
        beanFactory.getBean(StaticFileProvider.class).init();
        beanFactory.getBean(TemplateFileProvider.class).init();
        EndPointRegister.registerAll();
    }

    public SocketServer() {
        this(8080);
    }

    public void start() {
        startH2Thread();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.debug("Listening for connection on port 8080 ....");
            while (true) {
                executorService.execute(new ConnectionRunner(serverSocket.accept()));
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            System.exit(-1);
        }
    }

    private void startH2Thread() {
        Thread server = new Thread(new H2Server(beanFactory.getBean(ApplicationProperties.class)));
        server.start();
    }
}
