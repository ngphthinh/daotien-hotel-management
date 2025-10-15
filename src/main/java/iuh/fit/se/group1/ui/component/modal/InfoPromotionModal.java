/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.component.modal;

import com.raven.datechooser.DateChooser;
import com.raven.datechooser.SelectedAction;
import iuh.fit.se.group1.ui.component.custom.Button;
import iuh.fit.se.group1.ui.component.custom.TextField;
import iuh.fit.se.group1.util.Constants;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

/**
 *
 * @author Windows
 */
public class InfoPromotionModal extends javax.swing.JPanel {
    private DateChooser dateChooser1;
    private DateChooser dateChooser2;

    public JLabel getLblDesciption() {
        return lblDesciption;
    }

    public void setLblDesciption(JLabel lblDesciption) {
        this.lblDesciption = lblDesciption;
    }

    public JLabel getLblDiscountPersent() {
        return lblDiscountPersent;
    }

    public void setLblDiscountPersent(JLabel lblDiscountPersent) {
        this.lblDiscountPersent = lblDiscountPersent;
    }

    public JLabel getLblErrolDiscountPersent() {
        return lblErrolDiscountPersent;
    }

    public void setLblErrolDiscountPersent(JLabel lblErrolDiscountPersent) {
        this.lblErrolDiscountPersent = lblErrolDiscountPersent;
    }

    public TextField getTxtDesciption() {
        return txtDesciption;
    }

    public void setTxtDesciption(TextField txtDesciption) {
        this.txtDesciption = txtDesciption;
    }

    public TextField getTxtDiscountPersent() {
        return txtDiscountPersent;
    }

    public void setTxtDiscountPersent(TextField txtDiscountPersent) {
        this.txtDiscountPersent = txtDiscountPersent;
    }

    public JLabel getTxtErrolDesciption() {
        return txtErrolDesciption;
    }

    public void setTxtErrolDesciption(JLabel txtErrolDesciption) {
        this.txtErrolDesciption = txtErrolDesciption;
    }

    public JLabel getLblErrolStarDate() {
        return lblErrolStarDate;
    }

    public void setLblErrolStarDate(JLabel lblErrolStarDate) {
        this.lblErrolStarDate = lblErrolStarDate;
    }

    public JLabel getLblErrolEndDate() {
        return lblErrolEndDate;
    }

    public void setLblErrolEndDate(JLabel lblErrolEndDate) {
        this.lblErrolEndDate = lblErrolEndDate;
    }

    public JLabel getLblErrolName() {
        return lblErrolName;
    }

    public void setLblErrolName(JLabel lblErrolName) {
        this.lblErrolName = lblErrolName;
    }

    public JLabel getLblErrolPrice() {
        return lblErrolPrice;
    }

    public void setLblErrolPrice(JLabel lblErrolPrice) {
        this.lblErrolPrice = lblErrolPrice;
    }
    
    
    public void setButton1(Button button1) {
        this.btnSave = button1;
    }

    public void setButton2(Button button2) {
        this.btnClose = button2;
    }
    
    public void closeModel (ActionListener ac) {
        btnClose.addActionListener(ac);
    }

    public void saveData(ActionListener ac) {
        btnSave.addActionListener(ac);
    }

