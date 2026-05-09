package iuh.fit.son23641341.nhahanglau_phantan.control;

import iuh.fit.son23641341.nhahanglau_phantan.entity.MonAn;
import iuh.fit.son23641341.nhahanglau_phantan.dao.MonAn_DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Lớp điều khiển (Controller) quản lý danh sách các đối tượng {@link MonAn}.
 * Cung cấp các thao tác CRUD (Create, Read, Update, Delete) cho món ăn.
 */
public class MonAn_Ctr {
    private final MonAn_DAO monAnDao;
    private ArrayList<MonAn> danhSachMonAn;

    /**
     * Khởi tạo một đối tượng MonAn_Ctr mới.
     * Nạp dữ liệu từ cơ sở dữ liệu thông qua MonAn_DAO.
     */
    public MonAn_Ctr() {
        this.monAnDao = new MonAn_DAO();
        this.danhSachMonAn = new ArrayList<>(monAnDao.getAllMonAn());
    }

    /**
     * Trả về danh sách tất cả các món ăn hiện có.
     *
     * @return {@code ArrayList<MonAn>} chứa tất cả món ăn.
     */
    public ArrayList<MonAn> getDanhSachMonAn() {
        lamMoiDuLieu();
        return danhSachMonAn;
    }

    /**
     * Làm mới danh sách món ăn từ cơ sở dữ liệu.
     */
    public void lamMoiDuLieu() {
        if (monAnDao != null && monAnDao.isFunctional()) {
            this.danhSachMonAn = new ArrayList<>(monAnDao.getAllMonAn());
        } else {
            // Client side
            iuh.fit.son23641341.nhahanglau_phantan.network.Request req = 
                new iuh.fit.son23641341.nhahanglau_phantan.network.Request("GET_ALL_MONAN", null);
            iuh.fit.son23641341.nhahanglau_phantan.network.Response res = 
                iuh.fit.son23641341.nhahanglau_phantan.network.ClientControl.getInstance().sendRequest(req);
            if (res.getStatus().equals("SUCCESS")) {
                this.danhSachMonAn = new ArrayList<>((List<MonAn>) res.getData());
            }
        }
    }

    /**
     * Tạo mã món tiếp theo theo định dạng MAxxx.
     */
    private String taoMaTiepTheo() {
        if (danhSachMonAn == null || danhSachMonAn.isEmpty()) {
            return String.format("MA%03d", 1);
        }

        int max = danhSachMonAn.stream()
                .map(mon -> mon.getMaMon().substring(2))
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0);

        return String.format("MA%03d", max + 1);
    }

    // CREATE
    /**
     * Thêm một món ăn mới vào database và danh sách.
     */
    public boolean themMonAn(MonAn mon) {
        if (mon == null) return false;

        String ma = mon.getMaMon();
        if (ma == null || ma.trim().isEmpty() || ma.equals("MA00")) {
            try {
                mon.setMaMon(taoMaTiepTheo());
            } catch (Exception e) {
                return false;
            }
        } else {
            if (monAnDao.existsByMaMon(ma)) return false;
        }

        if (monAnDao.themMonAn(mon)) {
            lamMoiDuLieu();
            return true;
        }
        return false;
    }

    // READ
    /**
     * Tìm một món ăn theo mã món.
     */
    public MonAn timTheoMa(String maMon) {
        return monAnDao.timTheoMa(maMon);
    }

    /**
     * Tìm kiếm các món ăn có tên chứa một chuỗi truy vấn.
     */
    public List<MonAn> timTheoTen(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>(danhSachMonAn);
        }
        String q = query.trim().toLowerCase(Locale.ROOT);
        ArrayList<MonAn> kq = new ArrayList<>();
        for (MonAn m : danhSachMonAn) {
            String ten = m.getTenMon();
            if (ten != null && ten.toLowerCase(Locale.ROOT).contains(q)) {
                kq.add(m);
            }
        }
        return kq;
    }

    // UPDATE
    /**
     * Cập nhật thông tin cho món ăn.
     */
    public boolean capNhatMonAn(String maMon, String tenMon, String loaiMon, double gia, String moTa) {
        MonAn m = timTheoMa(maMon);
        if (m == null) return false;
        try {
            m.setTenMon(tenMon);
            m.setLoaiMon(loaiMon);
            m.setGia(gia);
            m.setMoTa(moTa);
            if (monAnDao.capNhatMonAn(m)) {
                lamMoiDuLieu();
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean capNhatMonAn(MonAn mon) {
        if (monAnDao.capNhatMonAn(mon)) {
            lamMoiDuLieu();
            return true;
        }
        return false;
    }

    // DELETE
    /**
     * Xóa một món ăn khỏi database và danh sách.
     */
    public boolean xoaMonAn(String maMon) {
        if (monAnDao.xoaMonAn(maMon)) {
            lamMoiDuLieu();
            return true;
        }
        return false;
    }
}
