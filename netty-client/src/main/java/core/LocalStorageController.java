package core;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LocalStorageController implements Initializable {
    private Network network = Network.getInstance();
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Thread t = new Thread(() -> {

        }
    }
}
