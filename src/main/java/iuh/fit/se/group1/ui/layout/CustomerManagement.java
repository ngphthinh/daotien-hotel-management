/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.layout;

import iuh.fit.se.group1.ui.component.custom.Combobox;
import iuh.fit.se.group1.ui.component.modal.InfoCustomerModal;
import iuh.fit.se.group1.ui.component.table.TableActionEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;
import raven.glasspanepopup.GlassPanePopup;

/**
 *
 * @author Windows
 */
public class CustomerManagement extends javax.swing.JPanel {

    private int customerCounter = 0;

    public CustomerManagement() {
        initComponents();
        btnAddCustomer.setBackground(new Color(108, 165, 200));
        btnAddCustomer.setForeground(Color.WHITE);
        btnAddCustomer.setBorderRadius(40);
        btnAddCustomer.setIcon(FontIcon.of(FontAwesomeSolid.PLUS, 17, Color.WHITE), SwingConstants.RIGHT);
        headerCustom1.getjLabel1().setText(
                "<html><span style='color:white;'>Quản lý khách hàng</span>");
        headerCustom1.getjLabel1().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));

        String cols[] = {"Mã khách hàng", "Họ tên", "Giới tính", "Email", "Số CCCD", "Số điện thoại", "Địa chỉ", "Ngày sinh", "Chức năng"};

        DefaultTableModel model = new DefaultTableModel(cols, 0);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tblCustomer.getTbl().setRowSorter(sorter);
        tblCustomer.getTbl().setModel(model);
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                DefaultTableModel model = (DefaultTableModel) tblCustomer.getTbl().getModel();

                String code = model.getValueAt(row, 0).toString();
                String name = model.getValueAt(row, 1).toString();
                String gender = model.getValueAt(row, 2).toString();
                String email = model.getValueAt(row, 3).toString();
                String citizen = model.getValueAt(row, 4).toString();
                String phone = model.getValueAt(row, 5).toString();
                String address = model.getValueAt(row, 6).toString();
                String dob = model.getValueAt(row, 7).toString();

                InfoCustomerModal modal = new InfoCustomerModal();
                modal.getTxtName().setText(name);
                modal.getCmbGender().setSelectedItem(gender);
                modal.getTxtEmail().setText(email);
                modal.getTxtCitizen().setText(citizen);
                modal.getTxtPhone().setText(phone);
                modal.getTxtAddress().setText(address);
                modal.getTxtDob().setText(dob);

                modal.saveData(ae -> {
                    String newName = modal.getTxtName().getText().trim();
                    String newGender = (String) modal.getCmbGender().getSelectedItem();
                    String newEmail = modal.getTxtEmail().getText().trim();
                    String newCitizen = modal.getTxtCitizen().getText().trim();
                    String newPhone = modal.getTxtPhone().getText().trim();
                    String newAddress = modal.getTxtAddress().getText().trim();
                    String newDob = modal.getTxtDob().getText().trim();

                    model.setValueAt(newName, row, 1);
                    model.setValueAt(newGender, row, 2);
                    model.setValueAt(newEmail, row, 3);
                    model.setValueAt(newCitizen, row, 4);
                    model.setValueAt(newPhone, row, 5);
                    model.setValueAt(newAddress, row, 6);
                    model.setValueAt(newDob, row, 7);

                    GlassPanePopup.closePopupLast(); 
                });

                modal.closeModel(ae -> GlassPanePopup.closePopupLast());
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
                DefaultTableModel model = (DefaultTableModel) tblCustomer.getTbl().getModel();
                String code = model.getValueAt(row, 0).toString();
                String name = model.getValueAt(row, 1).toString();
                String gender = model.getValueAt(row, 2).toString();
                String email = model.getValueAt(row, 3).toString();
                String citizen = model.getValueAt(row, 4).toString();
                String phone = model.getValueAt(row, 5).toString();
                String address = model.getValueAt(row, 6).toString();
                String dob = model.getValueAt(row, 7).toString();

                InfoCustomerModal modal = new InfoCustomerModal();
                modal.getBtnSave().setText("Cập nhật");
                modal.getTxtName().setText(name);
                modal.getCmbGender().setSelectedItem(gender);
                modal.getTxtEmail().setText(email);
                modal.getTxtCitizen().setText(citizen);
                modal.getTxtPhone().setText(phone);
                modal.getTxtAddress().setText(address);
                modal.getTxtDob().setText(dob);

                modal.getLblErrolName().setText("");
                modal.getLblErrolPhone().setText("");
                modal.getLblErrolEmail().setText("");
                modal.getLblErrolCitizen().setText("");
                modal.getLblErrolAddress().setText("");

                Color red = Color.RED;
                modal.getLblErrolName().setForeground(red);
                modal.getLblErrolPhone().setForeground(red);
                modal.getLblErrolEmail().setForeground(red);
                modal.getLblErrolCitizen().setForeground(red);
                modal.getLblErrolAddress().setForeground(red);

                modal.saveData(ae -> {
                    String newName = modal.getTxtName().getText().trim();
                    String newPhone = modal.getTxtPhone().getText().trim();
                    String newEmail = modal.getTxtEmail().getText().trim();
                    String newCitizen = modal.getTxtCitizen().getText().trim();
                    String newAddress = modal.getTxtAddress().getText().trim();
                    String newGender = (String) modal.getCmbGender().getSelectedItem();
                    String newDob = modal.getTxtDob().getText().trim();

                    boolean isValid = true;

                    if (newName.isEmpty()) {
                        modal.getLblErrolName().setText("Vui lòng nhập họ tên!");
                        isValid = false;
                    }

                    if (newPhone.isEmpty()) {
                        modal.getLblErrolPhone().setText("Vui lòng nhập số điện thoại!");
                        isValid = false;
                    } else if (!newPhone.matches("^(0[0-9]{9})$")) {
                        modal.getLblErrolPhone().setText("Số điện thoại không hợp lệ (10 chữ số, bắt đầu bằng 0)!");
                        isValid = false;
                    }

                    if (newEmail.isEmpty()) {
                        modal.getLblErrolEmail().setText("Vui lòng nhập email!");
                        isValid = false;
                    } else if (!newEmail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                        modal.getLblErrolEmail().setText("Email không hợp lệ!");
                        isValid = false;
                    }

                    if (newCitizen.isEmpty()) {
                        modal.getLblErrolCitizen().setText("Vui lòng nhập số CCCD/CMND!");
                        isValid = false;
                    } else if (!newCitizen.matches("^[0-9]{12}$")) {
                        modal.getLblErrolCitizen().setText("CCCD/CMND phải gồm 12 chữ số!");
                        isValid = false;
                    }

                    if (newAddress.isEmpty()) {
                        modal.getLblErrolAddress().setText("Vui lòng nhập địa chỉ");
                        isValid = false;
                    }

                    if (!isValid) {
                        return;
                    }

                    model.setValueAt(newName, row, 1);
                    model.setValueAt(newGender, row, 2);
                    model.setValueAt(newEmail, row, 3);
                    model.setValueAt(newCitizen, row, 4);
                    model.setValueAt(newPhone, row, 5);
                    model.setValueAt(newAddress, row, 6);
                    model.setValueAt(newDob, row, 7);

                    GlassPanePopup.closePopupLast();
                });

                modal.closeModel(ae -> GlassPanePopup.closePopupLast());
                GlassPanePopup.showPopup(modal);
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
                DefaultTableModel model = (DefaultTableModel) tblCustomer.getTbl().getModel();
                String code = model.getValueAt(row, 0).toString();
                String name = model.getValueAt(row, 1).toString();
                String gender = model.getValueAt(row, 2).toString();
                String email = model.getValueAt(row, 3).toString();
                String citizen = model.getValueAt(row, 4).toString();
                String phone = model.getValueAt(row, 5).toString();
                String address = model.getValueAt(row, 6).toString();
                String dob = model.getValueAt(row, 7).toString();

                InfoCustomerModal modal = new InfoCustomerModal();
                modal.getBtnSave().setText("Xong");
                modal.getTxtName().setText(name);
                modal.getCmbGender().setSelectedItem(gender);
                modal.getTxtEmail().setText(email);
                modal.getTxtCitizen().setText(citizen);
                modal.getTxtPhone().setText(phone);
                modal.getTxtAddress().setText(address);
                modal.getTxtDob().setText(dob);

                modal.getTxtName().setEditable(false);
                modal.getTxtPhone().setEditable(false);
                modal.getTxtEmail().setEditable(false);
                modal.getTxtCitizen().setEditable(false);
                modal.getCmbGender().setEnabled(false);
                modal.getTxtEmail().setEnabled(false);
                modal.getTxtDob().setEditable(false);
                modal.getTxtAddress().setEditable(false);

                modal.saveData(ae -> GlassPanePopup.closePopupLast());
                modal.closeModel(ae -> GlassPanePopup.closePopupLast());

                GlassPanePopup.showPopup(modal);
            }
        }, true);

        tblCustomer.getTbl().getColumnModel().getColumn(0).setPreferredWidth(100);  
        tblCustomer.getTbl().getColumnModel().getColumn(1).setPreferredWidth(150);
        tblCustomer.getTbl().getColumnModel().getColumn(2).setPreferredWidth(180);
        tblCustomer.getTbl().getColumnModel().getColumn(3).setPreferredWidth(180);
        tblCustomer.getTbl().getColumnModel().getColumn(4).setPreferredWidth(100);
        tblCustomer.getTbl().getColumnModel().getColumn(5).setPreferredWidth(90);
        tblCustomer.getTbl().getColumnModel().getColumn(6).setPreferredWidth(190);
        tblCustomer.getTbl().getColumnModel().getColumn(7).setPreferredWidth(80);
        tblCustomer.getTbl().getColumnModel().getColumn(8).setPreferredWidth(100);

        var header = tblCustomer.getTbl().getTableHeader();
        Combobox<String> cmbGender = new Combobox<>(new String[]{"Tất cả", "Nam", "Nữ"});

        TableCellRenderer defaultRenderer = header.getDefaultRenderer();
        TableColumn column = tblCustomer.getTbl().getColumnModel().getColumn(2);
        column.setHeaderRenderer((tbl, value, isSelected, hasFocus, row, col) -> {
            Component comp = defaultRenderer.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, col);

            if (comp instanceof JLabel lbl) {
                lbl.setText("Giới tính                             \u25BC");
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

                if (col == 8) {
                    tblCustomer.getTbl().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    tblCustomer.getTbl().setCursor(Cursor.getDefaultCursor());
                }
            }
        });

        cmbGender.addActionListener(ev -> {
            String selected = (String) cmbGender.getSelectedItem();
            filterCustomerTable(selected);
            header.remove(cmbGender);
            header.repaint();
        });

        header.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = tblCustomer.getTbl().columnAtPoint(e.getPoint());
                if (col == 2) { 
                    Rectangle rect = header.getHeaderRect(col);

                    cmbGender.setBounds(rect);
                    cmbGender.setVisible(true);
                    header.add(cmbGender);
                    cmbGender.showPopup();

                    cmbGender.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent fe) {
                            cmbGender.setVisible(false);
                            header.remove(cmbGender);
                        }
                    });
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerCustom1 = new iuh.fit.se.group1.ui.component.HeaderCustom();
        lblTitleCustomer = new javax.swing.JLabel();
        btnAddCustomer = new iuh.fit.se.group1.ui.component.custom.Button();
        tblCustomer = new iuh.fit.se.group1.ui.component.table.Table();

        setBackground(new java.awt.Color(241, 241, 241));

        lblTitleCustomer.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        lblTitleCustomer.setForeground(new java.awt.Color(102, 102, 102));
        lblTitleCustomer.setText("Quản lý khách hàng");

        btnAddCustomer.setText("Thêm khách hàng");
        btnAddCustomer.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAddCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddCustomerActionPerformed(evt);
            }
        });

        tblCustomer.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 0, 0, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerCustom1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1212, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(lblTitleCustomer)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAddCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(87, 87, 87))
            .addComponent(tblCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerCustom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTitleCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(tblCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 663, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    private String generateEmployeeCode() {
        customerCounter++;
        return String.format("KH%03d", customerCounter);
    }

    private void filterCustomerTable(String genderFilter) {
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) tblCustomer.getTbl().getRowSorter();
        sorter.setRowFilter(new RowFilter<DefaultTableModel, Object>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                String gender = entry.getStringValue(2);
                return genderFilter.equals("Tất cả") || gender.equals(genderFilter);
            }
        });
    }

    private void btnAddCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddCustomerActionPerformed
        InfoCustomerModal modal = new InfoCustomerModal();

        modal.closeModel(ae -> GlassPanePopup.closePopupLast());

        modal.saveData(ae -> {
            String name = modal.getTxtName().getText().trim();
            String phone = modal.getTxtPhone().getText().trim();
            String email = modal.getTxtEmail().getText().trim();
            String citizen = modal.getTxtCitizen().getText().trim();
            String address = modal.getTxtAddress().getText().trim();
            String gender = (String) modal.getCmbGender().getSelectedItem();
            String dob = modal.getTxtDob().getText().trim();

            boolean isValid = true;

            if (name.isEmpty()) {
                modal.getLblErrolName().setText("Vui lòng nhập họ tên!");
                isValid = false;
            }

            if (phone.isEmpty()) {
                modal.getLblErrolPhone().setText("Vui lòng nhập số điện thoại!");
                isValid = false;
            } else if (!phone.matches("^(0[0-9]{9})$")) {
                modal.getLblErrolPhone().setText("Số điện thoại không hợp lệ (10 chữ số, bắt đầu bằng 0)!");
                isValid = false;
            }

            if (email.isEmpty()) {
                modal.getLblErrolEmail().setText("Vui lòng nhập email!");
                isValid = false;
            } else if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                modal.getLblErrolEmail().setText("Email không hợp lệ!");
                isValid = false;
            }

            if (citizen.isEmpty()) {
                modal.getLblErrolCitizen().setText("Vui lòng nhập số CCCD/CMND!");
                isValid = false;
            } else if (!citizen.matches("^[0-9]{12}$")) {
                modal.getLblErrolCitizen().setText("CCCD/CMND phải gồm 12 chữ số!");
                isValid = false;
            }

            if (address.isEmpty()) {
                modal.getLblErrolAddress().setText("Vui lòng nhập địa chỉ");
                isValid = false;
            }

            if (!isValid) {
                return;
            }

            modal.getLblErrolName().setText("");
            modal.getLblErrolPhone().setText("");
            modal.getLblErrolEmail().setText("");
            modal.getLblErrolCitizen().setText("");
            modal.getLblErrolAddress().setText("");

            Color red = Color.RED;
            modal.getLblErrolName().setForeground(red);
            modal.getLblErrolPhone().setForeground(red);
            modal.getLblErrolEmail().setForeground(red);
            modal.getLblErrolCitizen().setForeground(red);
            modal.getLblErrolAddress().setForeground(red);

            DefaultTableModel model = (DefaultTableModel) tblCustomer.getTbl().getModel();
            String code = generateEmployeeCode();
            model.addRow(new Object[]{code, name, gender, email, citizen, phone, address, dob, ""});

            GlassPanePopup.closePopupLast();
        });

        GlassPanePopup.showPopup(modal);
    }//GEN-LAST:event_btnAddCustomerActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private iuh.fit.se.group1.ui.component.custom.Button btnAddCustomer;
    private iuh.fit.se.group1.ui.component.HeaderCustom headerCustom1;
    private javax.swing.JLabel lblTitleCustomer;
    private iuh.fit.se.group1.ui.component.table.Table tblCustomer;
    // End of variables declaration//GEN-END:variables
}
