package codesquad.http;

import codesquad.formatter.DateTimeResponseFormatter;
import codesquad.http.enums.StatusCode;
import codesquad.register.EndPoint;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpResponse {

    private final StatusCode statusCode;
    private final Map<String, String> headers = new HashMap<>();
    private final byte[] body;

    private HttpResponse(EndPoint endPoint, String query) {
        this.statusCode = endPoint.getStatusCode();
        this.body = endPoint.getFunction().apply(query);
        addDefaultHeaders();
        manageHeaders(endPoint);
    }

    private HttpResponse(StatusCode statusCode) {
        this.statusCode = statusCode;
        this.body = new byte[0];
        addDefaultHeaders();
    }

    public static HttpResponse from(StatusCode statusCode) {
        return new HttpResponse(statusCode);
    }

    public static HttpResponse of(EndPoint endPoint, String query) {
        return new HttpResponse(endPoint, query);
    }

    public void addHeader(String key, String value) {
        Optional.ofNullable(headers.get(key))
            .ifPresentOrElse(
                prev -> headers.put(key, prev + "\n" + value),
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
