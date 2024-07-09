package codesquad.handler;

import codesquad.http.enums.HttpMethod;
import codesquad.reader.FileByteReader;
import codesquad.register.EndPointRegister;
import codesquad.register.model.EndPoint;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaticFileEndPointHandler implements EndPointHandler {

    private static final Logger log = LoggerFactory.getLogger(StaticFileEndPointHandler.class);
    private static final StaticFileEndPointHandler INSTANCE = new StaticFileEndPointHandler();
    private static final String STATIC_PATH = "static";

    private final EndPointRegister endpointRegister;

    private StaticFileEndPointHandler() {
        this.endpointRegister = EndPointRegister.getInstance();
    }

    public static StaticFileEndPointHandler getInstance() {
        return INSTANCE;
    }

    @Override
    public void provideAll() {
        boolean isJar = getClass().getClassLoader().getResource(STATIC_PATH).toString()
            .startsWith("jar");
        if (isJar) {
            provideJarFile();
        } else {
            provideIDEFile();
        }
    }

    private void provideJarFile() {
        try {
            String jarPath = getClass().getProtectionDomain().getCodeSource().getLocation().toURI()
                .getPath();
            JarFile jarFile = new JarFile(jarPath);
            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                if (entryName.startsWith(STATIC_PATH + "/") && !entry.isDirectory()) {
                    provideFileFromJar(jarFile, entry);
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void provideFileFromJar(JarFile jarFile, JarEntry entry) {
        try (InputStream inputStream = jarFile.getInputStream(entry)) {
            byte[] bytes = inputStream.readAllBytes();
            String path = "/" + entry.getName().substring(STATIC_PATH.length() + 1);
            endpointRegister.addEndpoint(HttpMethod.GET, new EndPoint(path, query -> bytes));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void provideIDEFile() {
        try {
            Enumeration<URL> resources = getClass().getClassLoader().getResources(STATIC_PATH);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                provideFile(new File(resource.getFile()));
            }
        } catch (IOException e) {
            log.error(e.getMessage());
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
        endpointRegister.addEndpoint(HttpMethod.GET, generateStaticEndPoint(path, file));
    }

    private EndPoint generateStaticEndPoint(String path, File file) {
        return new EndPoint(path, query -> new FileByteReader(file).readAllBytes());
    }
}
