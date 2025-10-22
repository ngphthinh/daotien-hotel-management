package iuh.fit.se.group1.ui.layout;

import java.awt.Desktop;
import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

public class AboutPanel extends JPanel {

    public AboutPanel() {
        setLayout(new BorderLayout());

        try {
            // Đường dẫn đến file about1.html trong resources/static
            File htmlFile = new File("src/main/resources/static/about.html");

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
