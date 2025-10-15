package iuh.fit.se.group1.ui.component.custom;

import javax.swing.*;
import java.awt.*;

public class CircularProgress extends JPanel {
    private int progress = 42; // phần trăm (0–100)
    private Color trackColor = new Color(220, 220, 220);
    private Color progressColor = new Color(255, 102, 0);
    private Color textColor = Color.DARK_GRAY;
    private int thickness = 6; // độ dày nhỏ lại

    public CircularProgress() {
        setPreferredSize(new Dimension(50, 50)); // kích thước nhỏ
        setBackground(Color.WHITE);
    }

    public void setProgress(int progress) {
        this.progress = Math.max(0, Math.min(100, progress));
        repaint();
    }

    public void setProgressColor( Color progressColor) {
        this.progressColor = progressColor;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        int size = Math.min(getWidth(), getHeight());
        int x = (getWidth() - size) / 2;
        int y = (getHeight() - size) / 2;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int arcDiameter = size - thickness;
        int arcX = x + thickness / 2;
        int arcY = y + thickness / 2;

        // vòng nền
        g2.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(trackColor);
        g2.drawArc(arcX, arcY, arcDiameter, arcDiameter, 0, 360);

        // phần progress
        g2.setColor(progressColor);
        g2.drawArc(arcX, arcY, arcDiameter, arcDiameter, 90, -(int) (progress * 3.6));

        // chữ %
        g2.setFont(new Font("Arial", Font.BOLD, 13));
        String text = progress + "%";
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textAscent = fm.getAscent();
        int textDescent = fm.getDescent();

        g2.setColor(textColor);
        // canh giữa cả chiều ngang và chiều dọc
        int textX = (getWidth() - textWidth) / 2;
        int textY = (getHeight() + (textAscent - textDescent)) / 2;
        g2.drawString(text, textX, textY);

        g2.dispose();
    }

}