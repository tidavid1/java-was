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
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    title      VARCHAR(255) NOT NULL,
    body       TEXT,
    image_path VARCHAR(255) NOT NULL,
    user_id    BIGINT       NOT NULL,
    username   VARCHAR(255) NOT NULL
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

INSERT INTO USERS (user_id, password, name, email)
SELECT 'account', 'account', 'account', 'account@gmail.com'
WHERE NOT EXISTS(SELECT 1 FROM USERS WHERE user_id = 'account');

INSERT INTO ARTICLES (title, body, image_path, user_id, username)
SELECT '리엑티브 시스템(Reactive System)',
       '우리는 시스템 아키텍처에 대한 일관성 있는 접근이 필요하며, 필요한모든 측면은 이미 개별적으로 인식되고 있다고 생각합니다. 즉, 응답이 잘 되고, 탄력적이며 유연하고 메시지 기반으로 동작하는 시스템 입니다. 우리는 이것을 리액티브 시스템(Reactive Systems)라고 부릅니다. 리액티브 시스템으로 구축된 시스템은 보다 유연하고, 느슨한 결합을 갖고, 확장성 이 있습니다. 이로 인해 개발이 더 쉬워지고 변경 사항을 적용하기 쉬워집니다. 이 시스템은 장애 에 대해 더 강한 내성을 지니며, 비록 장애가 발생 하더라도, 재난이 일어나기 보다는 간결한 방식으로 해결합니다. 리액티브 시스템은 높은 응답성을 가지며 사용자 에게 효과적인 상호적 피드백을 제공합니다.',
       './default.webp',
       1,
       'account'
WHERE NOT EXISTS(SELECT 1 FROM ARTICLES WHERE title = '리엑티브 시스템(Reactive System)');

INSERT INTO COMMENTS (body, user_id, username, article_id)
SELECT '1번', 1, 'account', 1
WHERE NOT EXISTS(SELECT 1 FROM COMMENTS WHERE body = '1번' AND article_id = 1);

INSERT INTO COMMENTS (body, user_id, username, article_id)
SELECT '2번', 1, 'account', 1
WHERE NOT EXISTS(SELECT 1 FROM COMMENTS WHERE body = '2번' AND article_id = 1);

INSERT INTO COMMENTS (body, user_id, username, article_id)
SELECT '3번', 1, 'account', 1
WHERE NOT EXISTS(SELECT 1 FROM COMMENTS WHERE body = '3번' AND article_id = 1);