/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.layout;

import iuh.fit.se.group1.enums.TimeType;
import iuh.fit.se.group1.service.OrderService;
import iuh.fit.se.group1.util.Constants;

import javax.swing.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 *
 * @author THIS PC
 */
public class RevenueStatistics extends javax.swing.JPanel {

    private final OrderService orderService = new OrderService();

    /**
     * Creates new form RevenueStatistics
     */
    public RevenueStatistics() {
        initComponents();
        headerChart1.getjLabel1().setText(
                "<html><span style='color:white;'>Quản lý thống kê</span>"

                        + "<span style='color:rgb(204,204,204);'> &gt; Doanh thu</span></html>");



        setActionButtonRange();

        loadDataTo7Day();

        headerChart1.getBtnView().addActionListener(e -> {
            String fromDate = headerChart1.getTxtFromDate().getText();
            String toDate = headerChart1.getTxtToDate().getText();

            LocalDate from = parseDate(fromDate);
            LocalDate to = parseDate(toDate);

            loadData(from, to);
        });
    }

    private void loadDataTo7Day () {
        LocalDate today = LocalDate.now();
        LocalDate from = today.minusDays(6); // 7 days including today
        loadData(from, today);
    }



    private void loadData(LocalDate from, LocalDate to) {

        // Lấy tổng doanh thu
        BigDecimal totalRevenue = orderService.getTotalRevenueBetweenDates(from, to);

        // Tính phần trăm tăng trưởng so với kỳ trước
        long days = ChronoUnit.DAYS.between(from, to) + 1;
        LocalDate prevFrom = from.minusDays(days);
        LocalDate prevTo = from.minusDays(1);
        BigDecimal prevRevenue = orderService.getTotalRevenueBetweenDates(prevFrom, prevTo);

        double growthPercent = 0;
        if (prevRevenue.compareTo(BigDecimal.ZERO) > 0) {
            growthPercent = totalRevenue.subtract(prevRevenue)
                .divide(prevRevenue, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
        }

        String growthText = String.format("%s%.1f%%",
            growthPercent >= 0 ? "+" : "", growthPercent);

        // Set value for card with growth rate
        card1.setValue(Constants.VND_FORMAT.format(totalRevenue));


        loadRevenueColumnChart(from, to);
    }

    /**
     * Load dữ liệu biểu đồ cột doanh thu theo loại phòng và ngày
     * Tối ưu số lượng cột hiển thị để tránh biểu đồ quá dày
     *
     * Logic phân chia (tối đa 10 cột):
     * - 1-10 ngày: Hiển thị theo ngày (tối đa 10 cột)
     * - 11-70 ngày: Nhóm theo tuần (tối đa 10 tuần)
     * - >70 ngày: Nhóm tùy chỉnh để đảm bảo tối đa 10 cột
     */
    private void loadRevenueColumnChart(LocalDate from, LocalDate to) {

        long days = ChronoUnit.DAYS.between(from, to) + 1;

        List<String> labels = new ArrayList<>();
        List<double[]> values = new ArrayList<>();

        if (days <= 10) {
            loadDailyData(from, to, labels, values);
        } else if (days <= 70) {
            loadWeeklyData(from, to, labels, values);
        } else {
            loadAdaptiveGroupData(from, to, labels, values, 10);
        }



        // Validate data
        if (labels.isEmpty() || values.isEmpty()) {

            values.add(new double[]{0, 0});
        } else {
            // Check if all values are zero
            boolean hasData = false;
            for (double[] val : values) {
                if (val[0] > 0 || val[1] > 0) {
                    hasData = true;
                    break;
                }
            }

            if (!hasData) {
                System.out.println("WARNING: All revenue values are ZERO!");
            } else {
                // Print some sample data
                for (int i = 0; i < Math.min(3, labels.size()); i++) {
                    System.out.println(String.format("  %s: Single=%.0f, Double=%.0f",
                        labels.get(i), values.get(i)[0], values.get(i)[1]));
                }
            }
        }

//        revenueColumnChart1.
        revenueColumnChart1.setData(labels, values);
        System.out.println("Chart update completed!");
    }


    /**
     * Load dữ liệu theo ngày (cho khoảng <= 7 ngày)
     */
    private void loadDailyData(LocalDate from, LocalDate to, List<String> labels, List<double[]> values) {
        LocalDate current = from;
        while (!current.isAfter(to)) {
            // Format: dd/MM (ví dụ: 21/12)
            String label = current.format(DateTimeFormatter.ofPattern("dd/MM"));
            labels.add(label);

            Map<String, BigDecimal> revenueMap = orderService.getRevenueByRoomType(current, current);
            double singleRevenue = revenueMap.getOrDefault("Phòng đơn", BigDecimal.ZERO).doubleValue();
            double doubleRevenue = revenueMap.getOrDefault("Phòng đôi", BigDecimal.ZERO).doubleValue();

            values.add(new double[]{singleRevenue, doubleRevenue});
            current = current.plusDays(1);
        }
    }


    /**
     * Load dữ liệu theo tuần (cho khoảng 11-70 ngày)
     * Mỗi tuần = 7 ngày, tuần cuối có thể ít hơn
     */
    private void loadWeeklyData(LocalDate from, LocalDate to, List<String> labels, List<double[]> values) {
        LocalDate current = from;
        int weekNum = 1;

        while (!current.isAfter(to)) {
            LocalDate weekEnd = current.plusDays(6);
            if (weekEnd.isAfter(to)) {
                weekEnd = to;
            }

            // Format: "Tuần 1: 01/12-07/12"
            String label = String.format("%s-%s",
                current.format(DateTimeFormatter.ofPattern("dd/MM")),
                weekEnd.format(DateTimeFormatter.ofPattern("dd/MM"))
            );
            labels.add(label);

            // Tổng doanh thu trong tuần
            double totalSingle = 0;
            double totalDouble = 0;

            LocalDate day = current;
            while (!day.isAfter(weekEnd)) {
                Map<String, BigDecimal> revenueMap = orderService.getRevenueByRoomType(day, day);
                totalSingle += revenueMap.getOrDefault("Phòng đơn", BigDecimal.ZERO).doubleValue();
                totalDouble += revenueMap.getOrDefault("Phòng đôi", BigDecimal.ZERO).doubleValue();
                day = day.plusDays(1);
            }

            values.add(new double[]{totalSingle, totalDouble});

            current = weekEnd.plusDays(1);
            weekNum++;
        }
    }

    /**
     * Load dữ liệu nhóm tự động để đảm bảo tối đa số cột mong muốn
     * Sử dụng cho khoảng thời gian dài > 70 ngày
     * @param maxColumns Số cột tối đa (mặc định 10)
     */
    private void loadAdaptiveGroupData(LocalDate from, LocalDate to, List<String> labels, List<double[]> values, int maxColumns) {
        long totalDays = ChronoUnit.DAYS.between(from, to) + 1;

        // Tính số ngày mỗi nhóm để có đúng số cột mong muốn
        int groupSize = (int) Math.ceil((double) totalDays / maxColumns);

        LocalDate current = from;
        int groupNum = 1;

        while (!current.isAfter(to) && labels.size() < maxColumns) {
            LocalDate groupEnd = current.plusDays(groupSize - 1);
            if (groupEnd.isAfter(to)) {
                groupEnd = to;
            }

            // Format label tùy theo độ dài khoảng thời gian
            String label;
            if (groupSize <= 7) {
                // Nếu nhóm ngắn, hiển thị ngày đầu-cuối
                label = String.format("%s-%s",
                    current.format(DateTimeFormatter.ofPattern("dd/MM")),
                    groupEnd.format(DateTimeFormatter.ofPattern("dd/MM"))
                );
            } else if (groupSize <= 31) {
                // Nếu nhóm ~1 tháng, hiển thị tuần hoặc khoảng ngày
                label = String.format("%s-%s",
                    current.format(DateTimeFormatter.ofPattern("dd/MM")),
                    groupEnd.format(DateTimeFormatter.ofPattern("dd/MM"))
                );
            } else {
                // Nếu nhóm dài, chỉ hiển thị tháng/năm
                if (current.getMonthValue() == groupEnd.getMonthValue()) {
                    label = current.format(DateTimeFormatter.ofPattern("MM/yyyy"));
                } else {
                    label = String.format("%s-%s",
                        current.format(DateTimeFormatter.ofPattern("MM/yy")),
                        groupEnd.format(DateTimeFormatter.ofPattern("MM/yy"))
                    );
                }
            }
            labels.add(label);

            // Tổng doanh thu trong nhóm
            double totalSingle = 0;
            double totalDouble = 0;

            LocalDate day = current;
            while (!day.isAfter(groupEnd)) {
                Map<String, BigDecimal> revenueMap = orderService.getRevenueByRoomType(day, day);
                totalSingle += revenueMap.getOrDefault("Phòng đơn", BigDecimal.ZERO).doubleValue();
                totalDouble += revenueMap.getOrDefault("Phòng đôi", BigDecimal.ZERO).doubleValue();
                day = day.plusDays(1);
            }

            values.add(new double[]{totalSingle, totalDouble});

            current = groupEnd.plusDays(1);
            groupNum++;
        }
    }


    public LocalDate parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(date, formatter);
    }

