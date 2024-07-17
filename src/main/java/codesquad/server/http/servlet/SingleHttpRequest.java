package codesquad.server.http.servlet;

import codesquad.server.http.servlet.values.HttpHeaders;
import codesquad.server.http.servlet.values.HttpRequestLine;
import java.util.List;
import java.util.Map;

public class SingleHttpRequest extends HttpRequest {

    private final String body;

    private SingleHttpRequest(HttpRequestLine requestLine, HttpHeaders headers, String body) {
        super(requestLine, headers);
        this.body = body;
    }

    public static SingleHttpRequest of(String[] requestLinePart, Map<String, List<String>> headers,
        String body) {
        return new SingleHttpRequest(HttpRequestLine.from(requestLinePart),
            HttpHeaders.from(headers), body);
    }

    public String getBody() {
        return body;
    }

}
