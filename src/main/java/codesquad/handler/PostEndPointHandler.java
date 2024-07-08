package codesquad.handler;

import codesquad.exception.BadRequestException;
import codesquad.http.enums.HttpMethod;
import codesquad.http.enums.StatusCode;
import codesquad.model.User;
import codesquad.register.EndPoint;
import codesquad.register.EndPointRegister;
import codesquad.register.UserRegister;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PostEndPointHandler implements EndPointHandler {

    private static final PostEndPointHandler INSTANCE = new PostEndPointHandler();

    private final EndPointRegister endPointRegister;

    public PostEndPointHandler() {
        this.endPointRegister = EndPointRegister.getInstance();
    }

    public static PostEndPointHandler getInstance() {
        return INSTANCE;
    }

    @Override
    public void provideAll() {
        create();
    }

    void create() {
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
            return new byte[0];
        };
        EndPoint endPoint = new EndPoint("/create", function, null, StatusCode.FOUND);
        endPoint.setRedirectUri("/index.html");
        endPointRegister.addEndpoint(HttpMethod.POST, endPoint);
    }
}
