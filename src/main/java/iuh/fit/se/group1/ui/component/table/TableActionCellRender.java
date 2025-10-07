/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iuh.fit.se.group1.ui.component.table;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author vietn
 */
public class TableActionCellRender extends DefaultTableCellRenderer{

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        PanelAction action = new PanelAction();
        action.setOpaque(false);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(true);
        wrapper.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        wrapper.add(action);

        return wrapper;
    }
    
}
