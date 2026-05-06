package iuh.fit.son23641341.nhahanglau_phantan.gui;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors; // Cần import cho phần lọc

import iuh.fit.son23641341.nhahanglau_phantan.control.MonAn_Ctr;
import iuh.fit.son23641341.nhahanglau_phantan.control.User_Ctr;
import iuh.fit.son23641341.nhahanglau_phantan.entity.MonAn;

// Lưu ý: Cần giả định rằng SideBar_GUI và ThemMonAn_Dialog tồn tại để code chạy được.

public class DanhSachMonAnQL_GUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final Color PRIMARY_COLOR = new Color(0xDC4332);
    // Grid config to keep tile size stable regardless of item count
    private static final int GRID_COLS = 3;
    private static final int GRID_HGAP = 15;
    private static final int GRID_VGAP = 15;
    private static final int DISH_TILE_HEIGHT = 200; // px

    // Form fields cho chỉnh sửa món ăn
    private JTextField txtMaMon;
    private JTextField txtTenMon;
    private JTextField txtGiaMon;
    private JTextField txtLoaiMon;
    private JTextField txtMoTa;
    private JButton btnSaveDish;
    private JButton btnCancelEdit;
    private JLabel lblEditTitle;

    // Controller và khu vực hiển thị món
    private MonAn_Ctr monAnCtr;
    private JPanel dishesPanel;

    // Field cho tìm kiếm
    private JTextField txtSearch;

    /**
     * Launch the application.
     */
    {
        // Khi cửa sổ được mở, chuyển sang trạng thái toàn màn hình (maximized)
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
            }
        });
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    DanhSachMonAnQL_GUI frame = new DanhSachMonAnQL_GUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public DanhSachMonAnQL_GUI() {
        setTitle("Quản lý món ăn - Quản lý");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Khởi tạo controller dữ liệu
        monAnCtr = new MonAn_Ctr();

        // Sử dụng BorderLayout cho content pane của JFrame
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        // === KHU VỰC BÊN TRÁI (MAIN PANEL) ===
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 1. Tim kiem
        JPanel headerPanel = createHeaderPanel();

        // 3. Content Container Panel (Chứa menu vàng và danh sách món ăn)
        JPanel contentContainerPanel = new JPanel(new BorderLayout());

        // 3a. Menu Panel (Màu vàng)
        JPanel menuPanel = createMenuPanel();

        // 3b. Dishes Panel (Khu vực hiển thị món ăn)
        dishesPanel = createDishesPanel(monAnCtr.getDanhSachMonAn()); // Truyền danh sách mặc định

        // Thêm menu panel và dishes panel vào content container
        contentContainerPanel.add(menuPanel, BorderLayout.NORTH);
        contentContainerPanel.add(new JScrollPane(dishesPanel), BorderLayout.CENTER); // Bọc trong JScrollPane để có thể
                                                                                      // cuộn

        // Thêm các panel con vào mainPanel
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Cần lớp SideBar_GUI
        // Giả định: SideBar_GUI sidebar = new SideBar_GUI();
        // sidebar.setMauNutKhiChon("Quản Lý Món");
        mainPanel.add(new SideBar_GUI(), BorderLayout.WEST); // Dùng constructor mặc định nếu có
        mainPanel.add(contentContainerPanel, BorderLayout.CENTER);

        // === KHU VỰC BÊN PHẢI (EDIT PANEL) ===
        // 4. Edit Panel - Form chỉnh sửa món ăn
        JPanel editPanel = createEditPanel();

        // Thêm mainPanel và editPanel vào cửa sổ chính (JFrame)
        contentPane.add(mainPanel, BorderLayout.CENTER);
        contentPane.add(editPanel, BorderLayout.EAST);
    }

    // ======================= START TIM KIEM ĐÃ SỬA LỖI ======================

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setPreferredSize(new Dimension(0, 40));
        panel.setBackground(PRIMARY_COLOR);
        panel.setBorder(new EmptyBorder(5, 10, 5, 10));

        JLabel titleLabel = new JLabel("Tìm kiếm");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);

        txtSearch = new JTextField(20);
        JButton searchButton = new JButton("🔍");
        searchButton.setBackground(Color.WHITE);
        searchButton.setFocusPainted(false);

        // Thêm sự kiện tìm kiếm vào nút
        searchButton.addActionListener(e -> {
            searchDishes(txtSearch.getText());
        });

        // Thêm sự kiện Enter vào TextField
        txtSearch.addActionListener(e -> {
            searchDishes(txtSearch.getText());
        });

        JPanel searchContainer = new JPanel(new BorderLayout());
        searchContainer.add(txtSearch, BorderLayout.CENTER);
        searchContainer.add(searchButton, BorderLayout.EAST);

        panel.add(searchContainer, BorderLayout.CENTER);
        panel.add(titleLabel, BorderLayout.WEST);

        return panel;
    }

    /**
     * Phương thức xử lý logic tìm kiếm và cập nhật giao diện.
     * Ưu tiên tìm kiếm theo MÃ MÓN (chính xác), nếu không tìm thấy mới tìm kiếm
     * theo TÊN MÓN (gần đúng).
     * 
     * @param query Chuỗi tìm kiếm theo tên hoặc mã.
     */
    private void searchDishes(String query) {
        String q = query.trim();
        List<MonAn> ketQuaTimKiem = new ArrayList<>();

        if (q.isEmpty()) {
            // Nếu query rỗng, trả về toàn bộ danh sách
            ketQuaTimKiem = monAnCtr.getDanhSachMonAn();
        } else {
            MonAn monTimTheoMa = monAnCtr.timTheoMa(q);
            if (monTimTheoMa != null) {
                ketQuaTimKiem.add(monTimTheoMa);
            } else {
                // Lưu ý: phương thức timTheoTen có logic xử lý query rỗng bên trong, nhưng ta
                // đã check ở ngoài rồi.
                ketQuaTimKiem = monAnCtr.timTheoTen(q);
            }
        }

        // Cập nhật lại khu vực hiển thị món ăn
        refreshDishes(ketQuaTimKiem);
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(Color.WHITE);

        String[] buttonNames = { "ALL", "Khai vị", "Món chính", "Tráng miệng", "Đồ uống" };

        for (String buttonName : buttonNames) {
            JButton button = new JButton(buttonName);

            // Styling
            button.setForeground(new Color(255, 140, 0));
            button.setBackground(Color.WHITE);
            button.setBorder(BorderFactory.createLineBorder(new Color(255, 69, 17), 2));
            button.setFocusPainted(false);
            button.setFont(new Font("Arial", Font.BOLD, 12));
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(255, 69, 17), 2),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)));

            // Thêm sự kiện filter
            button.addActionListener(e -> {
                String loai = buttonName.equalsIgnoreCase("ALL") ? "" : buttonName;

                if (loai.isEmpty()) {
                    refreshDishes(monAnCtr.getDanhSachMonAn());
                } else {
                    // Lọc theo loại món
                    List<MonAn> filteredList = monAnCtr.getDanhSachMonAn().stream()
                            .filter(mon -> mon.getLoaiMon() != null && mon.getLoaiMon().equalsIgnoreCase(loai))
                            .collect(Collectors.toList());
                    refreshDishes(filteredList);
                }
            });

            // Hiệu ứng hover
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

            panel.add(button);
        }

        return panel;
    }

    // ======================= END FILTER ======================

    // ======================= START DISHES ======================

    /**
     * Tạo JPanel chứa các món ăn dựa trên danh sách món được truyền vào.
     * 
     * @param ds Danh sách món ăn cần hiển thị.
     * @return JPanel đã được cấu hình.
     */
    private JPanel createDishesPanel(List<MonAn> ds) {
        JPanel panel = new JPanel(new GridLayout(0, GRID_COLS, GRID_HGAP, GRID_VGAP)); // 0 hàng => tự tính theo số
                                                                                       // lượng
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.WHITE);

        // Ô đầu tiên: nút "+" để thêm món ăn
        JPanel addItemTile = new JPanel(new BorderLayout());
        addItemTile.setBorder(BorderFactory.createDashedBorder(Color.LIGHT_GRAY));
        addItemTile.setBackground(Color.WHITE);
        addItemTile.setPreferredSize(new Dimension(250, DISH_TILE_HEIGHT));
        addItemTile.setMaximumSize(new Dimension(Integer.MAX_VALUE, DISH_TILE_HEIGHT));

        JButton btnAddDish = new JButton("+");
        btnAddDish.setFocusPainted(false);
        btnAddDish.setBorder(BorderFactory.createEmptyBorder());
        btnAddDish.setContentAreaFilled(false);
        btnAddDish.setFont(new Font("Arial", Font.BOLD, 64));
        btnAddDish.setForeground(new Color(255, 69, 17));
        btnAddDish.setToolTipText("Thêm món ăn mới");
        btnAddDish.addActionListener(e -> {
            // Cần lớp ThemMonAn_Dialog
            ThemMonAn_Dialog dialog = new ThemMonAn_Dialog(this);
            dialog.setVisible(true);
            MonAn monMoi = dialog.getMonAnMoi();
            if (monMoi != null) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Bạn có chắc chắn muốn thêm món '" + monMoi.getTenMon() + "'?", "Xác nhận thêm",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean ok = monAnCtr.themMonAn(monMoi);
                    if (ok) {
                        JOptionPane.showMessageDialog(this, "Thêm món ăn thành công!", "Thành công",
                                JOptionPane.INFORMATION_MESSAGE);
                        refreshDishes(monAnCtr.getDanhSachMonAn());
                    } else {
                        JOptionPane.showMessageDialog(this, "Thêm món ăn thất bại (trùng mã hoặc dữ liệu không hợp lệ)",
                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        addItemTile.add(btnAddDish, BorderLayout.CENTER);
        JLabel addLabel = new JLabel("Thêm món ăn", SwingConstants.CENTER);
        addLabel.setFont(new Font("Arial", Font.BOLD, 20));
        addLabel.setForeground(Color.BLACK);
        addLabel.setBorder(new EmptyBorder(5, 0, 8, 0));
        addItemTile.add(addLabel, BorderLayout.SOUTH);
        panel.add(addItemTile);

        // Đổ dữ liệu từ danh sách truyền vào
        for (MonAn mon : ds) {
            final String maMon = mon.getMaMon();
            final String dishName = mon.getTenMon();
            final String dishType = mon.getLoaiMon();
            final String dishPrice = String.format("%,.0f", mon.getGia());
            final String dishDesc = mon.getMoTa() == null ? "" : mon.getMoTa();

            JPanel dishItem = new JPanel(new BorderLayout());
            dishItem.setBackground(Color.WHITE);
            dishItem.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            dishItem.setPreferredSize(new Dimension(250, DISH_TILE_HEIGHT));
            dishItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, DISH_TILE_HEIGHT));

            JLabel imageLabel = new JLabel("Ảnh món ăn", SwingConstants.CENTER);
            imageLabel.setOpaque(true);
            imageLabel.setBackground(new Color(240, 240, 240));
            imageLabel.setPreferredSize(new Dimension(100, 120));

            JLabel nameLabel = new JLabel(dishName, SwingConstants.CENTER);
            nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
            nameLabel.setBorder(new EmptyBorder(5, 0, 0, 0));

            JLabel priceLabel = new JLabel(dishPrice + " VND", SwingConstants.CENTER);
            priceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            priceLabel.setForeground(PRIMARY_COLOR);

            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setBackground(Color.WHITE);
            infoPanel.add(nameLabel);
            infoPanel.add(priceLabel);

            dishItem.add(imageLabel, BorderLayout.CENTER);
            dishItem.add(infoPanel, BorderLayout.SOUTH);

            // Thêm hành động click (tải món ăn vào form chỉnh sửa)
            dishItem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            dishItem.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    loadDishToForm(maMon, dishName, dishType, dishPrice, dishDesc);
                }

                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    dishItem.setBackground(new Color(245, 245, 245));
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    dishItem.setBackground(Color.WHITE);
                }
            });

            panel.add(dishItem);
        }

        // Thông báo nếu không có kết quả tìm kiếm/lọc
        if (ds.isEmpty() && !txtSearch.getText().trim().isEmpty()) {
            JLabel noResultLabel = new JLabel(
                    "Không tìm thấy món ăn nào phù hợp với từ khóa: \"" + txtSearch.getText().trim() + "\"",
                    SwingConstants.CENTER);
            noResultLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            noResultLabel.setPreferredSize(new Dimension(Integer.MAX_VALUE, DISH_TILE_HEIGHT));
            panel.add(noResultLabel);
        }

        return panel;
    }

    // ======================= END DISHES ======================

    // ======================= START EDIT PANEL ======================

    private JPanel createEditPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        panel.setPreferredSize(new Dimension(350, 0));

        // Tiêu đề
        lblEditTitle = new JLabel("Chỉnh sửa món ăn", SwingConstants.CENTER);
        lblEditTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblEditTitle.setForeground(PRIMARY_COLOR);
        lblEditTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblEditTitle.setBorder(new EmptyBorder(20, 10, 20, 10));

        // Panel chứa form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Các trường nhập liệu...
        // Mã món
        JLabel lblMaMon = new JLabel("Mã món:");
        lblMaMon.setFont(new Font("Arial", Font.BOLD, 14));
        txtMaMon = new JTextField();
        txtMaMon.setFont(new Font("Arial", Font.PLAIN, 14));
        txtMaMon.setEditable(false);
        txtMaMon.setBackground(new Color(240, 240, 240));

        // Tên món
        JLabel lblTenMon = new JLabel("Tên món:");
        lblTenMon.setFont(new Font("Arial", Font.BOLD, 14));
        txtTenMon = new JTextField();
        txtTenMon.setFont(new Font("Arial", Font.PLAIN, 14));

        // Loại món
        JLabel lblLoaiMon = new JLabel("Loại món:");
        lblLoaiMon.setFont(new Font("Arial", Font.BOLD, 14));
        txtLoaiMon = new JTextField();
        txtLoaiMon.setFont(new Font("Arial", Font.PLAIN, 14));

        // Giá món
        JLabel lblGiaMon = new JLabel("Giá (VND):");
        lblGiaMon.setFont(new Font("Arial", Font.BOLD, 14));
        txtGiaMon = new JTextField();
        txtGiaMon.setFont(new Font("Arial", Font.PLAIN, 14));

        // Mô tả
        JLabel lblMoTa = new JLabel("Mô tả:");
        lblMoTa.setFont(new Font("Arial", Font.BOLD, 14));
        txtMoTa = new JTextField();
        txtMoTa.setFont(new Font("Arial", Font.PLAIN, 14));

        // Định dạng và thêm vào formPanel (Giữ nguyên logic layout)
        txtMaMon.setPreferredSize(new Dimension(0, 35));
        txtMaMon.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtTenMon.setPreferredSize(new Dimension(0, 35));
        txtTenMon.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtLoaiMon.setPreferredSize(new Dimension(0, 35));
        txtLoaiMon.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtGiaMon.setPreferredSize(new Dimension(0, 35));
        txtGiaMon.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtMoTa.setPreferredSize(new Dimension(0, 80));
        txtMoTa.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        formPanel.add(lblMaMon);
        formPanel.add(javax.swing.Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(txtMaMon);
        formPanel.add(javax.swing.Box.createRigidArea(new Dimension(0, 15)));

        formPanel.add(lblTenMon);
        formPanel.add(javax.swing.Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(txtTenMon);
        formPanel.add(javax.swing.Box.createRigidArea(new Dimension(0, 15)));

        formPanel.add(lblLoaiMon);
        formPanel.add(javax.swing.Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(txtLoaiMon);
        formPanel.add(javax.swing.Box.createRigidArea(new Dimension(0, 15)));

        formPanel.add(lblGiaMon);
        formPanel.add(javax.swing.Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(txtGiaMon);
        formPanel.add(javax.swing.Box.createRigidArea(new Dimension(0, 15)));

        formPanel.add(lblMoTa);
        formPanel.add(javax.swing.Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(txtMoTa);

        // Panel chứa các nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        // Nút LƯU
        btnSaveDish = new JButton("Lưu");
        btnSaveDish.setBackground(PRIMARY_COLOR);
        btnSaveDish.setForeground(Color.WHITE);
        btnSaveDish.setFont(new Font("Arial", Font.BOLD, 14));
        btnSaveDish.setPreferredSize(new Dimension(100, 40));
        btnSaveDish.setFocusPainted(false);
        btnSaveDish.addActionListener(e -> {
            String maMon = txtMaMon.getText();
            String tenMon = txtTenMon.getText();
            String loaiMon = txtLoaiMon.getText();
            String giaText = txtGiaMon.getText();
            String moTa = txtMoTa.getText();

            double gia;
            try {
                // Xử lý chuỗi giá để chỉ còn số
                String normalized = giaText.replaceAll("[^0-9.]", "");
                if (normalized.isEmpty())
                    throw new NumberFormatException();
                gia = Double.parseDouble(normalized);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Giá không hợp lệ", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn lưu thay đổi cho món này?",
                    "Xác nhận sửa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean ok = monAnCtr.capNhatMonAn(maMon, tenMon, loaiMon, gia, moTa);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Lưu thành công", "Thành công",
                            JOptionPane.INFORMATION_MESSAGE);
                    refreshDishes(monAnCtr.getDanhSachMonAn());
                    loadDishToForm(maMon, tenMon, loaiMon, String.format("%,.0f", gia), moTa);
                } else {
                    JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ hoặc món không tồn tại", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Nút HỦY
        btnCancelEdit = new JButton("Hủy");
        btnCancelEdit.setBackground(Color.GRAY);
        btnCancelEdit.setForeground(Color.WHITE);
        btnCancelEdit.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancelEdit.setPreferredSize(new Dimension(100, 40));
        btnCancelEdit.setFocusPainted(false);
        btnCancelEdit.addActionListener(e -> {
            clearEditForm();
        });

        // Nút XÓA
        JButton btnDeleteDish = new JButton("Xóa");
        btnDeleteDish.setBackground(new Color(255, 0, 0));
        btnDeleteDish.setForeground(Color.WHITE);
        btnDeleteDish.setFont(new Font("Arial", Font.BOLD, 14));
        btnDeleteDish.setPreferredSize(new Dimension(100, 40));
        btnDeleteDish.setFocusPainted(false);
        btnDeleteDish.addActionListener(e -> {
            String maMon = txtMaMon.getText();
            if (maMon.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn món ăn cần xóa.", "Cảnh báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn xóa món '" + txtTenMon.getText() + "' không?", "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean ok = monAnCtr.xoaMonAn(maMon);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Xóa món ăn thành công!", "Thành công",
                            JOptionPane.INFORMATION_MESSAGE);
                    clearEditForm();
                    refreshDishes(monAnCtr.getDanhSachMonAn());
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa món ăn thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttonPanel.add(btnSaveDish);
        buttonPanel.add(btnDeleteDish);
        buttonPanel.add(btnCancelEdit);

        // Thêm tất cả vào panel chính
        panel.add(lblEditTitle);
        panel.add(formPanel);
        panel.add(javax.swing.Box.createVerticalGlue());
        panel.add(buttonPanel);
        panel.add(javax.swing.Box.createRigidArea(new Dimension(0, 20)));

        return panel;
    }

    // Helper method để xóa form
    private void clearEditForm() {
        txtMaMon.setText("");
        txtTenMon.setText("");
        txtLoaiMon.setText("");
        txtGiaMon.setText("");
        txtMoTa.setText("");
        lblEditTitle.setText("Chỉnh sửa món ăn");
    }

    // Helper method để load dữ liệu món vào form
    private void loadDishToForm(String maMon, String tenMon, String loaiMon, String gia, String moTa) {
        txtMaMon.setText(maMon);
        txtTenMon.setText(tenMon);
        txtLoaiMon.setText(loaiMon);
        txtGiaMon.setText(gia);
        txtMoTa.setText(moTa);
        lblEditTitle.setText("Chỉnh sửa: " + tenMon);
    }

    /**
     * Làm mới danh sách món ăn trên panel từ một danh sách cụ thể.
     * 
     * @param ds Danh sách món ăn mới để hiển thị.
     */
    private void refreshDishes(List<MonAn> ds) {
        if (dishesPanel == null)
            return;
        dishesPanel.removeAll();
        // Tạo lại nội dung dựa trên danh sách món được truyền vào
        JPanel rebuilt = createDishesPanel(ds);
        // Copy các component từ panel rebuilt sang dishesPanel hiện tại
        for (Component c : rebuilt.getComponents()) {
            dishesPanel.add(c);
        }
        dishesPanel.revalidate();
        dishesPanel.repaint();
    }

    // Cập nhật phương thức refreshDishes() không tham số để gọi phương thức mới
    private void refreshDishes() {
        refreshDishes(monAnCtr.getDanhSachMonAn());
    }

    // ======================= END EDIT PANEL ======================

}
