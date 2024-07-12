package codesquad.server.http.filter;

import codesquad.server.http.servlet.HttpServletRequest;
import codesquad.server.http.servlet.HttpServletResponse;
import java.util.List;


public class SecurityFilterChain implements FilterChain {

    private final List<Filter> filters;
    private int currentFilterIndex = 0;

    public SecurityFilterChain(Filter... filters) {
        this.filters = List.of(filters);
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response) {
        if (currentFilterIndex < filters.size()) {
            Filter nextFilter = filters.get(currentFilterIndex++);
            nextFilter.doFilter(request, response, this);
        }
    }
}
