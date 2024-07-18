package codesquad.codestagram.domain.article.storage;

import codesquad.codestagram.domain.article.domain.Article;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArticleDao {

    private static final Logger log = LoggerFactory.getLogger(ArticleDao.class);

    private final ConnectManager connectManager;
    private final AtomicLong id = new AtomicLong(2);

    private ArticleDao(H2ConnectManager connectManager) {
        this.connectManager = connectManager;
    }

    public void save(Article article) {
        String insertSql = "INSERT INTO ARTICLES (id, title, body, image_path, user_id, username) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = connectManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(
            insertSql)) {
            preparedStatement.setLong(1, id.getAndAdd(1));
            preparedStatement.setString(2, article.getTitle());
            preparedStatement.setString(3, article.getBody());
            preparedStatement.setString(4, article.getImagePath());
            preparedStatement.setLong(5, article.getUserId());
            preparedStatement.setString(6, article.getUsername());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    public Optional<Article> findById(Long id) {
        String findByIdSql = "SELECT ARTICLES.ID, ARTICLES.TITLE, ARTICLES.BODY, ARTICLES.IMAGE_PATH ,ARTICLES.USER_ID, ARTICLES.USERNAME FROM ARTICLES WHERE id = ?";
        try (Connection connection = connectManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(
            findByIdSql)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Article article = new Article(
                    resultSet.getLong("id"),
                    resultSet.getString("title"),
                    resultSet.getString("body"),
                    resultSet.getString("image_path"),
                    resultSet.getLong("user_id"),
                    resultSet.getString("username"));
                return Optional.of(article);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    public List<Article> findAll() {
        List<Article> articles = new ArrayList<>();
        String findAllSql = "SELECT ARTICLES.ID, ARTICLES.TITLE, ARTICLES.BODY, ARTICLES.IMAGE_PATH ,ARTICLES.USER_ID, ARTICLES.USERNAME FROM ARTICLES";
        try (Connection connection = connectManager.getConnection(); Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(findAllSql);
            while (resultSet.next()) {
                Article article = new Article(
                    resultSet.getLong("id"),
                    resultSet.getString("title"),
                    resultSet.getString("body"),
                    resultSet.getString("image_path"),
                    resultSet.getLong("user_id"),
                    resultSet.getString("username"));
                articles.add(article);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Collections.unmodifiableList(articles);
    }

}
