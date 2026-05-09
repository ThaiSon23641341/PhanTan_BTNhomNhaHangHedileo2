package iuh.fit.son23641341.nhahanglau_phantan.control;

import iuh.fit.son23641341.nhahanglau_phantan.dao.User_DAO;
import iuh.fit.son23641341.nhahanglau_phantan.entity.NhanVien;
import iuh.fit.son23641341.nhahanglau_phantan.util.EntityManagerFactoryUtil;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;

public class User_Ctr {
    private static User_Ctr instance;

    private NhanVien nhanVienHienTai;
    private String usernameHienTai;

    private int selectedMonth;
    private int selectedYear;

    // Khai báo JPA và DAO
    private EntityManager em;
    private User_DAO userDAO;

    public User_Ctr() {
        LocalDate now = LocalDate.now();
        this.selectedMonth = now.getMonthValue();
        this.selectedYear = now.getYear();

        // Khởi tạo EntityManager và truyền vào User_DAO
        this.em = EntityManagerFactoryUtil.getEntityManager();
        this.userDAO = new User_DAO(this.em);
    }

    public static User_Ctr getInstance() {
        if (instance == null) instance = new User_Ctr();
        return instance;
    }

    // ĐÃ SỬA: Kiểm tra đăng nhập bằng JPA qua User_DAO
    public boolean kiemTraDangNhap(String username, String password) {
        if (userDAO.authenticate(username, password)) {
            try {
                // Lấy đối tượng NhanVien dựa vào username (nhờ vào relationship @OneToOne)
                String jpql = "SELECT nv FROM NhanVien nv WHERE nv.user.tenNguoiDung = :username";
                NhanVien nv = em.createQuery(jpql, NhanVien.class)
                        .setParameter("username", username)
                        .getSingleResult();

                this.nhanVienHienTai = nv;
                this.usernameHienTai = username;

                System.out.println("Đăng nhập thành công: " + nv.getHoten() + " - " + nv.getChucVu());
                return true;
            } catch (Exception e) {
                System.err.println("Lỗi truy xuất thông tin nhân viên sau khi đăng nhập!");
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    // ĐÃ SỬA: Gọi User_DAO để kiểm tra thông tin
    public boolean timKiemTaiKhoan(String hoTen, String sdt, String email) {
        return userDAO.timKiemTaiKhoan(hoTen, sdt, email);
    }

    // ĐÃ SỬA: Gọi User_DAO để thay đổi mật khẩu DB
    public boolean doiMatKhau(String hoTenNhanVien, String matKhauMoi) {
        return userDAO.doiMatKhau(hoTenNhanVien, matKhauMoi);
    }

    // ================== GETTERS & SETTERS ==================

    public void setNhanVienHienTai(NhanVien nv) {
        this.nhanVienHienTai = nv;
    }

    public void setUsernameHienTai(String username) {
        this.usernameHienTai = username;
    }

    public int getSelectedMonth() { return selectedMonth; }
    public void setSelectedMonth(int month) { this.selectedMonth = month; }

    public int getSelectedYear() { return selectedYear; }
    public void setSelectedYear(int year) { this.selectedYear = year; }

    public String getHoTenHienTai() {
        return nhanVienHienTai != null ? nhanVienHienTai.getHoten() : null;
    }

    public String getChucVuHienTai() {
        return nhanVienHienTai != null ? nhanVienHienTai.getChucVu() : null;
    }

    public String getTenNguoiDung() {
        return usernameHienTai;
    }

    public NhanVien getNhanVienHienTai() {
        return nhanVienHienTai;
    }

    public boolean isDangNhap() {
        return nhanVienHienTai != null;
    }

    public void dangXuat() {
        this.nhanVienHienTai = null;
        this.usernameHienTai = null;
        if (em != null && em.isOpen()) {
            em.clear(); // Xóa sạch bộ nhớ đệm JPA để tránh lộ dữ liệu phiên cũ
        }
    }
}