/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.layout;

import iuh.fit.se.group1.dto.*;
import iuh.fit.se.group1.enums.TimeType;
import iuh.fit.se.group1.network.Response;
import iuh.fit.se.group1.network.client.ClientSocketManager;
import iuh.fit.se.group1.network.client.SocketFacade;
import iuh.fit.se.group1.network.client.service.DashboardServiceClient;
import iuh.fit.se.group1.ui.component.chart.CardLiquid;
import iuh.fit.se.group1.ui.component.dashboard.*;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

/**
 * <<<<<<< HEAD
 * import iuh.fit.se.group1.ui.component.raven.chart.ModelChart;
 * <p>
 * =======
 * Dashboard với chức năng load dữ liệu thật
 * >>>>>>> 5d7ee2afcdde6fdbf72aa0b8988b00da6c42f147
 *
 * @author THIS PC
 */
public class Dashboard extends JPanel {

    private final DashboardServiceClient dashboardService;
    private final NumberFormat currencyFormat;

    /**
     * Creates new form Dashboard
     */
    public Dashboard() {

        this.dashboardService = SocketFacade.getInstance().getDashboard();
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        initComponents(); // GIỮ NGUYÊN - không đụng chạm


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

        headerDashboard1.setActiveButton(TimeType.TODAY);
        loadDashboardData(TimeType.TODAY);
    }

    /**
     * Refresh dashboard data với TimeType cụ thể
     *
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
                    Response response = dashboardService.getDashboardData(timeType);

                    if (response.getCode() != 200) {
                        JOptionPane.showMessageDialog(null, "Invalid response from server. Check again.", "Error", JOptionPane.ERROR_MESSAGE);
                        return null;
                    }

                    DashboardDTO dashboardDTO = (DashboardDTO) response.getData();

                    summaryData = dashboardDTO.getSummaryData();

                    revenueSources = dashboardDTO.getRevenueSources();
                    peakHours = dashboardDTO.getPeakHours();
                    warnings = dashboardDTO.getWarnings();

                    periodRevenue = dashboardDTO.getPeriodRevenue();
                    currentGuestCount = dashboardDTO.getCurrentGuestCount();
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
                .divide(revenueTarget, 0, RoundingMode.HALF_UP)
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
    private void updateCardLiquid1(BigDecimal revenue, TimeType timeType) throws Exception {
        // Lấy tổng số phòng đang được sử dụng (OCCUPIED)

        Response response = dashboardService.getRooms();
        if (response == null || response.getCode() != 200) {
            JOptionPane.showMessageDialog(null, "Failed to fetch room data. Check again.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        CountRoomDashboard countRoomDashboard = (CountRoomDashboard) response.getData();

        int occupiedRooms = countRoomDashboard.getOccupiedRooms();
        int totalRooms = countRoomDashboard.getTotalRooms();

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
    private void updateCardLiquid2(int guestCount, TimeType currentTimeType) throws Exception {
        // Đếm số hóa đơn hôm nay
        Response res = dashboardService.getOrderStatistics(currentTimeType);

        if (res == null || res.getCode() != 200) {
            JOptionPane.showMessageDialog(null, "Failed to fetch order statistics.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        OrderStatisticsResponse orderStatistics = (OrderStatisticsResponse) res.getData();

        int todayOrders = orderStatistics.getCurrentOrders();
        int previousOrders = orderStatistics.getPreviousOrders();

        // Tính % thay đổi
        double changePercentage = 0;
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
                description = "Hôm nay: " + todayOrders + comparisonInfo;
                comparisonDetail = "-" + previousOrders + " hóa đơn  (cùng ngày tuần trước)";
                break;
            case DAYS_7:
                description = "7 ngày: " + todayOrders + comparisonInfo;
                comparisonDetail = "- " + previousOrders + " hóa đơn (7 ngày trước đó)";
                break;
            case DAYS_30:
                description = "30 ngày: " + todayOrders + comparisonInfo;
                comparisonDetail = "- " + previousOrders + " hóa đơn (30 ngày trước đó)";
                break;
            case DAYS_90:
                description = "90 ngày: " + todayOrders + comparisonInfo;
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

        pnlListCard1 = new PnlListCard();
        headerDashboard1 = new HeaderDashboard();
        pnlFooter = new JPanel();
        panelWarning1 = new PanelWarning();
        cardLiquid1 = new CardLiquid();
        cardLiquid2 = new CardLiquid();
        revenueChart1 = new RevenueChart();
        lineChartPanel1 = new LineChartPanel();

        setBackground(new Color(241, 241, 241));
        setForeground(new Color(241, 241, 241));

        pnlFooter.setBackground(new Color(241, 241, 241));

        GroupLayout pnlFooterLayout = new GroupLayout(pnlFooter);
        pnlFooter.setLayout(pnlFooterLayout);
        pnlFooterLayout.setHorizontalGroup(
                pnlFooterLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(pnlFooterLayout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(panelWarning1, GroupLayout.PREFERRED_SIZE, 315, GroupLayout.PREFERRED_SIZE)
                                .addGap(51, 51, 51)
                                .addComponent(cardLiquid1, GroupLayout.PREFERRED_SIZE, 352, GroupLayout.PREFERRED_SIZE)
                                .addGap(47, 47, 47)
                                .addComponent(cardLiquid2, GroupLayout.PREFERRED_SIZE, 354, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlFooterLayout.setVerticalGroup(
                pnlFooterLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(pnlFooterLayout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addGroup(pnlFooterLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(cardLiquid2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(cardLiquid1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(panelWarning1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE))
        );

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(pnlListCard1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(headerDashboard1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(pnlFooter, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(revenueChart1, GroupLayout.PREFERRED_SIZE, 376, GroupLayout.PREFERRED_SIZE)
                                .addGap(56, 56, 56)
                                .addComponent(lineChartPanel1, GroupLayout.PREFERRED_SIZE, 707, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(headerDashboard1, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pnlListCard1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lineChartPanel1, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                        .addComponent(revenueChart1, GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE))
                                .addGap(0, 0, 0)
                                .addComponent(pnlFooter, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private CardLiquid cardLiquid1;
    private CardLiquid cardLiquid2;
    private HeaderDashboard headerDashboard1;
    private LineChartPanel lineChartPanel1;
    private PanelWarning panelWarning1;
    private JPanel pnlFooter;
    private PnlListCard pnlListCard1;
    private RevenueChart revenueChart1;
    // End of variables declaration//GEN-END:variables
}
