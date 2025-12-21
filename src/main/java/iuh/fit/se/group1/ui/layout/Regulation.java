/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package iuh.fit.se.group1.ui.layout;


import iuh.fit.se.group1.ui.component.scroll.ScrollPaneWin11;

import javax.swing.*;
import java.awt.*;


/**
 *
 * @author Windows
 */
public class Regulation extends javax.swing.JPanel {

    public JEditorPane getEditorRegulation() {
        return editorRegulation;
    }

    public void setEditorRegulation(JEditorPane editorRegulation) {
        this.editorRegulation = editorRegulation;
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
        editorRegulation.setContentType("text/html");
        editorRegulation.setEditable(false);
        editorRegulation.setText(
                "<html>"
                        + "<body style='font-family:Segoe UI; font-size:13px; line-height:1.7; text-align:justify; width:1050px; padding:15px;'>"

                        + "<div style='text-align:center; margin-bottom:18px;'>"
                        + "<h2 style='color:#1565C0; margin:5px 0;'>QUY ĐỊNH SỬ DỤNG DỊCH VỤ KHÁCH SẠN ĐÀO TIÊN</h2>"
                        + "<p style='color:#666; font-size:12px; margin:3px 0;'>Áp dụng từ ngày 21/12/2025</p>"
                        + "</div>"

                        + "<h3 style='color:#1565C0; margin-top:15px; border-bottom:2px solid #1565C0; padding-bottom:5px;'>I. LOẠI HÌNH PHÒNG VÀ GIÁ</h3>"

                        + "<p><b>1. Loại phòng (Room Type)</b></p>"
                        + "<ul style='margin:5px 0 10px 20px;'>"
                        + "<li><b>Phòng đơn:</b> 1 giường đơn, phù hợp cho 2 người và 1 trẻ em</li>"
                        + "<li><b>Phòng đôi:</b> 1 giường đôi hoặc 2 giường đơn, phù hợp cho 4 người và 2 trẻ em</li>"
                        + "<li><b>Người dưới 12 tuổi là trẻ em.</b></li>"
                        + "</ul>"

                        + "<p><b>2. Hình thức đặt phòng (Booking Type)</b></p>"
                        + "<ul style='margin:5px 0 10px 20px;'>"
                        + "<li><b>Theo giờ:</b> Tính theo số giờ thuê (tối thiểu 1 giờ)</li>"
                        + "<li><b>Qua đêm:</b> Từ 20:00 đến 10:00 sáng hôm sau</li>"
                        + "<li><b>Theo ngày:</b> Theo số ngày, check-in 14:00, check-out 12:00</li>"
                        + "</ul>"

                        + "<p><b>3. Bảng giá tham khảo</b></p>"
                        + "<table style='margin:5px 0 10px 20px; border-collapse:collapse;'>"
                        + "<tr style='background:#E3F2FD;'><th style='border:1px solid #90CAF9; padding:6px 12px;'>Loại phòng</th><th style='border:1px solid #90CAF9; padding:6px 12px;'>Theo giờ</th><th style='border:1px solid #90CAF9; padding:6px 12px;'>Qua đêm</th><th style='border:1px solid #90CAF9; padding:6px 12px;'>Theo ngày</th></tr>"
                        + "<tr><td style='border:1px solid #90CAF9; padding:6px 12px;'>Phòng đơn</td><td style='border:1px solid #90CAF9; padding:6px 12px;'>50.000đ/giờ</td><td style='border:1px solid #90CAF9; padding:6px 12px;'>250.000đ</td><td style='border:1px solid #90CAF9; padding:6px 12px;'>300.000đ/ngày</td></tr>"
                        + "<tr><td style='border:1px solid #90CAF9; padding:6px 12px;'>Phòng đôi</td><td style='border:1px solid #90CAF9; padding:6px 12px;'>80.000đ/giờ</td><td style='border:1px solid #90CAF9; padding:6px 12px;'>300.000đ</td><td style='border:1px solid #90CAF9; padding:6px 12px;'>500.000đ/ngày</td></tr>"
                        + "</table>"

                        + "<h3 style='color:#1565C0; margin-top:18px; border-bottom:2px solid #1565C0; padding-bottom:5px;'>II. QUY ĐỊNH ĐẶT PHÒNG</h3>"

                        + "<p><b>4. Đặt phòng trước (Pre-booking)</b></p>"
                        + "<ul style='margin:5px 0 10px 20px;'>"
                        + "<li>Khách sạn nhận đặt phòng trước tối thiểu <b>1 giờ</b> so với thời gian nhận phòng dự kiến</li>"
                        + "<li>Cung cấp đầy đủ: <b>Họ tên, Số điện thoại, CCCD/CMND/Hộ chiếu, Giới Giới tính</b></li>"
                        + "<li>Trạng thái đặt phòng: <b>Đặt trước</b> (Pre-booking) → Chuyển sang <b>Đang xử lý</b> khi check-in</li>"
                        + "</ul>"


                        + "<h3 style='color:#1565C0; margin-top:18px; border-bottom:2px solid #1565C0; padding-bottom:5px;'>III. NHẬN VÀ TRẢ PHÒNG</h3>"

                        + "<p><b>5. Nhận phòng (Check-in)</b></p>"
                        + "<ul style='margin:5px 0 10px 20px;'>"
                        + "<li>Giờ nhận phòng tiêu chuẩn cho <b>DAILY</b>: <b>14:00</b></li>"
                        + "<li>Xuất trình <b>giấy tờ tùy thân gốc</b> (CCCD/CMND/Passport) khi làm thủ tục</li>"
                        + "<li>Trạng thái sau check-in: <b>Đang xử lý</b> (Processing)</li>"
                        + "</ul>"

                        + "<p><b>6. Trả phòng (Check-out)</b></p>"
                        + "<ul style='margin:5px 0 10px 20px;'>"
                        + "<li>Giờ trả phòng tiêu chuẩn cho <b>DAILY</b>: <b>12:00 trưa</b></li>"
                        + "<li>Nếu check-out trễ thì tính theo phụ phí(Processing)</li>"
                        + "<li>Kiểm tra tài sản và thanh toán đầy đủ trước khi rời khách sạn</li>"
                        + "<li>Trạng thái sau thanh toán: <b>Đã hoàn thành</b> (Completed)</li>"
                        + "</ul>"

                        + "<p><b>7. Gia hạn (Extend Booking)</b></p>"
                        + "<ul style='margin:5px 0 10px 20px;'>"
                        + "<li>Khách có thể gia hạn thêm giờ/ngày nếu phòng còn trống</li>"
                        + "<li>Giá gia hạn áp dụng theo bảng giá hiện hành</li>"
                        + "</ul>"

                        + "<h3 style='color:#1565C0; margin-top:18px; border-bottom:2px solid #1565C0; padding-bottom:5px;'>IV. PHỤ THU (SURCHARGE)</h3>"

                        + "<p><b>8. Các loại phụ thu</b></p>"
                        + "<ul style='margin:5px 0 10px 20px;'>"
                        + "<li><b>Phụ thu ngày lễ:</b> 50.000đ/phòng (Tết, lễ lớn)</li>"
                        + "<li>Phụ thu được cộng vào tổng hóa đơn khi thanh toán</li>"
                        + "</ul>"

                        + "<h3 style='color:#1565C0; margin-top:18px; border-bottom:2px solid #1565C0; padding-bottom:5px;'>V. GIẤY TỜ VÀ AN NINH</h3>"

                        + "<p><b>9. Yêu cầu giấy tờ tùy thân</b></p>"
                        + "<ul style='margin:5px 0 10px 20px;'>"
                        + "<li><b>Công dân Việt Nam:</b> CCCD/CMND/Hộ chiếu <b>gốc</b> còn hiệu lực</li>"
                        + "<li><b>Khách nước ngoài:</b> Hộ chiếu (Passport) <b>gốc</b> và Visa hợp lệ (nếu có)</li>"
                        + "<li>Thông tin được lưu trữ theo quy định của Công an địa phương</li>"
                        + "</ul>"

                        + "<h3 style='color:#1565C0; margin-top:18px; border-bottom:2px solid #1565C0; padding-bottom:5px;'>VI. NỘI QUY VÀ AN TOÀN</h3>"


                        + "<h3 style='color:#1565C0; margin-top:18px; border-bottom:2px solid #1565C0; padding-bottom:5px;'>VII. THANH TOÁN</h3>"

                        + "<p><b>12. Phương thức thanh toán (Payment Type)</b></p>"
                        + "<ul style='margin:5px 0 10px 20px;'>"
                        + "<li><b>Tiền mặt:</b> Thanh toán trực tiếp tại quầy lễ tân</li>"
                        + "<li><b>Chuyển khoản:</b> Chuyển khoản ngân hàng qua Momo</li>"
                        + "<li>Vui lòng <b>kiểm tra hóa đơn</b> trước khi rời khách sạn</li>"
                        + "</ul>"

                        + "<p><b>13. Trạng thái đơn hàng (Order Type)</b></p>"
                        + "<ul style='margin:5px 0 10px 20px;'>"
                        + "<li><b>Đặt trước (Pre-booking):</b> Đơn đã đặt nhưng chưa check-in</li>"
                        + "<li><b>Đang xử lý (Processing):</b> Khách đã check-in, đang lưu trú</li>"
                        + "<li><b>Đã hoàn thành (Completed):</b> Đã check-out và thanh toán đầy đủ</li>"
                        + "<li><b>Đã hủy (Cancelled):</b> Đơn đã bị hủy bởi khách hoặc khách sạn</li>"
                        + "</ul>"

                        + "<h3 style='color:#1565C0; margin-top:18px; border-bottom:2px solid #1565C0; padding-bottom:5px;'>VIII. DỊCH VỤ BỔ SUNG</h3>"

                        + "<hr style='margin:22px 0; border:none; border-top:1.5px solid #BDBDBD;'>"
                        + "</body></html>");

        editorRegulation.setCaretPosition(0); // Scroll to top

    }

    @SuppressWarnings("unchecked")

    private void initComponents() {

        headerShift1 = new iuh.fit.se.group1.ui.component.HeaderShift();
        scrollPane = new ScrollPaneWin11();
        editorRegulation = new javax.swing.JEditorPane();

        setBackground(new java.awt.Color(255, 255, 255));

        editorRegulation.setFont(new java.awt.Font("Segoe UI", Font.PLAIN, 13));
        editorRegulation.setBackground(new java.awt.Color(255, 255, 255));
        editorRegulation.setBorder(null);

        scrollPane.setViewportView(editorRegulation);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(headerShift1, javax.swing.GroupLayout.DEFAULT_SIZE, 1155, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1101, Short.MAX_VALUE)
                                .addGap(27, 27, 27)));
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerShift1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 673, Short.MAX_VALUE)
                .addGap(18, 18, 18))
        );
    }

    private iuh.fit.se.group1.ui.component.HeaderShift headerShift1;
    private ScrollPaneWin11 scrollPane;
    private javax.swing.JEditorPane editorRegulation;

}
