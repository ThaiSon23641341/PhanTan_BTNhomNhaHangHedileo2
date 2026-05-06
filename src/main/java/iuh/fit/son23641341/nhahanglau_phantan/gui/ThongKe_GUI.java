package iuh.fit.son23641341.nhahanglau_phantan.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// === IMPORT TỪ PROJECT ===
import iuh.fit.son23641341.nhahanglau_phantan.control.User_Ctr;
import iuh.fit.son23641341.nhahanglau_phantan.control.ThongKe_Ctr;
import iuh.fit.son23641341.nhahanglau_phantan.control.ThongKe_Ctr.ThongKeCardData;
import iuh.fit.son23641341.nhahanglau_phantan.dao.ThongKe_DAO;
import iuh.fit.son23641341.nhahanglau_phantan.dao.ThongKe_DAO.TopMonAn;

public class ThongKe_GUI extends JFrame {

    private final ThongKe_Ctr thongKeCtr = ThongKe_Ctr.getInstance();
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    // Màu sắc chủ đạo (Giữ nguyên của bạn)

    private final Color COLOR_BACKGROUND = new Color(236, 240, 241);
    private final Color COLOR_HEADER = new Color(0xDC4332);
    private final Color COLOR_TEXT_HEADER = Color.WHITE;
    private final Color COLOR_PRIMARY_TEXT = new Color(52, 73, 94);
    private final Color COLOR_SECONDARY_TEXT = new Color(127, 140, 141);
    private final Color COLOR_GREEN_GROWTH = new Color(39, 174, 96);
    private final Color COLOR_YELLOW_GROWTH = new Color(243, 156, 18);
    private final Color COLOR_BUTTON_GREEN = new Color(26, 188, 156);
    private final Color BAR_COLOR = Color.decode("#7C1211");

    // Thành phần điều khiển
    private JComboBox<Integer> cbThang;
    private JComboBox<Integer> cbNam;
    private JRadioButton rbTheoThang;
    private JRadioButton rbTheoNgay;
    private com.toedter.calendar.JDateChooser dateChooser;
    private JPanel mainContent; 
    
    // Dữ liệu Top món ăn
    private List<TopMonAn> topMonAnList = new ArrayList<>();
    private int maxLuotGoi = 0;
    private int tongLuotGoiTopN = 0;

    public ThongKe_GUI() {
        if (!User_Ctr.getInstance().isDangNhap()) {
            dispose();
            return;
        }

        setTitle("Thống Kê");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_BACKGROUND);

        // 1. Sidebar (Giữ nguyên)
        SideBar_GUI sidebar = new SideBar_GUI();
        sidebar.setMauNutKhiChon("Thống Kê");
        add(sidebar, BorderLayout.WEST);

        // 2. Content Wrapper
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        add(contentWrapper, BorderLayout.CENTER);

        // 3. Header với bộ chọn Tháng/Năm/Ngày
        contentWrapper.add(createHeader(), BorderLayout.NORTH);

        // 4. Main Content
        mainContent = new JPanel(new BorderLayout(20, 20));
        mainContent.setOpaque(false);
        mainContent.setBorder(new EmptyBorder(20, 30, 20, 30));
        contentWrapper.add(mainContent, BorderLayout.CENTER);

