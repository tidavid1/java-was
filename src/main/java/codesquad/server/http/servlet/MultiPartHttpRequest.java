package codesquad.server.http.servlet;

import codesquad.server.http.servlet.values.HttpHeaders;
import codesquad.server.http.servlet.values.HttpRequestLine;
import codesquad.server.http.servlet.values.HttpRequestPart;
import java.util.List;
import java.util.Map;

public class MultiPartHttpRequest extends HttpRequest {

    private final List<HttpRequestPart> requestParts;

    private MultiPartHttpRequest(HttpRequestLine requestLine, HttpHeaders headers,
        List<HttpRequestPart> requestParts) {
        super(requestLine, headers);
        this.requestParts = requestParts;
    }

    public static MultiPartHttpRequest of(String[] requestLinePart,
        Map<String, List<String>> headers, List<HttpRequestPart> parts) {
        return new MultiPartHttpRequest(HttpRequestLine.from(requestLinePart),
            HttpHeaders.from(headers), parts);
    }

    public List<HttpRequestPart> getRequestParts() {
        return requestParts;
    }
}
