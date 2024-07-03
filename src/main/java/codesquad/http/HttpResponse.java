package codesquad.http;

import codesquad.formatter.ContentTypeFormatter;
import codesquad.formatter.DateTimeResponseFormatter;
import codesquad.http.enums.StatusCode;
import codesquad.reader.FileByteReader;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    private static final String DEFAULT_HTTP_VERSION = "HTTP/1.1";

    private StatusCode statusCode;
    private final Map<String, Object> headers;
    private byte[] body;

    private HttpResponse(String requestUri) {
        log.debug("requestUri: {}", requestUri);
        this.headers = createDefaultHeaders();
        processRequestUri(requestUri);
    }

    public static HttpResponse from(String requestUri) {
        return new HttpResponse(requestUri);
    }

    public byte[] generateResponse() {
        var tempResponse = (generateResponseLine() + generateHeaders() + "\r\n").getBytes();
        var response = new byte[tempResponse.length + body.length];
        System.arraycopy(tempResponse, 0, response, 0, tempResponse.length);
        System.arraycopy(body, 0, response, tempResponse.length, body.length);
        return response;
    }

    private String generateResponseLine() {
        return DEFAULT_HTTP_VERSION + " " + statusCode.getCode() + " " + statusCode.name() + "\r\n";
    }

    private String generateHeaders() {
        StringBuilder sb = new StringBuilder();
        headers.forEach((key, value) -> sb.append(key).append(": ").append(value).append("\r\n"));
        return sb.toString();
    }

    private Map<String, Object> createDefaultHeaders() {
        var defaultHeader = new HashMap<String, Object>();
        defaultHeader.put("Date",
            DateTimeResponseFormatter.formatZonedDateTime(ZonedDateTime.now()));
        defaultHeader.put("Server", "java-was");
        return defaultHeader;
    }

    private void processRequestUri(String requestUri) {
        try {
            this.body = new FileByteReader(requestUri).readAllBytes();
            this.statusCode = StatusCode.OK;
            headers.put("Connection", "keep-alive");
            headers.put("Content-Type", ContentTypeFormatter.formatContentType(
                requestUri.substring(requestUri.lastIndexOf('.'))));
            headers.put("Content-Length", body.length);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            handleNotFound();
        }
    }

    private void handleNotFound() {
        this.statusCode = StatusCode.NOT_FOUND;
        headers.put("Content-Type", "text/html");
        headers.put("Connection", "close");
        this.body = "<h1>404 Not Found</h1>".getBytes();
    }

}
