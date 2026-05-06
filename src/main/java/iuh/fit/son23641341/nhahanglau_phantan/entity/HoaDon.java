package iuh.fit.son23641341.nhahanglau_phantan.entity;

import java.sql.Date;
import java.sql.Time;

public class HoaDon {
    private String maHoaDon;
    private Date ngayLap;
    private PhieuDatBan phieuDat; 
    private String trangThai;
    private double tongTien;
    private String maKhuyenMai;  
    private String loaiHoaDon;    
    private Time gioLapHoaDon;
    private KhachHangThanhVien khachHangThanhVien;
    private KhuyenMai khuyenMai;

    // ================= CONSTRUCTORS =================

    // 1. Constructor mặc định
    public HoaDon() {
        super();
    }

    // 2. Constructor đầy đủ 
    public HoaDon(String maHoaDon, Date ngayLap, PhieuDatBan phieuDat, String trangThai, 
                  double tongTien, String maKhuyenMai, String loaiHoaDon, Time gioLapHoaDon) {
        this.maHoaDon = maHoaDon;
        this.ngayLap = ngayLap;
        this.phieuDat = phieuDat;
        this.trangThai = trangThai;
        this.tongTien = tongTien;
        this.maKhuyenMai = maKhuyenMai;
        this.loaiHoaDon = loaiHoaDon;
        this.gioLapHoaDon = gioLapHoaDon;
    }


    public HoaDon(PhieuDatBan phieuDat) {
        this.maHoaDon = "HD" + System.currentTimeMillis();
        
        this.phieuDat = phieuDat;
        
        long now = System.currentTimeMillis();
        this.ngayLap = new Date(now);
        this.gioLapHoaDon = new Time(now);
        
        // 4. Trạng thái & Khuyến mãi cố định
        this.trangThai = "Đã thanh toán";
        this.maKhuyenMai = "KM004"; // Cố định theo yêu cầu
        
		// 5. Tính Tổng Tiền từ Phiếu Đặt
        this.tongTien = phieuDat.tinhTienCoc(); 
        
        // 6. Xử lý Logic Loại Hóa Đơn
        String trangThaiPhieu = phieuDat.getTrangThai();
        if (trangThaiPhieu != null) {
            if (trangThaiPhieu.equalsIgnoreCase("Đặt trước")) {
                this.loaiHoaDon = "Đặt trước";
            } else if (trangThaiPhieu.equalsIgnoreCase("Đã xác nhận") || 
                       trangThaiPhieu.equalsIgnoreCase("Đang sử dụng")) {
                // Mở rộng thêm 'Đang sử dụng' để an toàn
                this.loaiHoaDon = "Tại chỗ";
            } else {
                this.loaiHoaDon = "Tại chỗ"; // Mặc định khác
            }
        } else {
            this.loaiHoaDon = "Tại chỗ";
        }
    }

    // ================= GETTERS & SETTERS =================

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public Date getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(java.util.Date ngayLap) {
        if (ngayLap != null) {
            this.ngayLap = new Date(ngayLap.getTime());
        } else {
            this.ngayLap = null;
        }
    }

    public PhieuDatBan getPhieuDat() {
        return phieuDat;
    }

    public void setPhieuDat(PhieuDatBan phieuDat) {
        this.phieuDat = phieuDat;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public String getMaKhuyenMai() {
        return maKhuyenMai;
    }

    public void setMaKhuyenMai(String maKhuyenMai) {
        this.maKhuyenMai = maKhuyenMai;
    }

    public String getLoaiHoaDon() {
        return loaiHoaDon;
    }

    public void setLoaiHoaDon(String loaiHoaDon) {
        this.loaiHoaDon = loaiHoaDon;
    }

    public Time getGioLapHoaDon() {
        return gioLapHoaDon;
    }

    public void setGioLapHoaDon(Time gioLapHoaDon) {
        this.gioLapHoaDon = gioLapHoaDon;
    }

    public KhachHangThanhVien getKhachHang() {
        return khachHangThanhVien;
    }

    public void setKhachHang(KhachHangThanhVien khachHangThanhVien) {
        this.khachHangThanhVien = khachHangThanhVien;
    }

    public KhuyenMai getKhuyenMai() {
        return khuyenMai;
    }

    public void setKhuyenMai(KhuyenMai khuyenMai) {
        this.khuyenMai = khuyenMai;
    }

    @Override
    public String toString() {
        return "HoaDon{" + "maHoaDon=" + maHoaDon + ", loai=" + loaiHoaDon + ", tien=" + tongTien + '}';
    }
}