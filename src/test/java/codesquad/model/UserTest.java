package codesquad.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class UserTest {

    @Nested
    @DisplayName("User를 생성할 때")
    class whenCreateUser {

        @Test
        @DisplayName("정적 팩토리 메서드 from을 사용하여 User를 생성한다.")
        void createUser() {
            // Arrange
            String expectedUserId = "hello";
            String expectedPassword = "password";
            String expectedName = "헬로우";
            String expectedEmail = "hello@gmail.com";
            Map<String, String> expectedMap = Map.of(
                "userId", expectedUserId,
                "password", expectedPassword,
                "name", expectedName,
                "email", expectedEmail
            );
            // Act
            User actualResult = User.from(expectedMap);
            // Assert
            assertThat(actualResult)
                .extracting("userId", "password", "name", "email")
                .containsExactly(expectedUserId, expectedPassword, expectedName, expectedEmail);
        }

        @MethodSource
        @DisplayName("하나라도 값에 null이 들어가면 예외를 던진다.")
        @ParameterizedTest(autoCloseArguments = false)
        void throwExceptionWhenNullValueInserted(Map<String, String> map, String message) {
            // Act, Assert
            assertThatThrownBy(() -> User.from(map))
                .isInstanceOf(NullPointerException.class)
                .hasMessage(message);
        }

        private static Stream<Arguments> throwExceptionWhenNullValueInserted() {
            return Stream.of(
                Arguments.of(
                    Map.of(
                        "userId", "hello",
                        "name", "헬로우",
                        "email", "hello@gmail.com"
                    ), "password는 null일 수 없습니다."),
                Arguments.of(Map.of(
                    "password", "password",
                    "userId", "hello",
                    "email", "hello@gmail.com"
                ), "name은 null일 수 없습니다."),
                Arguments.of(Map.of(
                    "password", "password",
                    "name", "헬로우",
                    "email", "hello@gmail.com"
                ), "userId는 null일 수 없습니다.")
            );
        }
    }

    @Test
    @DisplayName("User의 비밀번호를 검증한다.")
    void verifyPassword() {
        // Arrange
        String expectedPassword = "password";
        User user = User.from(Map.of(
            "userId", "hello",
            "password", expectedPassword,
            "name", "헬로우",
            "email", "hello@gmail.com"
        ));
        // Act, Assert
        assertThat(user.verifyPassword(expectedPassword)).isTrue();
    }
}