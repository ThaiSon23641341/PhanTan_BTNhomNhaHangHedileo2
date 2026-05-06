package iuh.fit.son23641341.nhahanglau_phantan.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	@Id
	@Column(name = "id")
	private String iD;
	
	@Column(name = "ten_nguoi_dung")
	private String tenNguoiDung;
	
	@Column(name = "mat_khau")
	private String matKhau;
	
	public User() {}
	
	public User(String iD, String tenNguoiDung, String matKhau) {
		super();
		this.iD = iD;
		this.tenNguoiDung = tenNguoiDung;
		this.matKhau = matKhau;
	}
	
	public String getiD() {
		return iD;
	}

	public void setiD(String iD) {
		this.iD = iD;
	}

	public String getTenNguoiDung() {
		return tenNguoiDung;
	}

	public void setTenNguoiDung(String tenNguoiDung) {
		this.tenNguoiDung = tenNguoiDung;
	}

	public String getMatKhau() {
		return matKhau;
	}

	public void setMatKhau(String matKhau) {
		this.matKhau = matKhau;
	}

	public boolean dangNhap() {
        return tenNguoiDung != null && !tenNguoiDung.trim().isEmpty()
            && matKhau != null && !matKhau.trim().isEmpty();
    }

    public boolean dangXuat() {
        return true;
    }

	@Override
	public String toString() {
		return "User [iD=" + iD + ", tenNguoiDung=" + tenNguoiDung + ", matKhau=" + matKhau + "]";
	}

	
}
