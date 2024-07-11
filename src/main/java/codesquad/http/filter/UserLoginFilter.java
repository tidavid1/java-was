package codesquad.http.filter;

import codesquad.http.servlet.HttpServletRequest;
import codesquad.http.servlet.HttpServletResponse;
import codesquad.http.session.SessionContext;
import codesquad.register.SessionStorage;
import java.net.HttpCookie;
import java.util.Optional;

/**
 * UserLoginFilter
 * <p>
 * Cookie의 SID를 통한 로그인을 진행합니다. SID가 존재하면 세션 정보 검증을 통해 SessionContext에 세션을 저장합니다.
 */
public class UserLoginFilter implements Filter {

    private static UserLoginFilter instance;

    private final SessionStorage sessionStorage = SessionStorage.getInstance();

    private UserLoginFilter() {
    }

    public static UserLoginFilter getInstance() {
        if (instance == null) {
            instance = new UserLoginFilter();
        }
        return instance;
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain) {
        Optional<HttpCookie> cookie = request.getCookie("SID");
        cookie.ifPresent(
            httpCookie -> {
                // 세션 ID 조회
                String sessionId = httpCookie.getValue();
                // 세션 검증 및 세션 저장
                sessionStorage.getSession(sessionId).ifPresent(SessionContext::setSession);
            }
        );
        chain.doFilter(request, response);
    }
}
