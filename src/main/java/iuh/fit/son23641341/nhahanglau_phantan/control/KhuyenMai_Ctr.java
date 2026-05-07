package iuh.fit.son23641341.nhahanglau_phantan.control;

import iuh.fit.son23641341.nhahanglau_phantan.entity.KhuyenMai;
import iuh.fit.son23641341.nhahanglau_phantan.dao.KhuyenMai_DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Controller quản lý danh sách khuyến mãi (KhuyenMai).
 * Cung cấp các thao tác CRUD kết nối với cơ sở dữ liệu thông qua JPA.
 */
public class KhuyenMai_Ctr {
    private final KhuyenMai_DAO khuyenMaiDao;
    private ArrayList<KhuyenMai> danhSachKhuyenMai;

    /**
     * Khởi tạo controller và nạp dữ liệu từ DB.
     */
    public KhuyenMai_Ctr() {
        this.khuyenMaiDao = new KhuyenMai_DAO();
        this.danhSachKhuyenMai = new ArrayList<>(khuyenMaiDao.getAllKhuyenMai());
    }

    public ArrayList<KhuyenMai> getDanhSachKhuyenMai() {
        lamMoiDuLieu();
        return danhSachKhuyenMai;
    }

    /**
     * Làm mới danh sách từ cơ sở dữ liệu.
     */
    public void lamMoiDuLieu() {
        this.danhSachKhuyenMai = new ArrayList<>(khuyenMaiDao.getAllKhuyenMai());
    }

    /**
     * Tạo mã khuyến mãi tiếp theo theo định dạng KMxxx.
     */
    private String taoMaTiepTheo() {
        int max = 0;
        for (KhuyenMai k : danhSachKhuyenMai) {
            String ma = k.getMaKhuyenMai();
            if (ma != null && ma.toUpperCase(Locale.ROOT).startsWith("KM")) {
                try {
                    int so = Integer.parseInt(ma.substring(2));
                    if (so > max) max = so;
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return String.format("KM%03d", max + 1);
    }

    // CREATE
    public boolean themKhuyenMai(KhuyenMai km) {
        if (km == null) return false;

        String ma = km.getMaKhuyenMai();
        if (ma == null || ma.trim().isEmpty()) {
            try {
                km.setMaKhuyenMai(taoMaTiepTheo());
            } catch (Exception e) {
                return false;
            }
        } else {
            if (khuyenMaiDao.timTheoMa(ma) != null) return false;
        }

        if (khuyenMaiDao.themKhuyenMai(km)) {
            lamMoiDuLieu();
            return true;
        }
        return false;
    }

    // READ
    public KhuyenMai timTheoMa(String ma) {
        return khuyenMaiDao.timTheoMa(ma);
    }

    public List<KhuyenMai> timTheoTen(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>(danhSachKhuyenMai);
        }
        String q = query.trim().toLowerCase(Locale.ROOT);
        ArrayList<KhuyenMai> kq = new ArrayList<>();
        for (KhuyenMai k : danhSachKhuyenMai) {
            String ten = k.getTenKhuyenMai();
            if (ten != null && ten.toLowerCase(Locale.ROOT).contains(q)) {
                kq.add(k);
            }
        }
        return kq;
    }

    // UPDATE
    public boolean capNhatKhuyenMai(String ma, String ten, double phanTram, String ngayBatDau, String ngayKetThuc, String moTa) {
        KhuyenMai k = timTheoMa(ma);
        if (k == null) return false;
        try {
            k.setTenKhuyenMai(ten);
            k.setPhanTramGiam(phanTram);
            k.setNgayBatDau(ngayBatDau);
            k.setNgayKetThuc(ngayKetThuc);
            k.setMoTa(moTa);
            if (khuyenMaiDao.capNhatKhuyenMai(k)) {
                lamMoiDuLieu();
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    // DELETE
    public boolean xoaKhuyenMai(String ma) {
        if (khuyenMaiDao.xoaKhuyenMai(ma)) {
            lamMoiDuLieu();
            return true;
        }
        return false;
    }
}

