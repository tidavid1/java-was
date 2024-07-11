package codesquad.server.http.filter;

import codesquad.server.http.servlet.HttpServletRequest;
import codesquad.server.http.servlet.HttpServletResponse;

public interface FilterChain {

    void doFilter(HttpServletRequest request, HttpServletResponse response);
}
