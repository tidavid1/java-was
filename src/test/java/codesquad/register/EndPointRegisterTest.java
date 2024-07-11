package codesquad.register;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import codesquad.exception.HttpCommonException;
import codesquad.http.servlet.HttpResponseDeprecated;
import codesquad.http.servlet.enums.HttpMethod;
import codesquad.http.servlet.enums.StatusCode;
import codesquad.register.model.EndPoint;
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
        var expectedEndpoint = EndPoint.of("/index.html",
            (headers, value) -> HttpResponseDeprecated.from(StatusCode.OK));
        // Act
        register.addEndpoint(HttpMethod.GET, expectedEndpoint);
        // Assert
        assertThat(register.getEndpoint(HttpMethod.GET, "/index.html")).isEqualTo(expectedEndpoint);
    }

    @Test
    @DisplayName("등록된 Endpoint를 반환한다.")
    void testGetEndpointSuccess() {
        // Arrange
        var expectedEndpoint = EndPoint.of("/index.html",
            (headers, value) -> HttpResponseDeprecated.from(StatusCode.OK));
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
            .isInstanceOf(HttpCommonException.class)
            .hasFieldOrPropertyWithValue("statusCode", StatusCode.NOT_FOUND)
            .hasMessage("존재하지 않는 Endpoint 입니다.");
    }
}