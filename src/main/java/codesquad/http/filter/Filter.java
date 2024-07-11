package codesquad.http.filter;

import codesquad.http.servlet.HttpServletRequest;
import codesquad.http.servlet.HttpServletResponse;

public interface Filter {

    void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain);

}
