/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.component.modal;

import com.raven.datechooser.DateChooser;
import com.raven.datechooser.SelectedAction;
import iuh.fit.se.group1.ui.component.custom.Button;
import iuh.fit.se.group1.ui.component.custom.Combobox;
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
public class InfoEmployeeModal extends javax.swing.JPanel {

    public Combobox getCboGender() {
        return cmbGender;
    }

    public void setCboGender(Combobox cboGender) {
        this.cmbGender = cboGender;
    }

    public TextField getTxtEmail() {
        return txtEmail;
    }

    public void setTxtEmail(TextField txtEmail) {
        this.txtEmail = txtEmail;
    }

    public TextField getTxtHireDate() {
        return txtHireDate;
    }

    public void setTxtHireDate(TextField txtHireDate) {
        this.txtHireDate = txtHireDate;
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
     * Creates new form InfoEmployeeModal
     */
    private DateChooser dateChooser;
    public InfoEmployeeModal() {
        initComponents();
        cmbGender.removeAllItems();
        cmbGender.addItem("Nam");
        cmbGender.setLightWeightPopupEnabled(false);
        cmbGender.addItem("Nữ");
        cmbGender.setBackground(new java.awt.Color(240, 248, 255)); 
        cmbGender.setForeground(new java.awt.Color(51, 51, 51));     
        cmbGender.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12)); 
        cmbPosition.removeAllItems();
        cmbPosition.addItem("Nhân viên");
        cmbPosition.setLightWeightPopupEnabled(false);
        cmbPosition.addItem("Quản lí");
        cmbPosition.setBackground(new java.awt.Color(240, 248, 255)); 
        cmbPosition.setForeground(new java.awt.Color(51, 51, 51));     
        cmbPosition.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        txtHireDate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconDate.setIcon(FontIcon.of(FontAwesomeSolid.CALENDAR_ALT, 20, Constants.COLOR_ICON_MENU));
        iconDate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        dateChooser = new DateChooser();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        txtHireDate.setText(sdf.format(new Date()));
        dateChooser.setDateFormat("dd/MM/yyyy");
        dateChooser.toDay();
        dateChooser.setForeground(Constants.COLOR_ICON_MENU);
        dateChooser.addEventDateChooser((action, date) -> {
            if (action.getAction() == SelectedAction.DAY_SELECTED) {
                dateChooser.hidePopup();
            }
        });
        dateChooser.setTextRefernce(txtHireDate);
        iconDate.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dateChooser.showPopup(txtHireDate, 0, txtHireDate.getHeight());
            }
        });
        dateChooser = new DateChooser();
        dateChooser.setTextRefernce(txtHireDate);
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

    public JLabel getLblErrolHireDate() {
        return lblErrolHireDate;
    }

    public void setLblErrolHireDate(JLabel lblErrolHireDate) {
        this.lblErrolHireDate = lblErrolHireDate;
    }

    public JLabel getLblErrolEmail() {
        return lblErrolEmail;
    }

    public void setLblErrolEmail(JLabel lblErrolEmail) {
        this.lblErrolEmail = lblErrolEmail;
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

    public void saveData(ActionListener ac) {
        btnSave.addActionListener(ac);
    }
    
    public Button getButton1() {
        return btnSave;
    }

    public void setButton1(Button button1) {
        this.btnSave = button1;
    }

    public Button getButton2() {
        return btnClose;
    }

    public void setButton2(Button button2) {
        this.btnClose = button2;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        lblTitle = new javax.swing.JLabel();
        lblImg = new javax.swing.JLabel();
        lblCode = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        lblGender = new javax.swing.JLabel();
        lblPosition = new javax.swing.JLabel();
        lblPhone = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblHireDate = new javax.swing.JLabel();
        txtName = new iuh.fit.se.group1.ui.component.custom.TextField();
        txtPhone = new iuh.fit.se.group1.ui.component.custom.TextField();
        txtEmail = new iuh.fit.se.group1.ui.component.custom.TextField();
        txtHireDate = new iuh.fit.se.group1.ui.component.custom.TextField();
        btnSave = new iuh.fit.se.group1.ui.component.custom.Button();
        btnClose = new iuh.fit.se.group1.ui.component.custom.Button();
        iconDate = new javax.swing.JLabel();
        lblErrolName = new javax.swing.JLabel();
        lblErrolPhone = new javax.swing.JLabel();
        lblErrolEmail = new javax.swing.JLabel();
        lblErrolHireDate = new javax.swing.JLabel();
        cmbGender = new iuh.fit.se.group1.ui.component.custom.Combobox();
        cmbPosition = new iuh.fit.se.group1.ui.component.custom.Combobox();

        jScrollPane1.setViewportView(jEditorPane1);

        setBackground(new java.awt.Color(255, 255, 255));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTitle.setText("Thông tin nhân viên");

        lblImg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/login.png"))); // NOI18N

        lblCode.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCode.setForeground(new java.awt.Color(255, 102, 51));
        lblCode.setText("NV36");

        lblStatus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(0, 204, 0));
        lblStatus.setText("Đang làm việc");

        lblName.setText("Họ tên:");

        lblGender.setText("Giới tính:");

        lblPosition.setText("Chức vụ:");

        lblPhone.setText("Số điện thoại:");

        lblEmail.setText("Email:");

        lblHireDate.setText("Ngày bắt đầu:");

        txtName.setText("Trầm Hồng Viên Thiệu");
        txtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameActionPerformed(evt);
            }
        });

        txtPhone.setText("0977707088");
        txtPhone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPhoneActionPerformed(evt);
            }
        });

        txtEmail.setText("vienthieu692005@gmail.com");
        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });

        txtHireDate.setText("06/09/2025");
        txtHireDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHireDateActionPerformed(evt);
            }
        });

        btnSave.setBackground(new java.awt.Color(91, 189, 64));
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setText("Lưu");
        btnSave.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        btnClose.setBackground(new java.awt.Color(255, 0, 0));
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
        lblErrolName.setText("Tên nhân viên không được bỏ trống");

        lblErrolPhone.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblErrolPhone.setForeground(new java.awt.Color(255, 255, 255));
        lblErrolPhone.setText("Số điện thoại không hợp lệ");

        lblErrolEmail.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblErrolEmail.setForeground(new java.awt.Color(255, 255, 255));
        lblErrolEmail.setText("Email không hợp lệ");

        lblErrolHireDate.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblErrolHireDate.setForeground(new java.awt.Color(255, 255, 255));
        lblErrolHireDate.setText("Ngày bắt đầu không hợp lệ");

        cmbGender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbGenderActionPerformed(evt);
            }
        });

        cmbPosition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbPositionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblName)
                            .addComponent(lblEmail))
                        .addGap(32, 32, 32)
                        .addComponent(cmbGender, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(lblPhone)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblGender)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(lblPosition)
                                .addGap(15, 15, 15)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(298, 298, 298)
                                        .addComponent(lblHireDate)
                                        .addGap(18, 18, 18))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblErrolName, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(138, 138, 138)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblErrolHireDate)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(txtHireDate, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(iconDate, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addContainerGap(27, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lblErrolPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(cmbPosition, javax.swing.GroupLayout.PREFERRED_SIZE, 623, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblStatus)
                        .addGap(346, 346, 346))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblCode)
                        .addGap(369, 369, 369))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblImg, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(169, 169, 169))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(277, 277, 277))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblErrolEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(185, 185, 185)
                                        .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(213, 213, 213))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 618, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)))
                                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(15, 15, 15))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTitle))
                .addGap(4, 4, 4)
                .addComponent(lblImg, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblCode, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblStatus)
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblName)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPhone)
                    .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblErrolName, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblErrolPhone))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblGender)
                        .addComponent(cmbGender, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtHireDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblHireDate)
                            .addComponent(iconDate))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblErrolHireDate)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbPosition, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPosition))
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEmail))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblErrolEmail)
                .addGap(33, 33, 33)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNameActionPerformed

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailActionPerformed

    private void txtHireDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHireDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHireDateActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
