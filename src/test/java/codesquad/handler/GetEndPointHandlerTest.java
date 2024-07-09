package codesquad.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import codesquad.http.enums.HttpMethod;
import codesquad.register.EndPointRegister;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GetEndPointHandlerTest {

    @Test
    @DisplayName("지정한 GET Endpoint들을 저장한다.")
    void provideAll() {
        // Arrange
        EndPointRegister register = EndPointRegister.getInstance();
        StaticFileEndPointHandler.getInstance().provideAll();
        // Act
        GetEndPointHandler.getInstance().provideAll();
        // Assert
        assertAll(
            () -> assertThat(register.getEndpoint(HttpMethod.GET, "/")).isNotNull(),
            () -> assertThat(register.getEndpoint(HttpMethod.GET, "/registration")).isNotNull()
        );

    }
}