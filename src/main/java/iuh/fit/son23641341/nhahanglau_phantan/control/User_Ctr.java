package iuh.fit.son23641341.nhahanglau_phantan.control;

import iuh.fit.son23641341.nhahanglau_phantan.entity.NhanVien;
import iuh.fit.son23641341.nhahanglau_phantan.dao.NhanVien_DAO; // Nhớ import DAO này
import java.time.LocalDate; // for default month/year

// NOTE: Controller now uses mock data; database logic removed.
public class User_Ctr {
    private static User_Ctr instance;
    
    private NhanVien nhanVienHienTai;
    private String usernameHienTai;

    // NEW: store selected month/year (optional; defaults to current month/year)
    private int selectedMonth;
    private int selectedYear;

    public User_Ctr() { 
        LocalDate now = LocalDate.now();
        this.selectedMonth = now.getMonthValue();
        this.selectedYear = now.getYear();
    }

    public static User_Ctr getInstance() {
        if (instance == null) instance = new User_Ctr();
        return instance;
    }

    public boolean kiemTraDangNhap(String username, String password) {
        NhanVien_DAO nvDao = new NhanVien_DAO();
        NhanVien nv = nvDao.timNhanVienTheoDangNhap(username, password);

        if (nv != null) {
            this.nhanVienHienTai = nv;
            
            System.out.println("Đăng nhập thành công: " + nv.getHoten() + " - " + nv.getChucVu());
            return true;
        }

        return false;
    }
    // ====================================
    
    public void setNhanVienHienTai(NhanVien nv) {
        this.nhanVienHienTai = nv;
    }
    
    public void setUsernameHienTai(String username) {
		this.usernameHienTai = username;
	}
    
    // NEW: getters/setters for month/year
    public int getSelectedMonth() {
        return selectedMonth;
    }

    public void setSelectedMonth(int month) {
        this.selectedMonth = month;
    }

    public int getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedYear(int year) {
        this.selectedYear = year;
    }
    
    
    public String getHoTenHienTai() {
        if (nhanVienHienTai != null) {
            return nhanVienHienTai.getHoten();
        }
        return null;
    }

    /**
     * Lấy Chức Vụ từ đối tượng Nhanvien hiện tại
     */
    public String getChucVuHienTai() {
        if (nhanVienHienTai != null) {
            return nhanVienHienTai.getChucVu();
        }
        return null;
    }

    /**
     * Lấy Tên đăng nhập (Username)
     */
    public String getTenNguoiDung() {
        return usernameHienTai;
    }

    // ... (Các phần khác giữ nguyên) ...
    
    public NhanVien getNhanVienHienTai() {
        return nhanVienHienTai;
    }

    public boolean isDangNhap() {
        return nhanVienHienTai != null;
    }

    public void dangXuat() {
        this.nhanVienHienTai = null;
        this.usernameHienTai = null;
    }
    
    
    public boolean timKiemTaiKhoan(String email, String sdt, String tenho) { 
    	return false; 
    }
    
    public boolean doiMatKhau(String username, String matKhauMoi) {
		return false;
	}
}

