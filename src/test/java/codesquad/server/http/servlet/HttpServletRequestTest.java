package codesquad.server.http.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.HttpCookie;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("HttpServletRequest는")
class HttpServletRequestTest {

    private final SingleHttpRequest httpRequest = SingleHttpRequest.of(
        new String[]{"GET", "/index.html", "HTTP/1.1"},
        Map.of("Cookie", List.of("cookie=value")),
        "");

    private HttpServletRequest httpServletRequest;

    @BeforeEach
    void init() {
        httpServletRequest = new HttpServletRequest();
        httpServletRequest.setRequest(httpRequest);
    }

    @Nested
    @DisplayName("생성 후")
    class whenCreated {

        @Test
        @DisplayName("HttpRequest 객체를 반환한다.")
        void getRequest() {
            // Act
            HttpRequest actualResult = httpServletRequest.getRequest();
            // Assert
            assertThat(actualResult).isEqualTo(httpRequest);
        }

        @Test
        @DisplayName("쿠키를 반환한다.")
        void getCookie() {
            // Act
            Optional<HttpCookie> actualResult = httpServletRequest.getCookie("cookie");
            // Assert
            assertThat(actualResult).isPresent()
                .get().extracting(HttpCookie::getValue).isEqualTo("value");
        }

        @Test
        @DisplayName("속성을 설정한다.")
        void setAttribute() {
            // Arrange
            String expectedKey = "key";
            String expectedValue = "value";
            // Act
            httpServletRequest.setAttribute(expectedKey, expectedValue);
            // Assert
            assertThat(httpServletRequest).extracting("attributes.key").asString()
                .isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("속성을 반환한다.")
        void getAttribute() {
            // Arrange
            String expectedKey = "key";
            String expectedValue = "value";
            httpServletRequest.setAttribute(expectedKey, expectedValue);
            // Act
            Object actualResult = httpServletRequest.getAttribute(expectedKey);
            // Assert
            assertEquals(expectedValue, actualResult);
        }
    }
}