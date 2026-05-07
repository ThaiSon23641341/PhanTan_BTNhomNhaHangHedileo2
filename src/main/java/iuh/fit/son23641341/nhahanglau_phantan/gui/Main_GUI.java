package iuh.fit.son23641341.nhahanglau_phantan.gui;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Cửa sổ chính chứa Sidebar cố định và vùng nội dung thay đổi linh hoạt.
 */
public class Main_GUI extends JFrame {
    private SideBar_GUI sidebar;
    private JPanel pnlContent;
    private CardLayout cardLayout;
    private Map<String, JPanel> cards;

    public Main_GUI() {
        setTitle("HÈ DÌ LEO - Hệ Thống Quản Lý Nhà Hàng");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        sidebar = new SideBar_GUI();
        sidebar.setMainGui(this); // Quan trọng
        add(sidebar, BorderLayout.WEST);

        // Đăng ký Main_GUI với GUIManager
        GUIManager.getInstance().setMainGUI(this);

        cardLayout = new CardLayout();
        pnlContent = new JPanel(cardLayout);
        add(pnlContent, BorderLayout.CENTER);

        cards = new HashMap<>();
        
        // Hiển thị Trang Chủ mặc định
        showCard("Trang Chủ", TrangChu_GUI.class);
    }

    public Component showCard(String name, Class<? extends Component> guiClass) {
        if (!cards.containsKey(name)) {
            try {
                System.out.println("Đang nạp Card: " + name);
                Component comp = guiClass.getDeclaredConstructor().newInstance();
                
                JPanel content;
                if (comp instanceof JFrame) {
                    content = extractContentFromFrame((JFrame) comp);
                } else if (comp instanceof JPanel) {
                    content = (JPanel) comp;
                } else {
                    content = new JPanel(new BorderLayout());
                    content.add(comp, BorderLayout.CENTER);
                }
                
                cards.put(name, content);
                pnlContent.add(content, name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cardLayout.show(pnlContent, name);
        
        // Đồng bộ Sidebar (nhấn nút tương ứng)
        String sidebarButtonName = name;
        if (name.equals("Phiếu Đặt") || name.equals("Chọn Món")) {
            sidebarButtonName = "Đặt Bàn";
        }
        sidebar.setMauNutKhiChon(sidebarButtonName);

        return cards.get(name);
    }

    private JPanel extractContentFromFrame(JFrame frame) {
        frame.setVisible(false);
        // Hầu hết các GUI của dự án đều có cấu trúc:
        // JFrame -> ContentPane -> pnlMain (BorderLayout) -> ContentWrapper (CENTER)
        
        Container contentPane = frame.getContentPane();
        
        // Tìm pnlMain (panel chứa BorderLayout có CENTER component)
        for (Component c : contentPane.getComponents()) {
            if (c instanceof JPanel) {
                JPanel p = (JPanel) c;
                if (p.getLayout() instanceof BorderLayout) {
                    BorderLayout bl = (BorderLayout) p.getLayout();
                    Component centerComp = bl.getLayoutComponent(BorderLayout.CENTER);
                    
                    if (centerComp instanceof JPanel) {
                        // Ẩn sidebar bên trong pnlMain nếu nó tồn tại
                        Component westComp = bl.getLayoutComponent(BorderLayout.WEST);
                        if (westComp instanceof SideBar_GUI) {
                            westComp.setVisible(false);
                        }
                        return (JPanel) centerComp;
                    }
                }
            }
        }
        
        // Nếu không tìm thấy theo cấu trúc chuẩn, fallback về ContentPane
        JPanel fallback = new JPanel(new BorderLayout());
        fallback.add(frame.getContentPane());
        return fallback;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main_GUI().setVisible(true));
    }
}
