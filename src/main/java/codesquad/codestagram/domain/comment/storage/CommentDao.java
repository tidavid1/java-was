package codesquad.codestagram.domain.comment.storage;

import codesquad.codestagram.domain.comment.domain.Comment;
import codesquad.server.database.H2ConnectManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommentDao {

    private static final Logger log = LoggerFactory.getLogger(CommentDao.class);
    private static CommentDao instance;

    private final H2ConnectManager h2ConnectManager;

    private CommentDao() {
        this.h2ConnectManager = H2ConnectManager.getInstance();
    }

    public static CommentDao getInstance() {
        if (instance == null) {
            instance = new CommentDao();
        }
        return instance;
    }

    public void save(Comment comment) {
        String insertSql = "INSERT INTO COMMENTS (BODY, ARTICLE_ID) VALUES ( ?, ? )";
        try (Connection connection = h2ConnectManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(
            insertSql)) {
            preparedStatement.setString(1, comment.getBody());
            preparedStatement.setLong(2, comment.getArticleId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    public Optional<Comment> findById(Long id) {
        String findByIdSql = "select COMMENTS.ID, COMMENTS.BODY, COMMENTS.ARTICLE_ID as AI from COMMENTS WHERE ID = ?";
        try (Connection connection = h2ConnectManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(
            findByIdSql)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Comment comment = new Comment(resultSet.getLong("id"), resultSet.getString("body"),
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
        String findAllByArticleIdSql = "select COMMENTS.ID, COMMENTS.BODY, COMMENTS.ARTICLE_ID as AI from COMMENTS WHERE ARTICLE_ID = ?";
        try (Connection connection = h2ConnectManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(
            findAllByArticleIdSql)) {
            preparedStatement.setLong(1, articleId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Comment comment = new Comment(resultSet.getLong("id"), resultSet.getString("body"),
                    resultSet.getLong("article_id"));
                comments.add(comment);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Collections.unmodifiableList(comments);
    }


}