//        java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);
//    if (window != null) {
//        window.dispose();
    
    }//GEN-LAST:event_btnCloseActionPerformed

    private void txtPhoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPhoneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPhoneActionPerformed

    private void cmbGenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbGenderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbGenderActionPerformed

    private void cmbPositionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPositionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbPositionActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private iuh.fit.se.group1.ui.component.custom.Button btnClose;
    private iuh.fit.se.group1.ui.component.custom.Button btnSave;
    private iuh.fit.se.group1.ui.component.custom.Combobox cmbGender;
    private iuh.fit.se.group1.ui.component.custom.Combobox cmbPosition;
    private javax.swing.JLabel iconDate;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCode;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblErrolEmail;
    private javax.swing.JLabel lblErrolHireDate;
    private javax.swing.JLabel lblErrolName;
    private javax.swing.JLabel lblErrolPhone;
    private javax.swing.JLabel lblGender;
    private javax.swing.JLabel lblHireDate;
    private javax.swing.JLabel lblImg;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblPhone;
    private javax.swing.JLabel lblPosition;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private iuh.fit.se.group1.ui.component.custom.TextField txtEmail;
    private iuh.fit.se.group1.ui.component.custom.TextField txtHireDate;
    private iuh.fit.se.group1.ui.component.custom.TextField txtName;
    private iuh.fit.se.group1.ui.component.custom.TextField txtPhone;
    // End of variables declaration//GEN-END:variables
}
