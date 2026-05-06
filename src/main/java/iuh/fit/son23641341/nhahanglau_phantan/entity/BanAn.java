package iuh.fit.son23641341.nhahanglau_phantan.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;

@Entity
@Table(name = "ban_an")
@Builder
public class BanAn {
    @Id
    @Column(name = "ma_ban")
    private int maBan;
    
    @Column(name = "so_cho")
    private int soCho;
    
    @Column(name = "loai_ban")
    private String loaiBan; // Thường, VIP, Deluxe

    // Constructor mặc định
    public BanAn() {
        this.maBan = 0;
        this.soCho = 0;
        this.loaiBan = "Thường";
    }

    // Constructor đầy đủ tham số
    public BanAn(int maBan, int soCho, String loaiBan) {
        this.maBan = (maBan > 0) ? maBan : 1;
        this.soCho = (soCho > 0) ? soCho : 2;

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

    public String getMaBanFormatted() {
        return String.format("%03d", maBan);
    }

    public int getSoCho() { return soCho; }
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

    @Override
    public String toString() {
        return "BanAn{" +
                "maBan=" + getMaBanFormatted() +
                ", soCho=" + soCho +
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