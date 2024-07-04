package codesquad.handler;

import codesquad.http.enums.HttpMethod;
import codesquad.reader.FileByteReader;
import codesquad.register.EndPoint;
import codesquad.register.EndPointRegister;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaticFileEndPointHandler implements EndPointHandler {

    private static final StaticFileEndPointHandler INSTANCE = new StaticFileEndPointHandler();
    private static final Logger log = LoggerFactory.getLogger(StaticFileEndPointHandler.class);
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
        log.debug(path);
        endpointRegister.addEndpoint(HttpMethod.GET, generateStaticEndPoint(path, file));
    }

    private EndPoint generateStaticEndPoint(String path, File file) {
        return new EndPoint(path, query -> new FileByteReader(file).readAllBytes());
    }
}
