package iuh.fit.se.group1.util;

import java.awt.*;
import java.lang.reflect.Field;
import javax.swing.*;

import com.raven.chart.Chart;
import iuh.fit.se.group1.ui.swing.Background;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import raven.chart.bar.HorizontalBarChart;
import raven.chart.line.LineChart;
import raven.chart.pie.PieChart;

public class ChartUtils {


    private static final Logger log = LoggerFactory.getLogger(ChartUtils.class);

    /**
     * Truy xuất và thay đổi màu nền của các JPanel bên trong PieChart sử dụng Reflection
     *
     * @param pieChart
     * @param color
     */
    public static void setPieChartBackground(PieChart pieChart, Color color) {
        try {
            // Danh sách các field cần thay màu
            String[] fieldNames = {"panelHeader", "panelLegend", "panelRender"};

            for (String name : fieldNames) {
                Field field = PieChart.class.getDeclaredField(name);
                field.setAccessible(true);
                JPanel panel = (JPanel) field.get(pieChart);
                if (panel != null) {
                    panel.setBackground(color);
                    panel.setOpaque(true);
                }
            }

        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException e) {
            log.error("Error setting PieChart background color", e);
        }
    }

    public static void setHorizontalBar(HorizontalBarChart chart, Color backgroundColor) {
        try {
            String[] fieldNames = {"panelHeader", "panelRender", "panelFooter"};

            for (String name : fieldNames) {
                Field field = HorizontalBarChart.class.getDeclaredField(name);
                field.setAccessible(true);
                JPanel panel = (JPanel) field.get(chart);
                if (panel != null) {
                    panel.setBackground(backgroundColor);
                    panel.setOpaque(true);
                }
            }

            // Thêm phần này để full nền trắng
            chart.setBackground(backgroundColor);
            chart.setOpaque(true);

            Field fieldLayered = HorizontalBarChart.class.getDeclaredField("layeredPane");
            fieldLayered.setAccessible(true);
            JLayeredPane layered = (JLayeredPane) fieldLayered.get(chart);
            layered.setBackground(backgroundColor);
            layered.setOpaque(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Set integer format (không có $ và .00) cho HorizontalBarChart
     */
    public static void setHorizontalBarIntegerFormat(HorizontalBarChart chart) {
        try {
            // Thử tìm và set DecimalFormat vào chart
            Field[] fields = HorizontalBarChart.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(chart);

                // Tìm DecimalFormat hoặc NumberFormat
                if (value instanceof java.text.DecimalFormat) {
                    java.text.DecimalFormat df = (java.text.DecimalFormat) value;
                    df.applyPattern("#,##0");  // Pattern số nguyên
                    log.info("Set DecimalFormat to integer pattern for field: {}", field.getName());
                } else if (value instanceof java.text.NumberFormat) {
                    // Thay thế bằng DecimalFormat mới
                    java.text.DecimalFormat newFormat = new java.text.DecimalFormat("#,##0");
                    field.set(chart, newFormat);
                    log.info("Replaced NumberFormat with DecimalFormat for field: {}", field.getName());
                }
            }

            // Thử gọi method nếu có
            try {
                java.lang.reflect.Method method = chart.getClass().getMethod("setLabelFormat", java.text.DecimalFormat.class);
                method.invoke(chart, new java.text.DecimalFormat("#,##0"));
                log.info("Called setLabelFormat(DecimalFormat) successfully");
            } catch (NoSuchMethodException e) {
                // Method không tồn tại, bỏ qua
            }

        } catch (Exception e) {
            log.warn("Could not set integer format for HorizontalBarChart: {}", e.getMessage());
        }
    }


    public static void clearDataColumnChart(Chart chart) {
        try {
            String[] fieldNames = {"model"};

            for (String name : fieldNames) {
                Field field = Chart.class.getDeclaredField(name);
                field.setAccessible(true);
                java.util.List<?> model = (java.util.List<?>) field.get(chart);
                model.clear();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void setLineChart(LineChart chart, Color backgroundColor){
        try {
            String[] fieldNames = {"panelHeader", "panelRender", "panelFooter"};

            for (String name : fieldNames) {
                Field field = LineChart.class.getDeclaredField(name);
                field.setAccessible(true);
                JPanel panel = (JPanel) field.get(chart);
                if (panel != null) {
                    panel.setBackground(backgroundColor);
                    panel.setOpaque(true);
                }
            }

            // Thêm phần này để full nền trắng
            chart.setBackground(backgroundColor);
            chart.setOpaque(true);

//            Field fieldLayered = HorizontalBarChart.class.getDeclaredField("layeredPane");
//            fieldLayered.setAccessible(true);
//            JLayeredPane layered = (JLayeredPane) fieldLayered.get(chart);
//            layered.setBackground(backgroundColor);
//            layered.setOpaque(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
