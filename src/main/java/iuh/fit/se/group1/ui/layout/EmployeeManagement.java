/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.layout;

import iuh.fit.se.group1.entity.Employee;
import iuh.fit.se.group1.service.AmenityService;
import iuh.fit.se.group1.service.EmployeeService;
import iuh.fit.se.group1.ui.component.custom.Combobox;
import iuh.fit.se.group1.ui.component.custom.message.Message;
import iuh.fit.se.group1.ui.component.modal.InfoEmployeeModal;
import iuh.fit.se.group1.ui.component.table.TableActionEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import iuh.fit.se.group1.util.Constants;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import raven.glasspanepopup.GlassPanePopup;

/**
 * @author VienThieu
 */
public class EmployeeManagement extends javax.swing.JPanel {
    private static final Logger log = LoggerFactory.getLogger(EmployeeManagement.class);
    private final EmployeeService employeeService;
    private int activeFilterColumn = -1;
//    private Map<String, String> employeeCitizenMap = new HashMap<>();
//    private Map<String, String> employeeEmailMap = new HashMap<>();
//    private Map<String, String> employeeHireDateMap = new HashMap<>();
//    private int employeeCounter = 0;

    public EmployeeManagement() {
        initComponents();
        custom();
        employeeService = new EmployeeService();
        loadTable(employeeService.getAllEmployees());
    }
    private void loadTable(java.util.List<Employee> employees) {
        DefaultTableModel model = (DefaultTableModel) tblEmployee.getTbl().getModel();
        model.setRowCount(0);
        for (Employee employee : employees) {
            String genderStr = employee.isGender() ? "Nữ" : "Nam";
            String roleName = employee.getAccount() != null && employee.getAccount().getRole() != null
                    ? employee.getAccount().getRole().getRoleName()
                    : "N/A";
            System.out.println("Loading employee: " + employee);
            model.addRow(new Object[]{
                    employee.getEmployeeId(),
                    employee.getFullName(),
                    genderStr,
                    roleName,
                    employee.getPhone()
            });
        }
    }
    private void custom() {
        headerCustom2.getLblTitle().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));

        headerCustom2.getLblTitle().setText(
                "<html><span style='color:white;'>Quản lý nhân viên</span>"
                        + "<span style='color:rgb(204,204,204);'> &gt; Thông tin nhân viên</span></html>"
        );
        btnAddEmployee.setBackground(new Color(108, 165, 200));
        btnAddEmployee.setForeground(Color.WHITE);
        btnAddEmployee.setBorderRadius(10);

        btnExport.setBackground(new Color(13, 200, 7));
        btnExport.setForeground(Color.WHITE);
        btnExport.setBorderRadius(10);

        btnImport.setBackground(new Color(255, 108, 3));
        btnImport.setForeground(Color.WHITE);
        btnImport.setBorderRadius(10);

        btnAddEmployee.setIcon(FontIcon.of(FontAwesomeSolid.PLUS, 17, Color.WHITE), SwingConstants.RIGHT);
        btnExport.setIcon(FontIcon.of(FontAwesomeSolid.FILE_EXPORT, 17, Color.WHITE), SwingConstants.RIGHT);
        btnImport.setIcon(FontIcon.of(FontAwesomeSolid.FILE_IMPORT, 17, Color.WHITE), SwingConstants.RIGHT);

        String cols[] = {"Mã nhân viên", "Họ tên", "Giới tính", "Chức vụ", "Số điện thoại", "Chức năng"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        tblEmployee.getTbl().setModel(model);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tblEmployee.getTbl().setRowSorter(sorter);

        tblEmployee.getTbl().setAutoCreateRowSorter(false);

        for (int i = 0; i < tblEmployee.getTbl().getColumnCount(); i++) {
            if (i != 2 && i != 3) {
                sorter.setSortable(i, false);
            }
        }
//        DefaultTableModel model = new DefaultTableModel(cols, 0);
//        tblPromotion.getTbl().setModel(model);
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                tblEmployee.getTbl().clearSelection();
            }
        });
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                // SỬA: Đổi từ tblPromotion sang tblEmployee
                DefaultTableModel model = (DefaultTableModel) tblEmployee.getTbl().getModel();
                // SỬA: Đổi tên biến và logic lấy dữ liệu Employee
                Long employeeId = (Long) model.getValueAt(row, 0);
                String fullName = (String) model.getValueAt(row, 1);
                String gender = (String) model.getValueAt(row, 2);
                String role = (String) model.getValueAt(row, 3);
                String phone = (String) model.getValueAt(row, 4);

                // SỬA: Đổi từ InfoPromotionModal sang InfoEmployeeModal
                InfoEmployeeModal modal = new InfoEmployeeModal();
                modal.getLblTitle().setText("Cập nhật nhân viên");
                modal.getBtnSave().setText("Cập nhật");

                // SỬA: Set giá trị cho Employee modal
                modal.getLblCode().setText(String.valueOf(employeeId));
                modal.getTxtName().setText(fullName);
                modal.getTxtPhone().setText(phone);
                modal.getCmbGender().setSelectedItem(gender);
                modal.getCmbPosition().setSelectedItem(role);

                modal.closeModel(ae -> GlassPanePopup.closePopupLast());
                modal.saveData(ae -> {
                    // SỬA: Đổi message
                    String title = "Xác nhận cập nhật nhân viên";
                    String message = "Bạn có chắc chắn muốn cập nhật nhân viên này không?";
                    Message.showConfirm(title, message, () -> {
                        var result = getValid(modal);
                        if (!result.valid) {
                            return;
                        }

                        // SỬA: Tạo Employee object thay vì Promotion
                        Employee employee = new Employee();
                        employee.setEmployeeId(employeeId);
                        employee.setFullName(result.fullName);
                        employee.setPhone(result.phone);
                        employee.setEmail(result.email);
                        employee.setGender(result.gender);
                        employee.setCitizenId(result.citizenId);
                        employee.setHireDate(result.hireDate);

                        // SỬA: Gọi employeeService
                        Employee entitySave = employeeService.updateEmployee(employee);

                        // SỬA: Update giá trị Employee vào table
                        String genderStr = entitySave.isGender() ? "Nữ" : "Nam";
                        String roleName = entitySave.getAccount() != null && entitySave.getAccount().getRole() != null
                                ? entitySave.getAccount().getRole().getRoleName()
                                : "N/A";

                        model.setValueAt(entitySave.getFullName(), row, 1);
                        model.setValueAt(genderStr, row, 2);
                        model.setValueAt(roleName, row, 3);
                        model.setValueAt(entitySave.getPhone(), row, 4);

                        GlassPanePopup.closePopupLast();
                    });
                });

                GlassPanePopup.showPopup(modal);
            }

            @Override
            public void onDelete(int row) {
                // SỬA: Đổi message
                String title = "Xác nhận xóa nhân viên";
                String message = "Bạn có chắc chắn muốn xóa nhân viên này không?";
                Message.showConfirm(title, message, () -> {
                    // SỬA: Đổi từ tblPromotion sang tblEmployee
                    JTable table = tblEmployee.getTbl();

                    if (table.isEditing()) {
                        table.getCellEditor().stopCellEditing();
                    }

                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    int rowDelete = table.getSelectedRow();

                    if (rowDelete >= 0) {
                        Long id = (Long) model.getValueAt(rowDelete, 0);
                        model.removeRow(rowDelete);
                        // SỬA: Gọi employeeService
                        employeeService.deleteEmployee(id);
                    }
                });
            }

            // SỬA: Thêm onView cho Employee
            @Override
            public void onView(int row) {
                DefaultTableModel model = (DefaultTableModel) tblEmployee.getTbl().getModel();
                Long employeeId = (Long) model.getValueAt(row, 0);
                String fullName = (String) model.getValueAt(row, 1);
                String gender = (String) model.getValueAt(row, 2);
                String role = (String) model.getValueAt(row, 3);
                String phone = (String) model.getValueAt(row, 4);

                InfoEmployeeModal modal = new InfoEmployeeModal();
                modal.getLblTitle().setText("Thông tin nhân viên");
                modal.getBtnSave().setText("Xong");

                modal.getLblCode().setText(String.valueOf(employeeId));
                modal.getTxtName().setText(fullName);
                modal.getTxtPhone().setText(phone);
                modal.getCmbGender().setSelectedItem(gender);
                modal.getCmbPosition().setSelectedItem(role);

                modal.getTxtName().setEditable(false);
                modal.getTxtPhone().setEditable(false);
                modal.getTxtEmail().setEditable(false);
                modal.getTxtCitizen().setEditable(false);
                modal.getTxtHireDate().setEditable(false);
                modal.getCmbGender().setEnabled(false);
                modal.getCmbPosition().setEnabled(false);

                modal.saveData(ae -> GlassPanePopup.closePopupLast());
                modal.closeModel(ae -> GlassPanePopup.closePopupLast());

                GlassPanePopup.showPopup(modal);
            }
        };


        tblEmployee.setTableActionColumn(tblEmployee.getTbl(), 5, event, true);
        tblEmployee.getTbl().getColumnModel().getColumn(0).setPreferredWidth(120);
        tblEmployee.getTbl().getColumnModel().getColumn(1).setPreferredWidth(200);
        tblEmployee.getTbl().getColumnModel().getColumn(2).setPreferredWidth(120);
        tblEmployee.getTbl().getColumnModel().getColumn(3).setPreferredWidth(120);
        tblEmployee.getTbl().getColumnModel().getColumn(4).setPreferredWidth(100);
        tblEmployee.getTbl().getColumnModel().getColumn(5).setPreferredWidth(80);

        tblEmployee.getTbl().addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                // SỬA: Đổi từ tblPromotion sang tblEmployee
                int col = tblEmployee.getTbl().columnAtPoint(e.getPoint());

                // SỬA: Đổi cột 7 thành cột 5
                if (col == 5) {
                    tblEmployee.getTbl().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    tblEmployee.getTbl().setCursor(Cursor.getDefaultCursor());
                }
            }
        });
        headerCustom2.handleSearch(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                // SỬA: Đổi từ headerCustom1 sang headerCustom2
                String text = headerCustom2.getSearchText();
                if (text.isEmpty()) {
                    // SỬA: Gọi employeeService
                    loadTable(employeeService.getAllEmployees());
                    return;
                }
                loadTable(employeeService.getEmployeeByKeyword(text));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                // SỬA: Đổi từ headerCustom1 sang headerCustom2
                String text = headerCustom2.getSearchText();
                if (text.isEmpty()) {
                    // SỬA: Gọi employeeService
                    loadTable(employeeService.getAllEmployees());
                    return;
                }
                loadTable(employeeService.getEmployeeByKeyword(text));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        var header = tblEmployee.getTbl().getTableHeader();
        Combobox<String> cmb = new Combobox<>(new String[]{"Tất cả", "Nam", "Nữ"});
        Combobox<String> cmbChucVu = new Combobox<>(new String[]{"Tất cả", "Nhân viên lễ tân", "Nhân viên quản lý"});

        TableCellRenderer defaultRenderer = header.getDefaultRenderer();

        for (int i = 0; i < tblEmployee.getTbl().getColumnCount(); i++) {
            tblEmployee.getTbl().getColumnModel().getColumn(i).setHeaderRenderer(defaultRenderer);
        }

        TableColumn colGender = tblEmployee.getTbl().getColumnModel().getColumn(2);
        colGender.setHeaderRenderer((tbl, value, isSelected, hasFocus, row, col) -> {
            Component comp = defaultRenderer.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, col);
            if (comp instanceof JLabel lbl) {
                String text = "Giới tính                           \u25BC";
                lbl.setText(text);
                lbl.setHorizontalAlignment(SwingConstants.LEFT);
            }
            return comp;
        });

        TableColumn colPosition = tblEmployee.getTbl().getColumnModel().getColumn(3);
        colPosition.setHeaderRenderer((tbl, value, isSelected, hasFocus, row, col) -> {
            Component comp = defaultRenderer.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, col);
            if (comp instanceof JLabel lbl) {
                String text = "Chức vụ                           \u25BC";
                lbl.setText(text);
                lbl.setHorizontalAlignment(SwingConstants.LEFT);
            }
            return comp;
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
                int col = header.columnAtPoint(e.getPoint());
                int oldCol = activeFilterColumn;

                header.remove(cmb);
                header.remove(cmbChucVu);

                if (col != 2 && col != 3) {
                    if (activeFilterColumn != -1) {
                        activeFilterColumn = -1;
                        header.repaint(header.getHeaderRect(oldCol));
                    }
                    return;
                }

                activeFilterColumn = col;
                Rectangle rect = header.getHeaderRect(col);

                if (col == 2) {
                    cmb.setBounds(rect);
                    header.add(cmb);
                    cmb.setVisible(true);
                    cmb.showPopup();
                } else if (col == 3) {
                    cmbChucVu.setBounds(rect);
                    header.add(cmbChucVu);
                    cmbChucVu.setVisible(true);
                    cmbChucVu.showPopup();
                }

//                Rectangle rect2 = header.getHeaderRect(2);
//                Rectangle rect3 = header.getHeaderRect(3);
//


                //// Chỉ vẽ lại khu vực 2 cột lọc
//                Rectangle repaintArea = rect2.union(rect3);
//
//                header.revalidate();
//
//// 🚫 Chỉ repaint đúng 2 cột lọc (Giới tính & Chức vụ)
//                header.repaint(header.getHeaderRect(2));
//                header.repaint(header.getHeaderRect(3));
//
//                SwingUtilities.invokeLater(() -> {
//                    Rectangle rectMaNV = header.getHeaderRect(0);
//                    header.repaint(rectMaNV);
//                });

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
        btnExport = new iuh.fit.se.group1.ui.component.custom.Button();
        btnImport = new iuh.fit.se.group1.ui.component.custom.Button();

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

        btnExport.setText("Xuất Excel");
        btnExport.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportActionPerformed(evt);
            }
        });

        btnImport.setText("Tải excel");
        btnImport.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerCustom2, javax.swing.GroupLayout.DEFAULT_SIZE, 988, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(tblEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(lblTitleEmployee)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAddEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnImport, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerCustom2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTitleEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnImport, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addComponent(tblEmployee, javax.swing.GroupLayout.DEFAULT_SIZE, 583, Short.MAX_VALUE)
                .addGap(37, 37, 37))
        );
    }// </editor-fold>//GEN-END:initComponents

