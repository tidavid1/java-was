package codesquad.register;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.function.Supplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EndPointTest {

    @Test
    @DisplayName("Endpoint를 생성한다.")
    void testCreate() {
        // Arrange
        String expectedPath = "/index.html";
        Supplier<byte[]> expectedSupplier = () -> new byte[0];
        // Act
        EndPoint actualResult = new EndPoint(expectedPath, expectedSupplier);
        // Assert
        assertThat(actualResult)
            .extracting("path", "supplier")
            .containsExactly(expectedPath, expectedSupplier);
    }

    @Test
    @DisplayName("Endpoint의 Supplier를 실행한다.")
    void testGet() {
        // Arrange
        byte[] expectedBytes = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Supplier<byte[]> supplier = () -> expectedBytes;
        EndPoint endPoint = new EndPoint("/index.html", supplier);
        // Act
        byte[] actualBytes = endPoint.get();
        // Assert
        assertArrayEquals(expectedBytes, actualBytes);
    }

}