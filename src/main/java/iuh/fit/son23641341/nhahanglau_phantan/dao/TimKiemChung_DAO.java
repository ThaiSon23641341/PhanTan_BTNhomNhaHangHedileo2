package iuh.fit.son23641341.nhahanglau_phantan.dao;

import iuh.fit.son23641341.nhahanglau_phantan.entity.MonAn;
import jakarta.persistence.EntityManager;

import java.util.List;

public class TimKiemChung_DAO {
    private EntityManager em;

    public TimKiemChung_DAO() {
        this.em = iuh.fit.son23641341.nhahanglau_phantan.util.EntityManagerFactoryUtil.getEntityManager();
    }

    public TimKiemChung_DAO(EntityManager em) {
        this.em = em;
    }

    public List<MonAn> getAllMonAn() {
        return em.createQuery("SELECT m FROM MonAn m", MonAn.class).getResultList();
    }

    public List<MonAn> timKiemMonAn(String keyword, boolean searchByCode, boolean searchByOrder, boolean searchByBill,
                                    boolean searchByCustomer, boolean searchByTable) {
        String jpql = "SELECT m FROM MonAn m WHERE ";
        if (searchByCode) {
            jpql += "m.maMon LIKE :keyword";
        } else {
            jpql += "LOWER(m.tenMon) LIKE LOWER(:keyword)";
        }
        
        return em.createQuery(jpql, MonAn.class)
                .setParameter("keyword", "%" + keyword + "%")
                .getResultList();
    }
}

