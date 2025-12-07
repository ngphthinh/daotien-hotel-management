package iuh.fit.se.group1.ui.component.booking2;

import javax.swing.border.Border;

import java.awt.*;
import javax.swing.border.Border;

public class RoundedBorder implements Border {

    private int radius;

    public RoundedBorder(int radius) {
        this.radius = radius;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(radius + 1, radius + 1, radius + 2, radius + 1);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(new Color(200, 200, 200));
        g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }
}

