package iuh.fit.son23641341.nhahanglau_phantan.entity;
import java.util.Objects;


public class NhanVien {

    // 1. Khai báo thuộc tính
    private String manv;
    private String hoten;
    private String gioiTinh;
    private String caLamViec;
    private String sdt;
    private String email;
    private String chucVu;
    private int idUser ; 

    // --- Constructors ---


    public NhanVien() {
    }

    public NhanVien(String manv, String hoten, String gioiTinh, String caLamViec, String sdt, String email, String chucVu, int idUser) throws Exception {
        this.manv = manv;
        this.hoten = hoten;
		this.gioiTinh = gioiTinh;
		this.caLamViec = caLamViec;
		this.sdt = sdt;
		this.email = email;
		this.chucVu = chucVu;		
		this.idUser = idUser ;
    }

    // --- 2. Viết các phương thức getter, setter ---

    // --- manv ---
    public String getManv() {
        return manv;
    }

    public void setManv(String manv) throws Exception {
		if (manv == null || manv.trim().isEmpty()) {
			throw new Exception("Mã nhân viên không được rỗng");
		}
        this.manv = manv;
    }

    // --- hoten ---
    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) throws Exception {
        if (hoten == null || hoten.trim().isEmpty()) {
            throw new Exception("Họ tên không được rỗng");
        }
        this.hoten = hoten;
    }

    // --- gioiTinh ---
    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) throws Exception {
        if (gioiTinh != null && (gioiTinh.equals("Nam") || gioiTinh.equals("Nữ"))) {
            this.gioiTinh = gioiTinh;
        } else {
            throw new Exception("Giới tính chỉ được Nam hoặc Nữ");
        }
    }

    // --- caLamViec ---
    public String getCaLamViec() {
        return caLamViec;
    }

    public void setCaLamViec(String caLamViec) throws Exception {
        if (caLamViec != null && (caLamViec.equals("Ca Sáng") || caLamViec.equals("Ca Tối") || caLamViec.equals("Ca Full"))) {
            this.caLamViec = caLamViec;
        } else {
            throw new Exception("Ca làm việc chỉ nhận giá trị: \"Ca Sáng\", \"Ca Tối\", \"Ca Full\"");
        }
    }

    // --- sdt ---
    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) throws Exception {
        if (sdt == null) {
            throw new Exception("Số điện thoại không được rỗng");
        }
        // Regex cho 10 chữ số
        String regex = "^\\d{10}$";
        if (!sdt.matches(regex)) {
            throw new Exception("Số điện thoại phải đúng định dạng 10 chữ số");
        }
        this.sdt = sdt;
    }

    // --- email ---
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws Exception {
        if (email == null) {
            throw new Exception("Email không được rỗng");
        }
        // Regex cho định dạng a-z0-9@gmail.com
        String regex = "^[a-z0-9]+@gmail\\.com$";
        if (!email.matches(regex)) {
            throw new Exception("Email phải đúng định dạng: a-z0-9@gmail.com");
        }
        this.email = email;
    }

    // --- chucVu ---
    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) throws Exception {
        if (chucVu != null && (chucVu.equals("Quản lý") || chucVu.equals("Thu ngân"))) {
            this.chucVu = chucVu;
        } else {
            throw new Exception("Chức vụ chỉ nhận giá trị: \"Quản lý\" hoặc \"Thu ngân\"");
        }
    }
    // --- idUser ---
    
    public int getIdUser() {
		return idUser;
	}

    // --- Aliases for UML naming ---
    public String getMaNhanVien() {
        return getManv();
    }

    public void setMaNhanVien(String maNhanVien) throws Exception {
        setManv(maNhanVien);
    }

    public String getHoTen() {
        return getHoten();
    }

    public void setHoTen(String hoTen) throws Exception {
        setHoten(hoTen);
    }

    public String getSoDienThoai() {
        return getSdt();
    }

    public void setSoDienThoai(String sdt) throws Exception {
        setSdt(sdt);
    }

    // --- Phương thức tiện ích (toString, equals, hashCode) ---

    @Override
    public String toString() {
        return "NhanVien{" +
                "manv=" + manv +
                ", hoten='" + hoten + '\'' +
                ", gioiTinh='" + gioiTinh + '\'' +
                ", caLamViec='" + caLamViec + '\'' +
                ", sdt='" + sdt + '\'' +
                ", email='" + email + '\'' +
                ", chucVu='" + chucVu + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NhanVien nhanVien = (NhanVien) o;
        return manv != null && manv.equals(nhanVien.manv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(manv);
    }
}


