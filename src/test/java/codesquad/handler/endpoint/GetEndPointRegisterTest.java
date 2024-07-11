package codesquad.handler.endpoint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import codesquad.handler.StaticFileProvider;
import codesquad.http.servlet.enums.HttpMethod;
import codesquad.storage.EndPointStorage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GetEndPointRegisterTest {

    @Test
    @DisplayName("지정한 GET Endpoint들을 저장한다.")
    void provideAll() {
        // Arrange
        EndPointStorage register = EndPointStorage.getInstance();
        StaticFileProvider.init();
        StaticFileEndPointRegister.getInstance().provideAll();
        // Act
        GetEndPointRegister.getInstance().provideAll();
        // Assert
        assertAll(
            () -> assertThat(register.getEndpoint(HttpMethod.GET, "/")).isNotNull(),
            () -> assertThat(register.getEndpoint(HttpMethod.GET, "/registration")).isNotNull()
        );

    }
}