package codesquad.handler.endpoint;

import codesquad.exception.HttpCommonException;
import codesquad.http.HttpResponse;
import codesquad.http.enums.HeaderKey;
import codesquad.http.enums.HttpMethod;
import codesquad.http.enums.StatusCode;
import codesquad.model.User;
import codesquad.register.EndPointRegister;
import codesquad.register.SessionIdRegister;
import codesquad.register.StaticFileRegister;
import codesquad.register.UserRegister;
import codesquad.register.model.EndPoint;
import codesquad.template.HTMLConvertor;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

public class GetEndPointHandler implements EndPointHandler {

    private static final GetEndPointHandler INSTANCE = new GetEndPointHandler();

    private final EndPointRegister endPointRegister;
    private final StaticFileRegister staticFileRegister;
    private final HTMLConvertor htmlConvertor;

    private GetEndPointHandler() {
        this.endPointRegister = EndPointRegister.getInstance();
        this.staticFileRegister = StaticFileRegister.getInstance();
        this.htmlConvertor = new HTMLConvertor();
    }

    public static GetEndPointHandler getInstance() {
        return INSTANCE;
    }

    @Override
    public void provideAll() {
        home();
        userList();
        homeRedirect();
        registration();
        login();
    }

    void home() {
        BiFunction<Map<String, String>, String, HttpResponse> biFunction = (headers, query) -> {
            try {
                String sessionId = verifyCookie(headers.get(HeaderKey.COOKIE.getValue()));
                User user = verifySession(sessionId);
                byte[] mainHtmlBytes = staticFileRegister.getFileBytes("/main/index.html");
                return HttpResponse.of(StatusCode.OK,
                    htmlConvertor.renderUsername(mainHtmlBytes, user.getName()));
            } catch (HttpCommonException hce) {
                byte[] indexHtmlBytes = staticFileRegister.getFileBytes("/index.html");
                return HttpResponse.of(StatusCode.OK, indexHtmlBytes);
            }
        };
        endPointRegister.addEndpoint(HttpMethod.GET, EndPoint.of("/index.html", biFunction));
    }

    void homeRedirect() {
        BiFunction<Map<String, String>, String, HttpResponse> biFunction = (headers, query) -> {
            HttpResponse response = HttpResponse.from(StatusCode.FOUND);
            response.addHeader(HeaderKey.LOCATION, "/index.html");
            return response;
        };
        endPointRegister.addEndpoint(HttpMethod.GET, EndPoint.of("/", biFunction));
        endPointRegister.addEndpoint(HttpMethod.GET, EndPoint.of("/main", biFunction));
    }

    void registration() {
        byte[] registrationHtmlBytes = staticFileRegister.getFileBytes("/registration/index.html");
        endPointRegister.addEndpoint(HttpMethod.GET, EndPoint.of("/registration",
            (headers, query) -> HttpResponse.of(StatusCode.OK, registrationHtmlBytes)));
        endPointRegister.addEndpoint(HttpMethod.GET, EndPoint.of("/registration/index.html",
            (headers, query) -> HttpResponse.of(StatusCode.OK, registrationHtmlBytes)));
    }

    void userList() {
        BiFunction<Map<String, String>, String, HttpResponse> biFunction = (headers, query) -> {
            try {
                String sessionId = verifyCookie(headers.get(HeaderKey.COOKIE.getValue()));
                User user = verifySession(sessionId);
                byte[] userListHtmlBytes = staticFileRegister.getFileBytes("/user/user_list.html");
                return HttpResponse.of(StatusCode.OK,
                    htmlConvertor.renderUserList(userListHtmlBytes, user.getName(),
                        UserRegister.getInstance().findAll()));
            } catch (HttpCommonException hce) {
                HttpResponse response = HttpResponse.from(StatusCode.FOUND);
                response.addHeader(HeaderKey.LOCATION, "/login");
                return response;
            }
        };
        endPointRegister.addEndpoint(HttpMethod.GET, EndPoint.of("/user/list", biFunction));
    }

    void login() {
        BiFunction<Map<String, String>, String, HttpResponse> biFunction = (headers, query) -> {
            try {
                verifyCookie(headers.get(HeaderKey.COOKIE.getValue()));
                HttpResponse response = HttpResponse.from(StatusCode.FOUND);
                response.addHeader(HeaderKey.LOCATION, "/index.html");
                return response;
            } catch (HttpCommonException hce) {
                byte[] loginHtml = staticFileRegister.getFileBytes("/login/index.html");
                return HttpResponse.of(StatusCode.OK, loginHtml);
            }
        };
        endPointRegister.addEndpoint(HttpMethod.GET, EndPoint.of("/login", biFunction));
        endPointRegister.addEndpoint(HttpMethod.GET, EndPoint.of("/login/index.html", biFunction));
    }

    private String verifyCookie(String cookie) {
        String value = Optional.ofNullable(cookie)
            .orElseThrow(() -> new HttpCommonException("세션이 존재하지 않습니다.", StatusCode.UNAUTHORIZED));
        return value.replace("SID=", "");
    }

    private User verifySession(String sessionId) {
        return SessionIdRegister.getInstance().findBySessionId(sessionId)
            .orElseThrow(() -> new HttpCommonException("세션이 존재하지 않습니다.", StatusCode.UNAUTHORIZED));
    }
}
