/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package iuh.fit.se.group1.ui.swing;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.*;

import iuh.fit.se.group1.entity.Employee;
import iuh.fit.se.group1.enums.Role;
import iuh.fit.se.group1.service.AuthenticateService;
import iuh.fit.se.group1.service.EmailSenderService;
import iuh.fit.se.group1.service.EmployeeService;
import iuh.fit.se.group1.ui.component.modal.SendResetCodeModal;
import iuh.fit.se.group1.ui.component.modal.VerifyIdentityModal;
import iuh.fit.se.group1.util.PropertiesReader;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import raven.glasspanepopup.GlassPanePopup;

/**
 * @author Administrator
 */
public class Login extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Login.class.getName());
    private static final Logger log = LoggerFactory.getLogger(Login.class);

    /**
     * Creates new form Login
     */

    private final AuthenticateService authenticateService;
    private final EmployeeService employeeService;
    private final EmailSenderService emailSenderService;

    private Animator animatorLogin;
    private Animator animatorBody;
    private boolean signIn;
    private JButton btnEye = new JButton();

    public Login() {
        this.authenticateService = new AuthenticateService();
        this.employeeService = new EmployeeService();
        this.emailSenderService = new EmailSenderService();
        initComponents();
        custom();

    }

    private void custom() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        // Bỏ layout mặc định để điều khiển vị trí thủ công
        background1.setLayout(null);

        // Thêm hai panel chính (thêm panelBody khi frame đã hiển thị để đảm bảo kích
        // thước chính xác)
        background1.add(panelLogin);

        // Ẩn body khi chưa đăng nhập
        panelBody.setVisible(false);

        // Sau khi frame được tạo xong, đặt kích thước ban đầu cho 2 panel
        SwingUtilities.invokeLater(() -> {
            int w = getWidth();
            int h = getHeight();
            // Đặt bounds ban đầu cho panelLogin (giữ theo preferred size)
            int pw = panelLogin.getPreferredSize().width;
            int ph = panelLogin.getPreferredSize().height;
            int x = (w - pw) / 2;
            int y = (h - ph) / 2;
            panelLogin.setBounds(x, y, pw, ph);

            // Panel body luôn full frame - thêm vào background sau khi frame đã hiện
            panelBody.setBounds(0, 0, w, h);
            background1.add(panelBody);
        });


        // Căn giữa và tự co giãn theo frame
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int w = getContentPane().getWidth();
                int h = getContentPane().getHeight();

                // Căn giữa panelLogin
                int pw = panelLogin.getPreferredSize().width;
                int ph = panelLogin.getPreferredSize().height;
                int x = (w - pw) / 2;
                int y = (h - ph) / 2;
                panelLogin.setBounds(x, y, pw, ph);

                // Panel body full content pane
                panelBody.setBounds(0, 0, w, h);
                background1.revalidate();
                background1.repaint();
                panelBody.revalidate();
                panelBody.repaint();
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
        txtUser.addActionListener(e -> {
            txtPass.requestFocus();
        });

        txtPass.addActionListener(e -> {
            doLogin();
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
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
                                                .addGroup(jPanel1Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(txtUser, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(txtPass, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addContainerGap())
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout
                                                .createSequentialGroup()
                                                .addGap(0, 92, Short.MAX_VALUE)
                                                .addGroup(jPanel1Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                jPanel1Layout.createSequentialGroup()
                                                                        .addComponent(btnSignIn,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                183,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addGap(95, 95, 95))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                jPanel1Layout.createSequentialGroup()
                                                                        .addComponent(btnForgotPass)
                                                                        .addGap(26, 26, 26)))))));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addComponent(jLabel1)
                                .addGap(38, 38, 38)
                                .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPass, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnForgotPass)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37,
                                        Short.MAX_VALUE)
                                .addComponent(btnSignIn, javax.swing.GroupLayout.PREFERRED_SIZE, 43,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(47, 47, 47)));

        javax.swing.GroupLayout panelLoginLayout = new javax.swing.GroupLayout(panelLogin);
        panelLogin.setLayout(panelLoginLayout);
        panelLoginLayout.setHorizontalGroup(
                panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelLoginLayout.createSequentialGroup()
                                .addGap(510, 510, 510)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(514, Short.MAX_VALUE)));
        panelLoginLayout.setVerticalGroup(
                panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLoginLayout.createSequentialGroup()
                                .addContainerGap(125, Short.MAX_VALUE)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(189, 189, 189)));

        background1.add(panelLogin, "card2");
        background1.add(panelBody, "card3");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(background1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(background1, javax.swing.GroupLayout.PREFERRED_SIZE, 733, Short.MAX_VALUE));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSignInActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnSignInActionPerformed
        doLogin();
    }// GEN-LAST:event_btnSignInActionPerformed

    private void doLogin() {
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

            var authenticate = authenticateService.authenticate(user, pass);
            if (authenticate == null) {
                txtPass.setHelperText("Mật khẩu không chính xác");
                if (action) {
                    txtPass.grabFocus();
                }
                txtUser.setHelperText("Tên đăng nhập không chính xác");
                if (action) {
                    txtUser.grabFocus();
                }
                action = false;
            }


            if (action) {
                boolean isManager = authenticate.getRole().getRoleId().equals(Role.MANAGER.toString());
                log.info("User '{}' login with role '{}'", authenticate.getUsername(), authenticate.getRole().getRoleId());

                animatorLogin.start();
                panelBody.setAuth(isManager);
                enableLogin(false);
            }
        }
    }

    private void btnForgotPassActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnForgotPassActionPerformed
        var modal = new VerifyIdentityModal();
        var sendCodeModal = new SendResetCodeModal();
        modal.closeModal(e -> {
            GlassPanePopup.closePopupLast();
        });
        AtomicReference<Employee> employeeAtomicReference = new AtomicReference<>();
        modal.sendRestCode(e -> {
            var result = sendCode(modal, sendCodeModal);
            employeeAtomicReference.set(result);
        });


        sendCodeModal.sendResetCode(e ->
                sendCodeToEmail(employeeAtomicReference.get())
        );


        sendCodeModal.goToBackLogin(e ->
                GlassPanePopup.closePopupAll()
        );

        GlassPanePopup.showPopup(modal);

    }// GEN-LAST:event_btnForgotPassActionPerformed


    private Employee sendCode(VerifyIdentityModal modal, SendResetCodeModal sendCodeModal) {
        String citizenId = modal.getTxtCitizenID().getText();

        if (citizenId.isBlank()) {
            modal.getTxtCitizenID().setHelperText("Vui lòng nhập số CMND/CCCD");
            modal.getTxtCitizenID().grabFocus();
            return null;
        }

        var employee = employeeService.getEmployeeByCitizenId(citizenId);
        if (employee == null) {
            modal.getTxtCitizenID().setHelperText("Số CMND/CCCD không tồn tại");
            modal.getTxtCitizenID().grabFocus();
            return null;
        }

        sendCodeToEmail(employee);

        GlassPanePopup.closePopupLast();
        sendCodeModal.setTextEmail(employee.getEmail());
        sendCodeModal.getLblName().setText(employee.getFullName());
        sendCodeModal.getLblPosition().setText(employee.getAccount().getRole().getRoleName());
        GlassPanePopup.showPopup(sendCodeModal);
        return employee;
    }

    private void sendCodeToEmail(Employee employee) {

        authenticateService.resetPassword(employee.getAccount().getUsername());

        String title = "Đặt lại mật khẩu cho tài khoản của bạn";
        String html = null;
        try {
            html = Files.readString(Paths.get("src/main/resources/static/reset-password.html"));
            html = html.replace("{{user_name}}", employee.getFullName())
                    .replace("{{user_password}}", PropertiesReader.getInstance().get("daotien.password"))
                    .replace("{{support_email}}", PropertiesReader.getInstance().get("daotien.mail.email"))
                    .replace("{{site_name}}", "Đào Tiên Hotel")
                    .replace("{{current_year}}", LocalDate.now().getYear() + "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        emailSenderService.sendHtmlMail(
                employee.getEmail(),
                title,
                html
        );


    }

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


    //    private void cmdSignOutActionPerformed(java.awt.event.ActionEvent evt) {
//        signIn = false;
//        clearLogin();
//        animatorBody.start();
//    }
    public void clearLogin() {
        txtUser.setText("");
        txtPass.setText("");
        txtUser.setHelperText("");
        txtPass.setHelperText("");
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel.
         * For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
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
        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            Login login = new Login();
            login.setVisible(true);
            login.setExtendedState(JFrame.MAXIMIZED_BOTH);
        });
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
