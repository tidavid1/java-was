package codesquad.server.http.filter;

import codesquad.server.http.servlet.HttpServletRequest;
import codesquad.server.http.servlet.HttpServletResponse;
import codesquad.server.http.session.Session;
import codesquad.server.http.session.SessionContext;
import java.util.List;

/**
 * AuthenticationFilter
 * <p>
 * 인증 필터는 특정 경로에 대한 인증을 진행합니다. 인증이 필요한 경로에 대해서는 세션 정보를 확인하여 인증을 진행합니다. 인증이 필요한 경로에 대해서는 세션 정보가 없을 경우
 * 로그인 페이지로 이동합니다.
 */
public class AuthenticationFilter implements Filter {

    private static AuthenticationFilter instance;

    private final List<String> authenticationPathList;

    // TODO: 자동 주입 해보까?
    private AuthenticationFilter() {
        this.authenticationPathList = List.of("/user/list", "/logout");
    }

    public static AuthenticationFilter getInstance() {
        if (instance == null) {
            instance = new AuthenticationFilter();
        }
        return instance;
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain) {
        if (request.getRequest() != null) {
            String path = request.getRequest().getUri().getPath();
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
