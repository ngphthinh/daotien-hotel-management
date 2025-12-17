package iuh.fit.se.group1.ui.component.custom;

import iuh.fit.se.group1.dto.AmenityDTO;
import iuh.fit.se.group1.entity.Amenity;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Panel quản lý dịch vụ với UX/UI tối ưu
 * Bảng trái: Danh sách dịch vụ hiện có (có thể click để chọn)
 * Bảng phải: Dịch vụ đã chọn
 */
public class AmenityManagementPanel extends JPanel {

    // Components
    private JTable availableAmenitiesTable;
    private JTable selectedAmenitiesTable;
    private DefaultTableModel availableModel;
    private DefaultTableModel selectedModel;
    private JLabel totalLabel;
    private JLabel instructionLabel;
    private Button saveButton;
    private Button clearAllButton;

    // Data storage
    private Map<String, AmenityDTO> selectedAmenities;
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

    public AmenityManagementPanel() {
        currencyFormat = Constants.VND_FORMAT;
        selectedAmenities = new HashMap<>();

        setPreferredSize(new Dimension(1200, 800));
        setMaximumSize(new Dimension(1200, 800));
        setMinimumSize(new Dimension(800, 600));

        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }

    private void initializeComponents() {
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        setupAvailableAmenitiesTable();
        setupSelectedAmenitiesTable();

        instructionLabel = new JLabel("<html><div style='text-align: center; color: #666;'>" +
                "💡 <b>Hướng dẫn:</b> Click vào dịch vụ bên trái để thêm • " +
                "Click chuột phải vào bảng bên phải để xóa/sửa</div></html>");
        instructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        totalLabel = new JLabel("Tổng tiền: " + currencyFormat.format(0));
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalLabel.setForeground(ACCENT_COLOR);
        totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        saveButton = Button.createSuccessButton("Lưu dịch vụ", FontIcon.of(FontAwesomeSolid.SAVE, 16, Color.WHITE));
        saveButton.setPreferredSize(new Dimension(130, 40));

        clearAllButton = Button.createWarningButton("Xóa tất cả",
                FontIcon.of(FontAwesomeSolid.TRASH_ALT, 16, Color.WHITE));
        clearAllButton.setPreferredSize(new Dimension(120, 40));
    }

    private void setupAvailableAmenitiesTable() {
        String[] columnNames = { "Mã dịch vụ", "Tên dịch vụ", "Giá" };
        availableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        availableAmenitiesTable = new JTable(availableModel);
        setupTableAppearance(availableAmenitiesTable, "Danh sách dịch vụ hiện có");

        availableAmenitiesTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        availableAmenitiesTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        availableAmenitiesTable.getColumnModel().getColumn(2).setPreferredWidth(120);
    }

    private void setupSelectedAmenitiesTable() {
        String[] columnNames = { "STT", "Tên dịch vụ", "Giá", "SL", "Thành tiền" };
        selectedModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        selectedAmenitiesTable = new JTable(selectedModel);
        setupTableAppearance(selectedAmenitiesTable, "Dữ liệu lưu");

        selectedAmenitiesTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        selectedAmenitiesTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        selectedAmenitiesTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        selectedAmenitiesTable.getColumnModel().getColumn(3).setPreferredWidth(50);
        selectedAmenitiesTable.getColumnModel().getColumn(4).setPreferredWidth(120);
    }

