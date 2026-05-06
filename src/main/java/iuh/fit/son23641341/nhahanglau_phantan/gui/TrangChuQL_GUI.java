package iuh.fit.son23641341.nhahanglau_phantan.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import iuh.fit.son23641341.nhahanglau_phantan.control.User_Ctr;
import iuh.fit.son23641341.nhahanglau_phantan.dao.ThongKe_DAO;
import iuh.fit.son23641341.nhahanglau_phantan.control.ThongKe_Ctr;
import iuh.fit.son23641341.nhahanglau_phantan.control.TongQuan_Ctr;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TrangChuQL_GUI extends JFrame {
    // Hằng màu
    private static final Color PRIMARY_COLOR = new Color(0xDC4332);

    // Các panel chính
    private JPanel pnlMain;
    private SideBar_GUI sidebar; 
    private JPanel pnlContent;
    private JPanel pnlBanner;
    private JPanel pnlFeaturedMenu;
    private JPanel pnlOurStory;
    
    // Controller
    private TongQuan_Ctr tongQuanCtr;

    // Phần header
    private JPanel pnlHeader;
    private JLabel lblLogoHeader;

    // Các thành phần cho banner (Đã cập nhật)
    private JLabel lblUserName;
    private JLabel lblUserRole;
    private JLabel lblAvatarBanner;
    private JButton btnTimeDate;
    // Thay btnNotification thành panel hiển thị doanh thu
    private JPanel pnlDoanhThu; 
    private JLabel lblDoanhThuValue; // Biến mới thay cho notification count

    // Phần số liệu thống kê
    private JLabel lblTieuDeSoLieu; 
    private JPanel pnlCacTheSoLieu;
    private JLabel[] lblTenSoLieu; 
    private JLabel[] lblGiaTriSoLieu; 

    private JLabel lblTieuDeCauChuyenCuaChungToi;
    private JTextArea txaCauChuyenCuaChungToi;
    private JPanel pnlTheDiaChi;
    private JLabel lblAnhDiaChi;
    private JLabel lblTieuDeDiaChi;
    private JLabel lblTextDiaChi;

    public TrangChuQL_GUI() {
        try {
            // 1. Kiểm tra đăng nhập
            if (!User_Ctr.getInstance().isDangNhap()) {
                // Xử lý chưa đăng nhập
            }
        } catch (NoClassDefFoundError e) {
             System.err.println("Lỗi: Không tìm thấy User_Ctr. " + e.getMessage());
        }
        
        // 2. Khởi tạo Controller
        tongQuanCtr = new TongQuan_Ctr();
        
        // 3. Khởi tạo và thiết lập GUI
        initializeComponents();
        setupLayout();
        
        // Cập nhật thông tin User/Vai trò lên Banner
        updateUserInfoOnBanner(); 
        
        loadFeaturedData(); // Tải dữ liệu thống kê & Doanh thu
        setupEventHandlers();
        applyStyling();
    }

    private void initializeComponents() {
        pnlMain = new JPanel(new BorderLayout());
        pnlContent = new JPanel();
        pnlHeader = new JPanel();
        pnlBanner = new JPanel();
        pnlFeaturedMenu = new JPanel();
        pnlOurStory = new JPanel();

        lblLogoHeader = new JLabel();

        // Khởi tạo các thành phần mới cho banner
        lblUserName = new JLabel("Tên User");
        lblUserRole = new JLabel("vai trò");
        lblAvatarBanner = new JLabel();
        btnTimeDate = new JButton();
        
        // --- CẬP NHẬT PHẦN DOANH THU ---
        pnlDoanhThu = new JPanel();
        lblDoanhThuValue = new JLabel("0 VNĐ"); // Mặc định
        ThongKe_DAO thongKeDAO = new ThongKe_DAO();
        double doanhThuHomNay = thongKeDAO.getTongTienHomNay(); // Giá trị mặc định ban đầu
        lblDoanhThuValue.setText(String.format("%,.0f VNĐ", doanhThuHomNay));

        lblTieuDeSoLieu = new JLabel(""); 
        pnlCacTheSoLieu = new JPanel(new GridLayout(0, 4, 30, 20)); 
        lblTenSoLieu = new JLabel[4];
        lblGiaTriSoLieu = new JLabel[4];
        
        String[] titles = {"SỐ KHUYẾN MÃI", "SỐ PHIẾU ĐẶT TRƯỚC", "SỐ NHÂN VIÊN", "SỐ BÀN ĐANG SỬ DỤNG"};
        for (int i = 0; i < 4; i++) {
            lblTenSoLieu[i] = new JLabel(titles[i]); 
            lblGiaTriSoLieu[i] = new JLabel("0"); 
        }

        lblTieuDeCauChuyenCuaChungToi = new JLabel("CÂU CHUYỆN CỦA CHÚNG TÔI");
        txaCauChuyenCuaChungToi = new JTextArea();
        pnlTheDiaChi = new JPanel();
        lblAnhDiaChi = new JLabel();
        lblTieuDeDiaChi = new JLabel("Địa chỉ của chúng tôi");
        lblTextDiaChi = new JLabel("12 Nguyễn Văn Bảo, Phường 4, Gò Vấp");
    }

    private void setupLayout() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("HÈ DÌ LEO Restaurant Management - Trang Chủ Quản Lý");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        add(pnlMain, BorderLayout.CENTER);
        
        JPanel pnlSidebarToUse;
        try {
            sidebar = new SideBar_GUI();
            sidebar.setMauNutKhiChon("Tổng Quan"); 
            pnlSidebarToUse = sidebar;
        } catch (NoClassDefFoundError e) {
            System.err.println("Không tìm thấy SideBar_GUI. Sử dụng JPanel thay thế.");
            pnlSidebarToUse = new JPanel();
            sidebar = null; 
        }

        setupHeader();
        setupContent();

        JPanel pnlContentWrapper = new JPanel(new BorderLayout());
        pnlContentWrapper.add(pnlHeader, BorderLayout.NORTH);
        pnlContentWrapper.add(pnlContent, BorderLayout.CENTER);

        pnlMain.add(pnlSidebarToUse, BorderLayout.WEST); 
        pnlMain.add(pnlContentWrapper, BorderLayout.CENTER);
    }
    
    private void setupHeader() {
        pnlHeader.setLayout(new BorderLayout());
        pnlHeader.setPreferredSize(new Dimension(0, 80));
        pnlHeader.setBackground(Color.WHITE);
        pnlHeader.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                new EmptyBorder(15, 20, 15, 20)));

        JPanel pnlPhanLogo = new JPanel(new BorderLayout());
        pnlPhanLogo.setBackground(Color.WHITE);

        try {
            ImageIcon iconLogo = new ImageIcon(getClass().getResource("/imgs/logo.png"));
            if (iconLogo.getIconWidth() > 0) {
                Image anhLogo = iconLogo.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                lblLogoHeader.setIcon(new ImageIcon(anhLogo));
            } else {
                throw new Exception("Không tìm thấy ảnh");
            }
        } catch (Exception e) {
            lblLogoHeader.setText("LOGO");
            lblLogoHeader.setFont(new Font("Arial", Font.BOLD, 14));
            lblLogoHeader.setForeground(PRIMARY_COLOR);
        }

        JPanel pnlChuaLogo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, -25));
        pnlChuaLogo.setBackground(Color.WHITE);
        pnlChuaLogo.add(lblLogoHeader);

        pnlHeader.add(pnlPhanLogo, BorderLayout.WEST);
    }

    private void setupContent() {
        pnlContent.setLayout(new BorderLayout());
        pnlContent.setBackground(Color.WHITE);

        setupBanner();
        setupFeaturedMenu(); 
        setupOurStory();

        JPanel pnlNoiDungChinh = new JPanel();
        pnlNoiDungChinh.setLayout(new BoxLayout(pnlNoiDungChinh, BoxLayout.Y_AXIS));
        pnlNoiDungChinh.setBackground(Color.WHITE);
        pnlNoiDungChinh.setBorder(new EmptyBorder(20, 20, 20, 20));

        pnlNoiDungChinh.add(pnlBanner);
        pnlNoiDungChinh.add(Box.createVerticalStrut(30));
        pnlNoiDungChinh.add(pnlFeaturedMenu);
        pnlNoiDungChinh.add(Box.createVerticalStrut(30));
        pnlNoiDungChinh.add(pnlOurStory);

        JScrollPane scrollPane = new JScrollPane(pnlNoiDungChinh);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        pnlContent.add(scrollPane, BorderLayout.CENTER);
    }
    
    private void updateUserInfoOnBanner() {
        try {
            User_Ctr userCtr = User_Ctr.getInstance();
            
            String hoTen = userCtr.getHoTenHienTai(); 
            String userRole = userCtr.getChucVuHienTai();
            
            if (hoTen != null && !hoTen.trim().isEmpty()) {
                lblUserName.setText(hoTen.toUpperCase()); 
            } else {
                 lblUserName.setText(userCtr.getTenNguoiDung().toUpperCase()); 
            }
            
            if (userRole != null && !userRole.trim().isEmpty()) {
                lblUserRole.setText(userRole.toUpperCase()); 
            } else {
                 lblUserRole.setText("VAI TRÒ");
            }
            
        } catch (NoClassDefFoundError e) {
             System.err.println("Lỗi: Không tìm thấy User_Ctr.");
        }
    }

    /**
     * Setup Banner: Cập nhật phần bên phải thành Doanh Thu
     */
    private void setupBanner() {
        pnlBanner.setLayout(new OverlayLayout(pnlBanner));
        pnlBanner.setPreferredSize(new Dimension(0, 250)); 
        pnlBanner.setBackground(Color.BLACK);

        Image anhBanner = null;
        try {
            ImageIcon iconBanner = new ImageIcon(getClass().getResource("/imgs/lau.png"));
            anhBanner = iconBanner.getImage();
        } catch (Exception e) {
             System.err.println("Không tìm thấy ảnh banner: " + e.getMessage());
        }

        ScaledImageLabel lblAnhBannerScaled = new ScaledImageLabel(anhBanner);
        lblAnhBannerScaled.setOpaque(true);
        lblAnhBannerScaled.setBackground(new Color(60, 60, 60));
        lblAnhBannerScaled.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblAnhBannerScaled.setAlignmentY(Component.CENTER_ALIGNMENT);

        JPanel pnlLopPhu = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(139, 69, 19, 100));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        pnlLopPhu.setOpaque(false);
        pnlLopPhu.setLayout(new BorderLayout());
        pnlLopPhu.setBorder(new EmptyBorder(20, 20, 20, 20)); 

        JPanel pnlContentWrapper = new JPanel(new BorderLayout());
        pnlContentWrapper.setOpaque(false);

        // === PHẦN TRÁI: USER INFO ===
        JPanel pnlUserContent = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, FlowLayout.CENTER));
        pnlUserContent.setOpaque(false);

        try {
            ImageIcon iconAvatar = new ImageIcon(getClass().getResource("/imgs/avatar.png"));
            Image img = iconAvatar.getImage();
            Image scaledImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH); 
            lblAvatarBanner.setIcon(new ImageIcon(scaledImg));
            lblAvatarBanner.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15)); 
        } catch (Exception e) {
            lblAvatarBanner.setText("👤");
            lblAvatarBanner.setFont(new Font("Arial", Font.BOLD, 60)); 
            lblAvatarBanner.setForeground(Color.WHITE);
        }
        
        JPanel pnlNameRole = new JPanel();
        pnlNameRole.setOpaque(false);
        pnlNameRole.setLayout(new BoxLayout(pnlNameRole, BoxLayout.Y_AXIS));
        
        lblUserName.setFont(new Font("Segoe UI", Font.BOLD, 32)); 
        lblUserName.setForeground(Color.WHITE);
        lblUserName.setAlignmentX(Component.LEFT_ALIGNMENT); 

        lblUserRole.setFont(new Font("Segoe UI", Font.PLAIN, 22)); 
        lblUserRole.setForeground(new Color(230, 230, 230));
        lblUserRole.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        pnlNameRole.add(lblUserName);
        pnlNameRole.add(lblUserRole);

        pnlUserContent.add(lblAvatarBanner);
        pnlUserContent.add(pnlNameRole);

        JPanel pnlUserWrapper = new JPanel();
        pnlUserWrapper.setOpaque(false);
        pnlUserWrapper.setLayout(new BoxLayout(pnlUserWrapper, BoxLayout.Y_AXIS));
        
        pnlUserWrapper.add(Box.createVerticalGlue());
        pnlUserWrapper.add(pnlUserContent);
        pnlUserWrapper.add(Box.createVerticalGlue());
        
        pnlContentWrapper.add(pnlUserWrapper, BorderLayout.WEST);

        // === PHẦN PHẢI: TIME & REVENUE (DOANH THU) ===
        
        JPanel pnlRightSide = new JPanel();
        pnlRightSide.setOpaque(false);
        pnlRightSide.setLayout(new BoxLayout(pnlRightSide, BoxLayout.Y_AXIS)); 
        pnlRightSide.setBorder(new EmptyBorder(0, 0, 0, 0));

        Dimension buttonSize = new Dimension(300, 70); 
        
        // Button Thời gian/Ngày
        btnTimeDate.setOpaque(true);
        btnTimeDate.setBackground(PRIMARY_COLOR);
        btnTimeDate.setForeground(Color.WHITE);
        btnTimeDate.setFont(new Font("Segoe UI", Font.BOLD, 22)); 
        btnTimeDate.setBorderPainted(false);
        btnTimeDate.setFocusPainted(false);
        btnTimeDate.setPreferredSize(buttonSize); 
        btnTimeDate.setMaximumSize(buttonSize);
        btnTimeDate.setAlignmentX(Component.CENTER_ALIGNMENT);

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date now = new Date();
                btnTimeDate.setText(timeFormat.format(now) + " " + dateFormat.format(now));
            }
        });
        timer.start();
        btnTimeDate.setText(timeFormat.format(new Date()) + " " + dateFormat.format(new Date()));

        // --- Panel DOANH THU HÔM NAY ---
        JPanel pnlDoanhThuContent = new JPanel();
        pnlDoanhThuContent.setLayout(new BoxLayout(pnlDoanhThuContent, BoxLayout.Y_AXIS)); 
        pnlDoanhThuContent.setOpaque(true);
        pnlDoanhThuContent.setBackground(new Color(255, 255, 255, 230)); // Màu trắng hơi trong suốt
        pnlDoanhThuContent.setPreferredSize(buttonSize); 
        pnlDoanhThuContent.setMaximumSize(buttonSize);
        pnlDoanhThuContent.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2), // Viền đỏ
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JLabel lblDoanhThuTitle = new JLabel("DOANH THU HÔM NAY");
        lblDoanhThuTitle.setFont(new Font("Segoe UI", Font.BOLD, 14)); 
        lblDoanhThuTitle.setForeground(Color.DARK_GRAY);
        lblDoanhThuTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Label hiển thị tiền
        lblDoanhThuValue.setFont(new Font("Segoe UI", Font.BOLD, 24)); 
        lblDoanhThuValue.setForeground(new Color(39, 174, 96)); // Màu xanh lá cây (tiền)
        lblDoanhThuValue.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlDoanhThuContent.add(Box.createVerticalGlue()); 
        pnlDoanhThuContent.add(lblDoanhThuTitle);
        pnlDoanhThuContent.add(lblDoanhThuValue);
        pnlDoanhThuContent.add(Box.createVerticalGlue()); 
        
        // Thêm vào container phải
        pnlRightSide.add(Box.createVerticalGlue()); 
        pnlRightSide.add(btnTimeDate);
        pnlRightSide.add(Box.createVerticalStrut(15)); 
        pnlRightSide.add(pnlDoanhThuContent);
        pnlRightSide.add(Box.createVerticalGlue()); 

        pnlContentWrapper.add(pnlRightSide, BorderLayout.EAST);
        
        pnlLopPhu.add(pnlContentWrapper, BorderLayout.CENTER);

        pnlBanner.add(pnlLopPhu);
        pnlBanner.add(lblAnhBannerScaled);
    }

    private void setupFeaturedMenu() {
        pnlFeaturedMenu.setLayout(new BoxLayout(pnlFeaturedMenu, BoxLayout.Y_AXIS));
        pnlFeaturedMenu.setBackground(Color.WHITE);

        pnlCacTheSoLieu.setOpaque(false);
        pnlCacTheSoLieu.setBorder(new EmptyBorder(10, 10, 10, 10)); 
        
        for (int i = 0; i < 4; i++) {
            JPanel pnlTheWrapper = new JPanel();
            pnlTheWrapper.setLayout(new BoxLayout(pnlTheWrapper, BoxLayout.Y_AXIS));
            pnlTheWrapper.setOpaque(false);
            
            JLabel lblTitle = lblTenSoLieu[i]; 
            lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16)); 
            lblTitle.setForeground(new Color(50, 50, 50)); 
            lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblTitle.setBorder(new EmptyBorder(0, 0, 8, 0)); 

            JPanel pnlThe = new JPanel(new BorderLayout()); 
            pnlThe.setBackground(Color.WHITE);
            pnlThe.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
            pnlThe.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            Dimension cardDim = new Dimension(Integer.MAX_VALUE, 150); 
            pnlThe.setPreferredSize(new Dimension(220, 150)); 
            pnlThe.setMaximumSize(cardDim);
            pnlThe.setMinimumSize(new Dimension(150, 150));

            lblGiaTriSoLieu[i].setFont(new Font("Segoe UI", Font.BOLD, 60)); 
            lblGiaTriSoLieu[i].setForeground(PRIMARY_COLOR); 
            lblGiaTriSoLieu[i].setHorizontalAlignment(SwingConstants.CENTER);
            
            pnlThe.add(lblGiaTriSoLieu[i], BorderLayout.CENTER); 
            
            pnlTheWrapper.add(lblTitle);
            pnlTheWrapper.add(pnlThe);
            
            pnlCacTheSoLieu.add(pnlTheWrapper);
        }
        
        pnlFeaturedMenu.add(pnlCacTheSoLieu); 
    }
    
    /**
     * Tải dữ liệu thống kê và Doanh thu hôm nay
     */
    private void loadFeaturedData() {
        try {
            int[] data = tongQuanCtr.layTatCaSoLieu();
            ThongKe_Ctr thongKeCtr = new ThongKe_Ctr();
            
            if (lblGiaTriSoLieu != null && lblGiaTriSoLieu.length == 4) {
                lblGiaTriSoLieu[0].setText(String.valueOf(data[0])); 
                lblGiaTriSoLieu[1].setText(String.valueOf(thongKeCtr.getTongPhieuDangDat())); 
                lblGiaTriSoLieu[2].setText(String.valueOf(data[2])); 
                lblGiaTriSoLieu[3].setText(String.valueOf(thongKeCtr.getTongBanDangSuDung())); 
            }
            
            // --- CẬP NHẬT DOANH THU HÔM NAY ---
            // Giả lập gọi hàm lấy doanh thu từ Controller (hoặc DAO)
            
        } catch (Exception e) {
            System.err.println("Lỗi khi tải dữ liệu tổng quan: " + e.getMessage());
            for (JLabel label : lblGiaTriSoLieu) {
                label.setText("Lỗi!");
            }
            lblDoanhThuValue.setText("0 VNĐ");
        }
    }


    private void setupOurStory() {
        pnlOurStory.setLayout(new BorderLayout());
        pnlOurStory.setBackground(Color.WHITE);

        JPanel pnlBenTraiCauChuyen = new JPanel();
        pnlBenTraiCauChuyen.setLayout(new BoxLayout(pnlBenTraiCauChuyen, BoxLayout.Y_AXIS));
        pnlBenTraiCauChuyen.setBackground(Color.WHITE);

        lblTieuDeCauChuyenCuaChungToi.setFont(new Font("Arial", Font.BOLD, 24));
        lblTieuDeCauChuyenCuaChungToi.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblTieuDeCauChuyenCuaChungToi.setBorder(new EmptyBorder(0, 0, 20, 0));

        txaCauChuyenCuaChungToi.setText("Chúng tôi là một nhà hàng chuyên về lẩu truyền thống với niềm đam mê " +
                "và tình yêu dành cho ẩm thực Việt Nam. Với những nguyên liệu được " +
                "lựa chọn kỹ lưỡng và công thức nấu ăn được truyền từ thế hệ này sang " +
                "thế hệ khác, chúng tôi cam kết mang đến cho khách hàng những trải nghiệm " +
                "ẩm thực tuyệt vời nhất.");
        txaCauChuyenCuaChungToi.setFont(new Font("Arial", Font.PLAIN, 14));
        txaCauChuyenCuaChungToi.setLineWrap(true);
        txaCauChuyenCuaChungToi.setWrapStyleWord(true);
        txaCauChuyenCuaChungToi.setEditable(false);
        txaCauChuyenCuaChungToi.setOpaque(false);
        txaCauChuyenCuaChungToi.setBorder(new EmptyBorder(0, 0, 0, 20));

        pnlBenTraiCauChuyen.add(lblTieuDeCauChuyenCuaChungToi);
        pnlBenTraiCauChuyen.add(txaCauChuyenCuaChungToi);

        JPanel pnlTheDiaChi = new JPanel();
        pnlTheDiaChi.setLayout(new BoxLayout(pnlTheDiaChi, BoxLayout.Y_AXIS));
        pnlTheDiaChi.setBackground(Color.WHITE);
        pnlTheDiaChi.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(10, 10, 10, 10)));
        pnlTheDiaChi.setMaximumSize(new Dimension(250, 200));
        pnlTheDiaChi.setPreferredSize(new Dimension(250, 200));

        try {
            ImageIcon iconDiaChi = new ImageIcon(getClass().getResource("/imgs/lau.png")); 
            Image anhDiaChi = iconDiaChi.getImage().getScaledInstance(230, 120, Image.SCALE_SMOOTH);
            lblAnhDiaChi.setIcon(new ImageIcon(anhDiaChi));
        } catch (Exception e) {
            lblAnhDiaChi.setText("Address Image");
            lblAnhDiaChi.setHorizontalAlignment(SwingConstants.CENTER);
        }

        lblAnhDiaChi.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTieuDeDiaChi.setFont(new Font("Arial", Font.BOLD, 14));
        lblTieuDeDiaChi.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTextDiaChi.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTextDiaChi.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlTheDiaChi.add(lblAnhDiaChi);
        pnlTheDiaChi.add(Box.createVerticalStrut(10));
        pnlTheDiaChi.add(lblTieuDeDiaChi);
        pnlTheDiaChi.add(Box.createVerticalStrut(5));
        pnlTheDiaChi.add(lblTextDiaChi);

        pnlOurStory.add(pnlBenTraiCauChuyen, BorderLayout.CENTER);
        pnlOurStory.add(pnlTheDiaChi, BorderLayout.EAST);
    }

    private void setupEventHandlers() {
        // Có thể thêm sự kiện click vào panel doanh thu để xem chi tiết
    }

    private void applyStyling() {
        
    }

    class ScaledImageLabel extends JComponent {
        private Image image;

        public ScaledImageLabel(Image image) {
            this.image = image;
            setOpaque(true);
        }

        public void setImage(Image image) {
            this.image = image;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            if (isOpaque()) {
                g2.setColor(getBackground());
                g2.fillRect(0, 0, getWidth(), getHeight());
            }

            if (image == null)
                return;

            int cw = getWidth();
            int ch = getHeight();

            int iw = image.getWidth(null);
            int ih = image.getHeight(null);
            if (iw <= 0 || ih <= 0)
                return;

            double scale = Math.max((double) cw / iw, (double) ch / ih); 
            int drawW = (int) Math.round(iw * scale);
            int drawH = (int) Math.round(ih * scale);

            int x = (cw - drawW) / 2;
            int y = (ch - drawH) / 2;

            g2.drawImage(image, x, y, drawW, drawH, this);
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            try {
                 // Giả lập đăng nhập
                 User_Ctr.getInstance().kiemTraDangNhap("admin", "123456"); 
            } catch (Exception e) {
                System.err.println("Lỗi mô phỏng đăng nhập.");
            }
            
            new TrangChuQL_GUI().setVisible(true);
        });
    }
}
