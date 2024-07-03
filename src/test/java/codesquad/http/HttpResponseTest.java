package codesquad.http;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpResponseTest {

    @Test
    @DisplayName("HttpResponse 객체를 생성한다.")
    void create() {
        // Arrange
        var expectedRequestUri = "/index.html";
        // Act
        var actualResult = HttpResponse.from(expectedRequestUri);
        // Assert
        assertAll(
            () -> assertNotNull(actualResult),
            () -> assertThat(actualResult.generateResponse())
                .isNotEmpty()
        );

    }
}