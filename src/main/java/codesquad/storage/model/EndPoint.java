package codesquad.storage.model;

import codesquad.http.servlet.HttpServletRequest;
import codesquad.http.servlet.HttpServletResponse;
import java.util.Objects;
import java.util.function.BiConsumer;

public class EndPoint {

    private final String path;
    private final BiConsumer<HttpServletRequest, HttpServletResponse> biConsumer;

    private EndPoint(String path, BiConsumer<HttpServletRequest, HttpServletResponse> biConsumer) {
        this.path = path;
        this.biConsumer = Objects.requireNonNull(biConsumer, "BiConsumer는 null일 수 없습니다.");
    }

    public static EndPoint of(String path,
        BiConsumer<HttpServletRequest, HttpServletResponse> biConsumer) {
        return new EndPoint(path, biConsumer);
    }

    public String getPath() {
        return path;
    }

    public void accept(HttpServletRequest request, HttpServletResponse response) {
        biConsumer.accept(request, response);
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
        return Objects.hashCode(getPath());
    }
}
