package iuh.fit.son23641341.nhahanglau_phantan.gui;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import iuh.fit.son23641341.nhahanglau_phantan.control.BanAn_Ctr;
import iuh.fit.son23641341.nhahanglau_phantan.control.MonAn_Ctr;
import iuh.fit.son23641341.nhahanglau_phantan.control.PhieuDatBan_Ctr;
import iuh.fit.son23641341.nhahanglau_phantan.entity.ChiTietDatMon;
import iuh.fit.son23641341.nhahanglau_phantan.entity.MonAn;
import iuh.fit.son23641341.nhahanglau_phantan.entity.PhieuDatBan;

public class DanhSachMonAnNV_GUI extends JFrame {

	private static final long serialVersionUID = 1L;

	// ======================= CÁC HẰNG SỐ =======================
	private static final Color PRIMARY_COLOR = new Color(0xDC4332);
	private static final Color ACTIVE_BG = new Color(0xF5E6D3);
	private static final int DISH_TILE_HEIGHT = 200; 

	// ======================= BIẾN CONTROLLER VÀ DANH SÁCH =======================
	private MonAn_Ctr monAnCtr;
	private ArrayList<ChiTietDatMon> chiTietDonHangHienTai;
	private PhieuDatBan_Ctr phieuDatBanCtr;

	// ======================= BIẾN GIAO DIỆN =======================
	private JPanel dishesPanel;
	private JTextField searchField;
	private JPanel orderItemsPanel;
	private JLabel totalLabel;
	
	// ======================= BIẾN DỮ LIỆU TRUYỀN VÀO =======================
	private int soBan;
	private ArrayList<Integer> danhSachBanDaChon; 
	private String ngayDat; 
	private PhieuDatBan phieuTamThoi;
	private boolean isThanhVienTamThoi;
	private boolean isCheckboxThanhVienTamThoi;
	private String maPhieuDangChinhSua; 
	private String cheDoHienThi; 

	// ======================= KHỐI KHỞI TẠO ICON =======================
	{
		URL iconURL = getClass().getResource("/images/icon/logo.png");
		if (iconURL != null) {
			ImageIcon icon = new ImageIcon(iconURL);
			setIconImage(icon.getImage());
		}
	}

	// ======================= CONSTRUCTORS =======================
	
	// Constructor đơn giản (ít dùng, chủ yếu để test)
	public DanhSachMonAnNV_GUI(int soBan) {
		this(soBan, new ArrayList<>(), null, null, false, false, null, null);
		this.danhSachBanDaChon.add(soBan);
	}
	
	// Constructor hay dùng 1
	public DanhSachMonAnNV_GUI(int soBan, ArrayList<Integer> danhSachBanDaChon, String ngayDat) {
		this(soBan, danhSachBanDaChon, ngayDat, null, false, false, null, null);
	}

	// Constructor hay dùng 2 (từ PhieuDat_GUI)
	public DanhSachMonAnNV_GUI(int soBan, ArrayList<Integer> danhSachBanDaChon, String ngayDat, PhieuDatBan phieuTamThoi, boolean isThanhVienTamThoi, boolean isCheckboxThanhVienTamThoi) {
		this(soBan, danhSachBanDaChon, ngayDat, phieuTamThoi, isThanhVienTamThoi, isCheckboxThanhVienTamThoi, null, null);
	}
	
