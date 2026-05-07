package iuh.fit.son23641341.nhahanglau_phantan.dao;

import iuh.fit.son23641341.nhahanglau_phantan.entity.PhieuDatBan;
import iuh.fit.son23641341.nhahanglau_phantan.entity.ChiTietDatMon;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.ArrayList;
import java.util.List;

public class PhieuDat_DAO {
    private EntityManager em;

    public PhieuDat_DAO() {
        this.em = iuh.fit.son23641341.nhahanglau_phantan.util.EntityManagerFactoryUtil.getEntityManager();
    }

    public PhieuDat_DAO(EntityManager em) {
        this.em = em;
    }

    public boolean insertPhieuDat(PhieuDatBan p) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(p);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public List<PhieuDatBan> getAllPhieuDat() {
        return em.createQuery("SELECT p FROM PhieuDatBan p", PhieuDatBan.class).getResultList();
    }

    public ArrayList<PhieuDatBan> timKiemPhieuDat(String keyword) {
        String jpql = "SELECT p FROM PhieuDatBan p WHERE " +
                      "p.maPhieu LIKE :kw OR " +
                      "p.tenKhachDat LIKE :kw OR " +
                      "p.sdtDat LIKE :kw";
        List<PhieuDatBan> list = em.createQuery(jpql, PhieuDatBan.class)
                .setParameter("kw", "%" + keyword + "%")
                .getResultList();
        return new ArrayList<>(list);
    }

    public ArrayList<PhieuDatBan> getPhieuDatByNgay(String ngayDat) {
        List<PhieuDatBan> list = em.createQuery("SELECT p FROM PhieuDatBan p WHERE p.ngayDat = :ngay", PhieuDatBan.class)
                .setParameter("ngay", ngayDat)
                .getResultList();
        return new ArrayList<>(list);
    }

    public ArrayList<PhieuDatBan> getPhieuDatByBanVaNgay(int maBan, String ngayDat) {
        // Lưu ý: maBan có thể là maBan chính hoặc nằm trong danh sách bàn đã chọn
        String jpql = "SELECT p FROM PhieuDatBan p WHERE p.ngayDat = :ngay AND (p.maBan = :ma OR :ma MEMBER OF p.danhSachBanDaChon)";
        List<PhieuDatBan> list = em.createQuery(jpql, PhieuDatBan.class)
                .setParameter("ngay", ngayDat)
                .setParameter("ma", maBan)
                .getResultList();
        return new ArrayList<>(list);
    }

    public boolean capNhatTrangThai(String maPhieu, String trangThai) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            PhieuDatBan p = em.find(PhieuDatBan.class, maPhieu);
            if (p != null) {
                p.setTrangThai(trangThai);
                em.merge(p);
            }
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            return false;
        }
    }

    public boolean capNhatThongTinKhachHang(String maPhieu, String tenKhach, String sdt, String email) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            PhieuDatBan p = em.find(PhieuDatBan.class, maPhieu);
            if (p != null) {
                p.setTenKhachDat(tenKhach);
                p.setSdtDat(sdt);
                p.setEmailDat(email);
                em.merge(p);
            }
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            return false;
        }
    }

    public boolean capNhatMonAnCuaPhieu(String maPhieu, List<ChiTietDatMon> danhSachMon) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            PhieuDatBan p = em.find(PhieuDatBan.class, maPhieu);
            if (p != null) {
                p.setDanhSachMonAn(new ArrayList<>(danhSachMon));
                em.merge(p);
            }
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            return false;
        }
    }

    public PhieuDatBan getPhieuDangSuDungTheoMaBan(int maBan) {
        try {
            String jpql = "SELECT p FROM PhieuDatBan p WHERE (p.maBan = :ma OR :ma MEMBER OF p.danhSachBanDaChon) AND p.trangThai = 'Đang sử dụng'";
            return em.createQuery(jpql, PhieuDatBan.class)
                    .setParameter("ma", maBan)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public PhieuDatBan timPhieuDatBangMa(String maPhieu) {
        return em.find(PhieuDatBan.class, maPhieu);
    }
}

