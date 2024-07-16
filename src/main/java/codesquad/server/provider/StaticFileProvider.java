package codesquad.server.provider;

import codesquad.server.storage.StaticFileStorage;
import codesquad.server.util.FileByteReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaticFileProvider {

    private static final Logger log = LoggerFactory.getLogger(StaticFileProvider.class);
    private static final String STATIC_PATH = "static";
    private final StaticFileStorage staticFileStorage;

    private StaticFileProvider(StaticFileStorage staticFileStorage) {
        this.staticFileStorage = staticFileStorage;
    }

    public void init() {
        try {
            if (Objects.requireNonNull(
                    StaticFileProvider.class.getClassLoader().getResource(STATIC_PATH)).toString()
                .startsWith("jar")) {
                provideJarFile();
            } else {
                provideIDEFile();
            }
        } catch (Exception e) {
            log.error("Failed to provide static files: {}", e.getMessage());
        }

    }

    private void provideJarFile() throws IOException, URISyntaxException {
        String jarPath = StaticFileProvider.class.getProtectionDomain().getCodeSource()
            .getLocation().toURI()
            .getPath();
        try (JarFile jarFile = new JarFile(jarPath)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                if (entryName.startsWith(STATIC_PATH + "/") && !entry.isDirectory()) {
                    provideFile(jarFile, entry);
                }
            }
        }
    }

    private void provideIDEFile() throws IOException {
        Enumeration<URL> resources = StaticFileProvider.class.getClassLoader()
            .getResources(STATIC_PATH);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            provideFile(new File(resource.getFile()));
        }
    }

    private void provideFile(File file) {
        if (file.isDirectory() && file.exists()) {
            for (File subFile : Objects.requireNonNull(file.listFiles())) {
                provideFile(subFile);
            }
            return;
        }
        String path = file.getPath().split(STATIC_PATH)[1];
        staticFileStorage.putFileBytes(path, FileByteReader.readAllBytes(file));
    }

    private void provideFile(JarFile jarFile, JarEntry jarEntry) throws IOException {
        try (InputStream is = jarFile.getInputStream(jarEntry)) {
            byte[] bytes = FileByteReader.readAllBytes(is);
            String path = "/" + jarEntry.getName().substring(STATIC_PATH.length() + 1);
            staticFileStorage.putFileBytes(path, bytes);
        }
    }

}
