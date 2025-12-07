/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iuh.fit.se.group1.ui.component.custom;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author THIS PC
 */
public class Button extends JButton{
    
    private int borderRadius = 30;


    public Button() {
        super("Button"); 
        setContentAreaFilled(false); // để tự vẽ nền
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public Button(String text) {
        super(text);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }


    public static Button createSuccessButton(String title, Icon icon) {
        Button btn = new Button(title);
        btn.setFont(new Font("Dialog", Font.BOLD, 14));
        btn.setIcon(icon);
        btn.setBorderRadius(5);
        btn.setBackground(new Color(40, 167, 69));
        btn.setForeground(Color.WHITE);
        return btn;
    }

    public static Button createDangerButton(String title){
        Button btn = new Button(title);
        btn.setFont(new Font("Dialog", Font.BOLD, 14));
        btn.setBackground(new Color(220, 53, 69));
        btn.setForeground(Color.WHITE);
        return btn;
    }

    public static Button createWarningButton(String title, Icon icon) {
        Button btn = new Button(title);
        btn.setFont(new Font("Dialog", Font.BOLD, 14));
        btn.setIcon(icon);
        btn.setBorderRadius(5);
        btn.setBackground(new Color(255, 193, 7));
        btn.setForeground(Color.WHITE);
        return btn;
    }


    /**
     * Set border radius
     * @param radius bán kính bo góc
     */
    public void setBorderRadius(int radius) {
        this.borderRadius = radius;
        repaint();
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

    /**
     * Set icon and position of text
     * @param icon
     * @param textPosition lấy từ {@link SwingConstants}
     */
    public void setIcon (Icon icon, int textPosition) {
        super.setIcon(icon);
        setHorizontalTextPosition(textPosition);
        setIconTextGap(10);
    }

//    @Override
//    protected void paintBorder(Graphics g) {
//        Graphics2D g2 = (Graphics2D) g.create();
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//
//        g2.setColor(getForeground());
//        g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, borderRadius, borderRadius);
//
//        g2.dispose();
//    }


}
