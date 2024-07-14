package codesquad.server.http.servlet;

import codesquad.server.http.servlet.values.HttpHeaders;
import codesquad.server.http.servlet.values.HttpRequestLine;
import java.util.List;

public abstract class HttpRequest {

    protected HttpRequestLine requestLine;
    protected HttpHeaders headers;

    protected HttpRequest(HttpRequestLine requestLine, HttpHeaders headers) {
        this.requestLine = requestLine;
        this.headers = headers;
    }

    public HttpRequestLine getRequestLine() {
        return requestLine;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public List<String> getHeader(String key) {
        return headers.getAllHeaders().getOrDefault(key, List.of());
    }

}
