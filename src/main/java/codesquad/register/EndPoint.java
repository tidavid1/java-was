package codesquad.register;

import codesquad.formatter.ContentTypeFormatter;
import codesquad.http.enums.StatusCode;
import java.util.Objects;
import java.util.function.Function;

public class EndPoint {

    private final String path;
    private final Function<String, byte[]> function;
    private final StatusCode statusCode;
    private String contentType;
    private String redirectUri;

    public EndPoint(String path, Function<String, byte[]> function) {
        this(path, function, ContentTypeFormatter.formatContentType(path));
    }

    public EndPoint(String path, Function<String, byte[]> function, String contentType) {
        this(path, function, contentType, StatusCode.OK);
    }

    public EndPoint(String path, Function<String, byte[]> function, String contentType,
        StatusCode statusCode) {
        this.path = path;
        this.function = function;
        this.statusCode = statusCode;
        this.contentType = contentType;
    }

    public String getPath() {
        return path;
    }

    public Function<String, byte[]> getFunction() {
        return function;
    }

    public String getContentType() {
        return contentType;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EndPoint endPoint)) {
            return false;
        }

        return Objects.equals(getPath(), endPoint.getPath());
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }
}
