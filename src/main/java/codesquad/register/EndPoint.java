package codesquad.register;

import java.util.Objects;
import java.util.function.Supplier;

public class EndPoint {

    private final String path;
    private final Supplier<byte[]> supplier;

    public EndPoint(String path, Supplier<byte[]> supplier) {
        this.path = path;
        this.supplier = supplier;
    }

    public byte[] get() {
        return supplier.get();
    }

    public String getPath() {
        return path;
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
