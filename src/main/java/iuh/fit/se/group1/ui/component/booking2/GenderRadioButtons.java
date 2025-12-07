package iuh.fit.se.group1.ui.component.booking2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Custom Gender Radio Buttons component with beautiful styling
 */
public class GenderRadioButtons extends JPanel {

    private JRadioButton maleRadio;
    private JRadioButton femaleRadio;
    private ButtonGroup genderGroup;

    public GenderRadioButtons() {
        initializeComponent();
        setupLayout();
        setupEventHandlers();
    }

    private void initializeComponent() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(200, 50));

        // Create radio buttons
        maleRadio = new JRadioButton("Nam");
        femaleRadio = new JRadioButton("Nữ");

        // Group radio buttons
        genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);

        // Style radio buttons
        styleRadioButton(maleRadio, new Color(54, 162, 235)); // Blue for male
        styleRadioButton(femaleRadio, new Color(255, 99, 132)); // Pink for female

        // Select male by default
        maleRadio.setSelected(true);
    }

    private void styleRadioButton(JRadioButton radio, Color accentColor) {
        radio.setBackground(Color.WHITE);
        radio.setForeground(Color.BLACK);
        radio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        radio.setFocusPainted(false);
        radio.setBorderPainted(false);
        radio.setOpaque(false);

        // Custom icon for radio button
        radio.setIcon(createCustomIcon(false, accentColor));
        radio.setSelectedIcon(createCustomIcon(true, accentColor));
        radio.setRolloverIcon(createCustomIcon(false, accentColor.brighter()));
        radio.setRolloverSelectedIcon(createCustomIcon(true, accentColor.brighter()));

        // Add some padding
        radio.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    private Icon createCustomIcon(boolean selected, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int size = Math.min(getIconWidth(), getIconHeight()) - 2;
                int centerX = x + size / 2;
                int centerY = y + size / 2;
                int radius = size / 2 - 1;

                // Draw outer circle
                g2d.setStroke(new BasicStroke(2));
                g2d.setColor(color);
                g2d.drawOval(centerX - radius, centerY - radius, radius * 2, radius * 2);

                // Draw inner circle if selected
                if (selected) {
                    int innerRadius = radius - 4;
                    g2d.setColor(color);
                    g2d.fillOval(centerX - innerRadius, centerY - innerRadius,
                            innerRadius * 2, innerRadius * 2);
                }

                g2d.dispose();
            }

            @Override
            public int getIconWidth() {
                return 16;
            }

            @Override
            public int getIconHeight() {
                return 16;
            }
        };
    }

    private void setupLayout() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

        // Add label
        JLabel genderLabel = new JLabel("Giới tính:");
        genderLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        genderLabel.setForeground(Color.BLACK);

        add(genderLabel);
        add(maleRadio);
        add(femaleRadio);
    }

    private void setupEventHandlers() {
        // Add hover effects
        addHoverEffect(maleRadio, new Color(54, 162, 235));
        addHoverEffect(femaleRadio, new Color(255, 99, 132));
    }

    private void addHoverEffect(JRadioButton radio, Color color) {
        radio.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                radio.setBackground(color.brighter());
                radio.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(color.brighter(), 1, true),
                        BorderFactory.createEmptyBorder(4, 9, 4, 9)));
                radio.setOpaque(true);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                radio.setBackground(Color.WHITE);
                radio.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                radio.setOpaque(false);
            }
        });
    }

    // Public methods to get/set selection
    public Boolean getSelectedGender() {
        if (femaleRadio.isSelected()) {
            return true;   // true = Nữ
        } else if (maleRadio.isSelected()) {
            return false;  // false = Nam
        }
        return null; // nếu chưa chọn gì
    }


    public void setSelectedGender(Boolean isFemale) {
        if (isFemale == null) {
            maleRadio.setSelected(false);
            femaleRadio.setSelected(false);
            return;
        }

        if (isFemale) {          // true = Nữ
            femaleRadio.setSelected(true);
        } else {                 // false = Nam
            maleRadio.setSelected(true);
        }
    }


    public boolean isMaleSelected() {
        return maleRadio.isSelected();
    }

    public boolean isFemaleSelected() {
        return femaleRadio.isSelected();
    }

    public void addGenderChangeListener(ActionListener listener) {
        maleRadio.addActionListener(listener);
        femaleRadio.addActionListener(listener);
    }

    // Enable/disable the component
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        maleRadio.setEnabled(enabled);
        femaleRadio.setEnabled(enabled);
    }
}