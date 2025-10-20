package iuh.fit.se.group1;

import org.imgscalr.Scalr;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageSwingDemo extends JFrame {

    public ImageSwingDemo() {
        setTitle("Hiển thị ảnh mượt trong Swing");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);

        try {
            // Đọc ảnh gốc
            BufferedImage original = ImageIO.read(new File("src/main/resources/images/hihi.png"));

            // Resize mượt với Imgscalr (Scalr.Method.QUALITY = cao nhất)
            BufferedImage scaled = Scalr.resize(original, Scalr.Method.QUALITY, 64, 64);

            // Đưa ảnh vào JLabel
            JLabel label = new JLabel(new ImageIcon(scaled));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            add(label);

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thể đọc ảnh!");
        }

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ImageSwingDemo::new);
    }
}
