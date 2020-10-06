import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public ListView<String> clientListView;
    public ListView<String> serverListView;
    private String clientPath = "io-client/src/main/resources/client_dir";
    private String serverPath = "io-server/src/main/resources/server_dir";
    private DataInputStream is;
    private DataOutputStream os;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Socket socket = new Socket("localhost", 8189);
            is = new DataInputStream(socket.getInputStream());
            os = new DataOutputStream(socket.getOutputStream());
            File clientFile = new File(clientPath);
            File serverFile = new File(serverPath);
            String[] clientFiles = clientFile.list();
            String[] serverFiles = serverFile.list();

            if (clientFiles != null) {
                for (String filename : clientFiles) {
                    clientListView.getItems().add(filename);
                }
            } else {
                clientListView.getItems().add("Empty");
            }

            if (serverFiles != null) {
                try {
                    serverListView.getItems().addAll(getServerFiles());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void upload(ActionEvent actionEvent) {
        String file = clientListView.getSelectionModel().getSelectedItem();
        System.out.println(file);

        try {
            os.writeUTF(file);
            File current = new File(clientPath + "/" + file);
            os.writeLong(current.length());
            FileInputStream is = new FileInputStream(current);

            int tmp;
            byte [] buffer = new byte[8192];
            while ((tmp = is.read(buffer)) != -1) {
                os.write(buffer, 0, tmp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshList() {
        File clientFile = new File(clientPath);
        String[] clientFiles = clientFile.list();

        clientListView.getItems().clear();
        if (clientFiles != null) {
            for (String name : clientFiles) {
                clientListView.getItems().add(name);
            }
        }

        try {
            serverListView.getItems().clear();
            serverListView.getItems().addAll(getServerFiles());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshList(ActionEvent actionEvent) { refreshList(); }

    private List<String> getServerFiles() throws IOException {
        List<String> files = new ArrayList<>();
        os.writeUTF("./getFilesList");
        os.flush();
        int listSize = is.readInt();
        for (int i = 0; i < listSize; i++) {
            files.add(is.readUTF());
        }
        return files;
    }
}
