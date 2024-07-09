package codesquad.http;

import codesquad.http.enums.StatusCode;
import codesquad.util.DateTimeResponseFormatter;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpResponse {

    private final StatusCode statusCode;
    private final Map<String, String> headers = new HashMap<>();
    private final byte[] body;

    public HttpResponse(StatusCode statusCode, byte[] body) {
        this.statusCode = statusCode;
        this.body = body;
        addDefaultHeaders();
    }

    public static HttpResponse from(StatusCode statusCode) {
        return new HttpResponse(statusCode, new byte[0]);
    }

    public static HttpResponse of(StatusCode statusCode, byte[] body) {
        return new HttpResponse(statusCode, body);
    }

    public void addHeader(String key, String value) {
        headers.put(Objects.requireNonNull(key), Objects.requireNonNull(value));
    }

    public void addHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
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
}
