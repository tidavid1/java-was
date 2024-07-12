package codesquad.server.http.servlet.enums;

public enum HttpVersion {
    HTTP_1_1("HTTP/1.1");

    private final String value;

    HttpVersion(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static HttpVersion from(String version) {
        for (HttpVersion httpVersion : values()) {
            if (httpVersion.value.equals(version)) {
                return httpVersion;
            }
        }
        throw new IllegalArgumentException("지원하지 않는 HTTP 버전입니다.");
    }
}