        // Tải dữ liệu mặc định ban đầu
        updateData();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_HEADER);
        header.setPreferredSize(new Dimension(0, 90)); // Tăng chiều cao header
        header.setBorder(new EmptyBorder(10, 30, 10, 30));

        JLabel title = new JLabel("Thống Kê Doanh Thu");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(Color.WHITE);

        // Panel chứa bộ chọn ComboBox thiết kế to rõ
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 12));
        filterPanel.setOpaque(false);

        // Radio buttons for mode selection
        rbTheoThang = new JRadioButton("Thống kê theo tháng", true);
        rbTheoNgay = new JRadioButton("Thống kê theo ngày");
        rbTheoThang.setFont(new Font("Segoe UI", Font.BOLD, 16));
        rbTheoNgay.setFont(new Font("Segoe UI", Font.BOLD, 16));
        rbTheoThang.setForeground(Color.BLACK);
        rbTheoNgay.setForeground(Color.BLACK);
        ButtonGroup group = new ButtonGroup();
        group.add(rbTheoThang);
        group.add(rbTheoNgay);

        // Cấu hình JComboBox Tháng
        JLabel lblT = new JLabel("Tháng:"); 
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblT.setForeground(Color.WHITE);
        cbThang = new JComboBox<>();
        for (int i = 1; i <= 12; i++) cbThang.addItem(i);
        cbThang.setSelectedItem(LocalDate.now().getMonthValue());
        styleComboBox(cbThang);

        // Cấu hình JComboBox Năm
        JLabel lblN = new JLabel("Năm:"); 
        lblN.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblN.setForeground(Color.WHITE);
        cbNam = new JComboBox<>();
        int yearNow = LocalDate.now().getYear();
        for (int i = yearNow - 5; i <= yearNow; i++) cbNam.addItem(i);
        cbNam.setSelectedItem(yearNow);
        styleComboBox(cbNam);

        // Date chooser for day statistics
        dateChooser = new com.toedter.calendar.JDateChooser();
        dateChooser.setDate(java.sql.Date.valueOf(LocalDate.now()));
        dateChooser.setFont(new Font("Segoe UI", Font.BOLD, 18));
        dateChooser.setPreferredSize(new Dimension(150, 45));
        dateChooser.setVisible(false);

        // Sự kiện cập nhật
        cbThang.addItemListener(e -> { if (e.getStateChange() == ItemEvent.SELECTED && rbTheoThang.isSelected()) updateData(); });
        cbNam.addItemListener(e -> { if (e.getStateChange() == ItemEvent.SELECTED && rbTheoThang.isSelected()) updateData(); });
        dateChooser.addPropertyChangeListener("date", evt -> { if (rbTheoNgay.isSelected()) updateData(); });
        rbTheoThang.addActionListener(e -> {
            cbThang.setVisible(true);
            cbNam.setVisible(true);
            lblT.setVisible(true);
            lblN.setVisible(true);
            dateChooser.setVisible(false);
            updateData();
        });
        rbTheoNgay.addActionListener(e -> {
            cbThang.setVisible(false);
            cbNam.setVisible(false);
            lblT.setVisible(false);
            lblN.setVisible(false);
            dateChooser.setVisible(true);
            updateData();
        });

        filterPanel.add(rbTheoThang);
        filterPanel.add(rbTheoNgay);
        filterPanel.add(lblT); filterPanel.add(cbThang);
        filterPanel.add(lblN); filterPanel.add(cbNam);
        filterPanel.add(dateChooser);

        header.add(title, BorderLayout.WEST);
        header.add(filterPanel, BorderLayout.EAST);
        return header;
    }

    private void styleComboBox(JComboBox<Integer> cb) {
        cb.setFont(new Font("Segoe UI", Font.BOLD, 18));
        cb.setPreferredSize(new Dimension(100, 45)); // Tăng kích thước
        cb.setBackground(Color.WHITE);
        cb.setFocusable(false);
    }

    /**
     * Hàm quan trọng: Cập nhật lại toàn bộ UI khi thay đổi Tháng/Năm
     */
    private void updateData() {
        mainContent.removeAll();
        if (rbTheoThang.isSelected()) {
            int thang = (int) cbThang.getSelectedItem();
            int nam = (int) cbNam.getSelectedItem();
            // 1. Lấy dữ liệu thực tế từ Controller
            ThongKeCardData cardData = thongKeCtr.getDuLieuChoTheThongKe(nam, thang);
            topMonAnList = thongKeCtr.getTopMonAn(thang, nam, 5);
            // 2. Tính toán các thông số Top món ăn
            maxLuotGoi = 0; tongLuotGoiTopN = 0;
            if (!topMonAnList.isEmpty()) {
                maxLuotGoi = topMonAnList.get(0).soLuongDat;
                for (TopMonAn m : topMonAnList) tongLuotGoiTopN += m.soLuongDat;
            } else { maxLuotGoi = 1; }
            // --- ROW 1: CARDS ---
            JPanel topRow = new JPanel(new GridLayout(1, 3, 20, 20));
            topRow.setOpaque(false);
            topRow.add(createStatCard("Tổng Doanh Số Tháng", currencyFormat.format(cardData.dtHienTai), cardData.dtChenhLech, "So với tháng trước", (cardData.dtChenhLech.startsWith("▲") ? COLOR_GREEN_GROWTH : Color.RED)));
            topRow.add(createStatCard("Tổng Hóa Đơn", String.valueOf(cardData.hdHienTai), cardData.hdChenhLech, "So với tháng trước", (cardData.hdChenhLech.startsWith("▲") ? COLOR_YELLOW_GROWTH : Color.RED)));
            String best;
            String count;
            if (topMonAnList.isEmpty()) {
                best = "Chưa có dữ liệu";
                count = "";
            } else {
                best = topMonAnList.get(0).tenMonAn;
                count = topMonAnList.get(0).soLuongDat + " Lượt gọi";
            }
            topRow.add(createStatCard("Món ăn bán chạy nhất", best, count, "", null));
            mainContent.add(topRow, BorderLayout.NORTH);
            // --- ROW 2: TOP 5 & BIỂU ĐỒ + PHÂN TÍCH ---
            JPanel bottomRow = new JPanel(new GridLayout(1, 2, 20, 20));
            bottomRow.setOpaque(false);
            // Trái: Top 5
            bottomRow.add(createTop5Panel());
            // Phải: Biểu đồ & Bảng phân tích
            JPanel rightCol = new JPanel();
            rightCol.setLayout(new BoxLayout(rightCol, BoxLayout.Y_AXIS));
            rightCol.setOpaque(false);
            rightCol.add(createGraphPanel(nam), BorderLayout.CENTER); 
            rightCol.add(createAnalysisPanel(nam), BorderLayout.SOUTH);
            bottomRow.add(rightCol);
            mainContent.add(bottomRow, BorderLayout.CENTER);
        } else if (rbTheoNgay.isSelected()) {
            java.util.Date selectedDate = dateChooser.getDate();
            if (selectedDate == null) selectedDate = java.sql.Date.valueOf(LocalDate.now());
            LocalDate localDate = new java.sql.Date(selectedDate.getTime()).toLocalDate();
            // 1. Lấy dữ liệu thực tế từ Controller
            ThongKeCardData cardData = thongKeCtr.getDuLieuChoTheThongKeTheoNgay(localDate);
            topMonAnList = thongKeCtr.getTopMonAnTheoNgay(localDate, 5);
            // 2. Tính toán các thông số Top món ăn
            maxLuotGoi = 0; tongLuotGoiTopN = 0;
            if (!topMonAnList.isEmpty()) {
                maxLuotGoi = topMonAnList.get(0).soLuongDat;
                for (TopMonAn m : topMonAnList) tongLuotGoiTopN += m.soLuongDat;
            } else { maxLuotGoi = 1; }
            // --- ROW 1: CARDS ---
            JPanel topRow = new JPanel(new GridLayout(1, 3, 20, 20));
            topRow.setOpaque(false);
            topRow.add(createStatCard("Tổng Doanh Số Ngày", currencyFormat.format(cardData.dtHienTai), cardData.dtChenhLech, "So với hôm trước", (cardData.dtChenhLech.startsWith("▲") ? COLOR_GREEN_GROWTH : Color.RED)));
            topRow.add(createStatCard("Tổng Hóa Đơn", String.valueOf(cardData.hdHienTai), cardData.hdChenhLech, "So với hôm trước", (cardData.hdChenhLech.startsWith("▲") ? COLOR_YELLOW_GROWTH : Color.RED)));
            String best = topMonAnList.isEmpty() ? "Chưa có dữ liệu" : topMonAnList.get(0).tenMonAn;
            String count = topMonAnList.isEmpty() ? "" : topMonAnList.get(0).soLuongDat + " Lượt gọi";
            topRow.add(createStatCard("Món ăn bán chạy nhất", best, count, "", null));
            mainContent.add(topRow, BorderLayout.NORTH);
            // --- ROW 2: TOP 5 & PHÂN TÍCH ---
            JPanel bottomRow = new JPanel(new GridLayout(1, 2, 20, 20));
            bottomRow.setOpaque(false);
            // Trái: Top 5
            bottomRow.add(createTop5Panel());
            // Phải: Bảng phân tích
            JPanel rightCol = new JPanel();
            rightCol.setLayout(new BoxLayout(rightCol, BoxLayout.Y_AXIS));
            rightCol.setOpaque(false);
            rightCol.add(createAnalysisPanelTheoNgay(localDate), BorderLayout.CENTER);
            bottomRow.add(rightCol);
            mainContent.add(bottomRow, BorderLayout.CENTER);
        }
        mainContent.revalidate();
        mainContent.repaint();
    }

    // Tạo bảng phân tích cho ngày
    private RoundedPanel createAnalysisPanelTheoNgay(LocalDate date) {
        RoundedPanel panel = new RoundedPanel(20);
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout(10, 15));
        panel.setBorder(new EmptyBorder(20, 25, 20, 25));

        double tong = thongKeCtr.TinhTongDoanhThuNgay(date);
        double tbH = thongKeCtr.TinhDoanhSoTrungBinhHoaDonTheoNgay(date);

        String[] cols = { "Chỉ số", "Số liệu ngày " + date, "Ghi chú" };
        Object[][] data = {
            { "Tổng doanh số ngày", currencyFormat.format(tong), "Dữ liệu thực tế" },
            { "Giá trị trung bình / hóa đơn", currencyFormat.format(tbH), "Dựa trên tổng hóa đơn" }
        };

        JTable table = new JTable(new DefaultTableModel(data, cols));
        table.setRowHeight(40);
        table.getColumnModel().getColumn(0).setCellRenderer(new CustomTableCellRenderer());
        table.getColumnModel().getColumn(1).setCellRenderer(new CustomTableCellRenderer());
        table.getColumnModel().getColumn(2).setCellRenderer(new CustomTableCellRenderer());
        panel.add(new JScrollPane(table) {{ setBorder(null); getViewport().setBackground(Color.WHITE); }}, BorderLayout.CENTER);
        return panel;
    }

    // --- CÁC HÀM TẠO PANEL (GIỮ NGUYÊN CỦA BẠN) ---

    private RoundedPanel createStatCard(String title, String value, String change, String subtitle, Color changeColor) {
        RoundedPanel panel = new RoundedPanel(20);
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(COLOR_SECONDARY_TEXT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(COLOR_PRIMARY_TEXT);

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(valueLabel);

        if (!change.isEmpty() || !subtitle.isEmpty()) {
            panel.add(Box.createVerticalStrut(10));
            JPanel sub = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            sub.setOpaque(false);
            if (!change.isEmpty()) {
                JLabel c = new JLabel(change);
                c.setFont(new Font("Segoe UI", Font.BOLD, 14));
                c.setForeground(change.startsWith("▲") ? COLOR_GREEN_GROWTH : (change.startsWith("▼") ? Color.RED : COLOR_SECONDARY_TEXT));
                sub.add(c);
            }
            if (!subtitle.isEmpty()) {
                JLabel s = new JLabel(subtitle);
                s.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                s.setForeground(COLOR_SECONDARY_TEXT);
                sub.add(s);
            }
            panel.add(sub);
        }
        return panel;
    }

    private RoundedPanel createTop5Panel() {
        RoundedPanel panel = new RoundedPanel(20);
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout(10, 15));
        panel.setBorder(new EmptyBorder(20, 25, 20, 25));

        String title;
        if (rbTheoThang.isSelected()) {
            title = "Top 5 món ăn được gọi nhiều nhất trong tháng";
        } else {
            title = "Top 5 món ăn được gọi nhiều nhất trong ngày";
        }
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        if (topMonAnList.isEmpty()) {
            JLabel noDataLabel = new JLabel("Chưa có dữ liệu món ăn cho thời gian này");
            noDataLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            noDataLabel.setForeground(COLOR_SECONDARY_TEXT);
            listPanel.add(noDataLabel);
        } else {
            for (TopMonAn item : topMonAnList) {
                double pct = (tongLuotGoiTopN > 0) ? ((double) item.soLuongDat / tongLuotGoiTopN * 100) : 0;
                listPanel.add(createProgressBarItem(item.tenMonAn, item.soLuongDat, pct, maxLuotGoi));
                listPanel.add(Box.createVerticalStrut(15));
            }
        }
        panel.add(listPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createProgressBarItem(String name, int value, double percentage, int maxValue) {
        JPanel itemPanel = new JPanel(new BorderLayout(10, 0));
        itemPanel.setOpaque(false);
        JLabel nameLabel = new JLabel(name);
        nameLabel.setPreferredSize(new Dimension(80, 30));
        JProgressBar bar = new JProgressBar(0, maxValue); 
        bar.setValue(value);
        bar.setForeground(BAR_COLOR);
        bar.setPreferredSize(new Dimension(100, 8));
        JLabel pLabel = new JLabel(value + " - " + String.format("%.1f", percentage) + "%");
        itemPanel.add(nameLabel, BorderLayout.WEST);
        itemPanel.add(bar, BorderLayout.CENTER);
        itemPanel.add(pLabel, BorderLayout.EAST);
        return itemPanel;
    }

    private RoundedPanel createGraphPanel(int nam) {
        RoundedPanel panel = new RoundedPanel(20);
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout(10, 15));
        panel.setBorder(new EmptyBorder(20, 25, 20, 25));
        JLabel title = new JLabel("Biểu đồ Cột Doanh Thu theo tháng (Năm " + nam + ")");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(title, BorderLayout.NORTH);
        
        BarChartPanel chart = new BarChartPanel(nam);
        chart.setPreferredSize(new Dimension(0, 250)); // Đảm bảo biểu đồ có chiều cao
        panel.add(chart, BorderLayout.CENTER); 
        return panel;
    }

    private RoundedPanel createAnalysisPanel(int nam) {
        RoundedPanel panel = new RoundedPanel(20);
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout(10, 15));
        panel.setBorder(new EmptyBorder(20, 25, 20, 25));

        double tong = thongKeCtr.TinhTongDoanhThuNam(nam);
        double tbN = thongKeCtr.TinhDoanhSoTrungBinhNgay(nam);
        double tbH = thongKeCtr.TinhDoanhSoTrungBinhHoaDon(nam);

        String[] cols = { "Chỉ số", "Số liệu cả năm " + nam, "Ghi chú" };
        Object[][] data = {
            { "Tổng doanh số năm", currencyFormat.format(tong), "Dữ liệu thực tế" },
            { "Doanh số trung bình / ngày", currencyFormat.format(tbN), "Tính trên 365/366 ngày" },
            { "Giá trị trung bình / hóa đơn", currencyFormat.format(tbH), "Dựa trên tổng hóa đơn" }
        };

        JTable table = new JTable(new DefaultTableModel(data, cols));
        table.setRowHeight(40);
        table.getColumnModel().getColumn(0).setCellRenderer(new CustomTableCellRenderer());
        table.getColumnModel().getColumn(1).setCellRenderer(new CustomTableCellRenderer());
        table.getColumnModel().getColumn(2).setCellRenderer(new CustomTableCellRenderer());
        panel.add(new JScrollPane(table) {{ setBorder(null); getViewport().setBackground(Color.WHITE); }}, BorderLayout.CENTER);
        return panel;
    }

    private JButton createStyledButton(String text, Color color, int fontSize) {
        RoundedButton button = new RoundedButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        button.setBackground(color);
        return button;
    }

    // --- LỚP HỖ TRỢ ĐỒ HỌA (GIỮ NGUYÊN CODE CỦA BẠN) ---

    class BarChartPanel extends JPanel {
        private final List<ThongKe_DAO.ThongKeThang> dataPoints;
        private final int PADDING = 40;
        private double maxValue = 0;
        private final int year;
        private ThongKe_DAO.ThongKeThang hoveredBar = null; 

        public BarChartPanel(int year) {
            setOpaque(false);
            this.year = year;
            this.dataPoints = ThongKe_Ctr.getInstance().getDoanhThuTheoThang(year);
            for (ThongKe_DAO.ThongKeThang dt : dataPoints) if (dt.tongDoanhThu > maxValue) maxValue = dt.tongDoanhThu;
            maxValue = (maxValue > 0) ? Math.ceil(maxValue / 10000000.0) * 10000000 : 100000000;
            
            addMouseMotionListener(new MouseAdapter() {
                @Override public void mouseMoved(MouseEvent e) {
                    ThongKe_DAO.ThongKeThang last = hoveredBar;
                    hoveredBar = findBarAt(e.getPoint());
                    if (last != hoveredBar) repaint();
                }
            });
        }

        private ThongKe_DAO.ThongKeThang findBarAt(Point p) {
            int graphWidth = getWidth() - 2 * PADDING;
            if (graphWidth <= 0 || dataPoints.isEmpty()) return null;
            int barWidth = (graphWidth / dataPoints.size()) - 10;
            int xStart = PADDING + (graphWidth - (barWidth + 10) * dataPoints.size()) / 2;
            for (int i = 0; i < dataPoints.size(); i++) {
                int barX = xStart + i * (barWidth + 10);
                if (p.x >= barX && p.x <= barX + barWidth) return dataPoints.get(i);
            }
            return null;
        }

        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int h = getHeight(), w = getWidth(), gH = h - 2 * PADDING, gW = w - 2 * PADDING;
            if (gH <= 0 || gW <= 0) return;

            g2.setColor(new Color(220, 220, 220));
            for (int i = 0; i <= 5; i++) {
                int y = h - PADDING - i * gH / 5;
                g2.drawLine(PADDING, y, w - PADDING, y);
                g2.drawString(String.format("%,.0f", (i * maxValue / 5) / 1000000) + "tr", PADDING - 35, y + 5);
            }
            int barWidth = (gW / dataPoints.size()) - 10;
            int xStart = PADDING + (gW - (barWidth + 10) * dataPoints.size()) / 2;
            for (int i = 0; i < dataPoints.size(); i++) {
                ThongKe_DAO.ThongKeThang dt = dataPoints.get(i);
                int barX = xStart + i * (barWidth + 10);
                int barH = (int) ((dt.tongDoanhThu / maxValue) * gH);
                int barY = h - PADDING - barH;
                g2.setColor(dt == hoveredBar ? BAR_COLOR.brighter() : BAR_COLOR);
                g2.fillRect(barX, barY, barWidth, barH);
                g2.setColor(COLOR_SECONDARY_TEXT);
                g2.drawString(String.valueOf(dt.thang), barX + barWidth/4, h - PADDING + 15);
            }
        }
    }

    class RoundedPanel extends JPanel {
        private int cornerRadius;
        public RoundedPanel(int radius) { this.cornerRadius = radius; setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D graphics = (Graphics2D) g;
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setColor(getBackground());
            graphics.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
        }
    }

    class RoundedButton extends JButton {
        public RoundedButton(String text) { super(text); setForeground(Color.WHITE); setContentAreaFilled(false); setFocusPainted(false); setBorderPainted(false); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isPressed() ? getBackground().darker() : getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    class CustomTableCellRenderer extends JLabel implements TableCellRenderer {
        public CustomTableCellRenderer() { setOpaque(true); setFont(new Font("Segoe UI", Font.BOLD, 14)); setBorder(new EmptyBorder(0, 10, 0, 10)); }
        @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            if (value == null) return this;
            setText(value.toString());
            setHorizontalAlignment(col == 0 ? SwingConstants.LEFT : SwingConstants.RIGHT);
            if (col == 2 && value.toString().contains("▲")) setForeground(COLOR_GREEN_GROWTH);
            else if (col == 2 && value.toString().contains("▼")) setForeground(Color.RED);
            else setForeground(COLOR_PRIMARY_TEXT);
            setBackground(Color.WHITE);
            return this;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ThongKe_GUI().setVisible(true));
    }
}
