package codesquad.codestagram.domain.user.storage;

import codesquad.codestagram.domain.user.domain.User;
import codesquad.server.database.ConnectManager;
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
import java.util.concurrent.atomic.AtomicLong;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDao {

    private static final Logger log = LoggerFactory.getLogger(UserDao.class);

    private final ConnectManager connectManager;
    private final AtomicLong id = new AtomicLong(2);

    private UserDao(H2ConnectManager connectManager) {
        this.connectManager = connectManager;
    }

    public void save(User user) {
        String insertSql = "INSERT INTO USERS (ID, USER_ID, PASSWORD, NAME, EMAIL) VALUES ( ?, ?, ?, ?, ?)";
        try (Connection connection = connectManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(
            insertSql)) {
            preparedStatement.setLong(1, id.getAndAdd(1));
            preparedStatement.setString(2, user.getUserId());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getName());
            preparedStatement.setString(5, user.getEmail());
            preparedStatement.executeUpdate();
        } catch (JdbcSQLIntegrityConstraintViolationException jsicve) {
            throw new IllegalArgumentException("이미 존재하는 사용자명입니다");
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    public Optional<User> findById(String userId) {
        String findByIdSql = "SELECT USERS.ID, USERS.USER_ID, USERS.PASSWORD, USERS.NAME, USERS.EMAIL FROM USERS WHERE USER_ID = ?";
        try (Connection connection = connectManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(
            findByIdSql)) {
            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User user = User.of(
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
        String findAllSql = "SELECT USERS.ID, USERS.USER_ID, USERS.PASSWORD, USERS.NAME, USERS.EMAIL FROM USERS";
        try (Connection connection = connectManager.getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(
            findAllSql)) {
            while (resultSet.next()) {
                User user = User.of(
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
