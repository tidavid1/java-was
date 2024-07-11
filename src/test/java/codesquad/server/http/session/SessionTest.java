package codesquad.server.http.session;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.server.util.RandomSessionIDGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("세션을")
class SessionTest {

    @Test
    @DisplayName("생성한다.")
    void createSession() {
        // Arrange
        String expectedSessionId = RandomSessionIDGenerator.generate();
        long currentTime = System.currentTimeMillis();
        // Act
        Session actualResult = new Session(expectedSessionId, currentTime);
        // Assert
        assertThat(actualResult)
            .extracting("sessionId", "expiredTime")
            .containsExactly(expectedSessionId, currentTime + Session.MAX_INACTIVE_INTERVAL);
    }

    @Nested
    @DisplayName("생성한 후")
    class whenCreatedSession {

        @Test
        @DisplayName("접근하면 만료시간이 갱신된다.")
        void accessSessionWillUpdateExpiredTime() {
            // Arrange
            String sessionId = RandomSessionIDGenerator.generate();
            long currentTime = System.currentTimeMillis();
            Session session = new Session(sessionId, currentTime);
            long expectedExpiredTime = currentTime + Session.MAX_INACTIVE_INTERVAL;
            // Act
            session.access(currentTime);
            // Assert
            assertThat(session.getExpiredTime()).isEqualTo(expectedExpiredTime);
        }

        @Test
        @DisplayName("만료 시간이 지나면 만료된다.")
        void sessionIsExpiredWhenExpiredTimePassed() {
            // Arrange
            String sessionId = RandomSessionIDGenerator.generate();
            long currentTime = System.currentTimeMillis();
            Session session = new Session(sessionId, currentTime);
            // Act
            boolean actualResult = session.isExpired(
                currentTime + Session.MAX_INACTIVE_INTERVAL + 1);
            // Assert
            assertThat(actualResult).isTrue();
        }

        @Test
        @DisplayName("어트리뷰트를 추가한다.")
        void addAttribute() {
            // Arrange
            String sessionId = RandomSessionIDGenerator.generate();
            long currentTime = System.currentTimeMillis();
            Session session = new Session(sessionId, currentTime);
            String key = "key";
            Object value = "value";
            // Act
            session.setAttribute(key, value);
            // Assert
            assertThat(session.getAttribute(key)).isEqualTo(value);
        }

        @Test
        @DisplayName("어트리뷰트를 가져온다.")
        void getAttribute() {
            // Arrange
            String sessionId = RandomSessionIDGenerator.generate();
            long currentTime = System.currentTimeMillis();
            Session session = new Session(sessionId, currentTime);
            String key = "key";
            Object value = "value";
            session.setAttribute(key, value);
            // Act
            Object actualResult = session.getAttribute(key);
            // Assert
            assertThat(actualResult).isEqualTo(value);
        }
    }
}