package codesquad.http;

import codesquad.formatter.DateTimeResponseFormatter;
import codesquad.http.enums.StatusCode;
import codesquad.register.EndPoint;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

    private final StatusCode statusCode;
    private final Map<String, String> headers = new HashMap<>();
    private final byte[] body;

    public HttpResponse(EndPoint endPoint, String query) {
        this.statusCode = endPoint.getStatusCode();
        this.body = endPoint.getFunction().apply(query);
        addDefaultHeaders();
        manageHeaders(endPoint);
    }

    public HttpResponse(StatusCode statusCode, byte[] body) {
        this.statusCode = statusCode;
        this.body = body;
        addDefaultHeaders();
    }

    public HttpResponse(StatusCode statusCode) {
        this(statusCode, new byte[0]);
    }

    public void addHeader(String key, String value) {
        Optional.ofNullable(headers.get(key))
            .ifPresentOrElse(
                prev -> {
                    log.warn("Header already exists: {}", prev);
                    headers.put(key, prev + "\n" + value);
                },
                () -> headers.put(key, value)
            );
    }

    public byte[] toResponseBytes() {
        byte[] responseLineWithHeaders = (convertResponseLineToString() + convertHeadersToString()
            + "\r\n").getBytes();
        byte[] response = new byte[responseLineWithHeaders.length + body.length];
        System.arraycopy(responseLineWithHeaders, 0, response, 0, responseLineWithHeaders.length);
        System.arraycopy(body, 0, response, responseLineWithHeaders.length, body.length);
        return response;
    }

    private void addDefaultHeaders() {
        headers.putAll(
            Map.of(
                "Date", DateTimeResponseFormatter.formatZonedDateTime(ZonedDateTime.now()),
                "Server", "java-was",
                "Content-Length", String.valueOf(body.length)
            )
        );
    }

    private String convertResponseLineToString() {
        return "HTTP/1.1 " + statusCode.getCode() + " " + statusCode.getMessage() + "\r\n";
    }

    private String convertHeadersToString() {
        StringBuilder sb = new StringBuilder();
        headers.forEach((key, value) -> sb.append(key).append(": ").append(value).append("\r\n"));
        return sb.toString();
    }

    private void manageHeaders(EndPoint endPoint) {
        Optional.ofNullable(endPoint.getContentType())
            .ifPresent(contentType -> addHeader("Content-Type", contentType));
        Optional.ofNullable(endPoint.getRedirectUri())
            .ifPresent(redirectUri -> addHeader("Location", redirectUri));
    }
}
