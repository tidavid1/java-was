package codesquad.codestagram.domain.comment.domain;

import codesquad.codestagram.domain.article.domain.Article;

public class Comment {

    private Long id;
    private String body;
    private Long articleId;


    public Comment(String body, Article article) {
        this.body = body;
        this.articleId = article.getId();
    }

    public Comment(Long id, String body, Long articleId) {
        this.id = id;
        this.body = body;
        this.articleId = articleId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public Long getArticleId() {
        return articleId;
    }
}
