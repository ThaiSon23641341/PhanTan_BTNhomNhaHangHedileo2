package iuh.fit.son23641341.nhahanglau_phantan.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.io.File;

import iuh.fit.son23641341.nhahanglau_phantan.control.KhuyenMai_Ctr;
import iuh.fit.son23641341.nhahanglau_phantan.control.PhieuDatBan_Ctr;
import iuh.fit.son23641341.nhahanglau_phantan.control.PrinterService; // Đảm bảo đã có class này
import iuh.fit.son23641341.nhahanglau_phantan.dao.KhachHang_DAO;
import iuh.fit.son23641341.nhahanglau_phantan.dao.PhieuDat_DAO; 
import iuh.fit.son23641341.nhahanglau_phantan.control.BanAn_Ctr;
import iuh.fit.son23641341.nhahanglau_phantan.control.HoaDon_Ctrl;
import iuh.fit.son23641341.nhahanglau_phantan.control.KhachHang_Ctr;
import iuh.fit.son23641341.nhahanglau_phantan.entity.ChiTietDatMon;
import iuh.fit.son23641341.nhahanglau_phantan.entity.HoaDon;
import iuh.fit.son23641341.nhahanglau_phantan.entity.KhuyenMai;
import iuh.fit.son23641341.nhahanglau_phantan.entity.PhieuDatBan;

public class XacNhanHoaDon_GUI extends JFrame {
    
    private static final Color MAU_CHINH = new Color(0xDC4332);
    private static final Color MAU_XAC_NHAN = new Color(0x7AB750);
    
    private HoaDon hoaDonHienTai; 
    private HoaDon_Ctrl hoaDonCtr; 
    
    private KhuyenMai khuyenMaiDaApDung; 
    private ArrayList<KhuyenMai> danhSachKhuyenMai;

    private JComboBox<String> cboKhuyenMai;
    private JButton btnApDungKhuyenMai;
    private JLabel lblThongBaoKhuyenMai;
    
    private JLabel lblSoBan;
    private JLabel lblTongTienMonAn;
    private JLabel lblTienGiam;
    private JLabel lblTienCoc;
    
    private JLabel lblThanhVien;      
    private JLabel lblGiamGiaThanhVien; 
    
    private JLabel lblTongThanhToan;
    private JPanel pnlDanhSachMonAn;
	private JFrame frameCha;
    
    public XacNhanHoaDon_GUI(HoaDon hoaDon) {
        init(hoaDon);
    }
    
    public XacNhanHoaDon_GUI() {
        PhieuDatBan phieu = new PhieuDatBan();
        phieu.setMaBan(1);
        HoaDon hd = new HoaDon();
        hd.setPhieuDat(phieu);
        init(hd);
    }
    
    public void setFrameCha(JFrame frame) {
        this.frameCha = frame;
    }

    private void init(HoaDon hoaDon) {
        this.hoaDonHienTai = hoaDon;
        this.hoaDonCtr = new HoaDon_Ctrl();
        
        KhuyenMai_Ctr khuyenMaiCtr = new KhuyenMai_Ctr();
        this.danhSachKhuyenMai = khuyenMaiCtr.getDanhSachKhuyenMai();
        
        khoiTaoGiaoDien();
        capNhatTinhToan();
    }

    // ... (Giữ nguyên các hàm setKhuyenMai, taoNhan...)
    public void setKhuyenMaiDaApDung(KhuyenMai km) {
        this.khuyenMaiDaApDung = km;
        if (km != null && danhSachKhuyenMai != null) {
             for (int i = 0; i < danhSachKhuyenMai.size(); i++) {
                if (danhSachKhuyenMai.get(i).getMaKhuyenMai().equals(km.getMaKhuyenMai())) {
                    cboKhuyenMai.setSelectedIndex(i + 1);
                    lblThongBaoKhuyenMai.setText("Đã áp dụng từ phiếu đặt!");
                    lblThongBaoKhuyenMai.setForeground(new Color(0, 128, 0));
                    break;
                }
            }
        }
        capNhatTinhToan();
    }

