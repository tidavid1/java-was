package codesquad.register;

import codesquad.model.User;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRegister {

    private static final Logger log = LoggerFactory.getLogger(UserRegister.class);

    private static final UserRegister INSTANCE = new UserRegister();
    private final Map<String, User> userRepository = new ConcurrentHashMap<>();

    private UserRegister() {
    }

    public static UserRegister getInstance() {
        return INSTANCE;
    }

    public User save(User user) {
        if (userRepository.containsKey(user.getUserId())) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }
        log.debug("User: {}", user);
        userRepository.put(user.getUserId(), user);
        return user;
    }
}
