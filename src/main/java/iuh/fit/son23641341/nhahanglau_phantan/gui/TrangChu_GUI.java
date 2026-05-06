package iuh.fit.son23641341.nhahanglau_phantan.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import iuh.fit.son23641341.nhahanglau_phantan.control.User_Ctr;
import iuh.fit.son23641341.nhahanglau_phantan.util.ImageLoader;

import java.awt.*;

public class TrangChu_GUI extends JFrame {
    // Hằng màu
    private static final Color PRIMARY_COLOR = new Color(0xDC4332);

    // Các panel chính
    private JPanel pnlMain;
    private SideBar_GUI sidebar;
    private JPanel pnlContent;
    private JPanel pnlBanner;
    private JPanel pnlFeaturedMenu;
    private JPanel pnlOurStory;

    // Phần header
    private JPanel pnlHeader;
    private JLabel lblAvatar;
    private JLabel lblLogoHeader;

    // Phần banner
    private JLabel lblTenNhaHang;
    private JLabel lblKhauHieu;
    private JButton btnDatBanNgay;

    // Phần menu nổi bật
    private JLabel lblTieuDeMenu;
    private JPanel pnlCacTheMonAn;
    private JLabel[] lblTenMonAn;
    private JLabel[] lblGiaMonAn;

    private JLabel lblTieuDeCauChuyenCuaChungToi;
    private JTextArea txaCauChuyenCuaChungToi;
    private JPanel pnlTheDiaChi;
    private JLabel lblAnhDiaChi;
    private JLabel lblTieuDeDiaChi;
    private JLabel lblTextDiaChi;
    
    // Lưu ý: Các lớp ngoài (User_Ctr, DangNhap_GUI, ChonBan_GUI, SideBar_GUI) cần phải tồn tại.

    public TrangChu_GUI() {
        // Kiểm tra đăng nhập
        if (!User_Ctr.getInstance().isDangNhap()) {
            // new DangNhap_GUI().setVisible(true);
            // dispose();
            // return;
        }
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        applyStyling();
    }

    private void initializeComponents() {
        // Khởi tạo panels
        pnlMain = new JPanel(new BorderLayout());
        pnlContent = new JPanel();
        pnlHeader = new JPanel();
        pnlBanner = new JPanel();
        pnlFeaturedMenu = new JPanel();
        pnlOurStory = new JPanel();

        // Khởi tạo header
        lblAvatar = new JLabel();
        lblLogoHeader = new JLabel();

        // Khởi tạo banner
        lblTenNhaHang = new JLabel("HẺ DÌ LEO RESTAURANT");
        lblKhauHieu = new JLabel("Nhà hàng số 1 Việt Nam");
        btnDatBanNgay = new JButton("Đặt bàn ngay");

        // Khởi tạo menu nổi bật
        lblTieuDeMenu = new JLabel("THỰC ĐƠN NỔI BẬT");
        pnlCacTheMonAn = new JPanel(new GridLayout(0, 4, 15, 15));
        lblTenMonAn = new JLabel[4];
        lblGiaMonAn = new JLabel[4];

        // Khởi tạo câu chuyện
        lblTieuDeCauChuyenCuaChungToi = new JLabel("CÂU CHUYỆN CỦA CHÚNG TÔI");
        txaCauChuyenCuaChungToi = new JTextArea();
        pnlTheDiaChi = new JPanel();
        lblAnhDiaChi = new JLabel();
        lblTieuDeDiaChi = new JLabel("Địa chỉ của chúng tôi");
        lblTextDiaChi = new JLabel("12 Nguyễn Văn Bảo, Phường 4, Gò Vấp");

        // Khởi tạo các thẻ món ăn
        for (int i = 0; i < 4; i++) {
            lblTenMonAn[i] = new JLabel("Lẩu Mala Sewcewle " + (i + 1));
            lblGiaMonAn[i] = new JLabel("459.000 VND");
        }
    }

    private void setupLayout() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("HÈ DÌ LEO Restaurant Management");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        add(pnlMain, BorderLayout.CENTER);
        
        // Khởi tạo sidebar và đánh dấu Trang Chủ
        sidebar = new SideBar_GUI();
        sidebar.setMauNutKhiChon("Trang Chủ");

        setupHeader();
        setupContent();

        // Tạo wrapper chứa header và content
        JPanel pnlContentWrapper = new JPanel(new BorderLayout());
        pnlContentWrapper.add(pnlHeader, BorderLayout.NORTH);
        pnlContentWrapper.add(pnlContent, BorderLayout.CENTER);

