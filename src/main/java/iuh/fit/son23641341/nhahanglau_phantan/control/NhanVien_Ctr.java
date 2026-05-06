package iuh.fit.son23641341.nhahanglau_phantan.control;

import java.util.ArrayList;

import iuh.fit.son23641341.nhahanglau_phantan.entity.NhanVien;

// NOTE: Controller now uses mock data; database logic removed.
public class NhanVien_Ctr {

	 private  final ArrayList<NhanVien> danhSachNhanVien;

	 
	    private NhanVien_Ctr() throws Exception {
	    		        this.danhSachNhanVien = new ArrayList<>();
	    		        napDuLieuTuSQL();
	    }

	    private void napDuLieuTuSQL() throws Exception {
					    	iuh.fit.son23641341.nhahanglau_phantan.dao.NhanVien_DAO dao = new iuh.fit.son23641341.nhahanglau_phantan.dao.NhanVien_DAO();
	    	    danhSachNhanVien.addAll(dao.getAllNhanVien());
		}

	    
	    public ArrayList<NhanVien> getDanhSachNhanVien() {
	    	System.out.println("Số lượng nhân viên: " + danhSachNhanVien.size());
	    	return danhSachNhanVien;
	    }
	 

}