    private void setActionButtonRange() {
        rangeDateButton1.getBtn7Days().addActionListener(e -> {
            rangeDateButton1.setActiveButton(TimeType.DAYS_7);

            LocalDate to = LocalDate.now();
            LocalDate from = to.minusDays(6); // 7 days including today
            headerChart1.getTxtFromDate().setText(from.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            headerChart1.getTxtToDate().setText(to.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            loadData(from, to);
        });

        rangeDateButton1.getBtn30Days().addActionListener(e -> {
            rangeDateButton1.setActiveButton(TimeType.DAYS_30);

            LocalDate to = LocalDate.now();
            LocalDate from = to.minusDays(29); // 30 days including today

            headerChart1.getTxtFromDate().setText(from.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            headerChart1.getTxtToDate().setText(to.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            loadData(from, to);
        });

        rangeDateButton1.getBtn90Days().addActionListener(e -> {
            rangeDateButton1.setActiveButton(TimeType.DAYS_90);

            LocalDate to = LocalDate.now();
            LocalDate from = to.minusDays(89); // 90 days including today
            headerChart1.getTxtFromDate().setText(from.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            headerChart1.getTxtToDate().setText(to.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            loadData(from, to);
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

        revenueColumnChart1 = new iuh.fit.se.group1.ui.component.chart.RevenueColumnChart();
        rangeDateButton1 = new iuh.fit.se.group1.ui.component.chart.RangeDateButton();
        card1 = new iuh.fit.se.group1.ui.component.chart.Card();
        headerChart1 = new iuh.fit.se.group1.ui.component.chart.HeaderChart();

        setBackground(new java.awt.Color(241, 241, 241));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(123, 123, 123)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(revenueColumnChart1, javax.swing.GroupLayout.PREFERRED_SIZE, 969, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(card1, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(rangeDateButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(67, Short.MAX_VALUE))
            .addComponent(headerChart1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(headerChart1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(card1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rangeDateButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(revenueColumnChart1, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62))
        );
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String[] args) {
        JFrame frame = new JFrame("Revenue Statistics Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.add(new RevenueStatistics());
        frame.setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private iuh.fit.se.group1.ui.component.chart.Card card1;
    private iuh.fit.se.group1.ui.component.chart.HeaderChart headerChart1;
    private iuh.fit.se.group1.ui.component.chart.RangeDateButton rangeDateButton1;
    private iuh.fit.se.group1.ui.component.chart.RevenueColumnChart revenueColumnChart1;
    // End of variables declaration//GEN-END:variables
}
