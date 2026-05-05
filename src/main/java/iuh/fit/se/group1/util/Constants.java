
package iuh.fit.se.group1.util;



import javax.swing.*;

import java.awt.*;

import java.text.DecimalFormat;

import java.time.format.DateTimeFormatter;

public class Constants {

    public static final Color COLOR_ICON_MENU = Color.decode("#FF6C03");



    public static final String SINGLE_ROOM_TYPE = "SINGLE";
    public static final String DOUBLE_ROOM_TYPE = "DOUBLE";

    public static final DecimalFormat VND_FORMAT = new DecimalFormat("#,##0.## VND");



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