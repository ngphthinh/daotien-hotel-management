package iuh.fit.se.group1.ui.layout;

import iuh.fit.se.group1.ui.component.menu.*;
import iuh.fit.se.group1.ui.swing.Login;
import iuh.fit.se.group1.util.Constants;
import raven.glasspanepopup.GlassPanePopup;

import javax.swing.*;
import java.awt.*;

public class MainLayout extends JPanel {

    private JPanel pnlMain;
    private JPanel pnlContent;

    public MainLayout() {
        init();
        setOpaque(false);
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        repaint();
    }

    private float alpha = 1f;
    private SideBar sideBar;

    @Override
    public void paint(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        super.paint(grphcs);
    }

    private void init() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(Constants.WIDTH_FRAME, Constants.HEIGHT_FRAME));

        pnlMain = new JPanel(new BorderLayout());
        pnlMain.setPreferredSize(new Dimension(Constants.WIDTH_FRAME, Constants.HEIGHT_FRAME));

        sideBar = new SideBar();
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
                }
                if (index == 0) {
                    setMainContent(new HomePage());
                } else if (index == 1) {
                    setMainContent(new RoomManagement());
                } else if (index == 7 && subIndex == 3) {
                    setMainContent(new RoomOccupancyStatistics());
                } else if (index == 6) {
                    setMainContent(new AmenityManagement());
                } else {
                    System.out.println("Selected Menu Item: " + index + ", SubItem: " + subIndex + " from MenuIcon");
                }
            }
        });

        JPanel homePage = new HomePage();
        pnlContent = new JPanel(new BorderLayout());
        pnlMain.add(pnlContent, BorderLayout.CENTER);
        pnlContent.add(homePage);
        add(pnlMain, BorderLayout.CENTER);

        sideBar.getCloseSideBar().addActionListener(e -> {
            sideBar.setVisible(false);
            pnlMain.remove(sideBar);
            pnlMain.add(menuIcon, BorderLayout.WEST);
        });

        sideBar.getMenu1().setMenuEvent(new MenuEvent() {
            @Override
            public void selected(int index, int subIndex) {
                if (index == 0) {
                    setMainContent(homePage);
                } else if (index == 6) {
                    setMainContent(new RoomManagement());
                } else if (index == 7 && subIndex == 3) {
                    setMainContent(new RoomOccupancyStatistics());
                } else if (index == 4) {
                    setMainContent(new AmenityManagement());
                } else if (index == 2 && subIndex == 1) {
                    setMainContent(new ShiftManagement());
                } else if (index == 2 && subIndex == 2) {
                    setMainContent(new EmployeeManagement());
                } else {
                    System.out.println("Selected Menu Item: " + index + ", SubItem: " + subIndex + " from SideBar");
                }
            }
        });
    }

    private void setMainContent(JPanel panel) {
        pnlContent.removeAll();
        pnlContent.add(panel, BorderLayout.CENTER);
        pnlContent.revalidate();
        pnlContent.repaint();
    }

    public JButton getBtnSignOut() {
        return sideBar.getBtnSignOut();
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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
                    JFrame frame = new JFrame("Main Layout");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setSize(Constants.WIDTH_FRAME, Constants.HEIGHT_FRAME);
                    frame.setLocationRelativeTo(null);
                    frame.setContentPane(new MainLayout());
                    frame.setVisible(true);
                }
        );
    }
}
