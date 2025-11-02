/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.component.payment;

import iuh.fit.se.group1.ui.component.booking.InfoOrderPanel;
import iuh.fit.se.group1.ui.component.custom.Combobox;
import iuh.fit.se.group1.ui.component.custom.TextField;

import java.awt.*;
import java.math.BigDecimal;
import javax.swing.*;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;


public class InfoPayment extends javax.swing.JPanel {


    public JLabel getIconCustomer() {
        return iconCustomer;
    }

    public void setIconCustomer(JLabel iconCustomer) {
        this.iconCustomer = iconCustomer;
    }

    public JLabel getIconRoom() {
        return iconRoom;
    }

    public void setIconRoom(JLabel iconRoom) {
        this.iconRoom = iconRoom;
    }


    public JLabel getLblCustomer() {
        return lblCustomer;
    }

    public void setLblCustomer(JLabel lblCustomer) {
        this.lblCustomer = lblCustomer;
    }

    public JLabel getLblGender() {
        return lblGender;
    }

    public void setLblGender(JLabel lblGender) {
        this.lblGender = lblGender;
    }

    public JLabel getLblMethod() {
        return lblMethod;
    }

    public void setLblMethod(JLabel lblMethod) {
        this.lblMethod = lblMethod;
    }

    public JLabel getLblName() {
        return lblName;
    }

    public void setLblName(JLabel lblName) {
        this.lblName = lblName;
    }

    public JLabel getLblPhone() {
        return lblPhone;
    }

    public void setLblPhone(JLabel lblPhone) {
        this.lblPhone = lblPhone;
    }

    public JLabel getLblTotalAmenitiAndSurchargeValue() {
        return lblPriceTotal;
    }

    public JLabel getLblPricePayment() {
        return lblPricePayment;
    }

    public void setLblPricePayment(JLabel lblPricePayment) {
        this.lblPricePayment = lblPricePayment;
    }

    public JLabel getLblPricePromotion() {
        return lblPricePromotion;
    }

    public void setLblPricePromotion(JLabel lblPricePromotion) {
        this.lblPricePromotion = lblPricePromotion;
    }

    public JLabel getLblPriceRoomValue() {
        return lblPriceRoomValue;
    }


    public JLabel getLblPriceTotal() {
        return lblPriceTotal;
    }

    public void setLblPriceTotal(JLabel lblPriceTotal) {
        this.lblPriceTotal = lblPriceTotal;
    }

    public JLabel getLblPromotion() {
        return lblPromotion;
    }

    public void setLblPromotion(JLabel lblPromotion) {
        this.lblPromotion = lblPromotion;
    }


    public JLabel getLblRoom() {
        return lblRoom;
    }

    public void setLblRoom(JLabel lblRoom) {
        this.lblRoom = lblRoom;
    }


    public JLabel getLblTotalOrder() {
        return lblTotalOrder;
    }

    public void setLblTotalOrder(JLabel lblTotalOrder) {
        this.lblTotalOrder = lblTotalOrder;
    }

    public JLabel getLblTotalPayment() {
        return lblTotalPayment;
    }

    public void setLblTotalPayment(JLabel lblTotalPayment) {
        this.lblTotalPayment = lblTotalPayment;
    }


    public JPanel getPnlPayment() {
        return pnlPayment;
    }

    public void setPnlPayment(JPanel pnlPayment) {
        this.pnlPayment = pnlPayment;
    }


//    public JPanel getPnlSurcharges() {
//        return jPanel1;
//    }
//
//    public void setPnlSurcharges(JPanel pnlSurcharges) {
//        this.jPanel1 = pnlSurcharges;
//    }

    public JScrollPane getScrRoom() {
        return scrRoom;
    }

    public void setScrRoom(JScrollPane scrRoom) {
        this.scrRoom = scrRoom;
    }


    public JSeparator getSprCustomer() {
        return sprCustomer;
    }

    public void setSprCustomer(JSeparator sprCustomer) {
        this.sprCustomer = sprCustomer;
    }


    public JSeparator getSprRoom() {
        return sprRoom;
    }

    public void setSprRoom(JSeparator sprRoom) {
        this.sprRoom = sprRoom;
    }


    public JTable getTblRoom() {
        return tblRoom;
    }

    public void setTblRoom(JTable tblRoom) {
        this.tblRoom = tblRoom;
    }

    public Combobox getCboGender() {
        return cboGender;
    }

