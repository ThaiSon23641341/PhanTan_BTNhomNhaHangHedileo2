package iuh.fit.son23641341.nhahanglau_phantan.entity;

import lombok.Builder;

@Builder

public class BanAn {
    private int maBan;
    private int soCho;
    private String trangThai;
    private String loaiBan; // Thường, VIP, Deluxe

    // Constructor mặc định
    public BanAn() {
        this.maBan = 0;
        this.soCho = 0;
        this.trangThai = "Trống";
        this.loaiBan = "Thường";
    }

    // Constructor đầy đủ tham số (tương thích ngược)
    public BanAn(int maBan, int soCho, String trangThai) {
        this.maBan = (maBan > 0) ? maBan : 1;
        this.soCho = (soCho > 0) ? soCho : 2; 
        
        // Kiểm tra trạng thái
        if (trangThai.equals("Trống") || 
            trangThai.equals("Đang sử dụng") || 
            trangThai.equals("Đặt trước")) {
            this.trangThai = trangThai;
        } else {
            this.trangThai = "Trống";
        }
        
        this.loaiBan = "Thường"; // Mặc định là bàn thường
    }

    // Constructor đầy đủ tham số với loại bàn
    public BanAn(int maBan, int soCho, String trangThai, String loaiBan) {
        this.maBan = (maBan > 0) ? maBan : 1;
        this.soCho = (soCho > 0) ? soCho : 2; 
        
        // Kiểm tra trạng thái
        if (trangThai != null) {
            trangThai = trangThai.trim();
        }
        if (trangThai != null && (trangThai.equals("Trống") || 
            trangThai.equals("Đang sử dụng") || 
            trangThai.equals("Đặt trước"))) {
            this.trangThai = trangThai;
        } else {
            this.trangThai = "Trống";
        }
        
        // Kiểm tra loại bàn (trim khoảng trắng)
        if (loaiBan != null) {
            loaiBan = loaiBan.trim();
        }
        if (loaiBan != null && (loaiBan.equals("Thường") || 
            loaiBan.equals("VIP") || 
            loaiBan.equals("Deluxe"))) {
            this.loaiBan = loaiBan;
        } else {
            this.loaiBan = "Thường";
        }
    }

    // Getter
    public int getMaBan() { return maBan; }
    
    // Lấy mã bàn đã format: 001, 002, 015...
    public String getMaBanFormatted() {
        return String.format("%03d", maBan);  // 3 chữ số, thêm số 0 phía trước
    }
    
    public int getSoCho() { return soCho; }
    public String getTrangThai() { return trangThai; }
    public String getLoaiBan() { return loaiBan; }

    // Setter 
    public void setMaBan(int maBan) {
        if (maBan > 0) {
            this.maBan = maBan;
        }
    }

    public void setSoCho(int soCho) {
        if (soCho > 0) {
            this.soCho = soCho;
        }
    }

    public void setTrangThai(String trangThai) {
        if (trangThai.equals("Trống") || 
            trangThai.equals("Đang sử dụng") || 
            trangThai.equals("Đặt trước")) {
            this.trangThai = trangThai;
        }
    }

    public void setLoaiBan(String loaiBan) {
        if (loaiBan != null) {
            loaiBan = loaiBan.trim();
        }
        if (loaiBan != null && (loaiBan.equals("Thường") || 
            loaiBan.equals("VIP") || 
            loaiBan.equals("Deluxe"))) {
            this.loaiBan = loaiBan;
        }
    }

    public void capNhatTrangThai(String trangThaiMoi) {
        setTrangThai(trangThaiMoi);
    }

    @Override
    public String toString() {
        return "BanAn{" +
                "maBan=" + getMaBanFormatted() +  // Hiển thị format 001, 002...
                ", soCho=" + soCho +
                ", trangThai='" + trangThai + '\'' +
                ", loaiBan='" + loaiBan + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BanAn banAn = (BanAn) o;
        return maBan == banAn.maBan;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(maBan);
    }
}
