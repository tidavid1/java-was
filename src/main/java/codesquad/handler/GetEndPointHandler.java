package codesquad.handler;

import codesquad.exception.BadRequestException;
import codesquad.http.enums.HttpMethod;
import codesquad.model.User;
import codesquad.register.EndPoint;
import codesquad.register.EndPointRegister;
import codesquad.register.UserRegister;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

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
        create();
    }

    public void home() {
        EndPoint staticEndPoint = endPointRegister.getEndpoint(HttpMethod.GET,
            "/index.html");
        endPointRegister.addEndpoint(HttpMethod.GET,
            new EndPoint("/", staticEndPoint.getFunction(),
                staticEndPoint.getContentType()));
    }

    public void registration() {
        EndPoint staticEndPoint = endPointRegister.getEndpoint(HttpMethod.GET,
            "/registration/index.html");
        endPointRegister.addEndpoint(HttpMethod.GET,
            new EndPoint("/registration", staticEndPoint.getFunction(),
                staticEndPoint.getContentType()));
    }

    public void create() {
        EndPoint staticEndPoint = endPointRegister.getEndpoint(HttpMethod.GET,
            "/index.html");
        Function<String, byte[]> function = query -> {
            Map<String, String> queryMap = new HashMap<>();
            String[] values = query.split("&");
            for (String value : values) {
                String[] split = value.split("=");
                if (split.length != 2) {
                    throw new BadRequestException("요청 값을 찾을 수 없습니다: " + split[0]);
                }
                queryMap.put(split[0], split[1]);
            }
            UserRegister.getInstance().save(User.from(queryMap));
            return staticEndPoint.getFunction().apply(query);
        };
        endPointRegister.addEndpoint(HttpMethod.GET,
            new EndPoint("/create", function, staticEndPoint.getContentType()));
    }

}
