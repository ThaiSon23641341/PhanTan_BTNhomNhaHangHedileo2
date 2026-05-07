package iuh.fit.son23641341.nhahanglau_phantan.control;

import iuh.fit.son23641341.nhahanglau_phantan.dao.BanAn_DAO;
import iuh.fit.son23641341.nhahanglau_phantan.entity.BanAn;
import iuh.fit.son23641341.nhahanglau_phantan.util.EntityManagerFactoryUtil;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class BanAn_Ctr {
    private static BanAn_Ctr instance;
    private BanAn_DAO banAnDAO;
    private EntityManager em;

    private BanAn_Ctr() {
        this.em = EntityManagerFactoryUtil.getEntityManager();
        this.banAnDAO = new BanAn_DAO(this.em);
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