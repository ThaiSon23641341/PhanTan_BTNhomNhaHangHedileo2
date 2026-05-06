
package iuh.fit.son23641341.nhahanglau_phantan.dao;

import iuh.fit.son23641341.nhahanglau_phantan.entity.KhuyenMai;
import iuh.fit.son23641341.nhahanglau_phantan.mock.MockData;

import java.util.ArrayList;
import java.util.List;

// NOTE: Database logic removed; DAO now uses in-memory mock data.
public class KhuyenMai_DAO {

    public List<KhuyenMai> getAllKhuyenMai() {
        return new ArrayList<>(MockData.khuyenMais());
    }

    public boolean themKhuyenMai(KhuyenMai km) {
        if (km == null || timTheoMa(km.getMaKhuyenMai()) != null) {
            return false;
        }
        return MockData.khuyenMais().add(km);
    }

    public boolean capNhatKhuyenMai(KhuyenMai km) {
        if (km == null) {
            return false;
        }
        KhuyenMai existing = timTheoMa(km.getMaKhuyenMai());
        if (existing == null) {
            return false;
        }
        existing.setTenKhuyenMai(km.getTenKhuyenMai());
        existing.setPhanTramGiam(km.getPhanTramGiam());
        existing.setNgayBatDau(km.getNgayBatDau());
        existing.setNgayKetThuc(km.getNgayKetThuc());
        existing.setMoTa(km.getMoTa());
        return true;
    }

    public boolean xoaKhuyenMai(String maKhuyenMai) {
        return MockData.khuyenMais().removeIf(km -> km.getMaKhuyenMai().equals(maKhuyenMai));
    }

    public KhuyenMai timTheoMa(String maKhuyenMai) {
        return MockData.khuyenMais().stream()
            .filter(km -> km.getMaKhuyenMai().equals(maKhuyenMai))
            .findFirst()
            .orElse(null);
    }
}

