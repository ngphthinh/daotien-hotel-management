/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.layout;

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
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;
import raven.glasspanepopup.GlassPanePopup;

/**
 * @author VienThieu
 */
public class EmployeeManagement extends javax.swing.JPanel {

    private Map<String, String> employeeCitizenMap = new HashMap<>();
    private Map<String, String> employeeEmailMap = new HashMap<>();
    private Map<String, String> employeeHireDateMap = new HashMap<>();
    private int employeeCounter = 0;

    public EmployeeManagement() {
        initComponents();
        headerCustom2.getjLabel1().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));

        headerCustom2.getjLabel1().setText(
                "<html><span style='color:white;'>Quản lý nhân viên</span>"
                + "<span style='color:rgb(204,204,204);'> &gt; Thông tin nhân viên</span></html>"
        );
        btnAddEmployee.setBackground(new Color(108, 165, 200));
        btnAddEmployee.setForeground(Color.WHITE);
        btnAddEmployee.setBorderRadius(40);

        btnAddEmployee.setIcon(FontIcon.of(FontAwesomeSolid.PLUS, 17, Color.WHITE), SwingConstants.RIGHT);
        String cols[] = {"Mã nhân viên", "Họ tên", "Giới tính", "Chức vụ", "Số điện thoại", "Chức năng"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tblEmployee.getTbl().setRowSorter(sorter);

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
                DefaultTableModel model = (DefaultTableModel) tblEmployee.getTbl().getModel();

                String maNV = (String) model.getValueAt(row, 0);
                String ten = (String) model.getValueAt(row, 1);
                String gioiTinh = (String) model.getValueAt(row, 2);
                String chucVu = (String) model.getValueAt(row, 3);
                String sdt = (String) model.getValueAt(row, 4);
                String citizen = employeeCitizenMap.getOrDefault(maNV, "");
                String email = employeeEmailMap.getOrDefault(maNV, "");
                String hireDate = employeeHireDateMap.getOrDefault(maNV, "");

                InfoEmployeeModal modal = new InfoEmployeeModal();

                modal.getLblCode().setText(maNV);
                modal.getTxtName().setText(ten);
                modal.getTxtPhone().setText(sdt);
                modal.getCmbGender().setSelectedItem(gioiTinh);
                modal.getCmbPosition().setSelectedItem(chucVu);
                modal.getTxtCitizen().setText(citizen);
                modal.getTxtEmail().setText(email);
                modal.getTxtHireDate().setText(hireDate);

                modal.getBtnSave().setText("Cập nhật");

                modal.closeModel(ae -> GlassPanePopup.closePopupLast());

                modal.saveData(ae -> {
                    String tenNew = modal.getTxtName().getText().trim();
                    String emailNew = modal.getTxtEmail().getText().trim();
                    String citizenNew = modal.getTxtCitizen().getText().trim();
                    String gioiTinhNew = modal.getCmbGender().getSelectedItem() != null
                            ? modal.getCmbGender().getSelectedItem().toString()
                            : "";
                    String chucVuNew = modal.getCmbPosition().getSelectedItem() != null
                            ? modal.getCmbPosition().getSelectedItem().toString()
                            : "";
                    String sdtNew = modal.getTxtPhone().getText().trim();
                    String hireDateNew = modal.getTxtHireDate().getText().trim();

                    modal.getLblErrolName().setText("");
                    modal.getLblErrolPhone().setText("");
                    modal.getLblErrolEmail().setText("");
                    modal.getLblErrolCitizen().setText("");

                    Color red = Color.RED;
                    modal.getLblErrolName().setForeground(red);
                    modal.getLblErrolPhone().setForeground(red);
                    modal.getLblErrolEmail().setForeground(red);
                    modal.getLblErrolCitizen().setForeground(red);

                    boolean isValid = true;

                    if (tenNew.isEmpty()) {
                        modal.getLblErrolName().setText("Vui lòng nhập họ tên!");
                        isValid = false;
                    }

                    if (sdtNew.isEmpty()) {
                        modal.getLblErrolPhone().setText("Vui lòng nhập số điện thoại!");
                        isValid = false;
                    } else if (!sdtNew.matches("^(0[0-9]{9})$")) {
                        modal.getLblErrolPhone().setText("Số điện thoại không hợp lệ (10 chữ số, bắt đầu bằng 0)!");
                        isValid = false;
                    }

                    if (emailNew.isEmpty()) {
                        modal.getLblErrolEmail().setText("Vui lòng nhập email!");
                        isValid = false;
                    } else if (!emailNew.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                        modal.getLblErrolEmail().setText("Email không hợp lệ!");
                        isValid = false;
                    }

                    if (citizenNew.isEmpty()) {
                        modal.getLblErrolCitizen().setText("Vui lòng nhập số CCCD/CMND!");
                        isValid = false;
                    } else if (!citizenNew.matches("^[0-9]{12}$")) {
                        modal.getLblErrolCitizen().setText("CCCD/CMND phải gồm 12 chữ số!");
                        isValid = false;
                    }

                    if (!isValid) {
                        return;
                    }

                    model.setValueAt(tenNew, row, 1);
                    model.setValueAt(gioiTinhNew, row, 2);
                    model.setValueAt(chucVuNew, row, 3);
                    model.setValueAt(sdtNew, row, 4);

                    employeeCitizenMap.put(maNV, citizenNew);
                    employeeEmailMap.put(maNV, emailNew);
                    employeeHireDateMap.put(maNV, hireDateNew);

                    GlassPanePopup.closePopupLast();
                });

                GlassPanePopup.showPopup(modal);
            }

            @Override
            public void onDelete(int row) {
                if (tblEmployee.getTbl().isEditing()) {
                    tblEmployee.getTbl().getCellEditor().stopCellEditing();
                }
                DefaultTableModel model = (DefaultTableModel) tblEmployee.getTbl().getModel();
                String maNV = (String) model.getValueAt(row, 0);
                employeeCitizenMap.remove(maNV);
                model.removeRow(row);
            }

            @Override
            public void onView(int row) {
                DefaultTableModel model = (DefaultTableModel) tblEmployee.getTbl().getModel();
                String maNV = (String) model.getValueAt(row, 0);
                String ten = (String) model.getValueAt(row, 1);
                String gioiTinh = (String) model.getValueAt(row, 2);
                String chucVu = (String) model.getValueAt(row, 3);
                String sdt = (String) model.getValueAt(row, 4);
                String citizen = employeeCitizenMap.getOrDefault(maNV, "");
                String email = employeeEmailMap.getOrDefault(maNV, "");
                String hireDate = employeeHireDateMap.getOrDefault(maNV, "");

                InfoEmployeeModal modal = new InfoEmployeeModal();
                modal.getBtnSave().setText("Xong");

                modal.getLblCode().setText(maNV);
                modal.getTxtName().setText(ten);
                modal.getTxtPhone().setText(sdt);
                modal.getCmbGender().setSelectedItem(gioiTinh);
                modal.getCmbPosition().setSelectedItem(chucVu);
                modal.getTxtCitizen().setText(citizen);
                modal.getTxtEmail().setText(email);
                modal.getTxtHireDate().setText(hireDate);

                modal.getTxtName().setEditable(false);
                modal.getTxtPhone().setEditable(false);
                modal.getTxtEmail().setEditable(false);
                modal.getTxtCitizen().setEditable(false);
                modal.getCmbGender().setEnabled(false);
                modal.getCmbPosition().setEnabled(false);
                modal.getTxtEmail().setEnabled(false);
                modal.getTxtHireDate().setEditable(false);

                modal.saveData(ae -> GlassPanePopup.closePopupLast());
                modal.closeModel(ae -> GlassPanePopup.closePopupLast());

                GlassPanePopup.showPopup(modal);
            }
        }, true);

        tblEmployee.getTbl().getColumnModel().getColumn(0).setPreferredWidth(120);
        tblEmployee.getTbl().getColumnModel().getColumn(1).setPreferredWidth(200);
        tblEmployee.getTbl().getColumnModel().getColumn(2).setPreferredWidth(120);
        tblEmployee.getTbl().getColumnModel().getColumn(3).setPreferredWidth(120);
        tblEmployee.getTbl().getColumnModel().getColumn(4).setPreferredWidth(100);
        tblEmployee.getTbl().getColumnModel().getColumn(5).setPreferredWidth(80);

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
        Combobox<String> cmb = new Combobox<>(new String[]{"Tất cả", "Nam", "Nữ"});
        TableCellRenderer defaultRenderer = header.getDefaultRenderer();
        TableColumn column = tblEmployee.getTbl().getColumnModel().getColumn(2);
        column.setHeaderRenderer((tbl, value, isSelected, hasFocus, row, col) -> {
            Component comp = defaultRenderer.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, col);

            if (comp instanceof JLabel lbl) {
                lbl.setText("Giới tính                           \u25BC");
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

        Combobox<String> cmbChucVu = new Combobox<>(new String[]{"Tất cả", "Nhân viên", "Quản lí"});
        TableColumn columnChucVu = tblEmployee.getTbl().getColumnModel().getColumn(3);

        columnChucVu.setHeaderRenderer((tbl, value, isSelected, hasFocus, row, col) -> {
            Component comp = defaultRenderer.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, col);
            if (comp instanceof JLabel lbl) {
                lbl.setText("Chức vụ                           \u25BC"); // ▼
                lbl.setHorizontalAlignment(SwingConstants.LEFT);
                lbl.setIconTextGap(5);
            }
            return comp;
        });

        header.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = tblEmployee.getTbl().columnAtPoint(e.getPoint());
                if (col == 3) {
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
            String selectedPosition = (String) cmbChucVu.getSelectedItem();
            String selectedGender = (String) cmb.getSelectedItem();
            filterTable(selectedGender, selectedPosition);
            header.remove(cmbChucVu);
            header.repaint();
        });

        cmb.addActionListener(ev -> {
            String selectedGender = (String) cmb.getSelectedItem();
            String selectedPosition = (String) cmbChucVu.getSelectedItem();
            filterTable(selectedGender, selectedPosition);
            header.remove(cmb);
            header.repaint();
        });

        header.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = tblEmployee.getTbl().columnAtPoint(e.getPoint());
                if (col == 2) {
                    Rectangle rect = header.getHeaderRect(col);

                    cmb.setBounds(rect);
                    cmb.setVisible(true);
                    header.add(cmb);
                    cmb.showPopup();

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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerCustom2 = new iuh.fit.se.group1.ui.component.HeaderCustom();
        btnAddEmployee = new iuh.fit.se.group1.ui.component.custom.Button();
        tblEmployee = new iuh.fit.se.group1.ui.component.table.Table();
        lblTitleEmployee = new javax.swing.JLabel();

        setBackground(new java.awt.Color(241, 241, 241));

        btnAddEmployee.setText("Thêm Nhân Viên");
        btnAddEmployee.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAddEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddEmployeeActionPerformed(evt);
            }
        });

        tblEmployee.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 20, 20, 20));

        lblTitleEmployee.setBackground(new java.awt.Color(131, 131, 131));
        lblTitleEmployee.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        lblTitleEmployee.setForeground(new java.awt.Color(102, 102, 102));
        lblTitleEmployee.setText("Danh sách nhân viên");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerCustom2, javax.swing.GroupLayout.DEFAULT_SIZE, 974, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tblEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(lblTitleEmployee)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAddEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(79, 79, 79))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerCustom2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTitleEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(tblEmployee, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
                .addGap(37, 37, 37))
        );
    }// </editor-fold>//GEN-END:initComponents

    private String generateEmployeeCode() {
        employeeCounter++;
        return String.format("NV%03d", employeeCounter);
    }

    private void btnAddEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddEmployeeActionPerformed

        InfoEmployeeModal modal = new InfoEmployeeModal();

        modal.closeModel(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                GlassPanePopup.closePopupLast();
            }
        });

        modal.saveData(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String maNV = generateEmployeeCode();
                String ten = modal.getTxtName().getText().trim();
                String email = modal.getTxtEmail().getText().trim();
                String citizen = modal.getTxtCitizen().getText().trim();
                String gioiTinh = modal.getCmbGender().getSelectedItem() != null
                        ? modal.getCmbGender().getSelectedItem().toString()
                        : "";
                String chucVu = modal.getCmbPosition().getSelectedItem() != null
                        ? modal.getCmbPosition().getSelectedItem().toString()
                        : "";
                String sdt = modal.getTxtPhone().getText().trim();
                String hireDate = modal.getTxtHireDate().getText().trim();

                modal.getLblErrolName().setText("");
                modal.getLblErrolPhone().setText("");
                modal.getLblErrolEmail().setText("");
                modal.getLblErrolCitizen().setText("");

                Color red = Color.RED;
                modal.getLblErrolName().setForeground(red);
                modal.getLblErrolPhone().setForeground(red);
                modal.getLblErrolEmail().setForeground(red);
                modal.getLblErrolCitizen().setForeground(red);

                boolean isValid = true;

                if (ten.isEmpty()) {
                    modal.getLblErrolName().setText("Vui lòng nhập họ tên!");
                    isValid = false;
                }

                if (sdt.isEmpty()) {
                    modal.getLblErrolPhone().setText("Vui lòng nhập số điện thoại!");
                    isValid = false;
                } else if (!sdt.matches("^(0[0-9]{9})$")) {
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
                if (!isValid) {
                    return;
                }

                DefaultTableModel model = (DefaultTableModel) tblEmployee.getTbl().getModel();
                model.addRow(new Object[]{maNV, ten, gioiTinh, chucVu, sdt, ""});
                employeeCitizenMap.put(maNV, citizen);
                employeeEmailMap.put(maNV, email);
                employeeHireDateMap.put(maNV, hireDate);

                GlassPanePopup.closePopupLast();
            }
        });

        GlassPanePopup.showPopup(modal);
    }//GEN-LAST:event_btnAddEmployeeActionPerformed
    private void filterTable(String genderFilter, String positionFilter) {
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) tblEmployee.getTbl().getRowSorter();

        RowFilter<DefaultTableModel, Object> rf = new RowFilter() {
            @Override
            public boolean include(RowFilter.Entry entry) {
                String gender = entry.getStringValue(2);
                String position = entry.getStringValue(3);

                boolean genderMatches = genderFilter == null || genderFilter.equals("Tất cả") || gender.equals(genderFilter);
                boolean positionMatches = positionFilter == null || positionFilter.equals("Tất cả") || position.equals(positionFilter);

                return genderMatches && positionMatches;
            }

        };
        sorter.setRowFilter(rf);
        sorter.setSortKeys(java.util.List.of(new javax.swing.RowSorter.SortKey(0, javax.swing.SortOrder.ASCENDING)));
        sorter.sort();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private iuh.fit.se.group1.ui.component.custom.Button btnAddEmployee;
    private iuh.fit.se.group1.ui.component.HeaderCustom headerCustom2;
    private javax.swing.JLabel lblTitleEmployee;
    private iuh.fit.se.group1.ui.component.table.Table tblEmployee;
    // End of variables declaration//GEN-END:variables
}
