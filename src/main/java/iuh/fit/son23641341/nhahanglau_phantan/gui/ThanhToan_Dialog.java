package iuh.fit.son23641341.nhahanglau_phantan.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import iuh.fit.son23641341.nhahanglau_phantan.entity.ChiTietDatMon;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class ThanhToan_Dialog extends JDialog {
    
    // Constants cho kết quả
	public static final int TIEN_MAT = 1;
	public static final int CHUYEN_KHOAN = 2; // Giữ lại nếu muốn (chưa dùng)
	public static final int DA_CHUYEN_KHOAN = 3; // Thêm hằng số này
	public static final int HUY = 0;
    
    private int luaChon = HUY;
    private final double tongThanhToan;
    private final int maBanChinh; // Dùng để truyền sang ChuyenKhoan_Dialog
    private final ArrayList<ChiTietDatMon> danhSachMonAn; // Thêm biến lưu danh sách món ăn

    private static final Color MAU_CHINH = new Color(0xDC4332);
    private static final Color MAU_XAC_NHAN = new Color(0x7AB750);
    private static final Color MAU_HUY = new Color(0x666666); // Màu xám cho Hủy

    public ThanhToan_Dialog(Frame parent, String thongBao, double tongThanhToan, int maBanChinh, List<? extends ChiTietDatMon> danhSachMonAn) {
        super(parent, "Xác nhận Thanh toán", true);
        this.tongThanhToan = tongThanhToan;
        this.maBanChinh = maBanChinh;
        this.danhSachMonAn = new ArrayList<>();
        if (danhSachMonAn != null) {
            this.danhSachMonAn.addAll(danhSachMonAn);
        }

        // Thiết lập giao diện
        setLayout(new BorderLayout());
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        // --- Panel Nội dung ---
        JPanel pnlTong = new JPanel(new BorderLayout(10, 10));
        pnlTong.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // --- 1. Phần Header thông báo và tổng tiền ---
        JLabel lblThongBao = new JLabel(thongBao, SwingConstants.CENTER);
        lblThongBao.setFont(new Font("Arial", Font.PLAIN, 14));
        lblThongBao.setHorizontalAlignment(SwingConstants.LEFT);
        
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.add(lblThongBao, BorderLayout.CENTER);
        pnlTong.add(pnlHeader, BorderLayout.NORTH);
        
        // --- 2. Phần Danh sách món ăn ---
        JPanel pnlMonAnWrapper = new JPanel(new BorderLayout());
        pnlMonAnWrapper.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY), 
            "Chi tiết Đơn Hàng", 
            javax.swing.border.TitledBorder.LEFT, 
            javax.swing.border.TitledBorder.TOP, 
            new Font("Arial", Font.BOLD, 14), 
            Color.BLACK));
            
        JPanel pnlDanhSachMon = new JPanel();
        pnlDanhSachMon.setLayout(new BoxLayout(pnlDanhSachMon, BoxLayout.Y_AXIS));
        pnlDanhSachMon.setBackground(Color.WHITE);
        
        hienThiDanhSachMon(pnlDanhSachMon);

        JScrollPane scrollMonAn = new JScrollPane(pnlDanhSachMon);
        scrollMonAn.setPreferredSize(new Dimension(400, 300));
        scrollMonAn.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollMonAn.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollMonAn.setBorder(BorderFactory.createEmptyBorder());

        pnlMonAnWrapper.add(scrollMonAn, BorderLayout.CENTER);
        pnlTong.add(pnlMonAnWrapper, BorderLayout.CENTER);
        
        // --- Panel Nút bấm ---
        JPanel pnlNutBam = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlNutBam.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        JButton btnTienMat = new JButton("Tiền Mặt");
        JButton btnChuyenKhoan = new JButton("Chuyển Khoản");
        JButton btnHuy = new JButton("Hủy");
        
        // Áp dụng kiểu dáng
        Dimension kichThuocNut = new Dimension(200, 40);
        
        setupButton(btnTienMat, MAU_XAC_NHAN, Color.WHITE, kichThuocNut);
        setupButton(btnChuyenKhoan, new Color(0x1E88E5), Color.WHITE, kichThuocNut);
        setupButton(btnHuy, MAU_HUY, Color.WHITE, kichThuocNut);
        
        // Thêm sự kiện
        btnTienMat.addActionListener(e -> {
            luaChon = TIEN_MAT;
            dispose();
        });
        
        btnChuyenKhoan.addActionListener(e -> {
            // Hiển thị dialog chuyển khoản
            ChuyenKhoan_Dialog ckDialog = new ChuyenKhoan_Dialog(this, tongThanhToan, maBanChinh);
            ckDialog.setVisible(true);
            
            // Xử lý kết quả từ ChuyenKhoan_Dialog
            if (ckDialog.getLuaChon() == ChuyenKhoan_Dialog.DA_CHUYEN_KHOAN) {
                luaChon = DA_CHUYEN_KHOAN; // Gán kết quả thành công
                dispose(); 
            }
        });
        
        btnHuy.addActionListener(e -> {
            luaChon = HUY;
            dispose();
        });
        
        pnlNutBam.add(btnTienMat);
        pnlNutBam.add(btnChuyenKhoan);
        pnlNutBam.add(btnHuy);
        
        add(pnlTong, BorderLayout.CENTER);
        add(pnlNutBam, BorderLayout.SOUTH);
        
        // Đảm bảo kết quả là HUY nếu người dùng đóng cửa sổ
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                luaChon = HUY;
            }
        });
    }
    
    private void hienThiDanhSachMon(JPanel pnlDanhSach) {
        // Thêm header
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        pnlHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        pnlHeader.setBackground(new Color(0xF0F0F0));

        JLabel lblTenMonHeader = new JLabel("  Tên Món");
        lblTenMonHeader.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel lblSLHeader = new JLabel("SL");
        lblSLHeader.setFont(new Font("Arial", Font.BOLD, 14));
        lblSLHeader.setPreferredSize(new Dimension(40, 20));
        lblSLHeader.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lblGiaHeader = new JLabel("Thành Tiền  ");
        lblGiaHeader.setFont(new Font("Arial", Font.BOLD, 14));
        lblGiaHeader.setPreferredSize(new Dimension(100, 20));
        lblGiaHeader.setHorizontalAlignment(SwingConstants.RIGHT);

        pnlHeader.add(lblTenMonHeader, BorderLayout.WEST);
        
        JPanel pnlGiaSLHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        pnlGiaSLHeader.setBackground(new Color(0xF0F0F0));
        pnlGiaSLHeader.add(lblSLHeader);
        pnlGiaSLHeader.add(lblGiaHeader);
        pnlHeader.add(pnlGiaSLHeader, BorderLayout.CENTER);
        
        pnlDanhSach.add(pnlHeader);
        
        if (danhSachMonAn == null || danhSachMonAn.isEmpty()) {
            JLabel lblChuaCoMon = new JLabel("  (Chưa có món ăn nào được chọn)", SwingConstants.CENTER);
            lblChuaCoMon.setFont(new Font("Arial", Font.ITALIC, 14));
            lblChuaCoMon.setForeground(Color.GRAY);
            lblChuaCoMon.setBorder(new EmptyBorder(10, 0, 10, 0));
            lblChuaCoMon.setAlignmentX(CENTER_ALIGNMENT);
            pnlDanhSach.add(lblChuaCoMon);
        } else {
            for (ChiTietDatMon ct : danhSachMonAn) {
                JPanel pnlMonAn = new JPanel(new BorderLayout());
                pnlMonAn.setBackground(Color.WHITE);
                pnlMonAn.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)));
                pnlMonAn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
                pnlMonAn.setPreferredSize(new Dimension(400, 35));

                JLabel lblTenMon = new JLabel("  " + ct.getMonAn().getTenMon());
                lblTenMon.setFont(new Font("Arial", Font.PLAIN, 13));
                lblTenMon.setPreferredSize(new Dimension(200, 35));
                lblTenMon.setMinimumSize(new Dimension(200, 35));
                
                JLabel lblSoLuong = new JLabel("x" + ct.getSoLuong(), SwingConstants.CENTER);
                lblSoLuong.setFont(new Font("Arial", Font.PLAIN, 13));
                lblSoLuong.setPreferredSize(new Dimension(40, 35));
                
                double thanhTien = ct.getMonAn().getGia() * ct.getSoLuong();
                JLabel lblGia = new JLabel(String.format("%,.0f VNĐ  ", thanhTien), SwingConstants.RIGHT);
                lblGia.setFont(new Font("Arial", Font.PLAIN, 13));
                lblGia.setPreferredSize(new Dimension(100, 35));

                pnlMonAn.add(lblTenMon, BorderLayout.WEST);
                
                JPanel pnlGiaSL = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
                pnlGiaSL.setBackground(Color.WHITE);
                pnlGiaSL.add(lblSoLuong);
                pnlGiaSL.add(lblGia);
                pnlMonAn.add(pnlGiaSL, BorderLayout.CENTER);
                
                pnlDanhSach.add(pnlMonAn);
            }
        }
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
