package codesquad.server.http.servlet;

import codesquad.server.http.servlet.enums.HeaderKey;
import codesquad.server.http.servlet.enums.StatusCode;
import codesquad.server.util.DateTimeResponseFormatter;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Deprecated(forRemoval = true)
public class HttpResponseDeprecated {

    private final StatusCode statusCode;
    private final Map<String, String> headers = new HashMap<>();
    private final byte[] body;

    public HttpResponseDeprecated(StatusCode statusCode, byte[] body) {
        this.statusCode = statusCode;
        this.body = body;
        addDefaultHeaders();
    }

    public static HttpResponseDeprecated from(StatusCode statusCode) {
        return new HttpResponseDeprecated(statusCode, new byte[0]);
    }

    public static HttpResponseDeprecated of(StatusCode statusCode, byte[] body) {
        return new HttpResponseDeprecated(statusCode, body);
    }

    public void addHeader(HeaderKey key, String value) {
        headers.put(Objects.requireNonNull(key.getValue()), Objects.requireNonNull(value));
    }

    public void addHeaders(Map<HeaderKey, String> headers) {
        for (Map.Entry<HeaderKey, String> entry : headers.entrySet()) {
            addHeader(entry.getKey(), entry.getValue());
        }
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
        addHeaders(
            Map.of(
                HeaderKey.DATE, DateTimeResponseFormatter.formatZonedDateTime(ZonedDateTime.now()),
                HeaderKey.SERVER, "java-was",
                HeaderKey.CONTENT_LENGTH, String.valueOf(body.length)
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
