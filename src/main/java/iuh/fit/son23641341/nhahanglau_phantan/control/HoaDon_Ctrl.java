package iuh.fit.son23641341.nhahanglau_phantan.control;

import iuh.fit.son23641341.nhahanglau_phantan.dao.HoaDon_DAO;
import iuh.fit.son23641341.nhahanglau_phantan.entity.ChiTietDatMon;
import iuh.fit.son23641341.nhahanglau_phantan.entity.HoaDon;
import iuh.fit.son23641341.nhahanglau_phantan.entity.KhuyenMai;
import iuh.fit.son23641341.nhahanglau_phantan.entity.PhieuDatBan;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable; // Thay thế Table bằng PdfPTable
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        boolean luuThanhCong;

        iuh.fit.son23641341.nhahanglau_phantan.dao.HoaDon_DAO dao = new iuh.fit.son23641341.nhahanglau_phantan.dao.HoaDon_DAO();
        if (dao.isFunctional()) {
            luuThanhCong = dao.addHoaDon(hoaDonMoi);
        } else {
            // Client side
            iuh.fit.son23641341.nhahanglau_phantan.network.Request req =
                    new iuh.fit.son23641341.nhahanglau_phantan.network.Request("ADD_HOA_DON", hoaDonMoi);
            iuh.fit.son23641341.nhahanglau_phantan.network.Response res =
                    iuh.fit.son23641341.nhahanglau_phantan.network.ClientControl.getInstance().sendRequest(req);
            luuThanhCong = (res != null && res.getStatus().equals("SUCCESS"));
        }

        return luuThanhCong && xuatPDF(hoaDonMoi, duongDanPDF);
    }

    public boolean xuatPDF(HoaDon hoaDon, String filePath) {
        if (hoaDon == null || filePath == null) {
            return false;
        }

        try {
            // Tạo thư mục nếu chưa tồn tại
            Path directoryPath = Path.of(filePath).getParent();
            if (directoryPath != null && !Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            Document document = new Document();
            PdfWriter.getInstance(document, new java.io.FileOutputStream(filePath));
            document.open();

            // Font
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Font regularFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);

            // Tiêu đề
            Paragraph title = new Paragraph("HỆ DÌ LEO - NHÀ HÀNG LẨU", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph subtitle = new Paragraph("HÓA ĐƠN THANH TOÁN", boldFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subtitle);

            Paragraph dateTime = new Paragraph("Ngày: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), regularFont);
            dateTime.setAlignment(Element.ALIGN_CENTER);
            document.add(dateTime);

            document.add(new Paragraph("\n"));

            // Thông tin hóa đơn
            Paragraph info = new Paragraph();
            info.add("Mã hóa đơn: " + (hoaDon.getMaHoaDon() != null ? hoaDon.getMaHoaDon() : "N/A"));
            info.setFont(regularFont);
            document.add(info);

            if (hoaDon.getPhieuDat() != null) {
                Paragraph khachHang = new Paragraph("Khách hàng: " + (hoaDon.getPhieuDat().getTenKhachHang() != null ? hoaDon.getPhieuDat().getTenKhachHang() : "Khách vãng lai"), regularFont);
                document.add(khachHang);

                Paragraph ban = new Paragraph("Bàn: " + hoaDon.getPhieuDat().getMaBan(), regularFont);
                document.add(ban);
            }

            document.add(new Paragraph("\n"));

            // Bảng chi tiết - Sử dụng PdfPTable cho iText 5
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100); // Mở rộng bảng 100% chiều ngang

            // Cấu hình padding và viền mặc định cho tất cả các ô trong bảng
            table.getDefaultCell().setPadding(5);
            table.getDefaultCell().setBorderWidth(1);
            table.getDefaultCell().setBorderColor(new com.itextpdf.text.BaseColor(0, 0, 0));

            // Thiết lập tỷ lệ độ rộng các cột (Ví dụ: Tên món rộng hơn các cột khác)
            float[] columnWidths = {4f, 1.5f, 2f, 2.5f};
            table.setWidths(columnWidths);

            // Thêm Header
            table.addCell(new Phrase("Món ăn", boldFont));
            table.addCell(new Phrase("S.Lượng", boldFont));
            table.addCell(new Phrase("Đơn giá", boldFont));
            table.addCell(new Phrase("Thành tiền", boldFont));

            double tongTien = 0;
            if (hoaDon.getPhieuDat() != null && hoaDon.getPhieuDat().getDanhSachMonAn() != null) {
                for (ChiTietDatMon ct : hoaDon.getPhieuDat().getDanhSachMonAn()) {
                    table.addCell(new Phrase(ct.getMonAn().getTenMon(), regularFont));
                    table.addCell(new Phrase(String.valueOf(ct.getSoLuong()), regularFont));
                    table.addCell(new Phrase(String.format("%,.0f", ct.getMonAn().getGia()), regularFont));
                    double thanhTien = ct.getMonAn().getGia() * ct.getSoLuong();
                    table.addCell(new Phrase(String.format("%,.0f", thanhTien), regularFont));
                    tongTien += thanhTien;
                }
            }

            document.add(table);
            document.add(new Paragraph("\n"));

            // Tổng cộng
            Paragraph total = new Paragraph();
            total.add(new Phrase("TỔNG TIỀN: ", boldFont));
            total.add(new Phrase(String.format("%,.0f VNĐ", hoaDon.getTongTien() > 0 ? hoaDon.getTongTien() : tongTien), boldFont));
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            document.add(new Paragraph("\n\n"));

            // Cảm ơn
            Paragraph footer = new Paragraph("Cảm ơn quý khách!\nRất mong được phục vụ quý khách lần tới!", regularFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
            return true;
        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) { // Bắt thêm IOException do Files.createDirectories()
            e.printStackTrace();
            return false;
        }
    }
}