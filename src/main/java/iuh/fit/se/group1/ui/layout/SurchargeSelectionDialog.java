package iuh.fit.se.group1.ui.layout;

import iuh.fit.se.group1.entity.Surcharge;
import iuh.fit.se.group1.repository.SurchargeRepository;
import iuh.fit.se.group1.util.Constants;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dialog chọn phụ phí (Surcharge) - giống SurchargeManagementPanel
 */
public class SurchargeSelectionDialog extends JDialog {

    private static final Color HEADER_COLOR = new Color(74, 144, 226);
    private static final Color ACCENT_COLOR = new Color(40, 167, 69);
    private static final Color HOVER_COLOR = new Color(230, 240, 255);
    private static final Color SELECTED_COLOR = new Color(220, 237, 255);

    private JTable availableTable;
    private JTable selectedTable;
    private DefaultTableModel availableModel;
    private DefaultTableModel selectedModel;

    private Map<Long, SurchargeItem> selectedItems;
    private SurchargeRepository surchargeRepository;

    private boolean confirmed = false;

    public SurchargeSelectionDialog(Window owner) {
        super(owner, "Chọn phụ phí", ModalityType.APPLICATION_MODAL);
        this.selectedItems = new HashMap<>();
        this.surchargeRepository = new SurchargeRepository();

        initComponents();
        loadAvailableSurcharges();

        setSize(900, 600);
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new MigLayout("fill, insets 15", "[grow][grow]", "[]10[grow]10[]"));
        mainPanel.setBackground(Color.WHITE);

        // Header
        JLabel title = new JLabel("CHỌN PHỤ PHÍ");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(HEADER_COLOR);
        mainPanel.add(title, "span 2, center, wrap");

        // Left: Available surcharges
        JPanel leftPanel = createTablePanel("Danh sách phụ phí có sẵn", true);
        mainPanel.add(leftPanel, "grow");

        // Right: Selected surcharges
        JPanel rightPanel = createTablePanel("Phụ phí đã chọn", false);
        mainPanel.add(rightPanel, "grow, wrap");

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnCancel = createButton("Hủy", new Color(220, 53, 69));
        JButton btnOk = createButton("Xác nhận", HEADER_COLOR);

        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        btnOk.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        buttonPanel.add(btnCancel);
        buttonPanel.add(btnOk);

        mainPanel.add(buttonPanel, "span 2, right");

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createTablePanel(String title, boolean isAvailable) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(HEADER_COLOR, 1, true),
                title,
                0, 0,
                new Font("Segoe UI", Font.BOLD, 13),
                HEADER_COLOR
        ));

        String[] columns = isAvailable ?
                new String[]{"Mã", "Tên phụ phí", "Đơn giá"} :
                new String[]{"Mã", "Tên phụ phí", "Đơn giá", "Số lượng"};

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return !isAvailable && column == 3; // Only quantity editable in selected table
            }
        };

        JTable table = new JTable(model);
        styleTable(table);

        if (isAvailable) {
            availableTable = table;
            availableModel = model;

            // Click to add
            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        int row = table.getSelectedRow();
                        if (row >= 0) {
                            addSurcharge(row);
                        }
                    }
                }
            });
        } else {
            selectedTable = table;
            selectedModel = model;

            // Right click to remove
            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        int row = table.getSelectedRow();
                        if (row >= 0) {
                            removeSurcharge(row);
                        }
                    }
                }
            });
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(new Color(221, 221, 221), 1));
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(32);
        table.setSelectionBackground(SELECTED_COLOR);
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(221, 221, 221));

        JTableHeader header = table.getTableHeader();
        header.setOpaque(true);
        header.setBackground(HEADER_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(0, 35));

        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setBackground(HEADER_COLOR);
                label.setForeground(Color.WHITE);
                label.setFont(new Font("Segoe UI", Font.BOLD, 13));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setOpaque(true);
                return label;
            }
        });
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setPreferredSize(new Dimension(110, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 2, true),
                new EmptyBorder(5, 15, 5, 15)
        ));

        button.addMouseListener(new MouseAdapter() {
            Color original = bgColor;
            @Override
            public void mouseEntered(MouseEvent e) { button.setBackground(bgColor.brighter()); }
            @Override
            public void mouseExited(MouseEvent e) { button.setBackground(original); }
        });

        return button;
    }

    private void loadAvailableSurcharges() {
        availableModel.setRowCount(0);
        List<Surcharge> surcharges = surchargeRepository.findAll();
        for (Surcharge s : surcharges) {
            availableModel.addRow(new Object[]{
                    s.getSurchargeId(),
                    s.getName(),
                    Constants.VND_FORMAT.format(s.getPrice())
            });
        }
    }

    private void addSurcharge(int row) {
        Long id = Long.parseLong(availableModel.getValueAt(row, 0).toString());
        String name = availableModel.getValueAt(row, 1).toString();
        String priceStr = availableModel.getValueAt(row, 2).toString();

        if (selectedItems.containsKey(id)) {
            // Increase quantity
            SurchargeItem item = selectedItems.get(id);
            item.quantity++;
            updateSelectedTable();
        } else {
            // Add new
            BigDecimal price = Constants.parseVNDToBigDecimal(priceStr);
            selectedItems.put(id, new SurchargeItem(id, name, price, 1));
            updateSelectedTable();
        }
    }

    private void removeSurcharge(int row) {
        Long id = Long.parseLong(selectedModel.getValueAt(row, 0).toString());
        selectedItems.remove(id);
        updateSelectedTable();
    }

    private void updateSelectedTable() {
        selectedModel.setRowCount(0);
        for (SurchargeItem item : selectedItems.values()) {
            selectedModel.addRow(new Object[]{
                    item.id,
                    item.name,
                    Constants.VND_FORMAT.format(item.price),
                    item.quantity
            });
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public List<SurchargeItem> getSelectedSurcharges() {
        return new ArrayList<>(selectedItems.values());
    }

    public static class SurchargeItem {
        public Long id;
        public String name;
        public BigDecimal price;
        public int quantity;

        public SurchargeItem(Long id, String name, BigDecimal price, int quantity) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }
    }
}

