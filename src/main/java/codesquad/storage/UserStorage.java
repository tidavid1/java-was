package codesquad.storage;

import codesquad.model.User;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserStorage {

    private static final Logger log = LoggerFactory.getLogger(UserStorage.class);

    private static final UserStorage INSTANCE = new UserStorage();
    private final Map<String, User> userRepository = new ConcurrentHashMap<>();

    private UserStorage() {
    }

    public static UserStorage getInstance() {
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

    public Optional<User> findById(String userId) {
        return Optional.ofNullable(userRepository.get(userId));
    }

    public List<User> findAll() {
        return userRepository.values().stream().toList();
    }
}
