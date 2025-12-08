/**
 * @ (#) ConfirmInfoModal.java   1.0     1/12/2025
 * <p>
 * Copyright (c) 2025 IUH. All rights reserved
 */
package iuh.fit.se.group1.ui.component.modal;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/**
 * @description
 * @author: Nguyen Tran Quoc Viet 
 * @version: 1.0
 * @created: 1/12/2025
 */
public class ConfirmInfoModal extends JPanel {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnConfirm;
    private JButton btnCancel;
    private boolean isProcessing = false;

    public ConfirmInfoModal() {
        setOpaque(false);
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
        initComponents();
        setupEnterKeyListeners();

        // Focus vào username
        SwingUtilities.invokeLater(() -> txtUsername.requestFocusInWindow());
    }

    private void initComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        JLabel lblTitle = new JLabel("XÁC NHẬN QUẢN LÝ");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(0, 102, 204));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(lblTitle, gbc);
        JLabel lblUsername = new JLabel("Tài khoản:");
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUsername.setForeground(Color.DARK_GRAY);
        JLabel lblPassword = new JLabel("Mật khẩu:");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPassword.setForeground(Color.DARK_GRAY);

        txtUsername = createTextField();
        txtPassword = createPasswordField();

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(lblUsername, gbc);
        gbc.gridx = 1;
        add(txtUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(lblPassword, gbc);
        gbc.gridx = 1;
        add(txtPassword, gbc);
        btnConfirm = createGradientButton("Xác nhận", new Color(0, 102, 204), new Color(51, 153, 255));
        btnCancel = createGradientButton("Hủy", new Color(204, 0, 0), new Color(255, 51, 51));
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(btnConfirm);
        btnPanel.add(btnCancel);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnPanel, gbc);
    }

    /**
     * THIẾT LẬP PHÍM ENTER
     */
    private void setupEnterKeyListeners() {
        // ENTER Ở USERNAME  SANG PASSWORD
        txtUsername.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    txtPassword.requestFocusInWindow();
                }
            }
        });

        //  ENTER Ở PASSWORD NÚT XÁC NHẬN
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnConfirm.doClick(); // Giả lập click button
                }
            }
        });
        txtPassword.addActionListener(e -> btnConfirm.doClick());
        // ESC để đóng modal
        KeyStroke escapeKey = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKey, "ESCAPE");
        getActionMap().put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                btnCancel.doClick();
            }
        });
    }

    private JTextField createTextField() {
        JTextField field = new JTextField(15);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        field.setBackground(new Color(250, 250, 250));
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField(15);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        field.setBackground(new Color(250, 250, 250));
        return field;
    }

    private JButton createGradientButton(String text, Color start, Color end) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, start, 0, getHeight(), end);
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(new Color(255, 255, 200));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(Color.WHITE);
            }
        });
        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gp = new GradientPaint(0, 0, new Color(245, 245, 245),
                0, getHeight(), new Color(220, 220, 220));
        g2d.setPaint(gp);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        g2d.dispose();
    }

    public JButton getBtnConfirm() { return btnConfirm; }
    public JButton getBtnCancel() { return btnCancel; }

    // kiểm tra và set trạng thái processing
    public boolean isProcessing() {
        return isProcessing;
    }

    public void setProcessing(boolean processing) {
        this.isProcessing = processing;
        btnConfirm.setEnabled(!processing);
    }
    public String getUsername() {
        if (txtUsername == null) {
            return "";
        }
        String text = txtUsername.getText();
        if (text == null) {
            return "";
        }
        String trimmed = text.trim();
        System.out.println("DEBUG getUsername: '" + text + "' -> trimmed: '" + trimmed + "' (length: " + trimmed.length() + ")");
        return trimmed;
    }

    public String getPassword() {
        if (txtPassword == null) {
            return "";
        }
        char[] pwd = txtPassword.getPassword();
        if (pwd == null || pwd.length == 0) {
            System.out.println("DEBUG getPassword: NULL or EMPTY");
            return "";
        }
        String result = new String(pwd).trim();
        System.out.println("DEBUG getPassword: length = " + result.length());
        java.util.Arrays.fill(pwd, ' ');
        return result;
    }
    public void clearFields() {
        txtUsername.setText("");
        txtPassword.setText("");
        isProcessing = false; // ✅ Reset cờ khi clear
        SwingUtilities.invokeLater(() -> txtUsername.requestFocusInWindow());
    }
    @Override
    public void addNotify() {
        super.addNotify();
        // focus vào username
        SwingUtilities.invokeLater(() -> txtUsername.requestFocusInWindow());
    }
}