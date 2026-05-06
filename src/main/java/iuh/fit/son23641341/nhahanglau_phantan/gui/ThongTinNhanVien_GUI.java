package iuh.fit.son23641341.nhahanglau_phantan.gui;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import iuh.fit.son23641341.nhahanglau_phantan.control.User_Ctr;
import iuh.fit.son23641341.nhahanglau_phantan.control.thongTinNhanVien_ctrl;
import iuh.fit.son23641341.nhahanglau_phantan.entity.NhanVien;
import iuh.fit.son23641341.nhahanglau_phantan.entity.User;

import java.awt.*;

public class ThongTinNhanVien_GUI extends JFrame {

    // Định nghĩa màu sắc để dễ quản lý
    private static final Color SIDEBAR_COLOR = new Color(245, 245, 245);
    private static final Color MAIN_CONTENT_COLOR = new Color(222, 222, 222);
    private static final Color PERSONAL_INFO_COLOR = new Color(255, 255, 255); // Light blue
    private static final Color ACCOUNT_DETAILS_COLOR = new Color(255, 255, 255); // Light red
    private static final Color LOGO_BACKGROUND_COLOR = new Color(253, 198, 0);


    private JLabel nameLabel;
    private JLabel roleLabel;
    private JLabel usernameValueLabel;
    private JLabel emailValueLabel;
    private JLabel phoneValueLabel;
    private JLabel joinDateValueLabel;
    {
        // Khi cửa sổ được mở, chuyển sang trạng thái toàn màn hình (maximized)
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
            }
        });
    }
    public ThongTinNhanVien_GUI() {

        setTitle("Thông tin tài khoản - Hẻ Di Leo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        // KHUNG VÀNG: Sidebar Panel
        JPanel sidebarPanel = createSidebarPanel();

        // KHUNG XANH LÁ: Main Content Panel
        JPanel mainContentPanel = createMainContentPanel();
        
        // Thêm 2 panel chính vào JFrame
        contentPane.add(sidebarPanel, BorderLayout.WEST);
        contentPane.add(mainContentPanel, BorderLayout.CENTER);
        
//        Lấy dữ liệu và cập nhật giao diện 
        NhanVien nvHienTai = User_Ctr.getInstance().getNhanVienHienTai();
        updateThongTin(nvHienTai);
    }

    private JPanel createSidebarPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(300, 0));
        panel.setBackground(SIDEBAR_COLOR);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));


        



    //====================START Sidebar Panel===================


        panel.add(Box.createRigidArea(new Dimension(0, 20))); // Khoảng cách

        SideBar_GUI sidebar = new SideBar_GUI();
			// sidebar.setMauNutKhiChon("Đặt Bàn");
        panel.add(sidebar);

        return panel;
    }

    // -------------------END Sidebar Panel-------------------





    //====================START Main Content Panel===================


    private JPanel createMainContentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 20)); // Khoảng cách ngang 10, dọc 20
        panel.setBackground(MAIN_CONTENT_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Thông tin tài khoản");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Panel trung tâm chứa cả xanh dương và đỏ
        JPanel centerWrapper = new JPanel();
        centerWrapper.setOpaque(false); // Làm trong suốt để lấy màu nền của panel cha
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.Y_AXIS));

        // KHUNG XANH DƯƠNG: Personal Info Panel
        JPanel personalInfoPanel = createPersonalInfoPanel();
        
        // KHUNG ĐỎ: Account Details Panel
        JPanel accountDetailsPanel = createAccountDetailsPanel();
        
        // KHU VỰC NÚT TÍM VÀ ĐỎ: Action Buttons Panel
        JPanel actionButtonPanel = createActionButtonPanel();

        centerWrapper.add(personalInfoPanel);
        centerWrapper.add(Box.createRigidArea(new Dimension(0, 20))); // Khoảng cách giữa panel xanh và đỏ
        centerWrapper.add(accountDetailsPanel);
        
        panel.add(centerWrapper, BorderLayout.CENTER);
        panel.add(actionButtonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createPersonalInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 0));
        panel.setBackground(PERSONAL_INFO_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.black, 1),
            new EmptyBorder(15, 15, 15, 15)
        ));

        // Avatar (vẽ hình tròn)
        JPanel avatarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.LIGHT_GRAY);
                g.fillOval(0, 0, getWidth(), getHeight());
            }
        };
        avatarPanel.setPreferredSize(new Dimension(80, 80));
        
        // Thông tin tên và chức vụ
        nameLabel = new JLabel("Đang tải..."); // Text tạm thời
        nameLabel.setFont(new Font("Arial", Font.BOLD, 22));
        roleLabel = new JLabel("Chức vụ: ...");
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JPanel namePanel = new JPanel();
        namePanel.setOpaque(false);
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.add(nameLabel);
        namePanel.add(roleLabel);

        JButton editButton = new JButton("Sửa");

    // 2. Tạo một panel trung gian để chứa nút Sửa
    //    JPanel mặc định dùng FlowLayout, nó sẽ không kéo dãn nút.
    JPanel editButtonPanel = new JPanel();
    editButtonPanel.setOpaque(false); // Làm trong suốt để lấy màu nền của panel cha
    editButtonPanel.add(editButton); // Thêm nút vào panel trung gian

    // 3. Thêm các component vào panel chính
    panel.add(avatarPanel, BorderLayout.WEST);
    panel.add(namePanel, BorderLayout.CENTER);
    panel.add(editButtonPanel, BorderLayout.EAST);

        
        // Giới hạn chiều cao tối đa của panel này
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height));

        return panel;
    }

    private JPanel createAccountDetailsPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 15)); // 4 hàng, 2 cột
        panel.setBackground(ACCOUNT_DETAILS_COLOR);
        Border redLine = BorderFactory.createLineBorder(Color.black, 1);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(redLine, " Chi tiết tài khoản ", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 16));
        panel.setBorder(BorderFactory.createCompoundBorder(
            titledBorder,
            new EmptyBorder(10, 10, 10, 10)
        ));



        usernameValueLabel = new JLabel();
        emailValueLabel = new JLabel();
        phoneValueLabel = new JLabel();
        joinDateValueLabel = new JLabel();

       // Thêm các trường thông tin (nhãn và giá trị)
        panel.add(new JLabel("Tên đăng nhập:"));
        panel.add(usernameValueLabel);
        panel.add(new JLabel("Email:"));
        panel.add(emailValueLabel);
        panel.add(new JLabel("SĐT:"));
        panel.add(phoneValueLabel);

        // Giới hạn chiều cao tối đa của panel này
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height));

        return panel;
    }
    
    private JPanel createActionButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panel.setOpaque(false); // Trong suốt để lấy nền xanh lá

        JButton logoutButton = new JButton("Đăng xuất");
        logoutButton.setBackground(Color.RED);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setPreferredSize(new Dimension(120, 40));

        panel.add(logoutButton);
        
        return panel;
    }
    public void updateThongTin(NhanVien nv) {
        if (nv == null) {
            nameLabel.setText("Không tìm thấy dữ liệu");
            roleLabel.setText("Chức vụ: N/A");
            usernameValueLabel.setText("N/A");
            emailValueLabel.setText("N/A");
            phoneValueLabel.setText("N/A");
            joinDateValueLabel.setText("N/A");
            return;
        }

        nameLabel.setText(nv.getHoten());
        roleLabel.setText("Chức vụ: " + nv.getChucVu());
        emailValueLabel.setText(nv.getEmail());
        phoneValueLabel.setText(nv.getSdt());
        joinDateValueLabel.setText("N/A"); 
    }
    

   public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {

        thongTinNhanVien_ctrl ctrl = new thongTinNhanVien_ctrl();

        NhanVien nv = ctrl.getNhanVienHienTai();
        User user = ctrl.getUserHienTai();
        // 3. Tạo GUI
        ThongTinNhanVien_GUI gui = new ThongTinNhanVien_GUI();

        // 4. Đẩy dữ liệu Model vào View
        if (nv == null) {
            JOptionPane.showMessageDialog(gui, 
                "Không thể tải dữ liệu người dùng. Lỗi khởi tạo.", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            gui.updateThongTin(null); 
        } else {
            // Dữ liệu hợp lệ, đẩy vào GUI
            gui.updateThongTin(nv);
        }

        // 5. Hiển thị GUI
        gui.setVisible(true);
       
    });
}


} 
