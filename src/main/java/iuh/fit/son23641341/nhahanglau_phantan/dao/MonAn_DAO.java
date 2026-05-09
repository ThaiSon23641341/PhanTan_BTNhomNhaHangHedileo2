
package iuh.fit.son23641341.nhahanglau_phantan.dao;

import iuh.fit.son23641341.nhahanglau_phantan.entity.MonAn;
import iuh.fit.son23641341.nhahanglau_phantan.util.EntityManagerFactoryUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class MonAn_DAO {
    private EntityManager em;

    public MonAn_DAO() {
        this.em = iuh.fit.son23641341.nhahanglau_phantan.util.EntityManagerFactoryUtil.getEntityManager();
    }

    public boolean isFunctional() {
        return em != null;
    }

    public MonAn_DAO(EntityManager em) {
        this.em = em;
    }

    public List<MonAn> getAllMonAn() {
        return em.createQuery("SELECT m FROM MonAn m", MonAn.class).getResultList();
    }

    public boolean existsByMaMon(String maMon) {
        return em.find(MonAn.class, maMon) != null;
    }

    public boolean themMonAn(MonAn mon) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(mon);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatMonAn(MonAn mon) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(mon);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaMonAn(String maMon) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            MonAn mon = em.find(MonAn.class, maMon);
            if (mon != null) {
                em.remove(mon);
                tx.commit();
                return true;
            }
            tx.rollback();
            return false;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public MonAn timTheoMa(String maMon) {
        return em.find(MonAn.class, maMon);
    }
}

