package codesquad.server.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.HttpCookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("HttpCookieReader는")
class HttpCookieReaderTest {

    @Test
    @DisplayName("쿠키를 읽을 수 있다.")
    void read() {
        // Arrange
        HttpCookie cookie = new HttpCookie("JSESSIONID", "1234");
        cookie.setMaxAge(0);
        // Act
        String actualResult = HttpCookieReader.readCookie(cookie);
        // Assert
        assertThat(actualResult).isEqualTo("JSESSIONID=1234; Max-Age=0; Version=1");
    }
}