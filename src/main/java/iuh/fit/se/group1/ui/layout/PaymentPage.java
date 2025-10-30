/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.layout;

import iuh.fit.se.group1.dto.BookingDisplayDTO;
import iuh.fit.se.group1.entity.Customer;
import iuh.fit.se.group1.entity.Order;
import iuh.fit.se.group1.service.BookingService;
import iuh.fit.se.group1.service.OrderService;
import iuh.fit.se.group1.ui.component.payment.CashPaymentModal;
import iuh.fit.se.group1.ui.component.payment.TransferPaymentModal;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.table.DefaultTableModel;

import raven.glasspanepopup.GlassPanePopup;

/**
 * @author Administrator
 */
public class PaymentPage extends javax.swing.JPanel {

//    private final BookingService bookingService;
    private final OrderService orderService;
    /**
     * Creates new form OrderManagement
     */
    public PaymentPage() {
        initComponents();
        custom();
        orderService = new OrderService();
        loadListBooking(orderService.findAllBookingDisplay());
    }

    public void setCustomer(Customer customer) {
        infoPayment1.getTxtName().setText(customer.getFullName());
        infoPayment1.getCboGender().setSelectedIndex(customer.isGender() ? 0 : 1);
        infoPayment1.getTxtPhone().setText(customer.getPhone());
    }

    public void loadListBooking(List<BookingDisplayDTO> bookingDisplayDTOs){
        DefaultTableModel defaultTableModel = (DefaultTableModel) listBooking.getTable().getModel();
        defaultTableModel.setRowCount(0);
        bookingDisplayDTOs.forEach(e-> defaultTableModel.addRow(new Object[]{
                e.getBookingId(),
                e.getRoomNumber(),
                e.getCustomerName(),
                e.getPhoneNumber()
        }));
    }

    private void custom() {
        scrollPaneWin111.setOpaque(false);
        scrollPaneWin111.getViewport().setOpaque(false);
        scrollPaneWin111.setBorder(null);

        // Bo góc trắng cho phần InfoOrder bên trong
        infoPayment1.setOpaque(true);
        infoPayment1.setBackground(Color.WHITE);
        infoPayment1.setBorder(
                javax.swing.BorderFactory.createCompoundBorder(
                        javax.swing.BorderFactory.createLineBorder(new Color(160, 160, 160), 1, true), // viền bo góc
                        javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)
                )
        );

        // Bo góc cho jPanel1 (bên phải)
        jPanel1.setOpaque(true);
        jPanel1.setBackground(new Color(241, 241, 241));
        jPanel1.setBorder(
                javax.swing.BorderFactory.createCompoundBorder(
                        javax.swing.BorderFactory.createLineBorder(new Color(160, 160, 160), 1, true),
                        javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)
                )
        );
        scrollPaneWin111.setViewportView(infoPayment1);
        scrollPaneWin111.setPreferredSize(new Dimension(700, 600));

        listBooking.hideOtherPanel();

        GroupLayout layout = (GroupLayout) this.getLayout();
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(headerBooking1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(scrollPaneWin111, GroupLayout.PREFERRED_SIZE, 700, GroupLayout.PREFERRED_SIZE)
                                .addGap(5)
                                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(headerBooking1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(10)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(scrollPaneWin111, GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                                .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE))
                        .addContainerGap()
        );
        int contentHeight = 1200; // chiều cao mong muốn, phải lớn hơn viewport
        infoPayment1.setPreferredSize(new Dimension(infoPayment1.getWidth(), contentHeight));
        scrollPaneWin111.setViewportView(infoPayment1);
        listSurcharge.getjLabel1().setText("Danh sách phụ thu");
        javax.swing.table.DefaultTableModel model =
                (javax.swing.table.DefaultTableModel) listSurcharge.getTable().getModel();

        // Thay tiêu đề cột
        model.setColumnIdentifiers(new Object[]{"Mã phụ thu", "Tên phụ thu", "Giá trị"});
        infoPayment1.getBtnCash().addActionListener(l -> {
            var modal = new CashPaymentModal();
            GlassPanePopup.showPopup(modal);
        });
        infoPayment1.getBtnTransfer().addActionListener(l -> {
            var modal = new TransferPaymentModal();
            GlassPanePopup.showPopup(modal);
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

        headerBooking1 = new iuh.fit.se.group1.ui.component.HeaderBooking();
        scrollPaneWin111 = new iuh.fit.se.group1.ui.component.scroll.ScrollPaneWin11();
        infoPayment1 = new iuh.fit.se.group1.ui.component.payment.InfoPayment();
        jPanel1 = new javax.swing.JPanel();
        listBooking = new iuh.fit.se.group1.ui.component.table.ListOrder();
        listSurcharge = new iuh.fit.se.group1.ui.component.table.ListOrder();

        setBackground(new java.awt.Color(241, 241, 241));

        scrollPaneWin111.setViewportView(infoPayment1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(listBooking, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(listSurcharge, javax.swing.GroupLayout.PREFERRED_SIZE, 477, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(listBooking, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(listSurcharge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(headerBooking1, javax.swing.GroupLayout.DEFAULT_SIZE, 1215, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(scrollPaneWin111, javax.swing.GroupLayout.PREFERRED_SIZE, 639, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(headerBooking1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(scrollPaneWin111, javax.swing.GroupLayout.PREFERRED_SIZE, 514, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(12, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private iuh.fit.se.group1.ui.component.HeaderBooking headerBooking1;
    private iuh.fit.se.group1.ui.component.payment.InfoPayment infoPayment1;
    private javax.swing.JPanel jPanel1;
    private iuh.fit.se.group1.ui.component.table.ListOrder listBooking;
    private iuh.fit.se.group1.ui.component.table.ListOrder listSurcharge;
    private iuh.fit.se.group1.ui.component.scroll.ScrollPaneWin11 scrollPaneWin111;
    // End of variables declaration//GEN-END:variables
}