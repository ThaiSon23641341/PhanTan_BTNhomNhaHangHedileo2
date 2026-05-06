package iuh.fit.son23641341.nhahanglau_phantan.entity;


import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;

@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@SuperBuilder
@Table(name = "chi_tiet_dat_mon")

public class ChiTietDatMon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ma_phieu")
    private PhieuDatBan phieuDatBan;

    @ManyToOne
    @JoinColumn(name = "ma_mon")
    private MonAn monAn;
    
    @Column(name = "so_luong")
    private int soLuong;
    
    @Column(name = "thanh_tien")
    private double thanhTien;
    
    public ChiTietDatMon() {
    }



    public ChiTietDatMon(MonAn monAn, int soLuong) {
        this.monAn = monAn;

        this.soLuong = soLuong;
        this.thanhTien = tinhThanhTien();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PhieuDatBan getPhieuDatBan() {
        return phieuDatBan;
    }

    public void setMonAn(MonAn monAn) {
        this.monAn = monAn;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    public void setPhieuDatBan(PhieuDatBan phieuDatBan) {
        this.phieuDatBan = phieuDatBan;
    }

    public MonAn getMonAn() {
        return monAn;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
        this.thanhTien = tinhThanhTien();
    }

    public void increment() {
        this.soLuong++;
        this.thanhTien = tinhThanhTien();
    }

    public void decrement() {
        this.soLuong--;
        this.thanhTien = tinhThanhTien();
    }

    private double tinhThanhTien() {
        return monAn != null ? monAn.getGia() * soLuong : 0;
    }
}
