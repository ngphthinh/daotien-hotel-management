
package iuh.fit.se.group1.util;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Locale;

public class Constants {
    public static final Color FOREGROUND_COLOR_MENU = new Color(77, 134, 168);

    public static final Color BACKGROUND_COLOR_MENU = new Color(255, 255, 255);

    public static final Color COLOR_ICON_MENU = Color.decode("#FF6C03");

    public static final Color LINE_SUBMENU = new Color(181, 181, 181);

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static final NumberFormat VND_FORMAT =
            NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    public static final int HEIGHT_FRAME = 800;
    public static final int WIDTH_FRAME = 1400;
    public static final int BARS = 99;

    public static Icon getIconOfManager(int index) {
        return switch (index) {
            case 99 -> FontIcon.of(FontAwesomeSolid.BARS, 16, Constants.COLOR_ICON_MENU);
            case 0 -> FontIcon.of(FontAwesomeSolid.HORSE, 16, Constants.COLOR_ICON_MENU);
            case 1 -> FontIcon.of(FontAwesomeSolid.CALENDAR_ALT, 16, Constants.COLOR_ICON_MENU);
            case 2 -> FontIcon.of(FontAwesomeSolid.MONEY_BILL, 14, Constants.COLOR_ICON_MENU);
            case 3 -> FontIcon.of(FontAwesomeSolid.USER, 16, Constants.COLOR_ICON_MENU);
            case 4 -> FontIcon.of(FontAwesomeSolid.USERS, 16, Constants.COLOR_ICON_MENU);
            case 5 -> FontIcon.of(FontAwesomeSolid.GIFT, 16, Constants.COLOR_ICON_MENU);
            case 6 -> FontIcon.of(FontAwesomeSolid.GIFTS, 16, Constants.COLOR_ICON_MENU);
            case 7 -> FontIcon.of(FontAwesomeSolid.BED, 16, Constants.COLOR_ICON_MENU);
            case 8 -> FontIcon.of(FontAwesomeSolid.FILE_INVOICE_DOLLAR, 16, Constants.COLOR_ICON_MENU);
            case 9 -> FontIcon.of(FontAwesomeSolid.CHART_LINE, 16, Constants.COLOR_ICON_MENU);
            case 10 -> FontIcon.of(FontAwesomeSolid.QUESTION_CIRCLE, 16, Constants.COLOR_ICON_MENU);
            default -> null;
        };
    }
    public static String bufferedImageToBase64(BufferedImage image, String format) {
        if (image == null) return null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, format, baos);
            byte[] bytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static BufferedImage base64ToBufferedImage(String base64) {
        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64);
            ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
            return ImageIO.read(bais);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Icon getIconOfEmployee(int index) {
        return switch (index) {
            case 99 -> FontIcon.of(FontAwesomeSolid.BARS, 16, Constants.COLOR_ICON_MENU);
            case 0 -> FontIcon.of(FontAwesomeSolid.HOME, 16, Constants.COLOR_ICON_MENU);
            case 1 -> FontIcon.of(FontAwesomeSolid.CALENDAR_ALT, 16, Constants.COLOR_ICON_MENU);
            case 2 -> FontIcon.of(FontAwesomeSolid.MONEY_BILL, 14, Constants.COLOR_ICON_MENU);
            case 3 -> FontIcon.of(FontAwesomeSolid.LOCK_OPEN, 16, Constants.COLOR_ICON_MENU);
            case 4 -> FontIcon.of(FontAwesomeSolid.QUESTION_CIRCLE, 16, Constants.COLOR_ICON_MENU);
            default -> null;
        };
    }
}