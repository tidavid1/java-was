package codesquad.codestagram.domain.comment.storage;

import codesquad.codestagram.domain.comment.domain.Comment;
import codesquad.server.database.ConnectManager;
import codesquad.server.database.CsvConnectManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommentDao {

    private static final Logger log = LoggerFactory.getLogger(CommentDao.class);

    private final ConnectManager connectManager;
    private AtomicLong id;

    private CommentDao(CsvConnectManager connectManager) {
        this.connectManager = connectManager;
    }

    public void save(Comment comment) {
        verifyId();
        String insertSql = "INSERT INTO COMMENTS (ID, BODY, USER_ID, USERNAME ,ARTICLE_ID) VALUES ( ?, ?, ?, ?, ? )";
        try (Connection connection = connectManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(
            insertSql)) {
            preparedStatement.setLong(1, id.getAndAdd(1));
            preparedStatement.setString(2, comment.getBody());
            preparedStatement.setLong(3, comment.getUserId());
            preparedStatement.setString(4, comment.getUsername());
            preparedStatement.setLong(5, comment.getArticleId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    public List<Comment> findAll() {
        List<Comment> comments = new ArrayList<>();
        String findAllSql = "select * from COMMENTS";
        try (Connection connection = connectManager.getConnection(); Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(findAllSql);
            while (resultSet.next()) {
                Comment comment = new Comment(
                    resultSet.getLong("id"),
                    resultSet.getString("body"),
                    resultSet.getLong("user_id"),
                    resultSet.getString("username"),
                    resultSet.getLong("article_id"));
                comments.add(comment);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Collections.unmodifiableList(comments);
    }

    public List<Comment> findAllByArticleId(Long articleId) {
        List<Comment> comments = new ArrayList<>();
        String findAllByArticleIdSql = "select * from COMMENTS WHERE ARTICLE_ID = ?";
        try (Connection connection = connectManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(
            findAllByArticleIdSql)) {
            preparedStatement.setLong(1, articleId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Comment comment = new Comment(
                    resultSet.getLong("id"),
                    resultSet.getString("body"),
                    resultSet.getLong("user_id"),
                    resultSet.getString("username"),
                    resultSet.getLong("article_id"));
                comments.add(comment);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Collections.unmodifiableList(comments);
    }

    private void verifyId() {
        if (id == null) {
            id = new AtomicLong(findAll().size() + 1L);
        }
    }

}
