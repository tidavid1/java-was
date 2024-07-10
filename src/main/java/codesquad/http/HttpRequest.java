package codesquad.http;

import codesquad.exception.HttpCommonException;
import codesquad.http.enums.HttpMethod;
import codesquad.http.enums.HttpVersion;
import codesquad.http.enums.StatusCode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    private HttpMethod httpMethod;
    private URI requestUri;
    private HttpVersion httpVersion;
    private final Map<String, String> headers = new ConcurrentHashMap<>();
    private String body;

    public HttpRequest(InputStream inputStream) throws IOException {
        var br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        parseRequestLine(br.readLine());
        parseHeaders(br);
        parseBody(br);
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public URI getRequestUri() {
        return requestUri;
    }

    public HttpVersion getHttpVersion() {
        return httpVersion;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getRequestQuery() {
        return requestUri.getQuery();
    }

    public String getBody() {
        return body;
    }

    private void parseRequestLine(String requestLine) {
        log.debug(requestLine);
        String[] tokens = validateRequestLine(requestLine);
        httpMethod = HttpMethod.valueOf(tokens[0]);
        requestUri = URI.create(tokens[1]);
        httpVersion = HttpVersion.from(tokens[2]);
    }

    private void parseHeaders(BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            if (line.isBlank()) {
                break;
            }
            log.debug(line);
            String[] header = line.split(": ");
            Optional.ofNullable(headers.get(header[0]))
                .ifPresentOrElse(
                    prev -> headers.put(header[0], prev + "\n" + header[1]),
                    () -> headers.put(header[0], header[1])
                );
        }
    }

    private void parseBody(BufferedReader br) throws IOException {
        int size = Integer.parseInt(headers.getOrDefault("Content-Length", "0"));
        if (size != 0) {
            char[] buffer = new char[size];
            br.read(buffer, 0, size);
            body = URLDecoder.decode(new String(buffer), "UTF-8");
        }
    }

    private String[] validateRequestLine(String requestLine) {
        if (requestLine == null || requestLine.isBlank()) {
            throw new HttpCommonException("요청 라인이 없습니다.", StatusCode.BAD_REQUEST);
        }
        String[] tokens = requestLine.split(" ");
        if (tokens.length != 3) {
            throw new HttpCommonException("요청 라인이 올바르지 않습니다.", StatusCode.BAD_REQUEST);
        }
        return tokens;
    }

}