package iuh.fit.se.group1.ui.component.custom;

import iuh.fit.se.group1.dto.AmenityDTO;
import iuh.fit.se.group1.dto.SurchargeDTO;
import iuh.fit.se.group1.entity.*;
import iuh.fit.se.group1.repository.jpa.SurchargeRepositoryImpl;
import iuh.fit.se.group1.service.*;
import iuh.fit.se.group1.ui.component.custom.message.CustomDialog;
import iuh.fit.se.group1.ui.component.scroll.ScrollPaneWin11;
import iuh.fit.se.group1.util.Constants;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.Button;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Order edit dialog with improved UX/UI following SurchargeManagementPanel style.
 */
public class OrderEditDialog extends JDialog {

    // Color palette matching SurchargeManagementPanel
    private static final Color HEADER_COLOR = new Color(74, 144, 226);
    private static final Color ACCENT_COLOR = new Color(40, 167, 69);
    private static final Color WARNING_COLOR = new Color(255, 193, 7);
    private static final Color DANGER_COLOR = new Color(220, 53, 69);
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Color PANEL_BG = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(221, 221, 221);
    private static final Color HOVER_COLOR = new Color(230, 240, 255);
    private static final Color SELECTED_COLOR = new Color(220, 237, 255);

    private final Order order;
    private final OrderService orderService = new OrderService();
    private final OrderDetailService orderDetailService = new OrderDetailService();
    private final SurchargeDetailService surchargeDetailService = new SurchargeDetailService();

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // UI components
    private JTextField txtCustomerName;
    private JTextField txtPhone;
    private JTextField txtDeposit;
    private JPanel bookingsPanel;

    private JTable tblAmenities;
    private DefaultTableModel amenityModel;
    private JTable tblSurcharges;
    private DefaultTableModel surchargeModel;

