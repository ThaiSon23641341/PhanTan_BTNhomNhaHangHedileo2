package iuh.fit.son23641341.nhahanglau_phantan.gui;

import iuh.fit.son23641341.nhahanglau_phantan.control.User_Ctr; // Import controller để lấy thông tin chức vụ
import iuh.fit.son23641341.nhahanglau_phantan.entity.NhanVien;
import iuh.fit.son23641341.nhahanglau_phantan.util.ImageLoader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.Box;
import javax.swing.border.EmptyBorder; 
import java.awt.Component;

// Import các lớp GUI để chắc chắn trình biên dịch tìm thấy
import iuh.fit.son23641341.nhahanglau_phantan.gui.TrangChu_GUI;
import iuh.fit.son23641341.nhahanglau_phantan.gui.TrangChuQL_GUI;
import iuh.fit.son23641341.nhahanglau_phantan.gui.ChonBan_GUI;
import iuh.fit.son23641341.nhahanglau_phantan.gui.TimKiem_GUI;
import iuh.fit.son23641341.nhahanglau_phantan.gui.DanhSachMonAnQL_GUI;
import iuh.fit.son23641341.nhahanglau_phantan.gui.QuanLyKhachHang_GUI;
import iuh.fit.son23641341.nhahanglau_phantan.gui.ThongKe_GUI;
import iuh.fit.son23641341.nhahanglau_phantan.gui.QuanLiDuLieu_GUI;
import iuh.fit.son23641341.nhahanglau_phantan.gui.TroGiup_GUI;
import iuh.fit.son23641341.nhahanglau_phantan.gui.DangNhap_GUI;


public class SideBar_GUI extends JPanel implements ActionListener {
    private JButton btnTrangChu;
    private JButton btnTimKiem;
    private JButton btnDatBan;
    private JButton btnQuanLyMon;
    private JButton btnKhachHang;
    private JButton btnThongKe;
    private JButton btnQuanLyDuLieu;
    private JButton btnTroGiup;
    private JButton btnTongQuan; 
    
    private Main_GUI mainGUI; // Đổi tên thành mainGUI
    private GUIManager guiManager;
    private Map<String, JButton> buttonMap;
    
    public void setMainGui(Main_GUI mainGUI) {
        this.mainGUI = mainGUI;
    }
    
    // Giả định các lớp GUI khác đã được import hoặc nằm cùng package.
    // VD: TrangChuQL_GUI, TrangChu_GUI, TimKiem_GUI, ChonBan_GUI, DanhSachMonAnQL_GUI, QuanLyKhachHang_GUI, ThongKe_GUI, QuanLiDuLieu_GUI, DangNhap_GUI

