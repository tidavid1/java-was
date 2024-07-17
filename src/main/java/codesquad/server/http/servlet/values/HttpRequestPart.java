package codesquad.server.http.servlet.values;

import java.util.List;
import java.util.Map;

public class HttpRequestPart {

    private final HttpHeaders headers;
    private final byte[] body;

    private HttpRequestPart(HttpHeaders headers, byte[] body) {
        this.headers = headers;
        this.body = body;
    }

    public static HttpRequestPart from(Map<String, List<String>> headers, byte[] body) {
        return new HttpRequestPart(HttpHeaders.from(headers), body);
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }
}
