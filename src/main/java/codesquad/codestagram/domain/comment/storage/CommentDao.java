package codesquad.codestagram.domain.comment.storage;

import codesquad.codestagram.domain.comment.domain.Comment;
import codesquad.server.database.ConnectManager;
import codesquad.server.database.H2ConnectManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommentDao {

    private static final Logger log = LoggerFactory.getLogger(CommentDao.class);

    private final ConnectManager connectManager;
    private final AtomicLong id = new AtomicLong(4);

    private CommentDao(H2ConnectManager connectManager) {
        this.connectManager = connectManager;
    }

    public void save(Comment comment) {
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

    public Optional<Comment> findById(Long id) {
        String findByIdSql = "select COMMENTS.ID, COMMENTS.BODY, COMMENTS.USER_ID, COMMENTS.USERNAME, COMMENTS.ARTICLE_ID as AI from COMMENTS WHERE ID = ?";
        try (Connection connection = connectManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(
            findByIdSql)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Comment comment = new Comment(
                    resultSet.getLong("id"),
                    resultSet.getString("body"),
                    resultSet.getLong("user_id"),
                    resultSet.getString("username"),
                    resultSet.getLong("article_id"));
                return Optional.of(comment);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    public List<Comment> findAllByArticleId(Long articleId) {
        List<Comment> comments = new ArrayList<>();
        String findAllByArticleIdSql = "select COMMENTS.ID, COMMENTS.BODY, COMMENTS.USER_ID, COMMENTS.USERNAME, COMMENTS.ARTICLE_ID as AI from COMMENTS WHERE ARTICLE_ID = ?";
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


}
