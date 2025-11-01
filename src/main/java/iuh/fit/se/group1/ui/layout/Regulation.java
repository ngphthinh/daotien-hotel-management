/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.layout;


import javax.swing.JLabel;


/**
 *
 * @author Windows
 */
public class Regulation extends javax.swing.JPanel {

    public JLabel getLblRegulation() {
        return lblRegulation;
    }

    public void setLblRegulation(JLabel lblRegulation) {
        this.lblRegulation = lblRegulation;
    }

    public Regulation() {
        initComponents();
        headerShift1.getLblTile().setText(
                "<html>"
                        + "<span style='font-size:20px;color:white;'>Quy Định - Đào Tiên Hotel</span><br>"
                        + "<span style='font-size:15px;color:white;'>Môn học: Phát triển Ứng dụng – Khoa CNTT, IUH</span><br>"
                        + "<span style='font-size:15px;color:white;'>GV hướng dẫn: Trần Thị Anh Thi | © 2025 Nhóm 1</span>"
                        + "</html>");

        headerShift1.getLblSubTitle().setText("");
        lblRegulation.setText(
                "<html>"
                        + "<body style='font-family:Segoe UI; font-size:14px; line-height:1.5; text-align:justify; width:600px;'>"

                        + "<b>QUY ĐỊNH SỬ DỤNG DỊCH VỤ KHÁCH SẠN</b><br><br>"

                        + "<b>1. Đặt phòng</b><br>"
                        + "- Khách sạn nhận đặt phòng trước tối thiểu <b>2 giờ</b>.<br>"
                        + "- Khách vui lòng cung cấp thông tin đầy đủ để xác nhận đặt phòng.<br><br>"

                        + "<b>2. Hủy phòng</b><br>"
                        + "- Hủy trước <b>24h</b> → hoàn <b>50%</b> tiền cọc.<br>"
                        + "- Hủy trong vòng <b>24h</b> → <b>không hoàn cọc</b>.<br><br>"

                        + "<b>3. Nhận phòng (Check-in)</b><br>"
                        + "- Giờ nhận phòng: <b>14:00</b>.<br>"
                        + "- Đến trễ hơn <b>20% thời lượng thuê</b> mà không báo → <b>hủy phòng</b>.<br><br>"

                        + "<b>4. Trả phòng (Check-out)</b><br>"
                        + "- Giờ trả phòng: <b>12:00</b>.<br>"
                        + "- Trễ 1 giờ → phụ thu theo bảng giá.<br><br>"

                        + "<b>5. Giấy tờ tùy thân</b><br>"
                        + "-Đối với công dân Việt Nam, yêu cầu xuất trình <b>CCCD/CMND/Hộ chiếu gốc</b> khi nhận phòng.<br><br>"
                        + "-Đối với người nước ngoài, yêu cầu xuất trình <b>Hộ chiếu gốc</b> khi nhận phòng.<br><br>"

                        + "<b>6. Quy định chung</b><br>"
                        + "- Không gây ồn sau <b>22:00</b>.<br>"
                        + "- Không hút thuốc trong phòng.<br>"
                        + "- Bồi thường nếu làm hỏng tài sản.<br>"

                        + "</body></html>");

    }

    @SuppressWarnings("unchecked")

    private void initComponents() {

        headerShift1 = new iuh.fit.se.group1.ui.component.HeaderShift();
        lblRegulation = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        lblRegulation.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblRegulation.setText("jLabel1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(headerShift1, javax.swing.GroupLayout.DEFAULT_SIZE, 1155, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(lblRegulation, javax.swing.GroupLayout.PREFERRED_SIZE, 651,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerShift1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67)
                .addComponent(lblRegulation, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );
    }
    private iuh.fit.se.group1.ui.component.HeaderShift headerShift1;
    private javax.swing.JLabel lblRegulation;

}
