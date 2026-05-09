package iuh.fit.son23641341.nhahanglau_phantan.gui;

import java.awt.Component;
import java.awt.Window;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Quản lý việc chuyển đổi giữa các GUI để tránh hiệu ứng "tắt bật" không mượt mà
 * Sử dụng cache để lưu các GUI instances và hỗ trợ Single-Frame Architecture
 */
public class GUIManager {
    private static GUIManager instance;
    private Map<Class<? extends Component>, Component> guiCache;
    private Component currentVisibleGUI; 
    private Main_GUI mainGUI; 
    
    private GUIManager() {
        guiCache = new HashMap<>();
        currentVisibleGUI = null;
    }
    
    public static GUIManager getInstance() {
        if (instance == null) {
            instance = new GUIManager();
        }
        return instance;
    }
    
    public void setMainGUI(Main_GUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    /**
     * Mở Main_GUI và đóng màn hình đăng nhập
     */
    public void openMainGUI(Window loginScreen) {
        if (mainGUI == null) {
            mainGUI = new Main_GUI();
        }
        mainGUI.setVisible(true);
        if (loginScreen != null) {
            loginScreen.dispose();
        }
    }

    /**
     * Chuyển sang GUI mới, hỗ trợ cả JPanel (CardLayout) và JFrame (Window độc lập)
     * @param guiClass Class của GUI cần hiển thị
     * @param currentComponent Component đang gọi (để tìm Window cha và đóng/ẩn nếu cần)
     * @return GUI instance đã được hiển thị
     */
    public Component switchToGUI(Class<? extends Component> guiClass, Component currentComponent) {
        if (guiClass == null) return null;
        
        System.out.println(">>> GUIManager: Yêu cầu chuyển sang " + guiClass.getSimpleName());

        // 1. Tìm Window cha của component hiện tại
        Window parentWindow = (currentComponent instanceof Window) ? (Window) currentComponent : 
                             (currentComponent != null ? SwingUtilities.getWindowAncestor(currentComponent) : null);

        // 2. Kiểm tra xem class này có phải là một Card trong Main_GUI không
        String cardName = getCardName(guiClass);
        System.out.println(">>> GUIManager: CardName xác định = " + cardName + " | mainGUI status = " + (mainGUI != null ? "READY" : "NULL"));

        if (mainGUI != null && cardName != null) {
            System.out.println(">>> GUIManager: Thực hiện chuyển CardLayout...");
            Component card = mainGUI.showCard(cardName, guiClass);
            
            if (card == null) {
                System.err.println(">>> GUIManager ERROR: showCard trả về NULL cho " + cardName);
                return null;
            }

            // Tự động làm mới dữ liệu nếu là các màn hình cần cập nhật số liệu
            try {
                if (card instanceof ChonBan_GUI) {
                    ((ChonBan_GUI) card).refreshData();
                } else if (card instanceof TrangChu_GUI) {
                    ((TrangChu_GUI) card).refreshData();
                } else if (card instanceof ThongKe_GUI) {
                    ((ThongKe_GUI) card).updateData();
                } else if (card instanceof TrangChuQL_GUI) {
                    ((TrangChuQL_GUI) card).refreshData();
                } else if (card instanceof QuanLyKhachHang_GUI) {
                    ((QuanLyKhachHang_GUI) card).refreshData();
                }
            } catch (Exception e) {
                System.err.println(">>> GUIManager: Lỗi khi refresh dữ liệu cho " + cardName);
                e.printStackTrace();
            }

            // Nếu đang ở trong một window khác (JFrame cũ), thì đóng nó đi
            if (parentWindow != null && parentWindow != mainGUI) {
                System.out.println(">>> GUIManager: Đóng cửa sổ cũ " + parentWindow.getClass().getSimpleName());
                parentWindow.dispose();
            }
            return card;
        }

        // 3. Nếu không phải Card hoặc không có Main_GUI, mở cửa sổ độc lập (JFrame)
        System.out.println(">>> GUIManager: Chế độ Window độc lập cho " + guiClass.getSimpleName());
        Component gui = getGUI(guiClass);
        if (gui == null) return null;

        if (gui instanceof Window) {
            if (parentWindow != null && parentWindow != gui) {
                parentWindow.dispose();
            }
            ((Window) gui).setVisible(true);
            ((Window) gui).toFront();
        }
        
        gui.requestFocus();
        currentVisibleGUI = gui;
        return gui;
    }

    /**
     * Lấy hoặc tạo mới instance của một GUI class
     */
    public Component getGUI(Class<? extends Component> guiClass) {
        Component gui = guiCache.get(guiClass);
        
        if (gui == null || !gui.isDisplayable()) {
            try {
                System.out.println("Khởi tạo GUI mới: " + guiClass.getSimpleName());
                gui = guiClass.getDeclaredConstructor().newInstance();
                
                if (gui instanceof JFrame) {
                    JFrame frame = (JFrame) gui;
                    // Mặc định cho phép đóng nhưng có thể điều chỉnh sau
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                }
                
                guiCache.put(guiClass, gui);
            } catch (Exception e) {
                System.err.println("Lỗi khi tạo GUI: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
        return gui;
    }

    public String getCardName(Class<? extends Component> guiClass) {
        if (guiClass == null) return null;
        String className = guiClass.getName();
        
        if (className.endsWith("TrangChu_GUI")) return "Trang Chủ";
        if (className.endsWith("TrangChuQL_GUI")) return "Tổng Quan";
        if (className.endsWith("ChonBan_GUI")) return "Đặt Bàn";
        if (className.endsWith("TimKiem_GUI")) return "Tìm Kiếm";
        if (className.endsWith("DanhSachMonAnQL_GUI")) return "Quản Lý Món";
        if (className.endsWith("QuanLyKhachHang_GUI")) return "Khách Hàng";
        if (className.endsWith("ThongKe_GUI")) return "Thống Kê";
        if (className.endsWith("QuanLiDuLieu_GUI")) return "Quản Lý Dữ Liệu";
        if (className.endsWith("TroGiup_GUI")) return "Trợ Giúp";
        if (className.endsWith("PhieuDat_GUI")) return "Phiếu Đặt";
        if (className.endsWith("DanhSachMonAnNV_GUI")) return "Chọn Món";
        
        return null;
    }
    
    public void disposeAll() {
        for (Component gui : guiCache.values()) {
            if (gui instanceof Window) {
                ((Window) gui).dispose();
            }
        }
        if (mainGUI != null) {
            mainGUI.dispose();
            mainGUI = null; // Reset Main_GUI để tạo mới ở lần đăng nhập sau
        }
        guiCache.clear();
        currentVisibleGUI = null;
    }
}
