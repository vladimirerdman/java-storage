package core;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MainPageController {

    public void goToLocalStorage(ActionEvent actionEvent) {
        try {
            Parent localStorage = FXMLLoader.load(getClass().getClassLoader().getResource("localStorage.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Local Storage");
            stage.setScene(new Scene(localStorage));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void settings(MouseEvent mouseEvent) {
        try {
            Parent localStorage = FXMLLoader.load(getClass().getClassLoader().getResource("mainSettings.fxml"));
            //MainSettingsController.setPath("/Users/vladimirerdman/downloads/");
            Stage stage = new Stage();
            stage.setTitle("Storage settings");
            stage.setScene(new Scene(localStorage));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
