package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class StorageLines {
    private int number;
    private String value;

    public StorageLines(int number, String value) {
        this.number = number;
        this.value = value;
    }

    public StorageLines(String value) {
        this.value = value;
    }

    public void setNumber(int number) { this.number = number; }

    public void setValue(String value) { this.value = value; }

    public int getNumber() { return number; }

    public String getValue() { return value; }

    public static List<StorageLines> getLines(File file, String regex) throws IOException {
        List<StorageLines> matchedLines = new ArrayList<>();
        FileInputStream fis = new FileInputStream(file);
        byte[] content = new byte[fis.available()];
        fis.read(content);
        fis.close();

        String[] lines = new String(content, "UTF-8").split("\n");

        Pattern pattern = Pattern.compile(regex);
        for (int i = 0; i < lines.length; i++) {
            if (pattern.matcher(lines[i]).find()) {
                matchedLines.add(new StorageLines(i, lines[i]));
            }
        }
        return matchedLines;
    }

    public static List<StorageLines> getParamValue(String filePath, String param) throws IOException {
        File file = new File(filePath);
        List<StorageLines> list = new ArrayList<StorageLines>();
        list = StorageLines.getLines(file, param);
        return list;
    }

    public static void updateParamValue(File file, int lineNumber, String param, String value) throws IOException {
        Path path = Paths.get(file.getCanonicalPath());
        Charset charset = StandardCharsets.UTF_8;
        String newLine = param + ": '" + value + "';";
        FileInputStream fis = new FileInputStream(file);
        byte[] content = new byte[fis.available()];
        fis.read(content);
        fis.close();
        String[] lines = new String(content, "UTF-8").split("\n");// Collect all data from the file

        List<String> collectLines = new ArrayList<>();// Modified data
        for (int i = 0; i < lines.length; i++) {
            if (i == lineNumber) {
                lines[i] = newLine;
            }
            collectLines.add(lines[i]);
        }
        Files.write(path, collectLines, charset);
    }
}
