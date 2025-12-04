/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.component.booking2;

import iuh.fit.se.group1.ui.component.custom.Button;
import iuh.fit.se.group1.ui.component.custom.Combobox;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;

/**
 * @author THIS PC
 */
public class MainFlow1 extends javax.swing.JPanel {


    /**
     * Creates new form MainFlow1
     */
    public MainFlow1() {
        initComponents();
        pnl1.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(18),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)
        ));
        cbmBookingType.addItem("Theo giờ");
        cbmBookingType.addItem("Theo ngày");
        cbmBookingType.addItem("Qua đêm");
        for (int i = 1; i < 16; i++) {
            cbmTime.addItem(i + " giờ");
        }
        btnAdultDecrement.setText("");
        btnChildDecrement.setText("");
        btnAdultDecrement.setIcon(FontIcon.of(FontAwesomeSolid.MINUS));
        btnChildDecrement.setIcon(FontIcon.of(FontAwesomeSolid.MINUS));
        btnAdultDecrement.setPreferredSize(new Dimension(30, 30));
        btnChildDecrement.setPreferredSize(new Dimension(30, 30));
        btnAdultIncrement.setText("");
        btnChildIncrement.setText("");
        btnAdultIncrement.setIcon(FontIcon.of(FontAwesomeSolid.PLUS));
        btnChildIncrement.setIcon(FontIcon.of(FontAwesomeSolid.PLUS));
        btnAdultIncrement.setPreferredSize(new Dimension(30, 30));
        btnChildIncrement.setPreferredSize(new Dimension(30, 30));
        txtCheckInDate.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),       // border ngoài
                BorderFactory.createEmptyBorder(5, 10, 5, 10)       // padding trong
        ));
        txtCheckOutDate.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),       // border ngoài
                BorderFactory.createEmptyBorder(5, 10, 5, 10)       // padding trong
        ));

        txtCheckInDate.setEditable(false);
        txtCheckOutDate.setEditable(false);
        resetInputDate();

    }

    public JTextField getTxtCheckInDate() {
        return txtCheckInDate;
    }

    public void setTxtCheckInDate(JTextField txtCheckInDate) {
        this.txtCheckInDate = txtCheckInDate;
    }

    public JTextField getTxtCheckOutDate() {
        return txtCheckOutDate;
    }

    public void setTxtCheckOutDate(JTextField txtCheckOutDate) {
        this.txtCheckOutDate = txtCheckOutDate;
    }

    public JTextField getTxtNumberOfAdult() {
        return txtNumberOfAdult;
    }

    public void setTxtNumberOfAdult(JTextField txtNumberOfAdult) {
        this.txtNumberOfAdult = txtNumberOfAdult;
    }

    public JTextField getTxtNumberOfChildren() {
        return txtNumberOfChildren;
    }

    public void setTxtNumberOfChildren(JTextField txtNumberOfChildren) {
        this.txtNumberOfChildren = txtNumberOfChildren;
    }

    public Combobox getCbmTime() {
        return cbmTime;
    }

    public void setCbmTime(Combobox cbmTime) {
        this.cbmTime = cbmTime;
    }

    public Button getBtnNext() {
        return btnNext;
    }

    public void setBtnNext(Button btnNext) {
        this.btnNext = btnNext;
    }

    public Combobox getCbmBookingType() {
        return cbmBookingType;
    }

    public void setCbmBookingType(Combobox cbmBookingType) {
        this.cbmBookingType = cbmBookingType;
    }

    public void setVisiableTimeBooking(boolean b) {
        cbmTime.setEnabled(b);
        if (b) {
            cbmTime.setBackground(new java.awt.Color(255, 255, 255));
        } else {
            cbmTime.setBackground(new java.awt.Color(240, 240, 240));
        }
    }


    public void resetInputDate() {
        LocalDateTime now = LocalDateTime.now();
        String format = "dd/MM/yyyy HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        if (cbmBookingType.getSelectedIndex() == 2) {
            now = now.withHour(20).withMinute(0);
            txtCheckInDate.setText(now.format(formatter));
            now = now.plusDays(1).withHour(10).withMinute(0);
            txtCheckOutDate.setText(now.format(formatter));
        }else if (cbmBookingType.getSelectedIndex() == 1) {
            now = now.withHour(14).withMinute(0);
            txtCheckInDate.setText(now.format(formatter));
            txtCheckOutDate.setText(format);
        }else if (cbmBookingType.getSelectedIndex() == 0) {
            txtCheckInDate.setText(now.format(formatter));
            now = now.plusHours(1);
            txtCheckOutDate.setText(now.format(formatter));
        }
        cbmTime.setSelectedIndex(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnl1 = new javax.swing.JPanel();
        lbl1 = new javax.swing.JLabel();
        lbl2 = new javax.swing.JLabel();
        lbl3 = new javax.swing.JLabel();
        cbmBookingType = new iuh.fit.se.group1.ui.component.custom.Combobox();
        lbl4 = new javax.swing.JLabel();
        lbl5 = new javax.swing.JLabel();
        txtCheckInDate = new javax.swing.JTextField();
        txtCheckOutDate = new javax.swing.JTextField();
        lbl6 = new javax.swing.JLabel();
        btnNext = new iuh.fit.se.group1.ui.component.custom.Button();
        lbl7 = new javax.swing.JLabel();
        cbmTime = new iuh.fit.se.group1.ui.component.custom.Combobox();
        btnAdultDecrement = new iuh.fit.se.group1.ui.component.custom.Button();
        txtNumberOfAdult = new javax.swing.JTextField();
        btnAdultIncrement = new iuh.fit.se.group1.ui.component.custom.Button();
        lbl8 = new javax.swing.JLabel();
        btnChildDecrement = new iuh.fit.se.group1.ui.component.custom.Button();
        txtNumberOfChildren = new javax.swing.JTextField();
        btnChildIncrement = new iuh.fit.se.group1.ui.component.custom.Button();

        setBackground(new java.awt.Color(255, 255, 255));
        setForeground(new java.awt.Color(241, 241, 241));

        pnl1.setBackground(new java.awt.Color(185, 215, 254));

        lbl1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lbl1.setForeground(new java.awt.Color(0, 0, 0));
        lbl1.setText("Nhập thông tin yêu cầu");

        lbl2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl2.setForeground(new java.awt.Color(0, 0, 0));
        lbl2.setText("Vui lòng chọn đầy đủ thông tin để tìm phòng phù hợp");

        javax.swing.GroupLayout pnl1Layout = new javax.swing.GroupLayout(pnl1);
        pnl1.setLayout(pnl1Layout);
        pnl1Layout.setHorizontalGroup(
            pnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(pnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl2, javax.swing.GroupLayout.PREFERRED_SIZE, 557, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(189, Short.MAX_VALUE))
        );
        pnl1Layout.setVerticalGroup(
            pnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl1Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(lbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lbl3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl3.setForeground(new java.awt.Color(0, 0, 0));
        lbl3.setText("Hình thức thuê phòng:");

        cbmBookingType.setBackground(new java.awt.Color(255, 255, 255));
        cbmBookingType.setForeground(new java.awt.Color(51, 51, 51));
        cbmBookingType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbmBookingTypeActionPerformed(evt);
            }
        });

        lbl4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl4.setForeground(new java.awt.Color(0, 0, 0));
        lbl4.setText("Thời gian trả phòng:");

        lbl5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl5.setForeground(new java.awt.Color(0, 0, 0));
        lbl5.setText("Thời gian nhận phòng:");

        txtCheckInDate.setBackground(new java.awt.Color(255, 255, 255));
        txtCheckInDate.setForeground(new java.awt.Color(51, 51, 51));
        txtCheckInDate.setText("dd/MM/yyyy hh:mm");
        txtCheckInDate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtCheckInDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCheckInDateActionPerformed(evt);
            }
        });

        txtCheckOutDate.setBackground(new java.awt.Color(255, 255, 255));
        txtCheckOutDate.setForeground(new java.awt.Color(51, 51, 51));
        txtCheckOutDate.setText("dd/MM/yyyy hh:mm");

        lbl6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl6.setForeground(new java.awt.Color(0, 0, 0));
        lbl6.setText("Số người lớn");

        btnNext.setBackground(new java.awt.Color(77, 134, 168));
        btnNext.setForeground(new java.awt.Color(255, 255, 255));
        btnNext.setText("TIẾP THEO");
        btnNext.setBorderRadius(5);
        btnNext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        lbl7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl7.setForeground(new java.awt.Color(0, 0, 0));
        lbl7.setText("Thời gian thuê phòng:");

        cbmTime.setBackground(new java.awt.Color(255, 255, 255));
        cbmTime.setForeground(new java.awt.Color(51, 51, 51));

        btnAdultDecrement.setBackground(new java.awt.Color(255, 255, 255));
        btnAdultDecrement.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnAdultDecrement.setForeground(new java.awt.Color(0, 0, 0));
        btnAdultDecrement.setText("-");
        btnAdultDecrement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdultDecrementActionPerformed(evt);
            }
        });

        txtNumberOfAdult.setBackground(new java.awt.Color(255, 255, 255));
        txtNumberOfAdult.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNumberOfAdult.setText("0");
        txtNumberOfAdult.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnAdultIncrement.setBackground(new java.awt.Color(255, 255, 255));
        btnAdultIncrement.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnAdultIncrement.setForeground(new java.awt.Color(0, 0, 0));
        btnAdultIncrement.setText("-");
        btnAdultIncrement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdultIncrementActionPerformed(evt);
            }
        });

        lbl8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl8.setForeground(new java.awt.Color(0, 0, 0));
        lbl8.setText("Số trẻ em");

        btnChildDecrement.setBackground(new java.awt.Color(255, 255, 255));
        btnChildDecrement.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnChildDecrement.setForeground(new java.awt.Color(0, 0, 0));
        btnChildDecrement.setText("-");
        btnChildDecrement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChildDecrementActionPerformed(evt);
            }
        });

        txtNumberOfChildren.setBackground(new java.awt.Color(255, 255, 255));
        txtNumberOfChildren.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNumberOfChildren.setText("0");
        txtNumberOfChildren.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnChildIncrement.setBackground(new java.awt.Color(255, 255, 255));
        btnChildIncrement.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnChildIncrement.setForeground(new java.awt.Color(0, 0, 0));
        btnChildIncrement.setText("-");
        btnChildIncrement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChildIncrementActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(42, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbl8, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(43, 43, 43)
                                .addComponent(btnChildDecrement, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtNumberOfChildren, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnChildIncrement, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbl5, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbl4, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbl7, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbl3, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbl6, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(28, 28, 28)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtCheckOutDate, javax.swing.GroupLayout.PREFERRED_SIZE, 503, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtCheckInDate, javax.swing.GroupLayout.PREFERRED_SIZE, 503, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cbmBookingType, javax.swing.GroupLayout.PREFERRED_SIZE, 503, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cbmTime, javax.swing.GroupLayout.PREFERRED_SIZE, 503, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnAdultDecrement, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(txtNumberOfAdult, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnAdultIncrement, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(16, 16, 16))))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(37, Short.MAX_VALUE)
                        .addComponent(pnl1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 51, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(pnl1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbmBookingType, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCheckInDate, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl7, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbmTime, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCheckOutDate, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAdultDecrement, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdultIncrement, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNumberOfAdult, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnChildDecrement, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNumberOfChildren, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnChildIncrement, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNextActionPerformed

    private void txtCheckInDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCheckInDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCheckInDateActionPerformed

    private void btnAdultDecrementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdultDecrementActionPerformed
        if (Integer.parseInt(txtNumberOfAdult.getText()) > 0) {
            int currentValue = Integer.parseInt(txtNumberOfAdult.getText());
            txtNumberOfAdult.setText(String.valueOf(currentValue - 1));
        }
    }//GEN-LAST:event_btnAdultDecrementActionPerformed

    private void btnAdultIncrementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdultIncrementActionPerformed
        int currentValue = Integer.parseInt(txtNumberOfAdult.getText());
        txtNumberOfAdult.setText(String.valueOf(currentValue + 1));
    }//GEN-LAST:event_btnAdultIncrementActionPerformed

    private void btnChildDecrementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChildDecrementActionPerformed
        if (Integer.parseInt(txtNumberOfChildren.getText()) > 0) {
            int currentValue = Integer.parseInt(txtNumberOfChildren.getText());
            txtNumberOfChildren.setText(String.valueOf(currentValue - 1));
        }
    }//GEN-LAST:event_btnChildDecrementActionPerformed

    private void btnChildIncrementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChildIncrementActionPerformed
        int currentValue = Integer.parseInt(txtNumberOfChildren.getText());
        txtNumberOfChildren.setText(String.valueOf(currentValue + 1));
    }//GEN-LAST:event_btnChildIncrementActionPerformed

    private void cbmBookingTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbmBookingTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbmBookingTypeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private iuh.fit.se.group1.ui.component.custom.Button btnAdultDecrement;
    private iuh.fit.se.group1.ui.component.custom.Button btnAdultIncrement;
    private iuh.fit.se.group1.ui.component.custom.Button btnChildDecrement;
    private iuh.fit.se.group1.ui.component.custom.Button btnChildIncrement;
    private iuh.fit.se.group1.ui.component.custom.Button btnNext;
    private iuh.fit.se.group1.ui.component.custom.Combobox cbmBookingType;
    private iuh.fit.se.group1.ui.component.custom.Combobox cbmTime;
    private javax.swing.JLabel lbl1;
    private javax.swing.JLabel lbl2;
    private javax.swing.JLabel lbl3;
    private javax.swing.JLabel lbl4;
    private javax.swing.JLabel lbl5;
    private javax.swing.JLabel lbl6;
    private javax.swing.JLabel lbl7;
    private javax.swing.JLabel lbl8;
    private javax.swing.JPanel pnl1;
    private javax.swing.JTextField txtCheckInDate;
    private javax.swing.JTextField txtCheckOutDate;
    private javax.swing.JTextField txtNumberOfAdult;
    private javax.swing.JTextField txtNumberOfChildren;
    // End of variables declaration//GEN-END:variables
}
