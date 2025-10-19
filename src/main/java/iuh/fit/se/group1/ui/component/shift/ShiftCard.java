/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.component.shift;

import iuh.fit.se.group1.ui.component.custom.AvatarLabel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.kordamp.ikonli.material.Material;
import org.kordamp.ikonli.swing.FontIcon;

/**
 *
 * @author Administrator
 */
public class ShiftCard extends javax.swing.JPanel {

    public AvatarLabel getAvatarLabel1() {
        return avatarLabel1;
    }

    public void setAvatarLabel1(AvatarLabel avatarLabel1) {
        this.avatarLabel1 = avatarLabel1;
    }

    public AvatarLabel getAvatarLabel2() {
        return avatarLabel2;
    }

    public void setAvatarLabel2(AvatarLabel avatarLabel2) {
        this.avatarLabel2 = avatarLabel2;
    }

    public JButton getBtnAdd() {
        return btnAdd;
    }

    public void setBtnAdd(JButton btnAdd) {
        this.btnAdd = btnAdd;
    }

    public JLabel getLblCode1() {
        return lblCode1;
    }

    public void setLblCode1(JLabel lblCode1) {
        this.lblCode1 = lblCode1;
    }

    public JLabel getLblCode2() {
        return lblCode2;
    }

    public void setLblCode2(JLabel lblCode2) {
        this.lblCode2 = lblCode2;
    }

    public JLabel getLblName1() {
        return lblName1;
    }

    public void setLblName1(JLabel lblName1) {
        this.lblName1 = lblName1;
    }

    public JLabel getLblName2() {
        return lblName2;
    }

    public void setLblName2(JLabel lblName2) {
        this.lblName2 = lblName2;
    }

    public JLabel getLblTime() {
        return lblTime;
    }

    public void setLblTime(JLabel lblTime) {
        this.lblTime = lblTime;
    }

    public JLabel getLblTitle() {
        return lblTitle;
    }

    public void setLblTitle(JLabel lblTitle) {
        this.lblTitle = lblTitle;
    }

    public JPanel getPnlInforEmployee1() {
        return pnlInforEmployee1;
    }

    public void setPnlInforEmployee1(JPanel pnlInforEmployee1) {
        this.pnlInforEmployee1 = pnlInforEmployee1;
    }

    public JPanel getPnlInforEmployee2() {
        return pnlInforEmployee2;
    }

    public void setPnlInforEmployee2(JPanel pnlInforEmployee2) {
        this.pnlInforEmployee2 = pnlInforEmployee2;
    }

    /**
     * Creates new form ShiftPanel
     */
    public ShiftCard() {
        initComponents();
        styleButton();
        styleCard();
        pnlInforEmployee1.setVisible(true);
        pnlInforEmployee2.setVisible(true);
        btnAdd.setVisible(true);
    }
    private void styleCard() {
        // Làm trong suốt để tự vẽ nền
        setOpaque(false);
    }
     @Override
    protected void paintComponent(java.awt.Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int arc = 25;
        int shadow = 6;
        int headerHeight = 40;

        // 🎨 Bóng mờ phía sau
        for (int i = shadow; i > 0; i--) {
            float alpha = 0.05f * (shadow - i + 1);
            g2.setColor(new Color(0, 0, 0, (int) (alpha * 255)));
            g2.fillRoundRect(i, i, w - i * 2, h - i * 2, arc, arc);
        }

        // 🎨 Nền trắng
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, w - shadow, h - shadow, arc, arc);

        // 🎨 Header xanh bo 2 góc trên
        g2.setColor(new Color(51, 204, 255));
        g2.fillRoundRect(0, 0, w - shadow, headerHeight, arc, arc);
        g2.fillRect(0, headerHeight - arc, w - shadow, arc); // tránh bị lõm ở giữa

