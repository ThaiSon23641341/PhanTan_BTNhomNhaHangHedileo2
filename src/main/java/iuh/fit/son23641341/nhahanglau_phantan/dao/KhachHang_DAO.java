package iuh.fit.son23641341.nhahanglau_phantan.dao;

import iuh.fit.son23641341.nhahanglau_phantan.entity.KhachHangThanhVien;
import iuh.fit.son23641341.nhahanglau_phantan.entity.PhieuDatBan;
import iuh.fit.son23641341.nhahanglau_phantan.mock.MockData;

import java.time.LocalDate;
import java.util.ArrayList;

// NOTE: Database logic removed; DAO now uses in-memory mock data.
public class KhachHang_DAO {

    public String getMaKhachHangBySDT(String sdt) {
        return MockData.khachHangs().stream()
            .filter(item -> item.getSoDienThoai().equals(sdt))
            .map(KhachHangThanhVien::getMaKhachHang)
            .findFirst()
            .orElse(null);
    }

    public ArrayList<KhachHangThanhVien> getAllKhachHang() {
        return new ArrayList<>(MockData.khachHangs());
    }

    public String taoMaKhachHangMoi() {
        int max = MockData.khachHangs().stream()
            .map(KhachHangThanhVien::getMaKhachHang)
            .filter(ma -> ma.startsWith("KH"))
            .map(ma -> ma.substring(2))
            .mapToInt(value -> {
                try {
                    return Integer.parseInt(value);
                } catch (NumberFormatException ignored) {
                    return 0;
                }
            })
            .max()
            .orElse(0);
        return String.format("KH%03d", max + 1);
    }

    public ArrayList<KhachHangThanhVien> timKhachHangTheoSDT(String sdt) {
        ArrayList<KhachHangThanhVien> list = new ArrayList<>();
        for (KhachHangThanhVien kh : MockData.khachHangs()) {
            if (kh.getSoDienThoai().contains(sdt)) {
                list.add(kh);
            }
        }
        return list;
    }

    public boolean existsBySoDienThoai(String sdt) {
        return MockData.khachHangs().stream()
            .anyMatch(item -> item.getSoDienThoai().equals(sdt));
    }

    public boolean existsBySoDienThoaiExcept(String sdt, String maKhachHang) {
        return MockData.khachHangs().stream()
            .anyMatch(item -> item.getSoDienThoai().equals(sdt) && !item.getMaKhachHang().equals(maKhachHang));
    }

    public boolean themKhachHang(KhachHangThanhVien kh) {
        if (kh == null || existsBySoDienThoai(kh.getSoDienThoai())) {
            return false;
        }
        if (kh.getMaKhachHang() == null || kh.getMaKhachHang().isEmpty()) {
            try {
                kh.setMaKhachHang(taoMaKhachHangMoi());
                kh.setNgayDangKy(LocalDate.now());
            } catch (Exception ignored) {
            }
        }
        return MockData.khachHangs().add(kh);
    }

    public boolean capNhatKhachHang(KhachHangThanhVien kh) {
        if (kh == null) {
            return false;
        }
        KhachHangThanhVien existing = MockData.khachHangs().stream()
            .filter(item -> item.getMaKhachHang().equals(kh.getMaKhachHang()))
            .findFirst()
            .orElse(null);
        if (existing == null) {
            return false;
        }
        try {
            existing.setHoTen(kh.getHoTen());
            existing.setSoDienThoai(kh.getSoDienThoai());
            existing.setEmail(kh.getEmail());
            existing.setThanhVien(kh.getThanhVien());
            existing.setDiemTichLuy(kh.getDiemTichLuy());
            existing.setGioiTinh(kh.getGioiTinh());
        } catch (Exception ignored) {
            return false;
        }
        return true;
    }

    public boolean xoaKhachHang(String maKhachHang) {
        return MockData.khachHangs().removeIf(item -> item.getMaKhachHang().equals(maKhachHang));
    }

    public boolean congDiemTichLuy(String maKhachHang, int diemCong) {
        KhachHangThanhVien existing = MockData.khachHangs().stream()
            .filter(item -> item.getMaKhachHang().equals(maKhachHang))
            .findFirst()
            .orElse(null);
        if (existing == null) {
            return false;
        }
        existing.setDiemTichLuy(existing.getDiemTichLuy() + diemCong);
        return true;
    }

    public String getHang(PhieuDatBan pdb) {
        if (pdb == null || pdb.getMaKhachHang() == null) {
            return null;
        }
        return MockData.khachHangs().stream()
            .filter(item -> item.getMaKhachHang().equals(pdb.getMaKhachHang()))
            .map(KhachHangThanhVien::getThanhVien)
            .findFirst()
            .orElse(null);
    }

    public String getmaKhachHangbySDT(String sdt) {
        return getMaKhachHangBySDT(sdt);
    }
}

