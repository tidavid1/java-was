package codesquad.http.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import codesquad.http.servlet.enums.StatusCode;
import java.net.HttpCookie;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("HttpServletResponse는")
class HttpServletResponseTest {

    @Nested
    @DisplayName("생성 후")
    class whenCreated {

        @Test
        @DisplayName("상태 코드를 설정할 수 있다.")
        void setStatusCode() {
            // Arrange
            HttpServletResponse httpResponse = new HttpServletResponse();
            // Act
            httpResponse.setStatus(StatusCode.OK);
            // Assert
            assertThat(httpResponse.getStatus()).isPresent().get().isEqualTo(StatusCode.OK);
        }

        @MethodSource
        @ParameterizedTest
        @DisplayName("컨텐츠 타입을 설정할 수 있다.")
        void setContentType(String contentType, String expectedContentType) {
            // Arrange
            HttpServletResponse httpResponse = new HttpServletResponse();
            // Act
            httpResponse.setContentType(contentType);
            // Assert
            assertThat(httpResponse).extracting("httpResponse.headers.Content-Type").asString()
                .isEqualTo(expectedContentType);
        }

        private static Stream<Arguments> setContentType() {
            return Stream.of(
                Arguments.of("text/html", "[text/html; charset=utf-8]"),
                Arguments.of("application/json", "[application/json]")
            );
        }

        @Test
        @DisplayName("해더를 추가할 수 있다.")
        void addHeader() {
            // Arrange
            HttpServletResponse httpResponse = new HttpServletResponse();
            // Act
            httpResponse.setHeader("hello", "world");
            // Assert
            assertThat(httpResponse).extracting("httpResponse.headers.hello").asString()
                .isEqualTo("[world]");
        }

        @Test
        @DisplayName("쿠키를 설정할 수 있다.")
        void setCookie() {
            // Arrange
            HttpServletResponse httpResponse = new HttpServletResponse();
            // Act
            httpResponse.setCookie(new HttpCookie("name", "value"));
            // Assert
            assertThat(httpResponse).extracting("httpResponse.headers.Set-Cookie").asString()
                .isEqualTo("[name=value; Version=1]");
        }

        @Test
        @DisplayName("리다이렉트를 수행할 수 있다.")
        void sendRedirect() {
            // Arrange
            HttpServletResponse httpResponse = new HttpServletResponse();
            // Act
            httpResponse.sendRedirect("/redirect");
            // Assert
            assertThat(httpResponse).extracting("httpResponse.statusCode")
                .isEqualTo(StatusCode.FOUND);
            assertThat(httpResponse).extracting("httpResponse.headers.Location").asString()
                .isEqualTo("[/redirect]");
        }

        @Test
        @DisplayName("바디를 설정할 수 있다.")
        void setBody() {
            // Arrange
            HttpServletResponse httpResponse = new HttpServletResponse();
            byte[] expectedBody = {1, 2, 3, 4, 5};
            // Act
            httpResponse.setBody(expectedBody);
            // Assert
            assertAll(
                () -> assertThat(httpResponse).extracting("httpResponse.body")
                    .isEqualTo(expectedBody),
                () -> assertThat(httpResponse).extracting("httpResponse.headers.Content-Length")
                    .asString().isEqualTo("[5]")
            );
        }

    }

    @Test
    @DisplayName("이미 설정한 상태 코드를 변경하려고 할 때 예외를 발생시킨다.")
    void setStatusCodeTwice() {
        // Arrange
        HttpServletResponse httpResponse = new HttpServletResponse();
        // Act
        httpResponse.setStatus(StatusCode.OK);
        // Assert
        assertThatThrownBy(() -> httpResponse.setStatus(StatusCode.BAD_REQUEST))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("상태 코드는 한 번만 설정할 수 있습니다.");
    }

    @Test
    @DisplayName("구성 후 바디를 포함한 응답 바이트 배열을 반환할 수 있다.")
    void toResponseBytes() {
        // Arrange
        HttpServletResponse httpResponse = new HttpServletResponse();
        httpResponse.setStatus(StatusCode.OK);
        httpResponse.setContentType("text/html");
        httpResponse.setBody("Hello, world!");
        // Act
        byte[] actualResult = httpResponse.toResponseBytes();
        // Assert
        assertThat(actualResult).isNotNull();
    }
}