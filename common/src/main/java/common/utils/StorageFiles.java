package common.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StorageFiles {
    private static List<File> files = new ArrayList<>();

    public static List<File> getFiles(File checkDir) {
        File[] givenDir = checkDir.listFiles();

        if (checkDir == null) { return null; }

        if (checkDir.exists()) {
            for (File dir : givenDir) {
                if (dir.isFile() && dir.getName().endsWith("php")) {
                    System.out.println("File: " + dir.getPath());
                    files.add(new File(dir.getAbsolutePath()));
                }
                if (dir.isDirectory()) {
                    getFiles(dir);
                }
            }
        }
        return files;
    }

    public static void getData(File file) throws IOException {
        BufferedReader buf = new BufferedReader(new FileReader(file.getAbsolutePath()));
        String line;
        while ((line = buf.readLine()) != null) {
            System.out.println(line);
        }
    }

    public static Boolean createFile(String pathToDir, String filename, String fileExtension, String fileEncoding) throws IOException {
        File file = new File(pathToDir + filename + fileExtension);
        if (!pathToDir.equals("")) {
            if(!StoragePath.checkFAvailability(pathToDir, filename + fileExtension)) {
                PrintWriter mkFile = new PrintWriter(file.getPath(), fileEncoding);
                //System.out.println("File has been created");
            } else {
                //System.out.println("File " + filename + fileExtension + " already exist");
                return false;
            }
        }
        return true;
    }

    public static void writeToFile(String pathToDir, String filename, String value) {
        File file = new File(pathToDir + filename);
        writingToFile(file, value);
    }

    public static void writeToFile(String pathToDir, String value) {
        File file = new File(pathToDir);
        writingToFile(file, value);
    }

    private static void writingToFile(File file, String value) {
        try (FileWriter writer = new FileWriter(file.getPath(), true)) {
            writer.write("\n" + value);
            writer.flush();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
