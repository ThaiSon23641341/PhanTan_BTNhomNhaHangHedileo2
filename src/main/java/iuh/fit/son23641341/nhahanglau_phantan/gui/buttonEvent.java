package iuh.fit.son23641341.nhahanglau_phantan.gui;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class buttonEvent {


    public static void addAdminPanelListener(JPanel panel) {

        Color originalColor = panel.getBackground();
        
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseClicked(MouseEvent e) {
                new ThongTinNhanVien_GUI().setVisible(true);
            }


            @Override
            public void mouseExited(MouseEvent e) {
                // Trả lại màu nền gốc
                panel.setBackground(originalColor); 
            }
        });
    }
    
    
    public static void addThoatPanelListener(JPanel panel) {

		Color originalColor = panel.getBackground();
		
		panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		panel.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// Lấy parent frame (PhieuDat_GUI)
				java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(panel);
				if (window instanceof PhieuDat_GUI) {
					PhieuDat_GUI phieuDatGUI = (PhieuDat_GUI) window;
					// Gọi logic giống nút Hủy: xóa giỏ hàng tạm
					phieuDatGUI.handleThoatButton();
				} else {
					// Fallback: chỉ mở ChonBan_GUI - Sử dụng GUIManager để chuyển mượt mà
					GUIManager.getInstance().switchToGUI(ChonBan_GUI.class, window);
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// Hiệu ứng hover: đổi nền
				panel.setBackground(new Color(240, 240, 240)); 
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// Trả lại màu nền gốc
				panel.setBackground(originalColor); 
			}
		});
	}
}