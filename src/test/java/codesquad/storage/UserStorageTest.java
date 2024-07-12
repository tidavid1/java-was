package codesquad.storage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import codesquad.model.User;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("UserRegister에")
class UserStorageTest {

    private final UserStorage userStorage = UserStorage.getInstance();

    @Nested
    @DisplayName("User를 저장할 때")
    class whenSaveUser {

        @Test
        @DisplayName("정상적으로 저장한다.")
        void willSave() {
            // Arrange
            var expectedQueryMap = Map.of(
                "userId", "hello",
                "password", "password",
                "name", "hi",
                "email", "hello@gmail.com"
            );
            var exceptedUser = User.from(expectedQueryMap);
            // Act
            var actualResult = userStorage.save(exceptedUser);
            // Assert
            assertAll(
                () -> assertThat(actualResult.getUserId()).isEqualTo(exceptedUser.getUserId()),
                () -> assertThat(actualResult.getPassword()).isEqualTo(exceptedUser.getPassword()),
                () -> assertThat(actualResult.getName()).isEqualTo(exceptedUser.getName()),
                () -> assertThat(actualResult.getEmail()).isEqualTo(exceptedUser.getEmail())
            );
        }

        @Test
        @DisplayName("중복된 User를 저장하면 예외를 던진다.")
        void willThrowExceptionWhenDuplicatedUser() {
            // Arrange
            var queryMap = Map.of(
                "userId", "hello2",
                "password", "password",
                "name", "hi",
                "email", "hello@gmail.com");
            var user = User.from(queryMap);
            userStorage.save(user);
            // Act & Assert
            assertThatThrownBy(() -> userStorage.save(user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 사용자입니다.");

        }

    }

    @Nested
    @DisplayName("사용자를 조회할 때")
    class whenFindUser {

        @Test
        @DisplayName("Optional로 래핑된 사용자를 반환한다.")
        void willReturnUser() {
            // Arrange
            String expectedUserId = "hello3";
            var queryMap = Map.of(
                "userId", expectedUserId,
                "password", "password",
                "name", "hi",
                "email", "hello@gmail.com");
            var expectedResult = User.from(queryMap);
            userStorage.save(expectedResult);
            // Act
            var actualResult = userStorage.findById(expectedUserId);
            // Assert
            assertAll(
                () -> assertThat(actualResult).isNotEmpty(),
                () -> assertThat(actualResult).contains(expectedResult)
            );
        }
    }

}