package iuh.fit.se.group1.ui.component.table;



import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TableActionCellRender extends DefaultTableCellRenderer {
    private final boolean isView;

    public TableActionCellRender(boolean isView) {
        this.isView = isView;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        PanelAction action = new PanelAction();
        action.setOpaque(false);
        if (!isView) action.remove(0); // ẩn nút con mắt nếu không dùng

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(true);
        wrapper.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        wrapper.add(action);
        return wrapper;
    }
}