    public void setCboGender(Combobox cboGender) {
        this.cboGender = cboGender;
    }

    public TextField getTxtName() {
        return txtName;
    }

    public void setTxtName(TextField txtName) {
        this.txtName = txtName;
    }

    public TextField getTxtPhone() {
        return txtPhone;
    }

    public void setTxtPhone(TextField txtPhone) {
        this.txtPhone = txtPhone;
    }

    /**
     * Creates new form infoOrder
     */
    public InfoPayment() {
        initComponents();
        setOpaque(false);
        iconCustomer.setIcon(FontIcon.of(FontAwesomeSolid.USER, 20, new java.awt.Color(131, 176, 212)));
        iconCustomer.setText("");
        lblTotalOrder.setFont(new java.awt.Font("Segoe UI", Font.PLAIN, 12)); // không đậm
        lblPromotion.setFont(new java.awt.Font("Segoe UI", Font.PLAIN, 12));
        iconRoom.setIcon(FontIcon.of(FontAwesomeSolid.BED, 20, new java.awt.Color(131, 176, 212)));
        iconRoom.setText("");
        txtPhone.setEditable(false);
        // chỉnh style header bảng
        tblRoom.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        tblRoom.getTableHeader().setBackground(new java.awt.Color(204, 204, 204));
        tblRoom.getTableHeader().setForeground(Color.BLACK);
        tblRoom.setRowHeight(30);
        tblRoom.getTableHeader().setPreferredSize(new java.awt.Dimension(0, 30));
//        lblAmenity.setIcon(FontIcon.of(FontAwesomeSolid.SNOWFLAKE, 20, new java.awt.Color(131, 176, 212)));
//        lblSurcharge.setIcon(FontIcon.of(FontAwesomeSolid.FILE_INVOICE_DOLLAR, 20, new java.awt.Color(131, 176, 212)));
//        lblPromition.setIcon(FontIcon.of(FontAwesomeSolid.TAGS, 20, new java.awt.Color(131, 176, 212)));
        cboGender.addItem("Nữ");
        cboGender.addItem("Nam");
        cboGender.setBackground(Color.WHITE);
        infoPromotionOrderPanel1.setVisible(false);
    }

    public JButton getBtnCash() {
        return btnCash;
    }

    public void setBtnCash(JButton btnCash) {
        this.btnCash = btnCash;
    }

    public JButton getBtnTransfer() {
        return btnTransfer;
    }

    public void setBtnTransfer(JButton btnTransfer) {
        this.btnTransfer = btnTransfer;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int arc = 5;         // độ cong góc
        int shadowSize = 5;   // độ dày bóng
        int width = getWidth();
        int height = getHeight();

        Color shadowColor = new Color(0, 0, 0, 50);
        g2.setColor(shadowColor);
        g2.fillRoundRect(shadowSize, shadowSize, width - shadowSize, height - shadowSize, arc, arc);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, width - shadowSize, height - shadowSize, arc, arc);

        g2.dispose();

        super.paintComponent(g);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sprCustomer = new javax.swing.JSeparator();
        lblCustomer = new javax.swing.JLabel();
        iconCustomer = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        txtName = new iuh.fit.se.group1.ui.component.custom.TextField();
        lblGender = new javax.swing.JLabel();
        lblPhone = new javax.swing.JLabel();
        txtPhone = new iuh.fit.se.group1.ui.component.custom.TextField();
        pnlPayment = new javax.swing.JPanel();
        lblTotalOrder = new javax.swing.JLabel();
        lblPriceTotal = new javax.swing.JLabel();
        lblPromotion = new javax.swing.JLabel();
        lblPricePromotion = new javax.swing.JLabel();
        lblTotalPayment = new javax.swing.JLabel();
        lblPricePayment = new javax.swing.JLabel();
        lblMethod = new javax.swing.JLabel();
        btnTransfer = new javax.swing.JButton();
        btnCash = new javax.swing.JButton();
        sprRoom = new javax.swing.JSeparator();
        lblRoom = new javax.swing.JLabel();
        iconRoom = new javax.swing.JLabel();
        scrRoom = new javax.swing.JScrollPane();
        tblRoom = new javax.swing.JTable();
        cboGender = new iuh.fit.se.group1.ui.component.custom.Combobox();
        lblPriceRoom = new javax.swing.JLabel();
        lblPriceRoomValue = new javax.swing.JLabel();
        amenitySurchargePanel1 = new iuh.fit.se.group1.ui.component.payment.AmenitySurchargePanel();
        lblPromotionTitle = new javax.swing.JLabel();
        infoPromotionOrderPanel1 = new iuh.fit.se.group1.ui.component.booking.InfoPromotionOrderPanel();

