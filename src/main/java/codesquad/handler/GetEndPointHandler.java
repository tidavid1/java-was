package codesquad.handler;

import codesquad.exception.UnauthorizedException;
import codesquad.http.HttpResponse;
import codesquad.http.enums.HeaderKey;
import codesquad.http.enums.HttpMethod;
import codesquad.http.enums.StatusCode;
import codesquad.model.User;
import codesquad.register.EndPointRegister;
import codesquad.register.SessionIdRegister;
import codesquad.register.model.EndPoint;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

public class GetEndPointHandler implements EndPointHandler {

    private static final GetEndPointHandler INSTANCE = new GetEndPointHandler();

    private final EndPointRegister endPointRegister;

    private GetEndPointHandler() {
        this.endPointRegister = EndPointRegister.getInstance();
    }

    public static GetEndPointHandler getInstance() {
        return INSTANCE;
    }

    @Override
    public void provideAll() {
        home();
        loginHome();
        registration();
        login();
    }

    @SuppressWarnings("unchecked")
    void home() {
        EndPoint<String> staticEndPoint = (EndPoint<String>) endPointRegister.getEndpoint(
            HttpMethod.GET, "/index.html");
        BiFunction<Map<String, String>, String, HttpResponse> biFunction = (headers, query) -> {
            try {
                Optional.ofNullable(headers.get(HeaderKey.COOKIE.getValue()))
                    .ifPresentOrElse(
                        cookie -> {
                        }
                        , () -> {
                            throw new UnauthorizedException("세션이 존재하지 않습니다.");
                        });
                HttpResponse response = HttpResponse.from(StatusCode.FOUND);
                response.addHeader(HeaderKey.LOCATION, "/main");
                return response;
            } catch (UnauthorizedException ue) {
                return staticEndPoint.apply(headers, query);
            }
        };
        endPointRegister.addEndpoint(HttpMethod.GET, EndPoint.of("/", biFunction));
    }

    @SuppressWarnings("unchecked")
    void loginHome() {
        EndPoint<String> staticEndPoint = (EndPoint<String>) endPointRegister.getEndpoint(
            HttpMethod.GET, "/main/index.html");
        BiFunction<Map<String, String>, String, HttpResponse> biFunction = (headers, query) -> {
            try {
                String cookie = Optional.ofNullable(headers.get(HeaderKey.COOKIE.getValue()))
                    .orElseThrow(() -> new UnauthorizedException("세션이 존재하지 않습니다."));
                cookie = cookie.split("=")[1];
                // TODO:  User 정보 등록을 위한 데이터 추출
                User user = SessionIdRegister.getInstance().findBySessionId(cookie)
                    .orElseThrow(() -> new UnauthorizedException("일치하지 않는 세션 ID입니다."));
                return staticEndPoint.apply(headers, query);
            } catch (UnauthorizedException ue) {
                HttpResponse response = HttpResponse.from(StatusCode.FOUND);
                response.addHeader(HeaderKey.LOCATION, "/");
                return response;
            }
        };
        endPointRegister.addEndpoint(HttpMethod.GET, EndPoint.of("/main", biFunction));
    }

    void registration() {
        EndPoint<?> staticEndPoint = endPointRegister.getEndpoint(HttpMethod.GET,
            "/registration/index.html");
        endPointRegister.addEndpoint(HttpMethod.GET,
            EndPoint.of("/registration", staticEndPoint.getBiFunction()));
    }

    void login() {
        EndPoint<?> staticEndPoint = endPointRegister.getEndpoint(HttpMethod.GET,
            "/login/index.html");
        endPointRegister.addEndpoint(HttpMethod.GET,
            EndPoint.of("/login", staticEndPoint.getBiFunction()));
    }
}
