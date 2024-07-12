package codesquad.server.http.servlet;

import codesquad.server.http.servlet.enums.StatusCode;
import codesquad.server.util.HttpCookieReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.util.Optional;

public class HttpServletResponse {

    private final HttpResponse httpResponse = new HttpResponse();

    public void setContentType(String contentType) {
        if (contentType.contains("text")) {
            contentType += "; charset=utf-8";
        }
        httpResponse.addHeader("Content-Type", contentType);
    }

    public Optional<StatusCode> getStatus() {
        return Optional.ofNullable(httpResponse.getStatusCode());
    }

    public void setStatus(StatusCode statusCode) {
        httpResponse.setStatusCode(statusCode);
    }

    public void setHeader(String key, String value) {
        httpResponse.addHeader(key, value);
    }

    public void setCookie(HttpCookie cookie) {
        httpResponse.addHeader("Set-Cookie", HttpCookieReader.readCookie(cookie));
    }

    public void sendRedirect(String path) {
        setStatus(StatusCode.FOUND);
        setHeader("Location", path);
    }

    public void setBody(String value) {
        httpResponse.setBody(value.getBytes());
    }

    public void setBody(byte[] body) {
        httpResponse.setBody(body);
    }

    public byte[] toResponseBytes() {
        return httpResponse.toResponseBytes();
    }

    public void flush(OutputStream os) throws IOException {
        os.write(toResponseBytes());
        os.flush();
    }

}
