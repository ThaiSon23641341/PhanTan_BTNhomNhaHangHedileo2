package iuh.fit.son23641341.nhahanglau_phantan.gui;

import iuh.fit.son23641341.nhahanglau_phantan.entity.KhachHangThanhVien;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ThemKhachHang_Dialog extends JDialog implements ActionListener {
    private final JTextField txtHoTen, txtSDT, txtEmail, txtDiemSo;
    private final JComboBox<String> cmbGioiTinh, cmbThanhVien;  
    private final JButton btnThem, btnHuy;
    private KhachHangThanhVien khachHangThanhVienMoi = null;

    /**
     * Lấy đối tượng KhachHang mới được tạo sau khi nút "Thêm" được nhấn.
     * Trả về null nếu người dùng nhấn "Hủy".
     */
    public KhachHangThanhVien getKhachHangMoi() {
        return khachHangThanhVienMoi;
    }

    public ThemKhachHang_Dialog(JFrame parent) {
        super(parent, "Thêm Khách Hàng Mới", true); 
        setSize(550, 500);
        setLayout(new BorderLayout(0, 10));
        setLocationRelativeTo(parent);
        
        // Panel Tiêu Đề
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(44, 62, 80));
        titlePanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel titleLabel = new JLabel("THÊM KHÁCH HÀNG MỚI");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        //  Panel Nội Dung ---
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

        // Helper method để tạo và style label
        Font labelFont = new Font("Arial", Font.BOLD, 13);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);
        int currentY = startY;

        //  Họ Tên
        JLabel lblHoTen = createStyledLabel("Họ Tên (*)", labelFont);
        lblHoTen.setBounds(startX, currentY, labelWidth, height);
        txtHoTen = createStyledTextField(fieldFont);
        txtHoTen.setBounds(startX + labelWidth, currentY, fieldWidth, height);
        currentY += gap + height;

        //  Số ĐT
        JLabel lblSDT = createStyledLabel("Số ĐT (*)", labelFont);
        lblSDT.setBounds(startX, currentY, labelWidth, height);
        txtSDT = createStyledTextField(fieldFont);
        txtSDT.setBounds(startX + labelWidth, currentY, fieldWidth, height);
        currentY += gap + height;

        //  Email
        JLabel lblEmail = createStyledLabel("Email", labelFont);
        lblEmail.setBounds(startX, currentY, labelWidth, height);
        txtEmail = createStyledTextField(fieldFont);
        txtEmail.setBounds(startX + labelWidth, currentY, fieldWidth, height);
        currentY += gap + height;

        //  Giới Tính
        JLabel lblGioiTinh = createStyledLabel("Giới Tính", labelFont);
        lblGioiTinh.setBounds(startX, currentY, labelWidth, height);
        cmbGioiTinh = createStyledComboBox(new String[]{"Nam", "Nữ"}, fieldFont);
        cmbGioiTinh.setBounds(startX + labelWidth, currentY, fieldWidth, height);
        currentY += gap + height;

        // Thành Viên (Không cho phép sửa, mặc định là Đồng)
        JLabel lblThanhVien = createStyledLabel("Cấp Thành Viên", labelFont);
        lblThanhVien.setBounds(startX, currentY, labelWidth, height);
        // Cấp độ theo DBS (Kim Cương/Vàng/Bạc/Đồng)
        cmbThanhVien = createStyledComboBox(new String[]{"Kim cương", "Vàng", "Bạc", "Đồng"}, fieldFont);
        cmbThanhVien.setSelectedItem("Đồng"); // Mặc định là Đồng
        cmbThanhVien.setEnabled(false); // <--- KHÔNG CHO SỬA
        cmbThanhVien.setBounds(startX + labelWidth, currentY, fieldWidth, height);
        currentY += gap + height;

        //  Điểm Tích Lũy (Không cho phép sửa, mặc định là 0)
        JLabel lblDiemSo = createStyledLabel("Điểm Tích Lũy (*)", labelFont);
        lblDiemSo.setBounds(startX, currentY, labelWidth, height);
        txtDiemSo = createStyledTextField(fieldFont);
        txtDiemSo.setText("0");
        txtDiemSo.setEditable(false); // <--- KHÔNG CHO SỬA
        txtDiemSo.setBackground(new Color(230, 230, 230)); // Đổi màu nền cho dễ nhận biết
        txtDiemSo.setBounds(startX + labelWidth, currentY, fieldWidth, height);
        currentY += gap + height;

        //  Ngày ĐK (Label only)
        JLabel lblNgayDK = createStyledLabel("Ngày ĐK:", labelFont);
        lblNgayDK.setBounds(startX, currentY, labelWidth, height);
        JLabel lblNgayDKAuto = new JLabel("(Tự động tạo)");
        lblNgayDKAuto.setFont(new Font("Arial", Font.ITALIC, 13));
        lblNgayDKAuto.setForeground(Color.GRAY);
        lblNgayDKAuto.setBounds(startX + labelWidth, currentY, fieldWidth, height);
        currentY += gap + height;


        // Add tất cả components vào panel
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
        contentPanel.add(lblNgayDKAuto);

        // --- Panel Nút Bấm ---
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)));

        btnThem = createStyledButton("Thêm", new Color(46, 204, 113));
        btnHuy = createStyledButton("Hủy", new Color(108, 117, 125));
        
        btnThem.addActionListener(this);
        btnHuy.addActionListener(this);

        buttonPanel.add(btnHuy);    
        buttonPanel.add(btnThem);

        // Thêm panels vào dialog
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Set size cho content panel để chứa tất cả components
        contentPanel.setPreferredSize(new Dimension(500, currentY));
    }
    
    /**
     * Tạo label với style tùy chỉnh
     */
    private JLabel createStyledLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(new Color(33, 33, 33));
        return label;
    }

    /**
     * Tạo text field với style tùy chỉnh
     */
    private JTextField createStyledTextField(Font font) {
        JTextField textField = new JTextField();
        textField.setFont(font);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return textField;
    }

    /**
     * Tạo combo box với style tùy chỉnh
     */
    private JComboBox<String> createStyledComboBox(String[] items, Font font) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(font);
        comboBox.setBackground(Color.WHITE);
        ((JComponent) comboBox.getRenderer()).setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return comboBox;
    }

    /**
     * Tạo nút bấm với style tùy chỉnh.
     */
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnHuy) {
            khachHangThanhVienMoi = null;
            dispose(); 
        } else if (e.getSource() == btnThem) {
            if (validateInputs()) {
                try {
                    // Lấy giá trị Điểm Tích Lũy từ trường, mặc dù nó là "0" và không thể sửa
                    int diemTichLuy = Integer.parseInt(txtDiemSo.getText().trim());
                    
                    khachHangThanhVienMoi = new KhachHangThanhVien(
                                null, // Mã KH sẽ được Controller tạo
                                txtHoTen.getText().trim(),
                                txtSDT.getText().trim(),
                                txtEmail.getText().trim(), // Email có thể là rỗng ("")
                                (String) cmbGioiTinh.getSelectedItem(),
                                ((String) cmbThanhVien.getSelectedItem()).trim(), // Lấy giá trị Đồng
                                diemTichLuy, 
                                null // Ngày ĐK sẽ được Controller gán
                    );
                    dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Điểm Tích Lũy phải là số hợp lệ.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
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
                // Điều kiện này luôn đúng nếu txtDiemSo là "0"
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
