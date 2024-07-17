package codesquad.server.endpoint;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.server.http.servlet.enums.HttpMethod;
import java.lang.reflect.Constructor;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("EndPointStorage는")
class EndPointStorageTest {

    private EndPointStorage endPointStorage;

    @BeforeEach
    void init() {
        try {
            for (Constructor<?> constructor : EndPointStorage.class.getDeclaredConstructors()) {
                constructor.setAccessible(true);
                endPointStorage = (EndPointStorage) constructor.newInstance();
            }
        } catch (Exception ignore) {

        }
    }

    @Test
    @DisplayName("EndPoint를 추가할 수 있다.")
    void testAddEndPoint() {
        // Arrange
        String expectedPath = "/test";
        EndPoint expectedEndPoint = EndPoint.of(expectedPath, (request, response) -> {
        });
        HttpMethod expectedHttpMethod = HttpMethod.GET;
        // Act
        endPointStorage.addEndpoint(expectedHttpMethod, expectedEndPoint);
        // Assert
        assertThat(endPointStorage.getEndpoint(expectedHttpMethod, expectedPath)).isPresent();
    }

    @Test
    @DisplayName("EndPoint를 조회할 수 있다.")
    void testGetEndPoint() {
        // Arrange
        String expectedPath = "/test";
        EndPoint expectedEndPoint = EndPoint.of(expectedPath, (request, response) -> {
        });
        HttpMethod expectedHttpMethod = HttpMethod.GET;
        endPointStorage.addEndpoint(expectedHttpMethod, expectedEndPoint);
        // Act
        Optional<EndPoint> actualResult = endPointStorage.getEndpoint(expectedHttpMethod,
            expectedPath);
        // Assert
        assertThat(actualResult).isPresent().get().isEqualTo(expectedEndPoint);
    }

    @Test
    @DisplayName("EndPoint가 없을 경우 빈 Optional을 반환한다.")
    void testGetEndPointWhenEmpty() {
        // Arrange
        String expectedPath = "/test";
        HttpMethod expectedHttpMethod = HttpMethod.GET;
        // Act
        Optional<EndPoint> actualResult = endPointStorage.getEndpoint(expectedHttpMethod,
            expectedPath);
        // Assert
        assertThat(actualResult).isEmpty();
    }
}