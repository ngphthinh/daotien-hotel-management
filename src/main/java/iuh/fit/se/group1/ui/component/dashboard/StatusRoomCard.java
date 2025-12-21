    package iuh.fit.se.group1.ui.component.dashboard;

    import com.formdev.flatlaf.FlatClientProperties;
    import iuh.fit.se.group1.dto.RoomStatusDto;
    import iuh.fit.se.group1.util.ChartUtils;
    import raven.chart.bar.HorizontalBarChart;
    import raven.chart.data.pie.DefaultPieDataset;

    import javax.swing.*;
    import java.awt.*;
    import java.util.Random;

    public class StatusRoomCard extends JPanel {
        private HorizontalBarChart roomStatusChart;

        public StatusRoomCard() {
            initComponents();
            ChartUtils.setHorizontalBar(roomStatusChart, Color.white);
            // Tắt currency format - hiển thị số nguyên
            ChartUtils.setHorizontalBarIntegerFormat(roomStatusChart);
        }



        private void initComponents() {

            roomStatusChart = new HorizontalBarChart();
            this.setBackground(Color.white);
            roomStatusChart.setPreferredSize(new Dimension(380, 260));
            roomStatusChart.setFont(roomStatusChart.getFont().deriveFont(16f));

            JLabel header2 = new JLabel("Biểu đồ trạng thái phòng");
            header2.setFont(header2.getFont().deriveFont(20f));
            roomStatusChart.setHeader(header2);
            roomStatusChart.setBarColor(Color.decode("#10b981"));
            roomStatusChart.setDataset(createData());

            JPanel panel2 = new JPanel(new BorderLayout());
            panel2.putClientProperty(FlatClientProperties.STYLE, ""
                    + "border:5,5,5,5,$Component.borderColor,,20");
            panel2.add(roomStatusChart);
            add(panel2);
        }

        private DefaultPieDataset createData() {
            DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
            Random random = new Random();
            dataset.addValue("Phòng đã đặt", random.nextInt(20));
            dataset.addValue("Phòng trống", random.nextInt(20));
            dataset.addValue("Trả phòng", random.nextInt(20));
            dataset.addValue("Hủy phòng", random.nextInt(20));
            return dataset;
        }

        /**
         * Cập nhật dữ liệu trạng thái phòng từ service
         */
        public void updateData(RoomStatusDto statusDto) {
            DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
            dataset.addValue("Phòng đã đặt", statusDto.getOccupiedRooms());
            dataset.addValue("Phòng trống", statusDto.getAvailableRooms());
            dataset.addValue("Trả phòng", statusDto.getCheckedOutRooms());
            dataset.addValue("Hủy phòng", statusDto.getCancelledBookings());

            roomStatusChart.setDataset(dataset);
            roomStatusChart.repaint();
        }
    }
