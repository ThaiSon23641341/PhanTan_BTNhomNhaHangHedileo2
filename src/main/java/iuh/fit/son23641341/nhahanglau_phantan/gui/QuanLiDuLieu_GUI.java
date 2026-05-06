package iuh.fit.son23641341.nhahanglau_phantan.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;

import iuh.fit.son23641341.nhahanglau_phantan.dao.KhuyenMai_DAO;
import iuh.fit.son23641341.nhahanglau_phantan.dao.NhanVien_DAO;

import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import iuh.fit.son23641341.nhahanglau_phantan.control.KhuyenMai_Ctr;
import iuh.fit.son23641341.nhahanglau_phantan.control.User_Ctr;
import iuh.fit.son23641341.nhahanglau_phantan.entity.KhuyenMai;
import iuh.fit.son23641341.nhahanglau_phantan.entity.NhanVien;

public class QuanLiDuLieu_GUI extends JFrame {
    // Khai báo các button làm biến instance
    private JButton btnTaiKhoan;
    private JButton btnThemKM;
    private JButton btnXoaKM;
    
    // controller và bảng khuyến mãi
    private KhuyenMai_Ctr khuyenMaiCtr;
    private DefaultTableModel kmModel;
    private JTable tblKM;
    private JTable tableNhanvien;
    private    DefaultTableModel modelNhanvien;
    private String[] columnNamesNV = {"Mã NV", "Họ Tên", "Giới Tính", "Ca Làm", "SDT", "Email", "Chức Vụ", "ID User"};
	private JButton btnThemNhanVien;
	private JButton btnXoaNV;
	private JButton btnSuaNV;
    public QuanLiDuLieu_GUI() {

        setLayout(new BorderLayout());
        // Chèn SideBoar_GUI vào vidu_GUI
        SideBar_GUI sideBar = new SideBar_GUI();
        add(sideBar, BorderLayout.WEST);
        sideBar.setMauNutKhiChon("Quản Lý Dữ Liệu");

        // Thêm các thành phần khác vào vidu_GUI nếu cần
        JPanel pnlPhanChinh = new JPanel();
        add(pnlPhanChinh, BorderLayout.CENTER);
        pnlPhanChinh.setLayout(new BorderLayout());
        // header của phần chính
        JPanel pnlHeader = new JPanel();
        pnlHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlHeader.setBackground(Color.decode("#37415C"));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 10));
        // bo góc của header
        
        
        
        
        
        

        // title của phần chính
        JLabel lblTitle = new JLabel("QUẢN LÝ DỮ LIỆU");
        lblTitle.setFont(new Font("Montserrat", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        // Cho title qua bên trái
        pnlHeader.add(lblTitle);
        
        


        
        pnlPhanChinh.add(pnlHeader, BorderLayout.NORTH);
        lblTitle.setHorizontalAlignment(JLabel.LEFT);
        // 1 khoản trắng giữa title và nút tài khoản có giá trị = 50% cái header
        pnlHeader.add(Box.createHorizontalStrut(700));
        // phần thân của phần chính
        JPanel pnlBody = new JPanel();
        pnlBody.setBackground(Color.WHITE);
        pnlBody.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        pnlBody.setLayout(new BoxLayout(pnlBody, BoxLayout.Y_AXIS));
        pnlPhanChinh.add(pnlBody, BorderLayout.CENTER);

        // Panel tiêu đề "Phiên bản hiện tại"
//        JPanel pnlTitlePhienBan = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
//        pnlTitlePhienBan.setBackground(Color.WHITE);



        pnlBody.add(Box.createVerticalStrut(10));

        // Phiên bản hiện tại: 1.0.0


        pnlBody.add(Box.createVerticalStrut(30));

        // Nhân viên - tiêu đề
        JPanel pnldsNhanVien = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        pnldsNhanVien.setBackground(Color.WHITE);
        JLabel lbldsNhanVien = new JLabel("Danh sách Nhân viên:");
        lbldsNhanVien.setFont(new Font("Segoe UI", Font.ITALIC, 28));
        
        // Bảng nhân viên 

       
        
     // Khởi tạo model với tiêu đề cột
        modelNhanvien = new DefaultTableModel(columnNamesNV, 0);

        // Khởi tạo table với model vừa tạo
        tableNhanvien = new JTable(modelNhanvien);
		tableNhanvien.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        // Thêm table vào JScrollPane để có thanh cuộn
        JScrollPane scrollPane = new JScrollPane(tableNhanvien);
        
        
        
        
        
        btnThemNhanVien = new JButton("<html><center>Thêm Nhân viên<br>và tài khoản</center></html>");
        btnThemNhanVien.setBackground(new Color(0, 153, 0)); // xanh lá
        btnThemNhanVien.setForeground(Color.WHITE);
        btnThemNhanVien.setFocusPainted(false);
        btnThemNhanVien.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnThemNhanVien.setPreferredSize(new Dimension(180, 45));
        btnThemNhanVien.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        
        
        //btnXoaNhanVien
        btnXoaNV = new JButton("<html><center>Xóa Nhân viên<br>và tài khoản</center></html>");	
        btnXoaNV.setBackground(new Color(204, 0, 0)); // đỏ
        btnXoaNV.setForeground(Color.WHITE);
        btnXoaNV.setFocusPainted(false);
        btnXoaNV.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnXoaNV.setPreferredSize(new Dimension(160, 45));
        btnXoaNV.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        //btn Sủa nhân Viên và tài khoản 
        
        btnSuaNV = new JButton("<html><center>Sửa Nhân viên<br>và tài khoản</center></html>");
        btnSuaNV.setBackground(new Color(0, 102, 204)); // xanh dương
        btnSuaNV.setForeground(Color.WHITE);
        btnSuaNV.setFocusPainted(false);
        btnSuaNV.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSuaNV.setPreferredSize(new Dimension(160, 45));
        btnSuaNV.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        
        
        
        JPanel	pnlNVHDeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        pnldsNhanVien.add(lbldsNhanVien);
		pnlNVHDeader.add(pnldsNhanVien);
		pnlNVHDeader.add(Box.createHorizontalStrut(20));
		pnlNVHDeader.add(btnThemNhanVien);
		pnlNVHDeader.add(Box.createHorizontalStrut(10));
		pnlNVHDeader.add(btnXoaNV);
		pnlNVHDeader.add(Box.createHorizontalStrut(10));
		pnlNVHDeader.add(btnSuaNV);
		

		
//        
//        lbldsNhanVien.setForeground(Color.BLACK);
//        pnldsNhanVien.add(lbldsNhanVien);
//        pnlBody.add(pnldsNhanVien);
        pnlBody.add(Box.createVerticalStrut(10));
        pnlBody.add(pnlNVHDeader);
        pnlBody.add(Box.createVerticalStrut(10));
        pnlBody.add(scrollPane);
        pnlBody.add(Box.createVerticalStrut(10));

        // BẢNG LỊCH SỬ
    

        pnlBody.add(Box.createVerticalStrut(30));

        // === PHẦN DANH SÁCH KHUYẾN MÃI ===
        JPanel pnlKMHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        pnlKMHeader.setBackground(Color.WHITE);

        // Tiêu đề
        // thêm ảnh chổ này
	        URL imgURLKM = this.getClass().getResource("/imgs/khuyenmai.png");
	        ImageIcon iconkm = new ImageIcon(imgURLKM);
	        JLabel lblIcon = new JLabel(new ImageIcon(iconkm.getImage().getScaledInstance((int)(iconkm.getIconWidth() * 0.15), (int)(iconkm.getIconHeight() * 0.15), Image.SCALE_SMOOTH)));
	        //thu nhỏ ảnh 

        pnlKMHeader.add(lblIcon);
        pnlKMHeader.add(Box.createVerticalStrut(20));
        
        JLabel lblKM = new JLabel("Danh sách các khuyến mãi");
        lblKM.setFont(new Font("Segoe UI", Font.ITALIC, 28));
        lblKM.setForeground(Color.BLACK);

        pnlKMHeader.add(lblKM);
       
        // Nút "Thêm khuyến mãi"
        btnThemKM = new JButton("Thêm khuyến mãi");
        btnThemKM.setBackground(new Color(0, 153, 0)); // xanh lá
        btnThemKM.setForeground(Color.WHITE);
        btnThemKM.setFocusPainted(false);
        btnThemKM.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnThemKM.setPreferredSize(new Dimension(160, 45));
        btnThemKM.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        
        

        pnlKMHeader.add(Box.createHorizontalStrut(20));
        pnlKMHeader.add(btnThemKM);
        
        
        btnXoaKM = new JButton("Xóa khuyến mãi");
        btnXoaKM.setBackground(new Color(204, 0, 0)); // đỏ
        btnXoaKM.setForeground(Color.WHITE);
        btnXoaKM.setFocusPainted(false);
        btnXoaKM.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnXoaKM.setPreferredSize(new Dimension(160, 45));
        btnXoaKM.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        pnlKMHeader.add(Box.createHorizontalStrut(10));
        pnlKMHeader.add(btnXoaKM);
        btnXoaKM.addActionListener(evt -> {
            int row = tblKM.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(QuanLiDuLieu_GUI.this,
                        "Vui lòng chọn khuyến mãi cần xóa.",
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String maKM = String.valueOf(kmModel.getValueAt(row, 0));
            int choice = JOptionPane.showConfirmDialog(QuanLiDuLieu_GUI.this,
                    "Bạn có chắc muốn xóa khuyến mãi \"" + maKM + "\"?",
                    "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (choice != JOptionPane.YES_OPTION) return;

            try {
                // Prefer controller deletion if available
                boolean ok = khuyenMaiCtr.xoaKhuyenMai(maKM);
                if (!ok) {
                    JOptionPane.showMessageDialog(QuanLiDuLieu_GUI.this,
                            "Xóa không thành công. Vui lòng thử lại.",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                refreshKMTable();
                JOptionPane.showMessageDialog(QuanLiDuLieu_GUI.this,
                        "Đã xóa khuyến mãi \"" + maKM + "\".",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(QuanLiDuLieu_GUI.this,
                        "Đã xảy ra lỗi khi xóa: " + ex.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        

        pnlBody.add(pnlKMHeader);

        pnlBody.add(Box.createVerticalStrut(10));

        // === BẢNG KHUYẾN MÃI ===
        String[] kmColumns = { "Mã KM", "Tên KM", "Phần % giảm", "Bắt đầu", "Kết thúc", "Mô tả" };

        // Khởi tạo controller và model bảng
        khuyenMaiCtr = new KhuyenMai_Ctr();
        kmModel = new DefaultTableModel(kmColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        loadDataToTable();

        tblKM = new JTable(kmModel);
        tblKM.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblKM.setRowHeight(28);
        tblKM.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblKM.setFillsViewportHeight(true);

        JScrollPane scrollKM = new JScrollPane(tblKM);
        scrollKM.setPreferredSize(new Dimension(800, 130));
        scrollKM.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        pnlBody.add(scrollKM);

        // Điền dữ liệu ban đầu từ controller
        refreshKMTable();

        pnlBody.add(Box.createVerticalStrut(30));

        // === PHẦN BUTTON "SAO LƯU" & "KHÔI PHỤC" ===
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 20));
        pnlButtons.setBackground(Color.WHITE);

        // Nút "Sao lưu"
     
        pnlBody.add(pnlButtons);

        // Sau khi tạo nút và bảng, gắn sự kiện cho btnThemKM
        btnThemKM.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ThemKhuyenMai_Dialog dlg = new ThemKhuyenMai_Dialog(QuanLiDuLieu_GUI.this, khuyenMaiCtr);
                dlg.setVisible(true);
                KhuyenMai created = dlg.getKhuyenMaiMoi();
                if (created != null) {
                    // Dialog đã thêm vào controller (nếu thành công) => refresh bảng
                    refreshKMTable();
                    // chọn dòng vừa thêm (tùy chọn)
                    for (int i = 0; i < kmModel.getRowCount(); i++) {
                        if (created.getMaKhuyenMai().equals(kmModel.getValueAt(i, 0))) {
                            tblKM.setRowSelectionInterval(i, i);
                            tblKM.scrollRectToVisible(tblKM.getCellRect(i, 0, true));
                            break;
                        }
                    }
                }
            }
        });

        
        //Xử lý xóa nhân viên và tài khoản 
        btnXoaNV.addActionListener(evt -> {
			int row = tableNhanvien.getSelectedRow();
			if (row < 0) {
				JOptionPane.showMessageDialog(QuanLiDuLieu_GUI.this,
						"Vui lòng chọn nhân viên cần xóa.",
						"Thông báo", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			String maNV = String.valueOf(modelNhanvien.getValueAt(row, 0));
			String idUser = String.valueOf(modelNhanvien.getValueAt(row, 7));
			int choice = JOptionPane.showConfirmDialog(QuanLiDuLieu_GUI.this,
					"Bạn có chắc muốn xóa nhân viên \"" + maNV + "\"?",
					"Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (choice != JOptionPane.YES_OPTION) return;

			try {
				NhanVien_DAO dao = new NhanVien_DAO();
				boolean ok = dao.xoaNhanVien(maNV,idUser);
				if (!ok) {
					JOptionPane.showMessageDialog(QuanLiDuLieu_GUI.this,
							"Xóa không thành công. Vui lòng thử lại.",
							"Lỗi", JOptionPane.ERROR_MESSAGE);
					return;
				}
				loadDataToTable();
				JOptionPane.showMessageDialog(QuanLiDuLieu_GUI.this,
						"Đã xóa nhân viên \"" + maNV + "\".",
						"Thành công", JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(QuanLiDuLieu_GUI.this,
						"Đã xảy ra lỗi khi xóa: " + ex.getMessage(),
						"Lỗi", JOptionPane.ERROR_MESSAGE);
			}
		});
        
        
        //Xử lý thêm nhân viên và tài khoản
        btnThemNhanVien.addActionListener(evt -> {

            NhanVien_Dialog dlg = new NhanVien_Dialog(QuanLiDuLieu_GUI.this);
            dlg.setVisible(true);
            NhanVien created = dlg.getNhanVienMoi();
            
            if (created != null) {
                loadDataToTable(); 
                int lastRow = tableNhanvien.getRowCount() - 1;
                if (lastRow >= 0) {
                    tableNhanvien.setRowSelectionInterval(lastRow, lastRow);
                    tableNhanvien.scrollRectToVisible(tableNhanvien.getCellRect(lastRow, 0, true));
                }
            }
        });
        
        btnSuaNV.addActionListener(evt -> {
            int row = tableNhanvien.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa!");
                return;
            }

            // 1. Lấy mã nhân viên từ dòng được chọn
            String maNV = tableNhanvien.getValueAt(row, 0).toString();

            // 2. Lấy trọn bộ thông tin (gồm cả Account) từ Database qua DAO
            // (Giả sử bạn đã thêm hàm getThongTinSua trong NhanVien_DAO như tôi đã hướng dẫn)
            Object[] data = null;
			try {
				data = NhanVien_DAO.getThongTinSua(maNV);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

            if (data != null) {
                NhanVien nv = (NhanVien) data[0];
                String user = (String) data[1];
                String pass = (String) data[2];
                int idUser = (int) data[3];

                // 3. Hiển thị Dialog ở chế độ SỬA
                NhanVien_Dialog dlg = new NhanVien_Dialog(QuanLiDuLieu_GUI.this, nv, user, pass, idUser);
                dlg.setVisible(true);

                // 4. Nếu sửa thành công, làm mới bảng và giữ nguyên lựa chọn tại dòng đó
                if (dlg.getNhanVienMoi() != null) {
                    loadDataToTable();
                    tableNhanvien.setRowSelectionInterval(row, row);
                }
            }
        });
        
     
   
        
        setTitle("QUẢN Lý Dữ Liệu");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
  	 
    }

 
    
    // Nạp lại dữ liệu từ controller vào model bảng khuyến mãi
    private void refreshKMTable() {
        if (kmModel == null || khuyenMaiCtr == null)
            return;
        kmModel.setRowCount(0);
        for (KhuyenMai k : khuyenMaiCtr.getDanhSachKhuyenMai()) {
            String ten = k.getTenKhuyenMai();
            String phan = String.format("%.0f%%", k.getPhanTramGiam());
            kmModel.addRow(
                    new Object[] { k.getMaKhuyenMai(), ten, phan, k.getNgayBatDauFormatted(), k.getNgayKetThucFormatted(), k.getMoTa() });
        }
    }
    
    public void loadDataToTable() {
        try {
            // Giả sử lớp chứa hàm getAllNhanVien là NhanVien_DAO
//            NhanVien_DAO dao = new NhanVien_DAO(); 
//            ArrayList<Nhanvien> list = dao.getAllNhanVien();
// Đổi thành xử lý phương thức phía controller
        

            // Xóa sạch dữ liệu cũ trên table trước khi nạp mới
            modelNhanvien.setRowCount(0);
        	NhanVien_DAO dao = new NhanVien_DAO();
        	ArrayList<NhanVien> list = dao.getAllNhanVien();
            // Duyệt danh sách và thêm từng dòng vào model
            for (NhanVien nv : list) {
                Object[] row = {
                    nv.getManv(), 
                    nv.getHoten(), 
                    nv.getGioiTinh(), 
                    nv.getCaLamViec(), 
                    nv.getSdt(), 
                    nv.getEmail(), 
                    nv.getChucVu(), 
                    nv.getIdUser()
                };
                modelNhanvien.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    
    

    public static void main(String[] args) {
        QuanLiDuLieu_GUI frame = new QuanLiDuLieu_GUI();
        frame.setVisible(true);
    }

}
