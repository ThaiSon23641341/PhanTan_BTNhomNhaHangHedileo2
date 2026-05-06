package iuh.fit.son23641341.nhahanglau_phantan.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import iuh.fit.son23641341.nhahanglau_phantan.control.KhachHang_Ctr; 
import iuh.fit.son23641341.nhahanglau_phantan.entity.KhachHangThanhVien;

// Triển khai giao diện ActionListener
public class QuanLyKhachHang_GUI extends JFrame implements ActionListener {

    // --- Components và Control ---
    private DefaultTableModel model;
    private JTable table;
    private JTextField searchField;
    private JButton addButton; 
    private JButton editButton;   
    private JButton deleteButton; 
    private KhachHang_Ctr khControl; 
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // --- Hằng màu cho Background ---
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);

    public QuanLyKhachHang_GUI() {

        // KHỞI TẠO CONTROL
        khControl = new KhachHang_Ctr();
        
        setTitle("Quản Lý Khách Hàng");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);

        // SIDEBAR
        SideBar_GUI sidebar = new SideBar_GUI(); // Giả định SideBar_GUI tồn tại
        sidebar.setMauNutKhiChon("Khách Hàng");
        add(sidebar, BorderLayout.WEST);

        JPanel contentWrapperPanel = new JPanel(new BorderLayout());
        contentWrapperPanel.setOpaque(false);

        // --- Header Panel ---
        RoundedPanel roundedHeaderContainer = new RoundedPanel(25); 
        roundedHeaderContainer.setBackground(Color.WHITE);
        roundedHeaderContainer.setLayout(new BorderLayout());
        roundedHeaderContainer.setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Khách Hàng");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setForeground(new Color(231, 76, 60));


        headerPanel.add(titleLabel, BorderLayout.WEST);
        roundedHeaderContainer.add(headerPanel, BorderLayout.CENTER);

        JPanel headerWrapper = new JPanel(new BorderLayout());
        headerWrapper.setOpaque(false);
        headerWrapper.setBorder(new EmptyBorder(0, 40, 0, 40));
        headerWrapper.add(roundedHeaderContainer, BorderLayout.CENTER);
        contentWrapperPanel.add(headerWrapper, BorderLayout.NORTH);

        // MAIN CONTENT 
        JPanel mainContent = new JPanel(new BorderLayout(10, 20));
        mainContent.setOpaque(false);
        mainContent.setBorder(new EmptyBorder(20, 40, 40, 40));

      
        RoundedPanel actionBarPanel = new RoundedPanel(25);
        actionBarPanel.setLayout(new BorderLayout());
        actionBarPanel.setBackground(Color.WHITE);
        actionBarPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setOpaque(false);

        JLabel searchIconLabel = new JLabel("🔍"); 
        searchIconLabel.setPreferredSize(new Dimension(50, 45));
        searchIconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        searchField = new JTextField("Nhập số điện thoại..."); 
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchField.setForeground(Color.GRAY);
        searchField.setBorder(new EmptyBorder(0, 10, 0, 0));
        searchField.setOpaque(false);
        addPlaceholderStyle(searchField);

        searchPanel.add(searchIconLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setOpaque(false);

        // Khai báo các nút và thêm Listener
        this.addButton = createStyledButton("Thêm Khách Hàng", new Color(46, 204, 113));
        this.editButton = createStyledButton("Sửa Thông Tin", new Color(52, 152, 219));
        this.deleteButton = createStyledButton("Xóa Khách Hàng", new Color(231, 76, 60)); 

        buttonPanel.add(this.addButton);
        buttonPanel.add(this.editButton);
        buttonPanel.add(this.deleteButton);
        
        // Thêm ActionListener 
        this.addButton.addActionListener(this); 
        this.editButton.addActionListener(this); 
        this.deleteButton.addActionListener(this); 

        actionBarPanel.add(searchPanel, BorderLayout.CENTER);
        actionBarPanel.add(buttonPanel, BorderLayout.EAST);
        mainContent.add(actionBarPanel, BorderLayout.NORTH);
        
        // GỌI HÀM THÊM CHỨC NĂNG TÌM KIẾM
        addSearchDocumentListener(searchField); 

        // --- Table ---
        String[] columnNames = { "Mã KH", "Tên KH", "Số Điện Thoại", "Email", "Giới Tính", "Thành Viên", "Điểm Số", "Ngày Tạo" };
        
        // KHỞI TẠO MODEL VÀ TABLE 
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        styleTable(table);

        // Nạp dữ liệu ban đầu
        loadTableData(model, khControl.getDanhSachKhachHang());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainContent.add(scrollPane, BorderLayout.CENTER);

        contentWrapperPanel.add(mainContent, BorderLayout.CENTER);
        add(contentWrapperPanel, BorderLayout.CENTER);
    }

    // Xử lý sự kiện nhấn nút 
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            xuLyNutThem();
        } else if (e.getSource() == editButton) {
            xuLyNutSua();
        } else if (e.getSource() == deleteButton) {
            xuLyNutXoa(); 
        }
    }

