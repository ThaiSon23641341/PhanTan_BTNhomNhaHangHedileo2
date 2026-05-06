package iuh.fit.son23641341.nhahanglau_phantan.gui;

import java.awt.*;
import java.net.URL;

import javax.swing.*;

import iuh.fit.son23641341.nhahanglau_phantan.control.User_Ctr;
import iuh.fit.son23641341.nhahanglau_phantan.dao.NhanVien_DAO; 
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
        // Khởi tạo controller
        userCtr = User_Ctr.getInstance();
        
        
        setTitle("Đăng Nhập");  
        setSize(780, 445);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel nền có ảnh (vẽ tự động, không méo)

        // Truyền Image vào BackgroundPanel
        JPanel backgroundPanel = new BackgroundPanel(ImageLoader.loadImage("/imgs/sidebar_dn/DangNhap_BG.png"));
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);
        // cho background mờ và tối  hơn
        backgroundPanel.setBackground(new Color(0, 0, 0, 100));
        // set độ mờ của panel nền (0-255), 0 là trong suốt hoàn toàn, 255 là không trong suốt
        // thêm panel đăng nhập vào ngay giữa panel nền
        

        //set lại layout của frame thành box 
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.X_AXIS));

        
//        URL imgURLlogin = getClass().getResource("/imgs/sidebar_dn/DangNhapPNL.png");
        BackgroundPanel loginPanel = new BackgroundPanel(ImageLoader.loadImage("/imgs/sidebar_dn/DangNhapPNL.png"));
         //setlogin nằm giữa
         loginPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
         // --- ADDED: ensure vertical centering as well ---
         loginPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
         loginPanel.setPreferredSize(new Dimension(500, 300));
         loginPanel.setMaximumSize(new Dimension(500, 300));
         // Để vùng trong suốt của PNG hiển thị nền cha phía sau, không cho loginPanel tự vẽ nền trắng
         loginPanel.setOpaque(false);
         // padding chung
         loginPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
         // chia trái / phải
         loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.X_AXIS));
         

         // left panel (có thể để trống hoặc dùng cho hình/miêu tả)
         JPanel leftPanel = new JPanel();
         leftPanel.setOpaque(false);
         leftPanel.setPreferredSize(new Dimension(240, 260));
         leftPanel.setMaximumSize(new Dimension(240, 260));
         leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
         leftPanel.add(Box.createVerticalGlue()); // giữ canh giữa theo dọc nếu cần
         

         // right panel: form chứa Tên đăng nhập và Mật khẩu chỉnh xuống dưới một chút
         JPanel rightPanel = new JPanel();

         rightPanel.setPreferredSize(new Dimension(240, 260));
         rightPanel.setMaximumSize(new Dimension(240, 260));
         rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
         // Xích các thành phần bên trong sang phải bằng padding (trái)
         rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 0));
         
         // Đẩy nội dung xuống một chút
         rightPanel.add(Box.createVerticalStrut(30));
         
         //Người dùng bấm quên mk -> truy cập vào quenmatKhau_Dialog -> ten, email, sdt (tìm ra mới có mk mới), mk mới 

         // Username
         JLabel lblUser = new JLabel("Tên đăng nhập:");
         lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtUser = new JTextField();
        // tăng chiều rộng và giảm chiều cao
        txtUser.setPreferredSize(new Dimension(200, 35));
        txtUser.setMaximumSize(new Dimension(200, 35));
        txtUser.setMinimumSize(new Dimension(200, 35));
        // viền màu #7C1211
        txtUser.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#7C1211"), 2),
            BorderFactory.createEmptyBorder(0, 6, 0, 0) // padding trái 6px
        ));
         txtUser.setAlignmentX(Component.LEFT_ALIGNMENT);
         

         // Spacer
         rightPanel.add(lblUser);
         rightPanel.add(Box.createRigidArea(new Dimension(0, 6)));
         rightPanel.add(txtUser);
         rightPanel.add(Box.createRigidArea(new Dimension(0, 12)));
         

         // Password
         JLabel lblPass = new JLabel("Mật khẩu:");
         lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);
         txtPass = new JPasswordField();
         // tăng chiều rộng và giảm chiều cao
         txtPass.setPreferredSize(new Dimension(200, 35));
         txtPass.setMaximumSize(new Dimension(200, 35));
         txtPass.setMinimumSize(new Dimension(200, 35));
         // viền màu #7C1211
         txtPass.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#7C1211"), 2),
            BorderFactory.createEmptyBorder(0, 6, 0, 0) // padding trái 6px
        ));
         txtPass.setAlignmentX(Component.LEFT_ALIGNMENT);
         

         rightPanel.add(lblPass);
         rightPanel.add(Box.createRigidArea(new Dimension(0, 6)));
         rightPanel.add(txtPass);
         // Khoảng cách nhỏ trước nút
         rightPanel.add(Box.createRigidArea(new Dimension(0, 12)));
         // Nút Đăng Nhập: nền #7C1211, chữ trắng
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
         

         // ghép vào loginPanel với khoảng cách giữa 2 cột
         loginPanel.add(Box.createHorizontalStrut(8));
         loginPanel.add(leftPanel);
         loginPanel.add(Box.createHorizontalStrut(12));
         loginPanel.add(rightPanel);
         loginPanel.add(Box.createHorizontalStrut(8));
         

         backgroundPanel.add(Box.createHorizontalGlue()); // Khoảng cách trái
         backgroundPanel.add(loginPanel);
         backgroundPanel.add(Box.createHorizontalGlue()); // Khoảng cách phải để căn giữa

         // Thêm sự kiện đăng nhập
         themSuKienDangNhap();
    }

    private void themSuKienDangNhap() {
        // Sự kiện khi nhấn nút "Đăng Nhập"
        btnDangNhap.addActionListener(e -> xuLyDangNhap());
        
        // Sự kiện khi nhấn Enter trong ô mật khẩu
        txtPass.addActionListener(e -> xuLyDangNhap());
        
        // Sự kiện khi nhấn nút "Quên Mật Khẩu"
        btnQuenMatKhau.addActionListener(e -> {
            dispose();
            new QuenMatKhau_Dialog().setVisible(true);
        });
    }
    
    
    
    private void xuLyDangNhap() {
        String username = txtUser.getText().trim();
        String password = new String(txtPass.getPassword()).trim();
        
        // Kiểm tra rỗng
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập tên đăng nhập!",
                "Thiếu thông tin",
                JOptionPane.WARNING_MESSAGE);
            txtUser.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập mật khẩu!",
                "Thiếu thông tin",
                JOptionPane.WARNING_MESSAGE);
            txtPass.requestFocus();
            return;
        }
        
        // Kiểm tra đăng nhập
        if (userCtr.kiemTraDangNhap(username, password)) {
        	
        	NhanVien nhanVienLogin = NhanVien_DAO.timNhanVienTheoDangNhap(username, password) ;
        	if (nhanVienLogin != null) {
                // === LƯU NHÂN VIÊN VÀO CONTROLLER ===
                userCtr.setNhanVienHienTai(nhanVienLogin); 
                JOptionPane.showMessageDialog(this, "Xin chào: " + nhanVienLogin.getHoten());
        	}
        	// Chuyển sang trang chủ - Sử dụng GUIManager để chuyển mượt mà
            GUIManager.getInstance().switchToGUI(TrangChu_GUI.class, this);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Tên đăng nhập hoặc mật khẩu không đúng!\n\n" ,
                "Đăng nhập thất bại",
                JOptionPane.ERROR_MESSAGE);
            txtPass.setText(""); // Xóa mật khẩu
            txtUser.requestFocus();
        }
    }

    // --- CLASS NỘI BỘ: Panel có ảnh nền tự co giãn đúng tỉ lệ ---
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(Image image) {
            backgroundImage = image;
        }

        @Override
        protected void paintComponent(Graphics g) {
            // Chỉ xóa nền mặc định nếu panel là opaque; giữ nguyên hành vi khi cần vẽ nền
            if (isOpaque()) {
                super.paintComponent(g);
            }

            // Nếu không có ảnh, không vẽ gì
            if (backgroundImage == null) {
                return;
            }

            // Vẽ ảnh full màn hình, giữ đúng tỉ lệ (scale vừa khung)
            int panelWidth = getWidth();
            int panelHeight = getHeight();

            int imgWidth = backgroundImage.getWidth(null);
            int imgHeight = backgroundImage.getHeight(null);

            double imgAspect = (double) imgWidth / imgHeight;
            double panelAspect = (double) panelWidth / panelHeight;

            int drawWidth, drawHeight;
            if (panelAspect > imgAspect) {
                // Khung rộng hơn ảnh → scale theo chiều ngang
                drawWidth = panelWidth;
                drawHeight = (int) (panelWidth / imgAspect);
            } else {
                // Khung cao hơn ảnh → scale theo chiều dọc
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

