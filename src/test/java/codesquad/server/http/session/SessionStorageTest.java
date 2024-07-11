package codesquad.server.http.session;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.server.util.RandomSessionIDGenerator;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("세션 스토리지를")
class SessionStorageTest {

    private final String sessionId = RandomSessionIDGenerator.generate();
    private final Session session = new Session(sessionId, System.currentTimeMillis());

    @Test
    @DisplayName("싱글턴 객체로 가지고 온다")
    void getSingletonObject() {
        // Act
        SessionStorage sessionStorage1 = SessionStorage.getInstance();
        SessionStorage sessionStorage2 = SessionStorage.getInstance();
        // Assert
        assertThat(sessionStorage1).isEqualTo(sessionStorage2);
    }

    @Nested
    @DisplayName("가져와 ")
    class bringSessionStorage {

        @Test
        @DisplayName("세션을 저장한다.")
        void saveSession() {
            // Arrange
            SessionStorage sessionStorage = SessionStorage.getInstance();
            // Act
            Session actualResult = sessionStorage.save(session);
            // Assert
            assertThat(actualResult).isEqualTo(session);
        }

        @Nested
        @DisplayName("세션을 조회할 때")
        class whenGetSession {

            @Test
            @DisplayName("정상 조회한다.")
            void getSession() {
                // Arrange
                SessionStorage sessionStorage = SessionStorage.getInstance();
                sessionStorage.save(session);
                // Act
                Optional<Session> actualResult = sessionStorage.getSession(sessionId);
                // Assert
                assertThat(actualResult).isPresent().contains(session);
            }

            @Test
            @DisplayName("만료된 세션은 조회할 수 없다.")
            void getSessionWhenSessionIsExpired() {
                // Arrange
                SessionStorage sessionStorage = SessionStorage.getInstance();
                Session expiredSession = new Session("test", 0);
                sessionStorage.save(expiredSession);
                // Act
                Optional<Session> actualResult = sessionStorage.getSession("test");
                // Assert
                assertThat(actualResult).isEmpty();
            }
        }

        @Test
        @DisplayName("세션을 삭제한다.")
        void removeSession() {
            // Arrange
            SessionStorage sessionStorage = SessionStorage.getInstance();
            sessionStorage.save(session);
            // Act
            sessionStorage.remove(sessionId);
            // Assert
            assertThat(sessionStorage.getSession(sessionId)).isEmpty();
        }


    }
}