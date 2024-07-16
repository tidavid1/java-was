package codesquad.server.http.filter;

import codesquad.codestagram.domain.user.domain.User;
import codesquad.server.http.exception.HttpCommonException;
import codesquad.server.http.servlet.HttpServletRequest;
import codesquad.server.http.servlet.HttpServletResponse;
import codesquad.server.http.session.SessionContext;
import codesquad.server.template.TemplateHTMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ExceptionHandlerFilter
 * <p>
 * 예외 처리를 담당하는 필터입니다. `HttpServletRequest`의 `Attributes`에 담겨 있는 예외를 가져와 Response에 핸들링해줍니다 :D
 */
public class ExceptionHandlerFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandlerFilter.class);
    private final TemplateHTMLFactory templateHTMLFactory;

    private ExceptionHandlerFilter(TemplateHTMLFactory templateHTMLFactory) {
        this.templateHTMLFactory = templateHTMLFactory;
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain) {
        HttpCommonException commonException = (HttpCommonException) request.getAttribute(
            "exception");
        if (commonException != null) {
            log.debug("Exception-> {}: {}", commonException.getStatusCode(),
                commonException.getMessage());
            response.setStatus(commonException.getStatusCode());
            response.setHeader("Content-Type", "text/html;charset=utf-8");
            byte[] data;
            if (SessionContext.getSession() == null) {
                data = templateHTMLFactory.exceptionPage(commonException);
            } else {
                User user = (User) SessionContext.getSession().getAttribute("user");
                data = templateHTMLFactory.exceptionPage(user, commonException);
            }
            response.setBody(data);
        }
        chain.doFilter(request, response);
    }

}
