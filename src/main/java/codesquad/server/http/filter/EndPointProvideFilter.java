package codesquad.server.http.filter;

import codesquad.server.endpoint.EndPointStorage;
import codesquad.server.http.exception.HttpCommonException;
import codesquad.server.http.servlet.HttpRequest;
import codesquad.server.http.servlet.HttpServletRequest;
import codesquad.server.http.servlet.HttpServletResponse;
import codesquad.server.http.servlet.enums.StatusCode;
import codesquad.server.http.servlet.values.HttpRequestLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EndPointProvideFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(EndPointProvideFilter.class);
    private static EndPointProvideFilter instance;

    private final EndPointStorage endPointStorage;

    private EndPointProvideFilter() {
        this.endPointStorage = EndPointStorage.getInstance();
    }

    public static EndPointProvideFilter getInstance() {
        if (instance == null) {
            instance = new EndPointProvideFilter();
        }
        return instance;
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain) {
        if (request.getRequest() != null) {
            response.getStatus().ifPresentOrElse(
                statusCode -> {
                },
                () -> {
                    HttpRequest httpRequest = request.getRequest();
                    HttpRequestLine requestLine = httpRequest.getRequestLine();
                    endPointStorage.getEndpoint(requestLine.getMethod(), requestLine.getPath())
                        .ifPresentOrElse(
                            endPoint -> {
                                log.debug("EndPoint: {}", endPoint);
                                switch (requestLine.getMethod()) {
                                    case GET, POST -> endPoint.accept(request, response);
                                    default -> request.setAttribute("exception",
                                        new HttpCommonException("Not implemented",
                                            StatusCode.NOT_IMPLEMENTED));
                                }
                            },
                            () -> request.setAttribute("exception",
                                new HttpCommonException("존재하지 않는 Endpoint 입니다.",
                                    StatusCode.NOT_FOUND))
                        );
                }
            );
        }
        chain.doFilter(request, response);
    }
}
