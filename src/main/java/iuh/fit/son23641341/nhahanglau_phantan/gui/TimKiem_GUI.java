package iuh.fit.son23641341.nhahanglau_phantan.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import iuh.fit.son23641341.nhahanglau_phantan.control.PhieuDatBan_Ctr;
import iuh.fit.son23641341.nhahanglau_phantan.dao.KhachHang_DAO;
import iuh.fit.son23641341.nhahanglau_phantan.dao.PhieuDat_DAO;
import iuh.fit.son23641341.nhahanglau_phantan.dao.TimKiemChung_DAO;
import iuh.fit.son23641341.nhahanglau_phantan.dao.HoaDon_DAO;
import iuh.fit.son23641341.nhahanglau_phantan.entity.HoaDon;
import iuh.fit.son23641341.nhahanglau_phantan.entity.KhachHangThanhVien;
import iuh.fit.son23641341.nhahanglau_phantan.entity.MonAn;
import iuh.fit.son23641341.nhahanglau_phantan.entity.PhieuDatBan;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TimKiem_GUI extends JFrame {

    // --- HẰNG SỐ MÀU SẮC GIAO DIỆN ---
    private static final Color PRIMARY_COLOR = new Color(0xDC4332);
    private static final Color COLOR_BACKGROUND_MAIN = new Color(236, 240, 241);
    private static final Color COLOR_TOP_BAR = new Color(44, 62, 80);

    // --- MÀU HẠNG THÀNH VIÊN ---
    private static final Color RANK_DIAMOND = new Color(52, 152, 219);
    private static final Color RANK_GOLD = new Color(241, 196, 15);
    private static final Color RANK_SILVER = new Color(149, 165, 166);
    private static final Color RANK_BRONZE = new Color(211, 84, 0);

    // --- THUỘC TÍNH CONTROLLER & DAO ---
    private TimKiemChung_DAO timKiemDao;
    private PhieuDatBan_Ctr phieuDatCtr;
    private KhachHang_DAO khachHangDao;
    private HoaDon_DAO hoaDonDao;
    private PhieuDat_DAO phieuDatDao;

    // --- UI COMPONENTS ---
    private JPanel pnlMain;
    private JPanel pnlContent;
    private JPanel foodGrid;
    private JTextField searchField;

    // Checkboxes (Đã xóa cbMaBanAn)
    private JCheckBox cbMaMon;
    private JCheckBox cbMaPhieu;
    private JCheckBox cbMaHoaDon;
    private JCheckBox cbMaKhachHang;

    private JCheckBox[] checkBoxes;

    private ImageIcon defaultFoodIcon = null;
    private static final int ITEM_WIDTH = 250;

    public TimKiem_GUI() {
        timKiemDao = new TimKiemChung_DAO();
        phieuDatCtr = PhieuDatBan_Ctr.getInstance();
        khachHangDao = new KhachHang_DAO();
        hoaDonDao = new HoaDon_DAO();
        phieuDatDao = new PhieuDat_DAO();

        try {
            defaultFoodIcon = new ImageIcon(getClass().getResource("/imgs/lau.png"));
        } catch (Exception e) {}

        initializeComponents();
        setupLayout();

        // Cập nhật mảng checkbox chỉ còn 4 phần tử
        checkBoxes = new JCheckBox[]{cbMaMon, cbMaPhieu, cbMaHoaDon, cbMaKhachHang};
        addEventHandlers();

        try {
            loadFoodDataToGrid(timKiemDao.getAllMonAn());
        } catch (Exception e) {}
    }

    private void initializeComponents() {
        pnlMain = new JPanel(new BorderLayout());
        pnlContent = createMainContent();
    }

    private void setupLayout() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("HÈ DÌ LEO - Tra Cứu Thông Tin");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        SideBar_GUI sidebar = new SideBar_GUI();
        sidebar.setMauNutKhiChon("Tìm Kiếm");
        add(sidebar, BorderLayout.WEST);

        add(pnlMain, BorderLayout.CENTER);
        pnlMain.add(pnlContent, BorderLayout.CENTER);
    }

    private void addEventHandlers() {
        ActionListener searchListener = e -> performSearch();
        searchField.addActionListener(searchListener);

        for (JCheckBox cb : checkBoxes) {
            cb.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        for (JCheckBox otherCb : checkBoxes) {
                            if (otherCb != cb) otherCb.setSelected(false);
                        }
                    }
                    // Cập nhật lại placeholder khi đổi checkbox
                    String currentText = searchField.getText();
                    if(currentText.isEmpty() || currentText.startsWith("Tìm kiếm") || currentText.startsWith("Nhập")) {
                        searchField.setText("");
                        searchField.transferFocus(); // Trick để kích hoạt focus lost
                    }
                    performSearch();
                }
            });
        }
    }

    /**
     * LOGIC TÌM KIẾM THÔNG MINH
     */
    private void performSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.startsWith("Tìm kiếm") || keyword.startsWith("Nhập")) keyword = "";

        // --- 1. KIỂM TRA CHECKBOX ---
        JCheckBox selectedCb = null;
        for (JCheckBox cb : checkBoxes) {
            if (cb.isSelected()) {
                selectedCb = cb;
                break;
            }
        }

        if (selectedCb != null) {
            if (selectedCb == cbMaKhachHang) timKiemKhachHang(keyword);
            else if (selectedCb == cbMaHoaDon) timKiemHoaDon(keyword);
            else if (selectedCb == cbMaPhieu) timKiemPhieuDat(keyword);
            else timKiemMonAnWithFilter(keyword);
            return;
        }

        // --- 2. TỰ ĐỘNG NHẬN DIỆN (Nếu không chọn checkbox) ---

        // A. Tìm Hóa Đơn (Bắt đầu bằng HD)
        if (keyword.toUpperCase().startsWith("HD")) {
            timKiemHoaDon(keyword);
            return;
        }

        // B. Tìm Phiếu Đặt (Bắt đầu bằng PD)
        if (keyword.toUpperCase().startsWith("PD")) {
            timKiemPhieuDat(keyword);
            return;
        }

        // C. Tìm gộp (Nếu là số -> Có thể là SĐT, hoặc Mã hóa đơn số)
        if (keyword.matches("\\d+") && !keyword.isEmpty()) {
            timKiemTongHopSo(keyword);
            return;
        }

        // D. Mặc định -> Tìm Món Ăn
        timKiemMonAnWithFilter(keyword);
    }

    /**
     * HÀM TÌM KIẾM TỔNG HỢP CHO SỐ (SĐT, Mã Hóa Đơn)
     */
    private void timKiemTongHopSo(String keyword) {
        foodGrid.removeAll();
        boolean found = false;

        // 1. TÌM KHÁCH HÀNG
        try {
            ArrayList<KhachHangThanhVien> listKH = khachHangDao.timKhachHangTheoSDT(keyword);
            if (listKH != null && !listKH.isEmpty()) {
                for (KhachHangThanhVien kh : listKH) foodGrid.add(taoTheKhachHang(kh));
                found = true;
            }
        } catch (Exception e) {}

        // 2. TÌM PHIẾU ĐẶT
        try {
            ArrayList<PhieuDatBan> listPhieu = phieuDatDao.timKiemPhieuDat(keyword);
            if (listPhieu != null && !listPhieu.isEmpty()) {
                for (PhieuDatBan pd : listPhieu) foodGrid.add(taoThePhieuDat(pd));
                found = true;
            }
        } catch (Exception e) {}

        // 3. TÌM HÓA ĐƠN
        try {
            List<HoaDon> listHD = hoaDonDao.timKiemHoaDon(keyword);
            if (listHD != null && !listHD.isEmpty()) {
                for (HoaDon hd : listHD) foodGrid.add(taoTheHoaDon(hd));
                found = true;
            }
        } catch (Exception e) {}

        if (!found) hienThiThongBaoRong("Không tìm thấy dữ liệu liên quan đến số: " + keyword);
        else setupGridLayout(foodGrid.getComponentCount());

        refreshGrid();
    }

    // --- CÁC HÀM TÌM KIẾM RIÊNG LẺ ---

    private void timKiemHoaDon(String keyword) {
        foodGrid.removeAll();
        try {
            List<HoaDon> ketQua = hoaDonDao.timKiemHoaDon(keyword);
            if (ketQua == null || ketQua.isEmpty()) hienThiThongBaoRong("Không tìm thấy hóa đơn: " + keyword);
            else {
                setupGridLayout(ketQua.size());
                for (HoaDon hd : ketQua) foodGrid.add(taoTheHoaDon(hd));
            }
        } catch (Exception e) {
            hienThiThongBaoRong("Lỗi khi tìm hóa đơn: " + e.getMessage());
        }
        refreshGrid();
    }

    private void timKiemPhieuDat(String keyword) {
        foodGrid.removeAll();
        ArrayList<PhieuDatBan> ketQua = phieuDatDao.timKiemPhieuDat(keyword);

        if (ketQua == null || ketQua.isEmpty()) {
            hienThiThongBaoRong("Không tìm thấy phiếu đặt: " + keyword);
        } else {
            setupGridLayout(ketQua.size());
            for (PhieuDatBan pd : ketQua) {
                foodGrid.add(taoThePhieuDat(pd));
            }
        }
        refreshGrid();
    }

    private void timKiemMonAnWithFilter(String keyword) {
        if (keyword.isEmpty() && !cbMaMon.isSelected() && !cbMaPhieu.isSelected() && !cbMaHoaDon.isSelected()) {
            try { loadFoodDataToGrid(timKiemDao.getAllMonAn()); } catch (Exception ex) {}
            return;
        }
        try {
            // Tham số searchByTable đã set là false
            List<MonAn> ketQua = timKiemDao.timKiemMonAn(
                    keyword, cbMaMon.isSelected(), cbMaPhieu.isSelected(), cbMaHoaDon.isSelected(), false, false
            );
            loadFoodDataToGrid(ketQua);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm món ăn: " + e.getMessage());
        }
    }

    private void timKiemKhachHang(String keyword) {
        foodGrid.removeAll();
        ArrayList<KhachHangThanhVien> ketQua = khachHangDao.timKhachHangTheoSDT(keyword);
        if (ketQua.isEmpty()) hienThiThongBaoRong("Không tìm thấy khách hàng: " + keyword);
        else {
            setupGridLayout(ketQua.size());
            for (KhachHangThanhVien kh : ketQua) foodGrid.add(taoTheKhachHang(kh));
        }
        refreshGrid();
    }

    // --- CÁC HÀM TẠO THẺ UI (CARD VIEW) ---

    // 1. THẺ PHIẾU ĐẶT
    private JPanel taoThePhieuDat(PhieuDatBan phieu) {
        JPanel pnlThe = new JPanel();
        pnlThe.setLayout(new BoxLayout(pnlThe, BoxLayout.Y_AXIS));
        pnlThe.setBackground(Color.WHITE);
        pnlThe.setPreferredSize(new Dimension(ITEM_WIDTH, 220));
        pnlThe.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(15, 10, 15, 10)));
        pnlThe.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Icon
        JLabel lblIcon = new JLabel("📅");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Mã Phiếu
        JLabel lblMaPhieu = new JLabel(phieu.getMaPhieu());
        lblMaPhieu.setFont(new Font("Arial", Font.BOLD, 18));
        lblMaPhieu.setForeground(new Color(44, 62, 80));
        lblMaPhieu.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblMaPhieu.setBorder(new EmptyBorder(5, 0, 5, 0));

        // Tên Khách
        String tenKhach = phieu.getTenKhachDat() != null ? phieu.getTenKhachDat() : "Khách vãng lai";
        JLabel lblTenKhach = new JLabel(tenKhach);
        lblTenKhach.setFont(new Font("Arial", Font.BOLD, 14));
        lblTenKhach.setForeground(Color.BLACK);
        lblTenKhach.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Ngày giờ đặt
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String thoiGian = (phieu.getThoiGianDat() != null) ? sdf.format(phieu.getThoiGianDat()) : phieu.getNgayDat();
        JLabel lblNgay = new JLabel(thoiGian);
        lblNgay.setFont(new Font("Arial", Font.ITALIC, 12));
        lblNgay.setForeground(Color.GRAY);
        lblNgay.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Tiền cọc
        String tienCocStr = String.format("Cọc: %,.0f VNĐ", phieu.getTienCoc());
        JLabel lblTienCoc = new JLabel(tienCocStr);
        lblTienCoc.setFont(new Font("Arial", Font.BOLD, 13));
        lblTienCoc.setForeground(new Color(192, 57, 43)); // Màu đỏ
        lblTienCoc.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Trạng thái (Xử lý màu sắc)
        JLabel lblTrangThai = new JLabel(phieu.getTrangThai() != null ? phieu.getTrangThai() : "N/A");
        lblTrangThai.setFont(new Font("Arial", Font.BOLD, 11));
        lblTrangThai.setForeground(Color.WHITE);
        lblTrangThai.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTrangThai.setOpaque(true);
        lblTrangThai.setBorder(new EmptyBorder(3, 8, 3, 8));

        // Hàm cập nhật màu trạng thái
        Runnable updateColorStatus = () -> {
            String tt = lblTrangThai.getText();
            if (tt.equalsIgnoreCase("Đã xác nhận") || tt.equalsIgnoreCase("Thành công"))
                lblTrangThai.setBackground(new Color(46, 204, 113));
            else if (tt.equalsIgnoreCase("Đặt trước"))
                lblTrangThai.setBackground(new Color(52, 152, 219));
            else if (tt.equalsIgnoreCase("Đã hủy"))
                lblTrangThai.setBackground(new Color(231, 76, 60));
            else
                lblTrangThai.setBackground(Color.GRAY);
        };
        updateColorStatus.run();

        JPanel pnlBadge = new JPanel();
        pnlBadge.setBackground(Color.WHITE);
        pnlBadge.add(lblTrangThai);
        pnlBadge.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlThe.add(lblIcon);
        pnlThe.add(lblMaPhieu);
        pnlThe.add(lblTenKhach);
        pnlThe.add(lblNgay);
        pnlThe.add(Box.createVerticalStrut(5));
        pnlThe.add(lblTienCoc);
        pnlThe.add(Box.createVerticalStrut(5));
        pnlThe.add(pnlBadge);

        addHoverEffect(pnlThe);

        pnlThe.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Logic chỉnh sửa phiếu đặt
                JTextField txtTen = new JTextField(phieu.getTenKhachDat());
                JTextField txtSDT = new JTextField(phieu.getSdtDat());
                JTextField txtEmail = new JTextField(phieu.getEmailDat());
                JTextField txtCoc = new JTextField(String.valueOf((long)phieu.getTienCoc()));

                String[] statusList = {"Đặt trước", "Đã xác nhận", "Đã hủy", "Hoàn thành"};
                JComboBox<String> cbStatus = new JComboBox<>(statusList);
                cbStatus.setSelectedItem(phieu.getTrangThai());

                JPanel pnlEdit = new JPanel(new GridLayout(0, 2, 10, 10));
                pnlEdit.add(new JLabel("Tên khách:")); pnlEdit.add(txtTen);
                pnlEdit.add(new JLabel("SĐT:")); pnlEdit.add(txtSDT);
                pnlEdit.add(new JLabel("Email:")); pnlEdit.add(txtEmail);
                pnlEdit.add(new JLabel("Tiền cọc:")); pnlEdit.add(txtCoc);
                pnlEdit.add(new JLabel("Trạng thái:")); pnlEdit.add(cbStatus);

                int resultEdit = JOptionPane.showConfirmDialog(null, pnlEdit,
                        "Chỉnh sửa Phiếu " + phieu.getMaPhieu(), JOptionPane.OK_CANCEL_OPTION);

                if (resultEdit == JOptionPane.OK_OPTION) {
                    String tenMoi = txtTen.getText();
                    String cocMoi = txtCoc.getText();
                    String trangThaiMoi = (String) cbStatus.getSelectedItem();

                    phieu.setTenKhachDat(tenMoi);
                    phieu.setSdtDat(txtSDT.getText());
                    phieu.setEmailDat(txtEmail.getText());
                    try {
                        phieu.setTienCoc(Double.parseDouble(cocMoi));
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Tiền cọc phải là số!");
                        return;
                    }
                    phieu.setTrangThai(trangThaiMoi);

                    lblTenKhach.setText(tenMoi);
                    lblTienCoc.setText("Cọc: " + String.format("%,.0f VNĐ", phieu.getTienCoc()));
                    lblTrangThai.setText(trangThaiMoi);
                    updateColorStatus.run();

                    JOptionPane.showMessageDialog(null, "Cập nhật thông tin thành công!");
                }
            }
        });
        return pnlThe;
    }

    // 2. THẺ HÓA ĐƠN
    private JPanel taoTheHoaDon(HoaDon hoaDon) {
        JPanel pnlThe = new JPanel();
        pnlThe.setLayout(new BoxLayout(pnlThe, BoxLayout.Y_AXIS));
        pnlThe.setBackground(Color.WHITE);
        pnlThe.setPreferredSize(new Dimension(ITEM_WIDTH, 200));
        pnlThe.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(15, 10, 15, 10)));
        pnlThe.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblIcon = new JLabel("🧾");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblMaHD = new JLabel(hoaDon.getMaHoaDon());
        lblMaHD.setFont(new Font("Arial", Font.BOLD, 18));
        lblMaHD.setForeground(new Color(44, 62, 80));
        lblMaHD.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblMaHD.setBorder(new EmptyBorder(5, 0, 5, 0));

        String tongTienStr = String.format("%,.0f VNĐ", hoaDon.getTongTien());
        JLabel lblTongTien = new JLabel(tongTienStr);
        lblTongTien.setFont(new Font("Arial", Font.BOLD, 16));
        lblTongTien.setForeground(new Color(192, 57, 43));
        lblTongTien.setAlignmentX(Component.CENTER_ALIGNMENT);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String ngayLapStr = (hoaDon.getNgayLap() != null) ? sdf.format(hoaDon.getNgayLap()) : "N/A";
        JLabel lblNgayLap = new JLabel(ngayLapStr);
        lblNgayLap.setFont(new Font("Arial", Font.ITALIC, 13));
        lblNgayLap.setForeground(Color.GRAY);
        lblNgayLap.setAlignmentX(Component.CENTER_ALIGNMENT);

        String trangThai = (hoaDon.getTrangThai() != null) ? hoaDon.getTrangThai() : "";
        Color mauNenTT = Color.GRAY;
        if (trangThai.equalsIgnoreCase("Đã thanh toán")) mauNenTT = new Color(46, 204, 113);
        else if (trangThai.equalsIgnoreCase("Chưa thanh toán")) mauNenTT = new Color(241, 196, 15);
        else if (trangThai.equalsIgnoreCase("Đã hủy")) mauNenTT = new Color(231, 76, 60);

        JLabel lblTrangThai = new JLabel(trangThai);
        lblTrangThai.setFont(new Font("Arial", Font.BOLD, 11));
        lblTrangThai.setForeground(Color.WHITE);
        lblTrangThai.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTrangThai.setOpaque(true);
        lblTrangThai.setBackground(mauNenTT);
        lblTrangThai.setBorder(new EmptyBorder(3, 8, 3, 8));

        JPanel pnlBadge = new JPanel();
        pnlBadge.setBackground(Color.WHITE);
        pnlBadge.add(lblTrangThai);
        pnlBadge.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlThe.add(lblIcon);
        pnlThe.add(lblMaHD);
        pnlThe.add(lblTongTien);
        pnlThe.add(lblNgayLap);
        pnlThe.add(Box.createVerticalStrut(5));
        pnlThe.add(pnlBadge);

        addHoverEffect(pnlThe);
        pnlThe.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JOptionPane.showMessageDialog(null,
                        "Mã Hóa Đơn: " + hoaDon.getMaHoaDon() +
                                "\nTổng Tiền: " + tongTienStr + "\nNgày lập: " + ngayLapStr + "\nTrạng Thái: " + trangThai,
                        "Chi Tiết Hóa Đơn", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        return pnlThe;
    }

    // 3. THẺ KHÁCH HÀNG (ĐÃ XÓA SỰ KIỆN CLICK POPUP)
    private JPanel taoTheKhachHang(KhachHangThanhVien kh) {
        JPanel pnlThe = new JPanel();
        pnlThe.setLayout(new BoxLayout(pnlThe, BoxLayout.Y_AXIS));
        pnlThe.setBackground(Color.WHITE);
        pnlThe.setPreferredSize(new Dimension(ITEM_WIDTH, 220));
        pnlThe.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1), new EmptyBorder(15, 10, 15, 10)));
        // Giữ hiệu ứng hover, nhưng xóa hand cursor nếu không còn click được,
        // hoặc giữ hand cursor nếu bạn muốn đẹp (nhưng click không ra gì).
        // Ở đây mình để Hand cursor để hiển thị là có tương tác (hover)
        pnlThe.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblAvatar = new JLabel("👤");
        lblAvatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        lblAvatar.setForeground(Color.DARK_GRAY);
        lblAvatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTen = new JLabel(kh.getHoTen());
        lblTen.setFont(new Font("Arial", Font.BOLD, 18));
        lblTen.setForeground(Color.BLACK);
        lblTen.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTen.setBorder(new EmptyBorder(10, 0, 5, 0));

        JLabel lblSDT = new JLabel(kh.getSoDienThoai());
        lblSDT.setFont(new Font("Arial", Font.PLAIN, 16));
        lblSDT.setForeground(Color.GRAY);
        lblSDT.setAlignmentX(Component.CENTER_ALIGNMENT);

        String hang = kh.getThanhVien().trim();
        Color mauHang = RANK_BRONZE;
        if (hang.equalsIgnoreCase("Kim Cương") || hang.equalsIgnoreCase("Diamond")) mauHang = RANK_DIAMOND;
        else if (hang.equalsIgnoreCase("Vàng") || hang.equalsIgnoreCase("Gold")) mauHang = RANK_GOLD;
        else if (hang.equalsIgnoreCase("Bạc") || hang.equalsIgnoreCase("Silver")) mauHang = RANK_SILVER;

        JLabel lblHang = new JLabel(hang.toUpperCase());
        lblHang.setFont(new Font("Arial", Font.BOLD, 12));
        lblHang.setForeground(Color.WHITE);
        lblHang.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblHang.setOpaque(true);
        lblHang.setBackground(mauHang);
        lblHang.setBorder(new EmptyBorder(5, 15, 5, 15));

        JPanel pnlBadge = new JPanel();
        pnlBadge.setBackground(Color.WHITE);
        pnlBadge.add(lblHang);
        pnlBadge.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDiem = new JLabel("Điểm: " + (int)kh.getDiemTichLuy());
        lblDiem.setFont(new Font("Arial", Font.ITALIC, 13));
        lblDiem.setForeground(new Color(100, 100, 100));
        lblDiem.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlThe.add(lblAvatar);
        pnlThe.add(Box.createVerticalStrut(5));
        pnlThe.add(lblTen);
        pnlThe.add(lblSDT);
        pnlThe.add(Box.createVerticalStrut(10));
        pnlThe.add(pnlBadge);
        pnlThe.add(lblDiem);

        addHoverEffect(pnlThe);
        // Đã xóa sự kiện MouseListener (click popup) tại đây
        return pnlThe;
    }

    // 4. THẺ MÓN ĂN
    private JPanel createFoodItemPanel(MonAn mon) {
        JPanel pnlThe = new JPanel();
        pnlThe.setLayout(new BoxLayout(pnlThe, BoxLayout.Y_AXIS));
        pnlThe.setBackground(Color.WHITE);
        pnlThe.setPreferredSize(new Dimension(ITEM_WIDTH, 240));
        pnlThe.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1), new EmptyBorder(10, 10, 10, 10)));
        pnlThe.setCursor(new Cursor(Cursor.HAND_CURSOR));

        ScaledImageLabel imageLabel = new ScaledImageLabel(defaultFoodIcon != null ? defaultFoodIcon.getImage() : null);
        imageLabel.setPreferredSize(new Dimension(200, 140));
        imageLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        imageLabel.setBackground(Color.WHITE);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTen = new JLabel(mon.getTenMon());
        lblTen.setFont(new Font("Arial", Font.BOLD, 18));
        lblTen.setForeground(new Color(51, 51, 51));
        lblTen.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTen.setBorder(new EmptyBorder(10, 0, 5, 0));

        JLabel lblLoai = new JLabel(mon.getLoaiMon());
        lblLoai.setFont(new Font("Arial", Font.PLAIN, 12));
        lblLoai.setForeground(Color.GRAY);
        lblLoai.setAlignmentX(Component.CENTER_ALIGNMENT);

        String priceStr = String.format("%,.0f VND", mon.getGia());
        JLabel lblGia = new JLabel(priceStr);
        lblGia.setFont(new Font("Arial", Font.BOLD, 16));
        lblGia.setForeground(PRIMARY_COLOR);
        lblGia.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblGia.setBorder(new EmptyBorder(5, 0, 0, 0));

        pnlThe.add(imageLabel);
        pnlThe.add(lblTen);
        pnlThe.add(lblLoai);
        pnlThe.add(Box.createVerticalGlue());
        pnlThe.add(lblGia);

        addHoverEffect(pnlThe);
        pnlThe.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JOptionPane.showMessageDialog(null, "Món: " + mon.getTenMon() + "\nGiá: " + priceStr);
            }
        });
        return pnlThe;
    }

    private void loadFoodDataToGrid(List<MonAn> list) {
        foodGrid.removeAll();
        if (list == null || list.isEmpty()) hienThiThongBaoRong("Không tìm thấy món ăn nào phù hợp.");
        else {
            setupGridLayout(list.size());
            for (MonAn mon : list) foodGrid.add(createFoodItemPanel(mon));
        }
        refreshGrid();
    }

    private void setupGridLayout(int itemCount) {
        int cols = 4;
        if (itemCount <= cols) foodGrid.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        else foodGrid.setLayout(new GridLayout(0, cols, 15, 15));
    }

    private void refreshGrid() {
        foodGrid.revalidate();
        foodGrid.repaint();
    }

    private void hienThiThongBaoRong(String tinNhan) {
        JLabel lblNoResult = new JLabel(tinNhan);
        lblNoResult.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblNoResult.setForeground(Color.GRAY);
        foodGrid.setLayout(new FlowLayout(FlowLayout.CENTER));
        foodGrid.add(lblNoResult);
    }

    private void addHoverEffect(JPanel pnl) {
        pnl.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pnl.setBackground(new Color(250, 248, 245));
                pnl.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 1));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pnl.setBackground(Color.WHITE);
                pnl.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
            }
        });
    }

    private JPanel createMainContent() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_BACKGROUND_MAIN);

        JPanel headerContainer = new JPanel();
        headerContainer.setLayout(new BoxLayout(headerContainer, BoxLayout.Y_AXIS));

        JPanel topBar = new JPanel(new BorderLayout(20, 0));
        topBar.setBackground(COLOR_TOP_BAR);
        topBar.setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel searchWrapper = new JPanel(new BorderLayout(10, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.dispose();
            }
        };
        searchWrapper.setBackground(new Color(222, 222, 222));
        searchWrapper.setOpaque(false);
        searchWrapper.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel searchIconLabel = new JLabel("🔍");
        searchIconLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 24));
        searchIconLabel.setForeground(Color.DARK_GRAY);

        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        searchField.setBorder(null);
        searchField.setOpaque(false);
        addPlaceholderStyle(searchField, "Tìm kiếm món ăn...");

        searchWrapper.add(searchIconLabel, BorderLayout.WEST);
        searchWrapper.add(searchField, BorderLayout.CENTER);
        topBar.add(searchWrapper, BorderLayout.CENTER);
        headerContainer.add(topBar);

        JPanel checkBoxArea = new JPanel(new FlowLayout(FlowLayout.CENTER));
        checkBoxArea.setBackground(COLOR_BACKGROUND_MAIN);
        checkBoxArea.setBorder(new EmptyBorder(10, 20, 10, 20));

        JPanel checkBoxPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        checkBoxPanel.setBackground(Color.WHITE);
        checkBoxPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        cbMaMon = new JCheckBox("Mã món ăn");
        cbMaPhieu = new JCheckBox("Mã phiếu đặt");
        cbMaHoaDon = new JCheckBox("Mã hóa đơn");
        cbMaKhachHang = new JCheckBox("Mã khách hàng");
        // Đã xóa cbMaBanAn ở đây

        // Vòng lặp chỉ chạy đến 4 (bỏ 5)
        for (int i = 0; i < 4; i++) {
            JCheckBox cb = new JCheckBox();
            if (i == 0) cb = cbMaMon;
            else if (i == 1) cb = cbMaPhieu;
            else if (i == 2) cb = cbMaHoaDon;
            else if (i == 3) cb = cbMaKhachHang;

            JPanel cbWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 45, 5));
            cbWrapper.setOpaque(false);
            cb.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            cb.setOpaque(false);
            cb.setForeground(Color.DARK_GRAY);
            cbWrapper.add(cb);
            if (i < 3) cbWrapper.setBorder(new MatteBorder(0, 0, 0, 1, new Color(220, 220, 220)));
            checkBoxPanel.add(cbWrapper);
        }

        checkBoxArea.add(checkBoxPanel);
        headerContainer.add(checkBoxArea);
        mainPanel.add(headerContainer, BorderLayout.NORTH);

        JPanel gridContainer = new JPanel(new BorderLayout());
        gridContainer.setBackground(COLOR_BACKGROUND_MAIN);
        gridContainer.setBorder(new EmptyBorder(10, 20, 20, 20));

        foodGrid = new JPanel(new GridLayout(0, 4, 15, 15));
        foodGrid.setOpaque(false);
        foodGrid.setBorder(new EmptyBorder(10, 10, 10, 10));

        gridContainer.add(foodGrid, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(gridContainer);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(COLOR_BACKGROUND_MAIN);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        return mainPanel;
    }

    public void addPlaceholderStyle(JTextField textField, String placeholder) {
        // --- CHỈNH SỬA Ở ĐÂY: Đặt Font.BOLD cho lúc nhập ---
        Font inputFont = new Font("Segoe UI", Font.BOLD, 18); // FONT ĐẬM
        Font placeholderFont = new Font("Segoe UI", Font.ITALIC, 18);

        Color defaultColor = Color.BLACK;
        Color placeholderColor = Color.LIGHT_GRAY;

        textField.setText(placeholder);
        textField.setFont(placeholderFont);
        textField.setForeground(placeholderColor);
        textField.setCaretColor(defaultColor);

        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                String currentText = textField.getText();
                if (currentText.startsWith("Tìm kiếm") || currentText.startsWith("Nhập")) {
                    textField.setText("");
                }
                // Quan trọng: Set font đậm ngay khi focus vào
                textField.setFont(inputFont);
                textField.setForeground(defaultColor);
            }

            @Override
            public void focusLost(FocusEvent e) {
                String currentText = textField.getText().trim();

                String currentPlaceholder = "Tìm kiếm món ăn...";
                if (cbMaKhachHang.isSelected() || currentText.matches("\\d+")) {
                    currentPlaceholder = "Nhập SĐT, Mã số...";
                } else if (cbMaHoaDon.isSelected() || currentText.toUpperCase().startsWith("HD")) {
                    currentPlaceholder = "Nhập Mã HĐ, SĐT hoặc Trạng thái...";
                } else if (cbMaPhieu.isSelected()) {
                    currentPlaceholder = "Nhập mã, tên hoặc SĐT...";
                }

                if (currentText.isEmpty() || currentText.equals(currentPlaceholder)) {
                    textField.setText(currentPlaceholder);
                    textField.setFont(placeholderFont);
                    textField.setForeground(placeholderColor);
                } else {
                    // Nếu đã nhập chữ thì giữ nguyên font ĐẬM
                    textField.setFont(inputFont);
                    textField.setForeground(defaultColor);
                }
            }
        });
    }

    class ScaledImageLabel extends JComponent {
        private Image image;
        public ScaledImageLabel(Image image) {
            this.image = image;
            setOpaque(true);
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
            if (image == null) return;
            int cw = getWidth();
            int ch = getHeight();
            int iw = image.getWidth(null);
            int ih = image.getHeight(null);
            if (iw <= 0 || ih <= 0) return;
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
            new TimKiem_GUI().setVisible(true);
        });
    }
}
