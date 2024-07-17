package codesquad.codestagram.domain.user.domain.values;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class EmailTest {

    @Test
    @DisplayName("이메일 형식이 올바르지 않은 경우 예외를 던진다.")
    void createFail() {
        // Arrange
        String invalidEmail = "codesquad";
        // Act & Assert
        assertThatThrownBy(() -> Email.from(invalidEmail))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("이메일 형식이 올바르지 않습니다.");
    }

    @MethodSource
    @ParameterizedTest
    @DisplayName("이메일 형식이 올바른 경우 Email 인스턴스를 생성한다.")
    void create(String email) {
        // Act
        Email actualResult = Email.from(email);
        // Assert
        assertThat(email).isEqualTo(actualResult.getValue());
    }

    private static Stream<Arguments> create() {
        return Stream.of(
            Arguments.of("a@naver.com"),
            Arguments.of("b@gmail.com"),
            Arguments.of("hello@daum.net"));
    }

}