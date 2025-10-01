/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iuh.fit.se.group1.ui.component.table;

import iuh.fit.se.group1.ui.component.table.PanelAction;
import java.awt.Component;
import java.awt.GridBagLayout;
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
        Component com = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        PanelAction action = new PanelAction();
        action.setOpaque(false);
        if (isSelected) {
            action.setBackground(table.getSelectionBackground());
        } else {
            action.setBackground(table.getBackground());
        }
        return action;
    }
    
}
