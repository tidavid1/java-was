package codesquad.http.servlet;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HttpServletRequest {

    private HttpRequest request;
    private final Map<String, Object> attributes;

    public HttpServletRequest() {
        this.attributes = new HashMap<>();
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public HttpRequest getRequest() {
        return request;
    }

    public Optional<HttpCookie> getCookie(String name) {
        return request.getHeaders().getOrDefault("Cookie", List.of()).stream()
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
