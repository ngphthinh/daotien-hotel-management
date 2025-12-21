package iuh.fit.se.group1.ui.component.chart;

import com.raven.chart.Chart;
import com.raven.chart.ModelChart;
import iuh.fit.se.group1.util.ChartUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class RevenueColumnChart extends JPanel {

    private Chart revenueColumnChart;

    public RevenueColumnChart() {
        initComponents();
        revenueColumnChart.addLegend("Phòng đơn", new Color(245, 189, 135));
        revenueColumnChart.addLegend("Phòng đôi", new Color(135, 189, 245));
        java.util.List<String> lbls = List.of("January","February","March","April","May");
        java.util.List<double[]> vals = List.of(new double[]{500, 200}, new double[]{600, 750}, new double[]{200, 350}, new double[]{480, 150}, new double[]{350, 540});
        setData(lbls, vals);
    }


    private void initComponents() {
        setLayout(new java.awt.BorderLayout());
        revenueColumnChart = new Chart();
        revenueColumnChart.setPreferredSize(new Dimension(500,500));
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        setBackground(Color.white);
        add(revenueColumnChart, java.awt.BorderLayout.CENTER);

    }

    /**
     * Set data for column chart
     * @param labels column labels
     * @param values column values, each double[] represents the values for each legend in order
     *               double[0] is single room revenue, double[1] is double room revenue
     */
    public void setData(java.util.List<String> labels , java.util.List<double[]> values) {
        // Tìm giá trị lớn nhất để tính maxValues
        double maxValue = 0;
        for (double[] val : values) {
            for (double v : val) {
                if (v > maxValue) {
                    maxValue = v;
                }
            }
        }

        double chartMaxValue = calculateChartMaxValue(maxValue);
        System.out.println("Recreating chart with scale: " + chartMaxValue + " (data max: " + maxValue + ")");

        // XÓA chart cũ và TẠO MỚI chart để đảm bảo scale được reset
        if (revenueColumnChart != null) {
            remove(revenueColumnChart);
        }

        // Tạo chart mới
        revenueColumnChart = new Chart();
        revenueColumnChart.setPreferredSize(new Dimension(500, 500));

        // Thêm lại legends
        revenueColumnChart.addLegend("Phòng đơn", new Color(245, 189, 135));
        revenueColumnChart.addLegend("Phòng đôi", new Color(135, 189, 245));

//        // Thêm data point ẩn để set scale
//        revenueColumnChart.addData(new ModelChart("", new double[]{chartMaxValue, 0}));

        // Add data thật
        for (int i = 0; i < labels.size(); i++) {
            revenueColumnChart.addData(new ModelChart(labels.get(i), values.get(i)));
        }

        // Thêm chart mới vào panel
        add(revenueColumnChart, BorderLayout.CENTER);

        // Force complete refresh
        revalidate();
        repaint();
    }

    /**
     * Tính toán giá trị max cho chart dựa trên giá trị lớn nhất trong data
     * Làm tròn lên để có độ chia đẹp
     */
    private double calculateChartMaxValue(double maxValue) {
        if (maxValue == 0) return 10_000_000; // Default nếu không có dữ liệu

        // Tìm bội số đẹp (1, 2, 5) * 10^n
        double magnitude = Math.pow(10, Math.floor(Math.log10(maxValue)));
        double normalized = maxValue / magnitude;

        double roundedNormalized;
        if (normalized <= 1) roundedNormalized = 1;
        else if (normalized <= 2) roundedNormalized = 2;
        else if (normalized <= 5) roundedNormalized = 5;
        else roundedNormalized = 10;

        double result = roundedNormalized * magnitude;

        // Đảm bảo result lớn hơn maxValue một chút để cột không chạm trần
        if (result <= maxValue) {
            result *= 1.2;
        }

        return result;
    }


}
