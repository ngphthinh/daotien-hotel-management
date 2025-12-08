package iuh.fit.se.group1.ui.layout;

import iuh.fit.se.group1.entity.Employee;
import iuh.fit.se.group1.entity.EmployeeShift;
import iuh.fit.se.group1.service.EmployeeShiftService;
import iuh.fit.se.group1.service.ShiftCloseService;
import iuh.fit.se.group1.ui.component.custom.Button;
import iuh.fit.se.group1.ui.component.custom.message.Message;
import iuh.fit.se.group1.ui.component.menu.*;
import iuh.fit.se.group1.ui.component.paymentv2.PaymentPagev2;
import iuh.fit.se.group1.ui.component.version.CheckForVersionPanel;

import iuh.fit.se.group1.util.Constants;
import raven.glasspanepopup.GlassPanePopup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private SurchargeManagement surchargeManagement;
    private CloseShift closeShift;
    private Runnable logoutCallback;

    private float alpha = 1f;
    private SideBar sideBar;
    private Employee currentEmployee;

    public MainLayout() {
        init();
        setOpaque(false);
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
                        setMainContent(new PaymentPagev2());
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
                        setMainContent(surchargeManagement);
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
                    } else if (index == 12){ 
                            setMainContent(new RoomTransferUI());
                    }else {
                        System.out.println("Selected Menu Item: " + index + ", SubItem: " + subIndex + " from MenuIcon");
                    }
                } else {
                    if (index == 0) {
                        setMainContent(dashboardEmployee);
                    } else if (index == 1) {
                        setMainContent(bookingPage);
                    } else if (index == 2) {
                        paymentPage.clearForm();
                        setMainContent(new PaymentPagev2());

                    } else if (index == 3) {
                        if (currentEmployee == null) {
                            Message.showMessage("Lỗi", "Không tìm thấy thông tin nhân viên!");
                            return;
                        }
                        EmployeeShiftService employeeShiftService = new EmployeeShiftService();
                        ShiftCloseService shiftCloseService = new ShiftCloseService();

                        //  THỜI GIAN DƯ SAU KHI KẾT THÚC CA (10 phút)
                        final int BUFFER_MINUTES = 10;

                        // LẤY TẤT CẢ CA TRONG NGÀY HÔM NAY
                        List<EmployeeShift> todayShifts = employeeShiftService.getShiftsByEmployeeAndDate(
                                currentEmployee.getEmployeeId(),
                                LocalDate.now()
                        );

                        if (todayShifts.isEmpty()) {
                            Message.showMessage("Thông báo",
                                    "Bạn chưa có ca làm việc nào trong ngày hôm nay!");
                            return;
                        }

                        //  LỌC RA CÁC CA CHƯA ĐÓNG
                        List<EmployeeShift> openShifts = todayShifts.stream()
                                .filter(shift -> shiftCloseService.getShiftCloseByEmployeeShift(shift).isEmpty())
                                .sorted((s1, s2) -> {
                                    // Parse string time để sắp xếp
                                    java.time.LocalTime t1 = java.time.LocalTime.parse(s1.getShift().getStartTime());
                                    java.time.LocalTime t2 = java.time.LocalTime.parse(s2.getShift().getStartTime());
                                    return t1.compareTo(t2);
                                })
                                .collect(java.util.stream.Collectors.toList());

                        if (openShifts.isEmpty()) {
                            Message.showMessage("Thông báo",
                                    "Tất cả ca làm việc hôm nay đã được đóng!");
                            return;
                        }

                        java.time.LocalTime currentTime = java.time.LocalTime.now();

                        // TÌM CA CÓ THỂ ĐÓNG (ĐANG MỞ HOẶC VỪA KẾT THÚC TRONG VÒNG 10 PHÚT)
                        EmployeeShift shiftToClose = null;

                        for (EmployeeShift shift : openShifts) {
                            try {
                                // PARSE STRING SANG LOCALTIME
                                java.time.LocalTime startTime = java.time.LocalTime.parse(shift.getShift().getStartTime());
                                java.time.LocalTime endTime = java.time.LocalTime.parse(shift.getShift().getEndTime());
                                java.time.LocalTime endTimeWithBuffer = endTime.plusMinutes(BUFFER_MINUTES);

                                // KIỂM TRA XEM CA NÀY CÓ ĐANG TRONG THỜI GIAN CHO PHÉP ĐÓNG KHÔNG
                                boolean isAfterStart = currentTime.isAfter(startTime) || currentTime.equals(startTime);
                                boolean isBeforeBufferEnd = currentTime.isBefore(endTimeWithBuffer) || currentTime.equals(endTimeWithBuffer);

                                if (isAfterStart && isBeforeBufferEnd) {
                                    shiftToClose = shift;
                                    break;
                                }
                            } catch (Exception e) {
                                System.err.println("Lỗi parse time cho ca: " + shift.getShift().getName() + " - " + e.getMessage());
                            }
                        }

                        if (shiftToClose != null) {
                            // Kiểm tra xem có ca cũ hơn chưa đóng không
                            EmployeeShift oldestOpenShift = openShifts.get(0);
                            boolean hasOlderUnclosedShift = !oldestOpenShift.getEmployeeShiftId()
                                    .equals(shiftToClose.getEmployeeShiftId());

                            if (hasOlderUnclosedShift) {
                                // CÓ CA CŨ HƠN CHƯA ĐÓNG → CẢNH BÁO
                                String oldShiftName = oldestOpenShift.getShift().getName();
                                String oldShiftTime = oldestOpenShift.getShift().getStartTime() + " - " +
                                        oldestOpenShift.getShift().getEndTime();
                                String currentShiftName = shiftToClose.getShift().getName();
                                String currentShiftTime = shiftToClose.getShift().getStartTime() + " - " +
                                        shiftToClose.getShift().getEndTime();

                                int choice = JOptionPane.showOptionDialog(
                                        null,
                                        "<html><b style='color:red;'>CẢNH BÁO: Bạn có ca cũ chưa đóng!</b><br><br>" +
                                                "Ca chưa đóng: <b>" + oldShiftName + "</b> (" + oldShiftTime + ")<br>" +
                                                "Ca hiện tại: <b>" + currentShiftName + "</b> (" + currentShiftTime + ")<br><br>" +
                                                "Bạn muốn đóng ca nào?</html>",
                                        "Cảnh báo",
                                        JOptionPane.YES_NO_CANCEL_OPTION,
                                        JOptionPane.WARNING_MESSAGE,
                                        null,
                                        new Object[]{"Đóng ca cũ", "Đóng ca hiện tại", "Hủy"},
                                        "Đóng ca cũ"
                                );

                                if (choice == JOptionPane.YES_OPTION) {
                                    openCloseShiftPanel(oldestOpenShift);
                                } else if (choice == JOptionPane.NO_OPTION) {
                                    openCloseShiftPanel(shiftToClose);
                                }
                                // Cancel → không làm gì

                            } else {
                                // KHÔNG CÓ CA CŨ CHƯA ĐÓNG → ĐÓNG CA HIỆN TẠI
                                try {
                                    java.time.LocalTime endTime = java.time.LocalTime.parse(shiftToClose.getShift().getEndTime());
                                    boolean isInBufferTime = currentTime.isAfter(endTime);

                                    if (isInBufferTime) {
                                        long minutesRemaining = java.time.Duration.between(currentTime,
                                                endTime.plusMinutes(BUFFER_MINUTES)).toMinutes();
                                        System.out.println("Đang trong thời gian buffer. Còn " + minutesRemaining + " phút để đóng ca.");
                                    }
                                } catch (Exception e) {
                                    System.err.println("Lỗi tính buffer time: " + e.getMessage());
                                }

                                openCloseShiftPanel(shiftToClose);
                            }

                        } else {

                            // Kiểm tra xem có ca cũ quá hạn chưa đóng không
                            EmployeeShift oldestOpenShift = openShifts.get(0);

                            try {
                                java.time.LocalTime oldestEndTime = java.time.LocalTime.parse(
                                        oldestOpenShift.getShift().getEndTime()
                                );
                                java.time.LocalTime oldestEndTimeWithBuffer = oldestEndTime.plusMinutes(BUFFER_MINUTES);

                                if (currentTime.isAfter(oldestEndTimeWithBuffer)) {
                                    //  CÓ CA QUÁ HẠN BUFFER TIME
                                    String oldShiftName = oldestOpenShift.getShift().getName();
                                    String oldShiftTime = oldestOpenShift.getShift().getStartTime() + " - " +
                                            oldestOpenShift.getShift().getEndTime();

                                    Message.showMessage("Cảnh báo",
                                            "<html><b style='color:red;'>Ca làm việc đã quá hạn đóng ca!</b><br><br>" +
                                                    "Ca: <b>" + oldShiftName + "</b> (" + oldShiftTime + ")<br>" +
                                                    "Thời gian cho phép đóng ca: đến " + oldestEndTimeWithBuffer.toString() + "<br><br>" +
                                                    "Vui lòng liên hệ quản lý để xử lý.</html>");
                                } else {
                                    //  CHƯA ĐẾN GIỜ LÀM CA
                                    EmployeeShift nextShift = openShifts.get(0);
                                    String nextShiftName = nextShift.getShift().getName();
                                    String nextShiftTime = nextShift.getShift().getStartTime() + " - " +
                                            nextShift.getShift().getEndTime();

                                    Message.showMessage("Thông báo",
                                            "<html>Chưa có ca làm việc nào có thể đóng!<br><br>" +
                                                    "Ca tiếp theo: <b>" + nextShiftName + "</b> (" + nextShiftTime + ")<br>" +
                                                    "Vui lòng quay lại trong giờ làm việc.</html>");
                                }
                            } catch (Exception e) {
                                System.err.println("Lỗi parse time: " + e.getMessage());
                                Message.showMessage("Lỗi", "Không thể xử lý thời gian ca làm việc!");
                            }
                        }
                    }
                    else {
                        System.out.println("hihi");
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
        this.isAdmin = isAdmin;
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
            surchargeManagement = new SurchargeManagement();
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