    public OrderEditDialog(Window owner, Order order) {
        super(owner, "Chỉnh sửa hóa đơn #" + (order != null ? order.getOrderId() : ""), ModalityType.APPLICATION_MODAL);
        this.order = order;
        initComponents();
        loadExistingDetails();
        setSize(1000, 700);
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        JPanel mainPanel = new JPanel(new MigLayout("fill, insets 20", "[grow]", "[]10[]10[]10[]10[]10[]"));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Header
        mainPanel.add(createHeaderPanel(), "growx, wrap");

        // Customer info
        mainPanel.add(createCustomerInfoPanel(), "growx, wrap");

        // Bookings
        mainPanel.add(createBookingsPanel(), "growx, wrap");

        // Amenities and Surcharges side by side
        JPanel tablesPanel = new JPanel(new MigLayout("fill, insets 0", "[grow][grow]", "[grow]"));
        tablesPanel.setBackground(BACKGROUND_COLOR);
        tablesPanel.add(createAmenitiesPanel(), "grow");
        tablesPanel.add(createSurchargesPanel(), "grow");
        mainPanel.add(tablesPanel, "growx, wrap");

        // Action buttons
        mainPanel.add(createActionPanel(), "growx");

        ScrollPaneWin11 scroll = new ScrollPaneWin11(mainPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(scroll, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new MigLayout("fill, insets 20", "[grow]", "[]10[]")) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                int w = getWidth();
                int h = getHeight();

                // Gradient from header color to slightly darker
                GradientPaint gp = new GradientPaint(0, 0, HEADER_COLOR, 0, h, HEADER_COLOR.darker());
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);

                g2d.dispose();
            }
        };
        panel.setOpaque(true);
        panel.setBackground(HEADER_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(HEADER_COLOR.darker(), 2),
                new EmptyBorder(5, 10, 5, 10)
        ));

        JLabel title = new JLabel("CHỈNH SỬA HÓA ĐƠN");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitle = new JLabel(String.format("Mã hóa đơn: %s | Ngày: %s",
                order != null ? order.getOrderId() : "",
                order != null && order.getOrderDate() != null ? order.getOrderDate().format(dtf) : ""));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(230, 240, 255)); // Lighter blue for subtitle
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(title, "growx, wrap");
        panel.add(subtitle, "growx");

        return panel;
    }

    private JPanel createCustomerInfoPanel() {
        JPanel panel = createStyledPanel("THÔNG TIN KHÁCH HÀNG");
        panel.setLayout(new MigLayout("fill, insets 15", "[][grow]20[][grow]20[][grow]", "[]"));

        txtCustomerName = new JTextField(order != null && order.getCustomer() != null ? order.getCustomer().getFullName() : "");
        txtPhone = new JTextField(order != null && order.getCustomer() != null ? order.getCustomer().getPhone() : "");
        txtDeposit = new JTextField(order != null && order.getDeposit() != null ? Constants.VND_FORMAT.format(order.getDeposit()) : "0");
        txtDeposit.setEditable(false);
        txtDeposit.setBackground(new Color(245, 245, 245));


        txtCustomerName.setEditable(false);
        txtCustomerName.setBackground(new Color(245, 245, 245));
        txtPhone.setEditable(false);
        txtPhone.setBackground(new Color(245, 245, 245));

        styleTextField(txtCustomerName);
        styleTextField(txtPhone);
        styleTextField(txtDeposit);

        panel.add(createLabel("Họ tên:", Font.BOLD));
        panel.add(txtCustomerName, "growx");
        panel.add(createLabel("SĐT:", Font.BOLD));
        panel.add(txtPhone, "growx");
        panel.add(createLabel("Đặt cọc:", Font.BOLD));
        panel.add(txtDeposit, "growx");

        return panel;
    }

    private JPanel createBookingsPanel() {
        JPanel panel = createStyledPanel("LỊCH ĐẶT PHÒNG");
        panel.setLayout(new BorderLayout());

        bookingsPanel = new JPanel();
        bookingsPanel.setLayout(new BoxLayout(bookingsPanel, BoxLayout.Y_AXIS));
        bookingsPanel.setBackground(PANEL_BG);

        final Long orderTypeId = (order != null && order.getOrderType() != null) ? order.getOrderType().getOrderTypeId() : null;

        if (order != null && order.getBookings() != null && !order.getBookings().isEmpty()) {
            // Get first booking for initial dates
            Booking firstBooking = order.getBookings().get(0);

            // Create single row for check-in and check-out (applies to all rooms)
            JPanel dateRow = new JPanel(new MigLayout("fill, insets 10", "[right]10[grow]20[right]10[grow]", "[]"));
            dateRow.setBackground(Color.WHITE);
            dateRow.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
                    new EmptyBorder(8, 8, 8, 8)));

            JLabel lblCheckIn = createLabel("Check-in:", Font.BOLD);
            lblCheckIn.setForeground(HEADER_COLOR);
            JTextField txtCheckIn = new JTextField(firstBooking.getCheckInDate().format(dtf), 20);
            txtCheckIn.setName("checkin-all");
            txtCheckIn.setEditable(false);
            styleTextField(txtCheckIn);

            JLabel lblCheckOut = createLabel("Check-out:", Font.BOLD);
            lblCheckOut.setForeground(HEADER_COLOR);
            JTextField txtCheckOut = new JTextField(firstBooking.getCheckOutDate().format(dtf), 20);
            txtCheckOut.setName("checkout-all");
            txtCheckOut.setEditable(false);
            styleTextField(txtCheckOut);
            txtCheckIn.setBackground(new Color(245, 245, 245));
            txtCheckOut.setBackground(new Color(245, 245, 245));


            dateRow.add(lblCheckIn);
            dateRow.add(txtCheckIn, "growx");
            dateRow.add(lblCheckOut);
            dateRow.add(txtCheckOut, "growx");

            bookingsPanel.add(dateRow);
            bookingsPanel.add(Box.createVerticalStrut(15));

            // Create room list panel
            JPanel roomListPanel = new JPanel(new MigLayout("fill, insets 10", "[grow]", "[]5[]"));
            roomListPanel.setBackground(new Color(245, 248, 250));
            roomListPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(HEADER_COLOR, 1, true),
                    new EmptyBorder(5, 5, 5, 5)));

            JLabel lblRoomTitle = createLabel("Danh sách phòng (" + order.getBookings().size() + " phòng):", Font.BOLD);
            lblRoomTitle.setForeground(HEADER_COLOR);
            roomListPanel.add(lblRoomTitle, "wrap");

            // Create room chips/badges
            JPanel roomChipsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
            roomChipsPanel.setBackground(new Color(245, 248, 250));

            for (Booking b : order.getBookings()) {
                String roomNumber = b.getRoom() != null ? b.getRoom().getRoomNumber() : "?";
                JLabel roomChip = new JLabel(" Phòng " + roomNumber + " ");
                roomChip.setFont(new Font("Segoe UI", Font.BOLD, 13));
                roomChip.setForeground(Color.WHITE);
                roomChip.setBackground(HEADER_COLOR);
                roomChip.setOpaque(true);
                roomChip.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(HEADER_COLOR.darker(), 1, true),
                        new EmptyBorder(6, 12, 6, 12)));

                // Store booking ID as client property for later reference
                roomChip.putClientProperty("bookingId", b.getBookingId());

                roomChipsPanel.add(roomChip);
            }

            roomListPanel.add(roomChipsPanel, "grow");
            bookingsPanel.add(roomListPanel);

            // Add note if editable
        }

        ScrollPaneWin11 scroll = new ScrollPaneWin11(bookingsPanel);
        scroll.setPreferredSize(new Dimension(0, 180));
        scroll.setBorder(null);

        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createAmenitiesPanel() {
        JPanel panel = createStyledPanel("DỊCH VỤ");
        panel.setLayout(new BorderLayout(0, 10));

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        toolbar.setBackground(PANEL_BG);

        java.awt.Button btnAdd = createIconButton("+ Thêm", ACCENT_COLOR);
        java.awt.Button btnRemove = createIconButton("- Xóa", DANGER_COLOR);

        btnAdd.addActionListener(e -> onAddAmenity());
        btnRemove.addActionListener(e -> onRemoveAmenity());

        toolbar.add(btnAdd);
        toolbar.add(btnRemove);

        // Table
        String[] columns = {"Mã", "Tên", "Đơn giá", "Số lượng"};
        amenityModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                Long orderTypeId = (order != null && order.getOrderType() != null) ? order.getOrderType().getOrderTypeId() : null;
                return orderTypeId != null && (orderTypeId == 2L || orderTypeId == 3L) && (column >= 2);
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Long.class;
                if (columnIndex == 2) return BigDecimal.class;
                if (columnIndex == 3) return Integer.class;
                return String.class;
            }
        };

        tblAmenities = new JTable(amenityModel);
        styleTable(tblAmenities);

        ScrollPaneWin11 scroll = new ScrollPaneWin11(tblAmenities);
        scroll.setPreferredSize(new Dimension(0, 200));

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSurchargesPanel() {
        JPanel panel = createStyledPanel("PHỤ PHÍ");
        panel.setLayout(new BorderLayout(0, 10));

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        toolbar.setBackground(PANEL_BG);

        java.awt.Button btnAdd = createIconButton("+ Thêm", ACCENT_COLOR);
        java.awt.Button btnRemove = createIconButton("- Xóa", DANGER_COLOR);

        btnAdd.addActionListener(e -> onAddSurcharge());
        btnRemove.addActionListener(e -> onRemoveSurcharge());

        toolbar.add(btnAdd);
        toolbar.add(btnRemove);

        // Table
        String[] columns = {"Mã", "Tên", "Đơn giá", "Số lượng"};
        surchargeModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                Long orderTypeId = (order != null && order.getOrderType() != null) ? order.getOrderType().getOrderTypeId() : null;
                return orderTypeId != null && (orderTypeId == 2L || orderTypeId == 3L) && (column >= 2);
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Long.class;
                if (columnIndex == 2) return BigDecimal.class;
                if (columnIndex == 3) return Integer.class;
                return String.class;
            }
        };

        tblSurcharges = new JTable(surchargeModel);
        styleTable(tblSurcharges);

        ScrollPaneWin11 scroll = new ScrollPaneWin11(tblSurcharges);
        scroll.setPreferredSize(new Dimension(0, 200));

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new MigLayout("fill, insets 10", "[grow][]15[]", "[]"));
        panel.setBackground(PANEL_BG);
        panel.setBorder(new LineBorder(BORDER_COLOR, 1, true));

        java.awt.Button btnSave = createStyledButton("Lưu", HEADER_COLOR);
        java.awt.Button btnCancel = createStyledButton("Hủy", DANGER_COLOR);

        btnSave.addActionListener(e -> onSave(order != null && order.getOrderType() != null ? order.getOrderType().getOrderTypeId() : null));
        btnCancel.addActionListener(e -> dispose());

        // Add "Convert to Processing" button if orderTypeId = 3 (Pre-booking)
        final Long orderTypeId = (order != null && order.getOrderType() != null) ? order.getOrderType().getOrderTypeId() : null;

        if (orderTypeId != null && orderTypeId == 3L) {
            java.awt.Button btnConvertToProcessing = createStyledButton("Chuyển sang đang xử lí", ACCENT_COLOR);
            btnConvertToProcessing.setPreferredSize(new Dimension(200, 40));
            btnConvertToProcessing.addActionListener(e -> onConvertToProcessing());

            panel.add(btnConvertToProcessing);
            panel.add(new JLabel(""), "grow");
            panel.add(btnCancel);
            panel.add(btnSave);
        } else {
            panel.add(new JLabel(""), "grow");
            panel.add(btnCancel);
            panel.add(btnSave);
        }

        return panel;
    }

    // Styling helpers
    private JPanel createStyledPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBackground(PANEL_BG);
        TitledBorder border = BorderFactory.createTitledBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                HEADER_COLOR
        );
        panel.setBorder(BorderFactory.createCompoundBorder(border, new EmptyBorder(10, 10, 10, 10)));
        return panel;
    }

    private JLabel createLabel(String text, int style) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", style == Font.BOLD ? Font.BOLD : Font.PLAIN, 13));
        return label;
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(5, 8, 5, 8)));
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(32);
        table.setSelectionBackground(SELECTED_COLOR);
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(BORDER_COLOR);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));

        // Header styling with custom renderer to ensure blue color shows
        JTableHeader header = table.getTableHeader();
        header.setOpaque(true);
        header.setBackground(HEADER_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(0, 35));
        header.setBorder(BorderFactory.createLineBorder(HEADER_COLOR.darker(), 1));

        // Custom header renderer to override LAF and force blue color
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setBackground(HEADER_COLOR);
                label.setForeground(Color.WHITE);
                label.setFont(new Font("Segoe UI", Font.BOLD, 13));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 2, 1, HEADER_COLOR.darker()),
                        BorderFactory.createEmptyBorder(8, 5, 8, 5)
                ));
                label.setOpaque(true);
                return label;
            }
        });

        // Custom cell renderer
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Hover effect
        table.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row > -1) {
                    table.setSelectionBackground(HOVER_COLOR);
                }
            }
        });
    }

    private java.awt.Button createStyledButton(String text, Color bgColor) {
        java.awt.Button button = new java.awt.Button(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);

        button.setPreferredSize(new Dimension(120, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            Color originalColor = bgColor;

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalColor);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();
                if (button.contains(p)) {
                    button.setBackground(bgColor.brighter());
                } else {
                    button.setBackground(originalColor);
                }
            }
        });

        return button;
    }

    private java.awt.Button createIconButton(String text, Color color) {
        java.awt.Button button = new Button(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);

        button.setPreferredSize(new Dimension(90, 32));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            Color originalColor = color;

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalColor);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(color.darker());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();
                if (button.contains(p)) {
                    button.setBackground(color.brighter());
                } else {
                    button.setBackground(originalColor);
                }
            }
        });

        return button;
    }

    // Data loading
    private void loadExistingDetails() {
        if (order == null) return;
        amenityModel.setRowCount(0);
        surchargeModel.setRowCount(0);

        var amenityDetails = orderDetailService.getOrderDetailsByOrderId(order.getOrderId());
        if (amenityDetails != null) {
            int index = 1;
            for (OrderDetail d : amenityDetails) {
                Long id = d.getAmenity() != null ? d.getAmenity().getAmenityId() : null;
                String name = d.getAmenity() != null ? d.getAmenity().getNameAmenity() : "";
                AmenityDTO amenityDTO = new AmenityDTO(id, name, d.getUnitPrice().doubleValue(), d.getQuantity());
                amenityModel.addRow(new Object[]{index++, amenityDTO, d.getUnitPrice(), d.getQuantity()});
            }
        }

        var surchargeDetails = surchargeDetailService.getSurchargeDetailsByOrderId(order.getOrderId());
        if (surchargeDetails != null) {
            int index = 1;
            for (SurchargeDetail s : surchargeDetails) {
                Long id = s.getSurcharge() != null ? s.getSurcharge().getSurchargeId() : null;
                String name = s.getSurcharge() != null ? s.getSurcharge().getName() : "";
                BigDecimal price = s.getSurcharge() != null ? s.getSurcharge().getPrice() : BigDecimal.ZERO;
                SurchargeDTO surchargeDTO = new SurchargeDTO(id, name, price, s.getQuantity());
                surchargeModel.addRow(new Object[]{index++, surchargeDTO, price, s.getQuantity()});
            }
        }
    }

    // Actions
    private void onAddAmenity() {
        // Create modal dialog with AmenityManagementPanel
        JDialog dialog = new JDialog(this, "Chọn dịch vụ", true);
        dialog.setLayout(new BorderLayout());

        // Create and load amenity panel
        AmenityManagementPanel amenityPanel =
                new AmenityManagementPanel();

        // Load available amenities from repository
        AmenityService amenityService = new AmenityService();
        java.util.List<iuh.fit.se.group1.entity.Amenity> availableAmenities = amenityService.getAllAmenities();

        List<AmenityDTO> existingAmenities = new ArrayList<>();
        for (int i = 0; i < amenityModel.getRowCount(); i++) {
            AmenityDTO amenityDTO = (AmenityDTO) amenityModel.getValueAt(i, 1);
            existingAmenities.add(amenityDTO);
        }


        amenityPanel.loadData(availableAmenities, existingAmenities);

        amenityPanel.getSaveButton().addActionListener(e -> {
            java.util.List<iuh.fit.se.group1.dto.AmenityDTO> selected = amenityPanel.getSelectedTableData();
            amenityModel.setRowCount(0);
            for (iuh.fit.se.group1.dto.AmenityDTO item : selected) {
                amenityModel.addRow(new Object[]{
                        amenityModel.getRowCount() + 1,
                        item,
                        BigDecimal.valueOf(item.getPrice()),
                        item.getQuantity()
                });
            }
            dialog.dispose();
        });

        dialog.add(amenityPanel, BorderLayout.CENTER);
        dialog.setSize(1000, 700);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void onRemoveAmenity() {
        int row = tblAmenities.getSelectedRow();
        if (row >= 0) {
            amenityModel.removeRow(row);
        } else {
            CustomDialog.showMessage(this, "Vui lòng chọn dòng cần xóa trong bảng dịch vụ.", "Thông báo", CustomDialog.MessageType.WARNING, 450, 180);
        }
    }

    private void onAddSurcharge() {
        JDialog dialog = new JDialog(this, "Chọn phụ phí", true);
        dialog.setLayout(new BorderLayout());

        SurchargeManagementPanel surchargePanel =
                new SurchargeManagementPanel();

        SurchargeService surchargeService = new SurchargeService();
        java.util.List<iuh.fit.se.group1.entity.Surcharge> availableSurcharges = surchargeService.getAllSurcharges();

        List<SurchargeDTO> existingSurcharges = new ArrayList<>();
        for (int i = 0; i < surchargeModel.getRowCount(); i++) {
            SurchargeDTO dto = (SurchargeDTO) surchargeModel.getValueAt(i, 1);
            existingSurcharges.add(dto);
        }

        surchargePanel.loadData(availableSurcharges, existingSurcharges);

        // Setup save button to close dialog and add selected items
        surchargePanel.getSaveButton().addActionListener(e -> {
            java.util.List<iuh.fit.se.group1.dto.SurchargeDTO> selected = surchargePanel.getSelectedTableData();
            surchargeModel.setRowCount(0);
            for (iuh.fit.se.group1.dto.SurchargeDTO item : selected) {
                surchargeModel.addRow(new Object[]{
                        surchargeModel.getRowCount() + 1,
                        item,
                        item.getPrice(),
                        item.getQuantity()
                });
            }
            dialog.dispose();
        });

        dialog.add(surchargePanel, BorderLayout.CENTER);
        dialog.setSize(1000, 700);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void onRemoveSurcharge() {
        int row = tblSurcharges.getSelectedRow();
        if (row >= 0) {
            surchargeModel.removeRow(row);
        } else {
            CustomDialog.showMessage(this, "Vui lòng chọn dòng cần xóa trong bảng phụ phí.", "Thông báo", CustomDialog.MessageType.WARNING, 450, 180);
        }
    }

    private void onSave(Long orderTypeId) {
        try {
            boolean allowEditDates = orderTypeId != null && orderTypeId == 3L;

            // Amenities & surcharges save
            boolean allowEditDetails = orderTypeId != null && (orderTypeId == 2L || orderTypeId == 3L);
            if (allowEditDetails) {
                Long orderId = order.getOrderId();

                // Get existing amenities for comparison
                var existingAmenities = orderDetailService.getOrderDetailsByOrderId(orderId);
                List<OrderDetail> newDetails = new ArrayList<>();

                for (int i = 0; i < amenityModel.getRowCount(); i++) {
                    AmenityDTO amenityDTO = (AmenityDTO) amenityModel.getValueAt(i, 1);
                    OrderDetail detail = toOrderDetails(amenityDTO);
                    newDetails.add(detail);
                }

                // Update or insert amenities (keep existing IDs where possible)
                for (OrderDetail newDetail : newDetails) {
                    OrderDetail existing = existingAmenities.stream()
                            .filter(od -> od.getAmenity().getAmenityId().equals(newDetail.getAmenity().getAmenityId()))
                            .findFirst()
                            .orElse(null);

                    if (existing != null) {
                        // Update existing
                        orderDetailService.updateOrderDetailFormOrderId(existing.getAmenity().getAmenityId(), newDetail.getUnitPrice(), newDetail.getQuantity(), orderId);
                    } else {
                        // Insert new
                        orderDetailService.save(newDetail, orderId);
                    }
                }

                // Delete removed amenities
                for (OrderDetail existing : existingAmenities) {
                    boolean stillExists = newDetails.stream()
                            .anyMatch(nd -> nd.getAmenity().getAmenityId().equals(existing.getAmenity().getAmenityId()));
                    if (!stillExists) {
                        orderDetailService.deleteById(existing.getAmenity().getAmenityId(), orderId);
                    }
                }

                // Similar logic for surcharges
                var existingSurcharges = surchargeDetailService.getSurchargeDetailsByOrderId(orderId);
                List<SurchargeDetail> newSurcharges = new ArrayList<>();

                for (int i = 0; i < surchargeModel.getRowCount(); i++) {
                    SurchargeDTO surchargeDTO = (SurchargeDTO) surchargeModel.getValueAt(i, 1);
                    SurchargeDetail sd = toSurchargeDetail(surchargeDTO);
                    newSurcharges.add(sd);
                }

                // Update or insert surcharges
                for (SurchargeDetail newSurcharge : newSurcharges) {
                    SurchargeDetail existing = existingSurcharges.stream()
                            .filter(sd -> sd.getSurcharge().getSurchargeId() == newSurcharge.getSurcharge().getSurchargeId())
                            .findFirst()
                            .orElse(null);

                    if (existing != null) {
                        surchargeDetailService.updateSurchargeDetail(existing.getSurcharge().getSurchargeId(), newSurcharge.getQuantity(), orderId);
                    } else {
                        surchargeDetailService.save(newSurcharge, orderId);
                    }
                }

                // Delete removed surcharges
                for (SurchargeDetail existing : existingSurcharges) {
                    boolean stillExists = newSurcharges.stream()
                            .anyMatch(ns -> ns.getSurcharge().getSurchargeId() == existing.getSurcharge().getSurchargeId());
                    if (!stillExists) {
                        surchargeDetailService.deleteById(existing.getSurcharge().getSurchargeId(), orderId);
                    }
                }

                // Recalculate and update order total price
                orderService.recalculateOrderTotal(orderId);
            }

            CustomDialog.showMessage(this, "Lưu thông tin hóa đơn thành công!", "Thành công", CustomDialog.MessageType.SUCCESS, 400, 180);
            dispose();
        } catch (Exception ex) {
            CustomDialog.showMessage(this, "Lỗi khi lưu thông tin hóa đơn: " + ex.getMessage(), "Lỗi", CustomDialog.MessageType.ERROR, 500, 200);
        }
    }

    private SurchargeDetail toSurchargeDetail(SurchargeDTO surchargeDTO) {
        SurchargeDetail detail = new SurchargeDetail();

        detail.setSurcharge(new Surcharge(surchargeDTO.getSurchargeId()));
        detail.setQuantity(surchargeDTO.getQuantity());
        return detail;
    }

    private OrderDetail toOrderDetails(AmenityDTO amenityDTO) {
        OrderDetail detail = new OrderDetail();

        detail.setAmenity(new Amenity(amenityDTO.getId()));
        detail.setUnitPrice(BigDecimal.valueOf(amenityDTO.getPrice()));
        detail.setQuantity(amenityDTO.getQuantity());
        return detail;
    }

    private void onConvertToProcessing() {

        // kiểm tra thời gian hiện tại so với thời gian check-in của booking đầu tiên
        Booking firstBooking = order.getBookings().get(0);
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(firstBooking.getCheckInDate())) {
            CustomDialog.showMessage(
                    this,
                    "Không thể chuyển trạng thái hóa đơn vì thời gian hiện tại chưa đến ngày check-in.",
                    "Lỗi",
                    CustomDialog.MessageType.ERROR,
                    700,
                    180
            );
            return;
        }

        int confirm = CustomDialog.showConfirm(
                this,
                "Bạn có chắc muốn chuyển hóa đơn từ 'Đặt trước' sang 'Đang xử lí'?\nTrạng thái phòng sẽ được cập nhật sang 'Đang sử dụng'.",
                "Xác nhận chuyển trạng thái",
                CustomDialog.MessageType.WARNING,
                850,
                200
        );

        if (confirm == JOptionPane.OK_OPTION) {
            try {
                // Change orderTypeId from 3 (Pre-booking) to 2 (Processing)
                orderService.updateOrderType(order.getOrderId(), 2L);

                // Update room status to OCCUPIED
                RoomService roomService = new RoomService();
                List<Long> roomIds = order.getBookings().stream()
                        .map(b -> b.getRoom().getRoomId())
                        .toList();
                roomService.updateRoomStatusBatch(roomIds, iuh.fit.se.group1.enums.RoomStatus.OCCUPIED);

                CustomDialog.showMessage(
                        this,
                        "Đã chuyển hóa đơn sang trạng thái 'Đang xử lí' thành công!\nCác phòng đã được đánh dấu là 'Đang sử dụng'.",
                        "Thành công",
                        CustomDialog.MessageType.SUCCESS,
                        850,
                        200
                );
                dispose();
            } catch (Exception ex) {
                CustomDialog.showMessage(
                        this,
                        "Lỗi khi chuyển trạng thái: " + ex.getMessage(),
                        "Lỗi",
                        CustomDialog.MessageType.ERROR,
                        300,
                        200
                );
            }
        }
    }
}

