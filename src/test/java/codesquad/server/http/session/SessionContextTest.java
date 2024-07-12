package codesquad.server.http.session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("세션 컨텍스트에 ")
class SessionContextTest {

    @AfterEach
    void clear() {
        SessionContext.removeSession();
    }

    @Test
    @DisplayName("세션을 추가한다.")
    void setSessionToSessionContext() {
        // Arrange
        Session session = new Session("test", System.currentTimeMillis());
        // Act
        SessionContext.setSession(session);
        // Assert
        assertEquals(session, SessionContext.getSession());
    }

    @Test
    @DisplayName("세션을 제거한다.")
    void removeSessionFromSessionContext() {
        // Arrange
        Session session = new Session("test", System.currentTimeMillis());
        // Act
        SessionContext.setSession(session);
        SessionContext.removeSession();
        // Assert
        assertNull(SessionContext.getSession());
    }

    @Test
    @DisplayName("추가된 세션을 가져온다.")
    void getSessionFromSessionContext() {
        // Arrange
        Session session = new Session("test", System.currentTimeMillis());
        // Act
        SessionContext.setSession(session);
        // Assert
        assertEquals(session, SessionContext.getSession());
    }
}