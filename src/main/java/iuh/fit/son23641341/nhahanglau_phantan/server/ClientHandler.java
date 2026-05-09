package iuh.fit.son23641341.nhahanglau_phantan.server;

import iuh.fit.son23641341.nhahanglau_phantan.control.*;
import iuh.fit.son23641341.nhahanglau_phantan.network.Request;
import iuh.fit.son23641341.nhahanglau_phantan.network.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            while (true) {
                Object input = in.readObject();
                if (input instanceof Request) {
                    Request request = (Request) input;
                    Response response = processRequest(request);
                    out.writeObject(response);
                    out.flush();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Client disconnected: " + socket.getInetAddress());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Response processRequest(Request request) {
        String action = request.getAction();
        Object data = request.getData();

        try {
            switch (action) {
                case "LOGIN":
                    String[] loginData = (String[]) data;
                    User_Ctr userCtr = User_Ctr.getInstance();
                    if (userCtr.kiemTraDangNhap(loginData[0], loginData[1])) {
                        return new Response("SUCCESS", userCtr.getNhanVienHienTai(), "Login successful");
                    } else {
                        return new Response("ERROR", null, "Invalid username or password");
                    }

                case "GET_TOP_MON_AN":
                    // data: int[] {thang, nam, gioiHan}
                    int[] paramsTop = (int[]) data;
                    return new Response("SUCCESS", ThongKe_Ctr.getInstance().getTopMonAn(paramsTop[0], paramsTop[1], paramsTop[2]), "Success");

                case "GET_ALL_CUSTOMERS":
                    return new Response("SUCCESS", new KhachHang_Ctr().getDanhSachKhachHang(), "Success");

                case "ADD_CUSTOMER":
                    return new Response("SUCCESS", new KhachHang_Ctr().themKhachHang((iuh.fit.son23641341.nhahanglau_phantan.entity.KhachHangThanhVien) data), "Success");

                case "UPDATE_CUSTOMER":
                    return new Response("SUCCESS", new KhachHang_Ctr().capNhatKhachHang((iuh.fit.son23641341.nhahanglau_phantan.entity.KhachHangThanhVien) data), "Success");

                case "DELETE_CUSTOMER":
                    return new Response("SUCCESS", new KhachHang_Ctr().xoaKhachHang((String) data), "Success");

                case "GET_ALL_MONAN":
                    return new Response("SUCCESS", new iuh.fit.son23641341.nhahanglau_phantan.dao.MonAn_DAO().getAllMonAn(), "Success");

                case "GET_ALL_BAN":
                    return new Response("SUCCESS", new iuh.fit.son23641341.nhahanglau_phantan.dao.BanAn_DAO().getAllBanAn(), "Success");

                case "GET_PHIEU_DAT_BY_BAN":
                    // data: Integer hoặc String
                    int maBan = (data instanceof Integer) ? (Integer)data : Integer.parseInt(data.toString());
                    return new Response("SUCCESS", PhieuDatBan_Ctr.getInstance().layPhieuDangSuDungTheoMaBan(maBan), "Success");

                case "LUU_PHIEU_DAT":
                    return new Response("SUCCESS", PhieuDatBan_Ctr.getInstance().themPhieuDat((iuh.fit.son23641341.nhahanglau_phantan.entity.PhieuDatBan) data), "Success");

                case "GET_PHIEU_DAT_BY_NGAY":
                    return new Response("SUCCESS", new iuh.fit.son23641341.nhahanglau_phantan.dao.PhieuDat_DAO().getPhieuDatByNgay((String) data), "Success");

                case "GET_THONG_KE_HOM_NAY":
                    ThongKe_Ctr tk = ThongKe_Ctr.getInstance();
                    double dt = tk.getTongTienHomNay();
                    int hd = tk.getSoHoaDonHomNay();
                    return new Response("SUCCESS", new Double[]{dt, (double)hd}, "Success");

                case "GET_ALL_KHUYEN_MAI":
                    return new Response("SUCCESS", new KhuyenMai_Ctr().getDanhSachKhuyenMai(), "Success");

                case "ADD_KHUYEN_MAI":
                    return new Response("SUCCESS", new KhuyenMai_Ctr().themKhuyenMai((iuh.fit.son23641341.nhahanglau_phantan.entity.KhuyenMai) data), "Success");

                case "SEARCH_MONAN":
                    // data: Object[] {keyword, searchByCode, ...}
                    Object[] sParams = (Object[]) data;
                    return new Response("SUCCESS", new iuh.fit.son23641341.nhahanglau_phantan.dao.TimKiemChung_DAO().timKiemMonAn(
                        (String)sParams[0], (boolean)sParams[1], (boolean)sParams[2], (boolean)sParams[3], (boolean)sParams[4], (boolean)sParams[5]), "Success");

                case "TAO_HOA_DON":
                    // data: Object[] {phieuDat, duongDanPDF}
                    Object[] hParams = (Object[]) data;
                    return new Response("SUCCESS", new HoaDon_Ctrl().taoHoaDonVaXuatPDF(
                        (iuh.fit.son23641341.nhahanglau_phantan.entity.PhieuDatBan)hParams[0], (String)hParams[1]), "Success");

                case "CAP_NHAT_MON_PHIEU":
                    // data: Object[] {maPhieu, ArrayList<ChiTietDatMon>}
                    Object[] cParams = (Object[]) data;
                    return new Response("SUCCESS", PhieuDatBan_Ctr.getInstance().capNhatMonAnCuaPhieu(
                        (String)cParams[0], (java.util.ArrayList<iuh.fit.son23641341.nhahanglau_phantan.entity.ChiTietDatMon>)cParams[1]), "Success");

                case "GET_THONG_KE_CARD_THANG":
                    // data: int[] {nam, thang}
                    int[] tParams = (int[]) data;
                    return new Response("SUCCESS", ThongKe_Ctr.getInstance().getDuLieuChoTheThongKe(tParams[0], tParams[1]), "Success");

                case "GET_THONG_KE_CARD_NGAY":
                    // data: LocalDate
                    return new Response("SUCCESS", ThongKe_Ctr.getInstance().getDuLieuChoTheThongKeTheoNgay((java.time.LocalDate) data), "Success");

                case "GET_TOP_MON_AN_NGAY":
                    // data: Object[] {LocalDate, limit}
                    Object[] tnParams = (Object[]) data;
                    return new Response("SUCCESS", ThongKe_Ctr.getInstance().getTopMonAnTheoNgay((java.time.LocalDate)tnParams[0], (int)tnParams[1]), "Success");

                case "GET_DOANH_THU_BIEU_DO":
                    // data: int nam
                    return new Response("SUCCESS", ThongKe_Ctr.getInstance().getDoanhThuTheoThang((int) data), "Success");

                case "GET_TONG_QUAN_SOLIEU":
                    return new Response("SUCCESS", new TongQuan_Ctr().layTatCaSoLieu(), "Success");

                case "ADD_MONAN":
                    return new Response("SUCCESS", new iuh.fit.son23641341.nhahanglau_phantan.dao.MonAn_DAO().themMonAn((iuh.fit.son23641341.nhahanglau_phantan.entity.MonAn) data), "Success");

                case "UPDATE_MONAN":
                    return new Response("SUCCESS", new iuh.fit.son23641341.nhahanglau_phantan.dao.MonAn_DAO().capNhatMonAn((iuh.fit.son23641341.nhahanglau_phantan.entity.MonAn) data), "Success");

                case "DELETE_MONAN":
                    return new Response("SUCCESS", new iuh.fit.son23641341.nhahanglau_phantan.dao.MonAn_DAO().xoaMonAn((String) data), "Success");

                case "GET_MONAN_BY_MA":
                    return new Response("SUCCESS", new iuh.fit.son23641341.nhahanglau_phantan.dao.MonAn_DAO().timTheoMa((String) data), "Success");

                case "GET_ALL_PHIEU":
                    return new Response("SUCCESS", new iuh.fit.son23641341.nhahanglau_phantan.dao.PhieuDat_DAO().getAllPhieuDat(), "Success");

                case "GET_PHIEU_BY_BAN_VA_NGAY":
                    // data: Object[] {maBan, ngay}
                    Object[] bnParams = (Object[]) data;
                    return new Response("SUCCESS", new iuh.fit.son23641341.nhahanglau_phantan.dao.PhieuDat_DAO().getPhieuDatByBanVaNgay((int)bnParams[0], (String)bnParams[1]), "Success");

                case "GET_PHIEU_BY_MA":
                    return new Response("SUCCESS", new iuh.fit.son23641341.nhahanglau_phantan.dao.PhieuDat_DAO().timPhieuDatBangMa((String) data), "Success");

                case "CAP_NHAT_TRANG_THAI_PHIEU":
                    // data: Object[] {maPhieu, trangThai}
                    Object[] stParams = (Object[]) data;
                    return new Response("SUCCESS", new iuh.fit.son23641341.nhahanglau_phantan.dao.PhieuDat_DAO().capNhatTrangThai((String)stParams[0], (String)stParams[1]), "Success");

                case "GET_PHIEU_DANG_SU_DUNG_BY_BAN":
                    return new Response("SUCCESS", new iuh.fit.son23641341.nhahanglau_phantan.dao.PhieuDat_DAO().getPhieuDangSuDungTheoMaBan((int) data), "Success");

                case "SEARCH_HOA_DON":
                    return new Response("SUCCESS", new iuh.fit.son23641341.nhahanglau_phantan.dao.HoaDon_DAO().timKiemHoaDon((String) data), "Success");

                case "SEARCH_PHIEU_DAT":
                    return new Response("SUCCESS", new iuh.fit.son23641341.nhahanglau_phantan.dao.PhieuDat_DAO().timKiemPhieuDat((String) data), "Success");

                case "SEARCH_KHACH_HANG":
                    return new Response("SUCCESS", new iuh.fit.son23641341.nhahanglau_phantan.dao.KhachHang_DAO().timKhachHangTheoSDT((String) data), "Success");

                case "SEARCH_MONAN_ADVANCED":
                    // data: Object[] {keyword, searchByCode, searchByOrder, searchByBill, searchByCustomer, searchByTable}
                    Object[] smParams = (Object[]) data;
                    return new Response("SUCCESS", new iuh.fit.son23641341.nhahanglau_phantan.dao.TimKiemChung_DAO().timKiemMonAn(
                        (String)smParams[0], (boolean)smParams[1], (boolean)smParams[2], (boolean)smParams[3], (boolean)smParams[4], (boolean)smParams[5]
                    ), "Success");

                case "GET_ALL_MONAN_TIMKIEM":
                    return new Response("SUCCESS", new iuh.fit.son23641341.nhahanglau_phantan.dao.TimKiemChung_DAO().getAllMonAn(), "Success");

                case "ADD_HOA_DON":
                    return new Response("SUCCESS", new iuh.fit.son23641341.nhahanglau_phantan.dao.HoaDon_DAO().addHoaDon((iuh.fit.son23641341.nhahanglau_phantan.entity.HoaDon) data), "Success");

                case "GET_ALL_NHANVIEN":
                    return new Response("SUCCESS", new iuh.fit.son23641341.nhahanglau_phantan.dao.NhanVien_DAO().getAllNhanVien(), "Success");

                case "GET_DOANH_THU_NAM":
                    return new Response("SUCCESS", ThongKe_Ctr.getInstance().TinhTongDoanhThuNam((int) data), "Success");

                case "GET_AVG_HOA_DON_NAM":
                    return new Response("SUCCESS", ThongKe_Ctr.getInstance().TinhDoanhSoTrungBinhHoaDon((int) data), "Success");

                case "GET_DOANH_THU_NGAY":
                    return new Response("SUCCESS", ThongKe_Ctr.getInstance().TinhTongDoanhThuNgay((java.time.LocalDate) data), "Success");

                case "GET_AVG_HOA_DON_NGAY":
                    return new Response("SUCCESS", ThongKe_Ctr.getInstance().TinhDoanhSoTrungBinhHoaDonTheoNgay((java.time.LocalDate) data), "Success");

                default:
                    return new Response("ERROR", null, "Unknown action: " + action);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Response("ERROR", null, "Server error: " + e.getMessage());
        }
    }
}
