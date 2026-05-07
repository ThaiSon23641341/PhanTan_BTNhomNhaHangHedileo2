package iuh.fit.son23641341.nhahanglau_phantan.dao;

import jakarta.persistence.EntityManager;

public class TongQuan_DAO {
    private EntityManager em;

    public TongQuan_DAO() {
        this.em = iuh.fit.son23641341.nhahanglau_phantan.util.EntityManagerFactoryUtil.getEntityManager();
    }

    public TongQuan_DAO(EntityManager em) {
        this.em = em;
    }

    public int getSoKhuyenMai() {
        try {
            return ((Number) em.createQuery("SELECT COUNT(k.maKhuyenMai) FROM KhuyenMai k").getSingleResult()).intValue();
        } catch (Exception e) {
            return 0;
        }
    }

    public int getSoNhanVien() {
        try {
            return ((Number) em.createQuery("SELECT COUNT(n.manv) FROM NhanVien n").getSingleResult()).intValue();
        } catch (Exception e) {
            return 0;
        }
    }
}

