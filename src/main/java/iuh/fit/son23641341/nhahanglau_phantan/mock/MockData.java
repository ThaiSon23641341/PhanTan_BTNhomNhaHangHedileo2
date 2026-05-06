package iuh.fit.son23641341.nhahanglau_phantan.mock;

import iuh.fit.son23641341.nhahanglau_phantan.entity.BanAn;
import iuh.fit.son23641341.nhahanglau_phantan.entity.ChiTietDatMon;
import iuh.fit.son23641341.nhahanglau_phantan.entity.HoaDon;
import iuh.fit.son23641341.nhahanglau_phantan.entity.KhachHangThanhVien;
import iuh.fit.son23641341.nhahanglau_phantan.entity.KhuyenMai;
import iuh.fit.son23641341.nhahanglau_phantan.entity.MonAn;
import iuh.fit.son23641341.nhahanglau_phantan.entity.NhanVien;
import iuh.fit.son23641341.nhahanglau_phantan.entity.PhieuDatBan;
import iuh.fit.son23641341.nhahanglau_phantan.entity.User;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MockData {
    private static final List<KhachHangThanhVien> KHACH_HANGS = new ArrayList<>();
    private static final List<NhanVien> NHAN_VIENS = new ArrayList<>();
    private static final List<User> USERS = new ArrayList<>();
    private static final List<MonAn> MON_ANS = new ArrayList<>();
    private static final List<BanAn> BAN_ANS = new ArrayList<>();
    private static final List<KhuyenMai> KHUYEN_MAIS = new ArrayList<>();
    private static final List<PhieuDatBan> PHIEU_DATS = new ArrayList<>();
    private static final List<HoaDon> HOA_DONS = new ArrayList<>();

    static {
        seedUsersAndNhanVien();
        seedKhachHang();
        seedMonAn();
        seedBanAn();
        seedKhuyenMai();
        seedPhieuDatVaHoaDon();
    }

    private MockData() {
    }

    public static List<KhachHangThanhVien> khachHangs() {
        return KHACH_HANGS;
    }

    public static List<NhanVien> nhanViens() {
        return NHAN_VIENS;
    }

    public static List<User> users() {
        return USERS;
    }

    public static List<MonAn> monAns() {
        return MON_ANS;
    }

    public static List<BanAn> banAns() {
        return BAN_ANS;
    }

    public static List<KhuyenMai> khuyenMais() {
        return KHUYEN_MAIS;
    }

    public static List<PhieuDatBan> phieuDats() {
        return PHIEU_DATS;
    }

    public static List<HoaDon> hoaDons() {
        return HOA_DONS;
    }

    public static List<KhachHangThanhVien> khachHangsSnapshot() {
        return new ArrayList<>(KHACH_HANGS);
    }

    public static List<MonAn> monAnsSnapshot() {
        return new ArrayList<>(MON_ANS);
    }

    public static List<KhuyenMai> khuyenMaisSnapshot() {
        return new ArrayList<>(KHUYEN_MAIS);
    }

    public static List<BanAn> banAnsSnapshot() {
        return new ArrayList<>(BAN_ANS);
    }

    public static List<PhieuDatBan> phieuDatsSnapshot() {
        return new ArrayList<>(PHIEU_DATS);
    }

    public static List<HoaDon> hoaDonsSnapshot() {
        return new ArrayList<>(HOA_DONS);
    }

    private static void seedUsersAndNhanVien() {
        USERS.add(new User("1", "admin", "123456"));
        USERS.add(new User("2", "thungan", "123456"));

        try {
            NHAN_VIENS.add(new NhanVien("NV001", "Nguyễn Quản Lý", "Nam", "Ca Full", "0912345678",
                "admin@gmail.com", "Quản lý", 1));
            NHAN_VIENS.add(new NhanVien("NV002", "Trần Thu Ngân", "Nữ", "Ca Sáng", "0987654321",
                "thungan@gmail.com", "Thu ngân", 2));
        } catch (Exception ignored) {
        }
    }

    private static void seedKhachHang() {
        KHACH_HANGS.add(new KhachHangThanhVien("KH001", "Nguyễn Văn A", "0901234567", "khach1@gmail.com",
            "Nam", "Vàng", 120, LocalDate.now().minusDays(20)));
        KHACH_HANGS.add(new KhachHangThanhVien("KH002", "Lê Thị B", "0912345678", "khach2@gmail.com",
            "Nữ", "Bạc", 80, LocalDate.now().minusDays(10)));
        KHACH_HANGS.add(new KhachHangThanhVien("KH003", "Phạm Văn C", "0923456789", "khach3@gmail.com",
            "Nam", "Đồng", 40, LocalDate.now().minusDays(5)));
    }

    private static void seedMonAn() {
        try {
            MON_ANS.add(new MonAn("MA001", "Phở bò", "Món chính", 45000, "Phở bò truyền thống"));
            MON_ANS.add(new MonAn("MA002", "Gỏi cuốn", "Khai vị", 30000, "Cuốn tôm thịt"));
            MON_ANS.add(new MonAn("MA003", "Chè đậu xanh", "Tráng miệng", 20000, "Ngọt thanh"));
            MON_ANS.add(new MonAn("MA004", "Trà đá", "Đồ uống", 5000, "Mát lạnh"));
        } catch (Exception ignored) {
        }
    }

    private static void seedBanAn() {
        BAN_ANS.add(new BanAn(1, 4, "Trống", "Thường"));
        BAN_ANS.add(new BanAn(2, 6, "Đang sử dụng", "VIP"));
        BAN_ANS.add(new BanAn(3, 2, "Đặt trước", "Thường"));
        BAN_ANS.add(new BanAn(4, 8, "Trống", "Deluxe"));
    }

    private static void seedKhuyenMai() {
        KHUYEN_MAIS.add(new KhuyenMai("KM001", "Giảm 10%", 10, "01/04/2026", "30/04/2026", "Ưu đãi tháng 4"));
        KHUYEN_MAIS.add(new KhuyenMai("KM002", "Giảm 15%", 15, "01/05/2026", "31/05/2026", "Ưu đãi tháng 5"));
    }

    private static void seedPhieuDatVaHoaDon() {
        if (MON_ANS.isEmpty()) {
            return;
        }

        ArrayList<ChiTietDatMon> chiTiet = new ArrayList<>();
        chiTiet.add(new ChiTietDatMon(MON_ANS.get(0), 2));
        chiTiet.add(new ChiTietDatMon(MON_ANS.get(1), 1));

        PhieuDatBan phieu = new PhieuDatBan();
        phieu.setMaPhieu("PD202604210900001");
        phieu.setMaKhachHang("KH001");
        phieu.setTenKhachDat("Nguyễn Văn A");
        phieu.setSdtDat("0901234567");
        phieu.setEmailDat("khach1@gmail.com");
        phieu.setTrangThai("Đặt trước");
        phieu.setMaNhanVien("NV001");
        phieu.setMaBan(1);
        phieu.setNgayDat("21/04/2026");
        phieu.setGioDat("09:00");
        phieu.setThoiGianDat(Timestamp.valueOf(LocalDate.now().atStartOfDay()));
        phieu.setDanhSachMonAn(chiTiet);
        phieu.setDanhSachBanDaChon(new ArrayList<>(Collections.singletonList(1)));
        phieu.setGiamGia(20000);
        phieu.setTongTien(chiTiet.stream().mapToDouble(item -> item.getMonAn().getGia() * item.getSoLuong()).sum());
        phieu.setTienCoc(phieu.tinhTienCoc());

        PHIEU_DATS.add(phieu);

        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHoaDon("HD001");
        hoaDon.setNgayLap(Date.valueOf(LocalDate.now()));
        hoaDon.setGioLapHoaDon(new Time(System.currentTimeMillis()));
        hoaDon.setPhieuDat(phieu);
        hoaDon.setTrangThai("Đã thanh toán");
        hoaDon.setTongTien(phieu.getTongTien());
        hoaDon.setMaKhuyenMai("KM001");
        hoaDon.setLoaiHoaDon("Đặt trước");
        HOA_DONS.add(hoaDon);
    }
}
