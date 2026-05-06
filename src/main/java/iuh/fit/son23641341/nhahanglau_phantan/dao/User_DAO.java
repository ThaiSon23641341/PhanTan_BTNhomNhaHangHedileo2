package iuh.fit.son23641341.nhahanglau_phantan.dao;

import iuh.fit.son23641341.nhahanglau_phantan.entity.NhanVien;
import iuh.fit.son23641341.nhahanglau_phantan.entity.User;
import iuh.fit.son23641341.nhahanglau_phantan.mock.MockData;

// NOTE: Database logic removed; authentication uses in-memory mock data.
public class User_DAO implements IUserDAO {

    private static User_DAO instance;

    public User_DAO() {
    }

    public static User_DAO getInstance() {
        if (instance == null) {
            instance = new User_DAO();
        }
        return instance;
    }

    @Override
    public boolean authenticate(String username, String password) {
        return MockData.users().stream()
            .anyMatch(user -> user.getTenNguoiDung().equals(username) && user.getMatKhau().equals(password));
    }

    public String getChucVuByUsername(String username) {
        NhanVien nv = findNhanvienByUsername(username);
        return nv != null ? nv.getChucVu() : null;
    }

    public String getHoTenByUsername(String username) {
        NhanVien nv = findNhanvienByUsername(username);
        return nv != null ? nv.getHoten() : null;
    }

    public boolean timKiemTaiKhoan(String username, String phone, String email) {
        return MockData.nhanViens().stream()
            .anyMatch(nv -> nv.getHoten().equals(username) && nv.getSdt().equals(phone) && nv.getEmail().equals(email));
    }

    public boolean doiMatKhau(String hoTenNhanVien, String newPassword) {
        NhanVien nv = MockData.nhanViens().stream()
            .filter(item -> item.getHoten().equals(hoTenNhanVien))
            .findFirst()
            .orElse(null);
        if (nv == null) {
            return false;
        }
        User user = findUserById(nv.getIdUser());
        if (user == null) {
            return false;
        }
        user.setMatKhau(newPassword);
        return true;
    }

    private NhanVien findNhanvienByUsername(String username) {
        User user = MockData.users().stream()
            .filter(item -> item.getTenNguoiDung().equals(username))
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

    private User findUserById(int idUser) {
        return MockData.users().stream()
            .filter(user -> parseUserId(user.getiD()) == idUser)
            .findFirst()
            .orElse(null);
    }

    private int parseUserId(String id) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException ignored) {
            return -1;
        }
    }
}
