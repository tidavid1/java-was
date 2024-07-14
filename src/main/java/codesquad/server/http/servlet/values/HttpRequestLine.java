package codesquad.server.http.servlet.values;

import codesquad.server.http.servlet.enums.HttpMethod;
import codesquad.server.http.servlet.enums.HttpVersion;
import java.net.URI;

public class HttpRequestLine {

    private final HttpMethod method;
    private final String path;
    private final HttpVersion version;
    private final HttpQueryParams queryParams;

    private HttpRequestLine(String method, String path, String version) {
        URI fullUri = URI.create(path);
        this.method = HttpMethod.valueOf(method);
        this.path = fullUri.getPath();
        this.version = HttpVersion.from(version);
        this.queryParams = HttpQueryParams.from(fullUri.getQuery());
    }

    public static HttpRequestLine from(String[] requestLine) {
        return new HttpRequestLine(requestLine[0], requestLine[1], requestLine[2]);
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public HttpVersion getVersion() {
        return version;
    }

    public HttpQueryParams getQueryParams() {
        return queryParams;
    }
}
