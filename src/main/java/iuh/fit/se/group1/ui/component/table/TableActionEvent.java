/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iuh.fit.se.group1.ui.component.table;

/**
 *
 * @author vietn
 */
public interface TableActionEvent {
    void onEdit(int row);
    void onDelete(int row);
    default void onView(int row) {}
}
