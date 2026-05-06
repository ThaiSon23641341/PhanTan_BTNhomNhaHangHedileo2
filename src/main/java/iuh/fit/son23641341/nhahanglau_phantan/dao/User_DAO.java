package iuh.fit.son23641341.nhahanglau_phantan.dao;

import iuh.fit.son23641341.nhahanglau_phantan.entity.NhanVien;
import iuh.fit.son23641341.nhahanglau_phantan.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

public class User_DAO implements IUserDAO {

    // Sử dụng EntityManager để tương tác với Database thay vì MockData
    private EntityManager em;

    // Trong mô hình phân tán (JPA), ta thường truyền EntityManager vào thông qua constructor
    public User_DAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public boolean authenticate(String username, String password) {
        String jpql = "SELECT u FROM User u WHERE u.tenNguoiDung = :username AND u.matKhau = :password";
        try {
            // Cố gắng tìm một user khớp với username và password
            User user = em.createQuery(jpql, User.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .getSingleResult();
            return user != null;
        } catch (NoResultException e) {
            // Không tìm thấy user nào khớp
            return false;
        }
    }

    public String getChucVuByUsername(String username) {
        // Dựa vào mapping @OneToOne giữa NhanVien và User để lấy chức vụ
        String jpql = "SELECT nv.chucVu FROM NhanVien nv WHERE nv.user.tenNguoiDung = :username";
        try {
            return em.createQuery(jpql, String.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public String getHoTenByUsername(String username) {
        String jpql = "SELECT nv.hoten FROM NhanVien nv WHERE nv.user.tenNguoiDung = :username";
        try {
            return em.createQuery(jpql, String.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public boolean timKiemTaiKhoan(String hoTen, String phone, String email) {
        String jpql = "SELECT nv FROM NhanVien nv WHERE nv.hoten = :hoten AND nv.sdt = :phone AND nv.email = :email";
        try {
            em.createQuery(jpql, NhanVien.class)
                    .setParameter("hoten", hoTen)
                    .setParameter("phone", phone)
                    .setParameter("email", email)
                    .getSingleResult();
            return true; // Nếu query thành công, tức là tồn tại nhân viên này
        } catch (NoResultException e) {
            return false;
        }
    }

    public boolean doiMatKhau(String hoTenNhanVien, String newPassword) {
        // Các thao tác làm thay đổi dữ liệu (Update/Insert/Delete) cần phải nằm trong Transaction
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            // 1. Tìm nhân viên theo họ tên
            String jpql = "SELECT nv FROM NhanVien nv WHERE nv.hoten = :hoten";
            NhanVien nv = em.createQuery(jpql, NhanVien.class)
                    .setParameter("hoten", hoTenNhanVien)
                    .getSingleResult();

            // 2. Lấy User của nhân viên đó và cập nhật mật khẩu
            if (nv.getUser() != null) {
                User user = nv.getUser();
                user.setMatKhau(newPassword);

                // 3. Cập nhật xuống DB
                em.merge(user);
                tx.commit();
                return true;
            }

            tx.rollback();
            return false;

        } catch (NoResultException e) {
            // Không tìm thấy nhân viên
            if (tx.isActive()) tx.rollback();
            return false;
        } catch (Exception e) {
            // Lỗi hệ thống khác
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }
}