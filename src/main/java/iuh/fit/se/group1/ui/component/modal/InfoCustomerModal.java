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
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;

/**
 *
 * @author Windows
 */
public class InfoCustomerModal extends javax.swing.JPanel {
    private DateChooser dateChooser;
    public Button getButton2() {
        return btnClose;
    }

    public JLabel getLblErrolEmail() {
        return lblErrolEmail;
    }

    public void setLblErrolEmail(JLabel lblErrolEmail) {
        this.lblErrolEmail = lblErrolEmail;
    }

    public void setButton2(Button button2) {
        this.btnClose = button2;
    }

    public JLabel getLblErrolAddress() {
        return lblErrolAddress;
    }

    public void setLblErrolAddress(JLabel lblErrolAddress) {
        this.lblErrolAddress = lblErrolAddress;
    }

    public JLabel getLblErrolCitizen() {
        return lblErrolCitizen;
    }

    public void setLblErrolCitizen(JLabel lblErrolCitizen) {
        this.lblErrolCitizen = lblErrolCitizen;
    }

    public JLabel getLblErrolDob() {
        return lblErrolDob;
    }

    public void setLblErrolDob(JLabel lblErrolDob) {
        this.lblErrolDob = lblErrolDob;
    }

    public JLabel getLblErrolName() {
        return lblErrolName;
    }

    public void setLblErrolName(JLabel lblErrolName) {
        this.lblErrolName = lblErrolName;
    }

    public JLabel getLblErrolPhone() {
        return lblErrolPhone;
    }

    public void setLblErrolPhone(JLabel lblErrolPhone) {
        this.lblErrolPhone = lblErrolPhone;
    }
    
    

    public TextField getTxtAddress() {
        return txtAddress;
    }

    public void setTxtAddress(TextField txtAddress) {
        this.txtAddress = txtAddress;
    }

    public TextField getTxtCitizen() {
        return txtCitizen;
    }

    public void setTxtCitizen(TextField txtCitizen) {
        this.txtCitizen = txtCitizen;
    }

    public TextField getTxtDob() {
        return txtDob;
    }

    public void setTxtDob(TextField txtDob) {
        this.txtDob = txtDob;
    }

    public TextField getTxtEmail() {
        return txtEmail;
    }

    public void setTxtEmail(TextField txtEmail) {
        this.txtEmail = txtEmail;
    }



    public TextField getTxtPhone() {
        return txtPhone;
    }

