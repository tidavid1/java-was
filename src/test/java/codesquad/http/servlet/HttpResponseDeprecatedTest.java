package codesquad.http.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import codesquad.http.servlet.enums.HeaderKey;
import codesquad.http.servlet.enums.StatusCode;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class HttpResponseDeprecatedTest {

    @Nested
    @DisplayName("HttpResponse 객체를 생성할 때")
    class whenCreateHttpResponseDeprecated {

        @Test
        @DisplayName("정적 팩토리 메서드 from을 사용하여 HttpResponse 객체를 생성한다.")
        void createHttpResponseWithFrom() {
            // Arrange
            var statusCode = StatusCode.OK;
            // Act
            var actualResult = HttpResponseDeprecated.from(statusCode);
            // Assert
            assertAll(
                () -> assertNotNull(actualResult),
                () -> assertThat(actualResult).isInstanceOf(HttpResponseDeprecated.class),
                () -> assertThat(actualResult).extracting("statusCode").isEqualTo(statusCode)
            );
        }

        @Test
        @DisplayName("정적 팩토리 메서드 of를 사용하여 HttpResponse 객체를 생성한다.")
        void createHttpResponseWithOf() {
            // Arrange
            byte[] expectedBody = {1, 2, 3, 4, 5};
            StatusCode expectedStatusCode = StatusCode.OK;
            // Act
            var actualResult = HttpResponseDeprecated.of(expectedStatusCode, expectedBody);
            // Assert
            assertAll(
                () -> assertNotNull(actualResult),
                () -> assertThat(actualResult).isInstanceOf(HttpResponseDeprecated.class),
                () -> assertThat(actualResult).extracting("statusCode")
                    .isEqualTo(expectedStatusCode),
                () -> assertThat(actualResult).extracting("body").isEqualTo(expectedBody)
            );
        }

    }

    @Nested
    @DisplayName("생성된 HttpResponse 객체에 대해")
    class createdHttpResponseDeprecated {

        @Test
        @DisplayName("기본 해더를 추가한다.")
        void addHeader() {
            // Arrange
            var statusCode = StatusCode.OK;
            var httpResponse = HttpResponseDeprecated.from(statusCode);
            // Act
            httpResponse.addHeader(HeaderKey.CONTENT_TYPE, "text/html");
            // Assert
            assertThat(httpResponse).extracting("headers")
                .hasFieldOrPropertyWithValue("Content-Type", "text/html");
        }

        @Test
        @DisplayName("여러 개의 해더를 추가한다.")
        void addHeaders() {
            // Arrange
            var statusCode = StatusCode.OK;
            var httpResponse = HttpResponseDeprecated.from(statusCode);
            // Act
            httpResponse.addHeaders(
                Map.of(
                    HeaderKey.CONTENT_TYPE, "text/html",
                    HeaderKey.CONTENT_LENGTH, "100"
                )
            );
            // Assert
            assertThat(httpResponse).extracting("headers")
                .hasFieldOrPropertyWithValue("Content-Type", "text/html")
                .hasFieldOrPropertyWithValue("Content-Length", "100");
        }

    }

    @Test
    @DisplayName("HttpResponse 객체를 byte[]로 변환한다.")
    void convertToResponseBytes() {
        // Arrange
        var statusCode = StatusCode.OK;
        var httpResponse = HttpResponseDeprecated.from(statusCode);
        // Act
        var actualResult = httpResponse.toResponseBytes();
        // Assert
        assertThat(actualResult).isNotNull();
    }
}