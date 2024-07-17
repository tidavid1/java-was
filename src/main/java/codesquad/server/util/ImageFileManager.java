package codesquad.server.util;

import codesquad.server.properties.ApplicationProperties;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class ImageFileManager {

    private final String absoluteImagePath;

    public ImageFileManager(ApplicationProperties applicationProperties) {
        absoluteImagePath = applicationProperties.getImageFolderPath();
        init();
    }

    private void init() {
        File file = new File(absoluteImagePath);
        file.mkdir();
        file = new File(absoluteImagePath + "/default.webp");
        try (InputStream inputStream = getClass().getResourceAsStream("/static/img/default.webp")) {
            if (!file.exists()) {
                saveImage("default.webp", Objects.requireNonNull(inputStream).readAllBytes());
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("File Not Found!");
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
