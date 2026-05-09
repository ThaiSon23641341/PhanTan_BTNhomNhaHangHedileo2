package iuh.fit.son23641341.nhahanglau_phantan.gui;

import iuh.fit.son23641341.nhahanglau_phantan.control.BanAn_Ctr;
import iuh.fit.son23641341.nhahanglau_phantan.control.HoaDon_Ctrl;
import iuh.fit.son23641341.nhahanglau_phantan.control.KhachHang_Ctr;
import iuh.fit.son23641341.nhahanglau_phantan.control.PhieuDatBan_Ctr;
import iuh.fit.son23641341.nhahanglau_phantan.control.User_Ctr;
import iuh.fit.son23641341.nhahanglau_phantan.dao.HoaDon_DAO;
import iuh.fit.son23641341.nhahanglau_phantan.dao.KhachHang_DAO;
import iuh.fit.son23641341.nhahanglau_phantan.dao.PhieuDat_DAO;
import iuh.fit.son23641341.nhahanglau_phantan.entity.BanAn;
import iuh.fit.son23641341.nhahanglau_phantan.entity.ChiTietDatMon;
import iuh.fit.son23641341.nhahanglau_phantan.entity.HoaDon;
import iuh.fit.son23641341.nhahanglau_phantan.entity.KhachHangThanhVien;
import iuh.fit.son23641341.nhahanglau_phantan.entity.KhuyenMai;
import iuh.fit.son23641341.nhahanglau_phantan.entity.PhieuDatBan;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog.ModalityType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.toedter.calendar.JDateChooser;
import iuh.fit.son23641341.nhahanglau_phantan.control.PrinterService;

import java.io.File;

public class PhieuDat_GUI extends JPanel {
    // Biến lưu trạng thái tạm của form (instance, không static)
    private PhieuDatBan phieuTamThoi = null;
    private boolean isThanhVienTamThoi = false;
    private boolean isCheckboxThanhVienTamThoi = false;
    private String cheDoHienThiTamThoi = null; // Lưu chế độ hiển thị khi chuyển màn hình
    private String maPhieuTamThoi = null; // Lưu mã phiếu khi chuyển màn hình
    private static final Color MAU_CHINH = new Color(0xDC4332);
    private static final Color MAU_XAC_NHAN = new Color(0x7AB750);
    private static final Color MAU_TONG_TIEN = new Color(0x666666);
    private static final Color MAU_PLACEHOLDER = new Color(0x606060);

    private JPanel pnlHeader;
    private JPanel pnlFormDatBan;

    private JLabel lblThongTinAdmin;
    private JLabel lblTieuDeTrang;

    // --- CÁC COMPONENT FORM ---
    private JTextField txtTenKhachHang;
    private JTextField txtSoDienThoai;
    private JTextField txtEmail;
    private JTextField txtSoDienThoaiThanhVien;

    private JCheckBox chkThemThanhVien;
    private JLabel lblTrangThaiThanhVien;

    private JComboBox<String> cboGioiTinh;

    private JDateChooser dateChooserNgayDat;
    private JSpinner spinnerGioDat;
    private JComboBox<String> cboPhuongThucThanhToan;
    private JLabel lblTongTienDatCoc;
    private JLabel lblNhanTongTien; // Label "Tổng tiền đặt cọc" hoặc "Tạm tính"

    private JPanel pnlThongTinDatMon;
    private JLabel lblSoBan;
    private JPanel pnlDanhSachMonAn;
    private JButton btnThemMonAn;
    private JButton btnXacNhan;
    private JButton btnHuy;
    private JButton btnBatDauSuDung;
    private JButton btnThanhToan;
    private JButton btnXemPhieuDat;
    private JButton btnTaoPhieuMoi;
    private JButton btnLuuThayDoi;
    private JPanel pnlFormBenTrai;

    // Chọn nhiều bàn
    private JButton btnChonThemBan;
    private JPanel pnlDanhSachBanDaChon;
    private ArrayList<Integer> danhSachBanDaChon;
    private ArrayList<Integer> danhSachBanKhoiPhuc;

    private KhuyenMai khuyenMaiDaApDung;

    private ArrayList<ChiTietDatMon> danhSachMonDaChon;
    private int soBan;
    private BanAn_Ctr banAnCtr;
    private BanAn banHienTai;
    private PhieuDatBan_Ctr phieuDatBanCtr;
    private PhieuDatBan phieuHienTai;
    private String cheDoHienThi;
    private String ngayDatTuChonBan;
    private String maPhieuDangXem; // Lưu mã phiếu khi load từ DB
    private boolean isBanFullKhungGio = false; // Đánh dấu bàn đã full 6/6 khung giờ

    private JLabel lblGiaTriTamTinh;
    private JLabel lblThoat;

    private KhachHang_Ctr khachHangCtr;

    /**
     * Constructor mặc định để tương thích với GUIManager/CardLayout
     */
    public PhieuDat_GUI() {
        this.banAnCtr = BanAn_Ctr.getInstance();
        this.phieuDatBanCtr = PhieuDatBan_Ctr.getInstance();
        this.khachHangCtr = new KhachHang_Ctr();
        
        initializeComponents();
        setupLayout();
        thieLapXuLySuKien();
    }
    
    /**
     * Phương thức nạp dữ liệu từ bên ngoài (để thay thế constructor có tham số cũ)
     */
    public void setData(int soBan, String ngayDat, ArrayList<Integer> danhSachBanKhoiPhuc) {
        this.soBan = soBan;
        this.ngayDatTuChonBan = ngayDat;
        this.danhSachBanKhoiPhuc = danhSachBanKhoiPhuc;
        this.phieuTamThoi = null;
        this.isThanhVienTamThoi = false;
        this.isCheckboxThanhVienTamThoi = false;
        
        // Reset danh sách món để tránh lem dữ liệu giữa các bàn
        this.danhSachMonDaChon = new ArrayList<>();
        this.maPhieuDangXem = null;
        
        khoiTaoVoiDuLieu();
    }

    /**
     * Nạp dữ liệu bao gồm cả phiếu tạm (từ màn hình Chọn Món)
     */
    public void nạpDuLieuTuPhieu(int soBan, String ngayDat, PhieuDatBan phieuTam) {
        this.soBan = soBan;
        this.ngayDatTuChonBan = ngayDat;
        this.phieuTamThoi = phieuTam;
        
        // Lưu lại mã phiếu đang xem nếu có
        if (phieuTam != null && phieuTam.getMaPhieu() != null) {
            this.maPhieuDangXem = phieuTam.getMaPhieu();
        }
        
        khoiTaoVoiDuLieu();

        // TỰ ĐỘNG LƯU MÓN ĂN NẾU LÀ PHIẾU ĐANG DÙNG HOẶC ĐÃ ĐẶT (Tránh user phải ấn Lưu 2 lần)
        if (phieuHienTai != null && phieuHienTai.getMaPhieu() != null) {
            String trangThai = phieuHienTai.getTrangThai();
            if ("Đang sử dụng".equals(trangThai) || "Đã xác nhận".equals(trangThai)) {
                System.out.println("=== AUTO SYNC DISHES TO DB ===");
                phieuDatBanCtr.capNhatMonAnCuaPhieu(phieuHienTai.getMaPhieu(), rawDanhSachMon(danhSachMonDaChon));
            }
        }
    }

    private void loadData() {
        this.banHienTai = banAnCtr.timBanTheoMa(soBan);
        if (banHienTai == null) return;
        
        lblSoBan.setText("Lập phiếu đặt: Bàn " + banHienTai.getMaBanFormatted() + " (" + banHienTai.getKhuVuc() + ")");
        
        // Cập nhật ngày lên DateChooser
        if (ngayDatTuChonBan != null && !ngayDatTuChonBan.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                java.util.Date ngay = sdf.parse(ngayDatTuChonBan);
                dateChooserNgayDat.setDate(ngay);
            } catch (Exception e) {
                dateChooserNgayDat.setDate(new java.util.Date());
            }
        } else {
            dateChooserNgayDat.setDate(new java.util.Date());
        }
        
