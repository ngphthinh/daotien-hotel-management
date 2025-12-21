package iuh.fit.se.group1.ui.component.custom;

import iuh.fit.se.group1.entity.*;
import iuh.fit.se.group1.service.*;
import iuh.fit.se.group1.ui.component.scroll.ScrollPaneWin11;
import iuh.fit.se.group1.util.Constants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import javax.swing.JFormattedTextField;
import javax.swing.text.NumberFormatter;
import java.util.List;

/**
 * Panel hiển thị chi tiết hóa đơn đầy đủ
 */
public class InvoicePanel extends JPanel {
    private Order order;
    private List<OrderDetail> orderDetails;
    private List<SurchargeDetail> surchargeDetails;
    // services to load details
    private BookingService bookingService = new BookingService();
    private final OrderDetailService orderDetailService = new OrderDetailService();
    private final SurchargeDetailService surchargeDetailService = new SurchargeDetailService();
    private final PromotionService promotionService = new PromotionService();
    // Header components
    private JLabel lblInvoiceTitle;
    private JLabel lblInvoiceId;
    private JLabel lblInvoiceDate;

    // Customer info
    private JLabel lblCustomerName;
    private JLabel lblCustomerPhone;
    private JLabel lblCustomerCitizenId;
    private JLabel lblCustomerEmail;

    // Employee info
    private JLabel lblEmployeeName;

    // Room booking info
    private JTable tblRoomBookings;
    private DefaultTableModel roomBookingModel;

    // Amenity (dịch vụ) info
    private JTable tblAmenities;
    private DefaultTableModel amenityModel;

    // Surcharge (phụ phí) info
    private JTable tblSurcharges;
    private DefaultTableModel surchargeModel;

    // Promotion info
    private JLabel lblPromotionName;
    private JLabel lblPromotionDiscount;

    // Summary
    private JLabel lblSubtotal;
    private JLabel lblPromotionAmount;
    private JLabel lblTotalAmount;
    private JLabel lblDeposit;
    private JLabel lblFinalAmount;
    private JLabel lblPaymentType;
    private JLabel lblPaymentDate;

    // Edit mode flag
    private boolean editMode = false;

    // color palette (centralized for easy tweaking)
    private static final Color PALETTE_NAVY = new Color(11, 76, 124); // deep navy
    private static final Color PALETTE_ACCENT = new Color(0, 137, 123); // teal accent if needed
    private static final Color PALETTE_LIGHT = new Color(244, 246, 248);
    private static final Color PALETTE_ROW_ALT = new Color(249, 250, 251);
    private static final Color PALETTE_SELECTION = new Color(183, 225, 250);
    private static final Color PALETTE_EDIT_HIGHLIGHT = new Color(229, 247, 255);
    private JLabel lblEmployeePayment;
    private EmployeeService employeeService = new EmployeeService();

    public InvoicePanel() {
        initComponents();
        setupLayout();
    }

    public InvoicePanel(Order order) {
        this();
        setOrder(order);
    }

