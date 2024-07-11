package codesquad.server.endpoint.handler;

import codesquad.server.endpoint.EndPoint;
import codesquad.server.endpoint.EndPointStorage;
import codesquad.server.http.servlet.enums.HttpMethod;
import codesquad.server.http.servlet.enums.StatusCode;
import codesquad.server.util.ContentTypeFormatter;
import codesquad.storage.StaticFileStorage;
import java.util.Map.Entry;
import java.util.Set;

public class StaticFileEndPointRegister implements EndPointRegister {

    private static final StaticFileEndPointRegister INSTANCE = new StaticFileEndPointRegister();

    private final EndPointStorage endpointStorage;
    private final StaticFileStorage staticFileStorage;

    private StaticFileEndPointRegister() {
        this.endpointStorage = EndPointStorage.getInstance();
        this.staticFileStorage = StaticFileStorage.getInstance();
    }

    public static StaticFileEndPointRegister getInstance() {
        return INSTANCE;
    }

    @Override
    public void provideAll() {
        Set<Entry<String, byte[]>> staticFiles = staticFileStorage.getAllData();
        staticFiles.forEach(entry -> {
            String path = entry.getKey();
            byte[] bytes = entry.getValue();
            endpointStorage.addEndpoint(HttpMethod.GET,
                EndPoint.of(path, (httpServletRequest, httpServletResponse) -> {
                    httpServletResponse.setStatus(StatusCode.OK);
                    httpServletResponse.setHeader("Content-Type",
                        ContentTypeFormatter.formatContentType(path));
                    httpServletResponse.setBody(bytes);
                }));
        });
    }

}
