package codesquad.codestagram.endpoint;

import codesquad.codestagram.domain.article.domain.Article;
import codesquad.codestagram.domain.article.storage.ArticleDao;
import codesquad.codestagram.domain.comment.domain.Comment;
import codesquad.codestagram.domain.comment.storage.CommentDao;
import codesquad.codestagram.domain.user.domain.User;
import codesquad.codestagram.domain.user.storage.UserDao;
import codesquad.server.endpoint.EndPoint;
import codesquad.server.endpoint.EndPointRegister;
import codesquad.server.endpoint.EndPointStorage;
import codesquad.server.http.exception.HttpCommonException;
import codesquad.server.http.servlet.HttpServletRequest;
import codesquad.server.http.servlet.HttpServletResponse;
import codesquad.server.http.servlet.SingleHttpRequest;
import codesquad.server.http.servlet.enums.HttpMethod;
import codesquad.server.http.servlet.enums.StatusCode;
import codesquad.server.http.session.Session;
import codesquad.server.http.session.SessionContext;
import codesquad.server.http.session.SessionStorage;
import codesquad.server.util.RandomSessionIDGenerator;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

public class PostEndPointRegister implements EndPointRegister {

    private final EndPointStorage endPointStorage;
    private final UserDao userDao;
    private final ArticleDao articleDao;
    private final CommentDao commentDao;
    private final SessionStorage sessionStorage;

    private PostEndPointRegister(EndPointStorage endPointStorage, UserDao userDao,
        ArticleDao articleDao, CommentDao commentDao, SessionStorage sessionStorage) {
        this.endPointStorage = endPointStorage;
        this.userDao = userDao;
        this.articleDao = articleDao;
        this.commentDao = commentDao;
        this.sessionStorage = sessionStorage;
    }

    @Override
    public void provideAll() {
        registerEndPoint("/create", this::handleCreate);
        registerEndPoint("/login", this::handleLogin);
        registerEndPoint("/logout", this::handleLogout);
        registerEndPoint("/write", this::handleWrite);
        registerEndPoint("/comment", this::handleComment);
    }

    public void handleCreate(HttpServletRequest request,
        HttpServletResponse response) {
        SingleHttpRequest httpRequest = (SingleHttpRequest) request.getRequest();
        try {
            Map<String, String> bodyMap = parseBody(httpRequest.getBody());
            userDao.save(User.from(bodyMap));
        } catch (IllegalArgumentException e) {
            setExceptionAttribute(request, e.getMessage(), StatusCode.BAD_REQUEST);
        }
        redirectToIndexHtml(response);
    }

    public void handleLogin(HttpServletRequest request,
        HttpServletResponse response) {
        SingleHttpRequest httpRequest = (SingleHttpRequest) request.getRequest();
        try {
            Map<String, String> bodyMap = parseBody(httpRequest.getBody());
            Optional<User> optionalUser = userDao.findById(
                Objects.requireNonNull(bodyMap.get("userId")));
            optionalUser.ifPresentOrElse(
                user -> {
                    if (!user.verifyPassword(bodyMap.get("password"))) {
                        response.sendRedirect("/login/login_failed.html");
                        return;
                    }
                    String sessionId = createSession(user);
                    HttpCookie cookie = generateCookie(sessionId);
                    response.setCookie(cookie);
                    redirectToIndexHtml(response);
                },
                () -> response.sendRedirect("/login/login_failed.html")
            );
        } catch (Exception e) {
            setExceptionAttribute(request, e.getMessage(), StatusCode.BAD_REQUEST);
        }
    }

    public void handleLogout(HttpServletRequest request,
        HttpServletResponse response) {
        Session session = SessionContext.getSession();
        sessionStorage.remove(session.getSessionId());
        HttpCookie cookie = generateCookie(session.getSessionId(), 0);
        response.setCookie(cookie);
        redirectToIndexHtml(response);
    }

    public void handleWrite(HttpServletRequest request,
        HttpServletResponse response) {
        SingleHttpRequest httpRequest = (SingleHttpRequest) request.getRequest();
        try {
            Map<String, String> body = parseBody(httpRequest.getBody());
            Article article = new Article(body.get("title"), body.get("content"),
                getUserInSessionContext());
            articleDao.save(article);
            redirectToIndexHtml(response);
        } catch (Exception e) {
            setExceptionAttribute(request, e.getMessage(), StatusCode.BAD_REQUEST);
        }
    }

    public void handleComment(HttpServletRequest request, HttpServletResponse response) {
        SingleHttpRequest httpRequest = (SingleHttpRequest) request.getRequest();
        try {
            Map<String, String> body = parseBody(httpRequest.getBody());
            Long articleId = Long.parseLong(body.get("article_id"));
            Article article = articleDao.findById(articleId).orElseThrow(
                () -> new HttpCommonException("존재하지 않는 아티클입니다.", StatusCode.NOT_FOUND));
            Comment comment = new Comment(body.get("value"), getUserInSessionContext(), article);
            commentDao.save(comment);
            redirectToIndexHtml(response);
        } catch (HttpCommonException e) {
            setExceptionAttribute(request, e.getMessage(), e.getStatusCode());
        } catch (Exception e) {
            setExceptionAttribute(request, e.getMessage(), StatusCode.BAD_REQUEST);
        }
    }

    private void registerEndPoint(String path,
        BiConsumer<HttpServletRequest, HttpServletResponse> biConsumer) {
        endPointStorage.addEndpoint(HttpMethod.POST, EndPoint.of(path, biConsumer));
    }

    private Map<String, String> parseBody(String body) {
        Map<String, String> queryMap = new HashMap<>();
        String[] values = body.split("&");
        for (String value : values) {
            String[] split = value.split("=");
            if (split.length != 2) {
                throw new IllegalArgumentException("요청 값을 찾을 수 없습니다: " + split[0]);
            }
            queryMap.put(split[0], split[1]);
        }
        return queryMap;
    }

    private void redirectToIndexHtml(HttpServletResponse response) {
        response.sendRedirect("/index.html");
    }

    private User getUserInSessionContext() {
        Session session = SessionContext.getSession();
        return (User) session.getAttribute("user");
    }

    private HttpCookie generateCookie(String sessionId) {
        HttpCookie cookie = new HttpCookie("SID", sessionId);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

    private HttpCookie generateCookie(String sessionId, int maxAge) {
        HttpCookie cookie = generateCookie(sessionId);
        cookie.setMaxAge(maxAge);
        return cookie;
    }

    private String createSession(User user) {
        String sessionId = RandomSessionIDGenerator.generate();
        Session session = new Session(sessionId, System.currentTimeMillis());
        session.setAttribute("user", user);
        sessionStorage.save(session);
        return sessionId;
    }

    private void setExceptionAttribute(HttpServletRequest request, String message,
        StatusCode statusCode) {
        request.setAttribute("exception", new HttpCommonException(message, statusCode));
    }
}