    public SideBar_GUI() {
        setLayout(new BorderLayout()); 
        setBackground(Color.WHITE); 

        // ===============================================
        // Panel chứa Logo và chữ ADMIN (NORTH)
        // ===============================================
        JPanel pnlAdminInfo = new JPanel();
        pnlAdminInfo.setLayout(new BoxLayout(pnlAdminInfo, BoxLayout.Y_AXIS));
        pnlAdminInfo.setBackground(Color.WHITE);
        pnlAdminInfo.setBorder(new EmptyBorder(10, 0, 10, 0)); 

        // 1. Logo
        JLabel logoLabel = new JLabel();
        ImageIcon logoIcon = ImageLoader.loadImageIcon("/imgs/logo.png");
        if (logoIcon != null) {
            Image scaledImage = logoIcon.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
            logoIcon = new ImageIcon(scaledImage);
            logoLabel.setIcon(logoIcon);
        } else {
            logoLabel.setText("LOGO HỆ THỐNG");
            logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        }
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 2. Panel con chứa Icon và Chữ ADMIN (xếp ngang)
        JPanel pnlAdminDetails = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0)); 
        pnlAdminDetails.setBackground(Color.WHITE);
        pnlAdminDetails.setBorder(new EmptyBorder(5, 0, 10, 0)); 

        // 2a. Icon Avatar mới ("/imgs/avatar.png")
        JLabel lblAvatar = new JLabel();
        ImageIcon iconAvatar = ImageLoader.loadImageIcon("/imgs/avatar.png");
        if (iconAvatar != null) {
            Image scaledIcon = iconAvatar.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            iconAvatar = new ImageIcon(scaledIcon);
            lblAvatar.setIcon(iconAvatar);
        } else {
            lblAvatar.setText("AVT");
        }
        
        // 2b. Chữ ADMIN --> ĐÃ CẬP NHẬT ĐỂ HIỂN THỊ CHỨC VỤ ĐỘNG
        String chucVu = User_Ctr.getInstance().getNhanVienHienTai() != null
				? User_Ctr.getInstance().getNhanVienHienTai().getChucVu()
				: null;
        JLabel lblAdmin = new JLabel(); 
        
        if (chucVu != null && !chucVu.trim().isEmpty()) {
            lblAdmin.setText(chucVu.toUpperCase()); // Chuyển sang chữ hoa
        } else {
            lblAdmin.setText("ADMIN"); // Mặc định
        }

        lblAdmin.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblAdmin.setForeground(Color.RED);

        // Thêm Icon và Chữ ADMIN vào Panel con
        pnlAdminDetails.add(lblAvatar);
        pnlAdminDetails.add(lblAdmin);
        
        // Thêm Logo và Panel con vào pnlAdminInfo (xếp dọc)
        pnlAdminInfo.add(logoLabel);
        pnlAdminInfo.add(pnlAdminDetails); 

        add(pnlAdminInfo, BorderLayout.NORTH);
        
        // ===============================================

        // Danh mục nằm ở center (MENU ITEMS)
        JPanel pnlCenter = new JPanel();
        pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));
        pnlCenter.setBackground(Color.WHITE);
        
        // Khởi tạo nút Tổng Quan
        btnTongQuan = createButton("Tổng Quan", "/imgs/sidebar_dn/thongke.png"); 
        btnTrangChu = createButton("Trang Chủ", "/imgs/sidebar_dn/trangchu.png");
        btnTimKiem = createButton("Tìm Kiếm", "/imgs/sidebar_dn/timkiem.png");
        btnDatBan = createButton("Đặt Bàn", "/imgs/sidebar_dn/datban.png");
        btnQuanLyMon = createButton("Quản Lý Món", "/imgs/sidebar_dn/quanlymon.png");
        btnKhachHang = createButton("Khách Hàng", "/imgs/sidebar_dn/khachhang.png");
        btnThongKe = createButton("Thống Kê", "/imgs/sidebar_dn/thongke.png");
        btnQuanLyDuLieu = createButton("Quản Lý Dữ Liệu", "/imgs/sidebar_dn/quanlydulieu.png");
        btnTroGiup = createButton("Hướng Dẫn Sử Dụng", "/imgs/sidebar_dn/trogiup.png");

        // Cho button vào map
        buttonMap = new HashMap<>();
        this.guiManager = GUIManager.getInstance();
        buttonMap.put("Tổng Quan", btnTongQuan); 
        buttonMap.put("Trang Chủ", btnTrangChu);
        buttonMap.put("Tìm Kiếm", btnTimKiem);
        buttonMap.put("Đặt Bàn", btnDatBan);
        buttonMap.put("Quản Lý Món", btnQuanLyMon);
        buttonMap.put("Khách Hàng", btnKhachHang);
        buttonMap.put("Thống Kê", btnThongKe);
        buttonMap.put("Quản Lý Dữ Liệu", btnQuanLyDuLieu);
        buttonMap.put("Trợ Giúp", btnTroGiup);
        
        // add button vào center
        pnlCenter.add(btnTongQuan);
        pnlCenter.add(btnTrangChu);
        pnlCenter.add(btnTimKiem);
        pnlCenter.add(btnDatBan);
        pnlCenter.add(btnQuanLyMon);
        pnlCenter.add(btnKhachHang);
        pnlCenter.add(btnThongKe);
        pnlCenter.add(btnQuanLyDuLieu);
        pnlCenter.add(btnTroGiup);
        
        // Nút Đăng Xuất (SOUTH)
        JButton btnDangXuat = createButton("Đăng Xuất", "/imgs/sidebar_dn/dangxuat.png");
        
        JPanel pnlLogout = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlLogout.setBackground(Color.WHITE);
        pnlLogout.add(btnDangXuat);
        
        add(pnlCenter, BorderLayout.CENTER);
        add(pnlLogout, BorderLayout.SOUTH);

        // acionlistener cho các nút
        btnTongQuan.addActionListener(this);
        btnTrangChu.addActionListener(this);
        btnTimKiem.addActionListener(this);
        btnDatBan.addActionListener(this);
        btnQuanLyMon.addActionListener(this);
        btnKhachHang.addActionListener(this);
        btnThongKe.addActionListener(this);
        btnQuanLyDuLieu.addActionListener(this);
        btnTroGiup.addActionListener(this);
        btnDangXuat.addActionListener(this);

        setSize(295, 840);
        
        phanQuyenTaiKhoan();

    }

    // Phương thức khởi tạo button với các thuộc tính chung
    private JButton createButton(String text, String iconPath) {
        ImageIcon icon = ImageLoader.loadImageIcon(iconPath);
        JButton button = new JButton(text, icon);
        button.setMaximumSize(new java.awt.Dimension(295, 50));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIconTextGap(15); 
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.BLACK);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        return button;
    }

    // Phương thức đổi màu nút
    public void setMauNutKhiChon(String xacdinh) {
        // Reset màu tất cả nút về mặc định
        for (JButton btn : buttonMap.values()) {
            btn.setContentAreaFilled(false);
            btn.setBackground(Color.WHITE);
        }

        // Đổi màu nút được chọn
        JButton selectedButton = buttonMap.get(xacdinh);
        if (selectedButton != null) {
            selectedButton.setContentAreaFilled(true);
            selectedButton.setBackground(Color.decode("#f8c59e"));
        }

    }

    public static void main(String[] args) {
        main1(args);
    }
    
    private void phanQuyenTaiKhoan() {
        NhanVien nv = User_Ctr.getInstance().getNhanVienHienTai();
        if (nv == null) return; 

        String chucVu = nv.getChucVu() != null ? nv.getChucVu().trim() : ""; 
        System.out.println("Phân quyền Sidebar cho chức vụ: " + chucVu);

        // Kiểm tra vai trò Thu ngân (chấp nhận cả có dấu và không dấu)
        if (chucVu.equalsIgnoreCase("Thu ngan") || chucVu.equalsIgnoreCase("Thu ngân")) {
            // Hiển thị: Tổng quan, Trang chủ, Đặt bàn, Thống kê, Hướng dẫn sử dụng
            btnTongQuan.setVisible(true);
            btnTrangChu.setVisible(true);
            btnDatBan.setVisible(true);
            btnThongKe.setVisible(true);
            btnTroGiup.setVisible(true);

            // Ẩn các chức năng không được phép: Tìm kiếm, Quản lý món, Khách hàng, Quản lý dữ liệu
            btnTimKiem.setVisible(false);
            btnQuanLyMon.setVisible(false);
            btnKhachHang.setVisible(false);
            btnQuanLyDuLieu.setVisible(false);
            
        } else {
            // Mặc định cho Quản lý hoặc các vai trò khác: Hiển thị tất cả
            btnTongQuan.setVisible(true);
            btnTrangChu.setVisible(true);
            btnTimKiem.setVisible(true);
            btnDatBan.setVisible(true);
            btnQuanLyMon.setVisible(true);
            btnKhachHang.setVisible(true);
            btnThongKe.setVisible(true);
            btnQuanLyDuLieu.setVisible(true);
            btnTroGiup.setVisible(true);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        Window window = SwingUtilities.getWindowAncestor(this);
        GUIManager guiManager = GUIManager.getInstance();

        // Xác định GUI class dựa trên nút bấm
        Class<? extends Component> targetClass = null;
        String actionName = "";

        if (source == btnTongQuan) {
            targetClass = TrangChuQL_GUI.class;
            actionName = "Tổng Quan";
        } else if (source == btnTrangChu) {
            targetClass = TrangChu_GUI.class;
            actionName = "Trang Chủ";
        } else if (source == btnTimKiem) {
            targetClass = TimKiem_GUI.class;
            actionName = "Tìm Kiếm";
        } else if (source == btnDatBan) {
            targetClass = ChonBan_GUI.class;
            actionName = "Đặt Bàn";
        } else if (source == btnQuanLyMon) {
            targetClass = DanhSachMonAnQL_GUI.class;
            actionName = "Quản Lý Món";
        } else if (source == btnKhachHang) {
            targetClass = QuanLyKhachHang_GUI.class;
            actionName = "Khách Hàng";
        } else if (source == btnThongKe) {
            targetClass = ThongKe_GUI.class;
            actionName = "Thống Kê";
        } else if (source == btnQuanLyDuLieu) {
            targetClass = QuanLiDuLieu_GUI.class;
            actionName = "Quản Lý Dữ Liệu";
        } else if (source == btnTroGiup) {
            targetClass = TroGiup_GUI.class;
            actionName = "Trợ Giúp";
        }

        if (targetClass != null) {
            System.out.println(">>> SideBar_GUI: Click nút " + actionName);
            guiManager.switchToGUI(targetClass, window);
        } else {
            // Trường hợp Đăng Xuất (không có targetClass)
            xuLyDangXuat(window);
        }
    }

    private void xuLyDangXuat(Window window) {
        int choice = JOptionPane.showConfirmDialog(window,
                "Bạn có chắc muốn đăng xuất?",
                "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            User_Ctr.getInstance().dangXuat();
            GUIManager.getInstance().disposeAll();
            if (window != null) {
                window.dispose();
            }
            try {
                new DangNhap_GUI().setVisible(true);
            } catch (NoClassDefFoundError ex) {
                JOptionPane.showMessageDialog(window, "Không tìm thấy DangNhap_GUI.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main1(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Sidebar Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SideBar_GUI sidebar = new SideBar_GUI();

        sidebar.setMauNutKhiChon("Tổng Quan");

        frame.add(sidebar);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
