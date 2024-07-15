package codesquad.codestagram.domain.article.domain;

public class Article {

    private Long id;
    private String title;
    private String body;

    public Article(String title, String body) {
        this.title = validateTitle(title);
        this.body = body;
    }

    public Article(Long id, String title, String body) {
        this.id = id;
        this.title = title;
        this.body = body;
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

    private String validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다.");
        }
        return title;
    }
}
