package iuh.fit.son23641341.nhahanglau_phantan.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.regex.Pattern;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "khuyen_mai")
public class KhuyenMai implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    @Transient
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ROOT);
    @Id
    @Column(name = "ma_khuyen_mai")
    private String maKhuyenMai;
    
    @Column(name = "ten_khuyen_mai")
    private String tenKhuyenMai;
    
    @Column(name = "phan_tram_giam")
    private double phanTramGiam;
    
    @Column(name = "ngay_bat_dau")
    private LocalDate ngayBatDau;
    
    @Column(name = "ngay_ket_thuc")
    private LocalDate ngayKetThuc;
    
    @Column(name = "mo_ta")
    private String moTa;

    public KhuyenMai() {
    }

    public KhuyenMai(KhuyenMai km) {
        this(km.maKhuyenMai, km.tenKhuyenMai, km.phanTramGiam, km.ngayBatDau, km.ngayKetThuc, km.moTa);
    }

    public KhuyenMai(String maKhuyenMai, String tenKhuyenMai, double phanTramGiam,
                     String ngayBatDau, String ngayKetThuc, String moTa) {
        setMaKhuyenMai(maKhuyenMai);
        setTenKhuyenMai(tenKhuyenMai);
        setPhanTramGiam(phanTramGiam);
        setNgayBatDau(ngayBatDau);
        setNgayKetThuc(ngayKetThuc);
        setMoTa(moTa);
    }

    public KhuyenMai(String maKhuyenMai, String tenKhuyenMai, double phanTramGiam,
                     LocalDate ngayBatDau, LocalDate ngayKetThuc, String moTa) {
        setMaKhuyenMai(maKhuyenMai);
        setTenKhuyenMai(tenKhuyenMai);
        setPhanTramGiam(phanTramGiam);
        setNgayBatDau(ngayBatDau);
        setNgayKetThuc(ngayKetThuc);
        setMoTa(moTa);
    }

    public String getMaKhuyenMai() {
        return maKhuyenMai;
    }

    public void setMaKhuyenMai(String maKhuyenMai) {
        if (maKhuyenMai == null || maKhuyenMai.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã khuyến mãi không được rỗng.");
        }
        this.maKhuyenMai = maKhuyenMai;
    }

    public String getTenKhuyenMai() {
        return tenKhuyenMai;
    }

    public void setTenKhuyenMai(String tenKhuyenMai) {
        if (tenKhuyenMai == null || tenKhuyenMai.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên khuyến mãi không được rỗng.");
        }
        this.tenKhuyenMai = tenKhuyenMai;
    }

    public double getPhanTramGiam() {
        return phanTramGiam;
    }

    public void setPhanTramGiam(double phanTramGiam) {
        if (phanTramGiam < 0 || phanTramGiam > 100) {
            throw new IllegalArgumentException("Phần trăm giảm phải nằm trong khoảng 0 đến 100.");
        }
        this.phanTramGiam = phanTramGiam;
    }

    public LocalDate getNgayBatDau() {
        return ngayBatDau;
    }

    public String getNgayBatDauFormatted() {
        return ngayBatDau != null ? ngayBatDau.format(DATE_FORMATTER) : "";
    }

    public void setNgayBatDau(LocalDate ngayBatDau) {
        if (ngayBatDau == null) {
            throw new IllegalArgumentException("Ngày bắt đầu không hợp lệ.");
        }
        this.ngayBatDau = ngayBatDau;
    }

    public void setNgayBatDau(String ngayBatDau) {
        this.ngayBatDau = parseNgay(ngayBatDau, "Ngày bắt đầu không hợp lệ.");
    }

    public LocalDate getNgayKetThuc() {
        return ngayKetThuc;
    }

    public String getNgayKetThucFormatted() {
        return ngayKetThuc != null ? ngayKetThuc.format(DATE_FORMATTER) : "";
    }

    public void setNgayKetThuc(LocalDate ngayKetThuc) {
        if (ngayKetThuc == null) {
            throw new IllegalArgumentException("Ngày kết thúc không hợp lệ.");
        }
        if (this.ngayBatDau != null && ngayKetThuc.isBefore(this.ngayBatDau)) {
            throw new IllegalArgumentException("Ngày kết thúc phải sau hoặc bằng ngày bắt đầu.");
        }
        this.ngayKetThuc = ngayKetThuc;
    }

    public void setNgayKetThuc(String ngayKetThuc) {
        LocalDate parsed = parseNgay(ngayKetThuc, "Ngày kết thúc không hợp lệ.");
        if (this.ngayBatDau != null && parsed.isBefore(this.ngayBatDau)) {
            throw new IllegalArgumentException("Ngày kết thúc phải sau hoặc bằng ngày bắt đầu.");
        }
        this.ngayKetThuc = parsed;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    // ------------------- Hàm hỗ trợ kiểm tra định dạng -------------------
    private static boolean isValidDateFormat(String dateStr) {
        String regex = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/(19|20)\\d{2}$";
        return Pattern.matches(regex, dateStr);
    }

    private static LocalDate parseNgay(String dateStr, String errorMessage) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
        try {
            return LocalDate.parse(dateStr.trim(), DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Ngày không hợp lệ. Định dạng đúng là dd/MM/yyyy (ví dụ: 15/10/2025).");
        }
    }

    // ------------------- Phương thức nghiệp vụ -------------------
    @Override
    public String toString() {
        return String.format("KhuyenMai[ma=%s, ten=%s, giam=%.1f%%, batDau=%s, ketThuc=%s, moTa=%s]",
                maKhuyenMai, tenKhuyenMai, phanTramGiam, getNgayBatDauFormatted(), getNgayKetThucFormatted(), moTa);
    }
}
