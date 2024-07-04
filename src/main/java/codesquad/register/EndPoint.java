package codesquad.register;

import codesquad.formatter.ContentTypeFormatter;
import java.util.Objects;
import java.util.function.Function;

public class EndPoint {

    private final String path;
    private final Function<String, byte[]> function;
    private final String contentType;

    public EndPoint(String path, Function<String, byte[]> function) {
        this.path = path;
        this.function = function;
        this.contentType = ContentTypeFormatter.formatContentType(path);
    }

    public EndPoint(String path, Function<String, byte[]> function, String contentType) {
        this.path = path;
        this.function = function;
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
