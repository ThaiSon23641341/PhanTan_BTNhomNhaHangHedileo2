package iuh.fit.son23641341.nhahanglau_phantan.gui;

import java.awt.Window;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;

/**
 * Quản lý việc chuyển đổi giữa các GUI để tránh hiệu ứng "tắt bật" không mượt mà
 * Sử dụng cache để lưu các GUI instances và chỉ ẩn/hiển thị thay vì dispose/tạo mới
 */
public class GUIManager {
    private static GUIManager instance;
    private Map<Class<? extends JFrame>, JFrame> guiCache;
    private JFrame currentVisibleGUI; // Track GUI đang hiển thị
    
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
    
    /**
     * Chuyển sang GUI mới, ẩn GUI cũ thay vì dispose
     * @param guiClass Class của GUI cần hiển thị
     * @param currentWindow Cửa sổ hiện tại (sẽ bị ẩn)
     * @return GUI instance đã được hiển thị
     */
    public <T extends JFrame> T switchToGUI(Class<T> guiClass, Window currentWindow) {
        // Kiểm tra xem GUI đã được cache chưa
        @SuppressWarnings("unchecked")
        T gui = (T) guiCache.get(guiClass);
        
        // Kiểm tra xem GUI đang hiển thị có phải là GUI cần chuyển đến không
        if (gui != null && gui.isVisible() && gui.isDisplayable() && gui == currentVisibleGUI) {
            // GUI này đã đang hiển thị rồi, chỉ cần đưa lên front
            gui.toFront();
            gui.requestFocus();
            return gui;
        }
        
        // Nếu GUI chưa được tạo hoặc đã bị dispose, tạo mới
        if (gui == null || !gui.isDisplayable()) {
            try {
                System.out.println("Tạo GUI mới: " + guiClass.getSimpleName());
                gui = guiClass.getDeclaredConstructor().newInstance();
                
                // QUAN TRỌNG: Thiết lập HIDE_ON_CLOSE trước khi add vào cache
                // Đảm bảo GUI không bị dispose khi đóng
                gui.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                
                guiCache.put(guiClass, gui);
                
                // Tạo final reference để dùng trong inner class
                final JFrame finalGui = gui;
                
                // Thêm WindowListener để theo dõi khi GUI bị đóng
                finalGui.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        // Ẩn GUI thay vì dispose để giữ lại trong cache
                        e.getWindow().setVisible(false);
                        if (currentVisibleGUI == finalGui) {
                            currentVisibleGUI = null;
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            // Đảm bảo GUI đã tồn tại cũng có HIDE_ON_CLOSE
            gui.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        }
        
        // Ẩn GUI cũ đang hiển thị và cửa sổ hiện tại (CHỈ ẨN, KHÔNG DISPOSE)
        // Điều này đảm bảo chuyển đổi mượt mà không có hiệu ứng tắt/mở
        if (currentVisibleGUI != null && currentVisibleGUI != gui && 
            currentVisibleGUI.isDisplayable() && currentVisibleGUI.isVisible()) {
            currentVisibleGUI.setVisible(false);
        }
        
        // Ẩn cửa sổ hiện tại nếu nó khác với GUI cần hiển thị (CHỈ ẨN, KHÔNG DISPOSE)
        if (currentWindow != null && currentWindow instanceof JFrame && 
            currentWindow != gui && currentWindow.isDisplayable() && currentWindow.isVisible()) {
            currentWindow.setVisible(false);
        }
        
        gui.setVisible(true);
        gui.toFront();
        gui.requestFocus();
        
        // Cập nhật currentVisibleGUI
        currentVisibleGUI = gui;
        
        System.out.println("Chuyển sang GUI: " + guiClass.getSimpleName() + 
                          " (Total cached: " + guiCache.size() + ")");
        
        return gui;
    }
    
    /**
     * Dispose tất cả GUI đã cache (dùng khi đăng xuất)
     */
    public void disposeAll() {
        for (JFrame gui : guiCache.values()) {
            if (gui != null && gui.isDisplayable()) {
                gui.dispose();
            }
        }
        guiCache.clear();
        currentVisibleGUI = null;
    }
    
    /**
     * Dispose một GUI cụ thể
     */
    public void disposeGUI(Class<? extends JFrame> guiClass) {
        JFrame gui = guiCache.get(guiClass);
        if (gui != null && gui.isDisplayable()) {
            gui.dispose();
            guiCache.remove(guiClass);
            if (currentVisibleGUI == gui) {
                currentVisibleGUI = null;
            }
        }
    }
    
    /**
     * Kiểm tra xem GUI có đang hiển thị không
     */
    public boolean isGUIVisible(Class<? extends JFrame> guiClass) {
        JFrame gui = guiCache.get(guiClass);
        return gui != null && gui.isVisible() && gui == currentVisibleGUI;
    }
    
    /**
     * Lấy GUI đang hiển thị
     */
    public JFrame getCurrentVisibleGUI() {
        return currentVisibleGUI;
    }
}

