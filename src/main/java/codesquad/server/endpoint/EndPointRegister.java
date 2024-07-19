package codesquad.server.endpoint;

import codesquad.codestagram.endpoint.GetEndPointRegister;
import codesquad.codestagram.endpoint.PostEndPointRegister;
import codesquad.server.bean.BeanFactory;
import java.util.List;

public interface EndPointRegister {

    static void registerAll() {
        List<EndPointRegister> handlers = List.of(
            BeanFactory.getInstance().getBean(StaticFileEndPointRegister.class),
            BeanFactory.getInstance().getBean(GetEndPointRegister.class),
            BeanFactory.getInstance().getBean(PostEndPointRegister.class));
        handlers.forEach(EndPointRegister::provideAll);
    }

    void provideAll();

}