    private JLabel taoNhanChiTiet(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.PLAIN, 14));
        return l;
    }

    private JLabel taoNhanGiaTri(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.BOLD, 14));
        l.setHorizontalAlignment(SwingConstants.RIGHT);
        return l;
    }

    // === GIAO DIỆN (Giữ nguyên) ===
    private void khoiTaoGiaoDien() {
        setTitle("HỆ DÌ LEO - Xác Nhận Thanh Toán");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        SideBar_GUI sidebar = new SideBar_GUI();
        sidebar.setMauNutKhiChon("Thanh Toán");
        add(sidebar, BorderLayout.WEST);

        JPanel pnlNoiDungChinh = taoNoiDungChinh();
        add(pnlNoiDungChinh, BorderLayout.CENTER);
    }

    private JPanel taoNoiDungChinh() {
        JPanel pnlChinh = new JPanel(new BorderLayout());
        pnlChinh.setBackground(new Color(245, 245, 245));
        pnlChinh.setBorder(new EmptyBorder(15, 25, 15, 25));

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setPreferredSize(new Dimension(0, 80));
        pnlHeader.setBackground(Color.WHITE);
        pnlHeader.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                new EmptyBorder(15, 20, 15, 20)));

        JLabel lblTieuDeTrang = new JLabel("HÓA ĐƠN THANH TOÁN");
        lblTieuDeTrang.setFont(new Font("Arial", Font.BOLD, 24));
        lblTieuDeTrang.setForeground(new Color(230, 81, 0));
        pnlHeader.add(lblTieuDeTrang, BorderLayout.WEST);
        pnlChinh.add(pnlHeader, BorderLayout.NORTH);

        JPanel pnlBaoPhat = new JPanel(new GridBagLayout());
        pnlBaoPhat.setOpaque(false);

        JPanel pnlHoaDon = new JPanel();
        pnlHoaDon.setLayout(new BoxLayout(pnlHoaDon, BoxLayout.Y_AXIS));
        pnlHoaDon.setBackground(Color.WHITE);
        pnlHoaDon.setBorder(new EmptyBorder(30, 40, 30, 40));
        pnlHoaDon.setPreferredSize(new Dimension(600, 650)); 

        JLabel lblTitleHD = new JLabel("XÁC NHẬN HÓA ĐƠN");
        lblTitleHD.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitleHD.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel pnlInfo = new JPanel();
        pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));
        pnlInfo.setBackground(new Color(245, 245, 245));
        pnlInfo.setBorder(new EmptyBorder(15, 15, 15, 15));
        pnlInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlInfo.setMaximumSize(new Dimension(520, 80));

        String tenKhach = (hoaDonHienTai.getPhieuDat() != null) ? hoaDonHienTai.getPhieuDat().getTenKhachHang() : "Khách vãng lai";
        JLabel lblKhachHang = new JLabel("Khách hàng: " + tenKhach);
        lblKhachHang.setFont(new Font("Arial", Font.PLAIN, 14));
        
        String dsBanStr = "";
        if (hoaDonHienTai.getPhieuDat() != null) {
            ArrayList<Integer> list1 = hoaDonHienTai.getPhieuDat().getDanhSachBanDaChon();
            ArrayList<Integer> list2 = hoaDonHienTai.getPhieuDat().getDanhSachMaBan();
            if (list1 != null && !list1.isEmpty()) dsBanStr = list1.toString();
            else if (list2 != null && !list2.isEmpty()) dsBanStr = list2.toString();
            else dsBanStr = String.valueOf(hoaDonHienTai.getPhieuDat().getMaBan());
        }
        lblSoBan = new JLabel("Bàn: " + dsBanStr);
        lblSoBan.setFont(new Font("Arial", Font.BOLD, 16));

        pnlInfo.add(lblKhachHang);
        pnlInfo.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlInfo.add(lblSoBan);

        pnlDanhSachMonAn = new JPanel();
        pnlDanhSachMonAn.setLayout(new BoxLayout(pnlDanhSachMonAn, BoxLayout.Y_AXIS));
        pnlDanhSachMonAn.setBackground(Color.WHITE);
        JScrollPane scrollMonAn = new JScrollPane(pnlDanhSachMonAn);
        scrollMonAn.setPreferredSize(new Dimension(520, 150));
        scrollMonAn.setBorder(BorderFactory.createTitledBorder("Chi tiết món"));
        napDuLieuMonAnTuEntity();

        JPanel pnlKhuyenMai = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlKhuyenMai.setBackground(Color.WHITE);
        pnlKhuyenMai.setBorder(BorderFactory.createTitledBorder("Khuyến mãi"));
        pnlKhuyenMai.setMaximumSize(new Dimension(520, 60));

        cboKhuyenMai = new JComboBox<>();
        cboKhuyenMai.addItem("-- Chọn khuyến mãi --");
        if (danhSachKhuyenMai != null) {
            for (KhuyenMai km : danhSachKhuyenMai) {
                cboKhuyenMai.addItem(km.getTenKhuyenMai() + " (" + km.getPhanTramGiam() + "%)");
            }
        }
        cboKhuyenMai.setPreferredSize(new Dimension(250, 30));
        btnApDungKhuyenMai = new JButton("Áp dụng");
        btnApDungKhuyenMai.setBackground(MAU_XAC_NHAN);
        btnApDungKhuyenMai.setForeground(Color.WHITE);
        lblThongBaoKhuyenMai = new JLabel("");
        lblThongBaoKhuyenMai.setForeground(Color.RED);
        pnlKhuyenMai.add(cboKhuyenMai);
        pnlKhuyenMai.add(btnApDungKhuyenMai);
        pnlKhuyenMai.add(lblThongBaoKhuyenMai);

        JPanel pnlTinhToan = new JPanel(new GridLayout(0, 2, 10, 5));
        pnlTinhToan.setBackground(Color.WHITE);
        pnlTinhToan.setMaximumSize(new Dimension(520, 120)); 

        lblTongTienMonAn = taoNhanGiaTri("0 VNĐ");
        lblTienGiam = taoNhanGiaTri("0 VNĐ");
        lblTienCoc = taoNhanGiaTri("0 VNĐ");
        lblThanhVien = taoNhanChiTiet("Thành viên:"); 
        lblGiamGiaThanhVien = taoNhanGiaTri("0 VNĐ");
        
        pnlTinhToan.add(taoNhanChiTiet("Tổng tiền món:"));
        pnlTinhToan.add(lblTongTienMonAn);
        pnlTinhToan.add(taoNhanChiTiet("Giảm giá (Voucher):"));
        pnlTinhToan.add(lblTienGiam);
        pnlTinhToan.add(taoNhanChiTiet("Đã đặt cọc:"));
        pnlTinhToan.add(lblTienCoc);
        pnlTinhToan.add(lblThanhVien);
        pnlTinhToan.add(lblGiamGiaThanhVien);

        JPanel pnlTongCong = new JPanel(new BorderLayout());
        pnlTongCong.setBackground(new Color(240, 255, 240)); 
        pnlTongCong.setBorder(new EmptyBorder(10, 10, 10, 10));
        pnlTongCong.setMaximumSize(new Dimension(520, 50));
        
        JLabel lblTextTong = new JLabel("TỔNG THANH TOÁN:");
        lblTextTong.setFont(new Font("Arial", Font.BOLD, 16));
        lblTongThanhToan = new JLabel("0 VNĐ");
        lblTongThanhToan.setFont(new Font("Arial", Font.BOLD, 20));
        lblTongThanhToan.setForeground(MAU_CHINH);
        lblTongThanhToan.setHorizontalAlignment(SwingConstants.RIGHT);
        
        pnlTongCong.add(lblTextTong, BorderLayout.WEST);
        pnlTongCong.add(lblTongThanhToan, BorderLayout.EAST);

        JPanel pnlNutXacNhan = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        pnlNutXacNhan.setBackground(Color.WHITE);

        JButton btnXacNhan = new JButton("XÁC NHẬN & IN HÓA ĐƠN");
        btnXacNhan.setBackground(MAU_CHINH);
        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setFont(new Font("Arial", Font.BOLD, 14));
        btnXacNhan.setPreferredSize(new Dimension(250, 40));
        btnXacNhan.setFocusPainted(false);
        btnXacNhan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JButton btnHuy = new JButton("Quay Lại");
        btnHuy.setBackground(new Color(158, 158, 158)); 
        btnHuy.setForeground(Color.WHITE);
        btnHuy.setFont(new Font("Arial", Font.BOLD, 14));
        btnHuy.setPreferredSize(new Dimension(150, 40));
        btnHuy.setFocusPainted(false);
        btnHuy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        pnlNutXacNhan.add(btnXacNhan);
        pnlNutXacNhan.add(btnHuy);
        
        pnlHoaDon.add(lblTitleHD);
        pnlHoaDon.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlHoaDon.add(pnlInfo);
        pnlHoaDon.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlHoaDon.add(scrollMonAn);
        pnlHoaDon.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlHoaDon.add(pnlKhuyenMai);
        pnlHoaDon.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlHoaDon.add(pnlTinhToan);
        pnlHoaDon.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlHoaDon.add(pnlTongCong);
        pnlHoaDon.add(Box.createRigidArea(new Dimension(0, 20)));
        pnlHoaDon.add(pnlNutXacNhan);

        pnlBaoPhat.add(pnlHoaDon);
        pnlChinh.add(pnlBaoPhat, BorderLayout.CENTER);

        // === SỰ KIỆN ===
        btnApDungKhuyenMai.addActionListener(e -> {
            int idx = cboKhuyenMai.getSelectedIndex();
            if (idx > 0) {
                this.khuyenMaiDaApDung = danhSachKhuyenMai.get(idx - 1);
                lblThongBaoKhuyenMai.setText("Đã áp dụng!");
                lblThongBaoKhuyenMai.setForeground(new Color(0, 128, 0));
            } else {
                this.khuyenMaiDaApDung = null;
                lblThongBaoKhuyenMai.setText("");
            }
            capNhatTinhToan();
        });

        btnHuy.addActionListener(e -> {
            dispose(); 
        });
        
        btnXacNhan.addActionListener(e -> {
            double tongMon = hoaDonCtr.tinhTongTienMonAn(hoaDonHienTai);
            double giamGiaVoucher = hoaDonCtr.tinhTienGiamGia(tongMon, khuyenMaiDaApDung);
            double giamGiaThanhVien = tinhGiamGiaHangThanhVien(tongMon);
            double tienCoc = (hoaDonHienTai.getPhieuDat() != null) ? hoaDonHienTai.getPhieuDat().getTienCoc() : 0;
            double thanhToanCuoiCung = Math.max(0, tongMon - giamGiaVoucher - giamGiaThanhVien - tienCoc);
            
            String thongTinKM = (khuyenMaiDaApDung != null) ? String.format("<br>Khuyến mãi: -%,.0f VNĐ", giamGiaVoucher) : "";
            String thongTinTV = (giamGiaThanhVien > 0) ? String.format("<br>Thành viên: -%,.0f VNĐ", giamGiaThanhVien) : "";
            String thongTinCoc = (tienCoc > 0) ? String.format("<br>Đã đặt cọc: -%,.0f VNĐ", tienCoc) : "";
            
            int maBanChinh = (hoaDonHienTai.getPhieuDat() != null) ? hoaDonHienTai.getPhieuDat().getMaBan() : 0;
            ArrayList<ChiTietDatMon> dsMonAn = (hoaDonHienTai.getPhieuDat() != null) ? hoaDonHienTai.getPhieuDat().getDanhSachMonAn() : null;

            String noiDungDialog = String.format("<html><body style='width: 300px;'>"
                    + "<h3>XÁC NHẬN THANH TOÁN</h3>"
                    + "<b>Tổng tiền món:</b> %,.0f VNĐ%s%s%s<hr>"
                    + "<b>CẦN THANH TOÁN: <font color='red' size='5'>%,.0f VNĐ</font></b></body></html>",
                    tongMon, thongTinKM, thongTinTV, thongTinCoc, thanhToanCuoiCung);

            ThanhToan_Dialog dialog = new ThanhToan_Dialog(this, noiDungDialog, thanhToanCuoiCung, maBanChinh, dsMonAn);
            dialog.setVisible(true);

            int luaChon = dialog.getLuaChon();
            if (luaChon == ThanhToan_Dialog.TIEN_MAT || luaChon == ThanhToan_Dialog.DA_CHUYEN_KHOAN) {
                hoaDonCtr.capNhatTongTienHoaDon(hoaDonHienTai, thanhToanCuoiCung);
                if (hoaDonHienTai.getPhieuDat() != null) {
                     hoaDonHienTai.getPhieuDat().setGiamGia(giamGiaVoucher + giamGiaThanhVien); 
                }
                thucHienThanhToanVaXoaDuLieu(thanhToanCuoiCung);
            }
        });
        return pnlChinh;
    }
    
    // === XỬ LÝ THANH TOÁN VÀ IN BILL ===
    private void thucHienThanhToanVaXoaDuLieu(double tongThanhToan) {
        PhieuDatBan_Ctr phieuDatCtr = PhieuDatBan_Ctr.getInstance();
        BanAn_Ctr banAnCtr = BanAn_Ctr.getInstance();
        
        int maBanChinh = 0;
        ArrayList<Integer> dsBanCanXoa = new ArrayList<>();
        
        if (hoaDonHienTai.getPhieuDat() != null) {
            maBanChinh = hoaDonHienTai.getPhieuDat().getMaBan();
            ArrayList<Integer> list1 = hoaDonHienTai.getPhieuDat().getDanhSachBanDaChon();
            ArrayList<Integer> list2 = hoaDonHienTai.getPhieuDat().getDanhSachMaBan();
            
            if (list1 != null && !list1.isEmpty()) dsBanCanXoa.addAll(list1);
            else if (list2 != null && !list2.isEmpty()) dsBanCanXoa.addAll(list2);
            else dsBanCanXoa.add(maBanChinh);
        }
        
        // 1. Cập nhật Database
        if (hoaDonHienTai.getPhieuDat() != null && hoaDonHienTai.getPhieuDat().getMaPhieu() != null) {
            PhieuDat_DAO phieuDAO = new PhieuDat_DAO();
            phieuDAO.capNhatTrangThai(hoaDonHienTai.getPhieuDat().getMaPhieu(), "Đã thanh toán");
        }

        // 2. Xóa bộ nhớ tạm
        phieuDatCtr.xoaPhieuDat(maBanChinh);
        
        // 3. Cập nhật Bàn ăn -> Trống
        boolean capNhatThanhCong = true;
        for (Integer maBan : dsBanCanXoa) {
            phieuDatCtr.xoaGioHangTamThoi(maBan);
            phieuDatCtr.xoaKhuyenMaiTamThoi(maBan);
            boolean kq = banAnCtr.capNhatTrangThai(maBan, "Trống");
            if (!kq) capNhatThanhCong = false;
        }
        
        if (capNhatThanhCong) {
            String thongBaoIn = "";
            
            // Xử lý điểm tích lũy
            try {
                KhachHang_Ctr khCtr = new KhachHang_Ctr();
                khCtr.xuLyCongDiemChoKhach(hoaDonHienTai.getPhieuDat());
            } catch (Exception e) { e.printStackTrace(); }

            // === TẠO VÀ IN HÓA ĐƠN ===
            try {
                String pathFolder = "src/HoaDon_ThanhToan_PDF"; 
                File folder = new File(pathFolder);
                if (!folder.exists()) folder.mkdirs();

                String tenFile = "HoaDon_" + maBanChinh + "_" + System.currentTimeMillis() + ".pdf";
                String fullPath = pathFolder + "/" + tenFile;
                
                // 1. Tạo file PDF
                boolean kqXuLy = hoaDonCtr.taoHoaDonVaXuatPDF(hoaDonHienTai.getPhieuDat(), fullPath);
                
                if (kqXuLy) {
                    // thongBaoIn = "\n(Đã lưu hóa đơn tại: " + fullPath + ")";
                    
                    String tenMayIn = "XP-58"; // Tên chính xác từ ảnh bạn gửi
                    boolean inThanhCong = PrinterService.printPDF(fullPath, tenMayIn);
                    
                    if (inThanhCong) {
                        thongBaoIn += "\n(Đã gửi lệnh in tới " + tenMayIn + ")";
                    } else {
                        thongBaoIn += "\n(Lỗi: Không tìm thấy máy in " + tenMayIn + " hoặc in thất bại)";
                        // Fallback: Mở file PDF lên nếu in tự động thất bại
                        // if (Desktop.isDesktopSupported()) {
                        //     Desktop.getDesktop().open(new File(fullPath));
                        // }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                thongBaoIn = "\n(Lỗi hệ thống khi in bill)";
            }

            JOptionPane.showMessageDialog(this, "THANH TOÁN THÀNH CÔNG!\n" + thongBaoIn, "Thành công", JOptionPane.INFORMATION_MESSAGE);
            
            // Dispose frame cha nếu có
            if (this.frameCha != null) {
                this.frameCha.dispose();
            }
            this.dispose();
            
            // Sử dụng GUIManager để chuyển mượt mà sang ChonBan_GUI
            GUIManager.getInstance().switchToGUI(ChonBan_GUI.class, this.frameCha);
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật trạng thái bàn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void napDuLieuMonAnTuEntity() {
        pnlDanhSachMonAn.removeAll();
        ArrayList<ChiTietDatMon> dsMon = (hoaDonHienTai.getPhieuDat() != null) ? hoaDonHienTai.getPhieuDat().getDanhSachMonAn() : null;
        if (dsMon != null) {
            for (ChiTietDatMon ct : dsMon) {
                JPanel row = new JPanel(new BorderLayout());
                row.setBackground(Color.WHITE);
                row.setBorder(BorderFactory.createMatteBorder(0,0,1,0, new Color(240,240,240)));
                row.add(new JLabel(ct.getMonAn().getTenMon()), BorderLayout.WEST);
                row.add(new JLabel(String.format("%d x %,.0f = %,.0f", ct.getSoLuong(), ct.getMonAn().getGia(), ct.getSoLuong()*ct.getMonAn().getGia())), BorderLayout.EAST);
                row.setMaximumSize(new Dimension(500, 30));
                pnlDanhSachMonAn.add(row);
            }
        }
        pnlDanhSachMonAn.revalidate(); pnlDanhSachMonAn.repaint();
    }
    
    private double tinhGiamGiaHangThanhVien(double tongTienMon) {
        if (hoaDonHienTai.getPhieuDat() != null && hoaDonHienTai.getPhieuDat().getMaKhachHang() != null) {
            String rank = new KhachHang_DAO().getHang(hoaDonHienTai.getPhieuDat());
            if (rank != null) {
                double pct = 0; String color = "#000000";
                switch(rank.trim().toUpperCase()) {
                    case "ĐỒNG": pct=0.02; color="#CD7F32"; break;
                    case "BẠC": pct=0.05; color="#708090"; break;
                    case "VÀNG": pct=0.10; color="#DAA520"; break;
                    case "KIM CƯƠNG": pct=0.15; color="#1E90FF"; break;
                }
                if (pct > 0) lblThanhVien.setText("<html>Thành viên <font color='"+color+"'>"+rank.toUpperCase()+"</font> (-"+(int)(pct*100)+"%):</html>");
                return tongTienMon * pct;
            }
        }
        lblThanhVien.setText("Không có thành viên:");
        return 0;
    }

    private void capNhatTinhToan() {
        double tong = hoaDonCtr.tinhTongTienMonAn(hoaDonHienTai);
        double km = hoaDonCtr.tinhTienGiamGia(tong, khuyenMaiDaApDung);
        double tv = tinhGiamGiaHangThanhVien(tong);
        double coc = (hoaDonHienTai.getPhieuDat() != null) ? hoaDonHienTai.getPhieuDat().getTienCoc() : 0;
        
        lblTongTienMonAn.setText(String.format("%,.0f VNĐ", tong));
        lblTienGiam.setText(km > 0 ? String.format("- %,.0f VNĐ", km) : "0 VNĐ");
        lblGiamGiaThanhVien.setText(tv > 0 ? String.format("- %,.0f VNĐ", tv) : "0 VNĐ");
        lblTienCoc.setText(coc > 0 ? String.format("- %,.0f VNĐ", coc) : "0 VNĐ");
        lblTongThanhToan.setText(String.format("%,.0f VNĐ", Math.max(0, tong - km - tv - coc)));
        
        if(tv > 0) lblGiamGiaThanhVien.setForeground(new Color(0, 102, 204));
        if(km > 0) lblTienGiam.setForeground(new Color(0, 128, 0));
    }

    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new XacNhanHoaDon_GUI().setVisible(true)); }
}
