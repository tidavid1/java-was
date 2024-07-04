package codesquad.register;

import codesquad.model.User;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserRegister {

    private static final UserRegister INSTANCE = new UserRegister();
    private final Map<String, User> userRepository;

    private UserRegister() {
        userRepository = new ConcurrentHashMap<>();
    }

    public static UserRegister getInstance() {
        return INSTANCE;
    }

    public User save(User user) {
        return userRepository.put(user.getUserId(), user);
    }
}
