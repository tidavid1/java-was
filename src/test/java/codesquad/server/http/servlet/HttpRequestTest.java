package codesquad.server.http.servlet;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.server.http.servlet.enums.HttpMethod;
import codesquad.server.http.servlet.enums.HttpVersion;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpRequestTest {

    @Test
    @DisplayName("HttpRequest 객체를 생성할 수 있다.")
    void create() {
        // Arrange
        String[] requestLineParts = {"GET", "/index.html", "HTTP/1.1"};
        Map<String, List<String>> headers = Map.of("Content-Length", List.of("0"));
        String body = "";
        // Act
        HttpRequest httpRequest = new HttpRequest(requestLineParts, headers, body);
        // Assert
        assertThat(httpRequest).isNotNull();
        assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(httpRequest.getUri().toString()).hasToString("/index.html");
        assertThat(httpRequest.getVersion()).isEqualTo(HttpVersion.HTTP_1_1);
        assertThat(httpRequest.getHeaders()).isEqualTo(headers);
        assertThat(httpRequest.getBody()).isEqualTo(body);
    }
}