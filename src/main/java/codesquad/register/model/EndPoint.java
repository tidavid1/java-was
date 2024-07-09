package codesquad.register.model;

import codesquad.http.HttpResponse;
import java.util.Objects;
import java.util.function.Function;

public class EndPoint<T> {

    private final String path;
    private final Function<T, HttpResponse> function;

    private EndPoint(String path, Function<T, HttpResponse> function) {
        this.path = path;
        this.function = Objects.requireNonNull(function, "Function은 null일 수 없습니다.");
    }

    public static <T> EndPoint<T> of(String path, Function<T, HttpResponse> function) {
        return new EndPoint<>(path, function);
    }

    public String getPath() {
        return path;
    }

    public Function<T, HttpResponse> getFunction() {
        return function;
    }

    public HttpResponse apply(T value) {
        return function.apply(value);
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
