package iuh.fit.son23641341.nhahanglau_phantan.control;

import iuh.fit.son23641341.nhahanglau_phantan.entity.KhuyenMai;
import iuh.fit.son23641341.nhahanglau_phantan.dao.KhuyenMai_DAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Controller quản lý danh sách khuyến mãi (KhuyenMai).
 * Cung cấp các thao tác CRUD lưu tạm trong ArrayList.
 */
// NOTE: Controller now uses mock data; database logic removed.
public class KhuyenMai_Ctr {
	private final ArrayList<KhuyenMai> danhSachKhuyenMai;

	/**
	 * Khởi tạo controller và nạp dữ liệu mẫu.
	 */
	public KhuyenMai_Ctr() {
		this.danhSachKhuyenMai = new ArrayList<>();
//		khoiTaoDuLieuMau();
		napDuLieuTuSQL();
	}

	public ArrayList<KhuyenMai> getDanhSachKhuyenMai() {
		return danhSachKhuyenMai;
	}

	//Nạp dữ liệu từ DB vào ctr
	private void napDuLieuTuSQL() {
	    KhuyenMai_DAO dao = new KhuyenMai_DAO();
//	    danhSachKhuyenMai.clear();
	    danhSachKhuyenMai.addAll(dao.getAllKhuyenMai());
	}

	/**
	 * Tạo mã khuyến mãi tiếp theo theo định dạng KMxxx.
	 */
	private String taoMaTiepTheo() {
		int max = 0;
		for (KhuyenMai k : danhSachKhuyenMai) {
			String ma = k.getMaKhuyenMai();
			if (ma != null && ma.toUpperCase(Locale.ROOT).startsWith("KM")) {
				try {
					int so = Integer.parseInt(ma.substring(2));
					if (so > max) max = so;
				} catch (NumberFormatException ignored) {
				}
			}
		}
		return String.format("KM%03d", max + 1);
	}

	// CREATE
	public boolean themKhuyenMai(KhuyenMai km) {
		if (km == null) return false;

		String ma = km.getMaKhuyenMai();
		if (ma == null || ma.trim().isEmpty()) {
			try {
				km.setMaKhuyenMai(taoMaTiepTheo());
			} catch (Exception e) {
				return false;
			}
		} else {
			if (timTheoMa(ma) != null) return false; // trùng mã
		}
		KhuyenMai_DAO dao = new KhuyenMai_DAO();
		dao.themKhuyenMai(km);
		return danhSachKhuyenMai.add(km);
	}

	// READ
	public KhuyenMai timTheoMa(String ma) {
		if (ma == null) return null;
		String target = ma.trim();
		for (KhuyenMai k : danhSachKhuyenMai) {
			if (target.equalsIgnoreCase(k.getMaKhuyenMai())) return k;
		}
		return null;
	}

	public List<KhuyenMai> timTheoTen(String query) {
		ArrayList<KhuyenMai> kq = new ArrayList<>();
		if (query == null) return kq;
		String q = query.trim().toLowerCase(Locale.ROOT);
		if (q.isEmpty()) {
			kq.addAll(danhSachKhuyenMai);
			return kq;
		}
		for (KhuyenMai k : danhSachKhuyenMai) {
			String ten = k.getTenKhuyenMai();
			if (ten != null && ten.toLowerCase(Locale.ROOT).contains(q)) {
				kq.add(k);
			}
		}
		return kq;
	}

	// UPDATE theo mã
	public boolean capNhatKhuyenMai(String ma, String ten, double phanTram, String ngayBatDau, String ngayKetThuc, String moTa) {
		KhuyenMai k = timTheoMa(ma);
		if (k == null) return false;
		try {
			k.setTenKhuyenMai(ten);
			k.setPhanTramGiam(phanTram);
			k.setNgayBatDau(ngayBatDau);
			k.setNgayKetThuc(ngayKetThuc);
			k.setMoTa(moTa);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// DELETE theo mã
	public boolean xoaKhuyenMai(String ma) {
		KhuyenMai k = timTheoMa(ma);
		if (k == null) return false;
		KhuyenMai_DAO dao = new KhuyenMai_DAO();
		dao.xoaKhuyenMai(ma);
	
		return danhSachKhuyenMai.remove(k);
	}
}

