/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.layout;

import iuh.fit.se.group1.entity.*;
import iuh.fit.se.group1.enums.BookingType;
import iuh.fit.se.group1.enums.PaymentType;
import iuh.fit.se.group1.service.*;
import iuh.fit.se.group1.ui.component.custom.message.CustomDialog;
import iuh.fit.se.group1.ui.component.payment.CashPaymentModal;
import iuh.fit.se.group1.ui.component.payment.TransferPaymentModal;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import iuh.fit.se.group1.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import raven.glasspanepopup.GlassPanePopup;

/**
 * @author Administrator
 */
public class PaymentPage extends javax.swing.JPanel {

    private static final Logger log = LoggerFactory.getLogger(PaymentPage.class);
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final PaymentService paymentService;
    private Order order;
    private final SurchargeService surchargeService;
    private PromotionService promotionService;
    private SurchargeDetailService surchargeDetailService;
    private Employee currentEmployee;

    private Map<Surcharge, Integer> surcharges = new HashMap<>();

    public void addSurchargeId(Surcharge surcharge) {
        surcharges.merge(surcharge, 1, Integer::sum);
    }


    public Employee getCurrentEmployee() {
        return currentEmployee;
    }

    /**
     * Creates new form OrderManagement
     */
    public PaymentPage() {
        initComponents();
        custom();
        orderService = new OrderService();
        orderDetailService = new OrderDetailService();
        paymentService = new PaymentService();
        surchargeDetailService = new SurchargeDetailService();
        surchargeService = new SurchargeService();
        promotionService = new PromotionService();
        loadData();
    }

    public void loadData() {
        loadListBooking(orderService.getAllOrders());
        setTblSurcharge();
        clearForm();
    }

    public void setCustomer(Customer customer) {
        infoPayment1.getTxtName().setText(customer.getFullName());
        infoPayment1.getCboGender().setSelectedIndex(customer.isGender() ? 0 : 1);
        infoPayment1.getTxtPhone().setText(customer.getPhone());
    }

