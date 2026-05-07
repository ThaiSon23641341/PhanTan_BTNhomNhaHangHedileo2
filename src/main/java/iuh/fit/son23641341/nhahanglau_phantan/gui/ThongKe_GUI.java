package iuh.fit.son23641341.nhahanglau_phantan.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import iuh.fit.son23641341.nhahanglau_phantan.control.User_Ctr;
import iuh.fit.son23641341.nhahanglau_phantan.control.ThongKe_Ctr;
import iuh.fit.son23641341.nhahanglau_phantan.control.ThongKe_Ctr.ThongKeCardData;
import iuh.fit.son23641341.nhahanglau_phantan.dao.ThongKe_DAO.ThongKeThang;
import iuh.fit.son23641341.nhahanglau_phantan.dao.ThongKe_DAO.TopMonAn;

public class ThongKe_GUI extends JFrame {

    private final ThongKe_Ctr thongKeCtr = ThongKe_Ctr.getInstance();
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    // Màu sắc hệ thống Hẻ Di Leo
    private final Color COLOR_BACKGROUND = new Color(245, 246, 250);
    private final Color COLOR_HEADER = new Color(0xDC4332);
    private final Color COLOR_PRIMARY_TEXT = new Color(44, 62, 80);
    private final Color COLOR_SECONDARY_TEXT = new Color(127, 140, 141);
    private final Color COLOR_GREEN_GROWTH = new Color(39, 174, 96);
    private final Color BAR_COLOR = Color.decode("#7C1211");

    private JComboBox<Integer> cbThang, cbNam;
    private JRadioButton rbTheoThang, rbTheoNgay;
    private com.toedter.calendar.JDateChooser dateChooser;
    private JPanel mainContent;
    private JLabel lblT, lblN;

    private List<TopMonAn> topMonAnList = new ArrayList<>();
    private int maxLuotGoi = 1;

    public ThongKe_GUI() {
        if (!User_Ctr.getInstance().isDangNhap()) {
            dispose();
            return;
        }

        setTitle("Hệ thống Thống kê - Nhà hàng Hẻ Di Leo");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_BACKGROUND);

        // Sidebar
        SideBar_GUI sidebar = new SideBar_GUI();
        sidebar.setMauNutKhiChon("Thống Kê");
        add(sidebar, BorderLayout.WEST);

        // Content Wrapper
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.add(createHeader(), BorderLayout.NORTH);

        mainContent = new JPanel(new BorderLayout(20, 20));
        mainContent.setOpaque(false);
        mainContent.setBorder(new EmptyBorder(25, 35, 25, 35));

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        contentWrapper.add(scrollPane, BorderLayout.CENTER);

