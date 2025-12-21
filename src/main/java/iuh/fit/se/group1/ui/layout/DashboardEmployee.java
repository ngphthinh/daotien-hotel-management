/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.layout;

import iuh.fit.se.group1.dto.DashboardSummaryDto;
import iuh.fit.se.group1.dto.PeakHourDto;
import iuh.fit.se.group1.dto.RevenueSourceDto;
import iuh.fit.se.group1.dto.RoomStatusDto;
import iuh.fit.se.group1.dto.ShiftNoteDto;
import iuh.fit.se.group1.dto.WarningDto;
import iuh.fit.se.group1.enums.TimeType;
import iuh.fit.se.group1.service.DashboardService;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

/**
 * Dashboard nhân viên với dữ liệu thật từ database
 * @author THIS PC
 */
public class DashboardEmployee extends javax.swing.JPanel {

    private static final Color ALMOST_OUT_OF_TIME = new Color(50, 145, 255);
    private static final Color NUMBER_CHECKIN = new Color(255, 108, 3);
    private static final Color NUMBER_CHECKOUT = new Color(239, 180, 46);
    private static final Color MONEY_OPEN_SHIFT = new Color(13, 200, 7);

    private final DashboardService dashboardService;
    private final NumberFormat currencyFormat;

    /**
     * Creates new form DashboardEmployee
     */
    public DashboardEmployee() {
        this.dashboardService = new DashboardService();
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        initComponents();
        setupCardIcons();
        addActionTimeType();

        // Load dữ liệu ban đầu (Hôm nay)
        loadDashboardData(TimeType.TODAY);
    }

    /**
     * Setup icons và màu sắc cho các card
     */
    private void setupCardIcons() {
        // Card 1: Phòng sắp hết hạn
        pnlListCard1.getRoomOccupancyRateCard().setLblIconRate(
            FontIcon.of(FontAwesomeSolid.CLOCK, 24, ALMOST_OUT_OF_TIME));
        pnlListCard1.getRoomOccupancyRateCard().setTitle("SỐ PHÒNG SẮP HẾT HẠN");

        // Card 2: Check-in
        pnlListCard1.getNumberCheckInCard().setLblIconRate(
            FontIcon.of(FontAwesomeSolid.MAP_MARKER_ALT, 24, NUMBER_CHECKIN));
        pnlListCard1.getNumberCheckInCard().setTitle("LƯỢT KHÁCH CHECKIN");

        // Card 3: Check-out
        pnlListCard1.getRevenueCard().setLblIconRate(
            FontIcon.of(FontAwesomeSolid.KEY, 24, NUMBER_CHECKOUT));
        pnlListCard1.getRevenueCard().setTitle("LƯỢT KHÁCH CHECKOUT");

        // Card 4: Tiền mở ca
        pnlListCard1.getBookingRateCard().setLblIconRate(
            FontIcon.of(FontAwesomeSolid.MONEY_CHECK, 24, MONEY_OPEN_SHIFT));
        pnlListCard1.getBookingRateCard().setTitle("TIỀN KHI MỞ CA");
        pnlListCard1.getBookingRateCard().getLblRoomCount().setForeground(Color.white);
    }

