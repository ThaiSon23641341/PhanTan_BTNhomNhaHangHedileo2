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

public class ChonBan_GUI extends JFrame {

    private static final Color MAU_CHINH = new Color(0xE44433);
    private static final Color MAU_TRONG = new Color(0x7AB750);
    private static final Color MAU_DANG_SU_DUNG = new Color(0xEC893E);
    private static final Color MAU_DA_DAT = new Color(0xD94B33);
    private static final Color MAU_NEN = new Color(0xF5F5F5);
    private static final Color MAU_LOAI_THUONG = new Color(0x4A90E2);
    private static final Color MAU_LOAI_VIP = new Color(0xF5A623);
    private static final Color MAU_LOAI_DELUXE = new Color(0x9013FE);

    private static final int TONG_SO_BAN = 20;

    // Controller
    private BanAn_Ctr banAnCtr;

    // Panels chính
    private JPanel pnlMain, pnlHeader, pnlPhanBan;
    private SideBar_GUI sidebar;

    // Phần header
    private JLabel lblAvatar, lblTieuDeTrang;

    // Phần bàn
    private JPanel pnlChiDanTrangThai, pnlTimKiem, pnlLuoiBan, pnlChonNgay;
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
        pnlMain = new JPanel(new BorderLayout());
        pnlHeader = new JPanel();
        pnlPhanBan = new JPanel();

        // Khởi tạo header

        lblAvatar = new JLabel();
        lblTieuDeTrang = new JLabel("CHỌN BÀN");

        // Khởi tạo phần bàn
        pnlChiDanTrangThai = new JPanel();
        pnlTimKiem = new JPanel();
        pnlChonNgay = new JPanel();
        txtTimKiem = new JTextField();
        btnIconTimKiem = new JButton();
        pnlLuoiBan = new JPanel();

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

        // Khởi tạo mảng bàn
        pnlCacTheBan = new JPanel[TONG_SO_BAN];
        lblSoBan = new JLabel[TONG_SO_BAN];
        lblThongTinBan = new JLabel[TONG_SO_BAN];
        lblLoaiBan = new JLabel[TONG_SO_BAN];
        for (int i = 0; i < TONG_SO_BAN; i++) {
            pnlCacTheBan[i] = new JPanel();
            lblSoBan[i] = new JLabel("Bàn " + String.format("%02d", i + 1));
            lblThongTinBan[i] = new JLabel();
            lblLoaiBan[i] = new JLabel();
        }

