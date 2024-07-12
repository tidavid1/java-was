package codesquad.server.endpoint.handler;

import java.util.List;

public interface EndPointRegister {

    static void handleAllHandler() {
        List<EndPointRegister> handlers = List.of(
            StaticFileEndPointRegister.getInstance(),
            GetEndPointRegister.getInstance(),
            PostEndPointRegister.getInstance());
        handlers.forEach(EndPointRegister::provideAll);
    }

    void provideAll();

}