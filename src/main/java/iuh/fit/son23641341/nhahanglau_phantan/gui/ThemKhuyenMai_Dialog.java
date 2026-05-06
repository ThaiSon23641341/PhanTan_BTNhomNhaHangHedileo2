package iuh.fit.son23641341.nhahanglau_phantan.gui;

import iuh.fit.son23641341.nhahanglau_phantan.control.KhuyenMai_Ctr;
import iuh.fit.son23641341.nhahanglau_phantan.entity.KhuyenMai;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Dialog thêm khuyến mãi. Sử dụng KhuyenMai_Ctr để thêm vào danh sách.
 */
public class ThemKhuyenMai_Dialog extends JDialog implements ActionListener {
    private final JTextField txtTen, txtPhanTram, txtNgayBD, txtNgayKT;
    private final JTextArea txtMoTa;
    private final JButton btnThem, btnHuy;
    private KhuyenMai khuyenMaiMoi = null;
    private final KhuyenMai_Ctr ctr;

    private final DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ThemKhuyenMai_Dialog(JFrame parent, KhuyenMai_Ctr ctr) {
        super(parent, "Thêm Khuyến Mãi", true);
        this.ctr = ctr == null ? new KhuyenMai_Ctr() : ctr;
        setSize(520, 420);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(0, 10));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(44, 62, 80));
        titlePanel.setBorder(new EmptyBorder(12, 16, 12, 16));
        JLabel title = new JLabel("THÊM KHUYẾN MÃI MỚI");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        titlePanel.add(title);
        add(titlePanel, BorderLayout.NORTH);

        JPanel content = new JPanel(null);
        content.setBackground(new Color(248, 249, 250));
        content.setBorder(new EmptyBorder(16, 20, 16, 20));

        Font lblFont = new Font("Arial", Font.BOLD, 13);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        int lx = 10, ly = 10, labelW = 160, fieldW = 320, h = 30, gap = 18;

        JLabel lTen = new JLabel("Tên khuyến mãi (*)");
        lTen.setFont(lblFont);
        lTen.setBounds(lx, ly, labelW, h);
        txtTen = new JTextField();
        txtTen.setFont(fieldFont);
        txtTen.setBounds(lx + labelW, ly, fieldW, h);

        ly += gap + h;
        JLabel lPhanTram = new JLabel("Phần trăm giảm (%) (*)");
        lPhanTram.setFont(lblFont);
        lPhanTram.setBounds(lx, ly, labelW, h);
        txtPhanTram = new JTextField();
        txtPhanTram.setFont(fieldFont);
        txtPhanTram.setBounds(lx + labelW, ly, fieldW, h);

        ly += gap + h;
        JLabel lNgayBD = new JLabel("Ngày bắt đầu (dd/MM/yyyy) (*)");
        lNgayBD.setFont(lblFont);
        lNgayBD.setBounds(lx, ly, labelW, h);
        txtNgayBD = new JTextField();
        txtNgayBD.setFont(fieldFont);
        txtNgayBD.setBounds(lx + labelW, ly, fieldW, h);

        ly += gap + h;
        JLabel lNgayKT = new JLabel("Ngày kết thúc (dd/MM/yyyy) (*)");
        lNgayKT.setFont(lblFont);
        lNgayKT.setBounds(lx, ly, labelW, h);
        txtNgayKT = new JTextField();
        txtNgayKT.setFont(fieldFont);
        txtNgayKT.setBounds(lx + labelW, ly, fieldW, h);

        ly += gap + h;
        JLabel lMoTa = new JLabel("Mô tả");
        lMoTa.setFont(lblFont);
        lMoTa.setBounds(lx, ly, labelW, h);
        txtMoTa = new JTextArea();
        txtMoTa.setFont(fieldFont);
        JScrollPane spMoTa = new JScrollPane(txtMoTa);
        spMoTa.setBounds(lx + labelW, ly, fieldW, 80);

        content.add(lTen); content.add(txtTen);
        content.add(lPhanTram); content.add(txtPhanTram);
        content.add(lNgayBD); content.add(txtNgayBD);
        content.add(lNgayKT); content.add(txtNgayKT);
        content.add(lMoTa); content.add(spMoTa);

        add(content, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        buttonPanel.setBackground(Color.WHITE);
        btnThem = new JButton("Thêm");
        btnThem.setBackground(new Color(39, 174, 96));
        btnThem.setForeground(Color.WHITE);
        btnThem.setFocusPainted(false);
        btnHuy = new JButton("Hủy");
        btnHuy.setBackground(new Color(192, 57, 43));
        btnHuy.setForeground(Color.WHITE);
        btnHuy.setFocusPainted(false);

        btnThem.addActionListener(this);
        btnHuy.addActionListener(this);
        buttonPanel.add(btnHuy);
        buttonPanel.add(btnThem);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public ThemKhuyenMai_Dialog(JFrame parent) {
        this(parent, null);
    }

    public KhuyenMai getKhuyenMaiMoi() {
        return khuyenMaiMoi;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnHuy) {
            khuyenMaiMoi = null;
            dispose();
            return;
        }
        if (e.getSource() == btnThem) {
            if (!validateInputs()) return;

            try {
                String ten = txtTen.getText().trim();
                double phanTram = Double.parseDouble(txtPhanTram.getText().trim());
                String bd = txtNgayBD.getText().trim();
                String kt = txtNgayKT.getText().trim();
                String moTa = txtMoTa.getText().trim();

                // Tạo mã khuyến mãi tiếp theo giống logic trong controller
                String ma = taoMaTiepTheoLocal();

                // Tạo đối tượng KhuyenMai, hỏi xác nhận và để dialog tự thêm vào controller
                KhuyenMai km = new KhuyenMai(ma, ten, phanTram, bd, kt, moTa);
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Bạn có chắc chắn muốn thêm khuyến mãi '" + km.getTenKhuyenMai() + "'?",
                        "Xác nhận thêm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean added = ctr.themKhuyenMai(km);
                    if (added) {
                        khuyenMaiMoi = km;
                        JOptionPane.showMessageDialog(this, "Thêm khuyến mãi thành công.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Không thể thêm khuyến mãi (trùng mã hoặc lỗi).", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi không xác định: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validateInputs() {
        String ten = txtTen.getText().trim();
        String ph = txtPhanTram.getText().trim();
        String bd = txtNgayBD.getText().trim();
        String kt = txtNgayKT.getText().trim();

        if (ten.isEmpty() || ph.isEmpty() || bd.isEmpty() || kt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ các trường bắt buộc (*)", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        double phanTram;
        try {
            phanTram = Double.parseDouble(ph);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Phần trăm giảm phải là một số.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (phanTram < 0 || phanTram > 100) {
            JOptionPane.showMessageDialog(this, "Phần trăm giảm phải trong khoảng 0 - 100.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            LocalDate d1 = LocalDate.parse(bd, df);
            LocalDate d2 = LocalDate.parse(kt, df);
            if (d2.isBefore(d1)) {
                JOptionPane.showMessageDialog(this, "Ngày kết thúc phải sau hoặc bằng ngày bắt đầu.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Định dạng ngày không hợp lệ. Vui lòng dùng dd/MM/yyyy.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    // Tạo mã theo cùng logic với controller (dựa trên danh sách hiện tại)
    private String taoMaTiepTheoLocal() {
        int max = 0;
        for (KhuyenMai k : ctr.getDanhSachKhuyenMai()) {
            String ma = k.getMaKhuyenMai();
            if (ma != null && ma.toUpperCase().startsWith("KM")) {
                try {
                    int so = Integer.parseInt(ma.substring(2));
                    if (so > max) max = so;
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return String.format("KM%03d", max + 1);
    }
}
