package iuh.fit.se.group1.ui.component.paymentv2;


import iuh.fit.se.group1.dto.BookingViewDTO;
import iuh.fit.se.group1.util.Constants;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

/**
 * Custom table for room bookings with checkbox selection
 */
public class CustomTableRoom extends JTable {

    private static final Color HEADER_COLOR = new Color(74, 144, 226);
    private static final Color ALTERNATE_ROW_COLOR = new Color(248, 249, 250);
    private static final Color SELECTION_COLOR = new Color(230, 240, 255);
    private static final Color BORDER_COLOR = new Color(222, 226, 230);
    private static final Color HOVER_COLOR = new Color(240, 245, 250);

    // Callback invoked when checkbox selection changes. The double parameter is the total price of selected rows.
    private Consumer<Double> onSelectionChanged;

    public CustomTableRoom() {
        initializeTable();
        setupTableModel();
        applyCustomStyling();
        addSampleData();
    }

    private void initializeTable() {
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setRowHeight(35);
        setIntercellSpacing(new Dimension(0, 1));
        setShowGrid(true);
        setGridColor(BORDER_COLOR);
        setSelectionBackground(SELECTION_COLOR);
        setSelectionForeground(Color.BLACK);
        setFillsViewportHeight(true);
    }

    private void setupTableModel() {
        String[] columnNames = {
                "Chọn",
                "Số phòng",
                "Ngày check-in",
                "Ngày check-out",
                "Giá phòng"
        };

        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class;
                }
                return Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // Only checkbox column is editable
            }
        };

        setModel(model);

        // Listen for changes (checkbox toggles) and notify listener
        model.addTableModelListener(e -> recalcAndNotify());
    }

    private void applyCustomStyling() {
        // Header styling
        JTableHeader header = getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setForeground(Color.WHITE);
        header.setBackground(HEADER_COLOR);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 35));
        header.setBorder(BorderFactory.createEmptyBorder());
        header.setDefaultRenderer(new HeaderRenderer());

        // Set column widths
        getColumnModel().getColumn(0).setPreferredWidth(80);  // Chọn
        getColumnModel().getColumn(0).setMaxWidth(80);
        getColumnModel().getColumn(1).setPreferredWidth(120); // Số phòng
        getColumnModel().getColumn(2).setPreferredWidth(150); // Ngày check-in
        getColumnModel().getColumn(3).setPreferredWidth(150); // Ngày check-out
        getColumnModel().getColumn(4).setPreferredWidth(150); // Giá phòng

        // Apply custom renderers
        setDefaultRenderer(Object.class, new AlternatingRowRenderer());
        getColumnModel().getColumn(0).setCellRenderer(new CheckboxRenderer());
        getColumnModel().getColumn(1).setCellRenderer(new RoomNumberRenderer());
        getColumnModel().getColumn(2).setCellRenderer(new DateRenderer());
        getColumnModel().getColumn(3).setCellRenderer(new DateRenderer());
        getColumnModel().getColumn(4).setCellRenderer(new PriceRenderer());
    }

    private void addSampleData() {
        DefaultTableModel model = (DefaultTableModel) getModel();

        // Sample data (matching columns)
        Object[][] sampleData = {
                {false, "101", "01/01/2025 14:00", "02/01/2025 12:00", 100000.0},
                {true, "102", "01/01/2025 15:00", "02/01/2025 12:00", 150000.0},
        };

        for (Object[] row : sampleData) {
            model.addRow(row);
        }
    }

    /**
     * Register a listener that will be called when checkbox selection changes.
     * The listener receives the total price of selected rows.
     */
    public void setOnSelectionChanged(Consumer<Double> listener) {
        this.onSelectionChanged = listener;
    }

    /**
     * Recalculate total of selected rows and notify listener.
     */
    private void recalcAndNotify() {
        DefaultTableModel model = (DefaultTableModel) getModel();
        double total = 0.0;

        for (int i = 0; i < model.getRowCount(); i++) {
            Object selObj = model.getValueAt(i, 0);
            boolean isSelected = selObj instanceof Boolean && (Boolean) selObj;
            if (isSelected) {
                Object priceObj = model.getValueAt(i, 4); // price column index
                if (priceObj instanceof Number) {
                    total += ((Number) priceObj).doubleValue();
                } else if (priceObj instanceof String) {
                    try {
                        total += Constants.parseVND((String) priceObj);
                    } catch (Exception ignored) {
                    }
                }
            }
        }

        if (onSelectionChanged != null) {
            onSelectionChanged.accept(total);
        }
    }

    /**
     * Add new row to table
     */
    public void addRow(boolean selected, BookingViewDTO booking, double price) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        String checkInRoom = booking.getCheckInDate()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));


        String checkOutRoom = booking.getCheckOutDate()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));


        model.addRow(new Object[]{selected, booking, checkInRoom, checkOutRoom, price});
    }

    /**
     * Clear all data
     */
    public void clearData() {
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.setRowCount(0);
    }

    /**
     * Get selected services
     */
    public java.util.List<BookingViewDTO> getSelectedRoom() {
        java.util.List<BookingViewDTO> selectedRoom = new java.util.ArrayList<>();
        DefaultTableModel model = (DefaultTableModel) getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            Boolean isSelected = (Boolean) model.getValueAt(i, 0);
            if (isSelected != null && isSelected) {
                BookingViewDTO selected = (BookingViewDTO) model.getValueAt(i, 1);
                selectedRoom.add(selected);
            }
        }
        return selectedRoom;
    }

    /**
     * Calculate total price of selected services
     */
    public double getTotalPrice() {
        DefaultTableModel model = (DefaultTableModel) getModel();
        double total = 0;

        for (int i = 0; i < model.getRowCount(); i++) {
            Boolean isSelected = (Boolean) model.getValueAt(i, 0);
            if (isSelected != null && isSelected) {
                Object priceObj = model.getValueAt(i, 4); // fixed column index
                if (priceObj instanceof Number) {
                    total += ((Number) priceObj).doubleValue();
                } else if (priceObj instanceof String) {
                    try {
                        total += Constants.parseVND((String) priceObj);
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        return total;
    }

    /**
     * Alternating row renderer
     */
    class AlternatingRowRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {

            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

            if (isSelected) {
                setBackground(SELECTION_COLOR);
                setForeground(Color.BLACK);
            } else {
                if (row % 2 == 0) {
                    setBackground(Color.WHITE);
                } else {
                    setBackground(ALTERNATE_ROW_COLOR);
                }
                setForeground(Color.BLACK);
            }

            return this;
        }
    }

    /**
     * Room number renderer - centered with nice formatting
     */
    class RoomNumberRenderer extends DefaultTableCellRenderer {

        public RoomNumberRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {

            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

            if (isSelected) {
                setBackground(SELECTION_COLOR);
                setForeground(Color.BLACK);
            } else {
                if (row % 2 == 0) {
                    setBackground(Color.WHITE);
                } else {
                    setBackground(ALTERNATE_ROW_COLOR);
                }
                setForeground(new Color(52, 58, 64)); // Dark gray
            }

            return this;
        }
    }

    /**
     * Date renderer - centered with nice formatting
     */
    class DateRenderer extends DefaultTableCellRenderer {

        public DateRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {

            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

            if (isSelected) {
                setBackground(SELECTION_COLOR);
                setForeground(Color.BLACK);
            } else {
                if (row % 2 == 0) {
                    setBackground(Color.WHITE);
                } else {
                    setBackground(ALTERNATE_ROW_COLOR);
                }
                setForeground(new Color(52, 58, 64)); // Dark gray
            }

            return this;
        }
    }

    /**
     * Price renderer - right aligned with currency formatting
     */
    class PriceRenderer extends DefaultTableCellRenderer {
        private NumberFormat currencyFormat;

        public PriceRenderer() {
            currencyFormat = Constants.VND_FORMAT;
            setHorizontalAlignment(SwingConstants.RIGHT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {

            if (value instanceof Number) {
                value = currencyFormat.format(((Number) value).longValue());
            }

            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 20));

            if (isSelected) {
                setBackground(SELECTION_COLOR);
                setForeground(Color.BLACK);
            } else {
                if (row % 2 == 0) {
                    setBackground(Color.WHITE);
                } else {
                    setBackground(ALTERNATE_ROW_COLOR);
                }
                setForeground(new Color(40, 167, 69)); // Green for price
            }

            return this;
        }
    }

    /**
     * Checkbox renderer - centered with nice styling
     */
    class CheckboxRenderer extends JCheckBox implements TableCellRenderer {

        public CheckboxRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {

            if (isSelected) {
                setBackground(SELECTION_COLOR);
                setForeground(table.getSelectionForeground());
            } else {
                if (row % 2 == 0) {
                    setBackground(Color.WHITE);
                } else {
                    setBackground(ALTERNATE_ROW_COLOR);
                }
                setForeground(table.getForeground());
            }

            setSelected((value != null && (Boolean) value));
            setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

            return this;
        }
    }

    /**
     * Custom header renderer
     */
    class HeaderRenderer extends DefaultTableCellRenderer {

        public HeaderRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {

            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            setBackground(HEADER_COLOR);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 15));
            setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
            setOpaque(true);

            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(HEADER_COLOR);
            g.fillRect(0, 0, getWidth(), getHeight());
            super.paintComponent(g);
        }
    }
}

