package codesquad.server.endpoint.handler;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.server.endpoint.EndPointStorage;
import codesquad.server.http.servlet.enums.HttpMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostEndPointRegisterTest {

    @Test
    @DisplayName("지정한 POST EndPoint를 저장한다.")
    void provideAll() {
        // Arrange
        EndPointStorage register = EndPointStorage.getInstance();
        // Act
        PostEndPointRegister.getInstance().provideAll();
        // Assert
        assertThat(register.getEndpoint(HttpMethod.POST, "/create")).isNotNull();
    }

}