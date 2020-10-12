package core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StoragePath {

    public static void printPaths(File file) throws IOException {
        System.out.println("Absolute Path: " + file.getAbsolutePath());
        System.out.println("Canonical Path: " + file.getCanonicalPath());
        System.out.println("Path: " + file.getPath());
    }

    public static Boolean checkFAvailability(String pathToSource, String searchedName) {
        Path path = Paths.get(pathToSource + searchedName);
        if (!Files.exists(path)) {
            return false;
        }
        return true;
    }

    public static Boolean checkFAvailability(File pathToSource) {
        Path path = Paths.get(pathToSource.getPath());
        if (!Files.exists(path)) {
            return false;
        }
        return true;
    }
}
