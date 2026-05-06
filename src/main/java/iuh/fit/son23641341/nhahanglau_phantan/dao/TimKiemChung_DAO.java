package iuh.fit.son23641341.nhahanglau_phantan.dao;

import iuh.fit.son23641341.nhahanglau_phantan.entity.MonAn;
import iuh.fit.son23641341.nhahanglau_phantan.mock.MockData;

import java.util.ArrayList;
import java.util.List;

// NOTE: Database logic removed; DAO now uses in-memory mock data.
public class TimKiemChung_DAO {

    public List<MonAn> getAllMonAn() {
        return new ArrayList<>(MockData.monAns());
    }

    public List<MonAn> timKiemMonAn(String keyword, boolean searchByCode, boolean searchByOrder, boolean searchByBill,
                                    boolean searchByCustomer, boolean searchByTable) {
        List<MonAn> list = new ArrayList<>();
        String query = keyword == null ? "" : keyword.trim().toLowerCase();
        for (MonAn mon : MockData.monAns()) {
            String target = searchByCode ? mon.getMaMon() : mon.getTenMon();
            if (query.isEmpty() || (target != null && target.toLowerCase().contains(query))) {
                list.add(mon);
            }
        }
        return list;
    }
}

