package codesquad.http;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import codesquad.http.enums.StatusCode;
import codesquad.register.EndPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpResponseTest {

    @Test
    @DisplayName("HttpResponse 객체를 생성한다.")
    void createHttpResponse() {
        // Arrange
        var statusCode = StatusCode.OK;
        // Act
        var actualResult = HttpResponse.from(statusCode);
        // Assert
        assertAll(
            () -> assertNotNull(actualResult),
            () -> assertThat(actualResult).isInstanceOf(HttpResponse.class),
            () -> assertThat(actualResult).extracting("statusCode").isEqualTo(statusCode)
        );
    }

    @Test
    @DisplayName("HttpResponse 객체를 생성한다.")
    void createHttpResponseWithoutBody() {
        // Arrange
        var endPoint = new EndPoint("/index.html", query -> new byte[0]);
        String query = null;
        // Act
        var actualResult = HttpResponse.of(endPoint, query);
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
        var httpResponse = HttpResponse.from(statusCode);
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
        var httpResponse = HttpResponse.from(statusCode);
        // Act
        var actualResult = httpResponse.toResponseBytes();
        // Assert
        assertThat(actualResult).isNotNull();
    }
}