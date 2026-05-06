package iuh.fit.son23641341.nhahanglau_phantan.control;

import iuh.fit.son23641341.nhahanglau_phantan.entity.MonAn;
import iuh.fit.son23641341.nhahanglau_phantan.mock.MockData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
/**
 * Lớp điều khiển (Controller) quản lý danh sách các đối tượng {@link MonAn}.
 * Cung cấp các thao tác CRUD (Create, Read, Update, Delete) cho món ăn.
 * Dữ liệu được lưu trữ tạm thời trong một ArrayList.
 *
 * @author Tên tác giả (nếu có)
 * @version 1.0
 * @see MonAn
 */
// NOTE: Controller now uses mock data; database logic removed.
public class MonAn_Ctr {
    private final ArrayList<MonAn> danhSachMonAn;

    /**
     * Khởi tạo một đối tượng MonAn_Ctr mới.
     * Khởi tạo danh sách món ăn và nạp dữ liệu mẫu.
     */
    public MonAn_Ctr() {
        this.danhSachMonAn = new ArrayList<>(MockData.monAns());
    }

    /**
     * Trả về danh sách tất cả các món ăn hiện có.
     *
     * @return {@code ArrayList<MonAn>} chứa tất cả món ăn.
     */
    public ArrayList<MonAn> getDanhSachMonAn() {
        return danhSachMonAn;
    }

    /**
     * Khởi tạo dữ liệu mẫu cho danh sách món ăn.
     * Dữ liệu được thêm vào danh sách và xử lý lỗi {@code Exception} do validation (nếu có).
     */
    private void khoiTaoDuLieuMau() {
        danhSachMonAn.clear();
        danhSachMonAn.addAll(MockData.monAns());
    }


    /**
     * Tạo mã món tiếp theo theo định dạng MAxxx, dựa trên số lớn nhất hiện có.
     * Ví dụ: nếu max là MA004, mã tiếp theo là MA005.
     *
     * @return {@code String} mã món ăn mới.
     */
		 private String taoMaTiepTheo() {
			    if (danhSachMonAn == null || danhSachMonAn.isEmpty()) {
			      
			        return String.format("MA%03d", 1 + new Random().nextInt(1000));
			    }

			    int max = danhSachMonAn.stream()
			            .map(mon -> mon.getMaMon().substring(2)) 
			            .mapToInt(Integer::parseInt)           
			            .max()                                  
			            .orElse(0);

			   
			    int randomXxx = new Random().nextInt(1000); 

			    return String.format("MA%03d", max + 1 + randomXxx);
			}

    // CREATE
    /**
     * Thêm một món ăn mới vào danh sách.
     * Nếu mã món trong đối tượng {@code mon} rỗng hoặc null, một mã mới sẽ được tạo tự động.
     * Nếu mã món đã tồn tại, thao tác thêm sẽ thất bại.
     *
     * @param mon Đối tượng {@link MonAn} cần thêm.
     * @return {@code true} nếu thêm thành công, {@code false} nếu thất bại (ví dụ: {@code mon} null, trùng mã, hoặc lỗi validation khi tạo mã).
     */
    
    
    
    public boolean themMonAn(MonAn mon) {
        if (mon == null) return false;

        // Nếu mã rỗng -> tự tạo; nếu có -> kiểm tra trùng
        String ma = mon.getMaMon();
        if (ma == "MA00" || ma.trim().isEmpty()) {
            try {
                mon.setMaMon(taoMaTiepTheo());
            } catch (Exception e) {
                return false; // Lỗi khi set mã tự động
            }
        } else {
            if (timTheoMa(ma) != null) return false; // trùng mã
        }
        MockData.monAns().add(mon);
        return danhSachMonAn.add(mon);
    }

    // READ
    /**
     * Tìm một món ăn theo mã món.
     * Việc so sánh mã không phân biệt chữ hoa/chữ thường.
     *
     * @param maMon Mã món ăn cần tìm.
     * @return Đối tượng {@link MonAn} nếu tìm thấy, {@code null} nếu không tìm thấy hoặc {@code maMon} là null.
     */
    public MonAn timTheoMa(String maMon) {
        if (maMon == null) return null;
        String target = maMon.trim();
        for (MonAn m : danhSachMonAn) {
            if (target.equalsIgnoreCase(m.getMaMon())) return m;
        }
        return null;
    }

    /**
     * Tìm kiếm các món ăn có tên chứa một chuỗi truy vấn (query).
     * Việc tìm kiếm không phân biệt chữ hoa/chữ thường và có thể tìm kiếm gần đúng.
     * Nếu {@code query} rỗng hoặc null, trả về danh sách rỗng (tránh trả về toàn bộ danh sách ở đây để GUI quyết định).
     *
     * @param query Chuỗi cần tìm kiếm trong tên món.
     * @return {@code ArrayList<MonAn>} chứa các món ăn phù hợp.
     */
    public List<MonAn> timTheoTen(String query) {
        ArrayList<MonAn> kq = new ArrayList<>();
        if (query == null) return kq;
        String q = query.trim().toLowerCase(Locale.ROOT);
        
        if (q.isEmpty()) {
            // Khi query rỗng, trả về tất cả
            kq.addAll(danhSachMonAn);
            return kq;
        }
        
        for (MonAn m : danhSachMonAn) {
            String ten = m.getTenMon();
            if (ten != null && ten.toLowerCase(Locale.ROOT).contains(q)) {
                kq.add(m);
            }
        }
        return kq;
    }

    // UPDATE theo mã
    /**
     * Cập nhật thông tin cho món ăn dựa trên mã món.
     *
     * @param maMon Mã món ăn cần cập nhật.
     * @param tenMon Tên mới.
     * @param loaiMon Loại món mới.
     * @param gia Giá mới.
     * @param moTa Mô tả mới.
     * @return {@code true} nếu cập nhật thành công, {@code false} nếu không tìm thấy món ăn hoặc xảy ra lỗi validation.
     */
    public boolean capNhatMonAn(String maMon, String tenMon, String loaiMon, double gia, String moTa) {
        MonAn m = timTheoMa(maMon);
        if (m == null) return false;
        try {
            m.setTenMon(tenMon);
            m.setLoaiMon(loaiMon);
            m.setGia(gia);
            m.setMoTa(moTa);
			return true;
        } catch (Exception e) {
            return false;
        }
    }

    // DELETE theo mã
    /**
     * Xóa một món ăn khỏi danh sách dựa trên mã món.
     *
     * @param maMon Mã món ăn cần xóa.
     * @return {@code true} nếu xóa thành công, {@code false} nếu không tìm thấy món ăn.
     */
    public boolean xoaMonAn(String maMon) {
        MonAn m = timTheoMa(maMon);
        if (m == null) return false;
        MockData.monAns().removeIf(item -> item.getMaMon().equalsIgnoreCase(maMon));
        return danhSachMonAn.remove(m);
    }
    
    
    

    
}


	 

