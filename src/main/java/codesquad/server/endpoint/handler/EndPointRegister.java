package codesquad.server.endpoint.handler;

import codesquad.server.bean.BeanFactory;
import codesquad.server.endpoint.StaticFileEndPointRegister;
import java.util.List;

public interface EndPointRegister {

    static void handleAllHandler() {
        List<EndPointRegister> handlers = List.of(
            BeanFactory.getInstance().getBean(StaticFileEndPointRegister.class),
            BeanFactory.getInstance().getBean(GetEndPointRegister.class),
            BeanFactory.getInstance().getBean(PostEndPointRegister.class));
        handlers.forEach(EndPointRegister::provideAll);
    }

    void provideAll();

}