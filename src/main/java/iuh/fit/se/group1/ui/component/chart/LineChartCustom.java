package iuh.fit.se.group1.ui.component.chart;

import raven.chart.ChartColor;
import raven.chart.blankchart.PanelChartRender;
import raven.chart.line.LineChart;
import raven.chart.utils.ChartUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class LineChartCustom extends LineChart {
    public LineChartCustom() {
        super();

        try {
            // Lấy field chartColor từ class cha
            Field field = LineChart.class.getDeclaredField("chartColor");
            field.setAccessible(true);
            ChartColor chartColor = (ChartColor) field.get(this);

            // Xóa màu cũ và thêm màu đen

            chartColor.addColor(Color.BLACK);
            this.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // đổi format tiền tệ sang ₫
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
        valuesFormat = new DecimalFormat("#,##0.##", symbols);


        super.layeredPane.setBackground(Color.white);
        super.panelLegend.setBackground(Color.white);
        super.panelHeader.setBackground(Color.white);
        super.panelValues.setBackground(Color.white);

        super.panelRender.setBackground(new Color(255, 255, 255));
        super.setBackground(Color.white);

    }


}
