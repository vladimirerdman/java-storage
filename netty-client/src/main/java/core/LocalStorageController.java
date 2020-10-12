package core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LocalStorageController {

    public ListView serverListView;

    public void settings(javafx.event.ActionEvent actionEvent) {
        try {
            Parent localStorage = FXMLLoader.load(getClass().getClassLoader().getResource("mainSettings.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Storage settings");
            stage.setScene(new Scene(localStorage));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
