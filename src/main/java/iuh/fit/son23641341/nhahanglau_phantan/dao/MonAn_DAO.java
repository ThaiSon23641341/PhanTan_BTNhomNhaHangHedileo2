
package iuh.fit.son23641341.nhahanglau_phantan.dao;

import iuh.fit.son23641341.nhahanglau_phantan.entity.MonAn;
import iuh.fit.son23641341.nhahanglau_phantan.mock.MockData;

import java.util.ArrayList;
import java.util.List;

// NOTE: Database logic removed; DAO now uses in-memory mock data.
public class MonAn_DAO {

	public List<MonAn> getAllMonAn() {
		return new ArrayList<>(MockData.monAns());
	}

	public boolean existsByMaMon(String maMon) {
		return MockData.monAns().stream()
			.anyMatch(mon -> mon.getMaMon().equalsIgnoreCase(maMon));
	}

	public boolean themMonAn(MonAn mon) {
		if (mon == null || existsByMaMon(mon.getMaMon())) {
			return false;
		}
		return MockData.monAns().add(mon);
	}

	public boolean capNhatMonAn(MonAn mon) {
		if (mon == null) {
			return false;
		}
		MonAn existing = MockData.monAns().stream()
			.filter(item -> item.getMaMon().equalsIgnoreCase(mon.getMaMon()))
			.findFirst()
			.orElse(null);
		if (existing == null) {
			return false;
		}
		try {
			existing.setTenMon(mon.getTenMon());
			existing.setLoaiMon(mon.getLoaiMon());
			existing.setGia(mon.getGia());
			existing.setMoTa(mon.getMoTa());
		} catch (Exception ignored) {
			return false;
		}
		return true;
	}

	public boolean xoaMonAn(String maMon) {
		return MockData.monAns().removeIf(mon -> mon.getMaMon().equalsIgnoreCase(maMon));
	}

	public MonAn timTheoMa(String maMon) {
		return MockData.monAns().stream()
			.filter(mon -> mon.getMaMon().equalsIgnoreCase(maMon))
			.findFirst()
			.orElse(null);
	}
}

