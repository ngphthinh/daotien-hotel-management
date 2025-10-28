/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.layout;

import iuh.fit.se.group1.entity.Customer;
import iuh.fit.se.group1.service.CustomerService;
import iuh.fit.se.group1.ui.component.custom.Combobox;
import iuh.fit.se.group1.ui.component.custom.message.Message;
import iuh.fit.se.group1.ui.component.modal.InfoCustomerModal;
import iuh.fit.se.group1.ui.component.table.TableActionEvent;
import iuh.fit.se.group1.util.Constants;

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
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JTable;
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
import org.slf4j.LoggerFactory;

import raven.glasspanepopup.GlassPanePopup;

/**
 *
 * @author Windows
 */
public class CustomerManagement extends javax.swing.JPanel {

    private int customerCounter = 0;
    private CustomerService customerService;

    public CustomerManagement() {
        initComponents();
        custom();
        customerService = new CustomerService();
        loadTable(customerService.getAllCustomer());
    }

    private void loadTable(List<Customer> customers) {
        DefaultTableModel modal = (DefaultTableModel) tblCustomer.getTbl().getModel();
        modal.setRowCount(0);
        for (Customer customer : customers) {
            String genderStr = customer.isGender() ? "Nữ" : "Nam";
            modal.addRow(new Object[]{
                customer.getCustomerId(),
                customer.getFullName(),
                genderStr,
                customer.getEmail(),
                customer.getCitizenId(),
                customer.getPhone()
            });

        }
    }

