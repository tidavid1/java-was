package codesquad.http.filter;

import codesquad.http.servlet.HttpServletRequest;
import codesquad.http.servlet.HttpServletResponse;

public interface FilterChain {

    void doFilter(HttpServletRequest request, HttpServletResponse response);
}
