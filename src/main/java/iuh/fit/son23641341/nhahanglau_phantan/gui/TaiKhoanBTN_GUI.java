package iuh.fit.son23641341.nhahanglau_phantan.gui;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class TaiKhoanBTN_GUI extends JButton {
    public TaiKhoanBTN_GUI() {

        

        setText("ADMIN/THU NGÂN");
        setFocusPainted(false);
        setBackground(Color.decode("#ffffff"));
        setForeground(Color.BLACK);
        //Thêm icon
        // setIcon(new ImageIcon(getClass().getResource("project_PTUD/src/imgs/sidebar_dn/taikhoan.png"))); 
        //Bo 4 góc lại va set mau nền
        
        setContentAreaFilled(false);
        setOpaque(true);

        setFont(new java.awt.Font("Montserrat", java.awt.Font.PLAIN, 16));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

 

}
