package codesquad.handler;

import java.util.List;

public interface EndPointHandler {

    static void handleAllHandler() {
        List<EndPointHandler> handlers = List.of(StaticFileEndPointHandler.getInstance(),
            GetEndPointHandler.getInstance());
        handlers.forEach(EndPointHandler::provideAll);
    }
    
    void provideAll();

}
