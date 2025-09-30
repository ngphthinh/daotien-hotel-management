package iuh.fit.se.group1.ui.component.menu;

import iuh.fit.se.group1.ui.component.effect.RippleEffect;
import iuh.fit.se.group1.util.Constants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public class MenuItem extends JButton {

    private int index;
    private boolean subMenuAble;
    //
    private int subMenuIndex;
    private int length;

    private float animate;
    private RippleEffect rippleEffect;

    public MenuItem(String name, int index, boolean subMenuAble) {
        super(name);
        this.setFont( new Font("Poppins", Font.BOLD, 15));
        this.index = index;
        this.subMenuAble = subMenuAble;
        setContentAreaFilled(false);
        setForeground(Constants.FOREGROUND_COLOR_MENU);
        setHorizontalAlignment(SwingConstants.LEFT);
        setBorder(new EmptyBorder(12, 16, 12, 16));
        setIconTextGap(10);
        rippleEffect = new RippleEffect(this);
        rippleEffect.setRippleColor(new Color(220, 220, 220));
    }


    public void initSubMenu(int subMenuIndex, int length) {
        this.subMenuIndex = subMenuIndex;
        this.length = length;
        setBorder(new EmptyBorder(9, 35, 9, 10));
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (length != 0) {
            g2.setColor(Constants.LINE_SUBMENU);
            if (subMenuIndex == length - 1) {
                //  Last Index
                g2.drawLine(18, 0, 18, getHeight() / 2);
                g2.drawLine(18, getHeight() / 2, 26, getHeight() / 2);
            } else {
                g2.drawLine(18, 0, 18, getHeight());
                g2.drawLine(18, getHeight() / 2, 26, getHeight() / 2);
            }

        }else if (subMenuAble) {
            g2.setColor(getForeground());
            int arrowWidth = 8;
            int arrowHeight = 4;
            Path2D path = new Path2D.Double();
            path.moveTo(0,arrowHeight * animate);
            path.lineTo(arrowWidth/2.0,(1-animate)*arrowHeight);
            path.lineTo(arrowWidth,arrowHeight * animate);
            g2.translate(getWidth() - arrowWidth - 15, (getHeight() - arrowHeight) / 2.0);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2.draw(path);
        }
        g2.dispose();
        rippleEffect.reder(g, new Rectangle2D.Double(0,0,getWidth(),getHeight()));
    }


    public float getAnimate() {
        return animate;
    }

    public void setAnimate(float animate) {
        this.animate = animate;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isSubMenuAble() {
        return subMenuAble;
    }

    public void setSubMenuAble(boolean subMenuAble) {
        this.subMenuAble = subMenuAble;
    }

    public int getSubMenuIndex() {
        return subMenuIndex;
    }

    public void setSubMenuIndex(int subMenuIndex) {
        this.subMenuIndex = subMenuIndex;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
