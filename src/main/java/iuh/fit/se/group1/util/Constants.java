
package iuh.fit.se.group1.util;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;

import java.text.DecimalFormat;

import java.text.ParseException;
import java.time.format.DateTimeFormatter;

public class Constants {
    public static final Color FOREGROUND_COLOR_MENU = new Color(77, 134, 168);

    public static final Color BACKGROUND_COLOR_MENU = new Color(255, 255, 255);

    public static final Color COLOR_ICON_MENU = Color.decode("#FF6C03");

    public static final Color LINE_SUBMENU = new Color(181, 181, 181);

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static final DecimalFormat VND_FORMAT = new DecimalFormat("#,##0.## VND");
    public static final int HEIGHT_FRAME = 800;
    public static final int WIDTH_FRAME = 1400;
    public static final int BARS = 99;

    public static Icon getIconOfManager(int index) {
        return switch (index) {
            case 99 -> FontIcon.of(FontAwesomeSolid.BARS, 16, Constants.COLOR_ICON_MENU);
            case 0 -> FontIcon.of(FontAwesomeSolid.CHART_AREA, 16, Constants.COLOR_ICON_MENU);
            case 1 -> FontIcon.of(FontAwesomeSolid.CALENDAR_ALT, 16, Constants.COLOR_ICON_MENU);
            case 2 -> FontIcon.of(FontAwesomeSolid.MONEY_BILL, 14, Constants.COLOR_ICON_MENU);
            case 3 -> FontIcon.of(FontAwesomeSolid.USER, 16, Constants.COLOR_ICON_MENU);
            case 4 -> FontIcon.of(FontAwesomeSolid.USERS, 16, Constants.COLOR_ICON_MENU);
            case 5 -> FontIcon.of(FontAwesomeSolid.GIFT, 16, Constants.COLOR_ICON_MENU);
            case 6 -> FontIcon.of(FontAwesomeSolid.GIFTS, 16, Constants.COLOR_ICON_MENU);
            case 7 -> FontIcon.of(FontAwesomeSolid.BED, 16, Constants.COLOR_ICON_MENU);
            case 8 -> FontIcon.of(FontAwesomeSolid.TOOLS, 16, Constants.COLOR_ICON_MENU);
            case 9 -> FontIcon.of(FontAwesomeSolid.FILE_INVOICE_DOLLAR, 16, Constants.COLOR_ICON_MENU);
            case 10 -> FontIcon.of(FontAwesomeSolid.MONEY_CHECK, 16, Constants.COLOR_ICON_MENU);
            case 11 -> FontIcon.of(FontAwesomeSolid.CHART_LINE, 16, Constants.COLOR_ICON_MENU);
            case 12 -> FontIcon.of(FontAwesomeSolid.QUESTION_CIRCLE, 16, Constants.COLOR_ICON_MENU);
            default -> null;
        };
    }

    public static Icon getIconOfEmployee(int index) {
        return switch (index) {
            case 99 -> FontIcon.of(FontAwesomeSolid.BARS, 16, Constants.COLOR_ICON_MENU);
            case 0 -> FontIcon.of(FontAwesomeSolid.HOME, 16, Constants.COLOR_ICON_MENU);
            case 1 -> FontIcon.of(FontAwesomeSolid.CALENDAR_ALT, 16, Constants.COLOR_ICON_MENU);
            case 2 -> FontIcon.of(FontAwesomeSolid.MONEY_BILL, 14, Constants.COLOR_ICON_MENU);
            case 3 -> FontIcon.of(FontAwesomeSolid.TOOLS, 14, Constants.COLOR_ICON_MENU);
            case 4 -> FontIcon.of(FontAwesomeSolid.FILE_INVOICE_DOLLAR, 16, Constants.COLOR_ICON_MENU);
            case 5 -> FontIcon.of(FontAwesomeSolid.LOCK_OPEN, 16, Constants.COLOR_ICON_MENU);
            case 6 -> FontIcon.of(FontAwesomeSolid.QUESTION_CIRCLE, 16, Constants.COLOR_ICON_MENU);
            default -> null;
        };
    }

    public static double parseVND(String formatted) {
        if (formatted == null || formatted.isEmpty())
            return 0.0;

        // Chỉ giữ số và dấu , .
        String cleaned = formatted.replaceAll("[^\\d.,]", "");

        // Trường hợp chỉ có số, không có dấu , hoặc .
        if (!cleaned.contains(",") && !cleaned.contains(".")) {
            return Double.parseDouble(cleaned);
        }

        // Nếu có cả dấu ',' và '.'
        if (cleaned.contains(",") && cleaned.contains(".")) {
            // Xác định dấu thập phân: cái nào nằm *cuối* hơn
            int lastComma = cleaned.lastIndexOf(',');
            int lastDot = cleaned.lastIndexOf('.');

            char decimalSeparator = lastComma > lastDot ? ',' : '.';

            // Chuẩn hóa về dấu thập phân = '.'
            if (decimalSeparator == ',') {
                cleaned = cleaned.replace(".", "");   // bỏ dấu ngàn
                cleaned = cleaned.replace(",", "."); // chuyển sang dấu thập phân
            } else {
                cleaned = cleaned.replace(",", "");  // bỏ dấu ngàn
            }

            return Double.parseDouble(cleaned);
        }

        // Nếu chỉ có dấu ',' → có thể là dấu ngàn hoặc thập phân
        if (cleaned.contains(",")) {
            // Nếu sau dấu phẩy có 3 số → đây là dấu ngàn ("12,000")
            int idx = cleaned.lastIndexOf(',');
            int digitsAfter = cleaned.length() - idx - 1;

            if (digitsAfter == 3) {
                cleaned = cleaned.replace(",", "");
                return Double.parseDouble(cleaned);
            } else {
                // dấu ',' là thập phân → chuyển về '.'
                cleaned = cleaned.replace(",", ".");
                return Double.parseDouble(cleaned);
            }
        }

        // Nếu chỉ có '.' làm dấu tương tự
        if (cleaned.contains(".")) {
            int idx = cleaned.lastIndexOf('.');
            int digitsAfter = cleaned.length() - idx - 1;

            if (digitsAfter == 3) {
                cleaned = cleaned.replace(".", "");
                return Double.parseDouble(cleaned);
            } else {
                return Double.parseDouble(cleaned);
            }
        }

        return Double.parseDouble(cleaned);
    }

    public static java.math.BigDecimal parseVNDToBigDecimal(String formatted) {
        double value = parseVND(formatted);
        return java.math.BigDecimal.valueOf(value);
    }

}