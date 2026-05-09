package iuh.fit.son23641341.nhahanglau_phantan.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.sql.Timestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.ToString;

@Entity
@ToString (exclude = {"khachHang", "nhanVien"} )

@Table(name = "phieu_dat_ban")
public class PhieuDatBan {
    // Alias cho DAO: getDanhSachBan/setDanhSachBan (dùng chung cho mọi nơi)
    public List<Integer> getDanhSachBan() {
        if (this.danhSachBan != null && !this.danhSachBan.isEmpty()) {
            return this.danhSachBan.stream().map(BanAn::getMaBan).collect(Collectors.toList());
        }
        return this.danhSachBanDaChon;
    }

    public void setDanhSachBan(List<Integer> list) {
        this.danhSachBanDaChon = new ArrayList<>(list);
    }

    @Id
    @Column(name = "ma_phieu")
    private String maPhieu;

    @ManyToOne
    @JoinColumn(name = "ma_khach_hang")
    private KhachHangThanhVien khachHang;
    
    @Transient
    private String maKhachHang; // for compatibility

    @Transient
    private int maBan; // for compatibility

    @Column(name = "ten_khach_dat")
    private String tenKhachDat;

    @Column(name = "sdt_dat")
    private String sdtDat;

    @Column(name = "email_dat")
    private String emailDat;

    @Column(name = "trang_thai")
    private String trangThai;

    @Transient
    private String maNhanVien;

    @Column(name = "so_nguoi")
    private int soNguoi;

    @ManyToOne
    @JoinColumn(name = "ma_nhan_vien")
    private NhanVien nhanVien;

    @Column(name = "ngay_dat")
    private java.sql.Date ngayDat;

    @Column(name = "gio_dat")
    private String gioDat; 

    @Column(name = "phuong_thuc_thanh_toan")
    private String phuongThucThanhToan;

    @Column(name = "thoi_gian_dat")
    private Timestamp thoiGianDat; 

    // --- CÁC BIẾN TIỀN TỆ (Dùng double cho chính xác) ---
    @Column(name = "tien_coc")
    private double tienCoc;
    
    @Column(name = "giam_gia")
    private double giamGia;
    
    @Column(name = "tong_tien")
    private double tongTien; // Tổng tiền cuối cùng (để lưu DB)

    // --- CÁC DANH SÁCH ---
    @OneToMany(mappedBy = "phieuDatBan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChiTietDatMon> danhSachMonAn;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "phieu_dat_ban_ban_an", 
        joinColumns = @JoinColumn(name = "ma_phieu"), 
        inverseJoinColumns = @JoinColumn(name = "ma_ban")
    )
    private List<BanAn> danhSachBan;

    public void setDanhSachBanPersist(List<BanAn> list) {
        this.danhSachBan = list;
    }
    
    public List<BanAn> getDanhSachBanPersist() {
        return danhSachBan;
    }

    @ManyToMany(mappedBy = "danhSachPhieuDat")
    private List<HoaDon> hoaDons;

    @Transient
    private ArrayList<Integer> danhSachBanDaChon; // Dùng khi đặt nhiều bàn

    // ================= CONSTRUCTORS =================
    public PhieuDatBan() {
        this.danhSachMonAn = new ArrayList<>();
        this.danhSachBanDaChon = new ArrayList<>();
        this.tienCoc = 0;
        this.giamGia = 0;
        this.tongTien = 0;
        this.trangThai = "Đang chờ";
    }
    
    
    // Construtor khởi tạo của t đâu???? học hướng đối tượng chưa z ??? 
	public PhieuDatBan(String maPhieu, String maKhachHang, String tenKhachDat, String sdtDat,
			String emailDat, String trangThai, String maNhanVien, String ngayDat, String gioDat, Timestamp thoiGianDat,
			String phuongThucThanhToan, double giamGia, ArrayList<ChiTietDatMon> danhSachMonAn,
			ArrayList<Integer> danhSachBanDaChon) {
		
		
		this.maPhieu = maPhieu;
		this.maKhachHang = maKhachHang;
		this.tenKhachDat = tenKhachDat;
		this.sdtDat = sdtDat;
		this.emailDat = emailDat;
		this.trangThai = trangThai;
		this.maNhanVien = maNhanVien;
		setNgayDat(ngayDat); // Sử dụng setter để parse String -> java.sql.Date
		this.gioDat = gioDat;
		this.thoiGianDat = thoiGianDat;
		this.phuongThucThanhToan = phuongThucThanhToan;
		this.giamGia = giamGia;
		
		this.danhSachMonAn = danhSachMonAn != null ? danhSachMonAn : new ArrayList<>();
		// Gán tham chiếu phiếu đặt cho từng chi tiết món ăn
		for (ChiTietDatMon ct : this.danhSachMonAn) {
			ct.setPhieuDatBan(this);
		}
		
		if ("Đặt trước".equalsIgnoreCase(trangThai)) {
			this.tienCoc = tinhTienCoc();
		}
		this.danhSachBanDaChon = danhSachBanDaChon != null ? danhSachBanDaChon : new ArrayList<>();
	}

