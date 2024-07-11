package codesquad.server.endpoint.handler;

import codesquad.model.User;
import codesquad.server.endpoint.EndPoint;
import codesquad.server.endpoint.EndPointStorage;
import codesquad.server.http.servlet.HttpServletRequest;
import codesquad.server.http.servlet.HttpServletResponse;
import codesquad.server.http.servlet.enums.HttpMethod;
import codesquad.server.http.servlet.enums.StatusCode;
import codesquad.server.http.session.Session;
import codesquad.server.http.session.SessionContext;
import codesquad.server.template.HTMLConvertor;
import codesquad.storage.StaticFileStorage;
import codesquad.storage.UserStorage;
import java.util.function.BiConsumer;

public class GetEndPointRegister implements EndPointRegister {

    private static final GetEndPointRegister INSTANCE = new GetEndPointRegister();

    private final EndPointStorage endPointStorage;
    private final StaticFileStorage staticFileStorage;
    private final HTMLConvertor htmlConvertor;

    private GetEndPointRegister() {
        this.endPointStorage = EndPointStorage.getInstance();
        this.staticFileStorage = StaticFileStorage.getInstance();
        this.htmlConvertor = new HTMLConvertor();
    }

    public static GetEndPointRegister getInstance() {
        return INSTANCE;
    }

    @Override
    public void provideAll() {
        home();
        userList();
        registration();
        login();
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
                    UserStorage.getInstance().findAll()));
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

}
