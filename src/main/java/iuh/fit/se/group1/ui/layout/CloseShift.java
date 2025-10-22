/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.layout;

import iuh.fit.se.group1.ui.component.HeaderShift;
import iuh.fit.se.group1.ui.component.custom.Button;
import iuh.fit.se.group1.ui.component.custom.TextField;
import iuh.fit.se.group1.ui.component.shift.OpenDifferenceNote;
import iuh.fit.se.group1.ui.component.shift.InfoShift;
import iuh.fit.se.group1.ui.component.shift.MinPanel;
import iuh.fit.se.group1.ui.component.shift.Money;
import iuh.fit.se.group1.ui.component.shift.MoneyInTheSafe;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

/**
 *
 * @author THIS PC
 */
public class CloseShift extends javax.swing.JPanel {


    public CloseShift() {
        initComponents();
        money1.getLblMoney().setText("500.000đ");
        money2.getLblMoney().setText("200.000đ");
        money3.getLblMoney().setText("100.000đ");
        money4.getLblMoney().setText("50.000đ");
        money5.getLblMoney().setText("20.000đ");
        money6.getLblMoney().setText("10.000đ");
        money7.getLblMoney().setText("5.000đ");
        money8.getLblMoney().setText("2.000đ");
        money9.getLblMoney().setText("1.000đ");
        
      lblTitleMoneyOpenShift.setIcon(FontIcon.of(FontAwesomeSolid.MONEY_BILL_ALT,20,Color.BLACK.BLACK));
lblTitleMoneyOfSafe.setIcon(FontIcon.of(FontAwesomeSolid.COINS, 17, Color.BLACK));
lblCheckDifference.setIcon(FontIcon.of(FontAwesomeSolid.SHIELD_ALT, 17, Color.BLACK));
lblNote.setIcon(FontIcon.of(FontAwesomeSolid.PEN, 17, Color.BLACK));
btnClose.setIcon(FontIcon.of(FontAwesomeSolid.CHECK_CIRCLE, 20, Color.white));

    }

    public OpenDifferenceNote getAtTheEnd1() {
        return atTheEnd1;
    }

    public void setAtTheEnd1(OpenDifferenceNote atTheEnd1) {
        this.atTheEnd1 = atTheEnd1;
    }

    public OpenDifferenceNote getAtTheEnd2() {
        return atTheEnd2;
    }

    public void setAtTheEnd2(OpenDifferenceNote atTheEnd2) {
        this.atTheEnd2 = atTheEnd2;
    }

    public OpenDifferenceNote getAtTheEnd3() {
        return atTheEnd3;
    }

    public void setAtTheEnd3(OpenDifferenceNote atTheEnd3) {
        this.atTheEnd3 = atTheEnd3;
    }

    public Button getBtnClose() {
        return btnClose;
    }

    public void setBtnClose(Button btnClose) {
        this.btnClose = btnClose;
    }

    public HeaderShift getHeaderShift1() {
        return headerShift1;
    }

    public void setHeaderShift1(HeaderShift headerShift1) {
        this.headerShift1 = headerShift1;
    }

    public InfoShift getInfoShift1() {
        return infoShift1;
    }

    public void setInfoShift1(InfoShift infoShift1) {
        this.infoShift1 = infoShift1;
    }

    public JLabel getjLabel10() {
        return lblPrice;
    }

    public void setjLabel10(JLabel jLabel10) {
        this.lblPrice = jLabel10;
    }

    public JLabel getjLabel8() {
        return lblTotalPrice;
    }

    public void setjLabel8(JLabel jLabel8) {
        this.lblTotalPrice = jLabel8;
    }

    public JTextArea getjTextArea1() {
        return jTextArea1;
    }

    public void setjTextArea1(JTextArea jTextArea1) {
        this.jTextArea1 = jTextArea1;
    }

    public JLabel getLblCheckDifference() {
        return lblCheckDifference;
    }

    public void setLblCheckDifference(JLabel lblCheckDifference) {
        this.lblCheckDifference = lblCheckDifference;
    }

    public JLabel getLblMoneyDifference() {
        return lblMoneyDifference;
    }

    public void setLblMoneyDifference(JLabel lblMoneyDifference) {
        this.lblMoneyDifference = lblMoneyDifference;
    }

