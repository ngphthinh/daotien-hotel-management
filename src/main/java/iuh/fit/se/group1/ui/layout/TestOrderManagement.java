/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iuh.fit.se.group1.ui.layout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author Windows
 */
public class TestOrderManagement {
    public static void main(String[] args) {
        
            JFrame frame = new JFrame("Test - Quản lý hóa đơn");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1300, 800); // Kích thước khung
            frame.setLocationRelativeTo(null); // Căn giữa màn hình

            // Thêm JPanel OrderManagement vào frame
            frame.add(new InstructionsForUser());

            frame.setVisible(true);
     
    }
}