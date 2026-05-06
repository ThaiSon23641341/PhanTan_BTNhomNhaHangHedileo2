package iuh.fit.son23641341.nhahanglau_phantan.entity;

import java.util.ArrayList;
import java.sql.Timestamp;

public class PhieuDatBan {
    // Alias cho DAO: getDanhSachBan/setDanhSachBan (dùng chung cho mọi nơi)
    public ArrayList<Integer> getDanhSachBan() {
        return getDanhSachBanDaChon();
    }

    public void setDanhSachBan(ArrayList<Integer> list) {
        setDanhSachBanDaChon(list);
    }

    private String maPhieu;
    private String maKhachHang;
    private int maBan;
    private String tenKhachDat;
    private String sdtDat;
    private String emailDat;
    private String trangThai;
    private String maNhanVien;
    private int soNguoi;
    private NhanVien nhanVien;
    private String ngayDat;
    private String gioDat; 
    private String phuongThucThanhToan; // Thêm phương thức thanh toán
    private Timestamp thoiGianDat; // thời gian mà Nhân viên bấm  cái phiếu đặt đó 

    // --- CÁC BIẾN TIỀN TỆ (Dùng double cho chính xác) ---
    private double tienCoc;
    private double giamGia;
    private double tongTien; // Tổng tiền cuối cùng (để lưu DB)

    // --- CÁC DANH SÁCH ---
    private ArrayList<ChiTietDatMon> danhSachMonAn;
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
			 double giamGia, ArrayList<ChiTietDatMon> danhSachMonAn,
			ArrayList<Integer> danhSachBanDaChon) {
		
		
		this.maPhieu = maPhieu;
		this.maKhachHang = maKhachHang;
		this.tenKhachDat = tenKhachDat;
		this.sdtDat = sdtDat;
		this.emailDat = emailDat;
		this.trangThai = trangThai;
		this.maNhanVien = maNhanVien;
		this.ngayDat = ngayDat;
		this.gioDat = gioDat;
		this.thoiGianDat = thoiGianDat;
		this.giamGia = giamGia;
		if ("Đặt trước".equalsIgnoreCase(trangThai)) {
		this.tienCoc = tinhTienCoc();
		}
		this.danhSachMonAn = danhSachMonAn != null ? danhSachMonAn : new ArrayList<>();
		this.danhSachBanDaChon = danhSachBanDaChon != null ? danhSachBanDaChon : new ArrayList<>();
	}

    // ================= GETTERS & SETTERS =================
    public String getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(String maPhieu) {
        this.maPhieu = maPhieu;
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
        return ngayDat;
    }

    public void setNgayDat(String ngayDat) {
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
    public ArrayList<Integer> getDanhSachBanDaChon() {
        if (this.danhSachBanDaChon == null)
            this.danhSachBanDaChon = new ArrayList<>();
        return this.danhSachBanDaChon;
    }

    // Alias để GUI gọi không lỗi
    public ArrayList<Integer> getDanhSachMaBan() {
        return getDanhSachBanDaChon();
    }

    public void setDanhSachBanDaChon(ArrayList<Integer> danhSachBanDaChon) {
        this.danhSachBanDaChon = danhSachBanDaChon;
    }

    // Alias
    public void setDanhSachMaBan(ArrayList<Integer> list) {
        setDanhSachBanDaChon(list);
    }

    public ArrayList<ChiTietDatMon> getDanhSachMonAn() {
        if (this.danhSachMonAn == null)
            this.danhSachMonAn = new ArrayList<>();
        return this.danhSachMonAn;
    }

    public void setDanhSachMonAn(ArrayList<ChiTietDatMon> danhSachMonAn) {
        this.danhSachMonAn = danhSachMonAn;
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
    public double tinhTienCoc(ArrayList<ChiTietDatMon> dsMonTam) {
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

	@Override
	public String toString() {
		return "PhieuDatBan [maPhieu=" + maPhieu + ", maKhachHang=" + maKhachHang + ", maBan=" + maBan
				+ ", tenKhachDat=" + tenKhachDat + ", sdtDat=" + sdtDat + ", emailDat=" + emailDat + ", trangThai="
				+ trangThai + ", maNhanVien=" + maNhanVien + ", ngayDat=" + ngayDat + ", gioDat=" + gioDat
				+ ", phuongThucThanhToan=" + phuongThucThanhToan + ", thoiGianDat=" + thoiGianDat + ", tienCoc="
				+ tienCoc + ", giamGia=" + giamGia + ", tongTien=" + tongTien + ", danhSachMonAn=" + danhSachMonAn
				+ ", danhSachBanDaChon=" + danhSachBanDaChon + "]";
	}
}

