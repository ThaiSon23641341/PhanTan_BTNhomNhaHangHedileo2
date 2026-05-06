package iuh.fit.son23641341.nhahanglau_phantan.control;

import java.util.ArrayList;
import java.util.List;
import iuh.fit.son23641341.nhahanglau_phantan.dao.ThongKe_DAO;
import java.text.DecimalFormat;
import java.time.LocalDate;

// NOTE: Controller now uses mock data; database logic removed.
public class ThongKe_Ctr {
    /**
     * Lấy và tính toán dữ liệu cho thống kê theo ngày
     * @param ngay Ngày cần thống kê
     * @return ThongKeCardData đã tính toán xong
     */
    public ThongKeCardData getDuLieuChoTheThongKeTheoNgay(LocalDate ngay) {
        ThongKe_DAO.ThongKeNgay hienTai = getThongKeTheoNgay(ngay);
        ThongKe_DAO.ThongKeNgay ngayTruoc = getThongKeTheoNgay(ngay.minusDays(1));
        ThongKeCardData result = new ThongKeCardData();
        result.dtHienTai = hienTai.tongDoanhThu;
        result.dtChenhLech = tinhPhanTramChenhLech(hienTai.tongDoanhThu, ngayTruoc.tongDoanhThu);
        result.hdHienTai = hienTai.tongHoaDon;
        result.hdChenhLech = tinhPhanTramChenhLech(hienTai.tongHoaDon, ngayTruoc.tongHoaDon);
        return result;
    }

    /**
     * Tính tổng doanh thu của một ngày
     */
    public double TinhTongDoanhThuNgay(LocalDate ngay) {
        ThongKe_DAO.ThongKeNgay data = getThongKeTheoNgay(ngay);
        return data.tongDoanhThu;
    }

    /**
     * Tính giá trị trung bình trên hóa đơn của một ngày
     */
    public double TinhDoanhSoTrungBinhHoaDonTheoNgay(LocalDate ngay) {
        ThongKe_DAO.ThongKeNgay data = getThongKeTheoNgay(ngay);
        if (data.tongHoaDon == 0) return 0;
        return data.tongDoanhThu / data.tongHoaDon;
    }

    public static class ThongKeCardData {
        public double dtHienTai;
        public String dtChenhLech; 
        public int hdHienTai;
        public String hdChenhLech; 
    }

    
    private ThongKe_DAO thongKeDAO; 
    
    private int namHienTai = 0; 
    private static ThongKe_Ctr instance = new ThongKe_Ctr();
    private ArrayList<ThongKe_DAO.ThongKeThang> cacheDoanhThuNam = new ArrayList<>();
    private ArrayList<ThongKe_DAO.TopMonAn> cacheTopMonAn = new ArrayList<>();
    private int cacheTopMonAnThang = 0;
    private int cacheTopMonAnNam = 0;
    private int cacheTopMonAnLimit = 0;

    public static ThongKe_Ctr getInstance() {
        return instance;
    }
    
    public ThongKe_Ctr() {
        this.thongKeDAO = new ThongKe_DAO();
    }

        /**
         * Lấy thống kê doanh thu và số hóa đơn theo ngày
         */
        public ThongKe_DAO.ThongKeNgay getThongKeTheoNgay(LocalDate ngay) {
            return thongKeDAO.getThongKeTheoNgay(ngay);
        }

        /**
         * Lấy top món ăn theo ngày
         */
        public ArrayList<ThongKe_DAO.TopMonAn> getTopMonAnTheoNgay(LocalDate ngay, int limit) {
            return thongKeDAO.getTopMonAnTheoNgay(ngay, limit);
        }



    public ArrayList<ThongKe_DAO.ThongKeThang> getDoanhThuTheoThang(int nam) {
    	if(this.namHienTai != nam || this.cacheDoanhThuNam.isEmpty()) { 
			this.namHienTai = nam;	
			System.out.println("Lấy dữ liệu doanh thu cho năm: " + nam);
    	 
    	}
    	   this.cacheDoanhThuNam = thongKeDAO.getDoanhThuTheoThangTrongNam(nam);
        return this.cacheDoanhThuNam; 
    }
    
    
  
    private ThongKe_DAO.ThongKeThang findThongKeByMonth(int thang, List<ThongKe_DAO.ThongKeThang> dataList) {
        for (ThongKe_DAO.ThongKeThang tk : dataList) {
            if (tk.thang == thang) {
                return tk;
            }
        }
        return new ThongKe_DAO.ThongKeThang(thang, 0, 0); 
    }
    
    
    /**
     * Hàm hỗ trợ: Tính toán % chênh lệch và trả về chuỗi định dạng 
     */
    private String tinhPhanTramChenhLech(double hienTai, double thangTruoc) {
        
        DecimalFormat percentageFormat = new DecimalFormat("0.0");
        
        if (thangTruoc == 0) {
            return (hienTai > 0) ? "N/A" : "---";
        }
        
        double change = ((hienTai - thangTruoc) / thangTruoc) * 100;
        String prefix = (change >= 0) ? "▲ " : "▼ ";
        
        return prefix + percentageFormat.format(Math.abs(change)) + "%";
    }
    
 // Trong class ThongKe_Ctr

