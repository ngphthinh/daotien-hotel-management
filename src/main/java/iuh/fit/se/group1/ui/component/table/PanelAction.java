package iuh.fit.se.group1.ui.component.table;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import java.awt.*;

public class PanelAction extends javax.swing.JPanel {

    private iuh.fit.se.group1.ui.component.custom.ActionButton btnEdit;
    private iuh.fit.se.group1.ui.component.custom.ActionButton btnDelete;
    private iuh.fit.se.group1.ui.component.custom.ActionButton btnView;

    public PanelAction() {
        initComponents();
        setOpaque(false);

        btnEdit.setIcon(FontIcon.of(FontAwesomeSolid.PEN, 17, new Color(30, 144, 255)));
        style(btnEdit);

        btnDelete.setIcon(FontIcon.of(FontAwesomeSolid.TRASH, 17, new Color(220, 53, 69)));
        style(btnDelete);

        btnView.setIcon(FontIcon.of(FontAwesomeSolid.EYE, 17, new Color(40, 167, 69))); // màu xanh lá
        style(btnView);
    }

    private void style(iuh.fit.se.group1.ui.component.custom.ActionButton btn) {
        btn.setBorder(null);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setBackground(new Color(0, 0, 0, 0));
    }

    public void initEvent(TableActionEvent event, int row) {
        btnEdit.addActionListener(ae -> {
            stopEditingIfNeeded();
            event.onEdit(row);
        });

        btnDelete.addActionListener(ae -> {
            stopEditingIfNeeded();
            event.onDelete(row);
        });

        btnView.addActionListener(ae -> {
            stopEditingIfNeeded();
            event.onView(row);
        });
    }

    /** Dừng chế độ chỉnh sửa cell nếu đang edit */
    private void stopEditingIfNeeded() {
        // Lấy JTable chứa panel này
        Component c = SwingUtilities.getAncestorOfClass(JTable.class, this);
        if (c instanceof JTable table) {
            if (table.isEditing()) {
                table.getCellEditor().stopCellEditing();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        btnEdit = new iuh.fit.se.group1.ui.component.custom.ActionButton();
        btnDelete = new iuh.fit.se.group1.ui.component.custom.ActionButton();
        btnView = new iuh.fit.se.group1.ui.component.custom.ActionButton();

        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 5));
        add(btnView);
        add(btnEdit);
        add(btnDelete);
    }
}
