package iuh.fit.son23641341.nhahanglau_phantan.gui;

import iuh.fit.son23641341.nhahanglau_phantan.entity.MonAn;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import iuh.fit.son23641341.nhahanglau_phantan.control.MonAn_Ctr;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ThemMonAn_Dialog extends JDialog implements ActionListener {
    private final JTextField txtTenMon, txtGia, txtMoTa;
    private final JComboBox<String> cmbLoaiMon;
    private final JButton btnThem, btnHuy;
    private MonAn monAnMoi = null;

    public MonAn getMonAnMoi() {
        return monAnMoi;
    }

    public ThemMonAn_Dialog(JFrame parent) {
        super(parent, "Thêm Món Ăn Mới", true);
        setSize(500, 400);
        setLayout(new BorderLayout(0, 10));
        setLocationRelativeTo(parent);

        // --- Panel Tiêu Đề ---
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(44, 62, 80));
        titlePanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel titleLabel = new JLabel("THÊM MÓN ĂN MỚI");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // --- Panel Nội Dung ---
        JPanel contentPanel = new JPanel(null);
        contentPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        contentPanel.setBackground(new Color(248, 249, 250));

        int labelWidth = 120;
        int fieldWidth = 300;
        int height = 30;
        int startX = 10;
        int startY = 10;
        int gap = 20;

        Font labelFont = new Font("Arial", Font.BOLD, 13);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

    // Mã món (tự sinh, không chỉnh sửa) - removed from dialog

        //  Tên món
        JLabel lblTenMon = createStyledLabel("Tên món (*)", labelFont);
        lblTenMon.setBounds(startX, startY + (gap + height), labelWidth, height);
        txtTenMon = createStyledTextField(fieldFont);
        txtTenMon.setBounds(startX + labelWidth, startY + (gap + height), fieldWidth, height);

        //  Loại món
        JLabel lblLoaiMon = createStyledLabel("Loại món (*)", labelFont);
        lblLoaiMon.setBounds(startX, startY + 2 * (gap + height), labelWidth, height);
        cmbLoaiMon = createStyledComboBox(new String[]{"Khai vị", "Món chính", "Tráng miệng", "Đồ uống"}, fieldFont);
        cmbLoaiMon.setBounds(startX + labelWidth, startY + 2 * (gap + height), fieldWidth, height);

        // Giá
        JLabel lblGia = createStyledLabel("Giá (VND) (*)", labelFont);
        lblGia.setBounds(startX, startY + 3 * (gap + height), labelWidth, height);
        txtGia = createStyledTextField(fieldFont);
        txtGia.setBounds(startX + labelWidth, startY + 3 * (gap + height), fieldWidth, height);

        //  Mô tả
        JLabel lblMoTa = createStyledLabel("Mô tả", labelFont);
        lblMoTa.setBounds(startX, startY + 4 * (gap + height), labelWidth, height);
        txtMoTa = createStyledTextField(fieldFont);
        txtMoTa.setBounds(startX + labelWidth, startY + 4 * (gap + height), fieldWidth, height);

    // Mã món field removed from dialog UI
        contentPanel.add(lblTenMon);
        contentPanel.add(txtTenMon);
        contentPanel.add(lblLoaiMon);
        contentPanel.add(cmbLoaiMon);
        contentPanel.add(lblGia);
        contentPanel.add(txtGia);
        contentPanel.add(lblMoTa);
        contentPanel.add(txtMoTa);

        // Mã món sẽ được sinh bởi controller khi thêm mới
        // updateMaMon logic removed; mã món sẽ do controller xử lý

        add(contentPanel, BorderLayout.CENTER);

        // --- Panel Button ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(248, 249, 250));
        btnThem = createStyledButton("Thêm", new Color(39, 174, 96));
        btnHuy = createStyledButton("Hủy", new Color(192, 57, 43));
        btnThem.addActionListener(this);
        btnHuy.addActionListener(this);
        buttonPanel.add(btnThem);
        buttonPanel.add(btnHuy);
        add(buttonPanel, BorderLayout.SOUTH);

    }

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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnHuy) {
            monAnMoi = null;
            dispose();
        } else if (e.getSource() == btnThem) {
            if (validateInputs()) {
                try {
                    String tenMon = txtTenMon.getText().trim();
                    String loaiMon = (String) cmbLoaiMon.getSelectedItem();
                    String giaStr = txtGia.getText().trim().replaceAll("[^0-9.]", "");
                    double gia = Double.parseDouble(giaStr);
                    String moTa = txtMoTa.getText().trim();
               
                    String maMon = "MA00";
                  
                    monAnMoi = new MonAn(maMon, tenMon, loaiMon, gia, moTa);
                    MonAn_Ctr ctr = new MonAn_Ctr();
                    Boolean themMon = ctr.themMonAn(monAnMoi);
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ: " + ex.getMessage(), "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private boolean validateInputs() {
        String tenMon = txtTenMon.getText().trim();
        String giaStr = txtGia.getText().trim().replaceAll("[^0-9.]", "");
        if (tenMon.isEmpty() || giaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên món và giá không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            double gia = Double.parseDouble(giaStr);
            if (gia <= 0) {
                JOptionPane.showMessageDialog(this, "Giá phải lớn hơn 0.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Giá phải là số hợp lệ.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    // Loại món đã được giới hạn bởi comboBox nên luôn hợp lệ
    return true;
    }
}
