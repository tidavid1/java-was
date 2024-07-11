package codesquad.server.http.session;

import java.util.HashMap;
import java.util.Map;

public class Session {

    static final long MAX_INACTIVE_INTERVAL = 30L * 60L * 1000L;

    private final String sessionId;
    private long expiredTime;
    private final Map<String, Object> attributes;

    public Session(String sessionId, long currentTime) {
        this.sessionId = sessionId;
        this.expiredTime = currentTime + MAX_INACTIVE_INTERVAL;
        this.attributes = new HashMap<>();
    }

    public String getSessionId() {
        return sessionId;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public void access(long currentTime) {
        expiredTime = currentTime + MAX_INACTIVE_INTERVAL;
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public boolean isExpired(long currentTime) {
        return currentTime > expiredTime;
    }


}
