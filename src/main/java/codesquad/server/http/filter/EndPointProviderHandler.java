package codesquad.server.http.filter;

import codesquad.server.endpoint.EndPointStorage;
import codesquad.server.http.exception.HttpCommonException;
import codesquad.server.http.servlet.HttpRequest;
import codesquad.server.http.servlet.HttpServletRequest;
import codesquad.server.http.servlet.HttpServletResponse;
import codesquad.server.http.servlet.enums.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EndPointProviderHandler implements Filter {

    private static final Logger log = LoggerFactory.getLogger(EndPointProviderHandler.class);
    private static EndPointProviderHandler instance;

    private final EndPointStorage endPointStorage;

    private EndPointProviderHandler() {
        this.endPointStorage = EndPointStorage.getInstance();
    }

    public static EndPointProviderHandler getInstance() {
        if (instance == null) {
            instance = new EndPointProviderHandler();
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
                    endPointStorage.getEndpoint(httpRequest.getMethod(),
                            httpRequest.getUri().getPath())
                        .ifPresentOrElse(
                            endPoint -> {
                                log.debug("EndPoint: {}", endPoint);
                                switch (httpRequest.getMethod()) {
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
