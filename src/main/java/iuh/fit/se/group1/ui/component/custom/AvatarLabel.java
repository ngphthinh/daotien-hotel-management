


package iuh.fit.se.group1.ui.component.custom;

import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *
 * @author Administrator
 */
public class AvatarLabel extends JPanel {

    private static final Logger log = LoggerFactory.getLogger(AvatarLabel.class);
    private BufferedImage image;   
    private BufferedImage scaled; 

    public AvatarLabel() {
        setPreferredSize(new Dimension(60, 60));
        setOpaque(false); // trong suốt nền

        try {
            URL url = getClass().getResource("/images/avttest.jpg");
            if (url != null) {
                image = ImageIO.read(url);
                log.info("avatar URL: {}", url);
            } else {
                log.warn("Avatar image resource not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
         this.scaled = null; // reset, để lần sau vẽ sẽ resize lại theo kích thước mới
        repaint();
    }


    @Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (image != null) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int size = Math.min(getWidth(), getHeight());

        if (scaled == null || scaled.getWidth() != size) {
                scaled = Scalr.resize(image, Scalr.Method.QUALITY, Scalr.Mode.FIT_EXACT, size, size, Scalr.OP_ANTIALIAS);
            }
        int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;
//        Shape clip = new Ellipse2D.Float(0, 0, size, size);
//        g2.setClip(clip);
//        g2.drawImage(scaled, 0, 0, size, size, this);
        Shape clip = new Ellipse2D.Float(x, y, size, size);
            g2.setClip(clip);
            g2.drawImage(scaled, x, y, size, size, this);
        // Viền avatar
        g2.setColor(new Color(220, 220, 220));
        g2.setStroke(new BasicStroke(2f));
        g2.draw(clip);

        g2.dispose();
    }
}

}
