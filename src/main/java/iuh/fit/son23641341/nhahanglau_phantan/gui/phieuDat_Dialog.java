package iuh.fit.son23641341.nhahanglau_phantan.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class phieuDat_Dialog extends JDialog implements ActionListener {

    // Các hằng số để PhieuDat_GUI biết người dùng đã chọn gì
    public static final int HUY = 0;
    public static final int THANH_TOAN = 1;
    public static final int THANH_TOAN_VA_IN = 2;

    private int luaChon = HUY; // Mặc định là Hủy

    private JButton btnThanhToan, btnThanhToanVaIn, btnHuy;

    public phieuDat_Dialog(Frame owner, String message) {
        // Gọi constructor của JDialog
        // true: để làm cho nó modal (chặn tương tác với cửa sổ cha)
        super(owner, "Xác nhận phiếu đặt", true); 
                                                
        setSize(800, 350);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        // 1. Panel nội dung (thông báo)
        JLabel lblMessage = new JLabel(message);
        lblMessage.setFont(new Font("Arial", Font.PLAIN, 14));
        lblMessage.setBorder(new EmptyBorder(15, 15, 15, 15));
        lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Bọc trong JScrollPane phòng khi thông báo quá dài
        JScrollPane scrollPane = new JScrollPane(lblMessage);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // 2. Panel các nút bấm
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pnlButtons.setBorder(new EmptyBorder(0, 10, 10, 10));
        
        btnThanhToan = new JButton("Thanh Toán");
        btnThanhToanVaIn = new JButton("Thanh Toán & In HĐ");
        btnHuy = new JButton("Hủy");

        // Style các nút
        setupButton(btnThanhToan, new Color(0x7AB750)); // Màu xanh lá
        setupButton(btnThanhToanVaIn, new Color(0x4A90E2)); // Màu xanh dương
        setupButton(btnHuy, new Color(0xDC4332)); // Màu đỏ chính

        // Thêm sự kiện
        btnThanhToan.addActionListener(this);
        btnThanhToanVaIn.addActionListener(this);
        btnHuy.addActionListener(this);

        pnlButtons.add(btnThanhToan);
        pnlButtons.add(btnThanhToanVaIn);
        pnlButtons.add(btnHuy);

        // Thêm vào dialog
        add(scrollPane, BorderLayout.CENTER);
        add(pnlButtons, BorderLayout.SOUTH);
    }

    // Phương thức tiện ích để style nút
    private void setupButton(JButton button, Color background) {
        button.setBackground(background);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(180, 40));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
    }

    // Xử lý sự kiện click
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == btnThanhToan) {
            this.luaChon = THANH_TOAN;
        } else if (source == btnThanhToanVaIn) {
            this.luaChon = THANH_TOAN_VA_IN;
        } else if (source == btnHuy) {
            this.luaChon = HUY;
        }
        
        // Đóng dialog sau khi chọn
        dispose();
    }

    // Phương thức để PhieuDat_GUI lấy kết quả
    public int getLuaChon() {
        return this.luaChon;
    }
}