        g2.dispose();
        super.paintComponent(g);
    }

    private void styleButton() {
        // Tạo icon "+" bằng Ikonli (Material pack)
        FontIcon plusIcon = FontIcon.of(Material.ADD, 36, new Color(33, 150, 243));
        btnAdd.setIcon(plusIcon);
        btnAdd.setText(null); // ẩn text mặc định

        // Làm nút trong suốt, không border mặc định
        btnAdd.setFocusPainted(false);
        btnAdd.setContentAreaFilled(false);
        btnAdd.setBorderPainted(false);
        btnAdd.setOpaque(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Gán kích thước nút
        btnAdd.setSize(60, 60);

        // Hover: đổi màu icon
        btnAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                plusIcon.setIconColor(new Color(25, 118, 210));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                plusIcon.setIconColor(new Color(33, 150, 243));
            }
        });

        // ⚙️ Bo tròn nút bằng custom UI thay vì tạo nút mới
        btnAdd.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
        @Override
        public void paint(java.awt.Graphics g, javax.swing.JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = c.getWidth();
            int h = c.getHeight();
            int shadowSize = 3; // kích thước bóng

            // 🔹 Đổ bóng mềm
            for (int i = shadowSize; i > 0; i--) {
                float alpha = 0.06f * (shadowSize - i + 1);
                g2.setColor(new Color(0, 0, 0, (int) (alpha * 255)));
                g2.fillOval(i, i, w - i * 2, h - i * 2);
            }

            // 🔹 Nền tròn (hover sáng hơn)
            if (btnAdd.getModel().isRollover()) {
                g2.setColor(new Color(245, 245, 245));
            } else {
                g2.setColor(Color.WHITE);
            }
            g2.fillOval(0, 0, w - shadowSize, h - shadowSize);

            // 🔹 Viền mảnh
            g2.setColor(new Color(230, 230, 230));
            g2.drawOval(0, 0, w - shadowSize - 1, h - shadowSize - 1);

            g2.dispose();
            super.paint(g, c); // Vẽ icon "+"
        }
    });
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
        lblTime = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        pnlInforEmployee1 = new javax.swing.JPanel();
        lblName1 = new javax.swing.JLabel();
        lblCode1 = new javax.swing.JLabel();
        avatarLabel1 = new iuh.fit.se.group1.ui.component.custom.AvatarLabel();
        pnlInforEmployee2 = new javax.swing.JPanel();
        avatarLabel2 = new iuh.fit.se.group1.ui.component.custom.AvatarLabel();
        lblName2 = new javax.swing.JLabel();
        lblCode2 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(500, 300));
        setPreferredSize(new java.awt.Dimension(450, 275));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(102, 102, 102));
        lblTitle.setText("CA 02");
        add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 61, 38));

        lblTime.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTime.setText("13H-18H");
        add(lblTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 40, 180, 50));

        btnAdd.setPreferredSize(new java.awt.Dimension(60, 60));
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 180, 70, 70));

        pnlInforEmployee1.setBackground(new java.awt.Color(255, 255, 255));

        lblName1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblName1.setText("Nguyễn Chí Tâm");

        lblCode1.setText("NV732221213");

        javax.swing.GroupLayout pnlInforEmployee1Layout = new javax.swing.GroupLayout(pnlInforEmployee1);
        pnlInforEmployee1.setLayout(pnlInforEmployee1Layout);
        pnlInforEmployee1Layout.setHorizontalGroup(
            pnlInforEmployee1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlInforEmployee1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(avatarLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(pnlInforEmployee1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblName1, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                    .addComponent(lblCode1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        pnlInforEmployee1Layout.setVerticalGroup(
            pnlInforEmployee1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlInforEmployee1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInforEmployee1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlInforEmployee1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(avatarLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlInforEmployee1Layout.createSequentialGroup()
                        .addComponent(lblName1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCode1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        add(pnlInforEmployee1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 300, 70));

        pnlInforEmployee2.setBackground(new java.awt.Color(255, 255, 255));

        lblName2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblName2.setText("Nguyễn Trần Phước Thịnh");

        lblCode2.setText("NV213812731");

        javax.swing.GroupLayout pnlInforEmployee2Layout = new javax.swing.GroupLayout(pnlInforEmployee2);
        pnlInforEmployee2.setLayout(pnlInforEmployee2Layout);
        pnlInforEmployee2Layout.setHorizontalGroup(
            pnlInforEmployee2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInforEmployee2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(avatarLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlInforEmployee2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblName2, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                    .addComponent(lblCode2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        pnlInforEmployee2Layout.setVerticalGroup(
            pnlInforEmployee2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlInforEmployee2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlInforEmployee2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInforEmployee2Layout.createSequentialGroup()
                        .addComponent(lblName2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCode2))
                    .addComponent(avatarLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        add(pnlInforEmployee2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 300, 70));
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed

    }//GEN-LAST:event_btnAddActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private iuh.fit.se.group1.ui.component.custom.AvatarLabel avatarLabel1;
    private iuh.fit.se.group1.ui.component.custom.AvatarLabel avatarLabel2;
    private javax.swing.JButton btnAdd;
    private javax.swing.JLabel lblCode1;
    private javax.swing.JLabel lblCode2;
    private javax.swing.JLabel lblName1;
    private javax.swing.JLabel lblName2;
    private javax.swing.JLabel lblTime;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlInforEmployee1;
    private javax.swing.JPanel pnlInforEmployee2;
    // End of variables declaration//GEN-END:variables
}
