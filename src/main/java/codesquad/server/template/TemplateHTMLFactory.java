package codesquad.server.template;

import codesquad.codestagram.domain.article.domain.Article;
import codesquad.codestagram.domain.comment.domain.Comment;
import codesquad.codestagram.domain.user.domain.User;
import codesquad.server.http.exception.HttpCommonException;
import java.util.List;

// TODO: add in BeanFactory
public class TemplateHTMLFactory {

    private static final String HEADER_KEY = "{{headers}}";

    private TemplateFileStorage templateFileStorage;

    public TemplateHTMLFactory() {
    }

    private TemplateHTMLFactory(TemplateFileStorage templateFileStorage) {
        this.templateFileStorage = templateFileStorage;
    }

    public byte[] mainPage(Article article, List<Comment> comments) {
        String html = templateFileStorage.getFileStr("/index.html");
        return html
            .replace(HEADER_KEY, TemplateHTMLGenerator.anonymousUserHeader())
            .replace("{{post}}", TemplateHTMLGenerator.post(article))
            .replace("{{comments}}", TemplateHTMLGenerator.commentList(comments))
            .getBytes();
    }

    public byte[] mainPage(User user, Article article, List<Comment> comments) {
        String html = templateFileStorage.getFileStr("/index.html");
        return html
            .replace(HEADER_KEY, TemplateHTMLGenerator.loginUserHeader(user.getName()))
            .replace("{{post}}", TemplateHTMLGenerator.post(article))
            .replace("{{comments}}", TemplateHTMLGenerator.commentList(comments))
            .getBytes();
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

    @Deprecated
    public byte[] renderUsername(byte[] htmlBytes, String username) {
        String htmlString = convertBytesToString(htmlBytes);
        htmlString = htmlString.replace("{{username}}", username);
        return htmlString.getBytes();
    }

    @Deprecated
    public byte[] renderUserList(byte[] htmlBytes, String username, List<User> userList) {
        String htmlString = convertBytesToString(htmlBytes);
        htmlString = htmlString.replace("{{username}}", username);
        StringBuilder sb = new StringBuilder();
        for (User user : userList) {
            sb.append("<tr>\n<th class=\"btn btn_size_s btn_ghost\">").append(user.getUserId())
                .append("</th>\n<th class=\"btn btn_size_s btn_ghost\">").append(user.getName())
                .append("</th>\n</tr>\n");
        }
        int insertPointIdx = htmlString.indexOf("</tr>") + 5;
        htmlString =
            htmlString.substring(0, insertPointIdx) + sb + htmlString.substring(insertPointIdx);
        return htmlString.getBytes();
    }

    @Deprecated
    private String convertBytesToString(byte[] bytes) {
        return new String(bytes);
    }

}
