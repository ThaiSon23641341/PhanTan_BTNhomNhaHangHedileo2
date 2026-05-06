package iuh.fit.son23641341.nhahanglau_phantan.control;

import iuh.fit.son23641341.nhahanglau_phantan.entity.PhieuDatBan;
import iuh.fit.son23641341.nhahanglau_phantan.mock.MockData;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import iuh.fit.son23641341.nhahanglau_phantan.dao.PhieuDat_DAO;

import iuh.fit.son23641341.nhahanglau_phantan.dao.KhachHang_DAO;
import iuh.fit.son23641341.nhahanglau_phantan.entity.ChiTietDatMon;
import iuh.fit.son23641341.nhahanglau_phantan.entity.NhanVien;

// NOTE: Controller now uses mock data; database logic removed.
public class PhieuDatBan_Ctr {
    private static PhieuDatBan_Ctr instance; // Singleton instance
    private ArrayList<PhieuDatBan> danhSachPhieu;
    private int maPhieuTiepTheo;
    private Map<Integer, ArrayList<ChiTietDatMon>> gioHangTamThoi;
    private Map<Integer, iuh.fit.son23641341.nhahanglau_phantan.entity.KhuyenMai> khuyenMaiTamThoi; // Lưu khuyến mãi đã chọn cho từng bàn

    // Private constructor
    private PhieuDatBan_Ctr() {
        danhSachPhieu = new ArrayList<>(MockData.phieuDats());
        maPhieuTiepTheo = danhSachPhieu.size() + 1;
        gioHangTamThoi = new HashMap<>();
        khuyenMaiTamThoi = new HashMap<>();
    }

    // Lấy instance duy nhất
    public static PhieuDatBan_Ctr getInstance() {
        if (instance == null) {
            instance = new PhieuDatBan_Ctr();
        }
        return instance;
    }

    // Tạo phiếu đặt

    // Phương thức khởi tạo mã phiếu

    public static String taoMaPhieu(String ngayDat, String gioDat, String sdt) {
        String nam = ngayDat.substring(6, 10);
        String thang = ngayDat.substring(3, 5);
        String ngay = ngayDat.substring(0, 2);
        String datePartFormatted = nam + thang + ngay;

        String timePartFormatted = gioDat.replace(":", "");

        String lastThreeSdt = "";
        if (sdt != null && sdt.length() >= 3) {
            lastThreeSdt = sdt.substring(sdt.length() - 3);
        } else {
            lastThreeSdt = "000";
        }

        Random random = new Random();
        int randomNum = random.nextInt(100);
        String randomPart = String.format("%02d", randomNum);

        String maPhieu = "PD" + datePartFormatted + timePartFormatted + lastThreeSdt + randomPart;

        return maPhieu;
    }

    // 1. Thêm phiếu đặt mới
    public boolean themPhieuDat(PhieuDatBan phieu) {
        // Tạo mã phiếu theo định dạng chuẩn
        String maPhieu = taoMaPhieu(phieu.getNgayDat(), phieu.getGioDat(), 
                                     phieu.getSdtDat() != null ? phieu.getSdtDat() : "000");
        phieu.setMaPhieu(maPhieu);
        
        // Lưu vào bộ nhớ
        danhSachPhieu.add(phieu);
        
        // Lưu vào database
        PhieuDat_DAO phieuDAO = new PhieuDat_DAO();
        boolean result = phieuDAO.insertPhieuDat(phieu);
        
        if (result) {
            System.out.println("=== LƯU PHIẾU ĐẶT THÀNH CÔNG VÀO DB ===");
            System.out.println("Mã phiếu: " + phieu.getMaPhieu());
            System.out.println("Mã khách hàng: " + phieu.getMaKhachHang());
            System.out.println("Tên khách: " + phieu.getTenKhachDat());
        } else {
            System.err.println("=== LỖI KHI LƯU PHIẾU ĐẶT VÀO DB ===");
        }
        
        return result;
    }

    public PhieuDatBan timPhieuTheoMaBan(int maBan) {
        for (PhieuDatBan phieu : danhSachPhieu) {
            if (phieu.getMaBan() == maBan) {
                return phieu;
            }
        }
        return null;
    }

    // 3. Tìm phiếu đặt theo mã phiếu
    public PhieuDatBan timPhieuTheoMaPhieu(String maPhieu) {
        for (PhieuDatBan phieu : danhSachPhieu) {
            if (phieu.getMaPhieu().equals(maPhieu)) {
                return phieu;
            }
        }
        return null;
    }

    // 4. Cập nhật phiếu đặt
    public boolean capNhatPhieuDat(PhieuDatBan phieuCapNhat) {
        PhieuDatBan phieu = timPhieuTheoMaPhieu(phieuCapNhat.getMaPhieu());
        if (phieu == null) {
            return false;
        }
        phieu.setTenKhachDat(phieuCapNhat.getTenKhachDat());
        phieu.setSdtDat(phieuCapNhat.getSdtDat());
        phieu.setEmailDat(phieuCapNhat.getEmailDat());
        phieu.setNgayDat(phieuCapNhat.getNgayDat());
        phieu.setThoiGianDat(phieuCapNhat.getThoiGianDat());
        phieu.setTienCoc(phieuCapNhat.getTienCoc());
        phieu.setTrangThai(phieuCapNhat.getTrangThai());
        phieu.setMaNhanVien(phieuCapNhat.getMaNhanVien());
        return true;
    }

