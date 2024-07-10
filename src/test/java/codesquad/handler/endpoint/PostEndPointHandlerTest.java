package codesquad.handler.endpoint;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.http.enums.HttpMethod;
import codesquad.register.EndPointRegister;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostEndPointHandlerTest {

    @Test
    @DisplayName("지정한 POST EndPoint를 저장한다.")
    void provideAll() {
        // Arrange
        EndPointRegister register = EndPointRegister.getInstance();
        // Act
        PostEndPointHandler.getInstance().provideAll();
        // Assert
        assertThat(register.getEndpoint(HttpMethod.POST, "/create")).isNotNull();
    }

}