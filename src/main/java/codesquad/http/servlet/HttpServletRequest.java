package codesquad.http.servlet;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpServletRequest {

    private final HttpRequest request;
    private final Map<String, Object> attributes;

    public HttpServletRequest(HttpRequest request) {
        this.request = request;
        this.attributes = new HashMap<>();
    }

    public HttpRequest getRequest() {
        return request;
    }

    public Optional<HttpCookie> getCookie(String name) {
        return request.getHeaders().get("Cookie").stream()
            .flatMap(cookie -> HttpCookie.parse(cookie).stream())
            .filter(httpCookie -> httpCookie.getName().equals(name))
            .findFirst();
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }
}
