package iuh.fit.son23641341.nhahanglau_phantan.entity;

public class MonAn {
    // 1. Khai báo thuộc tính
    private String maMon;    // Khóa chính
    private String tenMon;
    private String loaiMon;  // "Khai vị", "Món chính", "Tráng miệng", "Đồ uống"
    private double gia;      // > 0
    private String moTa;


    // Constructor mặc định
    public MonAn() throws Exception {
        this("M000", "Tên món", "Khai vị", 1.0, "");
    }
    

    // Constructor đầy đủ
    public MonAn(String maMon, String tenMon, String loaiMon, double gia, String moTa) throws Exception {
        setMaMon(maMon);
        setTenMon(tenMon);
        setLoaiMon(loaiMon);
        setGia(gia);
        setMoTa(moTa);
    }

    // 2. Viết phương thức getter và setter
    public String getMaMon() {
        return maMon;
    }

    public void setMaMon(String maMon) throws Exception {
        if (maMon == null || maMon.trim().isEmpty()) {
            throw new Exception("Mã món không được rỗng");
        }
        this.maMon = maMon;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) throws Exception {
        if (tenMon == null || tenMon.trim().isEmpty()) {
            throw new Exception("Tên món không được rỗng");
        }
        this.tenMon = tenMon;
    }

    public String getLoaiMon() {
        return loaiMon;
    }

    public void setLoaiMon(String loaiMon) throws Exception {
        if (loaiMon == null || loaiMon.trim().isEmpty()) {
            throw new Exception("Loại món không được rỗng");
        }
        String[] dsLoai = {"Khai vị", "Món chính", "Tráng miệng", "Đồ uống"};
        boolean hopLe = false;
        for (String loai : dsLoai) {
            if (loai.equalsIgnoreCase(loaiMon.trim())) {
                hopLe = true;
                break;
            }
        }
        if (!hopLe) {
            throw new Exception("Loại món không hợp lệ");
        }
        this.loaiMon = loaiMon;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) throws Exception {
        if (gia <= 0) {
            throw new Exception("Giá tiền không hợp lệ");
        }
        this.gia = gia;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public void capNhatThongTin(String tenMon, String loaiMon, double gia, String moTa) throws Exception {
        setTenMon(tenMon);
        setLoaiMon(loaiMon);
        setGia(gia);
        setMoTa(moTa);
    }

    @Override
    public String toString() {
        return String.format("Mã món: %s | Tên món: %s | Loại: %s | Giá: %.2f | Mô tả: %s",
                maMon, tenMon, loaiMon, gia, moTa);
    }
}
