package core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StorageFolders {
    private static List<File> folders = new ArrayList<>();

    public static List<File> getFolders(File parentDirectory, String excludeDirs, String sourceLang) {
        File[] dirList = parentDirectory.listFiles();
        String[] splitExcludeList = excludeDirs.split("\\|");

        if (parentDirectory == null) { return null; }

        if (parentDirectory.exists()) {
            for (File dir : dirList) {
                boolean match = false;
                for (String exDir : splitExcludeList) {// Compare folders with the names from exclude list
                    if (dir.getName().equals(exDir)) {
                        match = true;
                        break;
                    }
                }
                if (dir.isFile() || match) {
                    continue;
                }// Skipping

                if (dir.getName().equals(sourceLang)) {
                    folders.add(new File(dir.getAbsolutePath()));
                }

                if (dir.isDirectory()) {
                    getFolders(dir, excludeDirs, sourceLang);
                }
            }
        } else {
            System.out.println("Project dir is incorrect");
        }
        return folders;
    }

    public static void createFolder(String pathToSource, String folderName) {
        File folder = new File(pathToSource + folderName);

        if (!pathToSource.equals("")) {
            if(!StoragePath.checkFAvailability(pathToSource, folderName)) {
                folder.mkdir();
                //System.out.println("Folder created");
            } else {
                //System.out.println("Folder already exist");
            }
        } else {
            System.out.println("Path to source folder is empty");
        }
    }
}
