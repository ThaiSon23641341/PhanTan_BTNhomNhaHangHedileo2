package iuh.fit.son23641341.nhahanglau_phantan.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientControl {
    private static ClientControl instance;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    // Đọc IP từ system property, nếu không có thì dùng "localhost"
    // Để chạy với máy khác: truyền -DserverIp=192.168.x.x khi khởi động
    private String serverIp = System.getProperty("serverIp", "localhost");
    private int port = Integer.parseInt(System.getProperty("serverPort", "6789"));

    private ClientControl() {
        try {
            socket = new Socket(serverIp, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Connected to server at " + serverIp + ":" + port);
        } catch (IOException e) {
            System.err.println("Could not connect to server: " + e.getMessage());
        }
    }

    public static synchronized ClientControl getInstance() {
        if (instance == null) {
            instance = new ClientControl();
        }
        return instance;
    }

    public Response sendRequest(Request request) {
        try {
            if (socket == null || socket.isClosed()) {
                // Thử kết nối lại
                instance = new ClientControl();
            }
            out.writeObject(request);
            out.flush();
            return (Response) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error communication with server: " + e.getMessage());
            return new Response("ERROR", null, e.getMessage());
        }
    }

    public void close() {
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
