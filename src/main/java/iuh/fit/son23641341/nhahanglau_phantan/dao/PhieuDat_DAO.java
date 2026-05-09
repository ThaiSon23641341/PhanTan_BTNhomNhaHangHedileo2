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
            // Đảm bảo danh sách bàn được nạp đầy đủ trước khi persist (nếu chưa có)
            if ((p.getDanhSachBanPersist() == null || p.getDanhSachBanPersist().isEmpty()) &&
                    p.getDanhSachBan() != null && !p.getDanhSachBan().isEmpty()) {
                List<iuh.fit.son23641341.nhahanglau_phantan.entity.BanAn> dsBanEntities = new ArrayList<>();
                for (Integer maBan : p.getDanhSachBan()) {
                    iuh.fit.son23641341.nhahanglau_phantan.entity.BanAn b = em
                            .find(iuh.fit.son23641341.nhahanglau_phantan.entity.BanAn.class, maBan);
                    if (b != null)
                        dsBanEntities.add(b);
                }
                p.setDanhSachBanPersist(dsBanEntities);
            }
            em.persist(p);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
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

    public ArrayList<PhieuDatBan> getPhieuDatByNgay(String ngayDatStr) {
        // Xóa cache để đảm bảo lấy dữ liệu mới nhất từ DB (Real-time)
        em.clear();
        try {
            java.util.Date d = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(ngayDatStr);
            java.sql.Date sqlDate = new java.sql.Date(d.getTime());

            String jpql = "SELECT DISTINCT p FROM PhieuDatBan p LEFT JOIN FETCH p.danhSachBan WHERE p.ngayDat = :ngay";
            List<PhieuDatBan> list = em.createQuery(jpql, PhieuDatBan.class)
                    .setParameter("ngay", sqlDate)
                    .getResultList();
            return new ArrayList<>(list);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public ArrayList<PhieuDatBan> getPhieuDatByBanVaNgay(int maBan, String ngayDatStr) {
        try {
            java.util.Date d = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(ngayDatStr);
            java.sql.Date sqlDate = new java.sql.Date(d.getTime());

            String jpql = "SELECT DISTINCT p FROM PhieuDatBan p JOIN p.danhSachBan b WHERE b.maBan = :ma AND p.ngayDat = :ngay";
            List<PhieuDatBan> list = em.createQuery(jpql, PhieuDatBan.class)
                    .setParameter("ma", maBan)
                    .setParameter("ngay", sqlDate)
                    .getResultList();
            return new ArrayList<>(list);
        } catch (Exception e) {
            return new ArrayList<>();
        }
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
            if (tx.isActive())
                tx.rollback();
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
            if (tx.isActive())
                tx.rollback();
            return false;
        }
    }

    public boolean capNhatMonAnCuaPhieu(String maPhieu, List<ChiTietDatMon> danhSachMon) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            PhieuDatBan p = em.find(PhieuDatBan.class, maPhieu);
            if (p != null) {
                List<ChiTietDatMon> newDishes = new ArrayList<>(danhSachMon);
                for (ChiTietDatMon ct : newDishes) {
                    ct.setPhieuDatBan(p);
                }
                p.setDanhSachMonAn(newDishes);
                em.merge(p);
            }
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            return false;
        }
    }

    public PhieuDatBan getPhieuDangSuDungTheoMaBan(int maBan) {
        try {
            String jpql = "SELECT DISTINCT p FROM PhieuDatBan p JOIN p.danhSachBan b WHERE b.maBan = :ma AND p.trangThai = 'Đang sử dụng'";
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
