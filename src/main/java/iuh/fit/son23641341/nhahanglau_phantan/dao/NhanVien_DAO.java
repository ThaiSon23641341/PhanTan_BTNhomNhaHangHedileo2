package iuh.fit.son23641341.nhahanglau_phantan.dao;

import iuh.fit.son23641341.nhahanglau_phantan.entity.NhanVien;
import iuh.fit.son23641341.nhahanglau_phantan.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.ArrayList;
import java.util.List;

public class NhanVien_DAO {
    private EntityManager em;

    public NhanVien_DAO() {
        this.em = iuh.fit.son23641341.nhahanglau_phantan.util.EntityManagerFactoryUtil.getEntityManager();
    }

    public NhanVien_DAO(EntityManager em) {
        this.em = em;
    }

    public NhanVien timNhanVienTheoDangNhap(String username, String password) {
        String jpql = "SELECT nv FROM NhanVien nv WHERE nv.user.tenNguoiDung = :username AND nv.user.matKhau = :password";
        try {
            return em.createQuery(jpql, NhanVien.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public String getTenNhanVienByMa(String maNhanVien) {
        try {
            return em.createQuery("SELECT nv.hoten FROM NhanVien nv WHERE nv.manv = :ma", String.class)
                    .setParameter("ma", maNhanVien)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<NhanVien> getAllNhanVien() {
        return em.createQuery("SELECT nv FROM NhanVien nv", NhanVien.class).getResultList();
    }

    public boolean themNhanVien(NhanVien nv) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(nv);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            return false;
        }
    }

    public boolean capNhatNhanVien(NhanVien nv) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(nv);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            return false;
        }
    }

    public boolean xoaNhanVien(String maNV, String idUser) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            NhanVien nv = em.find(NhanVien.class, maNV);
            if (nv != null) {
                em.remove(nv);
            }
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            return false;
        }
    }

    public boolean themNhanVienVaTaiKhoan(NhanVien nv, String tenNguoiDung, String matKhau) {
        // 1. Kiểm tra xem tên người dùng đã tồn tại chưa
        Long count = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.tenNguoiDung = :user", Long.class)
                .setParameter("user", tenNguoiDung)
                .getSingleResult();
        if (count > 0) {
            System.err.println("Lỗi: Tên đăng nhập " + tenNguoiDung + " đã tồn tại.");
            return false; 
        }

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            
            // 2. Tạo ID cho User mới
            String maxIdStr = em.createQuery("SELECT MAX(u.iD) FROM User u", String.class).getSingleResult();
            int nextIdInt = 1;
            if (maxIdStr != null) {
                try {
                    String numericPart = maxIdStr.replaceAll("\\D", ""); 
                    if (!numericPart.isEmpty()) nextIdInt = Integer.parseInt(numericPart) + 1;
                } catch (NumberFormatException e) {
                    nextIdInt = (int) (System.currentTimeMillis() % 1000000);
                }
            }
            String newId = String.valueOf(nextIdInt);
            
            // 3. Tạo User và gán cho Nhân viên
            User newUser = new User(newId, tenNguoiDung, matKhau);
            em.persist(newUser);
            
            nv.setUser(newUser);
            em.persist(nv);
            
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatNhanVienVaTaiKhoan(NhanVien nv, String idUser, String user, String pass) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            User existingUser = em.find(User.class, idUser);
            if (existingUser != null) {
                existingUser.setTenNguoiDung(user);
                existingUser.setMatKhau(pass);
                em.merge(existingUser);
            }
            em.merge(nv);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            return false;
        }
    }

    public Object[] getThongTinSua(String maNV) {
        try {
            NhanVien nv = em.find(NhanVien.class, maNV);
            if (nv != null && nv.getUser() != null) {
                return new Object[] { nv, nv.getUser().getTenNguoiDung(), nv.getUser().getMatKhau(), nv.getUser().getiD() };
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
