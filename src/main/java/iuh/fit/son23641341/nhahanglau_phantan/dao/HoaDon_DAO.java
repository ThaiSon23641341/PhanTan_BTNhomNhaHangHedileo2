package iuh.fit.son23641341.nhahanglau_phantan.dao;

import iuh.fit.son23641341.nhahanglau_phantan.entity.HoaDon;
import iuh.fit.son23641341.nhahanglau_phantan.entity.PhieuDatBan;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.ArrayList;
import java.util.List;

public class HoaDon_DAO {
    private EntityManager em;

    public HoaDon_DAO() {
        this.em = iuh.fit.son23641341.nhahanglau_phantan.util.EntityManagerFactoryUtil.getEntityManager();
    }

    public boolean isFunctional() {
        return em != null;
    }

    public HoaDon_DAO(EntityManager em) {
        this.em = em;
    }

    public List<HoaDon> timKiemHoaDon(String keyword) {
        String jpql = "SELECT DISTINCT h FROM HoaDon h " +
                      "LEFT JOIN h.danhSachPhieuDat p " +
                      "LEFT JOIN h.khachHangThanhVien k " +
                      "WHERE h.maHoaDon LIKE :kw " +
                      "OR h.trangThai LIKE :kw " +
                      "OR p.sdtDat LIKE :kw " +
                      "OR k.soDienThoai LIKE :kw";
        return em.createQuery(jpql, HoaDon.class)
                .setParameter("kw", "%" + keyword + "%")
                .getResultList();
    }

    public boolean addHoaDon(HoaDon hd) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(hd);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            return false;
        }
    }

    public boolean taoHoaDonMoi(PhieuDatBan phieuDat) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            HoaDon hd = new HoaDon(phieuDat);
            em.persist(hd);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            return false;
        }
    }
}

