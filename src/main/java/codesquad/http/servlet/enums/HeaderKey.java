package codesquad.http.servlet.enums;

public enum HeaderKey {
    LOCATION("Location"), CONTENT_TYPE("Content-Type"), CONTENT_LENGTH("Content-Length"),
    DATE("Date"), SET_COOKIE("Set-Cookie"), SERVER("Server"), COOKIE("Cookie");

    private final String value;

    HeaderKey(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
