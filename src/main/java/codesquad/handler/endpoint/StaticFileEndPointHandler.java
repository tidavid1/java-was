package codesquad.handler.endpoint;

import codesquad.http.servlet.HttpResponse;
import codesquad.http.servlet.enums.HeaderKey;
import codesquad.http.servlet.enums.HttpMethod;
import codesquad.http.servlet.enums.StatusCode;
import codesquad.register.EndPointRegister;
import codesquad.register.StaticFileRegister;
import codesquad.register.model.EndPoint;
import codesquad.util.ContentTypeFormatter;
import java.util.Map.Entry;
import java.util.Set;

public class StaticFileEndPointHandler implements EndPointHandler {

    private static final StaticFileEndPointHandler INSTANCE = new StaticFileEndPointHandler();

    private final EndPointRegister endpointRegister;
    private final StaticFileRegister staticFileRegister;

    private StaticFileEndPointHandler() {
        this.endpointRegister = EndPointRegister.getInstance();
        this.staticFileRegister = StaticFileRegister.getInstance();
    }

    public static StaticFileEndPointHandler getInstance() {
        return INSTANCE;
    }

    @Override
    public void provideAll() {
        Set<Entry<String, byte[]>> staticFiles = staticFileRegister.getAllData();
        staticFiles.forEach(entry -> {
            String path = entry.getKey();
            if (path.contains(".html")) {
                return;
            }
            byte[] bytes = entry.getValue();
            endpointRegister.addEndpoint(HttpMethod.GET,
                EndPoint.of(path, (headers, query) -> {
                    HttpResponse httpResponse = HttpResponse.of(StatusCode.OK, bytes);
                    httpResponse.addHeader(HeaderKey.CONTENT_TYPE,
                        ContentTypeFormatter.formatContentType(path));
                    return httpResponse;
                }));
        });
    }

}
