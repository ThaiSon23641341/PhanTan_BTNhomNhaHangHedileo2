package iuh.fit.son23641341.nhahanglau_phantan.control;

import iuh.fit.son23641341.nhahanglau_phantan.dao.KhachHang_DAO;
import iuh.fit.son23641341.nhahanglau_phantan.entity.ChiTietDatMon;
import iuh.fit.son23641341.nhahanglau_phantan.entity.KhachHangThanhVien;
import iuh.fit.son23641341.nhahanglau_phantan.entity.PhieuDatBan;

import java.time.LocalDate;
import java.util.ArrayList;

// NOTE: Controller now uses mock DAO data; database logic removed.
public class KhachHang_Ctr {

    private KhachHang_DAO khachHangDAO;
    private ArrayList<KhachHangThanhVien> danhSachKhachHangThanhVien;

    public KhachHang_Ctr() {
        khachHangDAO = new KhachHang_DAO();
        danhSachKhachHangThanhVien = khachHangDAO.getAllKhachHang();
    }

    public ArrayList<KhachHangThanhVien> getDanhSachKhachHang() {
        this.danhSachKhachHangThanhVien = khachHangDAO.getAllKhachHang();
        return danhSachKhachHangThanhVien;
    }
    
    /**
     * Thêm khách hàng mới vào Database.
     * @param khachHangThanhVienMoi Đối tượng KhachHang mới
     * @return true nếu thêm thành công, false nếu thất bại (ví dụ: trùng SDT)
     */
    public boolean themKhachHang(KhachHangThanhVien khachHangThanhVienMoi) {
        if (khachHangThanhVienMoi == null) {
            System.err.println("LOI: Đối tượng Khách hàng rỗng.");
            return false;
        }
        
        // 1. Kiểm tra trùng lặp Số Điện Thoại
        if (khachHangDAO.existsBySoDienThoai(khachHangThanhVienMoi.getSoDienThoai())) {
            System.err.println("THEM KH THAT BAI: Số điện thoại " + khachHangThanhVienMoi.getSoDienThoai() + " đã tồn tại.");
            return false; 
        }
        
        // **BƯỚC QUAN TRỌNG:** Tự sinh và gán Mã Khách Hàng mới trước khi insert
        String maKhachHangMoi = khachHangDAO.taoMaKhachHangMoi();
        try {
            khachHangThanhVienMoi.setMaKhachHang(maKhachHangMoi);
        } catch (Exception e) {
            System.err.println("LOI: Không thể gán Mã Khách hàng mới (Entity KhachHang cần có setter cho MaKhachHang).");
            e.printStackTrace();
            return false;
        }
        
        // Gán Ngày Đăng Ký 
        try {
            khachHangThanhVienMoi.setNgayDangKy(LocalDate.now());
        } catch (IllegalArgumentException ignored) {}
        
        // 2. Thực hiện thêm vào DB
        boolean success = khachHangDAO.themKhachHang(khachHangThanhVienMoi);
        if (success) {
               System.out.println("THEM KH THANH CONG: Đã thêm khách hàng " + khachHangThanhVienMoi.getHoTen() + " với Mã KH: " + maKhachHangMoi);
        } else {
             System.err.println("THEM KH THAT BAI: Lỗi DB khi thực thi INSERT.");
        }
        return success;
    }


    public ArrayList<KhachHangThanhVien> timKhachHangTheoSDT(String query) {
        if (query == null || query.trim().isEmpty()) {
            return khachHangDAO.getAllKhachHang();
        }
        return khachHangDAO.timKhachHangTheoSDT(query);
    }
    
    /**
     * Kiểm tra xem số điện thoại đã tồn tại ở khách hàng khác (ngoại trừ khách hàng đang sửa) không.
     * Phương thức này được GUI gọi trực tiếp trước khi update.
     * @param sdt Số điện thoại cần kiểm tra
     * @param maKhachHang Mã khách hàng đang được cập nhật
     * @return true nếu SDT trùng với khách hàng khác, false nếu không trùng.
     */
    public boolean kiemTraTrungSDTKhac(String sdt, String maKhachHang) {
        return khachHangDAO.existsBySoDienThoaiExcept(sdt, maKhachHang);
    }


