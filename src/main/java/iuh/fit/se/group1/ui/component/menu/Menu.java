
package iuh.fit.se.group1.ui.component.menu;

import iuh.fit.se.group1.util.Constants;
import net.miginfocom.swing.MigLayout;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;


public class Menu extends JComponent {

    private MenuEvent menuEvent;

    public MenuEvent getMenuEvent() {
        return menuEvent;
    }

    public void setMenuEvent(MenuEvent menuEvent) {
        this.menuEvent = menuEvent;
    }

    private MigLayout layout;
    private final String[][] menuItems = new String[][]{
            {"Trang chủ"},
            {"Quản lý đặt phòng"},
            {"Quản lý nhân viên", "Quản lý ca", "Danh sách nhân viên"},
            {"Quản lý khách hàng"},
            {"Quản lý dịch vụ"},
            {"Quản lý khuyến mãi"},
            {"Quản lý phòng"},
            {"Quản lý thống kê", "Quản lí hóa đơn", "Thống kê doanh thu", "Tỷ lê lấp đầy phòng", "Thống kê theo xu hướng"},
            {"Hỗ trợ", "Hướng dẫn sử dụng", "Quy định", "Phiên bản"}
    };

    public Menu() {
        init();
    }

    private void init() {
        layout = new MigLayout("wrap 1, fillx, gapy 0, inset 2", "fill");
        setLayout(layout);
        setOpaque(true);
        // tạo menu items
        for (int i = 0; i < menuItems.length; i++) {
            addMenuItem(menuItems[i][0], i);
        }
    }

    private Icon getIcon(int index) {
        return switch (index) {
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

    private void addMenuItem(String name, int index) {
        int length = menuItems[index].length;
        boolean subMenuAble = length > 1;
        MenuItem item = new MenuItem(name, index, subMenuAble);

        Icon icon = getIcon(index);
        if (icon != null) {
            item.setIcon(icon);
        }
        item.addActionListener(e -> {
            if (subMenuAble) {
                if (!item.isSelected()) {
                    item.setSelected(true);
                    addSubMenu(item, index, length, getComponentZOrder(item));
                } else {
                    item.setSelected(false);
                    JPanel pnl = (JPanel) getComponent(getComponentZOrder(item) + 1);
                    MenuAnimation.showMenu(pnl, item, layout, false);
                    item.setSubMenuAble(true);
                }
            }else {
                if (menuEvent != null) {
                    menuEvent.selected(index, 0);
                }
            }
        });

        add(item);
        revalidate();
        repaint();
    }

    private void addSubMenu(MenuItem item, int index, int length, int indexZorder) {
        JPanel panel = new JPanel(new MigLayout("wrap 1, fillx, inset 0, gapy 0", "fill"));
        panel.setName(index + "");
        panel.setBackground(Constants.BACKGROUND_COLOR_MENU);
        for (int i = 1; i < length; i++) {
            MenuItem subItem = new MenuItem(menuItems[index][i], i, false);
            subItem.addActionListener(e -> {
                if (menuEvent != null) {
                    menuEvent.selected(index, subItem.getIndex());
                }
            });
            subItem.initSubMenu(i, length);
            panel.add(subItem);
        }
        add(panel, "h 0!", indexZorder + 1);
        revalidate();
        repaint();
        MenuAnimation.showMenu(panel, item, layout, true);
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Constants.BACKGROUND_COLOR_MENU);
        g2.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
        super.paintComponent(g);
    }
}
