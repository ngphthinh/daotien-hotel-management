/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.layout;

import iuh.fit.se.group1.dto.RoomDTO;
import iuh.fit.se.group1.dto.RoomSelection;
import iuh.fit.se.group1.entity.*;
import iuh.fit.se.group1.service.OrderService;
import iuh.fit.se.group1.service.RoomService;
import iuh.fit.se.group1.service.SurchargeDetailService;
import iuh.fit.se.group1.service.SurchargeService;
import iuh.fit.se.group1.ui.component.booking2.CalendarUI;
import iuh.fit.se.group1.ui.component.booking2.CalendarUI;
import iuh.fit.se.group1.ui.component.booking2.MainFlow2;
import iuh.fit.se.group1.ui.component.booking2.MainFlow2;
import iuh.fit.se.group1.ui.component.booking2.MainFlow3;
import iuh.fit.se.group1.ui.component.booking2.MainFlow3;
import iuh.fit.se.group1.ui.component.booking2.MainFlow4;
import iuh.fit.se.group1.ui.component.booking2.MainFlow4;
import iuh.fit.se.group1.ui.component.booking2.MainFlow5;
import iuh.fit.se.group1.ui.component.booking2.MainFlow5;
import iuh.fit.se.group1.ui.component.custom.message.CustomDialog;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author THIS PC
 */
public class BookingPage extends javax.swing.JPanel {

    private SurchargeDetailService  surchargeDetailService = new SurchargeDetailService();
    private List<RoomSelection> selectedRooms;
    private SurchargeService surchargeService = new SurchargeService();
    private Employee currentEmployee;
    private OrderService orderService = new OrderService();
    private RoomService roomService = new RoomService();
    private MainFlow3 mainFlow3;
    private MainFlow2 mainFlow2;
    private MainFlow4 mainFlow4;
    private MainFlow5 mainFlow5;




    private static final String[] bookingType = {"Theo giờ", "Theo ngày", "Qua đêm"};


    public void setCurrentEmployee(Employee employee) {
        this.currentEmployee = employee;
    }

