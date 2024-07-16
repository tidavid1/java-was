package codesquad.server.storage;

import codesquad.server.http.exception.HttpCommonException;
import codesquad.server.http.servlet.enums.StatusCode;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class StaticFileStorage {

    @Deprecated(forRemoval = true)
    private static final StaticFileStorage INSTANCE = new StaticFileStorage();

    private final Map<String, byte[]> staticFiles;

    private StaticFileStorage() {
        staticFiles = new HashMap<>();
    }

    @Deprecated(forRemoval = true)
    public static StaticFileStorage getInstance() {
        return INSTANCE;
    }

    public void putFileBytes(String path, byte[] bytes) {
        staticFiles.put(path, bytes);
    }

    public byte[] getFileBytes(String path) {
        return Optional.of(staticFiles.get(path))
            .orElseThrow(() -> new HttpCommonException("존재하지 않는 파일입니다.", StatusCode.NOT_FOUND));
    }

    public Set<Entry<String, byte[]>> getAllData() {
        return staticFiles.entrySet()
            .stream().filter(entry -> !entry.getKey().contains(".html"))
            .collect(Collectors.toSet());
    }


}
