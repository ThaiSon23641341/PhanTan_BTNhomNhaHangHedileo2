package iuh.fit.son23641341.nhahanglau_phantan.control;

import iuh.fit.son23641341.nhahanglau_phantan.dao.HoaDon_DAO;
import iuh.fit.son23641341.nhahanglau_phantan.entity.ChiTietDatMon;
import iuh.fit.son23641341.nhahanglau_phantan.entity.HoaDon;
import iuh.fit.son23641341.nhahanglau_phantan.entity.KhuyenMai;
import iuh.fit.son23641341.nhahanglau_phantan.entity.PhieuDatBan;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

// NOTE: Database/PDF export logic removed; controller now uses mock data and placeholder output.
public class HoaDon_Ctrl {

    public double tinhTongTienMonAn(HoaDon hoaDon) {
        double tong = 0;
        if (hoaDon.getPhieuDat() != null && hoaDon.getPhieuDat().getDanhSachMonAn() != null) {
            for (ChiTietDatMon ct : hoaDon.getPhieuDat().getDanhSachMonAn()) {
                tong += ct.getMonAn().getGia() * ct.getSoLuong();
            }
        }
        return tong;
    }

    public double tinhTienGiamGia(double tongTienMon, KhuyenMai khuyenMai) {
        if (khuyenMai != null) {
            return tongTienMon * (khuyenMai.getPhanTramGiam() / 100.0);
        }
        return 0;
    }

    public double layTienGiamGiaTuPhieu(HoaDon hoaDon) {
        if (hoaDon.getPhieuDat() != null) {
            return hoaDon.getPhieuDat().getGiamGia();
        }
        return 0;
    }

    public double tinhTongThanhToan(double tongTienMon, double tienGiam, double tienCoc) {
        double tong = tongTienMon - tienGiam - tienCoc;
        return (tong < 0) ? 0 : tong;
    }

    public void capNhatTongTienHoaDon(HoaDon hoaDon, double tongCuoi) {
        hoaDon.setTongTien(tongCuoi);
    }

    public boolean taoHoaDonVaXuatPDF(PhieuDatBan phieuDat, String duongDanPDF) {
        if (phieuDat == null) {
            return false;
        }
        HoaDon hoaDonMoi = new HoaDon(phieuDat);
        boolean luuThanhCong = new HoaDon_DAO().addHoaDon(hoaDonMoi);
        return luuThanhCong && xuatPDF(hoaDonMoi, duongDanPDF);
    }

    public boolean xuatPDF(HoaDon hoaDon, String filePath) {
        if (hoaDon == null || filePath == null) {
            return false;
        }
        String noiDung = "HÓA ĐƠN (DỮ LIỆU MẪU)\n"
            + "Mã hóa đơn: " + hoaDon.getMaHoaDon() + "\n"
            + "Tổng tiền: " + hoaDon.getTongTien() + "\n";
        try {
            Files.writeString(Path.of(filePath), noiDung);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
