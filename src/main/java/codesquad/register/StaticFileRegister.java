package codesquad.register;

import codesquad.exception.NotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

public class StaticFileRegister {

    private static final StaticFileRegister INSTANCE = new StaticFileRegister();

    private final Map<String, byte[]> staticFiles = new HashMap<>();

    private StaticFileRegister() {
    }

    public static StaticFileRegister getInstance() {
        return INSTANCE;
    }

    public void putFileBytes(String path, byte[] bytes) {
        staticFiles.put(path, bytes);
    }

    public byte[] getFileBytes(String path) {
        return Optional.of(staticFiles.get(path))
            .orElseThrow(() -> new NotFoundException("존재하지 않는 파일입니다."));
    }

    public Set<Entry<String, byte[]>> getAllData() {
        return staticFiles.entrySet();
    }


}
