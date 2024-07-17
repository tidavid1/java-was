package codesquad.codestagram.domain.article.domain;

import codesquad.codestagram.domain.user.domain.User;

public class Article {

    private static final String DEFAULT_IMAGE_PATH = "./default.webp";

    private Long id;
    private String title;
    private String body;
    private String imagePath;
    private Long userId;
    private String username;

    public Article(String title, String body, String imagePath, User user) {
        this.title = validateTitle(title);
        this.body = body;
        this.imagePath = imagePath == null ? DEFAULT_IMAGE_PATH : imagePath;
        this.userId = user.getId();
        this.username = user.getName();
    }

    public Article(Long id, String title, String body, String imagePath, Long userId,
        String username) {
        this.id = id;
        this.title = validateTitle(title);
        this.body = body;
        this.imagePath = imagePath;
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

    public String getImagePath() {
        return imagePath;
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