    /**
     * Cập nhật thông tin của một khách hàng.
     * @param khachHangThanhVienCapNhat Đối tượng KhachHang chứa thông tin mới (phải có Mã KH)
     * @return true nếu cập nhật thành công, false nếu Mã KH không tồn tại hoặc lỗi DB.
     */
    public boolean capNhatKhachHang(KhachHangThanhVien khachHangThanhVienCapNhat) {
        if (khachHangThanhVienCapNhat == null || khachHangThanhVienCapNhat.getMaKhachHang() == null || khachHangThanhVienCapNhat.getMaKhachHang().trim().isEmpty()) {
            System.err.println("LOI: Đối tượng Khách hàng cần cập nhật rỗng hoặc thiếu Mã KH.");
            return false;
        }
        
        // **Lưu ý: Logic kiểm tra trùng SDT (existsBySoDienThoaiExcept) đã được gọi ở GUI (SuaKhachHang_Dialog)
        // nên ta bỏ qua bước này trong Controller để tránh gọi 2 lần.**
        
        // 2. Cập nhật vào DB
        boolean success = khachHangDAO.capNhatKhachHang(khachHangThanhVienCapNhat);
          if (success) {
            System.out.println("SUA KH THANH CONG: Đã cập nhật khách hàng Mã: " + khachHangThanhVienCapNhat.getMaKhachHang());
        } else {
            System.err.println("SUA KH THAT BAI: Không tìm thấy Mã KH hoặc lỗi DB khi thực thi UPDATE.");
        }
        return success;
    }
    
    /**
     * Xóa một khách hàng khỏi database bằng Mã Khách Hàng.
     * @param maKhachHang Mã KH của khách hàng cần xóa.
     * @return true nếu xóa thành công, false nếu Mã KH không tồn tại hoặc có khóa ngoại.
     */
    public boolean xoaKhachHang(String maKhachHang) {
        boolean success = khachHangDAO.xoaKhachHang(maKhachHang);
        if (success) {
            System.out.println("XOA KH THANH CONG: Đã xóa khách hàng Mã: " + maKhachHang);
        } else {
             System.err.println("XOA KH THAT BAI: Lỗi DB (có thể do khóa ngoại) hoặc không tìm thấy Mã KH: " + maKhachHang);
        }
        return success;
    }
    
    /**
     * Kiểm tra xem số điện thoại đã tồn tại trong hệ thống chưa.
     * @param sdt Số điện thoại cần kiểm tra
     * @return true nếu đã tồn tại, false nếu chưa tồn tại
     */
    public boolean existsBySoDienThoai(String sdt) {
        return khachHangDAO.existsBySoDienThoai(sdt);
    }
    
    /**
     * Tạo mã khách hàng mới.
     * @return Mã khách hàng mới
     */
    public String taoMaKhachHangMoi() {
        return khachHangDAO.taoMaKhachHangMoi();
    }
    
    
    public void xuLyCongDiemChoKhach(PhieuDatBan phieuDat) {
        if (phieuDat == null) {
        	System.out.println("Không cộng điểm: Phiếu đặt rỗng.");
        	return;
        }
        
        String maKH = phieuDat.getMaKhachHang();
        if (maKH == null || maKH.isEmpty() || maKH.equalsIgnoreCase("Khách vãng lai")) {
            System.out.println("Không cộng điểm: Khách vãng lai hoặc không có mã KH.");
            return;
        }

        double tongTienMon = 0;
        if (phieuDat.getDanhSachMonAn() != null) {
            for (ChiTietDatMon ct : phieuDat.getDanhSachMonAn()) {
                double thanhTien = ct.getMonAn().getGia() * ct.getSoLuong();
                tongTienMon += thanhTien;
            }
        }

        int diemCong = (int) (tongTienMon / 100000);

        if (diemCong > 0) {
            boolean ketQua = khachHangDAO.congDiemTichLuy(maKH, diemCong);
            if (ketQua) {
                System.out.println("Đã cộng " + diemCong + " điểm cho khách hàng " + maKH);
            } else {
                System.err.println("Lỗi khi cộng điểm cho khách hàng " + maKH);
            }
        }
    }
}
