/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package iuh.fit.se.group1.ui.swing;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

import iuh.fit.se.group1.ui.component.modal.SendResetCodeModal;
import iuh.fit.se.group1.ui.component.modal.VerifyIdentityModal;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;
import raven.glasspanepopup.GlassPanePopup;

/**
 *
 * @author Administrator
 */
public class Login extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Login.class.getName());

    /**
     * Creates new form Login
     */
    private Animator animatorLogin;
    private Animator animatorBody;
    private boolean signIn;
    JButton btnEye = new JButton();

    public Login() {
        initComponents();
        // ⚙️ Bỏ layout mặc định để điều khiển vị trí thủ công
        background1.setLayout(null);

// Thêm hai panel chính
        background1.add(panelLogin);
        background1.add(panelBody);

// Ẩn body khi chưa đăng nhập
        panelBody.setVisible(false);

// Căn giữa và tự co giãn theo frame
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int w = getWidth();
                int h = getHeight();

                // Căn giữa panelLogin
                int pw = panelLogin.getPreferredSize().width;
                int ph = panelLogin.getPreferredSize().height;
                int x = (w - pw) / 2;
                int y = (h - ph) / 2;
                panelLogin.setBounds(x, y, pw, ph);

                // Panel body luôn full frame
                panelBody.setBounds(0, 0, w, h);
            }
        });
        btnEye.setIcon(FontIcon.of(FontAwesomeSolid.EYE, 20)); // 20px, to hơn mặc định
        btnEye.setBorderPainted(false);
        btnEye.setContentAreaFilled(false);
        btnEye.setFocusPainted(false);
        btnEye.setOpaque(false);
        btnEye.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Cho phép đặt vị trí tự do
        txtPass.setLayout(null);

        // Đặt vị trí và kích thước cho nút eye to
        btnEye.setBounds(txtPass.getWidth() - 40, 0, 36, 36); // to và sát bên phải
        txtPass.add(btnEye);
        txtPass.revalidate();
        txtPass.repaint();

        // Bật/tắt mật khẩu
        btnEye.addActionListener(e -> {
            if (txtPass.getEchoChar() != '\u0000') {
                txtPass.setEchoChar((char) 0);
                btnEye.setIcon(FontIcon.of(FontAwesomeSolid.EYE_SLASH, 20));
            } else {
                txtPass.setEchoChar('•');
                btnEye.setIcon(FontIcon.of(FontAwesomeSolid.EYE, 20));
            }
        });
        GlassPanePopup.install(this);
        getContentPane().setBackground(new Color(245, 245, 245));
        handleSignOut();

        btnForgotPass.setBorderPainted(false); // tắt viền
        btnForgotPass.setContentAreaFilled(false); // tắt nền mặc định
        btnForgotPass.setFocusPainted(false); // tắt viền focus khi nhấn
        btnForgotPass.setOpaque(false); // trong suốt

        TimingTarget targetLogin = new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                if (signIn) {
                    background1.setAnimate(fraction);
                } else {
                    background1.setAnimate(1f - fraction);
                }
            }

            @Override
            public void end() {
                if (signIn) {
                    panelLogin.setVisible(false);
                    background1.setShowPaint(true);
                    panelBody.setAlpha(0);
                    panelBody.setVisible(true);
                    animatorBody.start();
                } else {
                    enableLogin(true);
                    txtUser.grabFocus();
                }
            }
        };
        TimingTarget targetBody = new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                if (signIn) {
                    panelBody.setAlpha(fraction);
                } else {
                    panelBody.setAlpha(1f - fraction);
                }
            }

            @Override
            public void end() {
                if (signIn == false) {
                    panelBody.setVisible(false);
                    background1.setShowPaint(false);
                    background1.setAnimate(1);
                    panelLogin.setVisible(true);
                    animatorLogin.start();
                }
            }
        };
        animatorLogin = new Animator(1200, targetLogin);
        animatorBody = new Animator(10, targetBody);
        animatorLogin.setResolution(0);
        animatorBody.setResolution(0);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        background1 = new iuh.fit.se.group1.ui.swing.Background();
        panelLogin = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtUser = new iuh.fit.se.group1.ui.swing.TextField();
        txtPass = new iuh.fit.se.group1.ui.swing.PasswordField();
        btnSignIn = new javax.swing.JButton();
        btnForgotPass = new javax.swing.JButton();
        panelBody = new iuh.fit.se.group1.ui.layout.MainLayout();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        background1.setLayout(new java.awt.CardLayout());

        panelLogin.setBackground(new java.awt.Color(245, 245, 245));

        jPanel1.setBackground(new java.awt.Color(245, 245, 245));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logo.png"))); // NOI18N

        txtUser.setBackground(new java.awt.Color(245, 245, 245));
        txtUser.setLabelText("Tên đăng nhập");

        txtPass.setBackground(new java.awt.Color(245, 245, 245));
        txtPass.setLabelText("Mật khẩu");

        btnSignIn.setBackground(new java.awt.Color(157, 153, 255));
        btnSignIn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSignIn.setForeground(new java.awt.Color(255, 255, 255));
        btnSignIn.setText("Đăng nhập");
        btnSignIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSignInActionPerformed(evt);
            }
        });

        btnForgotPass.setBackground(new java.awt.Color(245, 245, 245));
        btnForgotPass.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnForgotPass.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnForgotPass.setLabel("Quên mật khẩu?");
        btnForgotPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnForgotPassActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtPass, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 92, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(btnSignIn, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(95, 95, 95))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(btnForgotPass)
                                .addGap(26, 26, 26))))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel1)
                .addGap(38, 38, 38)
                .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnForgotPass)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addComponent(btnSignIn, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47))
        );

        javax.swing.GroupLayout panelLoginLayout = new javax.swing.GroupLayout(panelLogin);
        panelLogin.setLayout(panelLoginLayout);
        panelLoginLayout.setHorizontalGroup(
            panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLoginLayout.createSequentialGroup()
                .addGap(510, 510, 510)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(514, Short.MAX_VALUE))
        );
        panelLoginLayout.setVerticalGroup(
            panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLoginLayout.createSequentialGroup()
                .addContainerGap(192, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(189, 189, 189))
        );

        background1.add(panelLogin, "card2");
        background1.add(panelBody, "card3");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSignInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSignInActionPerformed
        if (!animatorLogin.isRunning()) {
            signIn = true;
            String user = txtUser.getText().trim();
            String pass = String.valueOf(txtPass.getPassword());
            boolean action = true;
            if (user.equals("")) {
                txtUser.setHelperText("Hãy nhập tên đăng nhập");
                txtUser.grabFocus();
                action = false;
            }
            if (pass.equals("")) {
                txtPass.setHelperText("Hãy nhập mật khẩu");
                if (action) {
                    txtPass.grabFocus();
                }
                action = false;
            }
            if (action) {
                animatorLogin.start();
                enableLogin(false);
            }
        }
    }//GEN-LAST:event_btnSignInActionPerformed

    private void btnForgotPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnForgotPassActionPerformed
        var modal = new VerifyIdentityModal();
        var sendCodeModal = new SendResetCodeModal();
        modal.closeModal(e -> {
            GlassPanePopup.closePopupLast();
        });
        modal.sendRestCode(e -> {
            GlassPanePopup.closePopupLast();
            GlassPanePopup.showPopup(sendCodeModal);

        });

        sendCodeModal.sendResetCode(e -> {
            GlassPanePopup.closePopupLast();
        });

        sendCodeModal.sendResetCode(e -> {
            // Xử lý gửi mã xác nhận
            System.out.println("Gửi mã xác nhận");
        });


        GlassPanePopup.showPopup(modal);


    }//GEN-LAST:event_btnForgotPassActionPerformed
    private void enableLogin(boolean action) {
        txtUser.setEditable(action);
        txtPass.setEditable(action);
        btnSignIn.setEnabled(action);
    }

    private void handleSignOut() {
        panelBody.getBtnSignOut().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signIn = false;
                clearLogin();
                animatorBody.start();
            }
        });
    }

    ;
//    private void cmdSignOutActionPerformed(java.awt.event.ActionEvent evt) {
//            signIn = false;
//            clearLogin();
//            animatorBody.start();
//    }
    public void clearLogin() {
        txtUser.setText("");
        txtPass.setText("");
        txtUser.setHelperText("");
        txtPass.setHelperText("");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Login().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private iuh.fit.se.group1.ui.swing.Background background1;
    private javax.swing.JButton btnForgotPass;
    private javax.swing.JButton btnSignIn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private iuh.fit.se.group1.ui.layout.MainLayout panelBody;
    private javax.swing.JPanel panelLogin;
    private iuh.fit.se.group1.ui.swing.PasswordField txtPass;
    private iuh.fit.se.group1.ui.swing.TextField txtUser;
    // End of variables declaration//GEN-END:variables
}