    // ================= GETTERS & SETTERS =================
    public String getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(String maPhieu) {
        this.maPhieu = maPhieu;
    }

    public KhachHangThanhVien getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHangThanhVien khachHang) {
        this.khachHang = khachHang;
        if (khachHang != null) {
            this.maKhachHang = khachHang.getMaKhachHang();
        }
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public int getMaBan() {
        return maBan;
    }

    public void setMaBan(int maBan) {
        this.maBan = maBan;
    }

    public String getTenKhachDat() {
        return tenKhachDat;
    }

    // Đồng bộ tên gọi: GUI dùng setTenKhachHang, Entity dùng setTenKhachDat
    // Ta tạo thêm method này để GUI gọi không bị lỗi
    public void setTenKhachHang(String ten) {
        this.tenKhachDat = ten;
    }

    public String getTenKhachHang() {
        return this.tenKhachDat;
    }

    public void setTenKhachDat(String tenKhachDat) {
        this.tenKhachDat = tenKhachDat;
    }

    public String getSdtDat() {
        return sdtDat;
    }

    // Tương tự cho SĐT
    public void setSoDienThoai(String sdt) {
        this.sdtDat = sdt;
    }

    public String getSoDienThoai() {
        return this.sdtDat;
    }

    public void setSdtDat(String sdtDat) {
        this.sdtDat = sdtDat;
    }

    public String getEmailDat() {
        return emailDat;
    }

    // Tương tự cho Email
    public void setEmail(String email) {
        this.emailDat = email;
    }

    public String getEmail() {
        return this.emailDat;
    }

    public void setEmailDat(String emailDat) {
        this.emailDat = emailDat;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public int getSoNguoi() {
        return soNguoi;
    }

    public void setSoNguoi(int soNguoi) {
        if (soNguoi > 0) {
            this.soNguoi = soNguoi;
        }
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
        if (nhanVien != null) {
            this.maNhanVien = nhanVien.getManv();
        }
    }

    public String getNgayDat() {
        if (ngayDat == null) return null;
        return new java.text.SimpleDateFormat("dd/MM/yyyy").format(ngayDat);
    }

    public void setNgayDat(String ngayDatStr) {
        try {
            if (ngayDatStr != null && !ngayDatStr.isEmpty()) {
                java.util.Date d = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(ngayDatStr);
                this.ngayDat = new java.sql.Date(d.getTime());
            } else {
                this.ngayDat = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.ngayDat = null;
        }
    }

    public java.sql.Date getNgayDatSQL() {
        return ngayDat;
    }

    public void setNgayDatSQL(java.sql.Date ngayDat) {
        this.ngayDat = ngayDat;
    }

    public String getGioDat() {
        return gioDat;
    }

    public void setGioDat(String gioDat) {
        this.gioDat = gioDat;
    }

    public Timestamp getThoiGianDat() {
        return thoiGianDat;
    }

    public void setThoiGianDat(Timestamp thoiGianDat) {
        this.thoiGianDat = thoiGianDat;
    }

    public String getPhuongThucThanhToan() {
        return phuongThucThanhToan;
    }

    public void setPhuongThucThanhToan(String phuongThucThanhToan) {
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    public double getTienCoc() {
        return tienCoc;
    }

    public void setTienCoc(double tienCoc) {
        this.tienCoc = tienCoc;
    }

    public double getGiamGia() {
        return giamGia;
    }

    public void setGiamGia(double giamGia) {
        this.giamGia = giamGia;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    // ================= QUẢN LÝ DANH SÁCH =================
    public List<Integer> getDanhSachBanDaChon() {
        if (this.danhSachBanDaChon == null) {
            this.danhSachBanDaChon = new ArrayList<>();
            if (this.danhSachBan != null) {
                for (BanAn ban : this.danhSachBan) {
                    this.danhSachBanDaChon.add(ban.getMaBan());
                }
            }
        }
        return this.danhSachBanDaChon;
    }

    // Alias để GUI gọi không lỗi
    public List<Integer> getDanhSachMaBan() {
        return getDanhSachBanDaChon();
    }

    public void setDanhSachBanDaChon(ArrayList<Integer> danhSachBanDaChon) {
        this.danhSachBanDaChon = danhSachBanDaChon;
    }

    // Alias
    public void setDanhSachMaBan(ArrayList<Integer> list) {
        setDanhSachBanDaChon(list);
    }

    public List<BanAn> getDanhSachBanEntity() {
        return danhSachBan;
    }

    public void setDanhSachBanEntity(List<BanAn> danhSachBan) {
        this.danhSachBan = danhSachBan;
    }

    public List<HoaDon> getHoaDons() {
        return hoaDons;
    }

    public void setHoaDons(List<HoaDon> hoaDons) {
        this.hoaDons = hoaDons;
    }

    public List<ChiTietDatMon> getDanhSachMonAn() {
        if (this.danhSachMonAn == null)
            this.danhSachMonAn = new ArrayList<>();
        return this.danhSachMonAn;
    }

    public void setDanhSachMonAn(List<ChiTietDatMon> danhSachMonAn) {
        this.danhSachMonAn = danhSachMonAn != null ? danhSachMonAn : new ArrayList<>();
        for (ChiTietDatMon ct : this.danhSachMonAn) {
            ct.setPhieuDatBan(this);
        }
    }

    // ================= BUSINESS LOGIC =================

    /**
     * Kiểm tra khách vãng lai
     */
    public boolean kiemTraKhachVangLai() {
        return this.tenKhachDat == null ||
                this.tenKhachDat.trim().isEmpty() ||
                this.tenKhachDat.equalsIgnoreCase("Khách vãng lai");
    }

    /**
     * Tính tổng tiền các món ăn có trong phiếu
     */
    public double tinhTongTienMonAn() {
        if (this.danhSachMonAn == null)
            return 0;
        double tong = 0;
        for (ChiTietDatMon ct : danhSachMonAn) {
            tong += ct.getMonAn().getGia() * ct.getSoLuong();
        }
        return tong;
    }

    /**
     * Tính tiền cọc (Dùng dữ liệu nội tại của object)
     * Công thức: (250k * số bàn) + (30% tổng tiền món)
     */
    public double tinhTienCoc() {
        // Nếu là khách vãng lai thì không cần cọc (hoặc tùy quy định của bạn)
        // if (kiemTraKhachVangLai()) return 0;

        // Đếm số lượng bàn
        int soLuongBan = (this.danhSachBanDaChon != null && !this.danhSachBanDaChon.isEmpty())
                ? this.danhSachBanDaChon.size()
                : 1;

        double phiBan = 250000 * soLuongBan;
        double phiMon = tinhTongTienMonAn() * 0.3;

        return phiBan + phiMon;
    }

    /**
     * Overload: Tính tiền cọc dựa trên danh sách món truyền vào (Dùng cho GUI tính
     * nháp)
     */
    public double tinhTienCoc(List<ChiTietDatMon> dsMonTam) {
        double tongMon = 0;
        if (dsMonTam != null) {
            for (ChiTietDatMon ct : dsMonTam) {
                tongMon += ct.getMonAn().getGia() * ct.getSoLuong();
            }
        }

        int soLuongBan = (this.danhSachBanDaChon != null && !this.danhSachBanDaChon.isEmpty())
                ? this.danhSachBanDaChon.size()
                : 1;

        return (250000 * soLuongBan) + (tongMon * 0.3);
    }

    /**
     * Tính tổng tiền thanh toán cuối cùng (để tham khảo)
     * Logic chính nên để ở HoaDon_Ctrl, nhưng method này giúp debug nhanh
     */
    public double tinhTongThanhToan() {
        double tongMon = tinhTongTienMonAn();
        double sauGiamGia = tongMon - this.giamGia;
        double phaiTra = sauGiamGia - this.tienCoc;
        return (phaiTra < 0) ? 0 : phaiTra;
    }

}

