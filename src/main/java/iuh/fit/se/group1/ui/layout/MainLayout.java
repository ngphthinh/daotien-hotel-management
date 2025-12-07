package iuh.fit.se.group1.ui.layout;

import iuh.fit.se.group1.entity.Employee;
import iuh.fit.se.group1.entity.EmployeeShift;
import iuh.fit.se.group1.service.EmployeeShiftService;
import iuh.fit.se.group1.service.ShiftCloseService;
import iuh.fit.se.group1.ui.component.booking2.BookingPage;
import iuh.fit.se.group1.ui.component.custom.Button;
import iuh.fit.se.group1.ui.component.custom.message.Message;
import iuh.fit.se.group1.ui.component.menu.*;
import iuh.fit.se.group1.ui.component.version.CheckForVersionPanel;

import iuh.fit.se.group1.util.Constants;
import raven.glasspanepopup.GlassPanePopup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

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
    private RevenueStatistics revenueStatistics;
    private SurchageManagement surchageManagement;
    private CloseShift closeShift;
    private Runnable logoutCallback;

    private float alpha = 1f;
    private SideBar sideBar;
    private Employee currentEmployee;

    public MainLayout() {
        init();
        setOpaque(false);
        isAdmin = true;
        setAuth(isAdmin);
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

    public void setLogoutCallback(Runnable callback) {
        this.logoutCallback = callback;
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
        closeShift = new CloseShift();
        sideBar.getMenu1().setMenuEvent(new MenuEvent() {
            @Override
            public void selected(int index, int subIndex) {
                if (isAdmin) {
                    if (index == 0) {
                        setMainContent(dashboard);
                    } else if (index == 1) {
                        setMainContent(bookingPage);
                    } else if (index == 2) {
                        paymentPage.clearForm();
                        paymentPage.loadData();
                        setMainContent(paymentPage);
                    } else if (index == 3 && subIndex == 1) {
                        shiftManagement.getShiftList().reloadEmployees();
                        setMainContent(shiftManagement);
                    } else if (index == 3 && subIndex == 2) {
                        setMainContent(employeeManagement);
                    } else if (index == 4) {
                        customerManagement.loadData();
                        setMainContent(customerManagement);
                    } else if (index == 5) {
                        setMainContent(amenityManagement);
                    } else if (index == 6) {
                        setMainContent(promotionManagement);
                    } else if (index == 7) {
                        setMainContent(roomManagement);
                        roomManagement.loadData();
                    } else if (index == 8) {
                        orderManagement.loadData();
                        setMainContent(orderManagement);
                    } else if (index == 9) {
                        setMainContent(surchageManagement);
                    } else if (index == 10 && subIndex == 1) {
                        setMainContent(revenueStatistics);
                    } else if (index == 10 && subIndex == 2) {
                        setMainContent(new BookingTrend());
                    } else if (index == 10 && subIndex == 3) {
                        setMainContent(new StatisticsDetail());
                    } else if (index == 11 && subIndex == 2) {
                        setMainContent(new Regulation());
                    } else if (index == 11 && subIndex == 3) {
                        var modal = new CheckForVersionPanel();
                        GlassPanePopup.showPopup(modal);
                        modal.getBtnClose().addActionListener(e
                                -> GlassPanePopup.closePopupLast()
                        );
                    } else if (index == 11 && subIndex == 4) {
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
                        paymentPage.clearForm();
                        setMainContent(paymentPage);

                    } else if (index == 3) {
                        if (currentEmployee == null) {
                            Message.showMessage("Lỗi", "Không tìm thấy thông tin nhân viên!");
                            return;
                        }

                        EmployeeShiftService employeeShiftService = new EmployeeShiftService();
                        ShiftCloseService shiftCloseService = new ShiftCloseService();
                        List<EmployeeShift> todayShifts = employeeShiftService.getShiftsByEmployeeAndDate(
                                currentEmployee.getEmployeeId(),
                                LocalDate.now()
                        );

                        EmployeeShift openShift = todayShifts.stream()
                                .filter(shift -> shiftCloseService.getShiftCloseByEmployeeShift(shift).isEmpty())
                                .findFirst()
                                .orElse(null);

                        if (openShift == null) {
                            Message.showMessage("Thông báo",
                                    "Bạn chưa mở ca làm việc hôm nay hoặc tất cả ca đã đóng!");
                            return;
                        }
                        if (!employeeShiftService.isShiftActive(openShift)) {
                            String shiftTime = openShift.getShift().getStartTime() + " - " +
                                    openShift.getShift().getEndTime();
                            Message.showMessage("Cảnh báo",
                                    "Ca làm việc (" + shiftTime + ") chưa bắt đầu hoặc đã kết thúc!\n");
                        }
                        openCloseShiftPanel(openShift);
                    } else {
                        System.out.println("Selected Menu Item: " + index + ", SubItem: " + subIndex + " from MenuIcon");
                    }
                }
            }
        });


        sideBar.getLblAvt().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Profile profilePage = new Profile();
                if (currentEmployee != null) {
                    profilePage.setEmployeeInfo(currentEmployee);
                }
                setMainContent(profilePage);
            }

        });

    }

    private void openCloseShiftPanel(EmployeeShift openShift) {
        CloseShift closeShiftPanel = new CloseShift();
        closeShiftPanel.setCurrentEmployeeShift(openShift);

        // Set callback để đăng xuất khi đóng ca thành công
        closeShiftPanel.setOnCloseShiftSuccess(() -> {
            if (logoutCallback != null) {
                logoutCallback.run();
            }
        });

        setMainContent(closeShiftPanel);
    }

    public void setMainContent(JPanel panel) {
        pnlContent.removeAll();
        pnlContent.add(panel, BorderLayout.CENTER);
        pnlContent.revalidate();
        pnlContent.repaint();
    }

    public JButton getBtnSignOut() {
        return sideBar.getBtnSignOut();
    }

    public Button getBtnClose() {
        return closeShift.getBtnClose();
    }

    public void saveData() {
        closeShift.saveData();
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
        this.isAdmin = true;
        sideBar.getMenu1().setAuth(isAdmin);
        if (isAdmin) {
            dashboard = new Dashboard();
            bookingPage = new BookingPage();
            bookingPage.setCurrentEmployee(currentEmployee);
            paymentPage = new PaymentPage();
            paymentPage.setCurrentEmployee(currentEmployee);
            shiftManagement = new ShiftManagement();
            employeeManagement = new EmployeeManagement();
            customerManagement = new CustomerManagement();
            amenityManagement = new AmenityManagement();
            promotionManagement = new PromotionManagement();
            roomManagement = new RoomManagement();
            orderManagement = new OrderManagement();
            orderManagement.setParent(this);
            orderManagement.setPaymentPage(paymentPage);
            checkForVersionPanel = new CheckForVersionPanel();
            revenueStatistics = new RevenueStatistics();
            surchageManagement = new SurchageManagement();
            setMainContent(dashboard);
        } else {


            dashboardEmployee = new DashboardEmployee();
            bookingPage = new BookingPage();
            bookingPage.setCurrentEmployee(currentEmployee);
            paymentPage = new PaymentPage();
            paymentPage.setCurrentEmployee(currentEmployee);
            setMainContent(dashboardEmployee);
        }

    }

    public void setCurrentEmployee(Employee employee) {
        this.currentEmployee = employee;
        sideBar.getFooter1().setEmployeeInfo(employee);
    }
}
