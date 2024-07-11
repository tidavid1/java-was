package codesquad.http.servlet;

import codesquad.http.servlet.enums.StatusCode;
import java.net.HttpCookie;

public class HttpServletResponse {

    private final HttpResponse httpResponse = new HttpResponse();

    public void setContentType(String contentType) {
        if (contentType.contains("text")) {
            contentType += "; charset=utf-8";
        }
        httpResponse.addHeader("Content-Type", contentType);
    }

    public void setStatus(StatusCode statusCode) {
        if (httpResponse.getStatusCode() != null) {
            throw new IllegalStateException("상태 코드는 한 번만 설정할 수 있습니다.");
        }
        httpResponse.setStatusCode(statusCode);
    }

    public void setHeader(String key, String value) {
        httpResponse.addHeader(key, value);
    }

    public void setCookie(HttpCookie cookie) {
        httpResponse.addHeader("Set-Cookie", cookie.toString());
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
}
