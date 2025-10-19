/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.layout;

import com.raven.datechooser.DateChooser;
import com.raven.datechooser.SelectedAction;
import com.raven.datechooser.SelectedDate;
import iuh.fit.se.group1.ui.component.custom.message.Message;
import iuh.fit.se.group1.ui.component.shift.ShiftCard;
import iuh.fit.se.group1.ui.component.shift.ShiftList;
import iuh.fit.se.group1.ui.component.shift.ShiftProfile;
import iuh.fit.se.group1.util.Constants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;
import raven.glasspanepopup.GlassPanePopup;



/**
 *
 * @author THIS PC
 */
public class ShiftManagement extends javax.swing.JPanel {

    private DateChooser dateChooser;

    /**
     * Creates new form ShiftManagement
     */
    public ShiftManagement() {
        initComponents();


        txtDate.setEditable(false);
        txtDate.setFocusable(false);
        txtDate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconDate.setIcon(FontIcon.of(FontAwesomeSolid.CALENDAR_ALT, 20, Constants.COLOR_ICON_MENU));
        iconDate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        dateChooser = new DateChooser();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        txtDate.setText(sdf.format(new Date()));
        dateChooser.setDateFormat("dd/MM/yyyy");
        dateChooser.toDay();
        dateChooser.setForeground(Constants.COLOR_ICON_MENU);
        dateChooser.addEventDateChooser((action, date) -> {
            if (action.getAction() == SelectedAction.DAY_SELECTED) {
                dateChooser.hidePopup();
            }
        });
        dateChooser.setTextRefernce(txtDate);
        iconDate.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dateChooser.showPopup(txtDate, 0, txtDate.getHeight());
            }
        });
        shiftCard1.setHeaderColor(Color.red);
        shiftCard3.setHeaderColor(Color.GREEN);
        shiftCard4.setHeaderColor(Color.yellow);
        shiftCard1.getLblShiftName().setText("CA 01");
        shiftCard1.getLblTime().setText("0H-6H");
        shiftCard2.getLblShiftName().setText("CA 02");
        shiftCard2.getLblTime().setText("6H-12H");
        shiftCard3.getLblShiftName().setText("CA 03");
        shiftCard3.getLblTime().setText("12H-18H");
        shiftCard4.getLblShiftName().setText("CA 04");
        shiftCard4.getLblTime().setText("18H-0H");
        setupShiftCardButtons();
    }
    private void setupShiftCardButtons() {
        // Gắn sự kiện cho từng ShiftCard
        shiftCard1.getBtnAdd().addActionListener(e -> handleAddEmployeesToShift(shiftCard1));
        shiftCard3.getBtnAdd().addActionListener(e -> handleAddEmployeesToShift(shiftCard3));
        shiftCard2.getBtnAdd().addActionListener(e -> handleAddEmployeesToShift(shiftCard2));
        shiftCard4.getBtnAdd().addActionListener(e -> handleAddEmployeesToShift(shiftCard4));
    }

    private void handleAddEmployeesToShift(ShiftCard shiftCard) {
        // Lấy danh sách nhân viên đã chọn từ ShiftList
        List<ShiftProfile> selectedProfiles = shiftList2.getSelectedEmployees();

        // Kiểm tra xem có đúng 2 nhân viên được chọn không
        if (selectedProfiles.size() != 2) {
            Message.showMessageNoCancel("Thông báo", "Vui lòng chọn đúng 2 nhân viên!");
            return;
        }
        
        // Cập nhật thông tin nhân viên vào ShiftCard
        ShiftProfile profile1 = selectedProfiles.get(0);
        ShiftProfile profile2 = selectedProfiles.get(1);
        // Lấy tên nhân viên và tên ca để hiển thị trong thông báo
        String name1 = profile1.getLblName().getText();
        String name2 = profile2.getLblName().getText();
        String shiftName = shiftCard.getLblShiftName().getText(); // Giả sử ShiftCard có label tên ca

        // Hiển thị hộp xác nhận
        Message.showConfirm(
        "Xác nhận",
        "Bạn có chắc chắn muốn thêm " + name1 + " và " + name2 + " vào ca " + shiftName + " không?",
            () -> {
                // Đây là code sẽ thực thi khi người dùng nhấn OK
                shiftCard.getLblName1().setText(name1);
                shiftCard.getLblCode1().setText(profile1.getLblCode().getText());
                shiftCard.getAvatarLabel1().setImage(profile1.getAvatarLabel().getImage());

                shiftCard.getLblName2().setText(name2);
                shiftCard.getLblCode2().setText(profile2.getLblCode().getText());
                shiftCard.getAvatarLabel2().setImage(profile2.getAvatarLabel().getImage());

                shiftCard.getPnlInforEmployee1().setVisible(true);
                shiftCard.getPnlInforEmployee2().setVisible(true);
                shiftCard.getBtnAdd().setVisible(true);

                shiftList2.clearAllSelections();
                
            }
        );
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerShift1 = new iuh.fit.se.group1.ui.component.HeaderShift();
        panelShiftCard = new javax.swing.JPanel();
        shiftCard1 = new iuh.fit.se.group1.ui.component.shift.ShiftCard();
        shiftCard3 = new iuh.fit.se.group1.ui.component.shift.ShiftCard();
        shiftCard2 = new iuh.fit.se.group1.ui.component.shift.ShiftCard();
        shiftCard4 = new iuh.fit.se.group1.ui.component.shift.ShiftCard();
        jLabel1 = new javax.swing.JLabel();
        txtDate = new javax.swing.JTextField();
        iconDate = new javax.swing.JLabel();
        search1 = new iuh.fit.se.group1.ui.component.booking.Search();
        shiftList2 = new iuh.fit.se.group1.ui.component.shift.ShiftList();
        jLabel2 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(241, 241, 241));
        setOpaque(false);

        panelShiftCard.setBackground(new java.awt.Color(241, 241, 241));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("Danh sách ca làm");

        txtDate.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtDate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        txtDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDateActionPerformed(evt);
            }
        });

        iconDate.setText(" ");

        search1.setBackground(new java.awt.Color(241, 241, 241));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Tìm kiếm nhân viên:");

        javax.swing.GroupLayout panelShiftCardLayout = new javax.swing.GroupLayout(panelShiftCard);
        panelShiftCard.setLayout(panelShiftCardLayout);
        panelShiftCardLayout.setHorizontalGroup(
            panelShiftCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelShiftCardLayout.createSequentialGroup()
                .addGroup(panelShiftCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelShiftCardLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jLabel1))
                    .addGroup(panelShiftCardLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(shiftCard1, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelShiftCardLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(shiftCard3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addGroup(panelShiftCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelShiftCardLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelShiftCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(shiftCard2, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(shiftCard4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(18, 18, Short.MAX_VALUE))
                    .addGroup(panelShiftCardLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(iconDate, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)))
                .addGroup(panelShiftCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelShiftCardLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(search1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(shiftList2, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        panelShiftCardLayout.setVerticalGroup(
            panelShiftCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelShiftCardLayout.createSequentialGroup()
                .addGroup(panelShiftCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                    .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(iconDate, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(search1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelShiftCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelShiftCardLayout.createSequentialGroup()
                        .addGroup(panelShiftCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(shiftCard1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(shiftCard2, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(panelShiftCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(shiftCard4, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(shiftCard3, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(shiftList2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerShift1, javax.swing.GroupLayout.DEFAULT_SIZE, 1201, Short.MAX_VALUE)
            .addComponent(panelShiftCard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerShift1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(panelShiftCard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDateActionPerformed

    }//GEN-LAST:event_txtDateActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private iuh.fit.se.group1.ui.component.HeaderShift headerShift1;
    private javax.swing.JLabel iconDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel panelShiftCard;
    private iuh.fit.se.group1.ui.component.booking.Search search1;
    private iuh.fit.se.group1.ui.component.shift.ShiftCard shiftCard1;
    private iuh.fit.se.group1.ui.component.shift.ShiftCard shiftCard2;
    private iuh.fit.se.group1.ui.component.shift.ShiftCard shiftCard3;
    private iuh.fit.se.group1.ui.component.shift.ShiftCard shiftCard4;
    private iuh.fit.se.group1.ui.component.shift.ShiftList shiftList2;
    private javax.swing.JTextField txtDate;
    // End of variables declaration//GEN-END:variables
}