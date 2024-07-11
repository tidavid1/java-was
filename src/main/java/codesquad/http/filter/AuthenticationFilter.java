package codesquad.http.filter;

import codesquad.http.servlet.HttpServletRequest;
import codesquad.http.servlet.HttpServletResponse;
import codesquad.http.session.Session;
import codesquad.http.session.SessionContext;
import java.util.List;

/**
 * AuthenticationFilter
 * <p>
 * 인증 필터는 특정 경로에 대한 인증을 진행합니다. 인증이 필요한 경로에 대해서는 세션 정보를 확인하여 인증을 진행합니다. 인증이 필요한 경로에 대해서는 세션 정보가 없을 경우
 * 로그인 페이지로 이동합니다.
 */
public class AuthenticationFilter implements Filter {

    // TODO: 자동주입
    private static AuthenticationFilter instance;

    private final List<String> authenticationPathList;

    private AuthenticationFilter(String... paths) {
        this.authenticationPathList = List.of(paths);
    }

    public static AuthenticationFilter createInstance(String... paths) {
        if (instance != null) {
            throw new IllegalStateException("이미 존재하는 인스턴스가 있습니다.");
        }
        instance = new AuthenticationFilter(paths);
        return instance;
    }

    public static AuthenticationFilter getInstance() {
        if (instance == null) {
            throw new IllegalStateException("존재하지 않는 인스턴스입니다.");
        }
        return instance;
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain) {
        String path = request.getRequest().getUri().getPath();
        // 검증하기
        if (authenticationPathList.contains(path)) {
            Session session = SessionContext.getSession();
            if (session == null) {
                response.sendRedirect("/login");
            }
        }
        chain.doFilter(request, response);
    }
}