        // Reset form cơ bản
        txtTenKhachHang.setText("");
        txtSoDienThoai.setText("");
        txtEmail.setText("");
        chkThemThanhVien.setSelected(false);
        lblTrangThaiThanhVien.setText("Chưa là khách hàng thành viên");
        lblTrangThaiThanhVien.setForeground(Color.RED);
    }

    public PhieuDat_GUI(int soBan) {
        this(soBan, BanAn_Ctr.getInstance(), PhieuDatBan_Ctr.getInstance(), null, null, null, false, false);
    }

    public PhieuDat_GUI(int soBan, BanAn_Ctr banAnController, PhieuDatBan_Ctr phieuDatController) {
        this(soBan, banAnController, phieuDatController, null, null, null, false, false);
    }

    public PhieuDat_GUI(int soBan, BanAn_Ctr banAnController, PhieuDatBan_Ctr phieuDatController, String ngayDat) {
        this(soBan, banAnController, phieuDatController, ngayDat, null, null, false, false);
    }

    // Constructor mới: nhận thêm isBanFull (không còn dùng nữa, giữ lại để tương
    // thích)
    public PhieuDat_GUI(int soBan, BanAn_Ctr banAnController, PhieuDatBan_Ctr phieuDatController, String ngayDat,
            boolean isBanFull) {
        this(soBan, banAnController, phieuDatController, ngayDat, null, null, false, false);
    }

    public PhieuDat_GUI(int soBan, BanAn_Ctr banAnController, PhieuDatBan_Ctr phieuDatController, String ngayDat,
            ArrayList<Integer> danhSachBanKhoiPhuc) {
        this(soBan, banAnController, phieuDatController, ngayDat, danhSachBanKhoiPhuc, null, false, false);
    }

    public PhieuDat_GUI(int soBan, BanAn_Ctr banAnController, PhieuDatBan_Ctr phieuDatController, String ngayDat,
            ArrayList<Integer> danhSachBanKhoiPhuc, PhieuDatBan phieuTamThoi, boolean isThanhVienTamThoi,
            boolean isCheckboxThanhVienTamThoi) {
        this.banAnCtr = banAnController;
        this.phieuDatBanCtr = phieuDatController;
        this.khachHangCtr = new KhachHang_Ctr();

        this.soBan = soBan;
        this.ngayDatTuChonBan = ngayDat;
        this.danhSachBanKhoiPhuc = danhSachBanKhoiPhuc;
        this.phieuTamThoi = phieuTamThoi;
        this.isThanhVienTamThoi = isThanhVienTamThoi;
        this.isCheckboxThanhVienTamThoi = isCheckboxThanhVienTamThoi;

        initializeComponents();
        setupLayout();
        thieLapXuLySuKien();
        khoiTaoVoiDuLieu();
    }

    private void khoiTaoVoiDuLieu() {
        if (this.banAnCtr == null || this.phieuDatBanCtr == null) {
            return;
        }

        loadData();
        kichHoatForm();

        banHienTai = this.banAnCtr.timBanTheoMa(soBan);
        if (banHienTai == null) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy thông tin cho bàn số " + soBan + ".", "Lỗi Dữ Liệu",
                    JOptionPane.ERROR_MESSAGE);
            GUIManager.getInstance().switchToGUI(ChonBan_GUI.class, this);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String ngayHienTai = sdf.format(new Date());
        String ngayDangXet = (ngayDatTuChonBan != null) ? ngayDatTuChonBan : ngayHienTai;

        // 1. XÁC ĐỊNH PHIẾU HIỆN TẠI VÀ TRẠNG THÁI
        phieuHienTai = phieuDatBanCtr.layPhieuDangSuDungTheoMaBan(soBan);
        if (phieuHienTai == null) {
            phieuHienTai = phieuDatBanCtr.timPhieuTheoMaBan(soBan);
        }

        if (phieuHienTai != null) {
            this.maPhieuDangXem = phieuHienTai.getMaPhieu();
            if ("Đang sử dụng".equals(phieuHienTai.getTrangThai()) || "Đã xác nhận".equals(phieuHienTai.getTrangThai())) {
                cheDoHienThi = "DANG_SU_DUNG";
            } else {
                cheDoHienThi = "XEM_PHIEU";
            }
        } else {
            cheDoHienThi = "DAT_MOI";
            maPhieuDangXem = null;
        }

        // 2. XỬ LÝ DANH SÁCH BÀN (Multi-table)
        this.danhSachBanDaChon = new ArrayList<>();
        ArrayList<PhieuDatBan> phieuDatCungNgay = PhieuDatBan_Ctr.getInstance().layTatCaPhieu();
        ArrayList<Integer> banDaDatTruoc = new ArrayList<>();
        if (phieuDatCungNgay != null) {
            for (PhieuDatBan p : phieuDatCungNgay) {
                if (p.getNgayDat() != null && p.getNgayDat().equals(ngayDangXet) && !"Đã hủy".equals(p.getTrangThai())) {
                    banDaDatTruoc.addAll(p.getDanhSachBan());
                }
            }
        }

        if (this.danhSachBanKhoiPhuc != null && !this.danhSachBanKhoiPhuc.isEmpty()) {
            for (Integer b : this.danhSachBanKhoiPhuc) {
                if (!banDaDatTruoc.contains(b) || (phieuHienTai != null && phieuHienTai.getDanhSachBan().contains(b))) {
                    this.danhSachBanDaChon.add(b);
                }
            }
        } else {
            this.danhSachBanDaChon.add(soBan);
        }

        // 3. NẠP DỮ LIỆU TỪ PHIẾU TẠM (Nếu quay về từ màn hình chọn món)
        if (this.phieuTamThoi != null) {
            txtTenKhachHang.setText(this.phieuTamThoi.getTenKhachDat());
            txtSoDienThoai.setText(this.phieuTamThoi.getSdtDat());
            txtEmail.setText(this.phieuTamThoi.getEmailDat());
            try {
                if (this.phieuTamThoi.getGioDat() != null)
                    spinnerGioDat.setValue(new SimpleDateFormat("HH:mm").parse(this.phieuTamThoi.getGioDat()));
            } catch (Exception ex) {
            }
            cboPhuongThucThanhToan.setSelectedItem(this.phieuTamThoi.getPhuongThucThanhToan());
            danhSachBanDaChon = new ArrayList<>(this.phieuTamThoi.getDanhSachBanDaChon());

            // Ưu tiên danh sách món từ phiếu tạm
            if (this.phieuTamThoi.getDanhSachMonAn() != null) {
                this.danhSachMonDaChon = new ArrayList<>(this.phieuTamThoi.getDanhSachMonAn());
            }

            if (this.cheDoHienThiTamThoi != null) {
                cheDoHienThi = this.cheDoHienThiTamThoi;
                this.cheDoHienThiTamThoi = null;
            }
        } else {
            // 4. NẠP DỮ LIỆU TỪ DB (Nếu không có phiếu tạm)
            if (phieuHienTai != null) {
                hienThiThongTinPhieuDat();
                this.danhSachMonDaChon = new ArrayList<>(phieuHienTai.getDanhSachMonAn());
            } else {
                // Load từ giỏ hàng tạm trong controller
                this.danhSachMonDaChon = new ArrayList<>(phieuDatBanCtr.layDanhSachMonAnChoBan(soBan));
            }
        }

        // Đảm bảo đồng bộ với controller
        phieuDatBanCtr.capNhatDanhSachMonAn(soBan, rawDanhSachMon(this.danhSachMonDaChon));
        capNhatPanelThongTinDatMon();

        // 5. KIỂM TRA FULL KHUNG GIỜ
        int soPhieuDat = 0;
        if (phieuDatCungNgay != null) {
            for (PhieuDatBan p : phieuDatCungNgay) {
                if (p.getNgayDat() != null && p.getNgayDat().equals(ngayDangXet) && !"Đã hủy".equals(p.getTrangThai())) {
                    if (p.getDanhSachBan().contains(soBan)) soPhieuDat++;
                }
            }
        }
        this.isBanFullKhungGio = (soPhieuDat >= 6);

        // 6. THIẾT LẬP GIAO DIỆN NÚT BẤM
        thietLapGiaoDienTheoTrangThai(cheDoHienThi);
        
        apDungKieuDang();
    }

    private void initializeComponents() {
        pnlHeader = new JPanel();
        pnlFormDatBan = new JPanel();

        lblTieuDeTrang = new JLabel("ĐẶT BÀN");
        lblThoat = new JLabel("Thoát");
        lblThoat.setFont(new Font("Arial", Font.PLAIN, 18));
        lblThoat.setForeground(Color.GRAY);

        txtTenKhachHang = taoTextFieldCoPlaceholder("Nhập họ tên");
        txtSoDienThoai = taoTextFieldCoPlaceholder("Nhập số điện thoại");
        txtEmail = taoTextFieldCoPlaceholder("Nhập email");
        txtSoDienThoaiThanhVien = taoTextFieldCoPlaceholder("Nhập SĐT thành viên");

        chkThemThanhVien = new JCheckBox("Thêm khách hàng thành viên");
        chkThemThanhVien.setFont(new Font("Arial", Font.PLAIN, 16));
        chkThemThanhVien.setBackground(Color.WHITE);

        lblTrangThaiThanhVien = new JLabel("Chưa là khách hàng thành viên");
        lblTrangThaiThanhVien.setFont(new Font("Arial", Font.ITALIC, 12));
        lblTrangThaiThanhVien.setForeground(Color.RED);

        String[] arrGioiTinh = { "Nam", "Nữ" };
        cboGioiTinh = new JComboBox<>(arrGioiTinh);
        cboGioiTinh.setFont(new Font("Arial", Font.PLAIN, 16));
        cboGioiTinh.setBackground(Color.WHITE);

        dateChooserNgayDat = new JDateChooser();
        dateChooserNgayDat.setDateFormatString("dd/MM/yyyy");
        dateChooserNgayDat.setFont(new Font("Arial", Font.PLAIN, 16));

        if (ngayDatTuChonBan != null && !ngayDatTuChonBan.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date ngay = sdf.parse(ngayDatTuChonBan);
                dateChooserNgayDat.setDate(ngay);
            } catch (Exception e) {
                dateChooserNgayDat.setDate(new Date());
            }
        } else {
            dateChooserNgayDat.setDate(new Date());
        }

        SpinnerDateModel timeModel = new SpinnerDateModel();
        spinnerGioDat = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(spinnerGioDat, "HH:mm");
        spinnerGioDat.setEditor(timeEditor);
        // Đặt giá trị mặc định là giờ hiện tại
        java.util.Calendar cal = java.util.Calendar.getInstance();
        spinnerGioDat.setValue(cal.getTime());

        spinnerGioDat.setFont(new Font("Arial", Font.PLAIN, 16));
        JComponent timeEditorComponent = spinnerGioDat.getEditor();
        if (timeEditorComponent instanceof JSpinner.DefaultEditor) {
            JTextField spinnerTextField = ((JSpinner.DefaultEditor) timeEditorComponent).getTextField();
            spinnerTextField.setFont(new Font("Arial", Font.PLAIN, 16));
            spinnerTextField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }

        String[] phuongThucThanhToan = { "Tiền mặt", "Chuyển khoản" };
        cboPhuongThucThanhToan = new JComboBox<>(phuongThucThanhToan);
        cboPhuongThucThanhToan.setFont(new Font("Arial", Font.PLAIN, 16));

        lblTongTienDatCoc = new JLabel(" 0 VNĐ");

        pnlThongTinDatMon = new JPanel();
        lblSoBan = new JLabel("Bàn --");

        pnlDanhSachMonAn = new JPanel();
        btnThemMonAn = new JButton("Thêm món ăn");
        btnXacNhan = new JButton("Xác nhận đặt");
        btnXacNhan.setPreferredSize(new Dimension(150, 35));
        btnHuy = new JButton("Hủy");
        btnHuy.setPreferredSize(new Dimension(150, 35));
        btnBatDauSuDung = new JButton("Bắt đầu sử dụng");
        btnBatDauSuDung.setPreferredSize(new Dimension(150, 35));
        btnBatDauSuDung.setVisible(false);
        btnThanhToan = new JButton("Thanh toán");
        btnXemPhieuDat = new JButton("Xem phiếu đặt");
        btnTaoPhieuMoi = new JButton("Tạo phiếu mới");
        btnLuuThayDoi = new JButton("Lưu thay đổi");
        btnLuuThayDoi.setVisible(false);

        khuyenMaiDaApDung = phieuDatBanCtr.layKhuyenMaiTamThoi(soBan);

        btnChonThemBan = new JButton("+ Chọn thêm bàn");
        pnlDanhSachBanDaChon = new JPanel();
        danhSachBanDaChon = new ArrayList<>();

        if (this.danhSachBanKhoiPhuc != null && !this.danhSachBanKhoiPhuc.isEmpty()) {
            danhSachBanDaChon.addAll(this.danhSachBanKhoiPhuc);
        } else {
            danhSachBanDaChon.add(soBan);
        }
    }

    private JTextField taoTextFieldCoPlaceholder(String placeholder) {
        JTextField textField = new JTextField();
        textField.setText(placeholder);
        textField.setForeground(MAU_PLACEHOLDER);
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(MAU_PLACEHOLDER);
                }
            }
        });
        return textField;
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        thieLapHeader();
        thieLapFormDatBan();

        JPanel pnlBaoBoiNoiDung = new JPanel(new BorderLayout());
        pnlBaoBoiNoiDung.setBackground(new Color(0xF5F5F5));
        pnlBaoBoiNoiDung.add(pnlHeader, BorderLayout.NORTH);
        pnlBaoBoiNoiDung.add(pnlFormDatBan, BorderLayout.CENTER);

        add(pnlBaoBoiNoiDung, BorderLayout.CENTER);

    }

    private void thieLapHeader() {
        pnlHeader.setLayout(new BorderLayout());
        pnlHeader.setPreferredSize(new Dimension(0, 80));
        pnlHeader.setBackground(Color.WHITE);
        pnlHeader.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                new EmptyBorder(15, 20, 15, 20)));

        JPanel pnlThoat = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        pnlThoat.setBackground(Color.LIGHT_GRAY);
        pnlThoat.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnlThoat.add(lblThoat);

        JPanel pnlPhanTieuDe = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 17));
        pnlPhanTieuDe.setBackground(Color.WHITE);
        lblTieuDeTrang.setFont(new Font("Arial", Font.BOLD, 24));
        lblTieuDeTrang.setForeground(MAU_CHINH);
        pnlPhanTieuDe.add(lblTieuDeTrang);

        pnlHeader.add(pnlPhanTieuDe, BorderLayout.CENTER);
        pnlHeader.add(pnlThoat, BorderLayout.WEST);
        buttonEvent.addThoatPanelListener(pnlThoat);
    }

    private void thieLapFormDatBan() {
        pnlFormDatBan.setLayout(new BorderLayout());
        pnlFormDatBan.setBackground(new Color(0xF5F5F5));
        pnlFormDatBan.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel pnlHeaderSoBan = new JPanel(new BorderLayout());
        pnlHeaderSoBan.setBackground(new Color(0xF5F5F5));

        JPanel pnlTieuDeBan = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlTieuDeBan.setBackground(new Color(0xF5F5F5));
        lblSoBan.setFont(new Font("Arial", Font.BOLD, 24));
        lblSoBan.setForeground(Color.BLACK);
        pnlTieuDeBan.add(lblSoBan);

        pnlDanhSachBanDaChon.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pnlDanhSachBanDaChon.setBackground(new Color(0xF5F5F5));
        capNhatDanhSachBanDaChon();

        JPanel pnlNutChonThem = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlNutChonThem.setBackground(new Color(0xF5F5F5));
        btnChonThemBan.setFont(new Font("Arial", Font.PLAIN, 16));
        btnChonThemBan.setPreferredSize(new Dimension(150, 35));
        btnChonThemBan.setBackground(new Color(0x4CAF50));
        btnChonThemBan.setForeground(Color.WHITE);
        pnlNutChonThem.add(btnChonThemBan);

        pnlHeaderSoBan.add(pnlTieuDeBan, BorderLayout.NORTH);
        pnlHeaderSoBan.add(pnlDanhSachBanDaChon, BorderLayout.CENTER);
        pnlHeaderSoBan.add(pnlNutChonThem, BorderLayout.SOUTH);

        JPanel pnlNoiDungChinh = new JPanel(new BorderLayout(30, 0));
        pnlNoiDungChinh.setBackground(new Color(0xF5F5F5));

        pnlFormBenTrai = new JPanel(new GridBagLayout());
        pnlFormBenTrai.setBackground(Color.WHITE);
        pnlFormBenTrai.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.WEST;

        themTruongForm(pnlFormBenTrai, gbc, 0, "Thông tin khách hàng", null, true);
        themTruongForm(pnlFormBenTrai, gbc, 1, "Họ tên", txtTenKhachHang, false);
        themTruongForm(pnlFormBenTrai, gbc, 2, "Số điện thoại", txtSoDienThoai, false);
        themTruongForm(pnlFormBenTrai, gbc, 3, "Email", txtEmail, false);

        gbc.gridy = 4;
        gbc.gridx = 1;
        pnlFormBenTrai.add(lblTrangThaiThanhVien, gbc);

        themTruongForm(pnlFormBenTrai, gbc, 5, "SĐT thành viên", txtSoDienThoaiThanhVien, false);

        gbc.gridy = 6;
        gbc.gridx = 1;
        pnlFormBenTrai.add(chkThemThanhVien, gbc);

        themTruongForm(pnlFormBenTrai, gbc, 7, "Giới tính", cboGioiTinh, false);
        // themTruongForm(pnlFormBenTrai, gbc, 8, "Ngày đặt", dateChooserNgayDat, false);
        themTruongForm(pnlFormBenTrai, gbc, 9, "Giờ đặt", spinnerGioDat, false);
        themTruongForm(pnlFormBenTrai, gbc, 10, "Phương thức:", cboPhuongThucThanhToan, false);

        gbc.gridy = 11;
        gbc.gridx = 0;
        lblNhanTongTien = new JLabel("Tổng tiền đặt cọc:");
        lblNhanTongTien.setFont(new Font("Arial", Font.BOLD, 16));
        pnlFormBenTrai.add(lblNhanTongTien, gbc);

        gbc.gridx = 1;
        lblTongTienDatCoc.setFont(new Font("Arial", Font.BOLD, 16));
        lblTongTienDatCoc.setForeground(MAU_TONG_TIEN);
        pnlFormBenTrai.add(lblTongTienDatCoc, gbc);

        thieLapThongTinDatMon();

        JPanel pnlCacNutBam = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        pnlCacNutBam.setBackground(new Color(0xF5F5F5));
        pnlCacNutBam.add(btnXemPhieuDat);
        pnlCacNutBam.add(btnTaoPhieuMoi);
        pnlCacNutBam.add(btnXacNhan);
        pnlCacNutBam.add(btnLuuThayDoi);
        pnlCacNutBam.add(btnBatDauSuDung);
        pnlCacNutBam.add(btnHuy);

        pnlNoiDungChinh.add(pnlFormBenTrai, BorderLayout.WEST);
        pnlNoiDungChinh.add(pnlThongTinDatMon, BorderLayout.CENTER);

        pnlFormDatBan.add(pnlHeaderSoBan, BorderLayout.NORTH);
        pnlFormDatBan.add(pnlNoiDungChinh, BorderLayout.CENTER);
        pnlFormDatBan.add(pnlCacNutBam, BorderLayout.SOUTH);
    }

    private void themTruongForm(JPanel parent, GridBagConstraints gbc, int hang, String nhan, JComponent thanhPhan,
            boolean laTieuDe) {
        gbc.gridy = hang;
        gbc.gridx = 0;
        JLabel lblTruong = new JLabel(nhan);

        if (laTieuDe) {
            lblTruong.setFont(new Font("Arial", Font.BOLD, 18));
            gbc.gridwidth = 2;
            parent.add(lblTruong, gbc);
            gbc.gridwidth = 1;
        } else {
            lblTruong.setFont(new Font("Arial", Font.PLAIN, 16));
            parent.add(lblTruong, gbc);
            if (thanhPhan != null) {
                gbc.gridx = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                thanhPhan.setPreferredSize(new Dimension(280, 35));
                parent.add(thanhPhan, gbc);
                gbc.fill = GridBagConstraints.NONE;
            }
        }
    }

    private void thieLapThongTinDatMon() {
        pnlThongTinDatMon.setLayout(new BorderLayout());
        pnlThongTinDatMon.setBackground(Color.WHITE);
        pnlThongTinDatMon.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(15, 15, 15, 15)));
        pnlThongTinDatMon.setPreferredSize(new Dimension(350, 350));

        JPanel pnlHeaderDatMon = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlHeaderDatMon.setBackground(Color.WHITE);
        JLabel lblTieuDeDatMon = new JLabel("Thông tin đặt món");
        lblTieuDeDatMon.setFont(new Font("Arial", Font.BOLD, 20));
        pnlHeaderDatMon.add(lblTieuDeDatMon);

        pnlDanhSachMonAn.setLayout(new BoxLayout(pnlDanhSachMonAn, BoxLayout.Y_AXIS));
        pnlDanhSachMonAn.setBackground(Color.WHITE);
        JScrollPane scrollMonAn = new JScrollPane(pnlDanhSachMonAn);
        scrollMonAn.setBorder(BorderFactory.createEmptyBorder());

        JPanel pnlTamTinh = new JPanel(new BorderLayout());
        pnlTamTinh.setBackground(Color.WHITE);
        pnlTamTinh.setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel lblTextTamTinh = new JLabel("Tạm tính");
        lblTextTamTinh.setFont(new Font("Arial", Font.BOLD, 16));

        lblGiaTriTamTinh = new JLabel("0 VNĐ");
        lblGiaTriTamTinh.setFont(new Font("Arial", Font.BOLD, 16));
        lblGiaTriTamTinh.setHorizontalAlignment(SwingConstants.RIGHT);

        pnlTamTinh.add(lblTextTamTinh, BorderLayout.WEST);
        pnlTamTinh.add(lblGiaTriTamTinh, BorderLayout.EAST);

        btnThemMonAn.setPreferredSize(new Dimension(200, 35));
        JPanel pnlNutThemMon = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlNutThemMon.setBackground(Color.WHITE);
        pnlNutThemMon.add(btnThemMonAn);

        btnThanhToan.setPreferredSize(new Dimension(200, 35));
        pnlNutThemMon.add(btnThanhToan);

        pnlThongTinDatMon.add(pnlHeaderDatMon, BorderLayout.NORTH);
        pnlThongTinDatMon.add(scrollMonAn, BorderLayout.CENTER);

        JPanel pnlDuoi = new JPanel(new BorderLayout());
        pnlDuoi.setBackground(Color.WHITE);
        pnlDuoi.add(pnlTamTinh, BorderLayout.CENTER);
        pnlDuoi.add(pnlNutThemMon, BorderLayout.SOUTH);
        pnlThongTinDatMon.add(pnlDuoi, BorderLayout.SOUTH);
    }

    private void themMonAn(String tenMon, String soLuong, String gia) {
        JPanel pnlMonAn = new JPanel(new BorderLayout());
        pnlMonAn.setBackground(Color.WHITE);
        pnlMonAn.setBorder(new EmptyBorder(3, 8, 3, 8));
        pnlMonAn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

        JPanel pnlHang = new JPanel();
        pnlHang.setLayout(new BoxLayout(pnlHang, BoxLayout.X_AXIS));
        pnlHang.setBackground(Color.WHITE);

        JLabel lblTenMon = new JLabel(tenMon);
        lblTenMon.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel lblSoLuong = new JLabel(soLuong, SwingConstants.CENTER);
        lblSoLuong.setFont(new Font("Arial", Font.PLAIN, 16));
        lblSoLuong.setPreferredSize(new Dimension(40, 20));

        JLabel lblGia = new JLabel(gia, SwingConstants.RIGHT);
        lblGia.setFont(new Font("Arial", Font.PLAIN, 16));
        lblGia.setPreferredSize(new Dimension(100, 20));

        pnlHang.add(lblTenMon);
        pnlHang.add(Box.createHorizontalGlue());
        pnlHang.add(lblSoLuong);
        pnlHang.add(lblGia);

        pnlMonAn.add(pnlHang, BorderLayout.CENTER);
        pnlDanhSachMonAn.add(pnlMonAn);
    }

    private void capNhatPanelThongTinDatMon() {
        pnlDanhSachMonAn.removeAll();
        double tongTienMonAn = 0;

        if (danhSachMonDaChon != null && !danhSachMonDaChon.isEmpty()) {
            for (ChiTietDatMon ct : danhSachMonDaChon) {
                String ten = ct.getMonAn().getTenMon();
                String sl = "x" + ct.getSoLuong();
                String gia = String.format("%,.0f VNĐ", ct.getMonAn().getGia() * ct.getSoLuong());
                themMonAn(ten, sl, gia);
                tongTienMonAn += ct.getMonAn().getGia() * ct.getSoLuong();
            }

            danhSachMonDaChon = danhSachMonDaChon;
            System.out.println("Số món đã chọn: " + danhSachMonDaChon.size());

        } else {
            JLabel lblChuaCoMon = new JLabel("Chưa có món ăn nào được chọn");
            lblChuaCoMon.setFont(new Font("Arial", Font.ITALIC, 16));
            lblChuaCoMon.setForeground(Color.GRAY);
            lblChuaCoMon.setHorizontalAlignment(SwingConstants.CENTER);
            pnlDanhSachMonAn.add(lblChuaCoMon);
        }

        lblGiaTriTamTinh.setText(String.format("%,.0f VNĐ", tongTienMonAn));

        if (!cheDoHienThi.equals("DANG_SU_DUNG")) {
            int soLuongBan = danhSachBanDaChon.size();
            double phiBan = 250000 * soLuongBan;
            double phiMonAn = tongTienMonAn * 0.3;
            double tienCoc = phiBan + phiMonAn;
            lblTongTienDatCoc.setText(String.format(" %,.0f VNĐ", tienCoc));
        }
        pnlDanhSachMonAn.revalidate();
        pnlDanhSachMonAn.repaint();
    }

    private void thieLapXuLySuKien() {
        // SỰ KIỆN CHUYỂN SANG MÀN CHỌN MÓN
        btnThemMonAn.addActionListener(e -> {
            // Lưu thông tin form vào phieuTamThoi
            this.phieuTamThoi = new PhieuDatBan();
            // Lưu mã phiếu nếu đang xem phiếu
            if (maPhieuDangXem != null) {
                this.phieuTamThoi.setMaPhieu(maPhieuDangXem);
            }
            this.phieuTamThoi.setTenKhachDat(txtTenKhachHang.getText());
            this.phieuTamThoi.setSdtDat(txtSoDienThoai.getText());
            this.phieuTamThoi.setEmailDat(txtEmail.getText());
            this.phieuTamThoi.setGioDat(new SimpleDateFormat("HH:mm").format((Date) spinnerGioDat.getValue()));
            this.phieuTamThoi.setNgayDat(new SimpleDateFormat("dd/MM/yyyy").format(dateChooserNgayDat.getDate()));
            this.phieuTamThoi.setPhuongThucThanhToan((String) cboPhuongThucThanhToan.getSelectedItem());
            this.phieuTamThoi.setDanhSachBanDaChon(new ArrayList<>(danhSachBanDaChon));
            this.phieuTamThoi.setDanhSachMonAn(new ArrayList<>(danhSachMonDaChon));
            // Lưu trạng thái thành viên và checkbox
            this.isThanhVienTamThoi = lblTrangThaiThanhVien.getText().equals("Đã là khách hàng thành viên");
            this.isCheckboxThanhVienTamThoi = chkThemThanhVien.isSelected();
            Component gui = GUIManager.getInstance().switchToGUI(DanhSachMonAnNV_GUI.class, this);
            if (gui instanceof DanhSachMonAnNV_GUI) {
                ((DanhSachMonAnNV_GUI) gui).setData(
                    banHienTai.getMaBan(),
                    new ArrayList<>(danhSachBanDaChon),
                    ngayDatTuChonBan,
                    this.phieuTamThoi,
                    this.isThanhVienTamThoi,
                    this.isCheckboxThanhVienTamThoi
                );
            }
        });
        // Nút SDT DocumentListener...
        txtSoDienThoai.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkSDT();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkSDT();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkSDT();
            }

            private void checkSDT() {
                // Đẩy việc xử lý ra cuối hàng đợi sự kiện để đảm bảo Document đã cập nhật xong
                SwingUtilities.invokeLater(() -> {
                    try {
                        String sdt = txtSoDienThoai.getText().trim();

                        // 1. Kiểm tra cơ bản: Rỗng hoặc là Placeholder
                        if (sdt.isEmpty() || sdt.equals("Nhập số điện thoại")) {
                            lblTrangThaiThanhVien.setText("Chưa là khách hàng thành viên");
                            lblTrangThaiThanhVien.setForeground(Color.RED);
                            return;
                        }

                        // 2. Tối ưu: Chỉ kiểm tra Database khi đủ độ dài (ví dụ >= 10 số)
                        // Điều này giúp tránh gọi DB khi người dùng mới gõ "09..."
                        if (sdt.length() >= 10) {
                            boolean exists = khachHangCtr.existsBySoDienThoai(sdt);
                            if (exists) {
                                lblTrangThaiThanhVien.setText("Đã là khách hàng thành viên");
                                lblTrangThaiThanhVien.setForeground(MAU_XAC_NHAN); // Màu xanh
                            } else {
                                lblTrangThaiThanhVien.setText("Chưa là khách hàng thành viên");
                                lblTrangThaiThanhVien.setForeground(Color.RED);
                            }
                        } else {
                            // Nếu chưa đủ số thì reset về mặc định
                            lblTrangThaiThanhVien.setText("Chưa là khách hàng thành viên");
                            lblTrangThaiThanhVien.setForeground(Color.RED);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace(); // In lỗi ra console nếu có lỗi kết nối DB
                    }
                });
            }
        });

        btnChonThemBan.addActionListener(e -> moDialogChonThemBan());

        // --- XỬ LÝ SỰ KIỆN NÚT XEM PHIẾU ĐẶT ---
        btnXemPhieuDat.addActionListener(e -> {
            Date ngayDatDate = dateChooserNgayDat.getDate();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String ngayDat = sdf.format(ngayDatDate);

            Window window = SwingUtilities.getWindowAncestor(this);
            XemPhieuDat_Dialog dialog = new XemPhieuDat_Dialog(window, soBan, ngayDatTuChonBan, this);
            dialog.setVisible(true);
        });

        // --- XỬ LÝ SỰ KIỆN NÚT TẠO PHIẾU MỚI ---
        btnTaoPhieuMoi.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc muốn tạo phiếu mới?\nDữ liệu hiện tại sẽ bị xóa.",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                resetForm();
            }
        });

        // --- XỬ LÝ SỰ KIỆN NÚT XÁC NHẬN ---
        btnXacNhan.addActionListener(e -> {
            String tenKH = txtTenKhachHang.getText().trim();
            if (tenKH.isEmpty() || tenKH.equals("Nhập họ tên")) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên khách hàng!", "Thiếu thông tin",
                        JOptionPane.WARNING_MESSAGE);
                txtTenKhachHang.requestFocus();
                return;
            }

            String sdt = txtSoDienThoai.getText().trim();
            if (sdt.isEmpty() || sdt.equals("Nhập số điện thoại")) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại!", "Thiếu thông tin",
                        JOptionPane.WARNING_MESSAGE);
                txtSoDienThoai.requestFocus();
                return;
            }

            if (!sdt.matches("^0\\d{9,10}$")) {
                JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ!", "Lỗi định dạng",
                        JOptionPane.ERROR_MESSAGE);
                txtSoDienThoai.requestFocus();
                return;
            }

            String email = txtEmail.getText().trim();
            if (email.equals("Nhập email"))
                email = "";

            String gioiTinh = (String) cboGioiTinh.getSelectedItem();

            // === LOGIC CỐT LÕI: CHỈ THÊM DB NẾU CHECKBOX ĐƯỢC CHỌN ===
            if (chkThemThanhVien.isSelected()) {
                // 1. Kiểm tra xem đã tồn tại trong DB chưa
                if (khachHangCtr.existsBySoDienThoai(sdt)) {
                    // Nếu đã tồn tại -> Không thêm mới, chỉ báo lỗi (hoặc có thể thông báo là dùng
                    // thông tin cũ)
                    // Ở đây chọn cách báo lỗi để người dùng biết
                    JOptionPane.showMessageDialog(this,
                            "Số điện thoại này đã là thành viên rồi!\nHệ thống sẽ sử dụng thông tin thành viên cũ.",
                            "Thông báo",
                            JOptionPane.INFORMATION_MESSAGE);
                    // Không return, vẫn cho phép đặt bàn
                } else {
                    // 2. Nếu chưa tồn tại -> Thêm mới vào DB
                    String maKH = khachHangCtr.taoMaKhachHangMoi();
                    KhachHangThanhVien khMoi = new KhachHangThanhVien(maKH, tenKH, sdt, email, gioiTinh, "Đồng", 0,
                            LocalDate.now());

                    boolean themThanhCong = khachHangCtr.themKhachHang(khMoi);
                    if (themThanhCong) {
                        JOptionPane.showMessageDialog(this, "Đã thêm thành viên mới thành công: " + maKH);
                        // Cập nhật lại trạng thái hiển thị
                        lblTrangThaiThanhVien.setText("Đã là khách hàng thành viên");
                        lblTrangThaiThanhVien.setForeground(MAU_XAC_NHAN);
                    } else {
                        JOptionPane.showMessageDialog(this, "Lỗi khi thêm thành viên!", "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                        return; // Dừng lại nếu thêm thất bại
                    }
                }
            }
            // === NẾU KHÔNG CHECK: BỎ QUA BƯỚC TRÊN, CHỈ ĐẶT BÀN ===

            Date ngayDatDate = dateChooserNgayDat.getDate();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String ngayDat = sdf.format(ngayDatDate);

            Date gioDatDate = (Date) spinnerGioDat.getValue();
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            String gioDat = timeFormat.format(gioDatDate);

            try {
                java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter
                        .ofPattern("dd/MM/yyyy");
                LocalDate ngayDatLocal = LocalDate.parse(ngayDat, dateFormatter);
                java.time.LocalTime gioDatLocal = java.time.LocalTime.parse(gioDat);
                java.time.LocalDateTime thoiGianDat = java.time.LocalDateTime.of(ngayDatLocal, gioDatLocal);
                java.time.LocalDateTime thoiGianHienTai = java.time.LocalDateTime.now();

                if (ngayDatLocal.isBefore(LocalDate.now())) {
                    JOptionPane.showMessageDialog(this, "Ngày đặt không được nhỏ hơn ngày hiện tại!", "Lỗi ngày đặt",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (ngayDatLocal.isEqual(LocalDate.now()) && thoiGianDat.isBefore(thoiGianHienTai)) {
                    JOptionPane.showMessageDialog(this, "Thời gian đặt phải sau thời gian hiện tại!", "Lỗi thời gian",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ngày hoặc giờ đặt không hợp lệ!", "Lỗi định dạng",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String phuongThuc = (String) cboPhuongThucThanhToan.getSelectedItem();

            // === KIỂM TRA TRÙNG LỊCH CHO TẤT CẢ BÀN ĐÃ CHỌN ===
            ArrayList<PhieuDatBan> phieuDatTrongNgay = phieuDatBanCtr.layPhieuDatTheoNgay(ngayDat);
            ArrayList<String> banBiTrung = new ArrayList<>();

            System.out.println("\n=== KIỂM TRA TRÙNG LỊCH TRƯỚC KHI ĐẶT BÀN ===");
            System.out.println("Số bàn đã chọn: " + danhSachBanDaChon.size());
            System.out.println("Danh sách bàn: " + danhSachBanDaChon);
            System.out.println("Giờ đặt: " + gioDat);
            System.out.println("Ngày đặt: " + ngayDat);
            System.out.println("Số phiếu trong ngày: " + phieuDatTrongNgay.size());

            for (Integer maBan : danhSachBanDaChon) {
                System.out.println("\nKiểm tra bàn " + maBan + "...");

                for (PhieuDatBan phieu : phieuDatTrongNgay) {
                    // Bỏ qua phiếu hiện tại (nếu đang sửa)
                    if (phieuHienTai != null && phieu.getMaPhieu() != null &&
                            phieu.getMaPhieu().equals(phieuHienTai.getMaPhieu())) {
                        System.out.println("  - Bỏ qua phiếu hiện tại: " + phieu.getMaPhieu());
                        continue;
                    }

                    // Chỉ kiểm tra phiếu đang hoạt động
                    String trangThai = phieu.getTrangThai();
                    System.out.println("  - Phiếu " + phieu.getMaPhieu() + " - Trạng thái: " + trangThai + " - Giờ: "
                            + phieu.getGioDat());

                    if ("Đặt trước".equals(trangThai) || "Đã xác nhận".equals(trangThai)) {
                        ArrayList<Integer> dsBanPhieu = (ArrayList<Integer>) phieu.getDanhSachBan();
                        System.out.println("  - Danh sách bàn trong phiếu: " + dsBanPhieu);

                        if (dsBanPhieu != null && dsBanPhieu.contains(maBan)) {
                            String gioPhieu = phieu.getGioDat();
                            System.out.println("  - Bàn " + maBan + " có trong phiếu! So sánh giờ: '" + gioPhieu
                                    + "' vs '" + gioDat + "'");

                            // Kiểm tra khung giờ cách nhau 2 tiếng
                            try {
                                java.time.LocalTime thoiGianDangDat = java.time.LocalTime.parse(gioDat);
                                java.time.LocalTime thoiGianPhieu = java.time.LocalTime.parse(gioPhieu.trim());

                                long phutChenhLech = Math
                                        .abs(java.time.Duration.between(thoiGianDangDat, thoiGianPhieu).toMinutes());
                                System.out.println("  - Khoảng cách: " + phutChenhLech + " phút");

                                // Nếu cách nhau < 2 tiếng (120 phút) -> trùng khung giờ
                                if (phutChenhLech < 120) {
                                    System.out.println("  - *** TRÙNG KHUNG GIỜ (cách nhau < 2 tiếng)! ***");
                                    banBiTrung.add("Bàn " + maBan + " (đã đặt lúc " + gioPhieu +
                                            " - cách " + (phutChenhLech / 60.0) + " tiếng - Phiếu: "
                                            + phieu.getMaPhieu() + ")");
                                    break;
                                } else {
                                    System.out.println("  - Cách nhau >= 2 tiếng, OK!");
                                }
                            } catch (Exception ex) {
                                System.out.println("  - Lỗi parse giờ: " + ex.getMessage());
                            }
                        }
                    }
                }
            }

            // Nếu có bàn bị trùng giờ, hiển thị lỗi và dừng lại
            if (!banBiTrung.isEmpty()) {
                System.out.println("\n!!! CÓ BÀN BỊ TRÙNG LỊCH !!!");
                String danhSachBanTrung = String.join("\n", banBiTrung);
                JOptionPane.showMessageDialog(this,
                        "Các bàn sau đã có người đặt vào giờ này:\n\n" + danhSachBanTrung +
                                "\n\nVui lòng chọn bàn khác hoặc chọn giờ khác!",
                        "Trùng lịch đặt bàn",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            System.out.println("\n✓ Không có bàn nào bị trùng lịch. Tiếp tục đặt bàn...\n");

            // Kiểm tra trùng lặp khung giờ (cách nhau ít nhất 2 tiếng)
            if (!kiemTraKhungGioHopLe(gioDat, ngayDat)) {
                JOptionPane.showMessageDialog(this,
                        "Khung giờ này đã có người đặt!\nVui lòng chọn khung giờ khác (cách nhau ít nhất 2 tiếng).",
                        "Trùng lịch",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            ArrayList<Integer> banTrungLich = phieuDatBanCtr.layDanhSachBanTrungLich(danhSachBanDaChon, ngayDat,
                    gioDat);
            if (!banTrungLich.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Bàn đã có người đặt vào khung giờ này!", "Trùng lịch",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            PhieuDatBan phieuTam = new PhieuDatBan();
            phieuTam.setDanhSachMonAn(this.danhSachMonDaChon);
            // Tính tiền cọc trực tiếp thay vì gọi phương thức không tồn tại
            int soLuongBan = danhSachBanDaChon.size();
            double phiBan = 250000 * soLuongBan;
            double tienMonAn = 0;
            if (danhSachMonDaChon != null && !danhSachMonDaChon.isEmpty()) {
                for (ChiTietDatMon ct : danhSachMonDaChon) {
                    tienMonAn += ct.getMonAn().getGia() * ct.getSoLuong();
                }
            }
            double phiMonAn = tienMonAn * 0.3;
            double tienCoc = phiBan + phiMonAn;
            String tienCocFormatted = String.format("%,.0f VNĐ", tienCoc);

            String thongBao = "<html><body style='width: 250px;'>Xác nhận thông tin đặt bàn<br>"
                    + "<b>Tổng tiền đặt cọc: <font color='red'>" + tienCocFormatted + "</font></b><br>"
                    + "- Tên: " + tenKH + "<br>"
                    + "- SĐT: " + sdt + "<br>"
                    + "- Email: " + (email.isEmpty() ? "Chưa có" : email) + "<br>"
                    + "- Ngày: " + ngayDat + " " + gioDat
                    + "</body></html>";

            Window window = SwingUtilities.getWindowAncestor(this);
            phieuDat_Dialog dialog = new phieuDat_Dialog(window, thongBao);
            dialog.setVisible(true);
            HoaDon_DAO hoaDonDAO = new HoaDon_DAO();
            HoaDon_Ctrl hoaDonCtr = new HoaDon_Ctrl();

            int luaChon = dialog.getLuaChon();

            if (luaChon != phieuDat_Dialog.HUY) {
                PhieuDatBan pd = thucHienLuuPhieuDat(tenKH, sdt, ngayDat, gioDat, phuongThuc);
                HoaDon hoaDonMoi = new HoaDon(pd);

                // 3. Thực hiện theo lựa chọn
                if (luaChon == phieuDat_Dialog.THANH_TOAN) {
                    // Chỉ lưu vào DB
                    boolean kq = hoaDonDAO.addHoaDon(hoaDonMoi);
                    if (kq) {
                        JOptionPane.showMessageDialog(this, "Đã thanh toán và lưu hóa đơn thành công!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Lỗi khi lưu hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }

                } else if (luaChon == phieuDat_Dialog.THANH_TOAN_VA_IN) {
                    // 1. Lưu hóa đơn vào DB trước
                    boolean kqLuu = hoaDonDAO.addHoaDon(hoaDonMoi);

                    if (kqLuu) {
                        try {
                            // 2. Chuẩn bị đường dẫn file PDF
                            String pathFolder = "src/HoaDon_DatTruoc_PDF";
                            File folder = new File(pathFolder);
                            if (!folder.exists())
                                folder.mkdirs();

                            String tenFile = "HoaDon_" + pd.getMaBan() + "_" + System.currentTimeMillis() + ".pdf";
                            String fullPath = pathFolder + "/" + tenFile;

                            // 3. Tạo file PDF
                            boolean kqXuatPDF = hoaDonCtr.xuatPDF(hoaDonMoi, fullPath);

                            if (kqXuatPDF) {
                                String thongBaoIn = "Đã lưu hóa đơn tại: " + fullPath;

                                // 4. GỌI PRINTER SERVICE ĐỂ IN
                                String tenMayIn = "XP-58"; // Tên máy in của bạn
                                boolean inThanhCong = PrinterService.printPDF(fullPath, tenMayIn);

                                if (inThanhCong) {
                                    thongBaoIn += "\n(Đã gửi lệnh in tới " + tenMayIn + ")";
                                } else {
                                    thongBaoIn += "\n(Lỗi: Không tìm thấy máy in hoặc in thất bại)";
                                    // Fallback: Mở file PDF lên nếu in tự động thất bại
                                    // if (Desktop.isDesktopSupported()) {
                                    // Desktop.getDesktop().open(new File(fullPath));
                                    // }
                                }

                                JOptionPane.showMessageDialog(this,
                                        "THANH TOÁN & IN THÀNH CÔNG!\n" + thongBaoIn,
                                        "Thành công",
                                        JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(this, "Lỗi khi tạo file PDF!", "Lỗi",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(this, "Lỗi hệ thống khi in!", "Lỗi",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Lỗi khi lưu hóa đơn vào CSDL!", "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                GUIManager.getInstance().switchToGUI(ChonBan_GUI.class, PhieuDat_GUI.this);
            } else
                JOptionPane.showMessageDialog(this, "Đặt bàn không thành công");

        });

        btnHuy.addActionListener(e -> {
            if (cheDoHienThi.equals("DANG_SU_DUNG")) {
                int ketQua = JOptionPane.showConfirmDialog(this, "Kết thúc sử dụng bàn?", "Xác nhận",
                        JOptionPane.YES_NO_OPTION);
                if (ketQua == JOptionPane.YES_OPTION) {
                    if (phieuHienTai != null)
                        phieuDatBanCtr.xoaPhieuDat(banHienTai.getMaBan());
                    GUIManager.getInstance().switchToGUI(ChonBan_GUI.class, PhieuDat_GUI.this);
                }
            } else if (cheDoHienThi.equals("XEM_PHIEU")) {
                int ketQua = JOptionPane.showConfirmDialog(this, "Hủy đặt bàn này?", "Xác nhận",
                        JOptionPane.YES_NO_OPTION);
                if (ketQua == JOptionPane.YES_OPTION) {
                    // Cập nhật trạng thái phiếu thành "Đã hủy" thay vì xóa
                    boolean thanhCong = phieuDatBanCtr.capNhatTrangThaiPhieu(maPhieuDangXem, "Đã hủy");
                    if (thanhCong) {
                        System.out.println("Đã hủy phiếu đặt bàn thành công!");
                        phieuDatBanCtr.xoaGioHangTamThoi(banHienTai.getMaBan());
                        GUIManager.getInstance().switchToGUI(ChonBan_GUI.class, PhieuDat_GUI.this);
                    } else {
                        JOptionPane.showMessageDialog(this, "Lỗi khi hủy phiếu đặt!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else if (cheDoHienThi.equals("DAT_MOI")) {
                // Không xóa giỏ hàng khi nhấn Hủy ở màn hình đặt mới, chỉ quay lại chọn bàn
                // để món ăn vẫn được lưu trong controller
                GUIManager.getInstance().switchToGUI(ChonBan_GUI.class, PhieuDat_GUI.this);
            }
        });

        // XỬ LÝ NÚT LƯU THAY ĐỔI - Cập nhật món ăn vào DB
        btnLuuThayDoi.addActionListener(e -> {
            // Lấy mã phiếu từ phiếu hiện tại hoặc mã phiếu đang xem
            String maPhieu = (phieuHienTai != null) ? phieuHienTai.getMaPhieu() : maPhieuDangXem;

            if (maPhieu == null || maPhieu.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy mã phiếu để cập nhật!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Xác nhận trước khi cập nhật
            int ketQua = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc muốn lưu thay đổi thông tin và món ăn?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION);

            if (ketQua == JOptionPane.YES_OPTION) {
                // Lấy thông tin từ form
                String tenKhach = txtTenKhachHang.getText().trim();
                String sdt = txtSoDienThoai.getText().trim();
                String email = txtEmail.getText().trim();
                if (email.equals("Nhập email"))
                    email = "";
                String gioiTinh = (String) cboGioiTinh.getSelectedItem();

                // === XỬ LÝ THÊM THÀNH VIÊN NẾU CHECKBOX ĐƯỢC CHỌN ===
                if (chkThemThanhVien.isSelected() && sdt != null && !sdt.isEmpty()) {
                    // Kiểm tra xem đã tồn tại trong DB chưa
                    if (khachHangCtr.existsBySoDienThoai(sdt)) {
                        JOptionPane.showMessageDialog(this,
                                "Số điện thoại này đã là thành viên rồi!",
                                "Thông báo",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        // Thêm mới thành viên vào DB
                        String maKH = khachHangCtr.taoMaKhachHangMoi();
                        KhachHangThanhVien khMoi = new KhachHangThanhVien(maKH, tenKhach, sdt, email, gioiTinh, "Đồng",
                                0, LocalDate.now());

                        boolean themThanhCong = khachHangCtr.themKhachHang(khMoi);
                        if (themThanhCong) {
                            JOptionPane.showMessageDialog(this, "Đã thêm thành viên mới thành công: " + maKH,
                                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
                            // Cập nhật mã khách hàng vào phiếu
                            if (phieuHienTai != null) {
                                phieuHienTai.setMaKhachHang(maKH);
                            }
                            // Cập nhật lại label trạng thái
                            if (lblTrangThaiThanhVien != null) {
                                lblTrangThaiThanhVien.setText("Đã là khách hàng thành viên");
                                lblTrangThaiThanhVien.setForeground(MAU_XAC_NHAN);
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Lỗi khi thêm thành viên!", "Lỗi",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }

                // Cập nhật thông tin khách hàng vào phiếu
                if (phieuHienTai != null) {
                    phieuHienTai.setTenKhachHang(tenKhach);
                    phieuHienTai.setSdtDat(sdt);
                    phieuHienTai.setEmailDat(email);
                }

                // Cập nhật thông tin khách hàng vào DB
                PhieuDat_DAO phieuDAO = new PhieuDat_DAO();
                phieuDAO.capNhatThongTinKhachHang(maPhieu, tenKhach, sdt, email);

                // Cập nhật món ăn - Ưu tiên danhSachMonDaChon (đã được cập nhật từ controller)
                ArrayList<ChiTietDatMon> danhSachMon = new ArrayList<>(danhSachMonDaChon);

                System.out.println("=== LƯU THAY ĐỔI ===");
                System.out.println("Số món trong danhSachMonDaChon: " + danhSachMonDaChon.size());
                System.out.println("Số món trong danhSachMonDaChon: "
                        + (danhSachMonDaChon != null ? danhSachMonDaChon.size() : "null"));

                boolean thanhCong = phieuDatBanCtr.capNhatMonAnCuaPhieu(maPhieu, rawDanhSachMon(danhSachMon));

                if (thanhCong) {
                    // Cập nhật danh sách món vào phiếu hiện tại
                    if (phieuHienTai != null) {
                        phieuHienTai.setDanhSachMonAn(new ArrayList<>(danhSachMon));
                    }

                    // Cập nhật vào controller
                    phieuDatBanCtr.capNhatDanhSachMonAn(soBan, rawDanhSachMon(danhSachMon));

                    System.out.println("Đã cập nhật thông tin thành công!");

                    // Cập nhật lại tiền cọc sau khi thay đổi món (chỉ cho khách đặt trước)
                    if (phieuHienTai != null && phieuHienTai.getTienCoc() > 0) {
                        double tienMonAn = 0;
                        if (danhSachMon != null && !danhSachMon.isEmpty()) {
                            for (ChiTietDatMon ct : danhSachMon) {
                                tienMonAn += ct.getMonAn().getGia() * ct.getSoLuong();
                            }
                        }
                        int soLuongBan = danhSachBanDaChon != null ? danhSachBanDaChon.size() : 1;
                        double phiBan = 250000 * soLuongBan;
                        double phiMonAn = tienMonAn * 0.3;
                        double tienCocMoi = phiBan + phiMonAn;

                        lblTongTienDatCoc.setText(String.format(" %,.0f VNĐ", tienCocMoi));
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật thông tin!", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnBatDauSuDung.addActionListener(e -> {
            int ketQua = JOptionPane.showConfirmDialog(this,
                    "Bắt đầu sử dụng bàn " + banHienTai.getMaBanFormatted() + "?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION);

            if (ketQua == JOptionPane.YES_OPTION) {
                System.out.println("=== BẮT ĐẦU SỬ DỤNG ===");
                System.out.println("phieuHienTai: " + (phieuHienTai != null ? phieuHienTai.getMaPhieu() : "null"));

                // Biến để đánh dấu có phải phiếu đặt trước không
                boolean coPhieuDatTruoc = false;

                // 2. --- TẠO VÀ LƯU PHIẾU ĐẶT NGAY TẠI ĐÂY (QUAN TRỌNG) ---

                // Nếu chưa có phiếu (Khách vãng lai vào bàn trống)
                if (phieuHienTai == null) {
                    System.out.println("=== TẠO PHIẾU MỚI CHO KHÁCH VÃNG LAI ===");
                    PhieuDatBan phieuVangLai = new PhieuDatBan();

                    // Điền thông tin mặc định
                    phieuVangLai.setMaBan(soBan);
                    // Tạo danh sách bàn chứa chính nó (để tránh lỗi null pointer khi tính tiền)
                    ArrayList<Integer> dsBan = new ArrayList<>();
                    dsBan.add(soBan);
                    phieuVangLai.setDanhSachMaBan(dsBan);

                    String tenKhach = txtTenKhachHang.getText().trim();
                    String sdt = txtSoDienThoai.getText().trim();
                    String email = txtEmail.getText().trim();
                    String sdtTV = txtSoDienThoaiThanhVien.getText().trim();

                    // Xử lý Placeholder (nếu người dùng chưa nhập gì)
                    if (tenKhach.equals("Nhập họ tên") || tenKhach.isEmpty()) {
                        tenKhach = "Khách vãng lai";
                    }
                    if (sdt.equals("Nhập số điện thoại")) {
                        sdt = "";
                    }
                    if (email.equals("Nhập email")) {
                        email = "";
                    }

                    // Xử lý Mã Khách Hàng
                    String maKhachHang = null;
                    if (!sdtTV.isEmpty()) {
                        KhachHang_DAO khDao = new KhachHang_DAO();
                        maKhachHang = khDao.getmaKhachHangbySDT(sdtTV);

                        // Debug kiểm tra
                        System.out.println("SĐT: " + sdtTV + " -> Mã KH tìm được: " + maKhachHang);
                    }

                    // Set thông tin vào phiếu
                    phieuVangLai.setMaKhachHang(maKhachHang);
                    phieuVangLai.setTenKhachHang(tenKhach);
                    phieuVangLai.setSdtDat(sdt);
                    phieuVangLai.setEmailDat(email);
                    // Set danh sách món đã chọn (QUAN TRỌNG)
                    phieuVangLai.setDanhSachMonAn(new ArrayList<>(danhSachMonDaChon));
                    
                    // Set ngày giờ hiện tại
                    SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
                    Date now = new Date();
                    phieuVangLai.setNgayDat(sdfDate.format(now));
                    phieuVangLai.setGioDat(sdfTime.format(now));
                    phieuVangLai.setThoiGianDat(new java.sql.Timestamp(now.getTime()));

                    phieuVangLai.setTrangThai("Đang sử dụng");
                    phieuVangLai.setTienCoc(0);
                    phieuVangLai.setPhuongThucThanhToan((String) cboPhuongThucThanhToan.getSelectedItem());

                    // Lấy mã nhân viên hiện tại
                    if (User_Ctr.getInstance().getNhanVienHienTai() != null) {
                        phieuVangLai.setMaNhanVien(User_Ctr.getInstance().getNhanVienHienTai().getManv());
                    }

                    // GỌI HÀM THÊM ĐỂ LƯU VÀO CONTROLLER/DB
                    PhieuDatBan_Ctr.getInstance().themPhieuDat(phieuVangLai);

                    // Gán ngược lại vào biến hiện tại để dùng tiếp nếu không đóng form
                    this.phieuHienTai = phieuVangLai;
                    coPhieuDatTruoc = false; // Không phải phiếu đặt trước

                } else {
                    // Nếu đã có phiếu (Đặt trước -> Khách đến)
                    System.out.println("=== CẬP NHẬT PHIẾU ĐẶT TRƯỚC: " + phieuHienTai.getMaPhieu() + " ===");
                    coPhieuDatTruoc = true; // CÓ phiếu đặt trước
                    phieuHienTai.setTrangThai("Đang sử dụng");

                    // CẬP NHẬT TRẠNG THÁI VÀO DB
                    if (phieuHienTai.getMaPhieu() != null && !phieuHienTai.getMaPhieu().isEmpty()) {
                        PhieuDat_DAO phieuDAO = new PhieuDat_DAO();
                        boolean updated = phieuDAO.capNhatTrangThai(phieuHienTai.getMaPhieu(), "Đang sử dụng");
                        System.out.println(
                                "=== Cập nhật DB trạng thái: " + (updated ? "Thành công" : "Thất bại") + " ===");
                    }

                    // Load danh sách món ăn từ phiếu
                    if (phieuHienTai.getDanhSachMonAn() != null && !phieuHienTai.getDanhSachMonAn().isEmpty()) {
                        this.danhSachMonDaChon = new ArrayList<>(phieuHienTai.getDanhSachMonAn());
                        phieuDatBanCtr.capNhatDanhSachMonAn(soBan, rawDanhSachMon(danhSachMonDaChon));
                        System.out.println("=== Load " + danhSachMonDaChon.size() + " món ăn từ phiếu ===");
                    }
                }

                System.out.println("=== coPhieuDatTruoc: " + coPhieuDatTruoc + " ===");

                // Kiểm tra: Nếu có phiếu đặt trước thì giữ form, chỉ ẩn/hiện nút
                if (coPhieuDatTruoc) {
                    System.out.println("=== GIỮ FORM VÀ ẨN/HIỆN NÚT ===");
                    // GIỮ FORM, chỉ ẩn/hiện nút theo yêu cầu
                    cheDoHienThi = "DANG_SU_DUNG";
                    thietLapGiaoDienTheoTrangThai("DANG_SU_DUNG");

                    // Render lại món ăn lên giao diện
                    capNhatPanelThongTinDatMon();

                    System.out.println("Đã bắt đầu sử dụng bàn thành công!");
                } else {
                    System.out.println("=== GIỮ FORM CHO KHÁCH VÃNG LAI ===");
                    // Khách vãng lai -> GIỮ FORM để có thể thêm món và cập nhật thông tin
                    cheDoHienThi = "DANG_SU_DUNG";
                    thietLapGiaoDienTheoTrangThai("DANG_SU_DUNG");

                    // Render lại món ăn lên giao diện (nếu có)
                    capNhatPanelThongTinDatMon();

                    System.out.println("Bàn vãng lai đã sẵn sàng sử dụng!");
                }
            }
        });

        btnThanhToan.addActionListener(e -> {
            // Kiểm tra phiếu phải có trạng thái "Đã xác nhận" hoặc bàn "Đang sử dụng"
            BanAn banHienTaiCheck = banAnCtr.timBanTheoMa(soBan);
            boolean coBanDangSuDung = (banHienTaiCheck != null);
            boolean coPhieuDaXacNhan = (phieuHienTai != null && "Đã xác nhận".equals(phieuHienTai.getTrangThai()));

            if (!coBanDangSuDung && !coPhieuDaXacNhan) {
                JOptionPane.showMessageDialog(this,
                        "Chỉ có thể Thanh toán khi bàn đang sử dụng hoặc phiếu đã xác nhận!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Tìm phiếu đang sử dụng hoặc đã xác nhận cho bàn này
            if (phieuHienTai == null) {
                // Tìm phiếu "Đã xác nhận" hoặc bàn "Đang sử dụng"
                phieuHienTai = phieuDatBanCtr.layPhieuDangSuDungTheoMaBan(soBan);

                // Nếu vẫn null, tìm phiếu theo logic cũ
                if (phieuHienTai == null) {
                    phieuHienTai = phieuDatBanCtr.timPhieuTheoMaBan(soBan);
                }
            }

            // Đảm bảo phiếu có trạng thái phù hợp để thanh toán
            if (phieuHienTai != null && "Đặt trước".equals(phieuHienTai.getTrangThai())) {
                phieuHienTai.setTrangThai("Đã xác nhận");
            }

            // 3. Kiểm tra và cập nhật dữ liệu (Phần này giữ nguyên, nhưng giờ phieuHienTai
            // đã có dữ liệu)
            if (phieuHienTai != null) {
                phieuHienTai.setDanhSachMonAn(this.danhSachMonDaChon);
                phieuHienTai.setTenKhachHang(txtTenKhachHang.getText());
                phieuHienTai.setSoDienThoai(txtSoDienThoai.getText());
                
                // Bổ sung: Ghi nhận Khách hàng thành viên nếu thu ngân nhập SDT lúc thanh toán
                String sdtTV = txtSoDienThoaiThanhVien.getText().trim();
                if (!sdtTV.isEmpty() && !sdtTV.equals("Nhập số điện thoại") && !sdtTV.equals("Nhập SĐT thành viên")) {
                    KhachHang_DAO khDao = new KhachHang_DAO();
                    String maKhachHang = khDao.getmaKhachHangbySDT(sdtTV);
                    if (maKhachHang != null) {
                        phieuHienTai.setMaKhachHang(maKhachHang);
                    }
                }

                HoaDon hoaDonMoi = new HoaDon();
                hoaDonMoi.setPhieuDat(this.phieuHienTai);

                XacNhanHoaDon_GUI guiXacNhan = new XacNhanHoaDon_GUI(hoaDonMoi);
                if (this.khuyenMaiDaApDung != null) {
                    guiXacNhan.setKhuyenMaiDaApDung(this.khuyenMaiDaApDung);
                }
                guiXacNhan.setFrameCha(this);
                guiXacNhan.setVisible(true);

            } else {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy phiếu đặt cho bàn này!\n(Code lỗi: Phiếu null tại btnThanhToan)", "Lỗi dữ liệu",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private ArrayList<ChiTietDatMon> rawDanhSachMon(ArrayList<ChiTietDatMon> danhSachMon) {
        return danhSachMon;
    }

    private PhieuDatBan thucHienLuuPhieuDat(String tenKH, String sdt, String ngayDat, String gioDat,
            String phuongThuc) {
        String emailDat = txtEmail.getText().trim();
        if (emailDat.equals("Nhập email")) {
            emailDat = "";
        }

        System.out.println("Danh sách món đã chọn: " + danhSachMonDaChon);

        PhieuDatBan_Ctr phieuDatBanCtr = PhieuDatBan_Ctr.getInstance();
        PhieuDatBan phieuDatMoi = phieuDatBanCtr.taoPhieuDat(tenKH, sdt, ngayDat, gioDat, phuongThuc, emailDat,
                danhSachBanDaChon, rawDanhSachMon(danhSachMonDaChon), 0);
        if (phieuDatMoi != null) {
            System.out.println("Lưu phiếu đặt bàn thành công!");

            // Reset giỏ hàng tạm thời cho tất cả các bàn đã chọn
            for (Integer maBan : danhSachBanDaChon) {
                phieuDatBanCtr.xoaGioHangTamThoi(maBan);
            }

            // Hiển thị thông báo thành công vào console thay vì dialog thừa
            System.out.println("Đặt bàn thành công! Mã phiếu: " + PhieuDatBan_Ctr.taoMaPhieu(ngayDat, gioDat, sdt));
            return phieuDatMoi;
        } else {
            JOptionPane.showMessageDialog(this, "Lưu phiếu đặt bàn thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }

    }

    private void hienThiThongTinPhieuDat() {
        if (phieuHienTai == null)
            return;
        txtTenKhachHang.setText(phieuHienTai.getTenKhachDat());
        txtSoDienThoai.setText(phieuHienTai.getSdtDat());
        // Hiển thị email nếu có
        if (phieuHienTai.getEmailDat() != null && !phieuHienTai.getEmailDat().isEmpty()) {
            txtEmail.setText(phieuHienTai.getEmailDat());
        } else {
            txtEmail.setText("Nhập email");
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date ngay = sdf.parse(phieuHienTai.getNgayDat());
            dateChooserNgayDat.setDate(ngay);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Load giờ đặt
        try {
            if (phieuHienTai.getGioDat() != null && !phieuHienTai.getGioDat().isEmpty()) {
                // Parse giờ từ String "HH:mm" thành Date
                SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
                Date gio = sdfTime.parse(phieuHienTai.getGioDat());
                spinnerGioDat.setValue(gio);
            } else if (phieuHienTai.getThoiGianDat() != null) {
                // Fallback: load từ thoiGianDat nếu gioDat không có
                Date gio = new Date(phieuHienTai.getThoiGianDat().getTime());
                spinnerGioDat.setValue(gio);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        double tienCocPhieu = phieuHienTai.getTienCoc();
        lblTongTienDatCoc.setText(String.format(" %,.0f VNĐ", tienCocPhieu));
        btnChonThemBan.setVisible(true);

        // Mặc định hiển thị label và combo phương thức (sẽ bị ẩn sau nếu là khách vãng
        // lai)
        lblNhanTongTien.setVisible(true);
        lblTongTienDatCoc.setVisible(true);
        cboPhuongThucThanhToan.setVisible(true);
    }

    /**
     * Thiết lập giao diện (nút bấm, độ khả dụng của field) dựa trên trạng thái phiếu
     * @param trangThai "DAT_MOI", "XEM_PHIEU", "DANG_SU_DUNG"
     */
    private void thietLapGiaoDienTheoTrangThai(String trangThai) {
        if (trangThai == null) trangThai = "DAT_MOI";
        
        // 1. CÁC NÚT LUÔN HIỆN (Theo yêu cầu của USER)
        btnThemMonAn.setVisible(true);
        btnXemPhieuDat.setVisible(true);
        btnHuy.setVisible(true);

        // 2. CÁC NÚT PHỤ THUỘC TRẠNG THÁI
        switch (trangThai) {
            case "DAT_MOI":
                btnXacNhan.setVisible(true);
                btnBatDauSuDung.setVisible(true);
                btnTaoPhieuMoi.setVisible(true);
                btnLuuThayDoi.setVisible(false);
                btnThanhToan.setVisible(false);
                btnChonThemBan.setVisible(true);
                
                btnHuy.setText("Hủy");
                kichHoatForm();
                break;

            case "XEM_PHIEU":
                btnXacNhan.setVisible(false);
                btnBatDauSuDung.setVisible(true);
                btnTaoPhieuMoi.setVisible(true); // Cho phép tạo phiếu mới (vãng lai) kể cả khi bàn đã được đặt trước
                btnLuuThayDoi.setVisible(true);
                btnThanhToan.setVisible(false);
                btnChonThemBan.setVisible(true);
                
                btnHuy.setText("Hủy đặt bàn");
                voHieuHoaForm();
                break;

            case "DANG_SU_DUNG":
                btnXacNhan.setVisible(false);
                btnBatDauSuDung.setVisible(false);
                btnTaoPhieuMoi.setVisible(false);
                btnLuuThayDoi.setVisible(true);
                btnThanhToan.setVisible(true);
                btnChonThemBan.setVisible(false);
                
                btnHuy.setText("Kết thúc sử dụng");
                hienThiCheDoDangSuDung(); // Thiết lập chi tiết cho chế độ đang dùng
                break;
        }
        
        // Ẩn nút "Bắt đầu sử dụng" nếu không phải ngày hôm nay
        if (trangThai.equals("DAT_MOI") || trangThai.equals("XEM_PHIEU")) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String ngayHienTai = sdf.format(new Date());
            String ngayDat = (ngayDatTuChonBan != null) ? ngayDatTuChonBan : sdf.format(dateChooserNgayDat.getDate());
            if (!ngayDat.equals(ngayHienTai)) {
                btnBatDauSuDung.setVisible(false);
            }
        }
        
        // Ẩn các nút đặt nếu bàn đã full khung giờ
        if (isBanFullKhungGio && trangThai.equals("DAT_MOI")) {
            btnXacNhan.setVisible(false);
            btnBatDauSuDung.setVisible(false);
        }

        // Áp dụng kiểu dáng và đồng bộ visibility cuối cùng
        apDungKieuDang();
    }

    private void hienThiCheDoDangSuDung() {
        if (phieuHienTai != null)
            hienThiThongTinPhieuDat();

        // Kiểm tra xem có phải khách vãng lai không
        boolean laKhachVangLai = (phieuHienTai != null && phieuHienTai.getTienCoc() == 0);

        // Cho phép điền thông tin khách hàng để cập nhật
        txtTenKhachHang.setEditable(true);
        txtSoDienThoai.setEditable(true);
        txtEmail.setEditable(true);
        txtSoDienThoaiThanhVien.setEditable(true);
        cboGioiTinh.setEnabled(true);
        cboPhuongThucThanhToan.setEnabled(!laKhachVangLai);
        chkThemThanhVien.setVisible(true);

        txtTenKhachHang.setBackground(Color.WHITE);
        txtSoDienThoai.setBackground(Color.WHITE);
        txtEmail.setBackground(Color.WHITE);
        txtSoDienThoaiThanhVien.setBackground(Color.WHITE);
        cboGioiTinh.setBackground(Color.WHITE);

        // Không cho phép chỉnh ngày giờ
        dateChooserNgayDat.setEnabled(false);
        spinnerGioDat.setEnabled(false);

        if (laKhachVangLai) {
            lblNhanTongTien.setVisible(false);
            lblTongTienDatCoc.setVisible(false);
            cboPhuongThucThanhToan.setVisible(false);
        } else {
            lblNhanTongTien.setVisible(true);
            lblTongTienDatCoc.setVisible(true);
            cboPhuongThucThanhToan.setVisible(true);
            lblNhanTongTien.setText("Tổng tiền đặt cọc:");
        }
    }

    private void voHieuHoaForm() {
        txtTenKhachHang.setEditable(false);
        txtSoDienThoai.setEditable(false);
        txtEmail.setEditable(false);
        dateChooserNgayDat.setEnabled(false);
        spinnerGioDat.setEnabled(false);
        cboPhuongThucThanhToan.setEnabled(false);
        cboGioiTinh.setEnabled(false);

        Color mauVoHieuHoa = new Color(240, 240, 240);
        txtTenKhachHang.setBackground(mauVoHieuHoa);
        txtSoDienThoai.setBackground(mauVoHieuHoa);
        txtEmail.setBackground(mauVoHieuHoa);
        cboGioiTinh.setBackground(mauVoHieuHoa);
        dateChooserNgayDat.setBackground(mauVoHieuHoa);
        spinnerGioDat.setBackground(mauVoHieuHoa);
    }

    /**
     * Kích hoạt lại form (enable các field) khi tạo phiếu mới
     */
    private void kichHoatForm() {
        txtTenKhachHang.setEditable(true);
        txtSoDienThoai.setEditable(true);
        txtEmail.setEditable(true);
        dateChooserNgayDat.setEnabled(true);
        spinnerGioDat.setEnabled(true);
        cboPhuongThucThanhToan.setEnabled(true);
        cboGioiTinh.setEnabled(true);

        // Khôi phục màu trắng cho các field
        txtTenKhachHang.setBackground(Color.WHITE);
        txtSoDienThoai.setBackground(Color.WHITE);
        txtEmail.setBackground(Color.WHITE);
        cboGioiTinh.setBackground(Color.WHITE);
        // Spinner không cần set background
    }

    private void apDungKieuDang() {
        btnXacNhan.setBackground(MAU_XAC_NHAN);
        btnXacNhan.setForeground(Color.WHITE);
        btnBatDauSuDung.setBackground(new Color(0xEC893E));
        btnBatDauSuDung.setForeground(Color.WHITE);
        btnHuy.setBackground(MAU_CHINH);
        btnHuy.setForeground(Color.WHITE);
        btnThanhToan.setBackground(MAU_XAC_NHAN);
        btnThanhToan.setForeground(Color.WHITE);
        btnThanhToan.setVisible(cheDoHienThi.equals("DANG_SU_DUNG"));
        btnThemMonAn.setBackground(MAU_CHINH);
        btnThemMonAn.setForeground(Color.WHITE);
        btnXemPhieuDat.setBackground(new Color(0x2196F3));
        btnXemPhieuDat.setForeground(Color.WHITE);
        btnXemPhieuDat.setFocusPainted(false);
        btnXemPhieuDat.setPreferredSize(new Dimension(150, 35));

        btnTaoPhieuMoi.setBackground(new Color(0x9C27B0));
        btnTaoPhieuMoi.setForeground(Color.WHITE);
        btnTaoPhieuMoi.setFocusPainted(false);
        btnTaoPhieuMoi.setPreferredSize(new Dimension(150, 35));

        btnLuuThayDoi.setBackground(new Color(0xFF9800));
        btnLuuThayDoi.setForeground(Color.WHITE);
        btnLuuThayDoi.setFocusPainted(false);
        btnLuuThayDoi.setPreferredSize(new Dimension(150, 35));
    }

    private void capNhatDanhSachBanDaChon() {
        pnlDanhSachBanDaChon.removeAll();
        for (Integer maBan : danhSachBanDaChon) {
            JPanel chipBan = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            chipBan.setBackground(new Color(0xE3F2FD));
            JLabel lblBan = new JLabel("Bàn " + maBan);
            lblBan.setForeground(new Color(0x1976D2));

            JButton btnXoa = new JButton("×");
            btnXoa.setForeground(new Color(0xDC4332));
            btnXoa.setBorderPainted(false);
            btnXoa.setContentAreaFilled(false);

            final Integer banCanXoa = maBan;
            btnXoa.addActionListener(e -> {
                if (danhSachBanDaChon.size() > 1) {
                    danhSachBanDaChon.remove(banCanXoa);
                    capNhatDanhSachBanDaChon();
                    if (!danhSachBanDaChon.isEmpty())
                        lblSoBan.setText("Bàn " + danhSachBanDaChon.get(0));
                }
            });

            chipBan.add(lblBan);
            chipBan.add(btnXoa);
            pnlDanhSachBanDaChon.add(chipBan);
        }
        pnlDanhSachBanDaChon.revalidate();
        pnlDanhSachBanDaChon.repaint();
    }

    private void moDialogChonThemBan() {
        Window window = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(window, "Chọn thêm bàn", ModalityType.APPLICATION_MODAL);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);

        JPanel pnlNoiDung = new JPanel(new BorderLayout(10, 10));
        pnlNoiDung.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTieuDe = new JLabel("Chọn các bàn muốn đặt thêm:");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel pnlDanhSachBan = new JPanel(new GridLayout(0, 4, 10, 10));
        JScrollPane scrollPane = new JScrollPane(pnlDanhSachBan);

        ArrayList<BanAn> danhSachTatCaBan = BanAn_Ctr.getInstance().layTatCaBan();
        ArrayList<JCheckBox> danhSachCheckBox = new ArrayList<>();

        for (BanAn ban : danhSachTatCaBan) {
            // Chỉ hiển thị bàn nếu: trạng thái trống + chưa được chọn
            // Kiểm tra trùng lịch sẽ thực hiện trong btnXacNhanChon với giờ hiện tại
            if (!danhSachBanDaChon.contains(ban.getMaBan())) {
                JPanel pnlBan = new JPanel(new BorderLayout());
                pnlBan.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                pnlBan.setBackground(Color.WHITE);

                JCheckBox chkBan = new JCheckBox("Bàn " + ban.getMaBan());
                chkBan.setFont(new Font("Arial", Font.PLAIN, 14));
                chkBan.setHorizontalAlignment(SwingConstants.CENTER);
                chkBan.putClientProperty("maBan", ban.getMaBan());
                danhSachCheckBox.add(chkBan);

                JLabel lblSoCho = new JLabel(ban.getSoCho() + " chỗ", SwingConstants.CENTER);
                lblSoCho.setFont(new Font("Arial", Font.PLAIN, 12));
                lblSoCho.setForeground(Color.GRAY);

                pnlBan.add(chkBan, BorderLayout.CENTER);
                pnlBan.add(lblSoCho, BorderLayout.SOUTH);

                pnlDanhSachBan.add(pnlBan);
            }
        }

        JPanel pnlNutBam = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnXacNhanChon = new JButton("Xác nhận");
        JButton btnHuyChon = new JButton("Hủy");

        btnXacNhanChon.setPreferredSize(new Dimension(120, 35));
        btnXacNhanChon.setBackground(MAU_XAC_NHAN);
        btnXacNhanChon.setForeground(Color.WHITE);
        btnXacNhanChon.setFocusPainted(false);

        btnHuyChon.setPreferredSize(new Dimension(120, 35));
        btnHuyChon.setBackground(MAU_CHINH);
        btnHuyChon.setForeground(Color.WHITE);
        btnHuyChon.setFocusPainted(false);

        btnXacNhanChon.addActionListener(e -> {
            ArrayList<Integer> banMoiThem = new ArrayList<>();
            for (JCheckBox chk : danhSachCheckBox) {
                if (chk.isSelected()) {
                    Integer maBan = (Integer) chk.getClientProperty("maBan");
                    banMoiThem.add(maBan);
                }
            }

            // Thêm bàn vào danh sách (kiểm tra trùng lịch sẽ thực hiện ở nút Đặt bàn)
            for (Integer maBan : banMoiThem) {
                if (!danhSachBanDaChon.contains(maBan)) {
                    danhSachBanDaChon.add(maBan);
                }
            }

            int soLuongBan = danhSachBanDaChon.size();
            double phiBan = 250000 * soLuongBan;
            double tienMonAn = 0;
            if (!danhSachMonDaChon.isEmpty()) {
                for (ChiTietDatMon ct : danhSachMonDaChon) {
                    tienMonAn += ct.getMonAn().getGia() * ct.getSoLuong();
                }
            }
            double phiMonAn = tienMonAn * 0.3;
            double tienCocMoi = phiBan + phiMonAn;
            lblTongTienDatCoc.setText(String.format(" %,.0f VNĐ", tienCocMoi));

            if (phieuHienTai != null && !banMoiThem.isEmpty()) {
                // Logic xử lý thêm bàn khi đang xem phiếu (giữ nguyên)
            }
            capNhatDanhSachBanDaChon();
            dialog.dispose();
        });

        btnHuyChon.addActionListener(e -> dialog.dispose());
        pnlNutBam.add(btnXacNhanChon);
        pnlNutBam.add(btnHuyChon);
        pnlNoiDung.add(lblTieuDe, BorderLayout.NORTH);
        pnlNoiDung.add(scrollPane, BorderLayout.CENTER);
        pnlNoiDung.add(pnlNutBam, BorderLayout.SOUTH);
        dialog.add(pnlNoiDung);
        dialog.setVisible(true);
    }

    private PhieuDatBan timPhieuTheoMaBanVaNgay(int maBan, String ngayCanTim) {
        ArrayList<PhieuDatBan> tatCaPhieu = phieuDatBanCtr.layTatCaPhieu();
        if (tatCaPhieu == null)
            return null;
        for (PhieuDatBan phieu : tatCaPhieu) {
            if (phieu.getTrangThai().equals("Đã hủy"))
                continue;
            if (phieu.getMaBan() == maBan && phieu.getNgayDat().equals(ngayCanTim))
                return phieu;
        }
        return null;
    }

    /**
     * Kiểm tra xem giờ đặt có hợp lệ không (cách các phiếu đã đặt ít nhất 2 tiếng)
     */
    private boolean kiemTraKhungGioHopLe(String gioDat, String ngayDat) {
        // Lấy danh sách phiếu đặt đã có cho bàn này trong ngày
        ArrayList<PhieuDatBan> dsPhieu = phieuDatBanCtr.layPhieuDatTheoBanVaNgay(soBan, ngayDat);

        if (dsPhieu == null || dsPhieu.isEmpty()) {
            return true; // Chưa có phiếu nào -> hợp lệ
        }

        try {
            java.time.LocalTime gioMoi = java.time.LocalTime.parse(gioDat);

            for (PhieuDatBan phieu : dsPhieu) {
                if (phieu.getGioDat() == null || phieu.getGioDat().isEmpty()) {
                    continue;
                }

                java.time.LocalTime gioCu = java.time.LocalTime.parse(phieu.getGioDat());

                // Kiểm tra khoảng cách giữa 2 giờ
                long khoangCachPhut = Math.abs(java.time.Duration.between(gioCu, gioMoi).toMinutes());

                if (khoangCachPhut < 120) { // 2 tiếng = 120 phút
                    return false; // Trùng lặp
                }
            }

            return true; // Không trùng lặp

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Reset form về trạng thái trống (tạo phiếu mới)
     */
    public void resetForm() {
        kichHoatForm();

        txtTenKhachHang.setText("Nhập họ tên");
        txtTenKhachHang.setForeground(MAU_PLACEHOLDER);
        txtSoDienThoai.setText("Nhập số điện thoại");
        txtSoDienThoai.setForeground(MAU_PLACEHOLDER);
        txtEmail.setText("Nhập email");
        txtEmail.setForeground(MAU_PLACEHOLDER);
        txtSoDienThoaiThanhVien.setText("Nhập SĐT thành viên");
        txtSoDienThoaiThanhVien.setForeground(MAU_PLACEHOLDER);

        chkThemThanhVien.setSelected(false);
        lblTrangThaiThanhVien.setText("Chưa là khách hàng thành viên");
        lblTrangThaiThanhVien.setForeground(Color.RED);

        maPhieuDangXem = null;
        phieuHienTai = null;

        java.util.Calendar cal = java.util.Calendar.getInstance();
        spinnerGioDat.setValue(cal.getTime());

        if (ngayDatTuChonBan != null && !ngayDatTuChonBan.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date ngay = sdf.parse(ngayDatTuChonBan);
                dateChooserNgayDat.setDate(ngay);
            } catch (Exception e) {
                dateChooserNgayDat.setDate(new Date());
            }
        } else {
            dateChooserNgayDat.setDate(new Date());
        }

        cboPhuongThucThanhToan.setSelectedIndex(0);

        danhSachMonDaChon.clear();
        phieuDatBanCtr.xoaGioHangTamThoi(soBan); 
        phieuDatBanCtr.capNhatDanhSachMonAn(soBan, rawDanhSachMon(danhSachMonDaChon));
        capNhatPanelThongTinDatMon();

        danhSachBanDaChon.clear();
        danhSachBanDaChon.add(soBan);
        capNhatDanhSachBanDaChon();

        lblTongTienDatCoc.setText(" 250,000 VNĐ");
        lblNhanTongTien.setVisible(true);
        lblTongTienDatCoc.setVisible(true);
        lblNhanTongTien.setText("Tổng tiền đặt cọc:");
        cboPhuongThucThanhToan.setVisible(true);

        cheDoHienThi = "DAT_MOI";
        thietLapGiaoDienTheoTrangThai("DAT_MOI");

        System.out.println("=== FORM ĐÃ ĐƯỢC RESET - SẴN SÀNG TẠO PHIẾU MỚI ===");
    }

    /**
     * Xử lý khi click nút Thoát (giống nút Hủy)
     */
    public void handleThoatButton() {
        // KHÔNG xóa giỏ hàng khi thoát (quay lại chọn bàn) để giữ lại món ăn đang chọn dở
        // Chuyển mượt mà sang màn hình chọn bàn
        GUIManager.getInstance().switchToGUI(ChonBan_GUI.class, PhieuDat_GUI.this);
    }

    /**
     * Load phiếu đặt từ database và render lên form
     */
    public void loadPhieuDatFromDB(PhieuDatBan phieu) {
        if (phieu == null)
            return;

        System.out.println("=== LOAD PHIẾU: " + phieu.getMaPhieu() + " - " + phieu.getTenKhachDat());

        danhSachMonDaChon = new ArrayList<>();
        danhSachMonDaChon = new ArrayList<>();

        this.phieuHienTai = phieu;
        this.maPhieuDangXem = phieu.getMaPhieu();

        // Load thông tin khách hàng
        txtTenKhachHang.setText(phieu.getTenKhachDat());
        txtSoDienThoai.setText(phieu.getSdtDat());
        if (phieu.getEmailDat() != null && !phieu.getEmailDat().isEmpty()) {
            txtEmail.setText(phieu.getEmailDat());
        }

        // Load ngày giờ
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date ngay = sdf.parse(phieu.getNgayDat());
            dateChooserNgayDat.setDate(ngay);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (phieu.getGioDat() != null && !phieu.getGioDat().isEmpty()) {
            try {
                SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
                Date gio = sdfTime.parse(phieu.getGioDat());
                spinnerGioDat.setValue(gio);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // Load phương thức thanh toán
        if (phieu.getPhuongThucThanhToan() != null) {
            cboPhuongThucThanhToan.setSelectedItem(phieu.getPhuongThucThanhToan());
        }

        // Load danh sách bàn
        if (phieu.getDanhSachBan() != null && !phieu.getDanhSachBan().isEmpty()) {
            danhSachBanDaChon = new ArrayList<>(phieu.getDanhSachBan());
            capNhatDanhSachBanDaChon();
        }

        // Load danh sách món ăn
        if (phieu.getDanhSachMonAn() != null && !phieu.getDanhSachMonAn().isEmpty()) {
            System.out.println("=== LOAD PHIẾU: Số món ăn từ phiếu: " + phieu.getDanhSachMonAn().size());
            for (ChiTietDatMon ct : phieu.getDanhSachMonAn()) {
                System.out.println("    - " + ct.getMonAn().getTenMon() + " x" + ct.getSoLuong());
                danhSachMonDaChon.add(new ChiTietDatMon(ct.getMonAn(), ct.getSoLuong()));
            }
            capNhatPanelThongTinDatMon();
            System.out.println("=== LOAD PHIẾU: Đã render " + danhSachMonDaChon.size() + " món lên giao diện");
        } else {
            System.out.println("=== LOAD PHIẾU: Phiếu không có món ăn");
            capNhatPanelThongTinDatMon();
        }

        // Load tiền cọc
        lblTongTienDatCoc.setText(String.format(" %,.0f VNĐ", phieu.getTienCoc()));

        // Kiểm tra trạng thái thành viên
        String sdt = phieu.getSdtDat();
        if (sdt != null && !sdt.isEmpty()) {
            boolean exists = khachHangCtr.existsBySoDienThoai(sdt);
            if (exists) {
                lblTrangThaiThanhVien.setText("Đã là khách hàng thành viên");
                lblTrangThaiThanhVien.setForeground(MAU_XAC_NHAN);
            }
        }

        // Kiểm tra trạng thái phiếu để hiển thị đúng chế độ
        if (phieu.getTrangThai() != null && phieu.getTrangThai().equals("Đã xác nhận")) {
            cheDoHienThi = "DANG_SU_DUNG";
            hienThiCheDoDangSuDung();
        } else {
            cheDoHienThi = "XEM_PHIEU";
            btnHuy.setText("Hủy đặt bàn");
            btnHuy.setVisible(true);
            btnLuuThayDoi.setVisible(true);
            btnXacNhan.setVisible(false);
            btnBatDauSuDung.setVisible(true);
            btnXemPhieuDat.setVisible(true);
            btnTaoPhieuMoi.setVisible(true);
            btnThanhToan.setVisible(false);
            btnChonThemBan.setVisible(true);
            btnThemMonAn.setVisible(true);

            lblNhanTongTien.setVisible(true);
            lblTongTienDatCoc.setVisible(true);
            lblNhanTongTien.setText("Tổng tiền đặt cọc:");
        }
        voHieuHoaForm();
    }
}
