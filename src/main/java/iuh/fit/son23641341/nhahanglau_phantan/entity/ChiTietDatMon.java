package iuh.fit.son23641341.nhahanglau_phantan.entity;

public class ChiTietDatMon {
    private MonAn monAn;
    private int soLuong;
    private double thanhTien;

    public ChiTietDatMon(MonAn monAn, int soLuong) {
        this.monAn = monAn;
        this.soLuong = soLuong;
        this.thanhTien = tinhThanhTien();
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
