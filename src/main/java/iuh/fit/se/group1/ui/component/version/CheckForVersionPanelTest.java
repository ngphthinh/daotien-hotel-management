/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iuh.fit.se.group1.ui.component.version;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author Windows
 */
public class CheckForVersionPanelTest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Thông tin phiên bản");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Thêm panel của bạn vào JFrame
            frame.add(new CheckForVersionPanel());

            // Tự động kích thước vừa với nội dung
            frame.pack();

            // Canh giữa màn hình
            frame.setLocationRelativeTo(null);

            frame.setVisible(true);
        });
    }
}
