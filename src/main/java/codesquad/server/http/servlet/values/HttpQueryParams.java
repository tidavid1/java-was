package codesquad.server.http.servlet.values;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpQueryParams {

    private final Map<String, String> queryParams;

    private HttpQueryParams(String queryString) {
        this.queryParams = parseQueryParams(queryString);
    }

    public static HttpQueryParams from(String queryString) {
        return new HttpQueryParams(queryString);
    }

    public Optional<String> getParameter(String key) {
        return Optional.ofNullable(queryParams.get(key));
    }

    public Map<String, String> getAllParams() {
        return Collections.unmodifiableMap(queryParams);
    }

    private Map<String, String> parseQueryParams(final String queryString) {
        Map<String, String> params = new HashMap<>();
        if (queryString == null || queryString.isBlank()) {
            return params;
        }
        String[] queryParts = queryString.split("&");
        for (String queryPart : queryParts) {
            String[] keyValue = queryPart.split("=");
            String value = keyValue.length > 1 ? keyValue[1] : "";
            params.put(keyValue[0], value);
        }
        return params;
    }

}
