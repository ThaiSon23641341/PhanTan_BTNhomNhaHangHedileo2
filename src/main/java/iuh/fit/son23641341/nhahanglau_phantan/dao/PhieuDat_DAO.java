package iuh.fit.son23641341.nhahanglau_phantan.dao;

import iuh.fit.son23641341.nhahanglau_phantan.entity.PhieuDatBan;
import iuh.fit.son23641341.nhahanglau_phantan.entity.ChiTietDatMon;
import iuh.fit.son23641341.nhahanglau_phantan.mock.MockData;

import java.util.ArrayList;

// NOTE: Database logic removed; DAO now uses in-memory mock data.
public class PhieuDat_DAO {

    public boolean insertPhieuDat(PhieuDatBan p) {
        if (p == null) {
            return false;
        }
        return MockData.phieuDats().add(p);
    }

    public ArrayList<PhieuDatBan> getAllPhieuDat() {
        return new ArrayList<>(MockData.phieuDats());
    }

    public ArrayList<PhieuDatBan> timKiemPhieuDat(String keyword) {
        ArrayList<PhieuDatBan> ds = new ArrayList<>();
        String query = keyword == null ? "" : keyword.trim();
        for (PhieuDatBan phieu : MockData.phieuDats()) {
            if (query.isEmpty()
                || (phieu.getMaPhieu() != null && phieu.getMaPhieu().contains(query))
                || (phieu.getTenKhachDat() != null && phieu.getTenKhachDat().contains(query))
                || (phieu.getSdtDat() != null && phieu.getSdtDat().contains(query))) {
                ds.add(phieu);
            }
        }
        return ds;
    }

    public ArrayList<PhieuDatBan> getPhieuDatByNgay(String ngayDat) {
        ArrayList<PhieuDatBan> ds = new ArrayList<>();
        if (ngayDat == null) {
            return ds;
        }
        for (PhieuDatBan phieu : MockData.phieuDats()) {
            if (ngayDat.equals(phieu.getNgayDat())) {
                ds.add(phieu);
            }
        }
        return ds;
    }

    public ArrayList<PhieuDatBan> getPhieuDatByBanVaNgay(int maBan, String ngayDat) {
        ArrayList<PhieuDatBan> ds = new ArrayList<>();
        for (PhieuDatBan phieu : MockData.phieuDats()) {
            boolean matchNgay = ngayDat == null || ngayDat.equals(phieu.getNgayDat());
            boolean matchBan = phieu.getDanhSachBanDaChon().contains(maBan) || phieu.getMaBan() == maBan;
            if (matchNgay && matchBan) {
                ds.add(phieu);
            }
        }
        return ds;
    }

    public boolean capNhatTrangThai(String maPhieu, String trangThai) {
        PhieuDatBan phieu = timPhieuDatBangMa(maPhieu);
        if (phieu == null) {
            return false;
        }
        phieu.setTrangThai(trangThai);
        return true;
    }

    public boolean capNhatThongTinKhachHang(String maPhieu, String tenKhach, String sdt, String email) {
        PhieuDatBan phieu = timPhieuDatBangMa(maPhieu);
        if (phieu == null) {
            return false;
        }
        phieu.setTenKhachDat(tenKhach);
        phieu.setSdtDat(sdt);
        phieu.setEmailDat(email);
        return true;
    }

    public boolean capNhatMonAnCuaPhieu(String maPhieu, ArrayList<ChiTietDatMon> danhSachMon) {
        PhieuDatBan phieu = timPhieuDatBangMa(maPhieu);
        if (phieu == null) {
            return false;
        }
        phieu.setDanhSachMonAn(danhSachMon);
        return true;
    }

    public PhieuDatBan getPhieuDangSuDungTheoMaBan(int maBan) {
        for (PhieuDatBan phieu : MockData.phieuDats()) {
            if (phieu.getMaBan() == maBan || phieu.getDanhSachBanDaChon().contains(maBan)) {
                return phieu;
            }
        }
        return null;
    }

    public PhieuDatBan timPhieuDatBangMa(String maPhieu) {
        if (maPhieu == null) {
            return null;
        }
        return MockData.phieuDats().stream()
            .filter(item -> maPhieu.equals(item.getMaPhieu()))
            .findFirst()
            .orElse(null);
    }
}

