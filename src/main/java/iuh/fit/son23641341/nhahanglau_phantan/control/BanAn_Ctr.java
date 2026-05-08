package iuh.fit.son23641341.nhahanglau_phantan.control;

import iuh.fit.son23641341.nhahanglau_phantan.dao.BanAn_DAO;
import iuh.fit.son23641341.nhahanglau_phantan.entity.BanAn;
import iuh.fit.son23641341.nhahanglau_phantan.util.EntityManagerFactoryUtil;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;

public class BanAn_Ctr {
    private static BanAn_Ctr instance;
    private BanAn_DAO banAnDAO;
    private EntityManager em;

    private BanAn_Ctr() {
        this.em = EntityManagerFactoryUtil.getEntityManager();
        this.banAnDAO = new BanAn_DAO(this.em);
        initializeDefaultTables(); // Tự động khởi tạo 40 bàn với các khu vực khác nhau
    }

    private void initializeDefaultTables() {
        ArrayList<BanAn> existing = layTatCaBan();
        
        // Sửa lỗi các bàn đã có nhưng bị thiếu khu vực
        for (BanAn ban : existing) {
            if (ban.getKhuVuc() == null || ban.getKhuVuc().isEmpty()) {
                int i = ban.getMaBan();
                String khuVuc = "Trong nhà";
                if (i > 15 && i <= 30) khuVuc = "Trên lầu";
                else if (i > 30) khuVuc = "Ngoài trời";
                ban.setKhuVuc(khuVuc);
                banAnDAO.mergeBanAn(ban);
            }
        }

        // Thêm bàn mới nếu tổng số bàn < 40
        if (existing.size() < 40) {
            System.out.println("=== INITIALIZING ADDITIONAL TABLES ===");
            for (int i = 1; i <= 40; i++) {
                if (timBanTheoMa(i) != null) continue;
                
                String khuVuc = "Trong nhà";
                if (i > 15 && i <= 30) khuVuc = "Trên lầu";
                else if (i > 30) khuVuc = "Ngoài trời";
                
                String loaiBan = "Thường";
                if (i % 5 == 0) loaiBan = "VIP";
                if (i % 8 == 0) loaiBan = "Deluxe";
                
                int soCho = (i % 2 == 0) ? 4 : 2;
                if (loaiBan.equals("VIP")) soCho = 6;
                
                BanAn ban = new BanAn(i, soCho, loaiBan, khuVuc);
                banAnDAO.mergeBanAn(ban);
            }
        }
    }

    public static synchronized BanAn_Ctr getInstance() {
        if (instance == null) instance = new BanAn_Ctr();
        return instance;
    }

    // Lấy dữ liệu tươi từ Database
    public ArrayList<BanAn> layTatCaBan() {
        return new ArrayList<>(banAnDAO.getAllBanAn());
    }

    public BanAn timBanTheoMa(int maBan) {
        return banAnDAO.findById(maBan);
    }

    public boolean themBan(BanAn banMoi) {
        // Kiểm tra tồn tại trước khi thêm
        if (timBanTheoMa(banMoi.getMaBan()) != null) return false;
        return banAnDAO.mergeBanAn(banMoi);
    }

    public boolean capNhatBan(BanAn banCapNhat) {
        return banAnDAO.mergeBanAn(banCapNhat);
    }

    public boolean xoaBan(int maBan) {
        return banAnDAO.deleteBanAn(maBan);
    }

    public ArrayList<BanAn> layBanTheoLoai(String loaiBan) {
        // Có thể viết thêm JPQL trong DAO để tối ưu, hoặc lọc tại stream như dưới đây
        return (ArrayList<BanAn>) banAnDAO.getAllBanAn().stream()
                .filter(b -> b.getLoaiBan().equalsIgnoreCase(loaiBan))
                .toList();
    }

    // Tương thích với GUI cũ
    public void loadBanFromDB() {
        // Với JPA, dữ liệu được query trực tiếp mỗi khi gọi layTatCaBan()
        // nên hàm này không cần thiết phải giữ list local nữa.
    }
}