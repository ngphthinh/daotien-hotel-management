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
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

public class MainLayout extends JPanel {

    private JPanel pnlMain;
    private JPanel pnlContent;

    private boolean isAdmin;
    private Dashboard dashboard;
    private DashboardEmployee dashboardEmployee;
    private BookingPage bookingPage;
    private PaymentPagev2 paymentPage;
    private ShiftManagement shiftManagement;
    private EmployeeManagement employeeManagement;
    private CustomerManagement customerManagement;
    private AmenityManagement amenityManagement;
    private PromotionManagement promotionManagement;
    private RoomManagement roomManagement;
    private RoomToolsManagement roomToolsManagement;
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
                        paymentPage.setOnPayment();
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
                        promotionManagement.loadData();
                        setMainContent(promotionManagement);
                    } else if (index == 7) {
                        setMainContent(roomManagement);
                        roomManagement.loadData();
                    } else if (index == 8) {
                        roomToolsManagement.loadData();
                        setMainContent(roomToolsManagement);
                    } else if (index == 9) {
                        setMainContent(orderManagement);
                        orderManagement.loadData();
                    } else if (index == 10) {
                        setMainContent(surchargeManagement);
                    } else if (index == 11 && subIndex == 1) {
                        setMainContent(new RevenueStatistics());
                    } else if (index == 11 && subIndex == 2) {
                        setMainContent(new BookingTrend());
                    } else if (index == 12 && subIndex == 1) {
                        handleGuidePanel();
                    } else if (index == 12 && subIndex == 2) {
                        setMainContent(new Regulation());
                    } else if (index == 12 && subIndex == 3) {
                        GlassPanePopup.showPopup(checkForVersionPanel);
                        checkForVersionPanel.getBtnClose().addActionListener(e
                                -> GlassPanePopup.closePopupLast()
                        );
                    } else if (index == 12 && subIndex == 4) {
                        handleAboutPanel();
                    } else {
                        System.out.println("Selected Menu Item: " + index + ", SubItem: " + subIndex + " from MenuIcon");
                    }
                } else {
                    if (index == 0) {
                        setMainContent(dashboardEmployee);
                    } else if (index == 1) {
                        setMainContent(bookingPage);
                    } else if (index == 2) {
                        paymentPage.setOnPayment();
                        setMainContent(paymentPage);
                    } else if (index == 3) {
                        roomToolsManagement.loadData();
                        setMainContent(roomToolsManagement);
                    }
                    if (index == 4) {
                        setMainContent(orderManagement);
                        orderManagement.loadData();
                    } else if (index == 5) {
                        closeShiftHandle();
                    } else if (index == 6) {
                        if (subIndex == 1) {
                            handleGuidePanel();
                        } else if (subIndex == 2) {
                            setMainContent(new Regulation());
                        } else if (subIndex == 3) {

                            GlassPanePopup.showPopup(checkForVersionPanel);
                            checkForVersionPanel.getBtnClose().addActionListener(e
                                    -> GlassPanePopup.closePopupLast()
                            );
                        } else if (subIndex == 4) {
                            handleAboutPanel();
                        }
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

    private void handleGuidePanel() {
        System.out.println("Guide Panel");
        try {
            Desktop.getDesktop().browse(new URI("https://vienthieu6925.github.io/user/"));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Không thể mở trang hướng dẫn!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void handleAboutPanel() {
        try {
            Desktop.getDesktop().browse(new URI("https://vienthieu6925.github.io/about/"));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Không thể mở trang giới thiệu!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void closeShiftHandle() {
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

        // TÌM CA CÓ THỂ ĐÓNG
        // CHỈ CHO PHÉP ĐÓNG CA ĐANG TRONG GIỜ LÀM VIỆC HOẶC BUFFER TIME
        EmployeeShift shiftToClose = null;
        EmployeeShift overdueShift = null;

        for (EmployeeShift shift : openShifts) {
            try {
                java.time.LocalTime startTime = java.time.LocalTime.parse(shift.getShift().getStartTime());
                java.time.LocalTime endTime = java.time.LocalTime.parse(shift.getShift().getEndTime());

                boolean isNightShift = endTime.isAfter(java.time.LocalTime.of(18, 0));
                boolean isAfterStart;
                boolean isBeforeBufferEnd;
                boolean isOverdue;

                if (isNightShift) {
                    isAfterStart = currentTime.isAfter(startTime) || currentTime.equals(startTime);
                    if (endTime.isAfter(java.time.LocalTime.of(23, 0))) {
                        // Ca kết thúc sau 23h -> có thể đóng đến 23:59:59
                        isBeforeBufferEnd = true; // Luôn cho phép trong ngày
                        isOverdue = false;
                    } else {
                        java.time.LocalTime endTimeWithBuffer = endTime.plusMinutes(BUFFER_MINUTES);
                        isBeforeBufferEnd = currentTime.isBefore(endTimeWithBuffer) || currentTime.equals(endTimeWithBuffer);
                        isOverdue = currentTime.isAfter(endTimeWithBuffer);
                    }
                } else {
                    // Ca ngày: logic bình thường
                    java.time.LocalTime endTimeWithBuffer = endTime.plusMinutes(BUFFER_MINUTES);
                    isAfterStart = currentTime.isAfter(startTime) || currentTime.equals(startTime);
                    isBeforeBufferEnd = currentTime.isBefore(endTimeWithBuffer) || currentTime.equals(endTimeWithBuffer);
                    isOverdue = currentTime.isAfter(endTimeWithBuffer);
                }

                // Debug log
                System.out.println("=== Kiểm tra ca: " + shift.getShift().getName() + " ===");
                System.out.println("Giờ bắt đầu: " + startTime);
                System.out.println("Giờ kết thúc: " + endTime);
                System.out.println("Giờ hiện tại: " + currentTime);
                System.out.println("Ca đêm: " + isNightShift);
                System.out.println("Sau giờ bắt đầu: " + isAfterStart);
                System.out.println("Trước giờ kết thúc + buffer: " + isBeforeBufferEnd);
                System.out.println("Quá hạn: " + isOverdue);

                // CHỈ LẤY CA ĐANG TRONG THỜI GIAN HỢP LỆ (không lấy ca quá hạn)
                if (isAfterStart && isBeforeBufferEnd && shiftToClose == null) {
                    shiftToClose = shift;
                    System.out.println(">>> Chọn ca để đóng: " + shift.getShift().getName());
                }

                // Lưu ca quá hạn để CẢNH BÁO (nhưng không cho đóng trực tiếp)
                if (isAfterStart && isOverdue && overdueShift == null) {
                    overdueShift = shift;
                    System.out.println(">>> Ca quá hạn: " + shift.getShift().getName());
                }
            } catch (Exception e) {
                System.err.println("Lỗi parse time cho ca: " + shift.getShift().getName() + " - " + e.getMessage());
            }
        }


        if (shiftToClose != null) {
            // Kiểm tra xem có ca quá hạn không
            if (overdueShift != null) {
                // CÓ CA QUÁ HẠN → HIỂN THỊ MODAL CHO PHÉP CHỌN CA ĐÓNG
                String overdueShiftName = overdueShift.getShift().getName();
                String overdueShiftTime = overdueShift.getShift().getStartTime() + " - " +
                        overdueShift.getShift().getEndTime();
                String currentShiftName = shiftToClose.getShift().getName();
                String currentShiftTime = shiftToClose.getShift().getStartTime() + " - " +
                        shiftToClose.getShift().getEndTime();

                int choice = JOptionPane.showOptionDialog(
                        null,
                        "<html><b style='color:red;'>CẢNH BÁO: Có ca làm việc chưa đóng!</b><br><br>" +
                                "Ca chưa đóng: <b>" + overdueShiftName + "</b> (" + overdueShiftTime + ")<br>" +
                                "Thời gian đóng ca đã hết từ: " +
                                java.time.LocalTime.parse(overdueShift.getShift().getEndTime()).plusMinutes(BUFFER_MINUTES) + "<br><br>" +
                                "Ca hiện tại có thể đóng: <b>" + currentShiftName + "</b> (" + currentShiftTime + ")<br><br>" +
                                "<b>Bạn muốn đóng ca nào?</b></html>",
                        "Cảnh báo",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        new Object[]{"Đóng ca cũ", "Đóng ca hiện tại", "Hủy"},
                        "Đóng ca cũ"
                );

                if (choice == JOptionPane.YES_OPTION) {
                    // Đóng ca cũ (ca quá hạn)
                    openCloseShiftPanel(overdueShift);
                } else if (choice == JOptionPane.NO_OPTION) {
                    // Đóng ca hiện tại
                    openCloseShiftPanel(shiftToClose);
                }
                // Cancel → không làm gì

            } else {
                // KHÔNG CÓ CA QUÁ HẠN → ĐÓNG CA HIỆN TẠI BÌNH THƯỜNG
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


    public void setAuth(boolean isAdmin) {
        this.isAdmin = isAdmin;
        sideBar.getMenu1().setAuth(isAdmin);
        if (isAdmin) {

            dashboard = new Dashboard();
            bookingPage = new BookingPage();
            bookingPage.setCurrentEmployee(currentEmployee);
            shiftManagement = new ShiftManagement();
            paymentPage = new PaymentPagev2();
            paymentPage.setCurrentEmployee(currentEmployee);
            employeeManagement = new EmployeeManagement();
            customerManagement = new CustomerManagement();
            amenityManagement = new AmenityManagement();
            promotionManagement = new PromotionManagement();
            roomManagement = new RoomManagement();
            roomToolsManagement = new RoomToolsManagement();
            orderManagement = new OrderManagement();
            checkForVersionPanel = new CheckForVersionPanel();
            revenueStatistics = new RevenueStatistics();
            surchargeManagement = new SurchargeManagement();
            setMainContent(dashboard);
        } else {
            dashboardEmployee = new DashboardEmployee();
            bookingPage = new BookingPage();
            bookingPage.setCurrentEmployee(currentEmployee);
            paymentPage = new PaymentPagev2();
            paymentPage.setCurrentEmployee(currentEmployee);
            roomToolsManagement = new RoomToolsManagement();
            orderManagement = new OrderManagement();
            bookingPage.setCurrentEmployee(currentEmployee);
            setMainContent(dashboardEmployee);
            checkForVersionPanel = new CheckForVersionPanel();
            revenueStatistics = new RevenueStatistics();
            surchargeManagement = new SurchargeManagement();
        }

    }

    public void setCurrentEmployee(Employee employee) {
        this.currentEmployee = employee;
        sideBar.getFooter1().setEmployeeInfo(employee);
    }
}
