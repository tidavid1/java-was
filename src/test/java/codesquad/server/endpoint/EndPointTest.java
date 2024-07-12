package codesquad.server.endpoint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import codesquad.server.http.servlet.HttpServletRequest;
import codesquad.server.http.servlet.HttpServletResponse;
import codesquad.server.http.servlet.enums.StatusCode;
import java.util.function.BiConsumer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("EndPoint는")
class EndPointTest {

    @Nested
    @DisplayName("생성 시")
    class whenCreate {

        @Test
        @DisplayName("정상적으로 생성된다.")
        void create() {
            // Arrange
            String path = "/index.html";
            BiConsumer<HttpServletRequest, HttpServletResponse> biConsumer = (request, response) -> {
            };
            // Act
            EndPoint actualResult = EndPoint.of(path, biConsumer);
            // Assert
            assertThat(actualResult).extracting("path", "biConsumer")
                .containsExactly(path, biConsumer);
        }

        @Test
        @DisplayName("BiConsumer가 null이면 예외를 던진다.")
        void willThrowExceptionWhenBiConsumerIsNull() {
            // Arrange
            String path = "/index.html";
            BiConsumer<HttpServletRequest, HttpServletResponse> biConsumer = null;
            // Act & Assert
            assertThatThrownBy(() -> EndPoint.of(path, biConsumer))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("BiConsumer는 null일 수 없습니다.");
        }
    }

    @Test
    @DisplayName("path가 같으면 같은 객체이다.")
    void equals() {
        // Arrange
        String path = "/index.html";
        BiConsumer<HttpServletRequest, HttpServletResponse> biConsumer = (request, response) -> {
        };
        EndPoint endPoint1 = EndPoint.of(path, biConsumer);
        EndPoint endPoint2 = EndPoint.of(path, biConsumer);
        // Act & Assert
        assertThat(endPoint1).isEqualTo(endPoint2);
    }

    @Test
    @DisplayName("path가 다르면 다른 객체이다.")
    void notEquals() {
        // Arrange
        String path1 = "/index.html";
        String path2 = "/index2.html";
        BiConsumer<HttpServletRequest, HttpServletResponse> biConsumer = (request, response) -> {
        };
        EndPoint endPoint1 = EndPoint.of(path1, biConsumer);
        EndPoint endPoint2 = EndPoint.of(path2, biConsumer);
        // Act & Assert
        assertThat(endPoint1).isNotEqualTo(endPoint2);
    }

    @Test
    @DisplayName("apply 메서드를 실행하면 BiConsumer가 실행된다.")
    void apply() {
        // Arrange
        String path = "/index.html";
        HttpServletRequest request = new HttpServletRequest();
        HttpServletResponse response = new HttpServletResponse();
        BiConsumer<HttpServletRequest, HttpServletResponse> biConsumer = (httpServletRequest, httpServletResponse) -> {
            request.setAttribute("key", "value");
            response.setStatus(StatusCode.OK);
        };
        EndPoint endPoint = EndPoint.of(path, biConsumer);
        // Act
        endPoint.accept(request, response);
        // Assert
        assertThat(request.getAttribute("key")).isEqualTo("value");
        assertThat(response.getStatus()).isPresent().get().isEqualTo(StatusCode.OK);
    }

}