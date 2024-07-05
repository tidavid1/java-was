package codesquad.handler;

import codesquad.exception.BadRequestException;
import codesquad.exception.NotFoundException;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.enums.StatusCode;
import codesquad.register.EndPoint;
import codesquad.register.EndPointRegister;
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
            // Find endpoint with request
            EndPoint endPoint = endpointRegister.getEndpoint(request.getHttpMethod(),
                request.getRequestUri().getPath());
            // Get response from endpoint
            return new HttpResponse(endPoint, request.getRequestQuery());
        } catch (BadRequestException be) {
            log.error("400: {}", be.getMessage());
            return new HttpResponse(StatusCode.BAD_REQUEST);
        } catch (NotFoundException ne) {
            log.info("404: {}", ne.getMessage());
            return new HttpResponse(StatusCode.NOT_FOUND);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new HttpResponse(StatusCode.INTERNAL_SERVER_ERROR);
        }
    }
}
