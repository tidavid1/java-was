package codesquad.handler;

import codesquad.exception.HttpCommonException;
import codesquad.http.parser.HttpRequestParser;
import codesquad.http.servlet.HttpRequest;
import codesquad.http.servlet.HttpResponseDeprecated;
import codesquad.http.servlet.enums.StatusCode;
import codesquad.register.EndPointRegister;
import codesquad.register.model.EndPoint;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestHandler {

    private static final Logger log = LoggerFactory.getLogger(HttpRequestHandler.class);

    private final EndPointRegister endpointRegister;
    private final HttpRequestParser httpRequestParser = new HttpRequestParser();

    public HttpRequestHandler() {
        this.endpointRegister = EndPointRegister.getInstance();
    }

    public HttpResponseDeprecated handle(InputStream is) {
        try {
            // Parse request to HttpRequest
            HttpRequest request = httpRequestParser.parse(is).getRequest();
            // Get response from endpoint
            return generateResponse(request);
        } catch (HttpCommonException hce) {
            log.error("{}: {}", hce.getStatusCode(), hce.getMessage());
            return HttpResponseDeprecated.from(hce.getStatusCode());
        } catch (Exception e) {
            log.error("500: {}", e.getMessage());
            return HttpResponseDeprecated.from(StatusCode.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings("unchecked")
    private HttpResponseDeprecated generateResponse(HttpRequest httpRequest) {
        // Find endpoint with request
        EndPoint<String> endPoint = (EndPoint<String>) endpointRegister.getEndpoint(
            httpRequest.getMethod(), httpRequest.getUri().getPath());
        return switch (httpRequest.getMethod()) {
            case GET -> endPoint.apply(null, httpRequest.getUri().getQuery());
            case POST -> endPoint.apply(null, httpRequest.getBody());
            default -> HttpResponseDeprecated.from(StatusCode.NOT_IMPLEMENTED);
        };
    }
}
