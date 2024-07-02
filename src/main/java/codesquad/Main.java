package codesquad;

import codesquad.handler.ConnectionHandler;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final int PORT = 8080;
    private static final int N_THREADS = 10;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT); // 8080 포트에서 서버를 엽니다.
        log.debug("Listening for connection on port 8080 ....");
        var executor = Executors.newFixedThreadPool(N_THREADS);
        while (true) {
            executor.execute(new ConnectionHandler(serverSocket.accept()));
        }
    }

}
