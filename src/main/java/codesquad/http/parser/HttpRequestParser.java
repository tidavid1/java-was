package codesquad.http.parser;

import codesquad.exception.HttpCommonException;
import codesquad.http.servlet.HttpRequest;
import codesquad.http.servlet.HttpServletRequest;
import codesquad.http.servlet.enums.StatusCode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequestParser {

    public static final String REQUEST_LINE_DELIMITER = " ";
    public static final String HEADER_DELIMITER = ": ";

    public HttpServletRequest parse(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        String[] requestLineParts = parseRequestLine(br.readLine());
        Map<String, List<String>> headers = parseHeaders(br);
        String body = parseBody(br,
            Integer.parseInt(headers.getOrDefault("Content-Length", List.of("0")).get(0)));
        return new HttpServletRequest(new HttpRequest(requestLineParts, headers, body));
    }

    private String[] parseRequestLine(String requestLine) {
        if (requestLine == null || requestLine.isBlank()) {
            throw new HttpCommonException("요청 라인이 없습니다.", StatusCode.BAD_REQUEST);
        }
        String[] parts = requestLine.split(REQUEST_LINE_DELIMITER);
        if (parts.length != 3) {
            throw new HttpCommonException("요청 라인이 올바르지 않습니다.", StatusCode.BAD_REQUEST);
        }
        return parts;
    }

    private Map<String, List<String>> parseHeaders(BufferedReader br) throws IOException {
        String line;
        Map<String, List<String>> headers = new HashMap<>();
        while ((line = br.readLine()) != null && !line.isBlank()) {
            String[] headerParts = line.split(HEADER_DELIMITER);
            if (headerParts.length != 2) {
                throw new HttpCommonException("헤더가 올바르지 않습니다.", StatusCode.BAD_REQUEST);
            }
            String key = headerParts[0].trim();
            String value = headerParts[1].trim();
            headers.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
        }
        return headers;
    }

    private String parseBody(BufferedReader br, int contentLength) throws IOException {
        char[] buffer = new char[contentLength];
        if (br.read(buffer, 0, contentLength) == -1) {
            throw new HttpCommonException("요청 바디가 없습니다.", StatusCode.BAD_REQUEST);
        }
        return new String(buffer);
    }

}
