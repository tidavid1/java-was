package codesquad.exception;

import codesquad.http.servlet.enums.StatusCode;

public class HttpCommonException extends RuntimeException {

    private final StatusCode statusCode;

    public HttpCommonException(String message, StatusCode statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }
}
