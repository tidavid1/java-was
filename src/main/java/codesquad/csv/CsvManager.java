package codesquad.csv;

import codesquad.server.properties.ApplicationProperties;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

// BEAN
public class CsvManager {

    private static final String COLUMN_DELIMITER = "\r\n";
    private static final String ROW_DELIMITER = ",";
    private static final String CSV_EXTENSION = ".csv";

    private final String folderPath;

    // id, title, body, something
    // 1, 안녕, "안녕 나는, ""누구""야!", 안녕
    private CsvManager(ApplicationProperties properties) {
        folderPath = properties.getCsvFolderPath();
        init();
    }

    public List<String[]> readCsv(String fileName) {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
            new FileInputStream(folderPath + "/" + fileName + CSV_EXTENSION)))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
                if (line.charAt(line.length() - 2) == '\r') {
                    data.add(parseRow(stringBuilder.toString()));
                    stringBuilder.setLength(0);
                }
            }
        } catch (IOException | NullPointerException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return data;
    }

    public void writeCsv(String fileName, Queue<String> queue) {
        try (FileWriter fileWriter = new FileWriter(fileName, true)) {
            while (queue.size() != 1 && !queue.isEmpty()) {
                String value = queue.poll();
                fileWriter.append(value).append(ROW_DELIMITER);
            }
            String value = queue.poll();
            fileWriter.append(value).append(COLUMN_DELIMITER);
            fileWriter.flush();
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }


    private void init() {
        File file = new File(folderPath);
        file.mkdir();
    }

    private String[] parseRow(String row) {
        List<String> values = new ArrayList<>();
        int idx = -1;
        while ((idx = row.indexOf(ROW_DELIMITER, idx + 1)) != -1) {
            String part = row.substring(0, idx).trim();
            if (validatePart(part)) {
                values.add(part);
                row = row.substring(idx + 1);
                idx = -1;
            }
        }
        values.add(row.trim());
        return values.toArray(new String[0]);
    }

    private boolean validatePart(String part) {
        return (part.length() - part.replace("\"", "").length()) % 2 == 0;
    }

}
