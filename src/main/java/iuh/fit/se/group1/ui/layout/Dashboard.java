/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.layout;

import iuh.fit.se.group1.dto.DashboardSummaryDto;
import iuh.fit.se.group1.dto.PeakHourDto;
import iuh.fit.se.group1.dto.RevenueSourceDto;
import iuh.fit.se.group1.dto.WarningDto;
import iuh.fit.se.group1.enums.TimeType;
import iuh.fit.se.group1.service.DashboardService;

import javax.swing.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

/**
 * Dashboard với chức năng load dữ liệu thật
 * @author THIS PC
 */
public class Dashboard extends javax.swing.JPanel {

    private final DashboardService dashboardService;
    private final NumberFormat currencyFormat;

    /**
     * Creates new form Dashboard
     */
    public Dashboard() {
        this.dashboardService = new DashboardService();
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        initComponents(); // GIỮ NGUYÊN - không đụng chạm

        // Chỉ thêm chức năng
        fetchData();
        addActionTimeType();
    }

    private void addActionTimeType() {
        headerDashboard1.getBtnToday().addActionListener(e -> {
            headerDashboard1.setActiveButton(TimeType.TODAY);
            loadDashboardData(TimeType.TODAY); // Load dữ liệu thật
        });

        headerDashboard1.getBtn7Days().addActionListener(e -> {
            headerDashboard1.setActiveButton(TimeType.DAYS_7);
            loadDashboardData(TimeType.DAYS_7); // Load dữ liệu thật
        });

        headerDashboard1.getBtn30Days().addActionListener(e -> {
            headerDashboard1.setActiveButton(TimeType.DAYS_30);
            loadDashboardData(TimeType.DAYS_30); // Load dữ liệu thật
        });

        headerDashboard1.getBtn90Days().addActionListener(e -> {
            headerDashboard1.setActiveButton(TimeType.DAYS_90);
            loadDashboardData(TimeType.DAYS_90); // Load dữ liệu thật
        });
    }

    private void fetchData() {
        // Load dữ liệu ban đầu
        loadDashboardData(TimeType.TODAY);
    }

    /**
     * Refresh dashboard data - Gọi method này khi chuyển tab vào Dashboard
     * Method public để MainLayout hoặc component khác có thể gọi
     */
    public void refreshData() {
        // Reset về tab "Hôm nay" và load dữ liệu mới
        headerDashboard1.setActiveButton(TimeType.TODAY);
        loadDashboardData(TimeType.TODAY);
    }

    /**
     * Refresh dashboard data với TimeType cụ thể
     * @param timeType Loại thời gian cần load
     */
    public void refreshData(TimeType timeType) {
        headerDashboard1.setActiveButton(timeType);
        loadDashboardData(timeType);
    }

