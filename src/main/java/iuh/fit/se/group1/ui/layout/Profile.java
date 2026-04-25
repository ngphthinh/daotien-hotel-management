/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.layout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import iuh.fit.se.group1.dto.EmployeeDTO;
import iuh.fit.se.group1.service.AccountService;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

/**
 * @author Windows
 */
public class Profile extends javax.swing.JPanel {
    private JTextField txtEmail;
    private JTextField txtUserName;
    private JTextField txtPhone;
    private JComboBox<String> cmbGender;
    private JComboBox<String> cmbRole;
    private JTextField txtHireDate;
    private JButton btnChangePass;

    // Info labels
    private JLabel lblName;
    private JLabel lblId;
    private JLabel lblRole;
    private static final Color PRIMARY_BLUE = new Color(108, 165, 200);
    private static final Color ACCENT_BLUE = new Color(66, 133, 244);
    private static final Color SUCCESS_GREEN = new Color(76, 175, 80);
    private static final Color DANGER_RED = new Color(244, 67, 54);
    private static final Color BG_LIGHT = new Color(248, 250, 252);
    private static final Color BORDER_COLOR = new Color(226, 232, 240);
    private static final Color TEXT_PRIMARY = new Color(51, 65, 85);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);

    /**
     * Creates new form Profile
     */
    public Profile() {
        initComponents();
        headerShift.getLblTile().setText("Quản lý nhân viên");
        headerShift.getLblSubTitle().setText("> Thông tin nhân viên");
        setupMainLayout();
        setupHeaderPanel();
        setupFormPanel();
    }

    public void setEmployeeInfo(EmployeeDTO employee) {
        if (employee == null) {
            return;
        }
        if (employee.getFullName() != null) {
            lblName.setText(employee.getFullName());
        }
        if (employee.getEmployeeId() != null) {
            lblId.setText(String.valueOf(employee.getEmployeeId()));
        }
        if (employee.getAvt() != null && employee.getAvt().length > 0) {
            avatarLabel.setImageFromBytes(employee.getAvt());
        } else {
            avatarLabel.resetToDefault();
        }
        if (employee.getEmail() != null) {
            txtEmail.setText(employee.getEmail());
        }
        if (employee.getPhone() != null) {
            txtPhone.setText(employee.getPhone());
        }
        if (employee.isGender()) {
            cmbGender.setSelectedItem("Nữ");
        } else {
            cmbGender.setSelectedItem("Nam");
        }
        if (employee.getAccount() != null
                && employee.getAccount().getRole() != null
                && employee.getAccount().getRole().getRoleName() != null) {
            cmbRole.setSelectedItem(employee.getAccount().getRole().getRoleName());
            lblRole.setText(employee.getAccount().getRole().getRoleName());
        }
        if (employee.getHireDate() != null) {
            txtHireDate.setText(employee.getHireDate().toString());
        }
        if (employee.getAccount() != null && employee.getAccount().getUsername() != null) {
            txtUserName.setText(employee.getAccount().getUsername());
        }
        System.out.println("Đã cập nhật thông tin employee lên Profile: " + employee.getFullName());
    }

    private void setupMainLayout() {
        setLayout(new BorderLayout());
        setBackground(BG_LIGHT);
        add(headerShift, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BG_LIGHT);

        pnlHead.setPreferredSize(new Dimension(0, 220));
        contentPanel.add(pnlHead, BorderLayout.NORTH);

        pnlBody = new JPanel(new BorderLayout());
        pnlBody.setBackground(BG_LIGHT);
        pnlBody.setOpaque(true);

        contentPanel.add(pnlBody, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);
    }

    private void setupHeaderPanel() {
        pnlHead.setLayout(null);
        pnlHead.setBackground(PRIMARY_BLUE);

        // Avatar
        avatarLabel.setBounds(60, 25, 150, 150);
        avatarLabel.setPreferredSize(new Dimension(150, 150));
        pnlHead.add(avatarLabel);

        // Tên nhân viên
        lblName = new JLabel("Nguyễn Trần Quốc Việt");
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblName.setForeground(Color.WHITE);
        lblName.setBounds(240, 60, 500, 40);
        pnlHead.add(lblName);

        JLabel lblIdTitle = new JLabel("Mã nhân viên:");
        lblIdTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblIdTitle.setForeground(new Color(230, 240, 250));
        lblIdTitle.setBounds(240, 100, 120, 25);
        pnlHead.add(lblIdTitle);

        // Mã nhân viên
        lblId = new JLabel("NV00947263554");
        lblId.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblId.setForeground(new Color(230, 240, 250));
        lblId.setBounds(240 + 110, 100, 200, 25);
        pnlHead.add(lblId);

        lblRole = createRole("Nhân viên quản lý");
        lblRole.setBounds(240, 130, 130, 28);
        pnlHead.add(lblRole);

        // Nút Đổi mật khẩu
        btnChangePass = createModernButton("Đổi mật khẩu", ACCENT_BLUE, 140);
        btnChangePass.setBounds(800, 70, 160, 40);
        btnChangePass.addActionListener(e -> handleChangePassword());
        pnlHead.add(btnChangePass);
    }

    private void setupFormPanel() {
        pnlBody.setLayout(new BorderLayout());
        pnlBody.setBackground(BG_LIGHT);
        // Form container
        JPanel formContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 8));
                g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 12, 12);
            }
        };
        formContainer.setLayout(null);
        formContainer.setBackground(Color.WHITE);

        // Section title
        JLabel sectionTitle = new JLabel("Thông tin cá nhân");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(TEXT_PRIMARY);
        sectionTitle.setBounds(30, 20, 300, 30);
        formContainer.add(sectionTitle);

        // Divider
        JSeparator divider = new JSeparator();
        divider.setBounds(30, 55, 976, 1);
        divider.setForeground(BORDER_COLOR);
        formContainer.add(divider);

        buildFormFields(formContainer);
        pnlBody.add(formContainer, BorderLayout.CENTER);
    }

    private void buildFormFields(JPanel container) {
        int leftColX = 30;
        int rightColX = 533;
        int fieldWidth = 473;
        int fieldHeight = 42;
        int labelHeight = 22;
        int startY = 75;
        int rowGap = 85;

        // Row 1: Gender and Position
        addFormField(container, "Giới tính", leftColX, startY, fieldWidth, fieldHeight, labelHeight, () -> {
            cmbGender = createStyledComboBox(new String[]{"Nữ", "Nam"});
            return cmbGender;
        });

        addFormField(container, "Chức vụ", rightColX, startY, fieldWidth, fieldHeight, labelHeight, () -> {
            cmbRole = createStyledComboBox(new String[]{"Nhân viên lễ tân", "Nhân viên quản lý"});
            return cmbRole;
        });

        // Row 2: Email and Start Date
        int row2Y = startY + rowGap;
        addFormField(container, "Email", leftColX, row2Y, fieldWidth, fieldHeight, labelHeight, () -> {
            txtEmail = createStyledTextField("example@company.com");
            return txtEmail;
        });

        addFormField(container, "Ngày bắt đầu", rightColX, row2Y, fieldWidth, fieldHeight, labelHeight, () -> {
            txtHireDate = createStyledTextField("DD/MM/YYYY");
            txtHireDate.setEditable(false); // KHÔNG CHO SỬA

            JPanel datePanel = new JPanel(new BorderLayout());
            datePanel.setBackground(Color.WHITE);
            datePanel.setOpaque(true);
            txtHireDate.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 8));
            txtHireDate.setBackground(Color.WHITE);
            // Tạo icon lịch
            JLabel calendarIcon = new JLabel(FontIcon.of(FontAwesomeSolid.CALENDAR_ALT, 16, new Color(100, 116, 139)));
            calendarIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
            calendarIcon.setToolTipText("Chọn ngày");
            calendarIcon.setHorizontalAlignment(SwingConstants.CENTER);
            calendarIcon.setPreferredSize(new Dimension(30, fieldHeight));

            // Thêm textfield và icon vào panel
            datePanel.add(txtHireDate, BorderLayout.CENTER);
            datePanel.add(calendarIcon, BorderLayout.EAST);
            datePanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1),
                    BorderFactory.createEmptyBorder(0, 8, 0, 8)
            ));

            return datePanel;
        });

        // Row 3
        int row3Y = row2Y + rowGap;
        addFormField(container, "Số điện thoại", leftColX, row3Y, fieldWidth, fieldHeight, labelHeight, () -> {
            txtPhone = createStyledTextField("XXX.XXXX.XXX");
            return txtPhone;
        });

        addFormField(container, "Tên đăng nhập", rightColX, row3Y, fieldWidth, fieldHeight, labelHeight, () -> {
            txtUserName = createStyledTextField("username");
            return txtUserName;
        });
    }

    private void addFormField(JPanel container, String labelText, int x, int y,
                              int fieldWidth, int fieldHeight, int labelHeight,
                              ComponentSupplier supplier) {
        JLabel label = createLabel(labelText);
        label.setBounds(x, y, 200, labelHeight);
        container.add(label);

        JComponent field = supplier.get();
        field.setBounds(x, y + 28, fieldWidth, fieldHeight);
        container.add(field);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_SECONDARY);
        return label;
    }

    private JTextField createStyledTextField(final String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner() && !placeholder.isEmpty()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2.setColor(new Color(160, 174, 192));
                    g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                    g2.drawString(placeholder, 14, 26);
                    g2.dispose();
                }
            }
        };

        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setEditable(false);
        field.setFocusable(false);
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(ACCENT_BLUE, 2),
                        BorderFactory.createEmptyBorder(7, 11, 7, 11)
                ));
                field.repaint();
            }

            public void focusLost(FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR, 1),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
                field.repaint();
            }
        });

        return field;
    }

    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(Color.WHITE);
        combo.setEnabled(false);
        combo.setForeground(TEXT_PRIMARY);
        combo.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        combo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        combo.setFocusable(false);
        combo.addActionListener(e -> combo.setSelectedIndex(combo.getSelectedIndex()));
        return combo;
    }

    private JButton createModernButton(String text, Color bgColor, int minWidth) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color color = bgColor;
                if (getModel().isPressed()) {
                    color = darken(bgColor, 0.15f);
                } else if (getModel().isRollover()) {
                    color = brighten(bgColor, 0.1f);
                }

                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent()) / 2 - 2;
                g2.drawString(getText(), textX, textY);

                g2.dispose();
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(minWidth, 42));

        return button;
    }

    private JLabel createRole(String text) {
        JLabel badge = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(220, 252, 231));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badge.setForeground(new Color(22, 163, 74));
        badge.setHorizontalAlignment(SwingConstants.CENTER);
        return badge;
    }

    private void handleChangePassword() {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
                "Đổi mật khẩu", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(420, 330);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);

        JLabel lblOld = new JLabel("Mật khẩu cũ");
        lblOld.setBounds(30, 20, 200, 20);

        JPasswordField txtOld = new JPasswordField();
        txtOld.setBounds(30, 45, 300, 38);

        JLabel lblNew = new JLabel("Mật khẩu mới");
        lblNew.setBounds(30, 90, 200, 20);

        JPasswordField txtNew = new JPasswordField();
        txtNew.setBounds(30, 115, 300, 38);

        JLabel lblConfirm = new JLabel("Xác nhận mật khẩu");
        lblConfirm.setBounds(30, 160, 200, 20);

        JPasswordField txtConfirm = new JPasswordField();
        txtConfirm.setBounds(30, 185, 300, 38);

        // 👁 Eye buttons
        JButton eyeOld = createEyeButton(txtOld);
        eyeOld.setBounds(340, 45, 38, 38);

        JButton eyeNew = createEyeButton(txtNew);
        eyeNew.setBounds(340, 115, 38, 38);

        JButton eyeConfirm = createEyeButton(txtConfirm);
        eyeConfirm.setBounds(340, 185, 38, 38);

        JButton btnSave = new JButton("Xác nhận");
        btnSave.setBounds(80, 240, 120, 38);

        JButton btnCancel = new JButton("Hủy");
        btnCancel.setBounds(220, 240, 120, 38);

        btnSave.addActionListener(e -> {
            String oldPass = new String(txtOld.getPassword());
            String newPass = new String(txtNew.getPassword());
            String confirm = new String(txtConfirm.getPassword());

            if (oldPass.isEmpty() || newPass.isEmpty() || confirm.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "Vui lòng nhập đầy đủ thông tin",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!isValidPassword(newPass)) {
                String errorMessage = getPasswordValidationMessage(newPass);
                JOptionPane.showMessageDialog(dialog,
                        errorMessage,
                        "Mật khẩu không hợp lệ", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!newPass.equals(confirm)) {
                JOptionPane.showMessageDialog(dialog,
                        "Mật khẩu xác nhận không khớp",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            AccountService accountService = new AccountService();

            boolean success = accountService
                    .changePassword(txtUserName.getText(), oldPass, newPass);

            if (success) {
                JOptionPane.showMessageDialog(dialog,
                        "Đổi mật khẩu thành công ",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog,
                        "Mật khẩu cũ không đúng ",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        panel.add(lblOld);
        panel.add(txtOld);
        panel.add(lblNew);
        panel.add(txtNew);
        panel.add(lblConfirm);
        panel.add(txtConfirm);
        panel.add(eyeOld);
        panel.add(eyeNew);
        panel.add(eyeConfirm);
        panel.add(btnSave);
        panel.add(btnCancel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private JButton createEyeButton(JPasswordField passwordField) {
        JButton btn = new JButton(
                FontIcon.of(FontAwesomeSolid.EYE_SLASH, 18, new Color(100, 116, 139))
        );

        btn.setBorder(null);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        passwordField.setEchoChar('*');

        btn.addActionListener(e -> {
            if (passwordField.getEchoChar() == '*') {
                passwordField.setEchoChar((char) 0);
                btn.setIcon(
                        FontIcon.of(FontAwesomeSolid.EYE, 18, new Color(66, 133, 244))
                );
            } else {
                passwordField.setEchoChar('*');
                btn.setIcon(
                        FontIcon.of(FontAwesomeSolid.EYE_SLASH, 18, new Color(100, 116, 139))
                );
            }
        });

        return btn;
    }

    private Color brighten(Color color, float factor) {
        int r = Math.min(255, (int) (color.getRed() + (255 - color.getRed()) * factor));
        int g = Math.min(255, (int) (color.getGreen() + (255 - color.getGreen()) * factor));
        int b = Math.min(255, (int) (color.getBlue() + (255 - color.getBlue()) * factor));
        return new Color(r, g, b);
    }

    private Color darken(Color color, float factor) {
        int r = (int) (color.getRed() * (1 - factor));
        int g = (int) (color.getGreen() * (1 - factor));
        int b = (int) (color.getBlue() * (1 - factor));
        return new Color(r, g, b);
    }

    /**
     * Kiểm tra mật khẩu có hợp lệ không
     */
    private boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUpperCase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (isSpecialCharacter(c)) {
                hasSpecialChar = true;
            }
        }

        return hasUpperCase && hasDigit && hasSpecialChar;
    }

    /**
     * Kiểm tra ký tự có phải ký tự đặc biệt không
     */
    private boolean isSpecialCharacter(char c) {
        String specialChars = "!@#$%^&*()_+-=[]{}|;:',.<>?/`~\"\\";
        return specialChars.indexOf(c) >= 0;
    }

    /**
     * Lấy thông báo lỗi cho mật khẩu không hợp lệ
     */
    private String getPasswordValidationMessage(String password) {
        if (password == null || password.isEmpty()) {
            return "Mật khẩu không được để trống";
        }

        StringBuilder message = new StringBuilder("Mật khẩu không hợp lệ:\n");

        if (password.length() < 8) {
            message.append("• Mật khẩu phải có ít nhất 8 ký tự\n");
        }

        boolean hasUpperCase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (isSpecialCharacter(c)) {
                hasSpecialChar = true;
            }
        }

        if (!hasUpperCase) {
            message.append("• Mật khẩu phải có ít nhất 1 ký tự in hoa\n");
        }

        if (!hasDigit) {
            message.append("• Mật khẩu phải có ít nhất 1 chữ số\n");
        }

        if (!hasSpecialChar) {
            message.append("• Mật khẩu phải có ít nhất 1 ký tự đặc biệt (!@#$%^&*...)\n");
        }

        return message.toString().trim();
    }


    @FunctionalInterface
    private interface ComponentSupplier {
        JComponent get();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerShift = new iuh.fit.se.group1.ui.component.HeaderShift();
        pnlHead = new javax.swing.JPanel();
        avatarLabel = new iuh.fit.se.group1.ui.component.custom.AvatarLabel();
        pnlBody = new javax.swing.JPanel();

        pnlHead.setBackground(new java.awt.Color(108, 165, 200));

        javax.swing.GroupLayout avatarLabel1Layout = new javax.swing.GroupLayout(avatarLabel);
        avatarLabel.setLayout(avatarLabel1Layout);
        avatarLabel1Layout.setHorizontalGroup(
                avatarLabel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 60, Short.MAX_VALUE)
        );
        avatarLabel1Layout.setVerticalGroup(
                avatarLabel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 60, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(pnlHead);
        pnlHead.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(104, 104, 104)
                                .addComponent(avatarLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap(88, Short.MAX_VALUE)
                                .addComponent(avatarLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(67, 67, 67))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(pnlBody);
        pnlBody.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 1200, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 498, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(headerShift, javax.swing.GroupLayout.DEFAULT_SIZE, 1200, Short.MAX_VALUE)
                        .addComponent(pnlHead, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pnlBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(headerShift, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(pnlHead, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(pnlBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private iuh.fit.se.group1.ui.component.custom.AvatarLabel avatarLabel;
    private iuh.fit.se.group1.ui.component.HeaderShift headerShift;
    private javax.swing.JPanel pnlHead;
    private javax.swing.JPanel pnlBody;
    // End of variables declaration//GEN-END:variables
}
