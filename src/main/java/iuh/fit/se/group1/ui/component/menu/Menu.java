
package iuh.fit.se.group1.ui.component.menu;

import iuh.fit.se.group1.util.Constants;
import net.miginfocom.swing.MigLayout;

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

    private boolean isManager = true;

    private MigLayout layout;
    private final String[][] menuItemsManager = new String[][]{
            {"Dashboard"},
            {"Đặt phòng"},
            {"Thanh toán"},
            {"Quản lý nhân viên", "Quản lý ca", "Danh sách nhân viên"},
            {"Quản lý khách hàng"},
            {"Quản lý dịch vụ"},
            {"Quản lý khuyến mãi"},
            {"Quản lý phòng"},
            {"Tiện ích phòng"},
            {"Quản lý hóa đơn"},
            {"Quản lý phụ phí"},
            {"Quản lý thống kê", "Thống kê doanh thu",  "Thống kê theo xu hướng"},
            {"Hỗ trợ", "Hướng dẫn sử dụng", "Quy định", "Phiên bản","Về chúng tôi"}

    };

    private final String[][] menuItemsEmployee = new String[][]{
            {"Dashboard"},
            {"Đặt phòng"},
            {"Thanh toán"},
            {"Tiện ích phòng"},
            {"Quản lý hóa đơn"},
            {"Đóng ca"},
            {"Hỗ trợ", "Hướng dẫn sử dụng", "Quy định", "Phiên bản", "Về chúng tôi"}
    };

    public Menu() {
        init();
    }

    private void init() {
        layout = new MigLayout("wrap 1, fillx, gapy 0, inset 2", "fill");
        setLayout(layout);
        setOpaque(true);
        // tạo menu items
        if (isManager) {
            setMenuManager();
        } else {
            setMenuEmployee();
        }
    }

    private void setMenuEmployee() {
        for (int i = 0; i < menuItemsEmployee.length; i++) {
            addMenuItem(menuItemsEmployee[i][0], i);
        }
    }

    private void setMenuManager() {
        for (int i = 0; i < menuItemsManager.length; i++) {
            addMenuItem(menuItemsManager[i][0], i);
        }
    }

    private void addMenuItem(String name, int index) {

        int length = isManager ? menuItemsManager[index].length : menuItemsEmployee[index].length;
        boolean subMenuAble = length > 1;
        MenuItem item = new MenuItem(name, index, subMenuAble);

        Icon icon = isManager ? Constants.getIconOfManager(index) : Constants.getIconOfEmployee(index);
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
            } else {
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
            if (isManager) {
                setSubmenuItem(index, length, panel, i, menuItemsManager);
            }else {
                setSubmenuItem(index, length, panel, i, menuItemsEmployee);
            }
        }
        add(panel, "h 0!", indexZorder + 1);
        revalidate();
        repaint();
        MenuAnimation.showMenu(panel, item, layout, true);
    }

    private void setSubmenuItem(int index, int length, JPanel panel, int i, String[][] menuItems) {
        MenuItem subItem = new MenuItem(menuItems[index][i], i, false);
        subItem.addActionListener(e -> {
            if (menuEvent != null) {
                menuEvent.selected(index, subItem.getIndex());
            }
        });
        subItem.initSubMenu(i, length);

        panel.add(subItem);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Constants.BACKGROUND_COLOR_MENU);
        g2.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
        super.paintComponent(g);
    }

    public void setAuth(boolean isAdmin) {
        this.isManager = isAdmin;
        removeAll();
        if (isManager) {
            setMenuManager();
        } else {
            setMenuEmployee();
        }
        revalidate();
        repaint();
    }
}
