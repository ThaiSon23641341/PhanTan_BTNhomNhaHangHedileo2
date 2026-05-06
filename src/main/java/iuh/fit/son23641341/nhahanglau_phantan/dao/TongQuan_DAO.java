package iuh.fit.son23641341.nhahanglau_phantan.dao;

import iuh.fit.son23641341.nhahanglau_phantan.mock.MockData;

// NOTE: Database logic removed; DAO now uses in-memory mock data.
public class TongQuan_DAO {

    public int getSoKhuyenMai() {
        return MockData.khuyenMais().size();
    }



    public int getSoNhanVien() {
        return MockData.nhanViens().size();
    }

}