    /**
     * Load dữ liệu từ database và cập nhật UI
     */
    private void loadDashboardData(TimeType timeType) {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            private DashboardSummaryDto summaryData;
            private List<RevenueSourceDto> revenueSources;
            private List<PeakHourDto> peakHours;
            private WarningDto warnings;
            private BigDecimal periodRevenue;
            private int currentGuestCount;

            @Override
            protected Void doInBackground() {
                try {
                    summaryData = dashboardService.getDashboardData(timeType);

                    LocalDateTime startDate = getStartDateForTimeType(timeType);
                    LocalDateTime endDate = LocalDateTime.now();
                    revenueSources = dashboardService.getRevenueSources(startDate, endDate);
                    peakHours = dashboardService.getPeakHours(startDate, endDate);
                    warnings = dashboardService.getWarnings();

                    // Lấy doanh thu theo time range đã chọn, không phải chỉ hôm nay
                    periodRevenue = dashboardService.getRevenueByDateRange(startDate, endDate);
                    currentGuestCount = dashboardService.getCurrentGuestCount();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    // Cập nhật dữ liệu lên các component có sẵn
                    if (summaryData != null) {
                        updateCards(summaryData, periodRevenue);
                    }
                    if (revenueSources != null) {
                        revenueChart1.updateData(revenueSources);
                    }
                    if (peakHours != null) {
                        lineChartPanel1.updateData(peakHours);
                    }
                    if (warnings != null) {
                        panelWarning1.updateData(warnings);
                    }
                    updateCardLiquid1(periodRevenue, timeType);
                    updateCardLiquid2(currentGuestCount, timeType);  // Truyền thêm timeType
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            private LocalDateTime getStartDateForTimeType(TimeType type) {
                switch (type) {
                    case TODAY: return LocalDate.now().atStartOfDay();
                    case DAYS_7: return LocalDate.now().minusDays(7).atStartOfDay();
                    case DAYS_30: return LocalDate.now().minusDays(30).atStartOfDay();
                    case DAYS_90: return LocalDate.now().minusDays(90).atStartOfDay();
                    default: return LocalDate.now().atStartOfDay();
                }
            }
        };
        worker.execute();
    }

    /**
     * Cập nhật 4 cards trên đầu
     */
    private void updateCards(DashboardSummaryDto data, BigDecimal todayRevenue) {
        int totalRooms = data.getTotalRooms() > 0 ? data.getTotalRooms() : 1;

        // Card 1: Số lượng PHÒNG TRỐNG - Có vòng tròn + message "X/Y phòng"
        int availableRooms = totalRooms - data.getRoomsNearExpiry();
        pnlListCard1.getRoomOccupancyRateCard().setPercentage(
            availableRooms, totalRooms); // Tự động set message "X/Y phòng"
        pnlListCard1.getRoomOccupancyRateCard().setLblValue(
            availableRooms + " PHÒNG");

        // Card 2: TỈ LỆ ĐẶT PHÒNG - KHÔNG có vòng tròn, message "Số lượt đặt phòng"
        pnlListCard1.getBookingRateCard().setLblValue(
            data.getBookingCount() + " LƯỢT");
        pnlListCard1.getBookingRateCard().setMessage("Số lượt đặt phòng"); // Ẩn vòng tròn

        // Card 3: DOANH THU - KHÔNG có vòng tròn
        // Sử dụng doanh thu THẬT từ database (todayRevenue)
        BigDecimal revenue = todayRevenue != null ? todayRevenue : BigDecimal.ZERO;

        // Tính % so với target (giả sử target 10 triệu/ngày)
        BigDecimal revenueTarget = new BigDecimal("10000000");
        int revenuePercentage = revenue.compareTo(BigDecimal.ZERO) > 0
            ? revenue.multiply(new BigDecimal("100"))
                     .divide(revenueTarget, 0, java.math.RoundingMode.HALF_UP)
                     .intValue()
            : 0;
        revenuePercentage = Math.min(100, Math.max(0, revenuePercentage));

        pnlListCard1.getRevenueCard().setPercentage(
            revenuePercentage, 100); // % so với target

        // Hiển thị doanh thu THẬT với format tiền VN
        pnlListCard1.getRevenueCard().setLblValue(
            currencyFormat.format(revenue));
        pnlListCard1.getRevenueCard().setMessage("Doanh thu"); // Ẩn vòng tròn

        // Card 4: Số lượng CHECK-IN - KHÔNG có vòng tròn
        pnlListCard1.getNumberCheckInCard().setPercentage(
            data.getCheckInCount(), 30); // Target 30 lượt
        pnlListCard1.getNumberCheckInCard().setLblValue(
            data.getCheckInCount() + " LƯỢT");
        pnlListCard1.getNumberCheckInCard().setMessage("Số lượt check-in"); // Ẩn vòng tròn
    }

    /**
     * Cập nhật CardLiquid 1: Phòng đang sử dụng
     */
    private void updateCardLiquid1(BigDecimal revenue, TimeType timeType) {
        // Lấy tổng số phòng đang được sử dụng (OCCUPIED)
        int occupiedRooms = dashboardService.getOccupiedRooms();
        int totalRooms = dashboardService.getTotalRooms();

        // Tính % phòng đang sử dụng
        int percentage = totalRooms > 0
            ? (occupiedRooms * 100) / totalRooms
            : 0;

        cardLiquid1.setTitle("PHÒNG ĐANG SỬ DỤNG");
        cardLiquid1.setDescription(occupiedRooms + "/" + totalRooms + " phòng");
        cardLiquid1.setValues(percentage);
    }

    /**
     * Cập nhật CardLiquid 2: Số hóa đơn hôm nay
     */
    private void updateCardLiquid2(int guestCount, TimeType currentTimeType) {
        // Đếm số hóa đơn hôm nay
        int todayOrders = dashboardService.getTodayOrderCount();

        // So sánh với ngày trước đó để tính %
        int previousOrders = 0;
        double changePercentage = 0;

        switch (currentTimeType) {
            case TODAY:
                // So sánh với cùng ngày tuần trước (7 ngày trước)
                previousOrders = dashboardService.getOrderCountDaysAgo(7);
                break;

            case DAYS_7:
                // So sánh tổng 7 ngày hiện tại với tổng 7 ngày trước đó
                todayOrders = dashboardService.getOrderCountForPeriod(7, false);
                previousOrders = dashboardService.getOrderCountForPeriod(7, true);
                break;

            case DAYS_30:
                // So sánh tổng 30 ngày hiện tại với tổng 30 ngày trước đó
                todayOrders = dashboardService.getOrderCountForPeriod(30, false);
                previousOrders = dashboardService.getOrderCountForPeriod(30, true);
                break;

            case DAYS_90:
                // So sánh tổng 90 ngày hiện tại với tổng 90 ngày trước đó
                todayOrders = dashboardService.getOrderCountForPeriod(90, false);
                previousOrders = dashboardService.getOrderCountForPeriod(90, true);
                break;
        }

        // Tính % thay đổi
        if (previousOrders > 0) {
            changePercentage = ((double) (todayOrders - previousOrders) / previousOrders) * 100;
        } else if (todayOrders > 0) {
            changePercentage = 100; // Tăng 100% nếu trước đó = 0
        }

        // Làm tròn % - GIỮ NGUYÊN DẤU, KHÔNG giới hạn
        // VD: 10 hóa đơn hôm nay, 1 hóa đơn tuần trước = 900%
        int displayPercentage = (int) Math.round(changePercentage);

        // Hiển thị
        cardLiquid2.setTitle("HÓA ĐƠN");

        // Mô tả theo time type với thông tin so sánh CHI TIẾT
        String description = "";
        String comparisonInfo = "";
        String comparisonDetail = "";  // Thêm thông tin chi tiết so sánh

        // Tạo chuỗi so sánh với số hóa đơn cụ thể
        if (changePercentage > 0) {
            comparisonInfo = " (↑" + displayPercentage + "%)";
        } else if (changePercentage < 0) {
            comparisonInfo = " (↓" + Math.abs(displayPercentage) + "%)";
        }

        // Tạo mô tả chi tiết về mốc thời gian so sánh
        switch (currentTimeType) {
            case TODAY:
                description = "Hôm nay: " + todayOrders  + comparisonInfo;
                comparisonDetail = "-" + previousOrders + " hóa đơn  (cùng ngày tuần trước)";
                break;
            case DAYS_7:
                description = "7 ngày: " + todayOrders  + comparisonInfo;
                comparisonDetail = "- " + previousOrders + " hóa đơn (7 ngày trước đó)";
                break;
            case DAYS_30:
                description = "30 ngày: " + todayOrders +  comparisonInfo;
                comparisonDetail = "- " + previousOrders + " hóa đơn (30 ngày trước đó)";
                break;
            case DAYS_90:
                description = "90 ngày: " + todayOrders  + comparisonInfo;
                comparisonDetail = "- " + previousOrders + " hóa đơn (90 ngày trước đó)";
                break;
        }

        // Gộp cả hai dòng: dòng chính + dòng so sánh
        cardLiquid2.setDescription(description + "\n" + comparisonDetail);

        // Hiển thị % thay đổi (giá trị tuyệt đối cho vòng tròn)
        cardLiquid2.setValues(Math.abs(displayPercentage));
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlListCard1 = new iuh.fit.se.group1.ui.component.dashboard.PnlListCard();
        headerDashboard1 = new iuh.fit.se.group1.ui.component.dashboard.HeaderDashboard();
        pnlFooter = new javax.swing.JPanel();
        panelWarning1 = new iuh.fit.se.group1.ui.component.dashboard.PanelWarning();
        cardLiquid1 = new iuh.fit.se.group1.ui.component.chart.CardLiquid();
        cardLiquid2 = new iuh.fit.se.group1.ui.component.chart.CardLiquid();
        revenueChart1 = new iuh.fit.se.group1.ui.component.dashboard.RevenueChart();
        lineChartPanel1 = new iuh.fit.se.group1.ui.component.dashboard.LineChartPanel();

        setBackground(new java.awt.Color(241, 241, 241));
        setForeground(new java.awt.Color(241, 241, 241));

        pnlFooter.setBackground(new java.awt.Color(241, 241, 241));

        javax.swing.GroupLayout pnlFooterLayout = new javax.swing.GroupLayout(pnlFooter);
        pnlFooter.setLayout(pnlFooterLayout);
        pnlFooterLayout.setHorizontalGroup(
            pnlFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFooterLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(panelWarning1, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51)
                .addComponent(cardLiquid1, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(cardLiquid2, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlFooterLayout.setVerticalGroup(
            pnlFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFooterLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(pnlFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cardLiquid2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cardLiquid1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelWarning1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlListCard1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(headerDashboard1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(pnlFooter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(revenueChart1, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56)
                .addComponent(lineChartPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 707, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerDashboard1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlListCard1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lineChartPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(revenueChart1, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addComponent(pnlFooter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private iuh.fit.se.group1.ui.component.chart.CardLiquid cardLiquid1;
    private iuh.fit.se.group1.ui.component.chart.CardLiquid cardLiquid2;
    private iuh.fit.se.group1.ui.component.dashboard.HeaderDashboard headerDashboard1;
    private iuh.fit.se.group1.ui.component.dashboard.LineChartPanel lineChartPanel1;
    private iuh.fit.se.group1.ui.component.dashboard.PanelWarning panelWarning1;
    private javax.swing.JPanel pnlFooter;
    private iuh.fit.se.group1.ui.component.dashboard.PnlListCard pnlListCard1;
    private iuh.fit.se.group1.ui.component.dashboard.RevenueChart revenueChart1;
    // End of variables declaration//GEN-END:variables
}