    /**
     * Creates new form InfoPromotionModal
     */
    public InfoPromotionModal() {
        initComponents();
        txtEndDate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconDate2.setIcon(FontIcon.of(FontAwesomeSolid.CALENDAR_ALT, 20, Constants.COLOR_ICON_MENU));
        iconDate2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        dateChooser2 = new DateChooser();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        txtEndDate.setText(sdf.format(new Date()));
        dateChooser2.setDateFormat("dd/MM/yyyy");
        dateChooser2.toDay();
        dateChooser2.setForeground(Constants.COLOR_ICON_MENU);
        dateChooser2.addEventDateChooser((action, date) -> {
            if (action.getAction() == SelectedAction.DAY_SELECTED) {
                dateChooser2.hidePopup();
            }
        });
        dateChooser2.setTextRefernce(txtEndDate);
        iconDate2.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dateChooser2.showPopup();
            }
        });
        dateChooser2 = new DateChooser();
        dateChooser2.setTextRefernce(txtEndDate);
        try {
            var popup = DateChooser.class.getDeclaredField("popup");
            popup.setAccessible(true);
            JPopupMenu popupMenu = (JPopupMenu) popup.get(dateChooser2);
            popupMenu.setLightWeightPopupEnabled(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        txtStarDate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconDate1.setIcon(FontIcon.of(FontAwesomeSolid.CALENDAR_ALT, 20, Constants.COLOR_ICON_MENU));
        iconDate1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        dateChooser1 = new DateChooser();

        txtStarDate.setText(sdf.format(new Date()));
        dateChooser1.setDateFormat("dd/MM/yyyy");
        dateChooser1.toDay();
        dateChooser1.setForeground(Constants.COLOR_ICON_MENU);
        dateChooser1.addEventDateChooser((action, date) -> {
            if (action.getAction() == SelectedAction.DAY_SELECTED) {
                dateChooser1.hidePopup();
            }
        });
        dateChooser1.setTextRefernce(txtStarDate);
        iconDate1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dateChooser1.showPopup();
            }
        });
        dateChooser1 = new DateChooser();
        dateChooser1.setTextRefernce(txtStarDate);
        try {
            var popup = DateChooser.class.getDeclaredField("popup");
            popup.setAccessible(true);
            JPopupMenu popupMenu = (JPopupMenu) popup.get(dateChooser1);
            popupMenu.setLightWeightPopupEnabled(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
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

        lblTitle = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        lblPrice = new javax.swing.JLabel();
        lblStarDate = new javax.swing.JLabel();
        txtName = new iuh.fit.se.group1.ui.component.custom.TextField();
        txtPrice = new iuh.fit.se.group1.ui.component.custom.TextField();
        txtStarDate = new iuh.fit.se.group1.ui.component.custom.TextField();
        btnSave = new iuh.fit.se.group1.ui.component.custom.Button();
        lblEndDate = new javax.swing.JLabel();
        txtEndDate = new iuh.fit.se.group1.ui.component.custom.TextField();
        btnClose = new iuh.fit.se.group1.ui.component.custom.Button();
        iconDate1 = new javax.swing.JLabel();
        iconDate2 = new javax.swing.JLabel();
        lblErrolName = new javax.swing.JLabel();
        lblErrolPrice = new javax.swing.JLabel();
        lblErrolStarDate = new javax.swing.JLabel();
        lblErrolEndDate = new javax.swing.JLabel();
        lblDiscountPersent = new javax.swing.JLabel();
        txtDiscountPersent = new iuh.fit.se.group1.ui.component.custom.TextField();
        lblDesciption = new javax.swing.JLabel();
        txtDesciption = new iuh.fit.se.group1.ui.component.custom.TextField();
        txtErrolDesciption = new javax.swing.JLabel();
        lblErrolDiscountPersent = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTitle.setText("Thông tin khuyến mãi");

        lblName.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblName.setText("Tên khuyến mãi:");

        lblPrice.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblPrice.setText("Giá khuyến mãi:");

        lblStarDate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblStarDate.setText("Ngày tạo:");

        txtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameActionPerformed(evt);
            }
        });

        txtPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPriceActionPerformed(evt);
            }
        });

        txtStarDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtStarDateActionPerformed(evt);
            }
        });

        btnSave.setBackground(new java.awt.Color(91, 189, 64));
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setText("Lưu");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        lblEndDate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblEndDate.setText("Ngày hết hạn:");

        btnClose.setBackground(new java.awt.Color(255, 51, 0));
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setText("X");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        iconDate1.setText("   ");

        iconDate2.setText("   ");

        lblErrolName.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblErrolName.setForeground(new java.awt.Color(255, 255, 255));
        lblErrolName.setText("Tên khuyến mãi không hợp lệ");

        lblErrolPrice.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblErrolPrice.setForeground(new java.awt.Color(255, 255, 255));
        lblErrolPrice.setText("Giá khuyến mãi không hợp lệ");

        lblErrolStarDate.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblErrolStarDate.setForeground(new java.awt.Color(255, 255, 255));
        lblErrolStarDate.setText("Ngày tạo không hợp lệ");

        lblErrolEndDate.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblErrolEndDate.setForeground(new java.awt.Color(255, 255, 255));
        lblErrolEndDate.setText("Ngày hết hạn không hợp lệ");

        lblDiscountPersent.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblDiscountPersent.setText("Phần trăm:");

        txtDiscountPersent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDiscountPersentActionPerformed(evt);
            }
        });

        lblDesciption.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblDesciption.setText("Mô tả:");

        txtDesciption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDesciptionActionPerformed(evt);
            }
        });

        txtErrolDesciption.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        txtErrolDesciption.setForeground(new java.awt.Color(255, 255, 255));
        txtErrolDesciption.setText("jLabel3");

        lblErrolDiscountPersent.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblErrolDiscountPersent.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblName)
                    .addComponent(lblStarDate)
                    .addComponent(lblEndDate)
                    .addComponent(lblDesciption))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(145, 145, 145)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lblErrolName, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lblPrice)
                                        .addGap(10, 10, 10)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblErrolPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(lblTitle))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblDiscountPersent)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addComponent(lblErrolDiscountPersent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(43, 43, 43))
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtDiscountPersent, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtEndDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtStarDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblErrolEndDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lblErrolStarDate, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(txtDesciption, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtErrolDesciption, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(iconDate2, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                                    .addComponent(iconDate1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(59, 59, 59))))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblName)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPrice)
                    .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDiscountPersent)
                    .addComponent(txtDiscountPersent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblErrolName)
                    .addComponent(lblErrolPrice)
                    .addComponent(lblErrolDiscountPersent))
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblStarDate)
                    .addComponent(txtStarDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(iconDate1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblErrolStarDate)
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEndDate)
                    .addComponent(iconDate2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblErrolEndDate)
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDesciption)
                    .addComponent(txtDesciption, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtErrolDesciption)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPriceActionPerformed

    private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNameActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCloseActionPerformed

    private void txtStarDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStarDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStarDateActionPerformed

    private void txtDesciptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDesciptionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDesciptionActionPerformed

    private void txtDiscountPersentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDiscountPersentActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDiscountPersentActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private iuh.fit.se.group1.ui.component.custom.Button btnClose;
    private iuh.fit.se.group1.ui.component.custom.Button btnSave;
    private javax.swing.JLabel iconDate1;
    private javax.swing.JLabel iconDate2;
    private javax.swing.JLabel lblDesciption;
    private javax.swing.JLabel lblDiscountPersent;
    private javax.swing.JLabel lblEndDate;
    private javax.swing.JLabel lblErrolDiscountPersent;
    private javax.swing.JLabel lblErrolEndDate;
    private javax.swing.JLabel lblErrolName;
    private javax.swing.JLabel lblErrolPrice;
    private javax.swing.JLabel lblErrolStarDate;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblPrice;
    private javax.swing.JLabel lblStarDate;
    private javax.swing.JLabel lblTitle;
    private iuh.fit.se.group1.ui.component.custom.TextField txtDesciption;
    private iuh.fit.se.group1.ui.component.custom.TextField txtDiscountPersent;
    private iuh.fit.se.group1.ui.component.custom.TextField txtEndDate;
    private javax.swing.JLabel txtErrolDesciption;
    private iuh.fit.se.group1.ui.component.custom.TextField txtName;
    private iuh.fit.se.group1.ui.component.custom.TextField txtPrice;
    private iuh.fit.se.group1.ui.component.custom.TextField txtStarDate;
    // End of variables declaration//GEN-END:variables
}
