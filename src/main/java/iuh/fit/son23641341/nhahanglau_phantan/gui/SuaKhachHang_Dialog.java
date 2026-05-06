package iuh.fit.son23641341.nhahanglau_phantan.gui;

import iuh.fit.son23641341.nhahanglau_phantan.control.KhachHang_Ctr; // Import Controller
import iuh.fit.son23641341.nhahanglau_phantan.entity.KhachHangThanhVien;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.format.DateTimeFormatter;


public class SuaKhachHang_Dialog extends JDialog implements ActionListener {
    private final JTextField txtMaKH, txtHoTen, txtSDT, txtEmail, txtDiemSo;
    private final JComboBox<String> cmbGioiTinh, cmbThanhVien;
    private final JButton btnLuu, btnHuy;
    private KhachHangThanhVien khachHangThanhVienCapNhat = null;
    private final KhachHangThanhVien khachHangThanhVienGoc;
    
    // Thêm đối tượng Controller
    private final KhachHang_Ctr khachHangCtr;

    // Định dạng ngày 
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Lấy đối tượng KhachHang đã được cập nhật sau khi nút "Lưu Thay Đổi" được nhấn.
     * Trả về null nếu người dùng nhấn "Hủy".
     */
    public KhachHangThanhVien getKhachHangCapNhat() {
        return khachHangThanhVienCapNhat;
    }

    // Constructor phải nhận KhachHang cần sửa
    public SuaKhachHang_Dialog(JFrame parent, KhachHangThanhVien khachHangThanhVien) {

        super(parent, "Cập Nhật Khách Hàng", true);
        this.khachHangThanhVienGoc = khachHangThanhVien;
        this.khachHangCtr = new KhachHang_Ctr(); // Khởi tạo Controller
        
        setSize(550, 550);
        setLayout(new BorderLayout(0, 10));
        setLocationRelativeTo(parent);
        
        // --- Panel Tiêu Đề ---
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(44, 62, 80));
        titlePanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("CẬP NHẬT KHÁCH HÀNG");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        //  Panel Nội Dung 
        JPanel contentPanel = new JPanel(null);
        contentPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        contentPanel.setBackground(new Color(248, 249, 250));

        // Cấu hình chung cho labels và fields
        int labelWidth = 120;
        int fieldWidth = 330;
        int height = 30;
        int startX = 10;
        int startY = 10;
        int gap = 20;

