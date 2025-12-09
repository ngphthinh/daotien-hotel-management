/**
 * @ (#) ExtendBookingModal.java   1.0     9/12/2025
 * <p>
 * Copyright (c) 2025 IUH. All rights reserved
 */
package iuh.fit.se.group1.ui.component.modal;
import iuh.fit.se.group1.entity.Order;
import iuh.fit.se.group1.entity.Room;
import iuh.fit.se.group1.enums.BookingType;
import iuh.fit.se.group1.service.RoomToolsService;
import iuh.fit.se.group1.ui.component.custom.message.CustomDialog;
import raven.glasspanepopup.GlassPanePopup;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description
 * @author: Nguyen Tran Quoc Viet
 * @version: 1.0
 * @created: 9/12/2025
 */

public class ExtendBookingModal extends JPanel {
    private final RoomToolsService service;
    private final Order currentBooking;
    private final List<Room> roomsToExtend;
    private final BookingType bookingType;
    private final Runnable onSuccess;
    private final Runnable onCancel;
    private JLabel totalAmountLabel;
    private JSpinner extendSpinner;
    private JComboBox<String> extendUnitCombo;
    private JPanel roomListPanel;
    private JButton confirmBtn;
    private JButton cancelBtn;

    public ExtendBookingModal(Order currentBooking, List<Room> roomsToExtend,
                              BookingType bookingType, Runnable onSuccess, Runnable onCancel) {
        this.service = new RoomToolsService();
        this.currentBooking = currentBooking;
        this.roomsToExtend = roomsToExtend;
        this.bookingType = bookingType;
        this.onSuccess = onSuccess;
        this.onCancel = onCancel;

        initComponents();
        setupLayout();
        calculateInitialAmount();
    }

    private void initComponents() {
        setBackground(Color.WHITE);
        setBorder(new LineBorder(new Color(229, 231, 235), 1, true));
        setPreferredSize(new Dimension(550, 650));

        roomListPanel = new JPanel();
        roomListPanel.setLayout(new BoxLayout(roomListPanel, BoxLayout.Y_AXIS));
        roomListPanel.setBackground(Color.WHITE);
        extendUnitCombo = new JComboBox<>();
        extendUnitCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        extendUnitCombo.addActionListener(e -> calculateInitialAmount());
        SpinnerModel spinnerModel = new SpinnerNumberModel(1, 1, 100, 1);
        extendSpinner = new JSpinner(spinnerModel);
        extendSpinner.setFont(new Font("Segoe UI", Font.BOLD, 14));
        ((JSpinner.DefaultEditor) extendSpinner.getEditor()).getTextField().setColumns(5);
        extendSpinner.addChangeListener(e -> calculateInitialAmount());
        totalAmountLabel = new JLabel("0đ");
        totalAmountLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalAmountLabel.setForeground(new Color(220, 38, 38));
        confirmBtn = createStyledButton("Xác nhận gia hạn", new Color(16, 185, 129));
        cancelBtn = createStyledButton("Hủy", new Color(156, 163, 175));
        confirmBtn.addActionListener(e -> handleConfirm());
        cancelBtn.addActionListener(e -> {
            if (onCancel != null) onCancel.run();
            GlassPanePopup.closePopup("ExtendBookingModal");
        });
        setupExtendUnits();
        updateRoomInfo();
    }

    private void setupExtendUnits() {
        extendUnitCombo.removeAllItems();

        switch (bookingType) {
            case HOURLY:
                extendUnitCombo.addItem("Giờ");
                break;
            case DAILY:
                extendUnitCombo.addItem("Ngày");
                break;
            case OVERNIGHT:
                extendUnitCombo.addItem("Không hỗ trợ");
                extendUnitCombo.setEnabled(false);
                extendSpinner.setEnabled(false);
                confirmBtn.setEnabled(false);
                break;
        }
    }

