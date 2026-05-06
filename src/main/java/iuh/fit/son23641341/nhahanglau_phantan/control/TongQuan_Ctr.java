package iuh.fit.son23641341.nhahanglau_phantan.control;

import iuh.fit.son23641341.nhahanglau_phantan.dao.TongQuan_DAO;

// NOTE: Controller now uses mock data; database logic removed.
public class TongQuan_Ctr {
    
    private TongQuan_DAO tongQuanDAO;

    public TongQuan_Ctr() {
        this.tongQuanDAO = new TongQuan_DAO();
    }
    
    /**
     * Lấy số lượng Khuyến mãi hiện tại.
     */
    public int laySoKhuyenMaiHienTai() {
        return tongQuanDAO.getSoKhuyenMai();
    }
    
    /**
     * Lấy số lượng Bàn đang được đặt trước.
     */
    public int laySoBanDangDat() {
        return tongQuanDAO.getSoBanDangDat();
    }
    
    /**
     * Lấy tổng số Nhân viên.
     */
    public int laySoNhanVien() {
        return tongQuanDAO.getSoNhanVien();
    }
    
    /**
     * Lấy số lượng Bàn đang được sử dụng (Đang phục vụ).
     */
    public int laySoBanDangSuDung() {
        return tongQuanDAO.getSoBanDangSuDung();
    }
    
    /**
     * Lấy tất cả số liệu thống kê dưới dạng mảng int.
     * Thứ tự: [Số KM, Số Bàn Đang Đặt, Số NV, Số Bàn Đang SD]
     * @return Mảng chứa 4 số liệu thống kê.
     */
    public int[] layTatCaSoLieu() {
        return new int[] {
            laySoKhuyenMaiHienTai(),
            laySoBanDangDat(),
            laySoNhanVien(),
            laySoBanDangSuDung()
        };
    }
}

