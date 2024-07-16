package codesquad.server.http.filter;

import codesquad.server.http.servlet.HttpServletRequest;
import codesquad.server.http.servlet.HttpServletResponse;
import codesquad.server.http.session.SessionContext;
import codesquad.server.http.session.SessionStorage;
import java.net.HttpCookie;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UserLoginFilter
 * <p>
 * Cookie의 SID를 통한 로그인을 진행합니다. SID가 존재하면 세션 정보 검증을 통해 SessionContext에 세션을 저장합니다.
 */
public class UserLoginFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(UserLoginFilter.class);
    private final SessionStorage sessionStorage = SessionStorage.getInstance();

    private UserLoginFilter() {
    }


    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain) {
        if (request.getRequest() != null) {
            Optional<HttpCookie> cookie = request.getCookie("SID");
            log.debug("Cookie: {}", cookie);
            cookie.ifPresent(
                httpCookie -> {
                    // 세션 ID 조회
                    String sessionId = httpCookie.getValue();
                    // 세션 검증 및 세션 저장
                    sessionStorage.getSession(sessionId).ifPresent(SessionContext::setSession);
                }
            );
        }
        chain.doFilter(request, response);
    }
}