    private void initComponents() {
        // Softer background instead of pure white
        setBackground(PALETTE_LIGHT);
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        lblInvoiceTitle = createBoldLabel("HÓA ĐƠN THANH TOÁN", 24, PALETTE_NAVY);
        lblInvoiceId = createLabel("Mã hóa đơn: ", 14);
        lblInvoiceDate = createLabel("Ngày tạo: ", 14);

        // Customer
        lblCustomerName = createLabel("", 13);
        lblCustomerPhone = createLabel("", 13);
        lblCustomerCitizenId = createLabel("", 13);
        lblCustomerEmail = createLabel("", 13);

        // Employee
        lblEmployeeName = createLabel("", 13);
        lblEmployeePayment = createLabel("", 13);
        // Tables
        setupRoomBookingTable();
        setupAmenityTable();
        setupSurchargeTable();

        // Promotion
        lblPromotionName = createLabel("", 13);
        lblPromotionDiscount = createLabel("", 13);

        // Summary
        lblSubtotal = createLabel("", 14);
        lblPromotionAmount = createLabel("", 14);
        lblTotalAmount = createBoldLabel("", 16, new Color(208, 67, 56));
        lblDeposit = createLabel("", 14);
        lblFinalAmount = createBoldLabel("", 18, new Color(39, 174, 96));
        lblPaymentType = createLabel("", 13);
        lblPaymentDate = createLabel("", 13);
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(PALETTE_LIGHT);

        // Header section
        mainPanel.add(createHeaderSection());
        mainPanel.add(Box.createVerticalStrut(20));

        // Info section (Customer + Employee)
        mainPanel.add(createInfoSection());
        mainPanel.add(Box.createVerticalStrut(20));

        // Room bookings section
        mainPanel.add(createSectionPanel("THÔNG TIN PHÒNG", new ScrollPaneWin11(tblRoomBookings)));
        mainPanel.add(Box.createVerticalStrut(15));

        // Amenities section
        mainPanel.add(createSectionPanel("DỊCH VỤ", new ScrollPaneWin11(tblAmenities)));
        mainPanel.add(Box.createVerticalStrut(15));

        // Surcharges section
        mainPanel.add(createSectionPanel("PHỤ PHÍ", new ScrollPaneWin11(tblSurcharges)));
        mainPanel.add(Box.createVerticalStrut(15));

        // Promotion section
        mainPanel.add(createPromotionSection());
        mainPanel.add(Box.createVerticalStrut(20));

        // Summary section
        mainPanel.add(createSummarySection());

        ScrollPaneWin11 scrollPane = new ScrollPaneWin11(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(255, 255, 255));
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(PALETTE_NAVY, 2),
                new EmptyBorder(15, 15, 15, 15)));

        lblInvoiceTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblInvoiceTitle);
        panel.add(Box.createVerticalStrut(10));

        JPanel infoPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        infoPanel.setBackground(new Color(255, 255, 255));
        infoPanel.add(lblInvoiceId);
        infoPanel.add(lblInvoiceDate);
        panel.add(infoPanel);

        return panel;
    }

    private JPanel createInfoSection() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 0));
        panel.setBackground(PALETTE_LIGHT);

        // Customer info
        JPanel customerPanel = createInfoPanel("THÔNG TIN KHÁCH HÀNG",
                new String[] { "Họ tên:", "Số điện thoại:", "CCCD/Passport:", "Email:" },
                new JLabel[] { lblCustomerName, lblCustomerPhone, lblCustomerCitizenId, lblCustomerEmail });

        // Employee info
        JPanel employeePanel = createInfoPanel("NHÂN VIÊN XỬ LÝ",
                new String[] { "Họ tên:", "Nhân viên thanh toán" },
                new JLabel[] { lblEmployeeName, lblEmployeePayment });

        panel.add(customerPanel);
        panel.add(employeePanel);

        return panel;
    }

    private JPanel createInfoPanel(String title, String[] labels, JLabel[] values) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(255, 255, 255));
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 233, 235), 1),
                new EmptyBorder(15, 15, 15, 15)));

        JLabel titleLabel = createBoldLabel(title, 14, PALETTE_NAVY.darker());
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));

        for (int i = 0; i < labels.length; i++) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            row.setBackground(new Color(255, 255, 255));

            JLabel label = createBoldLabel(labels[i] + " ", 13, new Color(80, 95, 110));
            row.add(label);
            row.add(values[i]);

            row.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(row);
            if (i < labels.length - 1) {
                panel.add(Box.createVerticalStrut(5));
            }
        }

        return panel;
    }

    private JPanel createSectionPanel(String title, JComponent content) {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(PALETTE_LIGHT);

        JLabel titleLabel = createBoldLabel(title, 14, new Color(33, 43, 54));
        panel.add(titleLabel, BorderLayout.NORTH);

        content.setPreferredSize(new Dimension(0, 120));
        panel.add(content, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createPromotionSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(250, 249, 240));
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(255, 210, 102), 2),
                new EmptyBorder(10, 15, 10, 15)));

        JLabel titleLabel = createBoldLabel("KHUYẾN MÃI", 14, new Color(220, 140, 30));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(5));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row1.setBackground(new Color(250, 249, 240));
        row1.add(createBoldLabel("Tên chương trình: ", 13, new Color(80, 95, 110)));
        row1.add(lblPromotionName);
        row1.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(row1);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row2.setBackground(new Color(250, 249, 240));
        row2.add(createBoldLabel("Giảm giá: ", 13, new Color(80, 95, 110)));
        row2.add(lblPromotionDiscount);
        row2.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(row2);

        return panel;
    }

    private JPanel createSummarySection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(244, 246, 247));
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(33, 43, 54), 2),
                new EmptyBorder(15, 15, 15, 15)));

        addSummaryRow(panel, "Tạm tính:", lblSubtotal, false);
        addSummaryRow(panel, "Khuyến mãi:", lblPromotionAmount, false);
        addSummaryRow(panel, "Tổng cộng:", lblTotalAmount, true);
        addSummaryRow(panel, "Đã đặt cọc:", lblDeposit, false);

        panel.add(Box.createVerticalStrut(10));
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(200, 210, 215));
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(separator);
        panel.add(Box.createVerticalStrut(10));

        addSummaryRow(panel, "THANH TOÁN:", lblFinalAmount, true);

        panel.add(Box.createVerticalStrut(10));
        addSummaryRow(panel, "Hình thức:", lblPaymentType, false);
        addSummaryRow(panel, "Ngày thanh toán:", lblPaymentDate, false);

        return panel;
    }

    private void addSummaryRow(JPanel parent, String label, JLabel valueLabel, boolean bold) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(new Color(244, 246, 247));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblLabel = bold ? createBoldLabel(label, 15, new Color(33, 43, 54)) : createLabel(label, 14);

        row.add(lblLabel, BorderLayout.WEST);
        row.add(valueLabel, BorderLayout.EAST);

        parent.add(row);
        parent.add(Box.createVerticalStrut(5));
    }

    private void setupRoomBookingTable() {
        String[] columns = { "Số phòng", "Loại", "Check-in", "Check-out", "Loại đặt", "Đơn giá" };
        roomBookingModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblRoomBookings = createStyledTable(roomBookingModel);
    }

    private void setupAmenityTable() {
        String[] columns = { "Dịch vụ", "Đơn giá", "Số lượng", "Thành tiền" };
        amenityModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Allow editing price (col 1) and quantity (col 2) when in editMode
                if (!editMode)
                    return false;
                return column == 1 || column == 2;
            }
        };

        tblAmenities = createStyledTable(amenityModel);

        // Attach listener to update totals when edited
        amenityModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    if (row >= 0) {
                        updateRowTotal(amenityModel, row);
                        updateSummaryFromTableTotals();
                    }
                }
            }
        });
    }

    private void setupSurchargeTable() {
        String[] columns = { "Phụ phí", "Đơn giá", "Số lượng", "Thành tiền" };
        surchargeModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (!editMode)
                    return false;
                return column == 1 || column == 2;
            }
        };

        tblSurcharges = createStyledTable(surchargeModel);

        surchargeModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    if (row >= 0) {
                        updateRowTotal(surchargeModel, row);
                        updateSummaryFromTableTotals();
                    }
                }
            }
        });
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        // updated header color to deep navy
        // table.getTableHeader().setBackground(PALETTE_NAVY);
        // table.getTableHeader().setForeground(Color.WHITE);
        // Use a header renderer so background shows across LAFs
        JTableHeader header = table.getTableHeader();
        header.setBackground(PALETTE_NAVY);
        header.setOpaque(true);
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected,
                    boolean hasFocus, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, col);
                lbl.setBackground(PALETTE_NAVY);
                lbl.setForeground(Color.WHITE);
                lbl.setOpaque(true);
                lbl.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                return lbl;
            }
        });
        header.repaint();
        table.getTableHeader().setReorderingAllowed(false);
        table.setGridColor(new Color(240, 243, 245));
        table.setSelectionBackground(PALETTE_SELECTION);
        // make table cells white with alternating subtle rows
        table.setBackground(Color.WHITE);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        // Custom renderer for alternating rows and edit highlighting
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (isSelected) {
                    c.setBackground(PALETTE_SELECTION);
                    c.setForeground(Color.BLACK);
                } else {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(PALETTE_ROW_ALT);
                    }
                    // highlight editable cells in edit mode
                    try {
                        if (editMode && table.getModel().isCellEditable(row, column)) {
                            c.setBackground(PALETTE_EDIT_HIGHLIGHT);
                        }
                    } catch (Exception ignore) {
                    }
                    c.setForeground(new Color(34, 47, 62));
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        return table;
    }

    private JLabel createLabel(String text, int fontSize) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, fontSize));
        label.setForeground(new Color(33, 43, 54));
        return label;
    }

    private JLabel createBoldLabel(String text, int fontSize, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        label.setForeground(color);
        return label;
    }

    public void setOrder(Order order) {
        this.order = order;
        if (order != null) {
            updateInvoiceData();
        }
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
        // update table editors
        enableTableEditors();
        // make sure models notify UI about editability change
        amenityModel.fireTableStructureChanged();
        surchargeModel.fireTableStructureChanged();

        // Apply models to existing tables (don't recreate table components)
        if (tblAmenities != null) {
            tblAmenities.setModel(amenityModel);
            enableTableEditors();
            tblAmenities.updateUI();
        }
        if (tblSurcharges != null) {
            tblSurcharges.setModel(surchargeModel);
            enableTableEditors();
            tblSurcharges.updateUI();
        }
    }

    private void enableTableEditors() {
        try {
            // Integer editor for quantity
            NumberFormat intFormat = NumberFormat.getIntegerInstance();
            NumberFormatter intFormatter = new NumberFormatter(intFormat);
            intFormatter.setAllowsInvalid(false);
            intFormatter.setMinimum(0);
            JFormattedTextField intField = new JFormattedTextField(intFormatter);
            DefaultCellEditor intEditor = new DefaultCellEditor(intField);

            // Decimal editor for price
            DecimalFormat decFmt = new DecimalFormat("#,##0.00");
            NumberFormatter decFormatter = new NumberFormatter(decFmt);
            decFormatter.setAllowsInvalid(false);
            decFormatter.setMinimum(0.0);
            JFormattedTextField priceField = new JFormattedTextField(decFormatter);
            DefaultCellEditor priceEditor = new DefaultCellEditor(priceField);

            if (tblAmenities != null && tblAmenities.getColumnCount() >= 3) {
                tblAmenities.getColumnModel().getColumn(1).setCellEditor(priceEditor);
                tblAmenities.getColumnModel().getColumn(2).setCellEditor(intEditor);
            }
            if (tblSurcharges != null && tblSurcharges.getColumnCount() >= 3) {
                tblSurcharges.getColumnModel().getColumn(1).setCellEditor(priceEditor);
                tblSurcharges.getColumnModel().getColumn(2).setCellEditor(intEditor);
            }
        } catch (Exception ex) {
            // ignore editor setup failures
        }
    }

    private void updateInvoiceData() {
        // Header
        lblInvoiceId.setText("Mã hóa đơn: #" + order.getOrderId());
        lblInvoiceDate.setText("Ngày tạo: " +
                order.getOrderDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

        // Customer
        Customer customer = order.getCustomer();
        if (customer != null) {
            lblCustomerName.setText(customer.getFullName());
            lblCustomerPhone.setText(customer.getPhone());
            lblCustomerCitizenId.setText(customer.getCitizenId());
            lblCustomerEmail.setText(customer.getEmail());
        }

        // Employee
        Employee employee = order.getEmployee();
        if (employee != null) {
            lblEmployeeName.setText(employee.getFullName());
        }
        Employee employeePayment = employeeService.getEmployeeById(order.getEmployeePayment().getEmployeeId());
        if (employeePayment != null) {
            lblEmployeePayment.setText(employeePayment.getFullName());
        }

        // Room bookings
        updateRoomBookings();

        // Amenities (load via OrderDetailService)
        updateAmenities();

        // Surcharges (load via SurchargeDetailService)
        updateSurcharges();

        // Promotion
        updatePromotion();

        // Summary
        updateSummary();
    }

    private void updateRoomBookings() {
        roomBookingModel.setRowCount(0);

        if (order.getBookings() != null) {
            for (Booking booking : order.getBookings()) {
                Room room = booking.getRoom();
                BigDecimal price = calculateRoomPrice(booking);

                roomBookingModel.addRow(new Object[] {
                        room.getRoomNumber(),
                        room.getRoomType().getName(),
                        booking.getCheckInDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                        booking.getCheckOutDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                        booking.getBookingType().getDisplayName(),
                        bookingService.getPriceFromBooking(booking)
                });
            }
        }
    }

    private BigDecimal calculateRoomPrice(Booking booking) {
        // This is a simplified calculation - adjust according to your business logic
        RoomType roomType = booking.getRoom().getRoomType();
        switch (booking.getBookingType()) {
            case HOURLY:
                return roomType.getHourlyRate();
            case OVERNIGHT:
                return roomType.getOvernightRate();
            case DAILY:
                return roomType.getDailyRate();
            default:
                return BigDecimal.ZERO;
        }
    }

    private void updateAmenities() {
        amenityModel.setRowCount(0);
        if (order == null || order.getOrderId() == null)
            return;

        List<OrderDetail> details = orderDetailService.getOrderDetailsByOrderId(order.getOrderId());
        if (details != null) {
            for (OrderDetail d : details) {
                BigDecimal unitPrice = d.getUnitPrice() != null ? d.getUnitPrice() : BigDecimal.ZERO;
                int qty = d.getQuantity();
                BigDecimal total = unitPrice.multiply(BigDecimal.valueOf(qty));
                String amenityName = d.getAmenity() != null ? d.getAmenity().getNameAmenity() : "-";
                amenityModel.addRow(new Object[] { amenityName, Constants.VND_FORMAT.format(unitPrice), qty,
                        Constants.VND_FORMAT.format(total) });
            }
        }
    }

    private void updateSurcharges() {
        surchargeModel.setRowCount(0);
        if (order == null || order.getOrderId() == null)
            return;

        List<SurchargeDetail> details = surchargeDetailService.getSurchargeDetailsByOrderId(order.getOrderId());
        if (details != null) {
            for (SurchargeDetail d : details) {
                BigDecimal unitPrice = d.getSurcharge() != null && d.getSurcharge().getPrice() != null
                        ? d.getSurcharge().getPrice()
                        : BigDecimal.ZERO;
                int qty = d.getQuantity();
                BigDecimal total = unitPrice.multiply(BigDecimal.valueOf(qty));
                String name = d.getSurcharge() != null ? d.getSurcharge().getName() : "-";
                surchargeModel.addRow(new Object[] { name, Constants.VND_FORMAT.format(unitPrice), qty,
                        Constants.VND_FORMAT.format(total) });
            }
        }
    }

    private void updatePromotion() {
        Promotion promotion = promotionService.getPromotionById(order.getPromotion().getPromotionId());

        if (promotion != null) {
            lblPromotionName.setText(promotion.getPromotionName());
            lblPromotionDiscount.setText(promotion.getDiscountPercent() + "%");
        } else {
            lblPromotionName.setText("Không áp dụng");
            lblPromotionDiscount.setText("0%");
        }
    }

    private void updateSummary() {
        // Default behavior: use order totals when not in edit mode
        if (!editMode) {
            BigDecimal totalAmount = order.getTotalAmount();
            BigDecimal deposit = order.getDeposit() != null ? order.getDeposit() : BigDecimal.ZERO;

            // Calculate promotion discount
            BigDecimal promotionAmount = BigDecimal.ZERO;
            Promotion promotion = promotionService.getPromotionById(order.getPromotion().getPromotionId());
            if (promotion != null) {

                float discountPercent = promotion.getDiscountPercent();
                promotionAmount = totalAmount.multiply(BigDecimal.valueOf(discountPercent / 100));
            }

            BigDecimal subtotal = totalAmount.add(promotionAmount); // subtotal before discount
            BigDecimal finalAmount = totalAmount.subtract(deposit);

            lblSubtotal.setText(Constants.VND_FORMAT.format(subtotal));
            lblPromotionAmount.setText("-" + Constants.VND_FORMAT.format(promotionAmount));
            lblTotalAmount.setText(Constants.VND_FORMAT.format(totalAmount));
            lblDeposit.setText(Constants.VND_FORMAT.format(deposit));
            lblFinalAmount.setText(Constants.VND_FORMAT.format(finalAmount));

            // Payment info
            if (order.getPaymentType() != null) {
                lblPaymentType.setText(order.getPaymentType().toString());
            } else {
                lblPaymentType.setText("Chưa thanh toán");
            }

            if (order.getPaymentDate() != null) {
                lblPaymentDate.setText(order.getPaymentDate().format(Constants.DATE_FORMATTER));
            } else {
                lblPaymentDate.setText("Chưa thanh toán");
            }
        } else {
            // In edit mode recalc from tables
            updateSummaryFromTableTotals();
        }
    }

    private void updateRowTotal(DefaultTableModel model, int row) {
        Object priceObj = model.getValueAt(row, 1);
        Object qtyObj = model.getValueAt(row, 2);

        BigDecimal price = parseCurrency(priceObj);
        int qty = parseInt(qtyObj);
        BigDecimal total = price.multiply(BigDecimal.valueOf(qty));

        model.setValueAt(Constants.VND_FORMAT.format(total), row, 3);
    }

    private void updateSummaryFromTableTotals() {
        BigDecimal amenityTotal = BigDecimal.ZERO;
        for (int i = 0; i < amenityModel.getRowCount(); i++) {
            amenityTotal = amenityTotal.add(parseCurrency(amenityModel.getValueAt(i, 3)));
        }

        BigDecimal surchargeTotal = BigDecimal.ZERO;
        for (int i = 0; i < surchargeModel.getRowCount(); i++) {
            surchargeTotal = surchargeTotal.add(parseCurrency(surchargeModel.getValueAt(i, 3)));
        }

        BigDecimal roomTotal = getRoomTotal();

        BigDecimal promotionAmount = BigDecimal.ZERO;
        Promotion promotion = promotionService.getPromotionById(order.getPromotion().getPromotionId());
        if (promotion != null) {
            float discountPercent = promotion.getDiscountPercent();
            promotionAmount = roomTotal.multiply(BigDecimal.valueOf(discountPercent / 100));
        }

        BigDecimal subtotal = roomTotal.add(amenityTotal).add(surchargeTotal);
        BigDecimal totalAfterPromotion = subtotal.subtract(promotionAmount);
        BigDecimal deposit = order.getDeposit() != null ? order.getDeposit() : BigDecimal.ZERO;
        BigDecimal finalAmount = totalAfterPromotion.subtract(deposit);

        lblSubtotal.setText(Constants.VND_FORMAT.format(subtotal));
        lblPromotionAmount.setText("-" + Constants.VND_FORMAT.format(promotionAmount));
        lblTotalAmount.setText(Constants.VND_FORMAT.format(totalAfterPromotion));
        lblDeposit.setText(Constants.VND_FORMAT.format(deposit));
        lblFinalAmount.setText(Constants.VND_FORMAT.format(finalAmount));
    }

    private BigDecimal getRoomTotal() {
        BigDecimal sum = BigDecimal.ZERO;
        if (order.getBookings() != null) {
            for (Booking b : order.getBookings()) {
                sum = sum.add(calculateRoomPrice(b));
            }
        }
        return sum;
    }

    private BigDecimal parseCurrency(Object obj) {
        if (obj == null)
            return BigDecimal.ZERO;
        String s = obj.toString().replaceAll("[^\\d.-]", "");
        if (s.isEmpty())
            return BigDecimal.ZERO;
        try {
            return new BigDecimal(s);
        } catch (Exception ex) {
            return BigDecimal.ZERO;
        }
    }

    private int parseInt(Object obj) {
        if (obj == null)
            return 0;
        String s = obj.toString().replaceAll("[^\\d-]", "");
        if (s.isEmpty())
            return 0;
        try {
            return Integer.parseInt(s);
        } catch (Exception ex) {
            return 0;
        }
    }

    public Order getOrder() {
        return order;
    }
}
