package codesquad.server.template;

import codesquad.codestagram.domain.article.domain.Article;
import codesquad.codestagram.domain.comment.domain.Comment;
import codesquad.codestagram.domain.user.domain.User;
import codesquad.server.http.exception.HttpCommonException;
import java.util.List;

public class TemplateHTMLFactory {

    private static final String HEADER_KEY = "{{header}}";

    private final TemplateFileStorage templateFileStorage;

    private TemplateHTMLFactory(TemplateFileStorage templateFileStorage) {
        this.templateFileStorage = templateFileStorage;
    }

    public byte[] mainPage(Article article, List<Comment> comments) {
        String html = templateFileStorage.getFileStr("/index.html");
        return html
            .replace(HEADER_KEY, TemplateHTMLGenerator.anonymousUserHeader())
            .replace("{{post}}", TemplateHTMLGenerator.post(article))
            .replace("{{comments}}", TemplateHTMLGenerator.commentList(comments))
            .replace("{{nav}}", TemplateHTMLGenerator.navigationBar(article))
            .getBytes();
    }

    public byte[] mainPage(User user, Article article, List<Comment> comments) {
        String html = templateFileStorage.getFileStr("/index.html");
        return html
            .replace(HEADER_KEY, TemplateHTMLGenerator.loginUserHeader(user.getName()))
            .replace("{{post}}", TemplateHTMLGenerator.post(article))
            .replace("{{comments}}", TemplateHTMLGenerator.commentList(comments))
            .replace("{{nav}}", TemplateHTMLGenerator.navigationBar(article))
            .getBytes();
    }

    public byte[] registrationPage() {
        String html = templateFileStorage.getFileStr("/registration/index.html");
        return html.getBytes();
    }

    public byte[] loginPage() {
        String html = templateFileStorage.getFileStr("/login/index.html");
        return html.getBytes();
    }

    public byte[] loginFailPage() {
        String html = templateFileStorage.getFileStr("/login/login_failed.html");
        return html.getBytes();
    }

    public byte[] exceptionPage(HttpCommonException httpCommonException) {
        String html = templateFileStorage.getFileStr("/exception/index.html");
        return html
            .replace(HEADER_KEY, TemplateHTMLGenerator.anonymousUserHeader())
            .replace("{{exception.code}}",
                String.valueOf(httpCommonException.getStatusCode().getCode()))
            .replace("{{exception.message}}", httpCommonException.getMessage())
            .getBytes();
    }

    public byte[] exceptionPage(User user, HttpCommonException httpCommonException) {
        String html = templateFileStorage.getFileStr("/exception/index.html");
        return html
            .replace(HEADER_KEY, TemplateHTMLGenerator.loginUserHeader(user.getName()))
            .replace("{{exception.code}}",
                String.valueOf(httpCommonException.getStatusCode().getCode()))
            .replace("{{exception.message}}", httpCommonException.getMessage())
            .getBytes();
    }

    public byte[] userListPage(User user, List<User> users) {
        String html = templateFileStorage.getFileStr("/user/index.html");
        return html
            .replace(HEADER_KEY, TemplateHTMLGenerator.loginUserHeader(user.getName()))
            .replace("{{users}}", TemplateHTMLGenerator.userList(users))
            .getBytes();
    }

    public byte[] articlePage(User user) {
        String html = templateFileStorage.getFileStr("/article/index.html");
        return html
            .replace(HEADER_KEY, TemplateHTMLGenerator.loginUserHeader(user.getName()))
            .getBytes();
    }

    public byte[] commentPage(User user, Article article) {
        String html = templateFileStorage.getFileStr("/comment/index.html");
        return html
            .replace(HEADER_KEY, TemplateHTMLGenerator.loginUserHeader(user.getName()))
            .replace("{{articleId}}", String.valueOf(article.getId()))
            .getBytes();
    }

}
