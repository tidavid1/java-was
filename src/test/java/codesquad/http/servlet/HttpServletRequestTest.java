package codesquad.http.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.HttpCookie;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("HttpServletRequest는")
class HttpServletRequestTest {

    private final HttpRequest httpRequest = new HttpRequest(
        new String[]{"GET", "/index.html", "HTTP/1.1"},
        Map.of("Cookie", List.of("cookie=value")),
        "");

    @Nested
    @DisplayName("생성 후")
    class whenCreated {

        @Test
        @DisplayName("HttpRequest 객체를 반환한다.")
        void getRequest() {
            // Arrange
            HttpServletRequest httpServletRequest = new HttpServletRequest(httpRequest);
            // Act
            HttpRequest actualResult = httpServletRequest.getRequest();
            // Assert
            assertThat(actualResult).isEqualTo(httpRequest);
        }

        @Test
        @DisplayName("쿠키를 반환한다.")
        void getCookie() {
            // Arrange
            HttpServletRequest httpServletRequest = new HttpServletRequest(httpRequest);
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
            HttpServletRequest httpServletRequest = new HttpServletRequest(httpRequest);
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
            HttpServletRequest httpServletRequest = new HttpServletRequest(httpRequest);
            httpServletRequest.setAttribute(expectedKey, expectedValue);
            // Act
            Object actualResult = httpServletRequest.getAttribute(expectedKey);
            // Assert
            assertEquals(expectedValue, actualResult);
        }
    }
}