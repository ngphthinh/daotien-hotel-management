package iuh.fit.se.group1.ui.component.custom;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.net.URL;
import javax.swing.*;

/**
 *
 * @author Administrator
 */
public class AvatarLabel extends JPanel {

    private Image image;

    public AvatarLabel() {
        setPreferredSize(new Dimension(60, 60));
        setOpaque(false); // trong suốt nền

        try {
            URL url = getClass().getResource("/images/avttest.jpg");
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                this.image = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                System.out.println("✅ Ảnh avatar URL: " + url);
            } else {
                System.err.println("❌ Không tìm thấy ảnh avatar!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // vẽ nền trong suốt
        if (image != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = Math.min(getWidth(), getHeight());
            Shape clip = new Ellipse2D.Float(0, 0, size, size);
            g2.setClip(clip);
            g2.drawImage(image, 0, 0, size, size, this);

            // Viền nhẹ quanh avatar
            g2.setColor(new Color(220, 220, 220));
            g2.setStroke(new BasicStroke(2f));
            g2.draw(clip);

            g2.dispose();
        }
    }
}
