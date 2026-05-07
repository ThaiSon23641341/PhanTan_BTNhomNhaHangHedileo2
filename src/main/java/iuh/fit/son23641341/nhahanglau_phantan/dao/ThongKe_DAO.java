package iuh.fit.son23641341.nhahanglau_phantan.dao;

import jakarta.persistence.EntityManager;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ThongKe_DAO {
    private EntityManager em;

    public ThongKe_DAO(EntityManager em) {
        this.em = em;
    }

    public ThongKeNgay getThongKeTheoNgay(LocalDate ngay) {
        String jpql = "SELECT COUNT(h), SUM(h.tongTien) FROM HoaDon h WHERE CAST(h.ngayLap AS date) = :ngay";
        Object[] result = (Object[]) em.createQuery(jpql)
                .setParameter("ngay", java.sql.Date.valueOf(ngay))
                .getSingleResult();

        long count = (result[0] != null) ? (long) result[0] : 0;
        double sum = (result[1] != null) ? (double) result[1] : 0.0;
        return new ThongKeNgay(ngay, sum, (int) count);
    }

    public ArrayList<ThongKeThang> getDoanhThuTheoThangTrongNam(int nam) {
        ArrayList<ThongKeThang> dsThang = new ArrayList<>();
        for (int i = 1; i <= 12; i++) dsThang.add(new ThongKeThang(i, 0, 0));

        String jpql = "SELECT FUNCTION('MONTH', h.ngayLap), SUM(h.tongTien), COUNT(h) " +
                "FROM HoaDon h WHERE FUNCTION('YEAR', h.ngayLap) = :nam " +
                "GROUP BY FUNCTION('MONTH', h.ngayLap)";

        @SuppressWarnings("unchecked")
        List<Object[]> results = em.createQuery(jpql).setParameter("nam", nam).getResultList();
        for (Object[] row : results) {
            int thang = (int) row[0];
            dsThang.set(thang - 1, new ThongKeThang(thang, (double) row[1], ((Long) row[2]).intValue()));
        }
        return dsThang;
    }

    public ArrayList<TopMonAn> getTopMonAnTheoThang(int thang, int nam, int limit) {
        // Lấy top các món ăn bán chạy nhất theo tháng từ hóa đơn
        String jpql = "SELECT m.tenMon, SUM(ct.soLuong) " +
                "FROM HoaDon h " +
                "JOIN h.danhSachPhieuDat p " +
                "JOIN p.danhSachMonAn ct " +
                "JOIN ct.monAn m " +
                "WHERE FUNCTION('MONTH', h.ngayLap) = :thang " +
                "AND FUNCTION('YEAR', h.ngayLap) = :nam " +
                "GROUP BY m.tenMon " +
                "ORDER BY SUM(ct.soLuong) DESC";
        @SuppressWarnings("unchecked")
        List<Object[]> results = em.createQuery(jpql)
                .setParameter("thang", thang).setParameter("nam", nam)
                .setMaxResults(limit).getResultList();

        ArrayList<TopMonAn> topList = new ArrayList<>();
        for (Object[] row : results) {
            topList.add(new TopMonAn((String) row[0], ((Long) row[1]).intValue()));
        }
        return topList;
    }
    // --- 1. CÁC PHƯƠNG THỨC THỐNG KÊ THEO NGÀY ---

    public double getTongTienHomNay() {
        String jpql = "SELECT SUM(h.tongTien) FROM HoaDon h WHERE CAST(h.ngayLap AS date) = CURRENT_DATE";
        Double res = em.createQuery(jpql, Double.class).getSingleResult();
        return res != null ? res : 0.0;
    }

    // Lấy số lượng phiếu đặt bàn đang ở trạng thái chờ/đang dùng (chưa thanh toán)
    public long getSoPhieuDangDat() {
        // Lấy phiếu đặt bàn không có hóa đơn (chưa thanh toán) - qua mối quan hệ ManyToMany
        String jpql = "SELECT COUNT(p) FROM PhieuDatBan p WHERE p NOT IN " +
                "(SELECT p2 FROM HoaDon h JOIN h.danhSachPhieuDat p2)";
        return em.createQuery(jpql, Long.class).getSingleResult();
    }
    public double getDoanhThuNgay(LocalDate ngay) {
        String jpql = "SELECT SUM(h.tongTien) FROM HoaDon h WHERE CAST(h.ngayLap AS date) = :ngay";
        Double result = em.createQuery(jpql, Double.class)
                .setParameter("ngay", java.sql.Date.valueOf(ngay))
                .getSingleResult();
        return (result != null) ? result : 0.0;
    }

    public long getSoHoaDonNgay(LocalDate ngay) {
        String jpql = "SELECT COUNT(h) FROM HoaDon h WHERE CAST(h.ngayLap AS date) = :ngay";
        Long result = em.createQuery(jpql, Long.class)
                .setParameter("ngay", java.sql.Date.valueOf(ngay))
                .getSingleResult();
        return (result != null) ? result : 0L;
    }

    public ArrayList<TopMonAn> getTopMonAnTheoNgay(LocalDate ngay, int limit) {
        // Sử dụng bảng HoaDon để lọc ngày chính xác hơn cho doanh thu thực tế
        String jpql = "SELECT m.tenMon, SUM(ct.soLuong) " +
                "FROM HoaDon h " +
                "JOIN h.danhSachPhieuDat p " +
                "JOIN p.danhSachMonAn ct " +
                "JOIN ct.monAn m " +
                "WHERE CAST(h.ngayLap AS date) = :ngay " +
                "GROUP BY m.tenMon " +
                "ORDER BY SUM(ct.soLuong) DESC";

        @SuppressWarnings("unchecked")
        List<Object[]> results = em.createQuery(jpql)
                .setParameter("ngay", java.sql.Date.valueOf(ngay))
                .setMaxResults(limit)
                .getResultList();

        ArrayList<TopMonAn> topList = new ArrayList<>();
        for (Object[] row : results) {
            topList.add(new TopMonAn((String) row[0], ((Long) row[1]).intValue()));
        }
        return topList;
    }

    // --- 2. CÁC PHƯƠNG THỨC THỐNG KÊ THEO NĂM ---

    public double getDoanhThuNam(int nam) {
        String jpql = "SELECT SUM(h.tongTien) FROM HoaDon h WHERE FUNCTION('YEAR', h.ngayLap) = :nam";
        Double result = em.createQuery(jpql, Double.class)
                .setParameter("nam", nam)
                .getSingleResult();
        return (result != null) ? result : 0.0;
    }

    public long getSoHoaDonNam(int nam) {
        String jpql = "SELECT COUNT(h) FROM HoaDon h WHERE FUNCTION('YEAR', h.ngayLap) = :nam";
        Long result = em.createQuery(jpql, Long.class)
                .setParameter("nam", nam)
                .getSingleResult();
        return (result != null) ? result : 0L;
    }

    // Các class DTO giữ nguyên
    public static class ThongKeNgay {
        public LocalDate ngay; public double tongDoanhThu; public int tongHoaDon;
        public ThongKeNgay(LocalDate n, double dt, int hd) { this.ngay = n; this.tongDoanhThu = dt; this.tongHoaDon = hd; }
    }
    public static class ThongKeThang {
        public int thang; public double tongDoanhThu; public int tonghoaDon;
        public ThongKeThang(int t, double dt, int hd) { this.thang = t; this.tongDoanhThu = dt; this.tonghoaDon = hd; }
    }
    public static class TopMonAn {
        public String tenMonAn; public int soLuongDat;
        public TopMonAn(String t, int s) { this.tenMonAn = t; this.soLuongDat = s; }
    }
}