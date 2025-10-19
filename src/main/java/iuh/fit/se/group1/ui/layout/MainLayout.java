package iuh.fit.se.group1.ui.layout;

import iuh.fit.se.group1.ui.component.menu.*;
import iuh.fit.se.group1.ui.component.version.CheckForVersionPanel;

import iuh.fit.se.group1.util.Constants;
import raven.glasspanepopup.GlassPanePopup;

import javax.swing.*;
import java.awt.*;

public class MainLayout extends JPanel {

    private JPanel pnlMain;
    private JPanel pnlContent;

    private boolean isAdmin;

    private Dashboard dashboard;
    private DashboardEmployee dashboardEmployee;
    private BookingPage bookingPage;
    private PaymentPage paymentPage;
    private ShiftManagement shiftManagement;
    private EmployeeManagement employeeManagement;
    private CustomerManagement customerManagement;
    private AmenityManagement amenityManagement;
    private PromotionManagement promotionManagement;
    private RoomManagement roomManagement;
    private OrderManagement orderManagement;
    private CheckForVersionPanel checkForVersionPanel;


    public MainLayout() {
        init();
        setOpaque(false);
        isAdmin = true;
        setAuth(isAdmin);
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


        pnlContent = new JPanel(new BorderLayout());
        pnlMain.add(pnlContent, BorderLayout.CENTER);
        add(pnlMain, BorderLayout.CENTER);


        sideBar.getMenu1().setMenuEvent(new MenuEvent() {
            @Override
            public void selected(int index, int subIndex) {
                if (isAdmin) {
                    if (index == 0) {
                        setMainContent(dashboard);
                    } else if (index == 1) {
                        setMainContent(bookingPage);
                    } else if (index == 2) {
                        setMainContent(paymentPage);
                    } else if (index == 3 && subIndex == 1) {
                        setMainContent(shiftManagement);
                    } else if (index == 3 && subIndex == 2) {
                        setMainContent(employeeManagement);
                    } else if (index == 4) {
                        setMainContent(customerManagement);
                    } else if (index == 5) {
                        setMainContent(amenityManagement);
                    } else if (index == 6) {
                        setMainContent(promotionManagement);
                    } else if (index == 7) {
                        setMainContent(roomManagement);
                    } else if (index == 8) {
                        setMainContent(orderManagement);
                    } else if (index == 10 && subIndex == 3) {
                        var modal = new CheckForVersionPanel();
                        GlassPanePopup.showPopup(modal);
                        modal.getBtnClose().addActionListener(e ->
                                GlassPanePopup.closePopupLast()
                        );
                    } else {
                        System.out.println("Selected Menu Item: " + index + ", SubItem: " + subIndex + " from MenuIcon");
                    }
                } else {
                    if (index == 0) {
                        setMainContent(dashboardEmployee);
                    } else if (index == 1) {
                        setMainContent(bookingPage);
                    } else if (index == 2) {
                        setMainContent(paymentPage);
                    } else if (index == 3){
                        setMainContent(new CloseShift());
                    }else {
                        System.out.println("Selected Menu Item: " + index + ", SubItem: " + subIndex + " from MenuIcon");
                    }
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
                    GlassPanePopup.install(frame);
                    frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
                    frame.setLocationRelativeTo(null);
                    frame.setContentPane(new MainLayout());
                    frame.setVisible(true);
                }
        );
    }

    public void setAuth(boolean isAdmin) {
        this.isAdmin = isAdmin;
        sideBar.getMenu1().setAuth(isAdmin);
        if (isAdmin) {
            dashboard = new Dashboard();
            bookingPage = new BookingPage();
            paymentPage = new PaymentPage();
            shiftManagement = new ShiftManagement();
            employeeManagement = new EmployeeManagement();
            customerManagement = new CustomerManagement();
            amenityManagement = new AmenityManagement();
            promotionManagement = new PromotionManagement();
            roomManagement = new RoomManagement();
            orderManagement = new OrderManagement();
            checkForVersionPanel = new CheckForVersionPanel();
            setMainContent(dashboard);
        } else {
            dashboardEmployee = new DashboardEmployee();
            bookingPage = new BookingPage();
            paymentPage = new PaymentPage();
            setMainContent(dashboardEmployee);
        }

    }
}
