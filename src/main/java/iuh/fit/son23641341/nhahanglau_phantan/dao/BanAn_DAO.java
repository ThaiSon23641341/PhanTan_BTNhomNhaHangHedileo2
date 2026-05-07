package iuh.fit.son23641341.nhahanglau_phantan.dao;

import iuh.fit.son23641341.nhahanglau_phantan.entity.BanAn;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class BanAn_DAO {
    private EntityManager em;

    public BanAn_DAO() {
        this.em = iuh.fit.son23641341.nhahanglau_phantan.util.EntityManagerFactoryUtil.getEntityManager();
    }

    public BanAn_DAO(EntityManager em) {
        this.em = em;
    }

    public List<BanAn> getAllBanAn() {
        return em.createQuery("SELECT b FROM BanAn b", BanAn.class).getResultList();
    }

    public BanAn findById(int maBan) {
        return em.find(BanAn.class, maBan);
    }

    public boolean mergeBanAn(BanAn banAn) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(banAn);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBanAn(int maBan) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            BanAn ban = em.find(BanAn.class, maBan);
            if (ban != null) {
                em.remove(ban);
                tx.commit();
                return true;
            }
            tx.rollback();
            return false;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            return false;
        }
    }
}