// ---------------------------------------------------------------------------------------------------
// LOGIC XỬ LÝ CÁC NÚT BẤM 
// ---------------------------------------------------------------------------------------------------

    private void xuLyNutThem() {
        ThemKhachHang_Dialog dialog = new ThemKhachHang_Dialog(this);
        dialog.setVisible(true);

        KhachHangThanhVien khachHangThanhVienMoi = dialog.getKhachHangMoi();

        if (khachHangThanhVienMoi != null) {
            boolean success = khControl.themKhachHang(khachHangThanhVienMoi);

            if (success) {
                JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadTableData(model, khControl.getDanhSachKhachHang());
            } else {
                JOptionPane.showMessageDialog(this, "Thêm khách hàng thất bại! (Có thể do trùng Số Điện Thoại)", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void xuLyNutSua() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần sửa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maKH = model.getValueAt(selectedRow, 0).toString();
        
        // Lấy đối tượng khách hàng gốc từ danh sách trong Controller
        KhachHangThanhVien khachHangThanhVienGoc = timKhachHangTheoMa(maKH);
        
        if (khachHangThanhVienGoc == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy dữ liệu gốc của khách hàng này.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Mở dialog sửa, truyền Khách hàng gốc vào
        SuaKhachHang_Dialog dialogSua = new SuaKhachHang_Dialog(this, khachHangThanhVienGoc);
        dialogSua.setVisible(true);
        
        KhachHangThanhVien khachHangThanhVienCapNhat = dialogSua.getKhachHangCapNhat();

        // Nếu khachHangCapNhat != null, điều này có nghĩa là người dùng đã nhấn nút "Lưu Thay Đổi"
        // và việc cập nhật vào DATABASE đã được thực hiện thành công bên trong SuaKhachHang_Dialog.
        if (khachHangThanhVienCapNhat != null) {
            // Ta chỉ cần load lại bảng để hiển thị dữ liệu mới nhất từ DB
            loadTableData(model, khControl.getDanhSachKhachHang()); 
            
            // Tùy chọn: chọn lại dòng vừa sửa
            int rowToSelect = findRowByMaKH(khachHangThanhVienCapNhat.getMaKhachHang());
            if (rowToSelect != -1) {
                table.setRowSelectionInterval(rowToSelect, rowToSelect);
            }
        }
    }
    
    private void xuLyNutXoa() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Lấy Mã KH và Tên từ dòng được chọn
        String maKH = model.getValueAt(selectedRow, 0).toString();
        String hoTen = model.getValueAt(selectedRow, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn xóa khách hàng " + hoTen + " (Mã: " + maKH + ")?", 
            "Xác nhận xóa", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Gọi Control để xóa
            boolean success = khControl.xoaKhachHang(maKH);

            if (success) {
                JOptionPane.showMessageDialog(this, "Xóa khách hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadTableData(model, khControl.getDanhSachKhachHang()); 
            } else {
                // Lỗi có thể do khóa ngoại (Khách hàng có hóa đơn)
                JOptionPane.showMessageDialog(this, "Xóa khách hàng thất bại! (Kiểm tra khóa ngoại)", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Phương thức trợ giúp: Tìm đối tượng KhachHang gốc từ Mã KH
     */
    private KhachHangThanhVien timKhachHangTheoMa(String maKH) {
        // Cần đảm bảo maKH được truyền vào không có khoảng trắng thừa
        String trimmedMaKH = maKH.trim(); 
        for (KhachHangThanhVien kh : khControl.getDanhSachKhachHang()) {
            if (kh.getMaKhachHang().trim().equals(trimmedMaKH)) {
                return kh;
            }
        }
        return null;
    }

    /**
     * Phương thức trợ giúp: Tìm dòng trong JTable theo Mã KH
     */
    private int findRowByMaKH(String maKH) {
        String trimmedMaKH = maKH.trim();
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).toString().trim().equals(trimmedMaKH)) {
                return i;
            }
        }
        return -1;
    }


    
    /**
     * Thêm DocumentListener để tự động tìm kiếm khi người dùng gõ
     */

    private void addSearchDocumentListener(JTextField textField) {
        textField.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                SwingUtilities.invokeLater(() -> {
                    String text = textField.getText().trim();
                    boolean isPlaceholder = textField.getForeground().equals(Color.gray) && text.equals("Nhập số điện thoại...");

                    if (text.isEmpty() || isPlaceholder) {
                        loadTableData(model, khControl.getDanhSachKhachHang());
                    } else {
                        ArrayList<KhachHangThanhVien> filtered = khControl.timKhachHangTheoSDT(text);
                        loadTableData(model, filtered);
                    }
                });
            }

            @Override public void insertUpdate(DocumentEvent e) { update(); }
            @Override public void removeUpdate(DocumentEvent e) { update(); }
            @Override public void changedUpdate(DocumentEvent e) { update(); }
        });
    }

    
    /**
     * Nạp dữ liệu từ ArrayList<KhachHang> lên JTable.
     */

    private void loadTableData(DefaultTableModel model, ArrayList<KhachHangThanhVien> list) {
        model.setRowCount(0); // Xóa dữ liệu cũ
        if (list == null) return;

        for (KhachHangThanhVien kh : list) {
            Object[] row = new Object[] {
                kh.getMaKhachHang().trim(), // Loại bỏ khoảng trắng thừa
                kh.getHoTen(),
                kh.getSoDienThoai(),
                kh.getEmail(),
                kh.getGioiTinh().trim(), // Loại bỏ khoảng trắng thừa
                kh.getThanhVien().trim(), // Loại bỏ khoảng trắng thừa
                (int) kh.getDiemTichLuy(),
                kh.getNgayDangKy() != null ? kh.getNgayDangKy().format(dateFormatter) : ""
            };
            model.addRow(row);
        }
    }


    // thiết kế 
    private JButton createStyledButton(String text, Color color) {
       
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(12, 25, 12, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public void addPlaceholderStyle(JTextField textField) {
        
        final String placeholder = "Nhập số điện thoại...";
        Font font = textField.getFont();
        
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder) && textField.getForeground().equals(Color.gray)) {
                    textField.setText("");
                    textField.setFont(font.deriveFont(Font.PLAIN));
                    textField.setForeground(Color.black);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setFont(font.deriveFont(Font.ITALIC));
                    textField.setForeground(Color.gray);
                    
                    loadTableData(model, khControl.getDanhSachKhachHang());
                }
            }
        });
        
        // Áp dụng style placeholder ban đầu
        textField.setText(placeholder);
        textField.setFont(font.deriveFont(Font.ITALIC));
        textField.setForeground(Color.gray);
    }

    private void styleTable(JTable table) {
        table.setFillsViewportHeight(true);
        table.setRowHeight(40);
        table.setFont(new Font("Arial", Font.PLAIN, 15));
        table.setGridColor(new Color(221, 221, 221));

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(100, 50));
        header.setFont(new Font("Arial", Font.BOLD, 16));
        header.setBackground(new Color(189, 195, 199));
        header.setForeground(new Color(51, 51, 51));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);

        table.setDefaultRenderer(Object.class, new AlternatingRowColorRenderer());
    }

    class AlternatingRowColorRenderer extends DefaultTableCellRenderer {
        private final Color evenColor = new Color(236, 240, 241);
        private final Color oddColor = Color.WHITE;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setBorder(new EmptyBorder(0, 10, 0, 10));
            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? evenColor : oddColor);
            } else {
                c.setBackground(table.getSelectionBackground());
                c.setForeground(table.getSelectionForeground());
            }
            return c;
        }
    }

    class RoundedPanel extends JPanel {
        private int cornerRadius;

        public RoundedPanel(int radius) {
            super();
            this.cornerRadius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Dimension arcs = new Dimension(cornerRadius, cornerRadius);
            int width = getWidth();
            int height = getHeight();
            Graphics2D graphics = (Graphics2D) g;
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setColor(getBackground());
            graphics.fillRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);
        }
    }

    public static void main(String[] args) {
        // Giả sử ConnectDB đã được gọi ở đâu đó trước khi chạy GUI
        SwingUtilities.invokeLater(() -> new QuanLyKhachHang_GUI().setVisible(true));
    }
}
