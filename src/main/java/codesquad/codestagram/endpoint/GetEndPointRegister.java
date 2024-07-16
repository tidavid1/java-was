package codesquad.codestagram.endpoint;

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
import codesquad.server.storage.StaticFileStorage;
import codesquad.server.template.HTMLConvertor;
import java.util.function.BiConsumer;

public class GetEndPointRegister implements EndPointRegister {

    private final EndPointStorage endPointStorage;
    private final StaticFileStorage staticFileStorage;
    private final HTMLConvertor htmlConvertor;

    private GetEndPointRegister(EndPointStorage endPointStorage,
        StaticFileStorage staticFileStorage) {
        this.endPointStorage = endPointStorage;
        this.staticFileStorage = staticFileStorage;
        this.htmlConvertor = new HTMLConvertor();
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
            if (session == null) {
                byte[] indexHtmlBytes = staticFileStorage.getFileBytes("/index.html");
                httpServletResponse.setStatus(StatusCode.OK);
                httpServletResponse.setHeader("Content-Type", "text/html");
                httpServletResponse.setBody(indexHtmlBytes);
                return;
            }
            User user = (User) session.getAttribute("user");
            byte[] mainHtmlBytes = staticFileStorage.getFileBytes("/main/index.html");
            httpServletResponse.setStatus(StatusCode.OK);
            httpServletResponse.setHeader("Content-Type", "text/html");
            httpServletResponse.setBody(
                htmlConvertor.renderUsername(mainHtmlBytes, user.getName()));
        };
        endPointStorage.addEndpoint(HttpMethod.GET, EndPoint.of("/index.html", biConsumer));
        endPointStorage.addEndpoint(HttpMethod.GET,
            EndPoint.of("/",
                (httpServletRequest, httpServletResponse) -> httpServletResponse.sendRedirect(
                    "/index.html")));
        endPointStorage.addEndpoint(HttpMethod.GET,
            EndPoint.of("/main",
                (httpServletRequest, httpServletResponse) -> httpServletResponse.sendRedirect(
                    "/index.html")));
    }

    void registration() {
        byte[] registrationHtmlBytes = staticFileStorage.getFileBytes("/registration/index.html");
        endPointStorage.addEndpoint(HttpMethod.GET,
            EndPoint.of("/registration/index.html", (httpServletRequest, httpServletResponse) -> {
                httpServletResponse.setStatus(StatusCode.OK);
                httpServletResponse.setHeader("Content-Type", "text/html");
                httpServletResponse.setBody(registrationHtmlBytes);
            }));
        endPointStorage.addEndpoint(HttpMethod.GET, EndPoint.of("/registration",
            (httpServletRequest, httpServletResponse) -> httpServletResponse.sendRedirect(
                "/registration/index.html")));
    }

    void userList() {
        BiConsumer<HttpServletRequest, HttpServletResponse> biConsumer = (httpServletRequest, httpServletResponse) -> {
            byte[] userListHtmlBytes = staticFileStorage.getFileBytes("/user/user_list.html");
            User user = (User) SessionContext.getSession().getAttribute("user");
            httpServletResponse.setStatus(StatusCode.OK);
            httpServletResponse.setHeader("Content-Type", "text/html");
            httpServletResponse.setBody(
                htmlConvertor.renderUserList(userListHtmlBytes, user.getName(),
                    BeanFactory.getInstance().getBean(UserDao.class).findAll()));
        };
        endPointStorage.addEndpoint(HttpMethod.GET, EndPoint.of("/user/list", biConsumer));
    }

    void login() {
        BiConsumer<HttpServletRequest, HttpServletResponse> biConsumer = (httpServletRequest, httpServletResponse) -> {
            if (SessionContext.getSession() != null) {
                httpServletResponse.sendRedirect("/index.html");
                return;
            }
            byte[] loginHtml = staticFileStorage.getFileBytes("/login/index.html");
            httpServletResponse.setStatus(StatusCode.OK);
            httpServletResponse.setHeader("Content-Type", "text/html");
            httpServletResponse.setBody(loginHtml);
        };
        endPointStorage.addEndpoint(HttpMethod.GET, EndPoint.of("/login/index.html", biConsumer));
        endPointStorage.addEndpoint(HttpMethod.GET,
            EndPoint.of("/login",
                (httpServletRequest, httpServletResponse) -> httpServletResponse.sendRedirect(
                    "/login/index.html")));
    }


    void loginFail() {
        byte[] loginFailHtml = staticFileStorage.getFileBytes("/login/login_failed.html");
        endPointStorage.addEndpoint(HttpMethod.GET,
            EndPoint.of("/login/login_failed.html", (httpServletRequest, httpServletResponse) -> {
                httpServletResponse.setStatus(StatusCode.OK);
                httpServletResponse.setHeader("Content-Type", "text/html");
                httpServletResponse.setBody(loginFailHtml);
            }));
    }

    void write() {
        byte[] writeHtmlBytes = staticFileStorage.getFileBytes("/article/index.html");
        endPointStorage.addEndpoint(HttpMethod.GET,
            EndPoint.of("/write/index.html", (httpServletRequest, httpServletResponse) -> {
                httpServletResponse.setStatus(StatusCode.OK);
                httpServletResponse.setHeader("Content-Type", "text/html");
                httpServletResponse.setBody(writeHtmlBytes);
            }));
        endPointStorage.addEndpoint(HttpMethod.GET,
            EndPoint.of("/write",
                (httpServletRequest, httpServletResponse) -> httpServletResponse.sendRedirect(
                    "/write/index.html")));
        endPointStorage.addEndpoint(HttpMethod.GET,
            EndPoint.of("/article",
                (httpServletRequest, httpServletResponse) -> httpServletResponse.sendRedirect(
                    "/write/index.html")));
    }


}
