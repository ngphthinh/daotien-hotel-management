/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.component.booking;

import iuh.fit.se.group1.ui.component.custom.Button;
import iuh.fit.se.group1.ui.component.custom.Combobox;
import iuh.fit.se.group1.ui.component.custom.TextField;
import iuh.fit.se.group1.ui.component.custom.date.DateTimePicker;
import iuh.fit.se.group1.util.Constants;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;

/**
 * @author THIS PC
 */
public class InfoRoomPanel extends javax.swing.JPanel {

    private int borderRadius = 4;
    private Consumer<InfoRoomPanel> onSelect;

    /**
     * Creates new form InfoRoomPanel
     */
    public InfoRoomPanel() {
        initComponents();
        setOpaque(false);
        lblTitle.setIcon(FontIcon.of(FontAwesomeSolid.HOME, 20, lblTitle.getForeground()));
        btnClose.setText("");
        btnClose.setIcon(FontIcon.of(FontAwesomeSolid.TIMES_CIRCLE, 20, Constants.COLOR_ICON_MENU));
        btnClose.setBackground(Color.white);

        cboRoomType.addItem("Phòng đơn");
        cboRoomType.addItem("Phòng đôi");
        cboRoomType.setBackground(new java.awt.Color(240, 248, 255));
        cboRoomType.setForeground(new java.awt.Color(51, 51, 51));
        cboRoomType.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        cboBookingType.addItem("Theo giờ");
        cboBookingType.addItem("Qua đêm");
        cboBookingType.addItem("Theo ngày");
        cboBookingType.setBackground(new java.awt.Color(240, 248, 255));
        cboBookingType.setForeground(new java.awt.Color(51, 51, 51));
        cboBookingType.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));

        if (onSelect != null) {
            this.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    onSelect.accept(InfoRoomPanel.this);
                }
            });
        }

        // Khi click panel thì gọi callback
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (onSelect != null) onSelect.accept(InfoRoomPanel.this);
            }
        });

        // Gắn picker cho text field
        DateTimePicker.attachTo(txtStartDate, cboBookingType, () -> {
            if (onSelect != null) onSelect.accept(InfoRoomPanel.this);
        });

        DateTimePicker.attachTo(txtEndDate, cboBookingType, () -> {
            if (onSelect != null) onSelect.accept(InfoRoomPanel.this);
        });
    }

    public void setOnSelect(Consumer<InfoRoomPanel> onSelect) {
        this.onSelect = onSelect;
    }

    public Combobox getCboRoomType() {
        return cboRoomType;
    }

    public void setCboRoomType(Combobox cboRoomType) {
        this.cboRoomType = cboRoomType;
    }

    public Combobox getCboBookingType() {
        return cboBookingType;
    }

    public void setCboBookingType(Combobox cboBookingType) {
        this.cboBookingType = cboBookingType;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int offset = 2;            // độ lệch bóng
        Color shadowColor = new Color(0, 0, 0, 10); // màu bóng (đen mờ)

        int width = getWidth();
        int height = getHeight();

        // Vẽ bóng (vẽ sau, lệch nhẹ xuống phải)
        g2.setColor(shadowColor);
        g2.fillRoundRect(offset, offset, width - 1, height - 1, borderRadius, borderRadius);

        // Vẽ nền panel thật (che bóng phía trên)
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, width - offset - 1, height - offset - 1, borderRadius, borderRadius);

        g2.dispose();
        super.paintComponent(g);
    }

    public void repaintAll() {
        SwingUtilities.invokeLater(() -> {
            this.revalidate();
            this.repaint();
        });
    }

    public JLabel getLblTitle() {
        return lblTitle;
    }

    public void setLblTitle(JLabel lblTitle) {
        this.lblTitle = lblTitle;
    }

    public TextField getTxtEndDate() {
        return txtEndDate;
    }

    public void setTxtEndDate(TextField txtEndDate) {
        this.txtEndDate = txtEndDate;
    }

    public TextField getTxtPrice() {
        return txtPrice;
    }

    public void setTxtPrice(TextField txtPrice) {
        this.txtPrice = txtPrice;
    }

    public TextField getTxtRoomId() {
        return txtRoomId;
    }

    public void setTxtRoomId(TextField txtRoomId) {
        this.txtRoomId = txtRoomId;
    }

    public TextField getTxtRoomNumber() {
        return txtRoomNumber;
    }

    public void setTxtRoomNumber(TextField txtRoomNumber) {
        this.txtRoomNumber = txtRoomNumber;
    }

    public TextField getTxtStartDate() {
        return txtStartDate;
    }

    public void setTxtStartDate(TextField txtStartDate) {
        this.txtStartDate = txtStartDate;
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
        jSeparator1 = new javax.swing.JSeparator();
        lblTypeRoom = new javax.swing.JLabel();
        lblStartDate = new javax.swing.JLabel();
        lblEndDate = new javax.swing.JLabel();
        txtStartDate = new iuh.fit.se.group1.ui.component.custom.TextField();
        txtEndDate = new iuh.fit.se.group1.ui.component.custom.TextField();
        lblRoomId = new javax.swing.JLabel();
        lblPrice = new javax.swing.JLabel();
        lblRoomNumber = new javax.swing.JLabel();
        txtRoomNumber = new iuh.fit.se.group1.ui.component.custom.TextField();
        txtPrice = new iuh.fit.se.group1.ui.component.custom.TextField();
        txtRoomId = new iuh.fit.se.group1.ui.component.custom.TextField();
        btnClose = new iuh.fit.se.group1.ui.component.custom.Button();
        cboRoomType = new iuh.fit.se.group1.ui.component.custom.Combobox();
        lblBookingType = new javax.swing.JLabel();
        cboBookingType = new iuh.fit.se.group1.ui.component.custom.Combobox();

        setBackground(new java.awt.Color(255, 255, 255));
        setForeground(new java.awt.Color(217, 217, 217));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(131, 176, 211));
        lblTitle.setText("Phòng 01");

        lblTypeRoom.setBackground(new java.awt.Color(0, 0, 0));
        lblTypeRoom.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTypeRoom.setText("Loại phòng");

        lblStartDate.setBackground(new java.awt.Color(0, 0, 0));
        lblStartDate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblStartDate.setText("Ngày bắt đầu");

        lblEndDate.setBackground(new java.awt.Color(0, 0, 0));
        lblEndDate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblEndDate.setText("Ngày kết thúc");

        txtStartDate.setBorderRadius(4);
        txtStartDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtStartDateActionPerformed(evt);
            }
        });

        txtEndDate.setBorderRadius(4);
        txtEndDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEndDateActionPerformed(evt);
            }
        });

        lblRoomId.setBackground(new java.awt.Color(0, 0, 0));
        lblRoomId.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblRoomId.setText("Mã phòng");

        lblPrice.setBackground(new java.awt.Color(0, 0, 0));
        lblPrice.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblPrice.setText("Giá phòng");

        lblRoomNumber.setBackground(new java.awt.Color(0, 0, 0));
        lblRoomNumber.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblRoomNumber.setText("Số phòng");

        txtRoomNumber.setBorderRadius(4);
        txtRoomNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRoomNumberActionPerformed(evt);
            }
        });

        txtPrice.setBorderRadius(4);
        txtPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPriceActionPerformed(evt);
            }
        });

        txtRoomId.setBorderRadius(4);
        txtRoomId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRoomIdActionPerformed(evt);
            }
        });

        btnClose.setText("X");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        lblBookingType.setBackground(new java.awt.Color(0, 0, 0));
        lblBookingType.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblBookingType.setText("Loại đặt phòng");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(lblTypeRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(125, 125, 125)
                                                                .addComponent(lblStartDate))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(cboRoomType, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(27, 27, 27)
                                                                .addComponent(txtStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(33, 33, 33)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(lblEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtEndDate, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                                                        .addComponent(lblBookingType, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(cboBookingType, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                        .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 556, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(txtRoomId, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lblRoomId, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(36, 36, 36)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lblPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(36, 36, 36)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lblRoomNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtRoomNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblTypeRoom)
                                        .addComponent(lblStartDate)
                                        .addComponent(lblEndDate))
                                .addGap(0, 0, 0)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cboRoomType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblRoomId)
                                        .addComponent(lblRoomNumber)
                                        .addComponent(lblPrice)
                                        .addComponent(lblBookingType))
                                .addGap(5, 5, 5)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtRoomId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtRoomNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cboBookingType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtStartDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStartDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStartDateActionPerformed

    private void txtEndDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEndDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEndDateActionPerformed

    private void txtRoomNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRoomNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRoomNumberActionPerformed

    private void txtPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPriceActionPerformed

    private void txtRoomIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRoomIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRoomIdActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCloseActionPerformed

    public void closeRoomCard(ActionListener l) {
        btnClose.addActionListener(l);
    }

    public Button getBtnClose() {
        return btnClose;
    }

    public void setBtnClose(Button btnClose) {
        this.btnClose = btnClose;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private iuh.fit.se.group1.ui.component.custom.Button btnClose;
    private iuh.fit.se.group1.ui.component.custom.Combobox cboBookingType;
    private iuh.fit.se.group1.ui.component.custom.Combobox cboRoomType;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblBookingType;
    private javax.swing.JLabel lblEndDate;
    private javax.swing.JLabel lblPrice;
    private javax.swing.JLabel lblRoomId;
    private javax.swing.JLabel lblRoomNumber;
    private javax.swing.JLabel lblStartDate;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTypeRoom;
    private iuh.fit.se.group1.ui.component.custom.TextField txtEndDate;
    private iuh.fit.se.group1.ui.component.custom.TextField txtPrice;
    private iuh.fit.se.group1.ui.component.custom.TextField txtRoomId;
    private iuh.fit.se.group1.ui.component.custom.TextField txtRoomNumber;
    private iuh.fit.se.group1.ui.component.custom.TextField txtStartDate;
    // End of variables declaration//GEN-END:variables
}
