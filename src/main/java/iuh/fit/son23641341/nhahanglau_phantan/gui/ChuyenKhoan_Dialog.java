package iuh.fit.son23641341.nhahanglau_phantan.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class ChuyenKhoan_Dialog extends JDialog {

    // Constants cho kết quả
    public static final int DA_CHUYEN_KHOAN = 1;
    public static final int HUY = 0;
    
    private int luaChon = HUY;
    private static final Color MAU_XAC_NHAN = new Color(0x7AB750);
    private static final Color MAU_HUY = new Color(0xDC4332);

    public ChuyenKhoan_Dialog(JDialog parent, double soTienCanChuyen, int maBanChinh) {
        super(parent, "Thanh toán Chuyển Khoản", true);
        
        // Thiết lập giao diện
        setLayout(new BorderLayout(10, 10));
        setSize(500, 600);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        // Định dạng tiền tệ
        NumberFormat formatter = new DecimalFormat("#,##0 VNĐ");
        String soTienFormatted = formatter.format(soTienCanChuyen);

        // --- Panel Nội dung chính ---
        JPanel pnlNoiDung = new JPanel();
        pnlNoiDung.setLayout(new BoxLayout(pnlNoiDung, BoxLayout.Y_AXIS));
        pnlNoiDung.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // --- 1. Tiêu đề ---
        JLabel lblTieuDe = new JLabel("THANH TOÁN BẰNG CHUYỂN KHOẢN");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 18));
        lblTieuDe.setForeground(new Color(0x1E88E5));
        lblTieuDe.setAlignmentX(CENTER_ALIGNMENT);
        pnlNoiDung.add(lblTieuDe);
        pnlNoiDung.add(Box.createVerticalStrut(15));

        // --- 2. Mã QR ---
        JLabel lblQR = new JLabel();
        try {
            // Dùng ảnh giả lập nếu ảnh thật không có
            ImageIcon icon = new ImageIcon(getClass().getResource("/imgs/QR_Code.png")); 
            if (icon.getImage() != null) {
                Image img = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
                lblQR.setIcon(new ImageIcon(img));
            } else {
                 lblQR.setText("[Hình ảnh QR Code]");
                 lblQR.setFont(new Font("Arial", Font.ITALIC, 14));
                 lblQR.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                 lblQR.setPreferredSize(new Dimension(250, 250));
            }
        } catch (Exception e) {
             lblQR.setText("[Hình ảnh QR Code]");
             lblQR.setFont(new Font("Arial", Font.ITALIC, 14));
             lblQR.setBorder(BorderFactory.createLineBorder(Color.GRAY));
             lblQR.setPreferredSize(new Dimension(250, 250));
        }
        lblQR.setAlignmentX(CENTER_ALIGNMENT);
        lblQR.setHorizontalAlignment(SwingConstants.CENTER);
        pnlNoiDung.add(lblQR);
        pnlNoiDung.add(Box.createVerticalStrut(20));

        // --- 3. Thông tin chuyển khoản ---
        String htmlInfo = String.format(
            "<html><body style='text-align: center; width: 350px; font-family: Arial;'>"
            + "<p style='font-size: 16px; margin-bottom: 5px;'><b>NGÂN HÀNG:</b> Vietcombank</p>"
            + "<p style='font-size: 16px; margin-bottom: 5px;'><b>SỐ TÀI KHOẢN:</b> 001100220033</p>"
            + "<p style='font-size: 16px; margin-bottom: 5px;'><b>TÊN CHỦ TK:</b> CÔNG TY HÈ DÌ LEO</p>"
            + "<p style='font-size: 16px; margin-top: 15px;'><b>SỐ TIỀN CẦN CHUYỂN:</b> <font color='red' size='+1'>%s</font></p>"
            + "</body></html>", 
            soTienFormatted, maBanChinh);
        
        JLabel lblThongTin = new JLabel(htmlInfo, SwingConstants.CENTER);
        lblThongTin.setAlignmentX(CENTER_ALIGNMENT);
        pnlNoiDung.add(lblThongTin);
        pnlNoiDung.add(Box.createVerticalStrut(20));
        
        // --- 4. Nút bấm ---
        JPanel pnlNutBam = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        
        JButton btnDaThanhToan = new JButton("Đã Thanh Toán");
        JButton btnHuy = new JButton("Hủy");
        
        Dimension kichThuocNut = new Dimension(150, 40);
        
        setupButton(btnDaThanhToan, MAU_XAC_NHAN, Color.WHITE, kichThuocNut);
        setupButton(btnHuy, MAU_HUY, Color.WHITE, kichThuocNut);
        
        // Sự kiện
        btnDaThanhToan.addActionListener(e -> {
            luaChon = DA_CHUYEN_KHOAN;
            dispose();
        });
        
        btnHuy.addActionListener(e -> {
            luaChon = HUY;
            dispose();
        });
        
        pnlNutBam.add(btnDaThanhToan);
        pnlNutBam.add(btnHuy);
        
        pnlNoiDung.add(pnlNutBam);

        add(pnlNoiDung, BorderLayout.CENTER);

        // Đảm bảo kết quả là HUY nếu người dùng đóng cửa sổ
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                luaChon = HUY;
            }
        });
    }
    
    private void setupButton(JButton button, Color background, Color foreground, Dimension size) {
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(size);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public int getLuaChon() {
        return luaChon;
    }
}