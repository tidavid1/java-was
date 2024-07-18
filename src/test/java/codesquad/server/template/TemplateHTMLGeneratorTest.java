package codesquad.server.template;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import codesquad.codestagram.domain.article.domain.Article;
import codesquad.codestagram.domain.comment.domain.Comment;
import codesquad.codestagram.domain.user.domain.User;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("TemplateHTMLGenerator는")
class TemplateHTMLGeneratorTest {

    @Test
    @DisplayName("익명 사용자의 헤더를 생성한다.")
    void anonymousUserHeader() {
        // Act
        String actualResult = TemplateHTMLGenerator.anonymousUserHeader();
        // Assert
        assertThat(actualResult).isNotNull();
    }

    @Test
    @DisplayName("로그인 사용자의 헤더를 생성한다.")
    void loginUserHeader() {
        // Arrange
        String expectedUsername = "test";
        // Act
        String actualResult = TemplateHTMLGenerator.loginUserHeader(expectedUsername);
        // Assert
        assertThat(actualResult).contains(expectedUsername);
    }

    @Test
    @DisplayName("게시글을 바디를 생성한다.")
    void post() {
        // Arrange
        String expectedTitle = "test";
        String expectedContents = "contents";
        String expectedUsername = "사용자";
        Article expectedArticle = new Article(1L, expectedTitle, expectedContents, "", 1L,
            expectedUsername);
        // Act
        String actualResult = TemplateHTMLGenerator.post(expectedArticle);
        // Assert
        assertAll(
            () -> assertThat(actualResult).contains(expectedTitle),
            () -> assertThat(actualResult).contains(expectedContents),
            () -> assertThat(actualResult).contains(expectedUsername)
        );
    }

    @Test
    @DisplayName("네비 바를 생성한다.")
    void navigationBar() {
        // Arrange
        long expectedArticleId = 1L;
        Article expectedArticle = new Article(expectedArticleId, "title", "content", "", 1L,
            "username");
        // Act
        String actualResult = TemplateHTMLGenerator.navigationBar(expectedArticle);
        // Assert
        assertAll(
            () -> assertThat(actualResult).contains(
                "/index.html?id=" + (expectedArticleId + 1)),
            () -> assertThat(actualResult).contains(
                "/comment?articleId=" + expectedArticleId)
        );
    }

    @Test
    @DisplayName("댓글 목록을 생성한다.")
    void commentList() {
        // Arrange
        String expectedBody = "test";
        String expectedUsername = "사용자";
        List<Comment> expectedComments = List.of(
            new Comment(1L, expectedBody, 1L, expectedUsername, 1L));
        // Act
        String actualResult = TemplateHTMLGenerator.commentList(expectedComments);
        // Assert
        assertThat(actualResult)
            .contains(expectedBody, expectedUsername);
    }

    @Test
    @DisplayName("사용자 목록을 생성한다.")
    void userList() {
        // Arrange
        String expectedUserId = "test";
        String expectedUsername = "사용자";
        List<User> expectedUserList = List.of(
            User.of(1L, expectedUserId, "1234", expectedUsername, "hello@gmail.com"));
        // Act
        String actualResult = TemplateHTMLGenerator.userList(expectedUserList);
        // Assert
        assertThat(actualResult).contains(expectedUsername, expectedUserId);
    }
}