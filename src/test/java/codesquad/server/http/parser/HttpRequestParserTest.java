package codesquad.server.http.parser;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.server.http.exception.HttpCommonException;
import codesquad.server.http.servlet.HttpServletRequest;
import codesquad.server.http.servlet.SingleHttpRequest;
import codesquad.server.http.servlet.enums.StatusCode;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("HttpRequestParser는")
class HttpRequestParserTest {

    @Nested
    @DisplayName("Inpustream을 받아서")
    class WhenInputStreamGiven {

        @Test
        @DisplayName("HttpServletRequest를 반환한다.")
        void parse() throws IOException {
            // Arrange
            String inputStreamStr = """
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
            InputStream inputStream = new ByteArrayInputStream(inputStreamStr.getBytes());
            HttpRequestParser httpRequestParser = new HttpRequestParser();
            // Act
            SingleHttpRequest actualResult = (SingleHttpRequest) httpRequestParser.parse(
                inputStream).getRequest();
            // Assert
            assertThat(actualResult).isNotNull();
        }

        @Nested
        @DisplayName("예외 사항이 발생하면 Attribute에 예외를 포함한다")
        class addExceptionInAttribute {

            @Test
            @DisplayName("헤더가 올바르지 않은 경우")
            void parseWithInvalidHeader() throws IOException {
                // Arrange
                String inputStreamStr = """
                    GET /index.html HTTP/1.1
                    Host: localhost:8080
                    Sec-Fetch-Site: none
                    Upgrade-Insecure""";
                InputStream inputStream = new ByteArrayInputStream(inputStreamStr.getBytes());
                HttpRequestParser httpRequestParser = new HttpRequestParser();
                // Act
                HttpServletRequest actualResult = httpRequestParser.parse(inputStream);
                // Assert
                assertThat(actualResult.getAttribute("exception"))
                    .isInstanceOf(HttpCommonException.class)
                    .extracting("statusCode", "message")
                    .containsExactly(StatusCode.BAD_REQUEST, "헤더가 올바르지 않습니다.");
            }

            @Test
            @DisplayName("요청 라인이 존재하지 않는 경우")
            void parseWithNoRequestLine() throws IOException {
                // Arrange
                String inputStreamStr = """
                                        
                    Host: localhost:8080
                    Sec-Fetch-Site: none
                    Upgrade-Insecure""";
                InputStream inputStream = new ByteArrayInputStream(inputStreamStr.getBytes());
                HttpRequestParser httpRequestParser = new HttpRequestParser();
                // Act
                HttpServletRequest actualResult = httpRequestParser.parse(inputStream);
                // Assert
                assertThat(actualResult.getAttribute("exception"))
                    .isInstanceOf(HttpCommonException.class)
                    .extracting("statusCode", "message")
                    .containsExactly(StatusCode.BAD_REQUEST, "요청 라인이 올바르지 않습니다.");
            }

            @Test
            @DisplayName("요청 라인이 올바르지 않은 경우")
            void parseWithInvalidRequestLine() throws IOException {
                // Arrange
                String inputStreamStr = """
                    GET /index.html
                    Host: localhost:8080
                    Sec-Fetch-Site: none
                    Upgrade-Insecure""";
                InputStream inputStream = new ByteArrayInputStream(inputStreamStr.getBytes());
                HttpRequestParser httpRequestParser = new HttpRequestParser();
                // Act
                HttpServletRequest actualResult = httpRequestParser.parse(inputStream);
                // Assert
                assertThat(actualResult.getAttribute("exception"))
                    .isInstanceOf(HttpCommonException.class)
                    .extracting("statusCode", "message")
                    .containsExactly(StatusCode.BAD_REQUEST, "요청 라인이 올바르지 않습니다.");
            }

            @Test
            @DisplayName("요청 바디가 없는 경우")
            void parseWithNoBody() throws IOException {
                // Arrange
                String inputStreamStr = """
                    GET /index.html HTTP/1.1
                    Host: localhost:8080
                    Sec-Fetch-Site: none
                    Content-Length: 10\r
                    \r
                    """;
                InputStream inputStream = new ByteArrayInputStream(inputStreamStr.getBytes());
                HttpRequestParser httpRequestParser = new HttpRequestParser();
                // Act
                HttpServletRequest actualResult = httpRequestParser.parse(inputStream);
                // Assert
                assertThat(actualResult.getAttribute("exception"))
                    .isInstanceOf(HttpCommonException.class)
                    .extracting("statusCode", "message")
                    .containsExactly(StatusCode.BAD_REQUEST, "요청 바디가 없습니다.");
            }
        }
    }
}