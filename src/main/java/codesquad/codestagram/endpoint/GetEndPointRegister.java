package codesquad.codestagram.endpoint;

import codesquad.codestagram.domain.article.domain.Article;
import codesquad.codestagram.domain.comment.domain.Comment;
import codesquad.codestagram.domain.user.domain.User;
import codesquad.codestagram.domain.user.storage.UserDao;
import codesquad.server.bean.BeanFactory;
import codesquad.server.endpoint.EndPoint;
import codesquad.server.endpoint.EndPointRegister;
import codesquad.server.endpoint.EndPointStorage;
import codesquad.server.http.servlet.HttpServletRequest;
import codesquad.server.http.servlet.HttpServletResponse;
import codesquad.server.http.servlet.enums.HttpMethod;
import codesquad.server.http.servlet.enums.StatusCode;
import codesquad.server.http.session.Session;
import codesquad.server.http.session.SessionContext;
import codesquad.server.template.TemplateHTMLFactory;
import java.util.List;
import java.util.function.BiConsumer;

public class GetEndPointRegister implements EndPointRegister {

    private final EndPointStorage endPointStorage;
    private final TemplateHTMLFactory templateHtmlFactory;

    private GetEndPointRegister(EndPointStorage endPointStorage,
        TemplateHTMLFactory templateHTMLFactory) {
        this.endPointStorage = endPointStorage;
        this.templateHtmlFactory = templateHTMLFactory;
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
            Session session = SessionContext.getSession();
            byte[] data;
            Article testArticle = new Article(2L, "title", "달은 차고 아침은 밝다", 3L, "사용자");
            List<Comment> testComments = List.of(new Comment(1L, "굿", 3L, "사용자", 2L));
            if (session == null) {
                data = templateHtmlFactory.mainPage(testArticle, testComments);
            } else {
                User user = (User) session.getAttribute("user");
                data = templateHtmlFactory.mainPage(user, testArticle, testComments);
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
            List<User> users = BeanFactory.getInstance().getBean(UserDao.class).findAll();
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
        endPointStorage.addEndpoint(HttpMethod.GET, EndPoint.of("/write/index.html", biConsumer));
        BiConsumer<HttpServletRequest, HttpServletResponse> redirectBiConsumer = (httpServletRequest, httpServletResponse) -> httpServletResponse.sendRedirect(
            "/write/index.html");
        endPointStorage.addEndpoint(HttpMethod.GET, EndPoint.of("/write", redirectBiConsumer));
        endPointStorage.addEndpoint(HttpMethod.GET, EndPoint.of("/article", redirectBiConsumer));
        endPointStorage.addEndpoint(HttpMethod.GET,
            EndPoint.of("/article/index.html", redirectBiConsumer));
    }

    private void redirectToMain(HttpServletResponse httpServletResponse) {
        httpServletResponse.sendRedirect("/index.html");
    }

}
