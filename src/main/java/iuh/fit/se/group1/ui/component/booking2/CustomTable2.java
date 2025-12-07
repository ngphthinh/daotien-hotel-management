package iuh.fit.se.group1.ui.component.booking2;

import iuh.fit.se.group1.ui.component.custom.Button;
import iuh.fit.se.group1.util.Constants;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Custom Table 2 class for hotel service booking interface
 */
public class CustomTable2 extends JTable {

    public CustomTable2() {
        super();
        initializeTable();
    }

    private void initializeTable() {
        // Set table model
        setModel(new CustomTable2Model());

        // Set custom renderers and editors
        setupColumnRenderers();

        // Table appearance
        setRowHeight(30);
        setFont(new Font("Segoe UI", Font.PLAIN, 13));
        setGridColor(new Color(10, 10, 10));
        setShowGrid(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setIntercellSpacing(new Dimension(1, 1));

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
        setBackground(Color.WHITE);
    }

    private void setupColumnRenderers() {
        // Checkbox column (Chọn)
        getColumn("Chọn").setCellRenderer(new CheckboxRenderer());

        // Text column (Tên dịch vụ)
        getColumn("Tên dịch vụ").setCellRenderer(new TextRenderer());

        // Currency column (Giá)
        getColumn("Giá").setCellRenderer(new CurrencyRenderer());

        // Quantity column with +/- buttons
        getColumn("Số lượng").setCellRenderer(new QuantityRenderer());
        getColumn("Số lượng").setCellEditor(new QuantityEditor());
    }

    private void setupColumnWidths() {
        TableColumnModel columnModel = getColumnModel();

        // Set preferred widths based on content
        columnModel.getColumn(0).setPreferredWidth(30); // Chọn
        columnModel.getColumn(1).setPreferredWidth(250); // Tên dịch vụ
        columnModel.getColumn(2).setPreferredWidth(120); // Giá
        columnModel.getColumn(3).setPreferredWidth(130); // Số lượng
    }

    /**
     * White background renderer for all rows
     */
    class AlternatingRowRenderer extends DefaultTableCellRenderer {

        // Cột cần highlight đặc biệt (nếu không có thì để = -1)
        private int specialColumn = -1;
        private Color specialColor = new Color(255, 248, 220); // vàng nhạt

        public AlternatingRowRenderer() {
        }

        public AlternatingRowRenderer(int specialColumn, Color specialColor) {
            this.specialColumn = specialColumn;
            this.specialColor = specialColor;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            // ============================
            // 1️⃣ Nếu ô đang SELECT → ưu tiên màu select
            // ============================
            if (isSelected) {
                c.setBackground(table.getSelectionBackground());
                c.setForeground(table.getSelectionForeground());
                return c;
            }

            // ============================
            // 2️⃣ Nếu là SPECIAL COLUMN → highlight riêng
            // ============================
            if (column == specialColumn) {
                c.setBackground(specialColor);
                c.setForeground(Color.BLACK);
                return c;
            }

            // ============================
            // 3️⃣ Màu nền xen kẽ
            // ============================
            if (row % 2 == 0) {
                c.setBackground(Color.WHITE);
            } else {
                c.setBackground(new Color(245, 245, 245)); // xám rất nhạt
            }

            c.setForeground(Color.BLACK);
            return c;
        }
    }

    /**
     * Custom table model for service booking
     */
    class CustomTable2Model extends AbstractTableModel {

        private final String[] columnNames = {
                "Chọn", "Tên dịch vụ", "Giá", "Số lượng"
        };

        private final java.util.List<Object[]> data = new ArrayList<>();

        public CustomTable2Model() {
            // Nếu muốn giữ danh sách mặc định có thể thêm ở đây
            data.add(new Object[]{false, "Dịch vụ chăn màn", 100000, 1});
            data.add(new Object[]{false, "Đón khách tại sân bay", 500000, 1});
            // ...
        }

        @Override
        public int getRowCount() {
            return data.size();
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
            return data.get(rowIndex)[columnIndex];
        }

        @Override
        public void setValueAt(Object value, int rowIndex, int columnIndex) {

            // Xử lý cột checkbox
            if (columnIndex == 0) {
                boolean selected = (boolean) value;
                data.get(rowIndex)[0] = selected;

                // Checkbox ON → đặt SL = 1 (chỉ khi đang là 0)
                if (selected) {
                    int currentQty = (int) data.get(rowIndex)[3];
                    if (currentQty == 0) {   // chỉ set lại nếu = 0
                        data.get(rowIndex)[3] = 1;
                        fireTableCellUpdated(rowIndex, 3);
                    }
                } else {
                    // Checkbox OFF → đặt SL = 0
                    data.get(rowIndex)[3] = 0;
                    fireTableCellUpdated(rowIndex, 3);
                }

                fireTableCellUpdated(rowIndex, 0);
                return;
            }

            // Xử lý cột số lượng
            if (columnIndex == 3) {
                int qty = Integer.parseInt(value.toString());

                data.get(rowIndex)[3] = qty;

                // Nếu nhập số lượng > 0 thì auto tick checkbox
                if (qty > 0) {
                    data.get(rowIndex)[0] = true;
                    fireTableCellUpdated(rowIndex, 0);
                }

                fireTableCellUpdated(rowIndex, 3);
                return;
            }

            // Cột còn lại
            data.get(rowIndex)[columnIndex] = value;
            fireTableCellUpdated(rowIndex, columnIndex);
        }


        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 0 || columnIndex == 3;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) return Boolean.class;
            return Object.class;
        }

