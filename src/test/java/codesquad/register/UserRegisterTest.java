package codesquad.register;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import codesquad.model.User;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserRegisterTest {

    private UserRegister userRegister = UserRegister.getInstance();

    @Test
    @DisplayName("UserRegister에 User를 저장한다.")
    void save() {
        // Arrange
        var expectedQueryMap = Map.of(
            "userId", "hello",
            "password", "password",
            "name", "hi",
            "email", "hello@gmail.com"
        );
        var exceptedUser = User.from(expectedQueryMap);
        // Act
        var actualResult = userRegister.save(exceptedUser);
        // Assert
        assertAll(
            () -> assertThat(actualResult.getUserId()).isEqualTo(exceptedUser.getUserId()),
            () -> assertThat(actualResult.getPassword()).isEqualTo(exceptedUser.getPassword()),
            () -> assertThat(actualResult.getName()).isEqualTo(exceptedUser.getName()),
            () -> assertThat(actualResult.getEmail()).isEqualTo(exceptedUser.getEmail())
        );
    }

    @Test
    @DisplayName("UserRegister에 중복된 User를 저장하면 예외를 던진다.")
    void saveFail() {
        // Arrange
        var queryMap = Map.of(
            "userId", "hello2",
            "password", "password",
            "name", "hi",
            "email", "hello@gmail.com");
        var user = User.from(queryMap);
        userRegister.save(user);
        // Act & Assert
        assertThatThrownBy(() -> userRegister.save(user))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("이미 존재하는 사용자입니다.");

    }
}