    public JLabel getLblNote() {
        return lblNote;
    }

    public void setLblNote(JLabel lblNote) {
        this.lblNote = lblNote;
    }

    public JLabel getLblReality() {
        return lblReality;
    }

    public void setLblReality(JLabel lblReality) {
        this.lblReality = lblReality;
    }

    public JLabel getLblSystem() {
        return lblSystem;
    }

    public void setLblSystem(JLabel lblSystem) {
        this.lblSystem = lblSystem;
    }

    public JLabel getLblTitleMoneyOfSafe() {
        return lblTitleMoneyOfSafe;
    }

    public void setLblTitleMoneyOfSafe(JLabel lblTitleMoneyOfSafe) {
        this.lblTitleMoneyOfSafe = lblTitleMoneyOfSafe;
    }

    public JLabel getLblTitleMoneyOpenShift() {
        return lblTitleMoneyOpenShift;
    }

    public void setLblTitleMoneyOpenShift(JLabel lblTitleMoneyOpenShift) {
        this.lblTitleMoneyOpenShift = lblTitleMoneyOpenShift;
    }

    public MinPanel getMinPanel1() {
        return minPanel1;
    }

    public void setMinPanel1(MinPanel minPanel1) {
        this.minPanel1 = minPanel1;
    }

    public MinPanel getMinPanel2() {
        return minPanel2;
    }

    public void setMinPanel2(MinPanel minPanel2) {
        this.minPanel2 = minPanel2;
    }

    public MinPanel getMinPanel3() {
        return minPanel3;
    }

    public void setMinPanel3(MinPanel minPanel3) {
        this.minPanel3 = minPanel3;
    }

    public MinPanel getMinPanel4() {
        return minPanel4;
    }

    public void setMinPanel4(MinPanel minPanel4) {
        this.minPanel4 = minPanel4;
    }

    public Money getMoney1() {
        return money1;
    }

    public void setMoney1(Money money1) {
        this.money1 = money1;
    }

    public Money getMoney2() {
        return money2;
    }

    public void setMoney2(Money money2) {
        this.money2 = money2;
    }

    public Money getMoney3() {
        return money3;
    }

    public void setMoney3(Money money3) {
        this.money3 = money3;
    }

    public Money getMoney4() {
        return money4;
    }

    public void setMoney4(Money money4) {
        this.money4 = money4;
    }

    public Money getMoney5() {
        return money5;
    }

    public void setMoney5(Money money5) {
        this.money5 = money5;
    }

    public Money getMoney6() {
        return money6;
    }

    public void setMoney6(Money money6) {
        this.money6 = money6;
    }

    public Money getMoney7() {
        return money7;
    }

    public void setMoney7(Money money7) {
        this.money7 = money7;
    }

    public Money getMoney8() {
        return money8;
    }

    public void setMoney8(Money money8) {
        this.money8 = money8;
    }

    public Money getMoney9() {
        return money9;
    }

    public void setMoney9(Money money9) {
        this.money9 = money9;
    }

    public MoneyInTheSafe getMoneyInTheSafe1() {
        return moneyInTheSafe1;
    }

    public void setMoneyInTheSafe1(MoneyInTheSafe moneyInTheSafe1) {
        this.moneyInTheSafe1 = moneyInTheSafe1;
    }

    public JScrollPane getScrNote() {
        return scrNote;
    }

    public void setScrNote(JScrollPane scrNote) {
        this.scrNote = scrNote;
    }

    public JLabel getTxtMoneyDifference() {
        return txtMoneyDifference;
    }

    public void setTxtMoneyDifference(JLabel txtMoneyDifference) {
        this.txtMoneyDifference = txtMoneyDifference;
    }

    public TextField getTxtMoneyOpenShift() {
        return txtMoneyOpenShift;
    }

    public void setTxtMoneyOpenShift(TextField txtMoneyOpenShift) {
        this.txtMoneyOpenShift = txtMoneyOpenShift;
    }

    public TextField getTxtReality() {
        return txtReality;
    }

    public void setTxtReality(TextField txtReality) {
        this.txtReality = txtReality;
    }

    public TextField getTxtSystem() {
        return txtSystem;
    }

