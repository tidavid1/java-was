package codesquad.server.runner;

import codesquad.server.http.filter.AuthenticationFilter;
import codesquad.server.http.filter.EndPointProviderHandler;
import codesquad.server.http.filter.ExceptionHandlerFilter;
import codesquad.server.http.filter.FilterChain;
import codesquad.server.http.filter.SecurityFilterChain;
import codesquad.server.http.filter.SessionContextClearFilter;
import codesquad.server.http.filter.UserLoginFilter;
import codesquad.server.http.parser.HttpRequestParser;
import codesquad.server.http.servlet.HttpServletRequest;
import codesquad.server.http.servlet.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionRunner implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(ConnectionRunner.class);
    private final Socket clientSocket;
    private final HttpRequestParser httpRequestParser;
    private final FilterChain preFilterChain;
    private final FilterChain postFilterChain;

    public ConnectionRunner(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.httpRequestParser = new HttpRequestParser();
        // TODO: 이거 팩토리 같은거에서 한번에 제공 안해지면 개 드러워질듯 ㅇㅈ? ㅇㅈ
        this.preFilterChain = new SecurityFilterChain(UserLoginFilter.getInstance(),
            AuthenticationFilter.getInstance(), EndPointProviderHandler.getInstance());
        this.postFilterChain = new SecurityFilterChain(ExceptionHandlerFilter.getInstance(),
            SessionContextClearFilter.getInstance());
    }

    @Override
    public void run() {
        try (InputStream is = clientSocket.getInputStream(); OutputStream os = clientSocket.getOutputStream()) {
            log.debug("Client connected");
            HttpServletRequest servletRequest = httpRequestParser.parse(is);
            HttpServletResponse servletResponse = new HttpServletResponse();
            preFilterChain.doFilter(servletRequest, servletResponse);
            postFilterChain.doFilter(servletRequest, servletResponse);
            servletResponse.flush(os);
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()));
            log.error(e.getMessage());
        }
    }
}
