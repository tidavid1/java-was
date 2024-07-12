package codesquad.server.http.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import codesquad.server.http.servlet.enums.StatusCode;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("HttpResponse는")
class HttpResponseTest {

    @Test
    @DisplayName("생성시 기본 해더만 갖고 있다.")
    void create() {
        // Act
        HttpResponse actualResult = new HttpResponse();
        // Assert
        assertThat(actualResult).extracting("headers")
            .isInstanceOf(Map.class)
            .extracting("Content-Length", "Date", "Server").isNotNull();
    }

    @Nested
    @DisplayName("생성 후")
    class whenCreated {

        @Test
        @DisplayName("상태 코드를 설정할 수 있다.")
        void setStatusCode() {
            // Arrange
            HttpResponse httpResponse = new HttpResponse();
            // Act
            httpResponse.setStatusCode(StatusCode.OK);
            // Assert
            assertThat(httpResponse.getStatusCode()).isEqualTo(StatusCode.OK);
        }

        @Test
        @DisplayName("바디를 설정할 수 있다.")
        void setBody() {
            // Arrange
            HttpResponse httpResponse = new HttpResponse();
            byte[] expectedBody = {1, 2, 3, 4, 5};
            // Act
            httpResponse.setBody(expectedBody);
            // Assert
            assertAll(
                () -> assertThat(httpResponse).extracting("body").isEqualTo(expectedBody),
                () -> assertThat(httpResponse).extracting("headers").extracting("Content-Length")
                    .asString().isEqualTo("[5]")
            );
        }

        @Test
        @DisplayName("해더를 추가할 수 있다.")
        void addHeader() {
            // Arrange
            HttpResponse httpResponse = new HttpResponse();
            // Act
            httpResponse.addHeader("Content-Type", "text/html");
            // Assert
            assertThat(httpResponse).extracting("headers").extracting("Content-Type")
                .asString().isEqualTo("[text/html]");
        }

    }

    @Test
    @DisplayName("구성 후 바디를 포함한 응답 바이트 배열을 반환할 수 있다.")
    void toResponseBytes() {
        // Arrange
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(StatusCode.OK);
        httpResponse.addHeader("Content-Type", "text/html");
        httpResponse.setBody("Hello, World!".getBytes());
        // Act
        byte[] actualResult = httpResponse.toResponseBytes();
        // Assert
        assertThat(actualResult).isNotNull();
    }
}