package iuh.fit.son23641341.nhahanglau_phantan.entity;

import java.time.LocalDate;

/**
 * Lớp KhachHang (Customer) chứa thông tin cơ bản của một khách hàng.
 */
public class KhachHangThanhVien {
    private String maKhachHang;     
    private String hoTen;           
    private String soDienThoai;     
    private String email;           
    private String gioiTinh;        
    private String thanhVien;       
    private int diemTichLuy;
    private LocalDate ngayDangKy;


    public KhachHangThanhVien() {
    }

    public KhachHangThanhVien(String maKhachHang, String hoTen, String soDienThoai, String email, String gioiTinh, String thanhVien, int diemTichLuy, LocalDate ngayDangKy) {
        this.maKhachHang = maKhachHang;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        // Gọi setter để đảm bảo email được kiểm tra
        setEmail(email); 
        this.gioiTinh = gioiTinh;
        this.thanhVien = thanhVien;
        this.diemTichLuy = diemTichLuy;
        this.ngayDangKy = ngayDangKy;
    }



    public String getMaKhachHang() {
        return maKhachHang;
    }

    public String getHoTen() {
        return hoTen;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public String getThanhVien() {
        return thanhVien;
    }

    public int getDiemTichLuy() {
        return diemTichLuy;
    }

    public LocalDate getNgayDangKy() {
        return ngayDangKy;
    }



    public void setMaKhachHang(String maKhachHang) {
        if (maKhachHang == null || !maKhachHang.matches("KH\\d{3}")) {
            throw new IllegalArgumentException("Mã khách hàng phải có định dạng KHxxx (ví dụ: KH001).");
        }
        this.maKhachHang = maKhachHang;
    }

    public void setHoTen(String hoTen) {
        if (hoTen == null || hoTen.trim().isEmpty()) {
            throw new IllegalArgumentException("Họ tên không được để trống.");
        }
        this.hoTen = hoTen;
    }

    public void setSoDienThoai(String soDienThoai) {
        if (soDienThoai == null || !soDienThoai.matches("0\\d{9,10}")) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ (phải có 10 hoặc 11 số và bắt đầu bằng 0).");
        }
        this.soDienThoai = soDienThoai;
    }

    // ⭐ ĐÃ SỬA: Cho phép email rỗng hoặc null, chỉ kiểm tra định dạng nếu có nhập.
    public void setEmail(String email) {
        String trimmedEmail = (email == null) ? "" : email.trim();
        if (!trimmedEmail.isEmpty()) { 
            // Kiểm tra định dạng nếu email không rỗng
            if (!trimmedEmail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                throw new IllegalArgumentException("Email không hợp lệ.");
            }
        }
        this.email = trimmedEmail; 
    }

    public void setGioiTinh(String gioiTinh) {
        if (gioiTinh == null || (!gioiTinh.equalsIgnoreCase("Nam") && !gioiTinh.equalsIgnoreCase("Nữ"))) {
            throw new IllegalArgumentException("Giới tính phải là 'Nam' hoặc 'Nữ'.");
        }
        this.gioiTinh = gioiTinh;
    }

    public void setThanhVien(String thanhVien) {
        if (thanhVien == null || thanhVien.trim().isEmpty()) {
            throw new IllegalArgumentException("Trạng thái thành viên không được để trống.");
        }
        this.thanhVien = thanhVien;
    }

    public void setDiemTichLuy(int diemTichLuy) {
        if (diemTichLuy < 0) {
            throw new IllegalArgumentException("Điểm tích lũy phải >= 0.");
        }
        this.diemTichLuy = diemTichLuy;
    }

    public void setNgayDangKy(LocalDate ngayDangKy) {
        // Có thể cần nới lỏng ràng buộc này tùy vào logic ứng dụng (ví dụ: ngày quá khứ vẫn ok)
        // Hiện tại giữ nguyên logic cũ:
        if (ngayDangKy == null || ngayDangKy.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày đăng ký phải là ngày hiện tại hoặc trước đó.");
        }
        this.ngayDangKy = ngayDangKy;
    }



    @Override
    public String toString() {
        return "KhachHang{" +
                "maKhachHang='" + maKhachHang + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", email='" + email + '\'' +
                ", gioiTinh='" + gioiTinh + '\'' +
                ", thanhVien='" + thanhVien + '\'' +
                ", diemTichLuy=" + diemTichLuy +
                ", ngayDangKy=" + ngayDangKy +
                '}';
    }
}