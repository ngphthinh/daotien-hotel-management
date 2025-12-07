package iuh.fit.se.group1.ui.layout;

import iuh.fit.se.group1.entity.Room;
import iuh.fit.se.group1.enums.BookingType;
import iuh.fit.se.group1.repository.TransferRoomRepository.BookingDTO;
import iuh.fit.se.group1.service.TransferRoomService;
import iuh.fit.se.group1.ui.component.custom.message.CustomDialog;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class RoomTransferUI extends JPanel {
    private JPanel mainPanel;
    private JTextField searchField;
    private JTable bookingsTable;
    private DefaultTableModel bookingsModel;
    private JTable currentRoomsTable;
    private DefaultTableModel currentRoomsModel;
    private JTable newRoomsTable;
    private DefaultTableModel newRoomsModel;
    private JTextField totalSurchargeField;
    private JButton removeRoomBtn, clearAllBtn;
    private JLabel bookingTypeLabel;

    private final TransferRoomService service;

    private List<BookingDTO> bookings = new ArrayList<>();
    private Map<String, Room> currentRoomsMap = new HashMap<>();
    private List<Room> selectedOldRooms = new ArrayList<>();
    private List<Room> selectedNewRooms = new ArrayList<>();
    private BookingDTO currentBooking = null;
    private BookingType currentBookingType = null;

    public RoomTransferUI() {
        setLayout(new BorderLayout());
        this.service = new TransferRoomService();
        initComponents();
        loadBookingsFromDatabase();
        setVisible(true);
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(new Color(245, 247, 250));

        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout(20, 0));
        contentPanel.setBackground(new Color(245, 247, 250));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel leftPanel = createBookingsPanel();
        leftPanel.setPreferredSize(new Dimension(450, 0));
        contentPanel.add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = createTransferPanel();
        contentPanel.add(rightPanel, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(108, 165, 200));
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        JLabel titleLabel = new JLabel("CHUYỂN PHÒNG KHÁCH SẠN");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Quản lý chuyển phòng cho khách hàng");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(219, 234, 254));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 5));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);

        header.add(titlePanel, BorderLayout.WEST);
        return header;
    }

    private JPanel createBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(229, 231, 235), 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JLabel titleLabel = new JLabel("Danh sách đặt phòng");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(31, 41, 55));

        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(Color.WHITE);

        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(209, 213, 219), 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        searchField.setToolTipText("Nhập CCCD để tìm kiếm (tự động tìm khi gõ)");

        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                searchBooking();
            }
        });

        searchPanel.add(searchField, BorderLayout.CENTER);

        String[] columns = { "CCCD", "Khách hàng", "Phòng", "Loại thuê" };
        bookingsModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bookingsTable = new JTable(bookingsModel);
        styleTable(bookingsTable);
        bookingsTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        bookingsTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        bookingsTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        bookingsTable.getColumnModel().getColumn(3).setPreferredWidth(90);

        bookingsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && bookingsTable.getSelectedRow() != -1) {
                loadSelectedBooking();
            }
        });

        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        scrollPane.setBorder(new LineBorder(new Color(229, 231, 235), 1));

        JPanel topPanel = new JPanel(new BorderLayout(0, 15));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTransferPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(229, 231, 235), 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JLabel titleLabel = new JLabel("Thông tin chuyển phòng");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(31, 41, 55));

        JPanel bookingTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        bookingTypePanel.setBackground(new Color(254, 243, 199));
        bookingTypePanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(251, 191, 36), 2, true),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        bookingTypeLabel = new JLabel("Chưa chọn phòng - Vui lòng chọn phòng để bắt đầu");
        bookingTypeLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        bookingTypeLabel.setForeground(new Color(146, 64, 14));
        bookingTypePanel.add(bookingTypeLabel);

        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setBackground(Color.WHITE);

        contentPanel.add(createOldRoomsPanel());
        contentPanel.add(createNewRoomsPanel());

        JPanel bottomPanel = createBottomPanel();

        JPanel topSection = new JPanel(new BorderLayout(0, 10));
        topSection.setBackground(Color.WHITE);
        topSection.add(titleLabel, BorderLayout.NORTH);
        topSection.add(bookingTypePanel, BorderLayout.CENTER);

        panel.add(topSection, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createOldRoomsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel("Phòng hiện tại");
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        label.setForeground(new Color(55, 65, 81));

        String[] columns = { "Phòng", "Loại", "Giá" };
        currentRoomsModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        currentRoomsTable = new JTable(currentRoomsModel) {
            @Override
            public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
                if (isRowSelected(rowIndex)) {
                    getSelectionModel().removeSelectionInterval(rowIndex, rowIndex);
                } else {
                    getSelectionModel().addSelectionInterval(rowIndex, rowIndex);
                }
            }
        };

        styleTable(currentRoomsTable);
        currentRoomsTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scrollPane = new JScrollPane(currentRoomsTable);
        scrollPane.setBorder(new LineBorder(new Color(229, 231, 235), 1));
        scrollPane.setPreferredSize(new Dimension(0, 200));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton selectBtn = createStyledButton("Chọn phòng cần đổi", new Color(59, 130, 246), 180, 35);
        selectBtn.addActionListener(e -> selectOldRooms());
        buttonPanel.add(selectBtn);

        panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createNewRoomsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        JLabel label = new JLabel("Phòng mới");
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        label.setForeground(new Color(55, 65, 81));

        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        typePanel.setBackground(Color.WHITE);

        JButton singleBtn = createStyledButton("Phòng đơn", new Color(99, 102, 241), 100, 30);
        JButton doubleBtn = createStyledButton("Phòng đôi", new Color(139, 92, 246), 100, 30);

        singleBtn.addActionListener(e -> showAvailableRooms("SINGLE"));
        doubleBtn.addActionListener(e -> showAvailableRooms("DOUBLE"));

        typePanel.add(singleBtn);
        typePanel.add(doubleBtn);

        headerPanel.add(label, BorderLayout.WEST);
        headerPanel.add(typePanel, BorderLayout.EAST);

        String[] columns = { "Phòng", "Loại", "Giá" };
        newRoomsModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        newRoomsTable = new JTable(newRoomsModel);
        styleTable(newRoomsTable);

        JScrollPane scrollPane = new JScrollPane(newRoomsTable);
        scrollPane.setBorder(new LineBorder(new Color(229, 231, 235), 1));
        scrollPane.setPreferredSize(new Dimension(0, 200));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        buttonPanel.setBackground(Color.WHITE);

        removeRoomBtn = createStyledButton("Xóa phòng", new Color(239, 68, 68), 120, 35);
        clearAllBtn = createStyledButton("Xóa tất cả", new Color(156, 163, 175), 100, 35);

        removeRoomBtn.addActionListener(e -> removeNewRoom());
        clearAllBtn.addActionListener(e -> clearNewRooms());

        buttonPanel.add(removeRoomBtn);
        buttonPanel.add(clearAllBtn);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(Color.WHITE);

        JPanel summaryPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        summaryPanel.setBackground(new Color(254, 252, 232));
        summaryPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(250, 204, 21), 2, true),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        row1.setBackground(new Color(254, 252, 232));

        JLabel oldRoomsLabel = new JLabel("Phòng cũ: 0 phòng");
        oldRoomsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        oldRoomsLabel.setName("oldRoomsLabel");

        JLabel newRoomsLabel = new JLabel("Phòng mới: 0 phòng");
        newRoomsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        newRoomsLabel.setName("newRoomsLabel");

        row1.add(oldRoomsLabel);
        row1.add(new JLabel("→"));
        row1.add(newRoomsLabel);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row2.setBackground(new Color(254, 252, 232));

        JLabel surchargeLabel = new JLabel("Tổng phụ phí: ");
        surchargeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        totalSurchargeField = new JTextField(20);
        totalSurchargeField.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalSurchargeField.setForeground(new Color(220, 38, 38));
        totalSurchargeField.setEditable(false);
        totalSurchargeField.setBackground(Color.WHITE);
        totalSurchargeField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(252, 165, 165), 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        totalSurchargeField.setText("0đ");

        row2.add(surchargeLabel);
        row2.add(totalSurchargeField);

        summaryPanel.add(row1);
        summaryPanel.add(row2);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton cancelBtn = createStyledButton("Hủy bỏ", new Color(156, 163, 175), 120, 40);
        JButton confirmBtn = createStyledButton("Xác nhận chuyển", new Color(16, 185, 129), 150, 40);

        cancelBtn.addActionListener(e -> resetForm());
        confirmBtn.addActionListener(e -> performTransfer());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(confirmBtn);

        panel.add(summaryPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor, int width, int height) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(bgColor.darker().darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(bgColor.darker());
                } else {
                    g2.setColor(bgColor);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();

                super.paintComponent(g);
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(width, height));
        button.setOpaque(false);

        return button;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(32);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(243, 244, 246));
        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionForeground(new Color(31, 41, 55));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(249, 250, 251));
        header.setForeground(new Color(55, 65, 81));
        header.setPreferredSize(new Dimension(0, 35));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void loadBookingsFromDatabase() {
        bookings = service.getAllBookings();
        loadBookingsToTable();
    }

    private void loadBookingsToTable() {
        bookingsModel.setRowCount(0);
        for (BookingDTO booking : bookings) {
            bookingsModel.addRow(new Object[] {
                    booking.cccd,
                    booking.guestName,
                    booking.rooms,
                    service.getBookingTypeDisplay(booking.bookingType)
            });
        }
    }

    private void loadSelectedBooking() {
        int row = bookingsTable.getSelectedRow();
        if (row == -1)
            return;

        currentBooking = bookings.get(row);
        currentBookingType = currentBooking.bookingType;

        String typeDisplay = service.getBookingTypeDisplay(currentBookingType);
        bookingTypeLabel.setText(String.format(
                "%s (%s) - Loại thuê: %s",
                currentBooking.guestName,
                currentBooking.cccd,
                typeDisplay));
        bookingTypeLabel.setForeground(new Color(5, 150, 105));

        List<Room> rooms = service.getRoomsByOrderAndType(currentBooking.orderId, currentBookingType);

        currentRoomsMap.clear();
        currentRoomsModel.setRowCount(0);

        for (Room room : rooms) {
            currentRoomsMap.put(room.getRoomNumber(), room);
            long price = service.getRoomPriceByType(room, currentBookingType);
            currentRoomsModel.addRow(new Object[] {
                    room.getRoomNumber(),
                    room.getRoomType().getName(),
                    String.format("%,dđ", price)
            });
        }

        selectedOldRooms.clear();
        selectedNewRooms.clear();
        newRoomsModel.setRowCount(0);
        updateSummary();
    }

    private void selectOldRooms() {
        if (currentBooking == null) {
            CustomDialog.showMessage(this,
                    "Vui lòng chọn phòng trước!",
                    "Thông báo",
                    CustomDialog.MessageType.WARNING, 400, 200);
            return;
        }

        int[] rows = currentRoomsTable.getSelectedRows();
        if (rows.length == 0) {
            CustomDialog.showMessage(this,
                    "Vui lòng chọn phòng cần đổi!",
                    "Thông báo",
                    CustomDialog.MessageType.WARNING, 400, 200);
            return;
        }

        selectedOldRooms.clear();
        for (int row : rows) {
            String roomNum = currentRoomsModel.getValueAt(row, 0).toString();
            Room room = currentRoomsMap.get(roomNum);
            if (room != null) {
                selectedOldRooms.add(room);
            }
        }

        updateSummary();
        CustomDialog.showMessage(this,
                String.format("Đã chọn %d phòng cần đổi<br>Bây giờ chọn phòng mới!",
                        selectedOldRooms.size()),
                "Thông báo",
                CustomDialog.MessageType.INFO, 400, 200);
    }

    private void showAvailableRooms(String roomTypeId) {
        if (currentBooking == null) {
            CustomDialog.showMessage(this,
                    "Vui lòng chọn phòng đã đặt!",
                    "Lỗi",
                    CustomDialog.MessageType.ERROR, 380, 180);
            return;
        }

        if (selectedOldRooms.isEmpty()) {
            CustomDialog.showMessage(this,
                    "Vui lòng chọn phòng khách đang ở!",
                    "Thiếu thông tin",
                    CustomDialog.MessageType.WARNING, 380, 180);
            return;
        }

        List<Room> available = service.getAvailableRoomsByType(roomTypeId);

        if (available.isEmpty()) {
            String typeName = roomTypeId.equals("SINGLE") ? "Phòng đơn" : "Phòng đôi";
            CustomDialog.showMessage(this,
                    "Không có " + typeName + " trống!",
                    "Thông báo",
                    CustomDialog.MessageType.WARNING, 400, 200);
            return;
        }

        // Tạo custom dialog với kích thước lớn hơn và style đẹp hơn
        Room selectedRoom = showRoomSelectionDialog(available);

        if (selectedRoom != null && !selectedNewRooms.contains(selectedRoom)) {
            selectedNewRooms.add(selectedRoom);
            long price = service.getRoomPriceByType(selectedRoom, currentBookingType);
            newRoomsModel.addRow(new Object[] {
                    selectedRoom.getRoomNumber(),
                    selectedRoom.getRoomType().getName(),
                    String.format("%,dđ", price)
            });
            updateSummary();
        }
    }

    private Room showRoomSelectionDialog(List<Room> available) {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentFrame, "Chọn Phòng Trống", true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        // Header với hiệu ứng
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel titleLabel = new JLabel("DANH SÁCH PHÒNG TRỐNG");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Tạo list với custom renderer
        DefaultListModel<Room> listModel = new DefaultListModel<>();
        available.forEach(listModel::addElement);

        JList<Room> roomList = new JList<>(listModel);
        roomList.setFont(new Font("Arial", Font.PLAIN, 16));
        roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        roomList.setCellRenderer(new RoomListCellRenderer());
        roomList.setFixedCellHeight(60);

        JScrollPane scrollPane = new JScrollPane(roomList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton selectButton = new JButton("Chọn Phòng");
        selectButton.setFont(new Font("Arial", Font.BOLD, 14));
        selectButton.setPreferredSize(new Dimension(150, 40));
        selectButton.setBackground(new Color(46, 204, 113));
        selectButton.setForeground(Color.WHITE);
        selectButton.setFocusPainted(false);
        selectButton.setBorderPainted(false);

        JButton cancelButton = new JButton("Hủy");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setPreferredSize(new Dimension(150, 40));
        cancelButton.setBackground(new Color(231, 76, 60));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);

        // Hiệu ứng hover cho buttons
        addButtonHoverEffect(selectButton, new Color(46, 204, 113), new Color(39, 174, 96));
        addButtonHoverEffect(cancelButton, new Color(231, 76, 60), new Color(192, 57, 43));

        final Room[] selectedRoom = { null };

        selectButton.addActionListener(e -> {
            if (roomList.getSelectedValue() != null) {
                selectedRoom[0] = roomList.getSelectedValue();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog,
                        "Vui lòng chọn một phòng!",
                        "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        // Double click để chọn nhanh
        roomList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int index = roomList.locationToIndex(evt.getPoint());
                    if (index >= 0) {
                        selectedRoom[0] = roomList.getModel().getElementAt(index);
                        dialog.dispose();
                    }
                }
            }
        });

        buttonPanel.add(selectButton);
        buttonPanel.add(cancelButton);

        dialog.add(headerPanel, BorderLayout.NORTH);
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
        return selectedRoom[0];
    }

    // Custom cell renderer để làm đẹp list items
    private class RoomListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {

            Room room = (Room) value;
            JPanel panel = new JPanel(new BorderLayout(10, 5));
            panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)));

            if (isSelected) {
                panel.setBackground(new Color(52, 152, 219, 50));
            } else {
                panel.setBackground(Color.WHITE);
            }

            JLabel roomLabel = new JLabel("Phòng " + room.getRoomNumber());
            roomLabel.setFont(new Font("Arial", Font.BOLD, 16));
            roomLabel.setForeground(new Color(44, 62, 80));

            JLabel typeLabel = new JLabel(room.getRoomType().getName());
            typeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            typeLabel.setForeground(new Color(127, 140, 141));

            panel.add(roomLabel, BorderLayout.WEST);
            panel.add(typeLabel, BorderLayout.EAST);

            return panel;
        }
    }

    // Thêm hiệu ứng hover cho button
    private void addButtonHoverEffect(JButton button, Color normalColor, Color hoverColor) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(normalColor);
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    private void removeNewRoom() {
        int row = newRoomsTable.getSelectedRow();
        if (row == -1) {
            CustomDialog.showMessage(this,
                    "Chọn phòng cần xóa!",
                    "Thông báo",
                    CustomDialog.MessageType.WARNING, 400, 200);
            return;
        }

        selectedNewRooms.remove(row);
        newRoomsModel.removeRow(row);
        updateSummary();
    }

    private void clearNewRooms() {
        selectedNewRooms.clear();
        newRoomsModel.setRowCount(0);
        updateSummary();
    }

    private void updateSummary() {
        for (Component comp : ((JPanel) ((JPanel) totalSurchargeField.getParent().getParent()).getComponent(0))
                .getComponents()) {
            if (comp instanceof JLabel label) {
                if ("oldRoomsLabel".equals(label.getName())) {
                    label.setText("Phòng cũ: " + selectedOldRooms.size() + " phòng");
                } else if ("newRoomsLabel".equals(label.getName())) {
                    label.setText("Phòng mới: " + selectedNewRooms.size() + " phòng");
                }
            }
        }

        if (currentBookingType == null) {
            totalSurchargeField.setText("0đ");
            totalSurchargeField.setForeground(new Color(107, 114, 128));
            return;
        }

        long surcharge = service.calculateSurcharge(selectedOldRooms, selectedNewRooms, currentBookingType);

        if (surcharge > 0) {
            totalSurchargeField.setText(String.format("+%,dđ", surcharge));
            totalSurchargeField.setForeground(new Color(220, 38, 38));
        } else if (surcharge < 0) {
            totalSurchargeField.setText(String.format("%,dđ", surcharge));
            totalSurchargeField.setForeground(new Color(16, 185, 129));
        } else {
            totalSurchargeField.setText("0đ");
            totalSurchargeField.setForeground(new Color(107, 114, 128));
        }
    }

    private void searchBooking() {
        String keyword = searchField.getText().trim();
        bookings = service.searchBookings(keyword);
        loadBookingsToTable();
    }

    private void performTransfer() {
        if (currentBooking == null) {
            CustomDialog.showMessage(this,
                    "Chọn booking trước!",
                    "Thông báo",
                    CustomDialog.MessageType.WARNING, 400, 200);
            return;
        }

        var validation = service.validateTransfer(selectedOldRooms, selectedNewRooms, currentBookingType);
        if (!validation.valid) {
            CustomDialog.showMessage(this,
                    validation.message,
                    "Thông báo",
                    CustomDialog.MessageType.WARNING, 500, 300);
            return;
        }

        long oldTotal = selectedOldRooms.stream()
                .mapToLong(r -> service.getRoomPriceByType(r, currentBookingType))
                .sum();

        long newTotal = selectedNewRooms.stream()
                .mapToLong(r -> service.getRoomPriceByType(r, currentBookingType))
                .sum();

        long surcharge = newTotal - oldTotal;

        String oldRoomNumbers = selectedOldRooms.stream()
                .map(Room::getRoomNumber)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");

        String newRoomNumbers = selectedNewRooms.stream()
                .map(Room::getRoomNumber)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");

        String colorCode = surcharge >= 0 ? "red" : "green";
        String surchargeLabel = surcharge >= 0 ? "Phụ phí" : "Hoàn lại";

        String message = String.format(
                "Xác nhận chuyển phòng từ phòng cũ (%d) sang phòng mới (%d) với tổng giá thay đổi từ %,dđ thành %,dđ.",
                selectedOldRooms.size(),
                selectedNewRooms.size(),
                oldTotal,
                newTotal);
        int confirm = JOptionPane.showConfirmDialog(this, message, "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            BookingType bookingType = currentBooking.bookingType;
            TransferRoomService.TransferResult result = service.transferRooms(
                    currentBooking.orderId,
                    selectedOldRooms,
                    selectedNewRooms,
                    bookingType);

            if (result.success) {
                String successMsg = String.format(
                        "<b>CHUYỂN PHÒNG THÀNH CÔNG!</b><br><br>" +
                                "Từ: <b>%s</b> (%d phòng)<br>" +
                                "Sang: <b>%s</b> (%d phòng)<br>" +
                                "Loại thuê: <b>%s</b><br><br>" +
                                "%s: <b>%,dđ</b>",
                        oldRoomNumbers, selectedOldRooms.size(),
                        newRoomNumbers, selectedNewRooms.size(),
                        service.getBookingTypeDisplay(bookingType),
                        result.surcharge >= 0 ? "Đã thu phụ phí" : "Đã hoàn lại",
                        Math.abs(result.surcharge));

                CustomDialog.showMessage(
                        this,
                        successMsg,
                        "Thành công",
                        CustomDialog.MessageType.SUCCESS,
                        500,
                        350);

                resetForm();
                loadBookingsFromDatabase();
            } else {
                CustomDialog.showMessage(
                        this,
                        "Chuyển phòng thất bại! " + result.message,
                        "Thất bại",
                        CustomDialog.MessageType.ERROR,
                        500,
                        280);
            }
        }
    }

    private void resetForm() {
        bookingsTable.clearSelection();
        currentRoomsModel.setRowCount(0);
        newRoomsModel.setRowCount(0);
        selectedOldRooms.clear();
        selectedNewRooms.clear();
        currentBooking = null;
        currentBookingType = null;
        currentRoomsMap.clear();

        bookingTypeLabel.setText("Chưa chọn phòng đã đặt - Vui lòng chọn để bắt đầu");
        bookingTypeLabel.setForeground(new Color(146, 64, 14));

        updateSummary();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test RoomTransferUI");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1500, 900);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new RoomTransferUI());
            frame.setVisible(true);
        });
    }
}