    // 5. Hủy phiếu đặt
    public boolean huyPhieuDat(int maBan) {
        PhieuDatBan phieu = timPhieuTheoMaBan(maBan);
        if (phieu == null) {
            return false;
        }

        phieu.setTrangThai("Đã hủy");
        return true;
    }

    // 6. Xóa phiếu đặt (xóa hoàn toàn khỏi danh sách)
    // Phương thức này sẽ xóa TẤT CẢ các phiếu trong nhóm và cập nhật trạng thái TẤT
    // CẢ các bàn
    public boolean xoaPhieuDat(int maBan) {
        PhieuDatBan phieu = timPhieuTheoMaBan(maBan);
        if (phieu == null) {
            return false;
        }
        boolean ketQua = danhSachPhieu.remove(phieu);
        if (ketQua) {
            BanAn_Ctr banAnCtr = BanAn_Ctr.getInstance();
            banAnCtr.capNhatTrangThai(maBan, "Trống");
        }
        return ketQua;
    }

    // 7. Lấy tất cả phiếu đặt
    public ArrayList<PhieuDatBan> layTatCaPhieu() {
        return danhSachPhieu;
    }

    // 8. Lấy phiếu đặt theo trạng thái
    public ArrayList<PhieuDatBan> layPhieuTheoTrangThai(String trangThai) {
        ArrayList<PhieuDatBan> ketQua = new ArrayList<>();
        for (PhieuDatBan phieu : danhSachPhieu) {
            if (phieu.getTrangThai().equals(trangThai)) {
                ketQua.add(phieu);
            }
        }
        return ketQua;
    }

    // 9. Đếm số phiếu đặt theo trạng thái
    public int demPhieuTheoTrangThai(String trangThai) {
        int dem = 0;
        for (PhieuDatBan phieu : danhSachPhieu) {
            if (phieu.getTrangThai().equals(trangThai)) {
                dem++;
            }
        }
        return dem;
    }

    // Tạo phiếu đặt
    public PhieuDatBan taoPhieuDat(String tenKH, String sdt, String ngayDat, String gioDat, String phuongThuc,
            String emailDat, ArrayList<Integer> danhSachBan, List<? extends ChiTietDatMon> danhSachMon, double giamGia) {
        String maPhieu = taoMaPhieu(ngayDat, gioDat, sdt);
        String maKhachHang = null;
        // Nếu sdt không rỗng, thử lấy mã khách hàng từ DB
        if (sdt != null && !sdt.trim().isEmpty()) {
            KhachHang_DAO khachHangDAO = new KhachHang_DAO();
            maKhachHang = khachHangDAO.getMaKhachHangBySDT(sdt);
        }

        NhanVien nvHienTai = User_Ctr.getInstance().getNhanVienHienTai();
        ArrayList<ChiTietDatMon> danhSachMonCopy = new ArrayList<>();
        if (danhSachMon != null) {
            danhSachMonCopy.addAll(danhSachMon);
        }
        PhieuDatBan phieu = new PhieuDatBan(maPhieu, maKhachHang, tenKH, sdt, emailDat, "Đặt trước",
                nvHienTai.getManv(), ngayDat,
                gioDat,
                new Timestamp(System.currentTimeMillis()), giamGia, danhSachMonCopy, danhSachBan);

        // Tính tổng tiền món ăn
        double tongTienMonAn = 0;
        if (danhSachMon != null) {
            for (ChiTietDatMon ct : danhSachMonCopy) {
                tongTienMonAn += ct.getMonAn().getGia() * ct.getSoLuong();
            }
        }
        int soLuongBan = danhSachBan != null ? danhSachBan.size() : 1;
        double phiBan = 250000 * soLuongBan;
        double phiMonAn = tongTienMonAn * 0.3;
        double tienCoc = phiBan + phiMonAn;
        double tongTien = tongTienMonAn + tienCoc - giamGia;
        phieu.setTienCoc(tienCoc);
        phieu.setTongTien(tongTien);
        System.out.println("Phieu dat ban vua tao: " + phieu.toString());
        PhieuDat_DAO phieuDatDAO = new PhieuDat_DAO();
        phieuDatDAO.insertPhieuDat(phieu);
        return phieu;
    }

    // 12. Cập nhật danh sách món ăn cho bàn
    public void capNhatDanhSachMonAn(int maBan, List<? extends ChiTietDatMon> danhSachMon) {
        ArrayList<ChiTietDatMon> danhSachMonCopy = new ArrayList<>();
        if (danhSachMon != null) {
            danhSachMonCopy.addAll(danhSachMon);
        }
        PhieuDatBan phieu = timPhieuTheoMaBan(maBan);
        if (phieu != null) {
            // Nếu là phiếu đã tồn tại (Đặt trước, Đang sử dụng) -> cập nhật trực tiếp
            phieu.setDanhSachMonAn(danhSachMonCopy);
        } else {
            // Nếu là phiếu mới (chưa có trong CSDL) -> lưu vào giỏ hàng tạm
            gioHangTamThoi.put(maBan, danhSachMonCopy);
        }
    }

