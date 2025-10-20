package iuh.fit.se.group1.ui.component.chart;

import com.raven.chart.Chart;
import com.raven.chart.ModelChart;
import iuh.fit.se.group1.ui.swing.Login;
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
        ChartUtils.clearDataColumnChart(revenueColumnChart);
        for (int i = 0; i < labels.size(); i++) {
            revenueColumnChart.addData(new ModelChart(labels.get(i), values.get(i)));
        }
        revenueColumnChart.repaint();
    }

}
