package codesquad.server.http.servlet;

import codesquad.server.http.servlet.enums.HttpMethod;
import codesquad.server.http.servlet.enums.HttpVersion;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {

    private final HttpMethod method;
    private final URI uri;
    private final HttpVersion version;
    private final Map<String, List<String>> headers = new HashMap<>();
    private final String body;

    public HttpRequest(String[] requestLineParts, Map<String, List<String>> headers, String body) {
        this.method = HttpMethod.valueOf(requestLineParts[0]);
        this.uri = URI.create(requestLineParts[1]);
        this.version = HttpVersion.from(requestLineParts[2]);
        this.headers.putAll(headers);
        this.body = body;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public URI getUri() {
        return uri;
    }

    public HttpVersion getVersion() {
        return version;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }
}