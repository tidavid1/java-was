package codesquad.server.http.servlet;

import java.net.HttpCookie;
import java.util.Arrays;
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
        List<String> values = request.getHeader("Cookie");
        if (values.isEmpty()) {
            return Optional.empty();
        }
        String[] cookies = request.getHeader("Cookie").get(0).split(";");
        return Arrays.stream(cookies)
            .flatMap(cookie -> HttpCookie.parse(cookie.trim()).stream())
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
