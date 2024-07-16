package codesquad.server.bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanStorage {

    private static final BeanStorage INSTANCE = new BeanStorage();

    private final Map<Class<?>, Object> map;

    private BeanStorage() {
        this.map = new ConcurrentHashMap<>();
    }

    public static BeanStorage getInstance() {
        return INSTANCE;
    }

    public void addBean(Class<?> clazz, Object bean) {
        if (map.containsKey(clazz)) {
            throw new IllegalArgumentException("Bean Already Created");
        }
        map.put(clazz, bean);
    }

    public <T> T getBean(Class<T> clazz) {
        Object bean = map.get(clazz);
        if (bean == null) {
            return null;
        }
        return clazz.cast(bean);
    }
}
