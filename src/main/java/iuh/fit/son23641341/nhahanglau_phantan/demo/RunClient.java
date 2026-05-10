package iuh.fit.son23641341.nhahanglau_phantan.demo;

import iuh.fit.son23641341.nhahanglau_phantan.gui.DangNhap_GUI;

import javax.swing.*;

/**
 * Chạy file này trên MÁY CLIENT (máy không cần có CSDL).
 *
 * Cách chạy với IP server cụ thể:
 *
 *   Từ terminal (thay 192.168.1.100 bằng IP thực của máy Server):
 *     java -DserverIp=192.168.1.100 -cp <classpath> iuh.fit.son23641341.nhahanglau_phantan.demo.RunClient
 *
 *   Hoặc build JAR rồi chạy:
 *     java -DserverIp=192.168.1.100 -jar client.jar
 *
 *   Nếu Server và Client cùng 1 máy (không cần truyền tham số):
 *     java -jar client.jar
 */
public class RunClient {
    public static void main(String[] args) {
        String serverIp = System.getProperty("serverIp", "localhost");
        int serverPort = Integer.parseInt(System.getProperty("serverPort", "6789"));

        System.out.println("=== KHỞI ĐỘNG CLIENT NHÀ HÀNG HEDILEO ===");
        System.out.println("Đang kết nối đến Server: " + serverIp + ":" + serverPort);
        System.out.println("==========================================");

        SwingUtilities.invokeLater(() -> {
            new DangNhap_GUI().setVisible(true);
        });
    }
}
