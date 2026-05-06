package iuh.fit.son23641341.nhahanglau_phantan.dao;

import iuh.fit.son23641341.nhahanglau_phantan.mock.MockData;

// NOTE: Database logic removed; DAO now uses in-memory mock data.
public class TongQuan_DAO {

    public int getSoKhuyenMai() {
        return MockData.khuyenMais().size();
    }

    public int getSoBanDangDat() {
        return (int) MockData.banAns().stream()
            .filter(ban -> "Đặt trước".equals(ban.getTrangThai()))
            .count();
    }

    public int getSoNhanVien() {
        return MockData.nhanViens().size();
    }

    public int getSoBanDangSuDung() {
        return (int) MockData.banAns().stream()
            .filter(ban -> "Đang sử dụng".equals(ban.getTrangThai()))
            .count();
    }
}

