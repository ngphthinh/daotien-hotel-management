/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.layout;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;
/**
 *
 * @author Windows
 */
public class Profile extends javax.swing.JPanel {
    private JTextField emailField;
    private JTextField loginNameField;
    private JTextField phoneField;
    private JComboBox<String> genderCombo;
    private JComboBox<String> positionCombo;
    private JTextField birthdateField;
    private JButton changePasswordBtn;

    // Info labels
    private JLabel nameLabel;
    private JLabel idLabel;
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
        headerShift1.getLblTile().setText("Quản lý nhân viên");
        headerShift1.getLblSubTitle().setText("> Thông tin nhân viên");
        setupMainLayout();
        setupHeaderPanel();
        setupFormPanel();
    }
    private void setupMainLayout() {
        setLayout(new BorderLayout());
        setBackground(BG_LIGHT);
        add(headerShift1, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BG_LIGHT);
        
        jPanel1.setPreferredSize(new Dimension(0, 220));
        contentPanel.add(jPanel1, BorderLayout.NORTH);
        
        jPanel2 = new JPanel(new BorderLayout());
        jPanel2.setBackground(BG_LIGHT);
        jPanel2.setOpaque(true);
        
        contentPanel.add(jPanel2, BorderLayout.CENTER);
        
        add(contentPanel, BorderLayout.CENTER);
    }
    private void setupHeaderPanel() {
        jPanel1.setLayout(null);
        jPanel1.setBackground(PRIMARY_BLUE);

        // Avatar
        avatarLabel1.setBounds(60, 25, 150, 150);
        avatarLabel1.setPreferredSize(new Dimension(150, 150));
        jPanel1.add(avatarLabel1);

        // Tên nhân viên
        nameLabel = new JLabel("Nguyễn Trần Quốc Việt");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds(240, 60, 500, 40);
        jPanel1.add(nameLabel);

        // Mã nhân viên
        idLabel = new JLabel("NV00947263554");
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        idLabel.setForeground(new Color(230, 240, 250));
        idLabel.setBounds(240, 100, 200, 25);
        jPanel1.add(idLabel);

        JLabel role = createRole("Nhân viên quản lý");
        role.setBounds(240, 130, 130, 28);
        jPanel1.add(role);

        // Nút Đổi mật khẩu
        changePasswordBtn = createModernButton("Đổi mật khẩu", ACCENT_BLUE, 140);
        changePasswordBtn.setBounds(800, 70, 160, 40);
        changePasswordBtn.addActionListener(e -> handleChangePassword());
        jPanel1.add(changePasswordBtn);
    }
     private void setupFormPanel() {
        jPanel2.setLayout(new BorderLayout());
        jPanel2.setBackground(BG_LIGHT);
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
        jPanel2.add(formContainer, BorderLayout.CENTER);
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
            genderCombo = createStyledComboBox(new String[]{"Nam", "Nữ", "Khác"});
            return genderCombo;
        });
        
        addFormField(container, "Chức vụ", rightColX, startY, fieldWidth, fieldHeight, labelHeight, () -> {
            positionCombo = createStyledComboBox(new String[]{"Nhân viên", "Quản lý", "Giám đốc"});
            return positionCombo;
        });
        
        // Row 2: Email and Start Date
        int row2Y = startY + rowGap;
        addFormField(container, "Email", leftColX, row2Y, fieldWidth, fieldHeight, labelHeight, () -> {
            emailField = createStyledTextField("example@company.com");
            return emailField;
        });
        
        addFormField(container, "Ngày bắt đầu", rightColX, row2Y, fieldWidth, fieldHeight, labelHeight, () -> {
            birthdateField = createStyledTextField("DD/MM/YYYY");
            birthdateField.setEditable(false); // KHÔNG CHO SỬA
            
            JPanel datePanel = new JPanel(new BorderLayout());
            datePanel.setBackground(Color.WHITE);
            datePanel.setOpaque(true);
            birthdateField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 8));
            birthdateField.setBackground(Color.WHITE);
            // Tạo icon lịch
            JLabel calendarIcon = new JLabel(FontIcon.of(FontAwesomeSolid.CALENDAR_ALT, 16, new Color(100, 116, 139)));
            calendarIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
            calendarIcon.setToolTipText("Chọn ngày");
            calendarIcon.setHorizontalAlignment(SwingConstants.CENTER);
            calendarIcon.setPreferredSize(new Dimension(30, fieldHeight));

            // Thêm textfield và icon vào panel
            datePanel.add(birthdateField, BorderLayout.CENTER);
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
            phoneField = createStyledTextField("XXX.XXXX.XXX");
            return phoneField;
        });
        
        addFormField(container, "Tên đăng nhập", rightColX, row3Y, fieldWidth, fieldHeight, labelHeight, () -> {
            loginNameField = createStyledTextField("username");
            return loginNameField;
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
        combo.setEnabled(true);
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
        JOptionPane.showMessageDialog(this, 
            "Tính năng đổi mật khẩu sẽ được cập nhật sau.", 
            "Đổi mật khẩu", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    
    private Color brighten(Color color, float factor) {
        int r = Math.min(255, (int)(color.getRed() + (255 - color.getRed()) * factor));
        int g = Math.min(255, (int)(color.getGreen() + (255 - color.getGreen()) * factor));
        int b = Math.min(255, (int)(color.getBlue() + (255 - color.getBlue()) * factor));
        return new Color(r, g, b);
    }
    
    private Color darken(Color color, float factor) {
        int r = (int)(color.getRed() * (1 - factor));
        int g = (int)(color.getGreen() * (1 - factor));
        int b = (int)(color.getBlue() * (1 - factor));
        return new Color(r, g, b);
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

        headerShift1 = new iuh.fit.se.group1.ui.component.HeaderShift();
        jPanel1 = new javax.swing.JPanel();
        avatarLabel1 = new iuh.fit.se.group1.ui.component.custom.AvatarLabel();
        jPanel2 = new javax.swing.JPanel();

        jPanel1.setBackground(new java.awt.Color(108, 165, 200));

        javax.swing.GroupLayout avatarLabel1Layout = new javax.swing.GroupLayout(avatarLabel1);
        avatarLabel1.setLayout(avatarLabel1Layout);
        avatarLabel1Layout.setHorizontalGroup(
            avatarLabel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );
        avatarLabel1Layout.setVerticalGroup(
            avatarLabel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(104, 104, 104)
                .addComponent(avatarLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(88, Short.MAX_VALUE)
                .addComponent(avatarLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
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
            .addComponent(headerShift1, javax.swing.GroupLayout.DEFAULT_SIZE, 1200, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerShift1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private iuh.fit.se.group1.ui.component.custom.AvatarLabel avatarLabel1;
    private iuh.fit.se.group1.ui.component.HeaderShift headerShift1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
