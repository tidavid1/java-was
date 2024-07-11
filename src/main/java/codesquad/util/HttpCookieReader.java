package codesquad.util;

import java.net.HttpCookie;

public class HttpCookieReader {

    private HttpCookieReader() {
    }

    public static String readCookie(HttpCookie cookie) {
        StringBuilder sb = new StringBuilder();
        sb.append(cookie.getName()).append("=").append(cookie.getValue());
        if (cookie.getDomain() != null && !cookie.getDomain().isEmpty()) {
            sb.append("; Domain=").append(cookie.getDomain());
        }
        if (cookie.getPath() != null && !cookie.getPath().isEmpty()) {
            sb.append("; Path=").append(cookie.getPath());
        }
        if (cookie.getMaxAge() >= 0) {
            sb.append("; Max-Age=").append(cookie.getMaxAge());
        }
        if (cookie.getSecure()) {
            sb.append("; Secure");
        }
        if (cookie.isHttpOnly()) {
            sb.append("; HttpOnly");
        }
        if (cookie.getVersion() > 0) {
            sb.append("; Version=").append(cookie.getVersion());
        }
        if (cookie.getComment() != null && !cookie.getComment().isEmpty()) {
            sb.append("; Comment=").append(cookie.getComment());
        }
        if (cookie.getCommentURL() != null && !cookie.getCommentURL().isEmpty()) {
            sb.append("; CommentURL=").append(cookie.getCommentURL());
        }
        if (cookie.getPortlist() != null && !cookie.getPortlist().isEmpty()) {
            sb.append("; Port=").append(cookie.getPortlist());
        }
        if (cookie.getDiscard()) {
            sb.append("; Discard");
        }
        return sb.toString();
    }

}
