package codesquad.server.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FileByteReader {

    private FileByteReader() {

    }

    public static byte[] readAllBytes(File file) {
        try (InputStream fileInputStream = file.toURI().toURL().openStream()) {
            return fileInputStream.readAllBytes();
        } catch (IOException e) {
            throw new IllegalArgumentException("파일을 읽을 수 없습니다.");
        }
    }

    public static byte[] readAllBytes(InputStream inputStream) {
        try {
            return inputStream.readAllBytes();
        } catch (IOException e) {
            throw new IllegalArgumentException("파일을 읽을 수 없습니다.");
        }
    }

}