	// Constructor CHÍNH (Full tham số)
	public DanhSachMonAnNV_GUI(int soBan, ArrayList<Integer> danhSachBanDaChon, String ngayDat, PhieuDatBan phieuTamThoi, boolean isThanhVienTamThoi, boolean isCheckboxThanhVienTamThoi, String maPhieuDangChinhSua, String cheDoHienThi) {
		this.soBan = soBan;
		this.danhSachBanDaChon = (danhSachBanDaChon != null) ? danhSachBanDaChon : new ArrayList<>();
		if (this.danhSachBanDaChon.isEmpty()) this.danhSachBanDaChon.add(soBan);
		
		this.ngayDat = ngayDat;
		this.phieuTamThoi = phieuTamThoi;
		this.isThanhVienTamThoi = isThanhVienTamThoi;
		this.isCheckboxThanhVienTamThoi = isCheckboxThanhVienTamThoi;
		this.maPhieuDangChinhSua = maPhieuDangChinhSua;
		this.cheDoHienThi = cheDoHienThi;
		
		this.monAnCtr = new MonAn_Ctr();
		this.phieuDatBanCtr = PhieuDatBan_Ctr.getInstance();
		
		// Lấy danh sách món hiện tại của bàn (để hiển thị lên giỏ hàng bên phải)
		this.chiTietDonHangHienTai = phieuDatBanCtr.layDanhSachMonAnChoBan(soBan);
		if (this.chiTietDonHangHienTai == null) this.chiTietDonHangHienTai = new ArrayList<>();

		initUI();
	}

	// ======================= KHỞI TẠO GIAO DIỆN =======================
	private void initUI() {
		setTitle("Danh sách món ăn - Nhân viên");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Chỉ đóng cửa sổ này, không tắt app
		setSize(1200, 800);
		setLocationRelativeTo(null);
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		// --- MAIN PANEL (TRÁI) ---
		JPanel mainPanel = new JPanel(new BorderLayout());
		
		// Header tìm kiếm
		JPanel headerPanel = createHeaderPanel();
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		
		// Sidebar
		SideBar_GUI sidebar = new SideBar_GUI();
		sidebar.setMauNutKhiChon("Quản Lý Món");
		mainPanel.add(sidebar, BorderLayout.WEST);

		// Content (Menu + List món)
		JPanel contentContainerPanel = new JPanel(new BorderLayout());
		
		JPanel menuPanel = createMenuPanel();
		contentContainerPanel.add(menuPanel, BorderLayout.NORTH);

		this.dishesPanel = new JPanel(new GridLayout(0, 3, 15, 15));
		this.dishesPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
		contentContainerPanel.add(new JScrollPane(dishesPanel), BorderLayout.CENTER);
		
		// Load dữ liệu món ăn
		loadDishesData(monAnCtr.getDanhSachMonAn());
		
		mainPanel.add(contentContainerPanel, BorderLayout.CENTER);

		// --- ORDER PANEL (PHẢI) ---
		JPanel orderPanel = createOrderPanel();

		// Add vào ContentPane
		contentPane.add(mainPanel, BorderLayout.CENTER);
		contentPane.add(orderPanel, BorderLayout.EAST);

		capNhatGiaoDienDonHang();
	}

	// ======================= CÁC PANEL CON =======================
	
	private JPanel createHeaderPanel() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.setPreferredSize(new Dimension(0, 40));
		panel.setBackground(PRIMARY_COLOR);
		panel.setBorder(new EmptyBorder(5, 10, 5, 10));

		JLabel titleLabel = new JLabel("Tìm kiếm");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
		titleLabel.setForeground(Color.WHITE);

		searchField = new JTextField(20);
		JButton searchButton = new JButton("🔍");
		searchButton.addActionListener(e -> {
			String query = searchField.getText();
			ArrayList<MonAn> ketQua = (ArrayList<MonAn>) monAnCtr.timTheoTen(query);
			loadDishesData(ketQua);
		});

		JPanel searchContainer = new JPanel(new BorderLayout());
		searchContainer.add(searchField, BorderLayout.CENTER);
		searchContainer.add(searchButton, BorderLayout.EAST);

		panel.add(searchContainer, BorderLayout.CENTER);
		panel.add(titleLabel, BorderLayout.WEST);

