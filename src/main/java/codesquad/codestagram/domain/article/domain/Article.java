package codesquad.codestagram.domain.article.domain;

import codesquad.codestagram.domain.user.domain.User;

public class Article {

    private Long id;
    private String title;
    private String body;
    private Long userId;
    private String username;

    public Article(String title, String body, User user) {
        this.title = validateTitle(title);
        this.body = body;
        this.userId = user.getId();
        this.username = user.getName();
    }

    public Article(Long id, String title, String body, Long userId, String username) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.userId = userId;
        this.username = username;
    }

    public Long getId() {
        return id;
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

    public String getUsername() {
        return username;
    }

    private String validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다.");
        }
        return title;
    }
}
