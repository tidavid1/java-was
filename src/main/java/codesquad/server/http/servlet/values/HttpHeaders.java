package codesquad.server.http.servlet.values;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HttpHeaders {

    private final Map<String, List<String>> headers;

    private HttpHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public static HttpHeaders from(Map<String, List<String>> headers) {
        return new HttpHeaders(headers);
    }

    public Map<String, List<String>> getAllHeaders() {
        return Collections.unmodifiableMap(headers);
    }
}
