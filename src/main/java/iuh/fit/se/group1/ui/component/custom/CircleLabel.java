/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iuh.fit.se.group1.ui.component.custom;

/**
 *
 * @author THIS PC
 */
import javax.swing.*;
import java.awt.*;

public class CircleLabel extends JLabel {

    public CircleLabel(String text) {
        super(text, SwingConstants.CENTER);
        setOpaque(false); // Không dùng nền mặc định
    }

    public CircleLabel() {
        super("Label", SwingConstants.CENTER); // Constructor mặc định
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        int diameter = Math.min(getWidth(), getHeight());

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ nền tròn
        g2.setColor(getBackground());
        g2.fillOval(0, 0, diameter, diameter);

        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        int diameter = Math.min(getWidth(), getHeight());

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ viền tròn
        g2.setColor(Color.BLACK);
        g2.drawOval(0, 0, diameter - 1, diameter - 1);

        g2.dispose();
    }
}

