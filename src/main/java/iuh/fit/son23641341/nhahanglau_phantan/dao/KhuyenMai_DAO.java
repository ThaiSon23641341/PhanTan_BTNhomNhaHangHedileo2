package iuh.fit.son23641341.nhahanglau_phantan.dao;

import iuh.fit.son23641341.nhahanglau_phantan.entity.KhuyenMai;
import iuh.fit.son23641341.nhahanglau_phantan.util.EntityManagerFactoryUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class KhuyenMai_DAO {
    private EntityManager em;

    public KhuyenMai_DAO() {
        this.em = iuh.fit.son23641341.nhahanglau_phantan.util.EntityManagerFactoryUtil.getEntityManager();
    }

    public boolean isFunctional() {
        return em != null;
    }

    public KhuyenMai_DAO(EntityManager em) {
        this.em = em;
    }

    public List<KhuyenMai> getAllKhuyenMai() {
        return em.createQuery("SELECT k FROM KhuyenMai k", KhuyenMai.class).getResultList();
    }

    public boolean themKhuyenMai(KhuyenMai km) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(km);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatKhuyenMai(KhuyenMai km) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(km);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaKhuyenMai(String maKhuyenMai) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            KhuyenMai km = em.find(KhuyenMai.class, maKhuyenMai);
            if (km != null) {
                em.remove(km);
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

    public KhuyenMai timTheoMa(String maKhuyenMai) {
        return em.find(KhuyenMai.class, maKhuyenMai);
    }
}

