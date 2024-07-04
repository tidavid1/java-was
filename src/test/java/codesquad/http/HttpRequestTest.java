package codesquad.http;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.entry;
import static org.junit.jupiter.api.Assertions.assertAll;

import codesquad.exception.BadRequestException;
import codesquad.http.enums.HttpMethod;
import codesquad.http.enums.HttpVersion;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpRequestTest {

    private static final String INPUT_STREAM_STR = """
        GET /index.html HTTP/1.1
        Host: localhost:8080
        Sec-Fetch-Site: none
        Connection: keep-alive
        Upgrade-Insecure-Requests: 1
        Content-Length: 6
        Sec-Fetch-Mode: navigate
        Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
        User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.5 Safari/605.1.15
        Accept-Language: ko-KR,ko;q=0.9
        Sec-Fetch-Dest: document
        Accept-Encoding: gzip, deflate
                
        hello?
        """;

    private InputStream inputStream;

    @BeforeEach
    void init() {
        inputStream = new ByteArrayInputStream(INPUT_STREAM_STR.getBytes());
    }

    @Test
    @DisplayName("inputStream으로부터 HttpRequest 객체를 생성한다.")
    void create() throws IOException {
        // Act
        var actualResult = new HttpRequest(inputStream);

        // Assert
        assertAll(
            () -> assertThat(actualResult).isNotNull(),
            () -> assertThat(actualResult).isInstanceOf(HttpRequest.class),
            () -> assertThat(actualResult.getHttpMethod()).isEqualTo(HttpMethod.GET),
            () -> assertThat(actualResult.getRequestUri()).isEqualTo(new URI("/index.html")),
            () -> assertThat(actualResult.getHttpVersion()).isEqualTo(HttpVersion.HTTP_1_1),
            () -> assertThat(actualResult.getHeaders())
                .contains(
                    entry("Host", "localhost:8080"),
                    entry("Sec-Fetch-Site", "none"),
                    entry("Connection", "keep-alive"),
                    entry("Upgrade-Insecure-Requests", "1"),
                    entry("Sec-Fetch-Mode", "navigate"),
                    entry("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"),
                    entry("User-Agent",
                        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.5 Safari/605.1.15"),
                    entry("Accept-Language", "ko-KR,ko;q=0.9"),
                    entry("Sec-Fetch-Dest", "document"),
                    entry("Accept-Encoding", "gzip, deflate")),
            () -> assertThat(actualResult.getBody()).isEqualTo("hello?"),
            () -> assertThat(actualResult.getRequestQuery()).isNull()
        );
    }

    @Test
    @DisplayName("inputStream에서 요청 라인이 존재하지 않을 시 예외를 던진다.")
    void createWhenRequestLineNotFound() {
        // Arrange
        var expectedInputStream = new ByteArrayInputStream("".getBytes());

        // Act & Assert
        assertThatThrownBy(() -> new HttpRequest(expectedInputStream))
            .isInstanceOf(BadRequestException.class)
            .hasMessage("요청 라인이 없습니다.");
    }

    @Test
    @DisplayName("inputStream에서 요청 라인이 잘못된 형식일 시 예외를 던진다.")
    void createWhenRequestLineIsInvalid() {
        // Arrange
        var expectedInputStream = new ByteArrayInputStream("GET /index.html".getBytes());

        // Act & Assert
        assertThatThrownBy(() -> new HttpRequest(expectedInputStream))
            .isInstanceOf(BadRequestException.class)
            .hasMessage("요청 라인이 올바르지 않습니다.");
    }

    @Test
    @DisplayName("Content-Length와 Body의 길이가 일치하지 않을 시 예외를 던진다.")
    void createWhenContentLengthAndBodyLengthMismatch() {
        // Arrange
        var expectedInputStream = new ByteArrayInputStream("""
            GET /index.html HTTP/1.1
            Host: localhost:8080
            Content-Length: 10
                        
            hello?
            """.getBytes());

        // Act & Assert
        assertThatThrownBy(() -> new HttpRequest(expectedInputStream))
            .isInstanceOf(BadRequestException.class)
            .hasMessage("Content-Length와 Body의 길이가 일치하지 않습니다.");
    }

}