package iuh.fit.se.group1.ui.component.custom;

import javax.swing.*;
import java.awt.*;

public class TextField extends JTextField {

    private int borderRadius = 15;

    private Color borderColor = Color.BLACK;


    public TextField() {
        init();
    }

    public TextField(String text) {
        super(text);
        init();
    }

    private void init() {
        setOpaque(false); // để tự vẽ nền
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // padding bên trong
        setBackground(Color.WHITE); // đảm bảo nền trắng
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 🌕 Nền trắng bo góc
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), borderRadius, borderRadius);

        // Vẽ text và caret (phải sau khi vẽ nền)
        super.paintComponent(g);

        // ⚫ Viền đen bo góc (vẽ SAU CÙNG để không bị đè)
        g2.setColor(borderColor);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, borderRadius, borderRadius);

        g2.dispose();
    }

    public void setBorderColor(Color color) {
        this.borderColor = color;
        repaint();
    }
    
    public void setBorderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
        repaint();
    }
}
