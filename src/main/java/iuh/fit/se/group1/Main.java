package iuh.fit.se.group1;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or

import iuh.fit.se.group1.ui.component.SeachBar;
import javax.swing.JFrame;
import javax.swing.JPanel;

// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
      // chạy trên luồng EDT
        javax.swing.SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test GUI");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 150);
            frame.setLocationRelativeTo(null);

            // Thêm panel cần test
            frame.add(new SeachBar());

            frame.setVisible(true);
        });
    }
}