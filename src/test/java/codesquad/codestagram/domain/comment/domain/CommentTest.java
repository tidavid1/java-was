package codesquad.codestagram.domain.comment.domain;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.codestagram.domain.article.domain.Article;
import codesquad.codestagram.domain.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Comment는")
class CommentTest {

    private User expectedUser = User.of(1L, "user", "password", "name", "test@gmail.com");
    private Article expectedArticle = new Article(1L, "title", "content", expectedUser.getId(),
        expectedUser.getName());

    @Test
    @DisplayName("생성자를 통해 생성할 수 있다.")
    void create() {
        // Arrange
        String expectedBody = "body";
        // Act
        Comment actualResult = new Comment(expectedBody, expectedUser, expectedArticle);
        // Assert
        assertThat(actualResult)
            .extracting("body", "userId", "username", "articleId")
            .containsExactly(expectedBody, expectedUser.getId(), expectedUser.getName(),
                expectedArticle.getId());
    }

    @Test
    @DisplayName("생성자를 통해 생성할 수 있다. - DAO 용")
    void createForDao() {
        // Arrange
        Long expectedId = 1L;
        String expectedBody = "body";
        // Act
        Comment actualResult = new Comment(expectedId, expectedBody, expectedUser.getId(),
            expectedUser.getName(), expectedArticle.getId());
        // Assert
        assertThat(actualResult)
            .extracting("id", "body", "userId", "username", "articleId")
            .containsExactly(expectedId, expectedBody, expectedUser.getId(), expectedUser.getName(),
                expectedArticle.getId());
    }
}