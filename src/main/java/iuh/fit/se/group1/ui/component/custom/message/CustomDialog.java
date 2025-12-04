package iuh.fit.se.group1.ui.component.custom.message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.RoundRectangle2D;

public class CustomDialog extends JDialog {

    public enum MessageType { INFO, WARNING, ERROR, SUCCESS }

    private int result = JOptionPane.CLOSED_OPTION;

    public CustomDialog(Frame parent, String title, String message, MessageType type, boolean showCancel,int width, int height) {
        super(parent, title, true);
        setUndecorated(true);
        setSize(width, height);
        setLocationRelativeTo(parent);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 25, 25));

        // Shadow effect
        getRootPane().setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1)
        ));

        // Background blur panel
        JPanel background = new JPanel(new BorderLayout(15, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(255, 255, 255, 240));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.dispose();
            }
        };
        background.setBorder(BorderFactory.createEmptyBorder(25, 25, 20, 25));
        background.setOpaque(false);

        // Icon + message
        JPanel center = new JPanel(new BorderLayout(10, 0));
        center.setOpaque(false);
        JLabel iconLabel = new JLabel(getIcon(type));
        JLabel msgLabel = new JLabel("<html><body style='width:"+(width)+"px'>" + message + "</body></html>");
        msgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        msgLabel.setForeground(new Color(40, 40, 40));
        center.add(iconLabel, BorderLayout.WEST);
        center.add(msgLabel, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        JButton ok = createButton("OK", new Color(56, 142, 60), new Color(102, 187, 106));
        ok.addActionListener((ActionEvent e) -> {
            result = JOptionPane.OK_OPTION;
            dispose();
        });

        btnPanel.add(ok);

        if (showCancel) {
            JButton cancel = createButton("Hủy", new Color(211, 47, 47), new Color(239, 83, 80));
            cancel.addActionListener(e -> {
                result = JOptionPane.CANCEL_OPTION;
                dispose();
            });
            btnPanel.add(cancel);
        }

        background.add(center, BorderLayout.CENTER);
        background.add(btnPanel, BorderLayout.SOUTH);

        add(background);
    }

    private JButton createButton(String text, Color color1, Color color2) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private Icon getIcon(MessageType type) {
        String iconKey = switch (type) {
            case INFO -> "OptionPane.informationIcon";
            case WARNING -> "OptionPane.warningIcon";
            case ERROR -> "OptionPane.errorIcon";
            case SUCCESS -> "OptionPane.informationIcon";
        };
        return UIManager.getIcon(iconKey);
    }

    // Method showMessage to lớn hơn
    public static int showMessage(Component parent, String message, String title, MessageType type, int width, int height) {
        Frame frame = JOptionPane.getFrameForComponent(parent);
        CustomDialog dialog = new CustomDialog(frame, title, message, type, false, width, height);
        dialog.setVisible(true);
        return dialog.result;
    }

    // Method showConfirm to lớn hơn
    public static int showConfirm(Component parent, String message, String title, MessageType type, int width, int height) {
        Frame frame = JOptionPane.getFrameForComponent(parent);
        CustomDialog dialog = new CustomDialog(frame, title, message, type, true, width, height);
        dialog.setVisible(true);
        return dialog.result;
    }
}
