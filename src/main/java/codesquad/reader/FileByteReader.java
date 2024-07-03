package codesquad.reader;

import java.io.IOException;
import java.io.InputStream;

public class FileByteReader {

    private final InputStream fileInputStream;

    public FileByteReader(String filePath) {
        try {
            fileInputStream = getClass().getClassLoader().getResource("static" + filePath)
                .openStream();
        } catch (Exception e) {
            throw new IllegalArgumentException("파일을 찾을 수 없습니다.");
        }
    }

    public byte[] readAllBytes() {
        try {
            return fileInputStream.readAllBytes();
        } catch (IOException e) {
            throw new IllegalArgumentException("파일을 읽을 수 없습니다.");
        }
    }
}