    /**
     * Creates new form BookingPage
     */
    public BookingPage() {
        initComponents();

        mainFlow2 = new MainFlow2();
        mainFlow3 = new MainFlow3();
        mainFlow4 = new MainFlow4();
        mainFlow5 = new MainFlow5();
        header.getLblTile().setText("Đặt phòng khách sạn");
        header.getLblSubTitle().setText("");


        mainFlow1.getCbmBookingType().addActionListener(e -> {
                    mainFlow1.setVisiableTimeBooking(Objects.equals(mainFlow1.getCbmBookingType().getSelectedItem(), bookingType[0]));
                    mainFlow1.resetInputDate();
                }
        );

        sequenceBooking.setActiveStep(0);
        mainFlow1.getBtnNext().addActionListener(e -> {
            if (!validateInputStep1()) {
                return;
            }
            if (!findRoom()) {
                return;
            }
            sequenceBooking.setActiveStep(1);
            scrollPaneWin111.setViewportView(mainFlow2);
        });

        mainFlow2.getBtnPrev().addActionListener(e -> {
            sequenceBooking.setActiveStep(0);
            scrollPaneWin111.setViewportView(mainFlow1);
        });

        mainFlow2.getBtnNext().addActionListener(e -> {
            sequenceBooking.setActiveStep(2);
            scrollPaneWin111.setViewportView(mainFlow3);
        });


        mainFlow3.getBtnPrev().addActionListener(e -> {
            sequenceBooking.setActiveStep(1);
            scrollPaneWin111.setViewportView(mainFlow2);
        });

        mainFlow3.getBtnNext().addActionListener(e -> {
            sequenceBooking.setActiveStep(3);
            scrollPaneWin111.setViewportView(mainFlow4);
        });

        mainFlow4.getBtnPrev().addActionListener(e -> {
            sequenceBooking.setActiveStep(2);
            scrollPaneWin111.setViewportView(mainFlow3);
        });

        mainFlow4.getBtnNext().addActionListener(e -> {
            if (!mainFlow4.validateInput()) {
                return;
            }else
            if (!mainFlow4.setupDob()){
                return;
            }
            setupInfoStep5();
            sequenceBooking.setActiveStep(4);
            scrollPaneWin111.setViewportView(mainFlow5);

        });

        mainFlow4.getTxtDob().addActionListener(l->{

            if (!mainFlow4.validateInput()) {
                return;
            }else

            if (!mainFlow4.setupDob()){
                return;
            }

            setupInfoStep5();
            sequenceBooking.setActiveStep(4);
            scrollPaneWin111.setViewportView(mainFlow5);
        });

        mainFlow5.getBtnPrev().addActionListener(e -> {
            sequenceBooking.setActiveStep(3);
            scrollPaneWin111.setViewportView(mainFlow4);
        });

        mainFlow5.getBtnComplete().addActionListener(e -> {
            createOrder();
        });


        mainFlow1.getTxtCheckInDate().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (Objects.equals(mainFlow1.getCbmBookingType().getSelectedItem(), bookingType[2])) {
                    CalendarUI.showCalendar(mainFlow1.getTxtCheckInDate(), null, overNight, false);
                } else if (Objects.equals(mainFlow1.getCbmBookingType().getSelectedItem(), bookingType[1])) {
                    CalendarUI.showCalendar(mainFlow1.getTxtCheckInDate(), null, dailyCheckIn, false);
                } else if (Objects.equals(mainFlow1.getCbmBookingType().getSelectedItem(), bookingType[0])) {
                    CalendarUI.showCalendar(mainFlow1.getTxtCheckInDate(), null, hourly, true);
                }
            }
        });

        mainFlow1.getTxtCheckOutDate().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (Objects.equals(mainFlow1.getCbmBookingType().getSelectedItem(), bookingType[1])) {
                    CalendarUI.showCalendar(mainFlow1.getTxtCheckOutDate(), null, dailyCheckOut, false);
                }
            }
        });

        mainFlow1.getCbmTime().addActionListener(e -> {
            if (Objects.equals(mainFlow1.getCbmBookingType().getSelectedItem(), bookingType[0])) {
                handleHourly("");
            }
        });
    }

    private void createOrder() {

        var orderRs = mainFlow5.buildOrder(currentEmployee, mainFlow4.getCustomer(), selectedRooms, surchargeService);

        Order order = orderRs.getOrder();

        List<OrderDetail> orderDetails = mainFlow3.getSelectedAmenities().stream().map(e -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setAmenity(new Amenity(e.getId()));
            orderDetail.setQuantity(e.getQuantity());
            orderDetail.setUnitPrice(BigDecimal.valueOf(e.getPrice()));
            return orderDetail;
        }).toList();

        var orderSave = orderService.createOrder(order, orderDetails);

        SurchargeDetail surchargeDetail = orderRs.getSurchargeDetail();

        if (orderSave != null) {
            if (surchargeDetail != null) {
                surchargeDetailService.save(surchargeDetail,orderSave.getOrderId());
            }
            CustomDialog.showMessage(
                    null,
                    "Tạo đơn đặt phòng thành công!",
                    "Thành công",
                    CustomDialog.MessageType.SUCCESS,
                    400, 200
            );
            // Reset lại quy trình đặt phòng
            sequenceBooking.setActiveStep(0);
            resetAllInput();
            scrollPaneWin111.setViewportView(mainFlow1);
        } else {
            CustomDialog.showMessage(
                    null,
                    "Tạo đơn đặt phòng thất bại. Vui lòng thử lại.",
                    "Lỗi",
                    CustomDialog.MessageType.ERROR,
                    400, 200
            );
        }

    }

    private void resetAllInput() {
//         reset 23
        mainFlow1.resetInputDate();
        mainFlow2.reset();
        mainFlow3.reset();
        mainFlow4.reset();
        mainFlow5.reset();
//        mainFlow5.resetAllInput();
    }

    private void setupInfoStep5() {
        mainFlow5.setupCustomer(mainFlow4.getCustomer());
        mainFlow5.setAmenity(mainFlow3.getSelectedAmenities());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // Parse ngày
        LocalDateTime checkIn = LocalDateTime.parse(mainFlow1.getTxtCheckInDate().getText(), formatter);
        LocalDateTime checkOut = LocalDateTime.parse(mainFlow1.getTxtCheckOutDate().getText(), formatter);


        selectedRooms = mainFlow2.getSelectedRoom(roomService.getAvailableRooms(checkIn, checkOut));

        mainFlow5.setInfoBooking(
                mainFlow1.getTxtCheckInDate().getText(),
                mainFlow1.getTxtCheckOutDate().getText(),
                mainFlow1.getTxtNumberOfAdult().getText(),
                mainFlow1.getTxtNumberOfChildren().getText(),
                String.valueOf(mainFlow1.getCbmBookingType().getSelectedItem()),
                selectedRooms,
                mainFlow2.getLblTotalRoom().getText(),
                isHoliday(checkIn.toLocalDate(), checkOut.toLocalDate())
        );

    }

    public boolean isHoliday(LocalDate startDate, LocalDate endDate) {
        Set<String> FIXED_HOLIDAYS = Set.of(
                "01-01", // Tết Dương lịch
                "30-04", // Giải phóng miền Nam
                "01-05", // Quốc tế Lao động
                "02-09",  // Quốc khánh
                "25-12",  // Giáng sinh
                "24-12",   // Giáng sinh Eve
                "31-12",    // Năm mới Eve
                "14-02",    // Valentine
                "08-03",    // Quốc tế Phụ nữ
                "20-10",     // Ngày Phụ nữ Việt Nam
                "20-11",      // Ngày Nhà giáo Việt Nam
                "01-06"     // Ngày Quốc tế Thiếu nhi
        );

        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            // Kiểm tra ngày dương lịch
            String key = String.format("%02d-%02d", date.getDayOfMonth(), date.getMonthValue());
            if (FIXED_HOLIDAYS.contains(key)) return true;

            // Kiểm tra ngày âm lịch
//            ChineseDate lunar = new Chin  eseDate(java.sql.Date.valueOf(date));
//            int lunarDay = lunar.getDay();
//            int lunarMonth = lunar.getMonth();
//
//            // Giỗ Tổ (10/3 AL)
//            if (lunarMonth == 3 && lunarDay == 10) return true;
//
//            // Tết Nguyên Đán (29,30 tháng Chạp + 1–5 tháng Giêng)
//            if ((lunarMonth == 12 && (lunarDay == 29 || lunarDay == 30)) ||
//                    (lunarMonth == 1 && lunarDay <= 5)) return true;

            date = date.plusDays(1);
        }
        return false;
    }


    private boolean findRoom() {
        String checkInStr = mainFlow1.getTxtCheckInDate().getText().trim();
        String checkOutStr = mainFlow1.getTxtCheckOutDate().getText().trim();
        String adultStr = mainFlow1.getTxtNumberOfAdult().getText().trim();
        String childrenStr = mainFlow1.getTxtNumberOfChildren().getText().trim();



        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // Parse ngày
        LocalDateTime checkIn = LocalDateTime.parse(checkInStr, formatter);
        LocalDateTime checkOut = LocalDateTime.parse(checkOutStr, formatter);


        String timePlus = "0";
        if (Objects.equals(mainFlow1.getCbmBookingType().getSelectedItem(), bookingType[0])) {
            timePlus = Objects.requireNonNull(mainFlow1.getCbmTime().getSelectedItem()).toString().split(" ")[0];
        }else if (Objects.equals(mainFlow1.getCbmBookingType().getSelectedItem(), bookingType[1]) ){
            long daysBetween = ChronoUnit.DAYS.between(checkIn, checkOut) + 1;
            timePlus = String.valueOf(daysBetween);
        }

        int adults = Integer.parseInt(adultStr);
        int children = 0;
        if (!childrenStr.isEmpty()) {
            children = Integer.parseInt(childrenStr);
        }

        // Tìm phòng trống
        var availableRooms = roomService.countAvailableRooms(checkIn, checkOut);
//        // Cập nhật danh sách phòng trống lên mainFlow2
      return  mainFlow2.updateRoomList(
                availableRooms,
                adults,
                children,
                roomService,
                mainFlow1.getCbmBookingType().getSelectedIndex(),
                Integer.parseInt(timePlus)
        );

    }

    private boolean validateInputStep1() {
        try {
            String checkInStr = mainFlow1.getTxtCheckInDate().getText().trim();
            String checkOutStr = mainFlow1.getTxtCheckOutDate().getText().trim();
            String adultStr = mainFlow1.getTxtNumberOfAdult().getText().trim();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            // Parse ngày
            LocalDateTime checkIn = LocalDateTime.parse(checkInStr, formatter);
            LocalDateTime checkOut = LocalDateTime.parse(checkOutStr, formatter);
            LocalDateTime now = LocalDateTime.now();

            // ─────────────────────────────────────────────
            //            VALIDATION
            // ─────────────────────────────────────────────

            // 1. check-in phải >= hiện tại
            if (checkIn.toLocalDate().isBefore(now.toLocalDate())) {
                CustomDialog.showMessage(
                        null,
                        "Ngày nhận phòng phải lớn hơn hoặc bằng ngày hiện tại.",
                        "Lỗi nhập liệu",
                        CustomDialog.MessageType.ERROR,
                        500, 200
                );
                return false;
            }


            // 2. check-out phải > check-in
            if (!checkOut.isAfter(checkIn)) {
                CustomDialog.showMessage(
                        null,
                        "Ngày trả phòng phải sau ngày nhận phòng.",
                        "Lỗi nhập liệu",
                        CustomDialog.MessageType.ERROR,
                        400, 200
                );
                return false;
            }

            // 3. số người lớn phải > 0
            int adults;
            try {
                adults = Integer.parseInt(adultStr);
                if (adults <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                CustomDialog.showMessage(
                        null,
                        "Số người lớn phải là số nguyên dương.",
                        "Lỗi nhập liệu",
                        CustomDialog.MessageType.ERROR,
                        400, 200
                );
                return false;
            }

        } catch (Exception e) {
            CustomDialog.showMessage(
                    null,
                    "Ngày tháng không hợp lệ. Vui lòng kiểm tra lại.",
                    "Lỗi nhập liệu",
                    CustomDialog.MessageType.ERROR,
                    400, 200
            );
            return false;
        }

        return true;
    }


    private final Consumer<String> overNight = this::handleOverNight;
    private final Consumer<String> dailyCheckIn = this::handleDailyCheckIn;
    private final Consumer<String> dailyCheckOut = this::handleDailyCheckOut;
    private final Consumer<String> hourly = this::handleHourly;

    private void handleHourly(String s) {
        String checkInDateFormCalendar = mainFlow1.getTxtCheckInDate().getText();
        String[] parts = checkInDateFormCalendar.split(" ");

        String checkInDate = parts[0];
        String[] timeParts = parts[1].split(":");

        int checkInHour = Integer.parseInt(timeParts[0]);
        int checkInMinute = Integer.parseInt(timeParts[1]);

        // Số giờ thuê
        int timePlus = Integer.parseInt(mainFlow1.getCbmTime().getSelectedItem().toString().split(" ")[0]);

        // Tính giờ check-out
        int outHour = checkInHour + timePlus;

        // Tăng ngày nếu vượt 24h
        int addDay = 0;
        if (outHour >= 24) {
            addDay = outHour / 24;
            outHour = outHour % 24;
        }

        // Convert ngày check-in từ dd/MM/yyyy sang LocalDate
        LocalDate date = LocalDate.parse(checkInDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // Cộng ngày
        LocalDate outDate = date.plusDays(addDay);

        // Format lại kết quả cuối
        String finalOutDate = outDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                + " " + String.format("%02d:%02d", outHour, checkInMinute);

        // Set vào UI
        mainFlow1.getTxtCheckOutDate().setText(finalOutDate);
    }


    private void handleDailyCheckOut(String date) {
        String checkOutDateFormCalender = mainFlow1.getTxtCheckOutDate().getText();

        String hour = " 12:00";
        // set giờ nhận phòng là 12:00
        // dd/mm/yyyy hh:mm
        String checkOutDate = checkOutDateFormCalender.split(" ")[0].concat(hour);
        mainFlow1.getTxtCheckOutDate().setText(checkOutDate);
    }

    private void handleDailyCheckIn(String date) {
        String checkInDateFormCalender = mainFlow1.getTxtCheckInDate().getText();

        String hour = " 14:00";
        // set giờ nhận phòng là 14:00
        // dd/mm/yyyy hh:mm
        String checkInDate = checkInDateFormCalender.split(" ")[0].concat(hour);
        mainFlow1.getTxtCheckInDate().setText(checkInDate);
    }

    private void handleOverNight(String date) {
        String checkInDateFormCalender = mainFlow1.getTxtCheckInDate().getText();

        String checkInDate = checkInDateFormCalender.split(" ")[0].concat(" 20:00");
        // dd/mm/yyyy hh:mm -> dd/mm/yyyy

        // ngày checkout sau ngày checkin 1 ngày
        LocalDate checkOut = LocalDate.parse(checkInDateFormCalender.split(" ")[0], DateTimeFormatter.ofPattern("dd/MM/yyyy")).plusDays(1);

        String checkOutDate = checkOut.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).concat(" 10:00");
        mainFlow1.getTxtCheckInDate().setText(checkInDate);
        mainFlow1.getTxtCheckOutDate().setText(checkOutDate);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        header = new iuh.fit.se.group1.ui.component.HeaderShift();
        sequenceBooking = new iuh.fit.se.group1.ui.component.booking2.SequenceBooking();
        scrollPaneWin111 = new iuh.fit.se.group1.ui.component.scroll.ScrollPaneWin11();
        mainFlow1 = new iuh.fit.se.group1.ui.component.booking2.MainFlow1();

        setBackground(new java.awt.Color(241, 241, 241));
        setForeground(new java.awt.Color(241, 241, 241));

        scrollPaneWin111.setViewportView(mainFlow1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(header, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(sequenceBooking, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scrollPaneWin111, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sequenceBooking, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(scrollPaneWin111, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private iuh.fit.se.group1.ui.component.HeaderShift header;
    private iuh.fit.se.group1.ui.component.booking2.MainFlow1 mainFlow1;
    private iuh.fit.se.group1.ui.component.scroll.ScrollPaneWin11 scrollPaneWin111;
    private iuh.fit.se.group1.ui.component.booking2.SequenceBooking sequenceBooking;
    // End of variables declaration//GEN-END:variables


    public void loadData() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadData'");
    }
}