        setBackground(new java.awt.Color(241, 241, 241));
        setPreferredSize(new java.awt.Dimension(650, 643));

        lblCustomer.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCustomer.setForeground(new java.awt.Color(131, 176, 212));
        lblCustomer.setText("Khách hàng");

        iconCustomer.setText("jLabel2");

        lblName.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        lblName.setText("Tên khách hàng:");

        txtName.setBorderRadius(5);
        txtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameActionPerformed(evt);
            }
        });

        lblGender.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        lblGender.setText("Giới tính:");

        lblPhone.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        lblPhone.setText("SĐT:");

        txtPhone.setBorderRadius(5);
        txtPhone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPhoneActionPerformed(evt);
            }
        });

        pnlPayment.setBackground(new java.awt.Color(204, 204, 204));

        lblTotalOrder.setText("Tổng hoá đơn:");

        lblPriceTotal.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        lblPriceTotal.setForeground(new java.awt.Color(153, 153, 153));
        lblPriceTotal.setText("18.000.000đ");

        lblPromotion.setText("Khuyến mãi áp dụng:");

        lblPricePromotion.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        lblPricePromotion.setForeground(new java.awt.Color(255, 51, 51));
        lblPricePromotion.setText("-500.000đ");

        lblTotalPayment.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTotalPayment.setText("Tổng thanh toán:");

        lblPricePayment.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        lblPricePayment.setForeground(new java.awt.Color(255, 51, 51));
        lblPricePayment.setText("17.500.000đ");

        lblMethod.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblMethod.setText("Hình thức thanh toán ");

        btnTransfer.setBackground(new java.awt.Color(249, 115, 22));
        btnTransfer.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTransfer.setForeground(new java.awt.Color(255, 255, 255));
        btnTransfer.setText("Chuyển khoản");
        btnTransfer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransferActionPerformed(evt);
            }
        });

        btnCash.setBackground(new java.awt.Color(249, 115, 22));
        btnCash.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCash.setForeground(new java.awt.Color(255, 255, 255));
        btnCash.setText("Tiền mặt");
        btnCash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCashActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlPaymentLayout = new javax.swing.GroupLayout(pnlPayment);
        pnlPayment.setLayout(pnlPaymentLayout);
        pnlPaymentLayout.setHorizontalGroup(
            pnlPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPaymentLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(pnlPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPaymentLayout.createSequentialGroup()
                        .addComponent(lblMethod, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPaymentLayout.createSequentialGroup()
                        .addGroup(pnlPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnlPaymentLayout.createSequentialGroup()
                                .addComponent(lblTotalPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblPricePayment, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlPaymentLayout.createSequentialGroup()
                                .addComponent(lblTotalOrder)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblPriceTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlPaymentLayout.createSequentialGroup()
                                .addComponent(lblPromotion)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblPricePromotion, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlPaymentLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnTransfer)
                                .addGap(29, 29, 29)
                                .addComponent(btnCash, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(27, 27, 27))))
        );
        pnlPaymentLayout.setVerticalGroup(
            pnlPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPaymentLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalOrder)
                    .addComponent(lblPriceTotal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPromotion)
                    .addComponent(lblPricePromotion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPricePayment)
                    .addComponent(lblTotalPayment))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblMethod)
                .addGap(0, 0, 0)
                .addGroup(pnlPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTransfer, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCash, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        lblRoom.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblRoom.setForeground(new java.awt.Color(131, 176, 212));
        lblRoom.setText("Thông tin phòng");

        iconRoom.setText("jLabel13");

        tblRoom.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Số phòng", "Loại phòng", "Giá phòng", "Thời lượng", "Tổng tiền"
            }
        ));
        scrRoom.setViewportView(tblRoom);

        cboGender.setBackground(new java.awt.Color(255, 255, 255));
        cboGender.setForeground(new java.awt.Color(0, 0, 0));

        lblPriceRoom.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblPriceRoom.setForeground(new java.awt.Color(0, 0, 0));
        lblPriceRoom.setText("Tổng tiền");

        lblPriceRoomValue.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        lblPriceRoomValue.setForeground(new java.awt.Color(0, 0, 0));
        lblPriceRoomValue.setText("0");

        lblPromotionTitle.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblPromotionTitle.setForeground(new java.awt.Color(131, 176, 212));
        lblPromotionTitle.setText("Khuyến mãi áp dụng");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sprRoom, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(iconCustomer)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblCustomer))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblGender, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblName, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cboGender, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(80, 80, 80))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sprCustomer)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(32, 32, 32)))
                .addComponent(pnlPayment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
            .addComponent(scrRoom)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(356, 356, 356)
                        .addComponent(lblPriceRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPriceRoomValue, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(iconRoom)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblRoom)))
                .addContainerGap(40, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(amenitySurchargePanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 631, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPromotionTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(infoPromotionOrderPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 603, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlPayment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(iconCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                            .addComponent(lblCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sprCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 1, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblName)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cboGender, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblGender, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPhone)
                            .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRoom, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
                    .addComponent(iconRoom))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sprRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPriceRoom)
                    .addComponent(lblPriceRoomValue))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(amenitySurchargePanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(92, 92, 92)
                .addComponent(lblPromotionTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(infoPromotionOrderPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(100, 100, 100))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNameActionPerformed

    private void txtPhoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPhoneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPhoneActionPerformed

    private void btnTransferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransferActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTransferActionPerformed

    private void btnCashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCashActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCashActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private iuh.fit.se.group1.ui.component.payment.AmenitySurchargePanel amenitySurchargePanel1;
    private javax.swing.JButton btnCash;
    private javax.swing.JButton btnTransfer;
    private iuh.fit.se.group1.ui.component.custom.Combobox cboGender;
    private javax.swing.JLabel iconCustomer;
    private javax.swing.JLabel iconRoom;
    private iuh.fit.se.group1.ui.component.booking.InfoPromotionOrderPanel infoPromotionOrderPanel1;
    private javax.swing.JLabel lblCustomer;
    private javax.swing.JLabel lblGender;
    private javax.swing.JLabel lblMethod;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblPhone;
    private javax.swing.JLabel lblPricePayment;
    private javax.swing.JLabel lblPricePromotion;
    private javax.swing.JLabel lblPriceRoom;
    private javax.swing.JLabel lblPriceRoomValue;
    private javax.swing.JLabel lblPriceTotal;
    private javax.swing.JLabel lblPromotion;
    private javax.swing.JLabel lblPromotionTitle;
    private javax.swing.JLabel lblRoom;
    private javax.swing.JLabel lblTotalOrder;
    private javax.swing.JLabel lblTotalPayment;
    private javax.swing.JPanel pnlPayment;
    private javax.swing.JScrollPane scrRoom;
    private javax.swing.JSeparator sprCustomer;
    private javax.swing.JSeparator sprRoom;
    private javax.swing.JTable tblRoom;
    private iuh.fit.se.group1.ui.component.custom.TextField txtName;
    private iuh.fit.se.group1.ui.component.custom.TextField txtPhone;
    // End of variables declaration//GEN-END:variables

    public void addAmenity(String amenityName, BigDecimal price, String quantity) {
        InfoOrderPanel amenityOrderPanel = new InfoOrderPanel();
        amenityOrderPanel.getLblName().setText(amenityName);
        amenityOrderPanel.getLblPrice().setText(price.toString());
        amenityOrderPanel.getLblQuantity().setText(quantity);
        amenitySurchargePanel1.addAmenity(amenityOrderPanel, price);
    }

    public void addSurcharge(String surchargeName, BigDecimal price, String quantity) {
        InfoOrderPanel surchargeOrderPanel = new InfoOrderPanel();
        surchargeOrderPanel.getLblName().setText(surchargeName);
        surchargeOrderPanel.getLblPrice().setText(price.toString());
        surchargeOrderPanel.getLblQuantity().setText(quantity);

        amenitySurchargePanel1.addSurcharge(surchargeOrderPanel, price);
    }

    public void addPromotion(String promotionName, String price, String quantity) {
        infoPromotionOrderPanel1.setNamePromotion(promotionName);
        infoPromotionOrderPanel1.setPrice(price);
        infoPromotionOrderPanel1.setQuantity(quantity);
        infoPromotionOrderPanel1.setVisible(true);
    }

    public void clearAmenitiesAndSurcharges() {
        amenitySurchargePanel1.clearAll();
    }

    public void clearPromotion() {
        infoPromotionOrderPanel1.setVisible(false);
    }
}
