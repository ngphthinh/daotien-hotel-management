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
    import java.awt.geom.RoundRectangle2D;

    /**
     * @author ngphthinh
     */
    public class RevenueChart extends JPanel {

        private PieChart pieChart;

        private int cornerRadius = 10;
        private int shadowSize = 4;
        private Color shadowColor = new Color(122, 122, 122, 60);
        public RevenueChart() {
            initComponents();
            ChartUtils.setPieChartBackground(pieChart, Color.WHITE);
            setOpaque(false);
//            setBorder(BorderFactory.c);
        }

        public int getCornerRadius() {
            return cornerRadius;
        }

        public void setCornerRadius(int cornerRadius) {
            this.cornerRadius = cornerRadius;
        }

        public int getShadowSize() {
            return shadowSize;
        }

        public void setShadowSize(int shadowSize) {
            this.shadowSize = shadowSize;
        }

        public Color getShadowColor() {
            return shadowColor;
        }

        public void setShadowColor(Color shadowColor) {
            this.shadowColor = shadowColor;
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
        @Override
        protected void paintComponent(Graphics g) {
            int width = getWidth() +30;
            int height = getHeight();

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Tạo clip chỉ cho phép vẽ bóng ở 3 phía (phải + dưới)
            Shape clip = new RoundRectangle2D.Double(0, 0, width, height, cornerRadius, cornerRadius);
            g2.setClip(clip);

            // Vẽ bóng ở phải & dưới (không trên)
            for (int i = shadowSize; i > 0; i--) {
                float opacity = (float) (shadowSize - i) / shadowSize;
                Color c = new Color(
                        shadowColor.getRed(),
                        shadowColor.getGreen(),
                        shadowColor.getBlue(),
                        (int) (opacity * shadowColor.getAlpha())
                );
                g2.setColor(c);

                // Dịch bóng về phải + xuống một chút
                g2.fillRoundRect(i + 3, i + 3, width - 2 * i, height - 2 * i, cornerRadius, cornerRadius);
            }

            // Vẽ nền panel chính
            g2.setClip(null);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, width - shadowSize, height - shadowSize, cornerRadius, cornerRadius);

            g2.dispose();
            super.paintComponent(g);
        }


        private DefaultPieDataset<String> createPieData() {
            DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
            dataset.setValue("Tiền phòng", 40);
            dataset.setValue("Dịch vụ", 30);
            dataset.setValue("Phụ phí", 30);
            return dataset;
        }
    }