package codesquad.codestagram.domain.user.domain;

import codesquad.codestagram.domain.user.domain.values.Email;
import java.util.Map;
import java.util.Objects;

public class User {

    private Long id;
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

    private User(Long id, String userId, String password, String name, Email email) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public static User from(Long id, String userId, String password, String name, String email) {
        return new User(id, userId, password, name, Email.from(email));
    }

    public static User from(Map<String, String> queryMap) {
        return new User(queryMap.get("userId"), queryMap.get("password"), queryMap.get("name"),
            queryMap.get("email"));
    }

    public boolean verifyPassword(String password) {
        return this.password.equals(password);
    }

    public Long getId() {
        return id;
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
