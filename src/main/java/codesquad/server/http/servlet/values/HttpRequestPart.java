package codesquad.server.http.servlet.values;

public class HttpRequestPart {

    private final HttpHeaders headers;
    private final byte[] body;

    private HttpRequestPart(HttpHeaders headers, byte[] body) {
        this.headers = headers;
        this.body = body;
    }

    public static HttpRequestPart from(HttpHeaders headers, byte[] body) {
        return new HttpRequestPart(headers, body);
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }
}
