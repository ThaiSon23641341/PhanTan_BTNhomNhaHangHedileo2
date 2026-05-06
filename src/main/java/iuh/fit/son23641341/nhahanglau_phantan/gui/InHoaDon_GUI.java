package iuh.fit.son23641341.nhahanglau_phantan.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import iuh.fit.son23641341.nhahanglau_phantan.control.User_Ctr;

public class InHoaDon_GUI extends JFrame {

    public InHoaDon_GUI() {
        setTitle("HỆ DÌ LEO - In Hóa Đơn");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        SideBar_GUI sidebar = new SideBar_GUI();
        sidebar.setMauNutKhiChon("Thanh toán");
        add(sidebar, BorderLayout.WEST);

        JPanel pnlNoiDungChinh = taoNoiDungChinh();
        add(pnlNoiDungChinh, BorderLayout.CENTER);
    }

    private JPanel taoNoiDungChinh() {
        JPanel pnlChinh = new JPanel(new BorderLayout());
        pnlChinh.setBackground(new Color(0xF3F3F3));
        pnlChinh.setBorder(new EmptyBorder(15, 25, 15, 25));

        // Khu vực header
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setPreferredSize(new Dimension(0, 80));
        pnlHeader.setBackground(Color.WHITE);
        pnlHeader.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                new EmptyBorder(15, 20, 15, 20)));

        JPanel pnlTieuDe = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        pnlTieuDe.setBackground(Color.WHITE);
        JLabel lblTieuDeTrang = new JLabel("IN HÓA ĐƠN");
        lblTieuDeTrang.setFont(new Font("Arial", Font.BOLD, 24));
        lblTieuDeTrang.setForeground(new Color(230, 81, 0));
        pnlTieuDe.add(lblTieuDeTrang);

        JPanel pnlThongTinAdmin = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlThongTinAdmin.setBackground(Color.WHITE);

        JLabel lblAvatar = new JLabel();
        try {
            ImageIcon iconAvatar = new ImageIcon(getClass().getResource("/imgs/avatar.png"));
            if (iconAvatar.getIconWidth() > 0) {
                Image anhAvatar = iconAvatar.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                lblAvatar.setIcon(new ImageIcon(anhAvatar));
            } else {
                throw new Exception("Image not found");
            }
        } catch (Exception e) {
            lblAvatar.setText("👤");
            lblAvatar.setFont(new Font("Arial", Font.PLAIN, 24));
            System.out.println("Không tìm thấy ảnh avatar: " + e.getMessage());
        }

        JLabel lblThongTinAdmin = new JLabel("ADMIN");
        lblThongTinAdmin.setFont(new Font("Arial", Font.BOLD, 16));
        lblThongTinAdmin.setForeground(Color.BLACK);
        pnlThongTinAdmin.add(lblAvatar);
        pnlThongTinAdmin.add(lblThongTinAdmin);

        pnlHeader.add(pnlTieuDe, BorderLayout.WEST);
        pnlHeader.add(pnlThongTinAdmin, BorderLayout.EAST);
        pnlChinh.add(pnlHeader, BorderLayout.NORTH);

        // Khu vực hóa đơn
        JPanel pnlBaoBoiHoaDon = new JPanel();
        pnlBaoBoiHoaDon.setOpaque(false);
        pnlBaoBoiHoaDon.setLayout(new GridBagLayout());

        JPanel pnlHoaDon = new JPanel();
        pnlHoaDon.setLayout(new BoxLayout(pnlHoaDon, BoxLayout.Y_AXIS));
        pnlHoaDon.setBackground(Color.WHITE);
        pnlHoaDon.setBorder(new EmptyBorder(30, 40, 30, 40));
        pnlHoaDon.setMaximumSize(new Dimension(700, 500));
        pnlHoaDon.setPreferredSize(new Dimension(700, 500));

        JPanel pnlTieuDeHoaDon = new JPanel(new BorderLayout());
        pnlTieuDeHoaDon.setBackground(Color.WHITE);
        pnlTieuDeHoaDon.setMaximumSize(new Dimension(620, 40));

        JLabel lblTieuDe = new JLabel("HÓA ĐƠN THANH TOÁN");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 20));
        lblTieuDe.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel lblSoBan = new JLabel("Số bàn: 01");
        lblSoBan.setFont(new Font("Arial", Font.BOLD, 16));
        lblSoBan.setHorizontalAlignment(SwingConstants.RIGHT);

        pnlTieuDeHoaDon.add(lblTieuDe, BorderLayout.WEST);
        pnlTieuDeHoaDon.add(lblSoBan, BorderLayout.EAST);
        pnlTieuDeHoaDon.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblKhachHang = new JLabel("Khách hàng:");
        lblKhachHang.setFont(new Font("Arial", Font.PLAIN, 14));
        lblKhachHang.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblThoiGianThanhToan = new JLabel("thời gian thanh toán:");
        lblThoiGianThanhToan.setFont(new Font("Arial", Font.PLAIN, 14));
        lblThoiGianThanhToan.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblMaDonHang = new JLabel("Đơn hàng: 023571");
        lblMaDonHang.setFont(new Font("Arial", Font.BOLD, 14));
        lblMaDonHang.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Bảng chi tiết
        JPanel pnlChiTiet = new JPanel();
        pnlChiTiet.setLayout(new GridLayout(0, 2, 20, 15));
        pnlChiTiet.setBackground(Color.WHITE);
        pnlChiTiet.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 150, 200), 2),
                new EmptyBorder(15, 20, 15, 20)));
        pnlChiTiet.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlChiTiet.setMaximumSize(new Dimension(620, 180));

        pnlChiTiet.add(taoNhanChiTiet("Tổng tiền:"));
        pnlChiTiet.add(taoNhanGiaTri("1.008.000 VND"));
        pnlChiTiet.add(taoNhanChiTiet("Phí dịch vụ:"));
        pnlChiTiet.add(taoNhanGiaTri("0 VND"));
        pnlChiTiet.add(taoNhanChiTiet("Chiết khấu (%):"));
        pnlChiTiet.add(taoNhanGiaTri("10 %"));
        pnlChiTiet.add(taoNhanChiTiet("Giảm giá (%):"));
        pnlChiTiet.add(taoNhanGiaTri("10 %"));
        pnlChiTiet.add(taoNhanChiTietDam("Tổng thanh toán:"));
        pnlChiTiet.add(taoNhanGiaTriDam("1.008.000 VND"));

        JLabel lblThongDiep = new JLabel("Cảm ơn quý khách và hẹn gặp lại!");
        lblThongDiep.setFont(new Font("Arial", Font.ITALIC, 14));
        lblThongDiep.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnInHoaDon = new JButton("In hóa đơn");
        btnInHoaDon.setFont(new Font("Arial", Font.BOLD, 14));
        btnInHoaDon.setBackground(new Color(211, 67, 50));
        btnInHoaDon.setForeground(Color.WHITE);
        btnInHoaDon.setFocusPainted(false);
        btnInHoaDon.setAlignmentX(Component.RIGHT_ALIGNMENT);
        btnInHoaDon.setMaximumSize(new Dimension(150, 40));
        btnInHoaDon.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pnlHoaDon.add(pnlTieuDeHoaDon);
        pnlHoaDon.add(Box.createRigidArea(new Dimension(0, 15)));
        pnlHoaDon.add(lblKhachHang);
        pnlHoaDon.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlHoaDon.add(lblThoiGianThanhToan);
        pnlHoaDon.add(Box.createRigidArea(new Dimension(0, 15)));
        pnlHoaDon.add(lblMaDonHang);
        pnlHoaDon.add(Box.createRigidArea(new Dimension(0, 15)));
        pnlHoaDon.add(pnlChiTiet);
        pnlHoaDon.add(Box.createRigidArea(new Dimension(0, 20)));
        pnlHoaDon.add(lblThongDiep);
        pnlHoaDon.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel pnlNutBam = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlNutBam.setBackground(Color.WHITE);
        pnlNutBam.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlNutBam.add(btnInHoaDon);
        pnlHoaDon.add(pnlNutBam);

        pnlBaoBoiHoaDon.add(pnlHoaDon);
        pnlChinh.add(pnlBaoBoiHoaDon, BorderLayout.CENTER);

        return pnlChinh;
    }

    private JLabel taoNhanChiTiet(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        return label;
    }

    private JLabel taoNhanGiaTri(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        return label;
    }

    private JLabel taoNhanChiTietDam(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        return label;
    }

    private JLabel taoNhanGiaTriDam(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        return label;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InHoaDon_GUI frame = new InHoaDon_GUI();
            frame.setVisible(true);
        });
    }
}

