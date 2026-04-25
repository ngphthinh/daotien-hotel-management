/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.layout;

import com.raven.datechooser.DateChooser;
import com.raven.datechooser.SelectedAction;
import iuh.fit.se.group1.dto.EmployeeDTO;
import iuh.fit.se.group1.dto.OrderDTO;
import iuh.fit.se.group1.service.EmployeeService;
import iuh.fit.se.group1.service.OrderService;
import iuh.fit.se.group1.ui.component.custom.Combobox;
import iuh.fit.se.group1.ui.component.custom.InvoicePanel;
import iuh.fit.se.group1.ui.component.custom.OrderEditDialog;
import iuh.fit.se.group1.ui.component.table.TableActionEvent;
import iuh.fit.se.group1.util.Constants;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;

/**
 * @author Windows
 */
public class OrderManagement extends javax.swing.JPanel {

    private final OrderService orderService;
    private String currentTypeFilter = "Tất cả";
    private DateChooser dateChooser;
    private LocalDate selectedDate;

    /**
     * Creates new form OrderManagement
     */
    public OrderManagement() {
        initComponents();
        custom();
        orderService = new OrderService();
        selectedDate = LocalDate.now(); // Default to today
        loadData();
    }

    public void loadData() {
        DefaultTableModel model = (DefaultTableModel) tblOrder.getTbl().getModel();
        model.setRowCount(0); // Xóa dữ liệu hiện tại trong bảng

        for (OrderDTO order : orderService.getAllOrdersWithRelationship()) {

            if (!order.getBookings().isEmpty() && order.getBookings() != null) {

                String rooms = order.getBookings().stream()
                        .map(booking -> booking.getRoom().getRoomNumber())
                        .collect(Collectors.joining(", "));

                String checkIn = null;
                String checkOut = null;
                try {
                    checkIn = order.getBookings().get(0).getCheckInDate().format(Constants.DATE_FORMATTER);

                    checkOut = order.getBookings().get(0).getCheckOutDate().format(Constants.DATE_FORMATTER);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                model.addRow(new Object[]{
                        order.getOrderId(),
                        order.getCustomer().getFullName(),
                        order.getCustomer().getCitizenId(),
                        Constants.VND_FORMAT.format(order.getTotalAmount()),
                        order.getOrderType().getName(),
                        rooms,
                        checkIn,
                        checkOut
                });
            } else {
                model.addRow(new Object[]{
                        order.getOrderId(),
                        order.getCustomer().getFullName(),
                        order.getCustomer().getCitizenId(),
                        Constants.VND_FORMAT.format(order.getTotalAmount()),
                        order.getOrderType().getName(),
                        "",
                        "",
                        ""
                });
            }
        }
    }

    public void loadOrdersByDate(LocalDate date) {
        DefaultTableModel model = (DefaultTableModel) tblOrder.getTbl().getModel();
        model.setRowCount(0);

        for (OrderDTO order : orderService.getAllOrders()) {
            // Lọc theo orderDate (ngày tạo đơn)
            LocalDate orderDate = order.getOrderDate().toLocalDate();

            if (!orderDate.equals(date)) {
                continue;
            }

            if (!order.getBookings().isEmpty() && order.getBookings() != null) {
                String rooms = order.getBookings().stream()
                        .map(booking -> booking.getRoom().getRoomNumber())
                        .collect(Collectors.joining(", "));

                String checkIn = null;
                String checkOut = null;
                try {
                    checkIn = order.getBookings().get(0).getCheckInDate().format(Constants.DATE_FORMATTER);

                    checkOut = order.getBookings().get(0).getCheckOutDate().format(Constants.DATE_FORMATTER);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                model.addRow(new Object[]{
                        order.getOrderId(),
                        order.getCustomer().getFullName(),
                        order.getCustomer().getCitizenId(),
                        Constants.VND_FORMAT.format(order.getTotalAmount()),
                        order.getOrderType().getName(),
                        rooms,
                        checkIn,
                        checkOut
                });
            } else {
                model.addRow(new Object[]{
                        order.getOrderId(),
                        order.getCustomer().getFullName(),
                        order.getCustomer().getCitizenId(),
                        Constants.VND_FORMAT.format(order.getTotalAmount()),
                        order.getOrderType().getName(),
                        "",
                        "",
                        ""
                });
            }
        }
    }

    private void custom() {

        headerCustom1.getLblTitle().setText(
                "<html><span style='color:white;'>Quản lý hóa đơn</span>");
        headerCustom1.getLblTitle().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));

        String cols[] = {"Mã", "Tên khách hàng", "CCCD/Passport", "Tổng tiền", "Trạng thái", "Số phòng", "Ngày nhận",
                "Ngày trả", "Chức năng"};
        DefaultTableModel model = new DefaultTableModel(cols, 5) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Chỉ cho phép edit cột "Chức năng" (column 8) để button hoạt động
                return column == 8;
            }
        };
        tblOrder.getTbl().setModel(model);

        // Khởi tạo TableRowSorter
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tblOrder.getTbl().setRowSorter(sorter);

        // Tắt sort cho tất cả các cột (chỉ dùng cho filter)
        for (int i = 0; i < tblOrder.getTbl().getColumnCount(); i++) {
            sorter.setSortable(i, false);
        }


        headerCustom1.handleSearch(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                handleSearch(headerCustom1.getSearchText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                handleSearch(headerCustom1.getSearchText());

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        tblOrder.getTbl().setAutoCreateRowSorter(false);
        tblOrder.getTbl().getTableHeader().setReorderingAllowed(false);
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                int modelRow = tblOrder.getTbl().convertRowIndexToModel(row);

                try {


                    Long id = Long.valueOf(tblOrder.getTbl().getModel().getValueAt(modelRow, 0).toString());
                    OrderDTO order = orderService.getOrderById(id);
                    if (order == null) {
                        JOptionPane.showMessageDialog(OrderManagement.this,
                                "Không tìm thấy hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (order.getOrderType().getOrderTypeId() == 1) {
                        JOptionPane.showMessageDialog(OrderManagement.this,
                                "Không thể sửa hóa đơn đã thanh toán", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Show edit dialog
                    OrderEditDialog editDialog = new OrderEditDialog(SwingUtilities.getWindowAncestor(OrderManagement.this), order);
                    editDialog.setVisible(true);

                    // After dialog closed, reload data
                    loadData();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(OrderManagement.this,
                            "Lỗi khi mở cửa sổ chỉnh sửa: " + ex.getMessage(),
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }

            @Override
            public void onDelete(int row) {

                String orderType = tblOrder.getTbl().getModel().getValueAt(
                        tblOrder.getTbl().convertRowIndexToModel(row), 4).toString();
                if (!orderType.equals("Đặt trước")) {
                    JOptionPane.showMessageDialog(OrderManagement.this,
                            "Chỉ có thể xóa hóa đơn đặt trước!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int confirm = JOptionPane.showConfirmDialog(OrderManagement.this,
                        "Bạn có chắc chắn muốn xóa hóa đơn này?", "Xác nhận xóa",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    Long id = Long.valueOf(tblOrder.getTbl().getModel().getValueAt(
                            tblOrder.getTbl().convertRowIndexToModel(row), 0).toString());
                    orderService.deleteOrderById(id);
                    loadData();
                }
            }

            @Override
            public void onView(int row) {
                try {
                    // Convert view row to model row khi có filter
                    int modelRow = tblOrder.getTbl().convertRowIndexToModel(row);
                    System.out.println("View row: " + modelRow);

                    Long id = Long.valueOf(tblOrder.getTbl().getModel().getValueAt(modelRow, 0).toString());
                    OrderDTO order = orderService.getOrderById(id);

                    if (order == null) {
                        JOptionPane.showMessageDialog(OrderManagement.this,
                                "Không tìm thấy hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }


                    EmployeeService employeeService = new EmployeeService();
                    EmployeeDTO employee = employeeService.getEmployeeById(order.getEmployee().getEmployeeId());
                    order.setEmployee(employee);

                    InvoicePanel invoicePanel = new InvoicePanel(order);
                    JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(OrderManagement.this),
                            "Chi tiết hóa đơn #" + id, true);
                    dialog.getContentPane().add(invoicePanel);
                    dialog.setSize(900, 700);
                    dialog.setLocationRelativeTo(OrderManagement.this);
                    dialog.setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(OrderManagement.this,
                            "Lỗi khi hiển thị hóa đơn: " + ex.getMessage(),
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        setupHeaderFilters();
        setupDateChooser();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                tblOrder.getTbl().clearSelection();
            }
        });

        tblOrder.setTableActionColumn(tblOrder.getTbl(), 8, event, true);
        tblOrder.getTbl().getColumnModel().getColumn(0).setPreferredWidth(70); // chiều rộng mong muốn
        tblOrder.getTbl().getColumnModel().getColumn(1).setPreferredWidth(200);
        tblOrder.getTbl().getColumnModel().getColumn(2).setPreferredWidth(150);
        tblOrder.getTbl().getColumnModel().getColumn(3).setPreferredWidth(150);
        tblOrder.getTbl().getColumnModel().getColumn(4).setPreferredWidth(150);
        tblOrder.getTbl().getColumnModel().getColumn(5).setPreferredWidth(150);
        tblOrder.getTbl().getColumnModel().getColumn(6).setPreferredWidth(150);
        tblOrder.getTbl().getColumnModel().getColumn(7).setPreferredWidth(150);
        tblOrder.getTbl().getColumnModel().getColumn(8).setPreferredWidth(150);

        tblOrder.getTbl().addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int col = tblOrder.getTbl().columnAtPoint(e.getPoint());

                if (col == 8) {
                    tblOrder.getTbl().setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    tblOrder.getTbl().setCursor(Cursor.getDefaultCursor());
                }
            }
        });
        setupOrderTableColumnAlignment(tblOrder.getTbl());
    }

    private void setupDateChooser() {
        txtDate.setEditable(false);
        txtDate.setFocusable(false);
        txtDate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconDate.setIcon(FontIcon.of(FontAwesomeSolid.CALENDAR_ALT, 20, Constants.COLOR_ICON_MENU));
        iconDate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        dateChooser = new DateChooser();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        txtDate.setText(sdf.format(new Date()));
        dateChooser.setDateFormat("dd-MM-yyyy");
        dateChooser.toDay();
        dateChooser.setForeground(Constants.COLOR_ICON_MENU);
        dateChooser.addEventDateChooser((action, date) -> {
            if (action.getAction() == SelectedAction.DAY_SELECTED) {
                dateChooser.hidePopup();
                selectedDate = LocalDate.of(
                        date.getYear(),
                        date.getMonth(),
                        date.getDay()
                );

                // Load orders theo ngày đã chọn
                loadOrdersByDate(selectedDate);
            }
        });
        dateChooser.setTextRefernce(txtDate);
        iconDate.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dateChooser.showPopup(txtDate, 0, txtDate.getHeight());
            }
        });
    }

    private void handleSearch(String searchText) {
        java.util.List<OrderDTO> orders = orderService.searchOrdersByKeyword(searchText);
        DefaultTableModel model = (DefaultTableModel) tblOrder.getTbl().getModel();
        model.setRowCount(0); // Xóa dữ liệu hiện tại trong bảng

        for (OrderDTO order : orders) {

            String rooms = order.getBookings().stream()
                    .map(booking -> booking.getRoom().getRoomNumber())
                    .collect(Collectors.joining(", "));

            String checkIn = null;
            String checkOut = null;
            try {
                checkIn = order.getBookings().get(0).getCheckInDate().format(Constants.DATE_FORMATTER);

                checkOut = order.getBookings().get(0).getCheckOutDate().format(Constants.DATE_FORMATTER);
            } catch (Exception e) {
                order.getBookings().forEach(booking -> {
                    System.out.println(booking.getBookingId());
                });
                throw new RuntimeException(e);
            }

            model.addRow(new Object[]{
                    order.getOrderId(),
                    order.getCustomer().getFullName(),
                    order.getCustomer().getCitizenId(),
                    Constants.VND_FORMAT.format(order.getTotalAmount()),
                    order.getOrderType().getName(),
                    rooms,
                    checkIn,
                    checkOut
            });
        }
    }

    public void setupOrderTableColumnAlignment(JTable tblOrder) {

        DefaultTableCellRenderer left = new DefaultTableCellRenderer();
        left.setHorizontalAlignment(SwingConstants.LEFT);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(SwingConstants.RIGHT);

        // Căn trái mặc định toàn bộ
        for (int i = 0; i < tblOrder.getColumnCount() - 1; i++) {
            tblOrder.getColumnModel().getColumn(i).setCellRenderer(left);
        }

        tblOrder.getColumnModel().getColumn(0).setCellRenderer(center);
        tblOrder.getColumnModel().getColumn(4).setCellRenderer(center);

        // Cột căn phải (index: 3, 5, 6, 7)
        tblOrder.getColumnModel().getColumn(3).setCellRenderer(right);
        tblOrder.getColumnModel().getColumn(5).setCellRenderer(right);
        tblOrder.getColumnModel().getColumn(6).setCellRenderer(right);
        tblOrder.getColumnModel().getColumn(7).setCellRenderer(right);
    }

    private void setupHeaderFilters() {
        var header = tblOrder.getTbl().getTableHeader();

        Combobox<String> cmbType = new Combobox<>(
                new String[]{"Tất cả", "Đã hoàn thành", "Đang xử lí", "Đặt trước", "Đã hủy"});
        TableCellRenderer defaultRenderer = header.getDefaultRenderer();

        // Reset tất cả cột về default renderer
        for (int i = 0; i < tblOrder.getTbl().getColumnCount(); i++) {
            tblOrder.getTbl().getColumnModel().getColumn(i).setHeaderRenderer(defaultRenderer);
        }

        // Cột Trạng thái - Dùng defaultRenderer với text có mũi tên
        TableColumn colStatus = tblOrder.getTbl().getColumnModel().getColumn(4);
        colStatus.setHeaderRenderer((tbl, value, isSelected, hasFocus, row, col) -> {
            Component comp = defaultRenderer.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, col);
            if (comp instanceof JLabel lbl) {
                lbl.setText("Trạng thái \u25BC");
                lbl.setHorizontalAlignment(SwingConstants.LEFT);
            }
            return comp;
        });

        // ACTION LISTENER
        cmbType.addActionListener(ev -> {
            currentTypeFilter = (String) cmbType.getSelectedItem();
            applyFilters();
            header.remove(cmbType);
            header.repaint();
        });

        // MOUSE LISTENER: Click vào header để show combobox
        header.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = tblOrder.getTbl().columnAtPoint(e.getPoint());

                // Remove combobox cũ
                header.remove(cmbType);

                // Chỉ show combobox khi click vào cột 4 (Trạng thái)
                if (col != 4) {
                    return;
                }

                Rectangle rect = header.getHeaderRect(col);

                if (col == 4) {
                    // Show combobox Trạng thái
                    cmbType.setBounds(rect);
                    header.add(cmbType);
                    cmbType.setVisible(true);
                    cmbType.showPopup();
                }
            }
        });
    }

    private void applyFilters() {
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) tblOrder.getTbl().getRowSorter();
        RowFilter<DefaultTableModel, Object> rf = new RowFilter<DefaultTableModel, Object>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                String status = entry.getStringValue(4);

                boolean statusMatches = currentTypeFilter.equals("Tất cả")
                        || (status != null && status.equals(currentTypeFilter));

                return statusMatches;
            }
        };
        sorter.setRowFilter(rf);
        sorter.setSortKeys(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerCustom1 = new iuh.fit.se.group1.ui.component.HeaderCustom();
        tblOrder = new iuh.fit.se.group1.ui.component.table.Table();
        jLabel1 = new javax.swing.JLabel();
        txtDate = new javax.swing.JTextField();
        iconDate = new javax.swing.JLabel();
        btnReset = new javax.swing.JButton();

        setBackground(new java.awt.Color(241, 241, 241));

        tblOrder.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 20, 20, 20));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("Danh sách hóa đơn");

        txtDate.setFont(new java.awt.Font("Segoe UI", 0, 14));
        txtDate.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        iconDate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        btnReset.setIcon(FontIcon.of(FontAwesomeSolid.SYNC_ALT, 18, Constants.COLOR_ICON_MENU));
        btnReset.setBorderPainted(false);
        btnReset.setContentAreaFilled(false);
        btnReset.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnReset.setFocusPainted(false);
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });


        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(headerCustom1, javax.swing.GroupLayout.DEFAULT_SIZE, 1212, Short.MAX_VALUE)
                        .addComponent(tblOrder, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(iconDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(headerCustom1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15,
                                        Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                        .addComponent(jLabel1)
                                        .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(iconDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(15, 15, 15)
                                .addComponent(tblOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 637,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)));
    }// </editor-fold>//GEN-END:initComponents

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {
        // Reset về ngày hiện tại
        selectedDate = LocalDate.now();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        txtDate.setText(sdf.format(new Date()));

        // Load lại toàn bộ dữ liệu
        loadData();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnReset;
    private iuh.fit.se.group1.ui.component.HeaderCustom headerCustom1;
    private javax.swing.JLabel iconDate;
    private javax.swing.JLabel jLabel1;
    private iuh.fit.se.group1.ui.component.table.Table tblOrder;
    private javax.swing.JTextField txtDate;
    // End of variables declaration//GEN-END:variables
}
