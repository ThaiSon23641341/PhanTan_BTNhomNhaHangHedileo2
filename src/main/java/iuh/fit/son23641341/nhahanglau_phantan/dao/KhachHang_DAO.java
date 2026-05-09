package iuh.fit.son23641341.nhahanglau_phantan.dao;

import iuh.fit.son23641341.nhahanglau_phantan.entity.KhachHangThanhVien;
import iuh.fit.son23641341.nhahanglau_phantan.entity.PhieuDatBan;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.ArrayList;
import java.util.List;

public class KhachHang_DAO {
    private EntityManager em;

    public KhachHang_DAO() {
        this.em = iuh.fit.son23641341.nhahanglau_phantan.util.EntityManagerFactoryUtil.getEntityManager();
    }

    public KhachHang_DAO(EntityManager em) {
        this.em = em;
    }

    public String getMaKhachHangBySDT(String sdt) {
        try {
            return em.createQuery("SELECT kh.maKhachHang FROM KhachHangThanhVien kh WHERE kh.soDienThoai = :sdt", String.class)
                    .setParameter("sdt", sdt)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<KhachHangThanhVien> getAllKhachHang() {
        em.clear(); // Xóa cache để đảm bảo lấy dữ liệu mới nhất từ DB
        return em.createQuery("SELECT kh FROM KhachHangThanhVien kh", KhachHangThanhVien.class).getResultList();
    }

    public String taoMaKhachHangMoi() {
        try {
            String maxMa = em.createQuery("SELECT MAX(kh.maKhachHang) FROM KhachHangThanhVien kh WHERE kh.maKhachHang LIKE 'KH%'", String.class)
                    .getSingleResult();
            if (maxMa == null) return "KH001";
            int max = Integer.parseInt(maxMa.substring(2));
            return String.format("KH%03d", max + 1);
        } catch (Exception e) {
            return "KH001";
        }
    }

    public List<KhachHangThanhVien> timKhachHangTheoSDT(String sdt) {
        return em.createQuery("SELECT kh FROM KhachHangThanhVien kh WHERE kh.soDienThoai LIKE :sdt", KhachHangThanhVien.class)
                .setParameter("sdt", "%" + sdt + "%")
                .getResultList();
    }

    public boolean existsBySoDienThoai(String sdt) {
        try {
            Long count = em.createQuery("SELECT COUNT(kh.maKhachHang) FROM KhachHangThanhVien kh WHERE kh.soDienThoai = :sdt", Long.class)
                    .setParameter("sdt", sdt)
                    .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean existsBySoDienThoaiExcept(String sdt, String maKhachHang) {
        try {
            Long count = em.createQuery("SELECT COUNT(kh.maKhachHang) FROM KhachHangThanhVien kh WHERE kh.soDienThoai = :sdt AND kh.maKhachHang <> :ma", Long.class)
                    .setParameter("sdt", sdt)
                    .setParameter("ma", maKhachHang)
                    .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean themKhachHang(KhachHangThanhVien kh) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (kh.getMaKhachHang() == null || kh.getMaKhachHang().isEmpty()) {
                kh.setMaKhachHang(taoMaKhachHangMoi());
            }
            em.persist(kh);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            return false;
        }
    }

    public boolean capNhatKhachHang(KhachHangThanhVien kh) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(kh);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            return false;
        }
    }

    public boolean xoaKhachHang(String maKhachHang) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            KhachHangThanhVien kh = em.find(KhachHangThanhVien.class, maKhachHang);
            if (kh != null) {
                em.remove(kh);
            }
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            return false;
        }
    }

    public boolean congDiemTichLuy(String maKhachHang, int diemCong) {
        EntityTransaction tx = em.getTransaction();
        try {
            System.out.println("DEBUG: Dang cong " + diemCong + " diem cho KH: " + maKhachHang);
            tx.begin();
            // Sử dụng UPDATE trực tiếp để đảm bảo tính nguyên tử và tránh vấn đề cache/detached entity
            int updatedCount = em.createQuery("UPDATE KhachHangThanhVien kh SET kh.diemTichLuy = kh.diemTichLuy + :diem WHERE kh.maKhachHang = :ma")
                    .setParameter("diem", diemCong)
                    .setParameter("ma", maKhachHang)
                    .executeUpdate();
            tx.commit();
            
            if (updatedCount > 0) {
                System.out.println("DEBUG: Cap nhat diem thanh cong cho " + maKhachHang);
                return true;
            } else {
                System.err.println("DEBUG: Khong tim thay khach hang voi ma: " + maKhachHang);
                return false;
            }
        } catch (Exception e) {
            System.err.println("DEBUG: Loi khi cong diem: " + e.getMessage());
            e.printStackTrace();
            if (tx.isActive()) tx.rollback();
            return false;
        }
    }

    public String getHang(PhieuDatBan pdb) {
        if (pdb == null || pdb.getMaKhachHang() == null) {
            return null;
        }
        try {
            return em.createQuery("SELECT kh.thanhVien FROM KhachHangThanhVien kh WHERE kh.maKhachHang = :ma", String.class)
                    .setParameter("ma", pdb.getMaKhachHang())
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public String getmaKhachHangbySDT(String sdt) {
        return getMaKhachHangBySDT(sdt);
    }
}

