package codesquad.server.endpoint;

import codesquad.server.endpoint.handler.EndPointRegister;
import codesquad.server.http.servlet.enums.HttpMethod;
import codesquad.server.http.servlet.enums.StatusCode;
import codesquad.server.storage.StaticFileStorage;
import codesquad.server.util.ContentTypeFormatter;
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
