package iuh.fit.se.group1.ui.component.paymentv2;

import iuh.fit.se.group1.util.Constants;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Custom table for payment management with beautiful UI
 */
public class CustomTable extends JTable {

    private static final Color HEADER_COLOR = new Color(74, 144, 226);
    private static final Color ALTERNATE_ROW_COLOR = new Color(248, 249, 250);
    private static final Color SELECTION_COLOR = new Color(230, 240, 255);
    private static final Color BORDER_COLOR = new Color(222, 226, 230);

    public CustomTable() {
        initializeTable();
        setupTableModel();
        applyCustomStyling();
        addSampleData();
    }

    private void initializeTable() {
        setFont(new Font("Segoe UI", Font.PLAIN, 13));
        setRowHeight(45);
        setIntercellSpacing(new Dimension(0, 1));
        setShowGrid(true);
        setGridColor(BORDER_COLOR);
        setSelectionBackground(SELECTION_COLOR);
        setSelectionForeground(Color.BLACK);
        setFillsViewportHeight(true);
    }

    private void setupTableModel() {
        String[] columnNames = {
                "Mã hóa đơn",
                "Tên khách hàng",
                "Số phòng",
                "Tổng tiền",
                "Ngày check-in",
                "Ngày check-out",
                "Số điện thoại"
        };

        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };

        setModel(model);
    }

    private void applyCustomStyling() {
        // Header styling with custom renderer to override LAF
        JTableHeader header = getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setForeground(Color.WHITE);
        header.setBackground(HEADER_COLOR);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 50));
        header.setBorder(BorderFactory.createEmptyBorder());

        // Custom header renderer to force blue background even with Nimbus LAF
        header.setDefaultRenderer(new HeaderRenderer());

        // Set column widths
        getColumnModel().getColumn(0).setPreferredWidth(120); // Mã hóa đơn
        getColumnModel().getColumn(1).setPreferredWidth(180); // Tên khách hàng
        getColumnModel().getColumn(2).setPreferredWidth(100); // Số phòng
        getColumnModel().getColumn(3).setPreferredWidth(130); // Tổng tiền
        getColumnModel().getColumn(4).setPreferredWidth(120); // Check-in
        getColumnModel().getColumn(5).setPreferredWidth(120); // Check-out
        getColumnModel().getColumn(6).setPreferredWidth(130); // Số điện thoại

        // Apply custom renderers
        setDefaultRenderer(Object.class, new AlternatingRowRenderer());
        getColumnModel().getColumn(3).setCellRenderer(new CurrencyRenderer()); // Tổng tiền
        getColumnModel().getColumn(4).setCellRenderer(new DateRenderer()); // Check-in
        getColumnModel().getColumn(5).setCellRenderer(new DateRenderer()); // Check-out
    }

    private void addSampleData() {
        DefaultTableModel model = (DefaultTableModel) getModel();

        // Sample data
        Object[][] sampleData = {
                { "1", "Nguyễn Văn An", "P101", 2500000, "15/12/2024", "18/12/2024", "0912345678" },
                { "2", "Trần Thị Bình", "P205", 3200000, "16/12/2024", "20/12/2024", "0987654321" },
                { "3", "Lê Minh Cường", "P303", 1800000, "17/12/2024", "19/12/2024", "0901234567" },
                { "4", "Phạm Thu Dung", "P102", 4500000, "18/12/2024", "22/12/2024", "0934567890" },
                { "5", "Hoàng Văn Em", "P401", 2100000, "19/12/2024", "21/12/2024", "0923456789" }
        };

        for (Object[] row : sampleData) {
            model.addRow(row);
        }
    }

    /**
     * Add new row to table
     */
    public void addRow(String orderId, String customerName, String roomNumbers,
            double totalAmount, String checkIn, String checkOut, String phone) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.addRow(new Object[] { orderId, customerName, roomNumbers, totalAmount, checkIn, checkOut, phone });
    }

    /**
     * Clear all data
     */
    public void clearData() {
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.setRowCount(0);
    }

    /**
     * Get selected row data
     */
    public Object[] getSelectedRowData() {
        int selectedRow = getSelectedRow();
        if (selectedRow == -1)
            return null;

        Object[] rowData = new Object[getColumnCount()];
        for (int i = 0; i < getColumnCount(); i++) {
            rowData[i] = getValueAt(selectedRow, i);
        }
        return rowData;
    }

    /**
     * Alternating row renderer
     */
    class AlternatingRowRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            setFont(new Font("Segoe UI", Font.PLAIN, 13));
            setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

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
     * Currency renderer for money columns
     */
    class CurrencyRenderer extends DefaultTableCellRenderer {
        private NumberFormat currencyFormat;

        public CurrencyRenderer() {
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

            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

            if (isSelected) {
                setBackground(SELECTION_COLOR);
                setForeground(Color.BLACK);
            } else {
                if (row % 2 == 0) {
                    setBackground(Color.WHITE);
                } else {
                    setBackground(ALTERNATE_ROW_COLOR);
                }
                setForeground(new Color(40, 167, 69)); // Green for money
            }

            return this;
        }
    }

    /**
     * Date renderer for date columns
     */
    class DateRenderer extends DefaultTableCellRenderer {

        public DateRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            setFont(new Font("Segoe UI", Font.PLAIN, 13));
            setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

            if (isSelected) {
                setBackground(SELECTION_COLOR);
                setForeground(Color.BLACK);
            } else {
                if (row % 2 == 0) {
                    setBackground(Color.WHITE);
                } else {
                    setBackground(ALTERNATE_ROW_COLOR);
                }
                setForeground(new Color(108, 117, 125)); // Gray for dates
            }

            return this;
        }
    }

    /**
     * Custom header renderer to override LAF styling
     */
    class HeaderRenderer extends DefaultTableCellRenderer {

        public HeaderRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Force the blue header color even with Nimbus LAF
            setBackground(HEADER_COLOR);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setBorder(BorderFactory.createEmptyBorder(12, 8, 12, 8));
            setOpaque(true);

            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            // Override paint to ensure blue background
            g.setColor(HEADER_COLOR);
            g.fillRect(0, 0, getWidth(), getHeight());
            super.paintComponent(g);
        }
    }
}