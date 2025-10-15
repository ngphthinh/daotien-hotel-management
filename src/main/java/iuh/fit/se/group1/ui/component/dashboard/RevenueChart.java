/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iuh.fit.se.group1.ui.component.dashboard;

import iuh.fit.se.group1.util.ChartUtils;
import raven.chart.data.pie.DefaultPieDataset;
import raven.chart.pie.PieChart;

import javax.swing.*;
import java.awt.*;

/**
 * @author ngphthinh
 */
public class RevenueChart extends JPanel {

    private PieChart pieChart;


    public RevenueChart() {
        initComponents();
        ChartUtils.setPieChartBackground(pieChart, Color.WHITE);


    }

    private void initComponents() {
        // Nền tổng thể
        setOpaque(true);
        setBackground(Color.white); // đổi nền ngoài

        pieChart = new PieChart();
        pieChart.setOpaque(false); // giữ trong suốt
        pieChart.setBorder(BorderFactory.createEmptyBorder());

        JLabel header3 = new JLabel("Nguồn doanh thu");
        header3.setFont(new Font("Segoe UI", Font.BOLD, 16));



        pieChart.setHeader(header3);


        pieChart.getChartColor().addColor(
                Color.decode("#a3e635"),
                Color.decode("#f87171"),
                Color.decode("#34d399")
        );
        pieChart.setChartType(PieChart.ChartType.DONUT_CHART);
        pieChart.setDataset(createPieData());

        setLayout(new BorderLayout());
        add(pieChart, BorderLayout.CENTER);
    }


    private DefaultPieDataset<String> createPieData() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.setValue("Tiền phòng", 40);
        dataset.setValue("Dịch vụ", 30);
        dataset.setValue("Phụ phí", 30);
        return dataset;
    }
}