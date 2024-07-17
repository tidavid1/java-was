package codesquad.server.template;

import codesquad.server.http.exception.HttpCommonException;
import codesquad.server.http.servlet.enums.StatusCode;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TemplateFileStorage {

    private final Map<String, String> templateFiles;

    private TemplateFileStorage() {
        templateFiles = new HashMap<>();
    }

    public void saveFile(String path, String fileStr) {
        templateFiles.put(path, fileStr);
    }

    public String getFileStr(String path) {
        return Optional.of(templateFiles.get(path))
            .orElseThrow(() -> new HttpCommonException("존재하지 않는 파일입니다.", StatusCode.NOT_FOUND));
    }

}
