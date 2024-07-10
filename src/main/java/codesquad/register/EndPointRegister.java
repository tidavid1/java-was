package codesquad.register;

import codesquad.exception.HttpCommonException;
import codesquad.http.enums.HttpMethod;
import codesquad.http.enums.StatusCode;
import codesquad.register.model.EndPoint;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Endpoint를 등록하고 관리하는 클래스 Singleton Pattern을 사용합니다.
 */
public class EndPointRegister {

    private static final Logger log = LoggerFactory.getLogger(EndPointRegister.class);
    private static final EndPointRegister INSTANCE = new EndPointRegister();
    private final Map<HttpMethod, Set<EndPoint<?>>> endpointMap = new ConcurrentHashMap<>();

    private EndPointRegister() {
    }

    public static EndPointRegister getInstance() {
        return INSTANCE;
    }

    /**
     * Endpoint를 추가합니다.
     *
     * @param httpMethod HTTP Method
     * @param endpoint   Endpoint
     */
    public void addEndpoint(HttpMethod httpMethod, EndPoint<?> endpoint) {
        log.debug("Add Endpoint: {}", endpoint.getPath());
        Set<EndPoint<?>> endpoints = endpointMap.getOrDefault(httpMethod, new HashSet<>());
        endpoints.add(endpoint);
        endpointMap.put(httpMethod, endpoints);
    }


    /**
     * HTTP Method와 URI에 해당하는 Endpoint를 반환합니다.
     *
     * @param httpMethod HTTP Method
     * @param uri        URI
     * @return Endpoint
     * @throws IllegalArgumentException Endpoint가 없을 경우
     */
    public EndPoint<?> getEndpoint(HttpMethod httpMethod, String uri) {
        Set<EndPoint<?>> endpoints = endpointMap.getOrDefault(httpMethod, Set.of());
        return endpoints.stream()
            .filter(endpoint -> endpoint.getPath().equals(uri))
            .findFirst()
            .orElseThrow(
                () -> new HttpCommonException("존재하지 않는 Endpoint 입니다.", StatusCode.NOT_FOUND));
    }

}
