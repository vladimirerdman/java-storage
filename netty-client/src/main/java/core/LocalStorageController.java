package core;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class LocalStorageController implements Initializable {
    private static final Logger log = LogManager.getLogger(LocalStorageController.class.getName());
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
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(() -> {
            network.getClientHandler().setCallback(serviceMessage -> {
                System.out.println(serviceMessage.toString());
//                String[] command = serviceMessage.split("\n");
//                if (command[0].equals("/FileList")) {
//
//                }
            });
        }).start();
    }
}
