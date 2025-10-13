/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iuh.fit.se.group1.ui.component.booking;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 *
 * @author THIS PC
 */
public class Search extends JTextField {

    private int borderRadius = 40;
    private Color borderColor = new Color(108, 165, 200);
    private Color focusBorderColor = new Color(108, 165, 200);
    private Color backgroundColor = Color.WHITE;
    private boolean focused = false;
    private Icon searchIcon;

    public Search() {
        init();
    }

    private void init() {
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 36)); // chừa khoảng bên phải cho icon
        setFont(new Font("Segoe UI", Font.PLAIN, 13));
        setForeground(new Color(60, 60, 60));
        setBackground(backgroundColor);

        // Icon kính lúp
        searchIcon = FontIcon.of(FontAwesomeSolid.SEARCH, 14, new Color(108, 165, 200));

        addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                focused = true;
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                focused = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();

        // Vẽ nền
        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, w - 1, h - 1, borderRadius, borderRadius);

        // Vẽ text
        super.paintComponent(g2);

        // Vẽ icon kính lúp (bên phải)
        if (searchIcon != null) {
            int iconY = (h - searchIcon.getIconHeight()) / 2;
            int iconX = w - searchIcon.getIconWidth() - 14;
            searchIcon.paintIcon(this, g2, iconX, iconY);
        }

        // Vẽ viền
        g2.setStroke(new BasicStroke(1.4f));
        g2.setColor(focused ? focusBorderColor : borderColor);
        g2.drawRoundRect(1, 1, w - 3, h - 3, borderRadius, borderRadius);

        g2.dispose();
    }
}
