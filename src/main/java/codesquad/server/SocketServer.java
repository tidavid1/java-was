package codesquad.server;

import codesquad.handler.StaticFileProvider;
import codesquad.handler.endpoint.EndPointHandler;
import codesquad.runner.ConnectionRunner;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketServer {

    private final Logger log = LoggerFactory.getLogger(SocketServer.class);

    private final int port;
    private final ExecutorService executorService;

    public SocketServer(int port) {
        this.port = port;
        this.executorService = Executors.newCachedThreadPool();
        StaticFileProvider.init();
        EndPointHandler.handleAllHandler();
    }

    public SocketServer() {
        this(8080);
    }

    public void start() {
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
}