    private void setupLayout() {
        setLayout(new BorderLayout(0, 0));

        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = createContentPanel();
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(16, 185, 129));
        panel.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel titleLabel = new JLabel("GIA HẠN PHÒNG");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Chọn thời gian gia hạn và xác nhận");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(220, 252, 231));

        panel.setLayout(new GridLayout(2, 1, 0, 5));
        panel.add(titleLabel);
        panel.add(subtitleLabel);

        return panel;
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(createCustomerInfoSection());
        panel.add(Box.createVerticalStrut(15));
        panel.add(createRoomListSection());
        panel.add(Box.createVerticalStrut(15));
        panel.add(createBookingTypeSection());
        panel.add(Box.createVerticalStrut(15));
        if (bookingType != BookingType.OVERNIGHT) {
            panel.add(createExtendInputPanel());
            panel.add(Box.createVerticalStrut(15));
        } else {
            panel.add(createOvernightWarningPanel());
            panel.add(Box.createVerticalStrut(12));
        }

        panel.add(createTotalAmountPanel());

        // Thêm khoảng trống để cuộn
        panel.add(Box.createVerticalStrut(20));

        return panel;
    }

    private JPanel createCustomerInfoSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(249, 250, 251));
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(229, 231, 235), 1, true),
                new EmptyBorder(12, 12, 12, 12)));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));

        JLabel headerLabel = new JLabel("THÔNG TIN KHÁCH HÀNG");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        headerLabel.setForeground(new Color(107, 114, 128));
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 6));
        infoPanel.setOpaque(false);
        infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nameLabel = new JLabel("Tên: " + currentBooking.getCustomer().getFullName());
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        nameLabel.setForeground(new Color(31, 41, 55));

        JLabel cccdLabel = new JLabel("CCCD: " + currentBooking.getCustomer().getCitizenId());
        cccdLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cccdLabel.setForeground(new Color(31, 41, 55));

        infoPanel.add(nameLabel);
        infoPanel.add(cccdLabel);

        panel.add(headerLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(infoPanel);

        return panel;
    }

    private JPanel createRoomListSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.setBackground(Color.WHITE);
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel headerLabel = new JLabel("DANH SÁCH PHÒNG GIA HẠN:");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        headerLabel.setForeground(new Color(107, 114, 128));

        headerPanel.add(headerLabel);

        section.add(headerPanel);
        section.add(Box.createVerticalStrut(8));

        // Room container
        JPanel roomContainer = new JPanel();
        roomContainer.setLayout(new BoxLayout(roomContainer, BoxLayout.Y_AXIS));
        roomContainer.setBackground(Color.WHITE);
        roomContainer.setBorder(new LineBorder(new Color(229, 231, 235), 1, true));
        roomContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        roomContainer.add(roomListPanel);

        section.add(roomContainer);

        return section;
    }

    private JPanel createBookingTypeSection() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(254, 249, 195));
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(250, 204, 21), 2, true),
                new EmptyBorder(10, 12, 10, 12)));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        JLabel label = new JLabel("Loại thuê: " + bookingType.getDisplayName());
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(146, 64, 14));

        panel.add(label, BorderLayout.WEST);

        return panel;
    }

    private JPanel createExtendInputPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(243, 244, 246));
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 3));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));

        JLabel label = new JLabel("Thời gian gia hạn:");
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));

        extendSpinner.setPreferredSize(new Dimension(75, 32));
        extendUnitCombo.setPreferredSize(new Dimension(95, 32));

        panel.add(label);
        panel.add(extendSpinner);
        panel.add(extendUnitCombo);

        return panel;
    }

    private JPanel createOvernightWarningPanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setBackground(new Color(254, 242, 242));
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(239, 68, 68), 2, true),
                new EmptyBorder(12, 12, 12, 12)));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel iconLabel = new JLabel("⚠");
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        iconLabel.setForeground(new Color(239, 68, 68));

        JLabel messageLabel = new JLabel(
                "<html><b>Không hỗ trợ gia hạn cho thuê qua đêm</b><br>" +
                        "Vui lòng tạo booking mới nếu muốn tiếp tục thuê phòng</html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        messageLabel.setForeground(new Color(127, 29, 29));

        panel.add(iconLabel, BorderLayout.WEST);
        panel.add(messageLabel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTotalAmountPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(254, 252, 232));
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(250, 204, 21), 2, true),
                new EmptyBorder(15, 18, 15, 18)));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));

        JLabel label = new JLabel("Tổng tiền gia hạn:");
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        label.setForeground(new Color(120, 53, 15));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setOpaque(false);
        leftPanel.add(label);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(totalAmountLabel);

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 30, 25, 30));

        panel.add(confirmBtn);
        panel.add(cancelBtn);

        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setPreferredSize(new Dimension(165, 40));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void updateRoomInfo() {
        roomListPanel.removeAll();
        roomListPanel.setBorder(new EmptyBorder(8, 8, 8, 8));

        for (int i = 0; i < roomsToExtend.size(); i++) {
            Room room = roomsToExtend.get(i);
            long currentPrice = service.getRoomPriceWithDuration(
                    room, bookingType, currentBooking.getOrderId());

            JPanel roomCard = createRoomCard(room, currentPrice, i);
            roomListPanel.add(roomCard);

            if (i < roomsToExtend.size() - 1) {
                roomListPanel.add(Box.createVerticalStrut(8));
            }
        }

        roomListPanel.revalidate();
        roomListPanel.repaint();
    }

    private JPanel createRoomCard(Room room, long currentPrice, int index) {
        JPanel card = new JPanel(new BorderLayout(12, 0));
        card.setBackground(new Color(249, 250, 251));
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(229, 231, 235), 1, true),
                new EmptyBorder(10, 12, 10, 12)));
        card.setPreferredSize(new Dimension(490, 55));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setOpaque(false);

        JLabel roomNumberLabel = new JLabel("Phòng " + room.getRoomNumber());
        roomNumberLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        roomNumberLabel.setForeground(new Color(31, 41, 55));

        leftPanel.add(roomNumberLabel);
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        centerPanel.setOpaque(false);

        JLabel typeLabel = new JLabel(room.getRoomType().getName());
        typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        typeLabel.setForeground(new Color(107, 114, 128));

        centerPanel.add(typeLabel);
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setOpaque(false);

        JLabel priceLabel = new JLabel(String.format("%,dđ", currentPrice));
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        priceLabel.setForeground(new Color(220, 38, 38));

        rightPanel.add(priceLabel);
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setOpaque(false);
        infoPanel.add(centerPanel, BorderLayout.CENTER);
        infoPanel.add(rightPanel, BorderLayout.EAST);

        card.add(leftPanel, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);

        return card;
    }

    private void calculateInitialAmount() {
        if (bookingType == BookingType.OVERNIGHT) {
            totalAmountLabel.setText("N/A");
            totalAmountLabel.setForeground(new Color(156, 163, 175));
            return;
        }

        int extendValue = (Integer) extendSpinner.getValue();
        BigDecimal totalAmount = service.calculateExtensionAmount(
                roomsToExtend, bookingType, extendValue);

        totalAmountLabel.setText(String.format("%,dđ", totalAmount.longValue()));
        totalAmountLabel.setForeground(new Color(220, 38, 38));
    }

    private void handleConfirm() {
        if (bookingType == BookingType.OVERNIGHT) {
            showOvernightError();
            return;
        }

        int extendValue = (Integer) extendSpinner.getValue();

        String unit = bookingType == BookingType.HOURLY ? "giờ" : "ngày";
        BigDecimal amount = service.calculateExtensionAmount(
                roomsToExtend, bookingType, extendValue);

        String message = String.format(
                "<div style='line-height: 1.8;'>" +
                        "<b style='font-size: 15px;'>Xác nhận gia hạn phòng?</b><br><br>" +
                        "• Thời gian: <b>%d %s</b><br>" +
                        "• Phòng: <b>%s</b><br>" +
                        "• Số tiền gia hạn: <b style='color: #DC2626;'>+%,dđ</b><br><br>" +
                        "<b>Hóa đơn cũ:</b> %,dđ<br>" +
                        "<b>Hóa đơn mới:</b> <span style='color: #DC2626; font-size: 15px;'>%,dđ</span><br><br>" +
                        "<i style='color: #6B7280; font-size: 12px;'>Số tiền sẽ được cộng vào hóa đơn</i>" +
                        "</div>",
                extendValue,
                unit,
                roomsToExtend.stream()
                        .map(Room::getRoomNumber)
                        .collect(Collectors.joining(", ")),
                amount.longValue(),
                currentBooking.getTotalAmount().longValue(),
                currentBooking.getTotalAmount().longValue() + amount.longValue()
        );

        int confirm = CustomDialog.showConfirm(
                this,
                message,
                "Xác nhận gia hạn",
                CustomDialog.MessageType.INFO,
                500,
                400
        );

        if (confirm == JOptionPane.OK_OPTION) {
            performExtension(extendValue);
        }
    }

    private void performExtension(int extendValue) {
        try {
            boolean success = service.extendRoomBooking(
                    currentBooking.getOrderId(),
                    roomsToExtend,
                    extendValue,
                    bookingType
            );

            if (success) {
                String unit = bookingType == BookingType.HOURLY ? "giờ" : "ngày";
                BigDecimal extensionAmount = service.calculateExtensionAmount(
                        roomsToExtend, bookingType, extendValue);

                long oldTotal = currentBooking.getTotalAmount().longValue();
                long newTotal = oldTotal + extensionAmount.longValue();

                String successMsg = String.format(
                        "<div style='line-height: 1.8;'>" +
                                "<b style='color: #10B981; font-size: 16px;'>✓ GIA HẠN THÀNH CÔNG!</b><br><br>" +
                                "Phòng: <b>%s</b><br>" +
                                "Đã gia hạn: <b>%d %s</b><br>" +
                                "Số tiền gia hạn: <b style='color: #DC2626;'>+%,dđ</b><br>" +
                                "<hr style='border: 1px solid #E5E7EB; margin: 15px 0;'>" +
                                "Tổng hóa đơn cũ: <b>%,dđ</b><br>" +
                                "Tổng hóa đơn mới: <b style='color: #DC2626; font-size: 18px;'>%,dđ</b>" +
                                "</div>",
                        roomsToExtend.stream()
                                .map(Room::getRoomNumber)
                                .collect(Collectors.joining(", ")),
                        extendValue,
                        unit,
                        extensionAmount.longValue(),
                        oldTotal,
                        newTotal
                );

                CustomDialog.showMessage(
                        this,
                        successMsg,
                        "Thành công",
                        CustomDialog.MessageType.SUCCESS,
                        480,
                        380
                );

                if (onSuccess != null) onSuccess.run();
                GlassPanePopup.closePopup("ExtendBookingModal");
            } else {
                CustomDialog.showMessage(
                        this,
                        "<b style='color: #DC2626;'>❌ Không thể gia hạn phòng!</b><br><br>Vui lòng thử lại sau.",
                        "Lỗi",
                        CustomDialog.MessageType.ERROR,
                        400,
                        250
                );
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            CustomDialog.showMessage(
                    this,
                    "<b style='color: #DC2626;'>Lỗi khi gia hạn:</b><br><br>" + ex.getMessage(),
                    "Lỗi",
                    CustomDialog.MessageType.ERROR,
                    450,
                    280
            );
        }
    }

    private void showOvernightError() {
        CustomDialog.showMessage(
                this,
                "<b style='font-size: 15px;'>⚠️ Không hỗ trợ gia hạn cho thuê qua đêm</b><br><br>" +
                        "Loại thuê qua đêm không thể gia hạn.<br>" +
                        "Vui lòng tạo booking mới nếu muốn thuê tiếp.",
                "Không hỗ trợ",
                CustomDialog.MessageType.WARNING,
                450,
                280
        );
    }
}