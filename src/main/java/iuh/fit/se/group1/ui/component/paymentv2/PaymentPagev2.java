/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.component.paymentv2;

import iuh.fit.se.group1.dto.EmployeeDTO;
import iuh.fit.se.group1.dto.OrderDTO;
import iuh.fit.se.group1.service.OrderDetailService;
import iuh.fit.se.group1.service.OrderService;
import iuh.fit.se.group1.service.SurchargeDetailService;
import iuh.fit.se.group1.ui.component.custom.message.CustomDialog;
import lombok.Getter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

/**
 * @author THIS PC
 */
public class PaymentPagev2 extends javax.swing.JPanel {


    private final OrderService orderService = new OrderService();
    private final OrderDetailService orderDetailService = new OrderDetailService();
    private final SurchargeDetailService surchargeDetailService = new SurchargeDetailService();
    private PaymentMain paymentMain;
    @Getter
    private EmployeeDTO currentEmployee;


    public void setCurrentEmployee(EmployeeDTO currentEmployee) {
        this.currentEmployee = currentEmployee;
        paymentMain.setCurrentEmployee(currentEmployee);
    }

    /**
     * Creates new form PaymentPagev2
     */
    public PaymentPagev2() {
        initComponents();
        paymentMain = new PaymentMain();

        Runnable backStep1Action = this::backStep1;
        paymentMain.setStep1(backStep1Action);
        sequencePayment1.setActiveStep(0);
        Runnable toStep3Action = this::toStep3;
        paymentMain.setBackStep3Action(toStep3Action);
        loadDataTable();

        headerShift1.getLblSubTitle().setText("");
        headerShift1.getLblTile().setText("Thanh toán hóa đơn");
        btnNext.addActionListener(e -> {
            int row = tbl.getSelectedRow();
            if (row == -1) {
                CustomDialog.showMessage(
                        null,
                        "Vui lòng chọn hóa đơn cần thanh toán",
                        "Thông báo",
                        CustomDialog.MessageType.ERROR,
                        400,
                        300
                );
            }


            Long orderId = Long.parseLong(tbl.getValueAt(row, 0).toString());

            sequencePayment1.setActiveStep(1);

            // set hóa đơn cho bước tiếp theo

            paymentMain.setOrder(orderId, orderService, orderDetailService, surchargeDetailService);
            scrollPaneWin111.setViewportView(paymentMain);
            SwingUtilities.invokeLater(() ->
                    scrollPaneWin111.getViewport().setViewPosition(new Point(0, 0))
            );

        });

        paymentMain.getBtnPrev().addActionListener(e -> {
            backStep1();
        });

        search1.setText("");
        search1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                findPendingOrders(search1.getText().trim());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                findPendingOrders(search1.getText().trim());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

    }

    public void setOnPayment() {
        sequencePayment1.setActiveStep(0);
        loadDataTable();
        scrollPaneWin111.setViewportView(jPanel1);
    }

    private void backStep1() {
        sequencePayment1.setActiveStep(0);
        loadDataTable();
        scrollPaneWin111.setViewportView(jPanel1);

    }

    private void toStep3() {
        sequencePayment1.setActiveStep(2);
    }

    private void findPendingOrders(String keyword) {
        tbl.clearData();

        for (OrderDTO order : orderService.getUnpaidOrdersByKeyword(keyword)) {
            displayOrderOnTable(order);
        }
    }

    private void displayOrderOnTable(OrderDTO order) {
        String rooms = order.getBookings().stream()
                .map(e -> e.getRoom().getRoomNumber())
                .collect(Collectors.joining(", "));

        String checkIn = order.getBookings().get(0).getCheckInDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String checkOut = order.getBookings().get(0).getCheckOutDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        tbl.addRow(
                order.getOrderId().toString(),
                order.getCustomer().getFullName(),
                rooms,
                order.getTotalAmount().doubleValue(),
                checkIn,
                checkOut,
                order.getCustomer().getPhone()
        );
    }

    private void loadDataTable() {
        tbl.clearData();
        for (OrderDTO order : orderService.getUnpaidOrders()) {
            displayOrderOnTable(order);
        }
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerShift1 = new iuh.fit.se.group1.ui.component.HeaderShift();
        scrollPaneWin111 = new iuh.fit.se.group1.ui.component.scroll.ScrollPaneWin11();
        jPanel1 = new javax.swing.JPanel();
        btnNext = new iuh.fit.se.group1.ui.component.custom.Button();
        scrollPaneWin112 = new iuh.fit.se.group1.ui.component.scroll.ScrollPaneWin11();
        tbl = new iuh.fit.se.group1.ui.component.paymentv2.CustomTable();
        search1 = new iuh.fit.se.group1.ui.component.booking.Search();
        jLabel1 = new javax.swing.JLabel();
        sequencePayment1 = new iuh.fit.se.group1.ui.component.paymentv2.SequencePayment();

        setBackground(new java.awt.Color(241, 241, 241));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        btnNext.setBackground(new java.awt.Color(77, 134, 168));
        btnNext.setForeground(new java.awt.Color(255, 255, 255));
        btnNext.setText("TIẾP THEO");
        btnNext.setToolTipText("");
        btnNext.setBorderRadius(5);
        btnNext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        scrollPaneWin112.setForeground(new java.awt.Color(255, 255, 255));

        tbl.setBackground(new java.awt.Color(255, 255, 255));
        scrollPaneWin112.setViewportView(tbl);

        search1.setText("search1");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Tìm kiếm hóa đơn");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap(12, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(scrollPaneWin112, javax.swing.GroupLayout.PREFERRED_SIZE, 848, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(search1, javax.swing.GroupLayout.PREFERRED_SIZE, 631, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(258, 258, 258))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(283, 283, 283))))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(search1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scrollPaneWin112, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(77, Short.MAX_VALUE))
        );

        scrollPaneWin111.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap(79, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(sequencePayment1, javax.swing.GroupLayout.DEFAULT_SIZE, 871, Short.MAX_VALUE)
                                        .addComponent(scrollPaneWin111, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addContainerGap(80, Short.MAX_VALUE))
                        .addComponent(headerShift1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(headerShift1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(sequencePayment1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(scrollPaneWin111, javax.swing.GroupLayout.PREFERRED_SIZE, 541, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(37, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private iuh.fit.se.group1.ui.component.custom.Button btnNext;
    private iuh.fit.se.group1.ui.component.HeaderShift headerShift1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private iuh.fit.se.group1.ui.component.scroll.ScrollPaneWin11 scrollPaneWin111;
    private iuh.fit.se.group1.ui.component.scroll.ScrollPaneWin11 scrollPaneWin112;
    private iuh.fit.se.group1.ui.component.booking.Search search1;
    private iuh.fit.se.group1.ui.component.paymentv2.SequencePayment sequencePayment1;
    private iuh.fit.se.group1.ui.component.paymentv2.CustomTable tbl;
    // End of variables declaration//GEN-END:variables
}
