package codesquad.http;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import codesquad.http.enums.StatusCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpResponseTest {

    @Test
    @DisplayName("HttpResponse 객체를 생성한다.")
    void createHttpResponse() {
        // Arrange
        var statusCode = StatusCode.OK;
        var body = new byte[0];
        // Act
        var actualResult = new HttpResponse(statusCode, body);
        // Assert
        assertAll(
            () -> assertNotNull(actualResult),
            () -> assertThat(actualResult).isInstanceOf(HttpResponse.class)
        );
    }

    @Test
    @DisplayName("HttpResponse 객체를 생성한다.")
    void createHttpResponseWithoutBody() {
        // Arrange
        var statusCode = StatusCode.OK;
        // Act
        var actualResult = new HttpResponse(statusCode);
        // Assert
        assertAll(
            () -> assertNotNull(actualResult),
            () -> assertThat(actualResult).isInstanceOf(HttpResponse.class)
        );
    }

    @Test
    @DisplayName("생성된 HttpResponse 객체에 해더를 추가한다.")
    void addHeader() {
        // Arrange
        var statusCode = StatusCode.OK;
        var body = new byte[0];
        var httpResponse = new HttpResponse(statusCode, body);
        // Act
        httpResponse.addHeader("Content-Type", "text/html");
        // Assert
        assertThat(httpResponse).extracting("headers")
            .hasFieldOrPropertyWithValue("Content-Type", "text/html");
    }

    @Test
    @DisplayName("HttpResponse 객체를 byte[]로 변환한다.")
    void convertToResponseBytes() {
        // Arrange
        var statusCode = StatusCode.OK;
        var body = new byte[0];
        var httpResponse = new HttpResponse(statusCode, body);
        // Act
        var actualResult = httpResponse.toResponseBytes();
        // Assert
        assertThat(actualResult).isNotNull();
    }
}