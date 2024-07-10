package codesquad.register;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import codesquad.exception.HttpCommonException;
import codesquad.http.enums.StatusCode;
import codesquad.model.User;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class SessionIdRegisterTest {

    private final SessionIdRegister sessionIdRegister = SessionIdRegister.getInstance();

    @Nested
    @DisplayName("SessionIdRegister에 저장할 때")
    class whenSessionIdRegisterSave {

        @Test
        @DisplayName("register 메서드를 호출하면 sessionId가 생성 및 저장된다.")
        void register() {
            // Arrange
            User expectedUser = User.from(
                Map.of(
                    "userId", "hello",
                    "password", "world",
                    "name", "hello world",
                    "email", "hello@gmail.com"
                )
            );
            // Act
            String actualResult = sessionIdRegister.register(expectedUser);
            // Assert
            assertThat(actualResult).isNotNull();
        }
    }

    @Nested
    @DisplayName("SessionIdRegister에 저장된 sessionId를 조회할 때")
    class whenFindUserBySessionId {

        @Test
        @DisplayName("정상적으로 User 객체를 반환한다.")
        void returnUserSuccess() {
            // Arrange
            User expectedUser = User.from(
                Map.of(
                    "userId", "hello2",
                    "password", "world",
                    "name", "hello world",
                    "email", "hello@gmail.com"
                )
            );
            String expectedSessionId = SessionIdRegister.getInstance().register(expectedUser);
            // Act
            User actualUser = sessionIdRegister.getUser(expectedSessionId);
            // Assert
            assertThat(actualUser).isEqualTo(expectedUser);
        }

        @Test
        @DisplayName("잘못된 sessionId로 조회하면 HttpCommonException이 발생한다.")
        void returnHttpCommonException() {
            // Arrange
            String invalidSessionId = "invalidSessionId";
            // Act & Assert
            assertThatThrownBy(() -> sessionIdRegister.getUser(invalidSessionId))
                .isInstanceOf(HttpCommonException.class)
                .hasFieldOrPropertyWithValue("statusCode", StatusCode.UNAUTHORIZED)
                .hasMessage("잘못된 쿠키입니다. : " + invalidSessionId);
        }

    }

    @Nested
    @DisplayName("SessionIdRegister에 저장된 sessionId를 삭제할 때")
    class whenDeleteSessionId {

        @Test
        @DisplayName("unregister 메서드를 호출하면 sessionId가 삭제된다.")
        void unregister() {
            // Arrange
            User expectedUser = User.from(
                Map.of(
                    "userId", "hello3",
                    "password", "world",
                    "name", "hello world",
                    "email", "hello@gmail.com"));
            String sessionId = sessionIdRegister.register(expectedUser);
            // Act
            sessionIdRegister.unregister(sessionId);
            // Assert
            assertThatThrownBy(() -> sessionIdRegister.getUser(sessionId))
                .isInstanceOf(HttpCommonException.class)
                .hasFieldOrPropertyWithValue("statusCode", StatusCode.UNAUTHORIZED)
                .hasMessage("잘못된 쿠키입니다. : " + sessionId);
        }

        @Test
        @DisplayName("잘못된 sessionId로 삭제하면 HttpCommonException이 발생한다.")
        void unregisterWithInvalidSessionId() {
            // Arrange
            String invalidSessionId = "invalidSessionId";
            // Act & Assert
            assertThatThrownBy(() -> sessionIdRegister.unregister(invalidSessionId))
                .isInstanceOf(HttpCommonException.class)
                .hasFieldOrPropertyWithValue("statusCode", StatusCode.UNAUTHORIZED)
                .hasMessage("잘못된 쿠키입니다. : " + invalidSessionId);
        }
    }
}