        pnlMain.add(sidebar, BorderLayout.WEST);
        pnlMain.add(pnlContentWrapper, BorderLayout.CENTER);
    }
    
    private void setupHeader() {
        pnlHeader.setLayout(new BorderLayout());
        pnlHeader.setPreferredSize(new Dimension(0, 80));
        pnlHeader.setBackground(Color.WHITE);
        pnlHeader.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                new EmptyBorder(15, 20, 15, 20)));

        // *** ĐÃ XÓA pnlPhanAdmin KHỎI HEADER ***
        
        // Bên trái - Logo (WEST)
        JPanel pnlPhanLogo = new JPanel(new BorderLayout());
        pnlPhanLogo.setBackground(Color.WHITE);

        // Tải ảnh logo
        ImageIcon iconLogo = ImageLoader.loadImageIcon("/imgs/logo.png");
        if (iconLogo != null && iconLogo.getIconWidth() > 0) {
            Image anhLogo = iconLogo.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            lblLogoHeader.setIcon(new ImageIcon(anhLogo));
        } else {
            lblLogoHeader.setText("LOGO");
        }
        lblLogoHeader.setFont(new Font("Arial", Font.BOLD, 14));
        lblLogoHeader.setForeground(PRIMARY_COLOR);

        // Căn Logo sang bên trái
        JPanel pnlChuaLogo = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, -25));
        pnlChuaLogo.setBackground(Color.WHITE);
        pnlChuaLogo.add(lblLogoHeader);

        pnlPhanLogo.add(pnlChuaLogo, BorderLayout.NORTH);

        pnlHeader.add(pnlPhanLogo, BorderLayout.WEST);
        // *** KHÔNG THÊM pnlPhanAdmin VÀO BorderLayout.EAST ***
    }

    private void setupContent() {
        pnlContent.setLayout(new BorderLayout());
        pnlContent.setBackground(Color.WHITE);

        setupBanner();
        setupFeaturedMenu();
        setupOurStory();

        // Thêm vào content panel
        JPanel pnlNoiDungChinh = new JPanel();
        pnlNoiDungChinh.setLayout(new BoxLayout(pnlNoiDungChinh, BoxLayout.Y_AXIS));
        pnlNoiDungChinh.setBackground(Color.WHITE);
        pnlNoiDungChinh.setBorder(new EmptyBorder(20, 20, 20, 20));

        pnlNoiDungChinh.add(pnlBanner);
        pnlNoiDungChinh.add(Box.createVerticalStrut(30));
        pnlNoiDungChinh.add(pnlFeaturedMenu);
        pnlNoiDungChinh.add(Box.createVerticalStrut(30));
        pnlNoiDungChinh.add(pnlOurStory);

        // Thêm ScrollPane cho toàn bộ nội dung
        JScrollPane scrollPane = new JScrollPane(pnlNoiDungChinh);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        pnlContent.add(scrollPane, BorderLayout.CENTER);
    }

    private void setupBanner() {
        pnlBanner.setLayout(new OverlayLayout(pnlBanner));
        pnlBanner.setPreferredSize(new Dimension(0, 300));
        pnlBanner.setBackground(Color.BLACK);

        // Tải ảnh banner
        Image anhBanner = null;
        ImageIcon iconBanner = ImageLoader.loadImageIcon("/imgs/lau.png");
        if (iconBanner != null) {
            anhBanner = iconBanner.getImage();
        } else {
            System.err.println("Không tìm thấy ảnh banner");
        }

        ScaledImageLabel lblAnhBannerScaled = new ScaledImageLabel(anhBanner);
        lblAnhBannerScaled.setOpaque(true);
        lblAnhBannerScaled.setBackground(new Color(60, 60, 60));
        lblAnhBannerScaled.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblAnhBannerScaled.setAlignmentY(Component.CENTER_ALIGNMENT);

        // Lớp phủ màu
        JPanel pnlLopPhu = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(139, 69, 19, 150)); // Màu nâu đỏ overlay
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        pnlLopPhu.setOpaque(false);
        pnlLopPhu.setLayout(new BoxLayout(pnlLopPhu, BoxLayout.Y_AXIS));
        pnlLopPhu.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlLopPhu.setAlignmentY(Component.CENTER_ALIGNMENT);

        // Nội dung giữa
        lblTenNhaHang.setForeground(Color.WHITE);
        lblTenNhaHang.setFont(new Font("Arial", Font.BOLD, 48));
        lblTenNhaHang.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblKhauHieu.setForeground(Color.WHITE);
        lblKhauHieu.setFont(new Font("Arial", Font.PLAIN, 20));
        lblKhauHieu.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnDatBanNgay.setBackground(PRIMARY_COLOR);
        btnDatBanNgay.setForeground(Color.WHITE);
        btnDatBanNgay.setFont(new Font("Arial", Font.BOLD, 16));
        btnDatBanNgay.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDatBanNgay.setPreferredSize(new Dimension(200, 40));
        btnDatBanNgay.setBorderPainted(false);
        btnDatBanNgay.setFocusPainted(false);

        pnlLopPhu.add(Box.createVerticalGlue());
        pnlLopPhu.add(lblTenNhaHang);
        pnlLopPhu.add(Box.createVerticalStrut(10));
        pnlLopPhu.add(lblKhauHieu);
        pnlLopPhu.add(Box.createVerticalStrut(20));
        pnlLopPhu.add(btnDatBanNgay);
        pnlLopPhu.add(Box.createVerticalGlue());

        pnlBanner.add(pnlLopPhu);
        pnlBanner.add(lblAnhBannerScaled);
    }

    private void setupFeaturedMenu() {
        pnlFeaturedMenu.setLayout(new BoxLayout(pnlFeaturedMenu, BoxLayout.Y_AXIS));
        pnlFeaturedMenu.setBackground(Color.WHITE);

        lblTieuDeMenu.setFont(new Font("Arial", Font.BOLD, 24));
        lblTieuDeMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTieuDeMenu.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Thêm padding và background cho grid món ăn
        pnlCacTheMonAn.setOpaque(false);
        pnlCacTheMonAn.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Các thẻ món ăn
        for (int i = 0; i < 4; i++) {
            JPanel pnlThe = new JPanel();
            pnlThe.setLayout(new BoxLayout(pnlThe, BoxLayout.Y_AXIS));
            pnlThe.setBackground(Color.WHITE);
            pnlThe.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
            pnlThe.setCursor(new Cursor(Cursor.HAND_CURSOR));
            pnlThe.setMaximumSize(new Dimension(200, 250));

            // Tạo ScaledImageLabel cho ảnh món ăn với chế độ cover
            Image anhMonAn = null;
            ImageIcon iconMonAn = ImageLoader.loadImageIcon("/imgs/lau.png");
            if (iconMonAn != null) {
                anhMonAn = iconMonAn.getImage();
            } else {
                System.err.println("Không tìm thấy ảnh lẩu");
            }

            ScaledImageLabel lblAnh = new ScaledImageLabel(anhMonAn);
            lblAnh.setOpaque(true);
            lblAnh.setBackground(Color.WHITE);
            lblAnh.setPreferredSize(new Dimension(200, 150));
            lblAnh.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
            lblAnh.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblAnh.setBorder(new EmptyBorder(0, 0, 0, 0));

            lblTenMonAn[i].setFont(new Font("Segoe UI", Font.BOLD, 18));
            lblTenMonAn[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            lblTenMonAn[i].setBorder(new EmptyBorder(10, 10, 5, 10));

            lblGiaMonAn[i].setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblGiaMonAn[i].setForeground(new Color(51, 51, 51));
            lblGiaMonAn[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            lblGiaMonAn[i].setBorder(new EmptyBorder(5, 10, 15, 10));

            pnlThe.add(lblAnh);
            pnlThe.add(lblTenMonAn[i]);
            pnlThe.add(lblGiaMonAn[i]);

            pnlCacTheMonAn.add(pnlThe);
        }

        pnlFeaturedMenu.add(lblTieuDeMenu);
        pnlFeaturedMenu.add(pnlCacTheMonAn);
    }

    private void setupOurStory() {
        pnlOurStory.setLayout(new BorderLayout());
        pnlOurStory.setBackground(Color.WHITE);

        // Bên trái - Văn bản câu chuyện
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

        // Bên phải - Thẻ địa chỉ
        pnlTheDiaChi.setLayout(new BoxLayout(pnlTheDiaChi, BoxLayout.Y_AXIS));
        pnlTheDiaChi.setBackground(Color.WHITE);
        pnlTheDiaChi.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(10, 10, 10, 10)));
        pnlTheDiaChi.setMaximumSize(new Dimension(250, 200));
        pnlTheDiaChi.setPreferredSize(new Dimension(250, 200));

        // Tải ảnh địa chỉ
        ImageIcon iconDiaChi = ImageLoader.loadImageIcon("/imgs/test_2.jpg");
        if (iconDiaChi != null) {
            Image anhDiaChi = iconDiaChi.getImage().getScaledInstance(230, 120, Image.SCALE_SMOOTH);
            lblAnhDiaChi.setIcon(new ImageIcon(anhDiaChi));
        } else {
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
        btnDatBanNgay.addActionListener(e -> {
            // Chuyển sang trang chọn bàn
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose();
            }
            // new ChonBan_GUI().setVisible(true);
        });
    }

    private void applyStyling() {
        // Styling đã được xử lý trong các phương thức setup 
    }

    /**
     * Label vẽ ảnh theo chế độ 'cover' (fill & crop) để ảnh lấp đầy toàn bộ vùng
     * hiển thị.
     */
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

            // draw background if opaque
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

            double scale = Math.max((double) cw / iw, (double) ch / ih); // cover
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
                UIManager.setLookAndFeel(UIManager.getLookAndFeel());
            } catch (Exception e) {
                e.printStackTrace();
            }

            new TrangChu_GUI().setVisible(true);
        });
    }
}

