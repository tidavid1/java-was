package codesquad.handler;

import codesquad.exception.HttpCommonException;
import codesquad.http.servlet.HttpRequest;
import codesquad.http.servlet.HttpResponse;
import codesquad.http.servlet.enums.StatusCode;
import codesquad.register.EndPointRegister;
import codesquad.register.model.EndPoint;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestHandler {

    private static final Logger log = LoggerFactory.getLogger(HttpRequestHandler.class);

    private final EndPointRegister endpointRegister;

    public HttpRequestHandler() {
        this.endpointRegister = EndPointRegister.getInstance();
    }

    public HttpResponse handle(InputStream is) {
        try {
            // Parse request to HttpRequest
            HttpRequest request = new HttpRequest(is);
            // Get response from endpoint
            return generateResponse(request);
        } catch (HttpCommonException hce) {
            log.error("{}: {}", hce.getStatusCode(), hce.getMessage());
            return HttpResponse.from(hce.getStatusCode());
        } catch (Exception e) {
            log.error("500: {}", e.getMessage());
            return HttpResponse.from(StatusCode.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings("unchecked")
    private HttpResponse generateResponse(HttpRequest httpRequest) {
        // Find endpoint with request
        EndPoint<String> endPoint = (EndPoint<String>) endpointRegister.getEndpoint(
            httpRequest.getHttpMethod(), httpRequest.getRequestUri().getPath());
        return switch (httpRequest.getHttpMethod()) {
            case GET -> endPoint.apply(httpRequest.getHeaders(), httpRequest.getRequestQuery());
            case POST -> endPoint.apply(httpRequest.getHeaders(), httpRequest.getBody());
            default -> HttpResponse.from(StatusCode.NOT_IMPLEMENTED);
        };
    }
}
