package iuh.fit.son23641341.nhahanglau_phantan.control;

import iuh.fit.son23641341.nhahanglau_phantan.dao.ThongKe_DAO;
import iuh.fit.son23641341.nhahanglau_phantan.util.EntityManagerFactoryUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ThongKe_Ctr {
    private static ThongKe_Ctr instance;
    private ThongKe_DAO thongKeDAO;

    private ThongKe_Ctr() {
    }

    public static synchronized ThongKe_Ctr getInstance() {
        if (instance == null) instance = new ThongKe_Ctr();
        return instance;
    }

    public double getTongTienHomNay() {
        jakarta.persistence.EntityManager em = EntityManagerFactoryUtil.getEntityManager();
        if (em == null) return 0;
        try {
            return new ThongKe_DAO(em).getTongTienHomNay();
        } finally {
            em.close();
        }
    }

    public long getTongPhieuDangDat() {
        jakarta.persistence.EntityManager em = EntityManagerFactoryUtil.getEntityManager();
        if (em == null) return 0;
        try {
            return new ThongKe_DAO(em).getSoPhieuDangDat();
        } finally {
            em.close();
        }
    }

    public int getSoHoaDonHomNay() {
        jakarta.persistence.EntityManager em = EntityManagerFactoryUtil.getEntityManager();
        if (em == null) return 0;
        try {
            return (int) new ThongKe_DAO(em).getSoHoaDonNgay(LocalDate.now());
        } finally {
            em.close();
        }
    }


    public ThongKeCardData getDuLieuChoTheThongKe(int nam, int thang) {
        jakarta.persistence.EntityManager em = EntityManagerFactoryUtil.getEntityManager();
        if (em != null) {
            try {
                ThongKe_DAO dao = new ThongKe_DAO(em);
                ArrayList<ThongKe_DAO.ThongKeThang> allData = dao.getDoanhThuTheoThangTrongNam(nam);
                ThongKe_DAO.ThongKeThang hienTai = allData.get(thang - 1);
                ThongKe_DAO.ThongKeThang truocData = (thang > 1) ? allData.get(thang - 2) : new ThongKe_DAO.ThongKeThang(0, 0, 0);

                ThongKeCardData res = new ThongKeCardData();
                res.dtHienTai = hienTai.tongDoanhThu;
                res.dtChenhLech = tinhPhanTram(hienTai.tongDoanhThu, truocData.tongDoanhThu);
                res.hdHienTai = (int) hienTai.tonghoaDon;
                res.hdChenhLech = tinhPhanTram(hienTai.tonghoaDon, truocData.tonghoaDon);
                return res;
            } finally {
                em.close();
            }
        } else {
            // Client side
            iuh.fit.son23641341.nhahanglau_phantan.network.Request req = 
                new iuh.fit.son23641341.nhahanglau_phantan.network.Request("GET_THONG_KE_CARD_THANG", new int[]{nam, thang});
            iuh.fit.son23641341.nhahanglau_phantan.network.Response res = 
                iuh.fit.son23641341.nhahanglau_phantan.network.ClientControl.getInstance().sendRequest(req);
            return (res.getStatus().equals("SUCCESS")) ? (ThongKeCardData) res.getData() : new ThongKeCardData();
        }
    }

    public ArrayList<ThongKe_DAO.TopMonAn> getTopMonAn(int t, int n, int l) {
        jakarta.persistence.EntityManager em = EntityManagerFactoryUtil.getEntityManager();
        if (em != null) {
            try {
                return new ThongKe_DAO(em).getTopMonAnTheoThang(t, n, l);
            } finally {
                em.close();
            }
        } else {
            // Client side
            iuh.fit.son23641341.nhahanglau_phantan.network.Request req = 
                new iuh.fit.son23641341.nhahanglau_phantan.network.Request("GET_TOP_MON_AN", new int[]{t, n, l});
            iuh.fit.son23641341.nhahanglau_phantan.network.Response res = 
                iuh.fit.son23641341.nhahanglau_phantan.network.ClientControl.getInstance().sendRequest(req);
            return (res.getStatus().equals("SUCCESS")) ? (ArrayList<ThongKe_DAO.TopMonAn>) res.getData() : new ArrayList<>();
        }
    }

    public ThongKeCardData getDuLieuChoTheThongKeTheoNgay(LocalDate ngay) {
        jakarta.persistence.EntityManager em = EntityManagerFactoryUtil.getEntityManager();
        if (em != null) {
            try {
                ThongKe_DAO dao = new ThongKe_DAO(em);
                double dtHienTai = dao.getDoanhThuNgay(ngay);
                double dtHomTruoc = dao.getDoanhThuNgay(ngay.minusDays(1));

                long hdHienTai = dao.getSoHoaDonNgay(ngay);
                long hdHomTruoc = dao.getSoHoaDonNgay(ngay.minusDays(1));

                ThongKeCardData res = new ThongKeCardData();
                res.dtHienTai = dtHienTai;
                res.dtChenhLech = tinhPhanTram(dtHienTai, dtHomTruoc);
                res.hdHienTai = (int) hdHienTai;
                res.hdChenhLech = tinhPhanTram(hdHienTai, hdHomTruoc);
                return res;
            } finally {
                em.close();
            }
        } else {
            // Client side
            iuh.fit.son23641341.nhahanglau_phantan.network.Request req = 
                new iuh.fit.son23641341.nhahanglau_phantan.network.Request("GET_THONG_KE_CARD_NGAY", ngay);
            iuh.fit.son23641341.nhahanglau_phantan.network.Response res = 
                iuh.fit.son23641341.nhahanglau_phantan.network.ClientControl.getInstance().sendRequest(req);
            return (res.getStatus().equals("SUCCESS")) ? (ThongKeCardData) res.getData() : new ThongKeCardData();
        }
    }

    public ArrayList<ThongKe_DAO.TopMonAn> getTopMonAnTheoNgay(LocalDate ngay, int limit) {
        jakarta.persistence.EntityManager em = EntityManagerFactoryUtil.getEntityManager();
        if (em != null) {
            try {
                return new ThongKe_DAO(em).getTopMonAnTheoNgay(ngay, limit);
            } finally {
                em.close();
            }
        } else {
            // Client side
            iuh.fit.son23641341.nhahanglau_phantan.network.Request req = 
                new iuh.fit.son23641341.nhahanglau_phantan.network.Request("GET_TOP_MON_AN_NGAY", new Object[]{ngay, limit});
            iuh.fit.son23641341.nhahanglau_phantan.network.Response res = 
                iuh.fit.son23641341.nhahanglau_phantan.network.ClientControl.getInstance().sendRequest(req);
            return (res.getStatus().equals("SUCCESS")) ? (ArrayList<ThongKe_DAO.TopMonAn>) res.getData() : new ArrayList<>();
        }
    }

    public ArrayList<ThongKe_DAO.ThongKeThang> getDoanhThuTheoThang(int nam) {
        jakarta.persistence.EntityManager em = EntityManagerFactoryUtil.getEntityManager();
        if (em != null) {
            try {
                return new ThongKe_DAO(em).getDoanhThuTheoThangTrongNam(nam);
            } finally {
                em.close();
            }
        } else {
            // Client side
            iuh.fit.son23641341.nhahanglau_phantan.network.Request req = 
                new iuh.fit.son23641341.nhahanglau_phantan.network.Request("GET_DOANH_THU_BIEU_DO", nam);
            iuh.fit.son23641341.nhahanglau_phantan.network.Response res = 
                iuh.fit.son23641341.nhahanglau_phantan.network.ClientControl.getInstance().sendRequest(req);
            return (res.getStatus().equals("SUCCESS")) ? (ArrayList<ThongKe_DAO.ThongKeThang>) res.getData() : new ArrayList<>();
        }
    }

    public double TinhTongDoanhThuNam(int nam) {
        jakarta.persistence.EntityManager em = EntityManagerFactoryUtil.getEntityManager();
        if (em != null) {
            try {
                return new ThongKe_DAO(em).getDoanhThuNam(nam);
            } finally {
                em.close();
            }
        } else {
            iuh.fit.son23641341.nhahanglau_phantan.network.Request req = new iuh.fit.son23641341.nhahanglau_phantan.network.Request("GET_DOANH_THU_NAM", nam);
            iuh.fit.son23641341.nhahanglau_phantan.network.Response res = iuh.fit.son23641341.nhahanglau_phantan.network.ClientControl.getInstance().sendRequest(req);
            return (res.getStatus().equals("SUCCESS")) ? (double) res.getData() : 0.0;
        }
    }

    public double TinhDoanhSoTrungBinhNgay(int nam) {
        double tong = TinhTongDoanhThuNam(nam);
        return tong / 365.0; 
    }

    public double TinhDoanhSoTrungBinhHoaDon(int nam) {
        jakarta.persistence.EntityManager em = EntityManagerFactoryUtil.getEntityManager();
        if (em != null) {
            try {
                double tong = new ThongKe_DAO(em).getDoanhThuNam(nam);
                long soHD = new ThongKe_DAO(em).getSoHoaDonNam(nam);
                return soHD > 0 ? tong / soHD : 0;
            } finally {
                em.close();
            }
        } else {
            iuh.fit.son23641341.nhahanglau_phantan.network.Request req = new iuh.fit.son23641341.nhahanglau_phantan.network.Request("GET_AVG_HOA_DON_NAM", nam);
            iuh.fit.son23641341.nhahanglau_phantan.network.Response res = iuh.fit.son23641341.nhahanglau_phantan.network.ClientControl.getInstance().sendRequest(req);
            return (res.getStatus().equals("SUCCESS")) ? (double) res.getData() : 0.0;
        }
    }

    public double TinhTongDoanhThuNgay(LocalDate date) {
        jakarta.persistence.EntityManager em = EntityManagerFactoryUtil.getEntityManager();
        if (em != null) {
            try {
                return new ThongKe_DAO(em).getDoanhThuNgay(date);
            } finally {
                em.close();
            }
        } else {
            iuh.fit.son23641341.nhahanglau_phantan.network.Request req = new iuh.fit.son23641341.nhahanglau_phantan.network.Request("GET_DOANH_THU_NGAY", date);
            iuh.fit.son23641341.nhahanglau_phantan.network.Response res = iuh.fit.son23641341.nhahanglau_phantan.network.ClientControl.getInstance().sendRequest(req);
            return (res.getStatus().equals("SUCCESS")) ? (double) res.getData() : 0.0;
        }
    }

    public double TinhDoanhSoTrungBinhHoaDonTheoNgay(LocalDate date) {
        jakarta.persistence.EntityManager em = EntityManagerFactoryUtil.getEntityManager();
        if (em != null) {
            try {
                double tong = new ThongKe_DAO(em).getDoanhThuNgay(date);
                long soHD = new ThongKe_DAO(em).getSoHoaDonNgay(date);
                return soHD > 0 ? tong / soHD : 0;
            } finally {
                em.close();
            }
        } else {
            iuh.fit.son23641341.nhahanglau_phantan.network.Request req = new iuh.fit.son23641341.nhahanglau_phantan.network.Request("GET_AVG_HOA_DON_NGAY", date);
            iuh.fit.son23641341.nhahanglau_phantan.network.Response res = iuh.fit.son23641341.nhahanglau_phantan.network.ClientControl.getInstance().sendRequest(req);
            return (res.getStatus().equals("SUCCESS")) ? (double) res.getData() : 0.0;
        }
    }
    
    // Helper tính toán %
    private String tinhPhanTram(double hienTai, double truoc) {
        if (truoc <= 0) return hienTai > 0 ? "▲ N/A" : "---";
        double change = ((hienTai - truoc) / truoc) * 100;
        return (change >= 0 ? "▲ " : "▼ ") + String.format("%.1f%%", Math.abs(change));
    }

    public static class ThongKeCardData {
        public double dtHienTai;
        public String dtChenhLech;
        public int hdHienTai;
        public String hdChenhLech;
    }
}