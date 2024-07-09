package codesquad.handler;

import codesquad.http.enums.HttpMethod;
import codesquad.register.EndPoint;
import codesquad.register.EndPointRegister;

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
        EndPoint staticEndPoint = endPointRegister.getEndpoint(HttpMethod.GET,
            "/index.html");
        endPointRegister.addEndpoint(HttpMethod.GET,
            new EndPoint("/", staticEndPoint.getFunction(),
                staticEndPoint.getContentType()));
    }

    void registration() {
        EndPoint staticEndPoint = endPointRegister.getEndpoint(HttpMethod.GET,
            "/registration/index.html");
        endPointRegister.addEndpoint(HttpMethod.GET,
            new EndPoint("/registration", staticEndPoint.getFunction(),
                staticEndPoint.getContentType()));
    }

    void login() {
        EndPoint staticEndPoint = endPointRegister.getEndpoint(HttpMethod.GET,
            "/login/index.html");
        endPointRegister.addEndpoint(HttpMethod.GET,
            new EndPoint("/login", staticEndPoint.getFunction(),
                staticEndPoint.getContentType()));
    }
}
