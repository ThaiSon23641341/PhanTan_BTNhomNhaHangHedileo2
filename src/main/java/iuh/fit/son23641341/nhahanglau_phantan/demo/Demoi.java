package iuh.fit.son23641341.nhahanglau_phantan.demo;

import iuh.fit.son23641341.nhahanglau_phantan.gui.DangNhap_GUI;
import iuh.fit.son23641341.nhahanglau_phantan.server.Server;

import javax.swing.*;

public class Demoi {
    public static void main(String[] args) {
        // Khởi chạy Server trong một luồng riêng
        new Thread(() -> {
            Server.main(args);
        }).start();

        // Chờ một chút để Server khởi động xong
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Khởi chạy Client (GUI đăng nhập)
        SwingUtilities.invokeLater(() -> {
            new DangNhap_GUI().setVisible(true);
        });
    }
}
