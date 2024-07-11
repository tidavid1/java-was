package codesquad.register.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import codesquad.http.servlet.HttpResponseDeprecated;
import codesquad.http.servlet.enums.StatusCode;
import java.util.Map;
import java.util.function.BiFunction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class EndPointTest {

    @Nested
    @DisplayName("EndPoint Class를 생성할 때")
    class whenCreateEndPoint {

        @Test
        @DisplayName("정적 팩토리 메서드 of를 사용하여 생성한다.")
        void testCreateWithOf() {
            // Arrange
            String expectedPath = "/index.html";
            BiFunction<Map<String, String>, String, HttpResponseDeprecated> expectedBiFunction = (header, query) -> HttpResponseDeprecated.from(
                StatusCode.OK);
            // Act
            EndPoint<String> actualResult = EndPoint.of(expectedPath, expectedBiFunction);
            // Assert
            assertThat(actualResult)
                .extracting("path", "biFunction")
                .containsExactly(expectedPath, expectedBiFunction);
        }

        @Test
        @DisplayName("Function이 null인 경우 예외를 반환한다.")
        void testWhenFunctionIsNullThenThrowException() {
            // Arrange
            String expectedPath = "/index.html";
            // Act & Assert
            assertThatThrownBy(() -> EndPoint.of(expectedPath, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("BiFunction은 null일 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("생성된 EndPoint에 대해")
    class whenCreatedEndPointExist {

        @Test
        @DisplayName("생성된 EndPoint의 Function을 실행한다.")
        void testApply() {
            // Arrange
            BiFunction<Map<String, String>, String, HttpResponseDeprecated> expectedBiFunction = (header, query) -> HttpResponseDeprecated.from(
                StatusCode.OK);
            EndPoint<String> endPoint = EndPoint.of("/", expectedBiFunction);
            // Act
            HttpResponseDeprecated actualResult = endPoint.apply(Map.of(), null);
            // Assert
            assertThat(actualResult).hasFieldOrPropertyWithValue("statusCode", StatusCode.OK);
        }

        @Test
        @DisplayName("path가 동일하면 같은 EndPoint로 판단한다.")
        void testEquals() {
            // Arrange
            EndPoint<String> endPoint1 = EndPoint.of("/",
                (headers, query) -> HttpResponseDeprecated.from(StatusCode.OK));
            EndPoint<String> endPoint2 = EndPoint.of("/",
                (headers, query) -> HttpResponseDeprecated.from(StatusCode.OK));
            // Act & Assert
            assertThat(endPoint1.equals(endPoint2)).isTrue();
        }
    }


}