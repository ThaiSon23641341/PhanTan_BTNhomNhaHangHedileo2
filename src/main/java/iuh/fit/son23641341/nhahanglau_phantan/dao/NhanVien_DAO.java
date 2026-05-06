package iuh.fit.son23641341.nhahanglau_phantan.dao;

import iuh.fit.son23641341.nhahanglau_phantan.entity.NhanVien;
import iuh.fit.son23641341.nhahanglau_phantan.entity.User;
import iuh.fit.son23641341.nhahanglau_phantan.mock.MockData;

import java.util.ArrayList;

// NOTE: Database logic removed; DAO now uses in-memory mock data.
public class NhanVien_DAO {

    public static NhanVien timNhanVienTheoDangNhap(String username, String password) {
        User user = MockData.users().stream()
            .filter(item -> item.getTenNguoiDung().equals(username) && item.getMatKhau().equals(password))
            .findFirst()
            .orElse(null);
        if (user == null) {
            return null;
        }
        int idUser = parseUserId(user.getiD());
        return MockData.nhanViens().stream()
            .filter(item -> item.getIdUser() == idUser)
            .findFirst()
            .orElse(null);
    }

    public String getTenNhanVienByMa(String maNhanVien) {
        return MockData.nhanViens().stream()
            .filter(item -> item.getManv().equals(maNhanVien))
            .map(NhanVien::getHoten)
            .findFirst()
            .orElse(null);
    }

    public ArrayList<NhanVien> getAllNhanVien() {
        return new ArrayList<>(MockData.nhanViens());
    }

    public boolean themNhanVien(NhanVien nv) {
        if (nv == null || timTheoMa(nv.getManv()) != null) {
            return false;
        }
        return MockData.nhanViens().add(nv);
    }

    public boolean capNhatNhanVien(NhanVien nv) {
        if (nv == null) {
            return false;
        }
        NhanVien existing = timTheoMa(nv.getManv());
        if (existing == null) {
            return false;
        }
        try {
            existing.setHoten(nv.getHoten());
            existing.setGioiTinh(nv.getGioiTinh());
            existing.setCaLamViec(nv.getCaLamViec());
            existing.setSdt(nv.getSdt());
            existing.setEmail(nv.getEmail());
            existing.setChucVu(nv.getChucVu());
        } catch (Exception ignored) {
            return false;
        }
        return true;
    }

    public boolean xoaNhanVien(String maNV, String idUser) {
        NhanVien existing = timTheoMa(maNV);
        if (existing == null) {
            return false;
        }
        MockData.nhanViens().remove(existing);
        MockData.users().removeIf(user -> user.getiD().equals(idUser));
        return true;
    }

    public int themTaiKhoanVaLayId(String tenNguoiDung, String matKhau) {
        int newId = MockData.users().stream()
            .map(User::getiD)
            .mapToInt(NhanVien_DAO::parseUserId)
            .max()
            .orElse(0) + 1;
        MockData.users().add(new User(String.valueOf(newId), tenNguoiDung, matKhau));
        return newId;
    }

    public boolean themNhanVienVoiIdUser(NhanVien nv, int idUser) {
        if (nv == null || timTheoMa(nv.getManv()) != null) {
            return false;
        }
        try {
            nv = new NhanVien(nv.getManv(), nv.getHoten(), nv.getGioiTinh(), nv.getCaLamViec(), nv.getSdt(),
                nv.getEmail(), nv.getChucVu(), idUser);
        } catch (Exception ignored) {
            return false;
        }
        return MockData.nhanViens().add(nv);
    }

    public boolean capNhatNhanVienVaTaiKhoan(NhanVien nv, int idUser, String user, String pass) {
        if (!capNhatNhanVien(nv)) {
            return false;
        }
        User existingUser = MockData.users().stream()
            .filter(item -> parseUserId(item.getiD()) == idUser)
            .findFirst()
            .orElse(null);
        if (existingUser == null) {
            return false;
        }
        existingUser.setTenNguoiDung(user);
        existingUser.setMatKhau(pass);
        return true;
    }

    public static Object[] getThongTinSua(String maNV) throws Exception {
        NhanVien nv = MockData.nhanViens().stream()
            .filter(item -> item.getManv().equals(maNV))
            .findFirst()
            .orElse(null);
        if (nv == null) {
            return null;
        }
        User user = MockData.users().stream()
            .filter(item -> parseUserId(item.getiD()) == nv.getIdUser())
            .findFirst()
            .orElse(null);
        if (user == null) {
            return null;
        }
        return new Object[] { nv, user.getTenNguoiDung(), user.getMatKhau(), nv.getIdUser() };
    }

    private NhanVien timTheoMa(String maNV) {
        return MockData.nhanViens().stream()
            .filter(item -> item.getManv().equals(maNV))
            .findFirst()
            .orElse(null);
    }

    private static int parseUserId(String id) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException ignored) {
            return -1;
        }
    }
}
