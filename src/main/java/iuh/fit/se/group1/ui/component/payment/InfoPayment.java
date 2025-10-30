/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.component.payment;

import iuh.fit.se.group1.ui.component.booking.InfoOrderPanel;
import iuh.fit.se.group1.ui.component.booking.InfoPromotionOrderPanel;
import iuh.fit.se.group1.ui.component.booking.InfoRoomPanel;
import iuh.fit.se.group1.ui.component.custom.Combobox;
import iuh.fit.se.group1.ui.component.custom.TextField;
import iuh.fit.se.group1.ui.component.scroll.ScrollPaneWin11;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.*;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

/**
 *
 * @author Administrator
 */
public class InfoPayment extends javax.swing.JPanel {

    public JLabel getIconAmenity() {
        return iconAmenity;
    }

    public void setIconAmenity(JLabel iconAmenity) {
        this.iconAmenity = iconAmenity;
    }

    public JLabel getIconCustomer() {
        return iconCustomer;
    }

    public void setIconCustomer(JLabel iconCustomer) {
        this.iconCustomer = iconCustomer;
    }

    public JLabel getIconPromotion() {
        return iconPromotion;
    }

    public void setIconPromotion(JLabel iconPromotion) {
        this.iconPromotion = iconPromotion;
    }

    public JLabel getIconRoom() {
        return iconRoom;
    }

    public void setIconRoom(JLabel iconRoom) {
        this.iconRoom = iconRoom;
    }

    public JLabel getIconSurcharge() {
        return iconSurcharge;
    }

    public void setIconSurcharge(JLabel iconSurcharge) {
        this.iconSurcharge = iconSurcharge;
    }

    public JLabel getLblAmenity() {
        return lblAmenity;
    }

