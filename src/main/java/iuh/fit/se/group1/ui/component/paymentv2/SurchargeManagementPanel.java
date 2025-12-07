package iuh.fit.se.group1.ui.component.paymentv2;

import iuh.fit.se.group1.dto.SurchargeDTO;
import iuh.fit.se.group1.entity.Surcharge;
import iuh.fit.se.group1.ui.component.custom.Button;
import iuh.fit.se.group1.ui.component.custom.message.CustomDialog;
import iuh.fit.se.group1.util.Constants;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Panel quản lý phụ phí với UX/UI tối ưu
 * Bảng trái: Danh sách phụ phí hiện có (có thể click để chọn)
 * Bảng phải: Phụ phí đã chọn (dữ liệu lưu trong database)
 */
public class SurchargeManagementPanel extends JPanel {

    // Components
    private JTable availableSurchargesTable;
    private JTable selectedSurchargesTable;
    private DefaultTableModel availableModel;
    private DefaultTableModel selectedModel;
    private JLabel totalLabel;
    private JLabel instructionLabel;
    private Button saveButton;
    private Button clearAllButton;

    // Data storage
    private Map<String, SurchargeDTO> selectedSurcharges;
    private double totalAmount = 0.0;

    // UI Colors
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Color HEADER_COLOR = new Color(74, 144, 226);
    private static final Color BORDER_COLOR = new Color(221, 221, 221);
    private static final Color ACCENT_COLOR = new Color(40, 167, 69);
    private static final Color WARNING_COLOR = new Color(255, 193, 7);
    private static final Color HOVER_COLOR = new Color(230, 240, 255);
    private static final Color SELECTED_COLOR = new Color(220, 237, 255);

    private NumberFormat currencyFormat;

    public SurchargeManagementPanel() {
        currencyFormat = Constants.VND_FORMAT;
        selectedSurcharges = new HashMap<>();

        // Set preferred and maximum dimensions
        setPreferredSize(new Dimension(1200, 800));
        setMaximumSize(new Dimension(1200, 800));
        setMinimumSize(new Dimension(800, 600));

        initializeComponents();
        setupLayout();
        // loadSampleData();
        setupEventHandlers();
    }

    private void initializeComponents() {
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Available surcharges table (left)
        setupAvailableSurchargesTable();

        // Selected surcharges table (right)
        setupSelectedSurchargesTable();

        // Labels
        instructionLabel = new JLabel("<html><div style='text-align: center; color: #666;'>" +
                "💡 <b>Hướng dẫn:</b> Click vào phụ phí bên trái để thêm • " +
                "Click chuột phải vào bảng bên phải để xóa/sửa</div></html>");
        instructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        totalLabel = new JLabel("Tổng tiền: " + currencyFormat.format(0));
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalLabel.setForeground(ACCENT_COLOR);
        totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        // Buttons
        saveButton = Button.createSuccessButton("Lưu phụ phí", FontIcon.of(FontAwesomeSolid.SAVE, 16, Color.WHITE));
        saveButton.setPreferredSize(new Dimension(130, 40));

        clearAllButton = Button.createWarningButton("Xóa tất cả",
                FontIcon.of(FontAwesomeSolid.TRASH_ALT, 16, Color.WHITE));
        clearAllButton.setPreferredSize(new Dimension(120, 40));
    }

