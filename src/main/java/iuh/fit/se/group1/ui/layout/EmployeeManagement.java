/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.layout;


import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.CellStyle;

import java.awt.Graphics2D;
import java.io.ByteArrayOutputStream;
import java.io.File;

import iuh.fit.se.group1.entity.Account;
import iuh.fit.se.group1.entity.Employee;
import iuh.fit.se.group1.enums.Role;
import iuh.fit.se.group1.service.EmployeeService;
import iuh.fit.se.group1.service.ExportExcelService;
import iuh.fit.se.group1.service.RoleService;
import iuh.fit.se.group1.ui.component.custom.AvatarLabel;
import iuh.fit.se.group1.ui.component.custom.Combobox;
import iuh.fit.se.group1.ui.component.custom.message.Message;
import iuh.fit.se.group1.ui.component.modal.InfoEmployeeModal;
import iuh.fit.se.group1.ui.component.shift.ShiftList;
import iuh.fit.se.group1.ui.component.table.Table;
import iuh.fit.se.group1.ui.component.table.TableActionEvent;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import iuh.fit.se.group1.util.Constants;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import raven.glasspanepopup.GlassPanePopup;

import iuh.fit.se.group1.service.ExportExcelService;
import iuh.fit.se.group1.service.ImportExcelService;

import java.io.File;
import java.util.List;

public class EmployeeManagement extends javax.swing.JPanel {

    private static final Logger log = LoggerFactory.getLogger(EmployeeManagement.class);
    private final EmployeeService employeeService;
    private final RoleService roleService;
    private int activeFilterColumn = -1;
    private ShiftList shiftList;

    public EmployeeManagement() {
        initComponents();
        custom();
        employeeService = new EmployeeService();
        roleService = new RoleService();
        loadTable(employeeService.getAllEmployees());
    }

