package iuh.fit.son23641341.nhahanglau_phantan.dao;

import iuh.fit.son23641341.nhahanglau_phantan.entity.HoaDon;
import iuh.fit.son23641341.nhahanglau_phantan.entity.MonAn;
import iuh.fit.son23641341.nhahanglau_phantan.mock.MockData;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

// NOTE: Database logic removed; DAO now uses in-memory mock data.
public class ThongKe_DAO {
    public static class ThongKeNgay {
        public LocalDate ngay;
        public double tongDoanhThu;
        public int tongHoaDon;

        public ThongKeNgay(LocalDate ngay, double tongDoanhThu, int tongHoaDon) {
            this.ngay = ngay;
            this.tongDoanhThu = tongDoanhThu;
            this.tongHoaDon = tongHoaDon;
        }
    }

    public static class ThongKeThang {
        public int thang;
        public double tongDoanhThu;
        public int tonghoaDon;

        public ThongKeThang(int thang, double tongDoanhThu, int tonghoaDon) {
            this.thang = thang;
            this.tongDoanhThu = tongDoanhThu;
            this.tonghoaDon = tonghoaDon;
        }
    }

    public static class TopMonAn {
        public String tenMonAn;
        public int soLuongDat;

        public TopMonAn(String tenMonAn, int soLuongDat) {
            this.tenMonAn = tenMonAn;
            this.soLuongDat = soLuongDat;
        }

        @Override
        public String toString() {
            return tenMonAn + " - " + soLuongDat + " lượt";
        }
    }

    public ThongKeNgay getThongKeTheoNgay(LocalDate ngay) {
        double doanhThu = 0;
        int hoaDon = 0;
        for (HoaDon hd : MockData.hoaDons()) {
            if (hd.getNgayLap() != null && hd.getNgayLap().toLocalDate().equals(ngay)) {
                doanhThu += hd.getTongTien();
                hoaDon++;
            }
        }
        return new ThongKeNgay(ngay, doanhThu, hoaDon);
    }

    public ArrayList<TopMonAn> getTopMonAnTheoNgay(LocalDate ngay, int limit) {
        return getTopMonAnTheoThang(ngay.getMonthValue(), ngay.getYear(), limit);
    }

    public ArrayList<ThongKeThang> getDoanhThuTheoThangTrongNam(int nam) {
        ArrayList<ThongKeThang> danhSachThang = new ArrayList<>();
        for (int thang = 1; thang <= 12; thang++) {
            YearMonth yearMonth = YearMonth.of(nam, thang);
            double doanhThu = 0;
            int hoaDon = 0;
            for (HoaDon hd : MockData.hoaDons()) {
                if (hd.getNgayLap() != null && YearMonth.from(hd.getNgayLap().toLocalDate()).equals(yearMonth)) {
                    doanhThu += hd.getTongTien();
                    hoaDon++;
                }
            }
            danhSachThang.add(new ThongKeThang(thang, doanhThu, hoaDon));
        }
        return danhSachThang;
    }

    public ArrayList<TopMonAn> getTopMonAnTheoThang(int thang, int nam, int limit) {
        ArrayList<TopMonAn> topMonAnList = new ArrayList<>();
        List<MonAn> monAns = MockData.monAnsSnapshot();
        for (int i = 0; i < Math.min(limit, monAns.size()); i++) {
            MonAn mon = monAns.get(i);
            topMonAnList.add(new TopMonAn(mon.getTenMon(), (limit - i) * 3));
        }
        return topMonAnList;
    }

    public double getTongTienHomNay() {
        LocalDate today = LocalDate.now();
        return getThongKeTheoNgay(today).tongDoanhThu;
    }

    public int getTongPhieuDangDatTruoc() {
        return (int) MockData.phieuDats().stream()
            .filter(phieu -> "Đặt trước".equals(phieu.getTrangThai()))
            .count();
    }

    public int getTongBanDANGSUDUNG() {
        return (int) MockData.banAns().stream()
            .filter(ban -> "Đang sử dụng".equals(ban.getTrangThai()))
            .count();
    }
}