    private void setupAvailableSurchargesTable() {
        String[] columnNames = { "Mã phụ phí", "Tên phụ phí", "Giá" };
        availableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0)
                    return Integer.class;
                if (columnIndex == 2 || columnIndex == 3)
                    return Double.class;
                return String.class;
            }
        };

        availableSurchargesTable = new JTable(availableModel);
        setupTableAppearance(availableSurchargesTable, "Danh sách phụ phí hiện có");

        // Column widths for available table
        availableSurchargesTable.getColumnModel().getColumn(0).setPreferredWidth(100); // STT
        availableSurchargesTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Tên
        availableSurchargesTable.getColumnModel().getColumn(2).setPreferredWidth(120); // Giá
    }

    private void setupSelectedSurchargesTable() {
        String[] columnNames = { "STT", "Tên phụ phí", "Giá", "SL", "Thành tiền" };
        selectedModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2 || columnIndex == 4)
                    return Double.class;
                if (columnIndex == 3)
                    return Integer.class;
                return String.class;
            }
        };

        selectedSurchargesTable = new JTable(selectedModel);
        setupTableAppearance(selectedSurchargesTable, "Dữ liệu lưu trong database");

        // Column widths for selected table
        selectedSurchargesTable.getColumnModel().getColumn(0).setPreferredWidth(100); // Mã
        selectedSurchargesTable.getColumnModel().getColumn(1).setPreferredWidth(180); // Tên
        selectedSurchargesTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Giá
        selectedSurchargesTable.getColumnModel().getColumn(3).setPreferredWidth(50); // Số lượng
        selectedSurchargesTable.getColumnModel().getColumn(4).setPreferredWidth(120); // Thành tiền
    }

    private void setupTableAppearance(JTable table, String title) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(40);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(BORDER_COLOR);
        table.setShowGrid(true);
        table.setSelectionBackground(SELECTED_COLOR);
        table.setSelectionForeground(Color.BLACK);

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(HEADER_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 45));

        // Custom header renderer to force colors
        header.setDefaultRenderer(new HeaderRenderer());

        // Custom cell renderers
        table.setDefaultRenderer(Object.class, new AlternatingRowRenderer());
        table.setDefaultRenderer(Double.class, new CurrencyRenderer());
        table.setDefaultRenderer(Integer.class, new CenterRenderer());
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Top panel with instruction
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.setBorder(new EmptyBorder(0, 0, 15, 0));
        topPanel.add(instructionLabel, BorderLayout.CENTER);

        // Center panel with tables
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        centerPanel.setBackground(BACKGROUND_COLOR);

        // Left panel (Available surcharges)
        JPanel leftPanel = createTablePanel(availableSurchargesTable,
                "Danh sách phụ phí hiện có",
                "Click để thêm vào đơn hàng");

        // Right panel (Selected surcharges)
        JPanel rightPanel = createTablePanel(selectedSurchargesTable,
                "Dữ liệu lưu trong database",
                "Click chuột phải để xóa/sửa");

        centerPanel.add(leftPanel);
        centerPanel.add(rightPanel);

        // Bottom panel with total and buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(BACKGROUND_COLOR);
        bottomPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        // Total panel
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setBackground(BACKGROUND_COLOR);
        totalPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR, 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        totalPanel.add(totalLabel);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonsPanel.setBackground(BACKGROUND_COLOR);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(Box.createHorizontalStrut(10));
        buttonsPanel.add(clearAllButton);

        bottomPanel.add(buttonsPanel, BorderLayout.WEST);
        bottomPanel.add(totalPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createTablePanel(JTable table, String title, String subtitle) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                HEADER_COLOR));

        // Subtitle label
        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setBorder(new EmptyBorder(5, 10, 10, 10));

        // Table with scroll
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        panel.add(subtitleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void setupEventHandlers() {
        // Click on available table to add surcharge
        availableSurchargesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    addSurchargeFromAvailable();
                }
            }
        });

        // Right click on selected table for context menu
        selectedSurchargesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    showContextMenu(e);
                }
            }
        });

        // Clear all button
        clearAllButton.addActionListener(e -> clearAllSurcharges());
    }

    private void addSurchargeFromAvailable() {
        int selectedRow = availableSurchargesTable.getSelectedRow();
        if (selectedRow == -1)
            return;

        SurchargeDTO surchargeDTO = new SurchargeDTO();
        surchargeDTO.setSurchargeId((Long) availableModel.getValueAt(selectedRow, 0));

        surchargeDTO.setName((String) availableModel.getValueAt(selectedRow, 1));
        BigDecimal price = (BigDecimal) availableModel.getValueAt(selectedRow, 2);
        surchargeDTO.setPrice(price);
        Long id = surchargeDTO.getSurchargeId();

        // Check if exists
        boolean exists = false;
        for (SurchargeDTO data : selectedSurcharges.values()) {
            if (data.getSurchargeId().equals(id)) {
                data.setQuantity(data.getQuantity() + 1);
                exists = true;
                showNotification("Đã tăng số lượng: " + data.getName(), ACCENT_COLOR);
                break;
            }
        }

        if (!exists) {
            selectedModel.addRow(new Object[] {
                    id,
                    surchargeDTO,
                    surchargeDTO.getPrice(),
                    1,
                    surchargeDTO.getPrice()
            });

            surchargeDTO.setQuantity(1);
            selectedSurcharges.put(id.toString(), surchargeDTO);
            showNotification("Đã thêm: " + surchargeDTO, ACCENT_COLOR);
        }

        totalAmount += surchargeDTO.getPrice().doubleValue();
        updateTotalLabel();
        refreshSelectedTable();
        selectedSurchargesTable.revalidate();
        selectedSurchargesTable.repaint();
    }

    private void showContextMenu(MouseEvent e) {
        int row = selectedSurchargesTable.rowAtPoint(e.getPoint());
        if (row == -1)
            return;

        selectedSurchargesTable.setRowSelectionInterval(row, row);

        JPopupMenu popup = new JPopupMenu();
        popup.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        var surchargeDTO = (SurchargeDTO) selectedModel.getValueAt(row, 1);
        if (surchargeDTO == null) {
            return; // No data to show context menu
        }

        String id = surchargeDTO.getSurchargeId().toString();
        SurchargeDTO data = selectedSurcharges.get(id);

        if (data == null) {
            return; // Data not found in map
        }

        // Edit quantity menu
        JMenuItem editItem = new JMenuItem("Sửa số lượng (" + data.getQuantity() + ")");
        editItem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        editItem.addActionListener(e2 -> editQuantity(row, id, data));

        // Delete menu
        JMenuItem deleteItem = new JMenuItem("Xóa \"" + data.getName() + "\"");
        deleteItem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        deleteItem.setForeground(new Color(220, 53, 69));
        deleteItem.addActionListener(e2 -> deleteSurcharge(row, id, data));

        popup.add(editItem);
        popup.addSeparator();
        popup.add(deleteItem);

        popup.show(selectedSurchargesTable, e.getX(), e.getY());
    }

    private void editQuantity(int row, String id, SurchargeDTO data) {
        String input = CustomDialog.showInput(
                this,
                "Nhập số lượng mới cho \"" + data.getName() + "\":",
                "Sửa số lượng",
                CustomDialog.MessageType.INFO,
                500, 200);

        if (input != null && !input.trim().isEmpty()) {
            try {
                int newQuantity = Integer.parseInt(input.trim());
                if (newQuantity <= 0) {
                    CustomDialog.showMessage(this, "Số lượng phải lớn hơn 0!",
                            "Lỗi", CustomDialog.MessageType.ERROR, 400, 200);
                    return;
                }

                // Update quantity
                double oldTotal = data.getPrice().doubleValue() * data.getQuantity();
                data.setQuantity(newQuantity);
                double newTotal = data.getPrice().doubleValue() * newQuantity;

                totalAmount = totalAmount - oldTotal + newTotal;
                updateTotalLabel();
                refreshSelectedTable();

                showNotification("Đã cập nhật số lượng: " + data.getName() + " x" + newQuantity, ACCENT_COLOR);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSurcharge(int row, String id, SurchargeDTO data) {

        int opt = CustomDialog.showConfirm(this,
                "Bạn có chắc muốn xóa \"" + data.getName() + "\"?",
                "Xác nhận xóa",
                CustomDialog.MessageType.WARNING,
                400, 200);

        if (opt == JOptionPane.YES_OPTION) {
            selectedModel.removeRow(row);
            selectedSurcharges.remove(id);
            totalAmount -= data.getPrice().doubleValue() * data.getQuantity();
            updateTotalLabel();

            showNotification("Đã xóa: " + data.getName(), WARNING_COLOR);
        }
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public void setSaveButton(Button saveButton) {
        this.saveButton = saveButton;
    }

    private void clearAllSurcharges() {
        if (selectedSurcharges.isEmpty())
            return;

        int confirm = CustomDialog.showConfirm(this,
                "Bạn có chắc muốn xóa tất cả phụ phí?",
                "Xác nhận xóa tất cả",
                CustomDialog.MessageType.WARNING,
                400, 200);

        if (confirm == JOptionPane.YES_OPTION) {
            selectedModel.setRowCount(0);
            selectedSurcharges.clear();
            totalAmount = 0.0;
            updateTotalLabel();

            showNotification("Đã xóa tất cả phụ phí", WARNING_COLOR);
        }
    }

    private void updateTotalLabel() {
        totalLabel.setText("Tổng tiền: " + currencyFormat.format(totalAmount));
    }

    private void refreshSelectedTable() {
        selectedModel.setRowCount(0);
        int idx = 1;
        for (SurchargeDTO data : selectedSurcharges.values()) {
            double total = data.getPrice().doubleValue() * data.getQuantity();
            selectedModel.addRow(new Object[] {
                    idx++,
                    data,
                    data.getPrice(),
                    data.getQuantity(),
                    total
            });
        }
    }

    private void showNotification(String message, Color color) {
        JLabel notification = new JLabel(message);
        notification.setFont(new Font("Segoe UI", Font.BOLD, 12));
        notification.setForeground(color);
        notification.setBorder(new EmptyBorder(5, 0, 5, 0));

        // Replace instruction label temporarily
        Container parent = instructionLabel.getParent();
        if (parent != null) {
            parent.remove(instructionLabel);
            parent.add(notification, BorderLayout.CENTER);
            parent.revalidate();
            parent.repaint();
        }

        // Restore original instruction after 2 seconds
        Timer timer = new Timer(2000, e -> {
            if (parent != null) {
                parent.remove(notification);
                parent.add(instructionLabel, BorderLayout.CENTER);
                parent.revalidate();
                parent.repaint();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    // Custom renderers
    private class HeaderRenderer extends DefaultTableCellRenderer {
        public HeaderRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setBackground(HEADER_COLOR);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
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

    private class AlternatingRowRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (isSelected) {
                setBackground(SELECTED_COLOR);
                setForeground(Color.BLACK);
            } else {
                setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 249, 250));
                setForeground(Color.BLACK);
            }

            setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
            return this;
        }
    }

    private class CurrencyRenderer extends AlternatingRowRenderer {
        public CurrencyRenderer() {
            setHorizontalAlignment(SwingConstants.RIGHT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            // if (value instanceof Double) {
            // value = currencyFormat.format((Double) value).split(" ")[0];
            // }
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            return this;
        }
    }

    private class CenterRenderer extends AlternatingRowRenderer {
        public CenterRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }
    }

    // Public methods
    public Map<String, SurchargeDTO> getSelectedSurcharges() {
        return new HashMap<>(selectedSurcharges);
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void addSurcharge(String id, String name, double price, int quantity) {
        double total = price * quantity;
        selectedModel.addRow(new Object[] { id, name, price, quantity, total });
        SurchargeDTO dto = new SurchargeDTO();
        dto.setSurchargeId(Long.parseLong(id));
        dto.setName(name);
        dto.setPrice(BigDecimal.valueOf(price));
        dto.setQuantity(quantity);
        selectedSurcharges.put(id, dto);
        totalAmount += total;
        updateTotalLabel();
    }

    /**
     * Lấy danh sách dữ liệu từ bảng bên phải (bảng phụ phí đã chọn)
     * 
     * @return List các SurchargeDTO từ bảng phụ phí đã chọn
     */
    public java.util.List<SurchargeDTO> getSelectedTableData() {
        java.util.List<SurchargeDTO> data = new java.util.ArrayList<>();
        for (int i = 0; i < selectedModel.getRowCount(); i++) {



            Integer quantity = (Integer) selectedModel.getValueAt(i, 3); // Số lượng

            SurchargeDTO dto = (SurchargeDTO) selectedModel.getValueAt(i, 1);

            dto.setQuantity(quantity);

            data.add(dto);
        }
        return data;
    }

    /**
     * Đưa dữ liệu vào 2 bảng
     * 
     * @param availableData Dữ liệu cho bảng trái (danh sách phụ phí có sẵn)
     *                      Format: [STT, Tên phụ phí, Giá, Thành tiền]
     * @param selectedData  Dữ liệu cho bảng phải (phụ phí đã chọn)
     *                      Format: [Mã, Tên, Giá, Số lượng, Thành tiền]
     */
    public void loadData(java.util.List<Surcharge> availableData, ArrayList<SurchargeDTO> selectedData) {
        // Clear existing data
        availableModel.setRowCount(0);
        selectedModel.setRowCount(0);
        selectedSurcharges.clear();
        totalAmount = 0.0;

        // Load available surcharges (left table)
        if (availableData != null) {
            for (Surcharge surcharge : availableData) {
                availableModel.addRow(new Object[] {
                        surcharge.getSurchargeId(),
                        surcharge.getName(),
                        surcharge.getPrice(),
                });
            }
        }

        // Load selected surcharges (right table)
        if (selectedData != null) {
            int index = 1;
            for (SurchargeDTO dto : selectedData) {
                BigDecimal total = dto.getPrice().multiply(new BigDecimal(dto.getQuantity()));
                selectedModel.addRow(new Object[] {
                        index++,
                        dto,
                        dto.getPrice(),
                        dto.getQuantity(),
                        total
                });
                selectedSurcharges.put(dto.getSurchargeId().toString(), dto);
                totalAmount += total.doubleValue();
            }
        }

        updateTotalLabel();
        selectedSurchargesTable.revalidate();
        selectedSurchargesTable.repaint();
        availableSurchargesTable.revalidate();
        availableSurchargesTable.repaint();
    }
}