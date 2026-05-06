package iuh.fit.son23641341.nhahanglau_phantan.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@NoArgsConstructor // Cần thiết cho Hibernate
@SuperBuilder
@Entity
@Table(name = "chi_tiet_don_hang")
public class ChiTietDonHang extends ChiTietDatMon {
    public ChiTietDonHang(MonAn monAn, int soLuong) {
        this.setMonAn(monAn);
        this.setSoLuong(soLuong);
    }
}
