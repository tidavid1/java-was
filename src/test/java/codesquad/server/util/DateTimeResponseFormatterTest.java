package codesquad.server.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DateTimeResponseFormatterTest {

    @Test
    @DisplayName("HttpResponse Header Date 포멧에 맞게 포맷팅한다.")
    void formatZonedDateTime() {
        // Arrange
        var expectedResult = "Fri, 01 Jan 2021 00:00:00 GMT";
        var zonedDateTime = ZonedDateTime.of(2021, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
        // Act
        var actualResult = DateTimeResponseFormatter.formatZonedDateTime(zonedDateTime);
        // Assert
        assertThat(actualResult).isEqualTo(expectedResult);
    }
}