/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iuh.fit.se.group1.ui.component.dashboard;


import iuh.fit.se.group1.dto.PeakHourDto;
import iuh.fit.se.group1.ui.component.raven.chart.CurveLineChart;
import iuh.fit.se.group1.ui.component.raven.chart.ModelChart;
import iuh.fit.se.group1.ui.component.raven.panel.PanelShadow;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LineChartPanel extends PanelShadow {


    /**
     * Creates new form Test
     */
    public LineChartPanel() {
        initComponents();
        chart.setTitle("Khung giờ cao điểm");
        chart.addLegend("Amount", Color.decode("#7b4397"), Color.decode("#dc2430"));
        test();
    }

    private void initComponents() {

        chart = new CurveLineChart();


        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        chart.setForeground(new java.awt.Color(0, 0, 0));
        chart.setFillColor(true);

        setLayout(new BorderLayout());
        add(chart, BorderLayout.CENTER);
    }

    private void setData() {
        try {
            chart.clear();
            List<ModelData> lists = new ArrayList<>();

            // Hardcode dữ liệu giả lập 7 tháng gần nhất
            lists.add(new ModelData("March", 12));
            lists.add(new ModelData("April", 15));
            lists.add(new ModelData("May", 18));
            lists.add(new ModelData("June", 20));
            lists.add(new ModelData("July", 22));
            lists.add(new ModelData("August", 25));
            lists.add(new ModelData("September", 20));

            //  Add Data to chart
            for (int i = lists.size() - 1; i >= 0; i--) {
                ModelData d = lists.get(i);
                chart.addData(new ModelChart(d.getLabel(), new double[]{d.getQuantity()}));
            }

            //  Start to show data with animation
            chart.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void test() {
        chart.clear();
        chart.addData(new ModelChart("10:00", new double[]{50}));
        chart.addData(new ModelChart("16:00", new double[]{60}));
        chart.addData(new ModelChart("20:00", new double[]{20}));
        chart.addData(new ModelChart("4:00", new double[]{48}));
        chart.addData(new ModelChart("10:00", new double[]{35}));
        chart.start();
    }

    /**
     * Cập nhật dữ liệu khung giờ cao điểm từ service
     */
    public void updateData(List<PeakHourDto> peakHours) {
        chart.clear();

        if (peakHours == null || peakHours.isEmpty()) {
            // Thêm dữ liệu mặc định (cần ít nhất 4 điểm cho spline)
            chart.addData(new ModelChart("00:00", new double[]{0}));
            chart.addData(new ModelChart("06:00", new double[]{0}));
            chart.addData(new ModelChart("12:00", new double[]{0}));
            chart.addData(new ModelChart("18:00", new double[]{0}));
        } else {
            // Thêm dữ liệu thật
            for (PeakHourDto peak : peakHours) {
                chart.addData(new ModelChart(peak.getHour(), new double[]{peak.getBookingCount()}));
            }

            // Đảm bảo có ít nhất 4 điểm cho spline interpolation
            int dataSize = peakHours.size();
            if (dataSize < 4) {
                // Thêm các điểm padding với giá trị 0
                for (int i = dataSize; i < 4; i++) {
                    chart.addData(new ModelChart("--:--", new double[]{0}));
                }
            }
        }

        chart.start();
    }

    private CurveLineChart chart;

}