        // Khởi tạo combo box lọc loại bàn
        String[] loaiBan = { "Tất cả", "Thường", "VIP", "Deluxe" };
        cboLocLoaiBan = new JComboBox<>(loaiBan);
    }

    private void setupLayout() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("HÈ DÌ LEO - Chọn Bàn");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        add(pnlMain, BorderLayout.CENTER);

        // Khởi tạo sidebar và đánh dấu Đặt Bàn
        sidebar = new SideBar_GUI();
        sidebar.setMauNutKhiChon("Đặt Bàn");

        setupHeader();
        setupTableSection();

        JPanel pnlContentWrapper = new JPanel(new BorderLayout());
        pnlContentWrapper.add(pnlHeader, BorderLayout.NORTH);
        pnlContentWrapper.add(pnlPhanBan, BorderLayout.CENTER);

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

        // Phần trên (chỉ dẫn trạng thái + tìm kiếm)
        JPanel pnlTren = new JPanel(new BorderLayout());
        pnlTren.setBackground(MAU_NEN);
        pnlTren.setBorder(new EmptyBorder(0, 0, 20, 0));

        pnlChiDanTrangThai.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 15));
        pnlChiDanTrangThai.setBackground(MAU_NEN);
        pnlChiDanTrangThai.add(taoChiDanTrangThai("TRỐNG", MAU_TRONG));
        pnlChiDanTrangThai.add(taoChiDanTrangThai("ĐÃ ĐẶT FULL", MAU_DA_DAT));
        pnlChiDanTrangThai.add(taoChiDanTrangThai("ĐANG SỬ DỤNG", MAU_DANG_SU_DUNG));

        // Thêm chỉ dẫn loại bàn
        JPanel pnlKhoangCach = new JPanel();
        pnlKhoangCach.setPreferredSize(new Dimension(5, 1));
        pnlKhoangCach.setBackground(MAU_NEN);
        pnlChiDanTrangThai.add(pnlKhoangCach);

        pnlChiDanTrangThai.add(taoChiDanTrangThai("THƯỜNG", MAU_LOAI_THUONG));
        pnlChiDanTrangThai.add(taoChiDanTrangThai("VIP", MAU_LOAI_VIP));
        pnlChiDanTrangThai.add(taoChiDanTrangThai("DELUXE", MAU_LOAI_DELUXE));

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

        pnlTren.add(pnlChiDanTrangThai, BorderLayout.WEST);
        pnlTren.add(pnlChonNgay, BorderLayout.CENTER); // Thêm panel chọn ngày
        pnlTren.add(pnlTimKiem, BorderLayout.EAST);

        setupTableGrid();

        JScrollPane scrollPane = new JScrollPane(pnlLuoiBan);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        pnlPhanBan.add(pnlTren, BorderLayout.NORTH);
        pnlPhanBan.add(scrollPane, BorderLayout.CENTER);
    }

    private void setupTableGrid() {
        final int soCot = 4;
        final int soHang = (TONG_SO_BAN + soCot - 1) / soCot;
        pnlLuoiBan.setLayout(new GridLayout(soHang, soCot, 15, 15));
        pnlLuoiBan.setBackground(MAU_NEN);
        pnlLuoiBan.setPreferredSize(new Dimension(1000, soHang * 200));

        // Lấy danh sách bàn từ controller
        // Luôn load lại danh sách bàn từ DB để đảm bảo đồng bộ trạng thái
        banAnCtr.loadBanFromDB();
        ArrayList<BanAn> danhSachBan = banAnCtr.layTatCaBan();

        // Truyền trực tiếp ngày dd/MM/yyyy vào DAO, không chuyển đổi
        danhSachPhieuTheoNgay = phieuDatDAO.getPhieuDatByNgay(ngayDatDaChon);

        for (int i = 0; i < danhSachBan.size(); i++) {
            BanAn ban = danhSachBan.get(i);
            // Không cập nhật trạng thái BanAn, chỉ dùng trangThaiHienThi để render màu và
            // tooltip
            JPanel pnlThe = new JPanel(new BorderLayout());
            pnlThe.setBackground(Color.WHITE);
            pnlThe.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                    new EmptyBorder(10, 10, 10, 10)));

            // Hiển thị số bàn (format 001, 002...)
            JPanel pnlSoBan = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            pnlSoBan.setBackground(Color.WHITE);
            lblSoBan[i].setText("Bàn " + ban.getMaBanFormatted());
            lblSoBan[i].setFont(new Font("Arial", Font.BOLD, 14));
            lblSoBan[i].setForeground(Color.BLACK);
            lblSoBan[i].setHorizontalAlignment(SwingConstants.CENTER);

            // Đếm số phiếu đặt của bàn này trong ngày (chỉ phiếu "Đặt trước", không tính
            // "Đã xác nhận" và "Đã hủy")
            int soPhieuDat = 0;
            for (PhieuDatBan phieu : danhSachPhieuTheoNgay) {
                ArrayList<Integer> danhSachBanPhieu = phieu.getDanhSachBan();
                if (danhSachBanPhieu != null && danhSachBanPhieu.contains(ban.getMaBan())
                        && "Đặt trước".equals(phieu.getTrangThai())) {
                    soPhieuDat++;
                }
            }

            // Thêm chấm đỏ (số lượng = số phiếu đặt)
            if (soPhieuDat > 0) {
                // Tạo chuỗi chấm đỏ theo số phiếu (tối đa 6)
                StringBuilder chamDo = new StringBuilder();
                for (int j = 0; j < Math.min(soPhieuDat, 6); j++) {
                    chamDo.append("●");
                }

                JLabel lblChamDo = new JLabel(chamDo.toString());
                lblChamDo.setFont(new Font("Arial", Font.BOLD, 16));
                lblChamDo.setForeground(Color.RED);
                pnlSoBan.add(lblSoBan[i]);
                pnlSoBan.add(lblChamDo);
            } else {
                pnlSoBan.add(lblSoBan[i]);
            }

            // Hiển thị thông tin bàn (số chỗ + trạng thái)
            String thongTinBan = ban.getSoCho() + " chỗ";
            Color mauTrangThai;

            // Kiểm tra trạng thái bàn theo ngày đã chọn (ưu tiên DB)
            String trangThaiHienThi = "Trống";
            String tooltip = null;
            PhieuDatBan phieuDB = null;
            ArrayList<Integer> danhSachBanPhieu = null;

            // Kiểm tra xem bàn có full khung giờ không (6 khung giờ/ngày)
            final int MAX_KHUNG_GIO = 6; // 10:00-12:00, 12:00-14:00, 14:00-16:00, 16:00-18:00, 18:00-20:00, 20:00-22:00
            boolean isFullKhungGio = (soPhieuDat >= MAX_KHUNG_GIO);

            // Nếu là ngày hiện tại, ưu tiên check bàn đang sử dụng hoặc phiếu đã xác nhận
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String ngayHienTai = sdf.format(new Date());

            if (ngayDatDaChon.equals(ngayHienTai)) {
                // Check xem có phiếu "Đã xác nhận" không
                for (PhieuDatBan phieu : danhSachPhieuTheoNgay) {
                    danhSachBanPhieu = phieu.getDanhSachBan();
                    if (danhSachBanPhieu != null && danhSachBanPhieu.contains(ban.getMaBan())
                            && "Đã xác nhận".equals(phieu.getTrangThai())) {
                        trangThaiHienThi = "Đang sử dụng";
                        phieuDB = phieu;
                        tooltip = "Khách: " + phieu.getTenKhachDat() +
                                " | SĐT: " + phieu.getSdtDat() +
                                (phieu.getEmailDat() != null && !phieu.getEmailDat().isEmpty()
                                        ? " | Email: " + phieu.getEmailDat()
                                        : "");
                        break;
                    }
                }

                // Nếu chưa tìm thấy phiếu đã xác nhận, check trạng thái bàn
                if (!"Đang sử dụng".equals(trangThaiHienThi) && ban.getTrangThai().equals("Đang sử dụng")) {
                    trangThaiHienThi = "Đang sử dụng";
                }
            }

            // Nếu chưa phải "Đang sử dụng", check phiếu "Đặt trước"
            if (!"Đang sử dụng".equals(trangThaiHienThi)) {
                for (PhieuDatBan phieu : danhSachPhieuTheoNgay) {
                    danhSachBanPhieu = phieu.getDanhSachBan();
                    if (danhSachBanPhieu != null && danhSachBanPhieu.contains(ban.getMaBan())
                            && "Đặt trước".equals(phieu.getTrangThai())) {
                        if (!isFullKhungGio) {
                            // Nếu chưa full khung giờ, chỉ đánh dấu có phiếu đặt (không đổi màu)
                            trangThaiHienThi = "Có phiếu đặt";
                        } else {
                            // Nếu đã full khung giờ, đổi trạng thái thành Đặt trước
                            trangThaiHienThi = "Đặt trước";
                        }
                        phieuDB = phieu;
                        tooltip = "Khách: " + phieu.getTenKhachDat() +
                                " | SĐT: " + phieu.getSdtDat() +
                                (phieu.getEmailDat() != null && !phieu.getEmailDat().isEmpty()
                                        ? " | Email: " + phieu.getEmailDat()
                                        : "");
                        break;
                    }
                }
            }

            // Chọn màu theo trạng thái
            // - Trống / Có phiếu đặt (chưa full): Màu xanh lá (có chấm đỏ nếu có phiếu)
            // - Đặt trước (full khung giờ): Màu đỏ
            // - Đang sử dụng: Màu cam
            switch (trangThaiHienThi) {
                case "Trống":
                case "Có phiếu đặt":
                    if (isFullKhungGio) {
                        mauTrangThai = MAU_DA_DAT; // Màu đỏ
                        tooltip = "Bàn đã đầy (6/6 khung giờ)";
                    } else {
                        mauTrangThai = MAU_TRONG; // Xanh lá
                    }
                    break;
                case "Đang sử dụng":
                    mauTrangThai = MAU_DANG_SU_DUNG; // Cam
                    break;
                case "Đặt trước":
                    mauTrangThai = MAU_DA_DAT; // Đỏ (chỉ khi full khung giờ)
                    tooltip = "Bàn đã đầy (6/6 khung giờ)";
                    break;
                default:
                    mauTrangThai = MAU_NEN;
            }

            lblThongTinBan[i].setText(thongTinBan);
            lblThongTinBan[i].setFont(new Font("Arial", Font.BOLD, 12));
            lblThongTinBan[i].setForeground(Color.WHITE);
            lblThongTinBan[i].setHorizontalAlignment(SwingConstants.CENTER);
            lblThongTinBan[i].setOpaque(true);
            lblThongTinBan[i].setBackground(mauTrangThai);
            lblThongTinBan[i].setBorder(new EmptyBorder(8, 5, 8, 5));
            if (tooltip != null) {
                lblThongTinBan[i].setToolTipText(tooltip);
            } else {
                lblThongTinBan[i].setToolTipText(null);
            }

            // Cập nhật trạng thái bàn vào database nếu khác trạng thái hiện tại
            String trangThaiDB = ban.getTrangThai();
            String trangThaiCanCapNhat;
            if ("Đang sử dụng".equals(trangThaiHienThi)) {
                trangThaiCanCapNhat = "Đang sử dụng";
            } else if ("Đặt trước".equals(trangThaiHienThi) || isFullKhungGio) {
                trangThaiCanCapNhat = "Đặt trước";
            } else {
                trangThaiCanCapNhat = "Trống";
            }
            // Debug print trạng thái update DB
            System.out.println("[" + trangThaiCanCapNhat + "]");
            System.out.println("[DEBUG] Ban " + ban.getMaBan() + ": DB='" + trangThaiDB + "', UI='" + trangThaiCanCapNhat + "'");
            if (!trangThaiDB.equals(trangThaiCanCapNhat)) {
                System.out.println("[DEBUG] -> Gọi update DB cho bàn " + ban.getMaBan() + " sang '" + trangThaiCanCapNhat + "'");
                banAnCtr.capNhatTrangThai(ban.getMaBan(), trangThaiCanCapNhat);
                ban.setTrangThai(trangThaiCanCapNhat); // đồng bộ lại trong bộ nhớ
            }

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

            lblLoaiBan[i].setText(ban.getLoaiBan());
            lblLoaiBan[i].setFont(new Font("Arial", Font.BOLD, 11));
            lblLoaiBan[i].setForeground(Color.WHITE);
            lblLoaiBan[i].setHorizontalAlignment(SwingConstants.CENTER);
            lblLoaiBan[i].setOpaque(true);
            lblLoaiBan[i].setBackground(mauLoaiBan);
            lblLoaiBan[i].setBorder(new EmptyBorder(5, 5, 5, 5));

            pnlThe.add(pnlSoBan, BorderLayout.NORTH);
            pnlThe.add(lblThongTinBan[i], BorderLayout.CENTER);
            pnlThe.add(lblLoaiBan[i], BorderLayout.SOUTH);

            pnlCacTheBan[i] = pnlThe;

            // Thêm sự kiện click vào bàn và label trạng thái
            final int maBan = ban.getMaBan();
            final PhieuDatBan phieuDatBanDB = phieuDB;
            final ArrayList<Integer> danhSachBanDaChon = (phieuDB != null) ? phieuDB.getDanhSachBan() : null;
            final boolean isBanFull = isFullKhungGio; // Lưu trạng thái full để dùng trong listener
            
            java.awt.event.MouseListener moListener = new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    BanAn banDuocChon = banAnCtr.timBanTheoMa(maBan);
                    if (banDuocChon != null) {
                        PhieuDatBan_Ctr phieuDatCtr = PhieuDatBan_Ctr.getInstance();
                        dispose();
                        // Mở form - Truyền thêm isBanFull để disable nút "Xác nhận đặt" nếu full
                        new PhieuDat_GUI(maBan, banAnCtr, phieuDatCtr, ngayDatDaChon, isBanFull).setVisible(true);
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
            pnlThe.setCursor(new Cursor(Cursor.HAND_CURSOR));
            pnlThe.addMouseListener(moListener);
            lblThongTinBan[i].addMouseListener(moListener);

            pnlLuoiBan.add(pnlThe);
        }
    }

    private JPanel taoChiDanTrangThai(String text, Color mau) {
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
                pnlLuoiBan.removeAll();
                setupTableGrid();
                pnlLuoiBan.revalidate();
                pnlLuoiBan.repaint();
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
            // Tạo lại lưới với tất cả bàn
            pnlLuoiBan.removeAll();
            setupTableGrid();
            pnlLuoiBan.revalidate();
            pnlLuoiBan.repaint();
            return;
        }

        // Xóa lưới cũ
        pnlLuoiBan.removeAll();

        // Giữ nguyên layout với 4 cột cố định
        final int soCot = 4;
        final int soHang = (TONG_SO_BAN + soCot - 1) / soCot; // Đủ số hàng như ban đầu
        pnlLuoiBan.setLayout(new GridLayout(soHang, soCot, 15, 15));
        pnlLuoiBan.setBackground(MAU_NEN);
        pnlLuoiBan.setPreferredSize(new Dimension(1000, soHang * 200));

        // Thêm các bàn tìm thấy liên tục từ đầu (không có khoảng trống)
        for (BanAn ban : danhSachBanTimThay) {
            pnlLuoiBan.add(taoTheBan(ban));
        }

        // Điền các ô trống còn lại để giữ kích cỡ lưới
        int soODaThemh = danhSachBanTimThay.size();
        while (soODaThemh < TONG_SO_BAN) {
            JPanel pnlTrong = new JPanel();
            pnlTrong.setBackground(MAU_NEN);
            pnlLuoiBan.add(pnlTrong);
            soODaThemh++;
        }

        pnlLuoiBan.revalidate();
        pnlLuoiBan.repaint();
    }

    private void hienThiTatCaBan() {
        // Reset combo box về "Tất cả"
        cboLocLoaiBan.setSelectedIndex(0);

        // Xóa lưới cũ
        pnlLuoiBan.removeAll();

        // Tạo lại lưới với tất cả bàn
        setupTableGrid();

        pnlLuoiBan.revalidate();
        pnlLuoiBan.repaint();
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

        // Kiểm tra trạng thái bàn theo ngày đã chọn
        PhieuDatBan_Ctr phieuDatCtr = PhieuDatBan_Ctr.getInstance();
        String trangThaiHienThi = layTrangThaiBanTheoNgay(ban, ngayDatDaChon, phieuDatCtr);

        switch (trangThaiHienThi) {
            case "Trống":
                mauTrangThai = MAU_TRONG;
                break;
            case "Đang sử dụng":
                mauTrangThai = MAU_DANG_SU_DUNG;
                break;
            case "Đặt trước":
                mauTrangThai = MAU_DA_DAT;
                break;
            default:
                mauTrangThai = MAU_NEN;
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
        pnlThe.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BanAn banDuocChon = banAnCtr.timBanTheoMa(maBan);
                if (banDuocChon != null) {
                    PhieuDatBan_Ctr phieuDatCtr = PhieuDatBan_Ctr.getInstance();

                    // Lấy trạng thái bàn theo ngày đã chọn
                    String trangThaiBan = layTrangThaiBanTheoNgay(banDuocChon, ngayDatDaChon, phieuDatCtr);

                    // Chỉ mở form nếu bàn có trạng thái hợp lệ
                    if (trangThaiBan.equals("Trống") ||
                            trangThaiBan.equals("Đặt trước") ||
                            trangThaiBan.equals("Đang sử dụng")) {
                        dispose();
                        // Truyền ngày đã chọn vào PhieuDat_GUI
                        new PhieuDat_GUI(maBan, banAnCtr, phieuDatCtr, ngayDatDaChon).setVisible(true);
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
        });

        return pnlThe;
    }

    private void locBanTheoLoai(String loaiBan) {
        if (loaiBan.equals("Tất cả")) {
            // Tạo lại lưới với tất cả bàn
            pnlLuoiBan.removeAll();
            setupTableGrid();
            pnlLuoiBan.revalidate();
            pnlLuoiBan.repaint();
            return;
        }

        // Lấy danh sách bàn theo loại
        ArrayList<BanAn> danhSachBanLoc = new ArrayList<>();
        ArrayList<BanAn> tatCaBan = banAnCtr.layTatCaBan();

        for (BanAn ban : tatCaBan) {
            if (ban.getLoaiBan().equals(loaiBan)) {
                danhSachBanLoc.add(ban);
            }
        }

        // Xóa lưới cũ
        pnlLuoiBan.removeAll();

        // Giữ nguyên layout với 4 cột cố định
        final int soCot = 4;
        final int soHang = (TONG_SO_BAN + soCot - 1) / soCot; // Đủ số hàng như ban đầu
        pnlLuoiBan.setLayout(new GridLayout(soHang, soCot, 15, 15));
        pnlLuoiBan.setBackground(MAU_NEN);
        pnlLuoiBan.setPreferredSize(new Dimension(1000, soHang * 200));

        // Thêm các bàn đã lọc liên tục từ đầu (không có khoảng trống)
        for (BanAn ban : danhSachBanLoc) {
            pnlLuoiBan.add(taoTheBan(ban));
        }

        // Điền các ô trống còn lại để giữ kích cỡ lưới
        int soODaThemh = danhSachBanLoc.size();
        while (soODaThemh < TONG_SO_BAN) {
            JPanel pnlTrong = new JPanel();
            pnlTrong.setBackground(MAU_NEN);
            pnlLuoiBan.add(pnlTrong);
            soODaThemh++;
        }

        pnlLuoiBan.revalidate();
        pnlLuoiBan.repaint();
    }

    /**
     * Lấy trạng thái bàn theo ngày đã chọn
     * Kiểm tra xem bàn có phiếu đặt vào ngày cụ thể không
     */
    private String layTrangThaiBanTheoNgay(BanAn ban, String ngayKiemTra, PhieuDatBan_Ctr phieuDatCtr) {
        // Lấy ngày hiện tại (định dạng dd/MM/yyyy)
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String ngayHienTai = sdf.format(new Date());

        // Chỉ hiển thị trạng thái "Đang sử dụng" nếu ngày kiểm tra là ngày hiện tại
        if (ngayKiemTra.equals(ngayHienTai) && ban.getTrangThai().equals("Đang sử dụng")) {
            return "Đang sử dụng";
        }

        // Kiểm tra xem bàn có phiếu đặt vào ngày này không
        ArrayList<PhieuDatBan> tatCaPhieu = phieuDatCtr.layTatCaPhieu();

        for (PhieuDatBan phieu : tatCaPhieu) {
            // Bỏ qua phiếu đã hủy
            if (phieu.getTrangThai().equals("Đã hủy")) {
                continue;
            }

            // Kiểm tra xem phiếu này có chứa bàn cần kiểm tra không
            if (phieu.getMaBan() == ban.getMaBan() && phieu.getNgayDat().equals(ngayKiemTra)) {
                return "Đặt trước";
            }
        }

        // Nếu không có phiếu nào vào ngày này → Bàn trống
        return "Trống";
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


