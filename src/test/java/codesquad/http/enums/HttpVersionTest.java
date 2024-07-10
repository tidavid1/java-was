package codesquad.http.enums;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class HttpVersionTest {

    @Nested
    @DisplayName("HttpVersion Enum을 생성할 때")
    class whenCreateHttpVersionEnum {

        @Test
        @DisplayName("HTTP/1.1 버전을 생성한다.")
        void testCreateHttpVersionEnum() {
            // Act
            HttpVersion actualResult = HttpVersion.HTTP_1_1;
            // Assert
            assertThat(actualResult)
                .isEqualTo(HttpVersion.HTTP_1_1)
                .hasFieldOrPropertyWithValue("version", "HTTP/1.1");
        }

        @Test
        @DisplayName("잘못된 HTTP 버전을 입력하면 예외를 반환한다.")
        void testWhenInputInvalidHttpVersionThenThrowException() {
            // Act & Assert
            assertThatThrownBy(() -> HttpVersion.from("HTTP/1.0"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지원하지 않는 HTTP 버전입니다.");
        }

    }

}