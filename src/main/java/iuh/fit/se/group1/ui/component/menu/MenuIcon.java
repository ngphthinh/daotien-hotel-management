package iuh.fit.se.group1.ui.component.menu;

import iuh.fit.se.group1.util.Constants;
import net.miginfocom.swing.MigLayout;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class MenuIcon extends JComponent {

    private final int SIZE_ICON = 20;

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
            {"Quản lý nhân viên"},
            {"Quản lý ca"},
            {"Danh sách nhân viên"},
            {"Quản lý khách hàng"},
            {"Quản lý dịch vụ"},
            {"Quản lý khuyến mãi"},
            {"Quản lý phòng"},
            {"Quản lý thống kê"},
            {"Hỗ trợ"}
    };

    public MenuIcon() {
        init();
    }

    private void init() {
        layout = new MigLayout("wrap 1, fillx, gapy 0, inset 2", "fill");
        setLayout(layout);
        setOpaque(true);
        // tạo menu items
        addMenuItem(Constants.BARS);
        for (int i = 0; i < menuItems.length; i++) {
            addMenuItem(i);
        }
    }

    private Icon getIcon(int index) {
        return switch (index) {
            case 99 -> FontIcon.of(FontAwesomeSolid.BARS, SIZE_ICON, Constants.COLOR_ICON_MENU);
            case 0 -> FontIcon.of(FontAwesomeSolid.HOME, SIZE_ICON,Constants.COLOR_ICON_MENU);
            case 1 -> FontIcon.of(FontAwesomeSolid.BED, SIZE_ICON,Constants.COLOR_ICON_MENU);
            case 2 -> FontIcon.of(FontAwesomeSolid.USERS, SIZE_ICON,Constants.COLOR_ICON_MENU);
            case 3 -> FontIcon.of(FontAwesomeSolid.CLOCK, SIZE_ICON,Constants.COLOR_ICON_MENU);
            case 4 -> FontIcon.of(FontAwesomeSolid.ID_BADGE, SIZE_ICON, Constants.COLOR_ICON_MENU);   // Danh sách nhân viên
            case 5 -> FontIcon.of(FontAwesomeSolid.USER_FRIENDS, SIZE_ICON, Constants.COLOR_ICON_MENU); // Quản lý khách hàng
            case 6 -> FontIcon.of(FontAwesomeSolid.CONCIERGE_BELL, SIZE_ICON,Constants.COLOR_ICON_MENU);
            case 7 -> FontIcon.of(FontAwesomeSolid.GIFT, SIZE_ICON,Constants.COLOR_ICON_MENU);
            case 8 -> FontIcon.of(FontAwesomeSolid.DOOR_OPEN, SIZE_ICON,Constants.COLOR_ICON_MENU);
            case 9 -> FontIcon.of(FontAwesomeSolid.CHART_BAR, SIZE_ICON,Constants.COLOR_ICON_MENU);
            case 10 -> FontIcon.of(FontAwesomeSolid.QUESTION_CIRCLE, SIZE_ICON,Constants.COLOR_ICON_MENU);
            default -> null;
        };
    }


    private void addMenuItem(int index) {
        MenuIconItem item = new MenuIconItem(index);

        Icon icon = getIcon(index);
        if (icon != null) {
            item.setIcon(icon);
        }

        // Lấy tên của button từ menuItems
        String name = index == 99 ? "Mở sidebar": menuItems[index][0];

        // Tạo popup đơn giản chỉ chứa JLabel
        JPopupMenu popup = new JPopupMenu() {
            @Override
            public void updateUI() {
                super.updateUI();
                setBorder(BorderFactory.createEmptyBorder()); // xóa border mặc định
            }
        };
        popup.setBorder(null);
        popup.setOpaque(false);
        popup.setBackground(new Color(0,0,0,0)); // trong suốt nếu muốn

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Constants.BACKGROUND_COLOR_MENU); // màu nền
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12); // bo góc
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false); // để paintComponent xử lý
        JLabel label = new JLabel(name);
        label.setOpaque(false); // không cần nền riêng
        label.setForeground(Constants.FOREGROUND_COLOR_MENU);
        label.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        panel.add(label, BorderLayout.CENTER);
        popup.add(panel);

        // Hiển thị popup khi hover
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                popup.show(item, item.getWidth(), 0);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                popup.setVisible(false); // ẩn popup khi rời button
            }
        });

        item.addActionListener(e-> {
            if (menuEvent != null) {
                menuEvent.selected(index,0);
            }
        });
        add(item);
        revalidate();
        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Constants.BACKGROUND_COLOR_MENU);
        g2.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
        super.paintComponent(g);
    }
}
