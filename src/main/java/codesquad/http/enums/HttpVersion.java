package codesquad.http.enums;

public enum HttpVersion {
    HTTP_1_1("HTTP/1.1");

    private final String version;

    HttpVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public static HttpVersion from(String version) {
        for (HttpVersion httpVersion : values()) {
            if (httpVersion.version.equals(version)) {
                return httpVersion;
            }
        }
        throw new IllegalArgumentException("지원하지 않는 HTTP 버전입니다.");
    }
}
