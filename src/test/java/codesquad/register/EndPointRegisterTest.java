package codesquad.register;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import codesquad.http.enums.HttpMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EndPointRegisterTest {

    private EndPointRegister register;

    @BeforeEach
    void setUp() {
        register = EndPointRegister.getInstance();
    }

    @Test
    @DisplayName("Endpoint를 저장한다.")
    void testAddEndpoint() {
        // Arrange
        var expectedEndpoint = new EndPoint("/index.html", () -> new byte[0]);
        // Act
        register.addEndpoint(HttpMethod.GET, expectedEndpoint);
        // Assert
        assertThat(register.getEndpoint(HttpMethod.GET, "/index.html")).isEqualTo(expectedEndpoint);
    }

    @Test
    @DisplayName("등록된 Endpoint를 반환한다.")
    void testGetEndpointSuccess() {
        // Arrange
        var expectedEndpoint = new EndPoint("/index.html", () -> new byte[0]);
        register.addEndpoint(HttpMethod.GET, expectedEndpoint);
        // Act
        var actualEndpoint = register.getEndpoint(HttpMethod.GET, "/index.html");
        // Assert
        assertThat(actualEndpoint).isEqualTo(expectedEndpoint);
    }

    @Test
    @DisplayName("등록되지 않은 Endpoint를 요청하면 예외를 던진다.")
    void testGetEndpointFailWhenNotFound() {
        // Act & Assert
        assertThatThrownBy(() -> register.getEndpoint(HttpMethod.GET, "/index2.html"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("404 Not Found");
    }
}