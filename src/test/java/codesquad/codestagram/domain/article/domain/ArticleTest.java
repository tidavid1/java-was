package codesquad.codestagram.domain.article.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import codesquad.codestagram.domain.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Article은")
class ArticleTest {

    private User expectedUser = User.of(1L, "test", "test", "test", "test@gmail.com");

    @Test
    @DisplayName("생성자를 통해 생성할 수 있다.")
    void create() {
        // Arrange
        String expectedTitle = "제목";
        String expectedBody = "내용";
        // Act
        Article actualResult = new Article(expectedTitle, expectedBody, expectedUser);
        // Assert
        assertThat(actualResult)
            .extracting("title", "body", "userId", "username")
            .containsExactly(expectedTitle, expectedBody, expectedUser.getId(),
                expectedUser.getName());
    }

    @Test
    @DisplayName("생성자를 통해 생성할 수 있다. - DAO용")
    void createForDao() {
        // Arrange
        Long expectedId = 1L;
        String expectedTitle = "제목";
        String expectedBody = "내용";
        Long expectedUserId = 1L;
        String expectedUsername = "사용자";
        // Act
        Article actualResult = new Article(expectedId, expectedTitle, expectedBody, expectedUserId,
            expectedUsername);
        // Assert
        assertThat(actualResult)
            .extracting("id", "title", "body", "userId", "username")
            .containsExactly(expectedId, expectedTitle, expectedBody, expectedUserId,
                expectedUsername);
    }

    @Nested
    @DisplayName("생성시")
    class whenCreated {

        @Test
        @DisplayName("제목이 없으면 예외를 던진다.")
        void validateTitle() {
            // Arrange
            String expectedTitle = "";
            String expectedBody = "내용";
            // Act & Assert
            assertThatThrownBy(() -> new Article(expectedTitle, expectedBody, expectedUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("제목은 필수입니다.");
        }

        @Test
        @DisplayName("제목이 null이면 예외를 던진다.")
        void validateTitleWithNull() {
            // Arrange
            String expectedBody = "내용";
            // Act & Assert
            assertThatThrownBy(() -> new Article(null, expectedBody, expectedUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("제목은 필수입니다.");
        }
    }
}