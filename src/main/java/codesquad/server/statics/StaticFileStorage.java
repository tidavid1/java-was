package codesquad.server.statics;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class StaticFileStorage {

    private final Map<String, byte[]> staticFiles;

    private StaticFileStorage() {
        staticFiles = new HashMap<>();
    }

    public void putFileBytes(String path, byte[] bytes) {
        staticFiles.put(path, bytes);
    }

    public Set<Entry<String, byte[]>> getAllData() {
        return staticFiles.entrySet();
    }


}
