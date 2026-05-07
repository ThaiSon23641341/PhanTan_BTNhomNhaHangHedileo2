package iuh.fit.son23641341.nhahanglau_phantan.control;

import iuh.fit.son23641341.nhahanglau_phantan.dao.ThongKe_DAO;
import iuh.fit.son23641341.nhahanglau_phantan.util.EntityManagerFactoryUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ThongKe_Ctr {
    private static ThongKe_Ctr instance;
    private ThongKe_DAO thongKeDAO;

    private ThongKe_Ctr() {
        this.thongKeDAO = new ThongKe_DAO(EntityManagerFactoryUtil.getEntityManager());
    }

    public static synchronized ThongKe_Ctr getInstance() {
        if (instance == null) instance = new ThongKe_Ctr();
        return instance;
    }

    public double getTongTienHomNay() {
        return thongKeDAO.getTongTienHomNay();
    }

    public long getTongPhieuDangDat() {
        return thongKeDAO.getSoPhieuDangDat();
    }


    public ThongKeCardData getDuLieuChoTheThongKe(int nam, int thang) {
        ArrayList<ThongKe_DAO.ThongKeThang> allData = thongKeDAO.getDoanhThuTheoThangTrongNam(nam);
        ThongKe_DAO.ThongKeThang hienTai = allData.get(thang - 1);
        ThongKe_DAO.ThongKeThang truocData = (thang > 1) ? allData.get(thang - 2) : new ThongKe_DAO.ThongKeThang(0, 0, 0);

        ThongKeCardData res = new ThongKeCardData();
        res.dtHienTai = hienTai.tongDoanhThu;
        res.dtChenhLech = tinhPhanTram(hienTai.tongDoanhThu, truocData.tongDoanhThu);
        res.hdHienTai = (int) hienTai.tonghoaDon;
        res.hdChenhLech = tinhPhanTram(hienTai.tonghoaDon, truocData.tonghoaDon);
        return res;
    }

    public ArrayList<ThongKe_DAO.TopMonAn> getTopMonAn(int t, int n, int l) {
        return thongKeDAO.getTopMonAnTheoThang(t, n, l);
    }


    // Fix lỗi: getDuLieuChoTheThongKeTheoNgay
    public ThongKeCardData getDuLieuChoTheThongKeTheoNgay(LocalDate ngay) {
        double dtHienTai = thongKeDAO.getDoanhThuNgay(ngay);
        double dtHomTruoc = thongKeDAO.getDoanhThuNgay(ngay.minusDays(1));

        long hdHienTai = thongKeDAO.getSoHoaDonNgay(ngay);
        long hdHomTruoc = thongKeDAO.getSoHoaDonNgay(ngay.minusDays(1));

        ThongKeCardData res = new ThongKeCardData();
        res.dtHienTai = dtHienTai;
        res.dtChenhLech = tinhPhanTram(dtHienTai, dtHomTruoc);
        res.hdHienTai = (int) hdHienTai;
        res.hdChenhLech = tinhPhanTram(hdHienTai, hdHomTruoc);
        return res;
    }

    // Fix lỗi: getTopMonAnTheoNgay
    public ArrayList<ThongKe_DAO.TopMonAn> getTopMonAnTheoNgay(LocalDate ngay, int limit) {
        return thongKeDAO.getTopMonAnTheoNgay(ngay, limit);
    }

    // Fix lỗi: TinhTongDoanhThuNam
    public double TinhTongDoanhThuNam(int nam) {
        return thongKeDAO.getDoanhThuNam(nam);
    }

    // Fix lỗi: TinhDoanhSoTrungBinhNgay (trong năm)
    public double TinhDoanhSoTrungBinhNgay(int nam) {
        double tong = TinhTongDoanhThuNam(nam);
        return tong / (LocalDate.of(nam, 1, 1).isLeapYear() ? 366 : 365);
    }

    // Fix lỗi: TinhDoanhSoTrungBinhHoaDon (trong năm)
    public double TinhDoanhSoTrungBinhHoaDon(int nam) {
        double tongDT = thongKeDAO.getDoanhThuNam(nam);
        long tongHD = thongKeDAO.getSoHoaDonNam(nam);
        return tongHD == 0 ? 0 : tongDT / tongHD;
    }

    // Fix lỗi: TinhTongDoanhThuNgay
    public double TinhTongDoanhThuNgay(LocalDate ngay) {
        return thongKeDAO.getDoanhThuNgay(ngay);
    }

    // Fix lỗi: TinhDoanhSoTrungBinhHoaDonTheoNgay
    public double TinhDoanhSoTrungBinhHoaDonTheoNgay(LocalDate ngay) {
        double tongDT = thongKeDAO.getDoanhThuNgay(ngay);
        long tongHD = thongKeDAO.getSoHoaDonNgay(ngay);
        return tongHD == 0 ? 0 : tongDT / tongHD;
    }

    // Fix lỗi: getDoanhThuTheoThang (Dùng cho biểu đồ)
    public ArrayList<ThongKe_DAO.ThongKeThang> getDoanhThuTheoThang(int nam) {
        return thongKeDAO.getDoanhThuTheoThangTrongNam(nam);
    }

    // Helper tính toán %
    private String tinhPhanTram(double hienTai, double truoc) {
        if (truoc <= 0) return hienTai > 0 ? "▲ N/A" : "---";
        double change = ((hienTai - truoc) / truoc) * 100;
        return (change >= 0 ? "▲ " : "▼ ") + String.format("%.1f%%", Math.abs(change));
    }

    public static class ThongKeCardData {
        public double dtHienTai;
        public String dtChenhLech;
        public int hdHienTai;
        public String hdChenhLech;
    }
}