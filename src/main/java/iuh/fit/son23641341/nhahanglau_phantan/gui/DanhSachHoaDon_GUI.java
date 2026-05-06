package iuh.fit.son23641341.nhahanglau_phantan.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.util.Vector;

import iuh.fit.son23641341.nhahanglau_phantan.control.User_Ctr;

public class DanhSachHoaDon_GUI extends JFrame {

    private JTable invoiceTable;
    private JFormattedTextField dateField;
    private JButton detailButton;

    public DanhSachHoaDon_GUI() {
        
        // --- 1. Basic JFrame Setup (Mini-Box Size) ---
        setTitle("Danh Sách Hóa Đơn");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 650); // Kích thước cố định của Mini-Box
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout());

        // Define colors
        Color mainBackgroundColor = new Color(50, 50, 50);
        Color contentBackgroundColor = new Color(240, 240, 240);
        Color headerBgColor = new Color(28, 40, 51);
        Color titleOrange = new Color(255, 102, 0);

        // --- 2. Header Panel (Top Title) ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(headerBgColor);
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel("Danh sách hóa đơn");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(titleOrange);
        headerPanel.add(titleLabel);
        
        add(headerPanel, BorderLayout.NORTH);


        // --- 3. Controls Panel (Date Picker and Button) ---
        JPanel controlsPanel = new JPanel();
        controlsPanel.setBackground(mainBackgroundColor);
        
        // Increased top padding for spacing
        controlsPanel.setBorder(new EmptyBorder(15, 15, 10, 15));
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.X_AXIS)); 

        // 3a. Date Input Field (10/2025)
        JPanel dateBoxPanel = new JPanel(new BorderLayout()); 
        dateBoxPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            new EmptyBorder(3, 5, 3, 5) 
        ));
        dateBoxPanel.setBackground(Color.WHITE);

        try {
            MaskFormatter monthYearMask = new MaskFormatter("##/####");
            monthYearMask.setPlaceholderCharacter('_');
            dateField = new JFormattedTextField(monthYearMask);
            dateField.setText("10/2025");
            dateField.setColumns(4); 
            dateField.setFont(new Font("Arial", Font.PLAIN, 16));
            dateField.setBorder(null);

        } catch (ParseException e) {
            dateField = new JFormattedTextField("10/2025");
            e.printStackTrace();
        }

        dateBoxPanel.add(dateField, BorderLayout.CENTER);
        
        // Thiết lập kích thước mong muốn (Preferred Size) cho dateBoxPanel.
        int newWidth = 90; 
        Dimension dateBoxSize = new Dimension(newWidth, dateField.getPreferredSize().height + 6); 
        dateBoxPanel.setPreferredSize(dateBoxSize);
        dateBoxPanel.setMaximumSize(dateBoxSize); 
        
        // Add date box to controls panel
        controlsPanel.add(dateBoxPanel);
        
        // 3b. "Xem Chi tiết" Button
        detailButton = new JButton("Xem Chi tiết");
        detailButton.setFont(new Font("Arial", Font.BOLD, 14));
        detailButton.setBackground(new Color(60, 179, 113)); // Medium Sea Green
        detailButton.setForeground(Color.WHITE);
        detailButton.setFocusPainted(false);
        detailButton.setBorderPainted(false);
        detailButton.setPreferredSize(new Dimension(150, 40));
        
        // Add glue to push button to the right
        controlsPanel.add(Box.createHorizontalGlue());
        controlsPanel.add(detailButton);
        
        // Thêm ActionListener cho nút Detail (Placeholder)
        detailButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Thực hiện chức năng xem chi tiết hóa đơn.");
        });


        // --- 4. Main Content Panel (Table Area) ---
        JPanel contentWrapperPanel = new JPanel(new BorderLayout());
        contentWrapperPanel.setBackground(mainBackgroundColor);
        contentWrapperPanel.setBorder(new EmptyBorder(0, 15, 15, 15));

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(contentBackgroundColor);
        
        // 4a. Table Data
        String[] columnNames = {"Mã Hóa Đơn", "Ngày Tạo"};
        Object[][] data = {
            {"012211", "20/11/2020"},
            {"012211", "20/11/2020"},
            {"012211", "20/11/2020"},
            {"012211", "20/11/2020"},
            {"012211", "20/11/2020"},
            {"012211", "20/11/2020"},
            {"012211", "20/11/2020"},
            {"012211", "20/11/2020"}
        };
        
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        invoiceTable = new JTable(model);
        invoiceTable.setFont(new Font("Arial", Font.PLAIN, 16));
        invoiceTable.setRowHeight(30); 
        invoiceTable.setBackground(contentBackgroundColor);
        invoiceTable.setShowGrid(false); 
        invoiceTable.setIntercellSpacing(new Dimension(0, 5)); 

        // Table Header Styling
        JTableHeader tableHeader = invoiceTable.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 18));
        tableHeader.setBackground(contentBackgroundColor);
        tableHeader.setForeground(Color.BLACK);
        tableHeader.setReorderingAllowed(false);
        tableHeader.setResizingAllowed(false);
        tableHeader.setBorder(new EmptyBorder(10, 0, 10, 0)); 
        
        
        // Custom Cell Renderer for Centering Data
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (c instanceof JLabel) {
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                    ((JLabel) c).setBorder(new EmptyBorder(0, 0, 0, 0));
                }
                return c;
            }
        };

        // Apply the custom renderer to both columns (Invoice Code and Creation Date)
        for (int i = 0; i < invoiceTable.getColumnCount(); i++) {
            invoiceTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Center align the table header text
        ((DefaultTableCellRenderer)invoiceTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);


        // Scroll Pane
        JScrollPane scrollPane = new JScrollPane(invoiceTable);
        scrollPane.setBorder(null); 
        scrollPane.setBackground(contentBackgroundColor);

        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        contentWrapperPanel.add(contentPanel, BorderLayout.CENTER);


        // --- Add Controls and Table Area to Frame Center ---
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(mainBackgroundColor);
        mainContentPanel.add(controlsPanel, BorderLayout.NORTH);
        mainContentPanel.add(contentWrapperPanel, BorderLayout.CENTER);

        add(mainContentPanel, BorderLayout.CENTER);
    }
    
    // Main method to run the GUI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DanhSachHoaDon_GUI().setVisible(true));
    }
}
