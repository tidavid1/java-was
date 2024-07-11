package codesquad.server.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HexEncoderTest {

    @Test
    @DisplayName("byteToHex() 메서드는 byte 배열을 16진수 문자열로 변환한다.")
    void byteToHex() {
        // Arrange
        byte[] bytes = {0x01, 0x02, 0x03, 0x04, 0x05};
        String expectedResult = "0102030405";
        // Act
        String actualResult = HexEncoder.byteToHex(bytes);
        // Assert
        assertThat(actualResult).isEqualTo(expectedResult);
    }

}