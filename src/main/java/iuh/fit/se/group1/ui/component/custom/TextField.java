package iuh.fit.se.group1.ui.component.custom;

import javax.swing.*;
import java.awt.*;

public class TextField extends JTextField {

    private int borderRadius = 15;

    public TextField() {
        super();
        setOpaque(false); // để tự vẽ nền
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // padding bên trong
    }

    public TextField(String text) {
        super(text);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }


    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ nền bo góc
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), borderRadius, borderRadius);

        g2.dispose();
        super.paintComponent(g);
    }
}
