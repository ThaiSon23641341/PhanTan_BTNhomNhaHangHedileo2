package iuh.fit.son23641341.nhahanglau_phantan.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import iuh.fit.son23641341.nhahanglau_phantan.control.User_Ctr; // Đã đổi import

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuenMatKhau_Dialog extends JFrame {

    private JPanel contentPane;
    private JTextField txtTenNV;
    private JTextField txtSDT;
    private JTextField txtEmail;
    private JButton btnTimKiem;

    private JPanel pnlRightContent;
    private JPasswordField txtMatKhauMoi;
    private JPasswordField txtXacNhanMK;
    private JButton btnXacNhanDoi;

    // ĐÃ SỬA: Sử dụng Controller thay cho DAO
    private User_Ctr userCtr;
    private String foundEmployeeName = "";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                QuenMatKhau_Dialog frame = new QuenMatKhau_Dialog();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public QuenMatKhau_Dialog() {
        // ĐÃ SỬA: Lấy instance của Controller
        userCtr = User_Ctr.getInstance();

        // Cấu hình JFrame (Giữ nguyên giao diện của bạn)
        setTitle("Quên Mật Khẩu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(780, 445);
        setLocationRelativeTo(null);
        setResizable(false);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new GridLayout(0, 2, 0, 0));

        JPanel pnlLeft = new JPanel();
        pnlLeft.setBackground(new Color(255, 255, 255));
        pnlLeft.setBorder(new MatteBorder(0, 0, 0, 1, new Color(200, 200, 200)));
        contentPane.add(pnlLeft);
        pnlLeft.setLayout(null);

        JLabel lblTitleLeft = new JLabel("TÌM TÀI KHOẢN");
        lblTitleLeft.setForeground(new Color(0, 102, 204));
        lblTitleLeft.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitleLeft.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitleLeft.setBounds(0, 30, 380, 40);
        pnlLeft.add(lblTitleLeft);

        JLabel lblTen = new JLabel("Tên nhân viên:");
        lblTen.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTen.setBounds(40, 90, 300, 25);
        pnlLeft.add(lblTen);

        txtTenNV = new JTextField();
        txtTenNV.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTenNV.setBounds(40, 115, 300, 35);
        pnlLeft.add(txtTenNV);

        JLabel lblSDT = new JLabel("Số điện thoại:");
        lblSDT.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSDT.setBounds(40, 160, 300, 25);
        pnlLeft.add(lblSDT);

        txtSDT = new JTextField();
        txtSDT.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSDT.setBounds(40, 185, 300, 35);
        pnlLeft.add(txtSDT);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblEmail.setBounds(40, 230, 300, 25);
        pnlLeft.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtEmail.setBounds(40, 255, 300, 35);
        pnlLeft.add(txtEmail);

        btnTimKiem = new JButton("TÌM NHÂN VIÊN");
        btnTimKiem.setBackground(new Color(0, 102, 204));
        btnTimKiem.setForeground(Color.WHITE);
        btnTimKiem.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnTimKiem.setBounds(40, 320, 300, 40);
        btnTimKiem.setFocusPainted(false);
        pnlLeft.add(btnTimKiem);

        JPanel pnlRight = new JPanel();
        pnlRight.setBackground(new Color(245, 245, 245));
        contentPane.add(pnlRight);
        pnlRight.setLayout(null);

        pnlRightContent = new JPanel();
        pnlRightContent.setBackground(new Color(245, 245, 245));
        pnlRightContent.setBounds(0, 0, 380, 400);
        pnlRightContent.setLayout(null);
        pnlRightContent.setVisible(false);
        pnlRight.add(pnlRightContent);

        JLabel lblTitleRight = new JLabel("ĐẶT LẠI MẬT KHẨU");
        lblTitleRight.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitleRight.setForeground(new Color(0, 153, 51));
        lblTitleRight.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitleRight.setBounds(0, 30, 380, 40);
        pnlRightContent.add(lblTitleRight);

        JLabel lblPassMoi = new JLabel("Mật khẩu mới:");
        lblPassMoi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPassMoi.setBounds(40, 120, 300, 25);
        pnlRightContent.add(lblPassMoi);

        txtMatKhauMoi = new JPasswordField();
        txtMatKhauMoi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtMatKhauMoi.setBounds(40, 145, 300, 35);
        pnlRightContent.add(txtMatKhauMoi);

        JLabel lblXacNhan = new JLabel("Xác nhận mật khẩu:");
        lblXacNhan.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblXacNhan.setBounds(40, 200, 300, 25);
        pnlRightContent.add(lblXacNhan);

        txtXacNhanMK = new JPasswordField();
        txtXacNhanMK.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtXacNhanMK.setBounds(40, 225, 300, 35);
        pnlRightContent.add(txtXacNhanMK);

        btnXacNhanDoi = new JButton("XÁC NHẬN ĐỔI");
        btnXacNhanDoi.setForeground(Color.WHITE);
        btnXacNhanDoi.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnXacNhanDoi.setFocusPainted(false);
        btnXacNhanDoi.setBackground(new Color(0, 153, 51));
        btnXacNhanDoi.setBounds(40, 320, 300, 40);
        pnlRightContent.add(btnXacNhanDoi);

        // ================= XỬ LÝ SỰ KIỆN =================

        btnTimKiem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = txtTenNV.getText().trim();
                String phone = txtSDT.getText().trim();
                String email = txtEmail.getText().trim();

                if(name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(QuenMatKhau_Dialog.this,
                            "Vui lòng nhập đầy đủ thông tin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // ĐÃ SỬA: Gọi từ Controller
                boolean found = userCtr.timKiemTaiKhoan(name, phone, email);

                if (!found) {
                    int option = JOptionPane.showConfirmDialog(QuenMatKhau_Dialog.this,
                            "Không tìm thấy nhân viên!\nBạn có muốn quay lại màn hình đăng nhập?",
                            "Thông báo", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);

                    if (option == JOptionPane.YES_OPTION) {
                        new DangNhap_GUI().setVisible(true);
                        dispose();
                    }
                } else {
                    foundEmployeeName = name;
                    JOptionPane.showMessageDialog(QuenMatKhau_Dialog.this,
                            "Đã tìm thấy nhân viên: " + name + "\nVui lòng nhập mật khẩu mới.",
                            "Thành công", JOptionPane.INFORMATION_MESSAGE);

                    pnlRightContent.setVisible(true);
                    btnTimKiem.setEnabled(false);
                    txtTenNV.setEditable(false);
                    txtSDT.setEditable(false);
                    txtEmail.setEditable(false);
                }
            }
        });

        btnXacNhanDoi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newPass = new String(txtMatKhauMoi.getPassword());
                String confirmPass = new String(txtXacNhanMK.getPassword());

                if (newPass.isEmpty() || confirmPass.isEmpty()) {
                    JOptionPane.showMessageDialog(QuenMatKhau_Dialog.this, "Vui lòng nhập mật khẩu mới!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!newPass.equals(confirmPass)) {
                    JOptionPane.showMessageDialog(QuenMatKhau_Dialog.this, "Mật khẩu xác nhận không trùng khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // ĐÃ SỬA: Gọi từ Controller
                boolean success = userCtr.doiMatKhau(foundEmployeeName, newPass);

                if(success) {
                    JOptionPane.showMessageDialog(QuenMatKhau_Dialog.this, "Đổi mật khẩu hoàn tất!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    new DangNhap_GUI().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(QuenMatKhau_Dialog.this, "Có lỗi xảy ra khi đổi mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}