package codesquad.server.http.parser;

import codesquad.server.http.exception.HttpCommonException;
import codesquad.server.http.servlet.HttpServletRequest;
import codesquad.server.http.servlet.MultiPartHttpRequest;
import codesquad.server.http.servlet.SingleHttpRequest;
import codesquad.server.http.servlet.enums.StatusCode;
import codesquad.server.http.servlet.values.HttpRequestPart;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestParser {

    private static final Logger log = LoggerFactory.getLogger(HttpRequestParser.class);
    private static final String REQUEST_LINE_DELIMITER = " ";
    private static final String HEADER_DELIMITER = ":";

    public HttpServletRequest parse(InputStream inputStream) throws IOException {
        HttpServletRequest servletRequest = new HttpServletRequest();
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            Queue<String> lines = parseBytesToRequestLineAndHeaders(bufferedInputStream);
            String[] requestLinePart = parseRequestLine(lines.poll());
            Map<String, List<String>> headers = parseHeaders(lines);
            boolean multiPartYn = verifyMultiPart(headers.getOrDefault("Content-Type", List.of()));
            int contentLength = Integer.parseInt(
                headers.getOrDefault("Content-Length", List.of("0")).get(0));
            servletRequest.setRequest(
                multiPartYn ? MultiPartHttpRequest.of(requestLinePart, headers,
                    parseMultiPart(bufferedInputStream, headers.get("Content-Type"), contentLength))
                    : SingleHttpRequest.of(requestLinePart, headers,
                        parseSingleBody(bufferedInputStream, contentLength)));
        } catch (HttpCommonException e) {
            servletRequest.setAttribute("exception", e);
        }
        return servletRequest;
    }

    private Queue<String> parseBytesToRequestLineAndHeaders(BufferedInputStream bufferedInputStream)
        throws IOException {
        Queue<String> queue = new LinkedList<>();
        boolean flag = true;
        int length = 0;
        // Initial Marking
        bufferedInputStream.mark(1024);
        while (flag) {
            int value = bufferedInputStream.read();
            length += 1;
            if (value == -1) {
                // Reset Marking
                bufferedInputStream.reset();
                // set lineBuffer
                byte[] lineBuffer = new byte[length];
                // read n byte
                int size = bufferedInputStream.read(lineBuffer, 0, length);
                // convert byte to String
                String line = new String(lineBuffer).trim();
                queue.add(line);
                flag = false;
            }
            if (value == '\n') {
                // Reset Marking
                bufferedInputStream.reset();
                // set lineBuffer
                byte[] lineBuffer = new byte[length];
                // read n byte
                int size = bufferedInputStream.read(lineBuffer, 0, length);
                // convert byte to String
                String line = new String(lineBuffer, 0, size).trim();
                // verify EOH (End Of Headers)
                if (line.isBlank()) {
                    flag = false;
                    continue;
                }
                queue.add(line);
                bufferedInputStream.mark(1024);
                length = 0;
            }
        }
        return queue;
    }

    private String[] parseRequestLine(String requestLine) {
        log.debug("Request Line: {}", requestLine);
        if (requestLine == null || requestLine.isBlank()) {
            throw new HttpCommonException("요청 라인이 올바르지 않습니다.", StatusCode.BAD_REQUEST);
        }
        String[] parts = requestLine.split(REQUEST_LINE_DELIMITER);
        if (parts.length != 3) {
            throw new HttpCommonException("요청 라인이 올바르지 않습니다.", StatusCode.BAD_REQUEST);
        }
        return parts;
    }

    private Map<String, List<String>> parseHeaders(Queue<String> headersQueue) {
        Map<String, List<String>> headers = new HashMap<>();
        while (!headersQueue.isEmpty()) {
            String line = headersQueue.poll();
            log.debug("Header: {}", line);
            int idx = line.indexOf(HEADER_DELIMITER);
            if (idx == -1) {
                throw new HttpCommonException("헤더가 올바르지 않습니다.", StatusCode.BAD_REQUEST);
            }
            String key = line.substring(0, idx).trim();
            String[] values = line.substring(idx + 1).trim().split(",");
            for (String value : values) {
                headers.computeIfAbsent(key, k -> new ArrayList<>()).add(value.trim());
            }
        }
        return headers;
    }

    private String parseSingleBody(BufferedInputStream bufferedInputStream, int contentLength)
        throws IOException {
        byte[] buffer = new byte[contentLength];
        if (bufferedInputStream.read(buffer, 0, contentLength) == -1) {
            throw new HttpCommonException("요청 바디가 없습니다.", StatusCode.BAD_REQUEST);
        }
        return URLDecoder.decode(new String(buffer), "UTF-8");
    }

    private List<HttpRequestPart> parseMultiPart(BufferedInputStream bufferedInputStream,
        List<String> contentTypeValue, int contentLength) throws IOException {
        bufferedInputStream.mark(1024);
        bufferedInputStream.reset();
        String bounder = contentTypeValue.get(0);
        bounder = "--" + bounder.substring(bounder.indexOf("boundary=") + 9);
        byte[] buffer = new byte[contentLength];
        if (bufferedInputStream.read(buffer, 0, contentLength) == -1) {
            throw new HttpCommonException("요청 바디가 없습니다.", StatusCode.BAD_REQUEST);
        }
        List<HttpRequestPart> parts = new ArrayList<>();
        int start = 0;
        Queue<String> queue = new LinkedList<>();
        for (int i = 0; i < buffer.length; i++) {
            if (buffer[i] == '\n') {
                byte[] temp = new byte[i - start + 1];
                System.arraycopy(buffer, start, temp, 0, temp.length);
                String value = new String(temp).trim();
                start = i++ + 1;
                if (value.isBlank()) {
                    String bounderStr = queue.poll();
                    if (!bounder.contains(bounderStr)) {
                        throw new HttpCommonException("잘못된 바운더리 요청입니다", StatusCode.BAD_REQUEST);
                    }
                    Map<String, List<String>> subHeader = parseHeaders(queue);
                    i = indexOfBounder(bounder, buffer, i);
                    byte[] body = new byte[(i - start) - (buffer[i - 2] == '\r' ? 2 : 1)];
                    System.arraycopy(buffer, start, body, 0, body.length);
                    parts.add(HttpRequestPart.from(subHeader, body));
                    start = i;
                    continue;
                }
                queue.add(value);
            }
        }
        return parts;
    }

    private boolean verifyMultiPart(List<String> contentTypes) {
        return contentTypes.stream().anyMatch(contentType -> contentType.contains("multipart"));
    }

    private int indexOfBounder(String bounder, byte[] body, int start) {
        byte[] bounderBytes = bounder.getBytes();
        loop:
        for (int i = start; i <= body.length - bounderBytes.length; i++) {
            for (int j = 0; j < bounderBytes.length; j++) {
                if (body[i + j] != bounderBytes[j]) {
                    continue loop;
                }
            }
            return i;
        }
        return -1;
    }
}
