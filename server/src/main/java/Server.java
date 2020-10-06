import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final String path = "server/src/main/resources/server_dir/";

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(8189)) {
            System.out.println("Server started");
            Socket socket = server.accept();
            System.out.println("Client accepted");
            DataInputStream is = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            while (true) {
//                Thread.sleep(1000);
                String filename = is.readUTF();// name of file
                if (filename.equals("./getFilesList")) {
                    File dir = new File(path);
                    String [] files = dir.list();
                    if (files != null) {
                        dos.writeInt(files.length);
                        for (String file : files) {
                            dos.writeUTF(file);
                        }
                    } else {
                        dos.writeInt(0);
                    }
                    dos.flush();
                } else {
                    System.out.println("Getting file: " + filename);

                    File file = new File(path + filename);
                    if (!file.exists()) {
                        file.createNewFile();
                    } else {
                        System.out.println("Error. File already exist");
                    }

                    FileOutputStream os = new FileOutputStream(file);
                    long fileLength = is.readLong();// file length
                    System.out.println("Waiting: " + fileLength + " bytes");
                    byte[] buffer = new byte[8192];
                    for (int i = 0; i < (fileLength + 8191) / 8192; i++) {// file bytes
                        int cnt = is.read(buffer);
                        os.write(buffer, 0, cnt);
                    }

                    System.out.println("File successfully uploaded!");
                    os.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
