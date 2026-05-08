package iuh.fit.son23641341.nhahanglau_phantan.gui;

import java.awt.*;
import javax.swing.*;

import iuh.fit.son23641341.nhahanglau_phantan.control.User_Ctr;
import iuh.fit.son23641341.nhahanglau_phantan.entity.NhanVien;
import iuh.fit.son23641341.nhahanglau_phantan.util.ImageLoader;

public class DangNhap_GUI extends JFrame {

    private JPasswordField txtPass;
    private JTextField txtUser;
    private JButton btnDangNhap;
    private JButton btnQuenMatKhau;
    private User_Ctr userCtr;
    private static final Color PRIMARY_COLOR = new Color(0xDC4332);

    public DangNhap_GUI() {
        userCtr = User_Ctr.getInstance();

        setTitle("Đăng Nhập");
        setSize(780, 445);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel backgroundPanel = new BackgroundPanel(ImageLoader.loadImage("/imgs/sidebar_dn/DangNhap_BG.png"));
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.X_AXIS));
        setContentPane(backgroundPanel);
        backgroundPanel.setBackground(new Color(0, 0, 0, 100));

        BackgroundPanel loginPanel = new BackgroundPanel(ImageLoader.loadImage("/imgs/sidebar_dn/DangNhapPNL.png"));
        loginPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        loginPanel.setPreferredSize(new Dimension(500, 300));
        loginPanel.setMaximumSize(new Dimension(500, 300));
        loginPanel.setOpaque(false);
        loginPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.X_AXIS));

        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(240, 260));
        leftPanel.setMaximumSize(new Dimension(240, 260));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(Box.createVerticalGlue());

        JPanel rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(240, 260));
        rightPanel.setMaximumSize(new Dimension(240, 260));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 0));
        rightPanel.add(Box.createVerticalStrut(30));

        JLabel lblUser = new JLabel("Tên đăng nhập:");
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtUser = new JTextField();
        txtUser.setPreferredSize(new Dimension(200, 35));
        txtUser.setMaximumSize(new Dimension(200, 35));
        txtUser.setMinimumSize(new Dimension(200, 35));
        txtUser.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#7C1211"), 2),
                BorderFactory.createEmptyBorder(0, 6, 0, 0)
        ));
        txtUser.setAlignmentX(Component.LEFT_ALIGNMENT);

        rightPanel.add(lblUser);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        rightPanel.add(txtUser);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        JLabel lblPass = new JLabel("Mật khẩu:");
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtPass = new JPasswordField();
        txtPass.setPreferredSize(new Dimension(200, 35));
        txtPass.setMaximumSize(new Dimension(200, 35));
        txtPass.setMinimumSize(new Dimension(200, 35));
        txtPass.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#7C1211"), 2),
                BorderFactory.createEmptyBorder(0, 6, 0, 0)
        ));
        txtPass.setAlignmentX(Component.LEFT_ALIGNMENT);

        rightPanel.add(lblPass);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        rightPanel.add(txtPass);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        btnDangNhap = new JButton("Đăng Nhập");
        btnDangNhap.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnDangNhap.setPreferredSize(new Dimension(200, 36));
        btnDangNhap.setMaximumSize(new Dimension(200, 36));
        btnDangNhap.setBackground(Color.decode("#7C1211"));
        btnDangNhap.setForeground(Color.WHITE);
        btnDangNhap.setFocusPainted(false);
        btnDangNhap.setBorderPainted(false);
        btnDangNhap.setOpaque(true);

        btnQuenMatKhau = new JButton("Quên Mật Khẩu");
        btnQuenMatKhau.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnQuenMatKhau.setPreferredSize(new Dimension(200, 36));
        btnQuenMatKhau.setMaximumSize(new Dimension(200, 36));
        btnQuenMatKhau.setBackground(PRIMARY_COLOR);
        btnQuenMatKhau.setForeground(Color.WHITE);
        btnQuenMatKhau.setFocusPainted(false);
        btnQuenMatKhau.setBorderPainted(false);
        btnQuenMatKhau.setOpaque(true);

        rightPanel.add(btnDangNhap);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        rightPanel.add(btnQuenMatKhau);
        rightPanel.add(Box.createVerticalGlue());

        loginPanel.add(Box.createHorizontalStrut(8));
        loginPanel.add(leftPanel);
        loginPanel.add(Box.createHorizontalStrut(12));
        loginPanel.add(rightPanel);
        loginPanel.add(Box.createHorizontalStrut(8));

        backgroundPanel.add(Box.createHorizontalGlue());
        backgroundPanel.add(loginPanel);
        backgroundPanel.add(Box.createHorizontalGlue());

        themSuKienDangNhap();
    }

    private void themSuKienDangNhap() {
        btnDangNhap.addActionListener(e -> xuLyDangNhap());
        txtPass.addActionListener(e -> xuLyDangNhap());
        btnQuenMatKhau.addActionListener(e -> {
            dispose();
            new QuenMatKhau_Dialog().setVisible(true);
        });
    }

    private void xuLyDangNhap() {
        String username = txtUser.getText().trim();
        String password = new String(txtPass.getPassword()).trim();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên đăng nhập!", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
            txtUser.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu!", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
            txtPass.requestFocus();
            return;
        }

        // ĐÃ SỬA: Hàm kiemTraDangNhap tự động lưu NhanVien vào User_Ctr nếu đăng nhập đúng
        if (userCtr.kiemTraDangNhap(username, password)) {
            // Lấy trực tiếp nhân viên hiện tại ra hiển thị (không cần gọi hàm từ DAO nữa)
            NhanVien nhanVienLogin = userCtr.getNhanVienHienTai();
            if (nhanVienLogin != null) {
                System.out.println("Đăng nhập thành công! Xin chào: " + nhanVienLogin.getHoten());
            }

            // Chuyển sang khung giao diện chính (Single Frame)
            try {
                GUIManager.getInstance().openMainGUI(this);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi khởi chạy giao diện chính!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập hoặc mật khẩu không đúng!\n\n", "Đăng nhập thất bại", JOptionPane.ERROR_MESSAGE);
            txtPass.setText("");
            txtUser.requestFocus();
        }
    }

    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(Image image) {
            backgroundImage = image;
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (isOpaque()) {
                super.paintComponent(g);
            }
            if (backgroundImage == null) {
                return;
            }

            int panelWidth = getWidth();
            int panelHeight = getHeight();
            int imgWidth = backgroundImage.getWidth(null);
            int imgHeight = backgroundImage.getHeight(null);

            double imgAspect = (double) imgWidth / imgHeight;
            double panelAspect = (double) panelWidth / panelHeight;

            int drawWidth, drawHeight;
            if (panelAspect > imgAspect) {
                drawWidth = panelWidth;
                drawHeight = (int) (panelWidth / imgAspect);
            } else {
                drawHeight = panelHeight;
                drawWidth = (int) (panelHeight * imgAspect);
            }

            int x = (panelWidth - drawWidth) / 2;
            int y = (panelHeight - drawHeight) / 2;

            g.drawImage(backgroundImage, x, y, drawWidth, drawHeight, this);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DangNhap_GUI().setVisible(true));
    }
}