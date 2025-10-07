package iuh.fit.se.group1.ui.component.menu;

import iuh.fit.se.group1.ui.component.effect.RippleEffect;
import iuh.fit.se.group1.util.Constants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class MenuIconItem extends JButton {
    private int index;
    private boolean subMenuAble;
    //
    private int length;

    private float animate;
    private RippleEffect rippleEffect;

    public MenuIconItem(int index) {
        this.index = index;
        setContentAreaFilled(false);
        setForeground(Constants.FOREGROUND_COLOR_MENU);
        setBorder(new EmptyBorder(12, 10, 12, 16));
        setIconTextGap(10);
        rippleEffect = new RippleEffect(this);
        rippleEffect.setRippleColor(new Color(220, 220, 220));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
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


    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