    /**
     * Lấy danh sách Top N món ăn được cache theo tháng/năm và giới hạn.
     * @param thang Tháng hiện tại (1-12).
     * @param nam Năm hiện tại.
     * @param limit Số lượng món ăn tối đa cần lấy (ví dụ: 5).
     * @return Danh sách TopMonAn.
     */
    public ArrayList<ThongKe_DAO.TopMonAn> getTopMonAn(int thang, int nam, int limit) {
        
        if (this.cacheTopMonAnThang != thang || 
            this.cacheTopMonAnNam != nam || 
            this.cacheTopMonAnLimit != limit ||	
            this.cacheTopMonAn.isEmpty()) 
        {
            this.cacheTopMonAnThang = thang;
            this.cacheTopMonAnNam = nam;
            this.cacheTopMonAnLimit = limit;
                        System.out.println("Lấy dữ liệu Top Món Ăn cho tháng: " + thang + "/" + nam + " (Limit: " + limit + ")");
          
        }
        this.cacheTopMonAn = thongKeDAO.getTopMonAnTheoThang(thang, nam, limit);
        return this.cacheTopMonAn;
    }
    
    
    
    /**
     * Hàm tính tổng doanh thu trong năm. 
     * @param 
     * @return Tổng doanh thu trong năm.
     */
    public double TinhTongDoanhThuNam(int nam) {
		List<ThongKe_DAO.ThongKeThang> allData = getDoanhThuTheoThang(nam); 
		double tongDoanhThu = 0;
		for (ThongKe_DAO.ThongKeThang tk : allData) {
			tongDoanhThu += tk.tongDoanhThu;
		}
		return tongDoanhThu;
	}
    
    /**
     * Hàm tính doanh số trung bình hàng ngày trong năm 
     * @param 
     * @return Doanh số trung bình hàng ngày trong năm.
     */
    public double TinhDoanhSoTrungBinhNgay(int nam) {
		double tongDoanhThuNam = TinhTongDoanhThuNam(nam);
		boolean isNamNhuan = java.time.Year.isLeap(nam);
		int soNgayTrongNam = isNamNhuan ? 366 : 365;
		return tongDoanhThuNam / soNgayTrongNam;
	}
    
    /**
     * Hàm tính doanh số trung bình trên hóa đơn
     * @param 
     * @return giá trị trung bình trên hóa đơn.
     */
    public double TinhDoanhSoTrungBinhHoaDon(int nam) {
    			List<ThongKe_DAO.ThongKeThang> allData = getDoanhThuTheoThang(nam);
    		
    							return allData.stream()
										.mapToDouble(tk -> tk.tongDoanhThu)
										.sum() /
										allData.stream()
										.mapToInt(tk -> tk.tonghoaDon)
										.sum();
    }
    
    /**
     * 🟢HÀM CHÍNH: Lấy và tính toán toàn bộ dữ liệu cần thiết cho 2 thẻ thống kê đầu.
     * @param nam Năm hiện tại.
     * @param thang Tháng hiện tại.
     * @return Đối tượng ThongKeCardData đã tính toán xong.
     */
    public ThongKeCardData getDuLieuChoTheThongKe(int nam, int thang) {
       
        List<ThongKe_DAO.ThongKeThang> allData = getDoanhThuTheoThang(nam); 
        ThongKe_DAO.ThongKeThang hienTai = findThongKeByMonth(thang, allData);
        int thangTruoc = (thang == 1) ? 12 : thang - 1;
        ThongKe_DAO.ThongKeThang thangTruocData;
        if (thang == 1) {
            thangTruocData = new ThongKe_DAO.ThongKeThang(thangTruoc, 0, 0); 
        } else {
            thangTruocData = findThongKeByMonth(thangTruoc, allData);
        }
        
        ThongKeCardData result = new ThongKeCardData();
       
        result.dtHienTai = hienTai.tongDoanhThu;
        result.dtChenhLech = tinhPhanTramChenhLech(
            hienTai.tongDoanhThu, 
            thangTruocData.tongDoanhThu 
        );
        result.hdHienTai = hienTai.tonghoaDon;
        result.hdChenhLech = tinhPhanTramChenhLech(
            hienTai.tonghoaDon, 
            thangTruocData.tonghoaDon 
        );
        
        return result;
    }
    
    
    public double getDoanhThuHomNay() {
		ThongKe_DAO dao = new ThongKe_DAO();
		if (dao.getTongTienHomNay() != 0) {
			System.out.println("Doanh thu hom nay: " + dao.getTongTienHomNay());
			return dao.getTongTienHomNay();
		} 
		else { 
		 System.out.println("Khong co doanh thu hom nay");
		 return 0 ; 
		}
	}
    
    public int getTongBanDangSuDung() { 
    	ThongKe_DAO dao = new ThongKe_DAO();
    	if (dao.getTongBanDANGSUDUNG() != 0) {
    		System.out.println("Tong ban dang su dung: " + dao.getTongBanDANGSUDUNG());
			return dao.getTongBanDANGSUDUNG();
		} 
		else { 
		System.out.println("Khong co ban dang su dung");
		return 0 ;
    	}
    }
    
    public int getTongPhieuDangDat() { 
    	ThongKe_DAO dao = new ThongKe_DAO();
    	if (dao.getTongPhieuDangDatTruoc() != 0) {
    		System.out.println("Tong ban dang duoc dat: " + dao.getTongPhieuDangDatTruoc());
			return dao.getTongPhieuDangDatTruoc();
		} 
		else { 
		System.out.println("Khong co ban dang duoc dat");
		return 0 ;
    	}
    }

}

