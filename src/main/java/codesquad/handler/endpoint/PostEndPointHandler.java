package codesquad.handler.endpoint;

import codesquad.exception.HttpCommonException;
import codesquad.http.servlet.HttpResponse;
import codesquad.http.servlet.enums.HeaderKey;
import codesquad.http.servlet.enums.HttpMethod;
import codesquad.http.servlet.enums.StatusCode;
import codesquad.model.User;
import codesquad.register.EndPointRegister;
import codesquad.register.SessionIdRegister;
import codesquad.register.UserRegister;
import codesquad.register.model.EndPoint;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

public class PostEndPointHandler implements EndPointHandler {

    private static final PostEndPointHandler INSTANCE = new PostEndPointHandler();

    private final EndPointRegister endPointRegister;

    public PostEndPointHandler() {
        this.endPointRegister = EndPointRegister.getInstance();
    }

    public static PostEndPointHandler getInstance() {
        return INSTANCE;
    }

    @Override
    public void provideAll() {
        create();
        login();
        logout();
    }

    void create() {
        BiFunction<Map<String, String>, String, HttpResponse> biFunction = (headers, body) -> {
            Map<String, String> map = parseBody(body);
            try {
                UserRegister.getInstance().save(User.from(map));
            } catch (IllegalArgumentException e) {
                throw new HttpCommonException(e.getMessage(), StatusCode.BAD_REQUEST);
            }
            HttpResponse response = HttpResponse.from(StatusCode.FOUND);
            response.addHeader(HeaderKey.LOCATION, "/index.html");
            return response;
        };
        EndPoint<String> endPoint = EndPoint.of(
            "/create", biFunction
        );
        endPointRegister.addEndpoint(HttpMethod.POST, endPoint);
    }

    void login() {
        BiFunction<Map<String, String>, String, HttpResponse> biFunction = (headers, body) -> {
            HttpResponse response = HttpResponse.from(StatusCode.FOUND);
            Map<String, String> map = parseBody(body);
            UserRegister.getInstance().findById(Objects.requireNonNull(map.get("userId")))
                .ifPresentOrElse(
                    user -> {
                        // Verify User
                        if (!user.verifyPassword(map.get("password"))) {
                            response.addHeader(HeaderKey.LOCATION, "/login/login_failed.html");
                            return;
                        }
                        // Create Session
                        String sessionId = SessionIdRegister.getInstance().register(user);
                        // set Set-Cookie header
                        response.addHeader(HeaderKey.SET_COOKIE,
                            "SID=" + sessionId + "; Path=/; httpOnly");
                        // Set Redirect Location
                        response.addHeader(HeaderKey.LOCATION, "/index.html");

                    },
                    () -> response.addHeader(HeaderKey.LOCATION, "/login/login_failed.html")
                );
            return response;
        };
        EndPoint<String> endPoint = EndPoint.of("/login", biFunction);
        endPointRegister.addEndpoint(HttpMethod.POST, endPoint);
    }

    void logout() {
        BiFunction<Map<String, String>, String, HttpResponse> biFunction = (headers, body) -> {
            HttpResponse response = HttpResponse.from(StatusCode.FOUND);
            Optional.ofNullable(headers.get(HeaderKey.COOKIE.getValue()))
                .ifPresentOrElse(
                    cookie -> {
                        cookie = cookie.split("=")[1];
                        SessionIdRegister.getInstance().unregister(cookie);
                        response.addHeader(HeaderKey.SET_COOKIE,
                            "SID=" + cookie + "; Path=/; httpOnly; Max-Age=0");
                    },
                    () -> {
                        throw new HttpCommonException("로그인 세션이 존재하지 않습니다.",
                            StatusCode.UNAUTHORIZED);
                    }
                );
            response.addHeader(HeaderKey.LOCATION, "/");
            return response;
        };
        EndPoint<String> endPoint = EndPoint.of("/logout", biFunction);
        endPointRegister.addEndpoint(HttpMethod.POST, endPoint);
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
}
