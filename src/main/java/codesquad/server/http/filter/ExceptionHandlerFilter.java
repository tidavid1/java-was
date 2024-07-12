package codesquad.server.http.filter;

import codesquad.server.http.exception.HttpCommonException;
import codesquad.server.http.servlet.HttpServletRequest;
import codesquad.server.http.servlet.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ExceptionHandlerFilter
 * <p>
 * 예외 처리를 담당하는 필터입니다. `HttpServletRequest`의 `Attributes`에 담겨 있는 예외를 가져와 Response에 핸들링해줍니다 :D
 */
public class ExceptionHandlerFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandlerFilter.class);
    private static ExceptionHandlerFilter instance;

    private ExceptionHandlerFilter() {
    }

    public static ExceptionHandlerFilter getInstance() {
        if (instance == null) {
            instance = new ExceptionHandlerFilter();
        }
        return instance;
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
            response.setBody(generateErrorPage(commonException));
        }
        chain.doFilter(request, response);
    }

    private String generateErrorPage(HttpCommonException hce) {
        return "<h1>" + hce.getStatusCode().getCode() + " " + hce.getStatusCode().getMessage()
            + "</h1>\n" + "<h2>" + hce.getMessage() + "</h2>\n"
            + "<button onclick=\"history.back()\">뒤로가기</button>";
    }
}
