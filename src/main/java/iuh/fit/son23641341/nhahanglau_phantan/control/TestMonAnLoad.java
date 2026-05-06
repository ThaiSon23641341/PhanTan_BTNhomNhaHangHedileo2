package iuh.fit.son23641341.nhahanglau_phantan.control;

import iuh.fit.son23641341.nhahanglau_phantan.control.MonAn_Ctr;
import iuh.fit.son23641341.nhahanglau_phantan.entity.MonAn;

// NOTE: Uses mock data; SQL loading removed.
public class TestMonAnLoad {
    public static void main(String[] args) {
        MonAn_Ctr ctr = new MonAn_Ctr();

        System.out.println("=== DANH SÁCH MÓN ĂN (DỮ LIỆU MẪU) ===");
        for (MonAn mon : ctr.getDanhSachMonAn()) {
            System.out.printf("%s | %s | %s | %.0f | %s%n",
                    mon.getMaMon(), mon.getTenMon(), mon.getLoaiMon(), mon.getGia(), mon.getMoTa());
        }

        System.out.println("\nTổng số món trong danh sách: " + ctr.getDanhSachMonAn().size());
    }
}

