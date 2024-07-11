package codesquad.register;

import codesquad.http.session.Session;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SessionStorage {

    private static SessionStorage instance;

    private final Map<String, Session> sessionMap;

    private SessionStorage() {
        this.sessionMap = new ConcurrentHashMap<>();
    }

    public static SessionStorage getInstance() {
        if (instance == null) {
            instance = new SessionStorage();
        }
        return instance;
    }

    public Session save(Session session) {
        sessionMap.put(session.getSessionId(), session);
        return session;
    }

    public Optional<Session> getSession(String sessionId) {
        return Optional.ofNullable(sessionMap.get(sessionId))
            .filter(session -> !session.isExpired(System.currentTimeMillis())).map(session -> {
                session.access(System.currentTimeMillis());
                return save(session);
            }).or(() -> {
                remove(sessionId);
                return Optional.empty();
            });
    }

    public void remove(String sessionId) {
        sessionMap.remove(sessionId);
    }

}
