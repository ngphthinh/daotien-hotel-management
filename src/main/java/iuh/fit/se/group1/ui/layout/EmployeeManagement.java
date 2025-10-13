/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.layout;

import iuh.fit.se.group1.ui.component.modal.ServiceModal;
import iuh.fit.se.group1.ui.component.custom.Combobox;
import iuh.fit.se.group1.ui.component.modal.InfoEmployeeModal;
import iuh.fit.se.group1.ui.component.table.TableActionEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;
import raven.glasspanepopup.GlassPanePopup;

/**
 * @author THIS PC
 */
public class EmployeeManagement extends javax.swing.JPanel {

    /**
     * Creates new form EmployeeManagement
     */
    public EmployeeManagement() {
        initComponents();
        headerCustom2.getjLabel1().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));

       headerCustom2.getjLabel1().setText(
    "<html><span style='color:white;'>Quản lý nhân viên</span>"
  + "<span style='color:rgb(204,204,204);'> &gt; Thông tin nhân viên</span></html>"
);
        button1.setBackground(new Color(108, 165, 200));
        button1.setForeground(Color.WHITE);
        button1.setBorderRadius(40);

        button1.setIcon(FontIcon.of(FontAwesomeSolid.PLUS, 17, Color.WHITE), SwingConstants.RIGHT);
           String cols[] = {"Mã nhân viên", "Họ tên", "Giới tính", "Chức vụ","Số điện thoại","Chức năng"};
        DefaultTableModel model = new DefaultTableModel(cols, 10);
        tblEmployee.getTbl().setModel(model);
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                System.out.println("Edit row: " + row);
            }

            @Override
            public void onDelete(int row) {
                if (tblEmployee.getTbl().isEditing()) {
                    tblEmployee.getTbl().getCellEditor().stopCellEditing();
                }
                DefaultTableModel model = (DefaultTableModel) tblEmployee.getTbl().getModel();
                model.removeRow(row);
            }
        };
        tblEmployee.setTableActionColumn(tblEmployee.getTbl(), 5, new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                System.out.println("Edit row " + row);
            }

            @Override
            public void onDelete(int row) {
                if (tblEmployee.getTbl().isEditing()) {
                    tblEmployee.getTbl().getCellEditor().stopCellEditing();
                }
                DefaultTableModel model = (DefaultTableModel) tblEmployee.getTbl().getModel();
                model.removeRow(row);
            }

            @Override
            public void onView(int row) {
                System.out.println("View row " + row);
            }
        }, true);

        tblEmployee.getTbl().getColumnModel().getColumn(0).setPreferredWidth(120);  // chiều rộng mong muốn
        tblEmployee.getTbl().getColumnModel().getColumn(1).setPreferredWidth(200);
        tblEmployee.getTbl().getColumnModel().getColumn(2).setPreferredWidth(120);
        tblEmployee.getTbl().getColumnModel().getColumn(3).setPreferredWidth(120);
        tblEmployee.getTbl().getColumnModel().getColumn(4).setPreferredWidth(100);
        tblEmployee.getTbl().getColumnModel().getColumn(5).setPreferredWidth(80);


        tblEmployee.getTbl().addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                int col = tblEmployee.getTbl().columnAtPoint(e.getPoint());

                // Giả sử cột chứa nút là cột 4 (index = 3)
                if (col == 5) {
                    tblEmployee.getTbl().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    tblEmployee.getTbl().setCursor(Cursor.getDefaultCursor());
                }
            }
        });
        headerCustom2.handleSearch(new DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                String text = headerCustom2.getSearchText();
                System.out.println("Search text in amenity search: " + text);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }


        });
        var header = tblEmployee.getTbl().getTableHeader();
        Combobox<String> cmb = new Combobox<>(new String[]{"Nam", "Nữ"});
        TableCellRenderer defaultRenderer = header.getDefaultRenderer();
        TableColumn column = tblEmployee.getTbl().getColumnModel().getColumn(2);
        column.setHeaderRenderer((tbl, value, isSelected, hasFocus, row, col) -> {
            // Dùng renderer gốc để lấy màu & nền đúng
            Component comp = defaultRenderer.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, col);

            if (comp instanceof JLabel lbl) {
                lbl.setText("Giới tính                              \u25BC");
//                lbl.setIcon(FontIcon.of(FontAwesomeSolid.ARROW_DOWN, 16, Color.DARK_GRAY));
                lbl.setHorizontalTextPosition(SwingConstants.LEFT);
                lbl.setHorizontalAlignment(SwingConstants.LEFT);
                lbl.setIconTextGap(5);
            }
            return comp;
        });
        
        tblEmployee.getTbl().addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                int col = tblEmployee.getTbl().columnAtPoint(e.getPoint());

                if (col == 5) {
                    tblEmployee.getTbl().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    tblEmployee.getTbl().setCursor(Cursor.getDefaultCursor());
                }
            }
        });

        Combobox<String> cmbChucVu = new Combobox<>(new String[]{"Nhân viên", "Quản lý"});
        TableColumn columnChucVu = tblEmployee.getTbl().getColumnModel().getColumn(3);

        columnChucVu.setHeaderRenderer((tbl, value, isSelected, hasFocus, row, col) -> {
            Component comp = defaultRenderer.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, col);
            if (comp instanceof JLabel lbl) {
                lbl.setText("Chức vụ                              \u25BC"); // ▼
                lbl.setHorizontalAlignment(SwingConstants.LEFT);
                lbl.setIconTextGap(5);
            }
            return comp;
        });


        header.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = tblEmployee.getTbl().columnAtPoint(e.getPoint());
                if (col == 3) { // Cột "Chức vụ"
                    Rectangle rect = header.getHeaderRect(col);
                    cmbChucVu.setBounds(rect);
                    cmbChucVu.setVisible(true);
                    header.add(cmbChucVu);
                    cmbChucVu.showPopup();

                    cmbChucVu.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent fe) {
                            cmbChucVu.setVisible(false);
                            header.remove(cmbChucVu);
                        }
                    });
                }
            }
        });

        cmbChucVu.addActionListener(ev -> {
            String selected = (String) cmbChucVu.getSelectedItem();
            System.out.println("Filter chức vụ: " + selected);
            header.remove(cmbChucVu);
            header.repaint();
        });


        cmb.addActionListener(ev -> {
            String selected = (String) cmb.getSelectedItem();
            System.out.println("Filter: " + selected);
            header.remove(cmb);
            header.repaint();
        });
        header.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = tblEmployee.getTbl().columnAtPoint(e.getPoint());
                if (col == 2) {
                    Rectangle rect = header.getHeaderRect(col);

//                     Thiết lập vị trí và kích thước cho combo
                    cmb.setBounds(rect);
                    cmb.setVisible(true); // hiển thị combo tại vị trí cột
                    header.add(cmb);
                    cmb.showPopup(); // mở dropdown ngay lập tức

                    // Khi mất focus, ẩn combo
                    cmb.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent fe) {
                            cmb.setVisible(false);
                            header.remove(cmb);
                        }
                    });
                }
            }
        });
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerCustom2 = new iuh.fit.se.group1.ui.component.HeaderCustom();
        button1 = new iuh.fit.se.group1.ui.component.custom.Button();
        tblEmployee = new iuh.fit.se.group1.ui.component.table.Table();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(241, 241, 241));

        button1.setText("Thêm Nhân Viên");
        button1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });

        tblEmployee.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 20, 20, 20));

        jLabel1.setBackground(new java.awt.Color(131, 131, 131));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("Danh sách nhân viên");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerCustom2, javax.swing.GroupLayout.PREFERRED_SIZE, 974, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tblEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(79, 79, 79))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerCustom2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(tblEmployee, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
                .addGap(37, 37, 37))
        );
    }// </editor-fold>//GEN-END:initComponents

    private String generateEmployeeCode() {
    int rowCount = tblEmployee.getTbl().getRowCount();
    return String.format("NV%03d", rowCount + 1); // ví dụ NV001, NV002,...
}

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed

         var modal = new InfoEmployeeModal() ;
         modal.closeModel(new ActionListener() {       
                ServiceModal modal = new ServiceModal();
  
            @Override
            public void actionPerformed(ActionEvent ae) {
                GlassPanePopup.closePopupLast();
            }

        });
         modal.saveData(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {

                modal.getLblErrolName().setForeground(Color.red);
                modal.getLblErrolEmail().setForeground(Color.red);
                modal.getLblErrolPhone().setForeground(Color.red);
                modal.getLblErrolHireDate().setForeground(Color.red);
            }
        });
        GlassPanePopup.showPopup(modal);                                        
    }//GEN-LAST:event_button1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private iuh.fit.se.group1.ui.component.custom.Button button1;
    private iuh.fit.se.group1.ui.component.HeaderCustom headerCustom2;
    private javax.swing.JLabel jLabel1;
    private iuh.fit.se.group1.ui.component.table.Table tblEmployee;
    // End of variables declaration//GEN-END:variables
}
