/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.layout;

import iuh.fit.se.group1.ui.component.modal.ServiceModal;
import iuh.fit.se.group1.ui.component.custom.Combobox;
import iuh.fit.se.group1.ui.component.modal.InfoCustomerModal;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;
import raven.glasspanepopup.GlassPanePopup;

/**
 *
 * @author Windows
 */
public class CustomerManagement extends javax.swing.JPanel {

    /**
     * Creates new form CustomerManagement
     */
    public CustomerManagement() {
        initComponents();
        button1.setBackground(new Color(108, 165, 200));
        button1.setForeground(Color.WHITE);
        button1.setBorderRadius(40);
        button1.setIcon(FontIcon.of(FontAwesomeSolid.PLUS, 17, Color.WHITE), SwingConstants.RIGHT);
        headerCustom1.getjLabel1().setText(
        "<html><span style='color:white;'>Quản lý khách hàng</span>");
        headerCustom1.getjLabel1().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));
        
         String cols[] = {"Mã khách hàng", "Họ tên", "Số điện thoại", "Email","Số CCCD","Giới tính","Địa chỉ","Ngày sinh","Chức năng"};
        DefaultTableModel model = new DefaultTableModel(cols, 10);
        tblCustomer.getTbl().setModel(model);
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                System.out.println("Edit row: " + row);
            }

            @Override
            public void onDelete(int row) {
                if (tblCustomer.getTbl().isEditing()) {
                    tblCustomer.getTbl().getCellEditor().stopCellEditing();
                }
                DefaultTableModel model = (DefaultTableModel) tblCustomer.getTbl().getModel();
                model.removeRow(row);
            }
        };
        tblCustomer.setTableActionColumn(tblCustomer.getTbl(), 8, new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                System.out.println("Edit row " + row);
            }

            @Override
            public void onDelete(int row) {
                if (tblCustomer.getTbl().isEditing()) {
                    tblCustomer.getTbl().getCellEditor().stopCellEditing();
                }
                DefaultTableModel model = (DefaultTableModel) tblCustomer.getTbl().getModel();
                model.removeRow(row);
            }

            @Override
            public void onView(int row) {
                System.out.println("View row " + row);
            }
        }, true);

        tblCustomer.getTbl().getColumnModel().getColumn(0).setPreferredWidth(100);  // chiều rộng mong muốn
        tblCustomer.getTbl().getColumnModel().getColumn(1).setPreferredWidth(150);
        tblCustomer.getTbl().getColumnModel().getColumn(2).setPreferredWidth(100);
        tblCustomer.getTbl().getColumnModel().getColumn(3).setPreferredWidth(120);
        tblCustomer.getTbl().getColumnModel().getColumn(4).setPreferredWidth(100);
        tblCustomer.getTbl().getColumnModel().getColumn(5).setPreferredWidth(90);
        tblCustomer.getTbl().getColumnModel().getColumn(6).setPreferredWidth(120);
        tblCustomer.getTbl().getColumnModel().getColumn(7).setPreferredWidth(100);
        tblCustomer.getTbl().getColumnModel().getColumn(8).setPreferredWidth(100);
        
        var header = tblCustomer.getTbl().getTableHeader();
        Combobox<String> cmb = new Combobox<>(new String[]{"Nam", "Nữ"});
        TableCellRenderer defaultRenderer = header.getDefaultRenderer();
        TableColumn column = tblCustomer.getTbl().getColumnModel().getColumn(2);
        column.setHeaderRenderer((tbl, value, isSelected, hasFocus, row, col) -> {
            // Dùng renderer gốc để lấy màu & nền đúng
            Component comp = defaultRenderer.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, col);

            if (comp instanceof JLabel lbl) {
                lbl.setText("Giới tính        \u25BC");
//                lbl.setIcon(FontIcon.of(FontAwesomeSolid.ARROW_DOWN, 16, Color.DARK_GRAY));
                lbl.setHorizontalTextPosition(SwingConstants.LEFT);
                lbl.setHorizontalAlignment(SwingConstants.LEFT);
                lbl.setIconTextGap(5);
            }
            return comp;
        });

        tblCustomer.getTbl().addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                int col = tblCustomer.getTbl().columnAtPoint(e.getPoint());

                if (col == 4) {
                    tblCustomer.getTbl().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    tblCustomer.getTbl().setCursor(Cursor.getDefaultCursor());
                }
            }
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
                int col = tblCustomer.getTbl().columnAtPoint(e.getPoint());
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

        headerCustom1 = new iuh.fit.se.group1.ui.component.HeaderCustom();
        jLabel1 = new javax.swing.JLabel();
        button1 = new iuh.fit.se.group1.ui.component.custom.Button();
        tblCustomer = new iuh.fit.se.group1.ui.component.table.Table();

        setBackground(new java.awt.Color(241, 241, 241));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("Quản lý khách hàng");

        button1.setText("Thêm khách hàng");
        button1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });

        tblCustomer.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 20, 20, 20));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerCustom1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1212, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(87, 87, 87))
            .addComponent(tblCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerCustom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(tblCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 663, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
        var modal = new InfoCustomerModal() ;
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

                modal.getLblErrolAddress().setForeground(Color.red);
                modal.getLblErrolCitizen().setForeground(Color.red);
                modal.getLblErrolPhone().setForeground(Color.red);
                modal.getLblErrolEmail().setForeground(Color.red);
                modal.getLblErrolDob().setForeground(Color.red);
                modal.getLblErrolName().setForeground(Color.red);
            }
        });
        GlassPanePopup.showPopup(modal);                                       
    }//GEN-LAST:event_button1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private iuh.fit.se.group1.ui.component.custom.Button button1;
    private iuh.fit.se.group1.ui.component.HeaderCustom headerCustom1;
    private javax.swing.JLabel jLabel1;
    private iuh.fit.se.group1.ui.component.table.Table tblCustomer;
    // End of variables declaration//GEN-END:variables
}
