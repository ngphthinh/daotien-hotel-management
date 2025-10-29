package iuh.fit.se.group1.ui.layout;

import iuh.fit.se.group1.entity.Employee;
import iuh.fit.se.group1.ui.component.menu.*;
import iuh.fit.se.group1.ui.component.version.CheckForVersionPanel;

import iuh.fit.se.group1.util.Constants;
import raven.glasspanepopup.GlassPanePopup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

public class MainLayout extends JPanel {

    private JPanel pnlMain;
    private JPanel pnlContent;

    private boolean isAdmin;
    private ProfileButton profileButton;
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
    private RevenueStatistics revenueStatistics;

    private float alpha = 1f;
    private SideBar sideBar;
    private Employee currentEmployee;
    public MainLayout() {
        init();
        setOpaque(false);
        isAdmin = true;
        setAuth(isAdmin);
    }
    public void setEmployeeInfo(Employee employee) {
        this.currentEmployee = employee;
        if (employee != null && sideBar != null) {
            Footer footer = sideBar.getFooter1();
            if (footer != null) {
                footer.setEmployeeInfo(employee);
            }
        }
    }
    public Employee getCurrentEmployee() {
        return currentEmployee;
    }
    public void setAlpha(float alpha) {
        this.alpha = alpha;
        repaint();
    }

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
                    } else if (index == 9 && subIndex == 1) {
                        setMainContent(revenueStatistics);
                    } else if (index == 9 && subIndex ==2) {
                        setMainContent(new BookingTrend());
                    } else if (index == 9 && subIndex ==3) {
                        setMainContent(new StatisticsDetail());
                    } else if (index == 10 && subIndex == 2) {
                        setMainContent(new Regulation());
                    } else if (index == 10 && subIndex == 3) {
                        var modal = new CheckForVersionPanel();
                        GlassPanePopup.showPopup(modal);
                        modal.getBtnClose().addActionListener(e
                                -> GlassPanePopup.closePopupLast()
                        );
                    } else if (index == 10 && subIndex == 4) {
                        try {
                            File htmlFile = new File("src/main/resources/static/about.html");

                            if (htmlFile.exists()) {
                                Desktop.getDesktop().browse(htmlFile.toURI());
                            } else {
                                JOptionPane.showMessageDialog(null,
                                        "Không tìm thấy file: " + htmlFile.getAbsolutePath(),
                                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                            }

                        } catch (IOException e) {
                            JOptionPane.showMessageDialog(null,
                                    "Không thể mở trang giới thiệu trong trình duyệt!",
                                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
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
                    } else if (index == 3) {
                        setMainContent(new CloseShift());
                    } else {
                        System.out.println("Selected Menu Item: " + index + ", SubItem: " + subIndex + " from MenuIcon");
                    }
                }
            }
        });
        
          
        sideBar.getLblAvt().addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                Profile profilePage = new Profile();
                if (currentEmployee != null) {
                    profilePage.setEmployeeInfo(currentEmployee);
                }
                setMainContent(profilePage);
            }

            @Override
            public void mousePressed(MouseEvent e) {
             }

            @Override
            public void mouseReleased(MouseEvent e) {
             }

            @Override
            public void mouseEntered(MouseEvent e) {
                }

            @Override
            public void mouseExited(MouseEvent e) {
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
            revenueStatistics = new RevenueStatistics();
            setMainContent(dashboard);
        } else {
            dashboardEmployee = new DashboardEmployee();
            bookingPage = new BookingPage();
            paymentPage = new PaymentPage();
            setMainContent(dashboardEmployee);
        }

    }
}
