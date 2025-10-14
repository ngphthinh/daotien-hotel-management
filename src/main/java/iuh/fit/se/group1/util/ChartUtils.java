package iuh.fit.se.group1.util;

import java.awt.*;
import java.lang.reflect.Field;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import raven.chart.pie.PieChart;

public class ChartUtils {


    private static final Logger log = LoggerFactory.getLogger(ChartUtils.class);

    /**
     * Truy xuất và thay đổi màu nền của các JPanel bên trong PieChart sử dụng Reflection
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
}
