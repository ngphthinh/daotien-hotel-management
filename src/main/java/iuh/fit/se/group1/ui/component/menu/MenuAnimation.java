package iuh.fit.se.group1.ui.component.menu;

import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;

import java.awt.*;

public class MenuAnimation {
    public static void showMenu(Component component, MenuItem item, MigLayout layout, boolean show){
        int height = component.getPreferredSize().height;
        Animator animator = new Animator(300, new TimingTargetAdapter(){
            @Override
            public void timingEvent(float fraction) {
                float f = show ? fraction : 1 - fraction;
                layout.setComponentConstraints(component, "h " + (int)(f * height) + "!");
                item.setAnimate(f);
                component.revalidate();
                item.repaint();
            }
        });
        animator.setResolution(0);
        animator.setAcceleration(0.5f);
        animator.setDeceleration(0.5f);
        animator.start();

    }
    public static void showMenu(Component component, MigLayout layout, boolean show){
        int height = component.getPreferredSize().height;
        Animator animator = new Animator(300, new TimingTargetAdapter(){
            @Override
            public void timingEvent(float fraction) {
                float f = show ? fraction : 1 - fraction;
                layout.setComponentConstraints(component, "h " + (int)(f * height) + "!");
                component.revalidate();
                component.repaint();
            }
        });
        animator.setResolution(0);
        animator.setAcceleration(0.5f);
        animator.setDeceleration(0.5f);
        animator.start();

    }

}
