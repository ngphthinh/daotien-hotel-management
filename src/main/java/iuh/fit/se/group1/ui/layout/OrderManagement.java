
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.layout;

import iuh.fit.se.group1.entity.Customer;
import iuh.fit.se.group1.entity.Order;
import iuh.fit.se.group1.service.OrderService;
import iuh.fit.se.group1.ui.component.custom.Combobox;
import iuh.fit.se.group1.ui.component.table.TableActionEvent;
import iuh.fit.se.group1.util.Constants;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * @author Windows
 */
public class OrderManagement extends javax.swing.JPanel {

    public static void main(String[] args) {
        Frame frame = new Frame();
        OrderManagement orderManagement = new OrderManagement();
        frame.add(orderManagement);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        frame.setVisible(true);
    }

    private final OrderService orderService;
    private MainLayout parent;
    private PaymentPage paymentPage;

    /**
     * Creates new form OrderManagement
     */
    public OrderManagement() {
        initComponents();
        custom();
        orderService = new OrderService();
        loadData();
    }

    public void loadData(List<Order> orders) {
        DefaultTableModel model = (DefaultTableModel) tblOrder.getTbl().getModel();
        model.setRowCount(0); // Xóa dữ liệu hiện tại trong bảng

        System.out.println(orders);

        for (Order order : orders) {
            model.addRow(new Object[]{
                    order.getOrderId(),
                    Constants.DATE_FORMATTER.format(order.getOrderDate()),
                    order.getTotalAmount(),
                    order.getOrderType().getName(),
                    order.getCustomer().getFullName()
            });
        }
    }
    public void loadData() {
        DefaultTableModel model = (DefaultTableModel) tblOrder.getTbl().getModel();
        model.setRowCount(0); // Xóa dữ liệu hiện tại trong bảng

        for (Order order : orderService.getAllOrders()) {

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
                    rooms ,
                    checkIn,
                    checkOut
            });
        }
    }


    private void custom() {
        String cols[] = {"Mã", "Tên khách hàng", "CCCD/Passport", "Tổng tiền", "Trạng thái", "Số phòng","Ngày nhận","Ngày trả", "Chức năng"};
        DefaultTableModel model = new DefaultTableModel(cols, 5);
        tblOrder.getTbl().setModel(model);
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                System.out.println("Edit row: " + row);
            }

            @Override
            public void onDelete(int row) {
                if (tblOrder.getTbl().isEditing()) {
                    tblOrder.getTbl().getCellEditor().stopCellEditing();
                }
                DefaultTableModel model = (DefaultTableModel) tblOrder.getTbl().getModel();
                model.removeRow(row);
            }

            @Override
            public void onView(int row) {
//                Long id = Long.valueOf(tblOrder.getTbl().getValueAt(row, 0).toString());
//                Customer customer = orderService.getAllOrders()
//                        .stream()
//                        .filter(e -> e.getOrderId().equals(id))
//                        .findFirst().get()
//                        .getCustomer();
//                paymentPage.setCustomer(customer);
//                parent.setMainContent(paymentPage);
            }
        };

        var header = tblOrder.getTbl().getTableHeader();

        Combobox<String> cmbType = new Combobox<>(new String[]{"Đã hoàn thành", "Đang xử lí", "Đặt trước"});
        TableCellRenderer defaultRenderer = header.getDefaultRenderer();
        TableColumn column = tblOrder.getTbl().getColumnModel().getColumn(4);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                tblOrder.getTbl().clearSelection();
            }
        });
        column.setHeaderRenderer((tbl, value, isSelected, hasFocus, row, col) -> {
            Component comp = defaultRenderer.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, col);

            if (comp instanceof JLabel lbl) {
                lbl.setText("Loại hóa đơn      \u25BC");
                lbl.setHorizontalTextPosition(SwingConstants.LEFT);
                lbl.setHorizontalAlignment(SwingConstants.LEFT);
                lbl.setIconTextGap(5);
            }
            return comp;
        });

        cmbType.addActionListener(ev -> {
            String selected = (String) cmbType.getSelectedItem();
//            filterCustomerTable(selected);
            header.remove(cmbType);
            header.repaint();
        });

        header.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = tblOrder.getTbl().columnAtPoint(e.getPoint());
                if (col == 4) {
                    Rectangle rect = header.getHeaderRect(col);

                    cmbType.setBounds(rect);
                    cmbType.setVisible(true);
                    header.add(cmbType);
                    cmbType.showPopup();

                    cmbType.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent fe) {
                            cmbType.setVisible(false);
                            header.remove(cmbType);
                        }
                    });
                }
            }
        });

        tblOrder.setTableActionColumn(tblOrder.getTbl(), 8, event, true);
        tblOrder.getTbl().getColumnModel().getColumn(0).setPreferredWidth(70);  // chiều rộng mong muốn
        tblOrder.getTbl().getColumnModel().getColumn(1).setPreferredWidth(200);
        tblOrder.getTbl().getColumnModel().getColumn(2).setPreferredWidth(150);
        tblOrder.getTbl().getColumnModel().getColumn(3).setPreferredWidth(150);
        tblOrder.getTbl().getColumnModel().getColumn(4).setPreferredWidth(100);
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

    public void setupOrderTableColumnAlignment(JTable tblOrder) {

        DefaultTableCellRenderer left = new DefaultTableCellRenderer();
        left.setHorizontalAlignment(SwingConstants.LEFT);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(SwingConstants.RIGHT);

        // Căn trái mặc định toàn bộ
        for (int i = 0; i < tblOrder.getColumnCount() -1 ; i++) {
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


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerCustom1 = new iuh.fit.se.group1.ui.component.HeaderCustom();
        tblOrder = new iuh.fit.se.group1.ui.component.table.Table();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(241, 241, 241));

        tblOrder.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 20, 20, 20));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("Danh sách hóa đơn");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(headerCustom1, javax.swing.GroupLayout.DEFAULT_SIZE, 1212, Short.MAX_VALUE)
                        .addComponent(tblOrder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(jLabel1)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(headerCustom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                                .addComponent(jLabel1)
                                .addGap(15, 15, 15)
                                .addComponent(tblOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 637, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    public void setPaymentPage(PaymentPage paymentPage) {
        this.paymentPage = paymentPage;
    }

    public void setParent(MainLayout parent) {
        this.parent = parent;
    }



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private iuh.fit.se.group1.ui.component.HeaderCustom headerCustom1;
    private javax.swing.JLabel jLabel1;
    private iuh.fit.se.group1.ui.component.table.Table tblOrder;
    // End of variables declaration//GEN-END:variables
}
