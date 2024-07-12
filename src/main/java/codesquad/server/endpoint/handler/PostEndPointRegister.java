package codesquad.server.endpoint.handler;

import codesquad.model.User;
import codesquad.server.endpoint.EndPoint;
import codesquad.server.endpoint.EndPointStorage;
import codesquad.server.http.exception.HttpCommonException;
import codesquad.server.http.servlet.HttpServletRequest;
import codesquad.server.http.servlet.HttpServletResponse;
import codesquad.server.http.servlet.enums.HttpMethod;
import codesquad.server.http.servlet.enums.StatusCode;
import codesquad.server.http.session.Session;
import codesquad.server.http.session.SessionContext;
import codesquad.server.http.session.SessionStorage;
import codesquad.server.util.RandomSessionIDGenerator;
import codesquad.storage.UserStorage;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

public class PostEndPointRegister implements EndPointRegister {

    private static final PostEndPointRegister INSTANCE = new PostEndPointRegister();

    private final EndPointStorage endPointStorage;

    public PostEndPointRegister() {
        this.endPointStorage = EndPointStorage.getInstance();
    }

    public static PostEndPointRegister getInstance() {
        return INSTANCE;
    }

    @Override
    public void provideAll() {
        create();
        login();
        logout();
    }

    void create() {
        BiConsumer<HttpServletRequest, HttpServletResponse> biConsumer = (httpServletRequest, httpServletResponse) -> {
            Map<String, String> bodyMap = parseBody(httpServletRequest.getRequest().getBody());
            try {
                UserStorage.getInstance().save(User.from(bodyMap));
            } catch (IllegalArgumentException e) {
                httpServletRequest.setAttribute("exception",
                    new HttpCommonException(e.getMessage(), StatusCode.BAD_REQUEST));
            }
            httpServletResponse.sendRedirect("/index.html");
        };
        EndPoint endPoint = EndPoint.of("/create", biConsumer);
        endPointStorage.addEndpoint(HttpMethod.POST, endPoint);
    }

    void login() {
        BiConsumer<HttpServletRequest, HttpServletResponse> biConsumer = (httpServletRequest, httpServletResponse) -> {
            Map<String, String> bodyMap = parseBody(httpServletRequest.getRequest().getBody());
            UserStorage.getInstance().findById(Objects.requireNonNull(bodyMap.get("userId")))
                .ifPresentOrElse(
                    user -> {
                        if (!user.verifyPassword(bodyMap.get("password"))) {
                            httpServletResponse.sendRedirect("/login/login_failed.html");
                            return;
                        }
                        String sessionId = RandomSessionIDGenerator.generate();
                        Session session = new Session(sessionId, System.currentTimeMillis());
                        session.setAttribute("user", user);
                        SessionStorage.getInstance().save(session);
                        HttpCookie cookie = generateCookie(sessionId);
                        httpServletResponse.setCookie(cookie);
                        httpServletResponse.sendRedirect("/index.html");
                    },
                    () -> httpServletResponse.sendRedirect("/login/login_failed.html")
                );
        };
        EndPoint endPoint = EndPoint.of("/login", biConsumer);
        endPointStorage.addEndpoint(HttpMethod.POST, endPoint);
    }

    void logout() {
        BiConsumer<HttpServletRequest, HttpServletResponse> biConsumer = (httpServletRequest, httpServletResponse) -> {
            Session session = SessionContext.getSession();
            SessionStorage.getInstance().remove(session.getSessionId());
            HttpCookie cookie = generateCookie(session.getSessionId(), 0);
            httpServletResponse.setCookie(cookie);
            httpServletResponse.sendRedirect("/index.html");
        };
        EndPoint endPoint = EndPoint.of("/logout", biConsumer);
        endPointStorage.addEndpoint(HttpMethod.POST, endPoint);
    }

    private Map<String, String> parseBody(String body) {
        Map<String, String> queryMap = new HashMap<>();
        String[] values = body.split("&");
        for (String value : values) {
            String[] split = value.split("=");
            if (split.length != 2) {
                throw new HttpCommonException("요청 값을 찾을 수 없습니다: " + split[0],
                    StatusCode.BAD_REQUEST);
            }
            queryMap.put(split[0], split[1]);
        }
        return queryMap;
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
}
