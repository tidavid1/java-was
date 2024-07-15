package codesquad.codestagram.domain.user.storage;

import codesquad.codestagram.domain.user.domain.User;
import codesquad.server.database.H2ConnectManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserStorage {

    private static final Logger log = LoggerFactory.getLogger(UserStorage.class);
    private static UserStorage instance;

    private final H2ConnectManager h2ConnectManager;

    private UserStorage() {
        this.h2ConnectManager = H2ConnectManager.getInstance();
    }

    public static UserStorage getInstance() {
        if (instance == null) {
            instance = new UserStorage();
        }
        return instance;
    }

    public void save(User user) {
        String insertSql = "INSERT INTO USERS (USER_ID, PASSWORD, NAME, EMAIL) VALUES ( ?, ?, ?, ? )";
        try (Connection connection = h2ConnectManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(
            insertSql)) {
            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            // TODO: HOW Convert Duplicated User?
            log.error(e.getMessage());
        }
    }

    public Optional<User> findById(String userId) {
        String findByIdSql = "SELECT USERS.ID, USERS.USER_ID, USERS.PASSWORD, USERS.NAME, USERS.EMAIL FROM USERS WHERE USER_ID = ?";
        try (Connection connection = h2ConnectManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(
            findByIdSql)) {
            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User user = User.from(
                    resultSet.getLong("id"),
                    resultSet.getString("user_id"),
                    resultSet.getString("password"),
                    resultSet.getString("name"),
                    resultSet.getString("email")
                );
                return Optional.of(user);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String findAllSql = "";
        try (Connection connection = h2ConnectManager.getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(
            findAllSql)) {
            while (resultSet.next()) {
                User user = User.from(
                    resultSet.getLong("id"),
                    resultSet.getString("user_id"),
                    resultSet.getString("password"),
                    resultSet.getString("name"),
                    resultSet.getString("email")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return Collections.unmodifiableList(users);
    }
}
