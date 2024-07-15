package codesquad.codestagram.domain.article.domain;

import codesquad.codestagram.domain.user.domain.User;

public class Article {

    private Long id;
    private String title;
    private String body;
    private Long userId;

    public Article(String title, String body, User user) {
        this.title = validateTitle(title);
        this.body = body;
        this.userId = user.getId();
    }

    public Article(Long id, String title, String body, Long userId) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public Long getUserId() {
        return userId;
    }

    private String validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다.");
        }
        return title;
    }
}
