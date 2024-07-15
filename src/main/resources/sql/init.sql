CREATE TABLE IF NOT EXISTS users
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id  VARCHAR(255),
    password VARCHAR(255),
    name     VARCHAR(255),
    email    VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS articles
(
    id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    body  TEXT
);

CREATE TABLE IF NOT EXISTS comments
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    body       VARCHAR(255) NOT NULL,
    article_id BIGINT,
    CONSTRAINT fk_article FOREIGN KEY (article_id) REFERENCES articles (id) ON DELETE CASCADE
);