/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iuh.fit.se.group1.ui.layout;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Windows
 */
public class Regulations extends JPanel{
    public Regulations() {
        setLayout(new BorderLayout());

        try {
            // Đường dẫn đến file about1.html trong resources/static
            File htmlFile = new File("src/main/resources/static/regulations.html");

            if (htmlFile.exists()) {
                Desktop.getDesktop().browse(htmlFile.toURI());
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Không tìm thấy file: " + htmlFile.getAbsolutePath(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Không thể mở trang giới thiệu trong trình duyệt!", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