    private void setupTableAppearance(JTable table, String title) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(40);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(BORDER_COLOR);
        table.setShowGrid(true);
        table.setSelectionBackground(SELECTED_COLOR);
        table.setSelectionForeground(Color.BLACK);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(HEADER_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 45));
        header.setDefaultRenderer(new HeaderRenderer());

        table.setDefaultRenderer(Object.class, new AlternatingRowRenderer());
        table.setDefaultRenderer(Double.class, new CurrencyRenderer());
        table.setDefaultRenderer(Integer.class, new CenterRenderer());
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.setBorder(new EmptyBorder(0, 0, 15, 0));
        topPanel.add(instructionLabel, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        centerPanel.setBackground(BACKGROUND_COLOR);

        JPanel leftPanel = createTablePanel(availableAmenitiesTable,
                "Danh sách dịch vụ hiện có",
                "Click để thêm vào đơn hàng");

        JPanel rightPanel = createTablePanel(selectedAmenitiesTable,
                "Dữ liệu lưu",
                "Click chuột phải để xóa/sửa");

        centerPanel.add(leftPanel);
        centerPanel.add(rightPanel);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(BACKGROUND_COLOR);
        bottomPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setBackground(BACKGROUND_COLOR);
        totalPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR, 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        totalPanel.add(totalLabel);

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

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setBorder(new EmptyBorder(5, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        panel.add(subtitleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void setupEventHandlers() {
        availableAmenitiesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    addAmenityFromAvailable();
                }
            }
        });

        selectedAmenitiesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    showContextMenu(e);
                }
            }
        });

        clearAllButton.addActionListener(e -> clearAllAmenities());
    }

    private void addAmenityFromAvailable() {
        int selectedRow = availableAmenitiesTable.getSelectedRow();
        if (selectedRow == -1) return;

        AmenityDTO amenityDTO = new AmenityDTO();
        amenityDTO.setId((Long) availableModel.getValueAt(selectedRow, 0));
        amenityDTO.setName((String) availableModel.getValueAt(selectedRow, 1));
        BigDecimal priceObj = (BigDecimal) availableModel.getValueAt(selectedRow, 2);
        double price = priceObj.doubleValue();
        amenityDTO.setPrice(price);
        Long id = amenityDTO.getId();

        boolean exists = false;
        for (AmenityDTO data : selectedAmenities.values()) {
            if (data.getId().equals(id)) {
                data.setQuantity(data.getQuantity() + 1);
                exists = true;
                showNotification("Đã tăng số lượng: " + data.getName(), ACCENT_COLOR);
                break;
            }
        }

        if (!exists) {
            amenityDTO.setQuantity(1);
            selectedAmenities.put(id.toString(), amenityDTO);
            showNotification("Đã thêm: " + amenityDTO.getName(), ACCENT_COLOR);
        }

        totalAmount += amenityDTO.getPrice();
        updateTotalLabel();
        refreshSelectedTable();
    }

    private void showContextMenu(MouseEvent e) {
        int row = selectedAmenitiesTable.rowAtPoint(e.getPoint());
        if (row == -1) return;

        selectedAmenitiesTable.setRowSelectionInterval(row, row);

        JPopupMenu popup = new JPopupMenu();
        popup.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        var amenityDTO = (AmenityDTO) selectedModel.getValueAt(row, 1);
        if (amenityDTO == null) return;

        String id = amenityDTO.getId().toString();
        AmenityDTO data = selectedAmenities.get(id);
        if (data == null) return;

        JMenuItem editItem = new JMenuItem("Sửa số lượng (" + data.getQuantity() + ")");
        editItem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        editItem.addActionListener(e2 -> editQuantity(row, id, data));

        JMenuItem deleteItem = new JMenuItem("Xóa \"" + data.getName() + "\"");
        deleteItem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        deleteItem.setForeground(new Color(220, 53, 69));
        deleteItem.addActionListener(e2 -> deleteAmenity(row, id, data));

        popup.add(editItem);
        popup.addSeparator();
        popup.add(deleteItem);

        popup.show(selectedAmenitiesTable, e.getX(), e.getY());
    }

    private void editQuantity(int row, String id, AmenityDTO data) {
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

                double oldTotal = data.getPrice() * data.getQuantity();
                data.setQuantity(newQuantity);
                double newTotal = data.getPrice() * newQuantity;

                totalAmount = totalAmount - oldTotal + newTotal;
                updateTotalLabel();
                refreshSelectedTable();

                showNotification("Đã cập nhật số lượng: " + data.getName() + " x" + newQuantity, ACCENT_COLOR);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteAmenity(int row, String id, AmenityDTO data) {
        int opt = CustomDialog.showConfirm(this,
                "Bạn có chắc muốn xóa \"" + data.getName() + "\"?",
                "Xác nhận xóa",
                CustomDialog.MessageType.WARNING,
                400, 200);

        if (opt == JOptionPane.YES_OPTION) {
            selectedModel.removeRow(row);
            selectedAmenities.remove(id);
            totalAmount -= data.getPrice() * data.getQuantity();
            updateTotalLabel();

            showNotification("Đã xóa: " + data.getName(), WARNING_COLOR);
        }
    }

    private void clearAllAmenities() {
        if (selectedAmenities.isEmpty()) return;

        int confirm = CustomDialog.showConfirm(this,
                "Bạn có chắc muốn xóa tất cả dịch vụ?",
                "Xác nhận xóa tất cả",
                CustomDialog.MessageType.WARNING,
                400, 200);

        if (confirm == JOptionPane.YES_OPTION) {
            selectedModel.setRowCount(0);
            selectedAmenities.clear();
            totalAmount = 0.0;
            updateTotalLabel();

            showNotification("Đã xóa tất cả dịch vụ", WARNING_COLOR);
        }
    }

    private void updateTotalLabel() {
        totalLabel.setText("Tổng tiền: " + currencyFormat.format(totalAmount));
    }

    private void refreshSelectedTable() {
        selectedModel.setRowCount(0);
        int idx = 1;
        for (AmenityDTO data : selectedAmenities.values()) {
            double total = data.getPrice() * data.getQuantity();
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

        Container parent = instructionLabel.getParent();
        if (parent != null) {
            parent.remove(instructionLabel);
            parent.add(notification, BorderLayout.CENTER);
            parent.revalidate();
            parent.repaint();
        }

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
    public Button getSaveButton() {
        return saveButton;
    }

    public java.util.List<AmenityDTO> getSelectedTableData() {
        java.util.List<AmenityDTO> data = new java.util.ArrayList<>();
        for (int i = 0; i < selectedModel.getRowCount(); i++) {
            Integer quantity = (Integer) selectedModel.getValueAt(i, 3);
            AmenityDTO dto = (AmenityDTO) selectedModel.getValueAt(i, 1);
            dto.setQuantity(quantity);
            data.add(dto);
        }
        return data;
    }

    public void loadData(java.util.List<Amenity> availableData, java.util.List<AmenityDTO> selectedData) {
        availableModel.setRowCount(0);
        selectedModel.setRowCount(0);
        selectedAmenities.clear();
        totalAmount = 0.0;

        if (availableData != null) {
            for (Amenity amenity : availableData) {
                availableModel.addRow(new Object[] {
                        amenity.getAmenityId(),
                        amenity.getNameAmenity(),
                        amenity.getPrice(),
                });
            }
        }

        if (selectedData != null) {
            int index = 1;
            for (AmenityDTO dto : selectedData) {
                double total = dto.getPrice() * dto.getQuantity();
                selectedModel.addRow(new Object[] {
                        index++,
                        dto,
                        dto.getPrice(),
                        dto.getQuantity(),
                        total
                });
                selectedAmenities.put(dto.getId().toString(), dto);
                totalAmount += total;
            }
        }

        updateTotalLabel();
        selectedAmenitiesTable.revalidate();
        selectedAmenitiesTable.repaint();
        availableAmenitiesTable.revalidate();
        availableAmenitiesTable.repaint();
    }
}