    public void setTxtSystem(TextField txtSystem) {
        this.txtSystem = txtSystem;
    }

    
    @Override
    protected void paintComponent(Graphics g) {

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        headerShift1 = new iuh.fit.se.group1.ui.component.HeaderShift();
        infoShift1 = new iuh.fit.se.group1.ui.component.shift.InfoShift();
        moneyInTheSafe1 = new iuh.fit.se.group1.ui.component.shift.MoneyInTheSafe();
        money1 = new iuh.fit.se.group1.ui.component.shift.Money();
        money2 = new iuh.fit.se.group1.ui.component.shift.Money();
        money3 = new iuh.fit.se.group1.ui.component.shift.Money();
        money4 = new iuh.fit.se.group1.ui.component.shift.Money();
        money5 = new iuh.fit.se.group1.ui.component.shift.Money();
        money6 = new iuh.fit.se.group1.ui.component.shift.Money();
        money7 = new iuh.fit.se.group1.ui.component.shift.Money();
        money8 = new iuh.fit.se.group1.ui.component.shift.Money();
        money9 = new iuh.fit.se.group1.ui.component.shift.Money();
        lblTitleMoneyOfSafe = new javax.swing.JLabel();
        minPanel4 = new iuh.fit.se.group1.ui.component.shift.MinPanel();
        lblTotalPrice = new javax.swing.JLabel();
        lblPrice = new javax.swing.JLabel();
        atTheEnd1 = new iuh.fit.se.group1.ui.component.shift.OpenDifferenceNote();
        lblTitleMoneyOpenShift = new javax.swing.JLabel();
        txtMoneyOpenShift = new iuh.fit.se.group1.ui.component.custom.TextField();
        atTheEnd2 = new iuh.fit.se.group1.ui.component.shift.OpenDifferenceNote();
        lblCheckDifference = new javax.swing.JLabel();
        minPanel1 = new iuh.fit.se.group1.ui.component.shift.MinPanel();
        lblSystem = new javax.swing.JLabel();
        txtSystem = new iuh.fit.se.group1.ui.component.custom.TextField();
        minPanel2 = new iuh.fit.se.group1.ui.component.shift.MinPanel();
        lblReality = new javax.swing.JLabel();
        txtReality = new iuh.fit.se.group1.ui.component.custom.TextField();
        minPanel3 = new iuh.fit.se.group1.ui.component.shift.MinPanel();
        lblMoneyDifference = new javax.swing.JLabel();
        txtMoneyDifference = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        atTheEnd3 = new iuh.fit.se.group1.ui.component.shift.OpenDifferenceNote();
        lblNote = new javax.swing.JLabel();
        scrNote = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        btnClose = new iuh.fit.se.group1.ui.component.custom.Button();

        jMenu1.setText("jMenu1");

        setBackground(new java.awt.Color(241, 241, 241));

        headerShift1.setSubTitle("");
        headerShift1.setTitle("Đóng ca làm việc");

        moneyInTheSafe1.setBackground(new java.awt.Color(255, 255, 255));

        lblTitleMoneyOfSafe.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblTitleMoneyOfSafe.setText("Tiền trong két");

        minPanel4.setBackground(new java.awt.Color(153, 153, 153));
        minPanel4.setForeground(new java.awt.Color(255, 255, 255));

        lblTotalPrice.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTotalPrice.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalPrice.setText("Tổng tiền:");

        lblPrice.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblPrice.setText("10000000 VND");

        javax.swing.GroupLayout minPanel4Layout = new javax.swing.GroupLayout(minPanel4);
        minPanel4.setLayout(minPanel4Layout);
        minPanel4Layout.setHorizontalGroup(
            minPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(minPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTotalPrice)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );
        minPanel4Layout.setVerticalGroup(
            minPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(minPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(minPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalPrice)
                    .addComponent(lblPrice))
                .addGap(0, 15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout moneyInTheSafe1Layout = new javax.swing.GroupLayout(moneyInTheSafe1);
        moneyInTheSafe1.setLayout(moneyInTheSafe1Layout);
        moneyInTheSafe1Layout.setHorizontalGroup(
            moneyInTheSafe1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(moneyInTheSafe1Layout.createSequentialGroup()
                .addGroup(moneyInTheSafe1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(moneyInTheSafe1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(lblTitleMoneyOfSafe)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(moneyInTheSafe1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(moneyInTheSafe1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(money1, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                            .addComponent(money2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(money3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(money4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(money5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(money6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(money7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(money8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(money9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(minPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        moneyInTheSafe1Layout.setVerticalGroup(
            moneyInTheSafe1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(moneyInTheSafe1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitleMoneyOfSafe)
                .addGap(17, 17, 17)
                .addComponent(money1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(money2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(money3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(money4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(money5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(money6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(money7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(money8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(money9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addComponent(minPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        atTheEnd1.setBackground(new java.awt.Color(255, 255, 255));

        lblTitleMoneyOpenShift.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblTitleMoneyOpenShift.setText("Số tiền khi mở ca");

        javax.swing.GroupLayout atTheEnd1Layout = new javax.swing.GroupLayout(atTheEnd1);
        atTheEnd1.setLayout(atTheEnd1Layout);
        atTheEnd1Layout.setHorizontalGroup(
            atTheEnd1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(atTheEnd1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(atTheEnd1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(atTheEnd1Layout.createSequentialGroup()
                        .addComponent(lblTitleMoneyOpenShift)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txtMoneyOpenShift, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        atTheEnd1Layout.setVerticalGroup(
            atTheEnd1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(atTheEnd1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitleMoneyOpenShift)
                .addGap(20, 20, 20)
                .addComponent(txtMoneyOpenShift, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );

        atTheEnd2.setBackground(new java.awt.Color(255, 255, 255));

        lblCheckDifference.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblCheckDifference.setText("Kiểm tra chênh lệch");

        minPanel1.setBackground(new java.awt.Color(255, 255, 255));

        lblSystem.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblSystem.setText("Hệ thống:");

        txtSystem.setText("10000000 VND");

        javax.swing.GroupLayout minPanel1Layout = new javax.swing.GroupLayout(minPanel1);
        minPanel1.setLayout(minPanel1Layout);
        minPanel1Layout.setHorizontalGroup(
            minPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(minPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lblSystem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 123, Short.MAX_VALUE)
                .addComponent(txtSystem, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        minPanel1Layout.setVerticalGroup(
            minPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(minPanel1Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(minPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSystem)
                    .addComponent(txtSystem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        minPanel2.setBackground(new java.awt.Color(255, 255, 255));

        lblReality.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblReality.setText("Thực tế:");

        txtReality.setText("14000000 VND");
        txtReality.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRealityActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout minPanel2Layout = new javax.swing.GroupLayout(minPanel2);
        minPanel2.setLayout(minPanel2Layout);
        minPanel2Layout.setHorizontalGroup(
            minPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, minPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lblReality)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 137, Short.MAX_VALUE)
                .addComponent(txtReality, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        minPanel2Layout.setVerticalGroup(
            minPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(minPanel2Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(minPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblReality)
                    .addComponent(txtReality, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        minPanel3.setBackground(new java.awt.Color(0, 0, 0));

        lblMoneyDifference.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblMoneyDifference.setForeground(new java.awt.Color(255, 255, 255));
        lblMoneyDifference.setText("Số tiền chênh lệch:");

        txtMoneyDifference.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtMoneyDifference.setForeground(new java.awt.Color(255, 51, 0));
        txtMoneyDifference.setText("4000000 VND");

        javax.swing.GroupLayout minPanel3Layout = new javax.swing.GroupLayout(minPanel3);
        minPanel3.setLayout(minPanel3Layout);
        minPanel3Layout.setHorizontalGroup(
            minPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(minPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblMoneyDifference)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtMoneyDifference, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        minPanel3Layout.setVerticalGroup(
            minPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, minPanel3Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addGroup(minPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMoneyDifference)
                    .addComponent(txtMoneyDifference, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(13, 13, 13))
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("-   ");

        javax.swing.GroupLayout atTheEnd2Layout = new javax.swing.GroupLayout(atTheEnd2);
        atTheEnd2.setLayout(atTheEnd2Layout);
        atTheEnd2Layout.setHorizontalGroup(
            atTheEnd2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(atTheEnd2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCheckDifference)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(atTheEnd2Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(minPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(minPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, atTheEnd2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(minPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        atTheEnd2Layout.setVerticalGroup(
            atTheEnd2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(atTheEnd2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCheckDifference)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(atTheEnd2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(atTheEnd2Layout.createSequentialGroup()
                        .addGroup(atTheEnd2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(minPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(minPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, atTheEnd2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(37, 37, 37)))
                .addComponent(minPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        atTheEnd3.setBackground(new java.awt.Color(255, 255, 255));

        lblNote.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblNote.setText("Ghi chú cho ca làm");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        scrNote.setViewportView(jTextArea1);

        javax.swing.GroupLayout atTheEnd3Layout = new javax.swing.GroupLayout(atTheEnd3);
        atTheEnd3.setLayout(atTheEnd3Layout);
        atTheEnd3Layout.setHorizontalGroup(
            atTheEnd3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(atTheEnd3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblNote)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, atTheEnd3Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addComponent(scrNote, javax.swing.GroupLayout.PREFERRED_SIZE, 710, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        atTheEnd3Layout.setVerticalGroup(
            atTheEnd3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(atTheEnd3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblNote)
                .addGap(20, 20, 20)
                .addComponent(scrNote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        btnClose.setBackground(new java.awt.Color(0, 0, 0));
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setText("Xác nhận đóng ca");
        btnClose.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerShift1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(moneyInTheSafe1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(atTheEnd3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(atTheEnd2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 738, Short.MAX_VALUE)
                            .addComponent(atTheEnd1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(infoShift1, javax.swing.GroupLayout.PREFERRED_SIZE, 1194, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(233, 233, 233))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerShift1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(infoShift1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(atTheEnd1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)
                        .addComponent(atTheEnd2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)
                        .addComponent(atTheEnd3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(moneyInTheSafe1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtRealityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRealityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRealityActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCloseActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private iuh.fit.se.group1.ui.component.shift.OpenDifferenceNote atTheEnd1;
    private iuh.fit.se.group1.ui.component.shift.OpenDifferenceNote atTheEnd2;
    private iuh.fit.se.group1.ui.component.shift.OpenDifferenceNote atTheEnd3;
    private iuh.fit.se.group1.ui.component.custom.Button btnClose;
    private iuh.fit.se.group1.ui.component.HeaderShift headerShift1;
    private iuh.fit.se.group1.ui.component.shift.InfoShift infoShift1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel lblCheckDifference;
    private javax.swing.JLabel lblMoneyDifference;
    private javax.swing.JLabel lblNote;
    private javax.swing.JLabel lblPrice;
    private javax.swing.JLabel lblReality;
    private javax.swing.JLabel lblSystem;
    private javax.swing.JLabel lblTitleMoneyOfSafe;
    private javax.swing.JLabel lblTitleMoneyOpenShift;
    private javax.swing.JLabel lblTotalPrice;
    private iuh.fit.se.group1.ui.component.shift.MinPanel minPanel1;
    private iuh.fit.se.group1.ui.component.shift.MinPanel minPanel2;
    private iuh.fit.se.group1.ui.component.shift.MinPanel minPanel3;
    private iuh.fit.se.group1.ui.component.shift.MinPanel minPanel4;
    private iuh.fit.se.group1.ui.component.shift.Money money1;
    private iuh.fit.se.group1.ui.component.shift.Money money2;
    private iuh.fit.se.group1.ui.component.shift.Money money3;
    private iuh.fit.se.group1.ui.component.shift.Money money4;
    private iuh.fit.se.group1.ui.component.shift.Money money5;
    private iuh.fit.se.group1.ui.component.shift.Money money6;
    private iuh.fit.se.group1.ui.component.shift.Money money7;
    private iuh.fit.se.group1.ui.component.shift.Money money8;
    private iuh.fit.se.group1.ui.component.shift.Money money9;
    private iuh.fit.se.group1.ui.component.shift.MoneyInTheSafe moneyInTheSafe1;
    private javax.swing.JScrollPane scrNote;
    private javax.swing.JLabel txtMoneyDifference;
    private iuh.fit.se.group1.ui.component.custom.TextField txtMoneyOpenShift;
    private iuh.fit.se.group1.ui.component.custom.TextField txtReality;
    private iuh.fit.se.group1.ui.component.custom.TextField txtSystem;
    // End of variables declaration//GEN-END:variables
}
