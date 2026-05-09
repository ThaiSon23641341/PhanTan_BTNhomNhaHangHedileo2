package iuh.fit.son23641341.nhahanglau_phantan.entity;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "hoa_don")
public class HoaDon {
    @Id
    @Column(name = "ma_hoa_don")
    private String maHoaDon;
    
    @Column(name = "ngay_lap")
    private Date ngayLap;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "hoa_don_phieu_dat",
        joinColumns = @JoinColumn(name = "ma_hoa_don"),
        inverseJoinColumns = @JoinColumn(name = "ma_phieu")
    )
    private List<PhieuDatBan> danhSachPhieuDat; 
    
    @Column(name = "trang_thai")
    private String trangThai;
    
    @Column(name = "tong_tien")
    private double tongTien;
    
    @Transient
    private String maKhuyenMai;  
    
    @Column(name = "loai_hoa_don")
    private String loaiHoaDon;    
    
    @Column(name = "gio_lap_hoa_don")
    private Time gioLapHoaDon;
    
    @ManyToOne
    @JoinColumn(name = "ma_khach_hang")
    private KhachHangThanhVien khachHangThanhVien;
    
    @ManyToOne
    @JoinColumn(name = "ma_khuyen_mai")
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
        this.danhSachPhieuDat = new ArrayList<>();
        if (phieuDat != null) {
            this.danhSachPhieuDat.add(phieuDat);
        }
        this.trangThai = trangThai;
        this.tongTien = tongTien;
        this.maKhuyenMai = maKhuyenMai;
        this.loaiHoaDon = loaiHoaDon;
        this.gioLapHoaDon = gioLapHoaDon;
    }


    public HoaDon(PhieuDatBan phieuDat) {
        this.maHoaDon = "HD" + System.currentTimeMillis();
        
        this.danhSachPhieuDat = new ArrayList<>();
        if (phieuDat != null) {
            this.danhSachPhieuDat.add(phieuDat);
        }
        
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
        
        // 7. Ghi nhận Khách hàng thành viên
        if (phieuDat.getKhachHang() != null) {
            this.khachHangThanhVien = phieuDat.getKhachHang();
        } else if (phieuDat.getMaKhachHang() != null && !phieuDat.getMaKhachHang().trim().isEmpty()) {
            this.khachHangThanhVien = new KhachHangThanhVien();
            this.khachHangThanhVien.setMaKhachHang(phieuDat.getMaKhachHang());
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
        return (danhSachPhieuDat != null && !danhSachPhieuDat.isEmpty()) ? danhSachPhieuDat.get(0) : null;
    }

    public void setPhieuDat(PhieuDatBan phieuDat) {
        if (this.danhSachPhieuDat == null) {
            this.danhSachPhieuDat = new ArrayList<>();
        }
        if (phieuDat != null && !this.danhSachPhieuDat.contains(phieuDat)) {
            this.danhSachPhieuDat.add(phieuDat);
        }
    }
    
    public List<PhieuDatBan> getDanhSachPhieuDat() {
        return danhSachPhieuDat;
    }

    public void setDanhSachPhieuDat(List<PhieuDatBan> danhSachPhieuDat) {
        this.danhSachPhieuDat = danhSachPhieuDat;
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