    /**
     * Load dữ liệu dashboard theo loại thời gian
     */
    private void loadDashboardData(TimeType timeType) {
        // Chạy task nền để không block UI
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            private DashboardSummaryDto summaryData;
            private List<RevenueSourceDto> revenueSources;
            private List<PeakHourDto> peakHours;
            private RoomStatusDto roomStatus;
            private WarningDto warnings;
            private List<ShiftNoteDto> shiftNotes;

            @Override
            protected Void doInBackground() {
                try {
                    // Lấy summary data
                    summaryData = dashboardService.getDashboardData(timeType);

                    // Lấy nguồn doanh thu
                    LocalDateTime startDate = getStartDateForTimeType(timeType);
                    LocalDateTime endDate = LocalDateTime.now();
                    revenueSources = dashboardService.getRevenueSources(startDate, endDate);

                    // Lấy khung giờ cao điểm
                    peakHours = dashboardService.getPeakHours(startDate, endDate);

                    // Lấy trạng thái phòng
                    roomStatus = dashboardService.getRoomStatus();

                    // Lấy cảnh báo
                    warnings = dashboardService.getWarnings();

                    // Lấy ghi chú ca gần nhất
                    shiftNotes = dashboardService.getRecentShiftNotes();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    // Cập nhật các card tổng quan
                    if (summaryData != null) {
                        updateDashboardUI(summaryData);
                    }

                    // Cập nhật nguồn doanh thu
                    if (revenueSources != null) {
                        revenueChart.updateData(revenueSources);
                    }

                    // Cập nhật khung giờ cao điểm
                    if (peakHours != null) {
                        lineChartPanel.updateData(peakHours);
                    }

                    // Cập nhật trạng thái phòng
                    if (roomStatus != null) {
                        statusRoomCard.updateData(roomStatus);
                    }

                    // Cập nhật cảnh báo
                    if (warnings != null) {
                        panelWarning.updateData(warnings);
                    }

                    // Cập nhật ghi chú ca
                    if (shiftNotes != null) {
                        cardNote.updateData(shiftNotes);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(
                        DashboardEmployee.this,
                        "Lỗi khi tải dữ liệu dashboard: " + e.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }

            private LocalDateTime getStartDateForTimeType(TimeType type) {
                switch (type) {
                    case TODAY:
                        return LocalDate.now().atStartOfDay();
                    case DAYS_7:
                        return LocalDate.now().minusDays(7).atStartOfDay();
                    case DAYS_30:
                        return LocalDate.now().minusDays(30).atStartOfDay();
                    case DAYS_90:
                        return LocalDate.now().minusDays(90).atStartOfDay();
                    default:
                        return LocalDate.now().atStartOfDay();
                }
            }
        };
        worker.execute();
    }

    /**
     * Cập nhật UI với dữ liệu mới
     */
    private void updateDashboardUI(DashboardSummaryDto data) {
        // Card 1: Phòng sắp hết hạn
        pnlListCard1.getRoomOccupancyRateCard().setMessage(
            data.getRoomsNearExpiry() + "/" + data.getTotalRooms() + " phòng");
        pnlListCard1.getRoomOccupancyRateCard().setLblValue(
            data.getRoomsNearExpiry() + " PHÒNG");

        // Card 2: Check-in
        pnlListCard1.getNumberCheckInCard().setMessage("Lượt checkin");
        pnlListCard1.getNumberCheckInCard().setLblValue(
            data.getCheckInCount() + " LƯỢT");

        // Card 3: Check-out
        pnlListCard1.getRevenueCard().setMessage("Lượt checkout");
        pnlListCard1.getRevenueCard().setLblValue(
            data.getCheckOutCount() + " LƯỢT");

        // Card 4: Tiền mở ca
        pnlListCard1.getBookingRateCard().setMessage("Ca gần nhất");
        pnlListCard1.getBookingRateCard().setLblValue(
            currencyFormat.format(data.getOpenShiftCash()));
    }

    /**
     * Thêm action cho các button time filter
     */
    private void addActionTimeType() {
        headerDashboard.getBtnToday().addActionListener(e -> {
            headerDashboard.setActiveButton(TimeType.TODAY);
            loadDashboardData(TimeType.TODAY);
        });

        headerDashboard.getBtn7Days().addActionListener(e -> {
            headerDashboard.setActiveButton(TimeType.DAYS_7);
            loadDashboardData(TimeType.DAYS_7);
        });

        headerDashboard.getBtn30Days().addActionListener(e -> {
            headerDashboard.setActiveButton(TimeType.DAYS_30);
            loadDashboardData(TimeType.DAYS_30);
        });

        headerDashboard.getBtn90Days().addActionListener(e -> {
            headerDashboard.setActiveButton(TimeType.DAYS_90);
            loadDashboardData(TimeType.DAYS_90);
        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerDashboard = new iuh.fit.se.group1.ui.component.dashboard.HeaderDashboard();
        revenueChart = new iuh.fit.se.group1.ui.component.dashboard.RevenueChart();
        lineChartPanel = new iuh.fit.se.group1.ui.component.dashboard.LineChartPanel();
        cardNote = new iuh.fit.se.group1.ui.component.dashboard.CardNote();
        panelWarning = new iuh.fit.se.group1.ui.component.dashboard.PanelWarning();
        statusRoomCard = new iuh.fit.se.group1.ui.component.dashboard.StatusRoomCard();
        pnlListCard1 = new iuh.fit.se.group1.ui.component.dashboard.PnlListCard();

        setBackground(new java.awt.Color(241, 241, 241));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerDashboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(revenueChart, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lineChartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 713, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cardNote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(statusRoomCard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(panelWarning, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(42, 42, 42))
            .addComponent(pnlListCard1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlListCard1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(revenueChart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lineChartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cardNote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statusRoomCard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelWarning, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private iuh.fit.se.group1.ui.component.dashboard.CardNote cardNote;
    private iuh.fit.se.group1.ui.component.dashboard.HeaderDashboard headerDashboard;
    private iuh.fit.se.group1.ui.component.dashboard.LineChartPanel lineChartPanel;
    private iuh.fit.se.group1.ui.component.dashboard.PanelWarning panelWarning;
    private iuh.fit.se.group1.ui.component.dashboard.PnlListCard pnlListCard1;
    private iuh.fit.se.group1.ui.component.dashboard.RevenueChart revenueChart;
    private iuh.fit.se.group1.ui.component.dashboard.StatusRoomCard statusRoomCard;
    // End of variables declaration//GEN-END:variables
}