    // 13. Xóa giỏ hàng tạm thời khi phiếu được lưu
    public void xoaGioHangTamThoi(int maBan) {
        gioHangTamThoi.remove(maBan);
    }

    public ArrayList<ChiTietDatMon> layDanhSachMonAnChoBan(int maBan) {
        PhieuDatBan phieu = timPhieuTheoMaBan(maBan);
        if (phieu != null) {
            return phieu.getDanhSachMonAn();
        }
        // Nếu chưa có phiếu, trả về giỏ hàng tạm thời nếu có
        ArrayList<ChiTietDatMon> gioHang = gioHangTamThoi.get(maBan);
        if (gioHang != null) {
            return gioHang;
        }
        return new ArrayList<>();
    }

    // 14. Kiểm tra bàn có trùng lịch không (trùng cả ngày VÀ giờ)
    public boolean kiemTraTrungLich(int maBan, String ngayDat, String gioDat) {
        for (PhieuDatBan phieu : danhSachPhieu) {
            // Bỏ qua phiếu đã hủy
            if (phieu.getTrangThai().equals("Đã hủy")) {
                continue;
            }
            if (phieu.getMaBan() == maBan && phieu.getNgayDat().equals(ngayDat)) {
                // So sánh giờ đặt nếu cần, ở đây chỉ so sánh ngày
                return true;
            }
        }
        return false;
    }

    // 15. Kiểm tra danh sách bàn có bàn nào trùng lịch không
    public ArrayList<Integer> layDanhSachBanTrungLich(ArrayList<Integer> danhSachMaBan, String ngayDat, String gioDat) {
        ArrayList<Integer> banTrungLich = new ArrayList<>();
        for (Integer maBan : danhSachMaBan) {
            if (kiemTraTrungLich(maBan, ngayDat, gioDat)) {
                banTrungLich.add(maBan);
            }
        }
        return banTrungLich;
    }

    // 16. Lưu khuyến mãi tạm thời cho bàn
    public void luuKhuyenMaiTamThoi(int maBan, iuh.fit.son23641341.nhahanglau_phantan.entity.KhuyenMai khuyenMai) {
        if (khuyenMai != null) {
            khuyenMaiTamThoi.put(maBan, khuyenMai);
        } else {
            khuyenMaiTamThoi.remove(maBan);
        }
    }

    // 17. Lấy khuyến mãi tạm thời của bàn
    public iuh.fit.son23641341.nhahanglau_phantan.entity.KhuyenMai layKhuyenMaiTamThoi(int maBan) {
        return khuyenMaiTamThoi.get(maBan);
    }

    // 18. Xóa khuyến mãi tạm thời của bàn
    public void xoaKhuyenMaiTamThoi(int maBan) {
        khuyenMaiTamThoi.remove(maBan);
    }

    // 19. Cập nhật món ăn của phiếu đặt
    public boolean capNhatMonAnCuaPhieu(String maPhieu, List<? extends ChiTietDatMon> danhSachMon) {
        ArrayList<ChiTietDatMon> danhSachMonCopy = new ArrayList<>();
        if (danhSachMon != null) {
            danhSachMonCopy.addAll(danhSachMon);
        }
        PhieuDat_DAO phieuDAO = new PhieuDat_DAO();
        return phieuDAO.capNhatMonAnCuaPhieu(maPhieu, danhSachMonCopy);
    }

    // 20. Cập nhật trạng thái phiếu đặt
    public boolean capNhatTrangThaiPhieu(String maPhieu, String trangThai) {
        PhieuDat_DAO phieuDAO = new PhieuDat_DAO();
        return phieuDAO.capNhatTrangThai(maPhieu, trangThai);
    }
    
    // 21. Lấy danh sách phiếu đặt theo ngày
    public ArrayList<PhieuDatBan> layPhieuDatTheoNgay(String ngay) {
        PhieuDat_DAO phieuDAO = new PhieuDat_DAO();
        return phieuDAO.getPhieuDatByNgay(ngay);
    }
    
    // 22. Lấy danh sách phiếu đặt theo bàn và ngày
    public ArrayList<PhieuDatBan> layPhieuDatTheoBanVaNgay(int maBan, String ngay) {
        PhieuDat_DAO phieuDAO = new PhieuDat_DAO();
        return phieuDAO.getPhieuDatByBanVaNgay(maBan, ngay);
    }
    
    // 23. Tìm phiếu đang sử dụng theo mã bàn
    public PhieuDatBan layPhieuDangSuDungTheoMaBan(int maBan) {
        PhieuDat_DAO phieuDAO = new PhieuDat_DAO();
        return phieuDAO.getPhieuDangSuDungTheoMaBan(maBan);
    }

}