		return panel;
	}

	private JPanel createMenuPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		panel.setBackground(Color.WHITE);

		String[] buttonNames = { "ALL", "Khai Vị", "Món Chính", "Món Mặn", "Thức uống", "Khác" };

		for (String buttonName : buttonNames) {
			JButton button = new JButton(buttonName);
			button.setForeground(new Color(255, 140, 0));
			button.setBackground(Color.WHITE);
			button.setFocusPainted(false);
			button.setFont(new Font("Arial", Font.BOLD, 12));
			button.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(new Color(255, 69, 17), 2),
					BorderFactory.createEmptyBorder(8, 15, 8, 15)));

			button.addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					button.setBackground(new Color(255, 140, 0));
					button.setForeground(Color.WHITE);
				}
				@Override
				public void mouseExited(java.awt.event.MouseEvent evt) {
					button.setBackground(Color.WHITE);
					button.setForeground(new Color(255, 140, 0));
				}
			});

			button.addActionListener(e -> {
				String loaiMon = button.getText();
				ArrayList<MonAn> ketQuaFilter = new ArrayList<>();

				if (loaiMon.equalsIgnoreCase("ALL")) {
					ketQuaFilter = monAnCtr.getDanhSachMonAn();
				} else {
					for (MonAn mon : monAnCtr.getDanhSachMonAn()) {
						String loaiMonTrongData = mon.getLoaiMon();
						if (loaiMon.equalsIgnoreCase("Thức uống") && loaiMonTrongData.equalsIgnoreCase("Đồ uống")) {
							ketQuaFilter.add(mon);
						} else if (loaiMonTrongData.equalsIgnoreCase(loaiMon)) {
							ketQuaFilter.add(mon);
						}
					}
				}
				loadDishesData(ketQuaFilter);
			});
			panel.add(button);
		}
		return panel;
	}

	private void loadDishesData(ArrayList<MonAn> danhSach) {
		dishesPanel.removeAll();
		if (danhSach != null) {
			for (MonAn mon : danhSach) {
				JPanel dishItem = new JPanel(new BorderLayout());
				dishItem.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
				dishItem.setPreferredSize(new Dimension(250, DISH_TILE_HEIGHT));
				dishItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, DISH_TILE_HEIGHT));
				dishItem.setBackground(Color.WHITE);

				// Placeholder ảnh
				JLabel imageLabel = new JLabel("Ảnh", SwingConstants.CENTER);
				imageLabel.setOpaque(true);
				imageLabel.setBackground(Color.LIGHT_GRAY);
				imageLabel.setPreferredSize(new Dimension(100, 120));
				// TODO: Load ảnh thật từ path mon.getHinhAnh() nếu có

				JLabel nameLabel = new JLabel(mon.getTenMon(), SwingConstants.CENTER);
				String giaFormatted = String.format("%,.0f VND", mon.getGia());
				JLabel priceLabel = new JLabel(giaFormatted, SwingConstants.CENTER);
				priceLabel.setFont(new Font("Arial", Font.BOLD, 14));

				JPanel infoPanel = new JPanel();
				infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
				infoPanel.add(nameLabel);
				infoPanel.add(priceLabel);
				infoPanel.setBackground(Color.WHITE);

				dishItem.add(imageLabel, BorderLayout.CENTER);
				dishItem.add(infoPanel, BorderLayout.SOUTH);

				dishItem.addMouseListener(new java.awt.event.MouseAdapter() {
					@Override
					public void mouseEntered(java.awt.event.MouseEvent e) {
						dishItem.setBackground(ACTIVE_BG);
						infoPanel.setBackground(ACTIVE_BG);
						dishItem.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 2));
					}
					@Override
					public void mouseExited(java.awt.event.MouseEvent e) {
						dishItem.setBackground(Color.WHITE);
						infoPanel.setBackground(Color.WHITE);
						dishItem.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
					}
					@Override
					public void mouseClicked(java.awt.event.MouseEvent e) {
						themMonVaoDonHang(mon);
					}
				});
				dishesPanel.add(dishItem);
			}
		}
		dishesPanel.revalidate();
		dishesPanel.repaint();
	}

	private JPanel createOrderPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		panel.setPreferredSize(new Dimension(350, 0));

		// Header Bàn
		String tenBan = String.format("Bàn %03d", soBan);
		JLabel tableLabel = new JLabel(tenBan, SwingConstants.CENTER);
		tableLabel.setFont(new Font("Arial", Font.BOLD, 24));
		tableLabel.setForeground(new Color(255, 69, 17));
		tableLabel.setOpaque(true);
		tableLabel.setBackground(Color.WHITE);
		tableLabel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(255, 69, 17), 2),
				new EmptyBorder(10, 0, 10, 0)));
		panel.add(tableLabel, BorderLayout.NORTH);

		// List món đã chọn
		JPanel centerWrapperPanel = new JPanel(new BorderLayout());
		centerWrapperPanel.setBackground(Color.WHITE);

		JPanel orderHeaderPanel = createOrderHeaderPanel();
		centerWrapperPanel.add(orderHeaderPanel, BorderLayout.NORTH);

		orderItemsPanel = new JPanel();
		orderItemsPanel.setLayout(new BoxLayout(orderItemsPanel, BoxLayout.Y_AXIS));
		orderItemsPanel.setBackground(Color.WHITE);

		JScrollPane scrollPane = new JScrollPane(orderItemsPanel);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		centerWrapperPanel.add(scrollPane, BorderLayout.CENTER);

		panel.add(centerWrapperPanel, BorderLayout.CENTER);

		// Footer (Tổng tiền + Nút Lưu)
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		totalLabel = new JLabel("TẠM TÍNH: 0 VND");
		totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
		totalLabel.setBorder(new EmptyBorder(0, 0, 10, 0));

		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
		buttonPanel.setPreferredSize(new Dimension(20, 40));

		JButton saveButton = new JButton("Lưu");
		saveButton.setBackground(PRIMARY_COLOR);
		saveButton.setForeground(Color.WHITE);
		saveButton.setFocusPainted(false);
		saveButton.setFont(new Font("Arial", Font.BOLD, 14));
		
		// === XỬ LÝ NÚT LƯU ===
		saveButton.addActionListener(e -> {
			// 1. Cập nhật vào Controller (Singleton)
			phieuDatBanCtr.capNhatDanhSachMonAn(this.soBan, this.chiTietDonHangHienTai);
			
			// 2. CẬP NHẬT DANH SÁCH MÓN MỚI VÀO phieuTamThoi TRƯỚC KHI TRUYỀN ĐI
			if (phieuTamThoi != null) {
				phieuTamThoi.setDanhSachMonAn(new ArrayList<>(this.chiTietDonHangHienTai));
			}
			
			// 3. Đóng cửa sổ hiện tại
			dispose(); 
			
			// 4. Mở lại PhieuDat_GUI (chỉ mở 1 lần ở đây)
			BanAn_Ctr banAnCtr = BanAn_Ctr.getInstance();
			PhieuDatBan_Ctr phieuCtr = PhieuDatBan_Ctr.getInstance();
			
			PhieuDat_GUI guiPhieu = new PhieuDat_GUI(
				this.soBan,
				banAnCtr,
				phieuCtr,
				ngayDat,
				danhSachBanDaChon,
				phieuTamThoi,
				isThanhVienTamThoi,
				isCheckboxThanhVienTamThoi
			);
			guiPhieu.setVisible(true);
		});

		JButton backButton = new JButton("Quay lại");
		backButton.setBackground(Color.GRAY);
		backButton.setForeground(Color.WHITE);
		backButton.setFocusPainted(false);
		backButton.setFont(new Font("Arial", Font.BOLD, 14));
		backButton.addActionListener(e -> {
			// Quay lại mà không lưu thay đổi món ăn mới thêm (nếu muốn)
			// Hoặc lưu luôn tùy logic. Ở đây ta chọn không lưu.
			dispose();
			BanAn_Ctr banAnCtr = BanAn_Ctr.getInstance();
			PhieuDatBan_Ctr phieuCtr = PhieuDatBan_Ctr.getInstance();
			
			new PhieuDat_GUI(
				this.soBan,
				banAnCtr,
				phieuCtr,
				ngayDat,
				danhSachBanDaChon,
				phieuTamThoi,
				isThanhVienTamThoi,
				isCheckboxThanhVienTamThoi
			).setVisible(true);
		});

		buttonPanel.add(backButton);
		buttonPanel.add(saveButton);

		bottomPanel.add(totalLabel, BorderLayout.NORTH);
		bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

		panel.add(bottomPanel, BorderLayout.SOUTH);

		return panel;
	}

	private JPanel createOrderHeaderPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.setBackground(new Color(230, 230, 230));
		panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
		panel.setPreferredSize(new Dimension(0, 30));

		Font headerFont = new Font("Arial", Font.BOLD, 12);

		JLabel sttLabel = new JLabel("#");
		sttLabel.setFont(headerFont);
		sttLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
		panel.add(sttLabel);

		JLabel tenMonLabel = new JLabel("TÊN MÓN");
		tenMonLabel.setFont(headerFont);
		tenMonLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.add(tenMonLabel);

		panel.add(Box.createHorizontalGlue());

		JLabel soLuongLabel = new JLabel("SL");
		soLuongLabel.setFont(headerFont);
		soLuongLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
		panel.add(soLuongLabel);

		JLabel giaMonLabel = new JLabel("GIÁ");
		giaMonLabel.setFont(headerFont);
		giaMonLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
		panel.add(giaMonLabel);

		return panel;
	}

	// ======================= LOGIC XỬ LÝ ĐƠN HÀNG =======================

	private void themMonVaoDonHang(MonAn mon) {
		for (ChiTietDatMon item : chiTietDonHangHienTai) {
			if (item.getMonAn().getMaMon().equals(mon.getMaMon())) {
				item.increment();
				capNhatGiaoDienDonHang();
				return;
			}
		}
		ChiTietDatMon newItem = new ChiTietDatMon(mon, 1);
		chiTietDonHangHienTai.add(newItem);
		capNhatGiaoDienDonHang();
	}

	private void giamSoLuongMon(ChiTietDatMon item) {
		item.decrement();
		if (item.getSoLuong() <= 0) {
			chiTietDonHangHienTai.remove(item);
		}
		capNhatGiaoDienDonHang();
	}

	private void capNhatGiaoDienDonHang() {
		orderItemsPanel.removeAll();
		double tongTien = 0;
		int stt = 1;

		for (ChiTietDatMon item : chiTietDonHangHienTai) {
			JPanel itemPanel = createOrderItemPanel(item, stt);
			orderItemsPanel.add(itemPanel);
			orderItemsPanel.add(Box.createRigidArea(new Dimension(0, 2)));
			tongTien += item.getMonAn().getGia() * item.getSoLuong();
			stt++;
		}

		totalLabel.setText(String.format("TẠM TÍNH: %,.0f VND", tongTien));
		orderItemsPanel.revalidate();
		orderItemsPanel.repaint();
	}

	private JPanel createOrderItemPanel(ChiTietDatMon item, int stt) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
		panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		panel.setPreferredSize(new Dimension(0, 50));

		MonAn mon = item.getMonAn();
		
		JLabel sttLabel = new JLabel(String.format("%02d", stt));
		sttLabel.setFont(new Font("Arial", Font.BOLD, 14));
		sttLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
		panel.add(sttLabel);

		JLabel tenMonLabel = new JLabel(mon.getTenMon());
		tenMonLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		tenMonLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.add(tenMonLabel);

		panel.add(Box.createHorizontalGlue());

		JLabel soLuongLabel = new JLabel("x" + item.getSoLuong());
		soLuongLabel.setFont(new Font("Arial", Font.BOLD, 14));
		soLuongLabel.setForeground(PRIMARY_COLOR);
		soLuongLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
		panel.add(soLuongLabel);

		JLabel giaMonLabel = new JLabel(String.format("%,.0f", mon.getGia()));
		giaMonLabel.setFont(new Font("Arial", Font.BOLD, 14));
		giaMonLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
		panel.add(giaMonLabel);

		panel.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				giamSoLuongMon(item);
			}
			@Override
			public void mouseEntered(java.awt.event.MouseEvent e) {
				panel.setBackground(new Color(245, 245, 245));
			}
			@Override
			public void mouseExited(java.awt.event.MouseEvent e) {
				panel.setBackground(Color.WHITE);
			}
		});

		return panel;
	}
}


