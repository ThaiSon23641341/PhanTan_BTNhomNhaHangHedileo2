package iuh.fit.son23641341.nhahanglau_phantan.gui;

import javax.swing.*;
import java.awt.*;
import iuh.fit.son23641341.nhahanglau_phantan.entity.NhanVien;
import iuh.fit.son23641341.nhahanglau_phantan.dao.NhanVien_DAO;

public class NhanVien_Dialog extends JDialog {
    private JTextField txtMaNV, txtHoTen, txtSdt, txtEmail, txtUser, txtPass;
    private JComboBox<String> cbGioiTinh, cbCaLam, cbChucVu;
    private JButton btnAction, btnHuy;
    
    private NhanVien_DAO nhanVienDAO = new NhanVien_DAO();
    private boolean isEditMode = false;
    private int idUserUpdate = -1; // Lưu id tài khoản khi sửa
    private NhanVien nhanVienMoi; // Dùng để trả về cho màn hình chính sau khi thêm/sửa

    // Constructor cho THÊM MỚI
    public NhanVien_Dialog(Frame parent) {
        super(parent, "Thêm Nhân Viên Mới", true);
        this.isEditMode = false;
        initUI();
    }

    // Constructor cho SỬA
    public NhanVien_Dialog(Frame parent, NhanVien nv, String user, String pass, int idUser) {
        super(parent, "Cập Nhật Thông Tin Nhân Viên", true);
        this.isEditMode = true;
        this.idUserUpdate = idUser;
        initUI();
        
        // Đổ dữ liệu cũ vào các ô nhập
        txtMaNV.setText(nv.getManv());
        txtMaNV.setEditable(false); // Không cho sửa Mã (Khóa chính)
        txtHoTen.setText(nv.getHoten());
        cbGioiTinh.setSelectedItem(nv.getGioiTinh());
        cbCaLam.setSelectedItem(nv.getCaLamViec());
        txtSdt.setText(nv.getSdt());
        txtEmail.setText(nv.getEmail());
        cbChucVu.setSelectedItem(nv.getChucVu());
        txtUser.setText(user);
        txtPass.setText(pass);
        
        btnAction.setText("Lưu thay đổi");
    }

    private void initUI() {
        setSize(450, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel pnMain = new JPanel(new GridLayout(9, 2, 10, 10));
        pnMain.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        pnMain.add(new JLabel("Mã Nhân Viên:"));
        txtMaNV = new JTextField();
        pnMain.add(txtMaNV);

        pnMain.add(new JLabel("Họ Tên:"));
        txtHoTen = new JTextField();
        pnMain.add(txtHoTen);

        pnMain.add(new JLabel("Giới Tính:"));
        cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        pnMain.add(cbGioiTinh);

        pnMain.add(new JLabel("Ca Làm Việc:"));
        cbCaLam = new JComboBox<>(new String[]{"Sáng", "Chiều", "Tối"});
        pnMain.add(cbCaLam);

        pnMain.add(new JLabel("Số Điện Thoại:"));
        txtSdt = new JTextField();
        pnMain.add(txtSdt);

        pnMain.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        pnMain.add(txtEmail);

        pnMain.add(new JLabel("Chức Vụ:"));
        cbChucVu = new JComboBox<>(new String[]{"Quan ly", "Thu ngan"});
        pnMain.add(cbChucVu);

        pnMain.add(new JLabel("Tên đăng nhập:"));
        txtUser = new JTextField();
        pnMain.add(txtUser);

        pnMain.add(new JLabel("Mật khẩu:"));
        txtPass = new JPasswordField();
        pnMain.add(txtPass);

        JPanel pnSouth = new JPanel();
        btnAction = new JButton("Thêm");
        btnHuy = new JButton("Hủy");
        pnSouth.add(btnAction);
        pnSouth.add(btnHuy);

        add(pnMain, BorderLayout.CENTER);
        add(pnSouth, BorderLayout.SOUTH);

        // Sự kiện nút Hủy
        btnHuy.addActionListener(e -> dispose());

        // Sự kiện nút Thêm/Lưu
        btnAction.addActionListener(e -> {
			try {
				xuLyXacNhan();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
    }

    private void xuLyXacNhan() throws Exception {
        // 1. Lấy dữ liệu từ giao diện
        String ma = txtMaNV.getText().trim();
        String ten = txtHoTen.getText().trim();
        String gt = cbGioiTinh.getSelectedItem().toString();
        String ca = cbCaLam.getSelectedItem().toString();
        String sdt = txtSdt.getText().trim();
        String email = txtEmail.getText().trim();
        String cv = cbChucVu.getSelectedItem().toString();
        String user = txtUser.getText().trim();
        String pass = txtPass.getText().trim();

        // 2. Validate cơ bản
        if (ma.isEmpty() || ten.isEmpty() || user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ các trường bắt buộc!");
            return;
        }

        this.nhanVienMoi = new NhanVien(ma, ten, gt, ca, sdt, email, cv, -1); // idUser sẽ gán sau

        if (isEditMode) {
            // Chế độ Sửa: Gọi hàm cập nhật (Transaction)
            boolean ok = nhanVienDAO.capNhatNhanVienVaTaiKhoan(nhanVienMoi, idUserUpdate, user, pass);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại. Kiểm tra lại tên đăng nhập!");
            }
        } else {
            // Chế độ Thêm: Gọi hàm thêm mới
            int idMoi = nhanVienDAO.themTaiKhoanVaLayId(user, pass);
            if (idMoi != -1) {
                boolean ok = nhanVienDAO.themNhanVienVoiIdUser(nhanVienMoi, idMoi);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
                    dispose();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Tên đăng nhập đã tồn tại!");
            }
        }
    }

    public NhanVien getNhanVienMoi() {
        return nhanVienMoi;
    }
}
