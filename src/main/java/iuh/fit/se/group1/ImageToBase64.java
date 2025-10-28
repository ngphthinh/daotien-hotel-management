package iuh.fit.se.group1;

import org.imgscalr.Scalr;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

public class ImageToBase64 {
    public static void main(String[] args) {
        try {
            // Đường dẫn đến file ảnh
            File file = new File("src/main/resources/images/hihi.png");

            // Đọc toàn bộ file ảnh vào mảng byte
            byte[] fileContent = Files.readAllBytes(file.toPath());

            // Mã hóa Base64
            String encodedString = Base64.getEncoder().encodeToString(fileContent);

            System.out.println("Base64: ");
            System.out.println(encodedString);
            String base64Image = encodedString;
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            // Tạo ImageIcon từ mảng byte
            ImageIcon imageIcon = new ImageIcon(imageBytes);

            // (Tuỳ chọn) Resize ảnh cho vừa khung
            Image image = imageIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(image);

            // Hiển thị trong JFrame
            JFrame frame = new JFrame("Hiển thị ảnh từ Base64");
            JLabel label = new JLabel(scaledIcon);
            frame.add(label);
            frame.setSize(400, 400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

