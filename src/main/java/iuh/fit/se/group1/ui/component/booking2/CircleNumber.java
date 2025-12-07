package iuh.fit.se.group1.ui.component.booking2;

import java.awt.*;
import javax.swing.*;

public class CircleNumber extends JPanel {

    private String number;
    private boolean active = false;

    public CircleNumber() {
        this("1");
    }

    public CircleNumber(String number) {
        this.number = number;
        setPreferredSize(new Dimension(40, 40));
        setOpaque(false);
    }

    public void setNumber(String number) {
        this.number = number;
        repaint();
    }

    public String getNumber() {
        return number;
    }

    /**
     * Kích hoạt hoặc tắt chế độ active
     */
    public void setActive(boolean active) {
        this.active = active;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int diameter = Math.min(getWidth(), getHeight());

        if (active) {
            // nền xanh
            g2.setColor(new Color(77, 134, 168));
            g2.fillOval(2, 2, diameter - 4, diameter - 4);

            // viền xanh đậm hơn
            g2.setColor(new Color(19, 113, 169));
            g2.setStroke(new BasicStroke(3));
            g2.drawOval(2, 2, diameter - 4, diameter - 4);

            // chữ trắng
            g2.setColor(Color.WHITE);

        } else {
            // không active → viền xám, nền trong suốt
            g2.setColor(new Color(170, 170, 170));
            g2.setStroke(new BasicStroke(3));
            g2.drawOval(2, 2, diameter - 4, diameter - 4);

            g2.setColor(new Color(185, 185, 185));
        }

        // Vẽ số
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(number)) / 2;
        int y = (getHeight() + fm.getAscent()) / 2 - 3;

        g2.drawString(number, x, y);
    }
}
