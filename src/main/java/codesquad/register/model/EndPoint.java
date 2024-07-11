package codesquad.register.model;

import codesquad.http.servlet.HttpResponseDeprecated;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

public class EndPoint<T> {

    private final String path;
    private final BiFunction<Map<String, String>, T, HttpResponseDeprecated> biFunction;

    private EndPoint(String path,
        BiFunction<Map<String, String>, T, HttpResponseDeprecated> biFunction) {
        this.path = path;
        this.biFunction = Objects.requireNonNull(biFunction, "BiFunction은 null일 수 없습니다.");
    }

    public static <T> EndPoint<T> of(String path,
        BiFunction<Map<String, String>, T, HttpResponseDeprecated> biFunction) {
        return new EndPoint<>(path, biFunction);
    }

    public String getPath() {
        return path;
    }

    public BiFunction<Map<String, String>, T, HttpResponseDeprecated> getBiFunction() {
        return biFunction;
    }

    public HttpResponseDeprecated apply(Map<String, String> headers, T value) {
        return biFunction.apply(headers, value);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EndPoint<?> endPoint)) {
            return false;
        }

        return Objects.equals(getPath(), endPoint.getPath());
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }
}
