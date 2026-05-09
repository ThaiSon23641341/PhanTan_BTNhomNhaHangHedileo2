package iuh.fit.son23641341.nhahanglau_phantan.control;

import iuh.fit.son23641341.nhahanglau_phantan.dao.TongQuan_DAO;

// NOTE: Controller now uses mock data; database logic removed.
public class TongQuan_Ctr {
    
    private TongQuan_DAO tongQuanDAO;

    public TongQuan_Ctr() {
        try {
            this.tongQuanDAO = new TongQuan_DAO();
        } catch (Exception e) {
            System.err.println("TongQuan_DAO initialization failed: " + e.getMessage());
        }
    }
    
    /**
     * Lấy số lượng Khuyến mãi hiện tại.
     */
    public int laySoKhuyenMaiHienTai() {
        return tongQuanDAO != null ? tongQuanDAO.getSoKhuyenMai() : 0;
    }
    
    /**
     * Lấy số lượng Bàn đang được đặt trước.
     */

    
    /**
     * Lấy tổng số Nhân viên.
     */
    public int laySoNhanVien() {
        return tongQuanDAO != null ? tongQuanDAO.getSoNhanVien() : 0;
    }
    
    /**
     * Lấy số lượng Bàn đang được sử dụng (Đang phục vụ).
     */
    /**
     * Lấy tất cả số liệu thống kê dưới dạng mảng int.
     * Thứ tự: [Số KM, Số Bàn Đang Đặt, Số NV, Số Bàn Đang SD]
     * @return Mảng chứa 4 số liệu thống kê.
     */
    public int[] layTatCaSoLieu() {
        jakarta.persistence.EntityManager em = iuh.fit.son23641341.nhahanglau_phantan.util.EntityManagerFactoryUtil.getEntityManager();
        if (em != null && tongQuanDAO != null) {
            System.out.println(">>> TongQuan_Ctr: Lấy số liệu trực tiếp từ DB (Server Mode)");
            return new int[] {
                laySoKhuyenMaiHienTai(),
                0, // Placeholder cho Số Bàn Đang Đặt (sẽ được tính ở GUI)
                laySoNhanVien(),
                0  // Placeholder cho Số Bàn Đang SD
            };
        } else {
            // Client side
            System.out.println(">>> TongQuan_Ctr: Gửi yêu cầu lấy số liệu tổng quan tới Server (Client Mode)");
            iuh.fit.son23641341.nhahanglau_phantan.network.Request req = 
                new iuh.fit.son23641341.nhahanglau_phantan.network.Request("GET_TONG_QUAN_SOLIEU", null);
            iuh.fit.son23641341.nhahanglau_phantan.network.Response res = 
                iuh.fit.son23641341.nhahanglau_phantan.network.ClientControl.getInstance().sendRequest(req);
            
            if (res.getStatus().equals("SUCCESS") && res.getData() instanceof int[]) {
                return (int[]) res.getData();
            } else {
                System.err.println(">>> TongQuan_Ctr ERROR: " + res.getMessage());
                return new int[]{0, 0, 0, 0};
            }
        }
    }
}

