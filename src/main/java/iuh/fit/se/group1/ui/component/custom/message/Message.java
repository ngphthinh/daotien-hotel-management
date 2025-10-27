/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.component.custom.message;

import iuh.fit.se.group1.ui.component.custom.Button;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JLabel;
import raven.glasspanepopup.GlassPanePopup;

/**
 *
 * @author Vien Thieu
 */
public class Message extends javax.swing.JPanel {


    public Message() {
        initComponents();
        setOpaque(false);
        lblMessage.setBackground(new Color(0,0,0,0)); 
        lblMessage.setOpaque(false);
    }

    public Button getCmdCancel() {
        return cmdCancel;
    }

    public void setCmdCancel(Button cmdCancel) {
        this.cmdCancel = cmdCancel;
    }

    public Button getCmdOK() {
        return cmdOK;
    }

    public void setCmdOK(Button cmdOK) {
        this.cmdOK = cmdOK;
    }

    public JLabel getLblMessage() {
        return lblMessage;
    }

    public void setLblMessage(JLabel lblMessage) {
        this.lblMessage = lblMessage;
    }


    public JLabel getLblTitle() {
        return lblTitle;
    }

    public void setLblTitle(JLabel lblTitle) {
        this.lblTitle = lblTitle;
    }
    
    public static void showMessage(String title, String message) {
        Message msg = new Message();
        msg.setBackground(new java.awt.Color(255, 255, 255));
        msg.getLblTitle().setText(title);
        msg.getLblMessage().setText("<html><body style='width: 348px;'>" + message + "</body></html>");

        msg.eventOK(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GlassPanePopup.closePopupLast();
            }
        });

        GlassPanePopup.showPopup(msg);
    }
    
    public static void showConfirm(String title, String message, Runnable onOK) {
        Message msg = new Message();
        msg.setBackground(new java.awt.Color(255, 255, 255));
        msg.getLblTitle().setText(title);
        msg.getLblMessage().setText("<html><body style='width: 348px;'>" + message + "</body></html>");

        msg.eventOK(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GlassPanePopup.closePopupAll();
                if (onOK != null) {
                    onOK.run();
                }
            }
        });
      
        GlassPanePopup.showPopup(msg);
    }
    public static void showMessageNoCancel(String title, String message) {
        Message msg = new Message();
        msg.setBackground(new java.awt.Color(255, 255, 255));
        msg.getLblTitle().setText(title);
        msg.getLblMessage().setText("<html><body style='width: 348px;'>" + message + "</body></html>");

        msg.getCmdCancel().setVisible(false);
        msg.eventOK(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GlassPanePopup.closePopupLast();
            }
        });

        GlassPanePopup.showPopup(msg);
    }
    public static void showConfirmNoCancel(String title, String message, Runnable onOK) {
        Message msg = new Message();
        msg.setBackground(new java.awt.Color(255, 255, 255));
        msg.getLblTitle().setText(title);
        msg.getLblMessage().setText("<html><body style='width: 348px;'>" + message + "</body></html>");

        msg.getCmdCancel().setVisible(false);
        msg.eventOK(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GlassPanePopup.closePopupLast();
                if (onOK != null) {
                    onOK.run();
                }
            }
        });
      
        GlassPanePopup.showPopup(msg);
    }
    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D)grphcs.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),15,15));
        g2.dispose();
        super.paintComponent(grphcs); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        button2 = new iuh.fit.se.group1.ui.component.custom.Button();
        lblTitle = new javax.swing.JLabel();
        lblMessage = new javax.swing.JLabel();
        cmdOK = new iuh.fit.se.group1.ui.component.custom.Button();
        cmdCancel = new iuh.fit.se.group1.ui.component.custom.Button();

        button2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button2ActionPerformed(evt);
            }
        });

        setBackground(new java.awt.Color(255, 255, 255));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTitle.setText("Your Message Title Dialog Custom");

        lblMessage.setText("Hien Thi Thong Bao Loi ?????????????????????????????????");

        cmdOK.setBackground(new java.awt.Color(0, 204, 51));
        cmdOK.setText("Xác nhận");

        cmdCancel.setBackground(new java.awt.Color(102, 204, 255));
        cmdCancel.setText("Hủy");
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(cmdCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cmdOK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63))
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblTitle)
                        .addGap(0, 115, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(7, Short.MAX_VALUE)
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdOK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void button2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button2ActionPerformed
        GlassPanePopup.closePopupLast();
    }//GEN-LAST:event_button2ActionPerformed
    
    public void eventOK(ActionListener event){
        cmdOK.addActionListener(event);
    }
    
    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        GlassPanePopup.closePopupLast();
    }//GEN-LAST:event_cmdCancelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private iuh.fit.se.group1.ui.component.custom.Button button2;
    private iuh.fit.se.group1.ui.component.custom.Button cmdCancel;
    private iuh.fit.se.group1.ui.component.custom.Button cmdOK;
    private javax.swing.JLabel lblMessage;
    private javax.swing.JLabel lblTitle;
    // End of variables declaration//GEN-END:variables
}
