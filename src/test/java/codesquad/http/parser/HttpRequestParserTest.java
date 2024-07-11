package codesquad.http.parser;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.http.servlet.HttpServletRequest;
import codesquad.http.servlet.enums.HttpMethod;
import codesquad.http.servlet.enums.HttpVersion;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("HttpRequestParser는")
class HttpRequestParserTest {

    @Nested
    @DisplayName("Inpustream을 받아서")
    class WhenInputStreamGiven {

        private final String inputStreamStr = """
            GET /index.html HTTP/1.1
            Host: localhost:8080
            Sec-Fetch-Site: none
            Upgrade-Insecure-Requests: 1
            Content-Length: 0
            Sec-Fetch-Mode: navigate
            Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
            User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.5 Safari/605.1.15
            Accept-Language: ko-KR,ko;q=0.9
            Sec-Fetch-Dest: document
            Accept-Encoding: gzip, deflate
            \r
            """;

        @Test
        @DisplayName("HttpServletRequest를 반환한다.")
        void parse() throws IOException {
            // Arrange
            InputStream inputStream = new ByteArrayInputStream(inputStreamStr.getBytes());
            HttpRequestParser httpRequestParser = new HttpRequestParser();
            // Act
            HttpServletRequest actualResult = httpRequestParser.parse(inputStream);
            // Assert
            assertThat(actualResult).isNotNull();
            assertThat(actualResult.getRequest())
                .extracting("method", "uri", "version")
                .containsExactly(HttpMethod.GET, URI.create("/index.html"), HttpVersion.HTTP_1_1);
        }

    }


}