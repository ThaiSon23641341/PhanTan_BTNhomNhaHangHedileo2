package iuh.fit.son23641341.nhahanglau_phantan.dao;

import iuh.fit.son23641341.nhahanglau_phantan.entity.ChiTietDatMon;
import iuh.fit.son23641341.nhahanglau_phantan.util.EntityManagerFactoryUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class    ChiTietDatMon_Dao {
    private EntityManager em;

    public ChiTietDatMon_Dao() {
        this.em = EntityManagerFactoryUtil.getEntityManager();
    }

    public ChiTietDatMon_Dao(EntityManager em) {
        this.em = em;
    }

    public void insertChiTietDatMon(ChiTietDatMon ct) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(ct);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        }
    }
}
