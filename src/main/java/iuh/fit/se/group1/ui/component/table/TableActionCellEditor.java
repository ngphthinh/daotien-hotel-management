package iuh.fit.se.group1.ui.component.table;

import javax.swing.*;
import java.awt.*;

public class TableActionCellEditor extends DefaultCellEditor {
    private final TableActionEvent event;
    private final boolean isView;

    public TableActionCellEditor(TableActionEvent event, boolean isView) {
        super(new JCheckBox());
        this.event = event;
        this.isView = isView;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
                                                 int row, int column) {
        PanelAction action = new PanelAction();
        if (!isView) action.remove(0); // ẩn con mắt nếu không cần
        action.initEvent(event, row);
        action.setOpaque(false);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(true);
        wrapper.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        wrapper.add(action);

        return wrapper;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }
}
