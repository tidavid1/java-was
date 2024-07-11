package codesquad.http.filter;

import codesquad.http.servlet.HttpServletRequest;
import codesquad.http.servlet.HttpServletResponse;
import codesquad.http.session.SessionContext;

/**
 * SessionContextClearFilter
 * <p>
 * 세션 정보를 제거하는 필터입니다. ThreadLocal 제거 용도로 사용합니다.
 */
public class SessionContextClearFilter implements Filter {

    private static SessionContextClearFilter instance;

    private SessionContextClearFilter() {
    }

    public static SessionContextClearFilter getInstance() {
        if (instance == null) {
            instance = new SessionContextClearFilter();
        }
        return instance;
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain) {
        // 세션 정보 제거
        SessionContext.removeSession();
        chain.doFilter(request, response);
    }
}