        Font labelFont = new Font("Arial", Font.BOLD, 13);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);
        int currentY = startY;

        // Mã KH (Chỉ hiển thị, không cho sửa)
        JLabel lblMaKH = createStyledLabel("Mã KH:", labelFont);
        lblMaKH.setBounds(startX, currentY, labelWidth, height);
        txtMaKH = createStyledTextField(fieldFont);
        txtMaKH.setEditable(false); // Không cho sửa
        txtMaKH.setBackground(new Color(230, 230, 230));
        txtMaKH.setBounds(startX + labelWidth, currentY, fieldWidth, height);
        currentY += gap + height;

        // Họ Tên
        JLabel lblHoTen = createStyledLabel("Họ Tên (*)", labelFont);
        lblHoTen.setBounds(startX, currentY, labelWidth, height);
        txtHoTen = createStyledTextField(fieldFont);
        txtHoTen.setBounds(startX + labelWidth, currentY, fieldWidth, height);
        currentY += gap + height;

        // Số ĐT
        JLabel lblSDT = createStyledLabel("Số ĐT (*)", labelFont);
        lblSDT.setBounds(startX, currentY, labelWidth, height);
        txtSDT = createStyledTextField(fieldFont);
        txtSDT.setBounds(startX + labelWidth, currentY, fieldWidth, height);
        currentY += gap + height;

        // Email
        JLabel lblEmail = createStyledLabel("Email", labelFont);
        lblEmail.setBounds(startX, currentY, labelWidth, height);
        txtEmail = createStyledTextField(fieldFont);
        txtEmail.setBounds(startX + labelWidth, currentY, fieldWidth, height);
        currentY += gap + height;

        // Giới Tính
        JLabel lblGioiTinh = createStyledLabel("Giới Tính", labelFont);
        lblGioiTinh.setBounds(startX, currentY, labelWidth, height);
        cmbGioiTinh = createStyledComboBox(new String[]{"Nam", "Nữ"}, fieldFont);
        cmbGioiTinh.setBounds(startX + labelWidth, currentY, fieldWidth, height);
        currentY += gap + height;

        // Thành Viên (Không cho phép sửa)
        JLabel lblThanhVien = createStyledLabel("Cấp Thành Viên", labelFont);
        lblThanhVien.setBounds(startX, currentY, labelWidth, height);
        cmbThanhVien = createStyledComboBox(new String[]{"Kim cương", "Vàng", "Bạc", "Đồng"}, fieldFont);
        cmbThanhVien.setEnabled(false); // <--- KHÔNG CHO SỬA
        cmbThanhVien.setBounds(startX + labelWidth, currentY, fieldWidth, height);
        currentY += gap + height;

        // Điểm Tích Lũy (Không cho phép sửa)
        JLabel lblDiemSo = createStyledLabel("Điểm Tích Lũy (*)", labelFont);
        lblDiemSo.setBounds(startX, currentY, labelWidth, height);
        txtDiemSo = createStyledTextField(fieldFont);
        txtDiemSo.setEditable(false); // <--- KHÔNG CHO SỬA
        txtDiemSo.setBackground(new Color(230, 230, 230)); // Đổi màu nền cho dễ nhận biết
        txtDiemSo.setBounds(startX + labelWidth, currentY, fieldWidth, height);
        currentY += gap + height;

        // Ngày ĐK (Label only)
        JLabel lblNgayDK = createStyledLabel("Ngày ĐK:", labelFont);
        lblNgayDK.setBounds(startX, currentY, labelWidth, height);
        JLabel lblNgayDKValue = new JLabel(khachHangThanhVien.getNgayDangKy() != null ? khachHangThanhVien.getNgayDangKy().format(dateFormatter) : "N/A");
        lblNgayDKValue.setFont(new Font("Arial", Font.ITALIC, 13));
        lblNgayDKValue.setForeground(new Color(231, 76, 60));
        lblNgayDKValue.setBounds(startX + labelWidth, currentY, fieldWidth, height);

        // Add tất cả components vào panel
        contentPanel.add(lblMaKH);
        contentPanel.add(txtMaKH);
        contentPanel.add(lblHoTen);
        contentPanel.add(txtHoTen);
        contentPanel.add(lblSDT);
        contentPanel.add(txtSDT);
        contentPanel.add(lblEmail);
        contentPanel.add(txtEmail);
        contentPanel.add(lblGioiTinh);
        contentPanel.add(cmbGioiTinh);
        contentPanel.add(lblThanhVien);
        contentPanel.add(cmbThanhVien);
        contentPanel.add(lblDiemSo);
        contentPanel.add(txtDiemSo);
        contentPanel.add(lblNgayDK);
        contentPanel.add(lblNgayDKValue);

        // Gọi hàm điền dữ liệu gốc
        dienDuLieuGoc();

        //  Panel Nút Bấm 
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)));

        btnLuu = createStyledButton("Lưu Thay Đổi", new Color(52, 152, 219));
        btnHuy = createStyledButton("Hủy", new Color(108, 117, 125));
        
        btnLuu.addActionListener(this);
        btnHuy.addActionListener(this);

        buttonPanel.add(btnHuy);
        buttonPanel.add(btnLuu);

        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        contentPanel.setPreferredSize(new Dimension(500, currentY + height + 10));
    }
    
    /**
     * Điền dữ liệu của khách hàng gốc vào các trường.
     */

    private void dienDuLieuGoc() {
        if (khachHangThanhVienGoc != null) {
            txtMaKH.setText(khachHangThanhVienGoc.getMaKhachHang().trim());
            txtHoTen.setText(khachHangThanhVienGoc.getHoTen());
            txtSDT.setText(khachHangThanhVienGoc.getSoDienThoai().trim());
            txtEmail.setText(khachHangThanhVienGoc.getEmail());
            // Giữ nguyên giá trị Điểm Tích Lũy gốc
            txtDiemSo.setText(String.valueOf(khachHangThanhVienGoc.getDiemTichLuy()));
            
            String gioiTinh = khachHangThanhVienGoc.getGioiTinh().trim();
            String thanhVien = khachHangThanhVienGoc.getThanhVien().trim();
            
            cmbGioiTinh.setSelectedItem(gioiTinh);
            cmbThanhVien.setSelectedItem(thanhVien);
        }
    }

    // Phương thức Helper (Giữ nguyên)

    private JLabel createStyledLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(new Color(33, 33, 33));
        return label;
    }

    private JTextField createStyledTextField(Font font) {
        JTextField textField = new JTextField();
        textField.setFont(font);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return textField;
    }

    private JComboBox<String> createStyledComboBox(String[] items, Font font) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(font);
        comboBox.setBackground(Color.WHITE);
        ((JComponent) comboBox.getRenderer()).setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return comboBox;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
        return button;
    }

    // --- Xử lý Sự kiện ---

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnHuy) {
            khachHangThanhVienCapNhat = null;
            dispose();
        } else if (e.getSource() == btnLuu) {
            if (validateInputs()) {
                
                String sdtMoi = txtSDT.getText().trim();
                
                // BƯỚC 1: Kiểm tra trùng SDT với khách hàng khác (trừ chính nó)
                if (khachHangCtr.kiemTraTrungSDTKhac(sdtMoi, khachHangThanhVienGoc.getMaKhachHang())) {
                    JOptionPane.showMessageDialog(this, "Số điện thoại " + sdtMoi + " đã bị trùng với khách hàng khác.", "Lỗi trùng lặp", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // KHÔNG LẤY GIÁ TRỊ TỪ txtDiemSo và cmbThanhVien MÀ SỬ DỤNG GIÁ TRỊ GỐC TRONG khachHangGoc
                // (vì các trường này đã bị disabled)
                
                try {
                    // Cập nhật Entity gốc với dữ liệu mới
                    khachHangThanhVienGoc.setHoTen(txtHoTen.getText().trim());
                    khachHangThanhVienGoc.setSoDienThoai(sdtMoi);
                    khachHangThanhVienGoc.setEmail(txtEmail.getText().trim());
                    khachHangThanhVienGoc.setGioiTinh(((String) cmbGioiTinh.getSelectedItem()).trim());
                    
                    // BƯỚC 2: Gọi phương thức cập nhật vào Database
                    boolean success = khachHangCtr.capNhatKhachHang(khachHangThanhVienGoc);

                    if (success) {
                        khachHangThanhVienCapNhat = khachHangThanhVienGoc; // Gán lại để lấy kết quả
                        JOptionPane.showMessageDialog(this, "Cập nhật khách hàng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Cập nhật khách hàng thất bại. Vui lòng kiểm tra lại thông tin.", "Lỗi Cập Nhật", JOptionPane.ERROR_MESSAGE);
                    }
                    
                } catch (Exception ex) { // Catch tất cả các ngoại lệ từ setter (ví dụ: IllegalArgumentException)
                     JOptionPane.showMessageDialog(this, "Lỗi nhập liệu: " + ex.getMessage(), "Lỗi Cập Nhật", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * ⭐ ĐÃ SỬA: Kiểm tra tính hợp lệ của dữ liệu nhập vào (tên, SDT, email, điểm số).
     */
    
    private boolean validateInputs() {
        String hoTen = txtHoTen.getText().trim();
        String sdt = txtSDT.getText().trim();
        String email = txtEmail.getText().trim();
        // Kiểm tra DiemSo vẫn cần thiết, mặc dù nó không được sửa
        String diemSo = txtDiemSo.getText().trim();

        // Kiểm tra bắt buộc: Họ tên, Số điện thoại, và Điểm số
        if (hoTen.isEmpty() || sdt.isEmpty() || diemSo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Các trường có dấu (*) không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Kiểm tra định dạng Số điện thoại
        Pattern phonePattern = Pattern.compile("^0[0-9]{9,10}$");
        Matcher phoneMatcher = phonePattern.matcher(sdt);
        if (!phoneMatcher.matches()) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ (ví dụ: 0912345678).", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Kiểm tra Điểm số phải là số
        try {
            float diem = Float.parseFloat(diemSo);
            if (diem < 0) {
                // Điều kiện này luôn đúng nếu dữ liệu gốc hợp lệ, nhưng vẫn giữ để phòng lỗi dữ liệu DB
                JOptionPane.showMessageDialog(this, "Điểm Tích Lũy không được là số âm.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Điểm Tích Lũy phải là một số.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // ⭐ ĐÃ SỬA: Kiểm tra định dạng Email (chỉ khi có nhập)
        if (!email.isEmpty()) {
            Pattern emailPattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
            Matcher emailMatcher = emailPattern.matcher(email);
            if (!emailMatcher.matches()) {
                JOptionPane.showMessageDialog(this, "Địa chỉ Email không hợp lệ.", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        
        return true;
    }
}
