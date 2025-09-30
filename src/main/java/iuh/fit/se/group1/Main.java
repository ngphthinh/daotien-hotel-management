package iuh.fit.se.group1;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or

import iuh.fit.se.group1.ui.component.SeachBar;
import iuh.fit.se.group1.ui.layout.HomePage;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        // chạy trên luồng EDT
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test GUI");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800); // tăng size để HomePage hiển thị
            frame.setLocationRelativeTo(null);
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.add(new SeachBar(), BorderLayout.NORTH);
            mainPanel.add(new HomePage(), BorderLayout.CENTER);
            frame.setContentPane(mainPanel);
            frame.setVisible(true);
        });
    }
}