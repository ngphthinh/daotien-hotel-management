
package iuh.fit.se.group1.ui.layout;


import iuh.fit.se.group1.ui.component.menu.*;
import iuh.fit.se.group1.ui.component.menu.MenuItem;
import iuh.fit.se.group1.util.Constants;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class MainLayout extends JFrame {


    public MainLayout() {
       init();

    }

    private void init() {
        setTitle("Main Layout");
        setSize(Constants.WIDTH_FRAME, Constants.HEIGHT_FRAME);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel pnlMain = new JPanel(new BorderLayout());
        pnlMain.setPreferredSize(new Dimension(Constants.WIDTH_FRAME, Constants.HEIGHT_FRAME));

        SideBar sideBar = new SideBar();
        pnlMain.add(sideBar, BorderLayout.WEST);

        MenuIcon menuIcon = new MenuIcon();

        menuIcon.setMenuEvent(new MenuEvent() {
            @Override
            public void selected(int index, int subIndex) {
                if (index == Constants.BARS) {
                    sideBar.setVisible(true);
                    pnlMain.remove(menuIcon);
                    pnlMain.add(sideBar, BorderLayout.WEST);
                    pnlMain.revalidate();
                    pnlMain.repaint();
                } else {
                    // Xử lý sự kiện khi một mục menu được chọn
                    System.out.println("Selected Menu Item: " + index + ", SubItem: " + subIndex + " from MenuIcon" );

                }
            }
        });

        JPanel homePage = new HomePage();
        pnlMain.add(homePage, BorderLayout.CENTER);

        add(pnlMain);

        sideBar.getCloseSideBar().addActionListener(e -> {
            sideBar.setVisible(false);
            pnlMain.remove(sideBar);
            pnlMain.add(menuIcon, BorderLayout.WEST);
        });



    }



    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainLayout.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainLayout.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainLayout.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainLayout.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainLayout().setVisible(true);
            }
        });
    }


}
