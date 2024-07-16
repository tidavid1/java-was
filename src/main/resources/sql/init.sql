CREATE TABLE IF NOT EXISTS users
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id  VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255)        NOT NULL,
    name     VARCHAR(255)        NOT NULL,
    email    VARCHAR(255)        NOT NULL
);

CREATE TABLE IF NOT EXISTS articles
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    title    VARCHAR(255) NOT NULL,
    body     TEXT,
    user_id  BIGINT       NOT NULL,
    username VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS comments
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    body       VARCHAR(255) NOT NULL,
    user_id    BIGINT       NOT NULL,
    username   VARCHAR(255) NOT NULL,
    article_id BIGINT,
    CONSTRAINT fk_article FOREIGN KEY (article_id) REFERENCES articles (id) ON DELETE CASCADE
);