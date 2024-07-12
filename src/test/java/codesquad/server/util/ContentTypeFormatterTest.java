package codesquad.server.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ContentTypeFormatterTest {

    @MethodSource
    @ParameterizedTest
    @DisplayName("파일 확장자에 따른 Content-Type을 반환한다.")
    void formatContentType(String fileExtension, String expectedResult) {
        // Act
        var actualResult = ContentTypeFormatter.formatContentType(fileExtension);
        // Assert
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> formatContentType() {
        return Stream.of(
            Arguments.of(".html", "text/html"),
            Arguments.of(".css", "text/css"),
            Arguments.of(".js", "text/javascript"),
            Arguments.of(".ico", "image/x-icon"),
            Arguments.of(".svg", "image/svg+xml"),
            Arguments.of(".png", "image/png"),
            Arguments.of(".jpg", "image/jpeg"),
            Arguments.of(".jpeg", "image/jpeg"),
            Arguments.of(".gif", "image/gif"),
            Arguments.of(".pdf", "application/pdf"),
            Arguments.of(".json", "application/json"),
            Arguments.of(".xml", "application/xml"),
            Arguments.of(".zip", "application/zip"),
            Arguments.of(".mp3", "audio/mpeg"),
            Arguments.of(".mp4", "video/mp4"),
            Arguments.of(".webm", "video/webm"),
            Arguments.of(".webp", "image/webp"),
            Arguments.of(".txt", "text/plain")
        );
    }

}