    public void setTxtPhone(TextField txtPhone) {
        this.txtPhone = txtPhone;
    }

    
    /**
     * Creates new form InfoCustomerModal
     */
    public InfoCustomerModal() {
        initComponents();
        cmbGender.removeAllItems();
        cmbGender.addItem("Nam");
        cmbGender.setLightWeightPopupEnabled(false);
        cmbGender.addItem("Nữ");
        cmbGender.setBackground(new java.awt.Color(240, 248, 255)); 
        cmbGender.setForeground(new java.awt.Color(51, 51, 51));     
        cmbGender.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12)); 
        txtDob.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconDate.setIcon(FontIcon.of(FontAwesomeSolid.CALENDAR_ALT, 20, Constants.COLOR_ICON_MENU));
        iconDate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        dateChooser = new DateChooser();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        txtDob.setText(sdf.format(new Date()));
        dateChooser.setDateFormat("dd/MM/yyyy");
        dateChooser.toDay();
        dateChooser.setForeground(Constants.COLOR_ICON_MENU);
        dateChooser.addEventDateChooser((action, date) -> {
            if (action.getAction() == SelectedAction.DAY_SELECTED) {
                dateChooser.hidePopup();
            }
        });
        dateChooser.setTextRefernce(txtDob);
        iconDate.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dateChooser.showPopup(txtDob, 0, txtDob.getHeight());
            }
        });
        dateChooser = new DateChooser();
        dateChooser.setTextRefernce(txtDob);
        try {
            var popup = DateChooser.class.getDeclaredField("popup");
            popup.setAccessible(true);
            JPopupMenu popupMenu = (JPopupMenu) popup.get(dateChooser);
            popupMenu.setLightWeightPopupEnabled(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void closeModel (ActionListener ac) {
        btnClose.addActionListener(ac);
    }

    public void saveData(ActionListener ac) {
        btnSave.addActionListener(ac);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jProgressBar1 = new javax.swing.JProgressBar();
        lblTitle = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        lblPhone = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblGender = new javax.swing.JLabel();
        lblCitizen = new javax.swing.JLabel();
        lblAddress = new javax.swing.JLabel();
        lblDob = new javax.swing.JLabel();
        txtName = new iuh.fit.se.group1.ui.component.custom.TextField();
        txtPhone = new iuh.fit.se.group1.ui.component.custom.TextField();
        txtEmail = new iuh.fit.se.group1.ui.component.custom.TextField();
        txtCitizen = new iuh.fit.se.group1.ui.component.custom.TextField();
        txtAddress = new iuh.fit.se.group1.ui.component.custom.TextField();
        btnSave = new iuh.fit.se.group1.ui.component.custom.Button();
        txtDob = new iuh.fit.se.group1.ui.component.custom.TextField();
        btnClose = new iuh.fit.se.group1.ui.component.custom.Button();
        iconDate = new javax.swing.JLabel();
        lblErrolName = new javax.swing.JLabel();
        lblErrolPhone = new javax.swing.JLabel();
        lblErrolEmail = new javax.swing.JLabel();
        lblErrolCitizen = new javax.swing.JLabel();
        lblErrolAddress = new javax.swing.JLabel();
        lblErrolDob = new javax.swing.JLabel();
        cmbGender = new iuh.fit.se.group1.ui.component.custom.Combobox();

        setBackground(new java.awt.Color(255, 255, 255));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTitle.setText("Thông tin khách hàng");

        lblName.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblName.setText("Tên khách hàng:");

        lblPhone.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblPhone.setText("Số điện thoại:");

        lblEmail.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblEmail.setText("Email:");

        lblGender.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblGender.setText("Giới tính:");

        lblCitizen.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCitizen.setText("Căn cước công dân:");

        lblAddress.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblAddress.setText("Địa chỉ:");

        lblDob.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblDob.setText("Ngày sinh");

        txtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameActionPerformed(evt);
            }
        });

        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });

        txtCitizen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCitizenActionPerformed(evt);
            }
        });

        txtAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAddressActionPerformed(evt);
            }
        });

        btnSave.setBackground(new java.awt.Color(91, 189, 64));
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setText("Lưu");
        btnSave.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        txtDob.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDobActionPerformed(evt);
            }
        });

        btnClose.setBackground(new java.awt.Color(255, 51, 0));
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setText("X");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        iconDate.setText("   ");

        lblErrolName.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblErrolName.setForeground(new java.awt.Color(255, 255, 255));
        lblErrolName.setText("Tên khách hàng không hợp lệ");

        lblErrolPhone.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblErrolPhone.setForeground(new java.awt.Color(255, 255, 255));
        lblErrolPhone.setText("Số điện thoại không hợp lệ");

        lblErrolEmail.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblErrolEmail.setForeground(new java.awt.Color(255, 255, 255));
        lblErrolEmail.setText("Email không hợp lệ");

        lblErrolCitizen.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblErrolCitizen.setForeground(new java.awt.Color(255, 255, 255));
        lblErrolCitizen.setText("Căn cước công dân không hợp lệ");

        lblErrolAddress.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblErrolAddress.setForeground(new java.awt.Color(255, 255, 255));
        lblErrolAddress.setText("Địa chỉ không hợp lệ");

        lblErrolDob.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblErrolDob.setForeground(new java.awt.Color(255, 255, 255));
        lblErrolDob.setText("Ngày sinh không hợp lệ");

        cmbGender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbGenderActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblTitle)
                        .addGap(196, 196, 196))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblEmail)
                            .addComponent(lblPhone)
                            .addComponent(lblName)
                            .addComponent(lblAddress)
                            .addComponent(lblDob))
                        .addGap(59, 59, 59)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtAddress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtDob, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtName, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                                            .addComponent(txtPhone, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(61, 61, 61)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblGender)
                                            .addComponent(lblCitizen, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(lblErrolName)
                                    .addComponent(lblErrolEmail)
                                    .addComponent(lblErrolPhone))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblErrolCitizen)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(cmbGender, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                                        .addComponent(txtCitizen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblErrolDob)
                                    .addComponent(lblErrolAddress))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(iconDate, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31))
            .addGroup(layout.createSequentialGroup()
                .addGap(332, 332, 332)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitle)
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblName)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblGender)
                    .addComponent(cmbGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblErrolName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPhone)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblCitizen)
                        .addComponent(txtCitizen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblErrolPhone)
                    .addComponent(lblErrolCitizen))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEmail))
                .addGap(4, 4, 4)
                .addComponent(lblErrolEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAddress))
                .addGap(3, 3, 3)
                .addComponent(lblErrolAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(iconDate)
                    .addComponent(txtDob, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDob))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblErrolDob)
                .addGap(37, 37, 37)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailActionPerformed

    private void txtAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAddressActionPerformed

    private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNameActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCloseActionPerformed

    private void txtDobActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDobActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDobActionPerformed

    private void txtCitizenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCitizenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCitizenActionPerformed

    private void cmbGenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbGenderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbGenderActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private iuh.fit.se.group1.ui.component.custom.Button btnClose;
    private iuh.fit.se.group1.ui.component.custom.Button btnSave;
    private iuh.fit.se.group1.ui.component.custom.Combobox cmbGender;
    private javax.swing.JLabel iconDate;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JLabel lblAddress;
    private javax.swing.JLabel lblCitizen;
    private javax.swing.JLabel lblDob;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblErrolAddress;
    private javax.swing.JLabel lblErrolCitizen;
    private javax.swing.JLabel lblErrolDob;
    private javax.swing.JLabel lblErrolEmail;
    private javax.swing.JLabel lblErrolName;
    private javax.swing.JLabel lblErrolPhone;
    private javax.swing.JLabel lblGender;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblPhone;
    private javax.swing.JLabel lblTitle;
    private iuh.fit.se.group1.ui.component.custom.TextField txtAddress;
    private iuh.fit.se.group1.ui.component.custom.TextField txtCitizen;
    private iuh.fit.se.group1.ui.component.custom.TextField txtDob;
    private iuh.fit.se.group1.ui.component.custom.TextField txtEmail;
    private iuh.fit.se.group1.ui.component.custom.TextField txtName;
    private iuh.fit.se.group1.ui.component.custom.TextField txtPhone;
    // End of variables declaration//GEN-END:variables
}