    private void custom() {
        headerCustom2.getLblTitle().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));

        headerCustom2.getLblTitle().setText(
                "<html><span style='color:white;'>Quản lý nhân viên</span>"

                        + "<span style='color:rgb(204,204,204);'> &gt; Thông tin nhân viên</span></html>");

        btnAddEmployee.setBackground(new Color(108, 165, 200));
        btnAddEmployee.setForeground(Color.WHITE);
        btnAddEmployee.setBorderRadius(10);

        btnExport.setBackground(new Color(13, 200, 7));
        btnExport.setForeground(Color.WHITE);
        btnExport.setBorderRadius(10);

        btnImport.setBackground(new Color(255, 108, 3));
        btnImport.setForeground(Color.WHITE);
        btnImport.setBorderRadius(10);
        btnImport.addActionListener(ev -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                ImportExcelService importService = new ImportExcelService();
                List<Employee> imported = importService.importEmployeesFromExcel(file);
                if (imported != null && !imported.isEmpty()) {
                    employeeService.getAllEmployees().addAll(imported);
                    loadTable(employeeService.getAllEmployees());
                    Message.showMessage("Thành công", "Đã import " + imported.size() + " nhân viên!");
                } else {
                    Message.showMessage("Lỗi", "Không có dữ liệu nào được import!");
                }
            }
        });

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
        // DefaultTableModel model = new DefaultTableModel(cols, 0);
        // tblPromotion.getTbl().setModel(model);
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                tblEmployee.getTbl().clearSelection();
            }
        });
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                DefaultTableModel model = (DefaultTableModel) tblEmployee.getTbl().getModel();
                Long employeeId = (Long) model.getValueAt(row, 0);

                // Lấy thông tin đầy đủ từ database
                Employee employee = employeeService.getEmployeeById(employeeId);

                if (employee == null) {
                    Message.showMessage("Lỗi", "Không tìm thấy thông tin nhân viên!");
                    return;
                }

                InfoEmployeeModal modal = new InfoEmployeeModal(roleService);
                modal.getLblTitle().setText("Cập nhật nhân viên");
                modal.getBtnSave().setText("Cập nhật");

                // Set giá trị cho Employee modal
                modal.getLblCode().setText(String.valueOf(employee.getEmployeeId()));
                modal.getTxtName().setText(employee.getFullName());
                modal.getTxtPhone().setText(employee.getPhone());
                modal.getTxtEmail().setText(employee.getEmail());
                modal.getTxtCitizen().setText(employee.getCitizenId());
                modal.getTxtHireDate().setText(employee.getHireDate().format(Constants.DATE_FORMATTER));

                String genderStr = employee.isGender() ? "Nữ" : "Nam";
                modal.getCmbGender().setSelectedItem(genderStr);

                String roleName = employee.getAccount() != null && employee.getAccount().getRole() != null
                        ? employee.getAccount().getRole().getRoleName()
                        : "N/A";
                modal.getCmbPosition().setSelectedItem(roleName);

                // Load avatar hiện tại
                AvatarLabel avatarLabel = modal.getAvatarLabel();
                if (avatarLabel != null && employee.getAvt() != null && employee.getAvt().length > 0) {
                    avatarLabel.setImageFromBytes(employee.getAvt());
                }

                modal.closeModel(ae -> GlassPanePopup.closePopupLast());
                modal.saveData(ae -> {
                    String title = "Xác nhận cập nhật nhân viên";
                    String message = "Bạn có chắc chắn muốn cập nhật nhân viên này không?";
                    Message.showConfirm(title, message, () -> {
                        var result = getValid(modal);
                        if (!result.valid) {
                            return;
                        }

                        // Lấy giá trị mới từ modal
                        String genderSelected = (String) modal.getCmbGender().getSelectedItem();
                        boolean gender = "Nữ".equals(genderSelected); // true nếu là Nữ

                        String roleSelected = (String) modal.getCmbPosition().getSelectedItem();
                        String roleId = roleSelected.equalsIgnoreCase("Nhân viên quản lý")
                                ? Role.MANAGER.toString()
                                : Role.RECEPTIONIST.toString();

                        // Lấy Role entity từ database
                        iuh.fit.se.group1.entity.Role newRole = roleService.getRoleById(roleId);
                        if (newRole == null) {
                            Message.showMessage("Lỗi", "Không tìm thấy vai trò!");
                            return;
                        }

                        // Tạo đối tượng Employee mới để cập nhật
                        Employee employeeUpdate = new Employee();
                        employeeUpdate.setEmployeeId(employeeId);
                        employeeUpdate.setFullName(result.fullName);
                        employeeUpdate.setPhone(result.phone);
                        employeeUpdate.setEmail(result.email);
                        employeeUpdate.setCitizenId(result.citizenId);
                        employeeUpdate.setHireDate(result.hireDate);
                        employeeUpdate.setGender(gender); // Gán giới tính mới

                        // Cập nhật role cho account
                        if (employee.getAccount() != null) {
                            Account accountToUpdate = employee.getAccount();
                            accountToUpdate.setRole(newRole); // Gán toàn bộ đối tượng Role mới
                            employeeUpdate.setAccount(accountToUpdate);
                        } else {
                            Message.showMessage("Lỗi", "Nhân viên không có tài khoản!");
                            return;
                        }

                        // Avatar mới
                        AvatarLabel avt = modal.getAvatarLabel();
                        if (avt != null) {
                            byte[] avtBytes = avt.getImageAsBytes("jpg");
                            if (avtBytes != null && avtBytes.length > 0) {
                                employeeUpdate.setAvt(avtBytes);
                                log.info("Avatar updated for employee: {}", employeeId);
                            } else {
                                // Giữ nguyên avatar cũ nếu không có avatar mới
                                employeeUpdate.setAvt(employee.getAvt());
                            }
                        } else {
                            // Giữ nguyên avatar cũ nếu avatarLabel null
                            employeeUpdate.setAvt(employee.getAvt());
                        }

                        // Gọi service update xuống database
                        Employee entitySave = employeeService.updateEmployee(employeeUpdate);

                        // Cập nhật lại table
                        String genderStr2 = entitySave.isGender() ? "Nữ" : "Nam";
                        String roleName2 = entitySave.getAccount() != null && entitySave.getAccount().getRole() != null
                                ? entitySave.getAccount().getRole().getRoleName()
                                : "N/A";

                        model.setValueAt(entitySave.getFullName(), row, 1);
                        model.setValueAt(genderStr2, row, 2);
                        model.setValueAt(roleName2, row, 3);
                        model.setValueAt(entitySave.getPhone(), row, 4);

                        Message.showMessage("Thành công", "Cập nhật nhân viên thành công!");
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
                        if (shiftList != null) {
                            shiftList.reloadEmployees();
                            log.info("Reloaded ShiftList after deleting employee: {}", id);
                        }
                    }
                });
            }

            // SỬA: Thêm onView cho Employee
            @Override
            public void onView(int row) {
                DefaultTableModel model = (DefaultTableModel) tblEmployee.getTbl().getModel();
                Long employeeId = (Long) model.getValueAt(row, 0);

                // Lấy thông tin đầy đủ từ database
                Employee employee = employeeService.getEmployeeById(employeeId);

                if (employee == null) {
                    Message.showMessage("Lỗi", "Không tìm thấy thông tin nhân viên!");
                    return;
                }

                InfoEmployeeModal modal = new InfoEmployeeModal(roleService);
                modal.getLblTitle().setText("Thông tin nhân viên");
                modal.getBtnSave().setText("Xong");

                modal.getLblCode().setText(String.valueOf(employee.getEmployeeId()));
                modal.getTxtName().setText(employee.getFullName());
                modal.getTxtPhone().setText(employee.getPhone());
                modal.getTxtEmail().setText(employee.getEmail());
                modal.getTxtCitizen().setText(employee.getCitizenId());
                modal.getTxtHireDate().setText(employee.getHireDate().format(Constants.DATE_FORMATTER));

                String genderStr = employee.isGender() ? "Nữ" : "Nam";
                modal.getCmbGender().setSelectedItem(genderStr);

                String roleName = employee.getAccount() != null && employee.getAccount().getRole() != null
                        ? employee.getAccount().getRole().getRoleName()
                        : "N/A";
                modal.getCmbPosition().setSelectedItem(roleName);

                // Hiển thị avatar từ database
                AvatarLabel avatarLabel = modal.getAvatarLabel(); // Cần thêm getter cho AvatarLabel trong
                // InfoEmployeeModal
                if (avatarLabel != null) {
                    if (employee.getAvt() != null && employee.getAvt().length > 0) {
                        try {
                            // Convert byte array sang BufferedImage
                            ByteArrayInputStream bais = new ByteArrayInputStream(employee.getAvt());
                            BufferedImage image = ImageIO.read(bais);
                            if (image != null) {
                                avatarLabel.setImage(image);
                                log.info("Avatar loaded successfully for employee: {}", employeeId);
                            } else {
                                log.warn("Failed to read image from byte array for employee: {}", employeeId);
                            }
                        } catch (Exception e) {
                            log.error("Error loading avatar image: ", e);
                        }
                    } else {
                        // Reset về ảnh mặc định nếu không có avatar
                        log.info("No avatar found for employee: {}, using default image", employeeId);
                    }
                }
                modal.getBtnChooseImg().setVisible(false);
                // Disable các trường nhập liệu
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
            public void insertUpdate(DocumentEvent e) {
                String text = headerCustom2.getSearchText();
                if (text.isEmpty()) {
                    loadTable(employeeService.getAllEmployees());
                    return;
                }
                loadTable(employeeService.getEmployeeByKeyword(text));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = headerCustom2.getSearchText();
                if (text.isEmpty()) {
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
                String text = "Giới tính                                \u25BC";
                lbl.setText(text);
                lbl.setHorizontalAlignment(SwingConstants.LEFT);
            }
            return comp;
        });

        TableColumn colPosition = tblEmployee.getTbl().getColumnModel().getColumn(3);
        colPosition.setHeaderRenderer((tbl, value, isSelected, hasFocus, row, col) -> {
            Component comp = defaultRenderer.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, col);
            if (comp instanceof JLabel lbl) {
                String text = "Chức vụ                               \u25BC";
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
            }

        });

    }

    @SuppressWarnings("unchecked")
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
        btnExport.addActionListener(e -> exportAllEmployeesToExcel());

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
                                                .addComponent(tblEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 0,
                                                        Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(36, 36, 36)
                                                .addComponent(lblTitleEmployee)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnAddEmployee, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 148,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnImport, javax.swing.GroupLayout.PREFERRED_SIZE, 148,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(45, 45, 45)))
                                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(headerCustom2, javax.swing.GroupLayout.PREFERRED_SIZE, 75,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnAddEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 43,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblTitleEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 43,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 43,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnImport, javax.swing.GroupLayout.PREFERRED_SIZE, 43,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(25, 25, 25)
                                .addComponent(tblEmployee, javax.swing.GroupLayout.DEFAULT_SIZE, 583, Short.MAX_VALUE)
                                .addGap(37, 37, 37)));
    }

    private void loadTable(java.util.List<Employee> employees) {

        DefaultTableModel model = (DefaultTableModel) tblEmployee.getTbl().getModel();
        model.setRowCount(0);
        for (Employee employee : employees) {
            String genderStr = employee.isGender() ? "Nữ" : "Nam";
            String roleName = employee.getAccount() != null && employee.getAccount().getRole() != null
                    ? employee.getAccount().getRole().getRoleName()
                    : "N/A";
            model.addRow(new Object[]{
                    employee.getEmployeeId(),
                    employee.getFullName(),
                    genderStr,
                    roleName,
                    employee.getPhone()

            });
        }
    }

    public static void exportModalToExcel(Component parent, InfoEmployeeModal modal) {
        if (modal == null)
            return;

        // Lấy dữ liệu từ modal
        String employeeId = modal.getLblCode().getText();
        String fullName = modal.getTxtName().getText();
        String phone = modal.getTxtPhone().getText();
        String email = modal.getTxtEmail().getText();
        String citizenId = modal.getTxtCitizen().getText();
        String hireDateStr = modal.getTxtHireDate().getText();
        String genderStr = modal.getCmbGender().getSelectedItem() != null
                ? modal.getCmbGender().getSelectedItem().toString()
                : "N/A";
        String position = modal.getCmbPosition().getSelectedItem() != null
                ? modal.getCmbPosition().getSelectedItem().toString()
                : "N/A";

        LocalDate hireDate = LocalDate.now();
        try {
            hireDate = LocalDate.parse(hireDateStr, Constants.DATE_FORMATTER);
        } catch (Exception e) {
            hireDate = LocalDate.now();
        }

        // Avatar
        AvatarLabel avatarLabel = modal.getAvatarLabel();
        byte[] avatarBytes = avatarLabel != null ? avatarLabel.getImageAsBytes("png") : null;

        // Chọn file lưu
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn vị trí lưu file Excel");
        fileChooser.setSelectedFile(new java.io.File("NhanVien_" + employeeId + ".xlsx"));
        int userSelection = fileChooser.showSaveDialog(parent);
        if (userSelection != JFileChooser.APPROVE_OPTION)
            return;

        java.io.File fileToSave = fileChooser.getSelectedFile();
        String filePath = fileToSave.getAbsolutePath();
        if (!filePath.toLowerCase().endsWith(".xlsx"))
            filePath += ".xlsx";

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Nhân Viên");

            // Tạo font Times New Roman size 16
            Font font = workbook.createFont();
            font.setFontName("Times New Roman");
            font.setFontHeightInPoints((short) 16);

            // Styles
            CellStyle headerStyle = createHeaderStyle(workbook, font);
            CellStyle dataStyle = createDataStyle(workbook, font);
            CellStyle centerStyle = workbook.createCellStyle();
            centerStyle.cloneStyleFrom(dataStyle);
            centerStyle.setAlignment(HorizontalAlignment.CENTER);

            // Header
            String[] headers = {"STT", "Mã NV", "Họ tên", "Giới tính", "Chức vụ", "SĐT", "Email", "CCCD",
                    "Ngày tuyển dụng", "Avatar"};
            Row headerRow = sheet.createRow(0);
            headerRow.setHeightInPoints(30);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data
            Row row = sheet.createRow(1);
            row.setHeightInPoints(80);

            int colIndex = 0;

            // STT
            Cell sttCell = row.createCell(colIndex++);
            sttCell.setCellValue(1);
            sttCell.setCellStyle(centerStyle);

            // Các thông tin khác
            String[] data = {employeeId, fullName, genderStr, position, phone, email, citizenId,
                    hireDate.format(Constants.DATE_FORMATTER)};
            for (String d : data) {
                Cell cell = row.createCell(colIndex++);
                cell.setCellValue(d);
                cell.setCellStyle(dataStyle);
            }

            // Avatar
            if (avatarBytes != null && avatarBytes.length > 0) {
                BufferedImage bimg = ImageIO.read(new ByteArrayInputStream(avatarBytes));
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ImageIO.write(bimg, "png", bos);
                int pictureIdx = workbook.addPicture(bos.toByteArray(), Workbook.PICTURE_TYPE_PNG);
                bos.close();

                CreationHelper helper = workbook.getCreationHelper();
                Drawing<?> drawing = sheet.createDrawingPatriarch();
                ClientAnchor anchor = helper.createClientAnchor();
                anchor.setCol1(colIndex);
                anchor.setRow1(1);
                Picture pict = drawing.createPicture(anchor, pictureIdx);
                pict.resize(1.0);
            }

            // Auto-size cột
            for (int i = 0; i <= headers.length; i++) {
                sheet.autoSizeColumn(i);
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000);
            }

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }

            Message.showMessage("Thành công", "Xuất Excel thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            Message.showMessage("Lỗi", "Xuất Excel thất bại!");
        }
    }

    // Header style với font tùy chỉnh
    private static CellStyle createHeaderStyle(Workbook workbook, Font font) {
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        font.setBold(true);
        return style;
    }

    // Data style với font tùy chỉnh
    private static CellStyle createDataStyle(Workbook workbook, Font font) {
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private void btnAddEmployeeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAddEmployeeActionPerformed

        InfoEmployeeModal modal = new InfoEmployeeModal(roleService);
        modal.getLblCode().setVisible(false);
        modal.getLblStatus().setText("Hãy chọn avatar!");
        modal.getLblStatus().setForeground(Color.red);
        modal.closeModel(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                GlassPanePopup.closePopupLast();
            }

        });

        modal.saveData(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                boolean success = saveData(modal);
                if (success) {
                    GlassPanePopup.closePopupAll();
                }
            }
        });

        raven.glasspanepopup.GlassPanePopup.showPopup(modal);
    }

    private void exportAllEmployeesToExcel() {
        List<Employee> employees = employeeService.getAllEmployees();
        if (employees == null || employees.isEmpty()) {
            Message.showMessage("Thông báo", "Không có nhân viên để xuất Excel!");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn vị trí lưu file Excel");
        fileChooser.setSelectedFile(new File("DanhSachNhanVien.xlsx"));
        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
            return;

        File fileToSave = fileChooser.getSelectedFile();
        String filePath = fileToSave.getAbsolutePath();
        if (!filePath.toLowerCase().endsWith(".xlsx"))
            filePath += ".xlsx";

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Nhân Viên");

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);

            // ======= THÊM CỘT STT =======
            String[] headers = {"STT", "Mã NV", "Họ tên", "Giới tính", "Chức vụ", "SĐT", "Email", "CCCD",
                    "Ngày tuyển dụng"};

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // ======= DỮ LIỆU =======
            int rowIndex = 1;
            int stt = 1;

            for (Employee emp : employees) {
                Row row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(stt++); // STT
                row.createCell(1).setCellValue(emp.getEmployeeId());
                row.createCell(2).setCellValue(emp.getFullName());
                row.createCell(3).setCellValue(emp.isGender() ? "Nữ" : "Nam");

                String roleName = (emp.getAccount() != null && emp.getAccount().getRole() != null)
                        ? emp.getAccount().getRole().getRoleName()
                        : "N/A";
                row.createCell(4).setCellValue(roleName);
                row.createCell(5).setCellValue(emp.getPhone());
                row.createCell(6).setCellValue(emp.getEmail());
                row.createCell(7).setCellValue(emp.getCitizenId());
                row.createCell(8).setCellValue(emp.getHireDate().format(Constants.DATE_FORMATTER));

                for (int i = 0; i < headers.length; i++) {
                    row.getCell(i).setCellStyle(dataStyle);
                }
            }

            // Auto size
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }

            Message.showMessage("Thành công", "Xuất Excel toàn bộ nhân viên thành công!");
        } catch (Exception ex) {
            log.error("Lỗi xuất Excel toàn bộ nhân viên: ", ex);
            Message.showMessage("Lỗi", "Xuất Excel thất bại!");
        }
    }


    private boolean saveData(InfoEmployeeModal modal) {
        Valid result = getValid(modal);

        if (!result.valid) {
            Message.showMessage("Lỗi", result.errorMessage);
            return false;
        }

        try {
            int position = modal.getCmbPosition().getSelectedIndex();
            String roleId;
            if (position == 0) {
                roleId = Role.RECEPTIONIST.toString();
            } else {
                roleId = Role.MANAGER.toString();
            }
            Employee employee = new Employee();
            employee.setFullName(result.fullName);
            employee.setPhone(result.phone);
            employee.setEmail(result.email);
            employee.setGender(result.gender);
            employee.setCitizenId(result.citizenId);
            employee.setHireDate(result.hireDate);
            AvatarLabel avatarLabel = modal.getAvatarLabel();
            if (avatarLabel != null) {
                byte[] avtBytes = avatarLabel.getImageAsBytes("jpg");
                if (avtBytes != null && avtBytes.length > 0) {
                    employee.setAvt(avtBytes);
                    log.info("Avatar set for new employee, size: {} bytes", avtBytes.length);
                } else {
                    log.warn("No avatar data from AvatarLabel");
                }
            } else {
                log.warn("AvatarLabel is null in modal");
            }
            Employee employeeSave = employeeService.createEmployee(employee, roleId);

            if (employeeSave == null) {
                Message.showMessage("Lỗi", "Không thể tạo nhân viên!");
                return false;
            }

            DefaultTableModel model = (DefaultTableModel) tblEmployee.getTbl().getModel();
            String genderStr = employeeSave.isGender() ? "Nữ" : "Nam";

            System.out.println(employeeSave);


            model.addRow(new Object[]{
                    employeeSave.getEmployeeId(),
                    employeeSave.getFullName(),
                    genderStr,
                    employeeSave.getAccount().getRole().getRoleName(),
                    employeeSave.getPhone()
            });
            if (shiftList != null) {
                shiftList.addNewEmployee(employeeSave);
                log.info("Notified ShiftList about new employee: {}", employeeSave.getFullName());
            }
            Message.showMessage("Thành công", "Thêm nhân viên thành công!");
            return true;
        } catch (Exception e) {
            log.error("Error creating employee: ", e);
            Message.showMessage("Lỗi", "Có lỗi xảy ra: " + e.getMessage());
            return false;
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 16);
        style.setFont(font);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }


    private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {
        ExportExcelService.exportTableToExcel(
                this,
                tblEmployee.getTbl(),
                "Danh sách nhân viên",
                "DanhSachNhanVien");
    }

    private void filterTable(String genderFilter, String positionFilter) {
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) tblEmployee.getTbl()
                .getRowSorter();

        RowFilter<DefaultTableModel, Object> rf = new RowFilter() {
            @Override
            public boolean include(RowFilter.Entry entry) {
                String gender = entry.getStringValue(2);
                String position = entry.getStringValue(3);

                boolean genderMatches = genderFilter == null || genderFilter.equals("Tất cả")
                        || gender.equals(genderFilter);
                boolean positionMatches = positionFilter == null || positionFilter.equals("Tất cả")
                        || position.equals(positionFilter);

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
                && modal.getCmbGender().getSelectedItem().toString().equalsIgnoreCase("Nữ");
        EmployeeService service = new EmployeeService();

        // Reset lỗi
        Color white = Color.WHITE;
        modal.getLblErrolName().setForeground(white);
        modal.getLblErrolPhone().setForeground(white);
        modal.getLblErrolCitizen().setForeground(white);
        modal.getLblErrolEmail().setForeground(white);
        modal.getLblErrolHireDate().setForeground(white);

        boolean valid = true;
        Color red = Color.RED;
        String errorMessage = ""; // Lưu thông báo lỗi cụ thể

        // Kiểm tra Avatar
        AvatarLabel avatarLabel = modal.getAvatarLabel();
        byte[] avatarBytes = avatarLabel != null ? avatarLabel.getImageAsBytes("jpg") : null;

        if (avatarBytes == null || avatarBytes.length == 0) {
            errorMessage = "Vui lòng chọn ảnh đại diện!";
            modal.getLblStatus().setText(errorMessage);
            modal.getLblStatus().setForeground(Color.RED);
            valid = false;
        }

        // Tên
        if (name.isEmpty()) {
            if (errorMessage.isEmpty()) errorMessage = "Họ tên không được để trống!";
            modal.getLblErrolName().setText("Họ tên không được để trống!");
            modal.getLblErrolName().setForeground(red);
            valid = false;
        } else if (name.length() < 2) {
            if (errorMessage.isEmpty()) errorMessage = "Họ tên quá ngắn!";
            modal.getLblErrolName().setText("Họ tên quá ngắn!");
            modal.getLblErrolName().setForeground(red);
            valid = false;
        }

        // Số điện thoại
        if (phone.isEmpty()) {
            if (errorMessage.isEmpty()) errorMessage = "Số điện thoại không được để trống!";
            modal.getLblErrolPhone().setText("Số điện thoại không được để trống!");
            modal.getLblErrolPhone().setForeground(red);
            valid = false;
        } else if (!phone.matches("^0\\d{9}$")) {
            if (errorMessage.isEmpty()) errorMessage = "Số điện thoại không hợp lệ!";
            modal.getLblErrolPhone().setText("Số điện thoại không hợp lệ!");
            modal.getLblErrolPhone().setForeground(red);
            valid = false;
        }

        // CCCD
        if (citizenId.isEmpty()) {
            if (errorMessage.isEmpty()) errorMessage = "CCCD không được để trống!";
            modal.getLblErrolCitizen().setText("CCCD không được để trống!");
            modal.getLblErrolCitizen().setForeground(red);
            valid = false;
        } else if (!citizenId.matches("\\d{12}")) {
            if (errorMessage.isEmpty()) errorMessage = "CCCD phải có 12 chữ số!";
            modal.getLblErrolCitizen().setText("CCCD phải có 12 chữ số!");
            modal.getLblErrolCitizen().setForeground(red);
            valid = false;
        } else if (service.existsByCitizenId(citizenId) != null) {
            if (errorMessage.isEmpty()) errorMessage = "CCCD đã tồn tại trong hệ thống!";
            modal.getLblErrolCitizen().setText("CCCD đã tồn tại!");
            modal.getLblErrolCitizen().setForeground(red);
            valid = false;
        }

        // Email
        if (email.isEmpty()) {
            if (errorMessage.isEmpty()) errorMessage = "Email không được để trống!";
            modal.getLblErrolEmail().setText("Email không được để trống!");
            modal.getLblErrolEmail().setForeground(red);
            valid = false;
        } else if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            if (errorMessage.isEmpty()) errorMessage = "Email không hợp lệ!";
            modal.getLblErrolEmail().setText("Email không hợp lệ!");
            modal.getLblErrolEmail().setForeground(red);
            valid = false;
        }

        // Ngày tuyển dụng
        LocalDate hireDate = null;
        try {
            if (!hireDateStr.isEmpty()) {
                hireDate = LocalDate.parse(hireDateStr, Constants.DATE_FORMATTER);
            } else {
                hireDate = LocalDate.now();
            }
        } catch (DateTimeParseException e) {
            if (errorMessage.isEmpty()) errorMessage = "Ngày tuyển dụng không hợp lệ (dd/MM/yyyy)!";
            modal.getLblErrolHireDate().setText("Ngày không hợp lệ (dd/MM/yyyy)!");
            modal.getLblErrolHireDate().setForeground(red);
            valid = false;
            log.error("Error parsing hire date: ", e);
        }

        // Trả về kết quả kèm message lỗi
        return new Valid(name, valid, gender, phone, citizenId, email, hireDate, errorMessage);
    }

    // Cập nhật record Valid để chứa errorMessage
    private record Valid(
            String fullName,
            boolean valid,
            boolean gender,
            String phone,
            String citizenId,
            String email,
            LocalDate hireDate,
            String errorMessage) {
    }

    private iuh.fit.se.group1.ui.component.custom.Button btnAddEmployee;
    private iuh.fit.se.group1.ui.component.custom.Button btnExport;
    private iuh.fit.se.group1.ui.component.custom.Button btnImport;
    private iuh.fit.se.group1.ui.component.HeaderCustom headerCustom2;
    private javax.swing.JLabel lblTitleEmployee;
    private iuh.fit.se.group1.ui.component.table.Table tblEmployee;


}

