package codesquad.register;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import codesquad.http.enums.StatusCode;
import java.util.function.Function;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EndPointTest {

    @Test
    @DisplayName("Endpoint를 생성한다.")
    void testCreate() {
        // Arrange
        String expectedPath = "/index.html";
        Function<String, byte[]> expectedFunction = query -> new byte[0];
        // Act
        EndPoint actualResult = new EndPoint(expectedPath, expectedFunction);
        // Assert
        assertThat(actualResult)
            .extracting("path", "function", "contentType", "statusCode", "redirectUri")
            .containsExactly(expectedPath, expectedFunction, "text/html", StatusCode.OK, null);
    }

    @Test
    @DisplayName("Content-Type을 지정한 Endpoint를 생성한다.")
    void testCreateWithContentType() {
        // Arrange
        String expectedPath = "/index.html";
        Function<String, byte[]> expectedFunction = query -> new byte[0];
        String expectedContentType = "text/html";
        // Act
        EndPoint actualResult = new EndPoint(expectedPath, expectedFunction, expectedContentType);
        // Assert
        assertThat(actualResult)
            .extracting("path", "function", "contentType", "statusCode", "redirectUri")
            .containsExactly(expectedPath, expectedFunction, expectedContentType, StatusCode.OK,
                null);
    }

    @Test
    @DisplayName("Endpoint에 RedirectUri를 지정한다")
    void setRedirectUri() {
        // Arrange
        String expectedPath = "/index.html";
        Function<String, byte[]> expectedFunction = query -> new byte[0];
        String expectedRedirectUri = "/";
        EndPoint expectedEndPoint = new EndPoint(expectedPath, expectedFunction, null,
            StatusCode.FOUND);
        // Act
        expectedEndPoint.setRedirectUri(expectedRedirectUri);
        // Assert
        assertThat(expectedEndPoint.getRedirectUri()).isEqualTo(expectedRedirectUri);
    }

    @Test
    @DisplayName("Endpoint의 Supplier를 실행한다.")
    void testGet() {
        // Arrange
        byte[] expectedBytes = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Function<String, byte[]> function = query -> expectedBytes;
        EndPoint endPoint = new EndPoint("/index.html", function);
        // Act
        byte[] actualBytes = endPoint.getFunction().apply(null);
        // Assert
        assertArrayEquals(expectedBytes, actualBytes);
    }

}