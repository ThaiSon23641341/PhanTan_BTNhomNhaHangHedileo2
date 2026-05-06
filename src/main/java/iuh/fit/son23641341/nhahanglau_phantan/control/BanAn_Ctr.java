package iuh.fit.son23641341.nhahanglau_phantan.control;

import iuh.fit.son23641341.nhahanglau_phantan.entity.BanAn;
import iuh.fit.son23641341.nhahanglau_phantan.mock.MockData;
import java.util.ArrayList;

// NOTE: Controller now uses mock data; database logic removed.
public class BanAn_Ctr {
    private static BanAn_Ctr instance; // Singleton instance
    private ArrayList<BanAn> danhSachBan;

    // Private constructor để ngăn tạo instance từ bên ngoài
    private BanAn_Ctr() {
        danhSachBan = new ArrayList<>(MockData.banAns());
    }
    
    // Lấy instance duy nhất
    public static BanAn_Ctr getInstance() {
        if (instance == null) {
            instance = new BanAn_Ctr();
        }
        return instance;
    }

    // Load dữ liệu bàn từ database
    // Public để có thể gọi lại khi cần đồng bộ bộ nhớ với DB
    public void loadBanFromDB() {
        danhSachBan = new ArrayList<>(MockData.banAns());
    }

    // 1. Lấy tất cả bàn
    public ArrayList<BanAn> layTatCaBan() {
        return danhSachBan;
    }

    // 2. Tìm bàn theo mã
    public BanAn timBanTheoMa(int maBan) {
        for (BanAn ban : danhSachBan) {
            if (ban.getMaBan() == maBan) {
                return ban;
            }
        }
        return null; // Không tìm thấy
    }

    // 4. Thêm bàn mới
    public boolean themBan(BanAn banMoi) {
        // Kiểm tra mã bàn đã tồn tại chưa
        if (timBanTheoMa(banMoi.getMaBan()) != null) {
            return false; // Đã tồn tại
        }
        MockData.banAns().add(banMoi);
        return danhSachBan.add(banMoi);
    }

    // 5. Cập nhật thông tin bàn
    public boolean capNhatBan(BanAn banCapNhat) {
        BanAn ban = timBanTheoMa(banCapNhat.getMaBan());
        if (ban == null) {
            return false; // Không tìm thấy bàn
        }

        try {
            ban.setSoCho(banCapNhat.getSoCho());
            ban.setTrangThai(banCapNhat.getTrangThai());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 6. Cập nhật trạng thái bàn
    public boolean capNhatTrangThai(int maBan, String trangThaiMoi) {
        System.out.println(trangThaiMoi);
        BanAn ban = timBanTheoMa(maBan);
        if (ban == null) {
            return false;
        }

        try {
            // Cập nhật trong memory
            ban.capNhatTrangThai(trangThaiMoi);
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 7. Xóa bàn
    public boolean xoaBan(int maBan) {
        BanAn ban = timBanTheoMa(maBan);
        if (ban == null) {
            return false;
        }
        MockData.banAns().remove(ban);
        return danhSachBan.remove(ban);
    }

    // 8. Đếm số bàn theo trạng thái
    public int demBanTheoTrangThai(String trangThai) {
        int dem = 0;
        for (BanAn ban : danhSachBan) {
            if (ban.getTrangThai().equals(trangThai)) {
                dem++;
            }
        }
        return dem;
    }

    // 9. Lấy tổng số bàn
    public int layTongSoBan() {
        return danhSachBan.size();
    }

    // 10. Kiểm tra bàn có trống không
    public boolean kiemTraBanTrong(int maBan) {
        BanAn ban = timBanTheoMa(maBan);
        return ban != null && ban.getTrangThai().equals("Trống");
    }

    // 11. Lấy danh sách bàn theo loại
    public ArrayList<BanAn> layBanTheoLoai(String loaiBan) {
        ArrayList<BanAn> ketQua = new ArrayList<>();
        for (BanAn ban : danhSachBan) {
            if (ban.getLoaiBan().equals(loaiBan)) {
                ketQua.add(ban);
            }
        }
        return ketQua;
    }

    // 12. Đếm số bàn theo loại
    public int demBanTheoLoai(String loaiBan) {
        int dem = 0;
        for (BanAn ban : danhSachBan) {
            if (ban.getLoaiBan().equals(loaiBan)) {
                dem++;
            }
        }
        return dem;
    }
}

