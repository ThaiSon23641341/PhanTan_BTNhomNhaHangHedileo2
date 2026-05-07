package iuh.fit.son23641341.nhahanglau_phantan.gui;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Component;
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
				// Tìm instance của PhieuDat_GUI thông qua GUIManager
				Component gui = GUIManager.getInstance().getGUI(PhieuDat_GUI.class);
				if (gui instanceof PhieuDat_GUI) {
					((PhieuDat_GUI) gui).handleThoatButton();
				} else {
					// Fallback: quay về màn hình Chọn Bàn
					java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(panel);
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