//    private String generateEmployeeCode() {
//        employeeCounter++;
//        return String.format("NV%03d", employeeCounter);
//    }

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
                saveData(modal);
            }
        });

        raven.glasspanepopup.GlassPanePopup.showPopup(modal);
    }
    private void saveData(InfoEmployeeModal modal) {
        Valid result = getValid(modal);

        if (!result.valid) {
            // Validation fail - các lỗi đã được hiển thị trong getValid()
            Message.showMessage("Lỗi", "Vui lòng kiểm tra lại thông tin nhập vào!");
            return;
        }

        try {
            String position = (String) modal.getCmbPosition().getSelectedItem();
            String roleId;

            if ("Nhân viên quản lý".equals(position)) {
                roleId = "MANAGER"; // Hoặc ID tương ứng trong DB của bạn
            } else {
                roleId = "RECEPTIONIST"; // Role cho Nhân viên
            }
            Employee employee = new Employee();
            employee.setFullName(result.fullName);
            employee.setPhone(result.phone);
            employee.setEmail(result.email);
            employee.setGender(result.gender);
            employee.setCitizenId(result.citizenId);
            employee.setHireDate(result.hireDate);

            Employee employeeSave = employeeService.createEmployee(employee, roleId);

            if (employeeSave == null) {
                Message.showMessage("Lỗi", "Không thể tạo nhân viên!");
                return;
            }

            DefaultTableModel model = (DefaultTableModel) tblEmployee.getTbl().getModel();
            String genderStr = employeeSave.isGender() ? "Nữ" : "Nam";
            String roleName = employeeSave.getAccount() != null && employeeSave.getAccount().getRole() != null
                    ? employeeSave.getAccount().getRole().getRoleName()
                    : "N/A";

            model.addRow(new Object[]{
                    employeeSave.getEmployeeId(),
                    employeeSave.getFullName(),
                    genderStr,
                    roleName,
                    employeeSave.getPhone()
            });

            Message.showMessage("Thành công", "Thêm nhân viên thành công!");
            SwingUtilities.invokeLater(() -> GlassPanePopup.closePopupLast());

        } catch (Exception e) {
            log.error("Error creating employee: ", e);
            Message.showMessage("Lỗi", "Có lỗi xảy ra: " + e.getMessage());
        }
    }
    //GEN-LAST:event_btnAddEmployeeActionPerformed

    private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnExportActionPerformed
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
        sorter.setSortKeys(null);

    }
    private static Valid getValid(InfoEmployeeModal modal) {
        String name = modal.getTxtName().getText().trim();
        String phone = modal.getTxtPhone().getText().trim();
        String citizenId = modal.getTxtCitizen().getText().trim();
        String email = modal.getTxtEmail().getText().trim();
        String hireDateStr = modal.getTxtHireDate().getText().trim();
        boolean gender = modal.getCmbGender().getSelectedItem() != null
                && modal.getCmbGender().getSelectedItem().toString().equalsIgnoreCase("Nam");

        // Reset lỗi
        modal.getLblErrolName().setText("");
        modal.getLblErrolPhone().setText("");
        modal.getLblErrolCitizen().setText("");
        modal.getLblErrolEmail().setText("");
        modal.getLblErrolHireDate().setText("");

        Color red = Color.RED;
        modal.getLblErrolName().setForeground(red);
        modal.getLblErrolPhone().setForeground(red);
        modal.getLblErrolCitizen().setForeground(red);
        modal.getLblErrolEmail().setForeground(red);
        modal.getLblErrolHireDate().setForeground(red);

        boolean valid = true;

        // Tên
        if (name.isEmpty()) {
            modal.getLblErrolName().setText("Họ tên không được để trống!");
            valid = false;
        } else if (name.length() < 2) {
            modal.getLblErrolName().setText("Họ tên quá ngắn!");
            valid = false;
        }

        // Số điện thoại
        if (phone.isEmpty()) {
            modal.getLblErrolPhone().setText("Số điện thoại không được để trống!");
            valid = false;
        } else if (!phone.matches("^0\\d{9}$")) {
            modal.getLblErrolPhone().setText("Số điện thoại không hợp lệ!");
            valid = false;
        }

        // CCCD
        if (citizenId.isEmpty()) {
            modal.getLblErrolCitizen().setText("CCCD không được để trống!");
            valid = false;
        } else if (!citizenId.matches("\\d{12}")) {
            modal.getLblErrolCitizen().setText("CCCD phải có 12 chữ số!");
            valid = false;
        }

        // Email
        if (email.isEmpty()) {
            modal.getLblErrolEmail().setText("Email không được để trống!");
            valid = false;
        } else if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            modal.getLblErrolEmail().setText("Email không hợp lệ!");
            valid = false;
        }

        LocalDate hireDate = null;
        try {
            if (!hireDateStr.isEmpty()) {
                hireDate = LocalDate.parse(hireDateStr, Constants.DATE_FORMATTER);
            } else {
                hireDate = LocalDate.now();
            }
        } catch (DateTimeParseException e) {
            modal.getLblErrolHireDate().setText("Ngày không hợp lệ (dd/MM/yyyy)!");
            valid = false;
            log.error("Error parsing hire date: ", e);
        }

        return new Valid(name, valid, gender, phone, citizenId, email, hireDate);
    }
    private record Valid(
            String fullName,      // SỬA: name -> fullName
            boolean valid,
            boolean gender,       // SỬA: thêm gender
            String phone,         // SỬA: thêm phone
            String citizenId,     // SỬA: thêm citizenId
            String email,         // SỬA: thêm email
            LocalDate hireDate    // SỬA: thêm hireDate
            // SỬA: Xóa các field của Promotion: discountPrice, discountPercent, description, startDate, endDate
    ) {
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private iuh.fit.se.group1.ui.component.custom.Button btnAddEmployee;
    private iuh.fit.se.group1.ui.component.custom.Button btnExport;
    private iuh.fit.se.group1.ui.component.custom.Button btnImport;
    private iuh.fit.se.group1.ui.component.HeaderCustom headerCustom2;
    private javax.swing.JLabel lblTitleEmployee;
    private iuh.fit.se.group1.ui.component.table.Table tblEmployee;
    // End of variables declaration//GEN-END:variables
}
