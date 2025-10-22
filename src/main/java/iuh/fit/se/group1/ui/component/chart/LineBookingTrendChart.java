package iuh.fit.se.group1.ui.component.chart;

import com.formdev.flatlaf.FlatClientProperties;
import iuh.fit.se.group1.ui.component.dashboard.DateCalculator;
import raven.chart.ChartLegendRenderer;
import raven.chart.blankchart.PlotChart;
import raven.chart.data.category.DefaultCategoryDataset;
import raven.chart.line.LineChart;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.text.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class LineBookingTrendChart extends JPanel {

    public LineBookingTrendChart() {
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

    public void createLineChartData(int dateRange) {
        DefaultCategoryDataset<String, String> categoryDataset = new DefaultCategoryDataset<>();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Random ran = new Random();
        for (int i = 1; i <= dateRange; i++) {
            String date = df.format(cal.getTime());
            categoryDataset.addValue(ran.nextInt(700) + 5, "Phòng đơn", date);
            categoryDataset.addValue(ran.nextInt(700) + 5, "Phòng đôi", date);
            cal.add(Calendar.DATE, 1);
        }

        /**
         * Control the legend we do not show all legend
         */
        try {
            Date date = df.parse(categoryDataset.getColumnKey(0));
            Date dateEnd = df.parse(categoryDataset.getColumnKey(categoryDataset.getColumnCount() - 1));

            DateCalculator dcal = new DateCalculator(date, dateEnd);
            long diff = dcal.getDifferenceDays();

            double d = Math.ceil((diff / 10f));
            lineChart.setLegendRenderer(new ChartLegendRenderer() {
                @Override
                public Component getLegendComponent(Object legend, int index) {
                    if (index % d == 0) {
                        return super.getLegendComponent(legend, index);
                    } else {
                        return null;
                    }
                }
            });
        } catch (ParseException e) {
            System.err.println(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        lineChart.setCategoryDataset(categoryDataset);
        lineChart.getChartColor().addColor(Color.decode("#38bdf8"), Color.decode("#fb7185"), Color.decode("#34d399"));
        JLabel header = new JLabel("Xu hướng loại phòng");
        header.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:+1;"
                + "border:0,0,5,0");
        lineChart.setHeader(header);
    }

    private LineChartCustom lineChart;
}
