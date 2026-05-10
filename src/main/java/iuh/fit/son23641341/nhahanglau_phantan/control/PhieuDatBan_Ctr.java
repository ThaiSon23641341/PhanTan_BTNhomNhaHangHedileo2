package iuh.fit.son23641341.nhahanglau_phantan.control;

import iuh.fit.son23641341.nhahanglau_phantan.entity.PhieuDatBan;
import iuh.fit.son23641341.nhahanglau_phantan.dao.PhieuDat_DAO;
import iuh.fit.son23641341.nhahanglau_phantan.dao.KhachHang_DAO;
import iuh.fit.son23641341.nhahanglau_phantan.entity.ChiTietDatMon;
import iuh.fit.son23641341.nhahanglau_phantan.entity.NhanVien;
import iuh.fit.son23641341.nhahanglau_phantan.entity.KhuyenMai;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PhieuDatBan_Ctr {
    private static PhieuDatBan_Ctr instance;
    private final PhieuDat_DAO phieuDatDao;
    private ArrayList<PhieuDatBan> danhSachPhieu;
    private Map<Integer, ArrayList<ChiTietDatMon>> gioHangTamThoi;
    private Map<Integer, KhuyenMai> khuyenMaiTamThoi;

    private PhieuDatBan_Ctr() {
        this.phieuDatDao = new PhieuDat_DAO();
        this.gioHangTamThoi = new HashMap<>();
        this.khuyenMaiTamThoi = new HashMap<>();
        // Chỉ tải dữ liệu nếu đang ở Server side (có kết nối CSDL)
        if (phieuDatDao.isFunctional()) {
            this.danhSachPhieu = new ArrayList<>(phieuDatDao.getAllPhieuDat());
        } else {
            this.danhSachPhieu = new ArrayList<>();
            // Client side: tải dữ liệu qua mạng
            lamMoiDuLieu();
        }
    }

    public static PhieuDatBan_Ctr getInstance() {
        if (instance == null) {
            instance = new PhieuDatBan_Ctr();
        }
        return instance;
    }

    public void lamMoiDuLieu() {
        if (phieuDatDao != null && phieuDatDao.isFunctional()) {
            this.danhSachPhieu = new ArrayList<>(phieuDatDao.getAllPhieuDat());
        } else {
            // Client side
            iuh.fit.son23641341.nhahanglau_phantan.network.Request req = 
                new iuh.fit.son23641341.nhahanglau_phantan.network.Request("GET_ALL_PHIEU", null);
            iuh.fit.son23641341.nhahanglau_phantan.network.Response res = 
                iuh.fit.son23641341.nhahanglau_phantan.network.ClientControl.getInstance().sendRequest(req);
            if (res.getStatus().equals("SUCCESS")) {
                this.danhSachPhieu = new ArrayList<>((List<PhieuDatBan>) res.getData());
            }
        }
    }

    public static String taoMaPhieu(String ngayDat, String gioDat, String sdt) {
        String nam = ngayDat.substring(6, 10);
        String thang = ngayDat.substring(3, 5);
        String ngay = ngayDat.substring(0, 2);
        String datePartFormatted = nam + thang + ngay;
        String timePartFormatted = gioDat.replace(":", "");

        String lastThreeSdt = (sdt != null && sdt.length() >= 3) ? sdt.substring(sdt.length() - 3) : "000";
        String randomPart = String.format("%02d", new Random().nextInt(100));

        return "PD" + datePartFormatted + timePartFormatted + lastThreeSdt + randomPart;
    }

    public boolean themPhieuDat(PhieuDatBan phieu) {
        if (phieu.getMaPhieu() == null || phieu.getMaPhieu().isEmpty()) {
            phieu.setMaPhieu(taoMaPhieu(phieu.getNgayDat(), phieu.getGioDat(), phieu.getSdtDat()));
        }
        if (phieuDatDao != null && phieuDatDao.isFunctional()) {
            if (phieuDatDao.insertPhieuDat(phieu)) {
                lamMoiDuLieu();
                return true;
            }
        } else {
            // Client side
            iuh.fit.son23641341.nhahanglau_phantan.network.Request req = 
                new iuh.fit.son23641341.nhahanglau_phantan.network.Request("LUU_PHIEU_DAT", phieu);
            iuh.fit.son23641341.nhahanglau_phantan.network.Response res = 
                iuh.fit.son23641341.nhahanglau_phantan.network.ClientControl.getInstance().sendRequest(req);
            if (res.getStatus().equals("SUCCESS")) {
                lamMoiDuLieu();
                return true;
            }
        }
        return false;
    }

    public PhieuDatBan timPhieuTheoMaBan(int maBan) {
        if (phieuDatDao != null && phieuDatDao.isFunctional()) {
            // Server side: tìm trực tiếp qua DAO
            PhieuDatBan p = phieuDatDao.getPhieuDangSuDungTheoMaBan(maBan);
            if (p != null) return p;
            String ngayHômNay = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());
            ArrayList<PhieuDatBan> list = phieuDatDao.getPhieuDatByBanVaNgay(maBan, ngayHômNay);
            for (PhieuDatBan phieu : list) {
                if ("Đặt trước".equals(phieu.getTrangThai()) || "Đã xác nhận".equals(phieu.getTrangThai())) {
                    return phieu;
                }
            }
            return null;
        } else {
            // Client side: gửi request qua mạng
            iuh.fit.son23641341.nhahanglau_phantan.network.Request req =
                new iuh.fit.son23641341.nhahanglau_phantan.network.Request("TIM_PHIEU_THEO_BAN", maBan);
            iuh.fit.son23641341.nhahanglau_phantan.network.Response res =
                iuh.fit.son23641341.nhahanglau_phantan.network.ClientControl.getInstance().sendRequest(req);
            if (res != null && "SUCCESS".equals(res.getStatus())) {
                return (PhieuDatBan) res.getData();
            }
            return null;
        }
    }

    public PhieuDatBan timPhieuTheoMaPhieu(String maPhieu) {
        if (phieuDatDao != null && phieuDatDao.isFunctional()) {
            return phieuDatDao.timPhieuDatBangMa(maPhieu);
        } else {
            iuh.fit.son23641341.nhahanglau_phantan.network.Request req =
                new iuh.fit.son23641341.nhahanglau_phantan.network.Request("TIM_PHIEU_THEO_MA", maPhieu);
            iuh.fit.son23641341.nhahanglau_phantan.network.Response res =
                iuh.fit.son23641341.nhahanglau_phantan.network.ClientControl.getInstance().sendRequest(req);
            if (res != null && "SUCCESS".equals(res.getStatus())) {
                return (PhieuDatBan) res.getData();
            }
            return null;
        }
    }

    public boolean capNhatPhieuDat(PhieuDatBan phieuCapNhat) {
        if (phieuDatDao != null && phieuDatDao.isFunctional()) {
            if (phieuDatDao.capNhatThongTinKhachHang(phieuCapNhat.getMaPhieu(), phieuCapNhat.getTenKhachDat(),
                    phieuCapNhat.getSdtDat(), phieuCapNhat.getEmailDat())) {
                lamMoiDuLieu();
                return true;
            }
        } else {
            iuh.fit.son23641341.nhahanglau_phantan.network.Request req =
                new iuh.fit.son23641341.nhahanglau_phantan.network.Request("CAP_NHAT_PHIEU_DAT", phieuCapNhat);
            iuh.fit.son23641341.nhahanglau_phantan.network.Response res =
                iuh.fit.son23641341.nhahanglau_phantan.network.ClientControl.getInstance().sendRequest(req);
            if (res != null && "SUCCESS".equals(res.getStatus())) {
                lamMoiDuLieu();
                return true;
            }
        }
        return false;
    }

    public boolean huyPhieuDat(String maPhieu) {
        if (phieuDatDao != null && phieuDatDao.isFunctional()) {
            if (phieuDatDao.capNhatTrangThai(maPhieu, "Đã hủy")) {
                lamMoiDuLieu();
                return true;
            }
        } else {
            // Client side
            iuh.fit.son23641341.nhahanglau_phantan.network.Request req = 
                new iuh.fit.son23641341.nhahanglau_phantan.network.Request("CAP_NHAT_TRANG_THAI_PHIEU", new Object[]{maPhieu, "Đã hủy"});
            iuh.fit.son23641341.nhahanglau_phantan.network.Response res = 
                iuh.fit.son23641341.nhahanglau_phantan.network.ClientControl.getInstance().sendRequest(req);
            if (res.getStatus().equals("SUCCESS")) {
                lamMoiDuLieu();
                return true;
            }
        }
        return false;
    }

    public ArrayList<PhieuDatBan> layTatCaPhieu() {
        lamMoiDuLieu();
        return danhSachPhieu;
    }

    public PhieuDatBan taoPhieuDat(String tenKH, String sdt, String ngayDat, String gioDat, String phuongThuc,
            String emailDat, ArrayList<Integer> danhSachBan, List<? extends ChiTietDatMon> danhSachMon,
            double giamGia) {
        String maPhieu = taoMaPhieu(ngayDat, gioDat, sdt);
        String maKhachHang = null;
        if (sdt != null && !sdt.trim().isEmpty()) {
            // Chỉ tra cứu KH qua DAO khi ở Server side
            if (phieuDatDao != null && phieuDatDao.isFunctional()) {
                maKhachHang = new KhachHang_DAO().getMaKhachHangBySDT(sdt);
            }
        }

        NhanVien nvHienTai = User_Ctr.getInstance().getNhanVienHienTai();
        ArrayList<ChiTietDatMon> danhSachMonCopy = new ArrayList<>();
        if (danhSachMon != null)
            danhSachMonCopy.addAll(danhSachMon);

        PhieuDatBan phieu = new PhieuDatBan(maPhieu, maKhachHang, tenKH, sdt, emailDat, "Đặt trước",
                nvHienTai != null ? nvHienTai.getManv() : "NV001", ngayDat, gioDat,
                new Timestamp(System.currentTimeMillis()), phuongThuc, giamGia, danhSachMonCopy, danhSachBan);

        double tongTienMonAn = phieu.tinhTongTienMonAn();
        int soLuongBan = (danhSachBan != null) ? danhSachBan.size() : 1;
        double tienCoc = (250000 * soLuongBan) + (tongTienMonAn * 0.3);

        phieu.setTienCoc(tienCoc);
        phieu.setTongTien(tongTienMonAn + tienCoc - giamGia);

        if (themPhieuDat(phieu)) {
            return phieu;
        }
        return null;
    }

    public void capNhatDanhSachMonAn(int maBan, List<? extends ChiTietDatMon> danhSachMon) {
        ArrayList<ChiTietDatMon> danhSachMonCopy = new ArrayList<>();
        if (danhSachMon != null)
            danhSachMonCopy.addAll(danhSachMon);

        PhieuDatBan phieu = timPhieuTheoMaBan(maBan);
        if (phieu != null) {
            phieuDatDao.capNhatMonAnCuaPhieu(phieu.getMaPhieu(), danhSachMonCopy);
            lamMoiDuLieu();
        } else {
            gioHangTamThoi.put(maBan, danhSachMonCopy);
        }
    }

    public void xoaGioHangTamThoi(int maBan) {
        gioHangTamThoi.remove(maBan);
    }

    public ArrayList<ChiTietDatMon> layDanhSachMonAnChoBan(int maBan) {
        PhieuDatBan phieu = timPhieuTheoMaBan(maBan);
        if (phieu != null)
            return new ArrayList<>(phieu.getDanhSachMonAn());

        ArrayList<ChiTietDatMon> gioHang = gioHangTamThoi.get(maBan);
        return gioHang != null ? gioHang : new ArrayList<>();
    }

    public boolean kiemTraTrungLich(int maBan, String ngayDat, String gioDat) {
        ArrayList<PhieuDatBan> phieus;
        if (phieuDatDao != null && phieuDatDao.isFunctional()) {
            phieus = phieuDatDao.getPhieuDatByBanVaNgay(maBan, ngayDat);
        } else {
            phieus = layPhieuDatTheoBanVaNgay(maBan, ngayDat);
        }
        for (PhieuDatBan p : phieus) {
            if (!p.getTrangThai().equals("Đã hủy"))
                return true;
        }
        return false;
    }

    public void luuKhuyenMaiTamThoi(int maBan, KhuyenMai khuyenMai) {
        if (khuyenMai != null)
            khuyenMaiTamThoi.put(maBan, khuyenMai);
        else
            khuyenMaiTamThoi.remove(maBan);
    }

    public KhuyenMai layKhuyenMaiTamThoi(int maBan) {
        return khuyenMaiTamThoi.get(maBan);
    }

    public void xoaKhuyenMaiTamThoi(int maBan) {
        khuyenMaiTamThoi.remove(maBan);
    }

    public boolean capNhatTrangThaiPhieu(String maPhieu, String trangThai) {
        if (phieuDatDao != null && phieuDatDao.isFunctional()) {
            if (phieuDatDao.capNhatTrangThai(maPhieu, trangThai)) {
                lamMoiDuLieu();
                return true;
            }
        } else {
            iuh.fit.son23641341.nhahanglau_phantan.network.Request req =
                new iuh.fit.son23641341.nhahanglau_phantan.network.Request("CAP_NHAT_TRANG_THAI_PHIEU", new Object[]{maPhieu, trangThai});
            iuh.fit.son23641341.nhahanglau_phantan.network.Response res =
                iuh.fit.son23641341.nhahanglau_phantan.network.ClientControl.getInstance().sendRequest(req);
            if (res != null && "SUCCESS".equals(res.getStatus())) {
                lamMoiDuLieu();
                return true;
            }
        }
        return false;
    }

    public ArrayList<PhieuDatBan> layPhieuDatTheoNgay(String ngay) {
        if (phieuDatDao != null && phieuDatDao.isFunctional()) {
            return phieuDatDao.getPhieuDatByNgay(ngay);
        } else {
            iuh.fit.son23641341.nhahanglau_phantan.network.Request req =
                new iuh.fit.son23641341.nhahanglau_phantan.network.Request("GET_PHIEU_THEO_NGAY", ngay);
            iuh.fit.son23641341.nhahanglau_phantan.network.Response res =
                iuh.fit.son23641341.nhahanglau_phantan.network.ClientControl.getInstance().sendRequest(req);
            if (res != null && "SUCCESS".equals(res.getStatus())) {
                return new ArrayList<>((List<PhieuDatBan>) res.getData());
            }
            return new ArrayList<>();
        }
    }

    public ArrayList<Integer> layDanhSachBanTrungLich(ArrayList<Integer> danhSachMaBan, String ngayDat, String gioDat) {
        ArrayList<Integer> banTrungLich = new ArrayList<>();
        for (Integer maBan : danhSachMaBan) {
            if (kiemTraTrungLich(maBan, ngayDat, gioDat)) {
                banTrungLich.add(maBan);
            }
        }
        return banTrungLich;
    }

    public boolean xoaPhieuDat(int maBan) {
        PhieuDatBan p = layPhieuDangSuDungTheoMaBan(maBan);
        if (p != null) {
            if (phieuDatDao.capNhatTrangThai(p.getMaPhieu(), "Đã hủy")) {
                lamMoiDuLieu();
                return true;
            }
        }
        return false;
    }

    public boolean capNhatMonAnCuaPhieu(String maPhieu, ArrayList<ChiTietDatMon> danhSachMon) {
        if (phieuDatDao != null && phieuDatDao.isFunctional()) {
            if (phieuDatDao.capNhatMonAnCuaPhieu(maPhieu, danhSachMon)) {
                lamMoiDuLieu();
                return true;
            }
        } else {
            iuh.fit.son23641341.nhahanglau_phantan.network.Request req =
                new iuh.fit.son23641341.nhahanglau_phantan.network.Request("CAP_NHAT_MON_AN_PHIEU",
                    new Object[]{maPhieu, danhSachMon});
            iuh.fit.son23641341.nhahanglau_phantan.network.Response res =
                iuh.fit.son23641341.nhahanglau_phantan.network.ClientControl.getInstance().sendRequest(req);
            if (res != null && "SUCCESS".equals(res.getStatus())) {
                lamMoiDuLieu();
                return true;
            }
        }
        return false;
    }

    public ArrayList<PhieuDatBan> layPhieuDatTheoBanVaNgay(int maBan, String ngay) {
        if (phieuDatDao != null && phieuDatDao.isFunctional()) {
            return phieuDatDao.getPhieuDatByBanVaNgay(maBan, ngay);
        } else {
            iuh.fit.son23641341.nhahanglau_phantan.network.Request req =
                new iuh.fit.son23641341.nhahanglau_phantan.network.Request("GET_PHIEU_THEO_BAN_VA_NGAY",
                    new Object[]{maBan, ngay});
            iuh.fit.son23641341.nhahanglau_phantan.network.Response res =
                iuh.fit.son23641341.nhahanglau_phantan.network.ClientControl.getInstance().sendRequest(req);
            if (res != null && "SUCCESS".equals(res.getStatus())) {
                return new ArrayList<>((List<PhieuDatBan>) res.getData());
            }
            return new ArrayList<>();
        }
    }

    public PhieuDatBan layPhieuDangSuDungTheoMaBan(int maBan) {
        if (phieuDatDao != null && phieuDatDao.isFunctional()) {
            return phieuDatDao.getPhieuDangSuDungTheoMaBan(maBan);
        } else {
            iuh.fit.son23641341.nhahanglau_phantan.network.Request req =
                new iuh.fit.son23641341.nhahanglau_phantan.network.Request("GET_PHIEU_DANG_SU_DUNG", maBan);
            iuh.fit.son23641341.nhahanglau_phantan.network.Response res =
                iuh.fit.son23641341.nhahanglau_phantan.network.ClientControl.getInstance().sendRequest(req);
            if (res != null && "SUCCESS".equals(res.getStatus())) {
                return (PhieuDatBan) res.getData();
            }
            return null;
        }
    }
}
