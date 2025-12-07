package iuh.fit.se.group1.ui.component.booking2;

import iuh.fit.se.group1.util.Constants;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Custom Table class for hotel room booking interface
 */
public class CustomTable extends JTable {

    public CustomTable() {
        super();
        initializeTable();
    }

    private void initializeTable() {
        // Set table model
        setModel(new CustomTableModel());

        // Set custom renderers and editors
        setupColumnRenderers();

        // Table appearance
        setRowHeight(45);
        setFont(new Font("Segoe UI", Font.PLAIN, 13));
        setGridColor(new Color(10, 10, 10));
        setShowGrid(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setIntercellSpacing(new Dimension(1, 1));

        // Alternating row colors
        setDefaultRenderer(Object.class, new AlternatingRowRenderer());

        // Selection colors
        setSelectionBackground(new Color(184, 207, 229));
        setSelectionForeground(Color.BLACK);

        // Header styling
        JTableHeader header = getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(255, 255, 255));
        header.setForeground(Color.BLACK);
        header.setPreferredSize(new Dimension(0, 40));
        header.setBorder(BorderFactory.createRaisedBevelBorder());

        // Set column widths
        setupColumnWidths();
    }

    private void setupColumnRenderers() {
        // Currency columns (Giá, Tổng)
        getColumn("Giá").setCellRenderer(new CurrencyRenderer());
        getColumn("Tổng").setCellRenderer(new CurrencyRenderer());

        // Quantity column with +/- buttons
        getColumn("Số Lượng").setCellRenderer(new QuantityRenderer());
        getColumn("Số Lượng").setCellEditor(new QuantityEditor());

        // Center align numeric columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        getColumn("Còn trống").setCellRenderer(centerRenderer);
    }

    private void setupColumnWidths() {
        TableColumnModel columnModel = getColumnModel();

        // Set preferred widths based on content
        columnModel.getColumn(0).setPreferredWidth(120); // Loại phòng
        columnModel.getColumn(1).setPreferredWidth(80); // Sức chứa
        columnModel.getColumn(2).setPreferredWidth(100); // Giá
        columnModel.getColumn(3).setPreferredWidth(100); // Tổng
        columnModel.getColumn(4).setPreferredWidth(80); // Còn trống
        columnModel.getColumn(5).setPreferredWidth(130); // Số Lượng
    }

    /**
     * Alternating row color renderer
     */
    class AlternatingRowRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (!isSelected) {
                if (row % 2 == 0) {
                    c.setBackground(Color.WHITE);
                } else {
                    c.setBackground(new Color(248, 248, 248));
                }
            }

            // Center align numeric columns
            if (column == 5) { // Còn trống column
                setHorizontalAlignment(CENTER);
            } else {
                setHorizontalAlignment(LEFT);
            }

