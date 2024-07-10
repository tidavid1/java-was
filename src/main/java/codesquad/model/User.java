package codesquad.model;

import codesquad.model.values.Email;
import java.util.Map;
import java.util.Objects;

public class User {

    private final String userId;
    private final String password;
    private final String name;
    private final Email email;

    private User(String userId, String password, String name, String email) {
        this.userId = Objects.requireNonNull(userId, "userId는 null일 수 없습니다.");
        this.password = Objects.requireNonNull(password, "password는 null일 수 없습니다.");
        this.name = Objects.requireNonNull(name, "name은 null일 수 없습니다.");
        this.email = Email.from(email);
    }

    public static User from(Map<String, String> queryMap) {
        return new User(queryMap.get("userId"), queryMap.get("password"), queryMap.get("name"),
            queryMap.get("email"));
    }

    public boolean verifyPassword(String password) {
        return this.password.equals(password);
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email.getValue();
    }

    @Override
    public String toString() {
        return "User{" +
            "userId='" + userId + '\'' +
            ", password='" + password + '\'' +
            ", name='" + name + '\'' +
            ", email='" + email.getValue() + '\'' +
            '}';
    }
}
