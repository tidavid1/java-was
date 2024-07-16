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
import codesquad.server.http.servlet.enums.HttpMethod;
import codesquad.server.http.servlet.enums.StatusCode;
import codesquad.server.http.servlet.values.HttpQueryParams;
import codesquad.server.http.session.Session;
import codesquad.server.http.session.SessionContext;
import codesquad.server.template.TemplateHTMLFactory;
import java.util.List;
import java.util.function.BiConsumer;

public class GetEndPointRegister implements EndPointRegister {

    private final EndPointStorage endPointStorage;
    private final TemplateHTMLFactory templateHtmlFactory;
    private final UserDao userDao;
    private final ArticleDao articleDao;
    private final CommentDao commentDao;

    private GetEndPointRegister(EndPointStorage endPointStorage,
        TemplateHTMLFactory templateHtmlFactory,
        UserDao userDao, ArticleDao articleDao, CommentDao commentDao) {
        this.endPointStorage = endPointStorage;
        this.templateHtmlFactory = templateHtmlFactory;
        this.userDao = userDao;
        this.articleDao = articleDao;
        this.commentDao = commentDao;
    }

    @Override
    public void provideAll() {
        home();
        userList();
        registration();
        login();
        loginFail();
        write();
    }

    void home() {
        BiConsumer<HttpServletRequest, HttpServletResponse> biConsumer = (httpServletRequest, httpServletResponse) -> {
            HttpQueryParams queryParams = httpServletRequest.getRequest().getRequestLine()
                .getQueryParams();
            Long id = Long.parseLong(queryParams.getParameter("id").orElse("1"));
            Session session = SessionContext.getSession();
            byte[] data;
            Article article = articleDao.findById(id).orElseThrow(
                () -> new HttpCommonException("아티클을 찾을 수 없습니다!", StatusCode.BAD_REQUEST));
            List<Comment> comments = commentDao.findAllByArticleId(article.getId());
            if (session == null) {
                data = templateHtmlFactory.mainPage(article, comments);
            } else {
                User user = (User) session.getAttribute("user");
                data = templateHtmlFactory.mainPage(user, article, comments);
            }
            httpServletResponse.setBody(data);
            httpServletResponse.setStatus(StatusCode.OK);
            httpServletResponse.setHeader("Content-Type", "text/html");
        };
        endPointStorage.addEndpoint(HttpMethod.GET, EndPoint.of("/index.html", biConsumer));
        BiConsumer<HttpServletRequest, HttpServletResponse> redirection = (httpServletRequest, httpServletResponse) -> httpServletResponse.sendRedirect(
            "/index.html");
        endPointStorage.addEndpoint(HttpMethod.GET, EndPoint.of("/", redirection));
        endPointStorage.addEndpoint(HttpMethod.GET, EndPoint.of("/main", redirection));
        endPointStorage.addEndpoint(HttpMethod.GET, EndPoint.of("/main/index.html", redirection));
    }

    void registration() {
        endPointStorage.addEndpoint(HttpMethod.GET,
            EndPoint.of("/registration/index.html", (httpServletRequest, httpServletResponse) -> {
                Session session = SessionContext.getSession();
                if (session != null) {
                    redirectToMain(httpServletResponse);
                    return;
                }
                httpServletResponse.setStatus(StatusCode.OK);
                httpServletResponse.setHeader("Content-Type", "text/html");
                httpServletResponse.setBody(templateHtmlFactory.registrationPage());
            }));
        endPointStorage.addEndpoint(HttpMethod.GET, EndPoint.of("/registration",
            (httpServletRequest, httpServletResponse) -> httpServletResponse.sendRedirect(
                "/registration/index.html")));
    }

    void userList() {
        BiConsumer<HttpServletRequest, HttpServletResponse> biConsumer = (httpServletRequest, httpServletResponse) -> {
            User user = (User) SessionContext.getSession().getAttribute("user");
            List<User> users = userDao.findAll();
            byte[] data = templateHtmlFactory.userListPage(user, users);
            httpServletResponse.setStatus(StatusCode.OK);
            httpServletResponse.setHeader("Content-Type", "text/html");
            httpServletResponse.setBody(data);
        };
        endPointStorage.addEndpoint(HttpMethod.GET, EndPoint.of("/user/list", biConsumer));
    }

    void login() {
        BiConsumer<HttpServletRequest, HttpServletResponse> biConsumer = (httpServletRequest, httpServletResponse) -> {
            if (SessionContext.getSession() != null) {
                redirectToMain(httpServletResponse);
                return;
            }
            httpServletResponse.setStatus(StatusCode.OK);
            httpServletResponse.setHeader("Content-Type", "text/html");
            httpServletResponse.setBody(templateHtmlFactory.loginPage());
        };
        endPointStorage.addEndpoint(HttpMethod.GET, EndPoint.of("/login/index.html", biConsumer));
        endPointStorage.addEndpoint(HttpMethod.GET,
            EndPoint.of("/login",
                (httpServletRequest, httpServletResponse) -> httpServletResponse.sendRedirect(
                    "/login/index.html")));
    }


    void loginFail() {
        endPointStorage.addEndpoint(HttpMethod.GET,
            EndPoint.of("/login/login_failed.html", (httpServletRequest, httpServletResponse) -> {
                if (SessionContext.getSession() != null) {
                    redirectToMain(httpServletResponse);
                    return;
                }
                httpServletResponse.setStatus(StatusCode.OK);
                httpServletResponse.setHeader("Content-Type", "text/html");
                httpServletResponse.setBody(templateHtmlFactory.loginFailPage());
            }));
    }

    void write() {
        BiConsumer<HttpServletRequest, HttpServletResponse> biConsumer = (httpServletRequest, httpServletResponse) -> {
            User user = (User) SessionContext.getSession().getAttribute("user");
            byte[] data = templateHtmlFactory.articlePage(user);
            httpServletResponse.setStatus(StatusCode.OK);
            httpServletResponse.setHeader("Content-Type", "text/html");
            httpServletResponse.setBody(data);
        };
        endPointStorage.addEndpoint(HttpMethod.GET, EndPoint.of("/write.html", biConsumer));
        BiConsumer<HttpServletRequest, HttpServletResponse> redirectBiConsumer = (httpServletRequest, httpServletResponse) -> httpServletResponse.sendRedirect(
            "/write.html");
        endPointStorage.addEndpoint(HttpMethod.GET, EndPoint.of("/write", redirectBiConsumer));
        endPointStorage.addEndpoint(HttpMethod.GET,
            EndPoint.of("/write/index.html", redirectBiConsumer));
        endPointStorage.addEndpoint(HttpMethod.GET, EndPoint.of("/article", redirectBiConsumer));
        endPointStorage.addEndpoint(HttpMethod.GET,
            EndPoint.of("/article/index.html", redirectBiConsumer));
    }

    private void redirectToMain(HttpServletResponse httpServletResponse) {
        httpServletResponse.sendRedirect("/index.html");
    }

}
