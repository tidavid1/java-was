package codesquad.server.runner;

import codesquad.server.bean.BeanFactory;
import codesquad.server.http.filter.EndPointProvideFilter;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionRunner implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(ConnectionRunner.class);
    private final Socket clientSocket;
    private final HttpRequestParser httpRequestParser;
    private final FilterChain filterChain;

    public ConnectionRunner(Socket clientSocket) {
        BeanFactory beanFactory = BeanFactory.getInstance();
        this.clientSocket = clientSocket;
        this.httpRequestParser = new HttpRequestParser();
        this.filterChain = new SecurityFilterChain(
            beanFactory.getBean(UserLoginFilter.class),
            beanFactory.getBean(UserLoginFilter.class),
            beanFactory.getBean(EndPointProvideFilter.class),
            beanFactory.getBean(ExceptionHandlerFilter.class),
            beanFactory.getBean(SessionContextClearFilter.class));
    }

    @Override
    public void run() {
        try (InputStream is = clientSocket.getInputStream(); OutputStream os = clientSocket.getOutputStream()) {
            log.debug("Client connected");
            HttpServletRequest servletRequest = httpRequestParser.parse(is);
            HttpServletResponse servletResponse = new HttpServletResponse();
            filterChain.doFilter(servletRequest, servletResponse);
            servletResponse.flush(os);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
