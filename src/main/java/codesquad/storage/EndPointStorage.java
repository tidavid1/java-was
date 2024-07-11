package codesquad.storage;

import codesquad.http.servlet.enums.HttpMethod;
import codesquad.storage.model.EndPoint;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Endpoint를 등록하고 관리하는 클래스 Singleton Pattern을 사용합니다.
 */
public class EndPointStorage {

    private static EndPointStorage instance;
    private final Map<HttpMethod, Set<EndPoint>> endpointMap = new ConcurrentHashMap<>();

    private EndPointStorage() {
    }

    public static EndPointStorage getInstance() {
        if (instance == null) {
            instance = new EndPointStorage();
        }
        return instance;
    }

    /**
     * Endpoint를 추가합니다.
     *
     * @param httpMethod HTTP Method
     * @param endpoint   Endpoint
     */
    public void addEndpoint(HttpMethod httpMethod, EndPoint endpoint) {
        Set<EndPoint> endpoints = endpointMap.getOrDefault(httpMethod, new HashSet<>());
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
    public Optional<EndPoint> getEndpoint(HttpMethod httpMethod, String uri) {
        Set<EndPoint> endpoints = endpointMap.getOrDefault(httpMethod, Set.of());
        return endpoints.stream()
            .filter(endpoint -> endpoint.getPath().equals(uri))
            .findFirst();
    }

}