    public void setLblAmenity(JLabel lblAmenity) {
        this.lblAmenity = lblAmenity;
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

    public JLabel getLblPriceASP() {
        return lblPriceASP;
    }

    public void setLblPriceASP(JLabel lblPriceASP) {
        this.lblPriceASP = lblPriceASP;
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

    public JLabel getLblPriceRoom() {
        return lblPriceRoom;
    }

    public void setLblPriceRoom(JLabel lblPriceRoom) {
        this.lblPriceRoom = lblPriceRoom;
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

    public JLabel getLblPromotions() {
        return lblPromotions;
    }

    public void setLblPromotions(JLabel lblPromotions) {
        this.lblPromotions = lblPromotions;
    }

    public JLabel getLblRoom() {
        return lblRoom;
    }

    public void setLblRoom(JLabel lblRoom) {
        this.lblRoom = lblRoom;
    }

    public JLabel getLblSurcharge() {
        return lblSurcharge;
    }

    public void setLblSurcharge(JLabel lblSurcharge) {
        this.lblSurcharge = lblSurcharge;
    }

    public JLabel getLblTotalASP() {
        return lblTotalASP;
    }

    public void setLblTotalASP(JLabel lblTotalASP) {
        this.lblTotalASP = lblTotalASP;
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

    public JLabel getLblTotalRoom() {
        return lblTotalRoom;
    }

    public void setLblTotalRoom(JLabel lblTotalRoom) {
        this.lblTotalRoom = lblTotalRoom;
    }

    public JPanel getPnlAmenities() {
        return pnlAmenities;
    }

    public void setPnlAmenities(JPanel pnlAmenities) {
        this.pnlAmenities = pnlAmenities;
    }

    public JPanel getPnlAmenity() {
        return pnlAmenity;
    }

    public void setPnlAmenity(JPanel pnlAmenity) {
        this.pnlAmenity = pnlAmenity;
    }

    public JPanel getPnlPayment() {
        return pnlPayment;
    }

    public void setPnlPayment(JPanel pnlPayment) {
        this.pnlPayment = pnlPayment;
    }

    public JPanel getPnlPromotion() {
        return pnlPromotion;
    }

    public void setPnlPromotion(JPanel pnlPromotion) {
        this.pnlPromotion = pnlPromotion;
    }

    public JPanel getPnlPromotions() {
        return pnlPromotions;
    }

    public void setPnlPromotions(JPanel pnlPromotions) {
        this.pnlPromotions = pnlPromotions;
    }

    public JPanel getPnlSurcharge() {
        return pnlSurcharge;
    }

    public void setPnlSurcharge(JPanel pnlSurcharge) {
        this.pnlSurcharge = pnlSurcharge;
    }

    public JPanel getPnlSurcharges() {
        return pnlSurcharges;
    }

    public void setPnlSurcharges(JPanel pnlSurcharges) {
        this.pnlSurcharges = pnlSurcharges;
    }

    public JScrollPane getScrRoom() {
        return scrRoom;
    }

    public void setScrRoom(JScrollPane scrRoom) {
        this.scrRoom = scrRoom;
    }

    public JSeparator getSprAmenity() {
        return sprAmenity;
    }

    public void setSprAmenity(JSeparator sprAmenity) {
        this.sprAmenity = sprAmenity;
    }

    public JSeparator getSprCustomer() {
        return sprCustomer;
    }

    public void setSprCustomer(JSeparator sprCustomer) {
        this.sprCustomer = sprCustomer;
    }

    public JSeparator getSprEnd() {
        return sprEnd;
    }

    public void setSprEnd(JSeparator sprEnd) {
        this.sprEnd = sprEnd;
    }

    public JSeparator getSprPromotion() {
        return sprPromotion;
    }

    public void setSprPromotion(JSeparator sprPromotion) {
        this.sprPromotion = sprPromotion;
    }

    public JSeparator getSprRoom() {
        return sprRoom;
    }

    public void setSprRoom(JSeparator sprRoom) {
        this.sprRoom = sprRoom;
    }

    public JSeparator getSprSurcharge() {
        return sprSurcharge;
    }

    public void setSprSurcharge(JSeparator sprSurcharge) {
        this.sprSurcharge = sprSurcharge;
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
        iconCustomer.setIcon(FontIcon.of(FontAwesomeSolid.USER, 20, new java.awt.Color(131,176,212)));
        iconCustomer.setText("");
        lblTotalOrder.setFont(new java.awt.Font("Segoe UI", 0, 12)); // không đậm
        lblPromotion.setFont(new java.awt.Font("Segoe UI", 0, 12));
        iconRoom.setIcon(FontIcon.of(FontAwesomeSolid.BED, 20, new java.awt.Color(131,176,212)));
        iconRoom.setText("");
        txtPhone.setEditable(false);
        // chỉnh style header bảng
        tblRoom.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        tblRoom.getTableHeader().setBackground(new java.awt.Color(204, 204, 204));
        tblRoom.getTableHeader().setForeground(Color.BLACK);
        tblRoom.setRowHeight(30);
        tblRoom.getTableHeader().setPreferredSize(new java.awt.Dimension(0, 30));
        iconAmenity.setIcon(FontIcon.of(FontAwesomeSolid.SNOWFLAKE, 20, new java.awt.Color(131,176,212)));
        iconAmenity.setText("");
        iconSurcharge.setIcon(FontIcon.of(FontAwesomeSolid.FILE_INVOICE_DOLLAR, 20, new java.awt.Color(131,176,212)));
        iconSurcharge.setText("");
        iconPromotion.setIcon(FontIcon.of(FontAwesomeSolid.TAGS, 20, new java.awt.Color(131,176,212)));
        iconPromotion.setText("");
        cboGender.addItem("Nữ");
        cboGender.addItem("Nam");
        cboGender.setBackground(Color.WHITE);
//        addAmenity("Dịch vụ cho thuê chăn mền","200k","3");
//        addAmenity("Dịch vụ cho thuê chăn mền","200k","3");
//        addSurcharge("Phụ phí 1", "1", "2");
//        addPromotion("Dịch vụ cho thuê chăn mền","200k","3");
//        addPromotion("Dịch vụ cho thuê chăn mền","200k","3");addPromotion("Dịch vụ cho thuê chăn mền","200k","3");
//        addPromotion("Dịch vụ cho thuê chăn mền","200k","3");
//        addPromotion("Dịch vụ cho thuê chăn mền","200k","3");
//        addPromotion("Dịch vụ cho thuê chăn mền","200k","3");
        
        
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

    // Vẽ bóng mờ (chỉ quanh mép)
    Color shadowColor = new Color(0, 0, 0, 50);
    g2.setColor(shadowColor);
    g2.fillRoundRect(shadowSize, shadowSize, width - shadowSize, height - shadowSize, arc, arc);

    // Vẽ nền chính (không vẽ phủ toàn bộ — để nội dung vẫn hiển thị)
    g2.setColor(getBackground());
    g2.fillRoundRect(0, 0, width - shadowSize, height - shadowSize, arc, arc);

    g2.dispose();

    // ⚠️ Gọi super.paintComponent SAU KHI vẽ nền để nội dung con được hiển thị
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
        lblTotalRoom = new javax.swing.JLabel();
        lblPriceRoom = new javax.swing.JLabel();
        pnlAmenity = new javax.swing.JPanel();
        lblAmenity = new javax.swing.JLabel();
        sprAmenity = new javax.swing.JSeparator();
        iconAmenity = new javax.swing.JLabel();
        pnlAmenities = new javax.swing.JPanel();
        pnlSurcharge = new javax.swing.JPanel();
        lblSurcharge = new javax.swing.JLabel();
        iconSurcharge = new javax.swing.JLabel();
        pnlSurcharges = new javax.swing.JPanel();
        sprSurcharge = new javax.swing.JSeparator();
        pnlPromotion = new javax.swing.JPanel();
        lblPromotions = new javax.swing.JLabel();
        iconPromotion = new javax.swing.JLabel();
        sprPromotion = new javax.swing.JSeparator();
        pnlPromotions = new javax.swing.JPanel();
        sprEnd = new javax.swing.JSeparator();
        lblTotalASP = new javax.swing.JLabel();
        lblPriceASP = new javax.swing.JLabel();
        cboGender = new iuh.fit.se.group1.ui.component.custom.Combobox();

        setPreferredSize(new java.awt.Dimension(650, 643));

        lblCustomer.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCustomer.setForeground(new java.awt.Color(131, 176, 212));
        lblCustomer.setText("Khách hàng");

        iconCustomer.setText("jLabel2");

        lblName.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        lblName.setText("Tên khách hàng:");

        txtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameActionPerformed(evt);
            }
        });

        lblGender.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        lblGender.setText("Giới tính:");

        lblPhone.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        lblPhone.setText("SĐT:");

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

        lblTotalRoom.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        lblTotalRoom.setText("Tổng tiền:");

        lblPriceRoom.setFont(new java.awt.Font("Segoe UI", 2, 10)); // NOI18N
        lblPriceRoom.setText("0");

        pnlAmenity.setBackground(new java.awt.Color(255, 255, 255));

        lblAmenity.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblAmenity.setForeground(new java.awt.Color(131, 176, 212));
        lblAmenity.setText("Dịch vụ đi kèm");

        iconAmenity.setText("jLabel16");

        pnlAmenities.setBackground(new java.awt.Color(255, 255, 255));
        pnlAmenities.setLayout(new javax.swing.BoxLayout(pnlAmenities, javax.swing.BoxLayout.Y_AXIS));

        javax.swing.GroupLayout pnlAmenityLayout = new javax.swing.GroupLayout(pnlAmenity);
        pnlAmenity.setLayout(pnlAmenityLayout);
        pnlAmenityLayout.setHorizontalGroup(
            pnlAmenityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sprAmenity)
            .addGroup(pnlAmenityLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(iconAmenity)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblAmenity)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(pnlAmenities, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlAmenityLayout.setVerticalGroup(
            pnlAmenityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAmenityLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlAmenityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAmenity, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(iconAmenity))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sprAmenity, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlAmenities, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnlSurcharge.setBackground(new java.awt.Color(255, 255, 255));

        lblSurcharge.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblSurcharge.setForeground(new java.awt.Color(131, 176, 212));
        lblSurcharge.setText("Phụ phí");

        iconSurcharge.setText("jLabel25");

        pnlSurcharges.setBackground(new java.awt.Color(255, 255, 255));
        pnlSurcharges.setLayout(new javax.swing.BoxLayout(pnlSurcharges, javax.swing.BoxLayout.Y_AXIS));

        javax.swing.GroupLayout pnlSurchargeLayout = new javax.swing.GroupLayout(pnlSurcharge);
        pnlSurcharge.setLayout(pnlSurchargeLayout);
        pnlSurchargeLayout.setHorizontalGroup(
            pnlSurchargeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSurchargeLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(pnlSurchargeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlSurcharges, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlSurchargeLayout.createSequentialGroup()
                        .addComponent(iconSurcharge)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSurcharge, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 217, Short.MAX_VALUE))
                    .addComponent(sprSurcharge, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(0, 0, 0))
        );
        pnlSurchargeLayout.setVerticalGroup(
            pnlSurchargeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSurchargeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSurchargeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSurcharge, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(iconSurcharge))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sprSurcharge, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(pnlSurcharges, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlPromotion.setBackground(new java.awt.Color(255, 255, 255));

        lblPromotions.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblPromotions.setForeground(new java.awt.Color(131, 176, 212));
        lblPromotions.setText("Khuyến mãi");

        iconPromotion.setText("jLabel34");

        pnlPromotions.setBackground(new java.awt.Color(255, 255, 255));
        pnlPromotions.setLayout(new javax.swing.BoxLayout(pnlPromotions, javax.swing.BoxLayout.Y_AXIS));

        javax.swing.GroupLayout pnlPromotionLayout = new javax.swing.GroupLayout(pnlPromotion);
        pnlPromotion.setLayout(pnlPromotionLayout);
        pnlPromotionLayout.setHorizontalGroup(
            pnlPromotionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sprPromotion)
            .addGroup(pnlPromotionLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(iconPromotion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPromotions, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPromotionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlPromotions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlPromotionLayout.setVerticalGroup(
            pnlPromotionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPromotionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPromotionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPromotions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(iconPromotion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sprPromotion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlPromotions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        sprEnd.setForeground(new java.awt.Color(0, 0, 0));

        lblTotalASP.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        lblTotalASP.setText("Tổng tiền:");

        lblPriceASP.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblPriceASP.setText("0");

        cboGender.setBackground(new java.awt.Color(255, 255, 255));
        cboGender.setForeground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(iconRoom)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblRoom)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(sprEnd)
            .addComponent(scrRoom)
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
                                .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 62, Short.MAX_VALUE)))
                        .addGap(32, 32, 32)))
                .addComponent(pnlPayment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 462, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblTotalRoom)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPriceRoom)
                        .addGap(125, 125, 125))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblTotalASP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPriceASP, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(pnlAmenity, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlSurcharge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(pnlPromotion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblGender)
                            .addComponent(cboGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addComponent(scrRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPriceRoom))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlSurcharge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlAmenity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlPromotion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalASP)
                    .addComponent(lblPriceASP))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sprEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
    private javax.swing.JButton btnCash;
    private javax.swing.JButton btnTransfer;
    private iuh.fit.se.group1.ui.component.custom.Combobox cboGender;
    private javax.swing.JLabel iconAmenity;
    private javax.swing.JLabel iconCustomer;
    private javax.swing.JLabel iconPromotion;
    private javax.swing.JLabel iconRoom;
    private javax.swing.JLabel iconSurcharge;
    private javax.swing.JLabel lblAmenity;
    private javax.swing.JLabel lblCustomer;
    private javax.swing.JLabel lblGender;
    private javax.swing.JLabel lblMethod;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblPhone;
    private javax.swing.JLabel lblPriceASP;
    private javax.swing.JLabel lblPricePayment;
    private javax.swing.JLabel lblPricePromotion;
    private javax.swing.JLabel lblPriceRoom;
    private javax.swing.JLabel lblPriceTotal;
    private javax.swing.JLabel lblPromotion;
    private javax.swing.JLabel lblPromotions;
    private javax.swing.JLabel lblRoom;
    private javax.swing.JLabel lblSurcharge;
    private javax.swing.JLabel lblTotalASP;
    private javax.swing.JLabel lblTotalOrder;
    private javax.swing.JLabel lblTotalPayment;
    private javax.swing.JLabel lblTotalRoom;
    private javax.swing.JPanel pnlAmenities;
    private javax.swing.JPanel pnlAmenity;
    private javax.swing.JPanel pnlPayment;
    private javax.swing.JPanel pnlPromotion;
    private javax.swing.JPanel pnlPromotions;
    private javax.swing.JPanel pnlSurcharge;
    private javax.swing.JPanel pnlSurcharges;
    private javax.swing.JScrollPane scrRoom;
    private javax.swing.JSeparator sprAmenity;
    private javax.swing.JSeparator sprCustomer;
    private javax.swing.JSeparator sprEnd;
    private javax.swing.JSeparator sprPromotion;
    private javax.swing.JSeparator sprRoom;
    private javax.swing.JSeparator sprSurcharge;
    private javax.swing.JTable tblRoom;
    private iuh.fit.se.group1.ui.component.custom.TextField txtName;
    private iuh.fit.se.group1.ui.component.custom.TextField txtPhone;
    // End of variables declaration//GEN-END:variables

    private void addAmenity(String amenityName, String price, String quantity) {
        InfoOrderPanel amenityOrderPanel = new InfoOrderPanel();
        amenityOrderPanel.getLblName().setText(amenityName);
        amenityOrderPanel.getLblPrice().setText(price);
        amenityOrderPanel.getLblQuantity().setText(quantity);

        pnlAmenities.add(amenityOrderPanel);
        pnlAmenities.add(Box.createVerticalStrut(8));
        pnlAmenities.revalidate();
        pnlAmenities.repaint();
    }
    private void addSurcharge(String surchargeName, String price, String quantity) {
        InfoOrderPanel surchargeOrderPanel = new InfoOrderPanel();
        surchargeOrderPanel.getLblName().setText(surchargeName);
        surchargeOrderPanel.getLblPrice().setText(price);
        surchargeOrderPanel.getLblQuantity().setText(quantity);
        
        pnlSurcharges.add(surchargeOrderPanel);
        pnlSurcharges.add(Box.createVerticalStrut(8));
        pnlSurcharges.revalidate();
        pnlSurcharges.repaint();
    }
    private void addPromotion(String promotionName, String price, String quantity) {
        InfoPromotionOrderPanel promotionPanel = new InfoPromotionOrderPanel();
        promotionPanel.getLblName().setText(promotionName);
        promotionPanel.getLblPrice().setText(price);
        promotionPanel.getLblQuantity().setText(quantity);

        pnlPromotions.add(promotionPanel);
        pnlPromotions.add(Box.createVerticalStrut(8));
        
        pnlPromotions.revalidate();
        pnlPromotions.repaint();
    }

}
