package iuh.fit.se.group1.ui.component.custom;

import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * AvatarLabel component for displaying circular avatars
 * @author Administrator
 */
public class AvatarLabel extends JPanel {

    private static final Logger log = LoggerFactory.getLogger(AvatarLabel.class);
    private BufferedImage image;
    private BufferedImage scaled;
    private BufferedImage defaultImage;


    public AvatarLabel() {
        this(true);
    }
    public AvatarLabel(boolean loadDefaultImage) {
        setPreferredSize(new Dimension(60, 60));
        setOpaque(false);

        if (loadDefaultImage) {
            loadDefaultImage();
            if (defaultImage != null) {
                this.image = defaultImage;
            }
        }
    }
    private void loadDefaultImage() {
        try {
            URL url = getClass().getResource("/images/conmeo.jpg");
            if (url != null) {
                defaultImage = ImageIO.read(url);
                log.info("Default avatar loaded from URL: {}", url);
            } else {
                log.warn("Default avatar image resource not found at /images/conmeo.jpg");
            }
        } catch (Exception e) {
            log.error("Error loading default avatar image: ", e);
        }
    }
    public void resetToDefault() {
        if (defaultImage == null) {
            loadDefaultImage();
        }
        if (defaultImage != null) {
            this.image = defaultImage;
            this.scaled = null;
            repaint();
            log.info("Avatar reset to default image");
        } else {
            log.warn("Default avatar not found when resetting");
        }
    }
    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        if (image == null) {
            log.warn("Attempting to set null image, resetting to default");
            resetToDefault();
            return;
        }
        this.image = image;
        this.scaled = null;
        repaint();
        log.info("Avatar image updated successfully");
    }
    public byte[] getImageAsBytes(String format) {
        if (image == null) {
            log.warn("No image to convert to bytes");
            return null;
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, format, baos);
            byte[] bytes = baos.toByteArray();
            log.info("Image converted to bytes successfully");
            return bytes;
        } catch (Exception e) {
            log.error("Error converting image to bytes: ", e);
            return null;
        }
    }
    public String getImageAsBase64(String format) {
        byte[] bytes = getImageAsBytes(format);
        if (bytes == null) {
            return null;
        }
        String base64 = Base64.getEncoder().encodeToString(bytes);
        log.info("Image converted to Base64 successfully");
        return base64;
    }
    public boolean setImageFromBytes(byte[] imageBytes) {
        if (imageBytes == null || imageBytes.length == 0) {
            log.warn("Image bytes is null or empty, resetting to default");
            resetToDefault();
            return false;
        }

        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
            BufferedImage loadedImage = ImageIO.read(bais);

            if (loadedImage != null) {
                setImage(loadedImage);
                log.info("Image loaded from bytes successfully");
                return true;
            } else {
                log.error("Failed to decode image from bytes");
                resetToDefault();
                return false;
            }
        } catch (Exception e) {
            log.error("Error loading image from bytes: ", e);
            resetToDefault();
            return false;
        }
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