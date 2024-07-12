package codesquad.server.endpoint;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.server.http.servlet.enums.HttpMethod;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EndPointStorageTest {

    private EndPointStorage storage;

    @BeforeEach
    void setUp() {
        storage = EndPointStorage.getInstance();
    }

    @Test
    @DisplayName("Endpoint를 저장한다.")
    void testAddEndpoint() {
        // Arrange
        String expectedPath = "/";
        EndPoint expectedEndpoint = EndPoint.of(expectedPath,
            (httpServletRequest, httpServletResponse) -> {
            });
        // Act
        storage.addEndpoint(HttpMethod.GET, expectedEndpoint);
        // Assert
        assertThat(storage.getEndpoint(HttpMethod.GET, "/")).isPresent().get()
            .isEqualTo(expectedEndpoint);
    }

    @Test
    @DisplayName("등록된 Endpoint를 반환한다.")
    void testGetEndpointSuccess() {
        // Arrange
        String expectedPath = "/";
        EndPoint expectedEndpoint = EndPoint.of(expectedPath,
            (httpServletRequest, httpServletResponse) -> {
            });
        storage.addEndpoint(HttpMethod.GET, expectedEndpoint);
        // Act
        Optional<EndPoint> actualEndpoint = storage.getEndpoint(HttpMethod.GET, "/");
        // Assert
        assertThat(actualEndpoint).isPresent().get().isEqualTo(expectedEndpoint);
    }

}