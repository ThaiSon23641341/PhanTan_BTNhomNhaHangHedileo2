package iuh.fit.son23641341.nhahanglau_phantan.control;

import iuh.fit.son23641341.nhahanglau_phantan.dao.TimKiemChung_DAO;
import iuh.fit.son23641341.nhahanglau_phantan.entity.MonAn;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// NOTE: Controller now uses mock data; database logic removed.
public class TimKiemChung_Ctr {
    
    private TimKiemChung_DAO timKiemDao;

    public TimKiemChung_Ctr() {
        timKiemDao = new TimKiemChung_DAO();
    }

    /**
     * Phương thức thực hiện tìm kiếm món ăn, bao gồm cả việc lọc và sắp xếp.
     * @param keyword Từ khóa tìm kiếm.
     * @param searchByCode Lọc theo mã món ăn (true/false).
     * @param searchByOrder Lọc theo Mã phiếu đặt.
     * @param searchByBill Lọc theo Mã hóa đơn.
     * @param searchByCustomer Lọc theo Mã khách hàng.
     * @param searchByTable Lọc theo Mã bàn ăn.
     * @param sortAscending Lọc theo giá tăng dần (true) hoặc giảm dần (false). Null nếu không lọc theo giá.
     * @param newest Chỉ lấy món mới nhất (true/false).
     * @return Danh sách món ăn đã được tìm kiếm và lọc.
     */
    public List<MonAn> timKiemVaLocMonAn(String keyword, 
                                        boolean searchByCode, 
                                        boolean searchByOrder, 
                                        boolean searchByBill, 
                                        boolean searchByCustomer, 
                                        boolean searchByTable,
                                        Boolean sortAscending,
                                        boolean newest) throws Exception {
        
        // 1. Thực hiện tìm kiếm cơ bản qua DAO
        List<MonAn> ketQuaTimKiem = timKiemDao.timKiemMonAn(
            keyword, 
            searchByCode, 
            searchByOrder, 
            searchByBill, 
            searchByCustomer, 
            searchByTable
        );

        // 2. Thực hiện sắp xếp/lọc bổ sung (trên kết quả đã tìm kiếm)
        
        // Sắp xếp theo giá (Tăng dần/Giảm dần)
        if (sortAscending != null) {
            Comparator<MonAn> comparator = Comparator.comparing(MonAn::getGia);
            if (!sortAscending) {
                comparator = comparator.reversed();
            }
            Collections.sort(ketQuaTimKiem, comparator);
        }
        
        // TODO: Triển khai logic lọc "Món mới nhất" (nếu có trường ngày tạo)
        
        return ketQuaTimKiem;
    }
    
    public List<MonAn> getAllMonAn() throws Exception {
        return timKiemDao.getAllMonAn();
    }
}

