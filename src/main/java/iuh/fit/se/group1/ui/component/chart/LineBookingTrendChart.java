package iuh.fit.se.group1.ui.component.chart;

import com.formdev.flatlaf.FlatClientProperties;
import iuh.fit.se.group1.service.CustomerService;
import iuh.fit.se.group1.service.OrderService;
import iuh.fit.se.group1.ui.component.custom.message.CustomDialog;
import iuh.fit.se.group1.ui.component.dashboard.DateCalculator;
import raven.chart.ChartLegendRenderer;
import raven.chart.data.category.DefaultCategoryDataset;
import raven.chart.line.LineChart;

import javax.swing.*;
import java.awt.*;
import java.text.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

public class LineBookingTrendChart extends JPanel {

    private final OrderService orderService;

    public LineBookingTrendChart() {
        this.orderService = new OrderService();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.white);
        createLineChart();
    }

    private void createLineChart() {
        lineChart = new LineChartCustom();
        lineChart.setChartType(LineChart.ChartType.CURVE);
        lineChart.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:5,5,5,5,$Component.borderColor,,20");
        add(lineChart);
        lineChart.setPreferredSize(new Dimension(1040,290));
        createLineChartData(7);
    }

    public void createLineChartData(LocalDate startDate, LocalDate endDate) {
        DefaultCategoryDataset<String, String> categoryDataset = new DefaultCategoryDataset<>();

        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (startDate.isEqual(endDate)) {
            CustomDialog.showMessage(this,
                    "Vui lòng chọn khoảng thời gian lớn hơn 1 ngày!",
                    "Cảnh báo",
                    CustomDialog.MessageType.WARNING,
                    800,200);
            return;
        }

        // Tìm giá trị max để tính scale cho trục Y
        int maxBookingCount = 0;

        // Lấy dữ liệu thật từ database cho mỗi ngày
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            String dateStr = currentDate.format(df);

            // Lấy số lượng booking theo loại phòng cho ngày này
            Map<String, Integer> bookingCount = orderService.getBookingCountByRoomTypeAndDate(currentDate);

            int singleRoomCount = bookingCount.getOrDefault("Phòng đơn", 0);
            int doubleRoomCount = bookingCount.getOrDefault("Phòng đôi", 0);

            // Cập nhật maxBookingCount
            maxBookingCount = Math.max(maxBookingCount, Math.max(singleRoomCount, doubleRoomCount));

            System.out.println("Date: " + dateStr + " | Single: " + singleRoomCount + ", Double: " + doubleRoomCount);

            categoryDataset.addValue(singleRoomCount, "Phòng đơn", dateStr);
            categoryDataset.addValue(doubleRoomCount, "Phòng đôi", dateStr);

            currentDate = currentDate.plusDays(1);
        }

        // Tính maxValues cho trục Y
        double chartMaxValue = calculateChartMaxValue(maxBookingCount);
        System.out.println("Line chart max value: " + chartMaxValue + " (data max: " + maxBookingCount + ")");

        // WORKAROUND: Thêm data point ẩn với giá trị = chartMaxValue để buộc chart scale đúng
        // Thêm vào đầu tiên để set scale, sau đó data thật sẽ hiển thị đúng
        categoryDataset.addValue(chartMaxValue, "Phòng đơn", "");
        categoryDataset.addValue(0, "Phòng đôi", "");

        updateChartWithData(categoryDataset, chartMaxValue);
    }

    public void createLineChartData(int dateRange) {
        // Tính toán khoảng thời gian
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(dateRange - 1);

        // Gọi method chính với startDate và endDate
        createLineChartData(startDate, endDate);
    }

    private void updateChartWithData(DefaultCategoryDataset<String, String> categoryDataset, double chartMaxValue) {
        // Kiểm tra nếu không có dữ liệu
        if (categoryDataset.getColumnCount() == 0) {
            System.out.println("No data to display in chart");
            return;
        }

        // XÓA chart cũ và TẠO MỚI để đảm bảo scale được reset đúng
        if (lineChart != null) {
            remove(lineChart);
        }

        // Tạo LineChart mới
        lineChart = new LineChartCustom();
        lineChart.setChartType(LineChart.ChartType.CURVE);
        lineChart.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:5,5,5,5,$Component.borderColor,,20");
        lineChart.setPreferredSize(new Dimension(1040, 290));

        // Set data TRƯỚC để LineChart khởi tạo blankPlotChart
        lineChart.setCategoryDataset(categoryDataset);

        // SAU ĐÓ mới set maxValues bằng reflection
        setLineChartMaxValues(chartMaxValue);

        // Set màu sắc
        lineChart.getChartColor().addColor(Color.decode("#38bdf8"), Color.decode("#fb7185"), Color.decode("#34d399"));

        try {
            // Tìm index của data point đầu tiên và cuối cùng không rỗng
            int firstValidIdx = -1;
            int lastValidIdx = -1;
            int validCount = 0;

            for (int i = 0; i < categoryDataset.getColumnCount(); i++) {
                String label = categoryDataset.getColumnKey(i);
                if (label != null && !label.trim().isEmpty()) {
                    if (firstValidIdx == -1) {
                        firstValidIdx = i;
                    }
                    lastValidIdx = i;
                    validCount++;
                }
            }

            // Khai báo final để sử dụng trong inner class
            final int firstValidIndex = firstValidIdx;
            final int lastValidIndex = lastValidIdx;

            if (firstValidIndex != -1 && lastValidIndex != -1) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date date = sdf.parse(categoryDataset.getColumnKey(firstValidIndex));
                Date dateEnd = sdf.parse(categoryDataset.getColumnKey(lastValidIndex));

                DateCalculator dcal = new DateCalculator(date, dateEnd);
                long diff = dcal.getDifferenceDays();

                // Tính khoảng cách giữa các labels để tối đa hiển thị 10-12 labels
                // Với 90 ngày -> hiển thị mỗi 8-9 ngày một label
                int maxLabelsToShow = 10;
                final double labelInterval = diff > 0 ? Math.max(1, Math.ceil((double) validCount / maxLabelsToShow)) : 1;

                System.out.println("Chart range: " + diff + " days, Valid data points: " + validCount + ", Label interval: " + labelInterval);

                lineChart.setLegendRenderer(new ChartLegendRenderer() {
                    @Override
                    public Component getLegendComponent(Object legend, int index) {
                        String label = categoryDataset.getColumnKey(index);

                        // Ẩn label rỗng (data point ẩn)
                        if (label == null || label.trim().isEmpty()) {
                            return null;
                        }

                        // Luôn hiển thị label đầu tiên và cuối cùng
                        if (index == firstValidIndex || index == lastValidIndex) {
                            return super.getLegendComponent(legend, index);
                        }

                        // Hiển thị labels theo khoảng cách đều đặn
                        if ((index - firstValidIndex) % labelInterval == 0) {
                            return super.getLegendComponent(legend, index);
                        }

                        return null;
                    }
                });
            }
        } catch (Exception e) {
            System.err.println("Error updating chart: " + e.getMessage());
        }

        JLabel header = new JLabel("Xu hướng loại phòng");
        header.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:+1;"
                + "border:0,0,5,0");
        lineChart.setHeader(header);

        // Thêm chart mới vào panel
        add(lineChart, BorderLayout.CENTER);

        // Force refresh
        revalidate();
        repaint();
    }

    /**
     * Set maxValues cho LineChart bằng reflection với debug chi tiết
     */
    private void setLineChartMaxValues(double maxValue) {
        try {

            // In ra tất cả fields của LineChart
            System.out.println("LineChart fields:");
            for (java.lang.reflect.Field f : LineChart.class.getDeclaredFields()) {
                System.out.println("  - " + f.getName() + " (" + f.getType().getSimpleName() + ")");
            }

            // Thử tìm blankPlotChart
            java.lang.reflect.Field blankPlotChartField = null;
            for (java.lang.reflect.Field f : LineChart.class.getDeclaredFields()) {
                if (f.getName().toLowerCase().contains("blank") || f.getName().toLowerCase().contains("plot")) {
                    blankPlotChartField = f;
                    System.out.println("\nFound potential field: " + f.getName());
                    break;
                }
            }

            if (blankPlotChartField != null) {
                blankPlotChartField.setAccessible(true);
                Object blankPlotChart = blankPlotChartField.get(lineChart);

                if (blankPlotChart != null) {
                    System.out.println("BlankPlotChart type: " + blankPlotChart.getClass().getName());
                    System.out.println("BlankPlotChart fields:");

                    Class<?> clazz = blankPlotChart.getClass();
                    for (java.lang.reflect.Field f : clazz.getDeclaredFields()) {
                        System.out.println("  - " + f.getName() + " (" + f.getType().getSimpleName() + ")");
                    }

                    // Thử set maxValues
                    for (java.lang.reflect.Field f : clazz.getDeclaredFields()) {
                        if (f.getName().toLowerCase().contains("max")) {
                            f.setAccessible(true);
                            System.out.println("\nTrying to set field: " + f.getName() + " (type: " + f.getType() + ")");

                            if (f.getType() == double.class) {
                                f.setDouble(blankPlotChart, maxValue);
                                System.out.println("✓ Successfully set " + f.getName() + " = " + maxValue);
                                return;
                            } else if (f.getType() == Double.class) {
                                f.set(blankPlotChart, maxValue);
                                System.out.println("✓ Successfully set " + f.getName() + " = " + maxValue);
                                return;
                            }
                        }
                    }
                }
            }

            System.out.println("✗ Could not set maxValues - no suitable field found");

        } catch (Exception e) {
            System.err.println("Error setting maxValues: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Tính toán giá trị max cho chart dựa trên giá trị lớn nhất trong data
     * Làm tròn lên để có độ chia đẹp
     */
    private double calculateChartMaxValue(int maxValue) {
        if (maxValue == 0) return 10; // Default nếu không có dữ liệu

        // Tìm bội số đẹp (1, 2, 5, 10) * 10^n
        double magnitude = Math.pow(10, Math.floor(Math.log10(maxValue)));
        double normalized = maxValue / magnitude;

        double roundedNormalized;
        if (normalized <= 1) roundedNormalized = 1;
        else if (normalized <= 2) roundedNormalized = 2;
        else if (normalized <= 5) roundedNormalized = 5;
        else roundedNormalized = 10;

        double result = roundedNormalized * magnitude;

        // Đảm bảo result lớn hơn maxValue một chút để line không chạm trần
        if (result <= maxValue) {
            result *= 1.2;
        }

        return Math.ceil(result);
    }


    private LineChartCustom lineChart;
}
