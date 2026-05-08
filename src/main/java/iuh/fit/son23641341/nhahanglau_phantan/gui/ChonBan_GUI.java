    package iuh.fit.son23641341.nhahanglau_phantan.gui;
    
    import iuh.fit.son23641341.nhahanglau_phantan.control.BanAn_Ctr;
    import iuh.fit.son23641341.nhahanglau_phantan.control.PhieuDatBan_Ctr;
    import iuh.fit.son23641341.nhahanglau_phantan.dao.PhieuDat_DAO;
    import iuh.fit.son23641341.nhahanglau_phantan.entity.BanAn;
    import iuh.fit.son23641341.nhahanglau_phantan.entity.PhieuDatBan;
    import javax.swing.*;
    import javax.swing.border.EmptyBorder;
    import java.awt.*;
    import java.util.ArrayList;
    import java.util.Date;
    import java.text.SimpleDateFormat;
    import com.toedter.calendar.JDateChooser;
    
    public class ChonBan_GUI extends JPanel {
    
        private static final Color MAU_CHINH = new Color(0xE44433);
        private static final Color MAU_TRONG = new Color(0x7AB750);
        private static final Color MAU_DA_DAT = new Color(0xD94B33);
        private static final Color MAU_DANG_SU_DUNG = new Color(0xFFC107); // Màu vàng hổ phách (Amber)
        private static final Color MAU_NEN = new Color(0xF5F5F5);
        private static final Color MAU_LOAI_THUONG = new Color(0x4A90E2);
        private static final Color MAU_LOAI_VIP = new Color(0xF5A623);
        private static final Color MAU_LOAI_DELUXE = new Color(0x9013FE);
    
    
        // Controller
        private BanAn_Ctr banAnCtr;
    
        // Panels chính
        private JPanel pnlHeader, pnlPhanBan;
        private JTabbedPane tabKhuVuc;
    
        // Phần header
        private JLabel lblTieuDeTrang;
    
        // Phần bàn
        private JPanel pnlChiDan, pnlTimKiem, pnlChonNgay;
        private JTextField txtTimKiem;
        private JButton btnIconTimKiem;
        private JPanel[] pnlCacTheBan;
        private JLabel[] lblSoBan, lblThongTinBan, lblLoaiBan;
        private JComboBox<String> cboLocLoaiBan;
        private JDateChooser dateChooserNgayDat;
        private String ngayDatDaChon; // Lưu ngày đã chọn để truyền sang PhieuDat_GUI
    
        private PhieuDat_DAO phieuDatDAO;
        private ArrayList<PhieuDatBan> danhSachPhieuTheoNgay;
    
        public ChonBan_GUI() {
            banAnCtr = BanAn_Ctr.getInstance(); // Sử dụng Singleton
            phieuDatDAO = new PhieuDat_DAO();
            initializeComponents();
            setupLayout();
            setupEventHandlers();
        }
    
        private void initializeComponents() {
            pnlHeader = new JPanel();
            pnlPhanBan = new JPanel();
    
            // Khởi tạo header
    
            lblTieuDeTrang = new JLabel("CHỌN BÀN");
    
            // Khởi tạo phần bàn
            pnlChiDan = new JPanel();
            pnlTimKiem = new JPanel();
            pnlChonNgay = new JPanel();
            txtTimKiem = new JTextField();
            btnIconTimKiem = new JButton();
            tabKhuVuc = new JTabbedPane();
            tabKhuVuc.setFont(new Font("Arial", Font.BOLD, 16));
    
            // Khởi tạo JDateChooser chọn ngày đặt
            dateChooserNgayDat = new JDateChooser();
            dateChooserNgayDat.setDateFormatString("dd/MM/yyyy");
    
            // Đặt ngày hiện tại
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
            calendar.set(java.util.Calendar.MINUTE, 0);
            calendar.set(java.util.Calendar.SECOND, 0);
            calendar.set(java.util.Calendar.MILLISECOND, 0);
            Date ngayHienTai = calendar.getTime();
            dateChooserNgayDat.setDate(ngayHienTai);
    
            // Đặt ngày tối thiểu là ngày hiện tại (không cho chọn ngày quá khứ)
            dateChooserNgayDat.setMinSelectableDate(ngayHienTai);
    
            // Mặc định là ngày hôm nay
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            ngayDatDaChon = sdf.format(ngayHienTai);
    
            // Khởi tạo combo box lọc loại bàn
            String[] loaiBan = { "Tất cả", "Thường", "VIP", "Deluxe" };
            cboLocLoaiBan = new JComboBox<>(loaiBan);
        }
    
        private void setupLayout() {
            setLayout(new BorderLayout());

            setupHeader();
            setupTableSection();

            JPanel pnlContentWrapper = new JPanel(new BorderLayout());
            pnlContentWrapper.add(pnlHeader, BorderLayout.NORTH);
            pnlContentWrapper.add(pnlPhanBan, BorderLayout.CENTER);

            add(pnlContentWrapper, BorderLayout.CENTER);
        }
    
        private void setupHeader() {
            pnlHeader.setLayout(new BorderLayout());
            pnlHeader.setPreferredSize(new Dimension(0, 80));
            pnlHeader.setBackground(Color.WHITE);
            pnlHeader.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                    new EmptyBorder(15, 20, 15, 20)));
    
            JPanel pnlTieuDe = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
            pnlTieuDe.setBackground(Color.WHITE);
            lblTieuDeTrang.setFont(new Font("Arial", Font.BOLD, 24));
            lblTieuDeTrang.setForeground(MAU_CHINH);
            pnlTieuDe.add(lblTieuDeTrang);
    
            pnlHeader.add(pnlTieuDe, BorderLayout.WEST);
        }
    
        private void setupTableSection() {
            pnlPhanBan.setLayout(new BorderLayout());
            pnlPhanBan.setBackground(MAU_NEN);
            pnlPhanBan.setBorder(new EmptyBorder(20, 20, 0, 20));
    
            // Phần trên (chỉ dẫn + tìm kiếm)
            JPanel pnlTren = new JPanel(new BorderLayout());
            pnlTren.setBackground(MAU_NEN);
            pnlTren.setBorder(new EmptyBorder(0, 0, 20, 0));
    
            pnlChiDan.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 15));
            pnlChiDan.setBackground(MAU_NEN);
    
            // Thêm chỉ dẫn loại bàn
            JPanel pnlKhoangCach = new JPanel();
            pnlKhoangCach.setPreferredSize(new Dimension(5, 1));
            pnlKhoangCach.setBackground(MAU_NEN);
            pnlChiDan.add(pnlKhoangCach);
    
            pnlChiDan.add(taoChiDan("TRỐNG", MAU_TRONG));
            pnlChiDan.add(taoChiDan("ĐÃ ĐẶT", MAU_DA_DAT));
            pnlChiDan.add(taoChiDan("ĐANG DÙNG", MAU_DANG_SU_DUNG));
            
            pnlChiDan.add(taoChiDan("THƯỜNG", MAU_LOAI_THUONG));
            pnlChiDan.add(taoChiDan("VIP", MAU_LOAI_VIP));
            pnlChiDan.add(taoChiDan("DELUXE", MAU_LOAI_DELUXE));
    
            // Setup panel chọn ngày
            pnlChonNgay.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
            pnlChonNgay.setBackground(MAU_NEN);
            pnlChonNgay.setBorder(new EmptyBorder(0, 0, 0, 30)); // Thêm khoảng cách bên phải
    
            JLabel lblChonNgay = new JLabel("Chọn ngày:");
            lblChonNgay.setFont(new Font("Arial", Font.BOLD, 16));
            lblChonNgay.setForeground(MAU_CHINH);
    
            // Thiết lập giao diện cho JDateChooser
            dateChooserNgayDat.setPreferredSize(new Dimension(160, 35));
            dateChooserNgayDat.setFont(new Font("Arial", Font.PLAIN, 14));
            dateChooserNgayDat.getJCalendar().setTodayButtonVisible(true);
            dateChooserNgayDat.getJCalendar().setNullDateButtonVisible(false);
            dateChooserNgayDat.getJCalendar().setWeekOfYearVisible(false);
    
            pnlChonNgay.add(lblChonNgay);
            pnlChonNgay.add(dateChooserNgayDat);
    
            pnlTimKiem.setLayout(new FlowLayout(FlowLayout.RIGHT, 6, 5));
            pnlTimKiem.setBackground(MAU_NEN);
    
            // Thêm combo box lọc loại bàn
            JLabel lblLoc = new JLabel("Loại bàn:");
            lblLoc.setFont(new Font("Arial", Font.BOLD, 14));
            cboLocLoaiBan.setPreferredSize(new Dimension(110, 30));
            cboLocLoaiBan.setFont(new Font("Arial", Font.PLAIN, 14));
            pnlTimKiem.add(lblLoc);
            pnlTimKiem.add(cboLocLoaiBan);
            txtTimKiem.setPreferredSize(new Dimension(220, 30));
            txtTimKiem.setFont(new Font("Arial", Font.PLAIN, 14));
            txtTimKiem.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.GRAY, 1),
                    new EmptyBorder(5, 10, 5, 10)));
    
            // Thêm placeholder cho ô tìm kiếm
            txtTimKiem.setForeground(Color.GRAY);
            txtTimKiem.setText("Nhập số bàn hoặc số chỗ ngồi...");
    
            // Xử lý focus để hiển thị/ẩn placeholder
            txtTimKiem.addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusGained(java.awt.event.FocusEvent evt) {
                    if (txtTimKiem.getText().equals("Nhập số bàn hoặc số chỗ ngồi...")) {
                        txtTimKiem.setText("");
                        txtTimKiem.setForeground(Color.BLACK);
                    }
                }
    
                @Override
                public void focusLost(java.awt.event.FocusEvent evt) {
                    if (txtTimKiem.getText().trim().isEmpty()) {
                        txtTimKiem.setForeground(Color.GRAY);
                        txtTimKiem.setText("Nhập số bàn hoặc số chỗ ngồi...");
                    }
                }
            });
    
            // Tải icon tìm kiếm
            try {
                ImageIcon iconTimKiem = new ImageIcon(getClass().getResource("/imgs/search_icon.png"));
                Image anhTimKiem = iconTimKiem.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                btnIconTimKiem.setIcon(new ImageIcon(anhTimKiem));
            } catch (Exception e) {
                btnIconTimKiem.setText("🔍");
                btnIconTimKiem.setFont(new Font("Arial", Font.PLAIN, 16));
            }
    
            btnIconTimKiem.setPreferredSize(new Dimension(40, 30));
            btnIconTimKiem.setBackground(MAU_CHINH);
            btnIconTimKiem.setForeground(Color.WHITE);
            btnIconTimKiem.setBorderPainted(false);
            btnIconTimKiem.setFocusPainted(false);
            pnlTimKiem.add(txtTimKiem);
            pnlTimKiem.add(btnIconTimKiem);
    
            pnlTren.add(pnlChiDan, BorderLayout.WEST);
            pnlTren.add(pnlChonNgay, BorderLayout.CENTER); // Thêm panel chọn ngày
            pnlTren.add(pnlTimKiem, BorderLayout.EAST);
    
            setupTableGrid();
    
            pnlPhanBan.add(pnlTren, BorderLayout.NORTH);
            pnlPhanBan.add(tabKhuVuc, BorderLayout.CENTER);
        }
    

        private JPanel taoChiDan(String text, Color mau) {
            JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            pnl.setBackground(MAU_NEN);
    
            JLabel lblMau = new JLabel();
            lblMau.setPreferredSize(new Dimension(15, 15));
            lblMau.setBackground(mau);
            lblMau.setOpaque(true);
            lblMau.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    
            JLabel lblText = new JLabel(text);
            lblText.setFont(new Font("Arial", Font.BOLD, 12));
    
            pnl.add(lblMau);
            pnl.add(lblText);
            return pnl;
        }
    
        private void setupEventHandlers() {
            btnIconTimKiem.addActionListener(e -> {
                String textTimKiem = txtTimKiem.getText().trim();
                // Kiểm tra nếu không phải placeholder và không rỗng
                if (!textTimKiem.isEmpty() && !textTimKiem.equals("Nhập số bàn hoặc số chỗ ngồi...")) {
                    timKiemBan(textTimKiem);
                } else {
                    hienThiTatCaBan();
                }
            });
    
            // Tìm kiếm khi nhấn Enter
            txtTimKiem.addActionListener(e -> {
                String textTimKiem = txtTimKiem.getText().trim();
                // Kiểm tra nếu không phải placeholder và không rỗng
                if (!textTimKiem.isEmpty() && !textTimKiem.equals("Nhập số bàn hoặc số chỗ ngồi...")) {
                    timKiemBan(textTimKiem);
                } else {
                    hienThiTatCaBan();
                }
            });
    
            // Lọc theo loại bàn
            cboLocLoaiBan.addActionListener(e -> {
                String loaiDuocChon = (String) cboLocLoaiBan.getSelectedItem();
                locBanTheoLoai(loaiDuocChon);
            });
    
            // Lọc bàn khi chọn ngày
            dateChooserNgayDat.addPropertyChangeListener("date", e -> {
                Date ngayDuocChon = dateChooserNgayDat.getDate();
                if (ngayDuocChon != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    ngayDatDaChon = sdf.format(ngayDuocChon);
                    // Làm mới danh sách bàn theo ngày đã chọn
                    refreshData();
                }
            });
        }
    
        private void timKiemBan(String tuKhoa) {
            ArrayList<BanAn> tatCaBan = banAnCtr.layTatCaBan();
            ArrayList<BanAn> danhSachBanTimThay = new ArrayList<>();
    
            // Tìm kiếm theo mã bàn hoặc số chỗ
            for (BanAn ban : tatCaBan) {
                // Tìm theo mã bàn (001, 002...)
                if (ban.getMaBanFormatted().contains(tuKhoa) ||
                        String.valueOf(ban.getMaBan()).contains(tuKhoa) ||
                        ("Bàn " + ban.getMaBanFormatted()).toLowerCase().contains(tuKhoa.toLowerCase()) ||
                        (ban.getSoCho() + " chỗ").contains(tuKhoa)) {
                    danhSachBanTimThay.add(ban);
                }
            }
    
            if (danhSachBanTimThay.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy bàn với từ khóa: " + tuKhoa,
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
                setupTableGrid();
                return;
            }
    
            // Hiển thị kết quả tìm kiếm trong một tab tạm thời hoặc lọc lại các tab
            tabKhuVuc.removeAll();
            JPanel pnlResult = new JPanel(new GridLayout(0, 4, 20, 20));
            pnlResult.setBackground(MAU_NEN);
            pnlResult.setBorder(new EmptyBorder(20, 20, 20, 20));
            
            for (BanAn ban : danhSachBanTimThay) {
                pnlResult.add(taoTheBan(ban));
            }
            
            // Cố định độ cao kết quả tìm kiếm
            int soHangRes = (danhSachBanTimThay.size() + 3) / 4;
            pnlResult.setPreferredSize(new Dimension(1000, Math.max(200, soHangRes * 220)));
            
            JScrollPane scroll = new JScrollPane(pnlResult);
            scroll.setBorder(null);
            
            // Wrapper để không bị dãn
            JPanel pnlWrapper = new JPanel(new BorderLayout());
            pnlWrapper.add(pnlResult, BorderLayout.NORTH);
            scroll.setViewportView(pnlWrapper);
            
            tabKhuVuc.addTab("KẾT QUẢ TÌM KIẾM", scroll);
        }
    
        private void hienThiTatCaBan() {
            // Reset combo box về "Tất cả"
            cboLocLoaiBan.setSelectedIndex(0);
            refreshData();
        }

        /**
         * Làm mới dữ liệu và hiển thị lại lưới bàn
         */
        public void refreshData() {
            String loaiFilter = (cboLocLoaiBan != null) ? (String)cboLocLoaiBan.getSelectedItem() : "Tất cả";
            setupTableGrid(loaiFilter);
            tabKhuVuc.revalidate();
            tabKhuVuc.repaint();
        }
    
        private void setupTableGrid() {
            setupTableGrid("Tất cả");
        }
    
        private void setupTableGrid(String loaiFilter) {
            tabKhuVuc.removeAll();
            
            // Lấy danh sách bàn từ controller
            banAnCtr.loadBanFromDB();
            ArrayList<BanAn> tatCaBan = banAnCtr.layTatCaBan();
            danhSachPhieuTheoNgay = phieuDatDAO.getPhieuDatByNgay(ngayDatDaChon);
    
            // Phân loại bàn theo khu vực
            String[] khuVucs = {"Trong nhà", "Trên lầu", "Ngoài trời"};
            Color[] mauNens = {new Color(0xFDFCF0), new Color(0xF0F7FD), new Color(0xF0FDF4)}; // Vàng nhạt, Xanh dương nhạt, Xanh lá nhạt
            
            for (int k = 0; k < khuVucs.length; k++) {
                String kv = khuVucs[k];
                JPanel pnlLuoi = new JPanel();
                final int soCot = 4;
                pnlLuoi.setLayout(new GridLayout(0, soCot, 20, 20));
                pnlLuoi.setBackground(mauNens[k]);
                pnlLuoi.setBorder(new EmptyBorder(20, 20, 20, 20));
                
                // Lọc bàn theo khu vực VÀ loại bàn
                int count = 0;
                for (BanAn ban : tatCaBan) {
                    if (kv.equals(ban.getKhuVuc())) {
                        if (loaiFilter.equals("Tất cả") || loaiFilter.equalsIgnoreCase(ban.getLoaiBan())) {
                            pnlLuoi.add(taoTheBan(ban));
                            count++;
                        }
                    }
                }
                
                // Cố định độ cao để bàn không bị "dẹp"
                int soHang = (count + soCot - 1) / soCot;
                pnlLuoi.setPreferredSize(new Dimension(1000, Math.max(200, soHang * 220)));
                
                // Ngăn chặn việc dãn thẻ bàn khi số lượng bàn ít
                JPanel pnlWrapper = new JPanel(new BorderLayout());
                pnlWrapper.setBackground(mauNens[k]);
                pnlWrapper.add(pnlLuoi, BorderLayout.NORTH);
                
                JScrollPane scroll = new JScrollPane(pnlWrapper);
                scroll.setBorder(null);
                scroll.getVerticalScrollBar().setUnitIncrement(16);
                tabKhuVuc.addTab(kv.toUpperCase() + " (" + count + ")", scroll);
            }
        }
    
        // Phương thức tiện ích để tạo thẻ bàn
        private JPanel taoTheBan(BanAn ban) {
            JPanel pnlThe = new JPanel(new BorderLayout());
            pnlThe.setBackground(Color.WHITE);
            pnlThe.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                    new EmptyBorder(10, 10, 10, 10)));
    
            // Hiển thị số bàn
            JLabel lblSoBanTemp = new JLabel("Bàn " + ban.getMaBanFormatted());
            lblSoBanTemp.setFont(new Font("Arial", Font.BOLD, 14));
            lblSoBanTemp.setForeground(Color.BLACK);
            lblSoBanTemp.setHorizontalAlignment(SwingConstants.CENTER);
    
            // Hiển thị thông tin bàn
            String thongTinBan = ban.getSoCho() + " chỗ";
            Color mauTrangThai;
    
            ArrayList<PhieuDatBan> phieuTheoNgay = phieuDatDAO.getPhieuDatByNgay(ngayDatDaChon);
            int soPhieuDat = 0;
            boolean dangSuDung = false;
            
            for (PhieuDatBan phieu : phieuTheoNgay) {
                // Chỉ tính các phiếu CHƯA thanh toán và CHƯA hủy
                if (!"Đã hủy".equals(phieu.getTrangThai()) && !"Đã thanh toán".equals(phieu.getTrangThai())) {
                    ArrayList<Integer> danhSachBanPhieu = (ArrayList<Integer>) phieu.getDanhSachBan();
                    if (danhSachBanPhieu != null && danhSachBanPhieu.contains(ban.getMaBan())) {
                        soPhieuDat++;
                        if ("Đang sử dụng".equals(phieu.getTrangThai())) {
                            dangSuDung = true;
                        }
                    }
                }
            }
    
            final int MAX_KHUNG_GIO = 6;
            boolean isFullKhungGio = (soPhieuDat >= MAX_KHUNG_GIO);
    
            if (dangSuDung) {
                mauTrangThai = MAU_DANG_SU_DUNG;
            } else if (soPhieuDat > 0) {
                mauTrangThai = MAU_DA_DAT;
            } else {
                mauTrangThai = MAU_TRONG;
            }
    
            JLabel lblThongTinBanTemp = new JLabel(thongTinBan);
            lblThongTinBanTemp.setFont(new Font("Arial", Font.BOLD, 12));
            lblThongTinBanTemp.setForeground(Color.WHITE);
            lblThongTinBanTemp.setHorizontalAlignment(SwingConstants.CENTER);
            lblThongTinBanTemp.setOpaque(true);
            lblThongTinBanTemp.setBackground(mauTrangThai);
            lblThongTinBanTemp.setBorder(new EmptyBorder(8, 5, 8, 5));
    
            // Hiển thị loại bàn
            Color mauLoaiBan;
            switch (ban.getLoaiBan()) {
                case "VIP":
                    mauLoaiBan = MAU_LOAI_VIP;
                    break;
                case "Deluxe":
                    mauLoaiBan = MAU_LOAI_DELUXE;
                    break;
                default:
                    mauLoaiBan = MAU_LOAI_THUONG;
            }
    
            JLabel lblLoaiBanTemp = new JLabel(ban.getLoaiBan());
            lblLoaiBanTemp.setFont(new Font("Arial", Font.BOLD, 11));
            lblLoaiBanTemp.setForeground(Color.WHITE);
            lblLoaiBanTemp.setHorizontalAlignment(SwingConstants.CENTER);
            lblLoaiBanTemp.setOpaque(true);
            lblLoaiBanTemp.setBackground(mauLoaiBan);
            lblLoaiBanTemp.setBorder(new EmptyBorder(5, 5, 5, 5));
    
            pnlThe.add(lblSoBanTemp, BorderLayout.NORTH);
            pnlThe.add(lblThongTinBanTemp, BorderLayout.CENTER);
            pnlThe.add(lblLoaiBanTemp, BorderLayout.SOUTH);
    
            // Thêm sự kiện click
            final int maBan = ban.getMaBan();
            pnlThe.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            java.awt.event.MouseAdapter mouseAdapter = new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    BanAn banDuocChon = banAnCtr.timBanTheoMa(maBan);
                    if (banDuocChon != null) {
                        Window window = SwingUtilities.getWindowAncestor(ChonBan_GUI.this);
                        Component gui = GUIManager.getInstance().switchToGUI(PhieuDat_GUI.class, window);
                        if (gui instanceof PhieuDat_GUI) {
                            ((PhieuDat_GUI) gui).setData(maBan, ngayDatDaChon, null);
                        }
                    }
                }
    
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    pnlThe.setBackground(new Color(245, 245, 245));
                }
    
                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    pnlThe.setBackground(Color.WHITE);
                }
            };
            
            pnlThe.addMouseListener(mouseAdapter);
            // Quan trọng: Thêm listener vào tất cả component con để không bị chặn click
            for (Component c : pnlThe.getComponents()) {
                c.addMouseListener(mouseAdapter);
                if (c instanceof Container) {
                    for (Component cc : ((Container) c).getComponents()) {
                        cc.addMouseListener(mouseAdapter);
                    }
                }
            }
    
            return pnlThe;
        }
    
        private void locBanTheoLoai(String loaiBan) {
            refreshData();
        }
    
        @Override
    public void setVisible(boolean b) {
        if (b) {
            lamMoiDuLieu();
        }
        super.setVisible(b);
    }

    public void lamMoiDuLieu() {
        refreshData();
    }

    public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                try {
                    UIManager.setLookAndFeel(UIManager.getLookAndFeel());
                } catch (Exception e) {
                    e.printStackTrace();
                }
    
                new ChonBan_GUI().setVisible(true);
            });
        }
    }
