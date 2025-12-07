package iuh.fit.se.group1.ui.component.custom;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;

public class Combobox<E> extends JComboBox<E> {

    public Combobox(E[] items) {
        super(items);
        setFont(new Font("Segoe UI", Font.PLAIN, 13));
        setForeground(new Color(50, 50, 50));
        setBackground(new Color(250, 250, 250));
        setBorder(new EmptyBorder(4, 8, 4, 8));
        setFocusable(true);
        setUI(new ModernComboBoxUI());
    }


    
    public Combobox(ComboBoxModel<E> aModel) {
        super(aModel);
    }

    public Combobox() {
    }
    


    // ===== Custom modern flat UI =====
    private static class ModernComboBoxUI extends BasicComboBoxUI {
        private final Color borderColor = new Color(210, 210, 210);
        private final Color background = new Color(250, 250, 250);
        private final Color hoverBackground = new Color(235, 240, 255);
        private final Color focusBorder = new Color(140, 170, 255);

        @Override
protected JButton createArrowButton() {
    JButton arrow = new JButton("\u25BC"); // ▼
    arrow.setBorderPainted(false);
    arrow.setFocusPainted(false);
    arrow.setContentAreaFilled(false);
    arrow.setOpaque(false);
    arrow.setForeground(new Color(100, 100, 100));
    arrow.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 10));

    // ✅ Thêm dòng này để khi click vào mũi tên thì popup mở ra
    arrow.addActionListener(e -> {
        if (comboBox != null) {
            comboBox.setPopupVisible(!comboBox.isPopupVisible());
        }
    });

    return arrow;
}


        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
            Graphics2D g2 = (Graphics2D) g.create();
            comboBox.setFocusable(true);

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color bg = comboBox.isPopupVisible() ? hoverBackground : background;
            g2.setColor(bg);
            g2.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 5, 5);

            // Border
            g2.setColor(comboBox.hasFocus() ? focusBorder : borderColor);
            g2.drawRoundRect(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1, 5, 5);

            g2.dispose();
        }

        @Override
        protected ListCellRenderer<Object> createRenderer() {
            return new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                              boolean isSelected, boolean cellHasFocus) {
                    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                    label.setBorder(new EmptyBorder(5, 20, 5, 20));
                    if (isSelected) {
                        label.setBackground(new Color(225, 235, 255));
                        label.setForeground(new Color(25, 25, 60));
                    } else {
                        label.setBackground(Color.WHITE);
                        label.setForeground(new Color(40, 40, 40));
                    }
                    return label;
                }
            };
        }
    }

    // ===== Đưa combobox vào header JTable =====
    public static <E> void setHeaderCombo(JTable table, int column, E[] items) {
        Combobox<E> combo = new Combobox<>(items);
        combo.setPreferredSize(new Dimension(120, 26));

        TableColumn tableColumn = table.getColumnModel().getColumn(column);
        tableColumn.setHeaderRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                return combo;
            }
        });
    }
}