        add(contentWrapper, BorderLayout.CENTER);
        updateData(); // Load dữ liệu lần đầu
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_HEADER);
        header.setPreferredSize(new Dimension(0, 100));
        header.setBorder(new EmptyBorder(10, 30, 10, 30));

        JLabel title = new JLabel("Thống Kê Doanh Thu");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        filterPanel.setOpaque(false);

        rbTheoThang = new JRadioButton("Theo Tháng", true);
        rbTheoNgay = new JRadioButton("Theo Ngày");
        styleRadioButton(rbTheoThang);
        styleRadioButton(rbTheoNgay);

        ButtonGroup group = new ButtonGroup();
        group.add(rbTheoThang);
        group.add(rbTheoNgay);

        lblT = new JLabel("Tháng:");
        lblT.setForeground(Color.WHITE);
        cbThang = new JComboBox<>();
        for (int i = 1; i <= 12; i++) cbThang.addItem(i);
        cbThang.setSelectedItem(LocalDate.now().getMonthValue());

        lblN = new JLabel("Năm:");
        lblN.setForeground(Color.WHITE);
        cbNam = new JComboBox<>();
        int currentYear = LocalDate.now().getYear();
        for (int i = currentYear - 5; i <= currentYear; i++) cbNam.addItem(i);
        cbNam.setSelectedItem(currentYear);

        dateChooser = new com.toedter.calendar.JDateChooser();
        dateChooser.setDate(java.sql.Date.valueOf(LocalDate.now()));
        dateChooser.setPreferredSize(new Dimension(180, 40));
        dateChooser.setVisible(false);

        styleComboBox(cbThang);
        styleComboBox(cbNam);

        // Events
        rbTheoThang.addActionListener(e -> toggleFilterMode(true));
        rbTheoNgay.addActionListener(e -> toggleFilterMode(false));
        cbThang.addActionListener(e -> updateData());
        cbNam.addActionListener(e -> updateData());
        dateChooser.addPropertyChangeListener("date", evt -> {
            if ("date".equals(evt.getPropertyName())) updateData();
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

    private void toggleFilterMode(boolean isMonth) {
        lblT.setVisible(isMonth);
        cbThang.setVisible(isMonth);
        lblN.setVisible(isMonth);
        cbNam.setVisible(isMonth);
        dateChooser.setVisible(!isMonth);
        updateData();
    }

    private void updateData() {
        mainContent.removeAll();
        try {
            ThongKeCardData cardData;
            String timeTitle;

            if (rbTheoThang.isSelected()) {
                int thang = (int) cbThang.getSelectedItem();
                int nam = (int) cbNam.getSelectedItem();

                // Lấy dữ liệu từ Controller
                cardData = thongKeCtr.getDuLieuChoTheThongKe(nam, thang);
                topMonAnList = thongKeCtr.getTopMonAn(thang, nam, 5);
                timeTitle = "Tháng " + thang + "/" + nam;

                renderLayoutThang(cardData, timeTitle, nam);
            } else {
                java.util.Date d = dateChooser.getDate();
                if (d == null) return;
                LocalDate date = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                cardData = thongKeCtr.getDuLieuChoTheThongKeTheoNgay(date);
                topMonAnList = thongKeCtr.getTopMonAnTheoNgay(date, 5);
                timeTitle = "Ngày " + date.getDayOfMonth() + "/" + date.getMonthValue();

                renderLayoutNgay(cardData, timeTitle, date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mainContent.revalidate();
        mainContent.repaint();
    }

    private void renderLayoutThang(ThongKeCardData data, String title, int nam) {
        // ROW 1: Cards
        mainContent.add(createCardRow(data, title), BorderLayout.NORTH);

        // ROW 2: Body
        JPanel body = new JPanel(new GridLayout(1, 2, 25, 0));
        body.setOpaque(false);
        body.add(createTop5Panel());

        JPanel rightCol = new JPanel();
        rightCol.setLayout(new BoxLayout(rightCol, BoxLayout.Y_AXIS));
        rightCol.setOpaque(false);
        rightCol.add(createGraphPanel(nam));
        rightCol.add(Box.createVerticalStrut(20));
        rightCol.add(createAnalysisTable("Phân tích năm " + nam, new Object[][]{
                {"Tổng doanh thu năm", currencyFormat.format(thongKeCtr.TinhTongDoanhThuNam(nam))},
                {"Trung bình ngày", currencyFormat.format(thongKeCtr.TinhDoanhSoTrungBinhNgay(nam))},
                {"Trung bình/Hóa đơn", currencyFormat.format(thongKeCtr.TinhDoanhSoTrungBinhHoaDon(nam))}
        }));
        body.add(rightCol);

        mainContent.add(body, BorderLayout.CENTER);
    }

    private void renderLayoutNgay(ThongKeCardData data, String title, LocalDate date) {
        mainContent.add(createCardRow(data, title), BorderLayout.NORTH);

        JPanel body = new JPanel(new GridLayout(1, 2, 25, 0));
        body.setOpaque(false);
        body.add(createTop5Panel());
        body.add(createAnalysisTable("Phân tích " + title, new Object[][]{
                {"Tổng doanh số", currencyFormat.format(thongKeCtr.TinhTongDoanhThuNgay(date))},
                {"Trung bình/Hóa đơn", currencyFormat.format(thongKeCtr.TinhDoanhSoTrungBinhHoaDonTheoNgay(date))}
        }));
        mainContent.add(body, BorderLayout.CENTER);
    }

    private JPanel createCardRow(ThongKeCardData data, String title) {
        JPanel row = new JPanel(new GridLayout(1, 3, 25, 0));
        row.setOpaque(false);
        if (data == null) data = new ThongKeCardData();

        row.add(createStatCard("Doanh Thu", currencyFormat.format(data.dtHienTai), data.dtChenhLech, "So với kỳ trước"));
        row.add(createStatCard("Tổng Hóa Đơn", String.valueOf(data.hdHienTai), data.hdChenhLech, "So với kỳ trước"));

        String best = (topMonAnList.isEmpty()) ? "N/A" : topMonAnList.get(0).tenMonAn;
        String count = (topMonAnList.isEmpty()) ? "" : topMonAnList.get(0).soLuongDat + " lượt";
        row.add(createStatCard("Bán Chạy Nhất", best, count, title));

        return row;
    }

    private RoundedPanel createStatCard(String title, String val, String change, String sub) {
        RoundedPanel p = new RoundedPanel(20);
        p.setBackground(Color.WHITE);
        p.setLayout(new BorderLayout());
        p.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel lTitle = new JLabel(title);
        lTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lTitle.setForeground(COLOR_SECONDARY_TEXT);

        JLabel lVal = new JLabel(val);
        lVal.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lVal.setForeground(COLOR_PRIMARY_TEXT);

        JPanel foot = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        foot.setOpaque(false);
        if (change != null && !change.isEmpty()) {
            JLabel lChange = new JLabel(change);
            lChange.setForeground(change.contains("▲") ? COLOR_GREEN_GROWTH : Color.RED);
            foot.add(lChange);
        }
        JLabel lSub = new JLabel(sub);
        lSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lSub.setForeground(COLOR_SECONDARY_TEXT);
        foot.add(lSub);

        p.add(lTitle, BorderLayout.NORTH);
        p.add(lVal, BorderLayout.CENTER);
        p.add(foot, BorderLayout.SOUTH);
        return p;
    }

    private RoundedPanel createTop5Panel() {
        RoundedPanel p = new RoundedPanel(20);
        p.setBackground(Color.WHITE);
        p.setLayout(new BorderLayout());
        p.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel title = new JLabel("Top 5 Món Ăn");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setBorder(new EmptyBorder(0, 0, 15, 0));
        p.add(title, BorderLayout.NORTH);

        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);

        maxLuotGoi = (topMonAnList.isEmpty()) ? 1 : topMonAnList.get(0).soLuongDat;
        for (TopMonAn m : topMonAnList) {
            list.add(createProgressItem(m.tenMonAn, m.soLuongDat));
            list.add(Box.createVerticalStrut(15));
        }
        p.add(list, BorderLayout.CENTER);
        return p;
    }

    private JPanel createProgressItem(String name, int val) {
        JPanel p = new JPanel(new BorderLayout(10, 5));
        p.setOpaque(false);
        p.add(new JLabel(name), BorderLayout.NORTH);

        JProgressBar bar = new JProgressBar(0, maxLuotGoi);
        bar.setValue(val);
        bar.setForeground(BAR_COLOR);
        bar.setPreferredSize(new Dimension(100, 10));

        p.add(bar, BorderLayout.CENTER);
        p.add(new JLabel(val + " lượt"), BorderLayout.EAST);
        return p;
    }

    private RoundedPanel createAnalysisTable(String title, Object[][] data) {
        RoundedPanel p = new RoundedPanel(20);
        p.setBackground(Color.WHITE);
        p.setLayout(new BorderLayout());
        p.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel lTitle = new JLabel(title);
        lTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lTitle.setBorder(new EmptyBorder(0, 0, 10, 0));

        JTable table = new JTable(new DefaultTableModel(data, new String[]{"Chỉ số", "Giá trị"}));
        table.setRowHeight(35);
        table.setShowGrid(false);

        p.add(lTitle, BorderLayout.NORTH);
        p.add(table, BorderLayout.CENTER);
        return p;
    }

    private RoundedPanel createGraphPanel(int nam) {
        RoundedPanel p = new RoundedPanel(20);
        p.setBackground(Color.WHITE);
        p.setLayout(new BorderLayout());
        p.setBorder(new EmptyBorder(20, 25, 20, 25));
        p.add(new JLabel("Doanh Thu 12 Tháng (" + nam + ")") {{
            setFont(new Font("Segoe UI", Font.BOLD, 16));
        }}, BorderLayout.NORTH);
        p.add(new BarChartPanel(nam), BorderLayout.CENTER);
        return p;
    }

    // --- Styling Helpers ---
    private void styleComboBox(JComboBox<Integer> cb) {
        cb.setFont(new Font("Segoe UI", Font.BOLD, 16));
        cb.setPreferredSize(new Dimension(90, 40));
    }

    private void styleRadioButton(JRadioButton rb) {
        rb.setFont(new Font("Segoe UI", Font.BOLD, 14));
        rb.setForeground(Color.WHITE);
        rb.setOpaque(false);
    }

    // --- Graphics Classes ---
    class BarChartPanel extends JPanel {
        private final List<ThongKeThang> dataPoints;
        private double maxValue = 0;
        public BarChartPanel(int year) {
            setOpaque(false);
            setPreferredSize(new Dimension(0, 250));
            dataPoints = thongKeCtr.getDoanhThuTheoThang(year);
            for (ThongKeThang d : dataPoints) if (d.tongDoanhThu > maxValue) maxValue = d.tongDoanhThu;
            if (maxValue == 0) maxValue = 1000000;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int pad = 30;
            int h = getHeight() - 2 * pad, w = getWidth() - 2 * pad;
            int barW = (w / 12) - 10;

            for (int i = 0; i < dataPoints.size(); i++) {
                int barH = (int) ((dataPoints.get(i).tongDoanhThu / maxValue) * h);
                int x = pad + i * (barW + 10);
                int y = getHeight() - pad - barH;
                g2.setColor(BAR_COLOR);
                g2.fill(new RoundRectangle2D.Float(x, y, barW, barH, 8, 8));
                g2.setColor(COLOR_SECONDARY_TEXT);
                g2.drawString("T" + (i + 1), x + (barW/4), getHeight() - pad + 15);
            }
        }
    }

    class RoundedPanel extends JPanel {
        private int r;
        public RoundedPanel(int r) { this.r = r; setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), r, r);
        }
    }
}