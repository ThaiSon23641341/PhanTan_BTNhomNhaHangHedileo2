package iuh.fit.son23641341.nhahanglau_phantan.gui;

import iuh.fit.son23641341.nhahanglau_phantan.dao.PhieuDat_DAO;
import iuh.fit.son23641341.nhahanglau_phantan.entity.PhieuDatBan;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.ArrayList;

public class XemPhieuDat_Dialog extends JDialog {
    private static final Color MAU_CHINH = new Color(0xDC4332);
    private static final Color MAU_XAC_NHAN = new Color(0x7AB750);
    
    private JTable tblPhieuDat;
    private DefaultTableModel tableModel;
    private PhieuDat_DAO phieuDatDAO;
    private int maBan;
    private String ngayDat;
    
    public XemPhieuDat_Dialog(JFrame parent, int maBan, String ngayDat) {
        super(parent, "Danh Sách Phiếu Đặt - Bàn " + String.format("%03d", maBan), true);
        this.maBan = maBan;
        this.ngayDat = ngayDat;
        this.phieuDatDAO = new PhieuDat_DAO();
        
        initComponents();
        loadData();
        
        setSize(800, 500);
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Header
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);
        pnlHeader.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel lblTieuDe = new JLabel("Phiếu Đặt Đã Có - Ngày " + ngayDat);
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 20));
        lblTieuDe.setForeground(MAU_CHINH);
        pnlHeader.add(lblTieuDe, BorderLayout.WEST);
        
        // Thông tin hướng dẫn
        JLabel lblHuongDan = new JLabel("Vui lòng chọn khung giờ không trùng lặp (cách nhau ít nhất 2 tiếng)");
        lblHuongDan.setFont(new Font("Arial", Font.ITALIC, 12));
        lblHuongDan.setForeground(Color.GRAY);
        pnlHeader.add(lblHuongDan, BorderLayout.SOUTH);
        
        add(pnlHeader, BorderLayout.NORTH);
        
        // Table
        String[] columnNames = {"Mã Phiếu", "Giờ Đặt", "Tên Khách", "SĐT", "Email", "Trạng Thái"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblPhieuDat = new JTable(tableModel);
        tblPhieuDat.setFont(new Font("Arial", Font.PLAIN, 14));
        tblPhieuDat.setRowHeight(30);
        tblPhieuDat.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tblPhieuDat.getTableHeader().setBackground(MAU_CHINH);
        tblPhieuDat.getTableHeader().setForeground(Color.WHITE);
        
        // Thêm sự kiện double-click vào row để load phiếu
        tblPhieuDat.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) { // Double click
                    int row = tblPhieuDat.getSelectedRow();
                    if (row >= 0) {
                        String maPhieu = (String) tableModel.getValueAt(row, 0);
                        // Tìm phiếu trong database
                        ArrayList<PhieuDatBan> dsPhieu = phieuDatDAO.getPhieuDatByBanVaNgay(maBan, ngayDat);
                        PhieuDatBan phieuCanLoad = null;
                        for (PhieuDatBan p : dsPhieu) {
                            if (p.getMaPhieu().equals(maPhieu)) {
                                phieuCanLoad = p;
                                break;
                            }
                        }
                        
                        if (phieuCanLoad != null) {
                            // Đóng dialog này
                            dispose();
                            // Load dữ liệu lên PhieuDat_GUI (parent của dialog này)
                            Window owner = getOwner();
                            if (owner instanceof PhieuDat_GUI) {
                                ((PhieuDat_GUI) owner).loadPhieuDatFromDB(phieuCanLoad);
                            }
                        }
                    }
                }
            }
        });
        
        // Căn giữa các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tblPhieuDat.getColumnCount(); i++) {
            tblPhieuDat.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JScrollPane scrollPane = new JScrollPane(tblPhieuDat);
        scrollPane.setBorder(new EmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);
        
        // Footer với button đóng
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlFooter.setBackground(Color.WHITE);
        pnlFooter.setBorder(new EmptyBorder(10, 20, 15, 20));
        
        JButton btnDong = new JButton("Đóng");
        btnDong.setFont(new Font("Arial", Font.BOLD, 14));
        btnDong.setPreferredSize(new Dimension(120, 40));
        btnDong.setBackground(MAU_CHINH);
        btnDong.setForeground(Color.WHITE);
        btnDong.setFocusPainted(false);
        btnDong.setBorderPainted(false);
        btnDong.addActionListener(e -> dispose());
        
        pnlFooter.add(btnDong);
        add(pnlFooter, BorderLayout.SOUTH);
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        ArrayList<PhieuDatBan> dsPhieu = phieuDatDAO.getPhieuDatByBanVaNgay(maBan, ngayDat);
        
        if (dsPhieu == null || dsPhieu.isEmpty()) {
            JLabel lblEmpty = new JLabel("Chưa có phiếu đặt nào cho bàn này trong ngày " + ngayDat);
            lblEmpty.setFont(new Font("Arial", Font.ITALIC, 14));
            lblEmpty.setForeground(Color.GRAY);
            lblEmpty.setHorizontalAlignment(SwingConstants.CENTER);
            
            // Thay thế table bằng label
            Component[] components = getContentPane().getComponents();
            for (Component comp : components) {
                if (comp instanceof JScrollPane) {
                    remove(comp);
                }
            }
            add(lblEmpty, BorderLayout.CENTER);
            revalidate();
            repaint();
            return;
        }
        
        for (PhieuDatBan phieu : dsPhieu) {
            Object[] row = {
                phieu.getMaPhieu(),
                phieu.getGioDat() != null ? phieu.getGioDat() : "Chưa có",
                phieu.getTenKhachDat(),
                phieu.getSdtDat(),
                phieu.getEmailDat() != null ? phieu.getEmailDat() : "",
                phieu.getTrangThai()
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Lấy danh sách giờ đặt đã có
     */
    public ArrayList<String> getGioDaDat() {
        ArrayList<String> dsGio = new ArrayList<>();
        ArrayList<PhieuDatBan> dsPhieu = phieuDatDAO.getPhieuDatByBanVaNgay(maBan, ngayDat);
        
        if (dsPhieu != null) {
            for (PhieuDatBan phieu : dsPhieu) {
                if (phieu.getGioDat() != null && !phieu.getGioDat().isEmpty()) {
                    dsGio.add(phieu.getGioDat());
                }
            }
        }
        
        return dsGio;
    }
}

