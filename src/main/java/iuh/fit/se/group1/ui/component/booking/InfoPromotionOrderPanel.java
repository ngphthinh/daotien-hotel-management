package iuh.fit.se.group1.ui.component.booking;

import java.awt.Color;
import javax.swing.JLabel;

public class InfoPromotionOrderPanel extends javax.swing.JPanel {

    public InfoPromotionOrderPanel() {
        initComponents();
        setOpaque(true);
        setBackground(new Color(223, 228, 231));
    }

    public JLabel getLblName() {
        return lblName;
    }

    public JLabel getLblPrice() {
        return lblPrice;
    }

    public JLabel getLblQuantity() {
        return lblQuantity;
    }

    public void setPrice(String price) {
        lblPrice.setText(price);
    }

    public void setNamePromotion(String name) {
        lblName.setText(name);
    }

    public void setQuantity(String quantity) {
        lblQuantity.setText(quantity);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        lblName = new javax.swing.JLabel();
        lblPrice = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblQuantity = new javax.swing.JLabel();

        setBackground(new java.awt.Color(223, 228, 231));

        lblName.setFont(new java.awt.Font("Segoe UI", 1, 12));
        lblName.setForeground(new java.awt.Color(77, 134, 168));
        lblName.setText("Tên dịch vụ");

        lblPrice.setFont(new java.awt.Font("Segoe UI", 0, 10));
        lblPrice.setForeground(new java.awt.Color(255, 108, 3));
        lblPrice.setText("100k");

        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setText("Số lượng:");

        lblQuantity.setText("01");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(lblName, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                                        .addComponent(lblPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 462, Short.MAX_VALUE)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblQuantity)
                                .addGap(22, 22, 22))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(lblName)
                                                .addGap(0, 0, 0)
                                                .addComponent(lblPrice))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel3)
                                                .addComponent(lblQuantity)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }

    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblPrice;
    private javax.swing.JLabel lblQuantity;
}
