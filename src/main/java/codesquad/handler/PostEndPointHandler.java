package codesquad.handler;

import codesquad.exception.BadRequestException;
import codesquad.http.HttpResponse;
import codesquad.http.enums.HttpMethod;
import codesquad.http.enums.StatusCode;
import codesquad.model.User;
import codesquad.register.EndPointRegister;
import codesquad.register.UserRegister;
import codesquad.register.model.EndPoint;
import java.util.HashMap;
import java.util.Map;

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
        EndPoint<String> endPoint = EndPoint.of(
            "/create", body -> {
                Map<String, String> map = parseBody(body);
                try {
                    UserRegister.getInstance().save(User.from(map));
                } catch (IllegalArgumentException e) {
                    throw new BadRequestException(e.getMessage());
                }
                HttpResponse response = HttpResponse.from(StatusCode.FOUND);
                response.addHeader("Location", "/index.html");
                return response;
            }
        );
        endPointRegister.addEndpoint(HttpMethod.POST, endPoint);
    }

    private Map<String, String> parseBody(String body) {
        Map<String, String> queryMap = new HashMap<>();
        String[] values = body.split("&");
        for (String value : values) {
            String[] split = value.split("=");
            if (split.length != 2) {
                throw new BadRequestException("요청 값을 찾을 수 없습니다: " + split[0]);
            }
            queryMap.put(split[0], split[1]);
        }
        return queryMap;
    }
}
