package iuh.fit.son23641341.nhahanglau_phantan.dao;

import iuh.fit.son23641341.nhahanglau_phantan.entity.HoaDon;
import iuh.fit.son23641341.nhahanglau_phantan.entity.PhieuDatBan;
import iuh.fit.son23641341.nhahanglau_phantan.mock.MockData;

import java.util.ArrayList;
import java.util.List;

// NOTE: Database logic removed; DAO now uses in-memory mock data.
public class HoaDon_DAO {

    public List<HoaDon> timKiemHoaDon(String keyword) {
        List<HoaDon> dsHoaDon = new ArrayList<>();
        String query = keyword == null ? "" : keyword.trim();
        for (HoaDon hd : MockData.hoaDons()) {
            if (query.isEmpty()
                || hd.getMaHoaDon().contains(query)
                || (hd.getTrangThai() != null && hd.getTrangThai().contains(query))
                || (hd.getPhieuDat() != null && hd.getPhieuDat().getSdtDat() != null
                    && hd.getPhieuDat().getSdtDat().contains(query))) {
                dsHoaDon.add(hd);
            }
        }
        return dsHoaDon;
    }

    public boolean addHoaDon(HoaDon hd) {
        if (hd == null) {
            return false;
        }
        return MockData.hoaDons().add(hd);
    }

    public boolean taoHoaDonMoi(PhieuDatBan phieuDat) {
        if (phieuDat == null) {
            return false;
        }
        HoaDon hd = new HoaDon(phieuDat);
        return MockData.hoaDons().add(hd);
    }
}

