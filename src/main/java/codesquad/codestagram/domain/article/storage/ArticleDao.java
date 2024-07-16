package codesquad.codestagram.domain.article.storage;

import codesquad.codestagram.domain.article.domain.Article;
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

public class ArticleDao {

    private static final Logger log = LoggerFactory.getLogger(ArticleDao.class);

    private final H2ConnectManager h2ConnectManager;

    private ArticleDao(H2ConnectManager h2ConnectManager) {
        this.h2ConnectManager = h2ConnectManager;
    }

    public void save(Article article) {
        String insertSql = "INSERT INTO ARTICLES (title, body, user_id) VALUES (?, ?, ?)";
        try (Connection connection = h2ConnectManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(
            insertSql)) {
            preparedStatement.setString(1, article.getTitle());
            preparedStatement.setString(2, article.getBody());
            preparedStatement.setLong(3, article.getUserId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    public Optional<Article> findById(Long id) {
        String findByIdSql = "SELECT ARTICLES.ID, ARTICLES.TITLE, ARTICLES.BODY, ARTICLES.USER_ID FROM ARTICLES WHERE id = ?";
        try (Connection connection = h2ConnectManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(
            findByIdSql)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Article article = new Article(
                    resultSet.getLong("id"),
                    resultSet.getString("title"),
                    resultSet.getString("body"),
                    resultSet.getLong("user_id"));
                return Optional.of(article);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    public List<Article> findAll() {
        List<Article> articles = new ArrayList<>();
        String findAllSql = "SELECT ARTICLES.ID, ARTICLES.TITLE, ARTICLES.BODY, ARTICLES.USER_ID FROM ARTICLES";
        try (Connection connection = h2ConnectManager.getConnection(); Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(findAllSql);
            while (resultSet.next()) {
                Article article = new Article(
                    resultSet.getLong("id"),
                    resultSet.getString("title"),
                    resultSet.getString("body"),
                    resultSet.getLong("user_id"));
                articles.add(article);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Collections.unmodifiableList(articles);
    }

}
