package codesquad.http.servlet;

import codesquad.http.servlet.enums.StatusCode;
import codesquad.util.DateTimeResponseFormatter;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpResponse {

    private static final String HTTP_VERSION = "HTTP/1.1";
    private static final String CRLF = "\r\n";
    private static final String SP = " ";
    private static final String COLON = ": ";

    private StatusCode statusCode;
    private final Map<String, List<String>> headers;
    private byte[] body;

    HttpResponse() {
        this.headers = new HashMap<>(
            Map.of(
                "Content-Length", List.of("0"),
                "Date", List.of(DateTimeResponseFormatter.formatZonedDateTime(ZonedDateTime.now())),
                "Server", List.of("java-was")
            ));
        this.body = new byte[0];
    }

    StatusCode getStatusCode() {
        return statusCode;
    }

    void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    void setBody(byte[] body) {
        this.body = body;
        headers.put("Content-Length", List.of(String.valueOf(body.length)));
    }

    void addHeader(String key, String value) {
        headers.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }

    byte[] toResponseBytes() {
        byte[] responseLineWithHeaders = (convertResponseLineToString() + convertHeadersToString()
            + CRLF).getBytes();
        byte[] response = new byte[responseLineWithHeaders.length + body.length];
        System.arraycopy(responseLineWithHeaders, 0, response, 0, responseLineWithHeaders.length);
        System.arraycopy(body, 0, response, responseLineWithHeaders.length, body.length);
        return response;
    }

    private String convertResponseLineToString() {
        return HTTP_VERSION + SP + statusCode.getCode() + SP + statusCode.getMessage() + CRLF;
    }

    private String convertHeadersToString() {
        StringBuilder sb = new StringBuilder();
        headers.forEach((key, values) -> values.forEach(
            value -> sb.append(key).append(COLON).append(value).append(CRLF)));
        return sb.toString();
    }
}
