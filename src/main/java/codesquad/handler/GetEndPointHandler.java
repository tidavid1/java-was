package codesquad.handler;

import codesquad.http.enums.HttpMethod;
import codesquad.register.EndPointRegister;
import codesquad.register.model.EndPoint;

public class GetEndPointHandler implements EndPointHandler {

    private static final GetEndPointHandler INSTANCE = new GetEndPointHandler();

    private final EndPointRegister endPointRegister;

    private GetEndPointHandler() {
        this.endPointRegister = EndPointRegister.getInstance();
    }

    public static GetEndPointHandler getInstance() {
        return INSTANCE;
    }

    @Override
    public void provideAll() {
        home();
        registration();
        login();
    }

    void home() {
        EndPoint<?> staticEndPoint = endPointRegister.getEndpoint(HttpMethod.GET, "/index.html");
        endPointRegister.addEndpoint(HttpMethod.GET,
            EndPoint.of("/", staticEndPoint.getFunction()));
    }

    void registration() {
        EndPoint<?> staticEndPoint = endPointRegister.getEndpoint(HttpMethod.GET,
            "/registration/index.html");
        endPointRegister.addEndpoint(HttpMethod.GET,
            EndPoint.of("/registration", staticEndPoint.getFunction()));
    }

    void login() {
        EndPoint<?> staticEndPoint = endPointRegister.getEndpoint(HttpMethod.GET,
            "/login/index.html");
        endPointRegister.addEndpoint(HttpMethod.GET,
            EndPoint.of("/login", staticEndPoint.getFunction()));
    }
}
