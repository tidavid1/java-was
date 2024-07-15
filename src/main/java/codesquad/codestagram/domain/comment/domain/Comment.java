package codesquad.codestagram.domain.comment.domain;

import codesquad.codestagram.domain.article.domain.Article;
import codesquad.codestagram.domain.user.domain.User;

public class Comment {

    private Long id;
    private String body;
    private Long userId;
    private Long articleId;

    public Comment(String body, User user, Article article) {
        this.body = body;
        this.userId = user.getId();
        this.articleId = article.getId();
    }

    public Comment(Long id, String body, Long userId, Long articleId) {
        this.id = id;
        this.body = body;
        this.userId = userId;
        this.articleId = articleId;
    }

    public Long getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getArticleId() {
        return articleId;
    }
}
