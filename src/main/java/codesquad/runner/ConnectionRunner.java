package codesquad.runner;

import codesquad.handler.HttpRequestHandler;
import codesquad.http.HttpResponse;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionRunner implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(ConnectionRunner.class);
    private final Socket cilentSocket;
    private final HttpRequestHandler requestHandler;

    public ConnectionRunner(Socket cilentSocket) {
        this.cilentSocket = cilentSocket;
        this.requestHandler = new HttpRequestHandler();
    }

    @Override
    public void run() {
        try (var is = cilentSocket.getInputStream(); var os = cilentSocket.getOutputStream()) {
            log.debug("Client connected");
            HttpResponse response = requestHandler.handle(is);
            os.write(response.toResponseBytes());
            os.flush();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
