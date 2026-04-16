package iuh.fit.se.group1.ui.layout;
import iuh.fit.se.group1.entity.Order;
import iuh.fit.se.group1.entity.OrderType;
import iuh.fit.se.group1.entity.Room;
import iuh.fit.se.group1.enums.BookingType;
import iuh.fit.se.group1.enums.OrderBookStatus;
import iuh.fit.se.group1.service.RoomToolsService;
import iuh.fit.se.group1.ui.component.custom.message.CustomDialog;
import iuh.fit.se.group1.ui.component.modal.ExtendBookingModal;
import iuh.fit.se.group1.ui.component.scroll.ScrollPaneWin11;
import raven.glasspanepopup.GlassPanePopup;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class RoomToolsManagement extends JPanel {
    private JPanel pnlMain;
    private JTextField txtSearch;
    private JTable tblBookings;
    private DefaultTableModel bookingsModel;
    private JTable tblCurrentRooms;
    private DefaultTableModel currentRoomsModel;
    private JTable tblNewRooms;
    private DefaultTableModel newRoomsModel;
    private JTextField txtTotalSurcharge;
    private JButton btnRemoveRoom, btnClearAll;
    private JLabel lblBookingType;

    private final RoomToolsService service;

    private List<Order> orders = new ArrayList<>();

    private Map<String, Room> currentRoomsMap = new HashMap<>();
    private List<Room> selectedOldRooms = new ArrayList<>();
    private List<Room> selectedNewRooms = new ArrayList<>();
    private Order currentBooking = null;
    private BookingType currentBookingType = null;
    private OrderType currentOrderType = null;
    
    public RoomToolsManagement() {
        setLayout(new BorderLayout());
        this.service = new RoomToolsService();
        initComponents();
        loadBookingsFromDatabase();
        setVisible(true);
    }

    private void initComponents() {
        pnlMain = new JPanel(new BorderLayout(0, 0));
        pnlMain.setBackground(new Color(245, 247, 250));

        JPanel pnlHeader = createHeaderPanel();
        pnlMain.add(pnlHeader, BorderLayout.NORTH);

        JPanel pnlContent = new JPanel(new BorderLayout(20, 0));
        pnlContent.setBackground(new Color(245, 247, 250));
        pnlContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel pnlLeft = createBookingsPanel();
        pnlLeft.setPreferredSize(new Dimension(450, 0));
        pnlContent.add(pnlLeft, BorderLayout.WEST);

        JPanel pnlRight = createTransferPanel();
        pnlContent.add(pnlRight, BorderLayout.CENTER);

        pnlMain.add(pnlContent, BorderLayout.CENTER);
        add(pnlMain);
    }

    private JPanel createHeaderPanel() {
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(108, 165, 200));
        pnlHeader.setPreferredSize(new Dimension(0, 80));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        JLabel lblTitle = new JLabel("TIỆN ÍCH PHÒNG KHÁCH SẠN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSubtitle = new JLabel("Quản lý tiện ích phòng cho khách hàng");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitle.setForeground(new Color(219, 234, 254));

        JPanel pnlTitle = new JPanel(new GridLayout(2, 1, 0, 5));
        pnlTitle.setOpaque(false);
        pnlTitle.add(lblTitle);
        pnlTitle.add(lblSubtitle);

        pnlHeader.add(pnlTitle, BorderLayout.WEST);
        return pnlHeader;
    }

    private JPanel createBookingsPanel() {
        JPanel pnl = new JPanel(new BorderLayout(0, 15));
        pnl.setBackground(Color.WHITE);
        pnl.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(229, 231, 235), 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JLabel lblTitle = new JLabel("Danh sách đặt phòng");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(31, 41, 55));

        JPanel pnlSearch = new JPanel(new BorderLayout(10, 0));
        pnlSearch.setBackground(Color.WHITE);

        txtSearch = new JTextField();
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(209, 213, 219), 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        txtSearch.setToolTipText("Nhập CCCD để tìm kiếm (tự động tìm khi gõ)");

        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                searchBooking();
            }
        });

        pnlSearch.add(txtSearch, BorderLayout.CENTER);

        String[] columns = { "CCCD", "Khách hàng", "Phòng", "Loại thuê" };
        bookingsModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblBookings = new JTable(bookingsModel);
        styleTable(tblBookings);
        tblBookings.getColumnModel().getColumn(0).setPreferredWidth(100);
        tblBookings.getColumnModel().getColumn(1).setPreferredWidth(120);
        tblBookings.getColumnModel().getColumn(2).setPreferredWidth(80);
        tblBookings.getColumnModel().getColumn(3).setPreferredWidth(90);

        tblBookings.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblBookings.getSelectedRow() != -1) {
                loadSelectedBooking();
            }
        });
        ScrollPaneWin11 scrollPane = new ScrollPaneWin11(tblBookings);
        scrollPane.setBorder(new LineBorder(new Color(229, 231, 235), 1));

        JPanel pnlTop = new JPanel(new BorderLayout(0, 15));
        pnlTop.setBackground(Color.WHITE);
        pnlTop.add(lblTitle, BorderLayout.NORTH);
        pnlTop.add(pnlSearch, BorderLayout.CENTER);

        pnl.add(pnlTop, BorderLayout.NORTH);
        pnl.add(scrollPane, BorderLayout.CENTER);

        return pnl;
    }

    private JPanel createTransferPanel() {
        JPanel pnl = new JPanel(new BorderLayout(0, 15));
        pnl.setBackground(Color.WHITE);
        pnl.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(229, 231, 235), 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JLabel lblTitle = new JLabel("Thông tin chuyển phòng");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(31, 41, 55));

        JPanel pnlBookingType = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlBookingType.setBackground(new Color(254, 243, 199));
        pnlBookingType.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(251, 191, 36), 2, true),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        lblBookingType = new JLabel("Chưa chọn khách hàng - Vui lòng chọn khách hàng để bắt đầu");
        lblBookingType.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblBookingType.setForeground(new Color(146, 64, 14));
        pnlBookingType.add(lblBookingType);

        JPanel pnlContent = new JPanel(new GridLayout(1, 2, 20, 0));
        pnlContent.setBackground(Color.WHITE);

        pnlContent.add(createOldRoomsPanel());
        pnlContent.add(createNewRoomsPanel());

        JPanel pnlBottom = createBottomPanel();

        JPanel pnlTopSection = new JPanel(new BorderLayout(0, 10));
        pnlTopSection.setBackground(Color.WHITE);
        pnlTopSection.add(lblTitle, BorderLayout.NORTH);
        pnlTopSection.add(pnlBookingType, BorderLayout.CENTER);

        pnl.add(pnlTopSection, BorderLayout.NORTH);
        pnl.add(pnlContent, BorderLayout.CENTER);
        pnl.add(pnlBottom, BorderLayout.SOUTH);

        return pnl;
    }

    private JPanel createOldRoomsPanel() {
        JPanel pnl = new JPanel(new BorderLayout(0, 10));
        pnl.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Phòng hiện tại");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(new Color(55, 65, 81));

        String[] columns = { "Phòng", "Loại", "Giá", "Hủy phòng" };
        currentRoomsModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };
        tblCurrentRooms = new JTable(currentRoomsModel){
            @Override
            public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
                if (isRowSelected(rowIndex)) {
                    getSelectionModel().removeSelectionInterval(rowIndex, rowIndex);
                } else {
                    getSelectionModel().addSelectionInterval(rowIndex, rowIndex);
                }
            }
        };
        styleTable(tblCurrentRooms);
        tblCurrentRooms.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        tblCurrentRooms.getColumnModel().getColumn(0).setPreferredWidth(60);
        tblCurrentRooms.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblCurrentRooms.getColumnModel().getColumn(2).setPreferredWidth(100);
        tblCurrentRooms.getColumnModel().getColumn(3).setPreferredWidth(80);

        tblCurrentRooms.getColumnModel().getColumn(3).setCellRenderer(new TableCellRenderer() {
            private final JButton btn = new JButton("Hủy");

            {
                btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
                btn.setForeground(Color.WHITE);
                btn.setBackground(new Color(239, 68, 68));
                btn.setFocusPainted(false);
                btn.setBorderPainted(false);
            }

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                return btn;
            }
        });

        tblCurrentRooms.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = tblCurrentRooms.columnAtPoint(e.getPoint());
                int row = tblCurrentRooms.rowAtPoint(e.getPoint());

                if (column == 3 && row >= 0) {
                    handleCancelRoom(row);
                }
            }
        });

        ScrollPaneWin11 scrollPane = new ScrollPaneWin11(tblCurrentRooms);
        scrollPane.setBorder(new LineBorder(new Color(229, 231, 235), 1));
        scrollPane.setPreferredSize(new Dimension(0, 200));

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlButtons.setBackground(Color.WHITE);

        JButton btnSelect = createStyledButton("Chọn phòng hiện tại", new Color(59, 130, 246), 155, 35);
        JButton btnExtend = createStyledButton("Gia hạn phòng", new Color(16, 185, 129), 125, 35);

        btnSelect.addActionListener(e -> selectOldRooms());
        btnExtend.addActionListener(e -> extendRoomBooking());

        pnlButtons.add(btnSelect);
        pnlButtons.add(btnExtend);

        pnl.add(lblTitle, BorderLayout.NORTH);
        pnl.add(scrollPane, BorderLayout.CENTER);
        pnl.add(pnlButtons, BorderLayout.SOUTH);

        return pnl;
    }

    private void extendRoomBooking() {
        if (currentBooking == null) {
            CustomDialog.showMessage(this,
                    "Vui lòng chọn thông tin khách hàng!",
                    "Thông báo",
                    CustomDialog.MessageType.WARNING, 400, 200);
            return;
        }

        int[] rows = tblCurrentRooms.getSelectedRows();
        if (rows.length == 0) {
            CustomDialog.showMessage(this,
                    "Vui lòng chọn phòng cần gia hạn!",
                    "Thông báo",
                    CustomDialog.MessageType.WARNING, 400, 200);
            return;
        }

        List<Room> roomsToExtend = new ArrayList<>();
        for (int row : rows) {
            String roomNum = currentRoomsModel.getValueAt(row, 0).toString();
            Room room = currentRoomsMap.get(roomNum);
            if (room != null) {
                roomsToExtend.add(room);
            }
        }

        if (roomsToExtend.isEmpty()) {
            return;
        }

        showExtendBookingPopup(roomsToExtend);
    }

    private void showExtendBookingPopup(List<Room> roomsToExtend) {
        ExtendBookingModal extendPanel = new ExtendBookingModal(
                currentBooking,
                roomsToExtend,
                currentBookingType,
                () -> {
                    GlassPanePopup.closePopupLast();
                    loadSelectedBooking();
                },
                () -> {
                    GlassPanePopup.closePopupLast();
                }
        );

        GlassPanePopup.showPopup(extendPanel);
    }

    private void handleCancelRoom(int row) {
        if (currentBooking == null || row < 0 || row >= currentRoomsModel.getRowCount()) {
            return;
        }

        String roomNumber = currentRoomsModel.getValueAt(row, 0).toString();
        Room roomToCancel = currentRoomsMap.get(roomNumber);

        if (roomToCancel == null) {
            return;
        }
        currentOrderType = currentBooking.getOrderType();
        
        if (!currentOrderType.getName().equals(OrderBookStatus.RESERVED) ) {
            CustomDialog.showMessage(this,
                    "Chỉ có thể hủy phòng đặt trước!",
                    "Thông báo",
                    CustomDialog.MessageType.WARNING, 400, 250);
            return;
        }

        long roomPrice = service.getRoomPriceWithDuration(roomToCancel, currentBookingType,
                currentBooking.getOrderId());

        String message = String.format(
                "<div style='line-height: 1.8;'>" +
                        "<b style='font-size: 15px;'>Xác nhận hủy đặt phòng?</b><br><br>" +
                        "Phòng: <b>%s</b><br>" +
                        "Hóa đơn: <b>%s</b><br>" +
                        "Giá phòng: <b style='color: #DC2626;'>%,dđ</b><br><br>" +
                        "<i style='color: #6B7280;'>Phòng sẽ được chuyển về trạng thái trống</i>" +
                        "</div>",
                roomNumber,
                currentBooking.getOrderId(),
                roomPrice);

        int confirm = CustomDialog.showConfirm(
                this,
                message,
                "Xác nhận hủy phòng",
                CustomDialog.MessageType.WARNING,
                450,
                350);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = service.cancelRoomBooking(
                        currentBooking.getOrderId(),
                        roomToCancel.getRoomId(),
                        currentBookingType);

                if (success) {
                    currentRoomsModel.removeRow(row);
                    currentRoomsMap.remove(roomNumber);
                    selectedOldRooms.removeIf(r -> r.getRoomNumber().equals(roomNumber));

                    updateSummary();

                    String successMsg = String.format(
                            "<div style='line-height: 1.8;'>" +
                                    "<b style='color: #10B981; font-size: 16px;'>✓ HỦY PHÒNG THÀNH CÔNG!</b><br><br>" +
                                    "Phòng <b>%s</b> đã được hủy<br>" +
                                    "Phòng hiện đang trống và có thể đặt lại<br>" +
                                    "Số tiền hoàn: <b style='color: #10B981;'>%,dđ</b>" +
                                    "</div>",
                            roomNumber,
                            roomPrice);

                    CustomDialog.showMessage(this,
                            successMsg,
                            "Thành công",
                            CustomDialog.MessageType.SUCCESS, 450, 320);

                    if (currentRoomsModel.getRowCount() == 0) {
                        resetForm();
                        loadBookingsFromDatabase();
                    }
                } else {
                    CustomDialog.showMessage(this,
                            "Không thể hủy phòng! Vui lòng thử lại.",
                            "Lỗi",
                            CustomDialog.MessageType.ERROR, 400, 220);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                CustomDialog.showMessage(this,
                        "Lỗi khi hủy phòng: " + ex.getMessage(),
                        "Lỗi",
                        CustomDialog.MessageType.ERROR, 450, 250);
            }
        }
    }

    private JPanel createNewRoomsPanel() {
        JPanel pnl = new JPanel(new BorderLayout(0, 10));
        pnl.setBackground(Color.WHITE);

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Phòng mới");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(new Color(55, 65, 81));

        JPanel pnlType = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        pnlType.setBackground(Color.WHITE);

        JButton btnSingle = createStyledButton("Phòng đơn", new Color(99, 102, 241), 100, 30);
        JButton btnDouble = createStyledButton("Phòng đôi", new Color(139, 92, 246), 100, 30);

        btnSingle.addActionListener(e -> showAvailableRooms("SINGLE"));
        btnDouble.addActionListener(e -> showAvailableRooms("DOUBLE"));

        pnlType.add(btnSingle);
        pnlType.add(btnDouble);

        pnlHeader.add(lblTitle, BorderLayout.WEST);
        pnlHeader.add(pnlType, BorderLayout.EAST);

        String[] columns = { "Phòng", "Loại", "Giá" };
        newRoomsModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblNewRooms = new JTable(newRoomsModel);
        styleTable(tblNewRooms);

        ScrollPaneWin11 scrollPane = new ScrollPaneWin11(tblNewRooms);
        scrollPane.setBorder(new LineBorder(new Color(229, 231, 235), 1));
        scrollPane.setPreferredSize(new Dimension(0, 200));

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlButtons.setBackground(Color.WHITE);

        btnRemoveRoom = createStyledButton("Xóa phòng", new Color(239, 68, 68), 120, 35);
        btnClearAll = createStyledButton("Xóa tất cả", new Color(156, 163, 175), 100, 35);

        btnRemoveRoom.addActionListener(e -> removeNewRoom());
        btnClearAll.addActionListener(e -> clearNewRooms());

        pnlButtons.add(btnRemoveRoom);
        pnlButtons.add(btnClearAll);

        pnl.add(pnlHeader, BorderLayout.NORTH);
        pnl.add(scrollPane, BorderLayout.CENTER);
        pnl.add(pnlButtons, BorderLayout.SOUTH);

        return pnl;
    }

    private JPanel createBottomPanel() {
        JPanel pnl = new JPanel(new BorderLayout(0, 15));
        pnl.setBackground(Color.WHITE);

        JPanel pnlSummary = new JPanel(new GridLayout(2, 1, 0, 10));
        pnlSummary.setBackground(new Color(254, 252, 232));
        pnlSummary.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(250, 204, 21), 2, true),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)));

        JPanel pnlRow1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlRow1.setBackground(new Color(254, 252, 232));

        JLabel lblOldRooms = new JLabel("Phòng cũ: 0 phòng");
        lblOldRooms.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblOldRooms.setName("oldRoomsLabel");

        JLabel lblNewRooms = new JLabel("Phòng mới: 0 phòng");
        lblNewRooms.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNewRooms.setName("newRoomsLabel");

        pnlRow1.add(lblOldRooms);
        pnlRow1.add(new JLabel("→"));
        pnlRow1.add(lblNewRooms);

        JPanel pnlRow2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlRow2.setBackground(new Color(254, 252, 232));

        JLabel lblSurcharge = new JLabel("Tổng phụ phí: ");
        lblSurcharge.setFont(new Font("Segoe UI", Font.BOLD, 14));

        txtTotalSurcharge = new JTextField(20);
        txtTotalSurcharge.setFont(new Font("Segoe UI", Font.BOLD, 18));
        txtTotalSurcharge.setForeground(new Color(220, 38, 38));
        txtTotalSurcharge.setEditable(false);
        txtTotalSurcharge.setBackground(Color.WHITE);
        txtTotalSurcharge.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(252, 165, 165), 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        txtTotalSurcharge.setText("0đ");

        pnlRow2.add(lblSurcharge);
        pnlRow2.add(txtTotalSurcharge);

        pnlSummary.add(pnlRow1);
        pnlSummary.add(pnlRow2);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlButtons.setBackground(Color.WHITE);

        JButton btnCancel = createStyledButton("Hủy bỏ", new Color(156, 163, 175), 120, 40);
        JButton btnConfirm = createStyledButton("Xác nhận chuyển", new Color(16, 185, 129), 150, 40);

        btnCancel.addActionListener(e -> resetForm());
        btnConfirm.addActionListener(e -> performTransfer());

        pnlButtons.add(btnCancel);
        pnlButtons.add(btnConfirm);

        pnl.add(pnlSummary, BorderLayout.CENTER);
        pnl.add(pnlButtons, BorderLayout.SOUTH);

        return pnl;
    }

    private JButton createStyledButton(String text, Color bgColor, int width, int height) {
        JButton btn = new JButton(text) {
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

        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(width, height));
        btn.setOpaque(false);

        return btn;
    }

    private void styleTable(JTable tbl) {
        tbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tbl.setRowHeight(32);
        tbl.setShowVerticalLines(false);
        tbl.setGridColor(new Color(243, 244, 246));
        tbl.setSelectionBackground(new Color(219, 234, 254));
        tbl.setSelectionForeground(new Color(31, 41, 55));

        JTableHeader header = tbl.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(249, 250, 251));
        header.setForeground(new Color(55, 65, 81));
        header.setPreferredSize(new Dimension(0, 35));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tbl.getColumnCount(); i++) {
            tbl.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void loadBookingsFromDatabase() {
        orders = service.getAllOrders();
        loadBookingsToTable();
    }

    private void loadBookingsToTable() {
        bookingsModel.setRowCount(0);
        for (Order order : orders) {
            String room = order.getBookings().stream().map(e -> e.getRoom().getRoomNumber())
                    .collect(Collectors.joining(", "));
            bookingsModel.addRow(new Object[] {
                    order.getCustomer().getCitizenId(),
                    order.getCustomer().getFullName(),
                    room,
                    order.getBookings().get(0).getBookingType().getDisplayName()
            });
        }
    }

    private void loadSelectedBooking() {
        int row = tblBookings.getSelectedRow();
        if (row == -1)
            return;

        currentBooking = orders.get(row);
        currentBookingType = currentBooking.getBookings().get(0).getBookingType();
        currentOrderType = currentBooking.getBookings().get(0).getOrder().getOrderType();
        String typeDisplay = currentBookingType.getDisplayName();
        lblBookingType.setText(String.format(
                "%s (%s) - Loại thuê: %s - Loại hóa đơn: %s",
                currentBooking.getCustomer().getFullName(),
                currentBooking.getCustomer().getCitizenId(),
                typeDisplay, currentOrderType.getName()));
        lblBookingType.setForeground(new Color(5, 150, 105));

        List<Room> rooms = service.getRoomsByOrderAndType(currentBooking.getOrderId(), currentBookingType);

        currentRoomsMap.clear();
        currentRoomsModel.setRowCount(0);

        for (Room room : rooms) {
            currentRoomsMap.put(room.getRoomNumber(), room);
            long price = service.getRoomPriceWithDuration(room, currentBookingType, currentBooking.getOrderId());
            currentRoomsModel.addRow(new Object[] {
                    room.getRoomNumber(),
                    room.getRoomType().getName(),
                    String.format("%,dđ", price),
                    "Hủy"
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
                    "Vui lòng chọn thông tin khách hàng!",
                    "Thông báo",
                    CustomDialog.MessageType.WARNING, 400, 200);
            return;
        }

        int[] rows = tblCurrentRooms.getSelectedRows();
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
                    "Vui lòng chọn phòng hiện tại!",
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

        Room selectedRoom = showRoomSelectionDialog(available);

        if (selectedRoom != null && !selectedNewRooms.contains(selectedRoom)) {
            selectedNewRooms.add(selectedRoom);
            long price = calculateNewRoomPrice(selectedRoom);
            newRoomsModel.addRow(new Object[] {
                    selectedRoom.getRoomNumber(),
                    selectedRoom.getRoomType().getName(),
                    String.format("%,dđ", price)
            });
            updateSummary();
        }
    }
    
    private long calculateNewRoomPrice(Room newRoom) {
        if (currentBooking == null || currentBookingType == null || selectedOldRooms.isEmpty()) {
            return service.getRoomPriceByType(newRoom, currentBookingType);
        }

        Room referenceRoom = selectedOldRooms.get(0);
        return service.calculateNewRoomPriceWithBookingDuration(
                newRoom, currentBookingType, currentBooking.getOrderId(), referenceRoom);
    }
    
    public void loadData() {
        orders = service.getAllOrders();
        bookingsModel.setRowCount(0);

        for (Order order : orders) {
            String rooms = order.getBookings().stream()
                    .map(b -> b.getRoom().getRoomNumber())
                    .collect(Collectors.joining(", "));

            String bookingType = order.getBookings().isEmpty()
                    ? ""
                    : order.getBookings().get(0).getBookingType().getDisplayName();

            bookingsModel.addRow(new Object[] {
                    order.getCustomer().getCitizenId(),
                    order.getCustomer().getFullName(),
                    rooms,
                    bookingType
            });
        }
    }

    private Room showRoomSelectionDialog(List<Room> available) {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentFrame, "Chọn Phòng Trống", true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(new Color(52, 152, 219));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel lblTitle = new JLabel("DANH SÁCH PHÒNG TRỐNG");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle);

        DefaultListModel<Room> listModel = new DefaultListModel<>();
        available.forEach(listModel::addElement);

        JList<Room> lstRooms = new JList<>(listModel);
        lstRooms.setFont(new Font("Arial", Font.PLAIN, 16));
        lstRooms.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstRooms.setCellRenderer(new RoomListCellRenderer());
        lstRooms.setFixedCellHeight(60);

        ScrollPaneWin11 scrollPane = new ScrollPaneWin11(lstRooms);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        pnlButtons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnSelect = new JButton("Chọn Phòng");
        btnSelect.setFont(new Font("Arial", Font.BOLD, 14));
        btnSelect.setPreferredSize(new Dimension(150, 40));
        btnSelect.setBackground(new Color(46, 204, 113));
        btnSelect.setForeground(Color.WHITE);
        btnSelect.setFocusPainted(false);
        btnSelect.setBorderPainted(false);

        JButton btnCancel = new JButton("Hủy");
        btnCancel.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancel.setPreferredSize(new Dimension(150, 40));
        btnCancel.setBackground(new Color(231, 76, 60));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.setBorderPainted(false);

        addButtonHoverEffect(btnSelect, new Color(46, 204, 113), new Color(39, 174, 96));
        addButtonHoverEffect(btnCancel, new Color(231, 76, 60), new Color(192, 57, 43));

        final Room[] selectedRoom = { null };

        btnSelect.addActionListener(e -> {
            if (lstRooms.getSelectedValue() != null) {
                selectedRoom[0] = lstRooms.getSelectedValue();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog,
                        "Vui lòng chọn một phòng!",
                        "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        lstRooms.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int index = lstRooms.locationToIndex(evt.getPoint());
                    if (index >= 0) {
                        selectedRoom[0] = lstRooms.getModel().getElementAt(index);
                        dialog.dispose();
                    }
                }
            }
        });

        pnlButtons.add(btnSelect);
        pnlButtons.add(btnCancel);

        dialog.add(pnlHeader, BorderLayout.NORTH);
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(pnlButtons, BorderLayout.SOUTH);

        dialog.setVisible(true);
        return selectedRoom[0];
    }

    private class RoomListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {

            Room room = (Room) value;
            JPanel pnl = new JPanel(new BorderLayout(10, 5));
            pnl.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)));

            if (isSelected) {
                pnl.setBackground(new Color(52, 152, 219, 50));
            } else {
                pnl.setBackground(Color.WHITE);
            }

            JLabel lblRoom = new JLabel("Phòng " + room.getRoomNumber());
            lblRoom.setFont(new Font("Arial", Font.BOLD, 16));
            lblRoom.setForeground(new Color(44, 62, 80));

            JLabel lblType = new JLabel(room.getRoomType().getName());
            lblType.setFont(new Font("Arial", Font.PLAIN, 14));
            lblType.setForeground(new Color(127, 140, 141));

            pnl.add(lblRoom, BorderLayout.WEST);
            pnl.add(lblType, BorderLayout.EAST);

            return pnl;
        }
    }

    private void addButtonHoverEffect(JButton btn, Color normalColor, Color hoverColor) {
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(hoverColor);
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(normalColor);
                btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    private void removeNewRoom() {
        int row = tblNewRooms.getSelectedRow();
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
        for (Component comp : ((JPanel) ((JPanel) txtTotalSurcharge.getParent().getParent()).getComponent(0))
                .getComponents()) {
            if (comp instanceof JLabel lbl) {
                if ("oldRoomsLabel".equals(lbl.getName())) {
                    lbl.setText("Phòng cũ: " + selectedOldRooms.size() + " phòng");
                } else if ("newRoomsLabel".equals(lbl.getName())) {
                    lbl.setText("Phòng mới: " + selectedNewRooms.size() + " phòng");
                }
            }
        }

        if (currentBookingType == null || currentBooking == null ||
                selectedOldRooms.isEmpty() || selectedNewRooms.isEmpty()) {
            txtTotalSurcharge.setText("0đ");
            txtTotalSurcharge.setForeground(new Color(107, 114, 128));
            return;
        }

        long surcharge = service.calculateSurcharge(selectedOldRooms, selectedNewRooms,
                currentBookingType, currentBooking.getOrderId());

        if (surcharge > 0) {
            txtTotalSurcharge.setText(String.format("+%,dđ", surcharge));
            txtTotalSurcharge.setForeground(new Color(220, 38, 38));
        } else if (surcharge < 0) {
            txtTotalSurcharge.setText(String.format("%,dđ", surcharge));
            txtTotalSurcharge.setForeground(new Color(16, 185, 129));
        } else {
            txtTotalSurcharge.setText("0đ");
            txtTotalSurcharge.setForeground(new Color(107, 114, 128));
        }
    }

    private void searchBooking() {
        String keyword = txtSearch.getText().trim();
        orders = service.findOrdersUnPendingByKeyWord(keyword);
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

        long surcharge = service.calculateSurcharge(selectedOldRooms, selectedNewRooms,
                currentBookingType, currentBooking.getOrderId());

        String oldRoomNumbers = selectedOldRooms.stream()
                .map(Room::getRoomNumber)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");

        String newRoomNumbers = selectedNewRooms.stream()
                .map(Room::getRoomNumber)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");

        String message = String.format(
                "Xác nhận chuyển phòng từ %s sang %s với phụ phí %s%,dđ?",
                oldRoomNumbers,
                newRoomNumbers,
                surcharge >= 0 ? "+" : "",
                surcharge);

        int confirm = JOptionPane.showConfirmDialog(this, message, "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            BookingType bookingType = currentBooking.getBookings().get(0).getBookingType();
            RoomToolsService.TransferResult result = service.transferRooms(
                    currentBooking.getOrderId(),
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
                        currentBookingType.getDisplayName(),
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
        tblBookings.clearSelection();
        currentRoomsModel.setRowCount(0);
        newRoomsModel.setRowCount(0);
        selectedOldRooms.clear();
        selectedNewRooms.clear();
        currentBooking = null;
        currentBookingType = null;
        currentRoomsMap.clear();

        lblBookingType.setText("Chưa chọn phòng đã đặt - Vui lòng chọn để bắt đầu");
        lblBookingType.setForeground(new Color(146, 64, 14));

        updateSummary();
    }

}