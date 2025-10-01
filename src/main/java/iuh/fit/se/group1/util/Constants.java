
package iuh.fit.se.group1.util;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import java.awt.*;

public class Constants {
    public static final Color FOREGROUND_COLOR_MENU = new Color(77, 134, 168);

    public static final Color BACKGROUND_COLOR_MENU = new Color(255, 255, 255);

    public static final Color COLOR_ICON_MENU = Color.decode("#FF6C03");

    public static final Color LINE_SUBMENU = new Color(181, 181, 181);

    public static final int HEIGHT_FRAME = 800;
    public static final int WIDTH_FRAME = 1400;
    public static final int BARS = 99;

    public static Icon getIcon(int index) {
        return switch (index) {
            case 99 -> FontIcon.of(FontAwesomeSolid.BARS, 16, Constants.COLOR_ICON_MENU);
            case 0 -> FontIcon.of(FontAwesomeSolid.HOME, 16, Constants.COLOR_ICON_MENU);
            case 1 -> FontIcon.of(FontAwesomeSolid.CALENDAR_ALT, 16, Constants.COLOR_ICON_MENU);
            case 2 -> FontIcon.of(FontAwesomeSolid.USERS, 14, Constants.COLOR_ICON_MENU);
            case 3 -> FontIcon.of(FontAwesomeSolid.USER, 16, Constants.COLOR_ICON_MENU);
            case 4 -> FontIcon.of(FontAwesomeSolid.CONCIERGE_BELL, 16, Constants.COLOR_ICON_MENU);
            case 5 -> FontIcon.of(FontAwesomeSolid.GIFT, 16, Constants.COLOR_ICON_MENU);
            case 6 -> FontIcon.of(FontAwesomeSolid.BED, 16, Constants.COLOR_ICON_MENU);
            case 7 -> FontIcon.of(FontAwesomeSolid.CHART_BAR, 16, Constants.COLOR_ICON_MENU);
            case 8 -> FontIcon.of(FontAwesomeSolid.QUESTION_CIRCLE, 16, Constants.COLOR_ICON_MENU);
            default -> null;
        };
    }
}