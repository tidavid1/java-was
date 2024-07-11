package codesquad.register;

import codesquad.exception.HttpCommonException;
import codesquad.http.servlet.enums.StatusCode;
import codesquad.model.User;
import codesquad.util.RandomSessionIDGenerator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SessionIdRegister {

    private static final SessionIdRegister INSTANCE = new SessionIdRegister();

    private final Map<String, User> sessionIdRepository = new ConcurrentHashMap<>();

    private SessionIdRegister() {
    }

    public static SessionIdRegister getInstance() {
        return INSTANCE;
    }

    public String register(User user) {
        String sessionId = RandomSessionIDGenerator.generate();
        sessionIdRepository.put(sessionId, user);
        return sessionId;
    }

    public User getUser(String sessionId) {
        return Optional.ofNullable(sessionIdRepository.get(sessionId))
            .orElseThrow(() -> new HttpCommonException("잘못된 쿠키입니다. : " + sessionId,
                StatusCode.UNAUTHORIZED));
    }

    public void unregister(String sessionId) {
        if (sessionId == null || !sessionIdRepository.containsKey(sessionId)) {
            throw new HttpCommonException("잘못된 쿠키입니다. : " + sessionId, StatusCode.UNAUTHORIZED);
        }
        sessionIdRepository.remove(sessionId);
    }

    public Optional<User> findBySessionId(String sessionId) {
        return Optional.ofNullable(sessionIdRepository.get(sessionId));
    }
}
