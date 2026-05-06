
package iuh.fit.son23641341.nhahanglau_phantan.dao;

import iuh.fit.son23641341.nhahanglau_phantan.entity.BanAn;
import iuh.fit.son23641341.nhahanglau_phantan.entity.KhuyenMai;
import iuh.fit.son23641341.nhahanglau_phantan.mock.MockData;

import java.util.ArrayList;
import java.util.List;

// NOTE: Database logic removed; DAO now uses in-memory mock data.
public class BanAn_DAO {

    public List<BanAn> getAllBanAn() {
        return new ArrayList<>(MockData.banAns());
    }

    public boolean capNhatTrangThaiBan(int maBan, String trangThai) {
        BanAn ban = MockData.banAns().stream()
            .filter(item -> item.getMaBan() == maBan)
            .findFirst()
            .orElse(null);
        if (ban == null) {
            return false;
        }
        ban.capNhatTrangThai(trangThai);
        return true;
    }

    public KhuyenMai timTheoMa(String maKhuyenMai) {
        return null;
    }
}