            return c;
        }
    }

    /**
     * Custom table model for room booking data
     */
    class CustomTableModel extends AbstractTableModel {
        private final String[] columnNames = {
                "Loại phòng", "Sức chứa", "Giá", "Tổng", "Còn trống", "Số Lượng"
        };

        private Object[][] data = {
                {"Phòng đơn", "2 người", 500000, 4000000, 4, 4},
                {"Phòng đôi", "4 người", 800000, 10000000, 10, 3}
        };

        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return data[rowIndex][columnIndex];
        }

        @Override
        public void setValueAt(Object value, int rowIndex, int columnIndex) {
            data[rowIndex][columnIndex] = value;

            // Auto-calculate total when quantity changes
            if (columnIndex == 5 || columnIndex == 4) { // "Số Lượng" is column 5
                Number quantityNum = (Number) value;
                Number priceNum = (Number) data[rowIndex][2];  // giá

                int quantity = quantityNum.intValue();
                int price = priceNum.intValue();

                data[rowIndex][3] = price * quantity; // "Tổng" is column 3
                fireTableCellUpdated(rowIndex, 3);
            }

            fireTableCellUpdated(rowIndex, columnIndex);
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 5;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }
    }

    /**
     * Currency formatter renderer
     */
    class CurrencyRenderer extends DefaultTableCellRenderer {

        public CurrencyRenderer() {
            setHorizontalAlignment(JLabel.RIGHT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (value instanceof Number) {
                String formattedValue = Constants.VND_FORMAT.format(value);
                setText(formattedValue);
            }

            if (!isSelected) {
                if (row % 2 == 0) {
                    c.setBackground(Color.WHITE);
                } else {
                    c.setBackground(new Color(248, 248, 248));
                }
            }

            return c;
        }
    }

    /**
     * Quantity renderer with +/- buttons
     */
    class QuantityRenderer extends JPanel implements TableCellRenderer {
        private JButton minusButton;
        private JLabel quantityLabel;
        private JButton plusButton;

        public QuantityRenderer() {
            setLayout(new GridBagLayout());
            setOpaque(true);
            setPreferredSize(new Dimension(105, 35));

            // Styled minus button
            minusButton = new JButton();
            minusButton.setIcon(FontIcon.of(FontAwesomeSolid.MINUS));
            minusButton.setPreferredSize(new Dimension(35, 35));
            minusButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
            minusButton.setBackground(Color.WHITE);
            minusButton.setForeground(Color.BLACK);

            // Styled quantity label
            quantityLabel = new JLabel("0");
            quantityLabel.setHorizontalAlignment(JLabel.CENTER);
            quantityLabel.setPreferredSize(new Dimension(35, 35));
            quantityLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            quantityLabel.setOpaque(true);
            quantityLabel.setBackground(Color.WHITE);

            // Styled plus button
            plusButton = new JButton();
            plusButton.setIcon(FontIcon.of(FontAwesomeSolid.PLUS));
            plusButton.setPreferredSize(new Dimension(35, 35));
            plusButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
            plusButton.setBackground(Color.WHITE);
            plusButton.setForeground(Color.BLACK);
            plusButton.setFocusPainted(false);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(0, 0, 0, 0);
            add(minusButton, gbc);
            gbc.gridx = 1;
            add(quantityLabel, gbc);
            gbc.gridx = 2;
            add(plusButton, gbc);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {

            if (value != null) {
                quantityLabel.setText(value.toString());
            }

            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }

            return this;
        }
    }

    /**
     * Quantity editor with +/- buttons functionality
     */
    class QuantityEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private JButton minusButton;
        private JLabel quantityLabel;
        private JButton plusButton;
        private int currentValue = 0;
        private int currentRow;

        public QuantityEditor() {
            panel = new JPanel(new GridBagLayout());
            panel.setOpaque(true);
            panel.setPreferredSize(new Dimension(105, 35));

            // Styled minus button
            minusButton = new JButton("−");
            minusButton.setPreferredSize(new Dimension(35, 35));
            minusButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
            minusButton.setBackground(Color.WHITE);
            minusButton.setForeground(Color.BLACK);
            minusButton.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 0, Color.BLACK));

            // Styled quantity label
            quantityLabel = new JLabel("0");
            quantityLabel.setHorizontalAlignment(JLabel.CENTER);
            quantityLabel.setPreferredSize(new Dimension(35, 35));
            quantityLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            quantityLabel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
            quantityLabel.setOpaque(true);
            quantityLabel.setBackground(Color.WHITE);

            plusButton = new JButton("+");
            plusButton.setPreferredSize(new Dimension(35, 35));
            plusButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
            plusButton.setBackground(Color.WHITE);
            plusButton.setForeground(Color.BLACK);
            plusButton.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, Color.BLACK));

            minusButton.addActionListener(e -> {
                if (currentValue > 0) {
                    currentValue--;
                    quantityLabel.setText(String.valueOf(currentValue));

                    // Update model
                    CustomTable.this.setValueAt(currentValue, currentRow, 5);

                    // 🔥 Gọi callback ra ngoài
                    if (quantityChangeListener != null) {
                        quantityChangeListener.onQuantityChange(currentRow, currentValue);
                    }
                }
            });

            plusButton.addActionListener(e -> {
                Number availableNum = (Number) CustomTable.this.getValueAt(currentRow, 4);
                int available = availableNum.intValue();

                if (currentValue < available) {
                    currentValue++;
                    quantityLabel.setText(String.valueOf(currentValue));

                    // Update model
                    CustomTable.this.setValueAt(currentValue, currentRow, 5);

                    // 🔥 Gọi callback ra ngoài
                    if (quantityChangeListener != null) {
                        quantityChangeListener.onQuantityChange(currentRow, currentValue);
                    }
                }
            });


            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(0, 0, 0, 0);
            panel.add(minusButton, gbc);
            gbc.gridx = 1;
            panel.add(quantityLabel, gbc);
            gbc.gridx = 2;
            panel.add(plusButton, gbc);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {

            currentRow = row;
            if (value != null) {
                Number valueNum = (Number) value;
                currentValue = valueNum.intValue();
                quantityLabel.setText(value.toString());
            }
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return currentValue;
        }
    }

    private QuantityChangeListener quantityChangeListener;

    public void setQuantityChangeListener(QuantityChangeListener listener) {
        this.quantityChangeListener = listener;
    }
}
