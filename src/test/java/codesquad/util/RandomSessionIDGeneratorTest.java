package codesquad.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RandomSessionIDGeneratorTest {

    @Test
    @DisplayName("세션 아이디 생성은 중복되어서는 안된다.")
    void generate() {
        // Arrange
        Set<String> results = new HashSet<>();
        // Act
        for (int i = 0; i < 100; i++) {
            results.add(RandomSessionIDGenerator.generate());
        }
        // Assert
        assertThat(results).size().isEqualTo(100);
    }

}