        // ✅ Thêm dòng
        public void addRow(Object[] row) {
            data.add(row);
            fireTableRowsInserted(data.size() - 1, data.size() - 1);
        }

        // ✅ Xóa tất cả dòng
        public void clear() {
            data.clear();
            fireTableDataChanged();
        }
    }
    private QuantityChangeListener quantityChangeListener;

    public void setQuantityChangeListener(QuantityChangeListener listener) {
        this.quantityChangeListener = listener;
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
                c.setBackground(Color.WHITE);
                c.setForeground(Color.BLACK);
            }

            return c;
        }
    }

    /**
     * Text renderer with white background and black text
     */
    class TextRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (!isSelected) {
                c.setBackground(Color.WHITE);
                c.setForeground(Color.BLACK);
            } else {
                c.setBackground(table.getSelectionBackground());
                c.setForeground(table.getSelectionForeground());
            }

            return c;
        }
    }

    /**
     * Checkbox renderer for selection column
     */
    class CheckboxRenderer extends JCheckBox implements TableCellRenderer {
        public CheckboxRenderer() {
            setHorizontalAlignment(JCheckBox.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            setSelected(value != null && (Boolean) value);

            // Set background color to white
            if (!isSelected) {
                setBackground(Color.WHITE);
            } else {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            }
            setOpaque(true);

            return this;
        }
    }

    /**
     * Quantity renderer with +/- buttons - using BorderLayout to prevent height
     * expansion
     */
    class QuantityRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            QuantityComponent comp = new QuantityComponent();
            comp.quantityLabel.setText(value != null ? value.toString() : "0");

            comp.setBackground(Color.WHITE);

            return comp;
        }
    }

    /**
     * Quantity editor with +/- buttons - using BorderLayout to prevent height
     * expansion
     */
    class QuantityEditor extends AbstractCellEditor implements TableCellEditor {
        private QuantityComponent comp;
        private int currentValue;

        public QuantityEditor() {
            comp = new QuantityComponent();

            comp.plusBtn.addActionListener(e -> {
                currentValue++;
                comp.quantityLabel.setText(String.valueOf(currentValue));
                fireEditingStopped();

                if (quantityChangeListener != null) {
                    quantityChangeListener.onQuantityChange(editingRow, currentValue);
                }
            });


            comp.minusBtn.addActionListener(e -> {
                if (currentValue > 0) {
                    currentValue--;
                    comp.quantityLabel.setText(String.valueOf(currentValue));
                    fireEditingStopped();

                    if (quantityChangeListener != null) {
                        quantityChangeListener.onQuantityChange(editingRow, currentValue);
                    }
                }
            });

        }

        @Override
        public Object getCellEditorValue() {
            return currentValue;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            editingRow = row;
            currentValue = value != null ? (Integer) value : 0;
            comp.quantityLabel.setText(String.valueOf(currentValue));

            comp.setBackground(Color.WHITE);

            return comp;
        }
    }
    private int editingRow;
    class QuantityComponent extends JPanel {

        public JButton minusBtn;
        public JButton plusBtn;
        public JLabel quantityLabel;

        public QuantityComponent() {

            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(130, 30)); // đồng bộ renderer & editor
            setBackground(Color.WHITE);
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 3));
            buttonPanel.setBackground(Color.WHITE);

            minusBtn = createStyledButton("-", FontAwesomeSolid.MINUS, new Color(220, 53, 69));
            plusBtn = createStyledButton("+", FontAwesomeSolid.PLUS, new Color(40, 167, 69));

            quantityLabel = new JLabel("0", SwingConstants.CENTER);
            quantityLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            quantityLabel.setPreferredSize(new Dimension(40, 25));
            quantityLabel.setOpaque(true);
            quantityLabel.setBackground(Color.WHITE);
            quantityLabel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.LIGHT_GRAY));

            buttonPanel.add(minusBtn);
            buttonPanel.add(quantityLabel);
            buttonPanel.add(plusBtn);

            add(buttonPanel, BorderLayout.CENTER);
        }

        private JButton createStyledButton(String text, FontAwesomeSolid icon, Color bgColor) {
            JButton button = new Button();
            button.setText("");

            FontIcon fontIcon = FontIcon.of(icon, 10, Color.WHITE);
            button.setIcon(fontIcon);

            button.setPreferredSize(new Dimension(25, 25));
            button.setBackground(bgColor);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Segoe UI", Font.BOLD, 10));
            button.setOpaque(true);

            if ("-".equals(text))
                button.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 0, Color.LIGHT_GRAY));
            else
                button.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, Color.LIGHT_GRAY));

            return button;
        }
    }

}