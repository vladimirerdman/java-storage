package core;

import common.utils.Config;
import common.utils.StorageFiles;
import common.utils.StorageLines;
import common.utils.StoragePath;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainSettingsController implements Initializable {
    private final String configDir = "./netty-client/src/main/java/config/";
    private final String settingsFile = "settings.txt";
    private final File settingsFilePath = new File(configDir + settingsFile);
    private String LSPathBefore;

    @FXML
    private TextField localStoragePath;

    public void setLocalStoragePath(String value) { localStoragePath.setText(value); }

    public String getLocalStoragePath() { return localStoragePath.getText(); }

    public void settingsSave(ActionEvent actionEvent) throws IOException {
        String path = "";
        if (LSPathBefore.equals("") && !getLocalStoragePath().equals("")) {// Add new line to settings file
            System.out.println("Add: " + getLocalStoragePath());
            LSPathBefore = getLocalStoragePath();
            path = "Local Storage: '" + getLocalStoragePath() + "';";
            StorageFiles.writeToFile(configDir + settingsFile, path);
        } else if (!LSPathBefore.equals(getLocalStoragePath()) && !getLocalStoragePath().equals("")) {// Update line in settings file
            if (StoragePath.checkFAvailability(settingsFilePath)) {
                updateSettings(settingsFilePath, "Local Storage", getLocalStoragePath());
            } else {
                System.out.println("File not found");
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        localStoragePath.setPromptText("Share local folder");

        settings();
        lastPathOfLS();
    }

    /**
     * Checking that settings file available and getting value.
     * If file not available then we will create it.
     */
    private void settings() {
        if (StoragePath.checkFAvailability(settingsFilePath)) {
            try {
                getSettings();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {// If settings file not found -> create file
            try {
                PrintWriter mkFile = new PrintWriter(settingsFilePath.getPath(), "UTF-8");
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Searching for values from settings file
     *
     * @throws IOException
     */
    private void getSettings() throws IOException {
        getLSPathFromFile(settingsFilePath);// Get default path for Local Storage
    }

    /**
     * Getting last saved default Path for Local Storage
     *
     * @param file
     * @throws IOException
     */
    private void getLSPathFromFile(File file) throws IOException {
        Path path = Paths.get(file.getCanonicalPath());
        List<StorageLines> lines = new ArrayList<StorageLines>();
        List<Config> chosenPath = new ArrayList<Config>();
        try {
            lines = StorageLines.getParamValue(path.toString(), "Local Storage");
            if (!lines.isEmpty()) {
                for (int i = 0; i < lines.size(); i++) {
                    chosenPath = Config.parseConfigLine(lines.get(i).getValue());
                    for (int k = 0; k < chosenPath.size(); k++) {
                        setLocalStoragePath(chosenPath.get(k).getValue());
                    }
                }
            } else {
                setLocalStoragePath("");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updating value of given param
     *
     * @param file
     * @param param
     * @param value
     * @throws IOException
     */
    private void updateSettings(File file, String param, String value) throws IOException {
        Path path = Paths.get(file.getCanonicalPath());
        List<StorageLines> lines = new ArrayList<StorageLines>();
        try {
            lines = StorageLines.getParamValue(path.toString(), param);
            for (int i = 0; i < lines.size(); i++) {
                StorageLines.updateParamValue(file, lines.get(i).getNumber(), param, value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saving last chosen Path for Local Storage
     */
    private void lastPathOfLS() {
        if (getLocalStoragePath().isEmpty()) {
            LSPathBefore = "";
            System.out.println("If: " + LSPathBefore);
        } else {
            LSPathBefore = getLocalStoragePath();
            System.out.println("Else: " + LSPathBefore);
        }
    }

}
