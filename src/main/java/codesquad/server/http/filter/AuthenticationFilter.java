package codesquad.server.http.filter;

import codesquad.server.http.servlet.HttpServletRequest;
import codesquad.server.http.servlet.HttpServletResponse;
import codesquad.server.http.session.Session;
import codesquad.server.http.session.SessionContext;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AuthenticationFilter
 * <p>
 * 인증 필터는 특정 경로에 대한 인증을 진행합니다. 인증이 필요한 경로에 대해서는 세션 정보를 확인하여 인증을 진행합니다. 인증이 필요한 경로에 대해서는 세션 정보가 없을 경우
 * 로그인 페이지로 이동합니다.
 */
public class AuthenticationFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);
    private final List<String> authenticationPathList;

    private AuthenticationFilter() {
        this.authenticationPathList = List.of("/user/list", "/logout", "/write.html", "/write",
            "/article", "/comment", "/comment/index.html");
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain) {
        if (request.getRequest() != null) {
            String path = request.getRequest().getRequestLine().getPath();
            log.debug("path: {}", path);
            // 검증하기
            if (authenticationPathList.contains(path)) {
                Session session = SessionContext.getSession();
                if (session == null) {
                    response.sendRedirect("/login");
                }
            }
        }
        chain.doFilter(request, response);
    }
}