    public void loadListBooking(List<Order> orders) {
        DefaultTableModel defaultTableModel = (DefaultTableModel) listBooking.getTable().getModel();
        defaultTableModel.setRowCount(0);
        orders.forEach(e -> {
            String listRoom = e.getBookings().stream().map(b -> b.getRoom().getRoomNumber()).collect(Collectors.joining(","));
            defaultTableModel.addRow(new Object[]{
                    e.getOrderId(),
                    e.getCustomer().getFullName(),
                    e.getCustomer().getPhone(),
                    listRoom
            });
        });
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
            if (order == null) {
                CustomDialog.showMessage(null, "Vui lòng chọn đơn hàng để thanh toán!", "Thông báo", CustomDialog.MessageType.WARNING, 300,200);
                return;
            }
            handlePaymentCash(order);
        });
        infoPayment1.getBtnTransfer().addActionListener(l -> {
            if (order == null) {
                CustomDialog.showMessage(null, "Vui lòng chọn đơn hàng để thanh toán!", "Thông báo", CustomDialog.MessageType.WARNING,300,200);
                return;
            }
            handlePaymentTransfer(order);
        });

        listBooking.getTable().getTableHeader().setReorderingAllowed(false);

        listBooking.getTable().setCellSelectionEnabled(false);
        listBooking.getTable().setRowSelectionAllowed(true);
        listBooking.getTable().setColumnSelectionAllowed(false);

        listBooking.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = listBooking.getTable().getSelectedRow();
                clearForm();
                order = orderService.getOrderById(Long.valueOf(listBooking.getTable().getValueAt(row, 0).toString()));
                setCustomer(order.getCustomer());
                setTblRoom(order.getBookings(), order.getOrderId());
                setAmenityList(orderDetailService.getOrderDetailsByOrderId(order.getOrderId()));


                BigDecimal totalRoomPrice = order.getBookings().stream()
                        .map(Booking::getTotalPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);


                BigDecimal totalAmenityPrice = orderDetailService.getOrderDetailsByOrderId(order.getOrderId()).stream()
                        .map(od -> od.getAmenity().getPrice().multiply(BigDecimal.valueOf(od.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalSurcharge = surchargeDetailService.getSurchargeDetailsByOrderId(order.getOrderId()).stream()
                        .map(sd -> sd.getSurcharge().getPrice().multiply(BigDecimal.valueOf(sd.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalAmount = totalRoomPrice.add(totalAmenityPrice).add(totalSurcharge);


                Promotion promotion = setPromotion(totalAmount);

                if (promotion == null) {
                    infoPayment1.getLblPriceTotal().setText(Constants.VND_FORMAT.format(totalAmount.doubleValue()));
                    infoPayment1.getLblPricePromotion().setText("-0 VND");
                    infoPayment1.getLblPricePayment().setText(Constants.VND_FORMAT.format(totalAmount.doubleValue()));
                    order.setTotalAmount(totalAmount);
                    order.setPromotion(null);
                    return;
                }
                BigDecimal totalPromotion = totalAmount.multiply(BigDecimal.valueOf(promotion.getDiscountPercent()).divide(BigDecimal.valueOf(100)));

//                List<SurchargeDetail> surchargeDetails = setSurchargeTime();

                BigDecimal totalFinal = totalAmount.subtract(totalPromotion);

                infoPayment1.getLblPriceTotal().setText(Constants.VND_FORMAT.format(totalAmount.doubleValue()));
                infoPayment1.getLblPricePromotion().setText("-" + Constants.VND_FORMAT.format(totalPromotion.doubleValue()));
                infoPayment1.getLblPricePayment().setText(Constants.VND_FORMAT.format(totalFinal.doubleValue()));

                order.setTotalAmount(totalFinal);
                order.setPromotion(promotion);


            }

        });

        listSurcharge.getTable().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = listSurcharge.getTable().getSelectedRow();
                long surchargeId = Long.parseLong(listSurcharge.getTable().getValueAt(row, 0).toString());
                Surcharge surcharge = surchargeService.getSurchargeById(surchargeId);
                addSurchargeId(surcharge);
                infoPayment1.addSurcharge(surcharge.getSurchargeId() + ": " + surcharge.getName(),
                        surcharge.getPrice(),
                        "1");
                BigDecimal totalSurcharge = BigDecimal.valueOf(Constants.parseVND(infoPayment1.getLblTotalAmenitiAndSurchargeValue().getText()));
                totalSurcharge = totalSurcharge.add(surcharge.getPrice());
                infoPayment1.getLblTotalAmenitiAndSurchargeValue().setText(Constants.VND_FORMAT.format(totalSurcharge));
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    private void handlePaymentCash(Order order) {
        var modal = new CashPaymentModal(order.getTotalAmount().subtract(order.getDeposit()).longValue());
        GlassPanePopup.showPopup(modal);

        modal.getBtnComplete().addActionListener(e -> {
            if (modal.getMoneyGiven() < order.getTotalAmount().longValue()) {
                CustomDialog.showMessage(null,
                        "Khách đưa chưa đủ tiền!",
                        "Thông báo", CustomDialog.MessageType.WARNING, 300,200);
                return;
            }
            long change = modal.getMoneyGiven() - order.getTotalAmount().longValue();
            CustomDialog.showMessage(null,
                    "Thanh toán thành công! Tiền thừa: " + Constants.VND_FORMAT.format(change),
                    "Thông báo", CustomDialog.MessageType.SUCCESS, 300,200);
            saveOrder();
            GlassPanePopup.closePopupAll();
        });

    }

    private void saveOrder() {
        // update trang thái
        orderService.updateOrderStatusToPaid(order.getOrderId(), PaymentType.CASH, order.getTotalAmount());


        surcharges.forEach((k, v) -> {
            SurchargeDetail sd = new SurchargeDetail(k, v);
            surchargeDetailService.save(sd, order.getOrderId());
        });

        // Clear form
        clearForm();
    }

    private Promotion setPromotion(BigDecimal totalAmount) {
        infoPayment1.clearPromotion();

        Promotion promotionDiscountPriceMax = promotionService.getPromotionByPrice(totalAmount);

        if (promotionDiscountPriceMax != null) {
            infoPayment1.addPromotion(promotionDiscountPriceMax.getPromotionName(),
                    "- " + promotionDiscountPriceMax.getDiscountPercent() + "%",
                    "1");
            return promotionDiscountPriceMax;
        }
        return null;
    }


    private void handlePaymentTransfer(Order order) {
        try {
            String response = paymentService.createPayment(order);
            String payUrl = paymentService.extractJsonValue(response, "payUrl");
            String orderId = paymentService.extractJsonValue(response, "orderId");
            String priceFinal = order.getTotalAmount().subtract(order.getDeposit()).toString();
            var modal = new TransferPaymentModal();
            if (payUrl != null && !payUrl.isEmpty()) {
                modal.getLblQrCode().setIcon(new ImageIcon(paymentService.generateQRCodeImage(payUrl, 200, 200)));
            } else {
                CustomDialog.showMessage(null, "Hệ thống đang gặp sự cố khi tạo QR code vui lòng thử lại sau!", "Thông báo lỗi", CustomDialog.MessageType.ERROR,380,200);
            }

            modal.getLblTotaPrice().setText("Tổng tiền: " + order.getTotalAmount().longValue() + " VND");
            JFrame frame = new JFrame("Thanh toán MoMo QR");
            frame.setSize(300, 300);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            frame.setBackground(Color.WHITE);
            JPanel pnlMain = new JPanel();
            pnlMain.setBackground(Color.WHITE);
            pnlMain.setLayout(new BorderLayout());
            JLabel lblImage = new JLabel("", new ImageIcon(paymentService.generateQRCodeImage(payUrl, 250, 250)), SwingConstants.CENTER);
            JLabel lblPrice = new JLabel("Tổng tiền: " + order.getTotalAmount().longValue() + "VND", SwingConstants.CENTER);
            lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 16));

            pnlMain.add(lblImage, BorderLayout.CENTER);
            pnlMain.add(lblPrice, BorderLayout.SOUTH);
            frame.add(pnlMain, BorderLayout.CENTER);
            frame.setVisible(true);

            GlassPanePopup.showPopup(modal);

            modal.getBtnCheck().addActionListener(e ->
            {
                try {
                    if (orderId == null) {
                        JOptionPane.showMessageDialog(null, "Chưa có đơn hàng nào!");
                        return;
                    }
                    String responseCheck = paymentService.queryPayment(orderId);
                    String responseCodeCheck = paymentService.extractJsonValue(responseCheck, "resultCode");
                    String orderIdCheck = paymentService.extractJsonValue(responseCheck, "orderId");
                    if ("0".equals(responseCodeCheck)) {
                        CustomDialog.showMessage(null, "Thanh toán thành công cho đơn hàng: " + orderIdCheck, "Thông báo", CustomDialog.MessageType.SUCCESS,380,200);
                        GlassPanePopup.closePopupAll();
                        frame.dispose();
                        saveOrder();
                    } else {
                        CustomDialog.showMessage(null, "Đơn hàng: " + orderIdCheck + " chưa được thanh toán. Vui lòng kiểm tra lại!", "Thông báo", CustomDialog.MessageType.WARNING,380,200);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Lỗi: " + ex.getMessage());
                }
            });

        } catch (Exception e) {
            CustomDialog.showMessage(null, "Hệ thống đang gặp sự cố, vui lòng thử lại sau!", "Thông báo lỗi", CustomDialog.MessageType.ERROR,380,200);
        }
    }

    public void clearForm() {
        infoPayment1.getTxtName().setText("");
        infoPayment1.getCboGender().setSelectedIndex(0);
        infoPayment1.getTxtPhone().setText("");
        loadListBooking(orderService.getAllOrdersUnPaid());
        infoPayment1.getLblPriceRoomValue().setText("0 VND");
        infoPayment1.getLblTotalAmenitiAndSurchargeValue().setText("0 VND");
        infoPayment1.getLblPriceTotal().setText("0 VND");
        infoPayment1.getLblPricePromotion().setText("0 VND");
        infoPayment1.getLblPricePayment().setText("0 VND");
        infoPayment1.clearAmenitiesAndSurcharges();
        infoPayment1.clearPromotion();
        order = null;
        surcharges.clear();
    }

    private void setTblSurcharge() {
        DefaultTableModel defaultTableModel = (DefaultTableModel) listSurcharge.getTable().getModel();
        defaultTableModel.setRowCount(0);
        SurchargeService surchargeService = new SurchargeService();
        List<Surcharge> surcharges = surchargeService.getAllSurcharges();
        for (var s : surcharges) {
            defaultTableModel.addRow(new Object[]{
                    s.getSurchargeId(),
                    s.getName(),
                    Constants.VND_FORMAT.format(s.getPrice())
            });
        }
    }

    private void setAmenityList(List<OrderDetail> orderDetailsByOrderId) {
        BigDecimal total = BigDecimal.ZERO;
        for (var e : orderDetailsByOrderId) {
            infoPayment1.addAmenity(e.getAmenity().getNameAmenity(),
                    e.getAmenity().getPrice(),
                    String.valueOf(e.getQuantity()));
            total = total.add(e.getAmenity().getPrice().multiply(BigDecimal.valueOf(e.getQuantity())));
        }
        BigDecimal totalSurcharge = BigDecimal.valueOf(Constants.parseVND(infoPayment1.getLblTotalAmenitiAndSurchargeValue().getText()));
        infoPayment1.getLblTotalAmenitiAndSurchargeValue().setText(Constants.VND_FORMAT.format(totalSurcharge.add(total)));
    }

    private void setTblRoom(List<Booking> bookings, Long orderId) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        DefaultTableModel defaultTableModel = (DefaultTableModel) infoPayment1.getTblRoom().getModel();
        defaultTableModel.setRowCount(0);
        System.out.println(bookings);
        int countHoliday = 0;
        for (var b : bookings) {
            String time = getDuration(b.getCheckInDate(), b.getCheckOutDate(), b.getBookingType());
            totalPrice = totalPrice.add(b.getTotalPrice());
            defaultTableModel.addRow(new Object[]{
                    b.getRoom().getRoomNumber(),
                    b.getRoom().getRoomType().getName(),
                    getPriceRoom(b.getRoom().getRoomType().getRoomTypeId(), b.getBookingType().toString()),
                    time,
                    b.getTotalPrice()
            });

            if (b.isHoliday(b.getCheckInDate().toLocalDate(), b.getCheckOutDate().toLocalDate())) {
                countHoliday++;
            }

        }
        BigDecimal total = BigDecimal.ZERO;
        // thêm phụ phí ngày lễ
        if (countHoliday != 0) {
            Surcharge holidaySurcharge = surchargeService.getSurchargeByName("Phụ thu ngày lễ");
            if (holidaySurcharge != null) {
                total = holidaySurcharge.getPrice();
                addSurchargeId(holidaySurcharge);
                infoPayment1.addSurcharge(holidaySurcharge.getName() + ": " + holidaySurcharge.getName(), holidaySurcharge.getPrice(), String.valueOf(countHoliday));
                total = total.multiply(BigDecimal.valueOf(countHoliday));
                surchargeDetailService.save(new SurchargeDetail(holidaySurcharge, countHoliday), orderId);
            }
        }
        infoPayment1.getLblTotalAmenitiAndSurchargeValue().setText(Constants.VND_FORMAT.format(total.doubleValue()));
        infoPayment1.getLblPriceRoomValue().setText(Constants.VND_FORMAT.format(totalPrice));
    }

    private String getPriceRoom(String roomTypeId, String bookingType) {

        String fileName = "prices.properties";

        if (roomTypeId.equals("SINGLE")) {
            return switch (BookingType.valueOf(bookingType)) {
                case DAILY -> PropertiesService.get(fileName, "/price.single.daily");
                case HOURLY -> PropertiesService.get(fileName, "/price.single.hourly");
                case OVERNIGHT -> PropertiesService.get(fileName, "/price.single.overnight");
            };
        } else {
            return switch (BookingType.valueOf(bookingType)) {
                case DAILY -> PropertiesService.get(fileName, "/price.double.daily");
                case HOURLY -> PropertiesService.get(fileName, "/price.double.hourly");
                case OVERNIGHT -> PropertiesService.get(fileName, "/price.double.overnight");
            };
        }
    }

    private String getDuration(LocalDateTime checkInDate, LocalDateTime checkOutDate, BookingType bookingType) {
        switch (bookingType) {
            case HOURLY -> {
                return ChronoUnit.HOURS.between(checkInDate, checkOutDate) + "Giờ";
            }
            case OVERNIGHT -> {
                return "1 Đêm";
            }
            case DAILY -> {
                return ChronoUnit.DAYS.between(checkInDate, checkInDate) + 1 + "Ngày";
            }
        }
        return "N/A";
    }


    public void setCurrentEmployee(Employee currentEmployee) {
        this.currentEmployee = currentEmployee;
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