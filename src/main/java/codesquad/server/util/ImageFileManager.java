package codesquad.server.util;

import codesquad.server.endpoint.EndPoint;
import codesquad.server.endpoint.EndPointStorage;
import codesquad.server.http.servlet.enums.HttpMethod;
import codesquad.server.http.servlet.enums.StatusCode;
import codesquad.server.properties.ApplicationProperties;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class ImageFileManager {

    private final String absoluteImagePath;
    private final EndPointStorage endPointStorage;

    public ImageFileManager(ApplicationProperties applicationProperties,
        EndPointStorage endPointStorage) {
        absoluteImagePath = applicationProperties.getImageFolderPath();
        this.endPointStorage = endPointStorage;
        init();
    }

    private void init() {
        File file = new File(absoluteImagePath);
        file.mkdir();
        File imageFile = new File(absoluteImagePath + "/default.webp");
        try (InputStream inputStream = getClass().getResourceAsStream("/static/img/default.webp")) {
            if (!imageFile.exists()) {
                saveImage("default.webp", Objects.requireNonNull(inputStream).readAllBytes());
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("File Not Found!");
        }
        for (File subFile : file.listFiles()) {
            String path = subFile.getAbsolutePath().replace(absoluteImagePath, "/img");
            endPointStorage.addEndpoint(HttpMethod.GET, EndPoint.of(path, ((request, response) -> {
                response.setStatus(StatusCode.OK);
                response.setContentType("image/" + path.substring(path.lastIndexOf(".") + 1));
                response.setBody(this.readImage(path.replace("/img/", "")));
            })));
        }
    }

    public String saveImage(String filename, byte[] imageBytes) {
        String fullName = absoluteImagePath + "/" + filename;
        try (FileOutputStream fileOutputStream = new FileOutputStream(fullName)) {
            fileOutputStream.write(imageBytes);
            fileOutputStream.flush();
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return filename;
    }

    public byte[] readImage(String filename) {
        String fullName = absoluteImagePath + "/" + filename;
        try (FileInputStream fileInputStream = new FileInputStream(fullName)) {
            return fileInputStream.readAllBytes();
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

}