    private void custom() {
        btnAddCustomer.setBackground(new Color(108, 165, 200));
        btnAddCustomer.setForeground(Color.WHITE);
        btnAddCustomer.setBorderRadius(10);

        btnExport.setBackground(new Color(13, 200, 7));
        btnExport.setForeground(Color.WHITE);
        btnExport.setBorderRadius(10);

        btnImport.setBackground(new Color(255, 108, 3));
        btnImport.setForeground(Color.WHITE);
        btnImport.setBorderRadius(10);

        btnAddCustomer.setIcon(FontIcon.of(FontAwesomeSolid.PLUS, 17, Color.WHITE), SwingConstants.RIGHT);
        btnImport.setIcon(FontIcon.of(FontAwesomeSolid.FILE_IMPORT, 17, Color.WHITE), SwingConstants.RIGHT);
        btnExport.setIcon(FontIcon.of(FontAwesomeSolid.FILE_EXPORT, 17, Color.WHITE), SwingConstants.RIGHT);
        headerCustom1.getLblTitle().setText(
                "<html><span style='color:white;'>Quản lý khách hàng</span>");
        headerCustom1.getLblTitle().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));

        String cols[] = {"Mã khách hàng", "Họ tên", "Giới tính", "Email", "CCCD", "Số điện thoại", "Chức năng"};

        DefaultTableModel model = new DefaultTableModel(cols, 0);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tblCustomer.getTbl().setRowSorter(sorter);
        tblCustomer.getTbl().setModel(model);
        tblCustomer.getTbl().setAutoCreateRowSorter(false);
        tblCustomer.getTbl().setRowSorter(sorter);

        for (int i = 0; i < tblCustomer.getTbl().getColumnCount(); i++) {
            sorter.setSortable(i, false);
        }

        tblCustomer.setTableActionColumn(tblCustomer.getTbl(), 6, new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                DefaultTableModel model = (DefaultTableModel) tblCustomer.getTbl().getModel();
                String code = model.getValueAt(row, 0).toString();

                Customer customer = customerService.getCustomerById(code);
                if (customer == null) {
                    Message.showMessage("Lỗi", "Không tìm thấy khách hàng!");
                    return;
                }

                InfoCustomerModal modal = new InfoCustomerModal();
                modal.getBtnSave().setText("Cập nhật");
                modal.getTxtName().setText(customer.getFullName());
                modal.getCmbGender().setSelectedItem(customer.isGender() ? "Nam" : "Nữ");
                modal.getTxtEmail().setText(customer.getEmail());
                modal.getTxtCitizen().setText(customer.getCitizenId());
                modal.getTxtPhone().setText(customer.getPhone());
                modal.getTxtAddress().setText(customer.getAddress());
                modal.getTxtDob().setText(customer.getDateOfBirth().format(Constants.DATE_FORMATTER));

                modal.getLblErrolName().setText("");
                modal.getLblErrolPhone().setText("");
                modal.getLblErrolEmail().setText("");
                modal.getLblErrolCitizen().setText("");
                modal.getLblErrolAddress().setText("");
                modal.getLblErrolDob().setText("");

                Color red = Color.RED;
                modal.getLblErrolName().setForeground(red);
                modal.getLblErrolPhone().setForeground(red);
                modal.getLblErrolEmail().setForeground(red);
                modal.getLblErrolCitizen().setForeground(red);
                modal.getLblErrolAddress().setForeground(red);
                modal.getLblErrolDob().setForeground(red);

                modal.saveData(ae -> {
                    Valid rs = getValid(modal);
                    if (!rs.valid) {
                        return;
                    }
                    customer.setFullName(rs.name);
                    customer.setPhone(rs.phone);
                    customer.setEmail(rs.email);
                    customer.setCitizenId(rs.citizen);
                    customer.setAddress(rs.address);
                    customer.setGender(rs.gender);
                    customer.setDateOfBirth(rs.dob);

                    Customer updated = customerService.updateCustomer(customer);
                    if (updated != null) {
                        model.setValueAt(updated.getFullName(), row, 1);
                        model.setValueAt(updated.isGender() ? "Nam" : "Nữ", row, 2);
                        model.setValueAt(updated.getEmail(), row, 3);
                        model.setValueAt(updated.getCitizenId(), row, 4);
                        model.setValueAt(updated.getPhone(), row, 5);
                        GlassPanePopup.closePopupLast();
                    } else {
                        Message.showMessage("Lỗi", "Cập nhật khách hàng thất bại!");
                    }
                });

                modal.closeModel(ae -> GlassPanePopup.closePopupLast());
                GlassPanePopup.showPopup(modal);
            }

            @Override
            public void onDelete(int row) {
                String title = "Xác nhận xóa khách hàng";
                String message = "Bạn có chắc chắn muốn xóa khách hàng này không?";
                Message.showConfirm(title, message, () -> {
                    JTable table = tblCustomer.getTbl();
                    if (table.isEditing()) {
                        table.getCellEditor().stopCellEditing();
                    }
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    int rowDelete = table.getSelectedRow();

                    if (rowDelete >= 0) {
                        Long id = (Long) model.getValueAt(rowDelete, 0);
                        model.removeRow(rowDelete);
                        customerService.deleteCustomer(id);
                    }
                });
            }

            @Override
            public void onView(int row) {
                DefaultTableModel model = (DefaultTableModel) tblCustomer.getTbl().getModel();
                String code = model.getValueAt(row, 0).toString();
                Customer customer = customerService.getCustomerById(code);
                if (customer == null) {
                    return;
                }

                InfoCustomerModal modal = new InfoCustomerModal();
                modal.getBtnSave().setText("Xong");
                modal.getTxtName().setText(customer.getFullName());
                modal.getCmbGender().setSelectedItem(customer.isGender() ? "Nam" : "Nữ");
                modal.getTxtEmail().setText(customer.getEmail());
                modal.getTxtCitizen().setText(customer.getCitizenId());
                modal.getTxtPhone().setText(customer.getPhone());
                modal.getTxtAddress().setText(customer.getAddress());
                modal.getTxtDob().setText(customer.getDateOfBirth().format(Constants.DATE_FORMATTER));

                modal.getTxtName().setEditable(false);
                modal.getTxtPhone().setEditable(false);
                modal.getTxtEmail().setEditable(false);
                modal.getTxtCitizen().setEditable(false);
                modal.getTxtAddress().setEditable(false);
                modal.getTxtDob().setEditable(false);
                modal.getCmbGender().setEnabled(false);

                modal.saveData(ae -> GlassPanePopup.closePopupLast());
                modal.closeModel(ae -> GlassPanePopup.closePopupLast());
                GlassPanePopup.showPopup(modal);
            }
        }, true);

        tblCustomer.getTbl().getColumnModel().getColumn(0).setPreferredWidth(100);
        tblCustomer.getTbl().getColumnModel().getColumn(1).setPreferredWidth(150);
        tblCustomer.getTbl().getColumnModel().getColumn(2).setPreferredWidth(150);
        tblCustomer.getTbl().getColumnModel().getColumn(3).setPreferredWidth(180);
        tblCustomer.getTbl().getColumnModel().getColumn(4).setPreferredWidth(120);
        tblCustomer.getTbl().getColumnModel().getColumn(5).setPreferredWidth(120);
        tblCustomer.getTbl().getColumnModel().getColumn(6).setPreferredWidth(100);

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

                if (col == 6) {
                    tblCustomer.getTbl().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    tblCustomer.getTbl().setCursor(Cursor.getDefaultCursor());
                }
            }
        });
        headerCustom1.handleSearch(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = headerCustom1.getSearchText();
                if (text.isEmpty()) {
                    loadTable(customerService.getAllCustomer());
                    return;
                }
                loadTable(customerService.getAmenityByKeyword(text));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = headerCustom1.getSearchText();
                if (text.isEmpty()) {
                    loadTable(customerService.getAllCustomer());
                    return;
                }
                loadTable(customerService.getAmenityByKeyword(text));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
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

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                tblCustomer.getTbl().clearSelection();
            }
        });
    }

    private void searchCustomer() {
        String keyword = headerCustom1.getSearchText().trim();
        List<Customer> result;

        if (keyword.isEmpty()) {
            result = customerService.getAllCustomer();
        } else {
            result = customerService.getAmenityByKeyword(keyword); // đã có trong service
        }

        // Nếu bạn muốn kết hợp lọc giới tính cùng lúc:
        // String genderFilter = "Tất cả"; // Hoặc lấy từ cmbGender
        // filterAndSearchCustomer(genderFilter, keyword);
        loadTable(result);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerCustom1 = new iuh.fit.se.group1.ui.component.HeaderCustom();
        lblTitleCustomer = new javax.swing.JLabel();
        btnAddCustomer = new iuh.fit.se.group1.ui.component.custom.Button();
        tblCustomer = new iuh.fit.se.group1.ui.component.table.Table();
        btnExport = new iuh.fit.se.group1.ui.component.custom.Button();
        btnImport = new iuh.fit.se.group1.ui.component.custom.Button();

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

        tblCustomer.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 20, 20, 20));

        btnExport.setText("Xuất Excel");
        btnExport.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        btnImport.setText("Tải excel");
        btnImport.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(headerCustom1, javax.swing.GroupLayout.Alignment.TRAILING,
                                javax.swing.GroupLayout.DEFAULT_SIZE, 1214, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(lblTitleCustomer)
                                .addGap(346, 346, 346)
                                .addComponent(btnAddCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 180,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 148,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnImport, javax.swing.GroupLayout.PREFERRED_SIZE, 148,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(tblCustomer, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(headerCustom1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnAddCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 43,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblTitleCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 58,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 43,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnImport, javax.swing.GroupLayout.PREFERRED_SIZE, 43,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(25, 25, 25)
                                .addComponent(tblCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 663,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap()));
    }// </editor-fold>//GEN-END:initComponents

    private void filterCustomerTable(String genderFilter) {
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) tblCustomer.getTbl()
                .getRowSorter();

        RowFilter<DefaultTableModel, Object> rf = new RowFilter<>() {
            @Override
            public boolean include(RowFilter.Entry<? extends DefaultTableModel, ? extends Object> entry) {
                String gender = entry.getStringValue(2);
                boolean genderMatches = genderFilter == null
                        || genderFilter.equals("Tất cả")
                        || gender.equalsIgnoreCase(genderFilter);

                return genderMatches;
            }
        };

        sorter.setRowFilter(rf);
        sorter.setSortKeys(null);
    }

    private void btnAddCustomerActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAddCustomerActionPerformed
        InfoCustomerModal modal = new InfoCustomerModal();

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
                GlassPanePopup.closePopupAll();
            }

        });

        raven.glasspanepopup.GlassPanePopup.showPopup(modal);
    }// GEN-LAST:event_btnAddCustomerActionPerformed

    private void saveData(InfoCustomerModal modal) {
        Valid rs = getValid(modal);
        if (!rs.valid) {
            Message.showMessage("Loi", "Kiem tra lai thong tin");
        }
        try {
            Customer customer = new Customer();
            customer.setFullName(rs.name);
            customer.setGender(rs.gender);
            customer.setEmail(rs.email);
            customer.setCitizenId(rs.citizen);
            customer.setPhone(rs.phone);
            customer.setAddress(rs.address);
            customer.setDateOfBirth(rs.dob);

            Customer customerSave = customerService.createCustomer(customer);

            if (customerSave == null) {
                Message.showMessage("Loi", "Khong the tao nhan vien");
                return;
            }

            DefaultTableModel model = (DefaultTableModel) tblCustomer.getTbl().getModel();
            String genderStr = customer.isGender() ? "Nam" : "Nữ";
            System.out.println(customerSave);
            model.addRow(new Object[]{
                customerSave.getCustomerId(),
                customerSave.getFullName(),
                genderStr,
                customerSave.getEmail(),
                customerSave.getCitizenId(),
                customerSave.getPhone(),});
        } catch (Exception e) {

            Message.showMessage("Lỗi", "Có lỗi xảy ra: " + e.getMessage());
        }
    }

    private static Valid getValid(InfoCustomerModal modal) {
        String name = modal.getTxtName().getText().trim();
        String phone = modal.getTxtPhone().getText().trim();
        String email = modal.getTxtEmail().getText().trim();
        String citizen = modal.getTxtCitizen().getText().trim();
        String address = modal.getTxtAddress().getText().trim();
        boolean gender = modal.getCmbGender().getSelectedItem() != null
                && modal.getCmbGender().getSelectedItem().toString().equalsIgnoreCase("Nam");
        String dobStr = modal.getTxtDob().getText().trim();

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
        LocalDate dob = null;
        try {
            if (!dobStr.isEmpty()) {
                dob = LocalDate.parse(dobStr, Constants.DATE_FORMATTER);
            } else {
                dob = LocalDate.now();
            }
        } catch (DateTimeParseException e) {
            modal.getLblErrolDob().setText("Ngày không hợp lệ (dd-MM-yyyy)!");
            isValid = false;
        }
        return new Valid(name, isValid, phone, email, citizen, address, gender, dob);
    }

    private record Valid(
            String name,
            boolean valid,
            String phone,
            String email,
            String citizen,
            String address,
            boolean gender,
            LocalDate dob
            ) {

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private iuh.fit.se.group1.ui.component.custom.Button btnAddCustomer;
    private iuh.fit.se.group1.ui.component.custom.Button btnExport;
    private iuh.fit.se.group1.ui.component.custom.Button btnImport;
    private iuh.fit.se.group1.ui.component.HeaderCustom headerCustom1;
    private javax.swing.JLabel lblTitleCustomer;
    private iuh.fit.se.group1.ui.component.table.Table tblCustomer;
    // End of variables declaration//GEN-END:variables
}
